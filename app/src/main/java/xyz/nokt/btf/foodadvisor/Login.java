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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    FirebaseAuth fbAuth; // Declare firebase authentication

    //Declare UI components
    Button loginBtn, signUpBtn;
    EditText edEmail, edPassword;
    TextView tvResetLink;
    ProgressBar pgBar;

    //Initialize an instance of firebase database to be ready for data storage
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //Declare string data for login data to be passed to next activity
    String userRole, email, phoneNumber, firstName, lastName;

    //Declare and initialize String keys to make reference for data in new activity
    final String addUserRole = "role";
    final String uMail = "email";
    final String uNumber = "phone";
    final String uFirstName = "firstName";
    final String uLastName = "lastName";

    //onCreate is the method that renders the corrent UI
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fbAuth = FirebaseAuth.getInstance();//initialize firebase auth
        if (fbAuth.getCurrentUser() != null) {//check if the user is already logged in
            checkLoginInStatus();
        }
        edEmail = findViewById(R.id.edLogEmail);//attach java email field to XML element
        edPassword = findViewById(R.id.edLogPassword);//attach java password field to XML element

        tvResetLink = findViewById(R.id.tvRecievePasswordReset);//attach java password reset field to XML element

        //set onclick event listener so when the user clicks on pass reset a
        // reset link is sent to their email address
        tvResetLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check if the user entered an email address
                //if they did they are prompted to enter it
                if (edEmail.getText().toString().isEmpty()) {
                    edEmail.setError("Enter your email address");
                    edEmail.requestFocus();
                } else {
                    //if they did they will be sent an passwoord reset link
                    fbAuth.sendPasswordResetEmail(edEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),
                                        "Password Reset Sent",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                edEmail.setError("Make sure the email provided is correct");
                                edEmail.requestFocus();
                            }
                        }
                    });
                }
            }
        });

        loginBtn = findViewById(R.id.btnLogin);
        signUpBtn = findViewById(R.id.btnRegistration);

        pgBar = findViewById(R.id.logProgressBar);
        pgBar.setVisibility(View.GONE);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        //Sign up button to take the user to the signup page
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

    }

    //method to check if the user is logged in
    public void checkLoginInStatus()
    {
        //we check if the current user is not null we flag him as logged in
        if(fbAuth.getCurrentUser() != null)
        {
            //we then call the isLoggedIn method which loads
            //the current users data
            isLoggedIn();
        }
    }


        public void isLoggedIn()
        {
            //the isLoggedIn method gets the current user from firebase using their unique ID
            final String fbUser = fbAuth.getCurrentUser().getUid();
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
                            Intent customerIntent = new Intent(Login.this, MainActivity.class);
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
        }

        //Loggin the User
        private void loginUser()
        {
            //To login the user we first get the texts data and convert to Strings
            String email = edEmail.getText().toString().trim();
            String password = edPassword.getText().toString().trim();

            //We then check to make sure the fields (email and password) are not empty
            if (password.isEmpty())
            {
                edPassword.setError("Password can't be empty");
                edPassword.requestFocus();
            }

            if(email.isEmpty())
            {
                edEmail.setError("Enter your email address");
                edEmail.requestFocus();
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edEmail.setError("Enter a valid Email address");
                edEmail.requestFocus();
                return;
            }
            pgBar.setVisibility(View.VISIBLE);

            //We then call the firebase signin With password method
            //which has an onCompleteListener to check if the login is complete
            fbAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            pgBar.setVisibility(View.GONE);

                            //if the login is successful the isLoggedIn method is called
                            //to log the user in
                            if (task.isSuccessful())
                            {
                                isLoggedIn();
                                Toast.makeText(getApplicationContext(), "Login Successful",
                                        Toast.LENGTH_LONG).show();
                            }
                            //Else the user is told "some thing went wrong"
                            else {
                                Toast.makeText(getApplicationContext(), "something went wrong",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

