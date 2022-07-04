package com.iiitd.bookmysport.bottomnavbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.common.base.CharMatcher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iiitd.bookmysport.R;
import com.iiitd.bookmysport.other.Login;

public class Profile extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener mAuth;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView nametv = findViewById(R.id.nameTV);
        TextView emailtv = findViewById(R.id.emailTV);
        TextView rollnotv = findViewById(R.id.rollnoTV);
        TextView batchtv = findViewById(R.id.batchTV);
        imageView = findViewById(R.id.imageView2);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getDisplayName();
        String email = user.getEmail();
        String photo = user.getPhotoUrl().toString();

        String ss = email.split("@")[0];
        String theDigits = CharMatcher.inRange('0', '9').retainFrom(ss);
        String rollno = "-";
        String batch = "Phd./Staff";
        if(theDigits!= null && !theDigits.isEmpty()) {
            rollno = "20" + theDigits;
            batch = "B.Tech/M.Tech";
        }

        nametv.setText(name);
        emailtv.setText(email);
        Glide.with(this).load(photo).dontAnimate().into(imageView);
        rollnotv.setText(rollno);
        batchtv.setText(batch);


        setupFirebaseListener();

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Logout", "Attempting to Log out");
                FirebaseAuth.getInstance().signOut();
            }
        });

        // BOTTOM NAVIGATION BAR
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.profileTab);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sportsTab:
                        //Toast.makeText(Profile.this,"Sports Tab",Toast.LENGTH_SHORT).show();
                        Intent a = new Intent(Profile.this, RecyclerViewSports.class);
                        startActivity(a);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.mybookingsTab:
                        //Toast.makeText(Profile.this,"My Bookings Tab",Toast.LENGTH_SHORT).show();
                        Intent b = new Intent(Profile.this, MyBookings.class);
                        startActivity(b);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.profileTab:
                        //Toast.makeText(Profile.this,"Profile Tab",Toast.LENGTH_SHORT).show();
                        break;

                }
                return false;
            }
        });
        //
    }

    private void setupFirebaseListener() {

        Log.i("info", "Setting up the auth state listener");
        mAuth = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.i("info", "signed in");
                } else {
                    Log.i("info", "signed out");
                    Intent intent = new Intent(Profile.this, Login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("signOut", 1);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuth);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuth != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuth);
        }

    }

}