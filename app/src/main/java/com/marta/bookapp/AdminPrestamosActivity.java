package com.marta.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdminPrestamosActivity extends AppCompatActivity {

    PrestAdminAdapter adapter;
    ListView listViewAdminPrestamos;
    List<Prestamo> listaPrestamos = new ArrayList<>();

    Spinner spinner1;
    RadioButton rbt, rbc;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_prestamos);

        listViewAdminPrestamos = findViewById(R.id.lvPrestamosAdmin);

        rbt = findViewById(R.id.rbTodos);
        rbc = findViewById(R.id.rbClase);

        spinner1 = findViewById(R.id.spinner1);

    }

    public void comprobarRadioButton(View view){

        if(rbt.isChecked()){

            listViewAdminPrestamos.setVisibility(View.INVISIBLE);
            spinner1.setVisibility(View.INVISIBLE);
            listaPrestamos = obtenerPrestamos();
            listViewAdminPrestamos.setVisibility(View.VISIBLE);

        }else if(rbc.isChecked()){

            spinner1.setVisibility(View.VISIBLE);
            listViewAdminPrestamos.setVisibility(View.INVISIBLE);

            String [] opciones = {"PRIMERO PRIMARIA", "SEGUNDO PRIMARIA", "TERCERO PRIMARIA", "CUARTO PRIMARIA", "QUINTO PRIMARIA", "SEXTO PRIMARIA",
                    "PRIMERO ESO", "SEGUNDO ESO", "TERCERO ESO", "CUARTO ESO", "PRIMERO BACHILLERATO", "SEGUNDO BACHILLERATO"};

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
            spinner1.setAdapter(arrayAdapter);

            String clasecurso = spinner1.getSelectedItem().toString();

            listaPrestamos = obtenerPrestamosClases(clasecurso);
            listViewAdminPrestamos.setVisibility(View.VISIBLE);
        }

        adapter = new PrestAdminAdapter(this, listaPrestamos);
        listViewAdminPrestamos.setAdapter(adapter);

    }


    public List<Prestamo> obtenerPrestamos(){
        List<Prestamo> listaTodos = new ArrayList<>();

        db.collection("libros").whereEqualTo("Estado", "prestado").get().addOnCompleteListener( (@NonNull Task<QuerySnapshot> task) -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    System.out.println("Libro" + document.getString("Libro"));
                    db.collection("prestamos").whereEqualTo("Libro", document.getId()).get().addOnCompleteListener((@NonNull Task<QuerySnapshot> task2) -> {
                        if (task2.isSuccessful()) {
                            for (QueryDocumentSnapshot document2 : Objects.requireNonNull(task2.getResult())) {
                                Libro libro = new Libro(document.getId(), document.getString("Asignatura"), document.getString("Clase"), document.getString("Curso"),
                                        document.getString("Donante"), document.getString("Editorial"), document.getString("Estado"), (R.drawable.imagen_no_disp));
                                Prestamo p = new Prestamo(document2.getId(), libro, document2.getString("Usuario"),
                                        document2.getDate("FechaPrestamo"), document2.getDate("FechaDevolucion"));

                                listaTodos.add(p);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        });

        return listaTodos;
    }

    public List<Prestamo> obtenerPrestamosClases(String cursoClase){
        List<Prestamo> listaClases = new ArrayList<>();

        String [] partes = cursoClase.split(" ");
        String clase = partes[0];
        String curso = partes[1];


        db.collection("libros").whereEqualTo("Estado", "prestado").whereEqualTo("Clase", clase).whereEqualTo("Curso", curso).get().addOnCompleteListener( (@NonNull Task<QuerySnapshot> task) -> {
            if(task.isSuccessful()){
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    System.out.println("Libro"+document.getString("Libro") );
                    db.collection("prestamos").whereEqualTo("Libro",document.getId()).get().addOnCompleteListener( (@NonNull Task<QuerySnapshot> task2) -> {
                        if (task2.isSuccessful()) {
                            for (QueryDocumentSnapshot document2 : Objects.requireNonNull(task2.getResult())) {
                                Libro libro = new Libro(document.getId(), document.getString("Asignatura"), document.getString("Clase"), document.getString("Curso"),
                                        document.getString("Donante"),document.getString("Editorial"), document.getString("Estado"),(R.drawable.imagen_no_disp));
                                Prestamo p = new Prestamo(document2.getId(), libro, document2.getString("Usuario"),
                                        document2.getDate("FechaPrestamo"), document2.getDate("FechaDevolucion"));

                                listaClases.add(p);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        });

        return listaClases;
    }
}