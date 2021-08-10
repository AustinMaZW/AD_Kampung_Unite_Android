package com.example.ad_project_kampung_unite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Date;

public class MyGroceryListsActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_grocery_lists);

        //create model class
        //create myHolder class
        //create adapter class

        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        myAdapter = new MyAdapter(this, getMyList());
        mRecyclerView.setAdapter(myAdapter);
    }

    @NonNull
    private ArrayList<GroceryList> getMyList(){

        ArrayList<GroceryList> groceryLists = new ArrayList<>();

        GroceryList g = new GroceryList();
        g.setName("grocery list 1");
        g.setDate("10/8/2021");
        groceryLists.add(g);

        GroceryList g2 = new GroceryList();
        g2.setName("grocery list 2");
        g2.setDate("10/8/2021");
        groceryLists.add(g2);

        GroceryList g3 = new GroceryList();
        g3.setName("grocery list 3");
        g3.setDate("10/8/2021");
        groceryLists.add(g3);

        return groceryLists;
    }
}