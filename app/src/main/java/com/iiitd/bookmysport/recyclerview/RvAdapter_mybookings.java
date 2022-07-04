package com.iiitd.bookmysport.recyclerview;

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
import com.google.common.base.CharMatcher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.iiitd.bookmysport.DynamicData;
import com.iiitd.bookmysport.R;
import com.iiitd.bookmysport.SwimmingData;
import com.iiitd.bookmysport.User;
import com.iiitd.bookmysport.model_myBookings;

import java.util.ArrayList;
import java.util.List;

public class RvAdapter_mybookings extends RecyclerView.Adapter<RvAdapter_mybookings.myBookingsviewHolder> {

    List<model_myBookings> mmodel_myBookings;
    private onListListener OnListListener;

    FirebaseFirestore db;
    Context mContext;

    public RvAdapter_mybookings(Context context, List<model_myBookings> model_myBookings, onListListener OnListListener) {
        mContext = context;
        mmodel_myBookings = model_myBookings;
        db = FirebaseFirestore.getInstance();
        this.OnListListener = OnListListener;
    }

    @NonNull
    @Override
    public myBookingsviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_mybookings, parent, false);
        return new myBookingsviewHolder(view, OnListListener);
    }

    public void update(List<model_myBookings> model_myBookings1) {
        mmodel_myBookings = model_myBookings1;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull myBookingsviewHolder holder, final int position) {

        Log.d("tag", "position = " + position);

        holder.sportNametv.setText(mmodel_myBookings.get(position).getSportName());
        holder.Datetv.setText("Start Time : "+mmodel_myBookings.get(position).getDate());
        holder.Timetv.setText("End Time  : "+mmodel_myBookings.get(position).getEndTime());
        holder.tableNO.setText("Table : "+mmodel_myBookings.get(position).getTableNo());

        Log.d("zzz", mmodel_myBookings.get(position).toString());
        int boookedType = Integer.parseInt(mmodel_myBookings.get(position).getBookedType().trim());
        //SWIMMING
        if (boookedType == 3) {
            holder.bookedType.setVisibility(View.INVISIBLE);
        }//NONE
        else if (boookedType == 0) {
            holder.bookedType.setText("NONE");
        }//SINGLES / DOUBLES
        else {
            holder.bookedType.setText((Integer.parseInt(mmodel_myBookings.get(position).getBookedType())==1)?"Single":"Doubles");
        }


    }

    @Override
    public int getItemCount() {
        Log.d("tag", "size " + mmodel_myBookings.size());
        return mmodel_myBookings.size();
    }


    public class myBookingsviewHolder extends FragRecyclerViewAdaptor.ViewHolder implements View.OnClickListener {
        TextView sportNametv, Datetv, Timetv;
        TextView tableNO, bookedType;
        Button cancelbtn;
        onListListener OnListListener;

        public myBookingsviewHolder(View view, onListListener OnListListener) {
            super(view);
            sportNametv = view.findViewById(R.id.mybookings_sportname);
            Datetv = view.findViewById(R.id.mybookings_Date);
            Timetv = view.findViewById(R.id.mybookings_Time);
            this.OnListListener = OnListListener;
            cancelbtn = view.findViewById(R.id.mybookings_Cancel);
            cancelbtn.setOnClickListener(this);
            tableNO = view.findViewById(R.id.mybookings_tableNO);
            bookedType = view.findViewById(R.id.mybookings_bookedType);
        }

        @Override
        public void onClick(View view) {
            OnListListener.onListClick(getAdapterPosition());

            //NEED TO CHANGE ALL THIS, CANCELLING OF DYNAMIC DATA
            final int position = getAdapterPosition();

            String sportsName = sportNametv.getText().toString();
            String doc = "";

            switch (sportsName) {
                case "Swimming":
                    doc = "slot";
                    deleteSwimming(sportsName, doc + mmodel_myBookings.get(position).getTableNo(),
                            mmodel_myBookings.get(position).getStringInFirebaseFormat());
                    break;
                case "TableTennis":
                case "Tennis":
                case "Squash":
                case "Badminton":
                    doc = "table";
                    deletePairs(sportsName, doc + mmodel_myBookings.get(position).getTableNo(),
                            mmodel_myBookings.get(position).getStringInFirebaseFormat(),
                            mmodel_myBookings.get(position));
                    break;
                case "Basketball":
                case "Football":
                    //NEED TO IMPLEMENT THIS
                    doc = "NONE";
                    break;
            }


        }

        private void deleteSwimming(String sportsName, String doc, String stringInFirebaseFormat) {
            final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

            final DocumentReference sportsRef = db
                    .collection(sportsName)
                    .document(doc);

            final DocumentReference usersDoc = db.collection("Users").document(email);

            final String toRemove = stringInFirebaseFormat;

            Log.d("zzz", sportsName + "--" + doc);
            db.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    DocumentSnapshot snapshot = transaction.get(sportsRef);

                    SwimmingData doc = snapshot.toObject(SwimmingData.class);

                    doc.setRemaining(doc.getRemaining() + 1);
                    doc.setOccupied(doc.getOccupied() - 1);
                    ArrayList<String> arrayList = doc.getRollnos();
                    arrayList.remove(email);

                    transaction.update(sportsRef, "occupied", doc.getOccupied());
                    transaction.update(sportsRef, "remaining", doc.getRemaining());
                    transaction.update(sportsRef, "rollnos", arrayList);

                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("tag", "Transaction success!");
                    Toast.makeText(mContext, "Slot Successfully Removed", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag", "Transaction failure." + e);
                            Toast.makeText(mContext, "Slot Deletion Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


            //USER
            db.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    DocumentSnapshot snapshot2 = transaction.get(usersDoc);
                    User user = snapshot2.toObject(User.class);

                    user.getBookings().remove(toRemove);
                    transaction.update(usersDoc, "bookings", user.getBookings());
                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("tag", "Transaction success!");
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag", "Transaction failure." + e);
                            Toast.makeText(mContext, "Slot booking failed", Toast.LENGTH_SHORT).show();
                        }
                    });


        }
    }

    private void deletePairs(String sportsName, String doc, String stringInFirebaseFormat, model_myBookings model_myBookings) {
        final String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        final DocumentReference sportsRef = db
                .collection(sportsName)
                .document(doc);

        final DocumentReference usersDoc = db.collection("Users").document(email);

        final String toRemove = stringInFirebaseFormat;

        String ss = email.split("@")[0];
        String theDigits = CharMatcher.inRange('0', '9').retainFrom(ss);
        String rollno = "-";
        if(theDigits!= null && !theDigits.isEmpty()) {
            rollno = "20" + theDigits;
        }

        String start = model_myBookings.getStartTime();
        String end = model_myBookings.getEndTime();
        String bookedType = model_myBookings.getBookedType();
        String tableNO = model_myBookings.getTableNo();
        final String toRemoveSports = start + "AM_" + end + "PM_" + tableNO + "_" + bookedType + "_" + rollno + "@iiitd.ac.in";

        Log.d("zzz", sportsName + "--" + doc);
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sportsRef);

                DynamicData doc = snapshot.toObject(DynamicData.class);

                for(int i=0;i<doc.getData().size();i++){
                    if(doc.getData().get(i).contains(toRemoveSports)){
                        doc.getData().remove(i);
                        break;
                    }
                }

                transaction.update(sportsRef, "data", doc.getData());

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("tag", "Transaction success!");
                Toast.makeText(mContext, "Slot Successfully Removed", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("tag", "Transaction failure." + e);
                        Toast.makeText(mContext, "Slot Deletion Failed", Toast.LENGTH_SHORT).show();
                    }
                });


        //USER
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot2 = transaction.get(usersDoc);
                User user = snapshot2.toObject(User.class);

                user.getBookings().remove(toRemove);
                transaction.update(usersDoc, "bookings", user.getBookings());
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("tag", "Transaction success!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("tag", "Transaction failure." + e);
                        Toast.makeText(mContext, "Slot booking failed", Toast.LENGTH_SHORT).show();
                    }
                });


    }


    public interface onListListener {
        void onListClick(int pos);
    }

}
