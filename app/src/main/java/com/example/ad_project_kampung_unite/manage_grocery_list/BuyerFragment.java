package com.example.ad_project_kampung_unite.manage_grocery_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.ad_project_kampung_unite.GroupsFragment;
import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.data.remote.GroceryListService;
import com.example.ad_project_kampung_unite.data.remote.GroupPlanService;
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
import com.example.ad_project_kampung_unite.entities.GroceryList;
import com.example.ad_project_kampung_unite.entities.GroupPlan;
import com.example.ad_project_kampung_unite.entities.HitcherDetail;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyerFragment extends Fragment {

    private View layoutRoot;
    private EditText groupName, storeName, purchaseDate, purchaseTime, pickupDate, address, pickupTime1, pickupTime2, pickupTime3;
    private Button submitBtn;

    private GroupPlanService groupPlanService;
    private GroceryListService groceryListService;

    private GroceryList groceryList;
    private GroupPlan groupPlan;

    public BuyerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        layoutRoot = inflater.inflate(R.layout.fragment_buyer_detail, container, false);

        // Set title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Buyer Details");

        groupName = layoutRoot.findViewById(R.id.plan_name);
        storeName = layoutRoot.findViewById(R.id.store_name);
        purchaseDate = layoutRoot.findViewById(R.id.purchase_date);
        purchaseTime = layoutRoot.findViewById(R.id.purchase_time);
        address = layoutRoot.findViewById(R.id.address);
        pickupDate = layoutRoot.findViewById(R.id.pickup_date);
        pickupTime1 = layoutRoot.findViewById(R.id.pickup_time_1);
        pickupTime2 = layoutRoot.findViewById(R.id.pickup_time_2);
        pickupTime3 = layoutRoot.findViewById(R.id.pickup_time_3);
        submitBtn = layoutRoot.findViewById(R.id.submitGroupDetailsBtn);

        Bundle bundle = getArguments();
        if(bundle!=null) {
            groceryList = (GroceryList) bundle.getSerializable("editToBuyerDetailKey");
            System.out.println(groceryList.getId());
        }


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGroupPlanDetails();
            }
        });

        return layoutRoot;
    }

    public void saveGroupPlanDetails() {
        DateTimeFormatter df_date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter df_time =DateTimeFormatter.ofPattern("HH:mm:ss");
        String gName = groupName.getText().toString();
        String sName = storeName.getText().toString();
        String pDate = LocalDate.parse(purchaseDate.getText().toString()).format(df_date);
        String puDate = LocalDate.parse(pickupDate.getText().toString()).format(df_date);
        String add = address.getText().toString().concat(", Singapore, Singapore");
        String puTime1 = LocalTime.parse(pickupTime1.getText().toString()).format(df_time);
        String puTime2 = LocalTime.parse(pickupTime2.getText().toString()).format(df_time);
        String puTime3 = LocalTime.parse(pickupTime3.getText().toString()).format(df_time);

        if(gName == "") {
            gName = "Group";
        }

        if(!gName.equals(null) && !pDate.equals(null)
                && !puDate.equals(null) && !add.isEmpty()
                && !add.equals(null) && !puTime1.equals(null)
                && !puTime2.equals(null) && !puTime3.equals(null)){
            groupPlanService = RetrofitClient.createService(GroupPlanService.class);
            Call<GroupPlan> call = groupPlanService.createGroupPlan(gName, sName, pDate, add, puDate, puTime1, puTime2, puTime3);
            call.enqueue(new Callback<GroupPlan>() {
                @Override
                public void onResponse(Call<GroupPlan> call, Response<GroupPlan> response) {
                    groupPlan = response.body();
                    System.out.println("succesfully created group plan" + groupPlan.getId());
                    updateBuyerRoleAndGroceryList();
                }

                @Override
                public void onFailure(Call<GroupPlan> call, Throwable t) {
                    System.out.println("FAILURE");
                }
            });

        }
    }

    public void updateBuyerRoleAndGroceryList() {
        groceryListService = RetrofitClient.createService(GroceryListService.class);
        Call<GroceryList> call = groceryListService.updateBuyerRoleById(groceryList.getId(), groupPlan.getId());
        call.enqueue(new Callback<GroceryList>() {
            @Override
            public void onResponse(Call<GroceryList> call, Response<GroceryList> response) {
                groceryList = response.body();
                System.out.println("Role: " + groceryList.getRole());
                System.out.println("Group Plan Id: " + groceryList.getGroupPlanGL().getId());

                FragmentManager fragmentManager = getParentFragmentManager();
                GroupsFragment groupsFragment = new GroupsFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container,groupsFragment)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onFailure(Call<GroceryList> call, Throwable t) {

            }
        });


    }
}
