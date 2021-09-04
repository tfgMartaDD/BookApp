package com.marta.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


public class BusquedaActivity extends AppCompatActivity {

    TextView asignatura, clase, curso, editorial;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");

        asignatura = findViewById(R.id.busqAsig);
        clase = findViewById(R.id.busqClase);
        curso = findViewById(R.id.busqCurso);
        editorial = findViewById(R.id.busqEdit);

        db.collection("libros").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Libro libro = new Libro(documentSnapshot.getId(), documentSnapshot.getString("Asignatura"), documentSnapshot.getString("Clase"), documentSnapshot.getString("Curso"),
                        documentSnapshot.getString("Donante"), documentSnapshot.getString("Editorial"), documentSnapshot.getString("Estado"), documentSnapshot.getString("Imagen"), documentSnapshot.getString("Tipo") );

                asignatura.setText(documentSnapshot.getString("Asignatura"));
                clase.setText(documentSnapshot.getString("Clase"));
                curso.setText(documentSnapshot.getString("Curso"));
                editorial.setText(documentSnapshot.getString("Editorial"));
            }
        });

    }
}