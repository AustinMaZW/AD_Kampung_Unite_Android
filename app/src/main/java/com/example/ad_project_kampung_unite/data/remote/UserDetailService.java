package com.example.ad_project_kampung_unite.data.remote;

import com.example.ad_project_kampung_unite.entities.GroceryList;
import com.example.ad_project_kampung_unite.entities.UserDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserDetailService {
    @GET("user/find/{id}")
    Call<UserDetail> findUserById(@Path("id") int id);


    @POST("login")
    Call<UserDetail> login(@Body UserDetail userDetail);

    @POST("create")
    Call<UserDetail> create(@Body UserDetail userDetail);

}


