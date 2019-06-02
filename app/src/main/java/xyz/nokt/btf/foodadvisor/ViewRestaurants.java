package xyz.nokt.btf.foodadvisor;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ViewRestaurants extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener{

    //Declare in initialize FirebaseDatabase so we can view
    //save data
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //the branch we wish to view if the "Restaurants" branch
    DatabaseReference dbRef = database.getReference("Restaurants");
    // Declare firebase authentication
    FirebaseAuth fireAuth;

    // We the create a list of our restaurants object so we can
    //store all received restaurants
    List<RestaurantObj> restaurantObjs;

    //Declare a recyclerView which renders the list of Restaurants
    RecyclerView recyclerView;
    //Declare Our  Restaurant list adapter which holds restaurant list that
    //is passed to the recyclerView
    Bundle bundle =  new Bundle();
    private RestaurantListAdapter restaurantListAdapter;

    private OnFragmentInteractionListener mListener;

    public ViewRestaurants() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_restaurants, container, false);

        //initialize our Restaurant list
        restaurantObjs = new ArrayList<>();
        //initialize firebase authentication
        fireAuth = FirebaseAuth.getInstance();

        //Attach our recyclerView to the XML element
        recyclerView = rootView.findViewById(R.id.recycle_rests_list);
        recyclerView.setHasFixedSize(true);
        //Set the recyclerView layout to Linear so out list in vertical
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // we then load the restaurants via the loadRests() method
        loadRests();
        setHasOptionsMenu(true);
        //loadDialog();
        return rootView;
    }

    public void loadDialog()
    {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_nav, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Food Adivsor");
        final AlertDialog ab = dialogBuilder.create();

        final TextView tvMessage = dialogView.findViewById(R.id.tvDiagMessage);
        final Button btnYes = dialogView.findViewById(R.id.btnYes);
        final Button btnNo = dialogView.findViewById(R.id.btnNo);

        tvMessage.setText("Would you like to search for Restaurants based on your preference?");
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDietDialog();
                ab.dismiss();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ab.dismiss();
            }
        });


        ab.show();
    }

    public void loadDietDialog()
    {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_nav, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Food Adivsor");
        final AlertDialog ab = dialogBuilder.create();

        final TextView tvMessage = dialogView.findViewById(R.id.tvDiagMessage);
        final Button btnYes = dialogView.findViewById(R.id.btnYes);
        final Button btnNo = dialogView.findViewById(R.id.btnNo);

        tvMessage.setText("Search for Restaurants based on Dietary Restrictions?");
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("Diet", "Diet");

                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                AutoRecommendedRestaurant autoRec = new AutoRecommendedRestaurant();
                autoRec.setArguments(bundle);
                ft.replace(R.id.frag_main, autoRec)
                        .commit();

                ab.dismiss();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadLocalForeign();
                ab.dismiss();
            }
        });
        ab.show();
    }

    public void loadLocalForeign()
    {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_nav, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Food Adivsor");
        final AlertDialog ab = dialogBuilder.create();

        final TextView tvMessage = dialogView.findViewById(R.id.tvDiagMessage);
        final Button btnYes = dialogView.findViewById(R.id.btnYes);
        final Button btnNo = dialogView.findViewById(R.id.btnNo);


        btnYes.setText("Foreign");
        btnNo.setText("Local");

        tvMessage.setText("Do you want to eat at a Foreign or local Restaurant?");

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("localForeign", "Foreign");

                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                AutoRecommendedRestaurant autoRec = new AutoRecommendedRestaurant();
                autoRec.setArguments(bundle);
                ft.replace(R.id.frag_main, autoRec)
                        .commit();
                ab.dismiss();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bundle.putString("localForeign", "Local");

                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                AutoRecommendedRestaurant autoRec = new AutoRecommendedRestaurant();
                autoRec.setArguments(bundle);
                ft.replace(R.id.frag_main, autoRec)
                        .commit();
                ab.dismiss();
            }
        });
        ab.show();
    }

    //load the restaurants
    public void loadRests()
    {
        //we access our "Restaurants" branch using the already declared
        //dbRef object
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               //we first clear the list if it already loaded
                // to avoid duplicates
                restaurantObjs.clear();

                //We the iterate through the "Restaurants" branch
                //Getting all the children of the branch
               for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
               {
                   //We avoid null exception by making sure data exists
                   if(dataSnapshot.exists())
                   {
                       //Store all instance of restaurants in our object
                       RestaurantObj restObj = dataSnapshot.child(postSnapshot.getKey()).getValue(RestaurantObj.class);
                       restaurantObjs.add(restObj);
                   }
                   else {
                       //if their are no restaurants available
                       Log.i("NothingRest", "No restaurants");
                   }
               }
               //initialize our adapter
                restaurantListAdapter = new RestaurantListAdapter(getActivity(), getContext(), restaurantObjs);
               //Attach our adapter to the recyler view so the user can view in a list
               recyclerView.setAdapter(restaurantListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    ArrayList<RestaurantObj> newlist;
    //Perform search function
    @Override
    public boolean onQueryTextChange(String newText) {

        newText=newText.toLowerCase();
        newlist=new ArrayList<>();
        //Check through the list of restaurants if any one has
        //the searched term
        for(RestaurantObj itms:restaurantObjs)
        {
            String getSearchedIems=itms.getRest_features().toLowerCase();
            String getName = itms.getRest_name().toLowerCase();
            String getAddress = itms.getRest_address().toLowerCase();

            if(getSearchedIems.contains(newText) || getName.contains(newText) || getAddress.contains(newText)){
                newlist.add(itms);
            }
        }
        //If there is a match the adapter is updated with the
        //holding just those items
        restaurantListAdapter.filter(newlist);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("List of Restaurants");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        MenuItem menuItem=menu.findItem(R.id.actionsearch);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        restaurantListAdapter.filter(newlist);
        return true;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
