package com.marta.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DatosPersonalesActivity extends AppCompatActivity {

    TextView mail;
    EditText nombre, apellido;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
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



    }
}