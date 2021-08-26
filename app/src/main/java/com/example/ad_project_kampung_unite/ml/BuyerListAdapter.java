package com.example.ad_project_kampung_unite.ml;

import static android.provider.Settings.System.getString;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.MainActivity;
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
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private Map<Integer,List<String>> slotsList;


    public BuyerListAdapter(List<GroupPlan> plans, Context context,List<Integer> planIds,int hitcherDetailId,Recommendation recommendation,Map<Integer,List<String>> slotsList) {
        this.plans = plans;
        this.context = context;
        this.planIds = planIds;
        this.hitcherDetailId = hitcherDetailId;
        this.recommendation = recommendation;
        this.slotsList = slotsList;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = View.inflate(context, R.layout.recycle_view_buyer_item, null);
        root = View.inflate(context, R.layout.buyer_list_item_pop, null);
        groupPlanService = RetrofitClient.createService(GroupPlanService.class);
        return new MyViewHolder(view);
    }
    public void createView(MyViewHolder holder,int position,String slos){
        DecimalFormat df = new DecimalFormat("0.00%");
        holder.buyerName.setText(String.format("%s (Similarity %s)",plans.get(position).getStoreName(),df.format(recommendation.getProduct_score().get(position))));
        holder.pickUpDate.setText(String.format("Pick Up: %tF",plans.get(position).getPickupDate()));
        List<String> slots_str = this.slotsList.get(planIds.get(position));
        System.out.println("get the slots by map");
        if(slots_str != null && slots_str.size() > 0){
            System.out.println(slots_str.get(0));
            String slot = slots_str.stream().reduce((x,y)->x.concat(String.format(" %s",y))).toString();
            holder.timeSlot.setText(slot);
        }else{
            holder.timeSlot.setText("No Available time");
        }

        System.out.print("shit");

        holder.location.setText(String.format("Address: %s (Distance: %.2f)",plans.get(position).getPickupAddress(),recommendation.getDistance().get(position)));
        holder.sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryPronductsInplan(planIds.get(position), v,position); //use id
            }
        });
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        DecimalFormat df = new DecimalFormat("0.00%");
        holder.buyerName.setText(String.format("%s (Similarity %s)",plans.get(position).getStoreName(),df.format(recommendation.getProduct_score().get(position))));
        holder.pickUpDate.setText(String.format("Pick Up: %tF",plans.get(position).getPickupDate()));
        List<String> slots_str = this.slotsList.get(planIds.get(position));
        if(slots_str != null && slots_str.size() > 0){
            StringBuilder bler = new StringBuilder();
            slots_str.stream().forEach(x->bler.append(String.format("%s ",x)));
            String slot = bler.toString();
            holder.timeSlot.setText(slot);
        }else{
            holder.timeSlot.setText("No Available time");
        }

        System.out.print("shit");

        holder.location.setText(String.format("Address: %s (Distance: %.2f)",plans.get(position).getPickupAddress(),recommendation.getDistance().get(position)));
        holder.sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryPronductsInplan(planIds.get(position), v,position); //use id

            }
        });



//        Call<List<String>> getSlots = groupPlanService.getSlots(planIds.get(position));
//        getSlots.enqueue(new Callback<List<String>>() {
//            @Override
//            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
//                List<String> sls = response.body();
//                if(sls != null && sls.size() > 0){
//                    String strSlots = sls.stream().reduce((x,y)->x.concat(String.format(" , ",y))).toString();
//                    createView(holder,position,strSlots);
//                }else{
//                    createView(holder,position,null);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<String>> call, Throwable t) {
//                createView(holder,position,null);
//            }
//        });
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
                View view = View.inflate(context, R.layout.buyer_list_item_pop, null);
                PopupWindow popupWindow = popMaker(view, pList,position);
                popupWindow_ = popupWindow;
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

        PopupWindow popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
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
                getSlotsByPlanId(planIds.get(position),position);
                Log.e("ok", "ok");
            }
        });
        return popupWindow;
    }
    private void getSlotsByPlanId(int planId,int position){
        Call<List<String>> getSlots = groupPlanService.getSlots(planId);

        getSlots.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                List<String> slotList = response.body();
                if(slotList != null && slotList.size()>0){
                    String[] slots = new String[slotList.size()];
                    IntStream.range(0,slots.length).forEach(x->slots[x] = slotList.get(x));
                    showRadioDialog(position,slots);
                }else{
                    showRadioDialog(position,null);
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                showRadioDialog(position,null);
            }
        });
    }
    private int pos;
    private void showRadioDialog(int position,String[] pSlots){
        if(pSlots != null && pSlots.length >0){
            showSelectionDialog(position,pSlots);
        }else{
            final String[] timeslots = new String[]{LocalTime.of(9,0).toString(),LocalTime.of(9,30).toString(),LocalTime.of(10,0).toString()};
            showSelectionDialog(position,timeslots);
        }
    }
    private void showSelectionDialog(int position,String[] radioItems){
        AlertDialog.Builder radioDialog = new AlertDialog.Builder(context);
        System.out.println(plans.get(position).getStoreName());
        radioDialog.setTitle("Time Slots");
        radioDialog.setIcon(R.drawable.logo_small);
        radioDialog.setSingleChoiceItems(radioItems, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pos = which;
            }
        });
        radioDialog.setPositiveButton("Ok"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String timeslot = radioItems[pos];
                        LocalTime time = LocalTime.parse(timeslot,DateTimeFormatter.ISO_TIME);
                        LocalDateTime pickUpDateTime = LocalDateTime.of(plans.get(position).getPickupDate(),time);
                        sendRequest(planIds.get(position),hitcherDetailId,pickUpDateTime);
                        dialog.dismiss();
                        popupWindow_.dismiss();
                        Intent backToMain = new Intent(context, MainActivity.class);
                        backToMain.putExtra("hitcherDetail",false);
                        Toast.makeText(context,"Request Sent!",Toast.LENGTH_SHORT).show();
                        context.startActivity(backToMain);
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
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String pickUpTime_str = df.format(pickUpTime);
        Call<Integer> saveRequest = groupPlanService.saveRequest(planId,hitcherDetailId,pickUpTime_str);
        saveRequest.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                int idd = response.body();
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
