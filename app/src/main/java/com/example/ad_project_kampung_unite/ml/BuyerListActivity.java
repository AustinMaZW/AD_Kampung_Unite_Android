package com.example.ad_project_kampung_unite.ml;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.data.remote.GroupPlanService;
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
import com.example.ad_project_kampung_unite.entities.GroupPlan;

import java.time.LocalDate;
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
//    private RecyclerView recyclerView;
    private TextView show;
    private Button doml,getPlans;
    private Toolbar tbar;
    private List<GroupPlan> plans = new ArrayList<>();
    private List<Integer> planIds = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        planIds.add(19);
        planIds.add(20);
        planIds.add(21);
        planIds.add(22);
        plans.add(new GroupPlan("111",LocalDate.now(),"aa",LocalDate.now()));
        plans.add(new GroupPlan("221",LocalDate.now(),"bb",LocalDate.now()));
        plans.add(new GroupPlan("331",LocalDate.now(),"cc",LocalDate.now()));
        plans.add(new GroupPlan("441",LocalDate.now(),"dd",LocalDate.now()));
        plans.add(new GroupPlan("551",LocalDate.now(),"ff",LocalDate.now()));

        p = RetrofitClient.createService(GroupPlanService.class);
//        queryPlans();
        setContentView(R.layout.activity_buyer_list);
        tbar = findViewById(R.id.toolbar_allbuyers);
        doml = findViewById(R.id.doml);
        doml.setOnClickListener(this);

        FragmentManager fm = getSupportFragmentManager();
        BuyerRecycleFragment brv = (BuyerRecycleFragment)fm.findFragmentById(R.id.lists_byrv);
//        brv.setPlans(plans);
        brv.setPlanId(planIds);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.doml){
//            requestForRecommendList();


        }else if(id == R.id.toolbar_allbuyers){
            Log.e("Back","Back to previous");
        }
    }
    public void requestForRecommendList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(MLBASEURL) //设置网络请求的Url地址
                        .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                        .build();
                //create request interface object
                GroupPlanService request = retrofit.create(GroupPlanService.class);
                Call<List<Integer>> recommand = request.getRecommendId(2);
                try{
                    Response<List<Integer>> response = recommand.execute();
                    planIds = response.body();
                    queryPlans();
                }catch (Exception e){
                    Log.e("Request Fail 1","Ml fail");
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
    }
    public void queryPlans() {
//        Call<List<GroupPlan>> call = p.getPlans(this.planIds);
//        try{
//            Response<List<GroupPlan>> response = call.execute();
//            Log.e("r","f");
//            plans = response.body();
//        }catch (Exception e){
//            Log.e("shit","shit");
//        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call<List<GroupPlan>> call = p.getPlans(planIds);
                call.enqueue(new Callback<List<GroupPlan>>() {
                    //when request is successful, call back this
                    @Override
                    public void onResponse(Call<List<GroupPlan>> call, Response<List<GroupPlan>> response) {
                        plans = response.body();
                        show.setText(plans.get(0).getPickupAddress());
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






