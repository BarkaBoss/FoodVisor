package xyz.nokt.btf.foodadvisor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    //Declare UI elements
    EditText edEmail, edPassword, edPhone, edFirstName, edLastName;
    Button btnSignUp;

    //Declare string data for login data to be passed to next activity
    String userRole, email, phoneNumber, firstName, lastName;

    //Declare and initialize String keys to make reference for data in new activity
    final String addUserRole = "role";
    final String uMail = "email";
    final String uNumber = "phone";
    final String uFirstName = "firstName";
    final String uLastName = "lastName";

    //Declare firebase authentication
    FirebaseAuth fbAuth;
    //Initialize an instance of firebase database to be ready for data storage
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //Create a "User" branch in the database to store all users
    DatabaseReference mRef = database.getReference("User");

    ProgressBar pgRBar;

    //onCreate is the first method called in every page and it render the corrent UI
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initialize firebase auth
        fbAuth = FirebaseAuth.getInstance();

        //Attach UI elements to Java objects
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        edPhone = findViewById(R.id.edPhoneNumber);
        edFirstName = findViewById(R.id.edFirstName);
        edLastName = findViewById(R.id.edLastName);

        pgRBar = findViewById(R.id.regProgressBar);
        pgRBar.setVisibility(View.GONE);

        btnSignUp = findViewById(R.id.btnRegister);

        //set onClick Listener to register a user when the button
        //is clicked which calls the registerUser() method
        //which performs the registration action
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    //onStart is the first method that is called when a page
    //is loaded and we use the method to make sure there is
    //no currently logged in user
    @Override
    protected void onStart() {
        super.onStart();

        //so if user is not empty meaning a user
        //already exists we can just log them in
        if(fbAuth.getCurrentUser() != null)
        {
            Users users = new Users();
            final String fbUser = fbAuth.getCurrentUser().getUid();
            //String userID = mRef.getKey().toString().trim();
            database.getReference("User").child(fbUser)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Data is loaded into initialy declare string data
                            userRole = dataSnapshot.child("role").getValue().toString().trim();
                            email = dataSnapshot.child("email").getValue().toString().trim();
                            phoneNumber = dataSnapshot.child("phoneNumber").getValue().toString().trim();
                            firstName = dataSnapshot.child("firstName").getValue().toString().trim();
                            lastName = dataSnapshot.child("lastName").getValue().toString().trim();


                            //An intent is to the home page is created and
                            //User data is stored in the intent so we can have the data
                            //In the next page
                            Intent customerIntent = new Intent(Register.this, MainActivity.class);
                            customerIntent.putExtra(uMail, email);
                            customerIntent.putExtra(uNumber,phoneNumber);
                            customerIntent.putExtra(uFirstName, firstName);
                            customerIntent.putExtra(uLastName, lastName);
                            customerIntent.putExtra(addUserRole, userRole);
                            customerIntent.putExtra("uid", fbUser);
                            Log.i("User Role", userRole);
                            //We load the activity if the user role is customer
                            if(userRole.equals("customer"))
                            {
                                startActivity(customerIntent);
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
            //String mail = fbAuth.getCurrentUser().getEmail().toString().trim();

        }
    }

    private void registerUser()
    {
        //We get the text from all the fields and covert to strings
        //to be uploaded to firebase
        final String strEmail = edEmail.getText().toString().trim();
        final String strPassword = edPassword.getText().toString().trim();
        final String strPhone = edPhone.getText().toString().trim();
        final String strFirstName = edFirstName.getText().toString().trim();
        final String strLastName = edLastName.getText().toString().trim();
        final String strRole = "customer";

        //Start check
        //We check to make the fields are not empty
        if (strFirstName.isEmpty()) {
            edFirstName.setError("Enter your First Name");
            edFirstName.requestFocus();
            return;
        }

        if (strLastName.isEmpty()) {
            edLastName.setError("Enter your Last Name");
            edLastName.requestFocus();
            return;
        }

        if (strEmail.isEmpty()) {
            edEmail.setError("Enter your email address");
            edEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
            edEmail.setError("Enter a valid Email address");
            edEmail.requestFocus();
            return;
        }

        if (strPassword.isEmpty()) {
            edPassword.setError("Enter a password");
            edPassword.requestFocus();
            return;
        }

        if (strPassword.length() < 6) {
            edPassword.setError("Password too short");
            edPassword.requestFocus();
            return;
        }
        //End check


        pgRBar.setVisibility(View.VISIBLE);

        //we the call firebase create User with email and password
        // to create a new user
        fbAuth.createUserWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //if the user is created successfully
                        //we can then upload their data to the "User"
                        //subtree in firebase
                        if (task.isSuccessful())
                        {
                            //Pass the user data into our Users Object
                            Users users = new Users(
                                    strEmail,
                                    strPhone,
                                    strFirstName,
                                    strLastName,
                                    strRole
                            );

                            //We the set the value of our current user to the "users object"
                            //that holds our user data
                            FirebaseDatabase.getInstance().getReference("User")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    pgRBar.setVisibility(View.GONE);
                                    if (task.isSuccessful())
                                    {
                                        //When the task is completed you can now get a success message
                                        Toast.makeText(getApplicationContext(),
                                                "Registration Successful",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        //or a failed message
                                        Toast.makeText(getApplicationContext(),
                                                "Registration Failed",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),
                                    task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
