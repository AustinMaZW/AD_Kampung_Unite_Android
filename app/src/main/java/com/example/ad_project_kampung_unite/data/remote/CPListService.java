package com.example.ad_project_kampung_unite.data.remote;

import com.example.ad_project_kampung_unite.entities.CombinedPurchaseList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CPListService {
    @GET("cplist")
    Call<List<CombinedPurchaseList>> getAllCPLists();

    @GET("cplist/groupplan/{groupPlanId}")
    Call<List<CombinedPurchaseList>> getCPListByGroupPlanId(@Path("groupPlanId") int id);

    @POST("cplist/save/all")
    Call<Boolean> saveAll(@Body List<CombinedPurchaseList> list);

    @POST("cplist/update")
    Call<List<CombinedPurchaseList>> update(@Body List<CombinedPurchaseList> combinedPurchaseList);
}
