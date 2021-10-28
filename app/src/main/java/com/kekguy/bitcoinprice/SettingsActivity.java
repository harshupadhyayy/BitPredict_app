package com.kekguy.bitcoinprice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    private EditText bitcoins;
    private EditText money;
    private Button saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_activity_settings);

        bitcoins = findViewById(R.id.bitcoins);
        money = findViewById(R.id.money);
        saveButton = findViewById(R.id.saveButton);

        DBHandler dbHandler = new DBHandler(SettingsActivity.this);
        ArrayList<String> res = dbHandler.readBitcoinData();
        bitcoins.setText(res.get(0));
        money.setText(res.get(1));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = bitcoins.getText().toString();
                String password = money.getText().toString();

                if (userName.isEmpty() && password.isEmpty()) {
                    Toast.makeText(SettingsActivity.this, "Please enter data correctly", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(dbHandler.addNewBitcoinData(userName, password) <= 0) {
                    Toast.makeText(SettingsActivity.this, "Failed to add data.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SettingsActivity.this, "Successfully added data.", Toast.LENGTH_SHORT).show();
                }

                bitcoins.setText(userName);
                money.setText(password);
            }
        });
    }
}