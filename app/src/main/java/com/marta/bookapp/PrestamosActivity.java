package com.marta.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class PrestamosActivity extends AppCompatActivity {

    ListView prestamos;

    TextView seleccion;
    LinearLayout linearLayout;

    TextView asignatura, clase, fecha, fechaDev;
    ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prestamos);
    }
}