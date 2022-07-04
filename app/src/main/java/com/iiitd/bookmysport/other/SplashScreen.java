package com.iiitd.bookmysport.other;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iiitd.bookmysport.R;
import com.iiitd.bookmysport.bottomnavbar.RecyclerViewSports;

public class SplashScreen extends AppCompatActivity {

    private Boolean userLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userLoggedIn = currentUser != null;


        Handler handler = new Handler();
        int SPLASH_TIME = 500;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intentMainActivity = new Intent(SplashScreen.this, RecyclerViewSports.class);
                Intent intentLogin = new Intent(SplashScreen.this, Login.class);
                if (userLoggedIn) {
                    startActivity(intentMainActivity);
                } else {
                    startActivity(intentLogin);
                }
                finish();
            }
        }, SPLASH_TIME);

    }

}
