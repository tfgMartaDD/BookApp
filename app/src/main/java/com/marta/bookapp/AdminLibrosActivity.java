package com.marta.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdminLibrosActivity extends AppCompatActivity {

    ListView listViewListaLibros;
    List<Libro> listaLibros = new ArrayList<>();
    ListaLibrosAdapter adapter;

    Button anadirBTN, eliminarBTN;

    Spinner spinner;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_libros);

        spinner = findViewById(R.id.spinnerLL);

        String [] opciones = {"reservado", "prestado", "disponible", "todos"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        spinner.setAdapter(arrayAdapter);

        listViewListaLibros = findViewById(R.id.lvlistaLibros);

    }

    public void listar(View view){

        String eleccion = spinner.getSelectedItem().toString();

        listaLibros = obtenerListaLibros(eleccion);

        adapter = new ListaLibrosAdapter(this, listaLibros);
        listViewListaLibros.setAdapter(adapter);

    }


    public List<Libro> obtenerListaLibros(String eleccion){

        List<Libro> lista = new ArrayList<>();

        if(eleccion.equalsIgnoreCase("todos")){
            db.collection("libros").get().addOnCompleteListener((@NonNull Task<QuerySnapshot> task) ->{
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        Libro libro = new Libro(document.getString("Asignatura"), document.getString("Clase"), document.getString("Curso"),
                                document.getString("Donante"), document.getString("Editorial"), document.getString("Estado"), document.getString("Imagen"));

                        lista.add(libro);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }else{
            db.collection("libros").whereEqualTo("Estado",eleccion).get().addOnCompleteListener((@NonNull Task<QuerySnapshot> task) ->{
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        Libro libro = new Libro(document.getString("Asignatura"), document.getString("Clase"), document.getString("Curso"),
                                document.getString("Donante"), document.getString("Editorial"), document.getString("Estado"), document.getString("Imagen"));

                        lista.add(libro);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }



        return lista;
    }
}