package com.example.ad_project_kampung_unite.data.remote;

import com.example.ad_project_kampung_unite.entities.GroceryItem;
import com.example.ad_project_kampung_unite.entities.HitchRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GroceryItemService {
    @GET("groceries/{groceryListId}")
    Call<List<GroceryItem>> getGroceryItemsByGroceryListId(@Path("groceryListId") int groceryListId);
}
