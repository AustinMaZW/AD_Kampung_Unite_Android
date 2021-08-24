package com.example.ad_project_kampung_unite;

import android.content.Intent;
import android.graphics.Canvas;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.Button;

import com.example.ad_project_kampung_unite.adaptors.ExpandableRecyclerViewAdapter;
import com.example.ad_project_kampung_unite.adaptors.GroceryListItemAdaptor;
import com.example.ad_project_kampung_unite.data.remote.GroceryItemService;
import com.example.ad_project_kampung_unite.data.remote.GroceryListService;
import com.example.ad_project_kampung_unite.data.remote.HitchRequestService;
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
import com.example.ad_project_kampung_unite.entities.GroceryItem;
import com.example.ad_project_kampung_unite.entities.GroceryList;
import com.google.android.material.button.MaterialButton;
import com.example.ad_project_kampung_unite.entities.HitchRequest;
import com.example.ad_project_kampung_unite.manage_grocery_list.MyGroceryListsFragment;
import com.example.ad_project_kampung_unite.entities.HitchRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GroupDetailsFragment extends Fragment {

    private int groupId;
    private List<GroceryItem> buyerGroceryItemList = new ArrayList<>();
    private List<HitchRequest> hitchRequestList = new ArrayList<>();

      private RecyclerView rvBuyerGrocery,rvHitchRequests;
      private GroceryItemService groceryItemService;
      private HitchRequestService hitchRequestService;
//    private GroupDetailsAdapter myAdapter;
    private Button acceptHrqBtn; //just for testing purposes, delete later

    MaterialButton combinedListButton;

    RecyclerView expanderRecyclerView;
    View layoutRoot;

    public GroupDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layoutRoot = inflater.inflate(R.layout.fragment_group_details, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Group Details");

        Bundle bundle = getArguments();
        groupId = bundle.getInt("gpId");

        //Button to link to Combined List Fragment
        combinedListButton = layoutRoot.findViewById(R.id.combinedListButton);
        combinedListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //navigate to CombinedList Fragment
                FragmentManager fragmentManager = getParentFragmentManager();
                CombinedListFragment combinedListFragment = new CombinedListFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container,combinedListFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


//        //buyer can edit their own grocery list
        //pending to-do: pass buyer id to 'grocery list' fragment
//        FloatingActionButton editButton = layoutRoot.findViewById(R.id.groupDetails_buyerEditBtn);
//        editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentManager fragmentManager = getParentFragmentManager();
//                GroceryListFragment groceryListFragment = new GroceryListFragment();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.fragment_container,groceryListFragment)
//                        .addToBackStack(null)
//                        .commit();
//            }
//        });

        groceryItemService = RetrofitClient.createService(GroceryItemService.class);
        getBuyerGroceryItemsFromServer();

        hitchRequestService = RetrofitClient.createService(HitchRequestService.class);
        getHitchRequestsFromServer();

        //below are test codes for Austin (accept hrq)
        acceptHrqBtn = layoutRoot.findViewById(R.id.accept_hitchrq); //only for testing, this view should be in hrq recycle view of this frag
        acceptHrqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Approve this request?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        approveHitchRqToServer(290);    //hard hitchrqid here for testing

                        try {
                            Thread.sleep(500);      //need to wait or database hasn't updated...
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //below code to refresh ui with new data
//                        FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
//                        Fragment currentFrag = fm.findFragmentByTag("VIEW_HITCHER_GL_FRAG");  //replace this with appropriate frag tag
//                        fm.beginTransaction().detach(currentFrag).commitNow();
//                        fm.beginTransaction().attach(currentFrag).commitNow();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

//        //input dummy data
//        createMyList();
//        //recycler view adapter instantiated here
//        buildRecyclerView(layoutRoot);
//        //attaching touch helper to recycler view for swipe action itoms
//
//        expanderRecyclerView = layoutRoot.findViewById(R.id.groupDetails_expandableRecyclerView);
//        initiateExpander();

        return layoutRoot;
    }

    private void getBuyerGroceryItemsFromServer(){
        Call<List<GroceryItem>> call = groceryItemService.getBuyerGroceryItemsByGroupId(groupId);

        call.enqueue(new Callback<List<GroceryItem>>() {
            @Override
            public void onResponse(Call<List<GroceryItem>> call, Response<List<GroceryItem>> response) {

                if (response.isSuccessful()) {
                    buyerGroceryItemList = response.body();
                    Log.d("Success", String.valueOf(buyerGroceryItemList.get(0).getProduct().getProductName())); //for testing

                    //recycler view for grocery items
                    rvBuyerGrocery = layoutRoot.findViewById(R.id.recyclerviewGroupDetails);
                    rvBuyerGrocery.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));

                    GroceryListItemAdaptor groceryListItemAdaptor = new GroceryListItemAdaptor(buyerGroceryItemList);
                    rvBuyerGrocery.setAdapter(groceryListItemAdaptor);  //set the adaptor here
                } else {
                    Log.e("Error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<GroceryItem>> call, Throwable t) {
                // like no internet connection / the website doesn't exist
                call.cancel();
                Log.w("Failure", "Failure!");
                t.printStackTrace();
            }
        });
    }

    private void getHitchRequestsFromServer(){
        Call<List<HitchRequest>> call = hitchRequestService.getHitchRequestsByGroupId(groupId);

        call.enqueue(new Callback<List<HitchRequest>>() {
            @Override
            public void onResponse(Call<List<HitchRequest>> call, Response<List<HitchRequest>> response) {

                if (response.isSuccessful()) {
                    hitchRequestList = response.body();
                    Log.d("Success", String.valueOf(hitchRequestList.get(0).toString())); //for testing

                    //recycler view for grocery items
                    rvHitchRequests = layoutRoot.findViewById(R.id.groupDetails_expandableRecyclerView);
                    rvHitchRequests.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));

                    ExpandableRecyclerViewAdapter expandableRecyclerViewAdapter = new ExpandableRecyclerViewAdapter(hitchRequestList);
                    rvHitchRequests.setAdapter(expandableRecyclerViewAdapter);  //set the adaptor here

                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                    itemTouchHelper.attachToRecyclerView(rvHitchRequests);
                } else {
                    Log.e("Error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<HitchRequest>> call, Throwable t) {
                // like no internet connection / the website doesn't exist
                call.cancel();
                Log.w("Failure", "Failure!");
                t.printStackTrace();
            }
        });
    }



    private void approveHitchRqToServer(int hitchRqId) {
        HitchRequestService hitchRequestService = RetrofitClient.createService(HitchRequestService.class);
        Call<Boolean> call = hitchRequestService.approveHitchRq(hitchRqId); //hard coded hitchRqId here, replace later

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                if (response.isSuccessful()) {
                    Boolean status = response.body();
                    Log.d("Success", status.toString()); //for testing

                    //logic to change any UI here, but shouldn't need since other parts of this frag should've handled it
                } else {
                    Log.e("Error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                // like no internet connection / the website doesn't exist
                call.cancel();
                Log.w("Failure", "Failure!");
                t.printStackTrace();
            }
        });
    }
//    public void createMyList(){
//
//        mGroceryLists = new ArrayList<>();
//
//        mGroceryLists.add(new GroceryList("August Group Buy", "","","3","pending"));
//        mGroceryLists.add(new GroceryList("July Group Buy","19 Jul 2021", "0900", "10","completed"));
//        mGroceryLists.add(new GroceryList("July Group Buy 1", "", "", "10", "pending"));
//        mGroceryLists.add(new GroceryList("Week 3 groceries", "12 Jul 2021", "0900", "15", "accepted"));
//        mGroceryLists.add(new GroceryList("June Group Buy", "19 June 2021", "0900", "15", "completed"));
//    }
//
//    //build recycler view
//    public void buildRecyclerView(View layoutRoot){
//
//        mRecyclerView = layoutRoot.findViewById(R.id.recyclerviewGroupDetails);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));
//
//        myAdapter = new GroupDetailsAdapter(layoutRoot.getContext(), mGroceryLists);
//        mRecyclerView.setAdapter(myAdapter);
//    }
//
//    private void initiateExpander() {
//
//        ArrayList<String> parentList = new ArrayList<>();
//        ArrayList<ArrayList> childListHolder = new ArrayList<>();
//
//        parentList.add("List A");
//        parentList.add("List B");
//        parentList.add("List C");
//
//        ArrayList<String> childList = new ArrayList<>();
//        childList.add("Apple");
//        childList.add("Mango");
//        childList.add("Banana");
//
//        childListHolder.add(childList);
//
//        childList = new ArrayList<>();
//        childList.add("Red bull");
//        childList.add("Maa");
//        childList.add("Horlicks");
//
//        childListHolder.add(childList);
//
//        childList = new ArrayList<>();
//        childList.add("Knife");
//        childList.add("Vessels");
//        childList.add("Spoons");
//
//        childListHolder.add(childList);
//
//        ExpandableRecyclerViewAdapter expandableCategoryRecyclerViewAdapter =
//                new ExpandableRecyclerViewAdapter(layoutRoot.getContext(), parentList,
//                        childListHolder);
//
//        expanderRecyclerView.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));
//        expanderRecyclerView.setAdapter(expandableCategoryRecyclerViewAdapter);
//    }



     //attributes for deleting or archiving a list via swipe action
    HitchRequest deletedList = null;
    HitchRequest archiveList = null;
    String deletedListName = null;
    String archivedListName = null;

    List<HitchRequest> archivedLists = new ArrayList<>();

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
                    deletedList = hitchRequestList.get(position);
                    deletedListName = deletedList.toString();

                    hitchRequestList.remove(position);
                    rvHitchRequests.removeViewAt(position);
//                    ExpandableRecyclerViewAdapter.notifyItemRemoved(position);
//                    FragmentManager fm = ((AppCompatActivity)layoutRoot.getContext()).getSupportFragmentManager();
//
//                    Fragment currentFrag = fm.findFragmentById(R.id.groupDetails_expandableRecyclerView);
//                    fm.beginTransaction().detach(currentFrag).commitNow();
//                    fm.beginTransaction().attach(currentFrag).commitNow();

                    Snackbar.make(rvHitchRequests, deletedListName, Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener(){

                                @Override
                                public void onClick(View v) {
                                    hitchRequestList.add(position, deletedList);
//                                    ExpandableRecyclerViewAdapter.notifyItemInserted(position);
                                }
                            }).show();
                    break;
                case ItemTouchHelper.RIGHT:
                    int hitchRqId = hitchRequestList.get(position).getId();     //get id of hitchRq
                    approveHitchRqToServer(hitchRqId);  //send http rq to server to approve

                    archiveList = hitchRequestList.get(position);
                    archivedListName = archiveList.toString();
                    archivedLists.add(archiveList);

                    hitchRequestList.remove(position);

                    FragmentManager fm = ((AppCompatActivity)layoutRoot.getContext()).getSupportFragmentManager();
                    Fragment currentFrag = fm.findFragmentByTag("group_details_frag");
                    fm.beginTransaction().detach(currentFrag).commitNow();
                    fm.beginTransaction().attach(currentFrag).commitNow();

//                    ExpandableRecyclerViewAdapter.notifyItemRemoved(position);

                    //had to comment out below or there was an error, not sure what this should do (austin)
//                    Snackbar.make(rvHitchRequests, archivedListName, Snackbar.LENGTH_LONG)
//                            .setAction("Undo", new View.OnClickListener(){
//
//                                @Override
//                                public void onClick(View v) {
//                                    archivedLists.remove(archivedLists.lastIndexOf(archiveList));
//                                    hitchRequestList.add(position, archiveList);
////                                    ExpandableRecyclerViewAdapter.notifyItemInserted(position);
//                                }
//                            }).show();
                    break;
            }
        }

        //swipe action item decorator (set color and icon)
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(GroupDetailsFragment.this.getContext(), R.color.Kampong_Blue))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_cancel_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(GroupDetailsFragment.this.getContext(), R.color.Kampong_Yellow))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_check_circle_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
}
