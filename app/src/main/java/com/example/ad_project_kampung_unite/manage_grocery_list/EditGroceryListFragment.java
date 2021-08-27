package com.example.ad_project_kampung_unite.manage_grocery_list;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.data.remote.GroceryItemService;
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
import com.example.ad_project_kampung_unite.entities.GroceryItem;
import com.example.ad_project_kampung_unite.entities.GroceryList;
import com.example.ad_project_kampung_unite.ml.HitcherDetailFragment;
import com.example.ad_project_kampung_unite.search_product.SearchFragment;
import com.google.android.material.chip.Chip;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditGroceryListFragment extends Fragment {

    private Chip addButton;
    private RecyclerView groceryItemRecyclerView;
    private GroceryItemService groceryItemService;
    private GroceryList groceryList;
    private int groceryListId;
    private String groceryListName;
    private List<GroceryItem> groceryItems;
    private View layoutRoot;
    private Button findMatch;
    private Button startGroup;
    private Context context_;

    public EditGroceryListFragment() {
        // Empty constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(groceryList.getName());
        getGroceryItems();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        layoutRoot = inflater.inflate(R.layout.fragment_grocery_list, container, false);
        context_ = this.getContext();

        getParentFragmentManager().setFragmentResultListener("requestKey1", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                groceryList = (GroceryList) bundle.getSerializable("bundleKey1");
                groceryListId = groceryList.getId();

                if(groceryList.getHitcherDetail() != null || groceryList.getGroupPlanGL() != null) {
                    findMatch.setVisibility(View.GONE);
                    startGroup.setVisibility(View.GONE);
                }

                // Set title
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(groceryList.getName());
                getGroceryItems();
            }
        });



        findMatch = layoutRoot.findViewById(R.id.hitcherButton);

        findMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Jump to HitcherDetail Activity
                FragmentManager fm = getParentFragmentManager();
                Log.e("Hitcher Detail","yes_5");
                HitcherDetailFragment hitcerDetail = new HitcherDetailFragment();
                Log.e("list Id",Integer.toString(groceryList.getId()));

                hitcerDetail.setgList(groceryList);

                FragmentTransaction trans = fm.beginTransaction();
                trans.replace(R.id.fragment_container,hitcerDetail);
                trans.addToBackStack(null);
                trans.commit();
            }
        });
        addButton = layoutRoot.findViewById(R.id.add);



        setUpBtns();

        return layoutRoot;
    }

    public void getGroceryItems() {
        groceryItemService = RetrofitClient.createService(GroceryItemService.class);
        Call<List<GroceryItem>> call = groceryItemService.getGroceryItemsByGroceryListId(groceryList.getId());
        call.enqueue(new Callback<List<GroceryItem>>() {
            @Override
            public void onResponse(Call<List<GroceryItem>> call, Response<List<GroceryItem>> response) {
                if (response.isSuccessful()) {
                    groceryItems = response.body();
                    if(groceryItems!=null) {
                        Log.i("GroceryItems", groceryItems.toString());
                        buildGroceryListRecyclerView();
                    }
                } else {
                    Log.e("getGroceryItemsByGroceryListId Error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<GroceryItem>> call, Throwable t) {
                call.cancel();
                Log.w("Failure", "Failure!");
                t.printStackTrace();
            }
        });
    }

    public void buildGroceryListRecyclerView() {
        groceryItemRecyclerView = layoutRoot.findViewById(R.id.groceryItemRecyclerView);
        GroceryItemAdapter groceryItemAdapter = new GroceryItemAdapter(groceryItems);
        groceryItemRecyclerView.setAdapter(groceryItemAdapter);
        groceryItemRecyclerView.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(groceryItemRecyclerView.getContext(),DividerItemDecoration.VERTICAL);
        groceryItemRecyclerView.addItemDecoration(dividerItemDecoration);

    }

    public void setUpBtns() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send grocery list to grocery list fragment
                Bundle result = new Bundle();
                result.putSerializable("editToSearchKey", groceryList);

                // switch to search fragment
                FragmentManager fragmentManager = getParentFragmentManager();
                SearchFragment searchFragment = new SearchFragment();
                searchFragment.setArguments(result);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container,searchFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        startGroup = layoutRoot.findViewById(R.id.buyerButton);
        startGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send grocery list to grocery list fragment
                Bundle result = new Bundle();
                result.putSerializable("editToBuyerDetailKey", groceryList);

                // switch to buyer details fragment
                FragmentManager fragmentManager = getParentFragmentManager();
                BuyerFragment buyerFragment = new BuyerFragment();
                buyerFragment.setArguments(result);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container,buyerFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}

