package com.example.ad_project_kampung_unite.data.remote;

import com.example.ad_project_kampung_unite.entities.GroupPlan;
import com.example.ad_project_kampung_unite.entities.Product;
import com.example.ad_project_kampung_unite.entities.enums.GroupPlanStatus;
import com.example.ad_project_kampung_unite.ml.Recommendation;

import java.time.LocalDate;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GroupPlanService {
    @GET("groupplan/quit/{groceryListId}")
    Call<Boolean> quitGroupPlanByGroceryListId(@Path("groceryListId") int id);

    @GET("groupplan/{userDetailId}")
    Call<List<GroupPlan>> findGroupPlansByUserDetailId(@Path("userDetailId") int id);


    @GET("/native/{id}")
    Call<List<Integer>> getRecommendIds(@Path("id") int id);

    @GET("/native/{id}")
    Call<Recommendation> getRecommendId(@Path("id") int id);


    @POST("groupplan/listplans")
    Call<List<GroupPlan>> getPlans(@Body List<Integer> planIds);

    @POST("groupplan/retrieveproducts")
    Call<List<Product>> getProductsByPlanId(@Body int planId);

    @GET("hitchrequest/savereq")
    Call<Integer> saveRequest(@Query("planId") int planId, @Query("hitcherDetailId") int hitcherDetailId, @Query("pickUpTime") String pickUpTime);

    @GET("HitcherDetail/removedetail")
    Call<Integer> removeHitcherDetail(@Query("hdid") int hdid);

    @GET("groupplan/update/status/{id}/{status}")
    Call<Void> updateGroupPlanStatus(@Path("id") int id, @Path("status") GroupPlanStatus status);

    @GET("groupplan/plan/{id}")
    Call<GroupPlan> getGroupPlanById(@Path("id") int planId);



    @GET("groupplan/save")
    Call<GroupPlan> createGroupPlan(@Query("planName") String planName,
                                    @Query("storeName") String storeName,
                                    @Query("shoppingDate") String shoppingDate,
                                    @Query("pickUpAddress") String pickupAddress,
                                    @Query("pickUpDate") String pickupDate);
}
