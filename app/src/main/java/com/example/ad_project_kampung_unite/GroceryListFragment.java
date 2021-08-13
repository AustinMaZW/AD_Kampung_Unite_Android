package com.example.ad_project_kampung_unite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import com.google.android.material.button.MaterialButton;

public class GroceryListFragment extends Fragment {

    public GroceryListFragment() {
        // Empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                // We use a String here, but any type that can be put in a Bundle is supported
                String result = bundle.getString("bundleKey");
                // Do something with the result
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(result);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View layoutRoot = inflater.inflate(R.layout.fragment_grocery_list, container, false);
        SearchView searchView = layoutRoot.findViewById(R.id.searchView);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });

        MaterialButton buyerBtn;
        buyerBtn = layoutRoot.findViewById(R.id.buyerButton);
        buyerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditBuyingDetailsFragment editBuyingDetailsFragment = new EditBuyingDetailsFragment();

                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container,editBuyingDetailsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return layoutRoot;
    }
}

