package com.example.suitcase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class Login extends AppCompatActivity {
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SessionManager sessionManager = new SessionManager(getApplicationContext());

        if (sessionManager.isLoggedIn()){
            startActivity(new Intent(Login.this, Home.class));
        }

        final EditText email = findViewById(R.id.login_email);
        final EditText password = findViewById(R.id.login_password);
        final Button loginBtn = findViewById(R.id.btnLogin);
        final TextView redirectSignup = findViewById(R.id.signupRedirect);
        db = new DBHelper(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailTxt = email.getText().toString();
                final String passwordTxt = password.getText().toString();

                if (emailTxt.isEmpty() || passwordTxt.isEmpty()){
                    Toast.makeText(Login.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                }else {
                    Boolean login = db.loginUser(emailTxt, passwordTxt);
                    if (login == true){
                        User user = db.getUser(emailTxt);
                        sessionManager.createSession(user.getId(), user.getName(), user.getEmail());
                        Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this, Home.class));
                    }else {
                        Toast.makeText(Login.this, "Username or Password error", Toast.LENGTH_SHORT).show();
                    }
//                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if (snapshot.hasChild(emailTxt)){
//                                final String getPassword = snapshot.child(emailTxt).child("password").getValue(String.class);
//                                if (getPassword.equals(passwordTxt)){
//                                    Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                                    startActivity(new Intent(Login.this, Home.class));
//                                    finish();
//                                }else {
//                                    Toast.makeText(Login.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
//                                }
//                            }else {
//                                Toast.makeText(Login.this, "Email doesn't exist", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
                }
            }
        });

        redirectSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Signup.class));
            }
        });
    }
}