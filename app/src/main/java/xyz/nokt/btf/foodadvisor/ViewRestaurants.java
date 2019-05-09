package xyz.nokt.btf.foodadvisor;

import android.content.Context;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ViewRestaurants extends Fragment implements SearchView.OnQueryTextListener{

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
    private RestaurantListAdapter restaurantListAdapter;

    private OnFragmentInteractionListener mListener;

    public ViewRestaurants() {
        // Required empty public constructor
    }

    public static ViewRestaurants newInstance(String param1, String param2) {
        ViewRestaurants fragment = new ViewRestaurants();
        Bundle args = new Bundle();
        return fragment;
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
        return rootView;
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

    //Perform search function
    @Override
    public boolean onQueryTextChange(String newText) {

        newText=newText.toLowerCase();
        ArrayList<RestaurantObj> newlist=new ArrayList<>();
        //Check through the list of restaurants if any one has
        //the searched term
        for(RestaurantObj itms:restaurantObjs)
        {
            String getSearchedIems=itms.getRest_features().toLowerCase();
            String getName = itms.getRest_name().toLowerCase();

            if(getSearchedIems.contains(newText) || getName.contains(newText)){
                newlist.add(itms);
            }
        }
        //If there is a match the adapter is updated with the
        //holding just those items
        restaurantListAdapter.filter(newlist);
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        MenuItem menuItem=menu.findItem(R.id.actionsearch);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
