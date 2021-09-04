package com.marta.bookapp;

import static com.marta.bookapp.BotonesComunes.volverAMenuAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ResumenUsuarioActivity extends AppCompatActivity {

    Button menuBTN;
    TextView usuarioTV;
    ListView listViewUsuario;

    List<Libro> listaLibros = new ArrayList<>();
    ResumenAdapter adapter;

    ImageView imagen;
    TextView asig, clase, curso, editorial;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_usuario);

        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");

        usuarioTV = findViewById(R.id.userEmailTV);
        menuBTN = findViewById(R.id.buttonMenu);

        usuarioTV.setText(email);

        listaLibros = obtenerLibros(email);

        listViewUsuario = findViewById(R.id.usuariosLV);
        adapter = new ResumenAdapter(this, listaLibros);
        listViewUsuario.setAdapter(adapter);

        imagen = findViewById(R.id.imageView4);
        asig = findViewById(R.id.tvresasig);
        clase = findViewById(R.id.tvresclase);
        curso = findViewById(R.id.tvrescurso);
        editorial = findViewById(R.id.tvreseditorial);

        listViewUsuario.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {

            Libro l = listaLibros.get(position);

            Glide.with(ResumenUsuarioActivity.this)
                    .load(l.getImagen())
                    .into(imagen);

            asig.setText(l.getAsignatura());
            clase.setText(l.getClase());
            curso.setText(l.getCurso());
            editorial.setText(l.getEditorial());
        });

        menuBTN.setOnClickListener((View v) -> volverAMenuAdmin(ResumenUsuarioActivity.this));
    }

    private List<Libro> obtenerLibros(String mail){

        List<Libro> lista = new ArrayList<>();

        db.collection("libros").whereEqualTo("Donante", mail).get().addOnCompleteListener((@NonNull Task<QuerySnapshot> task) -> {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot document : task.getResult()){
                    Libro l = new Libro(document.getId(), document.getString("Asignatura"), document.getString("Clase"), document.getString("Curso"), document.getString("Donante"),
                            document.getString("Editorial"), document.getString("Estado"), document.getString("Imagen"), "DONACION" );

                    lista.add(l);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        db.collection("prestamos").whereEqualTo("Usuario", mail).get().addOnCompleteListener((@NonNull Task<QuerySnapshot> task3) -> {
            if(task3.isSuccessful()){
                for(QueryDocumentSnapshot document3 : task3.getResult()){
                    String libroid = document3.getString("Libro");
                    System.out.println("Libro----> "+libroid);
                    db.collection("libros").whereEqualTo(FieldPath.documentId(), libroid).get().addOnCompleteListener((@NonNull Task<QuerySnapshot> task2) -> {
                        if (task2.isSuccessful()) {
                            for (QueryDocumentSnapshot document2 : task2.getResult()) {

                                Libro libro = new Libro(document2.getId(), document2.getString("Asignatura"), document2.getString("Clase"), document2.getString("Curso"), document2.getString("Donante"),
                                        document2.getString("Editorial"), document2.getString("Estado"), document2.getString("Imagen"), "PRESTAMO");

                                lista.add(libro);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        });




        return lista;
    }
}