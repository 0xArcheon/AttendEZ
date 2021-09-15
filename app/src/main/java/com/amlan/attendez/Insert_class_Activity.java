package com.amlan.attendez;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amlan.attendez.Firebase.Class_Names;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Objects;

import co.ceryle.radiorealbutton.library.RadioRealButton;
import co.ceryle.radiorealbutton.library.RadioRealButtonGroup;
import io.realm.Realm;
import io.realm.RealmAsyncTask;

public class Insert_class_Activity extends AppCompatActivity {

    Button create_button;
    EditText _className;
    EditText _subjectName;

    private DatabaseReference mDatabase;

    public String position_bg = "0";

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_class_);

        Toolbar toolbar = findViewById(R.id.toolbar_insert_class);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        create_button = findViewById(R.id.button_createClass);
        _className = findViewById(R.id.className_createClass);
        _subjectName = findViewById(R.id.subjectName_createClass);

        mDatabase = FirebaseDatabase.getInstance().getReference();

       /* Realm.init(this);
        realm = Realm.getDefaultInstance(); */

        final RadioRealButton button1 = (RadioRealButton) findViewById(R.id.button1);
        final RadioRealButton button2 = (RadioRealButton) findViewById(R.id.button2);
        final RadioRealButton button3 = (RadioRealButton) findViewById(R.id.button3);
        final RadioRealButton button4 = (RadioRealButton) findViewById(R.id.button4);
        final RadioRealButton button5 = (RadioRealButton) findViewById(R.id.button5);
        final RadioRealButton button6 = (RadioRealButton) findViewById(R.id.button6);

        RadioRealButtonGroup group = (RadioRealButtonGroup) findViewById(R.id.group);
        group.setOnClickedButtonPosition(new RadioRealButtonGroup.OnClickedButtonPosition() {
            @Override
            public void onClickedButtonPosition(int position) {
                position_bg = String.valueOf(position);
            }
        });

        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValid()) {
                    storeClass();
                    /*
                    transaction = realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Class_Names class_name = realm.createObject(Class_Names.class);
                            String id = _className.getText().toString() + _subjectName.getText().toString();
                            class_name.setId(id);
                            class_name.setName_class(_className.getText().toString());
                            class_name.setName_subject(_subjectName.getText().toString());
                            class_name.setPosition_bg(position_bg);
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            progressDialog.dismiss();
                            Toast.makeText(Insert_class_Activity.this, "Successfully created", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            progressDialog.dismiss();
                            Toast.makeText(Insert_class_Activity.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    });  */
                } else {
                    Toast.makeText(Insert_class_Activity.this, "Fill all details", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    public void storeClass() {
        final ProgressDialog progressDialog = new ProgressDialog(Insert_class_Activity.this);
        progressDialog.setMessage("Creating class..");
        progressDialog.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();
        String name_class = _className.getText().toString().trim();
        String name_subject = _subjectName.getText().toString().trim();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Class_Names class_names = new Class_Names(id, name_class, name_subject, position_bg);
        mDatabase.child("Class Names").push().setValue(class_names).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Insert_class_Activity.this, "Class Successfully Created", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();
                String key = mDatabase.getKey();
            }
        });
    }

    public boolean isValid() {

        return !_className.getText().toString().isEmpty() && !_subjectName.getText().toString().isEmpty();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}