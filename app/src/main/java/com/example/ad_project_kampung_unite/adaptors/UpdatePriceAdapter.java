package com.example.ad_project_kampung_unite.adaptors;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.entities.CombinedPurchaseList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdatePriceAdapter extends RecyclerView.Adapter<UpdatePriceAdapter.Holder> {
    private List<CombinedPurchaseList> cpList;
    private Map<Integer, String> subtotalMap;
    private Map<Integer, String> discountMap;
    private Holder holder;
    private Map<Integer, Boolean> errors;

    public UpdatePriceAdapter(List<CombinedPurchaseList> list) {
        this.cpList = list;
        subtotalMap = new HashMap<>();
        discountMap = new HashMap<>();

        if (cpList != null) {
            for (int i = 0; i < cpList.size(); i++) {
                CombinedPurchaseList item = cpList.get(i);
                subtotalMap.put(item.getId(), String.valueOf(item.getProductSubtotal()));
                discountMap.put(item.getId(), String.valueOf(0));
            }
        }

        errors = new HashMap<>();
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
        holder.etSubtotal.setText(String.valueOf(item.getProductSubtotal()));
        holder.etDiscount.setText(String.valueOf(item.getProductDiscount()));
        /*holder.etSubtotal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    subtotalMap.put(item.getId(), holder.etSubtotal.getText().toString());
                }
            }
        });*/
        holder.etSubtotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                subtotalMap.put(item.getId(), s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = holder.etSubtotal.getText().toString();
                boolean isvalid = validate(text);
                if (!isvalid) {
                    holder.etSubtotal.setError("enter number only");
                    errors.put(position,true);
                } else {
                    if (errors.containsKey(position))
                        errors.remove(position);
                }
            }
        });
        holder.etDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                discountMap.put(item.getId(), s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = holder.etDiscount.getText().toString();
                boolean isvalid = validate(text);
                if (!isvalid) {
                    holder.etDiscount.setError("enter number only");
                    errors.put(position,true);
                } else {
                    if (errors.containsKey(position))
                        errors.remove(position);
                }
            }
        });
    }

    public Map<Integer, String> getSubtotalMap() { return subtotalMap; }

    public Map<Integer, String> getDiscountMap() { return discountMap; }

    private boolean validate(String text) {
        return isNumeric(text);
    }

    private static boolean isNumeric(String text) {
        if (text == null || text.equals("-0.0")) {
            return false;
        }

        try {
            if (!text.isEmpty()) {
                double d = Double.parseDouble(text);
                if (d < 0)
                    return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        //empty string is ok
        return true;
    }

    public boolean hasError() {
        if (errors.size() > 0) {
            return true;
        }
        return false;
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView tvName, tvQuantity;
        private EditText etSubtotal, etDiscount;

        public Holder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.name);
            tvQuantity = itemView.findViewById(R.id.quantity);
            etSubtotal = itemView.findViewById(R.id.subtotal);
            etDiscount = itemView.findViewById(R.id.discount);
        }
    }
}
