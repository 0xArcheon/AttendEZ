package com.amlan.attendez;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amlan.attendez.Firebase.UserHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPword;
    private RadioGroup RGroup;
    private RadioButton radioButton;
    private FloatingActionButton fabProceed;
    private TextView tvSignin;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_signup);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPword = findViewById(R.id.etPword);
        RGroup = findViewById(R.id.RGroup);
        fabProceed = findViewById(R.id.fabProceed);
        tvSignin = findViewById(R.id.tvSignin);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            Intent i = new Intent(SignupActivity.this, ClassActivity.class);
            startActivity(i);
            finish();
        }

        tvSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fabProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = RGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                String name = etName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPword.getText().toString().trim();
                String userType = radioButton.getText().toString().trim();

                if (name.isEmpty()) {
                    etName.setError("Please enter your Name");
                    etName.requestFocus();
                    return;
                }
                if (email.isEmpty()) {
                    etEmail.setError("Please enter an email");
                    etEmail.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    etPword.setError("Please set a passsword");
                    etPword.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.setError("email address not valid");
                    etEmail.requestFocus();
                    return;
                }
                if (password.length() < 6) {
                    etPword.setError("Provide atleast 6 characters");
                    return;
                }
                if (userType.matches("")) {
                    Toast.makeText(SignupActivity.this, "Select user type", Toast.LENGTH_SHORT).show();
                }

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            storeUserData();
                        } else {
                            Toast.makeText(SignupActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    private void storeUserData() {
        int radioId = RGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPword.getText().toString().trim();
        String userType = radioButton.getText().toString().trim();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        UserHelper userHelper = new UserHelper(name, email, password,userType);
        mDatabase.push().setValue(userHelper);
    }

    public void checkButton(View v) {
        int radioId = RGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
    }
}