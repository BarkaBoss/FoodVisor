package xyz.nokt.btf.foodadvisor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Reviews extends AppCompatActivity {

    FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = fbDatabase.getReference("comments");
    String restID;
//    DatabaseReference revRef = dbRef.child(restID);

    RecyclerView recyclerView;
    List<Comments> reviewsList;
    ReviewAdapter reviewAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        reviewsList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycle_rating_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        restID = getIntent().getStringExtra("restRateID");
        loadReviews();
    }

    public void loadReviews()
    {
        dbRef.child(restID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reviewsList.clear();
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    if(dataSnapshot.exists())
                    {
                        String key = postSnapshot.getKey();
                        String lowerKey = postSnapshot.child(key).getKey();

                        Log.i("mainKey", key);
                        Log.i("lowerKey", lowerKey);
                        Comments reviews = dataSnapshot.child(lowerKey).getValue(Comments.class);
                        reviewsList.add(reviews);
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), "None", Toast.LENGTH_SHORT).show();
                    }
                }

                reviewAdapter = new ReviewAdapter(Reviews.this, reviewsList, getApplicationContext());
                recyclerView.setAdapter(reviewAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
