package com.example.ad_project_kampung_unite;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ad_project_kampung_unite.data.remote.GroceryListService;
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
import com.example.ad_project_kampung_unite.entities.GroceryList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupsFragment extends Fragment {

    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layoutRoot = inflater.inflate(R.layout.fragment_groups, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Groups");

        FloatingActionButton addButton = layoutRoot.findViewById(R.id.temp);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        FragmentManager fragmentManager = getParentFragmentManager();
                        GroupDetailsFragment groupDetailsFragment = new GroupDetailsFragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container,groupDetailsFragment)
                                .addToBackStack(null)
                                .commit();
            }
        });


        return layoutRoot;
    }
}