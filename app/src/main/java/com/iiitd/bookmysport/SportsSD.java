package com.iiitd.bookmysport;

import android.app.ProgressDialog;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iiitd.bookmysport.bookings.Document;
import com.iiitd.bookmysport.other.Functions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SportsSD extends AppCompatActivity {

    int curHR = 0;
    int curMIN = 0;

    private TextView textView;
    private TextView timeView;
    private EditText startET;
    private EditText endET;
    private Button bookBTN;
    private Button backBTN;
    private RecyclerView r_view;
    private FirebaseFirestore db;
    private SportsSDData sportsSDData;
    TimeSlotsRecyclerView timeSlotsRecyclerView;
    private int sportsType;
    private String sportsName;
    private String start;
    private String end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportssd);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            sportsType = bundle.getInt("Type");
        } else {
            sportsType = -1;
//            finish();
        }

        sportsName = Functions.getSportsNameDynamic(sportsType);
        Log.d("tag", "SPORTS NAME = " + sportsName);

        timeView = findViewById(R.id.timeView);
        startET = findViewById(R.id.startTime);
        endET = findViewById(R.id.endTime);
        bookBTN = findViewById(R.id.book);
        backBTN = findViewById(R.id.bookBack);
        r_view = findViewById(R.id.recycler_view_test);

        textView = findViewById(R.id.game_nameTV);
        textView.setText(sportsName);
