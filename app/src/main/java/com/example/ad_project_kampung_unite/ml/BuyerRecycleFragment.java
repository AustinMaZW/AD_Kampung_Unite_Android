package com.example.ad_project_kampung_unite.ml;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;


import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.data.remote.GroupPlanService;
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
import com.example.ad_project_kampung_unite.entities.GroceryList;
import com.example.ad_project_kampung_unite.entities.GroupPlan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class BuyerRecycleFragment extends Fragment {
    //machine learning result, passed by BuyerListActivity
    private Recommendation recommendation;
    //because recommendation contains plan id list, can directly use it to deliver the plan id, a little bit unnecessary
    public void setRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
        this.planIds = recommendation.getPlandIds();
    }
    //get plans by using the plan id list
    private List<GroupPlan> plans;
    public void setPlans(List<GroupPlan> plans){
        this.plans = plans;
    }
    private List<Integer> planIds;
    public void setPlanId(List<Integer> planId){
        this.planIds = planId;
    }
    //current hitcher detail id
    private int hitcherDetailId;
    public void setHitcherDetailId(int hitcherDetailId) {
        this.hitcherDetailId = hitcherDetailId;
    }
    //current grocery list
    private GroceryList gList;
    public void setgList(GroceryList gList) {
        this.gList = gList;
    }
    //the area for showing the list of plans
    private RecyclerView recyclerView;
    private BuyerListAdapter myAdapter;
    private GroupPlanService p;
    //get plans for inflate the recycler view
    @Override
    public void onStart() {
        super.onStart();
        queryPlans();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(root == null){
            root  = inflater.inflate(R.layout.fragment_buyer_recycle, container, false);
        }
        return root;
    }

    //build recycler view, but need to get the pick up time slot first, and send all things to the adapter. time slots result current is string type, later will parse to Datetime format
    public void buildRecyclerView(View layoutRoot){
        Call<Map<Integer,List<String>>> getSlots = p.getSlotsByPlanIds(planIds);
        getSlots.enqueue(new Callback<Map<Integer, List<String>>>() {
            @Override
            public void onResponse(Call<Map<Integer, List<String>>> call, Response<Map<Integer, List<String>>> response) {
                Map<Integer,List<String>> slots = response.body();
                recyclerView = layoutRoot.findViewById(R.id.buerlistrv);
                LinearLayoutManager linear = new LinearLayoutManager(layoutRoot.getContext());
                recyclerView.setLayoutManager(linear);
                myAdapter = new BuyerListAdapter(plans,layoutRoot.getContext(),planIds,hitcherDetailId,recommendation,slots);
                recyclerView.setAdapter(myAdapter);
            }
            @Override
            public void onFailure(Call<Map<Integer, List<String>>> call, Throwable t) {
                //if cannot the request fail, show toast message
                Toast.makeText(getContext(),"Network Not Available",Toast.LENGTH_SHORT).show();
            }
        });
    }
    //get the group plans in the onStart status
    public void queryPlans() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                p = RetrofitClient.createService(GroupPlanService.class);
                Call<List<GroupPlan>> call = p.getPlans(planIds);
                call.enqueue(new Callback<List<GroupPlan>>() {
                    //when request is successful, call back this
                    @Override
                    public void onResponse(Call<List<GroupPlan>> call, Response<List<GroupPlan>> response) {
                        plans = response.body();
                        buildRecyclerView(root);
                    }
                    //when request is fail, call back this
                    @Override
                    public void onFailure(Call<List<GroupPlan>> call, Throwable t) {
                        Toast.makeText(getContext(),"Network Not Available",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }
}