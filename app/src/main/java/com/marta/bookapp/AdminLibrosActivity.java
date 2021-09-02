package com.marta.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
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
    int i;
    Boolean flag = false;
    String idPendiente;
    String idPet;

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

        anadirBTN = findViewById(R.id.anadirLibroButton);
        anadirBTN.setOnClickListener( (View v) -> {
            Intent in = new Intent (AdminLibrosActivity.this, LibroAddActivity.class);
            startActivity(in);
        });

        listViewListaLibros.setOnItemClickListener( (AdapterView<?> parent, View view, int position, long id) -> {
            i = position;
            flag = true;
        });

        eliminarBTN = findViewById(R.id.eliminarLibroButton);
        eliminarBTN.setOnClickListener( (View v) ->{
            if(flag){
                Libro l = listaLibros.get(i);
                String estado = l.getEstado();

                if(estado.equalsIgnoreCase("prestado")){
                    Toast.makeText(AdminLibrosActivity.this, "No puede eliminar un libro de la aplicación que actualmente está prestado.", Toast.LENGTH_LONG).show();

                }else if (estado.equalsIgnoreCase("reservado")){
                    String frase = "El libro que quieres borrar está reservado. \n ¿Está seguro de que desea eliminarlo de todas formas? ";

                    AlertDialog.Builder alerta = new AlertDialog.Builder(AdminLibrosActivity.this);
                    alerta.setMessage(frase).setPositiveButton("SI",  (DialogInterface dialog, int id) -> {

                        db.collection("peticiones").whereEqualTo("Libro", l.getId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot query) {
                                for (QueryDocumentSnapshot snap : query) {
                                    idPendiente = snap.getString("idPendiente");
                                    idPet = snap.getId();
                                }
                            }
                        });
                        db.collection("peticiones").document(idPet).delete();
                        db.collection("pendientes").document(idPendiente).delete();

                        db.collection("libros").document(l.getId()).delete().addOnSuccessListener( (Void unused) -> {
                            Toast.makeText(AdminLibrosActivity.this, "LIBRO ELIMINADO.", Toast.LENGTH_SHORT).show();
                            listaLibros.remove(i);
                            adapter.notifyDataSetChanged();
                        });

                    }).setNegativeButton("NO",  (DialogInterface dialog, int id) ->  Toast.makeText(AdminLibrosActivity.this, "ACCION CANCELADA", Toast.LENGTH_SHORT).show());

                    AlertDialog alertDialog = alerta.create();
                    alertDialog.setTitle("¿ESTAS SEGURO?");
                    alertDialog.show();


                }else if (estado.equalsIgnoreCase("disponible")){

                    String frase = "¿Estás seguro de que desea eliminar el libro "+ l.getAsignatura() +" del curso "+ l.getClase()
                            +" " + l.getCurso() +" ? ";

                    AlertDialog.Builder alerta = new AlertDialog.Builder(AdminLibrosActivity.this);
                    alerta.setMessage(frase).setPositiveButton("SI",  (DialogInterface dialog, int id) -> {
                        db.collection("libros").document(l.getId()).delete().addOnSuccessListener( (Void unused) -> {
                            Toast.makeText(AdminLibrosActivity.this, "LIBRO ELIMINADO.", Toast.LENGTH_SHORT).show();
                            listaLibros.remove(i);
                            adapter.notifyDataSetChanged();
                        });

                    }).setNegativeButton("NO",  (DialogInterface dialog, int id) ->  Toast.makeText(AdminLibrosActivity.this, "ACCION CANCELADA", Toast.LENGTH_SHORT).show());

                    AlertDialog alertDialog = alerta.create();
                    alertDialog.setTitle("¿ESTAS SEGURO?");
                    alertDialog.show();
                }

            }


        });

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