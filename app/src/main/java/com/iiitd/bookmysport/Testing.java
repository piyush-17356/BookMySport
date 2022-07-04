package com.iiitd.bookmysport;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.iiitd.bookmysport.other.Functions;

import java.util.ArrayList;
import java.util.Calendar;

public class Testing extends AppCompatActivity {

    private EditText startET;
    private EditText endET;
    private Button bookBTN;
    private TextView bookedTV;
    private TextView testTV;
    private FirebaseFirestore db;
    private static String TAG = "tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        startET = findViewById(R.id.startTime);
        endET = findViewById(R.id.endTime);
        bookedTV = findViewById(R.id.booked);
        bookBTN = findViewById(R.id.book);
        testTV = findViewById(R.id.testTV);

        db = FirebaseFirestore.getInstance();

//        db.collection("test").document("doc").set(new DynamicData(new ArrayList<String>()));
        startET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);


                TimePickerDialog timePickerDialog = new TimePickerDialog(Testing.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        String amPM = "";
                        if (hourOfDay >= 12) {
                            amPM = "PM";
                            //hourOfDay = hourOfDay-12;
                        } else {
                            amPM = "AM";
                        }
                        startET.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPM);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

            }
        });


        endET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);


                TimePickerDialog timePickerDialog = new TimePickerDialog(Testing.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        String amPM = "";
                        if (hourOfDay >= 12) {
                            amPM = "PM";
                            //hourOfDay = hourOfDay-12;
                        } else {
                            amPM = "AM";
                        }
                        endET.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPM);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

            }
        });


        bookBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String slot = "Slot Booked from " + startET.getText() + "  to " + endET.getText();
                Toast.makeText(Testing.this, slot, Toast.LENGTH_SHORT).show();


                String col = "test";
                String doc = "doc";


                final String startTime = "" + startET.getText();
                String amPMStart = startTime.substring(5);
                final String endTime = "" + endET.getText();
                String amPMEnd = endTime.substring(5);

                db.collection(col)
                        .document(doc)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                DynamicData dynamicData = documentSnapshot.toObject(DynamicData.class);
                                Log.d(TAG, "data = " + dynamicData.toString());
                                //Functions.getBookedSlots(dynamicData.getData());

                                check(startTime, endTime, dynamicData);

                            }
                        });
            }
        });

        String col = "test";
        String doc = "doc";

        db.collection(col)
                .document(doc)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        DynamicData dynamicData = documentSnapshot.toObject(DynamicData.class);
                        Log.d(TAG, "data = " + dynamicData.toString());
                        //Functions.getBookedSlots(dynamicData.getData());
                    }
                });


        final DocumentReference docRef = db
                .collection(col)
                .document(doc);

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    DynamicData data = snapshot.toObject(DynamicData.class);
                    Log.d(TAG, "Current data: " + data.toString());
                    bookedTV.setText(data.toString());


                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });


        //TEST FROM HERE

        ArrayList<String> slot1 = new ArrayList<>();
        ArrayList<String> slot2 = new ArrayList<>();

        slot1.add("03:00AM_03:15AM");
        slot1.add("03:20AM_03:30AM");
        slot1.add("04:00AM_04:30AM");
        slot1.add("08:00AM_08:20AM");
        slot1.add("08:40AM_09:10AM");


        slot2.add("03:00AM_03:28AM");
//        slot2.add("03:30AM_03:35AM");
        slot2.add("04:15AM_05:00AM");
        slot2.add("06:00AM_07:00AM");
        slot2.add("09:00AM_09:30AM");


        SportsSDData sportsSDData = new SportsSDData(slot1, slot2);


//        testTV.setText("Count = " + sportsSDData.getCount());

//        ArrayList<String> times = new ArrayList<>();
//
//        for(String s : sportsSDData.getMergedSlots()){
//            String[] split = s.split("_");
//            int hr = Integer.parseInt(split[0]);
//            int min = Integer.parseInt(split[1]);
//            String ans = Functions.getTimeFromInt(hr,min);
//        }


        StringBuilder stringBuilder = new StringBuilder();
        for(String s : sportsSDData.getMergedSlots())
            stringBuilder.append(s + "____");

        stringBuilder.append(sportsSDData.getCount());

        testTV.setText(stringBuilder);






        RecyclerView r_view = findViewById(R.id.recycler_view_test);
        TimeSlotsRecyclerView timeSlotsRecyclerView = new TimeSlotsRecyclerView(this, sportsSDData);

        if (r_view == null)
            Log.d("tag", "OPS");
        r_view.setAdapter(timeSlotsRecyclerView);
        r_view.setLayoutManager(new LinearLayoutManager(Testing.this));






    }

    public void check(final String start, final String end, DynamicData data) {



        int[] time = Functions.getBookedSlots(data.getData());

        for(int i =0;i<1440;i++){
            if(time[i] == 1){
                Log.d(TAG,"TIME -- " + i);
            }
        }


        int startHR = Integer.parseInt(start.substring(0, 2).trim());
        int startMIN = Integer.parseInt(start.substring(3, 5).trim());

        int endHR = Integer.parseInt(end.substring(0, 2).trim());
        int endMIN = Integer.parseInt(end.substring(3, 5).trim());

        final int startTime = startHR * 60 + startMIN;
        final int endTime = endHR * 60 + endMIN;

        Log.d(TAG,"start == " + startTime + "   end == " + endTime);

        if (time[startTime] == 1 && time[endTime] == 1) {
            //ALREADY SOMETHING BOOKED
            Toast.makeText(Testing.this, "Booked outside", Toast.LENGTH_SHORT).show();
        } else {
            boolean isAvailable = true;
            for (int i = startTime; i <= endTime; i++) {
                if (time[i] == 1) {
                    isAvailable = false;
                    break;
                }
            }

            if (isAvailable) {
                //AVAILABLE SLOT
                Toast.makeText(Testing.this, "aVAILABLE", Toast.LENGTH_SHORT).show();


                String col = "test";
                String doc = "doc";

                final DocumentReference usersDoc = db
                        .collection(col)
                        .document(doc);

                db.runTransaction(new Transaction.Function<Void>() {
                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                        DocumentSnapshot snapshot = transaction.get(usersDoc);

                        DynamicData doc = snapshot.toObject(DynamicData.class);

                        ArrayList<String> data = doc.getData();
                        data.add(start + "_" + end);

                        transaction.update(usersDoc,"data",data);


                        return null;
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Transaction success!");
                        Toast.makeText(Testing.this, "Slot Booked Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Transaction failure." + e);
                                Toast.makeText(Testing.this, "Slot Booking Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                //BOOKED
                Toast.makeText(Testing.this, "Booked inside", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
