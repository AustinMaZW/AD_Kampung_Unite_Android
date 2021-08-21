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
import com.example.ad_project_kampung_unite.entities.AvailableTime;
import com.example.ad_project_kampung_unite.entities.GroupPlan;
import com.example.ad_project_kampung_unite.entities.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuyerListAdapter extends RecyclerView.Adapter<BuyerListAdapter.MyViewHolder> {
    private View view;
    private View root;
    private Recommendation recommendation;
    private List<GroupPlan> plans;
    private List<Integer> planIds;
    private int hitcherDetailId;
    private Context context;
    private GroupPlanService groupPlanService;
    private List<Integer> requestIds = new ArrayList<>();

    public BuyerListAdapter(List<GroupPlan> plans, Context context,List<Integer> planIds,int hitcherDetailId,Recommendation recommendation) {
        this.plans = plans;
        this.context = context;
        this.planIds = planIds;
        this.hitcherDetailId = hitcherDetailId;
        this.recommendation = recommendation;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = View.inflate(context, R.layout.recycle_view_buyer_item, null);
        root = View.inflate(context, R.layout.buyer_list_item_pop, null);
        groupPlanService = RetrofitClient.createService(GroupPlanService.class);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DecimalFormat df = new DecimalFormat("0.00%");
        holder.buyerName.setText(String.format("%s (Similarity %s)",plans.get(position).getStoreName(),df.format(recommendation.getProduct_score().get(position))));
        holder.pickUpDate.setText(String.format("Pick Up: %tF",plans.get(position).getPickupDate()));
        List<AvailableTime> slos = plans.get(position).getAvailableTimes();
        if(slos != null){
            String slots = slos.stream().map(AvailableTime::getPickupSlots).map(LocalTime::toString).reduce((x,y) -> x.concat(String.format(" , ",y))).toString();
            holder.timeSlot.setText(slots);
        }else{
            holder.timeSlot.setText("No Available time");
        }
//        List<String> slots = slos.stream().map(AvailableTime::getPickupSlots).map(LocalTime::toString).collect(Collectors.toList());



        holder.location.setText(String.format("Address: %s (Distance: %.2f)",plans.get(position).getPickupAddress(),recommendation.getDistance().get(position)));
        holder.sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryPronductsInplan(planIds.get(position), v,position); //use id

            }
        });
    }
    @Override
    public int getItemCount() {
        if (plans == null) {
            return 0;
        }
        return plans.size();
    }

    public void queryPronductsInplan(int planId, View v,int position) {
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
                PopupWindow popupWindow = popMaker(view, pList,position);
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
        ProductListAdapter myAdapter = new ProductListAdapter(pList, layoutRoot.getContext(),planIds);
        recyclerView.setAdapter(myAdapter);

    }
    private PopupWindow popupWindow_;
    private PopupWindow popMaker(View layoutRoot, List<Product> pList,int position) {
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
                showRadioDialog(position);
                Log.e("ok", "ok");
            }
        });
        return popupWindow;
    }
    private int pos;
    private void showRadioDialog(int position) {
        final String[] radioItems = new String[]{"9:00 am - 9:30 am", "9:30 am - 10:00 am", "10:00 am - 10:30 am"};
        final LocalTime[] timeslots = new LocalTime[]{LocalTime.of(9,0),LocalTime.of(9,30),LocalTime.of(10,0)};
        AlertDialog.Builder radioDialog = new AlertDialog.Builder(context);

        radioDialog.setTitle("Time Slots");
        radioDialog.setIcon(R.mipmap.ic_launcher_round);
        radioDialog.setSingleChoiceItems(radioItems, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(context, Integer.toString(which), Toast.LENGTH_SHORT).show();
                pos = which;
            }
        });


        radioDialog.setPositiveButton("Ok"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        LocalTime timeslot = timeslots[pos];
                        LocalDateTime pickUpTime = LocalDateTime.of(plans.get(position).getPickupDate(),timeslot);
                        sendRequest(planIds.get(position),hitcherDetailId,pickUpTime);
                        dialog.dismiss();
                        popupWindow_.dismiss();
                    }
                }).setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pos = 0;
                dialog.dismiss();
            }
        });
        radioDialog.create().show();
    }

    private void sendRequest(int planId, int hitcherDetailId, LocalDateTime pickUpTime){
        int requestId = -1;
        DateTimeFormatter dfter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dd = pickUpTime.format(dfter);
        Log.e("date",pickUpTime.toString());
        Call<Integer> saveRequest = groupPlanService.saveRequest(planId,hitcherDetailId,dd);
        saveRequest.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                int idd = response.body();
//                Log.e("id",);
                if(idd > 0){
                    requestIds.add(idd);
                    Log.e("dd","succ");
                    System.out.println(idd);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("request","fail");
            }
        });
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
