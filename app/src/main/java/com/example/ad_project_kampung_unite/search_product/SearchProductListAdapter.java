package com.example.ad_project_kampung_unite.search_product;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
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
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchProductListAdapter extends RecyclerView.Adapter<SearchProductListAdapter.ViewHolder> {

    private List<Product> productListFull;
    private List<Product> productList;
    private GroceryList groceryList;
    private List<GroceryItem> addedGroceryItems;
    private GroceryItemService groceryItemService;
    private List<Product> addedProducts;

    // RecyclerView recyclerView;
    public SearchProductListAdapter(List<Product> productList, GroceryList groceryList) {
        this.productList = productList;
        productListFull = new ArrayList<>(productList);
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
        Product product = productList.get(position);

        if(product.getImgURL() != null) {
            String url = product.getImgURL();
            Picasso.get().load(url).into(holder.imageView);
        }

        holder.productNameView.setText(product.getProductName());
        holder.productDescView.setText(product.getProductDescription());
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                groceryItemService = RetrofitClient.createService(GroceryItemService.class);

                //check if grocery item exists
                Call<List<GroceryItem>> groceryItemListCall = groceryItemService.getGroceryItemsByGroceryListId(groceryList.getId());
                groceryItemListCall.enqueue(new Callback<List<GroceryItem>>() {
                    @Override
                    public void onResponse(Call<List<GroceryItem>> call, Response<List<GroceryItem>> response) {
                        if(response.isSuccessful()) {
                            addedGroceryItems = response.body();
                            addedProducts = addedGroceryItems.stream().map(GroceryItem::getProduct).collect(Collectors.toList());

                            // add product to grocery list
                            // check if item exists in grocery list
                            if(addedProducts.stream().filter(x -> x.getProductId() == product.getProductId()).findFirst().orElse(null) != null) {
                                Product existProduct = addedProducts.stream().filter(x -> x.getProductId() == product.getProductId()).findFirst().get();

                                GroceryItem existItem = addedGroceryItems.stream().filter(x -> x.getProduct().getProductId() == product.getProductId()).findFirst().get();

                                int quantity = existItem.getQuantity() + 1;

                                Call<Integer> call1 = groceryItemService.updateGroceryItemInGroceryList(existItem.getId(), quantity);
                                call1.enqueue(new Callback<Integer>() {
                                    @Override
                                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                                        Toast.makeText(view.getContext(),quantity + " " + product.getProductName() + " added",Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<Integer> call, Throwable t) {
                                        call.cancel();
                                        Log.w("Failure", "Failure!");
                                        t.printStackTrace();
                                    }
                                });
                            } else {
                                int productId = product.getProductId();
                                Call<Integer> call2 = groceryItemService.addGroceryItemToGroceryList(productId, 1, groceryList.getId());
                                call2.enqueue(new Callback<Integer>() {
                                    @Override
                                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                                        Toast.makeText(view.getContext(),"1 "+ product.getProductName() + " added",Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<Integer> call, Throwable t) {

                                    }
                                });
                            }
                        } else {
                        Log.e("getGroceryItemsByGroceryListId Error", response.errorBody().toString());
                        }
                    }
                    @Override
                    public void onFailure(Call<List<GroceryItem>> call, Throwable t) {
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

    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Product> filteredProducts = new ArrayList<>();
            if(constraint == null || constraint.length() == 0) {
                filteredProducts.addAll(productListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Product item : productListFull) {
                    if(item.getProductName().toLowerCase().contains(filterPattern)) {
                        filteredProducts.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredProducts;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            productList.clear();
            productList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

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
