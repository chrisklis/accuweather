package com.aarole.accuweather_api_test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = ((EditText)findViewById(R.id.edittext)).getText().toString();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.putExtra("city", city);
                startActivity(intent);
            }
        });
    }
}
