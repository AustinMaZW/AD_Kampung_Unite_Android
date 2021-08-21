package com.example.ad_project_kampung_unite.ml;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.data.remote.GroupPlanService;
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
import com.example.ad_project_kampung_unite.entities.GroupPlan;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuyerListActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String MLBASEURL = "http://10.0.2.2:5000";
    private GroupPlanService p;
    private Recommendation recommendation;
    private Button doml;
    private Toolbar tbar;
    private List<GroupPlan> plans = new ArrayList<>();
    private List<Integer> planIds = new ArrayList<>();
    private FragmentManager fm;
    private int hitcherDetailId = 37;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        p = RetrofitClient.createService(GroupPlanService.class);
        setContentView(R.layout.activity_buyer_list);
        Intent intent = getIntent();
        recommendation = (Recommendation)intent.getSerializableExtra("recommendation");
        tbar = findViewById(R.id.toolbar_allbuyers);
        tbar.setOnClickListener(this);
        doml = findViewById(R.id.doml);
//        doml.setOnClickListener(this);

        this.fm = getSupportFragmentManager();
//        BuyerRecycleFragment brv = (BuyerRecycleFragment)fm.findFragmentById(R.id.lists_byrv);
        BuyerRecycleFragment brv = new BuyerRecycleFragment();
        FragmentTransaction trans = fm.beginTransaction();
        brv.setRecommendation(recommendation);
        brv.setHitcherDetailId(hitcherDetailId);
        trans.replace(R.id.lists_byrv,brv);
//        trans.addToBackStack(null);
        trans.commit();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.doml){

            requestForRecommendList();


        }else if(id == R.id.toolbar_allbuyers){
            Intent back = new Intent(this,HitcherDetailActivity.class);
            startActivity(back);
        }
    }
    public void requestForRecommendList() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MLBASEURL) //设置网络请求的Url地址
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .build();
        //create request interface object
        GroupPlanService request = retrofit.create(GroupPlanService.class);
        Call<Recommendation> recommand = request.getRecommendId(hitcherDetailId);
        recommand.enqueue(new Callback<Recommendation>() {
            @Override
            public void onResponse(Call<Recommendation> call, Response<Recommendation> response) {
                recommendation = response.body();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BuyerRecycleFragment brv = new BuyerRecycleFragment();
                        FragmentTransaction trans = fm.beginTransaction();
                        brv.setRecommendation(recommendation);
                        brv.setHitcherDetailId(hitcherDetailId);
                        trans.replace(R.id.lists_byrv, brv);
                        trans.commit();
                    }
                });
            }

            @Override
            public void onFailure(Call<Recommendation> call, Throwable t) {
                Log.e("Machine Learning", "Fail");
            }
        });
    }
    public void queryPlans() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call<List<GroupPlan>> call = p.getPlans(planIds);
                call.enqueue(new Callback<List<GroupPlan>>() {
                    //when request is successful, call back this
                    @Override
                    public void onResponse(Call<List<GroupPlan>> call, Response<List<GroupPlan>> response) {
                        plans = response.body();
                    }
                    //when request is fail, call back this
                    @Override
                    public void onFailure(Call<List<GroupPlan>> call, Throwable t) {
                        System.out.println("fail_2");
                    }
                });
            }
        }).start();
    }
}






