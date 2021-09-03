package com.marta.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdminUsuariosActivity extends AppCompatActivity {

    UsuariosAdapter adapter;
    ListView listViewUsuarios;
    List<Usuario> listaUsuarios = new ArrayList<>();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_usuarios);

        listaUsuarios = obtenerListaUsuarios();

        listViewUsuarios = findViewById(R.id.lvUsuarios);
        adapter = new UsuariosAdapter(this, listaUsuarios);
        listViewUsuarios.setAdapter(adapter);

        listViewUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private List<Usuario> obtenerListaUsuarios() {

        List<Usuario> lista = new ArrayList<>();


        db.collection("users").get().addOnCompleteListener( (@NonNull Task<QuerySnapshot> task) -> {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                    long numDonaciones , numPrestamos;

                    if(document != null){
                        numDonaciones = document.getLong("numDonaciones");

                        numPrestamos =  document.getLong("numPrestamos");

                        Usuario user = new Usuario(document.getId(), document.getString("nombre"),document.getString("apellido"),
                                numDonaciones, numPrestamos);
                        lista.add(user);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        return lista;
    }
}