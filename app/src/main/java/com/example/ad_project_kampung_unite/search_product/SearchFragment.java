package com.example.ad_project_kampung_unite.search_product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.entities.Product;
import com.example.ad_project_kampung_unite.search_product.SearchProductListAdapter;

import java.util.Arrays;

public class SearchFragment extends Fragment {

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View layoutRoot = inflater.inflate(R.layout.fragment_search, container, false);

        SearchView searchView = (SearchView) layoutRoot.findViewById(R.id.searchView);

        RecyclerView recyclerView = (RecyclerView) layoutRoot.findViewById(R.id.searchRecyclerView);

        // mock data
        Product[] products = new Product[] {
                new Product("1627923825-1174","Old Town 3IN1 Sugar Cane White Coffee", "15 x 36 g","Beverages","https://ssecomm.s3-ap-southeast-1.amazonaws.com/products/md/WPVi3KzqNyNsW7uGdcTMQqZmyiC2WH.0.jpg"),
                new Product("1627923846-1184","Happy Family 2IN1 Kopi O With Sugar Mixture Bag","8 x 20 g","Beverages","https://ssecomm.s3-ap-southeast-1.amazonaws.com/products/md/yjwEFEKNPFnYIXrfN4s6wnfTAsdz5t.0.jpg"),
                new Product("1627923819-1171","Nescafe Original Ice Milk Coffee","240 ml","Beverages","https://ssecomm.s3-ap-southeast-1.amazonaws.com/products/md/QUZdsA8aBQFb4vP3iIf36UeLv9KjWK.0.jpg"),
                new Product("1627923829-1176","Old Town 3IN1 Hazelnut White Coffee","15 x 38 g","Beverages","https://ssecomm.s3-ap-southeast-1.amazonaws.com/products/md/S6k9DS5DfRko9CKoYg0fw1ERcJ8Kin.0.jpg"),
                new Product("1627923837-1180","Pokka Premium Milk Coffee","500 ml","Beverages","https://ssecomm.s3-ap-southeast-1.amazonaws.com/products/md/fEvosk2kZcZaSfik0TpXJGLBO00lcr.0.jpg")
        };


        SearchProductListAdapter adapter = new SearchProductListAdapter(products);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Arrays.stream(products).filter(x -> x.getProductName() == s).toArray();
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                List<Product> searchResult = Arrays.stream(products).filter(x -> x.getProductName() == query).collect(Collectors.toList());
//
//                if (list.contains(query)) {
//                    adapter.getFilter().filter(query);
//                } else {
//                    Toast.makeText(getContext(), "No Match found", Toast.LENGTH_LONG).show();
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                //    adapter.getFilter().filter(newText);
//                return false;
//            }
//        });
        return layoutRoot;
    }
}
