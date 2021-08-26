package com.marta.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DonacionesActivity extends AppCompatActivity {

    Button addBTN;

    ListView listViewDonaciones;
    List<DonacionPeticion> listaDonacion = new ArrayList<>();
    DonAdapter adapter;

    ImageView imagen;
    TextView asignatura, clase, editorial;
    TextView a, c, e, don;


    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donaciones);

        addBTN = findViewById(R.id.addButton);
        addBTN.setOnClickListener( (View v) -> {
            Intent in = new Intent (DonacionesActivity.this, NuevaDonacionActivity.class);
            startActivity(in);
        });

        prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String actualUser = prefs.getString("email","");


        listaDonacion = obtenerMisDonaciones(actualUser);

        listViewDonaciones = findViewById(R.id.lvDonaciones);

        adapter = new DonAdapter(this, listaDonacion);
        listViewDonaciones.setAdapter(adapter);

        asignatura = findViewById(R.id.asignaturatv);
        clase = findViewById(R.id.clasetv);
        editorial = findViewById(R.id.editorialTV);
        imagen = findViewById(R.id.portadaIV);

        a = findViewById(R.id.tvasig);
        c = findViewById(R.id.tvclase);
        e = findViewById(R.id.tveditorial);
        don = findViewById(R.id.donacionSelec);

        listViewDonaciones.setOnItemClickListener( (AdapterView<?> parent, View view, int position, long id) -> {
            DonacionPeticion d = listaDonacion.get(position);
            Libro l = d.getLibro();

            a.setVisibility(View.VISIBLE);
            c.setVisibility(View.VISIBLE);
            e.setVisibility(View.VISIBLE);
            don.setVisibility(View.VISIBLE);

            imagen.setImageResource(l.getImagen());
            asignatura.setText(l.getAsignatura());

            String clasecurso = l.getClase() +" " +l.getCurso();
            clase.setText(clasecurso);

            editorial.setText(l.getEditorial());
            System.out.println(l.getEditorial());
        });

    }

    public List<DonacionPeticion> obtenerMisDonaciones(String user){
        List<DonacionPeticion> lista = new ArrayList<>();


        //CollectionReference libros = db.collection("libros");
        CollectionReference donaciones = db.collection("donaciones");
        donaciones.whereEqualTo("Usuario",user).get().addOnCompleteListener( (@NonNull Task<QuerySnapshot> task) -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    String libroID = document.getString("Libro");

                    CollectionReference libros = db.collection("libros");
                    libros.whereEqualTo(FieldPath.documentId(),libroID).get().addOnCompleteListener( (@NonNull Task<QuerySnapshot> task2) -> {
                        if (task2.isSuccessful()) {
                            for (QueryDocumentSnapshot query : Objects.requireNonNull(task2.getResult())) {
                                Libro libro = new Libro(query.getId(), query.getString("Asignatura"), query.getString("Clase"), query.getString("Curso"),
                                        query.getString("Donante"), query.getString("Editorial"), query.getString("Estado"), (R.drawable.imagen_no_disp));

                                DonacionPeticion donacion = new DonacionPeticion(document.getId(), document.getString("Usuario"),
                                        libro, document.getDate("Fecha"));
                                listaDonacion.add(donacion);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }else {
                Toast.makeText(DonacionesActivity.this,  "Error getting documents: ", Toast.LENGTH_LONG).show();
            }
        });

        return lista;
    }
}