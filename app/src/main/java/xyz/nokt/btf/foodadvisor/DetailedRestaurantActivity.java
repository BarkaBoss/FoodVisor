package xyz.nokt.btf.foodadvisor;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailedRestaurantActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleMap mMap;

    TextView restName, restPhone, restAddress, restCallin, restMail, restFeat, ratings;
    FloatingActionButton fab;
    ImageView banner;
    LatLng userLoc, restLoc;
    FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = fbDatabase.getReference("comments");

    String address, restID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_restaurant);

        restName = findViewById(R.id.restName);
        restPhone = findViewById(R.id.restPhone);
        restAddress = findViewById(R.id.restAddress);
        restCallin = findViewById(R.id.restCallin);
        banner = findViewById(R.id.bBanner);
        restMail = findViewById(R.id.restMail);
        restFeat = findViewById(R.id.restFeat);
        ratings = findViewById(R.id.tvRating);
        fab = findViewById(R.id.fabRate);

        Picasso.get().load(getIntent()
        .getStringExtra("imageBanner")).into(banner);

        restPhone.setText(getIntent().getStringExtra("phone"));
        restName.setText(getIntent().getStringExtra("restName"));
        restAddress.setText(getIntent().getStringExtra("restAddress"));
        restMail.setText(getIntent().getStringExtra("mail"));
        restFeat.setText(getIntent().getStringExtra("feat"));
        restID = getIntent().getStringExtra("restID");



        address = getIntent().getStringExtra("restAddress");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDialog();
            }
        });
        ratings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailedRestaurantActivity.this, Reviews.class);
                intent.putExtra("restRateID", restID);
                startActivity(intent);
            }
        });
        restCallin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DetailedRestaurantActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 104);
                }
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    Intent callRest = new Intent(Intent.ACTION_CALL);
                    callRest.setData(Uri.parse("tel:"+getIntent().getStringExtra("phone")));
                    startActivity(callRest);
                }
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    public void loadDialog()
    {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.ratings, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Review this Restaurant");
        final AlertDialog ab = dialogBuilder.create();

        final EditText tvTitle = dialogView.findViewById(R.id.edRateTitle);
        final EditText tvMessage = dialogView.findViewById(R.id.edRateDescriprion);
        final Button btnRate = dialogView.findViewById(R.id.btnRate);


        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //loadDietDialog();

                String id = dbRef.push().getKey();

                Comments comments = new Comments(
                        id,
                        tvTitle.getText().toString(),
                        tvMessage.getText().toString()
                );
                dbRef.child(restID).child(id).setValue(comments);

                Toast.makeText(getApplicationContext(), "Review Recieved", Toast.LENGTH_SHORT).show();
                ab.dismiss();
            }
        });

        ab.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLatitude()))
                            .title("You"));
                    userLoc = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLoc));

                    //Plot Route Code

                    List<LatLng> path = new ArrayList();
                    restLoc = getLatLongFromAdd(getBaseContext(), address);

                    //userLoc = new LatLng(8.9576992, 7.7007401);


                    //SphericalUtil.computeDistanceBetween(passLoc, path.get(0));
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);

                    GeoApiContext context = new GeoApiContext.Builder()
                            .apiKey("AIzaSyAK_TyRo84WCqj5CI14wisdSTya4Vdxehk")
                            .build();

                    mMap.addMarker(new MarkerOptions().position(userLoc).title("You are here"));
                    DirectionsApiRequest req = DirectionsApi.getDirections(context, Double.toString(userLoc.latitude)+","+Double.toString(userLoc.longitude), Double.toString(restLoc.latitude)+","+Double.toString(restLoc.longitude));
                    //DirectionsApiRequest req = DirectionsApi.getDirections(context, "8.9576992, 7.7007401", "6.595680,3.337030");
                    try {
                        DirectionsResult res = req.await();

                        //Loop through legs and steps to get encoded polylines of each step
                        if (res.routes != null && res.routes.length > 0) {
                            DirectionsRoute route = res.routes[0];

                            if (route.legs !=null) {
                                for(int i=0; i<route.legs.length; i++) {
                                    DirectionsLeg leg = route.legs[i];
                                    if (leg.steps != null) {
                                        for (int j=0; j<leg.steps.length;j++){
                                            DirectionsStep step = leg.steps[j];
                                            if (step.steps != null && step.steps.length >0) {
                                                for (int k=0; k<step.steps.length;k++){
                                                    DirectionsStep step1 = step.steps[k];
                                                    EncodedPolyline points1 = step1.polyline;
                                                    if (points1 != null) {
                                                        //Decode polyline and add points to list of route coordinates
                                                        List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                                        for (com.google.maps.model.LatLng coord1 : coords1) {
                                                            path.add(new LatLng(coord1.lat, coord1.lng));
                                                        }
                                                    }
                                                }
                                            } else {
                                                EncodedPolyline points = step.polyline;
                                                if (points != null) {
                                                    //Decode polyline and add points to list of route coordinates
                                                    List<com.google.maps.model.LatLng> coords = points.decodePath();
                                                    for (com.google.maps.model.LatLng coord : coords) {
                                                        path.add(new LatLng(coord.lat, coord.lng));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch(Exception ex) {
                        Log.e("Localised", ex.getLocalizedMessage());
                    }

                    //Draw the polyline
                    if (path.size() > 0) {
                        PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
                        mMap.addPolyline(opts);
                    }
                    //End Plot Route
                }
            });
        }
    }

    private LatLng getLatLongFromAdd(Context context, String strAddress)
    {
        Geocoder gCoder = new Geocoder(context);
        List<Address> addresses;
        LatLng addLL = null;

        try{
            addresses = gCoder.getFromLocationName(strAddress, 1);
            if(addresses == null)
            {
                Toast.makeText(context, "Location not found", Toast.LENGTH_LONG).show();
            }

            Address loc = addresses.get(0);
            addLL = new LatLng(loc.getLatitude(), loc.getLongitude());
        }catch (IOException ioEx)
        {
            ioEx.printStackTrace();
        }

        return addLL;
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
