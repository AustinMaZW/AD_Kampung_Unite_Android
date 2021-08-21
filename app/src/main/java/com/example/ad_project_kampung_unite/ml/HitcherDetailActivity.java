package com.example.ad_project_kampung_unite.ml;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.data.remote.GroupPlanService;
import com.example.ad_project_kampung_unite.data.remote.HitcherDetailService;
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
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

public class HitcherDetailActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String MLBASEURL = "http://10.0.2.2:5000";
    private DrawerLayout drawer;
    private Recommendation recommendation;
    private Toolbar toolbar;
    private EditText pickUpDate,location,timeSlot;
    private Button submitBtn;
    private HitcherDetailService hds;
    private int id = -1;
    private List<Integer> planIds;
    private  Intent intent_buyerList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hitcher_detail);
        Log.e("Hitcher Detail","yes_4");
        intent_buyerList = new Intent(this,BuyerListActivity.class);
//        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar_hdmenu);
//        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(this);
        pickUpDate = findViewById(R.id.pick_up_date);
        location = findViewById(R.id.locationAd);
        timeSlot = findViewById(R.id.timeSlot_);
        submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(this);
        Log.e("Hitcher Detail","yes_5");
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.submitBtn){
            saveHitcherDetail();
        }else if(id == R.id.toolbar_hdmenu){
            Log.e("Toolbar","Clicked");
        }else {

        }
    }

    private void saveHitcherDetail(){
        LocalDate pickDate = LocalDate.parse(pickUpDate.getText().toString());
        LocalTime pickTime = LocalTime.parse(timeSlot.getText().toString());
        LocalDateTime pickUpDate = LocalDateTime.of(pickDate,pickTime);
        String address = location.getText().toString();
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
        Call<Integer> create = hds.saveHitcherDetail(date,time,address);
        create.enqueue(new Callback<Integer>() {
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
                Log.e("Request","Fail in Request to Create Hitcher Detail");
            }
        });
    }
    private List<Integer> getRecommendList(int id){
        List<Integer> ids = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(MLBASEURL) //设置网络请求的Url地址
                        .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                        .build();
                //create request interface object
                GroupPlanService request = retrofit.create(GroupPlanService.class);
                Call<Recommendation> getRecommand = request.getRecommendId(id);
                getRecommand.enqueue(new Callback<Recommendation>() {
                    //when request is successful, call back this
                    @Override
                    public void onResponse(Call<Recommendation> call, Response<Recommendation> response) {
                        recommendation = response.body();
                        intent_buyerList.putExtra("recommendation",recommendation);
                        System.out.println("Successful!!!!");
                        startActivity(intent_buyerList);

                    }
                    //when request is fail, call back this
                    @Override
                    public void onFailure(Call<Recommendation> call, Throwable t) {
                        System.out.println("fail_1");
                    }
                });
            }
        }).start();
        return ids;
    }
    private void checkEditText(){

    }
    private void showMenu(){

    }
}