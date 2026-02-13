package com.example.newsapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.newsapplication.LoginManager;
import com.example.newsapplication.R;

public class SettingsActivity extends AppCompatActivity {

    private EditText refreshRate;
    private Switch dailyHealthCheck, shareStatus, passwordLock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Settings");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences preferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        refreshRate = findViewById(R.id.txtRefreshRate);
        dailyHealthCheck = findViewById(R.id.swtHealthCheck);
        shareStatus = findViewById(R.id.swtShareStatus);
        passwordLock = findViewById(R.id.swtPasswordLock);

        refreshRate.setText(preferences.getString("refreshRate", ""));
        dailyHealthCheck.setChecked(preferences.getBoolean("dailyHealthCheck", false));
        shareStatus.setChecked(preferences.getBoolean("shareStatus", false));
        passwordLock.setChecked(preferences.getBoolean("passwordLock", false));

        /*
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
               "settings", Context.MODE_PRIVATE);
        */

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {

        // Save the data to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Store the data
        editor.putString("refreshRate", refreshRate.getText().toString());
        editor.putBoolean("dailyHealthCheck", dailyHealthCheck.isChecked());
        editor.putBoolean("shareStatus", shareStatus.isChecked());
        editor.putBoolean("passwordLock", passwordLock.isChecked());

        //editor.putInt("age", Integer.parseInt(age.getText().toString()));
        editor.commit();

        //System.out.println("Saving refresh rate: " + refreshRate.getText().toString());
        //System.out.println("Saving Daily Health Check: " + dailyHealthCheck.getText().toString());
        //System.out.println("Saving Daily Health Check: " + dailyHealthCheck.isChecked());

        super.onDestroy();

    }

    private void logout() {
        LoginManager loginManager = new LoginManager(this);
        loginManager.logoutUser();

        Intent intent = new Intent(SettingsActivity.this, StartUpActivity.class);
        startActivity(intent);
    }
}