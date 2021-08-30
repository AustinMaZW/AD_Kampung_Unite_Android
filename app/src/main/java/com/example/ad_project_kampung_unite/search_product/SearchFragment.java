package com.example.ad_project_kampung_unite.search_product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.data.remote.ProductService;
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
import com.example.ad_project_kampung_unite.entities.GroceryList;
import com.example.ad_project_kampung_unite.entities.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    ProductService productService;
    GroceryList groceryList;

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

        //get grocery list
        Bundle bundle = getArguments();
        if(bundle!=null){
            groceryList = (GroceryList) bundle.getSerializable("editToSearchKey");
        }

        SearchView searchView = (SearchView) layoutRoot.findViewById(R.id.searchView);

        RecyclerView recyclerView = (RecyclerView) layoutRoot.findViewById(R.id.searchRecyclerView);

        // get products from database
        productService = RetrofitClient.createService(ProductService.class);
        Call<List<Product>> call = productService.getProductList();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> products = response.body();

                SearchProductListAdapter adapter = new SearchProductListAdapter(products, groceryList);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));
                recyclerView.setAdapter(adapter);

                searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        adapter.getFilter().filter(newText);

                        return false;
                    }
                });

            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {

            }
        });

        return layoutRoot;
    }
}
