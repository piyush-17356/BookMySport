package com.iiitd.bookmysport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private int resourceLayout;
    private ArrayList<String> data;

    public ListViewAdapter(@NonNull Context context, int resource, ArrayList<String> data1) {
        super(context, resource, data1);
        mContext = context;
        resourceLayout = resource;
        data = data1;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }



        TextView tv = v.findViewById(R.id.listviewTV);
        tv.setText(data.get(position));

        return v;
    }




}
