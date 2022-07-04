package com.iiitd.bookmysport.recyclerview;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.iiitd.bookmysport.DynamicData;
import com.iiitd.bookmysport.GuardItemInfo;
import com.iiitd.bookmysport.R;

import java.util.List;

public class RvAdapter_guard extends RecyclerView.Adapter<RvAdapter_guard.GaurdViewHolder>{

    List<GuardItemInfo> details;
    Context mContext;

    public RvAdapter_guard(Context context,List<GuardItemInfo> details){
        mContext=context;
        this.details=details;
    }

    public void update(List<GuardItemInfo> details1) {
        details=details1;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GaurdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_guard,parent,false);
        return new GaurdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GaurdViewHolder holder, final int position) {
        holder.sportstv.setText("Sports Name: "+details.get(position).getSportName());
        holder.starttv.setText("Start Time: "+details.get(position).getStarttime().substring(0,5));
        holder.endtv.setText("End Time: "+details.get(position).getEndtime().substring(0,5));
        holder.rollnotv.setText("Roll no: "+details.get(position).getRollno());
        holder.tablenotv.setText("Table : " + details.get(position).getTableNO());
        String type1 = "Single";
        String type2 = "Doubles";
        if(Integer.parseInt(details.get(position).getBookingType()) == 1)
            holder.bookedtypetv.setText(type1);
        else if(Integer.parseInt(details.get(position).getBookingType()) == 2)
            holder.bookedtypetv.setText(type2);
        else
            holder.bookedtypetv.setVisibility(View.INVISIBLE);


        holder.reachedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmSlot(details.get(position).getDBMatchableString());
            }
        });

    }

    private void confirmSlot(String[] dbMatchableString) {

        final ProgressDialog dialog = ProgressDialog.show(mContext, "",
                "Confirming. Please wait...", true);


        final String col = dbMatchableString[0];
        final String match = dbMatchableString[1];
        final String doc = dbMatchableString[2];

        Log.d("zzz",col);
        Log.d("zzz",match);
        Log.d("zzz",doc);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference sportsRef = db
                .collection(col)
                .document(doc);

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sportsRef);

                DynamicData dynamicData = snapshot.toObject(DynamicData.class);

                int index = dynamicData.getData().indexOf(match);

                if(index != -1){
                    int len = dynamicData.getData().get(index).length();
                    String s = dynamicData.getData().get(index).substring(0, len - 1);
                    s = s + "1";
                    dynamicData.getData().set(index, s);

                    //UPDATE
                    transaction.update(sportsRef, "data", dynamicData.getData());

                    //REFRESH

                } else {
                    Toast.makeText(mContext, "Slot not removable", Toast.LENGTH_SHORT).show();
                }

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("tag", "Transaction success!");
                Toast.makeText(mContext, "Slot Confirmed Successfully", Toast.LENGTH_SHORT).show();

                dialog.cancel();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("tag", "Transaction failure." + e);
                        Toast.makeText(mContext, "Slot Removal Failed", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });




    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class GaurdViewHolder extends RecyclerView.ViewHolder{
        TextView sportstv,starttv,endtv,rollnotv,tablenotv,bookedtypetv;
        Button reachedbtn,notreachedbtn;
        public GaurdViewHolder(@NonNull View itemView) {
            super(itemView);
            sportstv = itemView.findViewById(R.id.guard_sportname);
            starttv = itemView.findViewById(R.id.guard_Starttime);
            endtv = itemView.findViewById(R.id.guard_Endtime);
            rollnotv = itemView.findViewById(R.id.guard_Rollno);
            tablenotv = itemView.findViewById(R.id.guard_tableno);
            bookedtypetv = itemView.findViewById(R.id.guard_bookedtype);
            reachedbtn = itemView.findViewById(R.id.guard_btn_reached);
            notreachedbtn = itemView.findViewById(R.id.guard_btn_notreached);

        }
    }
}
