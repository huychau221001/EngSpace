package com.example.engspace.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.engspace.model.RefreshResponse;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;

public class AuthorizationInterceptor implements Interceptor {
    private static Retrofit retrofit = null;
    private static String accessToken;
    private static String refreshToken;
    private static TokenManager tokenManager;
    private static Context mContext;

    public AuthorizationInterceptor(Context context) {
        this.mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        Request original = chain.request();
        Request request;
        if (original.url().toString().contains("users/register/") || original.url().toString().contains("users/login/") || original.url().toString().contains("users/reset-password/")) {
            request = original.newBuilder()
                    .build();
        } else {
            request = original.newBuilder()
                    .addHeader("Authorization", "Bearer " + sharedPreferences.getString("access", ""))
                    .build();
        }

        Request modifiedRequest = null;

        tokenManager = new TokenManager() {
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

            @Override
            public String getToken() {

                accessToken = sharedPreferences.getString("access", "");
                return accessToken;
            }

            @Override
            public boolean hasToken() {
                accessToken = sharedPreferences.getString("access", "");
                if (accessToken != null && !accessToken.equals("")) {
                    return true;
                }
                return false;
            }

            @Override
            public void clearToken() {
                sharedPreferences.edit().putString("access", "").apply();
            }

            @Override
            public String refreshToken() {
                final String accessToken = null;
                final String refresh_token = sharedPreferences.getString("refresh", "");

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("refresh", refresh_token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody reqbody = RequestBody.create(jsonObject.toString(),
                        MediaType.parse("application/json; charset=utf-8"));

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://engspace-app.herokuapp.com/api/users/login/refresh/")
                        .post(reqbody)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    if ((response.code()) == 200) {
                        // Get response
                        String jsonData = response.body().string();

                        Gson gson = new Gson();
                        RefreshResponse refreshTokenResponseModel = gson.fromJson(jsonData, RefreshResponse.class);
                        sharedPreferences.edit().putString("access", refreshTokenResponseModel.getAccess()).apply();
                        sharedPreferences.edit().putString("refresh", refreshTokenResponseModel.getRefresh()).apply();
                        return refreshTokenResponseModel.getAccess();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return accessToken;
            }
        };

        accessToken = sharedPreferences.getString("access", "");
        refreshToken = sharedPreferences.getString("refresh", "");

        Response response = chain.proceed(request);
        boolean unauthorized = false;
        if (response.code() == 401 || response.code() == 422) {
            unauthorized = true;
            response.close();
        }

        if (unauthorized) {
            tokenManager.clearToken();
            tokenManager.refreshToken();
            accessToken = sharedPreferences.getString("access", "");
            if (accessToken != null) {
                modifiedRequest = original.newBuilder()
                        .addHeader("Authorization", "Bearer " + tokenManager.getToken())
                        .build();
                return chain.proceed(modifiedRequest);
            }
        }
        return response;
    }
}