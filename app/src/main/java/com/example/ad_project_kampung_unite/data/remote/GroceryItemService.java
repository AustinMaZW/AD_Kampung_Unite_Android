package com.example.ad_project_kampung_unite.data.remote;

import com.example.ad_project_kampung_unite.entities.GroceryItem;
import com.example.ad_project_kampung_unite.entities.HitchRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GroceryItemService {
    @GET("groceries/{groceryListId}")
    Call<List<GroceryItem>> getGroceryItemsByGroceryListId(@Path("groceryListId") int groceryListId);

    @GET("groceries/buyer/{groupId}")
    Call<List<GroceryItem>> getBuyerGroceryItemsByGroupId(@Path("groupId") int groupId);

    @GET("groceries/hitcher/{groupId}")
    Call<List<GroceryItem>> getHitcherGroceryItemsByGroupId(@Path("groupId") int groupId);

    @GET("groceries/group/{groupId}")
    Call<List<GroceryItem>> getAcceptedGroceryItemsByGroupPlanId(@Path("groupId") int groupId);

    @POST("groceries/save/all")
    Call<Boolean> saveAll(@Body List<GroceryItem> list);

    @GET("groceries/save")
    Call<Integer> addGroceryItemToGroceryList (@Query("productId") int productId, @Query("quantity") int quantity, @Query("groceryListId")  int groceryListId);



    @GET("groceries/groupplan/hitcheritems")
    Call<List<List<GroceryItem>>> findGroceryItemsByHitchRequests(@Query("array") List<Integer> hitchRequestIds);

    @GET("groceries/groupplan/hitcheritems/testing/{hitchRequestId}")
    Call<List<GroceryItem>> findGroceryItemsByHitchRequest(@Path("hitchRequestId") int hitchRequestId);
}
