package com.marta.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NuevaDonacionActivity extends AppCompatActivity {

    Spinner asigSpin;
    Spinner claseSpin;
    Spinner cursoSpin;
    Spinner editorialSpin;
    TextView libroTextView;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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




    }

    public void seleccion(View view){

        String asignatura = asigSpin.getSelectedItem().toString();

        String clase = claseSpin.getSelectedItem().toString();

        String curso = cursoSpin.getSelectedItem().toString();

        String editorial = editorialSpin.getSelectedItem().toString();

        libroTextView.setText(asignatura + "\t" + clase + "\t" + curso +"\t" + editorial);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        System.out.println("----------"+user);

        Map<String, Object> donation = new HashMap<>();
        donation.put("Asignatura", asignatura);
        donation.put("Clase", clase);
        donation.put("Curso", curso);
        donation.put("Editorial", editorial);

        //añadir usuario(hay que cogerlo) y fecha

        db.collection("donaciones").document().set(donation);

    }
}