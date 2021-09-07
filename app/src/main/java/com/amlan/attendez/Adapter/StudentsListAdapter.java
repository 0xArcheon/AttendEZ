package com.amlan.attendez.Adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.amlan.attendez.BottomSheet.Student_Edit_Sheet;
import com.amlan.attendez.Firebase.Class_Names;
import com.amlan.attendez.Firebase.Students_List;
import com.amlan.attendez.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class StudentsListAdapter extends RecyclerView.Adapter<StudentsListAdapter.MyViewHolder>{
    ArrayList<Students_List> mList;
    private Activity mActivity;
    String stuID, mroomID;

    public StudentsListAdapter(ArrayList<Students_List> mList, Activity mActivity, String stuID, String mroomID)
    {
        this.mList = mList;
        this.mActivity = mActivity;
        this.stuID = stuID;
        this.mroomID = mroomID;
    }

    @NonNull
    @Override
    public StudentsListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_attendance_adapter, parent, false);
        return new MyViewHolder(itemView, mActivity, mList, mroomID);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentsListAdapter.MyViewHolder holder, int position) {
        Students_List temp = mList.get(position);
        holder.student_name.setText(temp.getName_student());
        holder.student_regNo.setText(temp.getRegNo_student());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        stuID = temp.getRegNo_student();
        String value = preferences.getString(stuID, null);
        if (value==null){

        }else {
            if (value.equals("Present")) {
                holder.radioButton_present.setChecked(true);
            } else {
                holder.radioButton_absent.setChecked(true);
            }
        }

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public Activity mActivity;
        //RealmResults<Students_List> mList;
        ArrayList<com.amlan.attendez.Firebase.Students_List> mList;

        public final TextView student_name;
        public final TextView student_regNo;
        public LinearLayout layout;
        public String stuName, regNo, mobileNo, mRoomID;
        public RadioGroup radioGroup;
        public RadioButton radioButton_present, radioButton_absent;
        DatabaseReference mDatabase;

        public MyViewHolder(@NonNull final View itemView, Activity MainActivity, ArrayList<Students_List> list, final String roomID)
        {
            super(itemView);

            student_name = itemView.findViewById(R.id.student_name_adapter);
            student_regNo = itemView.findViewById(R.id.student_regNo_adapter);
            radioGroup = itemView.findViewById(R.id.radioGroup);
            radioButton_present = itemView.findViewById(R.id.radio_present);
            radioButton_absent = itemView.findViewById(R.id.radio_absent);
            layout = itemView.findViewById(R.id.layout_click);

            mActivity = MainActivity;
            mList = list;
            mRoomID = roomID;
            mDatabase = FirebaseDatabase.getInstance().getReference();
            Query query = mDatabase.child("Attendance Reports").orderByChild("date_and_classID").equalTo(roomID);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot snap: snapshot.getChildren())
                    {
                        long reports_size = snap.getChildrenCount();
                        if (!(reports_size==0)){
                            radioGroup.setVisibility(View.GONE);
                        }else if (reports_size==0) {
                            radioGroup.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            radioButton_present.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String attendance = "Present";
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(mList.get(getAbsoluteAdapterPosition()).getRegNo_student(), attendance);
                    editor.apply();

                    mDatabase = FirebaseDatabase.getInstance().getReference("Attendance List");
                    final com.amlan.attendez.Firebase.Attendance_Students_List attendance_students_list = new com.amlan.attendez.Firebase.Attendance_Students_List();
                    attendance_students_list.setStudentName((mList.get(getAbsoluteAdapterPosition()).getName_student()));
                    attendance_students_list.setAttendance(attendance);
                    attendance_students_list.setMobNo((mList.get(getAbsoluteAdapterPosition()).getMobileNo_student()));
                    attendance_students_list.setStudentRegNo(mList.get(getAbsoluteAdapterPosition()).getRegNo_student());
                    attendance_students_list.setClassID(mList.get(getAbsoluteAdapterPosition()).getClass_id());
                    attendance_students_list.setDate_and_classID(mRoomID);
                    attendance_students_list.setUnique_ID(mList.get(getAbsoluteAdapterPosition()).getRegNo_student()+mRoomID);
                    mDatabase.push().setValue(attendance_students_list).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
            });
            radioButton_absent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String attendance = "Absent";
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(mList.get(getAbsoluteAdapterPosition()).getRegNo_student(), attendance);
                    editor.apply();
                    mDatabase = FirebaseDatabase.getInstance().getReference("Attendance List");
                    final com.amlan.attendez.Firebase.Attendance_Students_List attendance_students_list = new com.amlan.attendez.Firebase.Attendance_Students_List();
                    attendance_students_list.setStudentName((mList.get(getAbsoluteAdapterPosition()).getName_student()));
                    attendance_students_list.setAttendance(attendance);
                    attendance_students_list.setMobNo((mList.get(getAbsoluteAdapterPosition()).getMobileNo_student()));
                    attendance_students_list.setStudentRegNo(mList.get(getAbsoluteAdapterPosition()).getRegNo_student());
                    attendance_students_list.setClassID(mList.get(getAbsoluteAdapterPosition()).getClass_id());
                    attendance_students_list.setDate_and_classID(mRoomID);
                    attendance_students_list.setUnique_ID(mList.get(getAbsoluteAdapterPosition()).getRegNo_student()+mRoomID);
                    mDatabase.push().setValue(attendance_students_list).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
            });

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stuName = mList.get(getAbsoluteAdapterPosition()).getName_student();
                    regNo = mList.get(getAbsoluteAdapterPosition()).getRegNo_student();
                    mobileNo = mList.get(getAbsoluteAdapterPosition()).getMobileNo_student();
                    Student_Edit_Sheet student_edit_sheet = new Student_Edit_Sheet(stuName, regNo, mobileNo);
                    student_edit_sheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme);
                    student_edit_sheet.show(((FragmentActivity)view.getContext()).getSupportFragmentManager(), "BottomSheet");
                }
            });
        }
    }
}
/*
public class StudentsListAdapter extends RealmRecyclerViewAdapter<Students_List, ViewHolder_students> {

    private final Activity mActivity;
    RealmResults<Students_List> mList;
    String stuID, mroomID;
    Realm realm = Realm.getDefaultInstance();

    public StudentsListAdapter(RealmResults<Students_List> list, Activity context, String roomID, String extraClick) {

        super(context, list, true);

        mActivity = context;
        mList = list;
        mroomID =roomID;
    }

    @NonNull
    @Override
    public ViewHolder_students onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_attendance_adapter, parent, false);
        return new ViewHolder_students(itemView, mActivity, mList, mroomID);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_students holder, final int position) {
        Students_List temp = getItem(position);
        holder.student_name.setText(temp.getName_student());
        holder.student_regNo.setText(temp.getRegNo_student());


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        stuID = temp.getRegNo_student();
        String value = preferences.getString(stuID, null);
        if (value==null){

        }else {
            if (value.equals("Present")) {
                holder.radioButton_present.setChecked(true);
            } else {
                holder.radioButton_absent.setChecked(true);
            }
        }
    }

} */
