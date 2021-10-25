package com.marta.bookapp.Activitys;


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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.marta.bookapp.Modelo.DonacionPeticion;
import com.marta.bookapp.Modelo.Libro;
import com.marta.bookapp.R;
import com.marta.bookapp.adapter.DonAdminAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AdminDonacionesActivity extends AppCompatActivity {

    LinearLayout linearLayout;

    Button aceptarTodas;

    TextView asignatura, clase, editorial, usuario, fecha;
    TextView tv;

    DonAdminAdapter adapter;
    ListView listViewDonaciones;
    List<DonacionPeticion> listaDonaciones = new ArrayList<>();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_donaciones);

        linearLayout = findViewById(R.id.linearLayout7);
        tv = findViewById(R.id.textView25);


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

            linearLayout.setVisibility(View.VISIBLE);
            tv.setVisibility(View.VISIBLE);

            asignatura.setText(l.getAsignatura());
            editorial.setText(l.getEditorial());
            String clasecurso = l.getClase() +" " +l.getCurso();
            clase.setText(clasecurso);

            usuario.setText(d.getEmailUsuario());
            fecha.setText(d.getFecha().toString());

            String frase = "¿Que desea hacer con la donación seleccionada?";

            AlertDialog.Builder alerta1 = new AlertDialog.Builder(AdminDonacionesActivity.this);
            alerta1.setMessage(frase).setPositiveButton("ACEPTAR",  (DialogInterface dialog, int id2) ->{
                Date date = new Date();

                Map<String, Object> libro = new HashMap<>();
                libro.put("Asignatura", l.getAsignatura());
                libro.put("Clase", l.getClase());
                libro.put("Curso", l.getCurso());
                libro.put("Editorial", l.getEditorial());
                libro.put("Estado", "disponible");
                libro.put("Donante", d.getEmailUsuario());
                libro.put("Fecha",date);
                libro.put("Imagen", l.getImagen());

                db.collection("libros").document().set(libro).addOnSuccessListener( (Void unused) ->
                        Toast.makeText(AdminDonacionesActivity.this, "DONACION ACEPTADA.\n Libro añadido a la lista de disponibles. ", Toast.LENGTH_LONG).show() );

                db.collection("pendientes").document(d.getIdPendiente()).update("Estado","Aceptada");

                db.collection("posiblesDonaciones").document(d.getId()).delete();

                db.collection("users").document(d.getEmailUsuario()).get().addOnCompleteListener( (@NonNull Task<DocumentSnapshot> task) -> {
                    DocumentSnapshot document = task.getResult();
                    if(document != null){
                        Long num = document.getLong("numDonaciones");
                        if(num != null){
                            num++;
                            db.collection("users").document(d.getEmailUsuario()).update("numDonaciones",num);
                        }
                    }
                });

                listaDonaciones.remove(position);
                adapter.notifyDataSetChanged();
                linearLayout.setVisibility(View.INVISIBLE);
                tv.setVisibility(View.INVISIBLE);
            }).setNegativeButton("RECHAZAR",  (DialogInterface dialog, int id2) ->  {

                String frase2 = "¿Estás seguro de que desea rechazar la donacion de "+ d.getEmailUsuario()+" ? ";

                AlertDialog.Builder alerta2 = new AlertDialog.Builder(AdminDonacionesActivity.this);
                alerta2.setMessage(frase2).setPositiveButton("SI",  (DialogInterface dialog2, int id3) -> {
                    db.collection("posiblesDonaciones").document(d.getId()).delete().addOnSuccessListener( (Void unused) -> {
                        Toast.makeText(AdminDonacionesActivity.this, "PETICION RECHAZADA.", Toast.LENGTH_SHORT).show();
                        listaDonaciones.remove(position);
                        adapter.notifyDataSetChanged();
                    });
                    db.collection("pendientes").document(d.getIdPendiente()).update("Estado","Rechazada");

                }).setNegativeButton("NO",  (DialogInterface dialog2, int id3) ->  Toast.makeText(AdminDonacionesActivity.this, "ACCION CANCELADA", Toast.LENGTH_SHORT).show());

                AlertDialog alertDialog2 = alerta2.create();
                alertDialog2.setTitle("¿ESTAS SEGURO?");
                alertDialog2.show();
            });

            AlertDialog alertDialog = alerta1.create();
            alertDialog.setTitle("DONACION SELECCIONADA");
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
                libro.put("Imagen",l.getImagen());

                db.collection("libros").document().set(libro).addOnSuccessListener( (Void unused) ->
                        Toast.makeText(AdminDonacionesActivity.this, "DONACIONES ACEPTADAS.\n Libros añadidos a la lista de disponibles. ", Toast.LENGTH_LONG).show() );

                db.collection("pendientes").document(d.getIdPendiente()).update("Estado","Aceptada");
                db.collection("posiblesDonaciones").document(d.getId()).delete();

                db.collection("users").document(d.getEmailUsuario()).get().addOnCompleteListener( (@NonNull Task<DocumentSnapshot> task) -> {
                    DocumentSnapshot document = task.getResult();
                    if(document != null){
                        Long num = document.getLong("numDonaciones");
                        if(num != null){
                            num++;
                            db.collection("users").document(d.getEmailUsuario()).update("numDonaciones",num);
                        }
                    }
                });

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
                            document.getString("Donante"), document.getString("Editorial"), document.getString("Estado"), document.getString("Imagen"));

                    DonacionPeticion donacion = new DonacionPeticion(document.getId(), document.getString("Usuario"),
                            libro, document.getDate("Fecha"), document.getString("idPendiente"));
                    lista.add(donacion);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        return  lista;
    }
}