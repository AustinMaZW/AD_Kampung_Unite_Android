package com.example.ad_project_kampung_unite.ml;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HitcherDetailActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String MLBASEURL = "http://10.0.2.2:5000";
    private Toolbar toolbar;
    private EditText pickUpDate,location,timeSlot;
    private Button submitBtn;
    private HitcherDetailService hds;
    private int id = -1;
    private List<Integer> planIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hitcher_detail);
        toolbar = findViewById(R.id.toolbar_hdmenu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Toolbar","Clicked");
            }
        });
        pickUpDate = findViewById(R.id.pick_up_date);
        location = findViewById(R.id.locationAd);
        timeSlot = findViewById(R.id.timeSlot_);
        submitBtn = findViewById(R.id.submitBtn);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.submitBtn){
            saveHitcherDetail();
        }else if(id == R.id.toolbar_hdmenu){

        }else {

        }
    }

    private void saveHitcherDetail(){
        LocalDate pickDate = LocalDate.parse(pickUpDate.getText().toString());
        LocalTime pickTime = LocalTime.parse(timeSlot.getText().toString());
        String address = location.getText().toString();
        if(!pickDate.equals(null) && !pickTime.equals(null) && !address.isEmpty()&&!address.equals(null)){
            HitcherDetail hd = new HitcherDetail(pickDate,pickTime,address);
            sendRequest(hd);
        }
    }
    private void sendRequest(HitcherDetail hd){
        hds = RetrofitClient.createService(HitcherDetailService.class);
        id = -1;
        Call<Integer> create = hds.saveHitcherDetail(hd);
        try{
            Response<Integer> respons = create.execute();
            id = respons.body();
        }catch (Exception e){
            Log.e("Request","Fail in Request to Create Hitcher Detail");
        }
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
                Call<List<Integer>> getRecommand = request.getRecommendId(id);
                try{
                    Response<List<Integer>> respons = getRecommand.execute();
                    planIds = respons.body();
                }catch (Exception e){

                }
//                call.enqueue(new Callback<List<Integer>>() {
//                    //when request is successful, call back this
//                    @Override
//                    public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
//                        planIds = response.body();
//                    }
//                    //when request is fail, call back this
//                    @Override
//                    public void onFailure(Call<List<Integer>> call, Throwable t) {
//                        System.out.println("fail_1");
//                    }
//                });
            }
        }).start();
        return ids;
    }
    private void checkEditText(){

    }
    private void showMenu(){

    }
}