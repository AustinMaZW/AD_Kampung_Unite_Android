package com.example.ad_project_kampung_unite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ad_project_kampung_unite.model.UserDetail;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;


    SharedPreferences sharedPreferences;
    private static final String LOGIN_CREDENTIALS = "LoginCredentials";
    private static final String KEY_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new GroceryListsFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_grocerylists);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_grocerylists:
                //original code
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new GroceryListsFragment()).commit();

                //dev code - View hitcher grocery list (austin/ngu)
//                FragmentManager fm = getSupportFragmentManager();
//                ViewGroceryListFragment ViewGLFragment = new ViewGroceryListFragment();
//                fm.beginTransaction()
//                        .replace(R.id.fragment_container,ViewGLFragment)
//                        .addToBackStack(null)
//                        .commit();
                break;
            case R.id.nav_groups:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new GroupsFragment()).commit();
                break;
            case R.id.nav_logout:
                //logout request
                sharedPreferences = getSharedPreferences(LOGIN_CREDENTIALS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String username = sharedPreferences.getString(KEY_USERNAME, "");
                String url = "http://10.0.2.2:8080/user/";
                editor.clear();
                editor.apply();
                Intent logout = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(logout);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}