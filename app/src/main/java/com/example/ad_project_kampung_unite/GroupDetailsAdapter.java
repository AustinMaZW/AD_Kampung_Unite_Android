package com.example.ad_project_kampung_unite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroupDetailsAdapter extends RecyclerView.Adapter<GroupDetailsHolder> {

    Context c;
    ArrayList<GroceryList> lists;

    public GroupDetailsAdapter(Context c, ArrayList<GroceryList> lists) {
        this.c = c;
        this.lists = lists;
    }

    @NonNull
    @Override
    public GroupDetailsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_group_details_list,null);
        return new GroupDetailsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupDetailsHolder myholder, int i) {
        myholder.mGroceryDetailItemName.setText(lists.get(i).getName());
        myholder.mGroceryDetailItemQuantity.setText(lists.get(i).getName());
        myholder.mGroceryDetailItemPrice.setText(lists.get(i).getTotalitems());
        }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}
