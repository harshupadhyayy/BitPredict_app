package com.kekguy.bitcoinprice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    private EditText bitcoins;
    private EditText money;
    private Button saveButton;
    private NavigationView navigationView;
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

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    //DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
                    //drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                if (item.getItemId() == R.id.nav_gallery) {
                    Intent intent = new Intent(SettingsActivity.this, NewsActivity.class);
                    startActivity(intent);

                }
                if (item.getItemId() == R.id.nav_portfolio) {
                    DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_settings);
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });

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