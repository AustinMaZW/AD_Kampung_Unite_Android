package com.example.ad_project_kampung_unite;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//Holder for individual rows in 'My Grocery Lists' recyclerview
//each row is a separate grocery list
public class MyGroupPlansHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mGroupPlanName, mGroupPlanItemCount, mGroupPlanRequests;
    ItemClickListener itemClickListener;

    public MyGroupPlansHolder(@NonNull View itemView){
        super(itemView);

        this.mGroupPlanName = itemView.findViewById(R.id.group_plan_name);
        this.mGroupPlanItemCount = itemView.findViewById(R.id.group_plan_total_items);
        this.mGroupPlanRequests = itemView.findViewById(R.id.group_plan_hitch_requests);

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
