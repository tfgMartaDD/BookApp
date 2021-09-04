package com.marta.bookapp;

import static com.marta.bookapp.BotonesComunes.volverAMenuAdmin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LibroAddActivity extends AppCompatActivity {

    Spinner asigSpin, claseSpin, cursoSpin, editorialSpin;

    RadioButton defectoRB, galeriaRB;

    LinearLayout llimagen;
    ImageView imagen;
    TextView imagenTv;

    Button anadirBTN, menuBTN, seleccionarBTN, galeriaBTN;

    private StorageReference mStorage;
    private static final int GALLERY_INTENT = 1;

    String urlImagen;
    String donante = "COLEGIO";
    String urlDefecto = "https://firebasestorage.googleapis.com/v0/b/bookapp-3c15f.appspot.com/o/portadas%2Fno-image.png?alt=media&token=0a85d958-6e7a-4aa6-93f7-ef7d241d59de";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libro_add);

        mStorage = FirebaseStorage.getInstance().getReference();

        defectoRB = findViewById(R.id.defectoRB2);
        galeriaRB = findViewById(R.id.galeriaRB2);

        seleccionarBTN = findViewById(R.id.selecPortada);
        anadirBTN = findViewById(R.id.anadirLibroBTN);
        menuBTN = findViewById(R.id.volverMenuBTN);
        galeriaBTN = findViewById(R.id.galeriaBTN);

        imagenTv =findViewById(R.id.tvimagen2);
        imagen = findViewById(R.id.portadaImagen);

        asigSpin = findViewById(R.id.spinnerAsigLibro);
        claseSpin = findViewById(R.id.spinnerClaseLibro);
        cursoSpin = findViewById(R.id.spinnerCursoLibro);
        editorialSpin = findViewById(R.id.spinnerEditorialLibro);

        llimagen = findViewById(R.id.llimagen2);

        String [] asignaturas = {"MATEMATICAS", "LENGUA", "BIOLOGIA", "SOCIALES", "INGLES", "FRANCES", "EDUCACION FISICA",
                "PLASTICA", "QUIMICA", "NATURALES", "LATIN"};
        ArrayAdapter<String> asigAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, asignaturas);
        asigSpin.setAdapter(asigAdapter);

        String [] clases = {"PRIMERO","SEGUNDO","TERCERO","CUARTO", "QUINTO", "SEXTO"};
        ArrayAdapter<String> claseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clases);
        claseSpin.setAdapter(claseAdapter);

        String [] cursos = {"PRIMARIA","ESO", "BACHILLERATO"};
        ArrayAdapter<String> cursoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cursos);
        cursoSpin.setAdapter(cursoAdapter);

        String [] editoriales = {"SM", "ANAYA", "EDELVIVES", "SANTILLANA", "OXFORD"};
        ArrayAdapter<String> editorialAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, editoriales);
        editorialSpin.setAdapter(editorialAdapter);


        anadirBTN.setOnClickListener( (View v) -> {
            String claseTemp = claseSpin.getSelectedItem().toString();
            String cursoTemp = cursoSpin.getSelectedItem().toString();

            if( (claseTemp.equalsIgnoreCase("QUINTO") && ((cursoTemp.equalsIgnoreCase("ESO")||((cursoTemp.equalsIgnoreCase("BACHILLERATO"))))))
                    || ((claseTemp.equalsIgnoreCase("SEXTO"))&&((cursoTemp.equalsIgnoreCase("ESO")||((cursoTemp.equalsIgnoreCase("BACHILLERATO"))))))
                    || ((cursoTemp.equalsIgnoreCase("BACHILLERATO")) && ((claseTemp.equalsIgnoreCase("TERCERO")) || (claseTemp.equalsIgnoreCase("CUARTO"))))){
                Toast.makeText(LibroAddActivity.this,"DEBE SELECCIONAR UNA CLASE Y CURSOS VÁLIDOS.",Toast.LENGTH_LONG).show();

            }else {
                String asignatura = asigSpin.getSelectedItem().toString();
                String clase = claseSpin.getSelectedItem().toString();
                String curso = cursoSpin.getSelectedItem().toString();
                String editorial = editorialSpin.getSelectedItem().toString();


                String frase = "¿Está seguro de que quiere añadir el libro de la asignatura " + asignatura + " del curso " + clase + " " + curso + " de la editorial " + editorial + "?";

                AlertDialog.Builder alerta = new AlertDialog.Builder(LibroAddActivity.this);
                alerta.setMessage(frase).setPositiveButton("SI", (DialogInterface dialog, int id) -> {

                    Date date = new Date();

                    Map<String, Object> libro = new HashMap<>();
                    libro.put("Asignatura", asignatura);
                    libro.put("Clase",clase);
                    libro.put("Curso",curso);
                    libro.put("Editorial", editorial);
                    libro.put("Estado", "disponible");
                    libro.put("Donante", donante);
                    libro.put("Fecha", date);
                    libro.put("Imagen", urlImagen);

                    db.collection("libros").document().set(libro).addOnSuccessListener((Void unused) ->{
                        Toast.makeText(LibroAddActivity.this, "LIBRO AÑADIDO.\n", Toast.LENGTH_LONG).show();

                        Intent in = new Intent (LibroAddActivity.this, AdminLibrosActivity.class);
                        startActivity(in);
                    });

                }).setNegativeButton("NO", (DialogInterface dialog, int id) -> Toast.makeText(LibroAddActivity.this, "ACCION CANCELADA", Toast.LENGTH_SHORT).show());

                AlertDialog alertDialog = alerta.create();
                alertDialog.setTitle("AÑADIR LIBRO");
                alertDialog.show();
            }

        });

        galeriaBTN.setOnClickListener( (View v) -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_INTENT);
        });

        menuBTN.setOnClickListener( (View v) -> volverAMenuAdmin(LibroAddActivity.this));

    }

    public void comprobarRBLibro(View view){

        if(defectoRB.isChecked()){
            galeriaBTN.setVisibility(View.INVISIBLE);
            urlImagen = urlDefecto;
            Glide.with(LibroAddActivity.this)
                    .load(urlDefecto)
                    .into(imagen);
            String frase ="Portada no disponible";
            imagenTv.setText(frase);

            anadirBTN.setVisibility(View.VISIBLE);

        }else if(galeriaRB.isChecked()){
            galeriaBTN.setVisibility(View.VISIBLE);
            anadirBTN.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK && data != null){

            Uri fileUri = data.getData();

            StorageReference carpeta = mStorage.child("portadas");

            StorageReference filePath = carpeta.child("file"+fileUri.getLastPathSegment());

            filePath.putFile(fileUri).addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener( uri -> {
                urlImagen = String.valueOf(uri);
                System.out.println(urlImagen);
            }));

            Glide.with(LibroAddActivity.this)
                    .load(fileUri)
                    .into(imagen);

            String frase = "Imagen de la portada del libro que quiere donar";
            imagenTv.setText(frase);
        }
    }
}
