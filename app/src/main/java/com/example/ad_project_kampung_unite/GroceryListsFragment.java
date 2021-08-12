package com.example.ad_project_kampung_unite;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ad_project_kampung_unite.model.GroceryList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class GroceryListsFragment extends Fragment {

    private ArrayList<GroceryList> mGroceryLists;

    private RecyclerView mRecyclerView;
    private MyAdapter myAdapter;

    private Button buttonAdd;
    private String newlistName;

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
                        String name = "";
                        if(name == "") {
                            name = "My Grocery List";
                        } else {
                            name = input.getText().toString();
                        }
                        mGroceryLists.add(new GroceryList(name,"","","",""));


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

        //input dummy data
        createMyList();
        //recycler view adapter instantiated here
        buildRecyclerView(layoutRoot);
        //attaching touch helper to recycler view for swipe action itoms
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        return layoutRoot;
    }
    public void createMyList(){

        mGroceryLists = new ArrayList<>();

        mGroceryLists.add(new GroceryList("August Group Buy", "","","3","pending"));
        mGroceryLists.add(new GroceryList("July Group Buy","19 Jul 2021", "0900", "10","completed"));
        mGroceryLists.add(new GroceryList("July Group Buy 1", "", "", "10", "pending"));
        mGroceryLists.add(new GroceryList("Week 3 groceries", "12 Jul 2021", "0900", "15", "accepted"));
        mGroceryLists.add(new GroceryList("June Group Buy", "19 June 2021", "0900", "15", "completed"));
    }

    //build recycler view
    public void buildRecyclerView(View layoutRoot){

        mRecyclerView = layoutRoot.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext(), LinearLayoutManager.VERTICAL,false));

        myAdapter = new MyAdapter(layoutRoot.getContext(), mGroceryLists);
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
                    deletedList = mGroceryLists.get(position);
                    deletedListName = deletedList.getName().toString();

                    mGroceryLists.remove(position);
                    myAdapter.notifyItemRemoved(position);

                    Snackbar.make(mRecyclerView, deletedListName, Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener(){

                                @Override
                                public void onClick(View v) {
                                    mGroceryLists.add(position, deletedList);
                                    myAdapter.notifyItemInserted(position);
                                }
                            }).show();
                    break;
                case ItemTouchHelper.RIGHT:
                    archiveList = mGroceryLists.get(position);
                    archivedListName = archiveList.getName().toString();
                    archivedLists.add(archiveList);

                    mGroceryLists.remove(position);
                    myAdapter.notifyItemRemoved(position);

                    Snackbar.make(mRecyclerView, archivedListName, Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener(){

                                @Override
                                public void onClick(View v) {
                                    archivedLists.remove(archivedLists.lastIndexOf(archiveList));
                                    mGroceryLists.add(position, archiveList);
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

    public void sendInput(String input) {
//        Log.d(MyGroceryListsActivity.class.toString(), "send input: "+ input);
        newlistName = input;
        mGroceryLists.add(new GroceryList(newlistName,"","","",""));
    }
}

