package com.example.suitcase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.suitcase.R;

public class Options extends AppCompatActivity {

    EditText name, description, price;
    ImageView delete, edit;
    CheckBox checkBox;
    Button button;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        name = findViewById(R.id.editName);
        description = findViewById(R.id.editDescription);
        price = findViewById(R.id.editPrice);
        delete = findViewById(R.id.iconDelete);
        edit = findViewById(R.id.iconEdit);
        checkBox = findViewById(R.id.editCheckbox);
        button = findViewById(R.id.btnShare);
        db = new DBHelper(this);

        Intent intent = getIntent();

        String id = intent.getStringExtra("id");
        name.setText(intent.getStringExtra("names"));
        description.setText(intent.getStringExtra("descriptions"));
        price.setText(intent.getStringExtra("price"));
        checkBox.setChecked(intent.getStringExtra("purchased").equals("true"));

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean deleteItem = db.deleteitemdata(id);

                    if (deleteItem == true){
                        Toast.makeText(Options.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Options.this, MainActivity.class));
                    }else {
                        Toast.makeText(Options.this, "Error Deleting Item", Toast.LENGTH_SHORT).show();
                    }

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
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
                Boolean editItem = db.updateitemdata(id, nameText, descriptionText, priceText, purchased);

                    if (editItem == true){
                        Toast.makeText(Options.this, "Item Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Options.this, MainActivity.class));
                    }else {
                        Toast.makeText(Options.this, "Error Updating Item", Toast.LENGTH_SHORT).show();
                    }

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Options.this, sendSMS.class);
                intent.putExtra("item", name.getText().toString());
                intent.putExtra("description", description.getText().toString());
                intent.putExtra("price", price.getText().toString());
                if (checkBox.equals("true")){
                    intent.putExtra("purchased", "true");
                }else {
                    intent.putExtra("purchased", "false");
                }
                startActivity(intent);
            }
        });

    }
}