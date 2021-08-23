package com.example.ad_project_kampung_unite;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ad_project_kampung_unite.entities.CombinedPurchaseList;
import com.example.ad_project_kampung_unite.entities.GroupPlan;
import com.example.ad_project_kampung_unite.entities.Product;
import com.example.ad_project_kampung_unite.entities.UserDetail;
import com.example.ad_project_kampung_unite.entities.enums.GroupPlanStatus;
import com.example.ad_project_kampung_unite.service.CPLService;
import com.example.ad_project_kampung_unite.service.UserDetailService;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CombinedListFragment extends Fragment {

    MaterialButton finishShopping;
    SharedPreferences sharedPreferences;
    List<CombinedPurchaseList> cplList = new ArrayList<>();
    ListView combinedListItems;
    CheckBox[] checkBoxes;
    View[] views;

    public CombinedListFragment() {
        // Required empty public constructor
    }

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BASIC);

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View combinedListView = inflater.inflate(R.layout.fragment_combined_list, container, false);
        //Get groupPlanID from sharedpreferences using Key groupPlanId
        sharedPreferences = getActivity().getSharedPreferences("groupPlan",MODE_PRIVATE);

        Integer groupPlanID = sharedPreferences.getInt("groupPlanId", 18);
        String url = getResources().getString(R.string.cpl_base_url)+"getlist/_id="+groupPlanID;

        StringRequest getListRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null){
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        parseArray(jsonArray, combinedListView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(),
                        error.toString(),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });


        //Finish Shopping Button link
        finishShopping = combinedListView.findViewById(R.id.finish_shopping_button);
        finishShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update list
                        String url = getResources().getString(R.string.cpl_base_url);
                httpClient.addInterceptor(logging);
                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build());
                Retrofit retrofit = builder.build();
                CPLService combinedPurchaseList = retrofit.create(CPLService.class);
                Call<List<CombinedPurchaseList>> call = combinedPurchaseList.update(cplList);
                call.enqueue(new Callback<List<CombinedPurchaseList>>() {
                    @Override
                    public void onResponse(Call<List<CombinedPurchaseList>> call, retrofit2.Response<List<CombinedPurchaseList>> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(getActivity().getApplicationContext(), "List Processed", Toast.LENGTH_SHORT).show();
                            //redirect to enter unit price
                            FragmentManager fragmentManager = getParentFragmentManager();
                            UpdatePriceFragment updatePriceFragment = new UpdatePriceFragment();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container,updatePriceFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }
                    @Override
                    public void onFailure(Call<List<CombinedPurchaseList>> call, Throwable t) {
                        Toast.makeText(getActivity().getApplicationContext(), "Something went wrong :(", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        RequestQueue queueRequest = Volley.newRequestQueue(getActivity());
        queueRequest.add(getListRequest);

        return combinedListView;
    }

    private void parseArray(JSONArray jsonArray, View view) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject object = jsonArray.getJSONObject(i);
                CombinedPurchaseList cpl = new CombinedPurchaseList(
                        object.getInt("id"),
                        object.getJSONObject("product").getInt("id"),
                        object.getJSONObject("product").getString("name"),
                        object.getInt("quantity")
//                        ,
//                        buildGroupPlan(object),
//                        buildProduct(object)
                );
                cplList.add(cpl);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            combinedListItems = view.findViewById(R.id.CL_items);
            combinedListItems.setAdapter(new BaseAdapter() {
                @Override
                public int getCount() {
                    return cplList.size();
                }

                @Override
                public Object getItem(int position) {
                    return null;
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = getLayoutInflater().inflate(R.layout.combined_item, null);
                    CombinedPurchaseList cpl = cplList.get(position);

                    //get TextView and set text for item name
                    TextView itemName = view.findViewById(R.id.CIitemName);
                    itemName.setText(cpl.getProductName());

                    //get TextView and set text for quantity
                    TextView quantity = view.findViewById(R.id.CIquantity);
                    quantity.setText(String.valueOf(cpl.getQuantity()));

                    //get checkbox and set status for purchased status
                    checkBoxes = new CheckBox[getCount()];
                    checkBoxes[position] = views[position].findViewById(R.id.CIcheckbox);


                    });
                    return view;
                }
            });
        }
    }

    private Product buildProduct(JSONObject object) {
        Product product = null;
        try {
            product = new Product(
                    object.getJSONObject("product").getInt("id"),
                    object.getJSONObject("product").getString("name"),
                    object.getJSONObject("product").getString("description"),
                    object.getJSONObject("product").getString("category"),
                    object.getJSONObject("product").getString("imgURL")
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return product;
    }

    private GroupPlan buildGroupPlan(JSONObject object) {
        GroupPlan groupPlan = null;
        try {
            groupPlan = new GroupPlan(
                    object.getJSONObject("groupPlan").getInt("id"),
                    object.getJSONObject("groupPlan").getString("planName"),
                    object.getJSONObject("groupPlan").getString("storeName"),
                    LocalDate.parse(object.getJSONObject("groupPlan").getString("shoppingDate")),
                    object.getJSONObject("groupPlan").getString("pickupAddress"),
                    LocalDate.parse(object.getJSONObject("groupPlan").getString("pickupDate")),
                    GroupPlanStatus.valueOf(object.getJSONObject("groupPlan").getString("groupPlanStatus"))
            );

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return groupPlan;
    }
}