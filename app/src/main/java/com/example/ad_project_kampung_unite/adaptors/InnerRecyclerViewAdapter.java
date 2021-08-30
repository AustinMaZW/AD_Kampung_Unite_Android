package com.example.ad_project_kampung_unite.adaptors;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.entities.GroceryItem;
import com.example.ad_project_kampung_unite.entities.HitchRequest;
import com.example.ad_project_kampung_unite.entities.enums.GLStatus;
import com.example.ad_project_kampung_unite.entities.enums.GroupPlanStatus;

import java.util.ArrayList;
import java.util.List;

public class InnerRecyclerViewAdapter extends RecyclerView.Adapter<InnerRecyclerViewAdapter.ViewHolder> {
    public List<GroceryItem> nameList = new ArrayList<>();

    public InnerRecyclerViewAdapter(List<GroceryItem> nameList) {

        this.nameList = nameList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, test,price;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            test = itemView.findViewById(R.id.item_quantity);
            price = itemView.findViewById(R.id.item_amount);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.indiv_grocery_list_item, parent, false);

        InnerRecyclerViewAdapter.ViewHolder vh = new InnerRecyclerViewAdapter.ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView name = holder.name;
        TextView test = holder.test;
        TextView amount = holder.price;

        int quantity = nameList.get(position).getQuantity();
        name.setText(nameList.get(position).getProduct().getProductName());
        test.setText(Integer.toString(quantity) + "x");

        if(nameList.get(position).getSubtotal() == 0){
            amount.setText("Not Purchased");
        }else{
            amount.setText("$" + String.valueOf(nameList.get(position).getSubtotal()));
        }

        if(nameList.get(position).getGroceryList().getGroupPlanGL() ==null){
            amount.setVisibility(View.GONE);
        }
        //if group plan status is COMPLETED, don't show the item price
        else if(nameList.get(position).getGroceryList().getGroupPlanGL().getGroupPlanStatus().getDisplayStatus()!= GroupPlanStatus.SHOPPINGCOMPLETED.getDisplayStatus()){
            amount.setVisibility(View.GONE);
        };
//
//        name.setText(nameList.get(position).getProduct().getProductName());
//        test.setText(nameList.get(position).getProduct().getProductName());
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }


}