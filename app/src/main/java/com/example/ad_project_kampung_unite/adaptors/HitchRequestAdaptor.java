package com.example.ad_project_kampung_unite.adaptors;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.ViewGroceryListFragment;
import com.example.ad_project_kampung_unite.data.remote.HitchRequestService;
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
import com.example.ad_project_kampung_unite.entities.GroceryItem;
import com.example.ad_project_kampung_unite.entities.GroupPlan;
import com.example.ad_project_kampung_unite.entities.HitchRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HitchRequestAdaptor extends RecyclerView.Adapter<HitchRequestAdaptor.ViewHolder>{

    //viewholder obj provides access to all views within each item row
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView groupPlanName;
        public Button cancelRequestBtn;

        public ViewHolder(View itemView){
            super(itemView);

            groupPlanName = itemView.findViewById(R.id.group_plan_name);
            cancelRequestBtn = itemView.findViewById(R.id.cancel_rq_btn);
        }
    }

    private List<HitchRequest> hitchRequests;
    private HitchRequestService hitchRequestService;
    private Context context;

    public HitchRequestAdaptor(List<HitchRequest> hitchRequests){
        this.hitchRequests = hitchRequests;
    }

    // inflate item row layout and returning the holder
    @Override
    public HitchRequestAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View hitchRQView = inflater.inflate(R.layout.hitch_request_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(hitchRQView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(HitchRequestAdaptor.ViewHolder holder, int position) {
        // Get the data model based on position
        HitchRequest hitchRequest = hitchRequests.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.groupPlanName;
        textView.setText(hitchRequest.getGroupPlan().getPlanName());    //change this to getName later when name is added to group plan as attr

        hitchRequestService = RetrofitClient.createService(HitchRequestService.class);

        Button cancelRequestBtn = holder.cancelRequestBtn;
        cancelRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(hitchRequests.get(position).getId());

                //alert dialog below
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//                builder.setTitle("Title Here");
                builder.setMessage("Confirm Cancel Request?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        cancelHitchRequestToServer(hitchRequests.get(position).getId());

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

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return hitchRequests.size();
    }

    private void cancelHitchRequestToServer(int hitchRqId){
        Call<Boolean> call = hitchRequestService.cancelHitchRequest(hitchRqId); //hard coded grocerylistid here, replace later

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
}
