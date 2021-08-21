package com.example.ad_project_kampung_unite.data.remote;

import com.example.ad_project_kampung_unite.entities.HitcherDetail;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HitcherDetailService {
//    @POST("/HitcherDetail/saveHicherDetail")
//    Call<Integer> saveHitcherDetail(@Body HitcherDetail hitcherDetail);


    @GET("/HitcherDetail/savehd")
    Call<Integer> saveHitcherDetail(@Query("pickUpdate") String pickUpdate, @Query("pickUptime") String pickUptime, @Query("address") String address);



}
