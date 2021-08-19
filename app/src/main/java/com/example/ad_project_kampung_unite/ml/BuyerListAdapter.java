package com.example.ad_project_kampung_unite.ml;

import static android.provider.Settings.System.getString;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.data.remote.GroupPlanService;
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
import com.example.ad_project_kampung_unite.entities.GroupPlan;
import com.example.ad_project_kampung_unite.entities.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyerListAdapter extends RecyclerView.Adapter<BuyerListAdapter.MyViewHolder> {

    private List<GroupPlan> plans;
    private Context context;

    public BuyerListAdapter(List<GroupPlan> plans, Context context) {
        this.plans = plans;
        this.context = context;
    }

    private View view;
    private View root;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = View.inflate(context, R.layout.recycle_view_buyer_item, null);
        root = View.inflate(context, R.layout.buyer_list_item_pop, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.buyerName.setText(plans.get(position).getStoreName());
        holder.pickUpDate.setText(plans.get(position).getPickupDate().toString());
        holder.timeSlot.setText(plans.get(position).getPickupDate().toString());
        holder.location.setText(plans.get(position).getPickupAddress());
        holder.sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryPronductsInplan(19, v);

            }
        });
    }

    public void queryPronductsInplan(int planId, View v) {
        GroupPlanService p = RetrofitClient.createService(GroupPlanService.class);
        Call<List<Product>> call = p.getProductsByPlanId(planId);
        call.enqueue(new Callback<List<Product>>() {
            //when request is successful, call back this
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> pList = response.body();
                pList.stream().forEach(x -> System.out.println(x.getProductId()));
//                View view = LayoutInflater.from(v.getContext()).inflate(R.layout.buyer_list_item_pop_item,null);
                View view = View.inflate(context, R.layout.buyer_list_item_pop, null);
                PopupWindow popupWindow = popMaker(view, pList);
                popupWindow_ = popupWindow;
//                popupWindow.showAsDropDown(view);
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
            }

            //when request is fail, call back this
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                System.out.println("fail_2");
            }
        });
    }

    public void buildRecyclerView(View layoutRoot, List<Product> pList) {
        RecyclerView recyclerView = layoutRoot.findViewById(R.id.poprv);
        LinearLayoutManager linear = new LinearLayoutManager(layoutRoot.getContext());
        recyclerView.setLayoutManager(linear);
        ProductListAdapter myAdapter = new ProductListAdapter(pList, layoutRoot.getContext());
        recyclerView.setAdapter(myAdapter);

    }
    private PopupWindow popupWindow_;
    private PopupWindow popMaker(View layoutRoot, List<Product> pList) {
        View popView = LayoutInflater.from(context).inflate(R.layout.buyer_list_item_pop, null, false);
        buildRecyclerView(popView, pList);
        Button cancel = popView.findViewById(R.id.cancelPopBtn);
        Button ok = popView.findViewById(R.id.goToSlot);

        PopupWindow popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, 1000, true);
//                popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.arrow_back));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("cancel", "cancel");
                popupWindow.dismiss();

            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRadioDialog();
                Log.e("ok", "ok");
            }
        });
        return popupWindow;
    }

    private void showRadioDialog() {
        final String radioItems[] = new String[]{"radioItem1", "radioItem1", "radioItem1", "radioItem1"};
        AlertDialog.Builder radioDialog = new AlertDialog.Builder(context);
        radioDialog.setTitle("Time Slots");
        radioDialog.setIcon(R.mipmap.ic_launcher_round);
        radioDialog.setSingleChoiceItems(radioItems, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, radioItems[which], Toast.LENGTH_SHORT).show();
            }
        });
        //设置按钮
        radioDialog.setPositiveButton("Ok"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        popupWindow_.dismiss();
                    }
                }).setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        radioDialog.create().show();
    }


    @Override
    public int getItemCount() {
        if (plans == null) {
            return 0;
        }
        return plans.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView buyerName, pickUpDate, timeSlot, location;
        private Button sendRequest;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            buyerName = itemView.findViewById(R.id.buyer_Name);
            pickUpDate = itemView.findViewById(R.id.pick_Up_Date);
            timeSlot = itemView.findViewById(R.id.time_Slot);
            location = itemView.findViewById(R.id._location_);
            sendRequest = itemView.findViewById(R.id.sendRequestPlan);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (monRecyclerItemClickListener != null) {
                        int position = getLayoutPosition();
                        if (position != RecyclerView.NO_POSITION)
                            monRecyclerItemClickListener.onRecyclerItemClick(v, position);
                    }
                }
            });
        }

    }

    private onRecyclerItemClickListener monRecyclerItemClickListener;

    public void setRecyclerItemClickListener(onRecyclerItemClickListener listener) {
        monRecyclerItemClickListener = listener;
    }

    public interface onRecyclerItemClickListener {
        void onRecyclerItemClick(View view, int position);
    }
}
