package com.marta.bookapp;

import static com.marta.bookapp.BotonesComunes.volverAMenuAdmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UsuarioScanActivity extends AppCompatActivity {

    EditText nombreET, apellidoET;
    TextView emailTV;
    Button modificarBTN, eliminarBTN, volverBTN;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_scan);

        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");

        volverBTN = findViewById(R.id.buttonVolver);
        volverBTN.setOnClickListener( (View v) -> volverAMenuAdmin(UsuarioScanActivity.this) );

        nombreET = findViewById(R.id.nombreET);
        apellidoET = findViewById(R.id.apellidoET);
        emailTV = findViewById(R.id.correoTV);
        modificarBTN = findViewById(R.id.buttonmodificar);
        eliminarBTN  = findViewById(R.id.buttoneliminar);

        db.collection("users").document(email).get().addOnSuccessListener((DocumentSnapshot documentSnapshot) -> {
            nombreET.setText(documentSnapshot.getString("nombre"));
            apellidoET.setText(documentSnapshot.getString("apellido"));
            emailTV.setText(documentSnapshot.getString("email"));

        });

        modificarBTN.setOnClickListener( (View v) -> {

            Map<String, Object> user = new HashMap<>();
            user.put("nombre", nombreET.getText().toString());
            user.put("apellido", apellidoET.getText().toString());
            user.put("email", email);

            db.collection("users").document(email).set(user).addOnSuccessListener( (Void unused) ->
                    Toast.makeText(UsuarioScanActivity.this, "Datos modificados con exito", Toast.LENGTH_SHORT).show());

        });

    }
}