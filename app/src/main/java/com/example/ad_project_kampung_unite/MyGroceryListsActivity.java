package com.example.ad_project_kampung_unite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

//view all grocery list
//navigate to individual lists
//delete or archive a list
//add list
public class MyGroceryListsActivity extends AppCompatActivity implements AddGroceryListDialog.AddGroceryListDialogListener {

    private ArrayList<GroceryList> mGroceryLists;

    private RecyclerView mRecyclerView;
    private MyAdapter myAdapter;

    private Button buttonAdd;
    private EditText editTextInsert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_grocery_lists);

        //input dummy data
        createMyList();
        //recycler view adapter instantiated here
        buildRecyclerView();

        buttonAdd = findViewById(R.id.add);

        buttonAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        //attaching touch helper to recycler view for swipe action itoms
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
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
        public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,float dX, float dY,int actionState, boolean isCurrentlyActive){

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(MyGroceryListsActivity.this, R.color.Kampong_Blue))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(MyGroceryListsActivity.this, R.color.Kampong_Yellow))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_archive_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    //open dialog for adding new grocery list
    public void openDialog(){
        AddGroceryListDialog addListDiaglog = new AddGroceryListDialog();
        addListDiaglog.show(getSupportFragmentManager(),"example dialog");
    }

    //create dummy data
    public void createMyList(){

        mGroceryLists = new ArrayList<>();

        mGroceryLists.add(new GroceryList("August Group Buy", "","","3","pending"));
        mGroceryLists.add(new GroceryList("July Group Buy","19 Jul 2021", "0900", "10","completed"));
        mGroceryLists.add(new GroceryList("July Group Buy 1", "", "", "10", "pending"));
        mGroceryLists.add(new GroceryList("Week 3 groceries", "12 Jul 2021", "0900", "15", "accepted"));
        mGroceryLists.add(new GroceryList("June Group Buy", "19 June 2021", "0900", "15", "completed"));
    }

    //build recycler view
    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        myAdapter = new MyAdapter(this, mGroceryLists);
        mRecyclerView.setAdapter(myAdapter);
    }

    @Override
    public void applyTexts(String grocerylistName) {

        editTextInsert.setText(grocerylistName);
    }
}