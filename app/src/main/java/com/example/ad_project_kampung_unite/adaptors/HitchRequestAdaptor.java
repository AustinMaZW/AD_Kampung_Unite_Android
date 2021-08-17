package com.example.ad_project_kampung_unite.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.entities.GroupPlan;

import java.util.List;

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

    private List<GroupPlan> groupPlans;

    public HitchRequestAdaptor(List<GroupPlan> groupPlans){
        this.groupPlans = groupPlans;
    }

    // inflate item row layout and returning the holder
    @Override
    public HitchRequestAdaptor.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
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
        GroupPlan groupPlan = groupPlans.get(position);

        Integer gpId = groupPlan.getId();

        // Set item views based on your views and data model
        TextView textView = holder.groupPlanName;
        textView.setText(gpId.toString());    //change this to getName later when name is added to group plan as attr
        Button button = holder.cancelRequestBtn;
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return groupPlans.size();
    }
}
