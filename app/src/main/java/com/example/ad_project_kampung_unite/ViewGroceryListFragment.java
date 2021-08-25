package com.example.ad_project_kampung_unite;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.example.ad_project_kampung_unite.entities.enums.GroupPlanStatus;
import com.example.ad_project_kampung_unite.entities.enums.RequestStatus;
import com.example.ad_project_kampung_unite.manage_grocery_list.EditGroceryListFragment;
import com.example.ad_project_kampung_unite.ml.HitcherDetailFragment;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewGroceryListFragment extends Fragment implements View.OnClickListener {

    private static final boolean CONFIRMED = true;

    private List<GroupPlan> groupPlanList;
    private List<GroceryItem> groceryItemList = new ArrayList<>();
    private List<HitchRequest> hitchRequests = new ArrayList<>();
    private HitchRequest acceptedHitchRequest;
    private HitchRequestService hitchRequestService;
    private GroceryListService groceryListService;
    private GroupPlanService groupPlanService;
    private GroupPlan approvedGroupPlan;
    private GroceryList groceryList;

    private Context context;

    //views here
    private View layoutRoot;
    private RecyclerView rvHitchRequests,rvGroceryItems;
    private TextView rqStatusTitle, rqStatDescription, pickupStore, pickupLoc, pickupTime;
    private TextView tvSubtotalAmount, tvGstAmount, tvServicefeeAmount, tvTotalAmount, tvPaymentStatus;
    private Button hitchRqButton, quitGroupBtn, btnCompletePayment, editListBtn;
    private LinearLayout llPaymentComponent;

    public ViewGroceryListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layoutRoot = inflater.inflate(R.layout.fragment_view_grocery_list, container, false);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Grocery List Name");     //need grocery list name passed from previous frag

        context = layoutRoot.getContext();

        // old code to receive grocerylist, but couldnt make it work for refresh purposes
//        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
//            @Override
//            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
//                groceryList = (GroceryList) bundle.getSerializable("bundleKey");
//                // Do something with the result
//                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(groceryList.getName());
//
//                getGroceryItemsFromServer();            //call to retrieve groceryitems
//                getHitchRequestsFromServer();           //call to retrieve requests
//            }
//        });

        //new code to get result in bundle
        Bundle bundle = getArguments();
        if(bundle!=null){
            groceryList = (GroceryList) bundle.getSerializable("bundleKey");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(groceryList.getName());
            getGroceryItemsFromServer();            //call to retrieve groceryitems
            getHitchRequestsFromServer();           //call to retrieve requests
        }

        rqStatusTitle = layoutRoot.findViewById(R.id.rqStat_title);
        rqStatDescription = layoutRoot.findViewById(R.id.rqStat_description);
        pickupStore = layoutRoot.findViewById(R.id.pickup_store);
        pickupLoc = layoutRoot.findViewById(R.id.pickup_location);
        pickupTime = layoutRoot.findViewById(R.id.pickup_time);
        hitchRqButton = layoutRoot.findViewById(R.id.hitch_rq_btn);
        hitchRqButton.setOnClickListener(this);
        quitGroupBtn = layoutRoot.findViewById(R.id.quit_group);

        setQuitGroupBtn();       //for quitGroup
        editListBtn = layoutRoot.findViewById(R.id.edit_groceries);
        editListBtn.setOnClickListener(this);

        return layoutRoot;
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.hitch_rq_btn){
            FragmentManager fm = getParentFragmentManager();
            Log.e("Hitcher Detail","yes_5");
            Log.e("list Id",Integer.toString(groceryList.getId()));
            HitcherDetailFragment hitcerDetail = new HitcherDetailFragment();
            hitcerDetail.setgList(groceryList);
            FragmentTransaction trans = fm.beginTransaction();
            trans.replace(R.id.fragment_container,hitcerDetail);
            trans.addToBackStack(null);
            trans.commit();
        }
        if(view.getId() == R.id.complete_payment_btn) {
            Log.i("Click", "clicked complete payment");
            if (acceptedHitchRequest != null) {
                Log.i("HitchRequest", acceptedHitchRequest.toString());
                acceptedHitchRequest.setHitcherConfirmTransaction(CONFIRMED);
                updateConrirmTransaction();
            }
        }

        if(view.getId() == R.id.edit_groceries) {
            Log.i("Click", "clicked edit groceries");

            // send grocery list object
            GroceryList target = groceryList;

            // send grocery list to grocery list fragment
            Bundle result = new Bundle();
            result.putSerializable("bundleKey1", target);
            System.out.println("result:" + result);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.setFragmentResult("requestKey1", result);

            EditGroceryListFragment editGroceryListFragment = new EditGroceryListFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, editGroceryListFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void getGroceryItemsFromServer(){
        groceryListService = RetrofitClient.createService(GroceryListService.class);
        Call<List<GroceryItem>> call = groceryListService.getGroceryItemByGroceryListId(groceryList.getId());

        call.enqueue(new Callback<List<GroceryItem>>() {
            @Override
            public void onResponse(Call<List<GroceryItem>> call, Response<List<GroceryItem>> response) {

                if (response.isSuccessful()) {
                    groceryItemList = response.body();
//                    Log.d("Success", String.valueOf(groceryItemList.get(0).getProduct().getProductName())); //for testing

                    buildGroceryItemRV();

                    if(groceryList.getGroupPlanGL() != null) {
                        if (groceryList.getGroupPlanGL().getGroupPlanStatus() == GroupPlanStatus.SHOPPINGCOMPLETED) {
                            // view pending payment parts
                            Map<String, Double> totalPayment = calculateTotalPayment();
                            buildPaymentComponents(totalPayment);
                        }
                    }
                } else {
                    Log.e("getGroceryItemByGroceryListId Error", response.errorBody().toString());
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
        Call<List<HitchRequest>> call = hitchRequestService.getHitchRequestsByGroceryListId(groceryList.getId());    //for test data 38 is pending status, 36 is approved, 249 is for quit list test
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
                    Log.e("getHitchRequestsByGroceryListId Error", response.errorBody().toString());
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
        Call<Boolean> call = groupPlanService.quitGroupPlanByGroceryListId(groceryList.getId());

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                if (response.isSuccessful()) {
                    Boolean result = response.body();
                    Log.d("Success", result.toString()); //for testing

                } else {
                    Log.e("quitGroupPlanByGroceryListId Error", response.errorBody().toString());
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
        HitchRequestAdaptor hitchRequestAdaptor = new HitchRequestAdaptor(hitchRequests, groceryList);
        rvHitchRequests.setAdapter(hitchRequestAdaptor);
        rvHitchRequests.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));
    }

    private void buildGroceryItemRV() {
        //recycler view for grocery items
        rvGroceryItems = layoutRoot.findViewById(R.id.rv_grocery_list);
        GroceryListItemAdaptor groceryListItemAdaptor = new GroceryListItemAdaptor(groceryItemList);
        rvGroceryItems.setAdapter(groceryListItemAdaptor);  //set the adaptor here
        rvGroceryItems.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));

        llPaymentComponent = layoutRoot.findViewById(R.id.payment_component);
        if (groceryList == null || groceryList.getGroupPlanGL() == null ||
                groceryList.getGroupPlanGL().getGroupPlanStatus() != GroupPlanStatus.SHOPPINGCOMPLETED) {

            llPaymentComponent.setVisibility(View.GONE);
        }
    }

    private void buildPaymentComponents(Map<String, Double> totalPayment) {
        //view pending payment parts
        tvSubtotalAmount = layoutRoot.findViewById(R.id.subtotal_amount);
        tvGstAmount = layoutRoot.findViewById(R.id.gst_amount);
        tvServicefeeAmount = layoutRoot.findViewById(R.id.service_fee_amount);
        tvTotalAmount = layoutRoot.findViewById(R.id.total_amount);

        tvSubtotalAmount.setText("$" + totalPayment.get("subtotal"));
        tvGstAmount.setText("$" + totalPayment.get("gst"));
        tvServicefeeAmount.setText("$" + totalPayment.get("servicefee"));
        tvTotalAmount.setText("$" + totalPayment.get("total"));

        tvPaymentStatus = layoutRoot.findViewById(R.id.payment_status);
        btnCompletePayment = layoutRoot.findViewById(R.id.complete_payment_btn);
        btnCompletePayment.setOnClickListener(this);

        getAcceptedHitchRequestFromServer();
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

                        try {
                            Thread.sleep(500);      //need to wait or database hasn't updated...
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();     //below to refresh ui

                        Fragment currentFrag = fm.findFragmentByTag("VIEW_HITCHER_GL_FRAG");
                        fm.beginTransaction().detach(currentFrag).commitNow();
                        fm.beginTransaction().attach(currentFrag).commitNow();
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

    private Map<String, Double> calculateTotalPayment() {
        //calculate subtotal, gst, service fee, net total
        Map<String, Double> map = new HashMap<>();

        double subtotal = 0;
        for (GroceryItem groceryItem : groceryItemList) {
            subtotal += groceryItem.getSubtotal();
        }
        map.put("subtotal", subtotal);

        double gst = subtotal * 7 / 100;
        gst = Math.round(gst * 100.0) / 100.0;
        map.put("gst", gst);

        double servicefee = subtotal * 5 / 100;
        servicefee = Math.round(servicefee * 100.0) / 100.0;
        map.put("servicefee", servicefee);

        double total = subtotal + gst + servicefee;
        total = Math.round(total * 100.0) / 100.0;
        map.put("total", total);

        return map;
    }

    private void getAcceptedHitchRequestFromServer() {
        if (groceryList.getHitcherDetail() != null) {
            Call<HitchRequest> call = hitchRequestService.getAcceptedHitchRequestByHitcherDetailId(groceryList.getHitcherDetail().getId());
            call.enqueue(new Callback<HitchRequest>() {
                @Override
                public void onResponse(Call<HitchRequest> call, Response<HitchRequest> response) {
                    if (response.isSuccessful()) {
                        acceptedHitchRequest = response.body();
                        loadPaymentStatus();
                    } else {
                        Log.e("getAcceptedHitchRequestByHitcherDetailId Error", response.errorBody().toString());
                    }
                }

                @Override
                public void onFailure(Call<HitchRequest> call, Throwable t) {
                    call.cancel();
                    Log.w("Failure", "Failure!");
                    t.printStackTrace();
                }
            });
        }
    }

    private void loadPaymentStatus() {
        if (acceptedHitchRequest != null) {
            if (acceptedHitchRequest.isHitcherConfirmTransaction()) {
                if (acceptedHitchRequest.isBuyerConfirmTransaction()) {
                    tvPaymentStatus.setText(R.string.payment_status_complete);
                    tvPaymentStatus.setTextColor(getResources().getColor(R.color.Kampong_Green, null));
                } else {
                    tvPaymentStatus.setText(R.string.payment_status_waiting_buyer);
                    tvPaymentStatus.setTextColor(getResources().getColor(R.color.yellow, null));
                }
                layoutRoot.findViewById(R.id.button_container).setVisibility(View.GONE);
            } else {
                tvPaymentStatus.setVisibility(View.GONE);
            }
        }
    }

    private void updateConrirmTransaction() {
        Call<HitchRequest> call = hitchRequestService.updateHitchRequest(acceptedHitchRequest);
        call.enqueue(new Callback<HitchRequest>() {
            @Override
            public void onResponse(Call<HitchRequest> call, Response<HitchRequest> response) {
                if (response.isSuccessful()) {
                    HitchRequest updatedHR = response.body();

                    if (updatedHR.isBuyerConfirmTransaction()) {
                        tvPaymentStatus.setText(R.string.payment_status_complete);
                        tvPaymentStatus.setTextColor(getResources().getColor(R.color.Kampong_Green, null));
                    } else {
                        tvPaymentStatus.setText(R.string.payment_status_waiting_buyer);
                        tvPaymentStatus.setTextColor(getResources().getColor(R.color.yellow, null));
                    }
                    layoutRoot.findViewById(R.id.button_container).setVisibility(View.GONE);
                    tvPaymentStatus.setVisibility(View.VISIBLE);
                }
                else {
                    Log.e("updateHitchRequest Error", response.errorBody().toString());
                }
            }
            @Override
            public void onFailure(Call<HitchRequest> call, Throwable t) {
                call.cancel();
                Log.w("Failure", "Failure!");
                t.printStackTrace();
            }
        });
    }
}