package com.marta.bookapp.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.marta.bookapp.Modelo.Libro;
import com.marta.bookapp.Modelo.Prestamo;
import com.marta.bookapp.Modelo.Usuario;
import com.marta.bookapp.R;
import com.marta.bookapp.Adapter.PrestAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PrestamosActivity extends AppCompatActivity {

    ListView listViewPrestamos;

    TextView seleccion;
    LinearLayout linearLayout, llfechas;

    TextView asignatura, clase, fecha, fechaDev;
    ImageView imagen;

    List<Prestamo> listaPrestamos = new ArrayList<>();
    PrestAdapter adapter;

    Button devolver;
    int i;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prestamos);

        prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String actualUser = prefs.getString("email","");

        listaPrestamos = obtenerMisPrestamos(actualUser);

        listViewPrestamos = findViewById(R.id.lvPrestamos);
        adapter = new PrestAdapter(this, listaPrestamos);
        listViewPrestamos.setAdapter(adapter);

        seleccion = findViewById(R.id.prestamoSelec);
        linearLayout = findViewById(R.id.llSeleccion);
        llfechas = findViewById(R.id.llfechas);

        asignatura = findViewById(R.id.asignaturaPrestamo);
        clase = findViewById(R.id.clasePrestamo);
        fecha = findViewById(R.id.fechaPrestamo);
        fechaDev  = findViewById(R.id.fechaDevPrestamo);
        imagen = findViewById(R.id.imageView2);

        devolver = findViewById(R.id.devolverButton);

        listViewPrestamos.setOnItemClickListener( (AdapterView<?> parent, View view, int position, long id) -> {
            i = position;
            Prestamo p = listaPrestamos.get(position);
            Libro l = p.getLibro();

            asignatura.setText(l.getAsignatura());
            String clasecurso = l.getClase() +" "+l.getCurso();
            clase.setText(clasecurso);
            fecha.setText(p.getFecha().toString());
            fechaDev.setText(p.getFechaDev());
            //fechaDev.setText(p.getFechaDev().toString());


            Glide.with(PrestamosActivity.this)
                    .load(l.getImagen())
                    .into(imagen);

            linearLayout.setVisibility(View.VISIBLE);
            llfechas.setVisibility(View.VISIBLE);
            seleccion.setVisibility(View.VISIBLE);

            devolver.setVisibility(View.VISIBLE);
        });

        devolver.setOnClickListener( (View v) ->{
            Prestamo p = listaPrestamos.get(i);
            Libro l = p.getLibro();

            String frase = "¿Está seguro de que quiere devolver el libro de la asignatura " + l.getAsignatura() + " del curso " + l.getClase() + " " + l.getCurso() +
                    " de la editorial " + l.getEditorial() + " cuya fecha de devolución es " + p.getFechaDev()+ "?";

            AlertDialog.Builder alerta = new AlertDialog.Builder(PrestamosActivity.this);
            alerta.setMessage(frase).setPositiveButton("SI", (DialogInterface dialog, int id) -> {

                db.collection("prestamos").document(p.getId()).delete().addOnCompleteListener( (@NonNull Task<Void> task) ->
                    db.collection("libros").document(l.getId()).update("Estado", "disponible").addOnCompleteListener( (@NonNull Task<Void> task2) ->
                        db.collection("users").document(p.getUsuario()).get().addOnSuccessListener( (DocumentSnapshot documentSnapshot) ->{
                            Usuario u = new Usuario(documentSnapshot.getId(), documentSnapshot.getString("nombre"), documentSnapshot.getString("apellido"),
                                    documentSnapshot.getLong("numDonaciones"), documentSnapshot.getLong("numPrestamos"));
                            Long pres = documentSnapshot.getLong("numPrestamos");
                            pres--;
                            db.collection("users").document(u.getId()).update("numPrestamos", pres);
                            Toast.makeText(PrestamosActivity.this,"Libro devuelto correctamente.", Toast.LENGTH_SHORT).show();

                            listaPrestamos.remove(i);
                            adapter.notifyDataSetChanged();
                })));

                linearLayout.setVisibility(View.INVISIBLE);
                llfechas.setVisibility(View.INVISIBLE);
                seleccion.setVisibility(View.INVISIBLE);

                devolver.setVisibility(View.INVISIBLE);

            }).setNegativeButton("NO", (DialogInterface dialog, int id) -> Toast.makeText(PrestamosActivity.this, "DEVOLUCIÓN CANCELADA", Toast.LENGTH_SHORT).show());

            AlertDialog alertDialog = alerta.create();
            alertDialog.setTitle("¿ESTAS SEGURO?");
            alertDialog.show();

        });


    }

    public List<Prestamo> obtenerMisPrestamos(String user){
        List<Prestamo> lista = new ArrayList<>();

        db.collection("prestamos").whereEqualTo("Usuario",user).get().addOnCompleteListener( (@NonNull Task<QuerySnapshot> task) -> {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    db.collection("libros").whereEqualTo(FieldPath.documentId(), document.getString("Libro")).get().addOnCompleteListener((@NonNull Task<QuerySnapshot> task2) -> {
                        if (task2.isSuccessful()) {
                            for (QueryDocumentSnapshot document2 : Objects.requireNonNull(task2.getResult())) {
                                Libro libro = new Libro(document2.getId(), document2.getString("Asignatura"), document2.getString("Clase"), document2.getString("Curso"),
                                        document2.getString("Donante"),document2.getString("Editorial"), document2.getString("Estado"), document2.getString("Imagen"));


                                Prestamo p = new Prestamo(document.getId(), libro, document.getString("Usuario"),
                                        document.getDate("FechaPrestamo"), document.getString("FechaDevolucion"));

                                lista.add(p);
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