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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ad_project_kampung_unite.adaptors.ActiveGroupExpandableRecyclerViewAdapter;
import com.example.ad_project_kampung_unite.adaptors.ArchivedGroupExpandableRecyclerViewAdapter;
import com.example.ad_project_kampung_unite.adaptors.GroceryListItemAdaptor;
import com.example.ad_project_kampung_unite.data.remote.GroceryItemService;
import com.example.ad_project_kampung_unite.data.remote.GroupPlanService;
import com.example.ad_project_kampung_unite.data.remote.HitchRequestService;
import com.example.ad_project_kampung_unite.data.remote.RetrofitClient;
import com.example.ad_project_kampung_unite.entities.CombinedPurchaseList;
import com.example.ad_project_kampung_unite.entities.GroceryItem;
import com.example.ad_project_kampung_unite.entities.GroupPlan;
import com.example.ad_project_kampung_unite.entities.GroupPlan;
import com.example.ad_project_kampung_unite.entities.enums.GroupPlanStatus;
import com.example.ad_project_kampung_unite.entities.enums.RequestStatus;
import com.google.android.material.button.MaterialButton;
import com.example.ad_project_kampung_unite.entities.HitchRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GroupDetailsFragment extends Fragment {

    private int groupId;
    private String groupStatus;
    private List<GroceryItem> buyerGroceryItemList = new ArrayList<>();
    private List<GroceryItem> hitcherGroceryItemList = new ArrayList<>();

    private List<HitchRequest> hitchRequestList = new ArrayList<>();
    private List<List<GroceryItem>> childListHolder = new ArrayList<>();

    List<Integer> hitchids = new ArrayList<>();
    private GroupPlan groupPlan;

    private RecyclerView rvBuyerGrocery,rvHitchRequests;
    private ImageButton editBtn;
    private MaterialButton closeRequestBtn, combinedListBtn;

    private GroupPlanService groupPlanService;
    private GroceryItemService groceryItemService;
    private HitchRequestService hitchRequestService;

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
        groupStatus = bundle.getString("gpStatus");

        //if group status is not "available", buyer cannot edit their own grocery list
        editBtn = layoutRoot.findViewById(R.id.groupDetails_buyerEditBtn);
        if(groupStatus!="Available"){
            editBtn.setVisibility(View.GONE);
        }
        //buyer can edit their own grocery list
        //pending to-do: pass buyer id to 'grocery list' fragment
        else{
              editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = getParentFragmentManager();
//                    GroceryListFragment groceryListFragment = new GroceryListFragment();
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.fragment_container,groceryListFragment)
//                            .addToBackStack(null)
//                            .commit();
                }
            });
        }

        groceryItemService = RetrofitClient.createService(GroceryItemService.class);
        getBuyerGroceryItemsFromServer();

        hitchRequestService = RetrofitClient.createService(HitchRequestService.class);
        getHitchRequestsFromServer();

        groupPlanService = RetrofitClient.createService(GroupPlanService.class);

        closeRequestBtn = layoutRoot.findViewById(R.id.closeRequestButton);
        if(groupStatus!="Available"){
            closeRequestBtn.setVisibility(View.INVISIBLE);
        }
        else{
            closeRequestBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Create dialog to confirm close requests
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setMessage("Stop accepting hitch request for this group plan?");

                    alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //Create second dialog to confirm rejecting all pending purchase requests, before closing the group plan request
                            alert.setMessage("Reject all pending requests and close requests?");
                            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //change Group plan status to "closed" and update pending requests status to "rejected"
                                    //change all hitch request with status "Pending approval" to "Rejected"
                                    hitchRequestList.stream().forEach(x->{
                                        if(x.getRequestStatus().equals(RequestStatus.PENDING)){
                                            x.setRequestStatus(RequestStatus.REJECTED);
                                            updateHitchRequestStatusToServer(x);
                                        }
                                    });
                                    Toast.makeText(getContext(), "Stopped accepting new hitch requests", Toast.LENGTH_LONG).show();
                                    //change group plan status to Closed
                                    System.out.println(GroupPlanStatus.CLOSED);
                                    updateGroupPlanStatusToServer(groupId,GroupPlanStatus.CLOSED);

                                    FragmentManager fm = ((AppCompatActivity)layoutRoot.getContext()).getSupportFragmentManager();
                                    Fragment currentFrag = fm.findFragmentByTag("GROUP_DETAILS_FRAG");
                                    fm.beginTransaction().detach(currentFrag).commitNow();
                                    fm.beginTransaction().attach(currentFrag).commitNow();

                                    closeRequestBtn.setVisibility(View.INVISIBLE);
                                }
                            });
                            alert.show();
                        }
                    });

                    alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });

                    alert.show();
                }
            });
        }

        //Button to link to Combined List Fragment
        combinedListBtn = layoutRoot.findViewById(R.id.combinedListButton);
        combinedListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //navigate to CombinedList Fragment
                Bundle bundle = new Bundle();
                bundle.putInt("gpId", groupId);
                FragmentManager fragmentManager = getParentFragmentManager();
                CombinedListFragment combinedListFragment = new CombinedListFragment();
                combinedListFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container,combinedListFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


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
        Call<List<HitchRequest>> call = hitchRequestService.getHitchRequestsByGroupPlanId(groupId);

        call.enqueue(new Callback<List<HitchRequest>>() {
            @Override
            public void onResponse(Call<List<HitchRequest>> call, Response<List<HitchRequest>> response) {

                if (response.isSuccessful()) {
                    hitchRequestList = response.body();
                    Log.d("Success", String.valueOf(hitchRequestList.get(0).toString())); //for testing

                        //recycler view for grocery items

                    for(int i = 0; i<hitchRequestList.size(); i++){
                        int hitchRequestId = hitchRequestList.get(i).getId();
                        // make individual request to fetch grocery items based on hitch request id
//                        testing(hitchRequestId);
                        hitchids.add(hitchRequestId);
                    }

                    getHitcherGroceryItemsFromServer(hitchids);

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

    //get grocery items for each hitcher's grocery list
    private void getHitcherGroceryItemsFromServer(List<Integer> ids){
        Call<List<List<GroceryItem>>> call = groceryItemService.findGroceryItemsByHitchRequests(ids);

        call.enqueue(new Callback<List<List<GroceryItem>>>() {
            @Override
            public void onResponse(Call<List<List<GroceryItem>>> call, Response<List<List<GroceryItem>>> response) {

                if (response.isSuccessful()) {
                    childListHolder = response.body();
                    Log.d("Success", String.valueOf(childListHolder.get(0).toString())); //for testing
                    System.out.println("childlistholder updated ");

                    getGroupPlanFromServer();
                    //inflate recycler view for all hitch requests and grocery items
/*                    if(groupStatus=="Available")
                        initiateExpanderForActiveList();
                    else
                        initiateExpanderForArchivedList();*/    //move to inside of getGroupPlanFromServer() method

                } else {
                    Log.e("Error", response.errorBody().toString());
                    System.out.println("childlistholder not updated ");
                }
            }

            @Override
            public void onFailure(Call<List<List<GroceryItem>>> call, Throwable t) {
                // like no internet connection / the website doesn't exist
                call.cancel();
                Log.w("Failure", "Failure!");
                t.printStackTrace();
            }
        });
    }

    private void getGroupPlanFromServer() {
        groupPlanService = RetrofitClient.createService(GroupPlanService.class);
        Call<GroupPlan> call = groupPlanService.getGroupPlanById(groupId);

        call.enqueue(new Callback<GroupPlan>() {
            @Override
            public void onResponse(Call<GroupPlan> call, Response<GroupPlan> response) {
                if (response.isSuccessful()) {
                    groupPlan = response.body();

                    //inflate recycler view for all hitch requests and grocery items
                    if(groupStatus=="Available")
                        initiateExpanderForActiveList();
                    else
                        initiateExpanderForArchivedList(); //invisible
                } else {
                    Log.e("getGroupPlanById Error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<GroupPlan> call, Throwable t) {
                // like no internet connection / the website doesn't exist
                call.cancel();
                Log.w("Failure", "Failure!");
                t.printStackTrace();
            }
        });
    }

    private void updateHitchRequestStatusToServer(HitchRequest hitchRequest){
        Call<HitchRequest> call = hitchRequestService.updateHitchRequest(hitchRequest);

        call.enqueue(new Callback<HitchRequest>() {
            @Override
            public void onResponse(Call<HitchRequest> call, Response<HitchRequest> response) {

                if (response.isSuccessful()) {
                    Log.d("Success, hitchrequests updated", String.valueOf(hitchRequest)); //for testing

                } else {
                    Log.e("Error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<HitchRequest> call, Throwable t) {
                // like no internet connection / the website doesn't exist
                call.cancel();
                Log.w("Failure", "Failure!");
                t.printStackTrace();
            }
        });
    }

    private void updateGroupPlanStatusToServer(int id, GroupPlanStatus myUpdatedGPStatus){
        Call<Void> call = groupPlanService.updateGroupPlanStatus(id, myUpdatedGPStatus);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.isSuccessful()) {
                    Log.d("Success, hitchrequests updated", response.toString()); //for testing

                } else {
                    Log.e("Error", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
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
            public void onFailure(Call<Boolean> call, Throwable t) {
                call.cancel();
                Log.w("Failure", "Failure!");
                t.printStackTrace();
            }
        });
    }

    private void initiateExpanderForActiveList() {

        rvHitchRequests = layoutRoot.findViewById(R.id.groupDetails_expandableRecyclerView);

        ActiveGroupExpandableRecyclerViewAdapter expandableCategoryRecyclerViewAdapter =
                new ActiveGroupExpandableRecyclerViewAdapter(layoutRoot.getContext(), hitchRequestList,
                        childListHolder, groupPlan.getGroupPlanStatus());

        rvHitchRequests.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));
        rvHitchRequests.setAdapter(expandableCategoryRecyclerViewAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rvHitchRequests);
    }

    private void initiateExpanderForArchivedList() {

        rvHitchRequests = layoutRoot.findViewById(R.id.groupDetails_expandableRecyclerView);

        ArchivedGroupExpandableRecyclerViewAdapter expandableCategoryRecyclerViewAdapter =
                new ArchivedGroupExpandableRecyclerViewAdapter(layoutRoot.getContext(), hitchRequestList,
                        childListHolder, groupPlan.getGroupPlanStatus());

        rvHitchRequests.setLayoutManager(new LinearLayoutManager(layoutRoot.getContext()));
        rvHitchRequests.setAdapter(expandableCategoryRecyclerViewAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rvHitchRequests);
    }


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
//                    ExpandableRecyclerViewAdapter.notifyItemRemoved(position);

                    Snackbar.make(rvHitchRequests, archivedListName, Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener(){

                                @Override
                                public void onClick(View v) {
                                    archivedLists.remove(archivedLists.lastIndexOf(archiveList));
                                    hitchRequestList.add(position, archiveList);
//                                    ExpandableRecyclerViewAdapter.notifyItemInserted(position);
                                }
                            }).show();


                    try {
                        Thread.sleep(250);      //need to wait or database hasn't updated...
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //below to refresh the ui after accepting hitch rq
                    FragmentManager fm = ((AppCompatActivity)layoutRoot.getContext()).getSupportFragmentManager();
                    Fragment currentFrag = fm.findFragmentByTag("GROUP_DETAILS_FRAG");
                    fm.beginTransaction().detach(currentFrag).commitNow();
                    fm.beginTransaction().attach(currentFrag).commitNow();

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
