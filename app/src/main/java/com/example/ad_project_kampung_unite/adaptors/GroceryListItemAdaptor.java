package com.example.ad_project_kampung_unite.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.entities.GroceryItem;
import com.example.ad_project_kampung_unite.entities.GroupPlan;
import com.example.ad_project_kampung_unite.entities.enums.GroupPlanStatus;

import java.util.List;

public class GroceryListItemAdaptor extends RecyclerView.Adapter<GroceryListItemAdaptor.ViewHolder> {

    //viewholder obj provides access to all views within each item row
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView itemQuantity, itemName, itemAmount;

        public ViewHolder(View itemView){
            super(itemView);

            itemQuantity = itemView.findViewById(R.id.item_quantity);
            itemName = itemView.findViewById(R.id.item_name);
            itemAmount = itemView.findViewById(R.id.item_amount);
        }
    }

    private List<GroceryItem> groceryItemList;

    public GroceryListItemAdaptor(List<GroceryItem> groceryItemList){
        this.groceryItemList = groceryItemList;
    }

    // inflate item row layout and returning the holder
    @Override
    public GroceryListItemAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View groceryListItemView = inflater.inflate(R.layout.indiv_grocery_list_item, parent, false);

        // Return a new holder instance
        GroceryListItemAdaptor.ViewHolder viewHolder = new GroceryListItemAdaptor.ViewHolder(groceryListItemView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(GroceryListItemAdaptor.ViewHolder holder, int position) {
        // Get the data model based on position
        GroceryItem groceryItem = groceryItemList.get(position);

        // Set item views based on your views and data model
        TextView itemQuantity = holder.itemQuantity;
        TextView itemName = holder.itemName;
        TextView itemAmount = holder.itemAmount;

        itemQuantity.setText(String.valueOf(groceryItem.getQuantity()) + "x");
        itemName.setText(groceryItem.getProduct().getProductName());
        //add condition checking whether the prices are updated
        if(groceryItem.getGroceryList().getGroupPlanGL()==null){
            itemAmount.setVisibility(View.GONE);
        }
        else{
            if(groceryItem.getGroceryList().getGroupPlanGL().getGroupPlanStatus()== GroupPlanStatus.SHOPPINGCOMPLETED){
                if(groceryItem.getSubtotal()==0){
                    itemAmount.setText("Not Purchased");
                }
                else{
                    itemAmount.setText("$" + String.valueOf(groceryItem.getSubtotal()));
                }
            }
            else{
                itemAmount.setVisibility(View.GONE);
            }
        }
//        itemAmount.setVisibility(View.INVISIBLE);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return groceryItemList.size();
    }
}
