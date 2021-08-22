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
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
import com.example.ad_project_kampung_unite.entities.CombinedPurchaseList;
import com.example.ad_project_kampung_unite.manage_grocery_list.MyGroceryListsAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePriceFragment extends Fragment implements View.OnClickListener {

    private View layoutRoot;
    private RecyclerView rvCombinedList;
    private Button btnSubmit;

    private int gpId;
    private CPListService cplService;
    private List<CombinedPurchaseList> combinedPurchaseLists;
    private UpdatePriceAdapter updatePriceAdapter;

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
            gpId = (int) bundle.getSerializable("gpId");
        }
        gpId = 18; // hard coded to test

        btnSubmit = layoutRoot.findViewById(R.id.submit_btn);
        btnSubmit.setOnClickListener(this);

        loadCombinedPurchaseLists();

        return layoutRoot;
    }

    private void loadCombinedPurchaseLists() {
        cplService = RetrofitClient.createService(CPListService.class);
//        Call<List<CombinedPurchaseList>> call = cplService.getAllCPLists();
        Call<List<CombinedPurchaseList>> call = cplService.getCPListByGroupPlanId(gpId);
        call.enqueue(new Callback<List<CombinedPurchaseList>>() {
            @Override
            public void onResponse(Call<List<CombinedPurchaseList>> call, Response<List<CombinedPurchaseList>> response) {
                if (response.isSuccessful()) {
                    combinedPurchaseLists = response.body();
                    buildRecyclerView();
                } else {
                    Log.e("getCPListByGroupPlanId Error", response.errorBody().toString());
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
//            String subtotal = updatePriceAdapter.holder.etSubtotal.getText().toString();
        }
    }
}