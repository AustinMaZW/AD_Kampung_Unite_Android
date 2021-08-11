package com.example.ad_project_kampung_unite.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ad_project_kampung_unite.R;

public class SQLiteTestActivity extends AppCompatActivity {
    EditText txtName, txtSalary, txtDept;
    Button add, view;
    EmployeeAdapter eAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_test);

        txtName = findViewById(R.id.txtName);
        txtSalary = findViewById(R.id.txtSalary);
//        txtDept = findViewById(R.id.txtDept);
        add = findViewById(R.id.buttonAdd);
        view = findViewById(R.id.buttonView);

        eAdapter = new EmployeeAdapter(this);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEmployee(view);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewEmployee(view);
            }
        });
    }

    public void addEmployee(View view) {
        String name = txtName.getText().toString();
        String salary = txtSalary.getText().toString();
//        String dept = txtDept.getText().toString();

        long insertedId = eAdapter.insert(name, salary);
        if (insertedId <= 0)
            Toast.makeText(this, "Insertion Unsuccessful", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Inserted Id: " + insertedId, Toast.LENGTH_LONG).show();

//        eAdapter.insertRaw(name, salary);
//        Toast.makeText(this, "Successful", Toast.LENGTH_LONG).show();
    }

    public void viewEmployee(View view) {
        String data = eAdapter.getData();
        Toast.makeText(this, data, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        eAdapter.closeHelper();
        super.onDestroy();
    }
}