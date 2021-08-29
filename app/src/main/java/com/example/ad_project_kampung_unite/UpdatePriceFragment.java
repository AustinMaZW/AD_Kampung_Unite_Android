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

import com.example.ad_project_kampung_unite.adaptors.UpdatePriceAdapter;
import com.example.ad_project_kampung_unite.data.remote.CPListService;
import com.example.ad_project_kampung_unite.data.remote.GroceryItemService;
import com.example.ad_project_kampung_unite.data.remote.GroupPlanService;
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
import com.example.ad_project_kampung_unite.entities.CombinedPurchaseList;
import com.example.ad_project_kampung_unite.entities.GroceryItem;
import com.example.ad_project_kampung_unite.entities.enums.GroupPlanStatus;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePriceFragment extends Fragment implements View.OnClickListener {

    private View layoutRoot;
    private RecyclerView rvCombinedList;
    private Button btnSubmit;

    private int gpId;
    private CPListService cplService;
    private GroceryItemService giService;
    private GroupPlanService gpService;
    private List<CombinedPurchaseList> combinedPurchaseLists;
    private UpdatePriceAdapter updatePriceAdapter;
    private List<GroceryItem> items;
    //booleans for updating prices
    boolean saveCPL, saveGroceryItem, saveGroupPlanStatus;
    public UpdatePriceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layoutRoot = inflater.inflate(R.layout.fragment_update_price, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Submit Receipt");

        // to get arguments from previous fragment
        Bundle bundle = getArguments();
        if(bundle!=null){
            gpId = (int) bundle.getInt("gpId");
        }

        btnSubmit = layoutRoot.findViewById(R.id.submit_btn);
        btnSubmit.setOnClickListener(this);

        loadCombinedPurchaseLists();

        return layoutRoot;
    }

    private void loadCombinedPurchaseLists() {
        cplService = RetrofitClient.createService(CPListService.class);
//        Call<List<CombinedPurchaseList>> call = cplService.getAllCPLists();
        Call<List<CombinedPurchaseList>> call = cplService.getCPListByGroupPlanIdAndPurchasedStatus(gpId, true);
        call.enqueue(new Callback<List<CombinedPurchaseList>>() {
            @Override
            public void onResponse(Call<List<CombinedPurchaseList>> call, Response<List<CombinedPurchaseList>> response) {
                if (response.isSuccessful()) {
                    combinedPurchaseLists = response.body();
                    buildRecyclerView();
                } else {
                    Log.e("getCPListByGroupPlanIdAndPurchasedStatus Error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<CombinedPurchaseList>> call, Throwable t) {
                call.cancel();
                Log.w("Failure", "Failure!");
                t.printStackTrace();
            }
        });
    }

    private void buildRecyclerView() {
        rvCombinedList = layoutRoot.findViewById(R.id.rv_combined_list);
        rvCombinedList.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));

        updatePriceAdapter = new UpdatePriceAdapter(combinedPurchaseLists);
        rvCombinedList.setAdapter(updatePriceAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit_btn) {
            /*for (int i=0; i<combinedPurchaseLists.size(); i++) {
                View holder = rvCombinedList.getChildAt(i);
                EditText etSubtotal = holder.findViewById(R.id.subtotal);
                String subtotal = etSubtotal.getText().toString();
                EditText etDiscount = holder.findViewById(R.id.discount);
                String discount = etDiscount.getText().toString();
                Log.i("Update Price", subtotal + ", " + discount.isEmpty());
            }*/

            if (!updatePriceAdapter.hasError()) {
                getGroceryItemsForGroupPlan();
            }
        }
    }

    private void getGroceryItemsForGroupPlan() {
        giService = RetrofitClient.createService(GroceryItemService.class);
        Call<List<GroceryItem>> call = giService.getAcceptedGroceryItemsByGroupPlanId(gpId);
        call.enqueue(new Callback<List<GroceryItem>>() {
            @Override
            public void onResponse(Call<List<GroceryItem>> call, Response<List<GroceryItem>> response) {
                if (response.isSuccessful()) {
                    items = response.body();
                    calculateSubtotalPriceForEachItem();


                } else {
                    Log.e("getAcceptedGroceryItemsByGroupPlanId Error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<GroceryItem>> call, Throwable t) {
                call.cancel();
                Log.w("Failure", "Failure!");
                t.printStackTrace();
            }
        });
    }

    private void calculateSubtotalPriceForEachItem() {
        Map<Integer, String> sm = updatePriceAdapter.getSubtotalMap();
        Map<Integer, String> dm = updatePriceAdapter.getDiscountMap();

        for (int i=0; i<combinedPurchaseLists.size(); i++) {
            CombinedPurchaseList cp = combinedPurchaseLists.get(i);
            double subtotal = 0;
            if (!sm.get(cp.getId()).isEmpty())
                subtotal = Double.parseDouble(sm.get(cp.getId()));

            double discount = 0;
            if (!dm.get(cp.getId()).isEmpty())
                discount = Double.parseDouble(dm.get(cp.getId()));

            double unitprice = 0;
            if (subtotal > 0) {
                unitprice = (subtotal - discount) / cp.getQuantity();
                unitprice = Math.round(unitprice * 100.0) / 100.0;
            }

            cp.setProductSubtotal(subtotal);
            cp.setProductDiscount(discount);
            cp.setProductUnitPrice(unitprice);

            List<GroceryItem> list =  items.stream().filter(x -> x.getProduct().getProductId() == cp.getProduct().getProductId()).collect(Collectors.toList());
            for (GroceryItem gi : list) {
                gi.setSubtotal(gi.getQuantity() * unitprice);   //subtotal = qty * up;
            }
        }
        //save unit price of each product in CombinedPurchaseList table
        saveUnitPriceAndSubtotalInCombinedPurchaseList();


        Log.i("test", "//////");
    }

    private void saveUnitPriceAndSubtotalInCombinedPurchaseList() {
        Call<Boolean> call = cplService.saveAll(combinedPurchaseLists);

//        try {
//            Response<Boolean> response = call.execute();
//            saveCPL = response.body();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    saveCPL = response.body();
                    //save subtotal in GroceryItem table for all items of each grocery list of this group plan
                    saveSubtotalInGroceryItem(items);
                } else {
                    Log.e("CPL saveAll Error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                call.cancel();
                Log.w("Failure", "Failure!");
                t.printStackTrace();
            }
        });
    }

    private void saveSubtotalInGroceryItem(List<GroceryItem> items) {
        Call<Boolean> call = giService.saveAll(items);

//        try {
//            Response<Boolean> response = call.execute();
//            saveCPL = response.body();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    saveGroceryItem = response.body();
                    //set group plan status 'shopping completed'
                    updateGroupPlanStatus();
                } else {
                    Log.e("GroceryItem saveAll Error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                call.cancel();
                Log.w("Failure", "Failure!");
                t.printStackTrace();
            }
        });
    }

    private void updateGroupPlanStatus() {
        gpService = RetrofitClient.createService(GroupPlanService.class);
        Call<Void> call = gpService.updateGroupPlanStatus(gpId, GroupPlanStatus.SHOPPINGCOMPLETED);

//        try {
//            Response<Void> response = call.execute();
//            saveGroupPlanStatus = true;
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.i("updateGroupPlanStatus", "Successful");
                    saveGroupPlanStatus = true;
                    // go to group details fragment, passed group plan id
                    if(saveCPL && saveGroceryItem && saveGroupPlanStatus){
                        Bundle bundle = new Bundle();
                        bundle.putInt("gpId", gpId);

                        GroupDetailsFragment groupDetailsFragment = new GroupDetailsFragment();
                        groupDetailsFragment.setArguments(bundle);
                        getParentFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, groupDetailsFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                } else {
                    Log.e("updateGroupPlanStatus Error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                call.cancel();
                Log.w("Failure", "Failure!");
                t.printStackTrace();
            }
        });
    }
}