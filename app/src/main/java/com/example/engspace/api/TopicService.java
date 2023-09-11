package com.example.engspace.api;

import com.example.engspace.model.SetsResponse;
import com.example.engspace.model.TopicResponse;
import com.example.engspace.model.TopicsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TopicService {
    @GET("topics/")
    Call<TopicsResponse> getTopics();

    @GET("topics/{id}/")
    Call<TopicResponse> getTopic(@Path("id") int id);
}
