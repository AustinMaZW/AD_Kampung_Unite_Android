package com.example.ad_project_kampung_unite;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class GroceryListsFragment extends Fragment {

    public GroceryListsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layoutRoot = inflater.inflate(R.layout.fragment_grocery_lists, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Grocery Lists");

        FloatingActionButton addButton = layoutRoot.findViewById(R.id.fab);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create dialog
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Enter Name of Grocery List");

                // Set an EditText view to get user input
                final EditText input = new EditText(getContext());
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String name = input.getText().toString();
                        // Do something with value!
                        Bundle result = new Bundle();
                        result.putString("bundleKey", name);
                        getParentFragmentManager().setFragmentResult("requestKey", result);
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        GroceryListFragment groceryListFragment = new GroceryListFragment();
                        fragmentTransaction.replace(R.id.fragment_container,groceryListFragment);
                        fragmentTransaction.commit();
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }
        });

        return layoutRoot;
    }

    interface AddGroceryListDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
        public void sendInput(String input);
    }

    public AddGroceryListDialog.AddGroceryListDialogListener listener;


}

