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

public class AutoRecommendedRestaurant extends Fragment implements SearchView.OnQueryTextListener{

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference("Restaurants");
    FirebaseAuth fireAuth;

    List<RestaurantObj> restaurantObjs;
    RecyclerView recyclerView;
    private RestaurantListAdapter restaurantListAdapter;

    private ViewRestaurants.OnFragmentInteractionListener mListener;

    public AutoRecommendedRestaurant() {
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

        restaurantObjs = new ArrayList<>();
        fireAuth = FirebaseAuth.getInstance();
        recyclerView = rootView.findViewById(R.id.recycle_rests_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadRests();
        return rootView;
    }

    public void loadRests()
    {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                restaurantObjs.clear();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren())
                {
                    if(dataSnapshot.exists())
                    {
                        RestaurantObj restObj = dataSnapshot.child(postSnapshot.getKey()).getValue(RestaurantObj.class);
                        restaurantObjs.add(restObj);
                    }
                    else {
                        Log.i("NothingRest", "No restaurants");
                    }
                }
                RestaurantListAdapter restAdapter = new RestaurantListAdapter(getActivity(), getContext(), restaurantObjs);
                recyclerView.setAdapter(restAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ViewRestaurants.OnFragmentInteractionListener) {
            mListener = (ViewRestaurants.OnFragmentInteractionListener) context;
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

    @Override
    public boolean onQueryTextChange(String newText) {
        newText=newText.toLowerCase();
        ArrayList<RestaurantObj> newlist=new ArrayList<>();
        for(RestaurantObj itms:restaurantObjs)
        {
            String getSearchedIems=itms.getRest_features().toLowerCase();
            String getName = itms.getRest_name().toLowerCase();

            if(getSearchedIems.contains(newText) || getName.contains(newText)){
                newlist.add(itms);
            }
        }
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
