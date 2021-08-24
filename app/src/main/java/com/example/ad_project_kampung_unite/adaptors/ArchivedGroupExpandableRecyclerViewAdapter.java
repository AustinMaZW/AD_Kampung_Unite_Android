package com.example.ad_project_kampung_unite.adaptors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.entities.GroceryItem;
import com.example.ad_project_kampung_unite.entities.HitchRequest;
import com.google.android.material.chip.Chip;

import org.w3c.dom.Text;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ArchivedGroupExpandableRecyclerViewAdapter extends RecyclerView.Adapter<ArchivedGroupExpandableRecyclerViewAdapter.ViewHolder> {

    private List<HitchRequest> hitchRequestsList;
    private List<List<GroceryItem>> groceryItemList;
    List<Integer> counter = new ArrayList<>();

    Context context;

    public ArchivedGroupExpandableRecyclerViewAdapter(Context context,
                                                      List<HitchRequest> hitchRequestsList,
                                                      List<List<GroceryItem>> groceryItemList){
        this.context = context;
        this.hitchRequestsList = hitchRequestsList;
        this.groceryItemList = groceryItemList;

        for (int i = 0; i < hitchRequestsList.size(); i++) {
            counter.add(0);
        }
    }

    //viewholder obj provides access to all views within each item row
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, pickuptime, itemcount;
        ImageButton dropBtn;
        RecyclerView cardRecyclerView;
        CardView cardView;
        Chip mrequeststatus;

        public ViewHolder(View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.groceryDetailListTitle);
            pickuptime = itemView.findViewById(R.id.groceryDetailPickupTime);
            itemcount=itemView.findViewById(R.id.groceryDetailItemQuantitySum);
            dropBtn = itemView.findViewById(R.id.categoryExpandBtn);
            cardRecyclerView = itemView.findViewById(R.id.innerRecyclerView);
            cardView = itemView.findViewById(R.id.cardView);
            mrequeststatus = itemView.findViewById(R.id.group_details_status);
        }
    }
    // inflate item row layout and returning the holder
    @Override
    public ArchivedGroupExpandableRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_details_collapse_items, parent, false);

        ArchivedGroupExpandableRecyclerViewAdapter.ViewHolder vh = new ArchivedGroupExpandableRecyclerViewAdapter.ViewHolder(v);

        return vh;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ArchivedGroupExpandableRecyclerViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        HitchRequest hitchRequest = hitchRequestsList.get(position);
        List<GroceryItem> groceryItemsCount = groceryItemList.get(position);
        String mhitchRequestStatus = hitchRequest.getRequestStatus().getDisplayStatus();
        Double groceryItemsTotal = 0.0;
        for(int i = 0; i<groceryItemsCount.size();i++){
            groceryItemsTotal = groceryItemsTotal + groceryItemsCount.get(i).getSubtotal();
        }

        TextView name = holder.name;
        TextView pickup = holder.pickuptime;
        TextView itemcount = holder.itemcount;
        Chip requestStatusChip = holder.mrequeststatus;

        name.setText(groceryItemList.get(position).get(0).getGroceryList().getName());
        pickup.setVisibility(View.GONE);
        itemcount.setText("Items: " + Integer.toString(groceryItemsCount.size()));

        requestStatusChip.setText(mhitchRequestStatus);
        if(mhitchRequestStatus.equals("Pending Approval")){
            requestStatusChip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Kampong_Yellow)));
        }
        else if(mhitchRequestStatus.equals("Accepted")){
            requestStatusChip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Kampong_Green)));
        }
        else if(mhitchRequestStatus.equals("Rejected")){
            requestStatusChip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Kampong_Gray)));
        }


        InnerRecyclerViewAdapter itemInnerRecyclerView = new InnerRecyclerViewAdapter(groceryItemList.get(position));
        holder.cardRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (counter.get(position) % 2 == 0) {
                    holder.cardRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    holder.cardRecyclerView.setVisibility(View.GONE);
                }
                counter.set(position, counter.get(position) + 1);
            }
        });
        holder.cardRecyclerView.setAdapter(itemInnerRecyclerView);

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return (int)hitchRequestsList.stream().count();
    }
}
