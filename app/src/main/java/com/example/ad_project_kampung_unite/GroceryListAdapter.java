package com.example.ad_project_kampung_unite;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//Bridge between holder and list,
//for each holder - inflate, populate with data, add onClickListener, direct to activity 'view list details'
public class GroceryListAdapter extends RecyclerView.Adapter<GroceryListHolder> {

    Context c;
    ArrayList<GroceryList> lists;

    public GroceryListAdapter(Context c, ArrayList<GroceryList> lists) {
        this.c = c;
        this.lists = lists;
    }

    @NonNull
    @Override
    public GroceryListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.items_my_grocery_lists,null);
        return new GroceryListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryListHolder myholder, int i) {
        myholder.mGroceryListName.setText(lists.get(i).getName());
        myholder.mPickupDetail.setText((CharSequence) lists.get(i).getDate());
        //.setImageResource to set image.

        myholder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickLister(View v, int position) {
                String gName = lists.get(position).getName();
                String gDetails = lists.get(position).getDate();

                Intent intent = new Intent(c, ViewGroceryListActivity.class);
                intent.putExtra("gName",gName);
                intent.putExtra("gDetails",gDetails);
                c.startActivity(intent);
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
}
