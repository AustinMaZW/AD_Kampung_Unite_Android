package com.example.ad_project_kampung_unite;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//Holder for individual rows in 'My Grocery Lists' recyclerview
//each row is a separate grocery list
public class GroupDetailsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView mGroceryDetailItemName, mGroceryDetailItemQuantity,mGroceryDetailItemPrice;

    GroupDetailsHolder(@NonNull View itemView){
        super(itemView);

        this.mGroceryDetailItemName = itemView.findViewById(R.id.groceryDetailItemName);
        this.mGroceryDetailItemQuantity = itemView.findViewById(R.id.groceryDetailItemQuantity);
        this.mGroceryDetailItemPrice = itemView.findViewById(R.id.groceryDetailItemPrice);

    }

    @Override
    public void onClick(View v) {

    }
}
