package com.example.ad_project_kampung_unite.data.remote;

import com.example.ad_project_kampung_unite.entities.HitcherDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface HitcherDetailService {
    @POST("/HitcherDetail/saveHicherDetail")
    Call<Integer> saveHitcherDetail(@Body HitcherDetail hitcherDetail);
}
