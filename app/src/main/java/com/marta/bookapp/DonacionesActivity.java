package com.marta.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DonacionesActivity extends AppCompatActivity {

    Button addBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donaciones);

        addBTN = findViewById(R.id.addButton);
        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent (DonacionesActivity.this, NuevaDonacionActivity.class);
                startActivity(in);
            }
        });
    }
}