package com.marta.bookapp.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.marta.bookapp.Modelo.Pendientes;
import com.marta.bookapp.R;
import com.marta.bookapp.Adapter.PendientesAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Clase que muestra al usuario cual es el estado de sus peticiones
 * y donaciones, si han sido aceptadas, rechazadas o siguen pendientes.
 *
 * @author Marta Diego u158691@usal.es
 */
public class EstadoPendientesActivity extends AppCompatActivity {

    ListView listViewPendientes;

    List<Pendientes> listaPendientes = new ArrayList<>();
    PendientesAdapter adapter;

    SharedPreferences prefs;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estado_pendientes);

        prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String actualUser = prefs.getString("email","");

        listaPendientes = obtenerPendientes(actualUser);

        listViewPendientes = findViewById(R.id.lvPendientes);
        adapter = new PendientesAdapter(this, listaPendientes);
        listViewPendientes.setAdapter(adapter);
    }

    /**
     * Método para recuperar de la base de datos las donaciones y reservas pendientes.
     * @param user identificador del usuario activo
     * @return una lista de Pendientes con los datos recuperados
     */
    private List<Pendientes> obtenerPendientes(String user) {
        List<Pendientes> lista = new ArrayList<>();

        db.collection("pendientes").whereEqualTo("Usuario",user).get().addOnCompleteListener( (@NonNull Task<QuerySnapshot> task) -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                    Pendientes p = new Pendientes(document.getId(), Boolean.valueOf(document.getString("esPeticion") ), document.getString("Asignatura"),
                            document.getString("Clase"), document.getString("Curso"), document.getString("Estado"), document.getString("Usuario"));

                    lista.add(p);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        return lista;
    }
}