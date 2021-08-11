package com.example.ad_project_kampung_unite;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

//view details of each grocery list
public class ViewGroceryListActivity extends AppCompatActivity {

    TextView mName, mPickupDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_grocery_list);

        ActionBar actionBar = getSupportActionBar();

        this.mName = findViewById(R.id.grocerylistname);
        this.mPickupDetails = findViewById(R.id.pickupdetails);

        Intent intent = getIntent();

        String gName = intent.getStringExtra("gName");
        String gPickupDetails = intent.getStringExtra("gDetails");

        actionBar.setTitle(gName);

        mName.setText(gName);
        mPickupDetails.setText(gPickupDetails);

    }
}