package com.iiitd.bookmysport.bottomnavbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iiitd.bookmysport.GuardSwimmingData;
import com.iiitd.bookmysport.R;
import com.iiitd.bookmysport.SportsSD;
import com.iiitd.bookmysport.Swimming;
import com.iiitd.bookmysport.recyclerview.RecyclerViewAdaptor;

import java.util.ArrayList;

public class RecyclerViewSports extends AppCompatActivity implements RecyclerViewAdaptor.onListListener {

    private ArrayList<String> sport_names = new ArrayList<>();
    private ArrayList<Integer> sport_images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_sports);


        // BOTTOM NAVIGATION BAR
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.sportsTab);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sportsTab:
                        break;
                    case R.id.mybookingsTab:
                        Intent a = new Intent(RecyclerViewSports.this, MyBookings.class);
                        startActivity(a);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.profileTab:
                        Intent b = new Intent(RecyclerViewSports.this, Profile.class);
                        startActivity(b);
                        overridePendingTransition(0, 0);
                        finish();
                        break;

                }
                return false;
            }
        });

        setSport_images();
    }

    private void setSport_images() {

        sport_images.add(R.drawable.swimming);
        sport_names.add("Swimming");

        sport_images.add(R.drawable.tabbletennis);
        sport_names.add("Table Tennis");

        sport_images.add(R.drawable.badminton);
        sport_names.add("Badminton");

        sport_images.add(R.drawable.tennis);
        sport_names.add("Tennis");

        sport_images.add(R.drawable.squash);
        sport_names.add("Squash");

        sport_images.add(R.drawable.football);
        sport_names.add("FootBall");

        sport_images.add(R.drawable.basketball);
        sport_names.add("Basketball");

//        sport_images.add(R.drawable.pool);
//        sport_names.add("Pool");
//
//        sport_images.add(R.drawable.snooker);
//        sport_names.add("Snooker");
//
//        sport_images.add(R.drawable.cricket);
//        sport_names.add("Cricket");
//
//        sport_images.add(R.drawable.football);
//        sport_names.add("FootBall");


        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView r_view = findViewById(R.id.recycler_view);
        RecyclerViewAdaptor adaptor = new RecyclerViewAdaptor(RecyclerViewSports.this, sport_names, sport_images, this);
        if (r_view == null)
            Log.d("tag", "OPS");
        r_view.setAdapter(adaptor);
        r_view.setLayoutManager(new LinearLayoutManager(RecyclerViewSports.this));
    }

    @Override
    public void onListClick(int pos) {

        Intent intent;
        switch (pos) {
            case 0:
                checkPayment();
                break;
            case 1:
            case 2:
            case 3:
            case 4:
                intent = new Intent(RecyclerViewSports.this, SportsSD.class);
                intent.putExtra("Type", pos);
                startActivity(intent);
                break;
            case 5:
            case 6:
                Toast.makeText(RecyclerViewSports.this, "Coming Soon!", Toast.LENGTH_SHORT).show();
                break;
        }


    }

    private void checkPayment() {

        //IMPLEMENT THIS USING GAURD APP
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();

        final String col = "Guard";
        final String doc = "guard";

        final ProgressDialog dialog = ProgressDialog.show(RecyclerViewSports.this, "",
                "Checking. Please wait...", true);


//        Log.d("zzz","HEREE");
//        db.collection(col).document(doc).set(new GuardSwimmingData(new ArrayList<String>()));
//        Toast.makeText(RecyclerViewSports.this, ":(", Toast.LENGTH_SHORT).show();
//        dialog.cancel();

        final boolean[] hasPaid = {false};
        db.collection(col)
                .document(doc)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        GuardSwimmingData gsd = documentSnapshot.toObject(GuardSwimmingData.class);
                        //Log.d("zzz", " " + paid.toString());
                        ArrayList<String> paid = gsd.getPaid();

                        if (paid.contains(email)) {
                            dialog.cancel();
                            hasPaid[0] = true;
                            Intent intent = new Intent(RecyclerViewSports.this, Swimming.class);
                            startActivity(intent);

                        } else {
                            dialog.cancel();
                            hasPaid[0] = false;
                            new AlertDialog.Builder(RecyclerViewSports.this)
                                    .setTitle("Error")
                                    .setMessage("You have not paid for swimming!")
                                    .show();
                            return;
                        }
                    }
                });

    }

}