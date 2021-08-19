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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewGroceryListFragment extends Fragment {

    private List<GroupPlan> groupPlanList;
    private List<GroceryItem> groceryItemList = new ArrayList<>();
    private List<HitchRequest> hitchRequests = new ArrayList<>();
    private HitchRequestService hitchRequestService;
    private GroceryListService groceryListService;
    private RecyclerView rvHitchRequests;
    private RecyclerView rvGroceryItems;
    private LinearLayout llPaymentComponent;
    private TextView tvSubtotalAmount;
    private TextView tvGstAmount;
    private TextView tvServicefeeAmount;
    private TextView tvTotalAmount;

    public ViewGroceryListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layoutRoot = inflater.inflate(R.layout.fragment_view_grocery_list, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Grocery List Name");     //need grocery list name passed from previous frag

        createDummyData(); //  fake data to be replaced with http request list with database

        //recycler view for hitch requests
        rvHitchRequests = layoutRoot.findViewById(R.id.rv_hitch_rq);
        HitchRequestAdaptor hitchRequestAdaptor = new HitchRequestAdaptor(groupPlanList);
        rvHitchRequests.setAdapter(hitchRequestAdaptor);    //move this and above line to http request method when added
        rvHitchRequests.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));

        //recycler view for grocery items
        rvGroceryItems = layoutRoot.findViewById(R.id.rv_grocery_list);
        rvGroceryItems.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));

        groceryListService = RetrofitClient.createService(GroceryListService.class);

        //add condition if group plan status is shopping completed to show or hide this component
        layoutRoot.findViewById(R.id.payment_component).setVisibility(View.GONE);
        llPaymentComponent = layoutRoot.findViewById(R.id.payment_component);
        getGroceryItemsFromServer(layoutRoot);

        //add condition if request is pending then below are 'invisible
        layoutRoot.findViewById(R.id.status_approved).setVisibility(View.GONE);

        return layoutRoot;
    }

    private void getGroceryItemsFromServer(View layoutRoot){
        Call<List<GroceryItem>> call = groceryListService.getGroceryItemByGroceryListId(30); //hard coded grocerylistid here, replace later

        call.enqueue(new Callback<List<GroceryItem>>() {
            @Override
            public void onResponse(Call<List<GroceryItem>> call, Response<List<GroceryItem>> response) {

                if (response.isSuccessful()) {
                    groceryItemList = response.body();
                    Log.d("Success", String.valueOf(groceryItemList.get(0).getProduct().getProductName())); //for testing

                    GroceryListItemAdaptor groceryListItemAdaptor = new GroceryListItemAdaptor(groceryItemList);
                    rvGroceryItems.setAdapter(groceryListItemAdaptor);  //set the adaptor here

                    if (llPaymentComponent != null) {
                        // calculate total payment and load on screen
                        Map<String, Double> totalPayment = calculateTotalPayment();

                        tvSubtotalAmount = layoutRoot.findViewById(R.id.subtotal_amount);
                        tvGstAmount = layoutRoot.findViewById(R.id.gst_amount);
                        tvServicefeeAmount = layoutRoot.findViewById(R.id.service_fee_amount);
                        tvTotalAmount = layoutRoot.findViewById(R.id.total_amount);

                        tvSubtotalAmount.setText("$" + totalPayment.get("subtotal"));
                        tvGstAmount.setText("$" + totalPayment.get("gst"));
                        tvServicefeeAmount.setText("$" + totalPayment.get("servicefee"));
                        tvTotalAmount.setText("$" + totalPayment.get("total"));

                        llPaymentComponent.setVisibility(View.VISIBLE); // testing
                    }

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
//    private void getHitchRequestsFromServer(){
//        Call<List<HitchRequest>> call = hitchRequestService.getHitchRequestsByGroceryListId(36);    //hard coded grocerylistid here, replace later
//
//        call.enqueue(new Callback<List<HitchRequest>>() {
//            @Override
//            public void onResponse(Call<List<HitchRequest>> call, Response<List<HitchRequest>> response) {
//
//                if (response.isSuccessful()) {
//                    hitchRequests = response.body();
//                    Log.d("Success", String.valueOf(hitchRequests.get(0))); //continue here....
//
//                    GroceryListItemAdaptor groceryListItemAdaptor = new GroceryListItemAdaptor(groceryItemList);
//                    rvGroceryItems.setAdapter(groceryListItemAdaptor);  //set the adaptor here
//                } else {
//                    Log.e("Error", response.errorBody().toString());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<hitchRequests>> call, Throwable t) {
//                // like no internet connection / the website doesn't exist
//                call.cancel();
//                Log.w("Failure", "Failure!");
//                t.printStackTrace();
//            }
//        });
//    }

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

    private Map<String, Double> calculateTotalPayment() {
        Map<String, Double> map = new HashMap<>();

        double subtotal = 0;
        for (GroceryItem groceryItem : groceryItemList) {
            subtotal += groceryItem.getSubtotal();
            map.put("subtotal", subtotal);
        }

        double gst = subtotal * 7 / 100;
        gst = Math.round(gst * 100.0) / 100.0;
        map.put("gst", gst);

        double servicefee = subtotal * 5 / 100;
        servicefee = Math.round(servicefee * 100.0) / 100.0;
        map.put("servicefee", servicefee);

        double total = subtotal + gst + servicefee;
        map.put("total", total);

        return map;
    }
}