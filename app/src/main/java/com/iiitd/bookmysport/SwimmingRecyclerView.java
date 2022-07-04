package com.iiitd.bookmysport;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;

public class SwimmingRecyclerView extends RecyclerView.Adapter<SwimmingRecyclerView.SwimmingViewHolder> {

    private Context context;
    private ArrayList<SwimmingData> swimmingData;
    private String email;

    public SwimmingRecyclerView(Context context, ArrayList<SwimmingData> swimmingData, String email) {
        this.context = context;
        this.swimmingData = swimmingData;
        this.email = email;
    }

    public void update(ArrayList<SwimmingData> swimmingData) {
        this.swimmingData = swimmingData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SwimmingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.swimming_item_rv, parent, false);
        return new SwimmingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SwimmingViewHolder holder, final int position) {
        holder.slotTime.setText(swimmingData.get(position).getSlotTime());
        holder.left.setText("Remaining = " + swimmingData.get(position).getRemaining());
        holder.occupied.setText("Occupied = " + swimmingData.get(position).getOccupied());

        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookSlot(position);
            }
        });

        if(swimmingData.get(position).getRollnos().indexOf(email) != -1
                || swimmingData.get(position).getRemaining() == 0){
            holder.book.setEnabled(false);
        }
        else {
            holder.book.setEnabled(true);
        }


    }

    private void bookSlot(int position) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();

        final ProgressDialog dialog = ProgressDialog.show(this.context, "",
                "Booking Slot. Please wait...", true);


        final String col = "Swimming";
        String doc = "slot" + swimmingData.get(position).getSlotID();

        final DocumentReference sportsRef = db
                .collection(col)
                .document(doc);

        final DocumentReference usersRef = db
                .collection("Users")
                .document(email);

        final String finalEmail = email;

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sportsRef);
                DocumentSnapshot snapshot2 = transaction.get(usersRef);

                SwimmingData sd = snapshot.toObject(SwimmingData.class);
                User user = snapshot2.toObject(User.class);

                if (sd.getRollnos().indexOf(finalEmail) == -1) {
                    //DO DB OPERATIONS

                    if(sd.getRemaining() > 0){

                        //SPORTS
                        sd.getRollnos().add(email);
                        sd.setOccupied(sd.getOccupied() + 1);
                        sd.setRemaining(sd.getRemaining() - 1);

                        String time = sd.getTimeU();
                        String us = time + "_" + col + "_" + sd.getSlotID() +"_3";
                        //USER
                        user.getBookings().add(us);

                        //UPDATE NOW
                        transaction.update(sportsRef, "occupied", sd.getOccupied());
                        transaction.update(sportsRef, "remaining", sd.getRemaining());
                        transaction.update(sportsRef, "rollnos", sd.getRollnos());
                        transaction.update(usersRef, "bookings", user.getBookings());

                    }
                    else{
                        return null;
                    }


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

                dialog.cancel();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("tag", "Transaction failure." + e);
                        Toast.makeText(context, "Slot Booking Failed", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });


    }

    @Override
    public int getItemCount() {
        return swimmingData.size();
    }

    class SwimmingViewHolder extends RecyclerView.ViewHolder {

        TextView slotTime;
        TextView occupied;
        TextView left;
        Button book;


        public SwimmingViewHolder(@NonNull View itemView) {
            super(itemView);
            slotTime = itemView.findViewById(R.id.slottimeRV);
            occupied = itemView.findViewById(R.id.occupiedRV);
            left = itemView.findViewById(R.id.leftRV);
            book = itemView.findViewById(R.id.bookRV);
        }
    }
}
