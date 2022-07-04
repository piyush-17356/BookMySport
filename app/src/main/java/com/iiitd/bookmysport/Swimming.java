package com.iiitd.bookmysport;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Swimming extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private ArrayList<SwimmingData> swimmingData;
    private SwimmingRecyclerView swimmingRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swimming);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.swimmingRV);
        swimmingData = new ArrayList<>();

        //DO NOT UNCOMMENT BELOW FUNCTION
        //initFirebase();


        getDB();


    }

    private void getDB() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();

        final String col = "Swimming";

        final ProgressDialog dialog = ProgressDialog.show(Swimming.this, "",
                "Loading. Please wait...", true);

        db.collection(col)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            swimmingData = new ArrayList<>();

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                SwimmingData sd = documentSnapshot.toObject(SwimmingData.class);
                                Log.d("tag", " " + sd.toString());

                                swimmingData.add(sd);
                            }


                            swimmingRecyclerView = new SwimmingRecyclerView(Swimming.this, swimmingData, email);

                            if (recyclerView == null)
                                Log.d("tag", "OPS");
                            recyclerView.setAdapter(swimmingRecyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(Swimming.this));
                            //Toast.makeText(Swimming.this, "DB collected", Toast.LENGTH_SHORT).show();


                            dialog.cancel();
                            live(col);
                        } else {
                            //Toast.makeText(Swimming.this, "DB couldnt be loaded, IMPLEMENT GO BACK", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }


    private void live(String col) {
        db.collection(col)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {

                        swimmingData = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : value) {
                            SwimmingData sd = documentSnapshot.toObject(SwimmingData.class);
                            Log.d("tag", " " + sd.toString());

                            swimmingData.add(sd);
                        }

                        swimmingRecyclerView.update(swimmingData);

                    }
                });
    }


    //DO NOT CALL THIS METHOD
    private void initFirebase() {

        String col = "Swimming";
        String doc = "slot";

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        ArrayList<SwimmingData> swimmingData = new ArrayList<>();
        swimmingData.add(new SwimmingData("05:30AM - 06:15AM", 1, 0, 30, new ArrayList<String>(), "05:30_06:15"));
        swimmingData.add(new SwimmingData("06:30AM - 07:15AM", 2, 0, 30, new ArrayList<String>(), "06:30_07:15"));
        swimmingData.add(new SwimmingData("07:30AM - 08:15AM", 3, 0, 30, new ArrayList<String>(), "07:30_08:15"));
        swimmingData.add(new SwimmingData("08:15AM - 09:00AM", 4, 0, 30, new ArrayList<String>(), "08:15_09:00"));
        swimmingData.add(new SwimmingData("05:30PM - 06:15PM", 5, 0, 30, new ArrayList<String>(), "17:30_18:15"));
        swimmingData.add(new SwimmingData("06:30PM - 07:15PM", 6, 0, 30, new ArrayList<String>(), "18:30_19:15"));
        swimmingData.add(new SwimmingData("07:30PM - 08:15PM", 7, 0, 30, new ArrayList<String>(), "19:30_20:15"));


        for (int i = 1; i <= 7; i++) {
            db.collection(col)
                    .document(doc + i)
                    .set(swimmingData.get(i - 1));
        }
    }
}
