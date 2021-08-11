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
import androidx.fragment.app.DialogFragment;

//popup dialog to add new empty grocery list
//linked to add button in layout 'activity_my_grocery_lists'
public class AddGroceryListDialog extends DialogFragment {
    private EditText editTextGrocerylistName;

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
                        listener.onDialogNegativeClick(AddGroceryListDialog.this);
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String grocerylistName = editTextGrocerylistName.getText().toString();

                        listener.onDialogPositiveClick(AddGroceryListDialog.this);
                        listener.sendInput(grocerylistName);
                    }
                });
        editTextGrocerylistName = view.findViewById(R.id.namegrocerylist);

        return builder.create();
    }

    public interface AddGroceryListDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
        public void sendInput(String input);
    }

    public AddGroceryListDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            listener = (AddGroceryListDialogListener) getActivity();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(MyGroceryListsActivity.class.toString()
                    + " must implement AddGroceryListDialogListener");
        }
    }
}
