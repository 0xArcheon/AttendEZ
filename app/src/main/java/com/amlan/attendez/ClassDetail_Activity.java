package com.amlan.attendez;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amlan.attendez.Adapter.StudentsListAdapter;
import com.amlan.attendez.Firebase.Attendance_Reports;
import com.amlan.attendez.Firebase.Attendance_Students_List;
import com.amlan.attendez.Firebase.Students_List;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;


public class ClassDetail_Activity extends AppCompatActivity {

    private ImageView themeImage;
    private TextView className, total_students, place_holder;
    private CardView addStudent, reports_open;
    private Button submit_btn;
    private EditText student_name, reg_no, mobile_no;
    private LinearLayout layout_attendance_taken;
    private RecyclerView mRecyclerview;
    ArrayList<com.amlan.attendez.Firebase.Students_List> students;

    String room_ID, subject_Name, class_Name;

    public static final String TAG = "ClassDetail_Activity";

    DatabaseReference mDatabase;

    private Handler handler = new Handler();
    StudentsListAdapter mAdapter;

    ProgressBar progressBar;
    Dialog lovelyCustomDialog;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail_);

        getWindow().setExitTransition(null);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final String theme = getIntent().getStringExtra("theme");
        class_Name = getIntent().getStringExtra("className");
        subject_Name = getIntent().getStringExtra("subjectName");
        room_ID = getIntent().getStringExtra("classroom_ID");


        Toolbar toolbar = findViewById(R.id.toolbar_class_detail);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_disease_detail);
        collapsingToolbarLayout.setTitle(subject_Name);

        themeImage = findViewById(R.id.image_disease_detail);
        className = findViewById(R.id.classname_detail);
        total_students = findViewById(R.id.total_students_detail);
        layout_attendance_taken = findViewById(R.id.attendance_taken_layout);
        layout_attendance_taken.setVisibility(View.GONE);
        addStudent = findViewById(R.id.add_students);
        reports_open = findViewById(R.id.reports_open_btn);
        className.setText(class_Name);
        mRecyclerview = findViewById(R.id.recyclerView_detail);
        progressBar = findViewById(R.id.progressbar_detail);
        place_holder = findViewById(R.id.placeholder_detail);
        place_holder.setVisibility(View.GONE);
        submit_btn = findViewById(R.id.submit_attendance_btn);
        submit_btn.setVisibility(View.GONE);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        switch (theme) {
            case "0":
                themeImage.setImageResource(R.drawable.asset_bg_paleblue);
                break;
            case "1":
                themeImage.setImageResource(R.drawable.asset_bg_green);

                break;
            case "2":
                themeImage.setImageResource(R.drawable.asset_bg_yellow);

                break;
            case "3":
                themeImage.setImageResource(R.drawable.asset_bg_palegreen);

                break;
            case "4":
                themeImage.setImageResource(R.drawable.asset_bg_paleorange);

                break;
            case "5":
                themeImage.setImageResource(R.drawable.asset_bg_white);
                break;

        }

        //---------------------------------

       Runnable r = new Runnable() {
            @Override
            public void run() {
                FirebaseInit();
                progressBar.setVisibility(View.GONE);
            }
        };
        handler.postDelayed(r, 500);

        //----------------------------------------

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase = FirebaseDatabase.getInstance().getReference();
                Query countQ = mDatabase.child("Students").orderByChild("class_id").equalTo(room_ID);
                countQ.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ss : snapshot.getChildren())
                        {
                            long count = ss.getChildrenCount();
                            final String size, size2;
                            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ClassDetail_Activity.this);
                            size = String.valueOf(preferences.getAll().size());
                            size2 = String.valueOf(count);

                                submitAttendance();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        reports_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassDetail_Activity.this, Reports_Activity.class);
                intent.putExtra("class_name", class_Name);
                intent.putExtra("subject_name", subject_Name);
                intent.putExtra("room_ID", room_ID);
                startActivity(intent);
            }
        });


        addStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = LayoutInflater.from(ClassDetail_Activity.this);
                final View view1 = inflater.inflate(R.layout.popup_add_student, null);
                student_name = view1.findViewById(R.id.name_student_popup);
                reg_no = view1.findViewById(R.id.regNo_student_popup);
                mobile_no = view1.findViewById(R.id.mobileNo_student_popup);

                lovelyCustomDialog = new LovelyCustomDialog(ClassDetail_Activity.this)
                        .setView(view1)
                        .setTopColorRes(R.color.theme_light)
                        .setTitle("Add Student")
                        .setIcon(R.drawable.ic_baseline_person_add_24)
                        .setCancelable(false)
                        .setListener(R.id.add_btn_popup, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String name = student_name.getText().toString();
                                String regNo = reg_no.getText().toString();
                                String mobNo = mobile_no.getText().toString();

                                if (isValid()) {
                                    addStudentMethod(name, regNo, mobNo);
                                } else {
                                    Toast.makeText(ClassDetail_Activity.this, "Please fill all the details..", Toast.LENGTH_SHORT).show();
                                }


                            }
                        })
                        .setListener(R.id.cancel_btn_popup, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                lovelyCustomDialog.dismiss();
                            }
                        })
                        .show();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    public void FirebaseInit()
    {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query countQuery = mDatabase.child("Students").orderByChild("class_id").equalTo(room_ID);
        final String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
        countQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s : snapshot.getChildren())
                {
                    long count = s.getChildrenCount();
                    total_students.setText("Total Students : " + count);
                    if (!(count == 0)) {
                        submit_btn.setVisibility(View.VISIBLE);
                        place_holder.setVisibility(View.GONE);
                    } else if (count == 0) {
                        submit_btn.setVisibility(View.GONE);
                        place_holder.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Query reportSizeQuery = mDatabase.child("Attendance Reports").orderByChild("date_and_classID").equalTo(date + room_ID);
        reportSizeQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s : snapshot.getChildren())
                {
                    long reports_size = s.getChildrenCount();
                    if (!(reports_size == 0)) {
                        layout_attendance_taken.setVisibility(View.VISIBLE);
                        submit_btn.setVisibility(View.GONE);
                    } else {
                        layout_attendance_taken.setVisibility(View.GONE);
                        submit_btn.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        students = new ArrayList<>();
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        String extraClick = "";
        mAdapter = new StudentsListAdapter(students, ClassDetail_Activity.this, date + room_ID, extraClick);
        mRecyclerview.setAdapter(mAdapter);

        Query mQuery = mDatabase.child("Students").orderByChild("class_id").equalTo(room_ID);
        mQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                students.clear();
                for ( DataSnapshot ds : snapshot.getChildren()){
                    Students_List list = ds.getValue(Students_List.class);
                    students.add(list);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void submitAttendance() {

        final ProgressDialog progressDialog = new ProgressDialog(ClassDetail_Activity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
        final String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
        final ArrayList<Attendance_Students_List> list = new ArrayList<>();
        Query q = mDatabase.child("Attendance Students List").orderByChild("date_and_classID").equalTo(date +room_ID);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s : snapshot.getChildren())
                {
                    Attendance_Students_List list_students = snapshot.getValue(Attendance_Students_List.class);
                    list.add(list_students);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        try {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            final String dateOnly = String.valueOf(calendar.get(Calendar.DATE));
            @SuppressLint("SimpleDateFormat") final String monthOnly = new SimpleDateFormat("MMM").format(calendar.getTime());
            Attendance_Reports attendance_reports = new Attendance_Reports(date, monthOnly, dateOnly, room_ID, date + room_ID, class_Name, subject_Name, list);
            mDatabase.child("Attendance Reports").push().setValue(attendance_reports).addOnCompleteListener
                    (new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            Toast.makeText(ClassDetail_Activity.this, "Attendance Submitted", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

         catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(ClassDetail_Activity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        super.onDestroy();
    }

    public void addStudentMethod(final String studentName, final String regNo, final String mobileNo) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        String id = studentName+regNo;
        String name_student = student_name.getText().toString().trim();
        String regNo_student = reg_no.getText().toString().trim();
        String mobileNo_student = mobile_no.getText().toString().trim();
        String class_id = userId+class_Name;
        final ProgressDialog progressDialog = new ProgressDialog(ClassDetail_Activity.this);
        progressDialog.setMessage("Creating class..");
        progressDialog.show();
        mDatabase = FirebaseDatabase.getInstance().getReference("Students");
        com.amlan.attendez.Firebase.Students_List students_list = new com.amlan.attendez.Firebase.Students_List(id, name_student, regNo_student, mobileNo_student, class_id);
        mDatabase.push().setValue(students_list).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                lovelyCustomDialog.dismiss();
                Toast.makeText(ClassDetail_Activity.this, "Added students successfully", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public boolean isValid() {

        if (student_name.getText().toString().isEmpty() || reg_no.getText().toString().isEmpty() || mobile_no.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_class_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}