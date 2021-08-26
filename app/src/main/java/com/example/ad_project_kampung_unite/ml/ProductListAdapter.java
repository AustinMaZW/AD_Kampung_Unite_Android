package com.example.ad_project_kampung_unite.ml;

import android.content.Context;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.entities.GroceryList;
import com.example.ad_project_kampung_unite.entities.Product;

import java.util.List;
import java.util.zip.Inflater;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder>{
    private List<Product> pList;
    private List<Integer> planIds;
    private Context context;
    private GroceryList gList;

    public ProductListAdapter(List<Product> pList) {
        this.pList = pList;
    }

    public ProductListAdapter(List<Product> pList, Context context,List<Integer> planIds,GroceryList gList) {
        this.pList = pList;
        this.context = context;
        this.planIds = planIds;
        this.gList = gList;
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.buyer_list_item_pop_item,null);
        return new ProductListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {
        holder.item.setText(pList.get(position).getProductName());
//        gList.getGroceryItems().stream().forEach(x->{
//            if(x.getProduct().getProductId() == pList.get(position).getProductId()){
//                TextPaint tp = holder.item.getPaint();
//                tp.setFakeBoldText(true);
//            }
//        });
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

        public ProductListViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.productInplan);
        }
    }
}
