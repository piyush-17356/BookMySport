package com.iiitd.bookmysport.recyclerview;


import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.iiitd.bookmysport.R;
import com.iiitd.bookmysport.Time_Data;
import com.iiitd.bookmysport.User;
import com.iiitd.bookmysport.bookings.Document;
import com.iiitd.bookmysport.other.Functions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FragRecyclerViewAdaptor extends RecyclerView.Adapter<FragRecyclerViewAdaptor.ViewHolder> {

    private Context context;
    private Time_Data time_data;
    private FirebaseFirestore db;
    private static String TAG = "tag";
    private String email;
    private int type;
    private String day;
    private int max;

    public FragRecyclerViewAdaptor(Context context, Time_Data time_data, int type) {
        this.context = context;
        this.time_data = time_data;
        this.type = type;
        this.max = Functions.getSportsMaxAvailability(time_data.getSportsID());
        this.db = FirebaseFirestore.getInstance();
        this.email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        if (this.type == 1)
            this.day = "Today";
        else
            this.day = "Tom";

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.timeslots, parent, false);
        return new ViewHolder(view);
    }

    public void update(Time_Data time_data1) {
        time_data = time_data1;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        String s = " Available = ";
        int ava1 = Functions.getSportsMaxAvailability(time_data.getSportsID()) -
                time_data.getTimeSlots().get(position *3).getOccupied();
        int ava2 = Functions.getSportsMaxAvailability(time_data.getSportsID()) -
                time_data.getTimeSlots().get(position *3 + 1).getOccupied();
        int ava3 = Functions.getSportsMaxAvailability(time_data.getSportsID()) -
                time_data.getTimeSlots().get(position *3 + 2).getOccupied();


        String one = time_data.getTimeSlots().get(position * 3).getTime_slot_name()
                + s + ava1;
        String two = time_data.getTimeSlots().get(position * 3 + 1).getTime_slot_name()
                + s + ava2;
        String three = time_data.getTimeSlots().get(position * 3 + 2).getTime_slot_name()
                + s + ava3;

        holder.slot1.setText(one);
        holder.slot2.setText(two);
        holder.slot3.setText(three);

        int curHour = Functions.getCurrentTimeIndex();

        if (time_data.getTimeSlots().get(position * 3).getOccupied() == time_data.getMax()
            || (time_data.getTimeSlots().get(position * 3).getTime_slot_id() <= curHour && this.type == 1) ){
            //holder.slot1.setBackgroundColor(Color.RED);
            //holder.slot1.setTextColor(Color.WHITE);
            holder.slot1.setEnabled(false);
        } else {
            //holder.slot1.setBackgroundColor(Color.GREEN);
            //holder.slot1.setTextColor(Color.BLACK);
            holder.slot1.setEnabled(true);
        }

        if (time_data.getTimeSlots().get(position * 3 + 1).getOccupied() == time_data.getMax()
                || (time_data.getTimeSlots().get(position * 3 + 1).getTime_slot_id() <= curHour && this.type == 1)){
            //holder.slot2.setBackgroundColor(Color.RED);
            //holder.slot2.setTextColor(Color.WHITE);
            holder.slot2.setEnabled(false);
        } else {
            //holder.slot2.setBackgroundColor(Color.GREEN);
            //holder.slot2.setTextColor(Color.BLACK);
            holder.slot2.setEnabled(true);
        }

        if (time_data.getTimeSlots().get(position * 3 + 2).getOccupied() == time_data.getMax()
                || (time_data.getTimeSlots().get(position * 3 + 2).getTime_slot_id() <= curHour && this.type == 1)) {
            //holder.slot3.setBackgroundColor(Color.RED);
            //holder.slot3.setTextColor(Color.WHITE);
            holder.slot3.setEnabled(false);
        } else {
            //holder.slot3.setBackgroundColor(Color.GREEN);
            //holder.slot3.setTextColor(Color.BLACK);
            holder.slot3.setEnabled(true);
        }

        holder.slot1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder altdial= new AlertDialog.Builder(context);
                altdial.setMessage("Are you sure you want to book this slot?").setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                updateRollno();
                                click(holder,position,0);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertDialog=altdial.create();
                alertDialog.setTitle("Booking Confirmation");
                alertDialog.show();
//                click(holder, position, 0);
            }
        });

        holder.slot2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder altdial= new AlertDialog.Builder(context);
                altdial.setMessage("Are you sure you want to book this slot?").setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                updateRollno();
                                click(holder,position,1);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertDialog=altdial.create();
                alertDialog.setTitle("Booking Confirmation");
                alertDialog.show();
//                click(holder, position, 1);
            }
        });

        holder.slot3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder altdial= new AlertDialog.Builder(context);
                altdial.setMessage("Are you sure you want to book this slot?").setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                updateRollno();
                                click(holder,position,2);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertDialog=altdial.create();
                alertDialog.setTitle("Booking Confirmation");
                alertDialog.show();
//                click(holder, position, 2);
            }
        });

    }


    private void click(ViewHolder holder, int position, int index) {

        String col = time_data.getSportsName();
        String doc = Functions.getSportsDocName(time_data.getSportsID());
        String col2 = day;
        String doc2 = "" + (position * 3 + index + 1);
        String Date1 = "";
        Calendar calendar = Calendar.getInstance();
        if (type == 2)
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date1 = dateFormat.format(calendar.getTime());
        final String bookingInfo = time_data.getSportsName()
                + "_"
                + Date1
                + "_"
                + time_data.getTimeSlots().get(position * 3 + index).getTime_slot_name();

        final DocumentReference sfDocRef = db
                .collection(col)
                .document(doc)
                .collection(col2)
                .document(doc2);

        final DocumentReference usersDoc = db
                .collection("Users")
                .document(email);

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);
                DocumentSnapshot snapshot2 = transaction.get(usersDoc);

                Document doc = snapshot.toObject(Document.class);
                User user = snapshot2.toObject(User.class);

                if (doc.getRollnos().indexOf(email) == -1) {
                    int occ = doc.getOccupied() + 1;
                    ArrayList<String> rollnos = doc.getRollnos();
                    rollnos.add(email);
                    ArrayList<String> bookings = user.getBookings();
                    bookings.add(bookingInfo);

                    if (occ > max)
                        return null;

                    transaction.update(sfDocRef, "occupied", occ);
                    transaction.update(sfDocRef, "rollnos", rollnos);
                    transaction.update(usersDoc, "bookings", bookings);
                }
                else{
                    Toast.makeText(context,"You have already booked a slot",Toast.LENGTH_SHORT).show();
                }

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Transaction success!");
                Toast.makeText(context, "Slot Booked Successfully", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Transaction failure." + e);
                        Toast.makeText(context, "Slot Booking Failed", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public int getItemCount() {
        return time_data.getTimeSlots().size() / 3;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private Button slot1;
        private Button slot2;
        private Button slot3;

        ViewHolder(View view) {
            super(view);

            //SLOT ORDER 2, 3, 1, DUE TO ORDER IN "timeslots.xml"
            slot1 = view.findViewById(R.id.slot2);
            slot2 = view.findViewById(R.id.slot3);
            slot3 = view.findViewById(R.id.slot1);

        }
    }
}
