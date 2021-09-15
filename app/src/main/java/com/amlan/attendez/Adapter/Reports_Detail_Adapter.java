package com.amlan.attendez.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.amlan.attendez.Firebase.Attendance_Students_List;
import com.amlan.attendez.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class Reports_Detail_Adapter extends RecyclerView.Adapter<Reports_Detail_Adapter.MyViewHolder> {

    private final Activity mActivity;
    ArrayList<com.amlan.attendez.Firebase.Attendance_Students_List> mList;
    String stuID, mroomID;
    DatabaseReference mDatabase;

    public Reports_Detail_Adapter(Activity mActivity, ArrayList<Attendance_Students_List> mList, String mroomID) {
        this.mActivity = mActivity;
        this.mList = mList;
        this.mroomID = mroomID;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_detail_adapter_item, parent, false);
        return new MyViewHolder(itemView, mActivity, mList, mroomID);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Attendance_Students_List temp = mList.get(position);
        holder.namE.setText(temp.getStudentName());
        holder.regNo.setText(temp.getStudentRegNo());
        if (temp.getAttendance().equals("Present")){
            holder.status.setText("P");
            holder.circle.setCardBackgroundColor(mActivity.getResources().getColor(R.color.green_new));
        }else{
            holder.status.setText("A");
            holder.circle.setCardBackgroundColor(mActivity.getResources().getColor(R.color.red_new));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView namE;
        public TextView regNo;
        public TextView status;
        public CardView circle;
        public Activity mActivity;
        ArrayList<com.amlan.attendez.Firebase.Attendance_Students_List> mList;

        public MyViewHolder(@NonNull final View itemView, Activity MainActivity, ArrayList<Attendance_Students_List> list, String mroomID) {
            super(itemView);

            namE = itemView.findViewById(R.id.student_name_report_detail_adapter);
            regNo = itemView.findViewById(R.id.student_regNo_report_detail_adapter);
            status = itemView.findViewById(R.id.status_report_detail_adapter);
            circle = itemView.findViewById(R.id.cardView_report_detail_adapter);

            mActivity = MainActivity;
            mList = list;

        }
    }

    /*public Reports_Detail_Adapter(RealmResults<Attendance_Students_List> list, Activity context, String roomID) {

        super(context, list, true);

        mActivity = context;
        mList = list;
        mroomID =roomID;
    }

    @NonNull
    @Override
    public ViewHolder_reports_detail onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_detail_adapter_item, parent, false);
        return new ViewHolder_reports_detail(itemView, mActivity, mList);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_reports_detail holder, int position) {
        Attendance_Students_List temp = getItem(position);
        holder.namE.setText(temp.getStudentName());
        holder.regNo.setText(temp.getStudentRegNo());
        if (temp.getAttendance().equals("Present")){
            holder.status.setText("P");
            holder.circle.setCardBackgroundColor(mActivity.getResources().getColor(R.color.green_new));
        }else{
            holder.status.setText("A");
            holder.circle.setCardBackgroundColor(mActivity.getResources().getColor(R.color.red_new));
        }
    } */


}
