package com.example.ad_project_kampung_unite;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView mGroceryListName, mPickupDetail;
    ItemClickListener itemClickListener;

    MyHolder(@NonNull View itemView){
        super(itemView);

        this.mGroceryListName = itemView.findViewById(R.id.grocerylistname);
        this.mPickupDetail = itemView.findViewById(R.id.pickupdetails);

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
