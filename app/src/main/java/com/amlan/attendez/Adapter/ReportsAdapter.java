package com.amlan.attendez.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amlan.attendez.Firebase.Attendance_Reports;
import com.amlan.attendez.R;
import com.amlan.attendez.Reports_Detail_Activity;

import java.util.ArrayList;


public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.MyViewHolder> {

    private final Activity mActivity;
    ArrayList<com.amlan.attendez.Firebase.Attendance_Reports> mList;
    String stuID, mroomID;

    public ReportsAdapter(Activity mActivity, ArrayList<com.amlan.attendez.Firebase.Attendance_Reports> mList, String mroomID) {
        this.mActivity = mActivity;
        this.mList = mList;
        this.mroomID = mroomID;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reports_adapter_item, parent, false);
        return new MyViewHolder(itemView, mActivity, mList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Attendance_Reports temp = mList.get(position);
        holder.month.setText(temp.getMonthOnly());
        holder.date.setText(temp.getDateOnly());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView month;
        public TextView date;
        public Activity mActivity;
        ArrayList<Attendance_Reports> mList;

        public MyViewHolder(@NonNull View itemView, Activity MainActivity, ArrayList<Attendance_Reports> list) {
            super(itemView);
            month = itemView.findViewById(R.id.month_report_adapter);
            date = itemView.findViewById(R.id.date_report_adapter);

            mActivity = MainActivity;
            mList = list;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), Reports_Detail_Activity.class);
                    intent.putExtra("ID", mList.get(getAbsoluteAdapterPosition()).getDate_and_classID());
                    intent.putExtra("date", mList.get(getAbsoluteAdapterPosition()).getDate());
                    intent.putExtra("subject", mList.get(getAbsoluteAdapterPosition()).getSubjName());
                    intent.putExtra("class", mList.get(getAbsoluteAdapterPosition()).getClassname());
                    view.getContext().startActivity(intent);
                }
            });
        }

    }
}



