package com.marta.bookapp.Activitys;

import static com.marta.bookapp.BotonesComunes.volverAMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.marta.bookapp.Modelo.Libro;
import com.marta.bookapp.R;
import com.marta.bookapp.Adapter.ListAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Clase que muestra un listado con los libros disponibles del
 * curso que hemos seleccionado anteriormente.
 *
 * @author Marta Diego u158691@usal.es
 */
public class ListadoDispActivity extends AppCompatActivity {

    TextView cursoTV, claseTV, mostrarTV;
    TextView campos, seleccion;
    ListView listViewDisponibles;
    List<Libro> listaLibro = new ArrayList<>();
    ListAdapter adapter;
    Button reservarBTN, volverBTN, menuBTN;
    Libro libro;

    TextView noDisp;
    LinearLayout ll4;

    int i = 0;
    String idPendiente = "";
    Boolean flag = false;
    Boolean flag2 = false;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference libros = db.collection("libros");
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_disp);

        Bundle bundle = getIntent().getExtras();
        String curso = bundle.getString("curso");
        String clase = bundle.getString("clase");

        prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String actualUser = prefs.getString("email","");
        //final String idPendiente = "";

        noDisp = findViewById(R.id.noDisponibles);
        ll4 = findViewById(R.id.ll4);

        campos = findViewById(R.id.camposTV);
        seleccion = findViewById(R.id.seleccionTV);

        cursoTV = findViewById(R.id.textViewCurso);
        cursoTV.setText(curso);

        claseTV = findViewById(R.id.textViewClase);
        claseTV.setText(clase);

        mostrarTV = findViewById(R.id.mostrarTextView);

        listaLibro = obtenerLibros(curso, clase);

        listViewDisponibles = findViewById(R.id.lv1);

        adapter = new ListAdapter(this, listaLibro );
        listViewDisponibles.setAdapter(adapter);

        listViewDisponibles.setOnItemClickListener( (AdapterView<?> parent, View view, int position, long id) -> {
            Libro l = listaLibro.get(position);
            libro = listaLibro.get(position);

            reservarBTN.setVisibility(View.VISIBLE);
            campos.setVisibility(View.VISIBLE);
            seleccion.setVisibility(View.VISIBLE);

            i = position;

            String mostrar = l.getAsignatura() + "\t" + l.getClase() + "  " + l.getCurso() + "\t" + l.getEditorial();
            mostrarTV.setText(mostrar);
            flag = true;

            String asigL = libro.getAsignatura();
            String claseL = libro.getClase();
            String cursoL = libro.getCurso();

            db.collection("prestamos").whereEqualTo("Usuario", actualUser).get().addOnCompleteListener((@NonNull Task<QuerySnapshot> task) -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot query : Objects.requireNonNull(task.getResult())) {
                        String id2 = query.getString("Libro");
                        db.collection("libros").whereEqualTo(FieldPath.documentId(), id2).get().addOnCompleteListener((@NonNull Task<QuerySnapshot> task2) -> {
                            if (task2.isSuccessful()) {
                                for (QueryDocumentSnapshot query2 : Objects.requireNonNull(task2.getResult())) {
                                    String a = query2.getString("Asignatura");
                                    String cl = query2.getString("Clase");
                                    String cu = query2.getString("Curso");

                                    assert a != null;
                                    if ((a.equalsIgnoreCase(asigL))) {
                                        assert cl != null;
                                        if ((cl.equalsIgnoreCase(claseL))) {
                                            assert cu != null;
                                            if (cu.equalsIgnoreCase(cursoL)) {
                                                flag = false;
                                                flag2 = true;
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            });

        });



        reservarBTN = findViewById(R.id.reservarButton);
        reservarBTN.setOnClickListener( (View v) ->{

            if(flag){

                Map<String, Object> pendiente = new HashMap<>();
                pendiente.put("Asignatura", libro.getAsignatura());
                pendiente.put("esPeticion","true");
                pendiente.put("Clase", libro.getClase());
                pendiente.put("Curso",libro.getCurso());
                pendiente.put("Estado", "pendiente");
                pendiente.put("Usuario", actualUser);

                db.collection("pendientes").add(pendiente).addOnSuccessListener( (DocumentReference documentReference) ->  {
                    idPendiente = documentReference.getId();

                    /*//cambiamos el estado del libro de Disponible a Reservado
                    Map<String, Object> reserva= new HashMap<>();
                    reserva.put("Asignatura",libro.getAsignatura());
                    reserva.put("Clase",libro.getClase());
                    reserva.put("Curso",libro.getCurso());
                    reserva.put("Donante",libro.getDonante());
                    reserva.put("Editorial",libro.getEditorial());
                    reserva.put("Estado", "reservado");
                    reserva.put("Imagen", libro.getImagen());*/


                    libros.document(libro.getId()).update("Estado", "reservado").addOnSuccessListener( (Void unused) ->
                            Toast.makeText(ListadoDispActivity.this, "Libros Reservados con exito", Toast.LENGTH_SHORT).show());

                    Date date = new Date();

                    Map<String, Object> peticion= new HashMap<>();
                    peticion.put("Libro",libro.getId());
                    peticion.put("Usuario",actualUser);
                    peticion.put("Fecha",date);
                    peticion.put("idPendiente",idPendiente);

                    db.collection("peticiones").add(peticion);

                });

                listaLibro.remove(i);
                adapter.notifyDataSetChanged();

                mostrarTV.setVisibility(View.INVISIBLE);
                campos.setVisibility(View.INVISIBLE);
                seleccion.setVisibility(View.INVISIBLE);

            }else{
                if(flag2){
                    Toast.makeText(ListadoDispActivity.this, "No puede reservar más de un libro de la misma asignatura y la misma clase. \n SELECCIONE UN LIBRO DISTINTO",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ListadoDispActivity.this, "SELECCIONE EL LIBRO QUE DESEA RESERVAR.",Toast.LENGTH_SHORT).show();

                }
            }

        });

        volverBTN = findViewById(R.id.volverAtras);
        volverBTN.setOnClickListener( (View v) -> {
            Intent in = new Intent (ListadoDispActivity.this, CursosActivity.class);
            startActivity(in);
        });

        menuBTN = findViewById(R.id.volverMenu);
        menuBTN.setOnClickListener( (View v) ->  volverAMenu(ListadoDispActivity.this));

    }


    /**
     * Método para recuperar los libros disponibles de la base de datos del curso seleccionado
     * @param curso curso del libro que nos interesa
     * @param clase clase del libro que nos interesa
     * @return una lista de Libro con los libros recuperados
     */
    public List<Libro> obtenerLibros(String curso, String clase){

        List<Libro> lista = new ArrayList<>();


        libros.whereEqualTo("Curso", curso).whereEqualTo("Clase", clase).whereEqualTo("Estado", "disponible").get()
                .addOnCompleteListener( (@NonNull Task<QuerySnapshot> task) -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Libro libro = new Libro(document.getId(), document.getString("Asignatura"), document.getString("Clase"), document.getString("Curso"),
                                    document.getString("Donante"),document.getString("Editorial"), document.getString("Estado"), document.getString("Imagen"));

                            listaLibro.add(libro);
                            adapter.notifyDataSetChanged();
                        }
                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(ListadoDispActivity.this,  "Error getting documents: ", Toast.LENGTH_LONG).show();
                    }
                    if(lista.size()!=0){
                        noDisp.setVisibility(View.INVISIBLE);
                        ll4.setVisibility(View.VISIBLE);
                    }else{
                        noDisp.setVisibility(View.VISIBLE);
                        ll4.setVisibility(View.INVISIBLE);
                    }
                });
        return lista;
    }
}