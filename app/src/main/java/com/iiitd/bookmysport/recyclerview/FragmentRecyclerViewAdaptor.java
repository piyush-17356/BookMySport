package com.iiitd.bookmysport.recyclerview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;


public class FragmentRecyclerViewAdaptor extends FragmentPagerAdapter {

    private final ArrayList<Fragment> fragments = new ArrayList<>();
    private final ArrayList<String> Titles = new ArrayList<>();

    public FragmentRecyclerViewAdaptor(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return Titles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return Titles.get(position);
    }

    public void AddFragment(Fragment fragment, String Title) {
        fragments.add(fragment);
        Titles.add(Title);
    }
}
