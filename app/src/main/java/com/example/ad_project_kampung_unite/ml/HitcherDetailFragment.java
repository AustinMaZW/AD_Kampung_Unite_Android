package com.example.ad_project_kampung_unite.ml;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.data.remote.GroupPlanService;
import com.example.ad_project_kampung_unite.data.remote.HitcherDetailService;
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
import com.example.ad_project_kampung_unite.entities.GroceryList;
import com.example.ad_project_kampung_unite.entities.HitcherDetail;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HitcherDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HitcherDetailFragment extends Fragment{
    public static final String MLBASEURL = "http://10.0.2.2:5000";

    private Recommendation recommendation;
    private EditText pickUpDate,location,timeSlot;
    private Button submitBtn;
    private HitcherDetailService hds;
    private int id = -1;
    private List<Integer> planIds;
    private Intent intent_buyerList;
    private GroceryList gList;

    public void setgList(GroceryList gList) {
        this.gList = gList;
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HitcherDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public static HitcherDetailFragment newInstance(String param1, String param2) {
        HitcherDetailFragment fragment = new HitcherDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Hitcher Details");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hitcher_detail, container, false);
        intent_buyerList = new Intent(getContext(),BuyerListActivity.class);
        pickUpDate = view.findViewById(R.id.pick_up_date);
        location = view.findViewById(R.id.locationAd);
        timeSlot = view.findViewById(R.id.timeSlot_);
        submitBtn = view.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitBtn.setEnabled(false);
                try{
                    saveHitcherDetail();
                }catch (Exception e){
                    showDialog("Warning!!!!!!!!","Invalid Hitcher Detail");
                }
            }
        });
        return view;

    }

    private void showDialog(String title,String msg){
        AlertDialog.Builder radioDialog = new AlertDialog.Builder(getContext());
        radioDialog.setTitle(title).setMessage(msg).setIcon(R.drawable.logo_small).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                submitBtn.setEnabled(true);
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    private void saveHitcherDetail(){
        LocalDate pickDate = LocalDate.parse(pickUpDate.getText().toString());
        LocalTime pickTime = LocalTime.parse(timeSlot.getText().toString());
        LocalDateTime pickUpDate = LocalDateTime.of(pickDate,pickTime);
        String address = location.getText().toString().concat(", Singapore, Singapore");
        if(!pickDate.equals(null) && !pickTime.equals(null) && !address.isEmpty()&&!address.equals(null)){
            HitcherDetail hd = new HitcherDetail(pickUpDate,address);
            sendRequest(pickDate,pickTime,address);
        }
    }
    private void sendRequest(LocalDate pickUpDate, LocalTime pickUpTime, String address){
        hds = RetrofitClient.createService(HitcherDetailService.class);
        DateTimeFormatter df_date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter df_time = DateTimeFormatter.ofPattern("HH:mm:ss");
        String date = pickUpDate.format(df_date);
        String time = pickUpTime.format(df_time);
        id = -1;
        Toast.makeText(getContext(),"Please Wait",Toast.LENGTH_SHORT).show();
        Call<Integer> create_withList = hds.saveHitcherDetails(date,time,address,gList.getId());
        create_withList.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.e("Request","Successful!!!!!");
                id = response.body();
                System.out.println(id);
                if(id >= 0){
                    getRecommendList(id);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getContext(),"Please submit again!",Toast.LENGTH_SHORT).show();
                submitBtn.setEnabled(true);
            }
        });
    }
    private List<Integer> getRecommendList(int id){
        List<Integer> ids = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(MLBASEURL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                //create request interface object
                GroupPlanService request = retrofit.create(GroupPlanService.class);
                Call<Recommendation> getRecommand = request.getRecommendId(id);
                getRecommand.enqueue(new Callback<Recommendation>() {
                    //when request is successful, call back this
                    @Override
                    public void onResponse(Call<Recommendation> call, Response<Recommendation> response) {
                        recommendation = response.body();
                        if(recommendation.getPlandIds().size() > 0){
                            intent_buyerList.putExtra("recommendation",recommendation);
                            intent_buyerList.putExtra("hitcherDetailId",id);
                            intent_buyerList.putExtra("groceryList",gList);
                            System.out.println("Successful!!!!");

                            startActivity(intent_buyerList);
                            getActivity().overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                        }
                        else{
                            AlertDialog.Builder radioDialog_ = new AlertDialog.Builder(getContext());
                            radioDialog_.setTitle("Warning!!!!!!").setIcon(R.drawable.logo_small).setMessage("There is no group plan matched!!!")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    submitBtn.setEnabled(true);
                                }
                            }).show();
                        }
                    }
                    //when request is fail, call back this
                    @Override
                    public void onFailure(Call<Recommendation> call, Throwable t) {
                        System.out.println("fail_1");
                        submitBtn.setEnabled(true);
                    }
                });
            }
        }).start();
        return ids;
    }
}