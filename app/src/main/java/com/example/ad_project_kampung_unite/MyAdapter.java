package com.example.ad_project_kampung_unite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ad_project_kampung_unite.entities.GroceryList;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//Bridge between holder and list,
//for each holder - inflate, populate with data, add onClickListener, direct to activity 'view list details'
public class MyAdapter extends RecyclerView.Adapter<MyHolder> {

    Context c;
    List<GroceryList> lists;

    public MyAdapter(Context c, List<GroceryList> lists) {
        this.c = c;
        this.lists = lists;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_grocery_lists_items,null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myholder, int i) {
        myholder.mGroceryListName.setText(lists.get(i).getName());
        if(lists.get(i).getGroupPlanGL() != null) {
            myholder.mPickupDetail.setText(lists.get(i).getGroupPlanGL().getPickupDate().format(DateTimeFormatter.ISO_DATE));
        }

//        myholder.mPickupDetail.setText((CharSequence) lists.get(i).getDate());
        //.setImageResource to set image.

        myholder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickLister(View v, int position) {
                String gName = lists.get(position).getName();
//                String gDetails = lists.get(position).getDate();

//                Intent intent = new Intent(c, ViewGroceryListActivity.class);
//                intent.putExtra("gName",gName);
//                intent.putExtra("gDetails",gDetails);
//                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void setGroceryList(List<GroceryList> groceryLists){
        this.lists = (ArrayList<GroceryList>) groceryLists;
        notifyDataSetChanged();
    }
}
