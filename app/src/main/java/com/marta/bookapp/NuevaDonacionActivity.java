package com.marta.bookapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NuevaDonacionActivity extends AppCompatActivity {

    Spinner asigSpin;
    Spinner claseSpin;
    Spinner cursoSpin;
    Spinner editorialSpin;
    TextView libroTextView;

    Button donarBTN;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_donacion);

        libroTextView = findViewById(R.id.libroTV);

        asigSpin = findViewById(R.id.asigSpinner);
        claseSpin = findViewById(R.id.claseSpinner);
        cursoSpin = findViewById(R.id.cursoSpinner);
        editorialSpin = findViewById(R.id.editorialSpinner);

        String [] asignaturas = {"MATEMATICAS", "LENGUA", "BIOLOGIA", "SOCIALES", "INGLES"};
        ArrayAdapter<String> asigAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, asignaturas);
        asigSpin.setAdapter(asigAdapter);

        String [] clases = {"PRIMERO","SEGUNDO","TERCERO","CUARTO", "QUINTO", "SEXTO"};
        ArrayAdapter<String> claseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, clases);
        claseSpin.setAdapter(claseAdapter);

        String [] cursos = {"PRIMARIA","ESO", "BACHILLERATO"};
        ArrayAdapter<String> cursoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cursos);
        cursoSpin.setAdapter(cursoAdapter);

        String [] editoriales = {"SM","ANAYA"};
        ArrayAdapter<String> editorialAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, editoriales);
        editorialSpin.setAdapter(editorialAdapter);


        donarBTN = findViewById(R.id.donarButton);
        donarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String asignatura = asigSpin.getSelectedItem().toString();
                String clase = claseSpin.getSelectedItem().toString();
                String curso = cursoSpin.getSelectedItem().toString();
                String editorial = editorialSpin.getSelectedItem().toString();

                prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
                String actualUser = prefs.getString("email","");

                Date date = new Date();

                Map<String, Object> donation = new HashMap<>();
                donation.put("Asignatura", asignatura);
                donation.put("Clase", clase);
                donation.put("Curso", curso);
                donation.put("Editorial", editorial);
                donation.put("Usuario", actualUser);
                donation.put("Fecha",date);

                String frase = "¿Está seguro de que quiere donar el libro de la asignatura "+ asignatura +" del curso "+clase +" " +curso+" de la editorial "+ editorial + "?";


                AlertDialog.Builder alerta = new AlertDialog.Builder(NuevaDonacionActivity.this);
                alerta.setMessage(frase).setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        db.collection("posiblesDonaciones").document().set(donation).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(NuevaDonacionActivity.this, "DONACION RECIBIDA.\nLos administradores tienen que aprobar la donación. Se pondran en contacto con usted en breve.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(NuevaDonacionActivity.this, "DONACION CANCELADA", Toast.LENGTH_SHORT).show();

                    }
                });

                AlertDialog alertDialog = alerta.create();
                alertDialog.setTitle("¿ESTAS SEGURO?");
                alertDialog.show();

            }
        });



    }

   /* public void seleccion(View view){

        String asignatura = asigSpin.getSelectedItem().toString();
        String clase = claseSpin.getSelectedItem().toString();
        String curso = cursoSpin.getSelectedItem().toString();
        String editorial = editorialSpin.getSelectedItem().toString();

        prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String actualUser = prefs.getString("email","");

        Date date = new Date();

        Map<String, Object> donation = new HashMap<>();
        donation.put("Asignatura", asignatura);
        donation.put("Clase", clase);
        donation.put("Curso", curso);
        donation.put("Editorial", editorial);
        donation.put("Usuario", actualUser);
        donation.put("Fecha",date);

        String frase = "La donación del libro de la asignatura"+ asignatura +" del curso "+clase +" " +curso+" de la editorial "+ editorial
                +" está pendiente de aprobar por los administradores. En breve se pondran en contacto con usted.";

        db.collection("posiblesDonaciones").document().set(donation).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(NuevaDonacionActivity.this, frase, Toast.LENGTH_SHORT).show();
            }
        });



    }*/
}