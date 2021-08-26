package com.example.ad_project_kampung_unite.ml;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.example.ad_project_kampung_unite.MainActivity;
import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.data.remote.GroupPlanService;
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
import com.example.ad_project_kampung_unite.entities.GroceryList;
import com.example.ad_project_kampung_unite.entities.GroupPlan;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BuyerListActivity extends AppCompatActivity implements View.OnClickListener{
    private GroupPlanService p;
    private Recommendation recommendation;
    private Toolbar tbar;
    private FragmentManager fm;
    private int hitcherDetailId;
    private Intent back;
    private GroceryList gList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_list);

        p = RetrofitClient.createService(GroupPlanService.class);
        Intent intent = getIntent();
        hitcherDetailId = intent.getIntExtra("hitcherDetailId",0);
        recommendation = (Recommendation)intent.getSerializableExtra("recommendation");
        gList = (GroceryList) intent.getSerializableExtra("groceryList");
        tbar = findViewById(R.id.toolbar_allbuyers);
        tbar.setOnClickListener(this);
        back = new Intent(this, MainActivity.class);

        this.fm = getSupportFragmentManager();
        replaceFraments(this.fm,recommendation,hitcherDetailId,R.id.lists_byrv);

    }
    private void replaceFraments(FragmentManager fm,Recommendation recommendation,int hitcherDetailId,int id){
        BuyerRecycleFragment brv = new BuyerRecycleFragment();
        FragmentTransaction trans = fm.beginTransaction();
        brv.setRecommendation(recommendation);
        brv.setHitcherDetailId(hitcherDetailId);
        trans.replace(id,brv);
        trans.addToBackStack(null);
        trans.commit();
    }
    private void backMethod(){
        Call<Integer> rm = p.removeHitcherDetail(hitcherDetailId);
        rm.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                int _id = response.body();
                System.out.println(_id);
                back.putExtra("hitcherDetail",true);
                back.putExtra("groceryList",gList);
                startActivity(back);
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                System.out.println(hitcherDetailId);
                System.out.println("False");
            }
        });
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.toolbar_allbuyers){
            backMethod();
        }
    }
    @Override
    public void onBackPressed() {
        backMethod();
    }

}






