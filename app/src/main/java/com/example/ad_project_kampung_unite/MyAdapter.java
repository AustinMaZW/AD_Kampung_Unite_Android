package com.example.ad_project_kampung_unite;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.entities.GroceryList;
import com.example.ad_project_kampung_unite.search_product.SearchFragment;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//Bridge between holder and list,
//for each holder - inflate, populate with data, add onClickListener, direct to activity 'view list details'
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
        myholder.mGroceryListName.setText(lists.get(i).getName());
        if(lists.get(i).getGroupPlanGL() != null) {
            myholder.mPickupDetail.setText(lists.get(i).getGroupPlanGL().getPickupDate().format(DateTimeFormatter.ISO_DATE));
        }

        myholder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickLister(View v, int position) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();

                // send grocery list object
                GroceryList target = new GroceryList();
                target = lists.get(position);

                // send grocery list to grocery list fragment
                Bundle result = new Bundle();
                result.putSerializable("bundleKey", target);
                activity.getSupportFragmentManager()
                        .setFragmentResult("requestKey", result);

                // go to grocery list view fragment
                GroceryListFragment groceryListFragment = new GroceryListFragment();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,groceryListFragment)
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
