package com.example.ad_project_kampung_unite.data.remote;

import com.example.ad_project_kampung_unite.entities.GroceryItem;

import com.example.ad_project_kampung_unite.Post;
import com.example.ad_project_kampung_unite.entities.GroceryList;
import com.example.ad_project_kampung_unite.model.responseModel.StatusResponseEntity;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GroceryListService {

    // The return value wraps the response in a Call object with the type of the expected result
    @GET("/grocerylists/{userDetailId}")
    Call<List<GroceryList>> findGroceryListsByUserDetailId(@Path("userDetailId") int userDetailId);

    @POST("grocerylists/new")
    Call<Integer> createGroceryListByUserDetailId(@Query("groceryListName") String groceryListName, @Query("userDetailId") int userDetailId);

    @GET("grocerylists")
    Call<List<GroceryList>> getGroceryLists();

    @GET("groceries/{groceryListId}")
    Call<List<GroceryItem>> getGroceryItemByGroceryListId(@Path("groceryListId") int id);

    @GET("grocerylists/glistid/{groceryListId}")
    Call<GroceryList> findGroceryListByGroceryListId(@Path("groceryListId") int groceryListId);

    @GET("grocerylists/update/buyerrole")
    Call<GroceryList> updateBuyerRoleById(@Query("groceryListId") int groceryListId, @Query("groupPlanId") int groupPlanId);

    @GET("grocerylists/buyer/{groupPlanId}")
    Call <GroceryList> findGroceryListByGroupPlanId(@Path("groupPlanId") int groupPlanId);

}
