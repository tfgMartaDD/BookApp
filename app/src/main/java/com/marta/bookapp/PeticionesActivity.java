package com.marta.bookapp;

import static com.marta.bookapp.BotonesComunes.volverAMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PeticionesActivity extends AppCompatActivity {

    Button eliminarBTN, menuBTN;
    TextView mostrarTV;

    ListView listViewPeticiones;
    List<DonacionPeticion> listaPeticion = new ArrayList<>();
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

        mostrarTV = findViewById(R.id.mostrarPeticion);

        listViewPeticiones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DonacionPeticion d = listaPeticion.get(position);
                Libro l = d.getLibro();
                String mostrar = l.getAsignatura() + "\t\t" + l.getClase() + "  " + l.getCurso() + "\t\t" + l.getEditorial();
                mostrarTV.setText(mostrar);

            }
        });

        menuBTN = findViewById(R.id.menuPeticion);
        menuBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverAMenu(PeticionesActivity.this);
            }
        });

    }


    public List<DonacionPeticion> obtenerMisPeticiones(String user){
        List<DonacionPeticion> lista = new ArrayList<>();
        System.out.println("obtener");

        db.collection("peticiones").whereEqualTo("Usuario",user).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        db.collection("libros").whereEqualTo(FieldPath.documentId(),document.getString("Libro")).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document2 : task.getResult()) {
                                        Libro libro = new Libro(document2.getId(), document2.getString("Asignatura"), document2.getString("Clase"),
                                                document2.getString("Curso"), document2.getString("Donante"),document2.getString("Editorial"), document2.getString("Estado"));
                                        System.out.println("asig:"+libro.getAsignatura());
                                        DonacionPeticion donacion = new DonacionPeticion(document.getId(), document2.getString("Usuario"), libro, document.getDate("Fecha"));
                                        listaPeticion.add(donacion);
                                        adapter.notifyDataSetChanged();

                                    }
                                }
                            }
                        });

                    }
                }
            }
        });

        return lista;
    }

}