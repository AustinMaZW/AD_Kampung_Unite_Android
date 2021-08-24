package com.example.ad_project_kampung_unite.adaptors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.data.remote.HitchRequestService;
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
import com.example.ad_project_kampung_unite.entities.GroceryItem;
import com.example.ad_project_kampung_unite.entities.GroceryList;
import com.example.ad_project_kampung_unite.entities.GroupPlan;
import com.example.ad_project_kampung_unite.entities.HitchRequest;
import com.google.android.material.chip.Chip;
import com.example.ad_project_kampung_unite.entities.enums.GroupPlanStatus;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActiveGroupExpandableRecyclerViewAdapter extends RecyclerView.Adapter<ActiveGroupExpandableRecyclerViewAdapter.ViewHolder> {

    private HitchRequestService hitchRequestService;
    private List<HitchRequest> hitchRequestsList;
    private List<List<GroceryItem>> groceryItemList;
    private GroupPlanStatus groupPlanStatus;
    List<Integer> counter = new ArrayList<>();

    Context context;

    public ActiveGroupExpandableRecyclerViewAdapter(Context context,
                                         List<HitchRequest> hitchRequestsList,
                                         List<List<GroceryItem>> groceryItemList,
                                         GroupPlanStatus groupPlanStatus){
        this.context = context;
        this.hitchRequestsList = hitchRequestsList;
        this.groceryItemList = groceryItemList;
        this.groupPlanStatus = groupPlanStatus;

        for (int i = 0; i < hitchRequestsList.size(); i++) {
            counter.add(0);
        }
    }

    //viewholder obj provides access to all views within each item row
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, pickuptime, itemcount, paymentstatus;
        ImageButton dropBtn;
        Button completepaymentBtn;
        RecyclerView cardRecyclerView;
        CardView cardView;
        Chip mrequeststatus;

        public ViewHolder(View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.groceryDetailListTitle);
            pickuptime = itemView.findViewById(R.id.groceryDetailPickupTime);
            itemcount=itemView.findViewById(R.id.groceryDetailItemQuantitySum);
            paymentstatus = itemView.findViewById(R.id.payment_status);
            dropBtn = itemView.findViewById(R.id.categoryExpandBtn);
            completepaymentBtn = itemView.findViewById(R.id.complete_payment_btn);
            cardRecyclerView = itemView.findViewById(R.id.innerRecyclerView);
            cardView = itemView.findViewById(R.id.cardView);
            mrequeststatus = itemView.findViewById(R.id.group_details_status);
        }
    }
    // inflate item row layout and returning the holder
    @Override
    public ActiveGroupExpandableRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_details_collapse_items, parent, false);

        ActiveGroupExpandableRecyclerViewAdapter.ViewHolder vh = new ActiveGroupExpandableRecyclerViewAdapter.ViewHolder(v);

        return vh;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ActiveGroupExpandableRecyclerViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        HitchRequest hitchRequest = hitchRequestsList.get(position);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mma");
        String mpickuptime = hitchRequest.getPickupTimeChosen().toLocalTime().format(dtf);
        List<GroceryItem> groceryItemsCount = groceryItemList.get(position);
        String mhitchRequestStatus = hitchRequest.getRequestStatus().getDisplayStatus();
        Double groceryItemsTotal = 0.0;
        for(int i = 0; i<groceryItemsCount.size();i++){
            groceryItemsTotal = groceryItemsTotal + groceryItemsCount.get(i).getSubtotal();
        }

        TextView name = holder.name;
        TextView pickuptime = holder.pickuptime;
        TextView itemcount = holder.itemcount;
        Chip requestStatusChip = holder.mrequeststatus;

        name.setText(groceryItemList.get(position).get(0).getGroceryList().getName());

        pickuptime.setText("Pick-up time: " + mpickuptime);
        itemcount.setText("Items: " + Integer.toString(groceryItemsCount.size()));

        requestStatusChip.setText(mhitchRequestStatus);
        if(mhitchRequestStatus.equals("Pending Approval")){
            requestStatusChip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Kampong_Yellow)));
        }
        else if(mhitchRequestStatus.equals("Accepted")){
            requestStatusChip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Kampong_Green)));
        }
        else if(mhitchRequestStatus.equals("Rejected")){
            requestStatusChip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.Kampong_Gray)));
        }

        if (groupPlanStatus == GroupPlanStatus.SHOPPINGCOMPLETED) {
            if (hitchRequest.isBuyerConfirmTransaction()) {
                if (hitchRequest.isHitcherConfirmTransaction()) {
                    holder.paymentstatus.setText(R.string.paid);
                    holder.paymentstatus.setBackground(context.getResources().getDrawable(R.drawable.rounded_bg_green, null));
                } else {
                    holder.paymentstatus.setText(R.string.pending_payment);
                    holder.paymentstatus.setBackground(context.getResources().getDrawable(R.drawable.rounded_bg_yellow, null));
                }
                holder.completepaymentBtn.setVisibility(View.GONE);
            } else {
                holder.paymentstatus.setVisibility(View.GONE);
            }
        } else {
            holder.completepaymentBtn.setVisibility(View.GONE);
            holder.paymentstatus.setVisibility(View.GONE);
        }

        InnerRecyclerViewAdapter itemInnerRecyclerView = new InnerRecyclerViewAdapter(groceryItemList.get(position));
        holder.cardRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (counter.get(position) % 2 == 0) {
                    holder.cardRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    holder.cardRecyclerView.setVisibility(View.GONE);
                }
                counter.set(position, counter.get(position) + 1);
            }
        });
        holder.cardRecyclerView.setAdapter(itemInnerRecyclerView);

        holder.completepaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hitchRequest.setBuyerConfirmTransaction(true);
                updateConrirmTransaction(hitchRequest, holder);
            }
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return (int)hitchRequestsList.stream().count();
    }

    private void updateConrirmTransaction(HitchRequest hitchRequest, ViewHolder holder) {
        hitchRequestService = RetrofitClient.createService(HitchRequestService.class);
        Call<HitchRequest> call = hitchRequestService.updateHitchRequest(hitchRequest);
        call.enqueue(new Callback<HitchRequest>() {
            @Override
            public void onResponse(Call<HitchRequest> call, Response<HitchRequest> response) {
                if (response.isSuccessful()) {
                    HitchRequest updatedHR = response.body();

                    if (updatedHR.isHitcherConfirmTransaction()) {
                        holder.paymentstatus.setText(R.string.paid);
                        holder.paymentstatus.setBackground(context.getResources().getDrawable(R.drawable.rounded_bg_green, null));
                    } else {
                        holder.paymentstatus.setText(R.string.pending_payment);
                        holder.paymentstatus.setBackground(context.getResources().getDrawable(R.drawable.rounded_bg_yellow, null));
                    }
                    holder.completepaymentBtn.setVisibility(View.GONE);
                    holder.paymentstatus.setVisibility(View.VISIBLE);
                }
                else {
                    Log.e("updateHitchRequest Error", response.errorBody().toString());
                }
            }
            @Override
            public void onFailure(Call<HitchRequest> call, Throwable t) {
                call.cancel();
                Log.w("Failure", "Failure!");
                t.printStackTrace();
            }
        });
    }
}
