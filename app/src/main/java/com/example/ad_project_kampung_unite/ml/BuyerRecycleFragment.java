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


import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.data.remote.GroupPlanService;
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
import com.example.ad_project_kampung_unite.entities.GroupPlan;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class BuyerRecycleFragment extends Fragment {
    private Recommendation recommendation;

    public void setRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
        this.planIds = recommendation.getPlandIds();
    }

    private List<GroupPlan> plans;
    public void setPlans(List<GroupPlan> plans){
        this.plans = plans;
    }
    private List<Integer> planIds;
    public void setPlanId(List<Integer> planId){
        this.planIds = planId;
    }
    private int hitcherDetailId;

    public void setHitcherDetailId(int hitcherDetailId) {
        this.hitcherDetailId = hitcherDetailId;
    }

    private RecyclerView recyclerView;
    private BuyerListAdapter myAdapter;
    private GroupPlanService p;

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


    public void buildRecyclerView(View layoutRoot){
        recyclerView = layoutRoot.findViewById(R.id.buerlistrv);
        LinearLayoutManager linear = new LinearLayoutManager(layoutRoot.getContext());
        recyclerView.setLayoutManager(linear);
        myAdapter = new BuyerListAdapter(plans,layoutRoot.getContext(),planIds,hitcherDetailId,recommendation);
        recyclerView.setAdapter(myAdapter);
//        myAdapter.setRecyclerItemClickListener(new BuyerListAdapter.onRecyclerItemClickListener() {
//            @Override
//            public void onRecyclerItemClick(View view, int position ) {
//
//            }
//        });
    }

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
                        System.out.println("fail_2");
                    }
                });
            }
        }).start();
    }
}