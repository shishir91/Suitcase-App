package com.example.suitcase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Signup extends AppCompatActivity {
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        SessionManager sessionManager = new SessionManager(getApplicationContext());

        if (sessionManager.isLoggedIn()){
            startActivity(new Intent(Signup.this, Home.class));
        }

        final EditText name = findViewById(R.id.signup_name);
        final EditText email = findViewById(R.id.signup_email);
        final EditText password = findViewById(R.id.signup_password);
        final EditText con_password = findViewById(R.id.signup_confirm);
        final Button signupBtn = findViewById(R.id.btnSignup);
        final TextView redirectLogin = findViewById(R.id.loginRedirect);
        db = new DBHelper(this);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nameTxt = name.getText().toString();
                final String emailTxt = email.getText().toString();
                final String passwordTxt = password.getText().toString();
                final String c_passwordTxt = con_password.getText().toString();

                if (nameTxt.isEmpty() || emailTxt.isEmpty() || passwordTxt.isEmpty() || c_passwordTxt.isEmpty()){
                    Toast.makeText(Signup.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (db.isEmailExists(emailTxt)) {
                    Toast.makeText(Signup.this, "Email already exists", Toast.LENGTH_SHORT).show();
                }else if (!passwordTxt.equals(c_passwordTxt)) {
                    Toast.makeText(Signup.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                }else {
                    Boolean saveUser = db.signupUser(nameTxt, emailTxt, passwordTxt);
                    if (saveUser == true){
                        Toast.makeText(Signup.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Signup.this, Login.class));
                    }else {
                        Toast.makeText(Signup.this, "Signup Error", Toast.LENGTH_SHORT).show();
                    }
//                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if (snapshot.hasChild(emailTxt)){
//                                Toast.makeText(Signup.this, "Email is already used.", Toast.LENGTH_SHORT).show();
//                            }else {
//                                databaseReference.child("users").child(emailTxt).child("name").setValue(nameTxt);
//                                databaseReference.child("users").child(emailTxt).child("password").setValue(passwordTxt);
//                                Toast.makeText(Signup.this, "Signup Successful", Toast.LENGTH_SHORT).show();
//                                finish();
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

        redirectLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Signup.this, Login.class));
            }
        });
    }
}