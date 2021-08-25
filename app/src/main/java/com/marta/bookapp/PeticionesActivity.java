package com.marta.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PeticionesActivity extends AppCompatActivity {

    Button eliminarBTN, menuBTN;

    ListView listViewPeticiones;
    List<Donacion> listaPeticion = new ArrayList<Donacion>();
    DonAdapter adapter;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peticiones);

        prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String actualUser = prefs.getString("email","");


        listaPeticion = obtenerMisPeticiones(actualUser);

        listViewPeticiones = findViewById(R.id.lvPeticiones);

        adapter = new DonAdapter(this, listaPeticion);
        listViewPeticiones.setAdapter(adapter);

        listViewPeticiones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Donacion d = listaPeticion.get(position);
            }
        });

    }


    public List<Donacion> obtenerMisPeticiones(String user){
        List<Donacion> lista = new ArrayList<Donacion>();


        //CollectionReference libros = db.collection("libros");
        CollectionReference donaciones = db.collection("donaciones");
        donaciones.whereEqualTo("Usuario",user).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String libroID = document.getString("Libro");

                        CollectionReference libros = db.collection("libros");
                        libros.whereEqualTo(FieldPath.documentId(),libroID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                                if (task2.isSuccessful()) {
                                    for (QueryDocumentSnapshot query : task2.getResult()) {
                                        Libro libro = new Libro(query.getString("Id"), query.getString("Asignatura"), query.getString("Clase"),
                                                query.getString("Curso"), query.getString("Donante"), query.getString("Editorial"), query.getString("Estado"));

                                        Donacion donacion = new Donacion(document.getId(), document.getString("Usuario"),
                                                libro, document.getDate("Fecha"));
                                        listaDonacion.add(donacion);
                                        adapter.notifyDataSetChanged();
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
                        adapter.notifyDataSetChanged();
                    }
                    adapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(DonacionesActivity.this,  "Error getting documents: ", Toast.LENGTH_LONG).show();
                }
            }
        });
        return lista;
    }

}