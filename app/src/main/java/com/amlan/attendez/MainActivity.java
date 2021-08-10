package com.amlan.attendez;

import androidx.annotation.ColorLong;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText etEmail, etPword;
    FloatingActionButton fabLogin;
    FirebaseAuth mAuth;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources()
                    .getColor(R.color.blue_900));
            window.setNavigationBarColor(getResources().getColor(R.color.light_blue_200));
        }
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.etEmail);
        etPword = findViewById(R.id.etPword);
        fabLogin = findViewById(R.id.fabLogin);
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            Intent i = new Intent(MainActivity.this, ClassActivity.class);
            startActivity(i);
            finish();
        }

        fabLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPword.getText().toString().trim();

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

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(MainActivity.this, ClassActivity.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    public void OpenSignupPage(View view) {
        startActivity(new Intent(MainActivity.this, SignupActivity.class));
    }
}