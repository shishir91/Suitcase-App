package com.example.suitcase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.suitcase.R;

public class Home extends AppCompatActivity {
    TextView textview;
    Button manage, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        String name = "";

        SessionManager sessionManager = new SessionManager(getApplicationContext());

        if (!sessionManager.isLoggedIn()){
            startActivity(new Intent(Home.this, Login.class));
        }

        if (sessionManager.isLoggedIn()){
            name = sessionManager.getName();
        }

        manage = findViewById(R.id.manage);
        logout = findViewById(R.id.btnLogout);
        textview = findViewById(R.id.textView7);

        textview.setText("Welcome " + name);

        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, MainActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.endSession();
                startActivity(new Intent(Home.this, Login.class));
            }
        });
    }
}