package com.marta.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
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

public class DatosPersonalesActivity extends AppCompatActivity {

    TextView mail;
    EditText nombre, apellido;
    Button modificarBTN;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences prefs;
    String actualUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_personales);

        mail = findViewById(R.id.correoTextView);
        nombre = findViewById(R.id.nombreEditText);
        apellido = findViewById(R.id.apellidoEditText);
        prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        actualUser = prefs.getString("email","");

        db.collection("users").document(actualUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                nombre.setText(documentSnapshot.getString("nombre"));
                apellido.setText(documentSnapshot.getString("apellido"));
                mail.setText(documentSnapshot.getString("email"));
            }
        });

        modificarBTN = findViewById(R.id.modificarbutton);
        modificarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> user = new HashMap<>();
                user.put("nombre", nombre.getText().toString());
                user.put("apellido", apellido.getText().toString());
                user.put("email",mail.getText().toString());

                db.collection("users").document(mail.getText().toString()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(DatosPersonalesActivity.this, "Datos modificados con exito", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}