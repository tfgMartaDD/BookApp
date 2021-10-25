package com.marta.bookapp.Activitys;

import static com.marta.bookapp.BotonesComunes.volverAMenuAdmin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.marta.bookapp.R;

import java.util.HashMap;
import java.util.Map;

public class UsuarioScanActivity extends AppCompatActivity {

    EditText nombreET, apellidoET;
    TextView emailTV;
    Button modificarBTN, volverBTN;
    Button adminBTN;

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

        adminBTN = findViewById(R.id.adminButton);
        adminBTN.setOnClickListener( (View v)  -> {

            String frase = "¿Estás seguro de que desea hacer que "+email+" sea administrador?";
            AlertDialog.Builder alerta = new AlertDialog.Builder(UsuarioScanActivity.this);
            alerta.setMessage(frase).setPositiveButton("ACEPTAR",  (DialogInterface dialog, int id) -> {
                db.collection("users").document(email).update("esAdmin", "true");
                Toast.makeText(this, "Ahora "+email+" también es administrador en la app.", Toast.LENGTH_LONG).show();

            }).setNegativeButton("RECHAZAR",  (DialogInterface dialog, int id) ->   {
                dialog.dismiss();
                Toast.makeText(this, "ACCIÓN CANCELADA", Toast.LENGTH_SHORT).show();
            } );

            AlertDialog alertDialog = alerta.create();
            alertDialog.setTitle("NUEVO ADMINISTRADOR DE LA APP");
            alertDialog.show();

        });


    }
}