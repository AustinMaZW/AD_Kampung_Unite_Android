package com.example.ad_project_kampung_unite.ml;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.adaptors.HitchRequestAdaptor;
import com.example.ad_project_kampung_unite.entities.GroupPlan;

import java.util.List;

public class BuyerListAdapter extends RecyclerView.Adapter<BuyerListAdapter.MyViewHolder> {

    private List<GroupPlan> plans;
    private Context context;

    public BuyerListAdapter(List<GroupPlan> plans, Context context) {
        this.plans = plans;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context,R.layout.recycle_view_buyer_item,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.buyerName.setText(plans.get(position).getStoreName());
        holder.pickUpDate.setText(plans.get(position).getPickupDate().toString());
        holder.timeSlot.setText(plans.get(position).getPickupDate().toString());
        holder.location.setText(plans.get(position).getPickupAddress());
        Log.e("shit","shishishis");
    }

    @Override
    public int getItemCount() {
        if(plans == null){
            return 0;
        }
        return plans.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView buyerName,pickUpDate,timeSlot,location;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            buyerName = itemView.findViewById(R.id.buyer_Name);
            pickUpDate = itemView.findViewById(R.id.pick_Up_Date);
            timeSlot = itemView.findViewById(R.id.time_Slot);
            location = itemView.findViewById(R.id._location_);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(monRecyclerItemClickListener != null){
                        monRecyclerItemClickListener.onRecyclerItemClick(getLayoutPosition());
                    }
                }
            });
        }
    }
    private onRecyclerItemClickListener monRecyclerItemClickListener;
    public void setRecyclerItemClickListener(onRecyclerItemClickListener listener){
        monRecyclerItemClickListener = listener;
    }

    public interface onRecyclerItemClickListener{
        void onRecyclerItemClick(int position);
    }
}
