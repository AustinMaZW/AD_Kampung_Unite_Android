package com.example.ad_project_kampung_unite.adaptors;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.GroupDetailsFragment;
import com.example.ad_project_kampung_unite.ItemClickListener;
import com.example.ad_project_kampung_unite.MyGroupPlansHolder;
import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.ViewGroceryListFragment;
import com.example.ad_project_kampung_unite.entities.GroceryItem;
import com.example.ad_project_kampung_unite.entities.GroceryList;
import com.example.ad_project_kampung_unite.entities.GroupPlan;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GroupPlanItemAdapter extends RecyclerView.Adapter<MyGroupPlansHolder> {

    Context c;
    List<GroupPlan> lists;

    public GroupPlanItemAdapter(Context c, ArrayList<GroupPlan> lists) {
        this.c = c;
        this.lists = lists;
    }

    @NonNull
    @Override
    public MyGroupPlansHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_group_lists_items,null);
        return new MyGroupPlansHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyGroupPlansHolder myholder, int i) {
        GroupPlan groupPlan = lists.get(i);

        myholder.mGroupPlanName.setText(lists.get(i).getPlanName());
        myholder.mGroupPlanShoppingDate.setText(lists.get(i).getShoppingDate().toString());
        myholder.mGroupPlanRequests.setText(lists.get(i).getGroupPlanStatus().getDisplayStatus());
        //.setImageResource to set image.

        myholder.setItemClickListener(new ItemClickListener() {
            private GroupDetailsFragment groupDetailsFragment;

            @Override
            public void onItemClickLister(View v, int position) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();

                // send group plan object
                int target = lists.get(position).getId();
                String target2 = lists.get(position).getGroupPlanStatus().getDisplayStatus();

                // send grocery list to grocery list fragment
                Bundle result = new Bundle();
                result.putInt("gpId", target);
                result.putString("gpStatus", target2);

                // go to grocery list view fragment
                groupDetailsFragment = new GroupDetailsFragment();
                groupDetailsFragment.setArguments(result);       //putting bundle inside frag
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, groupDetailsFragment, "GROUP_DETAILS_FRAG")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void setGroceryList(List<GroupPlan> groceryLists){
        this.lists = (ArrayList<GroupPlan>) groceryLists;
        notifyDataSetChanged();
    }
}