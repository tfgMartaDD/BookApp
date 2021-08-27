package com.marta.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdminDonacionesActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    Button aceptar, rechazar, aceptarTodas;

    TextView asignatura, clase, editorial, usuario, fecha;

    DonAdminAdapter adapter;
    ListView listViewDonaciones;
    List<DonacionPeticion> listaDonaciones = new ArrayList<>();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_donaciones);

        linearLayout = findViewById(R.id.linearLayout7);

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

            linearLayout.setVisibility(View.VISIBLE);

            asignatura.setText(l.getAsignatura());
            editorial.setText(l.getEditorial());
            String clasecurso = l.getClase() +" " +l.getCurso();
            clase.setText(clasecurso);

            usuario.setText(d.getEmailUsuario());
            fecha.setText(d.getFecha().toString());
        });


    }


    public List<DonacionPeticion> obtenerDonaciones(){

        List<DonacionPeticion> lista = new ArrayList<>();

        db.collection("posiblesDonaciones").get().addOnCompleteListener((@NonNull Task<QuerySnapshot> task) -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    String libroID = document.getString("Libro");

                    db.collection("libros").whereEqualTo(FieldPath.documentId(),libroID).get().addOnCompleteListener( (@NonNull Task<QuerySnapshot> task2) -> {
                        if (task2.isSuccessful()) {
                            for (QueryDocumentSnapshot query : Objects.requireNonNull(task2.getResult())) {
                                Libro libro = new Libro(query.getId(), query.getString("Asignatura"), query.getString("Clase"), query.getString("Curso"),
                                        query.getString("Donante"), query.getString("Editorial"), query.getString("Estado"), (R.drawable.imagen_no_disp));

                                DonacionPeticion donacion = new DonacionPeticion(document.getId(), document.getString("Usuario"),
                                        libro, document.getDate("Fecha"));
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