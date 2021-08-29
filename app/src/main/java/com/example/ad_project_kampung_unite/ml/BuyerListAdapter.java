package com.example.ad_project_kampung_unite.ml;

import static android.provider.Settings.System.getString;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
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
    //the machine learning result
    private Recommendation recommendation;
    //list group plan which can match the hitcher detail and grocery list
    private List<GroupPlan> plans;
    //group plan id, a little bit unnecessary
    private List<Integer> planIds;
    private int hitcherDetailId;
    private Context context;
    //retrofit interface
    private GroupPlanService groupPlanService;
    //Request id list, originally i thought there will be someone will use it, but finally realize no one need it, but code is there i don't want to change it
    private List<Integer> requestIds = new ArrayList<>();
    //pick up time slots, the key is group plan id, value is the list of string version of date time data
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
        groupPlanService = RetrofitClient.createService(GroupPlanService.class);
        return new MyViewHolder(view);
    }
    //set recycler view
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //convert the cosine similarity(product score which are contained in Recommendation object) value to percent format
        DecimalFormat df = new DecimalFormat("0.00%");
        holder.buyerName.setText(String.format("%s (Similarity %s)",plans.get(position).getPlanName(),df.format(recommendation.getProduct_score().get(position))));
        //show the pick up date
        holder.pickUpDate.setText(String.format("Pick Up: %tF",plans.get(position).getPickupDate()));
        //get the pick up time slots string, if the time slot is empty or null, show 'No Available time'
        List<String> slots_str = this.slotsList.get(planIds.get(position));
        if(slots_str != null && slots_str.size() > 0){
            //concat the pick up time slot to one string, and show it in one line
            StringBuilder bler = new StringBuilder();
            slots_str.stream().forEach(x->bler.append(String.format("%s ",x)));
            holder.timeSlot.setText(bler.toString());
        }else{
            holder.timeSlot.setText("No Available time");
        }
        holder.location.setText(String.format("Address: %s (Distance: %.2f)",plans.get(position).getPickupAddress(),recommendation.getDistance().get(position)));
        //set join button listener, click on this button will send request to get list of products in this plan, and load the pop up window to show the list of products
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
    //when user click on join button and show the pop up window, need to make background become dark, then user can focus on the pop up window
    private void backgroudAlpha(float f,Activity myActivity){
        WindowManager.LayoutParams lp =myActivity.getWindow().getAttributes();
        lp.alpha=f;
        myActivity.getWindow().setAttributes(lp);
    }
    //because only activity can use the getWindow() method, so i need to get the activity object by using current context at first, this method quite useful
    private Activity getActivity(Context context) {
        if (context == null) return null;
        if (context instanceof Activity) return (Activity) context;
        if (context instanceof ContextWrapper) return getActivity(((ContextWrapper)context).getBaseContext());
        return null;
    }
    //find all products of each group plan by using plan id. if successfully get the products, it will load pop up window to show products
    //this method will be called after user click the join button
    public void queryPronductsInplan(int planId, View v,int position) {
        GroupPlanService p = RetrofitClient.createService(GroupPlanService.class);
        Call<List<Product>> call = p.getProductsByPlanId(planId);
        call.enqueue(new Callback<List<Product>>() {
            //when request is successful, call back this
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> pList = response.body();
                //print the product id for testing
                pList.stream().forEach(x -> System.out.println(x.getProductId()));
                //get pop up window view
                View view = View.inflate(context, R.layout.buyer_list_item_pop, null);
                //get pop up window object after inflate every elements
                PopupWindow popupWindow = popMaker(view, pList,position);
                //this is for instanciate the property, if i want to use this property in some method which cannot use the local variables, it will help me to load the pop up window
                //but, finally i realize i didn't have that situation in this part, but other part will have.
                popupWindow_ = popupWindow;
                //show the pop up window in the center
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                backgroudAlpha(0.4f,getActivity(context));
            }
            //when request is fail, call back this
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                //if the request if faill, print message to know where is the problem happened
                System.out.println("Query plan fail");
            }
        });
    }
    //set the recycler view for showing the list of product, this method will be called after user click on the join button and the products request success
    public void buildRecyclerView(View layoutRoot, List<Product> pList) {
        RecyclerView recyclerView = layoutRoot.findViewById(R.id.poprv);
        LinearLayoutManager linear = new LinearLayoutManager(layoutRoot.getContext());
        recyclerView.setLayoutManager(linear);
        ProductListAdapter myAdapter = new ProductListAdapter(pList, layoutRoot.getContext(),planIds);
        recyclerView.setAdapter(myAdapter);
    }
    private PopupWindow popupWindow_;
    //set the pop up window to show the products of the chosen group plan
    private PopupWindow popMaker(View layoutRoot, List<Product> pList,int position) {
        View popView = LayoutInflater.from(context).inflate(R.layout.buyer_list_item_pop, null, false);
        Button cancel = popView.findViewById(R.id.cancelPopBtn);
        Button ok = popView.findViewById(R.id.goToSlot);
        TextView title = popView.findViewById(R.id.poptitle);
        String newTitle = String.format("%s [%s]",title.getText().toString(),plans.get(position).getStoreName());
        title.setText(newTitle);
        //load the recycler view to show products
        buildRecyclerView(popView, pList);
        //instantiate the PopUpWindow object, and assigned width to wrap content, and height to 900, and click other place cannot dismiss the pop up window
        PopupWindow popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, 900, true);
        //set fade in and fade out animation
        popupWindow.setAnimationStyle(R.style.showPopupAnimation);
        //set method to be called after the pop up window is dismissed
        //mainly for make background become light(transparent)
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroudAlpha(1.0f,getActivity(context));
            }
        });
        //set cancel button to dismiss the pop up window
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("cancel", "cancel");
                popupWindow.dismiss();
            }
        });
        //set ok button to show the pick up time slots
        //the pick up time slots will show in the dialog, the data will extract from the Map object.
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSlotsByPlanId(planIds.get(position),position);
            }
        });
        return popupWindow;
    }
    private void getSlotsByPlanId(int planId,int position){
        List<String> slot = slotsList.get(planId);
        if(slot != null && slot.size()>0){
            String[] slots = new String[slot.size()];
            IntStream.range(0,slots.length).forEach(x->slots[x] = slot.get(x));
            showRadioDialog(position,slots);
        }else{
            showRadioDialog(position,null);
        }
    }
    //the position of user chosen in the dialog(actuall it is from dialog itself, unnecessary to make it become property, i assumed i will use it)
    private int pos;
    //two situation
    //one is the plan has pick up time slots, then show the time slot of the plan
    //if group plan doesn't have time slots, show the default time slots
    private void showRadioDialog(int position,String[] pSlots){
        if(pSlots != null && pSlots.length >0){
            showSelectionDialog(position,pSlots);
        }else{
            final String[] timeslots = new String[]{LocalTime.of(9,0).toString(),LocalTime.of(12,0).toString(),LocalTime.of(15,0).toString()};
            showSelectionDialog(position,timeslots);
        }
    }
    //show time slots selection in dialog
    private void showSelectionDialog(int position,String[] radioItems){
        AlertDialog.Builder radioDialog = new AlertDialog.Builder(context);
        System.out.println(plans.get(position).getStoreName());
        radioDialog.setTitle("Time Slots");
        radioDialog.setIcon(R.drawable.logo_small);
        //set single selection
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
                        //send request to save the Hitcher Request, in the method will convert the pick up date and time slot to pip up date time(combine together)
                        sendRequest(planIds.get(position),hitcherDetailId,pickUpDateTime);
                        //after click on ok button, dismiss dialog and pop up window
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
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String pickUpTime_str = df.format(pickUpTime);
        Call<Integer> saveRequest = groupPlanService.saveRequest(planId,hitcherDetailId,pickUpTime_str);
        saveRequest.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                int idd = response.body();
                if(idd > 0){

                    requestIds.add(idd);
                    //make background light (transparent)
                    backgroudAlpha(1.0f,getActivity(context));
                    //Remove the item which is already sent request, because if there are multiple group plan, i need to allow user to do multiple selection
                    //so i need to remove the invalid group plan in the group plan recycler view, to avoid use to make the same request
                    recommendation.removeByPlansId(planId);
                    //if there are still some valid group plans, refresh this activity to remove the invalid plans
                    if(recommendation.checkLengthOfPlanIds()){
                        goOthers(getActivity(context),null);
                    }else{
                        //if there is no any valid group plan in recommendation object, go back to main activity
                        Intent goTo = new Intent(getActivity(context),MainActivity.class);
                        goOthers(getActivity(context),goTo);
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("request","fail");
            }
        });
    }
    //this is the method to do the refresh and go to other activity
    private void goOthers(Activity activity,Intent intent){
        //alert the request is sent
        Toast.makeText(context,"Request Sent!",Toast.LENGTH_SHORT).show();
        //make the background become light(transparent)
        backgroudAlpha(1.0f,getActivity(context));
        //over the current activity, to avoid user to click on back button to see the previous item, because there will be some bugs if user can see it
        activity.finish();
        //set animation
        activity.overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
        if(intent == null){
            //if no intent in this method, refresh current activity
            activity.startActivity(activity.getIntent());
        }else {
            //if there is a intent in this method, go to other activity
            activity.startActivity(intent);
        }
        //set animation
        activity.overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
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
                    //for setting click event
                    if (monRecyclerItemClickListener != null) {
                        int position = getLayoutPosition();
                        if (position != RecyclerView.NO_POSITION)
                            monRecyclerItemClickListener.onRecyclerItemClick(v, position);
                    }
                }
            });
        }
    }
    //communication interface
    private onRecyclerItemClickListener monRecyclerItemClickListener;

    public void setRecyclerItemClickListener(onRecyclerItemClickListener listener) {
        monRecyclerItemClickListener = listener;
    }

    public interface onRecyclerItemClickListener {
        void onRecyclerItemClick(View view, int position);
    }

}
