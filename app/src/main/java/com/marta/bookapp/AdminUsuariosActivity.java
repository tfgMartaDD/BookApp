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
    List<UserLista> listaUsuarios = new ArrayList<>();

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

    private List<UserLista> obtenerListaUsuarios() {

        List<UserLista> lista = new ArrayList<>();


        db.collection("users").get().addOnCompleteListener( (@NonNull Task<QuerySnapshot> task) -> {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                    String numD, numP;
                    int numDonaciones = 0, numPrestamos = 0;

                    if(document != null){
                        numD = document.getString("numDonaciones");
                        if(numD != null){
                             numDonaciones = Integer.parseInt(numD);
                        }
                        numP = document.getString("numPrestamos");
                        if(numP != null){
                            numPrestamos = Integer.parseInt(numP);
                        }
                        UserLista user = new UserLista(document.getId(), document.getString("nombre"),document.getString("apellido"),
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