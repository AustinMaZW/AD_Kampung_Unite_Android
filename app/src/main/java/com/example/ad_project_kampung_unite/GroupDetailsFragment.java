package com.example.ad_project_kampung_unite;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ad_project_kampung_unite.adaptors.GroceryListItemAdaptor;
import com.example.ad_project_kampung_unite.data.remote.GroceryListService;
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
import com.example.ad_project_kampung_unite.entities.GroceryItem;
import com.example.ad_project_kampung_unite.entities.GroceryList;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GroupDetailsFragment extends Fragment {



    private List<GroceryItem> buyerGroceryItemList = new ArrayList<>();

      private RecyclerView rvBuyerGrocery;
      private GroceryListService groceryListService;
//    private GroupDetailsAdapter myAdapter;


    RecyclerView expanderRecyclerView;
    View layoutRoot;

    public GroupDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layoutRoot = inflater.inflate(R.layout.fragment_group_details, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Group Details");



//        //buyer can edit their own grocery list
        //pending to-do: pass buyer id to 'grocery list' fragment
//        FloatingActionButton editButton = layoutRoot.findViewById(R.id.groupDetails_buyerEditBtn);
//        editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentManager fragmentManager = getParentFragmentManager();
//                GroceryListFragment groceryListFragment = new GroceryListFragment();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.fragment_container,groceryListFragment)
//                        .addToBackStack(null)
//                        .commit();
//            }
//        });

        //recycler view for grocery items
        rvBuyerGrocery = layoutRoot.findViewById(R.id.recyclerviewGroupDetails);
        rvBuyerGrocery.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));

        groceryListService = RetrofitClient.createService(GroceryListService.class);
        getGroceryItemsFromServer();

//        //input dummy data
//        createMyList();
//        //recycler view adapter instantiated here
//        buildRecyclerView(layoutRoot);
//        //attaching touch helper to recycler view for swipe action itoms
//
//        expanderRecyclerView = layoutRoot.findViewById(R.id.groupDetails_expandableRecyclerView);
//        initiateExpander();

        return layoutRoot;
    }

    private void getGroceryItemsFromServer(){
        Call<List<GroceryItem>> call = groceryListService.getGroceryItemByGroceryListId(30); //hard coded grocerylistid here, replace later

        call.enqueue(new Callback<List<GroceryItem>>() {
            @Override
            public void onResponse(Call<List<GroceryItem>> call, Response<List<GroceryItem>> response) {

                if (response.isSuccessful()) {
                    buyerGroceryItemList = response.body();
                    Log.d("Success", String.valueOf(buyerGroceryItemList.get(0).getProduct().getProductName())); //for testing

                    GroceryListItemAdaptor groceryListItemAdaptor = new GroceryListItemAdaptor(buyerGroceryItemList);
                    rvBuyerGrocery.setAdapter(groceryListItemAdaptor);  //set the adaptor here
                } else {
                    Log.e("Error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<GroceryItem>> call, Throwable t) {
                // like no internet connection / the website doesn't exist
                call.cancel();
                Log.w("Failure", "Failure!");
                t.printStackTrace();
            }
        });
    }
//    public void createMyList(){
//
//        mGroceryLists = new ArrayList<>();
//
//        mGroceryLists.add(new GroceryList("August Group Buy", "","","3","pending"));
//        mGroceryLists.add(new GroceryList("July Group Buy","19 Jul 2021", "0900", "10","completed"));
//        mGroceryLists.add(new GroceryList("July Group Buy 1", "", "", "10", "pending"));
//        mGroceryLists.add(new GroceryList("Week 3 groceries", "12 Jul 2021", "0900", "15", "accepted"));
//        mGroceryLists.add(new GroceryList("June Group Buy", "19 June 2021", "0900", "15", "completed"));
//    }
//
//    //build recycler view
//    public void buildRecyclerView(View layoutRoot){
//
//        mRecyclerView = layoutRoot.findViewById(R.id.recyclerviewGroupDetails);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));
//
//        myAdapter = new GroupDetailsAdapter(layoutRoot.getContext(), mGroceryLists);
//        mRecyclerView.setAdapter(myAdapter);
//    }
//
//    private void initiateExpander() {
//
//        ArrayList<String> parentList = new ArrayList<>();
//        ArrayList<ArrayList> childListHolder = new ArrayList<>();
//
//        parentList.add("List A");
//        parentList.add("List B");
//        parentList.add("List C");
//
//        ArrayList<String> childList = new ArrayList<>();
//        childList.add("Apple");
//        childList.add("Mango");
//        childList.add("Banana");
//
//        childListHolder.add(childList);
//
//        childList = new ArrayList<>();
//        childList.add("Red bull");
//        childList.add("Maa");
//        childList.add("Horlicks");
//
//        childListHolder.add(childList);
//
//        childList = new ArrayList<>();
//        childList.add("Knife");
//        childList.add("Vessels");
//        childList.add("Spoons");
//
//        childListHolder.add(childList);
//
//        ExpandableRecyclerViewAdapter expandableCategoryRecyclerViewAdapter =
//                new ExpandableRecyclerViewAdapter(layoutRoot.getContext(), parentList,
//                        childListHolder);
//
//        expanderRecyclerView.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));
//        expanderRecyclerView.setAdapter(expandableCategoryRecyclerViewAdapter);
//    }
}