//        textView.setText("TABLE TENNIS");


        db = FirebaseFirestore.getInstance();
        final ArrayList<Document> list = new ArrayList<>();

        getDB();

    }

    private void live(String col) {
        db.collection(col)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {

                        ArrayList<String> slot1 = new ArrayList<>();
                        ArrayList<String> slot2 = new ArrayList<>();
                        int i = 1;
                        for (QueryDocumentSnapshot doc : value) {
                            DynamicData dynamicData = doc.toObject(DynamicData.class);
                            Log.d("tag", " " + dynamicData.getData());
                            if (i == 1)
                                slot1 = dynamicData.getData();
                            else
                                slot2 = dynamicData.getData();
                            i++;
                        }

                        sportsSDData = new SportsSDData(slot1, slot2);
                        timeSlotsRecyclerView.update(sportsSDData);
                    }
                });
    }


    private void getDB() {
        //CHANGE IT
        final String col = sportsName;
//        final String col = "TableTennis";
        final ProgressDialog dialog = ProgressDialog.show(SportsSD.this, "",
                "Loading. Please wait...", true);

        db.collection(col)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<String> slot1 = new ArrayList<>();
                            ArrayList<String> slot2 = new ArrayList<>();
                            int i = 1;
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                DynamicData dynamicData = documentSnapshot.toObject(DynamicData.class);
                                Log.d("tag", " " + dynamicData.getData());
                                if (i == 1)
                                    slot1 = dynamicData.getData();
                                else
                                    slot2 = dynamicData.getData();
                                i++;
                            }
                            sportsSDData = new SportsSDData(slot1, slot2);

                            timeSlotsRecyclerView = new TimeSlotsRecyclerView(SportsSD.this, sportsSDData);

                            if (r_view == null)
                                Log.d("tag", "OPS");
                            r_view.setAdapter(timeSlotsRecyclerView);
                            r_view.setLayoutManager(new LinearLayoutManager(SportsSD.this));


                            //Toast.makeText(SportsSD.this, "DB collected", Toast.LENGTH_SHORT).show();


                            init();
                            dialog.cancel();
                            live(col);
                        } else {
                            //Toast.makeText(SportsSD.this, "DB couldnt be loaded, IMPLEMENT GO BACK", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                            finish();
                        }
                    }
                });
    }


    private void getStartTime() {
        Date currentTime = Calendar.getInstance().getTime();
        curHR = currentTime.getHours();
        curMIN = currentTime.getMinutes();

        Log.d("tag", curHR + "--" + curMIN);
    }

    private void getEndTime() {
    }

    private void init() {

        getStartTime();
        startET.setText(String.format("%02d:%02d", curHR, curMIN));
        start = String.format("%02d:%02d", curHR, curMIN);

        startET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);


                TimePickerDialog timePickerDialog = new TimePickerDialog(SportsSD.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        startET.setText(String.format("%02d:%02d", hourOfDay, minutes));
                        start = String.format("%02d:%02d", hourOfDay, minutes);
                        updateTime();
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


                TimePickerDialog timePickerDialog = new TimePickerDialog(SportsSD.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        endET.setText(String.format("%02d:%02d", hourOfDay, minutes));
                        end = String.format("%02d:%02d", hourOfDay, minutes);
                        updateTime();
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

            }
        });


        bookBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (startET.getText().toString().isEmpty() || endET.getText().toString().isEmpty()) {
                    Toast.makeText(SportsSD.this, "Choose Start and End Time", Toast.LENGTH_SHORT).show();
                    return;
                }
                String start = startET.getText().toString();
                String end = endET.getText().toString();

                book(start, end);

            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void updateTime() {
        String start1 = start;
        String end1 = end;

        if (start == null || end == null)
            return;

        if (!start1.isEmpty() && !end1.isEmpty()) {
            int startHR = Integer.parseInt(start1.substring(0, 2).trim());
            int startMIN = Integer.parseInt(start1.substring(3).trim());

            int endHR = Integer.parseInt(end1.substring(0, 2).trim());
            int endMIN = Integer.parseInt(end1.substring(3).trim());

            int startTime = startHR * 60 + startMIN;
            int endTime = endHR * 60 + endMIN;

            int totalTime = endTime - startTime;

            String s = "0 minutes";
            if (totalTime < 0)
                s = "End Time should be more than Start Time.";
            else if (totalTime >= 0 && totalTime < 15)
                s = "Slot should be atleast 15 minutes long.";
            else if (totalTime > 60)
                s = "Slot cant be longer than 60 minutes.";
            else
                s = totalTime + " minutes";

            timeView.setText(s);
        } else {
            timeView.setText("0 minutes");
        }

    }

    private void book(String start, String end) {
        String slot = "Slot Booked from " + start + "  to " + end;

        int startHR = Integer.parseInt(start.substring(0, 2).trim());
        int startMIN = Integer.parseInt(start.substring(3).trim());

        int endHR = Integer.parseInt(end.substring(0, 2).trim());
        int endMIN = Integer.parseInt(end.substring(3).trim());

        int startTime = startHR * 60 + startMIN;
        int endTime = endHR * 60 + endMIN;

        Log.d("tag", "STARTTIME = " + startTime + "--" + startHR + "-" + startMIN);
        Log.d("tag", "ENDTIME = " + endTime + "--" + endHR + "-" + endMIN);

        if (startTime < endTime) {

            int diff = endTime - startTime;
            if (diff >= 15 && diff <= 60) {

                boolean avail1 = true;
                boolean avail2 = true;

                int[] slot1 = sportsSDData.getTable1Time();
                int[] slot2 = sportsSDData.getTable2Time();

                for (int i = startTime; i <= endTime; i++) {
                    if (slot1[i] != 0) {
                        avail1 = false;
                        break;
                    }
                }
                for (int i = startTime; i <= endTime; i++) {
                    if (slot2[i] != 0) {
                        avail2 = false;
                        break;
                    }
                }

                if (avail1 || avail2) {
                    //DO DB OPERATIONS

                    String sports = sportsName;
                    //Toast.makeText(SportsSD.this, slot, Toast.LENGTH_SHORT).show();
                    SportsSDBookSlotDialog sportsSDBookSlotDialog =
                            new SportsSDBookSlotDialog(
                                    SportsSD.this,
                                    avail1,
                                    avail2,
                                    start,
                                    end,
                                    startTime,
                                    endTime,
                                    sports);

                    sportsSDBookSlotDialog.show();

                } else {
                    Toast.makeText(SportsSD.this, "SLOT NOT AVAILABLE", Toast.LENGTH_SHORT).show();

                }

            } else {
                String error = "";
                if (diff < 15)
                    error = "Minimum slot book time = 15minutes.";
                else
                    error = "Maximim slot book time = 60minutes.";
                Toast.makeText(SportsSD.this,
                        error, Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(SportsSD.this,
                    "End Time Should be more than Start Time", Toast.LENGTH_SHORT).show();
        }

    }
}
