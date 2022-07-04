package com.iiitd.bookmysport;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iiitd.bookmysport.recyclerview.RvAdapter_guard;

import java.util.ArrayList;

public class GuardActivity extends AppCompatActivity {

    private RvAdapter_guard adapter;
    private ArrayList<GuardItemInfo> guardItemInfos;
    private RecyclerView guardrv;
    private FirebaseFirestore db;
    private Button refresh;
    ProgressDialog dialog;

    public static final int REFRESHDELAY = 1000 * 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard);
        guardItemInfos = new ArrayList<>();
        guardrv = (RecyclerView) findViewById(R.id.rv_guard);
        refresh = findViewById(R.id.guard_refreshbtn);
        db = FirebaseFirestore.getInstance();


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDB();
            }
        });

        Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                getDB();
                Log.d("Handlers", "Called on main thread");
            }
        };

        handler.postDelayed(runnableCode, REFRESHDELAY);

        getDB();

        //live();

        final String col1 = "TableTennis";
        final String col2 = "Badminton";
        final String col3 = "Tennis";
        final String col4 = "Squash";

        /*db.collection(col1)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            DynamicData dynamicData = documentSnapshot.toObject(DynamicData.class);
                            for (int i = 0; i < dynamicData.getData().size(); i++) {
                                if (guardItemInfos.contains(dynamicData.getGaurdItemInfo(i, col1)))
                                    return;
                                else
                                    guardItemInfos.add(dynamicData.getGaurdItemInfo(i, col1));
                            }
                        }
                    }
                });*/

    }

    private void getDB() {
        dialog = ProgressDialog.show(GuardActivity.this, "",
                "Loading. Please wait...", true);

        guardItemInfos = new ArrayList<>();

        getTableTennisDB("TableTennis");

    }


    public void getTableTennisDB(final String col) {

        db.collection(col)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                DynamicData dynamicData = documentSnapshot.toObject(DynamicData.class);
                                for (int i = 0; i < dynamicData.getData().size(); i++) {
                                    GuardItemInfo g = dynamicData.getGaurdItemInfo(i, col);
                                    if(g.hasConfirmed.equals("0"))
                                        guardItemInfos.add(g);
                                }

                            }

                            getBadmintonDB("Badminton");

                        }
                    }
                });


    }

    public void getBadmintonDB(final String col) {

        db.collection(col)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                DynamicData dynamicData = documentSnapshot.toObject(DynamicData.class);
                                for (int i = 0; i < dynamicData.getData().size(); i++) {
                                    guardItemInfos.add(dynamicData.getGaurdItemInfo(i, col));
                                }

                            }

                            getTennisDB("Tennis");

                        }
                    }
                });


    }

    public void getTennisDB(final String col) {

        db.collection(col)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                DynamicData dynamicData = documentSnapshot.toObject(DynamicData.class);
                                for (int i = 0; i < dynamicData.getData().size(); i++) {
                                    guardItemInfos.add(dynamicData.getGaurdItemInfo(i, col));
                                }

                            }

                            getSquashDB("Squash");

                        }
                    }
                });


    }

    public void getSquashDB(final String col) {

        db.collection(col)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                DynamicData dynamicData = documentSnapshot.toObject(DynamicData.class);
                                for (int i = 0; i < dynamicData.getData().size(); i++) {
                                    guardItemInfos.add(dynamicData.getGaurdItemInfo(i, col));
                                }

                            }

                            guardrv.setLayoutManager(new LinearLayoutManager(GuardActivity.this));
                            adapter = new RvAdapter_guard(GuardActivity.this, guardItemInfos);
                            guardrv.setAdapter(adapter);

                            dialog.cancel();


                        }
                    }
                });


    }


    public void live() {
        final String col1 = "TableTennis";
        final String col2 = "Badminton";
        final String col3 = "Tennis";
        final String col4 = "Squash";

        final boolean[] isEvent = {false};

        db.collection(col1)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
//                        getDB();
                        isEvent[0] = true;
                        db.collection(col2)
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                                        isEvent[0] = true;
                                        db.collection(col3)
                                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                                                        isEvent[0] = true;
                                                        db.collection(col4)
                                                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                                                                        isEvent[0] = true;
                                                                    }
                                                                });
                                                    }
                                                });
                                    }
                                });

                        if (isEvent[0])
                            getDB();
                    }


                });
    }

}
