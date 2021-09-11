package com.amlan.attendez;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.amlan.attendez.Adapter.ReportsAdapter;
import com.amlan.attendez.realm.Attendance_Reports;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;

public class Reports_Activity extends AppCompatActivity {

    String subjectName, className, room_ID;
    RecyclerView recyclerView;
    Realm realm;
    DatabaseReference mDatabase;
    ReportsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        //Realm.init(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        subjectName = getIntent().getStringExtra("subject_name");
        className = getIntent().getStringExtra("class_name");
        room_ID = getIntent().getStringExtra("room_ID");

        recyclerView = findViewById(R.id.recyclerView_reports);

        Toolbar toolbar = findViewById(R.id.toolbar_reports);
        setSupportActionBar(toolbar);
        toolbar.setTitle(subjectName);
        toolbar.setSubtitle(className);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        
        RealmResults<Attendance_Reports> results;
        realm = Realm.getDefaultInstance();
        results = realm.where(Attendance_Reports.class)
                .equalTo("classId", room_ID)
                .findAll();


        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);

        recyclerView.setLayoutManager(gridLayoutManager);

        mAdapter = new ReportsAdapter( results,Reports_Activity.this, room_ID);
        recyclerView.setAdapter(mAdapter);

    }

}