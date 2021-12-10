package com.marta.bookapp.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.marta.bookapp.Modelo.Usuario;
import com.marta.bookapp.R;

import java.util.HashMap;
import java.util.Map;

public class DatosPersonalesActivity extends AppCompatActivity {

    TextView mail;
    EditText nombre, apellido;
    Button modificarBTN, eliminarBTN, modoAdminBTN;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences prefs;
    String actualUser;

    Usuario datosUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_personales);

        mail = findViewById(R.id.correoTextView);
        nombre = findViewById(R.id.nombreEditText);
        apellido = findViewById(R.id.apellidoEditText);

        prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        actualUser = prefs.getString("email","");

        /*FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        if (usuario != null) {
            usuario.sendEmailVerification();
        }*/
        modoAdminBTN = findViewById(R.id.modoAdmin);

        db.collection("users").document(actualUser).get().addOnSuccessListener( (DocumentSnapshot documentSnapshot) -> {
            nombre.setText(documentSnapshot.getString("nombre"));
            apellido.setText(documentSnapshot.getString("apellido"));
            mail.setText(documentSnapshot.getString("email"));

            boolean esAdmin = Boolean.parseBoolean(documentSnapshot.getString("esAdmin"));
            if (esAdmin) {
                modoAdminBTN.setVisibility(View.VISIBLE);
            }
        });

        modificarBTN = findViewById(R.id.modificarbutton);
        modificarBTN.setOnClickListener( (View v) -> {

            /*Map<String, Object> user = new HashMap<>();
            user.put("nombre", nombre.getText().toString());
            user.put("apellido", apellido.getText().toString());
            user.put("email",mail.getText().toString());*/
            String email = mail.getText().toString();
            String nom = nombre.getText().toString();
            String ape = apellido.getText().toString();

            db.collection("users").document(email).update("nombre", nom);

            db.collection("users").document(email).update("apellido", ape).addOnSuccessListener( (Void unused) ->
                    Toast.makeText(DatosPersonalesActivity.this, "Datos modificados con exito", Toast.LENGTH_SHORT).show());

        });

        eliminarBTN = findViewById(R.id.eliminarCuentabutton);
        eliminarBTN.setOnClickListener( (View v) -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(DatosPersonalesActivity.this);
            builder.setTitle("¡CUIDADO! Accion irrevocable.");
            String frase = "¿Estas seguro de que deseas eliminar su cuenta ?";
            builder.setMessage(frase);
            builder.setPositiveButton("SI", (DialogInterface dialog, int which) ->{

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {

                    System.out.println("ACTUAL USER: " + actualUser);

                    //guardamos los datos temporalmente por si no se elimina correctamente el usuario
                    db.collection("users").document(actualUser).get().addOnSuccessListener( (DocumentSnapshot documentSnapshot) -> {
                        System.out.println("ID "+ documentSnapshot.getId() + "kkkk "+ documentSnapshot.getString("email"));
                        //String numd = documentSnapshot.getString("numDonaciones");
                        System.out.println("HHHH: " + documentSnapshot.getLong("numDonaciones"));

                        long numDon =  documentSnapshot.getLong("numDonaciones");
                        System.out.println("HHHH: " + numDon);

                        //String nump = documentSnapshot.getString("numPrestamos");
                        long numPrest = documentSnapshot.getLong("numPrestamos");

                        datosUser = new Usuario(documentSnapshot.getId(), documentSnapshot.getString("nombre"), documentSnapshot.getString("apellido"),
                                documentSnapshot.getString("fotoPerfil"),numDon,numPrest);

                        db.collection("users").document(actualUser).delete().addOnSuccessListener( (Void aVoid) -> {
                            System.out.println("DocumentSnapshot successfully deleted!");
                            user.delete().addOnCompleteListener( (@NonNull Task<Void> task) -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(DatosPersonalesActivity.this, "CUENTA ELIMINADA CORRECTAMENTE", Toast.LENGTH_LONG).show();

                                    SharedPreferences prefs  = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.clear();
                                    editor.apply();

                                    Intent i = new Intent(DatosPersonalesActivity.this, MainActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                }else{
                                    Toast.makeText(DatosPersonalesActivity.this, "No se pudo eliminar su cuenta.", Toast.LENGTH_LONG).show();
                                    //error al eliminar, asi que recuperamos los datos temporales

                                    Map<String, Object> userTemp = new HashMap<>();
                                    userTemp.put("nombre", datosUser.getNombre());
                                    userTemp.put("apellido", datosUser.getApellido());
                                    userTemp.put("email", datosUser.getId());
                                    userTemp.put("esAdmin","false");
                                    userTemp.put("numDonaciones", datosUser.getDonaciones());
                                    userTemp.put("numPrestamos", datosUser.getPrestamos() );
                                    userTemp.put("fotoPerfil", datosUser.getFotoPerfil());

                                    db.collection("users").document(actualUser).set(userTemp);
                                }
                            });

                        }).addOnFailureListener( (@NonNull Exception e) ->
                                System.out.println("Error deleting document") );

                    });


                    }

            }).setNegativeButton("NO",  (DialogInterface dialog, int which) -> {
                dialog.dismiss();
                Toast.makeText(DatosPersonalesActivity.this, "ACCION CANCELADA", Toast.LENGTH_SHORT).show();
            });

            builder.show();

        });

        modoAdminBTN.setOnClickListener((View v) -> {
            Intent in = new Intent(DatosPersonalesActivity.this, AdminMenuActivity.class);
            in.putExtra("email",actualUser);
            startActivity(in);
        });

    }
}