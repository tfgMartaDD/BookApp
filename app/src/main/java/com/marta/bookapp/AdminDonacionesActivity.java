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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AdminDonacionesActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    Button aceptar, rechazar, aceptarTodas;

    TextView asignatura, clase, editorial, usuario, fecha;
    TextView tv;

    DonAdminAdapter adapter;
    ListView listViewDonaciones;
    List<DonacionPeticion> listaDonaciones = new ArrayList<>();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    int i = 0;
    boolean b = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_donaciones);

        linearLayout = findViewById(R.id.linearLayout7);
        tv = findViewById(R.id.textView25);

        aceptar = findViewById(R.id.aceptarButton);
        rechazar = findViewById(R.id.rechazarButton);
        aceptarTodas = findViewById(R.id.aceptarTodasButton);

        asignatura = findViewById(R.id.asignaturatvDon);
        editorial = findViewById(R.id.editorialTVDon);
        clase = findViewById(R.id.clasetvDon);
        usuario = findViewById(R.id.userDon);
        fecha = findViewById(R.id.fechaDon);

        listaDonaciones = obtenerDonaciones();

        listViewDonaciones = findViewById(R.id.lvDonacionesAprobar);
        adapter = new DonAdminAdapter(this, listaDonaciones);
        listViewDonaciones.setAdapter(adapter);

        listViewDonaciones.setOnItemClickListener( (AdapterView<?>parent, View view, int position, long id) -> {

            DonacionPeticion d = listaDonaciones.get(position);
            Libro l = d.getLibro();
            i = position;
            b = true;

            linearLayout.setVisibility(View.VISIBLE);
            tv.setVisibility(View.VISIBLE);

            asignatura.setText(l.getAsignatura());
            editorial.setText(l.getEditorial());
            String clasecurso = l.getClase() +" " +l.getCurso();
            clase.setText(clasecurso);

            usuario.setText(d.getEmailUsuario());
            fecha.setText(d.getFecha().toString());
        });

        aceptar.setOnClickListener( (View v) -> {
            if(b){
                DonacionPeticion d = listaDonaciones.get(i);
                Libro l = d.getLibro();

                //
                Date date = new Date();

                Map<String, Object> libro = new HashMap<>();
                libro.put("Asignatura", l.getAsignatura());
                libro.put("Clase", l.getClase());
                libro.put("Curso", l.getCurso());
                libro.put("Editorial", l.getEditorial());
                libro.put("Estado", "disponible");
                libro.put("Donante", d.getEmailUsuario());
                libro.put("Fecha",date);

                db.collection("libros").document().set(libro).addOnSuccessListener( (Void unused) ->
                    Toast.makeText(AdminDonacionesActivity.this, "DONACION ACEPTADA.\n Libro añadido a la lista de disponibles. ", Toast.LENGTH_LONG).show() );

                db.collection("posiblesDonaciones").document(d.getId()).delete();

                listaDonaciones.remove(i);
                adapter.notifyDataSetChanged();
                linearLayout.setVisibility(View.INVISIBLE);
                tv.setVisibility(View.INVISIBLE);

            }

        });

        rechazar.setOnClickListener( (View v) -> {

            DonacionPeticion p = listaDonaciones.get(i);

            String frase = "¿Estás seguro de que desea rechazar la donacion de "+ p.getEmailUsuario()+" ? ";

            AlertDialog.Builder alerta = new AlertDialog.Builder(AdminDonacionesActivity.this);
            alerta.setMessage(frase).setPositiveButton("SI",  (DialogInterface dialog, int id) -> {
                db.collection("posiblesDonaciones").document(p.getId()).delete().addOnSuccessListener( (Void unused) -> {
                    Toast.makeText(AdminDonacionesActivity.this, "PETICION RECHAZADA.", Toast.LENGTH_SHORT).show();
                    listaDonaciones.remove(i);
                    adapter.notifyDataSetChanged();
                });

            }).setNegativeButton("NO",  (DialogInterface dialog, int id) ->  Toast.makeText(AdminDonacionesActivity.this, "ACCION CANCELADA", Toast.LENGTH_SHORT).show());

            AlertDialog alertDialog = alerta.create();
            alertDialog.setTitle("¿ESTAS SEGURO?");
            alertDialog.show();


        });

        aceptarTodas.setOnClickListener( (View v) -> {
            for (int in = 0 ; in < listaDonaciones.size(); in++){

                DonacionPeticion d = listaDonaciones.get(in);
                Libro l = d.getLibro();

                Date date = new Date();

                Map<String, Object> libro = new HashMap<>();
                libro.put("Asignatura", l.getAsignatura());
                libro.put("Clase", l.getClase());
                libro.put("Curso", l.getCurso());
                libro.put("Editorial", l.getEditorial());
                libro.put("Estado", "disponible");
                libro.put("Donante", d.getEmailUsuario());
                libro.put("Fecha",date);

                db.collection("libros").document().set(libro).addOnSuccessListener( (Void unused) ->
                        Toast.makeText(AdminDonacionesActivity.this, "DONACIONES ACEPTADAS.\n Libros añadidos a la lista de disponibles. ", Toast.LENGTH_LONG).show() );

                db.collection("posiblesDonaciones").document(d.getId()).delete();

            }
            listaDonaciones.clear();
            adapter.notifyDataSetChanged();
            volverAMenuAdmin(AdminDonacionesActivity.this);

        });


    }


    public List<DonacionPeticion> obtenerDonaciones(){

        List<DonacionPeticion> lista = new ArrayList<>();

        db.collection("posiblesDonaciones").get().addOnCompleteListener((@NonNull Task<QuerySnapshot> task) -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Libro libro = new Libro(document.getString("Asignatura"), document.getString("Clase"), document.getString("Curso"),
                            document.getString("Donante"), document.getString("Editorial"), document.getString("Estado"), (R.drawable.imagen_no_disp));

                    DonacionPeticion donacion = new DonacionPeticion(document.getId(), document.getString("Usuario"),
                            libro, document.getDate("Fecha"));
                    lista.add(donacion);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        return  lista;

    }
}