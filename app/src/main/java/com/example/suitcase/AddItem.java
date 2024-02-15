package com.example.suitcase;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddItem extends AppCompatActivity {

    EditText name, description, price;
    Button addItem;
    DBHelper db;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        Integer userId = sessionManager.getID();

        name = findViewById(R.id.itemName);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);
        addItem = findViewById(R.id.addItemBtn);
        checkBox = findViewById(R.id.addCheckbox);
        db = new DBHelper(this);


        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameText = name.getText().toString();
                String descriptionText = description.getText().toString();
                String priceText = price.getText().toString();
                int purchased;

                if (checkBox.isChecked()) {
                    purchased = 1;
                } else {
                    purchased = 0;
                }
                if (TextUtils.isEmpty(nameText)){
                    Toast.makeText(AddItem.this, "Add Item Name", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                        Boolean saveItem = db.saveitemdata(nameText, descriptionText, priceText, purchased, userId);

                        if (saveItem == true){
                            Toast.makeText(AddItem.this, "Item Saved", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddItem.this, MainActivity.class));
                        }else {
                            Toast.makeText(AddItem.this, "Error Saving Item", Toast.LENGTH_SHORT).show();
                        }
                }
            }
        });

    }
}