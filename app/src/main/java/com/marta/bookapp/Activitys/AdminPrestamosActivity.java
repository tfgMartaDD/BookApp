package com.marta.bookapp.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.marta.bookapp.Modelo.Libro;
import com.marta.bookapp.Modelo.Prestamo;
import com.marta.bookapp.Modelo.Usuario;
import com.marta.bookapp.R;
import com.marta.bookapp.Adapter.PrestAdminAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdminPrestamosActivity extends AppCompatActivity {

    PrestAdminAdapter adapter;
    ListView listViewAdminPrestamos;
    List<Prestamo> listaPrestamos = new ArrayList<>();

    LinearLayout ll;
    Spinner spinner1;
    RadioButton rbt, rbc;
    String clasecurso;

    Button mostrar;

    Button devolver;
    LinearLayout llprestamo;
    TextView user, fecha, libro;
    ImageView imagen;
    int i;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_prestamos);

        listViewAdminPrestamos = findViewById(R.id.lvPrestamosAdmin);
        adapter = new PrestAdminAdapter(this, listaPrestamos);
        listViewAdminPrestamos.setAdapter(adapter);

        rbt = findViewById(R.id.rbTodos);
        rbc = findViewById(R.id.rbClase);

        spinner1 = findViewById(R.id.spinner1);

        ll = findViewById(R.id.llspinner);

        mostrar = findViewById(R.id.buttonMostrar);

        devolver = findViewById(R.id.devolverBoton);
        user = findViewById(R.id.usertv);
        fecha = findViewById(R.id.fechaDevtv);
        libro = findViewById(R.id.libroprestamotv);
        llprestamo = findViewById(R.id.llprestamo);
        imagen = findViewById(R.id.imageView7);

        listViewAdminPrestamos.setOnItemClickListener( (AdapterView<?> parent, View view, int position, long id) -> {
            i = position;
            Prestamo p = listaPrestamos.get(position);
            Libro l = p.getLibro();

            user.setText(p.getUsuario());
            fecha.setText(p.getFechaDev());

            Glide.with(AdminPrestamosActivity.this)
                    .load(l.getImagen())
                    .into(imagen);

            String prestamo = l.getAsignatura()+ " "+l.getClase()+ " "+l.getCurso()+ " "+l.getEditorial();
            libro.setText(prestamo);

            llprestamo.setVisibility(View.VISIBLE);
            devolver.setVisibility(View.VISIBLE);
        });

        devolver.setOnClickListener( (View v) ->{
            Prestamo p = listaPrestamos.get(i);
            Libro l = p.getLibro();

            String frase = "¿Está seguro de que quiere devolver el libro de la asignatura " + l.getAsignatura() + " del curso " + l.getClase() + " " + l.getCurso() +
                    " de la editorial " + l.getEditorial() + " cuya fecha de devolución es " + p.getFechaDev()+ "?";

            AlertDialog.Builder alerta = new AlertDialog.Builder(AdminPrestamosActivity.this);
            alerta.setMessage(frase).setPositiveButton("SI", (DialogInterface dialog, int id) -> {

                db.collection("prestamos").document(p.getId()).delete().addOnCompleteListener( (@NonNull Task<Void> task) ->
                        db.collection("libros").document(l.getId()).update("Estado", "disponible").addOnCompleteListener( (@NonNull Task<Void> task2) ->
                                db.collection("users").document(p.getUsuario()).get().addOnSuccessListener( (DocumentSnapshot documentSnapshot) ->{
                                    Usuario u = new Usuario(documentSnapshot.getId(), documentSnapshot.getString("nombre"), documentSnapshot.getString("apellido"),
                                            documentSnapshot.getLong("numDonaciones"), documentSnapshot.getLong("numPrestamos"));
                                    Long pres = documentSnapshot.getLong("numPrestamos");
                                    pres--;
                                    db.collection("users").document(u.getId()).update("numPrestamos", pres);
                                    Toast.makeText(AdminPrestamosActivity.this,"Libro devuelto correctamente.", Toast.LENGTH_SHORT).show();

                                    listaPrestamos.remove(i);
                                    adapter.notifyDataSetChanged();
                                })));

                llprestamo.setVisibility(View.INVISIBLE);
                devolver.setVisibility(View.INVISIBLE);

            }).setNegativeButton("NO", (DialogInterface dialog, int id) -> Toast.makeText(AdminPrestamosActivity.this, "DEVOLUCIÓN CANCELADA", Toast.LENGTH_SHORT).show());

            AlertDialog alertDialog = alerta.create();
            alertDialog.setTitle("¿ESTAS SEGURO?");
            alertDialog.show();

        });
    }

    public void comprobarRadioButton(View view){

        if(rbt.isChecked()){

            ll.setVisibility(View.INVISIBLE);
            listaPrestamos = obtenerPrestamos();

            adapter = new PrestAdminAdapter(this, listaPrestamos);
            listViewAdminPrestamos.setAdapter(adapter);
            listViewAdminPrestamos.setVisibility(View.VISIBLE);


        }else if(rbc.isChecked()){

            ll.setVisibility(View.VISIBLE);
            listViewAdminPrestamos.setVisibility(View.INVISIBLE);

            String [] opciones = {"PRIMERO PRIMARIA", "SEGUNDO PRIMARIA", "TERCERO PRIMARIA", "CUARTO PRIMARIA", "QUINTO PRIMARIA", "SEXTO PRIMARIA",
                    "PRIMERO ESO", "SEGUNDO ESO", "TERCERO ESO", "CUARTO ESO", "PRIMERO BACHILLERATO", "SEGUNDO BACHILLERATO"};

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
            spinner1.setAdapter(arrayAdapter);

            mostrar.setOnClickListener( (View v) -> {
                clasecurso = spinner1.getSelectedItem().toString();
                listaPrestamos = obtenerPrestamosClases(clasecurso);
                listViewAdminPrestamos.setVisibility(View.VISIBLE);

                adapter = new PrestAdminAdapter(this, listaPrestamos);
                listViewAdminPrestamos.setAdapter(adapter);

            });

        }

    }


    public List<Prestamo> obtenerPrestamos(){
        List<Prestamo> listaTodos = new ArrayList<>();

        db.collection("libros").whereEqualTo("Estado", "prestado").get().addOnCompleteListener( (@NonNull Task<QuerySnapshot> task) -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                    db.collection("prestamos").whereEqualTo("Libro", document.getId()).get().addOnCompleteListener((@NonNull Task<QuerySnapshot> task2) -> {
                        if (task2.isSuccessful()) {
                            for (QueryDocumentSnapshot document2 : Objects.requireNonNull(task2.getResult())) {
                                Libro libro = new Libro(document.getId(), document.getString("Asignatura"), document.getString("Clase"), document.getString("Curso"),
                                        document.getString("Donante"), document.getString("Editorial"), document.getString("Estado"), document.getString("Imagen"));
                                Prestamo p = new Prestamo(document2.getId(), libro, document2.getString("Usuario"),
                                        document2.getDate("FechaPrestamo"), document2.getString("FechaDevolucion"));

                                listaTodos.add(p);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        });

        return listaTodos;
    }

    public List<Prestamo> obtenerPrestamosClases(String cursoClase){
        List<Prestamo> listaClases = new ArrayList<>();

        String [] partes = cursoClase.split(" ");
        String clase = partes[0];
        String curso = partes[1];


        db.collection("libros").whereEqualTo("Estado", "prestado").whereEqualTo("Clase", clase).whereEqualTo("Curso", curso).get().addOnCompleteListener( (@NonNull Task<QuerySnapshot> task) -> {
            if(task.isSuccessful()){
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                    db.collection("prestamos").whereEqualTo("Libro",document.getId()).get().addOnCompleteListener( (@NonNull Task<QuerySnapshot> task2) -> {
                        if (task2.isSuccessful()) {
                            for (QueryDocumentSnapshot document2 : Objects.requireNonNull(task2.getResult())) {

                                Libro libro = new Libro(document.getId(), document.getString("Asignatura"), document.getString("Clase"), document.getString("Curso"),
                                        document.getString("Donante"),document.getString("Editorial"), document.getString("Estado"), document.getString("Imagen"));
                                Prestamo p = new Prestamo(document2.getId(), libro, document2.getString("Usuario"),
                                        document2.getDate("FechaPrestamo"), document2.getString("FechaDevolucion"));

                                listaClases.add(p);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        });

        return listaClases;
    }
}