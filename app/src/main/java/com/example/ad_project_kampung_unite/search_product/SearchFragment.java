package com.example.ad_project_kampung_unite.search_product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.data.remote.ProductService;
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
import com.example.ad_project_kampung_unite.entities.GroceryList;
import com.example.ad_project_kampung_unite.entities.Product;
import com.example.ad_project_kampung_unite.search_product.SearchProductListAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    ProductService productService;

    public SearchFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Find Item");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View layoutRoot = inflater.inflate(R.layout.fragment_search, container, false);

        SearchView searchView = (SearchView) layoutRoot.findViewById(R.id.searchView);

        RecyclerView recyclerView = (RecyclerView) layoutRoot.findViewById(R.id.searchRecyclerView);

        // get products from database
        List<Product> products = new ArrayList<>();
        productService = RetrofitClient.createService(ProductService.class);

        Call<List<Product>> call = productService.getProductList();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> result = response.body();
                if(result!=null) {
                    result.stream().forEach(x -> products.add(x));
                }

                SearchProductListAdapter adapter = new SearchProductListAdapter(products);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });

        return layoutRoot;
    }
}
