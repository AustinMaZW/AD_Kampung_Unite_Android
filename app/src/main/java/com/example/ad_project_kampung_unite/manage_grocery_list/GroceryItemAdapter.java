package com.example.ad_project_kampung_unite.manage_grocery_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.entities.GroceryItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GroceryItemAdapter extends RecyclerView.Adapter<GroceryItemAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView pImage;
        public TextView pName, pDesc, pQty;

        public ViewHolder(View itemView) {
            super(itemView);

            pImage = itemView.findViewById(R.id.pImage);
            pName = itemView.findViewById(R.id.pName);
            pDesc = itemView.findViewById(R.id.pDesc);
            pQty = itemView.findViewById(R.id.pQty);
        }
    }

    private List<GroceryItem> groceryItemList;

    public GroceryItemAdapter(List<GroceryItem> groceryItemList) {
        this.groceryItemList = groceryItemList;
    }

    // inflate item row layout and returning the holder
    @Override
    public GroceryItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View groceryListItemView = inflater.inflate(R.layout.grocerylist_groceryitem, parent, false);

        // Return a new holder instance
        GroceryItemAdapter.ViewHolder viewHolder = new GroceryItemAdapter.ViewHolder(groceryListItemView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(GroceryItemAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        GroceryItem groceryItem = groceryItemList.get(position);

        // Set item views based on your views and data model
        ImageView pImage = holder.pImage;
        TextView pName = holder.pName;
        TextView pDesc = holder.pDesc;
        TextView pQty = holder.pQty;

        String url = groceryItem.getProduct().getImgURL();
        Picasso.get().load(url).into(pImage);

        pName.setText(groceryItem.getProduct().getProductName());
        pDesc.setText(groceryItem.getProduct().getProductDescription());
        pQty.setText(String.valueOf(groceryItem.getQuantity()));
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return groceryItemList.size();
    }
}
