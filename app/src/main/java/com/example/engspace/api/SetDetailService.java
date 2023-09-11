package com.example.engspace.api;

import com.example.engspace.model.SetDetailRequest;
import com.example.engspace.model.SetDetailResponse;
import com.example.engspace.model.SetResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SetDetailService {
    @POST("set-details/")
    Call<SetDetailResponse> addSetDetail(@Body SetDetailRequest setDetailRequest);

    @DELETE("set-details/{id}/")
    Call<SetDetailResponse> deleteSetDetail(@Path("id") int id);
}
