package com.iiitd.bookmysport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TimeSlotsRecyclerView extends RecyclerView.Adapter<com.iiitd.bookmysport.TimeSlotsRecyclerView.ViewHolder> {

    private Context context;
    private SportsSDData sportsSDData;

    public TimeSlotsRecyclerView(Context context, SportsSDData sportsSDData) {
        this.context = context;
        this.sportsSDData = sportsSDData;
    }

    void update(SportsSDData sportsSDData) {
        this.sportsSDData = sportsSDData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_show, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

//        holder.left.setAdapter(
//                new ArrayAdapter<String>(
//                        context, android.R.layout.simple_spinner_item,
//                        sportsSDData.getC_slots().get(position).uc_left ));

//        holder.right.setAdapter(
//                new ArrayAdapter<String>(
//                        context, android.R.layout.simple_spinner_item,
//                        sportsSDData.getC_slots().get(position).uc_right ));


        ListAdapter listAdapter = new ListViewAdapter(
                context, R.layout.listview_item, sportsSDData.getC_slots().get(position).uc_left);

        holder.left.setAdapter(listAdapter);


        ListAdapter listAdapter2 = new ListViewAdapter(
                context, R.layout.listview_item, sportsSDData.getC_slots().get(position).uc_right);

        holder.right.setAdapter(listAdapter2);


        int leftHeight = 0;
        int rightHeight = 0;

        int totalHeight = 0;
        for (int i = 0; i < holder.left.getAdapter().getCount(); i++) {
            View listItem = holder.left.getAdapter().getView(i, null, null);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = holder.left.getLayoutParams();
        leftHeight = totalHeight + (holder.left.getDividerHeight() * (holder.left.getAdapter().getCount() - 1));
//        params.height = totalHeight + (holder.left.getDividerHeight() * (holder.left.getAdapter().getCount() - 1));
//        holder.left.setLayoutParams(params);
//        holder.left.requestLayout();

        int totalHeight2 = 0;
        for (int i = 0; i < holder.right.getAdapter().getCount(); i++) {
            View listItem2 = holder.right.getAdapter().getView(i, null, null);
            listItem2.measure(0, 0);
            totalHeight2 += listItem2.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params2 = holder.right.getLayoutParams();
        rightHeight = totalHeight2 + (holder.right.getDividerHeight() * (holder.right.getAdapter().getCount() - 1));
//        params.height = totalHeight2 + (holder.right.getDividerHeight() * (holder.right.getAdapter().getCount() - 1));
//        holder.right.setLayoutParams(params2);
//        holder.right.requestLayout();

        int maxheight = leftHeight > rightHeight ? leftHeight : rightHeight;
        ViewGroup.LayoutParams f_param = holder.linearLayout.getLayoutParams();
        f_param.height = maxheight;

        holder.linearLayout.setLayoutParams(f_param);
        holder.linearLayout.requestLayout();
//        Random random = new Random();
//        int color = Color.argb(
//                255,
//                random.nextInt(256),
//                random.nextInt(256),
//                random.nextInt(256));
//        holder.left.setBackgroundColor(color);
//        holder.right.setBackgroundColor(color);

//        holder.left.setText(sportsSDData.getC_slots().get(position).uc_left.toString());
//        holder.right.setText(sportsSDData.getC_slots().get(position).uc_right.toString());

    }


    @Override
    public int getItemCount() {
        return sportsSDData.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ListView left;
        ListView right;
        LinearLayout linearLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            left = itemView.findViewById(R.id.left);
            right = itemView.findViewById(R.id.right);
            linearLayout = itemView.findViewById(R.id.time_show_linear_layout);

        }

    }
}