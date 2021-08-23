package com.example.ad_project_kampung_unite.search_product;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.data.remote.GroceryItemService;
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
import com.example.ad_project_kampung_unite.entities.GroceryItem;
import com.example.ad_project_kampung_unite.entities.GroceryList;
import com.example.ad_project_kampung_unite.entities.Product;
import com.google.android.material.chip.Chip;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchProductListAdapter extends RecyclerView.Adapter<SearchProductListAdapter.ViewHolder> {

    private List<Product> productList;
    private GroceryList groceryList;
    private GroceryItemService groceryItemService;

    // RecyclerView recyclerView;
    public SearchProductListAdapter(List<Product> productList, GroceryList groceryList) {
        this.productList = productList;
        this.groceryList = groceryList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.find_products_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Product product = productList.get(position);
//        holder.imageView.setImageURI(Uri.parse(productList[position].getImgURL()));
        holder.productNameView.setText(productList.get(position).getProductName());
        holder.productDescView.setText(productList.get(position).getProductDescription());
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int productId = product.getProductId();

                // add product to grocery list
                groceryItemService = RetrofitClient.createService(GroceryItemService.class);
                Call<Integer> call = groceryItemService.addGroceryItemToGroceryList(productId, 1, groceryList.getId());

                call.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        Toast.makeText(view.getContext(),"Added "+ product.getProductName() +" to grocery list",Toast.LENGTH_SHORT).show();
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


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView productNameView;
        public TextView productDescView;
        public Chip add;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.productNameView = (TextView) itemView.findViewById(R.id.productName);
            this.productDescView = (TextView) itemView.findViewById(R.id.productDesc);
            this.add = (Chip) itemView.findViewById(R.id.add);
        }
    }
}
