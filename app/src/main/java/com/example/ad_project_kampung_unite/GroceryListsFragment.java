package com.example.ad_project_kampung_unite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ad_project_kampung_unite.data.remote.GroceryListService;
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
import com.example.ad_project_kampung_unite.data.remote.UserDetailService;
import com.example.ad_project_kampung_unite.entities.GroceryList;
import com.example.ad_project_kampung_unite.entities.UserDetail;
import com.example.ad_project_kampung_unite.search_product.SearchFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GroceryListsFragment extends Fragment {

    private List<GroceryList> groceryLists;
    private UserDetail user;
    GroceryListService groceryListService;
    UserDetailService userDetailService;

    private RecyclerView mRecyclerView;
    private MyAdapter myAdapter;

    SharedPreferences sharedPreferences;
    int userId;

    public GroceryListsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layoutRoot = inflater.inflate(R.layout.fragment_grocery_lists, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Grocery Lists");

        // Get user id
        sharedPreferences = getContext().getSharedPreferences("LoginCredentials",0);
        userId = Integer.valueOf(sharedPreferences.getString("userId","-1"));


        // get grocery lists from database
        groceryLists = new ArrayList<>();
        groceryListService = RetrofitClient.createService(GroceryListService.class);

        // get user from database
        userDetailService = RetrofitClient.createService(UserDetailService.class);

        loadGroceryLists(layoutRoot);
        setUpAddButton(layoutRoot, userId);

        return layoutRoot;
    }

    public void loadGroceryLists(View layoutRoot) {
        Call<List<GroceryList>> call = groceryListService.findGroceryListsByUserDetailId(userId);
        call.enqueue(new Callback<List<GroceryList>>() {
            @Override
            public void onResponse(Call<List<GroceryList>> call, Response<List<GroceryList>> response) {
                List<GroceryList> result = response.body();
                if(result != null) {
                    result.stream().forEach(x -> groceryLists.add(x));
                    //recycler view adapter instantiated here
                    buildRecyclerView(layoutRoot);

                    //attaching touch helper to recycler view for swipe action itoms
                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                    itemTouchHelper.attachToRecyclerView(mRecyclerView);
                }
            }
            @Override
            public void onFailure(Call<List<GroceryList>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to connect", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setUpAddButton(View layoutRoot, int userId) {
        FloatingActionButton addButton = layoutRoot.findViewById(R.id.fab);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create dialog
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Enter Name of Grocery List");

                // Set an EditText view to get user input
                EditText input = new EditText(getContext());
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String name = input.getText().toString();
                        GroceryList newGroceryList = new GroceryList();
                        System.out.println("user id: " + userId);

                        Call<UserDetail> userCall = userDetailService.findUserById(userId);
                        userCall.enqueue(new Callback<UserDetail>() {
                            @Override
                            public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                                if(response.body() != null) {
                                    user = response.body();
                                    System.out.println("name: " + user.getFirstName());
                                    newGroceryList.setName(name);
                                    newGroceryList.setUserDetail(user);
                                }

                                // save new grocery list to database
                                Call<GroceryList> groceryListCall = groceryListService.addGroceryList(newGroceryList);
                                groceryListCall.enqueue(new Callback<GroceryList>() {
                                    @Override
                                    public void onResponse(Call<GroceryList> call, Response<GroceryList> response) {
                                        System.out.println("connected");

                                        FragmentManager fragmentManager = getParentFragmentManager();
                                        GroceryListFragment groceryListFragment = new GroceryListFragment();

                                        // addtobackstack to go back to previous fragment
                                        fragmentManager.beginTransaction()
                                                .replace(R.id.fragment_container,groceryListFragment)
                                                .addToBackStack(null)
                                                .commit();
                                    }

                                    @Override
                                    public void onFailure(Call<GroceryList> call, Throwable t) {
                                        System.out.println("failed to connect");

                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<UserDetail> call, Throwable t) {
                                Toast.makeText(getContext(), "Failed to connect", Toast.LENGTH_SHORT).show();
                            }
                        });

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



    }

    //build recycler view
    public void buildRecyclerView(View layoutRoot){

        mRecyclerView = layoutRoot.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext(), LinearLayoutManager.VERTICAL,false));

        myAdapter = new MyAdapter(layoutRoot.getContext(), groceryLists);
        mRecyclerView.setAdapter(myAdapter);
    }










    //attributes for deleting or archiving a list via swipe action
    GroceryList deletedList = null;
    GroceryList archiveList = null;
    String deletedListName = null;
    String archivedListName = null;

    List<GroceryList> archivedLists = new ArrayList<>();

    //defining what each swipe action does
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAbsoluteAdapterPosition();

            switch (direction){
                case ItemTouchHelper.LEFT:
                    deletedList = groceryLists.get(position);
                    deletedListName = deletedList.getName().toString();

                    groceryLists.remove(position);
                    myAdapter.notifyItemRemoved(position);

                    Snackbar.make(mRecyclerView, deletedListName, Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener(){

                                @Override
                                public void onClick(View v) {
                                    groceryLists.add(position, deletedList);
                                    myAdapter.notifyItemInserted(position);
                                }
                            }).show();
                    break;
                case ItemTouchHelper.RIGHT:
                    archiveList = groceryLists.get(position);
                    archivedListName = archiveList.getName().toString();
                    archivedLists.add(archiveList);

                    groceryLists.remove(position);
                    myAdapter.notifyItemRemoved(position);

                    Snackbar.make(mRecyclerView, archivedListName, Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener(){

                                @Override
                                public void onClick(View v) {
                                    archivedLists.remove(archivedLists.lastIndexOf(archiveList));
                                    groceryLists.add(position, archiveList);
                                    myAdapter.notifyItemInserted(position);
                                }
                            }).show();
                    break;
            }
        }
        //swipe action item decorator (set color and icon)
        public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(GroceryListsFragment.this.getContext(), R.color.Kampong_Blue))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(GroceryListsFragment.this.getContext(), R.color.Kampong_Yellow))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_archive_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
}

