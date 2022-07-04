package com.iiitd.bookmysport;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.base.CharMatcher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;

public class SportsSDBookSlotDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private boolean avail1;
    private boolean avail2;
    private TextView title;
    private EditText r1;
    private EditText r2;
    private EditText r3;
    private EditText r4;
    private Switch aSwitch;
    private Button cancel;
    private Button slot1;
    private Button slot2;
    private String start;
    private String end;
    private int startTime;
    private int endTime;
    private String sports;


    public SportsSDBookSlotDialog(@NonNull Context context, boolean avail1, boolean avail2,
                                  String start, String end, int startTime, int endTime, String sports) {
        super(context);
        this.context = context;
        this.avail1 = avail1;
        this.avail2 = avail2;
        this.start = start;
        this.end = end;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sports = sports;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogbook);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        String ss = email.split("@")[0];
        String theDigits = CharMatcher.inRange('0', '9').retainFrom(ss);
        String rollno = "-";
        if (theDigits != null && !theDigits.isEmpty()) {
            rollno = "20" + theDigits;
        }

        title = findViewById(R.id.t_book);
        aSwitch = findViewById(R.id.switch1);
        r1 = findViewById(R.id.r1);
        r2 = findViewById(R.id.r2);
        r3 = findViewById(R.id.r3);
        r4 = findViewById(R.id.r4);
        cancel = findViewById(R.id.c_book);
        slot1 = findViewById(R.id.s1_book);
        slot2 = findViewById(R.id.s2_book);


        r1.setEnabled(false);
        r1.setText(rollno);


        init();
        setSwitchListener();

        cancel.setOnClickListener(this);
        slot1.setOnClickListener(this);
        slot2.setOnClickListener(this);


    }

    private void init() {
        if (!avail1)
            slot1.setEnabled(false);
        if (!avail2)
            slot2.setEnabled(false);

        if (aSwitch.isChecked()) {
            if (!r3.isEnabled())
                r3.setEnabled(true);
            if (!r4.isEnabled())
                r4.setEnabled(true);
            r3.setHint(R.string.enter_3st_rollno);
            r4.setHint(R.string.enter_4st_rollno);
        } else {
            r3.setText("");
            r4.setText("");
            r3.setEnabled(false);
            r4.setEnabled(false);
            r3.setHint("Only for Doubles");
            r4.setHint("Only for Doubles");
        }
    }

    private void setSwitchListener() {
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //Toast.makeText(context, "CHECKED ON", Toast.LENGTH_SHORT).show();
                    //4 Players

                    if (!r3.isEnabled())
                        r3.setEnabled(true);
                    if (!r4.isEnabled())
                        r4.setEnabled(true);
                    r3.setHint(R.string.enter_3st_rollno);
                    r4.setHint(R.string.enter_4st_rollno);
                    aSwitch.setText("Doubles");
                } else {
                    //Toast.makeText(context, "CHECKED OFF", Toast.LENGTH_SHORT).show();
                    r3.setText("");
                    r4.setText("");
                    r3.setEnabled(false);
                    r4.setEnabled(false);
                    r3.setHint("Only for Doubles");
                    r4.setHint("Only for Doubles");
                    r3.setError(null);
                    r4.setError(null);

                    aSwitch.setText("Singles");
                }
            }
        });

    }


    @Override
    public void onClick(View view) {
        if (view == cancel) {
            this.cancel();
        } else if (view == slot1) {
            book(view);
        } else if (view == slot2) {
            book(view);
        }
    }

    private void book(View view) {

        String rollno1 = r1.getText().toString();
        String rollno2 = r2.getText().toString();
        String rollno3 = r3.getText().toString();
        String rollno4 = r4.getText().toString();

        boolean error = false;
        if (TextUtils.isEmpty(rollno1)) {
            r1.setError("Missing");
            error = true;
        }
        if (TextUtils.isEmpty(rollno2)) {
            r2.setError("Missing");
            error = true;
        }
        if (aSwitch.isChecked()) {
            if (TextUtils.isEmpty(rollno3)) {
                r3.setError("Missing");
                error = true;
            }
            if (TextUtils.isEmpty(rollno4)) {
                r4.setError("Missing");
                error = true;
            }
        }

        if (error)
            return;

        String slot = "";
        String doc = "";
        int type = 1;
        if (view == slot1) {
            slot = "slot1";
            doc = "table1";
            type = 1;
        } else if (view == slot2) {
            slot = "slot2";
            doc = "table2";
            type = 2;
        }

        ArrayList<String> rollnos = new ArrayList<>();

        //SINGLES
        if (!aSwitch.isChecked()) {
            Log.d("tag", slot + " Booking for Singles-> "
                    + rollno1 + ", " + rollno2 + " from " + start + " to " + end);
            rollnos.add(rollno1);
            rollnos.add(rollno2);
        }//DOUBLE
        else {
            Log.d("tag", slot + " Booking for Doubles-> "
                    + rollno1 + ", " + rollno2 + ", " + rollno3 + ", " + rollno4 + " from " + start + " to " + end);

            rollnos.add(rollno1);
            rollnos.add(rollno2);
            rollnos.add(rollno3);
            rollnos.add(rollno4);
        }


        writeInFirebase(type, sports, doc, rollnos, aSwitch.isChecked());

    }

    private void writeInFirebase(final int type, final String col, final String doc, final ArrayList<String> rollnos, boolean checked) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();

        String bookedType = "1";

        if (checked)
            bookedType = "2";


        final ProgressDialog dialog = ProgressDialog.show(this.context, "",
                "Booking Slot. Please wait...", true);


        //DELTE BELOW LINE AFTER COMPLETEING
//        email = "porvil17304@iiitd.ac.in";

        final DocumentReference sportsRef = db
                .collection(col)
                .document(doc);

        final DocumentReference usersRef = db
                .collection("Users")
                .document(email);

        //FORMAT
        // 02:45PM_03:12PM_1_ROLLNO,ROLLNO ...
        final String finalEmail = email;
        final String finalBookedType = bookedType;
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sportsRef);
                DocumentSnapshot snapshot2 = transaction.get(usersRef);

                DynamicData dynamicData = snapshot.toObject(DynamicData.class);
                User user = snapshot2.toObject(User.class);

                if (dynamicData.getData().indexOf(finalEmail) == -1) {
                    //DO DB OPERATIONS
                    StringBuilder s = new StringBuilder();
                    s.append(start + "AM_" + end + "PM_");
                    s.append(type + "_");
                    s.append(finalBookedType + "_");
                    for (String ss : rollnos) {
                        s.append(ss + "@iiitd.ac.in");
                        s.append(",");
                    }
                    // 0 = not yet confirmed the slot to guard
                    s.append("_0");

                    dynamicData.getData().add(s.toString());

                    String us = start + "_" + end + "_" + col + "_" + type + "_" + finalBookedType;
                    user.getBookings().add(us);

                    //UPDATE NOW
                    transaction.update(sportsRef, "data", dynamicData.getData());
                    transaction.update(usersRef, "bookings", user.getBookings());

                } else {
                    Toast.makeText(context, "You have already booked a slot", Toast.LENGTH_SHORT).show();
                }

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("tag", "Transaction success!");
                Toast.makeText(context, "Slot Booked Successfully", Toast.LENGTH_SHORT).show();

                SportsSDBookSlotDialog.this.cancel();
                dialog.cancel();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("tag", "Transaction failure." + e);
                        Toast.makeText(context, "Slot Booking Failed", Toast.LENGTH_SHORT).show();
                        SportsSDBookSlotDialog.this.cancel();
                        dialog.cancel();
                    }
                });

    }
}

