package com.example.ad_project_kampung_unite;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class GroupDetailsFragment extends Fragment {

    private ArrayList<GroceryList> mGroceryLists;

    private RecyclerView mRecyclerView;
    private GroupDetailsAdapter myAdapter;

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

        //input dummy data
        createMyList();
        //recycler view adapter instantiated here
        buildRecyclerView(layoutRoot);
        //attaching touch helper to recycler view for swipe action itoms

        expanderRecyclerView = layoutRoot.findViewById(R.id.groupDetails_expandableRecyclerView);
        initiateExpander();

        return layoutRoot;
    }
    public void createMyList(){

        mGroceryLists = new ArrayList<>();

        mGroceryLists.add(new GroceryList("August Group Buy", "","","3","pending"));
        mGroceryLists.add(new GroceryList("July Group Buy","19 Jul 2021", "0900", "10","completed"));
        mGroceryLists.add(new GroceryList("July Group Buy 1", "", "", "10", "pending"));
        mGroceryLists.add(new GroceryList("Week 3 groceries", "12 Jul 2021", "0900", "15", "accepted"));
        mGroceryLists.add(new GroceryList("June Group Buy", "19 June 2021", "0900", "15", "completed"));
    }

    //build recycler view
    public void buildRecyclerView(View layoutRoot){

        mRecyclerView = layoutRoot.findViewById(R.id.recyclerviewGroupDetails);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));

        myAdapter = new GroupDetailsAdapter(layoutRoot.getContext(), mGroceryLists);
        mRecyclerView.setAdapter(myAdapter);
    }

    private void initiateExpander() {

        ArrayList<String> parentList = new ArrayList<>();
        ArrayList<ArrayList> childListHolder = new ArrayList<>();

        parentList.add("List A");
        parentList.add("List B");
        parentList.add("List C");

        ArrayList<String> childList = new ArrayList<>();
        childList.add("Apple");
        childList.add("Mango");
        childList.add("Banana");

        childListHolder.add(childList);

        childList = new ArrayList<>();
        childList.add("Red bull");
        childList.add("Maa");
        childList.add("Horlicks");

        childListHolder.add(childList);

        childList = new ArrayList<>();
        childList.add("Knife");
        childList.add("Vessels");
        childList.add("Spoons");

        childListHolder.add(childList);

        ExpandableRecyclerViewAdapter expandableCategoryRecyclerViewAdapter =
                new ExpandableRecyclerViewAdapter(layoutRoot.getContext(), parentList,
                        childListHolder);

        expanderRecyclerView.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));
        expanderRecyclerView.setAdapter(expandableCategoryRecyclerViewAdapter);
    }
}

