package com.marta.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DonacionesActivity extends AppCompatActivity {

    Button addBTN;

    ListView listViewDonaciones;
    List<Donacion> listaDonacion = new ArrayList<Donacion>();
    DonAdapter adapter;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donaciones);

        addBTN = findViewById(R.id.addButton);
        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent (DonacionesActivity.this, NuevaDonacionActivity.class);
                startActivity(in);
            }
        });

        prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String actualUser = prefs.getString("email","");


        listaDonacion = obtenerMisDonaciones(actualUser);

        listViewDonaciones = findViewById(R.id.lvDonaciones);

        adapter = new DonAdapter(this, listaDonacion);
        listViewDonaciones.setAdapter(adapter);

        listViewDonaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Donacion d = listaDonacion.get(position);
            }
        });

    }

    public List<Donacion> obtenerMisDonaciones(String user){
        List<Donacion> lista = new ArrayList<>();

        CollectionReference donaciones = db.collection("donaciones");
        donaciones.whereEqualTo("Usuario",user).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Donacion donacion = new Donacion(document.getId(), document.getString("Usuario"),
                                document.getString("Libro"),document.getString("Fecha"));
                        listaDonacion.add(donacion);
                        adapter.notifyDataSetChanged();
                    }
                    adapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(DonacionesActivity.this,  "Error getting documents: ", Toast.LENGTH_LONG).show();
                }
            }
        });
        return lista;
    }
}