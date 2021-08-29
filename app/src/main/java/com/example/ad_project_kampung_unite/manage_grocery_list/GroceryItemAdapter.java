package com.example.ad_project_kampung_unite.manage_grocery_list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.data.remote.GroceryItemService;
import com.example.ad_project_kampung_unite.data.remote.GroceryListService;
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
import com.example.ad_project_kampung_unite.entities.GroceryItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroceryItemAdapter extends RecyclerView.Adapter<GroceryItemAdapter.ViewHolder> {

    private GroceryItemService groceryItemService;


    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView pImage;
        public TextView pName, pDesc, pQty;
        public Button plusBtn, minusBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            pImage = itemView.findViewById(R.id.pImage);
            pName = itemView.findViewById(R.id.pName);
            pDesc = itemView.findViewById(R.id.pDesc);
            pQty = itemView.findViewById(R.id.pQty);

            plusBtn = itemView.findViewById(R.id.plus);
            minusBtn = itemView.findViewById(R.id.minus);
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
    public void onBindViewHolder(GroceryItemAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Get the data model based on position
        GroceryItem groceryItem = groceryItemList.get(position);

        // Set item views based on your views and data model
        ImageView pImage = holder.pImage;
        TextView pName = holder.pName;
        TextView pDesc = holder.pDesc;
        TextView pQty = holder.pQty;
        Button plus = holder.plusBtn;
        Button minus = holder.minusBtn;

        if(groceryItem.getProduct().getImgURL().length() > 0) {
            String url = groceryItem.getProduct().getImgURL();
            Picasso.get().load(url).into(pImage);
        }
        pName.setText(groceryItem.getProduct().getProductName());
        pDesc.setText(groceryItem.getProduct().getProductDescription());
        pQty.setText(String.valueOf(groceryItem.getQuantity()));

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groceryItemService = RetrofitClient.createService(GroceryItemService.class);

                int quantity;

                if(groceryItem.getQuantity() > 1) {
                    quantity = groceryItem.getQuantity() -1;
                } else {
                    groceryItemList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, groceryItemList.size());
                    quantity = 0;
                }

                groceryItem.setQuantity(quantity);


                Call<Integer> call = groceryItemService.updateGroceryItemInGroceryList(groceryItem.getId(), quantity);
                call.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        pQty.setText(String.valueOf(quantity));

                    }
                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        call.cancel();
                        Log.w("Failure", "Failure!");
                        t.printStackTrace();
                    }
                });
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groceryItemService = RetrofitClient.createService(GroceryItemService.class);

                int quantity;

                if(groceryItem.getQuantity() > 0) {
                    quantity = groceryItem.getQuantity() + 1;
                } else {
                    quantity = 1;
                }

                groceryItem.setQuantity(quantity);


                Call<Integer> call = groceryItemService.updateGroceryItemInGroceryList(groceryItem.getId(), quantity);
                call.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        pQty.setText(String.valueOf(quantity));
                    }
                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        call.cancel();
                        Log.w("Failure", "Failure!");
                        t.printStackTrace();
                    }
                });
            }
        });


    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return groceryItemList.size();
    }
}
