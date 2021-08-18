package com.example.ad_project_kampung_unite.data.remote;

import com.example.ad_project_kampung_unite.entities.GroupPlan;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GroupPlanService {
    @GET("/native/{id}")
    Call<List<Integer>> getRecommendId(@Path("id") int id);

    @POST("groupplan/listplans")
    Call<List<GroupPlan>> getPlans(@Body List<Integer> planIds);
}
