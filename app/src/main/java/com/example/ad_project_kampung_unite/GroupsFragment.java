package com.example.ad_project_kampung_unite;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.adaptors.GroupPlanItemAdapter;
import com.example.ad_project_kampung_unite.data.remote.GroupPlanService;
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
import com.example.ad_project_kampung_unite.entities.GroupPlan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupsFragment extends Fragment {

    public GroupsFragment() {
        // Required empty public constructor
    }
    private Context context;

    RecyclerView rvGroupPlan;
    GroupPlanService gpService;

    List<GroupPlan> groupPlanList;

    private SharedPreferences sharedPreferences;
    private int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layoutRoot = inflater.inflate(R.layout.fragment_groups, container, false);
        context = layoutRoot.getContext();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Groups");

        // Get user id from shared prefs
        sharedPreferences = getContext().getSharedPreferences("LoginCredentials",0);
        userId = sharedPreferences.getInt("userId", -1);

        rvGroupPlan = layoutRoot.findViewById(R.id.group_plan_recyclerview);
        rvGroupPlan.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));

        gpService = RetrofitClient.createService(GroupPlanService.class);
        getGroupPlansFromServer();

        return layoutRoot;
    }
    private void getGroupPlansFromServer(){
        Call<List<GroupPlan>> call = gpService.findGroupPlansByUserDetailId(userId);

        call.enqueue(new Callback<List<GroupPlan>>() {
            @Override
            public void onResponse(Call<List<GroupPlan>> call, Response<List<GroupPlan>> response) {

                if (response.isSuccessful()) {
                    groupPlanList = response.body();
//                    Log.d("Success", String.valueOf(groupPlanList.get(0).getPlanName())); //for testing

                    GroupPlanItemAdapter groupPlanItemAdapter = new GroupPlanItemAdapter(context, (ArrayList<GroupPlan>) groupPlanList);
                    rvGroupPlan.setAdapter(groupPlanItemAdapter);  //set the adaptor here

                } else {
                    Log.e("Error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<GroupPlan>> call, Throwable t) {
                // like no internet connection / the website doesn't exist
                call.cancel();
                Log.w("Failure", "Failure!");
                t.printStackTrace();
            }
        });
    }
}