package com.example.engspace.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit getRetrofit(Context ctx) {
//        Dispatcher dispatcher = new Dispatcher();
//        dispatcher.setMaxRequests(1);
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .dispatcher(dispatcher)
                .addInterceptor(new AuthorizationInterceptor(ctx))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                //.baseUrl("https://octopus-app-hm3v6.ondigitalocean.app/api/")
                .baseUrl("https://engspace-app.herokuapp.com/api/")
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    public static UserService getUserService(Context ctx) {
        UserService userService = getRetrofit(ctx).create(UserService.class);
        return userService;
    }

    public static SetService getSetService(Context ctx) {
        SetService setService = getRetrofit(ctx).create(SetService.class);
        return setService;
    }

    public static SetDetailService getSetDetailService(Context ctx) {
        SetDetailService setDetailService = getRetrofit(ctx).create(SetDetailService.class);
        return setDetailService;
    }

    public static FolderService getFolderService(Context ctx) {
        FolderService folderService = getRetrofit(ctx).create(FolderService.class);
        return folderService;
    }

    public static TopicService getTopicService(Context ctx) {
        TopicService topicService = getRetrofit(ctx).create(TopicService.class);
        return topicService;
    }
}
