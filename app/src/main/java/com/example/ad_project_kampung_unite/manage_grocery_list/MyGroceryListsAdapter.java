package com.example.ad_project_kampung_unite.manage_grocery_list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.ItemClickListener;
import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.ViewGroceryListFragment;
import com.example.ad_project_kampung_unite.entities.GroceryList;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MyGroceryListsAdapter extends RecyclerView.Adapter<MyGroceryListsHolder> {

    private Context c;
    private List<GroceryList> lists;

    public MyGroceryListsAdapter(Context c, List<GroceryList> lists) {
        this.c = c;
        this.lists = lists;
    }

    @NonNull
    @Override
    public MyGroceryListsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_grocery_lists_items,null);
        return new MyGroceryListsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyGroceryListsHolder myholder, int i) {
        GroceryList groceryList = lists.get(i);

        myholder.mGroceryListName.setText(groceryList.getName());
        if(lists.get(i).getGroupPlanGL() != null) {
            myholder.mPickupDetail.setText(lists.get(i).getGroupPlanGL().getPickupDate().format(DateTimeFormatter.ISO_DATE));
        }

        myholder.status.setText(groceryList.getStatus().getGLStatus());




        myholder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickLister(View v, int position) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();

                // send grocery list object
                GroceryList target = lists.get(position);

                // send grocery list to grocery list fragment
                Bundle result = new Bundle();
                result.putSerializable("bundleKey", target);

                // go to grocery list view fragment
                ViewGroceryListFragment viewGroceryListFragment = new ViewGroceryListFragment();
                viewGroceryListFragment.setArguments(result);       //putting bundle inside frag
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,viewGroceryListFragment,"VIEW_HITCHER_GL_FRAG")
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
