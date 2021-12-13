package com.marta.bookapp.Activitys;

import static com.marta.bookapp.BotonesComunes.volverAMenu;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.marta.bookapp.R;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NuevaDonacionActivity extends AppCompatActivity {

    Spinner asigSpin;
    Spinner claseSpin;
    Spinner cursoSpin;
    Spinner editorialSpin;

    TextView imagenTV;

    Button donarBTN, menuBTN;
    ImageView imagen;
    Button anadirImagen;

    Button seleccionarBTN;
    RadioButton defectoRB, galeriaRB;
    LinearLayout llimagen;

    private StorageReference mStorage;
    private static final int GALLERY_INTENT = 1;

    String idPendiente;
    String urlImagen;
    String urlDefecto = "https://firebasestorage.googleapis.com/v0/b/bookapp-3c15f.appspot.com/o/portadas%2Fno-image.png?alt=media&token=0a85d958-6e7a-4aa6-93f7-ef7d241d59de";


    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_donacion);

        mStorage = FirebaseStorage.getInstance().getReference();

        seleccionarBTN= findViewById(R.id.tvanadir);
        defectoRB = findViewById(R.id.defectoRB);
        galeriaRB = findViewById(R.id.galeriaRB);
        llimagen = findViewById(R.id.llimagen);

        imagenTV= findViewById(R.id.tvimagen);

        imagen = findViewById(R.id.imageView3);

        asigSpin = findViewById(R.id.asigSpinner);
        claseSpin = findViewById(R.id.claseSpinner);
        cursoSpin = findViewById(R.id.cursoSpinner);
        editorialSpin = findViewById(R.id.editorialSpinner);

        anadirImagen = findViewById(R.id.anadirBTN);

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


        donarBTN = findViewById(R.id.donarButton);
        donarBTN.setOnClickListener( (View v) -> {
            String claseTemp = claseSpin.getSelectedItem().toString();
            String cursoTemp = cursoSpin.getSelectedItem().toString();

            if( (claseTemp.equalsIgnoreCase("QUINTO") && ((cursoTemp.equalsIgnoreCase("ESO")||((cursoTemp.equalsIgnoreCase("BACHILLERATO"))))))
                    || ((claseTemp.equalsIgnoreCase("SEXTO"))&&((cursoTemp.equalsIgnoreCase("ESO")||((cursoTemp.equalsIgnoreCase("BACHILLERATO"))))))
            || ((cursoTemp.equalsIgnoreCase("BACHILLERATO")) && ((claseTemp.equalsIgnoreCase("TERCERO")) || (claseTemp.equalsIgnoreCase("CUARTO"))))){
                Toast.makeText(NuevaDonacionActivity.this,"DEBE SELECCIONAR UNA CLASE Y CURSOS VÁLIDOS.",Toast.LENGTH_LONG).show();

            }else {
                String asignatura = asigSpin.getSelectedItem().toString();
                String clase = claseSpin.getSelectedItem().toString();
                String curso = cursoSpin.getSelectedItem().toString();
                String editorial = editorialSpin.getSelectedItem().toString();

                prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
                String actualUser = prefs.getString("email", "");

                String frase = "¿Está seguro de que quiere donar el libro de la asignatura " + asignatura + " del curso " + clase + " " + curso + " de la editorial " + editorial + "?";

                AlertDialog.Builder alerta = new AlertDialog.Builder(NuevaDonacionActivity.this);
                alerta.setMessage(frase).setPositiveButton("SI", (DialogInterface dialog, int id) -> {

                    Date date = new Date();

                    Map<String, Object> pendiente = new HashMap<>();
                    pendiente.put("Asignatura", asignatura);
                    pendiente.put("esPeticion","false");
                    pendiente.put("Clase",clase);
                    pendiente.put("Curso",curso);
                    pendiente.put("Estado", "Pendiente");
                    pendiente.put("Usuario",actualUser);

                    db.collection("pendientes").add(pendiente).addOnSuccessListener( (DocumentReference documentReference) ->{
                        idPendiente = documentReference.getId();

                        Map<String, Object> donation = new HashMap<>();
                        donation.put("Asignatura", asignatura);
                        donation.put("Clase", clase);
                        donation.put("Curso", curso);
                        donation.put("Editorial", editorial);
                        donation.put("Usuario", actualUser);
                        donation.put("Fecha", date);
                        donation.put("Imagen", urlImagen);
                        donation.put("idPendiente", idPendiente);

                        db.collection("posiblesDonaciones").document().set(donation).addOnSuccessListener((Void unused) -> {
                            Toast.makeText(NuevaDonacionActivity.this, "DONACION RECIBIDA.\nLos administradores tienen que aprobar la donación. Se pondran en contacto con usted en breve. ", Toast.LENGTH_LONG).show();
                            volverAMenu(NuevaDonacionActivity.this);
                        });
                    });

                }).setNegativeButton("NO", (DialogInterface dialog, int id) -> Toast.makeText(NuevaDonacionActivity.this, "DONACION CANCELADA", Toast.LENGTH_SHORT).show());

                AlertDialog alertDialog = alerta.create();
                alertDialog.setTitle("¿ESTAS SEGURO?");
                alertDialog.show();
            }

        });

        anadirImagen.setOnClickListener( (View v) -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_INTENT);
        });

        menuBTN = findViewById(R.id.menuDonacion);
        menuBTN.setOnClickListener( (View v) -> volverAMenu(NuevaDonacionActivity.this));

    }

    public void comprobarRB(View view){
        if(defectoRB.isChecked()){

            anadirImagen.setVisibility(View.INVISIBLE);

            urlImagen = urlDefecto;
            Glide.with(NuevaDonacionActivity.this)
                    .load(urlDefecto)
                    .into(imagen);
            String frase ="Portada no disponible";
            imagenTV.setText(frase);
            //imagenTV.setVisibility(View.VISIBLE);
            donarBTN.setVisibility(View.VISIBLE);

        }else if(galeriaRB.isChecked()){
            anadirImagen.setVisibility(View.VISIBLE);
            donarBTN.setVisibility(View.VISIBLE);
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


            Glide.with(NuevaDonacionActivity.this)
                    .load(fileUri)
                    .into(imagen);

            String frase = "Imagen de la portada del libro que quiere donar";
            imagenTV.setText(frase);
            //imagenTV.setVisibility(View.VISIBLE);
        }
    }
}