package com.marta.bookapp;

import static com.marta.bookapp.BotonesComunes.volverAMenuAdmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AdminPeticionesActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    Button aceptar, rechazar, aceptarTodas;

    TextView asignatura, clase, editorial, usuario, fecha;
    TextView tv;

    DonAdminAdapter adapter;
    ListView listViewPeticiones;
    List<DonacionPeticion> listaPeticiones = new ArrayList<>();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    int i = 0;
    boolean b = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_peticiones);

        linearLayout = findViewById(R.id.ll5);
        tv = findViewById(R.id.peticionestv);

        aceptar = findViewById(R.id.aceptarPetButton);
        rechazar = findViewById(R.id.rechazarPetButton);
        aceptarTodas = findViewById(R.id.aceptarTodasPetButton);

        asignatura = findViewById(R.id.asignaturatvPet);
        editorial = findViewById(R.id.editorialTVPet);
        clase = findViewById(R.id.clasetvPet);
        usuario = findViewById(R.id.userPet);
        fecha = findViewById(R.id.fechaPet);

        listaPeticiones = obtenerPeticiones();

        listViewPeticiones = findViewById(R.id.lvPeticiones);
        adapter = new DonAdminAdapter(this, listaPeticiones);
        listViewPeticiones.setAdapter(adapter);

        listViewPeticiones.setOnItemClickListener( (AdapterView<?> parent, View view, int position, long id) -> {

            DonacionPeticion d = listaPeticiones.get(position);
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
                DonacionPeticion p = listaPeticiones.get(i);
                Libro l = p.getLibro();

                Date date = new Date();

                Map<String, Object> libro = new HashMap<>();
                libro.put("Asignatura", l.getAsignatura());
                libro.put("Clase", l.getClase());
                libro.put("Curso", l.getCurso());
                libro.put("Editorial", l.getEditorial());
                libro.put("Estado", "prestado");
                libro.put("Donante", p.getEmailUsuario());
                libro.put("Fecha",date);

                db.collection("libros").document().set(libro).addOnSuccessListener( (Void unused) ->
                        Toast.makeText(AdminPeticionesActivity.this, "PETICION ACEPTADA.\n . ", Toast.LENGTH_SHORT).show() );

                Date fecha = new Date(2022,6,25);

                Map<String, Object> prestamo = new HashMap<>();
                prestamo.put("Libro", l.getId());
                prestamo.put("Usuario", p.getEmailUsuario());
                prestamo.put("FechaPrestamo", date);
                prestamo.put("FechaDevolucion", fecha);

                db.collection("prestamos").document().set(prestamo).addOnSuccessListener( (Void unused) ->
                        Toast.makeText(AdminPeticionesActivity.this, "LIBRO PRESTADO HASTA  "+ fecha, Toast.LENGTH_LONG).show() );

                db.collection("peticiones").document(p.getId()).delete();

                listaPeticiones.remove(i);
                adapter.notifyDataSetChanged();
                linearLayout.setVisibility(View.INVISIBLE);
                tv.setVisibility(View.INVISIBLE);

            }

        });

        rechazar.setOnClickListener( (View v) -> {


        });

        aceptarTodas.setOnClickListener( (View v) -> {
            for (int in = 0 ; in < listaPeticiones.size(); in++){

                DonacionPeticion p = listaPeticiones.get(in);
                Libro l = p.getLibro();

                Date date = new Date();

                Map<String, Object> libro = new HashMap<>();
                libro.put("Asignatura", l.getAsignatura());
                libro.put("Clase", l.getClase());
                libro.put("Curso", l.getCurso());
                libro.put("Editorial", l.getEditorial());
                libro.put("Estado", "prestado");
                libro.put("Donante", p.getEmailUsuario());
                libro.put("Fecha",date);

                db.collection("libros").document().set(libro).addOnSuccessListener( (Void unused) ->
                        Toast.makeText(AdminPeticionesActivity.this, "PETICIONES ACEPTADAS. ", Toast.LENGTH_LONG).show() );

                Date fecha = new Date(2022,6,25);

                Map<String, Object> prestamo = new HashMap<>();
                prestamo.put("Libro", l.getId());
                prestamo.put("Usuario", p.getEmailUsuario());
                prestamo.put("FechaPrestamo", date);
                prestamo.put("FechaDevolucion", fecha);

                db.collection("prestamos").document().set(prestamo).addOnSuccessListener( (Void unused) ->
                        Toast.makeText(AdminPeticionesActivity.this, "LIBRO PRESTADO HASTA  "+ fecha, Toast.LENGTH_LONG).show() );

                db.collection("peticiones").document(p.getId()).delete();

            }
            listaPeticiones.clear();
            adapter.notifyDataSetChanged();
            volverAMenuAdmin(AdminPeticionesActivity.this);

        });


    }


    public List<DonacionPeticion> obtenerPeticiones(){

        List<DonacionPeticion> lista = new ArrayList<>();

        db.collection("peticiones").get().addOnCompleteListener((@NonNull Task<QuerySnapshot> task) -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    db.collection("libros").whereEqualTo(FieldPath.documentId(),document.getString("Libro")).get().addOnCompleteListener( (@NonNull Task<QuerySnapshot> task2) -> {
                        if (task2.isSuccessful()) {
                            for (QueryDocumentSnapshot document2 : Objects.requireNonNull(task2.getResult())) {
                                Libro libro = new Libro(document2.getId(), document2.getString("Asignatura"), document2.getString("Clase"), document2.getString("Curso"),
                                        document2.getString("Donante"),document2.getString("Editorial"), document2.getString("Estado"),(R.drawable.imagen_no_disp));
                                System.out.println("asig:"+libro.getAsignatura());
                                DonacionPeticion donacion = new DonacionPeticion(document.getId(), document2.getString("Usuario"), libro, document.getDate("Fecha"));
                                lista.add(donacion);
                                adapter.notifyDataSetChanged();

                            }
                        }
                    });
                    /*Libro libro = new Libro(document.getString("Asignatura"), document.getString("Clase"), document.getString("Curso"),
                            document.getString("Donante"), document.getString("Editorial"), document.getString("Estado"), (R.drawable.imagen_no_disp));

                    DonacionPeticion donacion = new DonacionPeticion(document.getId(), document.getString("Usuario"),
                            libro, document.getDate("Fecha"));
                    lista.add(donacion);
                    adapter.notifyDataSetChanged();*/
                }
            }
        });

        return  lista;

    }
}