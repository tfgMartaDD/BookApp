package com.marta.bookapp.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.marta.bookapp.Modelo.Libro;
import com.marta.bookapp.R;
import com.marta.bookapp.Adapter.ListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DonacionesActivity extends AppCompatActivity {

    Button addBTN;

    ListView listViewDonaciones;
    List<Libro> listaDonacion = new ArrayList<>();
    ListAdapter adapter;

    ImageView imagen;
    TextView asignatura, clase, editorial;
    TextView a, c, e, don;


    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donaciones);

        addBTN = findViewById(R.id.addButton);
        addBTN.setOnClickListener( (View v) -> {
            Intent in = new Intent (DonacionesActivity.this, NuevaDonacionActivity.class);
            startActivity(in);
        });

        prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String actualUser = prefs.getString("email","");


        listaDonacion = obtenerMisDonaciones(actualUser);

        listViewDonaciones = findViewById(R.id.lvDonaciones);

        adapter = new ListAdapter(this, listaDonacion);
        listViewDonaciones.setAdapter(adapter);

        asignatura = findViewById(R.id.asignaturatv);
        clase = findViewById(R.id.clasetv);
        editorial = findViewById(R.id.editorialTV);
        imagen = findViewById(R.id.portadaIV);

        a = findViewById(R.id.tvasig);
        c = findViewById(R.id.tvclase);
        e = findViewById(R.id.tveditorial);
        don = findViewById(R.id.donacionSelec);

        listViewDonaciones.setOnItemClickListener( (AdapterView<?> parent, View view, int position, long id) -> {

            Libro l = listaDonacion.get(position);

            a.setVisibility(View.VISIBLE);
            c.setVisibility(View.VISIBLE);
            e.setVisibility(View.VISIBLE);
            don.setVisibility(View.VISIBLE);

            Glide.with(DonacionesActivity.this)
                    .load(l.getImagen())
                    .into(imagen);

            //imagen.setImageResource(l.getImagen());
            asignatura.setText(l.getAsignatura());

            String clasecurso = l.getClase() +" " +l.getCurso();
            clase.setText(clasecurso);

            editorial.setText(l.getEditorial());
        });

    }

    public List<Libro> obtenerMisDonaciones(String user){
        List<Libro> lista = new ArrayList<>();

        db.collection("libros").whereEqualTo("Donante",user).get().addOnCompleteListener( (@NonNull Task<QuerySnapshot> task) -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot query : Objects.requireNonNull(task.getResult())) {
                    Libro libro = new Libro(query.getId(), query.getString("Asignatura"), query.getString("Clase"), query.getString("Curso"),
                            query.getString("Donante"), query.getString("Editorial"), query.getString("Estado"), query.getString("Imagen"));

                    lista.add(libro);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        return lista;
    }
}