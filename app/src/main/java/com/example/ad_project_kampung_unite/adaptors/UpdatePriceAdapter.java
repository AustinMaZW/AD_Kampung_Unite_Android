package com.example.ad_project_kampung_unite.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.entities.CombinedPurchaseList;
import com.example.ad_project_kampung_unite.entities.GroceryItem;

import java.util.List;

public class UpdatePriceAdapter extends RecyclerView.Adapter<UpdatePriceAdapter.Holder> {
    private List<CombinedPurchaseList> cpList;
    public Holder holder;

    public UpdatePriceAdapter(List<CombinedPurchaseList> list) {
        this.cpList = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View updatePriceItemView = inflater.inflate(R.layout.update_price_item, parent, false);

        // Return a new holder instance
        holder = new Holder(updatePriceItemView);
        return holder;
    }

    @Override
    public int getItemCount() {
        return cpList.size();
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        // Get the data model based on position
        CombinedPurchaseList item = cpList.get(position);

        holder.tvName.setText(item.getProduct().getProductName());
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        holder.etSubtotal.setText(String.valueOf(item.getProductSubtotal())); // testing
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView tvName, tvQuantity;
        public EditText etSubtotal, etDiscount;

        public Holder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.name);
            tvQuantity = itemView.findViewById(R.id.quantity);
            etSubtotal = itemView.findViewById(R.id.subtotal);
            etDiscount = itemView.findViewById(R.id.discount);
        }
    }
}
