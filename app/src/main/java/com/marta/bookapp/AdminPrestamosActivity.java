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
    RadioButton rbu, rbc;

    Button ordenarBTN;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_prestamos);

        listViewAdminPrestamos = findViewById(R.id.lvPrestamosAdmin);

        rbu = findViewById(R.id.rbUsuario);
        rbc = findViewById(R.id.rbClase);

        spinner1 = findViewById(R.id.spinner1);


    }

    public void comprobarRadioButton(View view){

        if(rbu.isChecked()){
            System.out.println("user");
            spinner1.setVisibility(View.INVISIBLE);
            listaPrestamos = obtenerPrestamosUsuarios();

        }else if(rbc.isChecked()){
            System.out.println("spinner");
            spinner1.setVisibility(View.VISIBLE);

            String [] opciones = {"PRIMERO PRIMARIA", "SEGUNDO PRIMARIA", "TERCERO PRIMARIA", "CUARTO PRIMARIA", "QUINTO PRIMARIA", "SEXTO PRIMARIA",
                    "PRIMERO ESO", "SEGUNDO ESO", "TERCERO ESO", "CUARTO ESO", "PRIMERO BACHILLERATO", "SEGUNDO BACHILLERATO"};

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
            spinner1.setAdapter(arrayAdapter);

            String clasecurso = spinner1.getSelectedItem().toString();

            listaPrestamos = obtenerPrestamosClases(clasecurso);
        }

        adapter = new PrestAdminAdapter(this, listaPrestamos);
        listViewAdminPrestamos.setAdapter(adapter);

    }


    public List<Prestamo> obtenerPrestamosUsuarios(){
        List<Prestamo> listaUsuarios = new ArrayList<>();

        return listaUsuarios;
    }

    public List<Prestamo> obtenerPrestamosClases(String cursoClase){
        List<Prestamo> listaClases = new ArrayList<>();

        String [] partes = cursoClase.split(" ");
        String clase = partes[0];
        String curso = partes[1];

        System.out.println(clase);
        System.out.println(curso);

        db.collection("prestamos").whereEqualTo("Clase", clase).whereEqualTo("Curso", curso).get().addOnCompleteListener( (@NonNull Task<QuerySnapshot> task) -> {
            if(task.isSuccessful()){
                System.out.println("prestamo");
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    System.out.println("Libro"+document.getString("Libro") );
                    db.collection("libros").whereEqualTo(FieldPath.documentId(),document.getString("Libro")).get().addOnCompleteListener( (@NonNull Task<QuerySnapshot> task2) -> {
                        if (task2.isSuccessful()) {
                            System.out.println("libro");
                            for (QueryDocumentSnapshot document2 : Objects.requireNonNull(task2.getResult())) {
                                Libro libro = new Libro(document2.getId(), document2.getString("Asignatura"), document2.getString("Clase"), document2.getString("Curso"),
                                        document2.getString("Donante"),document2.getString("Editorial"), document2.getString("Estado"),(R.drawable.imagen_no_disp));
                                Prestamo p = new Prestamo(document.getId(), libro, document.getString("Usuario"),
                                        document.getDate("FechaPrestamo"), document.getDate("FechaDevolucion"));

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