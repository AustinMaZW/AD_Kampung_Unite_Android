package com.example.ad_project_kampung_unite.manage_grocery_list;

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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ad_project_kampung_unite.R;
import com.example.ad_project_kampung_unite.data.remote.GroceryListService;
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
import com.example.ad_project_kampung_unite.data.remote.UserDetailService;
import com.example.ad_project_kampung_unite.entities.GroceryList;
import com.example.ad_project_kampung_unite.entities.UserDetail;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyGroceryListsFragment extends Fragment {

    private List<GroceryList> groceryLists;
    private UserDetail user;
    private SharedPreferences sharedPreferences;
    private GroceryList newGroceryList;
    int userId;

    private GroceryListService groceryListService;
    private UserDetailService userDetailService;

    private RecyclerView mRecyclerView;
    private MyGroceryListsAdapter myGroceryListsAdapter;
    private View layoutRoot;
    FloatingActionButton addButton;

    public MyGroceryListsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layoutRoot = inflater.inflate(R.layout.fragment_grocery_lists, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Grocery Lists");

        // Get user id from shared prefs
        sharedPreferences = getContext().getSharedPreferences("LoginCredentials",0);
        userId = sharedPreferences.getInt("userId", -1);

        groceryLists = new ArrayList<>();
        groceryListService = RetrofitClient.createService(GroceryListService.class);
        userDetailService = RetrofitClient.createService(UserDetailService.class);

        getUser();
        loadGroceryLists();
        setUpAddButton();

        return layoutRoot;
    }

    public void getUser() {
        Call<UserDetail> userCall = userDetailService.findUserById(userId);
        userCall.enqueue(new Callback<UserDetail>() {
            @Override
            public void onResponse(Call<UserDetail> call, Response<UserDetail> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    if(user!=null) {
                        Log.i("User", user.toString());
                    }
                } else {
                    Log.e("findUserById Error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<UserDetail> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to connect", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void loadGroceryLists() {
        Call<List<GroceryList>> call = groceryListService.findGroceryListsByUserDetailId(userId);
        call.enqueue(new Callback<List<GroceryList>>() {
            @Override
            public void onResponse(Call<List<GroceryList>> call, Response<List<GroceryList>> response) {
                List<GroceryList> result = response.body();
                if(result != null) {
                    result.stream().forEach(x -> groceryLists.add(x));
                    //recycler view adapter instantiated here
                    buildRecyclerView();

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

    public void setUpAddButton() {
        addButton = layoutRoot.findViewById(R.id.fab);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });
    }

    public void createDialog() {
        // Create dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Enter Name of Grocery List");

        // Set an EditText view to get user input
        EditText input = new EditText(getContext());
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String name = input.getText().toString();
                saveNewGroceryList(name, userId);
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    public void saveNewGroceryList(String name, int userId) {
        Call<Integer> groceryListCall = groceryListService.createGroceryListByUserDetailId(name, userId);
        groceryListCall.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int groceryListId = response.body();
                    System.out.println("grocery list created: "+groceryListId);
                    sendGroceryListToFragment(groceryListId);

                } else {
                    Log.e("createGroceryListByUserDetailId Error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to connect", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendGroceryListToFragment(int groceryListId) {
        Call<GroceryList> call = groceryListService.findGroceryListByGroceryListId(groceryListId);
        call.enqueue(new Callback<GroceryList>() {
            @Override
            public void onResponse(Call<GroceryList> call, Response<GroceryList> response) {
                if(response.isSuccessful()) {
                    newGroceryList = response.body();
                    if(newGroceryList != null) {
                        // send grocery list to grocery list fragment
                        Bundle result = new Bundle();
                        result.putSerializable("bundleKey1", newGroceryList);
                        System.out.println("result:" + result);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.setFragmentResult("requestKey1", result);

                        EditGroceryListFragment editGroceryListFragment = new EditGroceryListFragment();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, editGroceryListFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                } else {
                    Log.e("findGroceryListByGroceryListId Error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<GroceryList> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to connect", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void buildRecyclerView(){
        mRecyclerView = layoutRoot.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext(), LinearLayoutManager.VERTICAL,false));
        myGroceryListsAdapter = new MyGroceryListsAdapter(layoutRoot.getContext(), groceryLists);
        mRecyclerView.setAdapter(myGroceryListsAdapter);
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
                    myGroceryListsAdapter.notifyItemRemoved(position);

                    Snackbar.make(mRecyclerView, deletedListName, Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener(){

                                @Override
                                public void onClick(View v) {
                                    groceryLists.add(position, deletedList);
                                    myGroceryListsAdapter.notifyItemInserted(position);
                                }
                            }).show();
                    break;
                case ItemTouchHelper.RIGHT:
                    archiveList = groceryLists.get(position);
                    archivedListName = archiveList.getName().toString();
                    archivedLists.add(archiveList);

                    groceryLists.remove(position);
                    myGroceryListsAdapter.notifyItemRemoved(position);

                    Snackbar.make(mRecyclerView, archivedListName, Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener(){

                                @Override
                                public void onClick(View v) {
                                    archivedLists.remove(archivedLists.lastIndexOf(archiveList));
                                    groceryLists.add(position, archiveList);
                                    myGroceryListsAdapter.notifyItemInserted(position);
                                }
                            }).show();
                    break;
            }
        }
        //swipe action item decorator (set color and icon)
        public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(MyGroceryListsFragment.this.getContext(), R.color.Kampong_Blue))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(MyGroceryListsFragment.this.getContext(), R.color.Kampong_Yellow))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_archive_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
}

