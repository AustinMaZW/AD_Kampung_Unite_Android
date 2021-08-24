package com.example.ad_project_kampung_unite.manage_grocery_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.ad_project_kampung_unite.R;

public class BuyerFragment extends Fragment {

    private View layoutRoot;


    public BuyerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        layoutRoot = inflater.inflate(R.layout.fragment_buyer_detail, container, false);

        // Set title
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Buyer Details");

        return layoutRoot;
    }
}
