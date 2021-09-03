package com.marta.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ResumenUsuarioActivity extends AppCompatActivity {

    Button menuBTN;
    TextView usuarioTV;
    ListView listViewUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_usuario);

        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");

        usuarioTV = findViewById(R.id.userEmailTV);
        menuBTN = findViewById(R.id.buttonMenu);

        usuarioTV.setText(email);

        listViewUsuario = findViewById(R.id.usuariosLV);
    }
}