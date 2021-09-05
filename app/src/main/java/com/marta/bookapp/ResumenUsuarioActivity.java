package com.marta.bookapp;

import static com.marta.bookapp.BotonesComunes.volverAMenuAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.marta.bookapp.Modelo.Libro;
import com.marta.bookapp.adapter.ResumenAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ResumenUsuarioActivity extends AppCompatActivity {

    Button menuBTN, adminBTN;
    TextView usuarioTV;
    ListView listViewUsuario;

    List<Libro> listaLibros = new ArrayList<>();
    ResumenAdapter adapter;

    ImageView imagen;
    TextView asig, clase, curso, editorial;
    TextView titulo;

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
        titulo = findViewById(R.id.tituloResumen);

        listViewUsuario.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {

            Libro l = listaLibros.get(position);

            Glide.with(ResumenUsuarioActivity.this)
                    .load(l.getImagen())
                    .into(imagen);

            titulo.setText(l.getTipo());

            asig.setText(l.getAsignatura());
            clase.setText(l.getClase());
            curso.setText(l.getCurso());
            editorial.setText(l.getEditorial());
        });

        menuBTN.setOnClickListener((View v) -> volverAMenuAdmin(ResumenUsuarioActivity.this));

        adminBTN = findViewById(R.id.admin2Button);
        adminBTN.setOnClickListener( (View v)  -> {

            String frase = "¿Estás seguro de que desea hacer que "+email+" sea administrador?";
            AlertDialog.Builder alerta = new AlertDialog.Builder(ResumenUsuarioActivity.this);
            alerta.setMessage(frase).setPositiveButton("ACEPTAR",  (DialogInterface dialog, int id) -> {
                db.collection("users").document(email).update("esAdmin", "true");
                Toast.makeText(this, "Ahora "+email+" también es administrador en la app.", Toast.LENGTH_LONG).show();

            }).setNegativeButton("RECHAZAR",  (DialogInterface dialog, int id) ->   {
                dialog.dismiss();
                Toast.makeText(this, "ACCIÓN CANCELADA", Toast.LENGTH_SHORT).show();
            } );

            AlertDialog alertDialog = alerta.create();
            alertDialog.setTitle("NUEVO ADMINISTRADOR DE LA APP");
            alertDialog.show();

        });
    }

    private List<Libro> obtenerLibros(String mail){

        List<Libro> lista = new ArrayList<>();

        db.collection("libros").whereEqualTo("Donante", mail).get().addOnCompleteListener((@NonNull Task<QuerySnapshot> task) -> {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())){
                    Libro l = new Libro(document.getId(), document.getString("Asignatura"), document.getString("Clase"), document.getString("Curso"), document.getString("Donante"),
                            document.getString("Editorial"), document.getString("Estado"), document.getString("Imagen"), "DONACION" );

                    lista.add(l);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        db.collection("prestamos").whereEqualTo("Usuario", mail).get().addOnCompleteListener((@NonNull Task<QuerySnapshot> task3) -> {
            if(task3.isSuccessful()){
                for(QueryDocumentSnapshot document3 : Objects.requireNonNull(task3.getResult())){
                    String libroid = document3.getString("Libro");
                    System.out.println("Libro----> "+libroid);
                    db.collection("libros").whereEqualTo(FieldPath.documentId(), libroid).get().addOnCompleteListener((@NonNull Task<QuerySnapshot> task2) -> {
                        if (task2.isSuccessful()) {
                            for (QueryDocumentSnapshot document2 : Objects.requireNonNull(task2.getResult())) {

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