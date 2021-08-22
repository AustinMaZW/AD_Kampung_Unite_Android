package com.example.ad_project_kampung_unite.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.entities.GroceryItem;
import com.example.ad_project_kampung_unite.entities.GroceryList;
import com.example.ad_project_kampung_unite.entities.GroupPlan;
import com.example.ad_project_kampung_unite.entities.HitchRequest;

import java.util.List;
import java.util.Map;

public class ExpandableRecyclerViewAdapter extends RecyclerView.Adapter<ExpandableRecyclerViewAdapter.ViewHolder> {

    //viewholder obj provides access to all views within each item row
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, pickuptime, itemcount;
        ImageButton dropBtn;

        public ViewHolder(View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.groceryDetailListTitle);
            pickuptime = itemView.findViewById(R.id.groceryDetailPickupTime);
            itemcount=itemView.findViewById(R.id.groceryDetailItemQuantitySum);
            dropBtn = itemView.findViewById(R.id.categoryExpandBtn);
        }
    }

    private List<HitchRequest> hitchRequestsList;
    private List<GroceryItem> groceryItemList;

    public ExpandableRecyclerViewAdapter(List<HitchRequest> hitchRequestsList){
        this.hitchRequestsList = hitchRequestsList;
    }

    // inflate item row layout and returning the holder
    @Override
    public ExpandableRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View groceryListView = inflater.inflate(R.layout.group_details_collapse_items, null);

        // Return a new holder instance
        ExpandableRecyclerViewAdapter.ViewHolder viewHolder = new ExpandableRecyclerViewAdapter.ViewHolder(groceryListView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ExpandableRecyclerViewAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        HitchRequest hitchRequest = hitchRequestsList.get(position);

        // Set item views based on your views and data model
        TextView name = holder.name;
        TextView pickuptime = holder.pickuptime;
        TextView itemcount = holder.itemcount;

//        name.setText(list.getName());
        pickuptime.setText(hitchRequest.getPickupTimeChosen().toString());
//        itemcount.setText((int) list.getGroceryItems().stream().count());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return (int)hitchRequestsList.stream().count();
    }
}
