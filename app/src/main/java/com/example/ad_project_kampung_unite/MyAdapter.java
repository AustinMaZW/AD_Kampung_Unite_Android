package com.example.ad_project_kampung_unite;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.entities.GroceryList;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyHolder> {

    Context c;
    List<GroceryList> lists;

    public MyAdapter(Context c, List<GroceryList> lists) {
        this.c = c;
        this.lists = lists;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_grocery_lists_items,null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myholder, int i) {
        GroceryList groceryList = lists.get(i);

        myholder.mGroceryListName.setText(groceryList.getName());
        if(lists.get(i).getGroupPlanGL() != null) {
            myholder.mPickupDetail.setText(lists.get(i).getGroupPlanGL().getPickupDate().format(DateTimeFormatter.ISO_DATE));
        }

        System.out.println(groceryList.getGroceryItems());

        myholder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickLister(View v, int position) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();

                // send grocery list object
                GroceryList target = lists.get(position);

                // send grocery list to grocery list fragment
                Bundle result = new Bundle();
                result.putSerializable("bundleKey", target);
                activity.getSupportFragmentManager()
                        .setFragmentResult("requestKey", result);

                // go to grocery list view fragment
                ViewGroceryListFragment viewGroceryListFragment = new ViewGroceryListFragment();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,viewGroceryListFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void setGroceryList(List<GroceryList> groceryLists){
        this.lists = (ArrayList<GroceryList>) groceryLists;
        notifyDataSetChanged();
    }

    public void add(GroceryList groceryList) {
        this.lists.add(groceryList);
        notifyDataSetChanged();
    }
}
