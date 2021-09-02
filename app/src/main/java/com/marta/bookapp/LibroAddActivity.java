package com.marta.bookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class LibroAddActivity extends AppCompatActivity {

    Spinner asigSpin, claseSpin, cursoSpin, editorialSpin;

    RadioButton defectoRB, galeriaRB;

    LinearLayout llimagen;
    ImageView imagen;
    TextView imagenTv;

    Button anadirBTN, menuBTN, seleccionarBTN, galeriaBTN;

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
        setContentView(R.layout.activity_libro_add);

        mStorage = FirebaseStorage.getInstance().getReference();

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

    }

    public void comprobarRBLibro(View view){
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
}
