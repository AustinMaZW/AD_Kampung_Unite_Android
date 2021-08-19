package com.example.ad_project_kampung_unite.search_product;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.ViewGroceryListActivity;
import com.example.ad_project_kampung_unite.entities.Product;
import com.google.android.material.chip.Chip;

import java.util.List;

public class SearchProductListAdapter extends RecyclerView.Adapter<SearchProductListAdapter.ViewHolder> {

    private List<Product> productList;

    // RecyclerView recyclerView;
    public SearchProductListAdapter(List<Product> productList) {
        this.productList = productList;
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
                Toast.makeText(view.getContext(),"Added "+ product.getProductName() +" to grocery list",Toast.LENGTH_SHORT).show();
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
