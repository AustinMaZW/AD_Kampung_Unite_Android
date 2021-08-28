package com.example.ad_project_kampung_unite.manage_grocery_list;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.ItemClickListener;
import com.example.ad_project_kampung_unite.R;

//Holder for individual rows in 'My Grocery Lists' recyclerview
//each row is a separate grocery list
public class MyGroceryListsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView mGroceryListName, mPickupDetail, status;
    ItemClickListener itemClickListener;

    MyGroceryListsHolder(@NonNull View itemView){
        super(itemView);

        this.mGroceryListName = itemView.findViewById(R.id.grocerylistname);
        this.mPickupDetail = itemView.findViewById(R.id.pickupdetails);
        this.status = itemView.findViewById(R.id.status);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClickLister(v,getLayoutPosition());
    }

    public void setItemClickListener(ItemClickListener ic){
        this.itemClickListener = ic;

    }
}
