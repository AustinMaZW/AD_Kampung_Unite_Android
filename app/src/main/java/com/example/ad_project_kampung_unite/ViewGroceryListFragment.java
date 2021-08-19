package com.example.ad_project_kampung_unite;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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
    private GroupPlan approvedGroupPlan;

    //views here
    private View layoutRoot;
    private RecyclerView rvHitchRequests,rvGroceryItems;
    private TextView rqStatusTitle, rqStatDescription, pickupStore, pickupLoc, pickupTime;
    private Button hitchRqButton;

    public ViewGroceryListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layoutRoot = inflater.inflate(R.layout.fragment_view_grocery_list, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Grocery List Name");     //need grocery list name passed from previous frag

        rqStatusTitle = layoutRoot.findViewById(R.id.rqStat_title);
        rqStatDescription = layoutRoot.findViewById(R.id.rqStat_description);
        pickupStore = layoutRoot.findViewById(R.id.pickup_store);
        pickupLoc = layoutRoot.findViewById(R.id.pickup_location);
        pickupTime = layoutRoot.findViewById(R.id.pickup_time);
        hitchRqButton = layoutRoot.findViewById(R.id.hitch_rq_btn);

        createDummyData(); //  fake data to be replaced with http request list with database

        //call to retrieve groceryitems
        groceryListService = RetrofitClient.createService(GroceryListService.class);
        getGroceryItemsFromServer();

        //call to retrieve requests
        hitchRequestService = RetrofitClient.createService(HitchRequestService.class);
        getHitchRequestsFromServer();



        return layoutRoot;
    }

    private void getGroceryItemsFromServer(){
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

    private void buildGroceryItemRV() {
        //recycler view for grocery items
        rvGroceryItems = layoutRoot.findViewById(R.id.rv_grocery_list);
        GroceryListItemAdaptor groceryListItemAdaptor = new GroceryListItemAdaptor(groceryItemList);
        rvGroceryItems.setAdapter(groceryListItemAdaptor);  //set the adaptor here
        rvGroceryItems.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));
    }

    //below code to be completed for hitch request
    private void getHitchRequestsFromServer(){
        Call<List<HitchRequest>> call = hitchRequestService.getHitchRequestsByGroceryListId(38);    //hard coded grocerylistid here, replace later

        call.enqueue(new Callback<List<HitchRequest>>() {
            @Override
            public void onResponse(Call<List<HitchRequest>> call, Response<List<HitchRequest>> response) {

                //need to add logic to differentiate pending request and approved.
                // might first loop through to see if any is approved, if one is approved then change ui to approved
                // else then build hitchrequestRV (cause this doesn't show if there is a approved status)
                if (response.isSuccessful()) {
                    hitchRequests = response.body();

                    //logic if there is an approved request, then do follow
                    hitchRequests.stream().forEach(x->{
                        if (x.getRequestStatus() == RequestStatus.ACCEPTED){
                            updateApprovedStatUI(x);
                        }
                    });
                    if(approvedGroupPlan==null){
                        //add condition if request is pending then below are 'invisible
                        layoutRoot.findViewById(R.id.status_approved).setVisibility(View.GONE);
                        buildHitchRequestRV();
                    }

                    Log.d("Success", String.valueOf(hitchRequests.get(0))); //continue here....

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
        rvHitchRequests.setAdapter(hitchRequestAdaptor);    //move this and above line to http request method when added
        rvHitchRequests.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));
    }

    //below code for initial UI testing, to be replaced with http call
    private void createDummyData(){

        groupPlanList = new ArrayList<>();

        LocalDate d1 = LocalDate.of(2021, 8, 1);
        LocalDate d2 = LocalDate.of(2021, 8, 15);

//        groupPlanList.add(new GroupPlan(1, "Giant",d1,"123 Canada St",d2));
//        groupPlanList.add(new GroupPlan(2, "7-Eleven",d1,"321 MapleSyrup St",d2));
//        groupPlanList.add(new GroupPlan(3, "FairPrice",d1,"Lalala St",d2));

//        groceryItemList = new ArrayList<>();
//
//        Product p1 = new Product(1627923825,"Old Town 3IN1 Sugar Cane White Coffee", "15 x 36 g","Beverages","https://ssecomm.s3-ap-southeast-1.amazonaws.com/products/md/WPVi3KzqNyNsW7uGdcTMQqZmyiC2WH.0.jpg");
//        Product p2 = new Product(1627923846,"Happy Family 2IN1 Kopi O With Sugar Mixture Bag","8 x 20 g","Beverages","https://ssecomm.s3-ap-southeast-1.amazonaws.com/products/md/yjwEFEKNPFnYIXrfN4s6wnfTAsdz5t.0.jpg");
//
//        groceryItemList.add(new GroceryItem(1, 5, 8.4, p1, null));
//        groceryItemList.add(new GroceryItem(1, 3, 5.4, p2, null));
    }
}