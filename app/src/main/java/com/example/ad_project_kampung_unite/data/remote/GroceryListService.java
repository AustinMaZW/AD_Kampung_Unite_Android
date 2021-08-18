package com.example.ad_project_kampung_unite.data.remote;


import com.example.ad_project_kampung_unite.Post;
import com.example.ad_project_kampung_unite.entities.GroceryList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GroceryListService {

    // The return value wraps the response in a Call object with the type of the expected result
    @GET("grocerylists")
    Call<List<GroceryList>> getGroceryLists();
}