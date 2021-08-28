package com.example.ad_project_kampung_unite;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ad_project_kampung_unite.adaptors.ShoppingListAdapter;
import com.example.ad_project_kampung_unite.data.remote.CPListService;
import com.example.ad_project_kampung_unite.entities.CombinedPurchaseList;
import com.example.ad_project_kampung_unite.entities.GroupPlan;
import com.example.ad_project_kampung_unite.entities.enums.GroupPlanStatus;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CombinedListFragment extends Fragment {

    private MaterialButton finishShopping, finishShoppingDisabled;
    private List<CombinedPurchaseList> cplList;
    private RecyclerView combinedListItems;
    private ShoppingListAdapter shoppingListAdapter;
    private View combinedListView;
    private GroupPlan groupPlan;
    boolean iscompleted;


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
//        iscompleted = false;
        combinedListView = inflater.inflate(R.layout.fragment_combined_list, container, false);
        finishShopping = combinedListView.findViewById(R.id.finish_shopping_button);
        finishShoppingDisabled = combinedListView.findViewById(R.id.finish_shopping_button_disabled);

        //Demo Code to get from GroupPlanID = 18;
//        Integer groupPlanID = 18;

        //Acutal code get data from server, groupPlanId from bundle
        Bundle bundle = getArguments();
        Integer groupPlanID = bundle.getInt("gpId");
        String url = getResources().getString(R.string.base_url)+getString(R.string.get_list_id)+groupPlanID;
//        String getGrpPlanURL = getResources().getString(R.string.base_url)+getString(R.string.get_groupplan_by_id)+ groupPlanID;
        RequestQueue queueRequest = Volley.newRequestQueue(getActivity());

        StringRequest getListRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("List"+response);
                if (response != null | response != ""){
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        parseArray(jsonArray);
                        finishShoppingButton();
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

//        StringRequest getGrpPlanRequest = new StringRequest(getGrpPlanURL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                System.out.println("grpPlan" +response);
//                if (response != null | !response.matches("")){
//
//                    try {
//                        JSONObject object = new JSONObject(response);
//                        buildGroupPlan(object);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity().getApplicationContext(),
//                        error.toString(),
//                        Toast.LENGTH_SHORT)
//                        .show();
//            }
//        });

//        queueRequest.add(getGrpPlanRequest);
        queueRequest.add(getListRequest);
//        queueRequest.start();

        //Finish Shopping Button
        finishShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update list to server
                String url = getResources().getString(R.string.base_url);
                httpClient.addInterceptor(logging);
                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build());
                Retrofit retrofit = builder.build();
                CPListService combinedPurchaseList = retrofit.create(CPListService.class);
                List<CombinedPurchaseList> purchasedList = shoppingListAdapter.getPurchasedList();
//                purchasedList.stream().forEach(x-> System.out.println(x));
                Call<List<CombinedPurchaseList>> call = combinedPurchaseList.update(purchasedList);
                //List<CombinedPurchaseList> is as desired up to this point.....
                call.enqueue(new Callback<List<CombinedPurchaseList>>() {
                    @Override
                    public void onResponse(Call<List<CombinedPurchaseList>> call, retrofit2.Response<List<CombinedPurchaseList>> response) {
                        if (response.isSuccessful()){
                            Toast.makeText(getActivity().getApplicationContext(), "List updated", Toast.LENGTH_SHORT).show();
//                            System.out.println(response.body());
                            //redirect to enter unit price
                            Bundle bundle = new Bundle();
                            bundle.putInt("gpId", groupPlanID);
                            FragmentManager fragmentManager = getParentFragmentManager();
                            UpdatePriceFragment updatePriceFragment = new UpdatePriceFragment();
                            updatePriceFragment.setArguments(bundle);
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


        //finishShoppingDisabled button
        finishShoppingDisabled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "Close requests before buying items", Toast.LENGTH_LONG).show();
            }
        });






        return combinedListView;
    }

    private void finishShoppingButton(){
        if (iscompleted){
            finishShoppingDisabled.setVisibility(View.GONE);
        }else{
            finishShopping.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void parseArray(JSONArray jsonArray) {
        cplList = new ArrayList<>();
        String gps[] = new String[(jsonArray.length())];
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject object = jsonArray.getJSONObject(i);
                CombinedPurchaseList cpl = new CombinedPurchaseList(
                        object.getInt("id"),
                        object.getJSONObject("product").getInt("id"),
                        object.getJSONObject("product").getString("name"),
                        object.getInt("quantity"),
                        object.getBoolean("purchasedStatus"));
                gps[i] = object.getJSONObject("groupPlan").getString("groupPlanStatus");
                cplList.add(cpl);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Arrays.stream(gps).forEach(x-> System.out.println(x));
//        status = gps[0];
        if(gps[0].matches("CLOSED")){
            iscompleted = true;
        }

        inflateItemView();
    }

    private void inflateItemView() {
        combinedListItems = combinedListView.findViewById(R.id.CL_items);
        combinedListItems.setLayoutManager(new LinearLayoutManager(combinedListView.getContext()));
        //passed cplList to ShoppingListAdapter
        shoppingListAdapter = new ShoppingListAdapter(cplList);
        combinedListItems.setAdapter(shoppingListAdapter);

    }


//    private void inflateViewWithDetails(List<CombinedPurchaseList> cplList, View combinedListView) {
//        combinedListItems = combinedListView.findViewById(R.id.CL_items);
//        for(CombinedPurchaseList cpl : cplList)
//        {
//            combinedListItems.setAdapter(new BaseAdapter() {
//                @Override
//                public int getCount() {
//                    return cplList.size();
//                }
//
//                @Override
//                public Object getItem(int position) {
//                    return cplList.get(position);
//                }
//
//                @Override
//                public long getItemId(int position) {
//                    return cplList.get(position).getId();
//                }
//
//                @Override
//                public View getView(int position, View convertView, ViewGroup parent) {
//                    View view = getLayoutInflater().inflate(R.layout.combined_item, null);;
//                    TextView itemName = view.findViewById(R.id.CIitemName);
//                    itemName.setText(cpl.getProductName());
//                    TextView quantity = view.findViewById(R.id.CIquantity);
//                    quantity.setText(String.valueOf(cpl.getQuantity()));
//
//                    //test
////                    checkBoxes[position] = view.findViewById(R.id.CIcheckbox);
////                    boolean isChecked = checkBoxes[position].isChecked();
////                    updateCheckBoxStatus(isChecked,position);
//                    CheckBox checkBox = view.findViewById(R.id.CIcheckbox);
//                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                            if(buttonView.isChecked()){
//                                cplList.get(position).setPurchased(true);
//                            }else
//                                cplList.get(position).setPurchased(false);
//                        }
//                    });
//
//                    return view;
//                }
//            });
//        }
//
//    }
//
//    private void updateCheckBoxStatus(boolean isChecked, int position) {
//        if(isChecked){
//            cplList.get(position).setPurchased(true);
//        }else{
//            cplList.get(position).setPurchased(false);
//        }
//    }
//
//
//
//
//    private void parseArray(JSONArray jsonArray, View view) {
//        for (int i = 0; i < jsonArray.length(); i++) {
//            try {
//                JSONObject object = jsonArray.getJSONObject(i);
//                CombinedPurchaseList cpl = new CombinedPurchaseList(
//                        object.getInt("id"),
//                        object.getJSONObject("product").getInt("id"),
//                        object.getJSONObject("product").getString("name"),
//                        object.getInt("quantity")
//                );
//                cplList.add(cpl);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            // start fill up individual item with data
//            combinedListItems = view.findViewById(R.id.CL_items);
//            combinedListItems.setAdapter(new BaseAdapter() {
//                @Override
//                public int getCount() {
//                    return cplList.size();
//                }
//                @Override
//                public Object getItem(int position) {
//                    return null;
//                }
//                @Override
//                public long getItemId(int position) {
//                    return 0;
//                }
//                @Override
//                public View getView(int position, View convertView, ViewGroup parent) {
//                    //working UI only
//                    View view = getLayoutInflater().inflate(R.layout.combined_item, null);
//                    CombinedPurchaseList cpl = cplList.get(position);
//                    //get TextView and set text for item name
//                    TextView itemName = view.findViewById(R.id.CIitemName);
//                    itemName.setText(cpl.getProductName());
//                    //get TextView and set text for quantity
//                    TextView quantity = view.findViewById(R.id.CIquantity);
//                    quantity.setText(String.valueOf(cpl.getQuantity()));
//                    CheckBox checkBox = view.findViewById(R.id.CIcheckbox);
//                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                            if(isChecked){
//                                cpl.setPurchased(true);
//                            }
//                            else
//                                cpl.setPurchased(false);
//                        }
//                    });
//                    //end of working UI only
//                    return view;
//                }
//            });
//
//            //End of fill up item with item detail
//        }
//    }
//
//    private Product buildProduct(JSONObject object) {
//        Product product = null;
//        try {
//            product = new Product(
//                    object.getJSONObject("product").getInt("id"),
//                    object.getJSONObject("product").getString("name"),
//                    object.getJSONObject("product").getString("description"),
//                    object.getJSONObject("product").getString("category"),
//                    object.getJSONObject("product").getString("imgURL")
//            );
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return product;
//    }

    private void buildGroupPlan(JSONObject jsonObject) {
        groupPlan = new GroupPlan();
        try {
                groupPlan.setId(jsonObject.getInt("id"));
                groupPlan.setPlanName(jsonObject.getString("planName"));
                groupPlan.setGroupPlanStatus(gpStatusCheck(jsonObject.getString("groupPlanStatus")));
                groupPlan.setPickupDate(LocalDate.parse(jsonObject.getString("pickupDate")));
                groupPlan.setStoreName(jsonObject.getString("storeName"));
                groupPlan.setShoppingDate(LocalDate.parse(jsonObject.getString("shoppingDate")));
                groupPlan.setPickupAddress(jsonObject.getString("pickupAddress"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private GroupPlanStatus gpStatusCheck(String string){
        switch (string) {
            case "AVAILABLE":
                return GroupPlanStatus.AVAILABLE;

            case "SHOPPINGCOMPLETED":
                return GroupPlanStatus.SHOPPINGCOMPLETED;

            case "CLOSED":
                return GroupPlanStatus.CLOSED;

            case "CANCELLED":
                return GroupPlanStatus.CANCELLED;

            default:
                return null;
        }
    }

}