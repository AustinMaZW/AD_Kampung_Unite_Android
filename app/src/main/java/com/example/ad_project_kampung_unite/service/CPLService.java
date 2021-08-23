package com.example.ad_project_kampung_unite.service;

import com.example.ad_project_kampung_unite.entities.CombinedPurchaseList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CPLService {

    @POST("update")
    Call<List<CombinedPurchaseList>> update(@Body List<CombinedPurchaseList> combinedPurchaseList);
}
