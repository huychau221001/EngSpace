package com.example.engspace.api;

import com.example.engspace.model.DeleteUserResponse;
import com.example.engspace.model.SetRequest;
import com.example.engspace.model.SetResponse;
import com.example.engspace.model.SetsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SetService {
    @GET("sets/")
    Call<SetsResponse> getSets();

    @GET("sets/?page=last")
    Call<SetsResponse> getLatestSets();

    @GET("sets/user/{id}/")
    Call<SetsResponse> getSetsByUser(@Path("id") int id);

    @GET("sets/")
    Call<SetsResponse> searchSets(@Query("search") String query, @Query("page") int page);

    @POST("sets/")
    Call<SetResponse> createSet(@Body SetRequest setRequest);

    @GET("sets/{id}/")
    Call<SetResponse> getSet(@Path("id") int id);

    @PUT("sets/{id}/")
    Call<SetResponse> updateSet(@Path("id") int id, @Body SetRequest setRequest);

    @DELETE("sets/{id}/")
    Call<SetResponse> deleteSet(@Path("id") int id);
}
