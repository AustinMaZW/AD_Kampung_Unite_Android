package com.example.ad_project_kampung_unite.adaptors;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.data.remote.HitchRequestService;
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
import com.example.ad_project_kampung_unite.entities.GroceryItem;
import com.example.ad_project_kampung_unite.entities.GroupPlan;
import com.example.ad_project_kampung_unite.entities.HitchRequest;
import com.example.ad_project_kampung_unite.entities.enums.RequestStatus;
import com.google.android.material.chip.Chip;
import com.example.ad_project_kampung_unite.entities.enums.GroupPlanStatus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArchivedGroupExpandableRecyclerViewAdapter extends RecyclerView.Adapter<ArchivedGroupExpandableRecyclerViewAdapter.ViewHolder> {

	private HitchRequestService hitchRequestService;
    private List<HitchRequest> hitchRequestsList;
    private List<List<GroceryItem>> groceryItemList;
	private GroupPlanStatus groupPlanStatus;
    List<Integer> counter = new ArrayList<>();

    Context context;

    public ArchivedGroupExpandableRecyclerViewAdapter(Context context,
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
        TextView name, pickuptime, itemcount, paymentstatus, hitchamount, hitchamountTag;
        ImageButton dropBtn;
        Button receivepaymentBtn;
        RecyclerView cardRecyclerView;
        CardView cardView;
        Chip mrequeststatus;

        public ViewHolder(View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.groceryDetailListTitle);
            pickuptime = itemView.findViewById(R.id.groceryDetailPickupTime);
            itemcount=itemView.findViewById(R.id.groceryDetailItemQuantitySum);
            hitchamount=itemView.findViewById(R.id.groceryDetailHitcherTotal);
            hitchamountTag = itemView.findViewById(R.id.groceryDetailHitcherTotal_tag);
            paymentstatus = itemView.findViewById(R.id.payment_status);
            dropBtn = itemView.findViewById(R.id.categoryExpandBtn);
            receivepaymentBtn = itemView.findViewById(R.id.receive_payment_btn);
            cardRecyclerView = itemView.findViewById(R.id.innerRecyclerView);
            cardView = itemView.findViewById(R.id.cardView);
            mrequeststatus = itemView.findViewById(R.id.group_details_status);
        }
    }
    // inflate item row layout and returning the holder
    @Override
    public ArchivedGroupExpandableRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_details_collapse_items, parent, false);

        ArchivedGroupExpandableRecyclerViewAdapter.ViewHolder vh = new ArchivedGroupExpandableRecyclerViewAdapter.ViewHolder(v);

        return vh;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ArchivedGroupExpandableRecyclerViewAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        HitchRequest hitchRequest = hitchRequestsList.get(position);
        List<GroceryItem> groceryItemsCount = groceryItemList.get(position);
        Double groceryItemsTotal = 0.0;
        for(int i = 0; i<groceryItemsCount.size();i++){
            groceryItemsTotal = groceryItemsTotal + groceryItemsCount.get(i).getSubtotal();
        }

        Double hitcherAmount = 0.0;
        for(int i= 0; i<groceryItemsCount.size(); i++){
            hitcherAmount = hitcherAmount + groceryItemsCount.get(i).getSubtotal();
        }
        hitcherAmount = hitcherAmount*1.07*1.05;
        hitcherAmount = Math.round(hitcherAmount*100.0)/100.0;

        TextView name = holder.name;
        TextView hitchamount = holder.hitchamount;
        TextView hitchamounttag = holder.hitchamountTag;
        TextView pickup = holder.pickuptime;
        TextView itemcount = holder.itemcount;
        Chip requestStatusChip = holder.mrequeststatus;
        ImageButton dropBtn = holder.dropBtn;

        name.setText(groceryItemList.get(position).get(0).getGroceryList().getName());
        hitchamount.setVisibility(View.GONE);
        hitchamounttag.setVisibility(View.GONE);
        pickup.setVisibility(View.GONE);
        itemcount.setText("Items: " + Integer.toString(groceryItemsCount.size()));

        requestStatusChip.setVisibility(View.GONE);

        if (groupPlanStatus == GroupPlanStatus.SHOPPINGCOMPLETED) {
            dropBtn.setVisibility(View.INVISIBLE);
            hitchamount.setVisibility(View.VISIBLE);
            hitchamounttag.setVisibility(View.VISIBLE);
            hitchamount.setText("Total: $" + Double.toString(hitcherAmount));

            if (hitchRequest.isBuyerConfirmTransaction()) {
                if (hitchRequest.isHitcherConfirmTransaction()) {
                    holder.paymentstatus.setText(R.string.transaction_complete);
                    holder.paymentstatus.setBackground(context.getResources().getDrawable(R.drawable.rounded_bg_green, null));
                } else {
                    holder.paymentstatus.setText(R.string.pending_payment);
                    holder.paymentstatus.setBackground(context.getResources().getDrawable(R.drawable.rounded_bg_yellow, null));
                }
                holder.receivepaymentBtn.setVisibility(View.GONE);
            } else {
                holder.paymentstatus.setVisibility(View.GONE);
            }
        } else {
            holder.receivepaymentBtn.setVisibility(View.GONE);
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

        holder.receivepaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadConfirmDialog(hitchRequest, holder);
            }
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return hitchRequestsList.size();
    }

    private void loadConfirmDialog(HitchRequest hitchRequest, ViewHolder holder) {
        // Create dialog to confirm close requests
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(R.string.receive_payment_confirm_msg);

        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //update buyer's confirm transaction
                hitchRequest.setBuyerConfirmTransaction(true);
                updateConrirmTransaction(hitchRequest, holder);
            }
        });

        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

    private void updateConrirmTransaction(HitchRequest hitchRequest, ViewHolder holder) {
        hitchRequestService = RetrofitClient.createService(HitchRequestService.class);
        //update buyer's confirm transaction
        Call<HitchRequest> call = hitchRequestService.updateHitchRequest(hitchRequest);
        call.enqueue(new Callback<HitchRequest>() {
            @Override
            public void onResponse(Call<HitchRequest> call, Response<HitchRequest> response) {
                if (response.isSuccessful()) {
                    HitchRequest updatedHR = response.body();

                    //change transaction status on UI
                    if (updatedHR.isHitcherConfirmTransaction()) {
                        holder.paymentstatus.setText(R.string.transaction_complete);
                        holder.paymentstatus.setBackground(context.getResources().getDrawable(R.drawable.rounded_bg_green, null));
                    } else {
                        holder.paymentstatus.setText(R.string.pending_payment);
                        holder.paymentstatus.setBackground(context.getResources().getDrawable(R.drawable.rounded_bg_yellow, null));
                    }
                    holder.receivepaymentBtn.setVisibility(View.GONE);
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
