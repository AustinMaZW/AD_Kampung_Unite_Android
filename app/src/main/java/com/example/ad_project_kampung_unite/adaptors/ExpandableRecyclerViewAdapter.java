package com.example.ad_project_kampung_unite.adaptors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.entities.GroceryItem;
import com.example.ad_project_kampung_unite.entities.GroceryList;
import com.example.ad_project_kampung_unite.entities.GroupPlan;
import com.example.ad_project_kampung_unite.entities.HitchRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExpandableRecyclerViewAdapter extends RecyclerView.Adapter<ExpandableRecyclerViewAdapter.ViewHolder> {

    private List<HitchRequest> hitchRequestsList;
    private List<List<GroceryItem>> groceryItemList;
    List<Integer> counter = new ArrayList<>();

    Context context;

    public ExpandableRecyclerViewAdapter(Context context,
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

        public ViewHolder(View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.groceryDetailListTitle);
            pickuptime = itemView.findViewById(R.id.groceryDetailPickupTime);
            itemcount=itemView.findViewById(R.id.groceryDetailItemQuantitySum);
            dropBtn = itemView.findViewById(R.id.categoryExpandBtn);
            cardRecyclerView = itemView.findViewById(R.id.innerRecyclerView);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
    // inflate item row layout and returning the holder
    @Override
    public ExpandableRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_details_collapse_items, parent, false);

        ExpandableRecyclerViewAdapter.ViewHolder vh = new ExpandableRecyclerViewAdapter.ViewHolder(v);

        return vh;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ExpandableRecyclerViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        HitchRequest hitchRequest = hitchRequestsList.get(position);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mma");
        String mpickuptime = hitchRequest.getPickupTimeChosen().toLocalTime().format(dtf);
        List<GroceryItem> groceryItemsCount = groceryItemList.get(position);

        TextView name = holder.name;
        TextView pickuptime = holder.pickuptime;
        TextView itemcount = holder.itemcount;

        name.setText(groceryItemList.get(position).get(0).getGroceryList().getName());
        pickuptime.setText("Pick-up time: " + mpickuptime);
        itemcount.setText("Items: " + Integer.toString(groceryItemsCount.size()));

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
