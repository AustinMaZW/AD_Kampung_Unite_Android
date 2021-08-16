package com.example.ad_project_kampung_unite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.InnerRecyclerViewAdapter;
import com.example.ad_project_kampung_unite.R;

import java.util.ArrayList;

public class ExpandableRecyclerViewAdapter extends RecyclerView.Adapter<ExpandableRecyclerViewAdapter.ViewHolder> {

    ArrayList<String> nameList = new ArrayList<String>();
    ArrayList<String> image = new ArrayList<String>();
    ArrayList<Integer> counter = new ArrayList<Integer>();
    ArrayList<ArrayList> itemNameList = new ArrayList<ArrayList>();
    Context context;

    public ExpandableRecyclerViewAdapter(Context context,
                                         ArrayList<String> nameList,
                                         ArrayList<ArrayList> itemNameList) {
        this.nameList = nameList;
        this.itemNameList = itemNameList;
        this.context = context;


        for (int i = 0; i < nameList.size(); i++) {
            counter.add(0);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, pickuptime, itemcount;
        ImageButton dropBtn;
        RecyclerView cardRecyclerView;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.groceryDetailListTitle);
            pickuptime = itemView.findViewById(R.id.groceryDetailPickupTime);
            itemcount=itemView.findViewById(R.id.groceryDetailItemQuantitySum);
            dropBtn = itemView.findViewById(R.id.categoryExpandBtn);
            cardRecyclerView = itemView.findViewById(R.id.innerRecyclerView);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_group_details_collapse, parent, false);

        ExpandableRecyclerViewAdapter.ViewHolder vh = new ExpandableRecyclerViewAdapter.ViewHolder(v);

        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(nameList.get(position));

        InnerRecyclerViewAdapter itemInnerRecyclerView = new InnerRecyclerViewAdapter(itemNameList.get(position));

        holder.cardRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (counter.get(position) % 2 == 0) {
                    holder.cardRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    holder.cardRecyclerView.setVisibility(View.GONE);
                }

                counter.set(position, counter.get(position) + 1);


            }
        });
        holder.cardRecyclerView.setAdapter(itemInnerRecyclerView);

    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }


}