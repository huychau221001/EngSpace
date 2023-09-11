package com.example.engspace.api;

import com.example.engspace.model.FolderRequest;
import com.example.engspace.model.FolderResponse;
import com.example.engspace.model.FolderSetRequest;
import com.example.engspace.model.FolderSetResponse;
import com.example.engspace.model.FoldersResponse;
import com.example.engspace.model.SetRequest;
import com.example.engspace.model.SetResponse;
import com.example.engspace.model.SetsResponse;
import com.example.engspace.model.TopicResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FolderService {
    @GET("folders/")
    Call<FoldersResponse> getFolders();

    @GET("folders/")
    Call<FoldersResponse> searchFolders(@Query("search") String query, @Query("page") int page);

    @POST("folders/")
    Call<FolderResponse> createFolders(@Body FolderRequest folderRequest);

    @GET("folders/{id}/")
    Call<FolderResponse> getFolder(@Path("id") int id);

    @PUT("folders/{id}/")
    Call<FolderResponse> updateFolder(@Path("id") int id, @Body FolderRequest folderRequest);

    @DELETE("folders/{id}/")
    Call<FolderResponse> deleteFolder(@Path("id") int id);

    @GET("folders/user/{id}/")
    Call<FoldersResponse> getFoldersByUser(@Path("id") int id);

    @POST("folder-sets/")
    Call<Void> addSet(@Body FolderSetRequest folderSetRequest);

    @HTTP(method = "DELETE", path = "folder-sets/", hasBody = true)
    Call<Void> deleteSet(@Body FolderSetRequest folderSetRequest);
}
