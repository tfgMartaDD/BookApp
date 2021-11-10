package com.marta.bookapp.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.marta.bookapp.Modelo.Libro;
import com.marta.bookapp.R;
import com.marta.bookapp.Adapter.BusquedaAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class BusquedaActivity extends AppCompatActivity {

    TextView asignatura, clase, curso, editorial;

    ListView listViewBusqueda;
    List<Libro> listaLibros = new ArrayList<>();
    BusquedaAdapter adapter;
    String asig, cl, cu, edit;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");


        asignatura = findViewById(R.id.busqAsig);
        clase = findViewById(R.id.busqClase);
        curso = findViewById(R.id.busqCurso);
        editorial = findViewById(R.id.busqEdit);



        db.collection("libros").document(id).get().addOnSuccessListener( (DocumentSnapshot documentSnapshot) -> {
            Libro libro = new Libro(documentSnapshot.getId(), documentSnapshot.getString("Asignatura"), documentSnapshot.getString("Clase"), documentSnapshot.getString("Curso"),
                    documentSnapshot.getString("Donante"), documentSnapshot.getString("Editorial"), documentSnapshot.getString("Estado"), documentSnapshot.getString("Imagen"), documentSnapshot.getString("Tipo") );
            asig = documentSnapshot.getString("Asignatura");
            asignatura.setText(asig);

            cl = documentSnapshot.getString("Clase");
            clase.setText(cl);

            cu = documentSnapshot.getString("Curso");
            curso.setText(cu);

            edit = documentSnapshot.getString("Editorial");
            editorial.setText(edit);

            listaLibros = obtenerLibros(asig, cl, cu, edit);
            listViewBusqueda = findViewById(R.id.listViewBusqueda);
            adapter = new BusquedaAdapter(this, listaLibros);
            listViewBusqueda.setAdapter(adapter);

        });


    }

    private List<Libro> obtenerLibros(String asig, String cl, String cu, String edit) {

        List<Libro> lista = new ArrayList<>();

        db.collection("libros").whereEqualTo("Asignatura", asig).whereEqualTo("Clase",cl).whereEqualTo("Curso",cu)
                .whereEqualTo("Editorial", edit).get().addOnCompleteListener( (@NonNull Task<QuerySnapshot> task) -> {

            if(task.isSuccessful()){
                for(QueryDocumentSnapshot query : Objects.requireNonNull(task.getResult())){

                    String estado = query.getString("Estado");
                    String idL = query.getId();
                    if(estado != null){
                        if(estado.equalsIgnoreCase("disponible")){

                            Libro l = new Libro(idL, query.getString("Asignatura"), query.getString("Clase"), query.getString("Curso"), query.getString("Donante"),
                                    query.getString("Editorial"), estado, query.getString("Imagen"), "donacion", query.getDate("Fecha") );

                            lista.add(l);
                            adapter.notifyDataSetChanged();

                        }else if(estado.equalsIgnoreCase("reservado")){

                            db.collection("peticiones").whereEqualTo("Libro", idL).get().addOnCompleteListener((@NonNull Task<QuerySnapshot> task2) -> {
                                if (task2.isSuccessful()) {
                                    for (QueryDocumentSnapshot query2 : Objects.requireNonNull(task2.getResult())) {

                                        Libro l = new Libro(idL, query.getString("Asignatura"), query.getString("Clase"), query.getString("Curso"), query2.getString("Usuario"),
                                                query.getString("Editorial"), estado, query.getString("Imagen"), "peticino", query.getDate("Fecha") );
                                        lista.add(l);
                                        adapter.notifyDataSetChanged();

                                    }
                                }
                            });


                        }else if(estado.equalsIgnoreCase("prestado")){

                            db.collection("prestamos").whereEqualTo("Libro", idL).get().addOnCompleteListener((@NonNull Task<QuerySnapshot> task3) -> {
                                if (task3.isSuccessful()) {
                                    for (QueryDocumentSnapshot query3 : Objects.requireNonNull(task3.getResult())) {

                                        Libro l = new Libro(idL, query.getString("Asignatura"), query.getString("Clase"), query.getString("Curso"), query3.getString("Usuario"),
                                                query.getString("Editorial"), estado, query.getString("Imagen"), "prestamo", query.getDate("Fecha") );
                                        lista.add(l);
                                        adapter.notifyDataSetChanged();

                                    }
                                }
                            });
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        return lista;
    }
}