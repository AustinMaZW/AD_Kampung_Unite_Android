package com.example.ad_project_kampung_unite;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.adaptors.GroceryListItemAdaptor;
import com.example.ad_project_kampung_unite.adaptors.HitchRequestAdaptor;
import com.example.ad_project_kampung_unite.entities.GroceryItem;
import com.example.ad_project_kampung_unite.entities.GroceryList;
import com.example.ad_project_kampung_unite.entities.GroupPlan;
import com.example.ad_project_kampung_unite.entities.Product;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class ViewGroceryListFragment extends Fragment {

    private List<GroupPlan> groupPlanList;
    private List<GroceryItem> groceryItemList;

    public ViewGroceryListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layoutRoot = inflater.inflate(R.layout.fragment_view_grocery_list, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Grocery List Name");     //need grocery list name passed from previous frag

        //add condition if request is pending then below are 'invisible
        layoutRoot.findViewById(R.id.status_approved).setVisibility(View.GONE);


        createDummyData(); //  fake data to be replaced with http request list with database

        //recycler view for hitch requests
        RecyclerView rvHitchRequests = layoutRoot.findViewById(R.id.rv_hitch_rq);
        HitchRequestAdaptor hitchRequestAdaptor = new HitchRequestAdaptor(groupPlanList);
        rvHitchRequests.setAdapter(hitchRequestAdaptor);
        rvHitchRequests.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));

        //recycler view for grocery items
        RecyclerView rvGroceryItems = layoutRoot.findViewById(R.id.rv_grocery_list);
        GroceryListItemAdaptor groceryListItemAdaptor = new GroceryListItemAdaptor(groceryItemList);
        rvGroceryItems.setAdapter(groceryListItemAdaptor);
        rvGroceryItems.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));

        return layoutRoot;
    }

    public void createDummyData(){

        groupPlanList = new ArrayList<>();

        LocalDate d1 = LocalDate.of(2021, 8, 1);
        LocalDate d2 = LocalDate.of(2021, 8, 15);

        groupPlanList.add(new GroupPlan(1, "Giant",d1,"123 Canada St",d2));
        groupPlanList.add(new GroupPlan(2, "7-Eleven",d1,"321 MapleSyrup St",d2));
        groupPlanList.add(new GroupPlan(3, "FairPrice",d1,"Lalala St",d2));

        groceryItemList = new ArrayList<>();

        Product p1 = new Product("1627923825-1174","Old Town 3IN1 Sugar Cane White Coffee", "15 x 36 g","Beverages","https://ssecomm.s3-ap-southeast-1.amazonaws.com/products/md/WPVi3KzqNyNsW7uGdcTMQqZmyiC2WH.0.jpg");
        Product p2 = new Product("1627923846-1184","Happy Family 2IN1 Kopi O With Sugar Mixture Bag","8 x 20 g","Beverages","https://ssecomm.s3-ap-southeast-1.amazonaws.com/products/md/yjwEFEKNPFnYIXrfN4s6wnfTAsdz5t.0.jpg");

        groceryItemList.add(new GroceryItem(1, 5, 8.4, p1, null));
        groceryItemList.add(new GroceryItem(1, 3, 5.4, p2, null));

    }
}