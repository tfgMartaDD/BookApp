package com.marta.bookapp.Activitys;

import static com.marta.bookapp.BotonesComunes.volverAMenuAdmin;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.marta.bookapp.R;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LibroAddActivity extends AppCompatActivity {

    Spinner asigSpin, claseSpin, cursoSpin, editorialSpin;

    RadioButton defectoRB, galeriaRB;

    RadioButton rellenarRB, listarRB;
    Button seleccionar;
    LinearLayout ll1, ll2;

    Boolean flag = true;

    LinearLayout llimagen;
    ImageView imagen;
    TextView imagenTv;
    EditText codigoET;

    EditText ETasignaturaR, ETcursoR, ETclaseR, ETeditorialR, ETcodigoR;

    Button anadirBTN, menuBTN, seleccionarPorBTN, galeriaBTN;

    private StorageReference mStorage;
    Uri uriImagen;

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
        seleccionarPorBTN = findViewById(R.id.selecPortada);


        rellenarRB = findViewById(R.id.radioButton);
        listarRB = findViewById(R.id.radioButton2);
        seleccionar = findViewById(R.id.button2);
        ll1 = findViewById(R.id.llLibro);
        ll2 = findViewById(R.id.llLibro_2);

        anadirBTN = findViewById(R.id.anadirLibroBTN);
        menuBTN = findViewById(R.id.volverMenuBTN);
        galeriaBTN = findViewById(R.id.galeriaBTN);

        imagenTv =findViewById(R.id.tvimagen2);
        imagen = findViewById(R.id.portadaImagen);

        asigSpin = findViewById(R.id.spinnerAsigLibro);
        claseSpin = findViewById(R.id.spinnerClaseLibro);
        cursoSpin = findViewById(R.id.spinnerCursoLibro);
        editorialSpin = findViewById(R.id.spinnerEditorialLibro);

        codigoET = findViewById(R.id.codigoET);

        ETasignaturaR = findViewById(R.id.etAsigLibro);
        ETclaseR = findViewById(R.id.etClaseLibro);
        ETcursoR = findViewById(R.id.etCursoLibro);
        ETeditorialR = findViewById(R.id.etEditorialLibro);
        ETcodigoR = findViewById(R.id.codigoET_2);

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

            if(flag){
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

                    String c = String.valueOf(codigoET.getText());
                    long codigo;

                    if(c.equalsIgnoreCase("")){
                        codigo = 0L;
                    }else{
                        codigo = Long.parseLong(c);
                    }
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
                        libro.put("Codigo", codigo);
                        //libro.put("Imagen", urlImagen);

                        if(urlImagen == null){
                            StorageReference carpeta = mStorage.child("portadas").child("libros");
                            StorageReference filePath = carpeta.child(uriImagen.getLastPathSegment());
                            filePath.putFile(uriImagen).addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener( uri -> {
                                urlImagen = String.valueOf(uri);
                                libro.put("Imagen", urlImagen);

                                db.collection("libros").document().set(libro).addOnSuccessListener((Void unused) ->{
                                    Toast.makeText(LibroAddActivity.this, "LIBRO AÑADIDO.\n", Toast.LENGTH_LONG).show();

                                    Intent in = new Intent (LibroAddActivity.this, AdminLibrosActivity.class);
                                    startActivity(in);
                                });
                            }));
                        }else{
                            libro.put("Imagen", urlImagen);

                            db.collection("libros").document().set(libro).addOnSuccessListener((Void unused) ->{
                                Toast.makeText(LibroAddActivity.this, "LIBRO AÑADIDO.\n", Toast.LENGTH_LONG).show();

                                Intent in = new Intent (LibroAddActivity.this, AdminLibrosActivity.class);
                                startActivity(in);
                            });
                        }

                    }).setNegativeButton("NO", (DialogInterface dialog, int id) -> Toast.makeText(LibroAddActivity.this, "ACCION CANCELADA", Toast.LENGTH_SHORT).show());

                    AlertDialog alertDialog = alerta.create();
                    alertDialog.setTitle("AÑADIR LIBRO");
                    alertDialog.show();
                }
            }else{

                String asignaturaR = String.valueOf(ETasignaturaR.getText());
                String claseR = String.valueOf(ETclaseR.getText());
                String cursoR = String.valueOf(ETcursoR.getText());
                String editorialR = String.valueOf(ETeditorialR.getText());

                long codigoR;
                String cod = String.valueOf(ETcodigoR.getText());
                if(cod.equalsIgnoreCase("")){
                    codigoR = 0L;
                }else{
                    codigoR = Long.parseLong(cod);
                }

                String frase2 = "¿Está seguro de que quiere añadir el libro de la asignatura " + asignaturaR + " del curso " + claseR + " " + cursoR + " de la editorial " + editorialR + "?";

                AlertDialog.Builder alerta2 = new AlertDialog.Builder(LibroAddActivity.this);
                alerta2.setMessage(frase2).setPositiveButton("SI", (DialogInterface dialog, int id) -> {

                    Date date = new Date();

                    Map<String, Object> libro2 = new HashMap<>();
                    libro2.put("Asignatura", asignaturaR.toUpperCase(Locale.ROOT));
                    libro2.put("Clase",claseR.toUpperCase(Locale.ROOT));
                    libro2.put("Curso",cursoR.toUpperCase(Locale.ROOT));
                    libro2.put("Editorial", editorialR.toUpperCase(Locale.ROOT));
                    libro2.put("Estado", "disponible");
                    libro2.put("Donante", donante);
                    libro2.put("Fecha", date);
                    libro2.put("Codigo", codigoR);
                    //libro.put("Imagen", urlImagen);

                    if(urlImagen == null){
                        StorageReference carpeta = mStorage.child("portadas").child("libros");
                        StorageReference filePath = carpeta.child(uriImagen.getLastPathSegment());
                        filePath.putFile(uriImagen).addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener( uri -> {
                            urlImagen = String.valueOf(uri);
                            libro2.put("Imagen", urlImagen);

                            db.collection("libros").document().set(libro2).addOnSuccessListener((Void unused) ->{
                                Toast.makeText(LibroAddActivity.this, "LIBRO AÑADIDO.\n", Toast.LENGTH_LONG).show();

                                Intent in = new Intent (LibroAddActivity.this, AdminLibrosActivity.class);
                                startActivity(in);
                            });
                        }));
                    }else{
                        libro2.put("Imagen", urlImagen);

                        db.collection("libros").document().set(libro2).addOnSuccessListener((Void unused) ->{
                            Toast.makeText(LibroAddActivity.this, "LIBRO AÑADIDO.\n", Toast.LENGTH_LONG).show();

                            Intent in = new Intent (LibroAddActivity.this, AdminLibrosActivity.class);
                            startActivity(in);
                        });
                    }

                }).setNegativeButton("NO", (DialogInterface dialog, int id) -> Toast.makeText(LibroAddActivity.this, "ACCION CANCELADA", Toast.LENGTH_SHORT).show());

                AlertDialog alertDialog = alerta2.create();
                alertDialog.setTitle("AÑADIR LIBRO");
                alertDialog.show();

            }
        });

        galeriaBTN.setOnClickListener( (View v) ->  mGetContent.launch("image/*") );

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

    public void comprobarRBSelec(View view){
        if(rellenarRB.isChecked()){
            ll2.setVisibility(View.VISIBLE);
            ll1.setVisibility(View.INVISIBLE);
            flag = false;

        }else if(listarRB.isChecked()){
            ll2.setVisibility(View.INVISIBLE);
            ll1.setVisibility(View.VISIBLE);
            flag = true;
        }
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null){
                        imagen.setImageURI(result);
                        uriImagen = result;
                    }
                }
            });
}
