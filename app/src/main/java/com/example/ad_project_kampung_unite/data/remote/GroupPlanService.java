package com.example.ad_project_kampung_unite.data.remote;

import com.example.ad_project_kampung_unite.entities.GroceryList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GroupPlanService {
    @GET("groupplan/quit/{groceryListId}")
    Call<Boolean> quitGroupPlanByGroceryListId(@Path("groceryListId") int id);
}
