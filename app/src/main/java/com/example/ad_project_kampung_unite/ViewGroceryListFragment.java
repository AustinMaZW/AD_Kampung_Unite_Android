package com.example.ad_project_kampung_unite;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.adaptors.GroceryListItemAdaptor;
import com.example.ad_project_kampung_unite.adaptors.HitchRequestAdaptor;
import com.example.ad_project_kampung_unite.data.remote.GroceryListService;
import com.example.ad_project_kampung_unite.data.remote.GroupPlanService;
import com.example.ad_project_kampung_unite.data.remote.HitchRequestService;
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
import com.example.ad_project_kampung_unite.entities.GroceryItem;
import com.example.ad_project_kampung_unite.entities.GroceryList;
import com.example.ad_project_kampung_unite.entities.GroupPlan;
import com.example.ad_project_kampung_unite.entities.HitchRequest;
import com.example.ad_project_kampung_unite.entities.Product;
import com.example.ad_project_kampung_unite.enums.RequestStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewGroceryListFragment extends Fragment {

    private List<GroupPlan> groupPlanList;
    private List<GroceryItem> groceryItemList = new ArrayList<>();
    private List<HitchRequest> hitchRequests = new ArrayList<>();
    private HitchRequestService hitchRequestService;
    private GroceryListService groceryListService;
    private GroupPlanService groupPlanService;
    private GroupPlan approvedGroupPlan;

    private Context context;

    //views here
    private View layoutRoot;
    private RecyclerView rvHitchRequests,rvGroceryItems;
    private TextView rqStatusTitle, rqStatDescription, pickupStore, pickupLoc, pickupTime;
    private Button hitchRqButton, quitGroupBtn;

    public ViewGroceryListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layoutRoot = inflater.inflate(R.layout.fragment_view_grocery_list, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Grocery List Name");     //need grocery list name passed from previous frag

        context = layoutRoot.getContext();

        rqStatusTitle = layoutRoot.findViewById(R.id.rqStat_title);
        rqStatDescription = layoutRoot.findViewById(R.id.rqStat_description);
        pickupStore = layoutRoot.findViewById(R.id.pickup_store);
        pickupLoc = layoutRoot.findViewById(R.id.pickup_location);
        pickupTime = layoutRoot.findViewById(R.id.pickup_time);
        hitchRqButton = layoutRoot.findViewById(R.id.hitch_rq_btn);
        quitGroupBtn = layoutRoot.findViewById(R.id.quit_group);

        getGroceryItemsFromServer();            //call to retrieve groceryitems
        getHitchRequestsFromServer();           //call to retrieve requests
        setQuitGroupBtn();       //for quitGroup

        return layoutRoot;
    }


    private void getGroceryItemsFromServer(){
        groceryListService = RetrofitClient.createService(GroceryListService.class);
        Call<List<GroceryItem>> call = groceryListService.getGroceryItemByGroceryListId(30); //hard coded grocerylistid here, replace later

        call.enqueue(new Callback<List<GroceryItem>>() {
            @Override
            public void onResponse(Call<List<GroceryItem>> call, Response<List<GroceryItem>> response) {

                if (response.isSuccessful()) {
                    groceryItemList = response.body();
                    Log.d("Success", String.valueOf(groceryItemList.get(0).getProduct().getProductName())); //for testing

                    buildGroceryItemRV();

                } else {
                    Log.e("Error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<GroceryItem>> call, Throwable t) {
                // like no internet connection / the website doesn't exist
                call.cancel();
                Log.w("Failure", "Failure!");
                t.printStackTrace();
            }
        });
    }

    //below code to be completed for hitch request
    private void getHitchRequestsFromServer(){
        hitchRequestService = RetrofitClient.createService(HitchRequestService.class);
        Call<List<HitchRequest>> call = hitchRequestService.getHitchRequestsByGroceryListId(38);    //for test data 38 is pending status, 36 is approved, 249 is for quit list test
        approvedGroupPlan = null;   //reset this upon new request

        call.enqueue(new Callback<List<HitchRequest>>() {
            @Override
            public void onResponse(Call<List<HitchRequest>> call, Response<List<HitchRequest>> response) {

                if (response.isSuccessful()) {
                    hitchRequests = response.body();
                    //logic if there is an approved request, then do following
                    if(hitchRequests.size()==0){
                        rqStatusTitle.setText("No request at this time");
                        rqStatDescription.setText("Please find a group by clicking 'FIND ANOTHER GROUP'");
                    }
                    hitchRequests.stream().forEach(x->{
                        if (x.getRequestStatus() == RequestStatus.ACCEPTED){
                            updateApprovedStatUI(x);
                        }
                    });
                    if(approvedGroupPlan==null){
                        //remove views that aren't applicable to status == pending
                        layoutRoot.findViewById(R.id.status_approved).setVisibility(View.GONE);
                        buildHitchRequestRV();
                    }

                } else {
                    Log.e("Error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<HitchRequest>> call, Throwable t) {
                // like no internet connection / the website doesn't exist
                call.cancel();
                Log.w("Failure", "Failure!");
                t.printStackTrace();
            }
        });
    }

    private void quitGroupPlan(){
        groupPlanService = RetrofitClient.createService(GroupPlanService.class);
        Call<Boolean> call = groupPlanService.quitGroupPlanByGroceryListId(249); //hard coded grocerylistid here, replace later

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                if (response.isSuccessful()) {
                    Boolean result = response.body();
                    Log.d("Success", result.toString()); //for testing

                } else {
                    Log.e("Error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                // like no internet connection / the website doesn't exist
                call.cancel();
                Log.w("Failure", "Failure!");
                t.printStackTrace();
            }
        });
    }


    private void updateApprovedStatUI(HitchRequest x) {
        System.out.println("There is an accepted request, change layout");
        approvedGroupPlan = x.getGroupPlan();
        rqStatusTitle.setText("You found a buyer!");
        rqStatDescription.setVisibility(View.GONE);
        pickupStore.setText("Buyer will purchase from: " + approvedGroupPlan.getStoreName());
        pickupLoc.setText("Pick up Location: " + approvedGroupPlan.getPickupAddress());

        LocalDateTime pickupTimeFrom =  x.getPickupTimeChosen();
        LocalTime pickupTimeTo = pickupTimeFrom.plusMinutes(30).toLocalTime();
        DateTimeFormatter df1 = DateTimeFormatter.ofPattern("dd-MMM-yyyy h:mm a");
        DateTimeFormatter df2 = DateTimeFormatter.ofPattern("h:mm a");
        pickupTime.setText("Pick up from: " + pickupTimeFrom.format(df1) + " to " +
                pickupTimeTo.format(df2));

        hitchRqButton.setVisibility(View.GONE);
    }

    private void buildHitchRequestRV() {
        //recycler view for hitch requests
        rvHitchRequests = layoutRoot.findViewById(R.id.rv_hitch_rq);
        HitchRequestAdaptor hitchRequestAdaptor = new HitchRequestAdaptor(hitchRequests);
        rvHitchRequests.setAdapter(hitchRequestAdaptor);
        rvHitchRequests.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));
    }

    private void buildGroceryItemRV() {
        //recycler view for grocery items
        rvGroceryItems = layoutRoot.findViewById(R.id.rv_grocery_list);
        GroceryListItemAdaptor groceryListItemAdaptor = new GroceryListItemAdaptor(groceryItemList);
        rvGroceryItems.setAdapter(groceryListItemAdaptor);  //set the adaptor here
        rvGroceryItems.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));
    }

    private void setQuitGroupBtn() {
        quitGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Confirm Quit Group Plan?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        quitGroupPlan();

                        FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
                        ViewGroceryListFragment ViewGLFragment = new ViewGroceryListFragment();
                        fm.beginTransaction()
                                .replace(R.id.fragment_container,ViewGLFragment)        //replaces fragment with itself (refreshes)
                                .addToBackStack(null)
                                .commit();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}