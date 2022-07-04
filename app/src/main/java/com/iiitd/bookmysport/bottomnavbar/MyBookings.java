package com.iiitd.bookmysport.bottomnavbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.iiitd.bookmysport.R;
import com.iiitd.bookmysport.model_myBookings;
import com.iiitd.bookmysport.recyclerview.RvAdapter_mybookings;

import java.util.ArrayList;
import java.util.List;

public class MyBookings extends AppCompatActivity implements RvAdapter_mybookings.onListListener {

    private RvAdapter_mybookings adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        RecyclerView mybookingsrv = findViewById(R.id.rv_mybookings);
        mybookingsrv.setLayoutManager(new LinearLayoutManager(this));
        List<model_myBookings> myBookings = new ArrayList<>();
        adapter = new RvAdapter_mybookings(MyBookings.this, myBookings, this);
        mybookingsrv.setAdapter(adapter);


        // BOTTOM NAVIGATION BAR
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.mybookingsTab);

        DocumentReference documentReference = db.collection("Users").document(email);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(MyBookings.this, "Error in Reading Database", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (documentSnapshot.exists()) {
                    List<String> newdetails = (List<String>) documentSnapshot.get("bookings");
                    List<model_myBookings> newmyBookings = new ArrayList<>();
                    for (int i = 0; i < newdetails.size(); i++) {
                        newmyBookings.add(new model_myBookings((String) newdetails.get(i)));
                    }
                    adapter.update(newmyBookings);
                }
            }
        });

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sportsTab:
                        Intent a = new Intent(MyBookings.this, RecyclerViewSports.class);
                        startActivity(a);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.mybookingsTab:
                        break;
                    case R.id.profileTab:
                        Intent b = new Intent(MyBookings.this, Profile.class);
                        startActivity(b);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onListClick(int pos) {

    }

}