package com.example.ad_project_kampung_unite.service;

import com.example.ad_project_kampung_unite.model.UserDetail;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserDetailService {

    @POST("login")
    Call<UserDetail> login(@Body UserDetail userDetail);
}
