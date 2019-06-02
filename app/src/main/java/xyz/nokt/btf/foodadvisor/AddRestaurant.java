package xyz.nokt.btf.foodadvisor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddRestaurant.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddRestaurant#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddRestaurant extends Fragment{

    private OnFragmentInteractionListener mListener;

    DateFormat dateFormatWithTime = new SimpleDateFormat("dd-mm-yy HH:mm:ss");
    Date date = new Date();

    EditText edRestName, edRestMail, edRestPhone, edRestAddress, edFeatures;
    Spinner localConti, sitinOut;
    ImageView imgBanner;
    Button btnAddRest;
    TextView imgAddCam, tvCACert;
    Uri locLink;
    Uri locLinkX;

    ConstraintLayout constraintLayout;

    String strRestName, strEmail, strPhone, strAddress, strImgUrl, strImgUrlX, strFeatures, locUri, locUriX, locCont, sitInOut;

    FirebaseDatabase fireDB = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = fireDB.getReference("Restaurants");

    FirebaseStorage fireStore = FirebaseStorage.getInstance();
    StorageReference storeRef = fireStore.getReference("restaurants"+dateFormatWithTime.format(date));

    final int REQ_CODE = 42;
    final int REQ_CODX = 43;
    public AddRestaurant() {
        // Required empty public constructor
    }


    public static AddRestaurant newInstance(String param1, String param2) {
        AddRestaurant fragment = new AddRestaurant();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        View rootView = inflater.inflate(R.layout.fragment_add_restaurant, container, false);
        edRestName = rootView.findViewById(R.id.edRestName);
        edRestMail = rootView.findViewById(R.id.edRestEmail);
        edRestPhone = rootView.findViewById(R.id.edRestPhone);
        edRestAddress = rootView.findViewById(R.id.edRestAddress);
        edFeatures = rootView.findViewById(R.id.edRestFeatures);

        localConti = rootView.findViewById(R.id.spRestLocCont);
        sitinOut = rootView.findViewById(R.id.spRestSitinOut);

        imgAddCam = rootView.findViewById(R.id.imgAddCam);
        tvCACert = rootView.findViewById(R.id.addCACert);

        imgBanner = rootView.findViewById(R.id.imgBanner);

        btnAddRest = rootView.findViewById(R.id.btnAddRest);

        constraintLayout = rootView.findViewById(R.id.constraintLayout);

        imgAddCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loadFileIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                loadFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
                loadFileIntent.setType("image/*");
                startActivityForResult(loadFileIntent, REQ_CODE);
            }
        });
        tvCACert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loadFileIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                loadFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
                loadFileIntent.setType("image/*");
                startActivityForResult(loadFileIntent, REQ_CODX);
            }
        });
        btnAddRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(locUri != null)
                {
                    storeRef.putFile(locLink).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            strImgUrl = taskSnapshot.getDownloadUrl().toString();
                            addRestaurant();
                        }
                    });
                }
                else {
                    Toast.makeText(getContext(), "Make sure you attach Restaurant Photo", Toast.LENGTH_SHORT).show();
                }

                if (locUriX != null)
                {
                    storeRef.putFile(locLinkX).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            strImgUrlX = taskSnapshot.getDownloadUrl().toString();
                            addRestaurant();
                        }
                    });
                }
                else {
                    Toast.makeText(getContext(), "Make sure you attach CAC certificate", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_CODE && resultCode == Activity.RESULT_OK)
        {
            Uri uri = null;
            if(data != null)
            {
                uri = data.getData();
                locLink = uri;
                locUri = uri.toString();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), locLink);
                    imgBanner.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(requestCode == REQ_CODX && resultCode == Activity.RESULT_OK)
        {
            Uri uri = null;
            if(data != null)
            {
                uri = data.getData();
                locLinkX = uri;
                locUriX = uri.toString();

                Toast.makeText(getContext(), "Certificate Added", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addRestaurant()
    {
        strRestName = edRestName.getText().toString().trim();
        strEmail = edRestMail.getText().toString().trim();
        strPhone = edRestPhone.getText().toString().trim();
        strAddress = edRestAddress.getText().toString().trim();
        strFeatures = edFeatures.getText().toString().trim();
        locCont = localConti.getSelectedItem().toString().trim();
        sitInOut = sitinOut.getSelectedItem().toString().trim();

        String id = dbRef.push().getKey();
        RestaurantObj restaurantObj = new RestaurantObj( strRestName,
                id,
                strEmail,
                strPhone,
                strAddress,
                strImgUrl,
                strFeatures,
                locCont,
                sitInOut,
                strImgUrlX);

        dbRef.child(id).setValue(restaurantObj);

        edRestName.setText("");
        edRestMail.setText("");
        edRestPhone.setText("");
        edRestAddress.setText("");
        edFeatures.setText("");

        Toast.makeText(getContext(), "New Restaurant Uploaded", Toast.LENGTH_LONG).show();
        /*Snackbar yummy = Snackbar.make(constraintLayout, "", Snackbar.LENGTH_LONG);
        yummy.show();*/
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
