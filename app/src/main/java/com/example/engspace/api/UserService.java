package com.example.engspace.api;

import com.example.engspace.model.ChangePasswordRequest;
import com.example.engspace.model.ChangePasswordResponse;
import com.example.engspace.model.DeleteUserResponse;
import com.example.engspace.model.LoginRequest;
import com.example.engspace.model.LoginResponse;
import com.example.engspace.model.RefreshRequest;
import com.example.engspace.model.RefreshResponse;
import com.example.engspace.model.RegisterRequest;
import com.example.engspace.model.RegisterResponse;
import com.example.engspace.model.ResetPasswordRequest;
import com.example.engspace.model.ResetPasswordResponse;
import com.example.engspace.model.UserRequest;
import com.example.engspace.model.UserResponse;
import com.example.engspace.model.UserStatus;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {
    @POST("users/login/")
    Call<LoginResponse> userLogin(@Body LoginRequest loginRequest);

    @POST("users/login/refresh/")
    Call<RefreshResponse> userRefresh(@Body RefreshRequest refreshRequest);

    @POST("users/register/")
    Call<RegisterResponse> userRegister(@Body RegisterRequest registerRequest);

    @GET("users/profile/")
    Call<UserResponse> getUserProfile();

    @PUT("users/profile/")
    Call<UserResponse> updateUserProfile(@Body UserRequest userRequest);

    @PUT("users/profile/change_password/{id}/")
    Call<ChangePasswordResponse> changePassword(@Body ChangePasswordRequest changePasswordRequest, @Path("id") int id);

    @GET("users/status/")
    Call<UserStatus> getUserStatus();

    @GET("users/reset-password/{email}/")
    Call<ResetPasswordResponse> resetPassword(@Path("email") String email);

    @POST("users/reset-password/{email}/")
    Call<ResetPasswordResponse> resetPasswordPost(@Body ResetPasswordRequest resetPasswordRequest, @Path("email") String email);

    @DELETE("users/profile/{id}/")
    Call<DeleteUserResponse> deleteUser(@Path("id") int id);
}
