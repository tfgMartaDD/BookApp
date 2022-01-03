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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.marta.bookapp.DatePickerFragment;
import com.marta.bookapp.Modelo.DonacionPeticion;
import com.marta.bookapp.Modelo.Libro;
import com.marta.bookapp.R;
import com.marta.bookapp.Adapter.DonAdminAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AdminPeticionesActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    Button aceptarTodas, decidir;


    EditText fechaEditText;

    TextView asignatura, clase, editorial, usuario, fecha;
    TextView tv;

    DonAdminAdapter adapter;
    ListView listViewPeticiones;
    List<DonacionPeticion> listaPeticiones = new ArrayList<>();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    String fechaDevolucion = "25 / 6 / 2022";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_peticiones);

        fechaEditText = findViewById(R.id.etFecha);
        fechaEditText.setOnClickListener( (View v) -> {
            if (v.getId() == R.id.etFecha) {
                showDatePickerDialog();
            }
        });

        linearLayout = findViewById(R.id.ll5);
        tv = findViewById(R.id.peticionestv);


        aceptarTodas = findViewById(R.id.aceptarTodasPetButton);
        decidir = findViewById(R.id.decidirPetButton);

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

            fechaEditText.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            tv.setVisibility(View.VISIBLE);

            asignatura.setText(l.getAsignatura());
            editorial.setText(l.getEditorial());
            String clasecurso = l.getClase() +" " +l.getCurso();
            clase.setText(clasecurso);

            usuario.setText(d.getEmailUsuario());
            fecha.setText(d.getFecha().toString());

            decidir.setVisibility(View.VISIBLE);
            decidir.setOnClickListener((View v) -> {
                String frase = "¿Que desea hacer con la petición seleccionada?";

                AlertDialog.Builder alerta1 = new AlertDialog.Builder(AdminPeticionesActivity.this);
                alerta1.setMessage(frase).setPositiveButton("ACEPTAR",  (DialogInterface dialog, int id2) ->{

                    Date date = new Date();

                    Map<String, Object> libro = new HashMap<>();
                    libro.put("Asignatura", l.getAsignatura());
                    libro.put("Clase", l.getClase());
                    libro.put("Curso", l.getCurso());
                    libro.put("Editorial", l.getEditorial());
                    libro.put("Estado", "prestado");
                    libro.put("Donante", d.getEmailUsuario());
                    libro.put("Fecha",date);
                    libro.put("Imagen",l.getImagen());

                    db.collection("libros").document(l.getId()).set(libro).addOnSuccessListener( (Void unused) ->
                            Toast.makeText(AdminPeticionesActivity.this, "PETICION ACEPTADA. ", Toast.LENGTH_SHORT).show() );

                    db.collection("pendientes").document(d.getIdPendiente()).update("Estado","Aceptada");

                    Map<String, Object> prestamo = new HashMap<>();
                    prestamo.put("Libro", l.getId());
                    prestamo.put("Usuario", d.getEmailUsuario());
                    prestamo.put("FechaPrestamo", date);
                    prestamo.put("FechaDevolucion", fechaDevolucion);

                    db.collection("prestamos").document().set(prestamo).addOnSuccessListener( (Void unused) ->
                            Toast.makeText(AdminPeticionesActivity.this, "LIBRO PRESTADO HASTA  "+ fechaDevolucion, Toast.LENGTH_LONG).show() );

                    db.collection("peticiones").document(d.getId()).delete();

                    db.collection("users").document(d.getEmailUsuario()).get().addOnCompleteListener( (@NonNull Task<DocumentSnapshot> task) -> {
                        DocumentSnapshot document = task.getResult();
                        if(document != null){
                            Long num = document.getLong("numPrestamos");
                            if(num != null){
                                num++;
                                db.collection("users").document(d.getEmailUsuario()).update("numPrestamos",num);
                            }
                        }
                    });

                    listaPeticiones.remove(position);
                    adapter.notifyDataSetChanged();
                    linearLayout.setVisibility(View.INVISIBLE);
                    tv.setVisibility(View.INVISIBLE);


                }).setNegativeButton("RECHAZAR",  (DialogInterface dialog, int id2) ->  {

                    String frase3 = "¿Estás seguro de que desea rechazar la peticion de "+ d.getEmailUsuario()+" ? ";

                    AlertDialog.Builder alerta2 = new AlertDialog.Builder(AdminPeticionesActivity.this);
                    alerta2.setMessage(frase3).setPositiveButton("SI",  (DialogInterface dialog2, int id3) ->{

                        db.collection("peticiones").document(d.getId()).delete().addOnSuccessListener( (Void unused) -> {
                            Toast.makeText(AdminPeticionesActivity.this, "PETICION RECHAZADA.", Toast.LENGTH_SHORT).show();
                            listaPeticiones.remove(position);
                            adapter.notifyDataSetChanged();
                        });

                        db.collection("pendientes").document(d.getIdPendiente()).update("Estado","Rechazada");

                    }).setNegativeButton("NO",  (DialogInterface dialog2, int id3) ->  Toast.makeText(AdminPeticionesActivity.this, "ACCION CANCELADA", Toast.LENGTH_SHORT).show());

                    AlertDialog alertDialog2 = alerta2.create();
                    alertDialog2.setTitle("¿ESTAS SEGURO?");
                    alertDialog2.show();

                    Toast.makeText(AdminPeticionesActivity.this, "ACCION CANCELADA", Toast.LENGTH_SHORT).show();
                });

                AlertDialog alertDialog = alerta1.create();
                alertDialog.setTitle("¿ESTAS SEGURO?");
                alertDialog.show();
            });
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
                libro.put("Fecha", date);
                libro.put("Imagen",l.getImagen());

                db.collection("libros").document(l.getId()).set(libro).addOnSuccessListener( (Void unused) ->
                        Toast.makeText(AdminPeticionesActivity.this, "PETICIONES ACEPTADAS. ", Toast.LENGTH_LONG).show() );

                //Date fechaDev = new Date(2022,6,25);

                Map<String, Object> prestamo = new HashMap<>();
                prestamo.put("Libro", l.getId());
                prestamo.put("Usuario", p.getEmailUsuario());
                prestamo.put("FechaPrestamo", date);
                prestamo.put("FechaDevolucion", fechaDevolucion);

                db.collection("prestamos").document().set(prestamo).addOnSuccessListener( (Void unused) ->
                        Toast.makeText(AdminPeticionesActivity.this, "LIBRO PRESTADO HASTA  "+ fechaDevolucion, Toast.LENGTH_LONG).show() );

                db.collection("peticiones").document(p.getId()).delete();

                db.collection("pendientes").document(p.getIdPendiente()).update("Estado","Aceptada");

                db.collection("users").document(p.getEmailUsuario()).get().addOnCompleteListener( (@NonNull Task<DocumentSnapshot> task) -> {
                    DocumentSnapshot document = task.getResult();
                    if(document != null){
                        Long num = document.getLong("numPrestamos");
                        if (num != null) {
                            num++;
                        }else{
                            num = 1L;
                        }

                        db.collection("users").document(p.getEmailUsuario()).update("numPrestamos",num);
                    }
                });

            }
            listaPeticiones.clear();
            adapter.notifyDataSetChanged();
            volverAMenuAdmin(AdminPeticionesActivity.this);

        });


    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance( (DatePicker datePicker, int year, int month, int day) -> {
            // +1 because January is zero
            final String selectedDate = day + " / " + (month+1) + " / " + year;
            fechaEditText.setText(selectedDate);
            fechaDevolucion = selectedDate;
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
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
                                        document2.getString("Donante"),document2.getString("Editorial"), document2.getString("Estado"), document2.getString("Imagen"));

                                DonacionPeticion donacion = new DonacionPeticion(document.getId(), document.getString("Usuario"),
                                        libro, document.getDate("Fecha"), document.getString("idPendiente"));
                                lista.add(donacion);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }
        });

        return  lista;
    }

}