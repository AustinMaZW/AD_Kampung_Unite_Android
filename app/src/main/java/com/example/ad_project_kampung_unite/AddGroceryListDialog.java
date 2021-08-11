package com.example.ad_project_kampung_unite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class AddGroceryListDialog extends AppCompatDialogFragment {
    private EditText editTextGrocerylistName;
    private AddGroceryListDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.addgrocerylist_dialog,null);

        builder.setView(view)
                .setTitle("New Grocery List")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String grocerylistName = editTextGrocerylistName.getText().toString();
                        listener.applyTexts(grocerylistName);

                        Intent intent = new Intent(getContext(),MyGroceryListsActivity.class);
                        intent.putExtra("newlistName", grocerylistName);
                    }
                });
        editTextGrocerylistName = view.findViewById(R.id.namegrocerylist);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (AddGroceryListDialogListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString()+"must implement dialoglistener");
        }
    }

    public interface AddGroceryListDialogListener{
        void applyTexts(String grocerylistName);
    }
}
