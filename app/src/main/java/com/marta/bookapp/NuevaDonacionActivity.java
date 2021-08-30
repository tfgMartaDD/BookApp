package com.marta.bookapp;

import static com.marta.bookapp.BotonesComunes.volverAMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NuevaDonacionActivity extends AppCompatActivity {

    Spinner asigSpin;
    Spinner claseSpin;
    Spinner cursoSpin;
    Spinner editorialSpin;
    TextView libroTextView;

    Button donarBTN, menuBTN;
    Button anadirImagen;

    private StorageReference mStorage;
    private static final int GALLERY_INTENT = 1;


    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_donacion);

        mStorage = FirebaseStorage.getInstance().getReference();

        libroTextView = findViewById(R.id.libroTV);

        asigSpin = findViewById(R.id.asigSpinner);
        claseSpin = findViewById(R.id.claseSpinner);
        cursoSpin = findViewById(R.id.cursoSpinner);
        editorialSpin = findViewById(R.id.editorialSpinner);

        anadirImagen = findViewById(R.id.anadirBTN);

        String [] asignaturas = {"MATEMATICAS", "LENGUA", "BIOLOGIA", "SOCIALES", "INGLES"};
        ArrayAdapter<String> asigAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, asignaturas);
        asigSpin.setAdapter(asigAdapter);

        String [] clases = {"PRIMERO","SEGUNDO","TERCERO","CUARTO", "QUINTO", "SEXTO"};
        ArrayAdapter<String> claseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clases);
        claseSpin.setAdapter(claseAdapter);

        String [] cursos = {"PRIMARIA","ESO", "BACHILLERATO"};
        ArrayAdapter<String> cursoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cursos);
        cursoSpin.setAdapter(cursoAdapter);

        String [] editoriales = {"SM","ANAYA"};
        ArrayAdapter<String> editorialAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, editoriales);
        editorialSpin.setAdapter(editorialAdapter);


        donarBTN = findViewById(R.id.donarButton);
        donarBTN.setOnClickListener( (View v) -> {
            String asignatura = asigSpin.getSelectedItem().toString();
            String clase = claseSpin.getSelectedItem().toString();
            String curso = cursoSpin.getSelectedItem().toString();
            String editorial = editorialSpin.getSelectedItem().toString();

            prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
            String actualUser = prefs.getString("email","");

            Date date = new Date();

            Map<String, Object> donation = new HashMap<>();
            donation.put("Asignatura", asignatura);
            donation.put("Clase", clase);
            donation.put("Curso", curso);
            donation.put("Editorial", editorial);
            donation.put("Usuario", actualUser);
            donation.put("Fecha",date);

            String frase = "¿Está seguro de que quiere donar el libro de la asignatura "+ asignatura +" del curso "+clase +" " +curso+" de la editorial "+ editorial + "?";


            AlertDialog.Builder alerta = new AlertDialog.Builder(NuevaDonacionActivity.this);
            alerta.setMessage(frase).setPositiveButton("SI",  (DialogInterface dialog, int id) -> {
                db.collection("posiblesDonaciones").document().set(donation).addOnSuccessListener( (Void unused) -> {
                    Toast.makeText(NuevaDonacionActivity.this, "DONACION RECIBIDA.\nLos administradores tienen que aprobar la donación. Se pondran en contacto con usted en breve. ", Toast.LENGTH_LONG).show();
                    volverAMenu(NuevaDonacionActivity.this);
                });

            }).setNegativeButton("NO",  (DialogInterface dialog, int id) ->  Toast.makeText(NuevaDonacionActivity.this, "DONACION CANCELADA", Toast.LENGTH_SHORT).show());

            AlertDialog alertDialog = alerta.create();
            alertDialog.setTitle("¿ESTAS SEGURO?");
            alertDialog.show();

        });

        anadirImagen.setOnClickListener( (View v) -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_INTENT);
        });

        menuBTN = findViewById(R.id.menuDonacion);
        menuBTN.setOnClickListener( (View v) -> volverAMenu(NuevaDonacionActivity.this));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            Uri uri = data.getData();

            StorageReference filePath = mStorage.child("portadas").child(uri.getLastPathSegment());

            filePath.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Toast.makeText(NuevaDonacionActivity.this, "Imagen subida correctamente ", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }
}