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
    //Retrofit interface
    private GroupPlanService p;
    //the result of machine learning, get from the intent
    private Recommendation recommendation;
    private Toolbar tbar;
    //the fragment for showing the plan list
    private FragmentManager fm;
    //the current hitcther detail id
    private int hitcherDetailId;
    //the intent for back button
    private Intent back;
    //current grocery list
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

    }

    @Override
    protected void onStart() {
        super.onStart();
        //loading the plan list by replace fragment and using recycler view
        replaceFraments(this.fm,recommendation,hitcherDetailId,R.id.lists_byrv);
    }
    //replace fragment
    public void replaceFraments(FragmentManager fm, Recommendation recommendation, int hitcherDetailId, int id){
        BuyerRecycleFragment brv = new BuyerRecycleFragment();
        FragmentTransaction trans = fm.beginTransaction();
        brv.setRecommendation(recommendation);
        brv.setHitcherDetailId(hitcherDetailId);
        trans.replace(id,brv);
        trans.addToBackStack(null);
        trans.commit();
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.toolbar_allbuyers){
            onBackPressed();
        }
    }
    //back button, let user go back to hitcher detail input page, and never come back again
    @Override
    public void onBackPressed() {
        back.putExtra("hitcherDetail",true);
        back.putExtra("groceryList",gList);
        startActivity(back);
        //set animation
        this.overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
        //over this activity to avoid user to click on back button back to this activity
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}






