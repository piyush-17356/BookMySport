package com.iiitd.bookmysport.bookings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iiitd.bookmysport.R;
import com.iiitd.bookmysport.Time_Data;
import com.iiitd.bookmysport.other.Functions;
import com.iiitd.bookmysport.recyclerview.FragRecyclerViewAdaptor;

import java.util.ArrayList;

public class BookingTodayFragment extends Fragment {

    private Time_Data time_data;
    private FragRecyclerViewAdaptor adaptor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        ArrayList<Document> list = (ArrayList<Document>) bundle.getSerializable("Data");

        int type = bundle.getInt("Type");

        time_data = new Time_Data(type);
        time_data.initialize(list);

        String col = Functions.getSportsName(type);
        String doc = Functions.getSportsDocName(type);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(col)
                .document(doc)
                .collection("Today")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {

                        ArrayList<Document> docs = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            Document document = doc.toObject(Document.class);
                            docs.add(document);
                        }

                        time_data.initialize(docs);
                        adaptor.update(time_data);
                    }
                });

    }

    public BookingTodayFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bookingfragmenttodaylayout, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.FragRecyclerViewToday);
        adaptor = new FragRecyclerViewAdaptor(getContext(), time_data, 1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adaptor);
        return view;
    }
}
