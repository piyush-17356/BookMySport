package com.iiitd.bookmysport.bookings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.iiitd.bookmysport.R;
import com.iiitd.bookmysport.other.Functions;
import com.iiitd.bookmysport.recyclerview.FragmentRecyclerViewAdaptor;

import java.util.ArrayList;

public class Booking extends AppCompatActivity {

    private static String TAG = "Tag";

    private int sportsType;

    private ArrayList<Document> list;
    private ArrayList<Document> list2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking2);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            sportsType = bundle.getInt("Type");
        }

        list = (ArrayList<Document>) getIntent().getSerializableExtra("Data");
        list2 = (ArrayList<Document>) getIntent().getSerializableExtra("Data2");

        String sportsName = Functions.getSportsName(sportsType);

        TabLayout tabLayout = findViewById(R.id.FragTab);
        ViewPager viewPager = findViewById(R.id.FragViewPager);
        FragmentRecyclerViewAdaptor adaptor = new FragmentRecyclerViewAdaptor(getSupportFragmentManager());

        BookingTodayFragment bookingTodayFragment = new BookingTodayFragment();
        Bundle bun = new Bundle();
        bun.putSerializable("Data", list);
        bun.putInt("Type", sportsType);
        bookingTodayFragment.setArguments(bun);


        BookingTomFragment bookingTomFragment = new BookingTomFragment();
        Bundle bun2 = new Bundle();
        bun2.putSerializable("Data", list2);
        bun2.putInt("Type", sportsType);
        bookingTomFragment.setArguments(bun2);

        adaptor.AddFragment(bookingTodayFragment, "Today");
        adaptor.AddFragment(bookingTomFragment, "Tomorrow");
        viewPager.setAdapter(adaptor);
        tabLayout.setupWithViewPager(viewPager);

    }
}