package xyz.nokt.btf.foodadvisor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class DetailedRestaurantActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleMap mMap;

    TextView restName, restPhone, restAddress, restCallin;
    ImageView banner;

    String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_restaurant);

        restName = findViewById(R.id.restName);
        restPhone = findViewById(R.id.restPhone);
        restAddress = findViewById(R.id.restAddress);
        restCallin = findViewById(R.id.restCallin);
        banner = findViewById(R.id.bBanner);

        Picasso.get().load(getIntent()
        .getStringExtra("imageBanner")).into(banner);

        restPhone.setText(getIntent().getStringExtra("phone"));
        restName.setText(getIntent().getStringExtra("restName"));
        restAddress.setText(getIntent().getStringExtra("restAddress"));


        address = getIntent().getStringExtra("restAddress");

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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));


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
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLatitude())));
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
