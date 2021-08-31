package com.example.ad_project_kampung_unite.manage_grocery_list;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

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

    private int mYear, mMonth, mDay, mHour, mMinute;

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

        createDatePickerDialog(purchaseDate);
        createDatePickerDialog(pickupDate);
        createTimePickerDialog(purchaseTime);
        createTimePickerDialog(pickupTime1);
        createTimePickerDialog(pickupTime2);
        createTimePickerDialog(pickupTime3);


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveGroupPlanDetails();
                } catch (Exception e) {
                    showDialog("Invalid Group Plan Details!","Please check all fields");
                }
            }
        });

        return layoutRoot;
    }

    private void showDialog(String title,String msg){
        AlertDialog.Builder radioDialog = new AlertDialog.Builder(getContext());
        radioDialog.setTitle(title).setMessage(msg).setIcon(R.drawable.logo_small).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                submitBtn.setEnabled(true);
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    public void createDatePickerDialog(EditText date) {
        date.setClickable(true);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                c.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                String strDate = format.format(c.getTime());
                                date.setText(strDate);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }

    public void createTimePickerDialog(EditText time) {
        time.setClickable(true);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, 3);
                c.set(Calendar.MINUTE, 0);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                String strTime = String.format("%02d:%02d",hourOfDay,minute);
                                time.setText(strTime+":00");
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
    }



    public void saveGroupPlanDetails() {
        DateTimeFormatter df_date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter df_time =DateTimeFormatter.ofPattern("HH:mm:ss");
        String gName = groupName.getText().toString();
        String sName = storeName.getText().toString();
        String add = address.getText().toString().concat(", Singapore, Singapore");

        String pDate;
        String pTime;
        String puDate;
        String puTime1;
        String puTime2;
        String puTime3;
        if(!purchaseDate.getText().toString().isEmpty() || !purchaseTime.getText().toString().isEmpty() || !pickupDate.getText().toString().isEmpty() || !pickupTime1.getText().toString().isEmpty()
        || !pickupTime2.getText().toString().isEmpty() || !pickupTime3.getText().toString().isEmpty()) {
            pDate = LocalDate.parse(purchaseDate.getText().toString()).format(df_date);
            puDate = LocalDate.parse(pickupDate.getText().toString()).format(df_date);
            puTime1 = LocalTime.parse(pickupTime1.getText().toString()).format(df_time);
            puTime2 = LocalTime.parse(pickupTime2.getText().toString()).format(df_time);
            puTime3 = LocalTime.parse(pickupTime3.getText().toString()).format(df_time);

            if(gName == "") {
                gName = "Group";
            }

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
        } else {
            Toast.makeText(getContext(),"Empty fields not allowed",Toast.LENGTH_LONG).show();
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
