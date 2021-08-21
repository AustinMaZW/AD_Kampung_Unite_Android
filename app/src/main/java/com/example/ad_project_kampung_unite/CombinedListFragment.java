package com.example.ad_project_kampung_unite;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
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
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CombinedListFragment extends Fragment {

    MaterialButton finishShopping;
    SharedPreferences sharedPreferences;
    ArrayList<CombinedPurchaseList> cplList = new ArrayList<>();
    ListView combinedListItems;

    public CombinedListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View combinedListView = inflater.inflate(R.layout.fragment_combined_list, container, false);
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
                // get & save all checked combined list items
                // link to update prices fragment

                //redirect to enter unit price
                FragmentManager fragmentManager = getParentFragmentManager();
                CombinedListFragment combinedListFragment = new CombinedListFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container,combinedListFragment)
                        .addToBackStack(null)
                        .commit();

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
                    TextView itemName = view.findViewById(R.id.CIitemName);
                    TextView quantity = view.findViewById(R.id.CIquantity);
                    CheckBox checkBox = view.findViewById(R.id.CIcheckbox);

                    itemName.setText(cpl.getProductName());
                    quantity.setText(String.valueOf(cpl.getQuantity()));

                    return view;
                }
            });


        }


    }
}