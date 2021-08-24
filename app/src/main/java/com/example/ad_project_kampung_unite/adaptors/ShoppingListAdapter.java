package com.example.ad_project_kampung_unite.adaptors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.entities.CombinedPurchaseList;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {

    public ViewHolder viewHolder;
    private List<CombinedPurchaseList> cplList;
    public ShoppingListAdapter(List<CombinedPurchaseList> list){
        this.cplList = list;
    }

    public List<CombinedPurchaseList> getPurchasedList() {
        return cplList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View combinedItemView = inflater.inflate(R.layout.combined_item,parent,false);
        viewHolder = new ViewHolder(combinedItemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CombinedPurchaseList item = cplList.get(position);

        holder.itemName.setText(item.getProductName());
        holder.itemQty.setText(String.valueOf(item.getQuantity()));
        System.out.println(item);
        if (item.isPurchasedStatus()){
            holder.checkBox.setChecked(item.isPurchasedStatus());
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updatePurchaseStatus(position, buttonView.isChecked());
            }
        });

    }

    @Override
    public int getItemCount() {
        return cplList.size();
    }

    public void updatePurchaseStatus(int position, boolean isPurchased){
        CombinedPurchaseList cpl = cplList.get(position);
        cpl.setPurchasedStatus(isPurchased);
        System.out.println(cpl);
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        private TextView itemName, itemQty;
        public CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.CIitemName);
            itemQty = itemView.findViewById(R.id.CIquantity);
            checkBox = itemView.findViewById(R.id.CIcheckbox);
        }
    }


}
