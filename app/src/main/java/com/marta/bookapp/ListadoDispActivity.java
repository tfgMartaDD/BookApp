package com.marta.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListadoDispActivity extends AppCompatActivity {

    TextView cursoTV, claseTV, mostrarTV;
    ListView listViewDisponibles;
    List<Libro> listaLibro;
    //ArrayList<Map<String, Object>> listaLibros;
    ArrayAdapter <String> arrayAdapter;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_disp);

        Bundle bundle = getIntent().getExtras();
        String curso = bundle.getString("curso");
        String clase = bundle.getString("clase");

        //listaLibros = new ArrayList<java.util.Map<String, Object>>();

        listViewDisponibles = findViewById(R.id.lv1);

        cursoTV = findViewById(R.id.textViewCurso);
        cursoTV.setText(curso);

        claseTV = findViewById(R.id.textViewClase);
        claseTV.setText(clase);

        mostrarTV = findViewById(R.id.mostrarTextView);

        listaLibro = obtenerLibros(curso, clase);

        ListAdapter adapter = new ListAdapter(this, listaLibro);
        listViewDisponibles.setAdapter(adapter);

        listViewDisponibles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Libro l = listaLibro.get(position);
                mostrarTV.setText(l.asignatura + "\t" + l.clase + "  " + l.curso + "\t" + l.editorial);

            }
        });

        //arrayAdapter = new ArrayAdapter<String>(this,R.layout);
    }

    public List<Libro> obtenerLibros(String curso, String clase){

        List<Libro> lista = new ArrayList<>();

        CollectionReference libros = db.collection("libros");
        libros.whereEqualTo("Curso", curso).whereEqualTo("Clase", clase).whereEqualTo("Estado", "disponible").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //

                                Libro libro = new Libro(document.getString("Id"), document.getString("Asignatura"), document.getString("Clase"),
                                        document.getString("Curso"), document.getString("Donante"),document.getString("Editorial"), document.getString("Estado"));
                                        //,(int)document.get("imagen"));*/

                                lista.add(libro);

                                System.out.println(libro.getAsignatura());
                                //System.out.println(document.getId()+" --> "+document.getData());
                            }
                        } else {
                            Toast.makeText(ListadoDispActivity.this,  "Error getting documents: ", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        return lista;
    }
}