package com.marta.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class BusquedaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
    }
}