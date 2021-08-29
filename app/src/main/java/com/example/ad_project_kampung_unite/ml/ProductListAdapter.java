package com.example.ad_project_kampung_unite.ml;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.entities.Product;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder>{
    private List<Product> pList;
    private List<Integer> planIds;
    private Context context;

    public ProductListAdapter(List<Product> pList) {
        this.pList = pList;
    }

    public ProductListAdapter(List<Product> pList, Context context,List<Integer> planIds) {
        this.pList = pList;
        this.context = context;
        this.planIds = planIds;
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.buyer_list_item_pop_item,null);
        return new ProductListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {

        if(pList.get(position).getImgURL().length() > 0) {
            String url = pList.get(position).getImgURL();
            Picasso.get().load(url).into(holder.itemImg);
        }
        holder.item.setText(pList.get(position).getProductName());
    }

    @Override
    public int getItemCount() {
        if(pList == null){
            return 0;
        }
        return pList.size();
    }

    public class ProductListViewHolder extends RecyclerView.ViewHolder {
        private TextView item;
        private ImageView itemImg;

        public ProductListViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.productInplan);
            itemImg = itemView.findViewById(R.id.productImg);
        }
    }
}
