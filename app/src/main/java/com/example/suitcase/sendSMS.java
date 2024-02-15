package com.example.suitcase;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.suitcase.R;

public class sendSMS extends AppCompatActivity {

    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;
    EditText phone, message;
    Button send, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);

        phone = findViewById(R.id.smsPhone);
        message = findViewById(R.id.smsItem);
        send = findViewById(R.id.sendButton);
        cancel = findViewById(R.id.cancelButton);

        Intent intent = getIntent();

        String itemName = intent.getStringExtra("item");
        String itemDescription = intent.getStringExtra("description");
        String itemPrice = "Rs. " + intent.getStringExtra("price");
        String isPurchased = "";
        if (intent.getStringExtra("purchased").equals("true")){
            isPurchased = "Purchased";
        }else {
            isPurchased = "Not Purchased";
        }

        message.setText(itemName + "\n" + itemDescription + "\n" + itemPrice + "\n" + isPurchased);

        send.setEnabled(false);
        if (checkPermission(Manifest.permission.SEND_SMS)){
            send.setEnabled(true);
        }else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSend(view);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(sendSMS.this, Options.class));
            }
        });
    }

    public void onSend(View v){
        String phoneNumber = phone.getText().toString();
        String itemName = message.getText().toString();

        if(phoneNumber == null || phoneNumber.length() == 0){
            Toast.makeText(this, "Enter Number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkPermission(Manifest.permission.SEND_SMS)){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, itemName, null, null);
            Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show();
            
        }else {
            Toast.makeText(this, "Permission Declined", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission(this, permission);
        return (check == PackageManager.PERMISSION_GRANTED);
    }
}