package com.marta.bookapp.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.marta.bookapp.MensajeError;
import com.marta.bookapp.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegistrarActivity extends AppCompatActivity {

    EditText  email, password, nombre, apellido;
    Button registrar;

    String mail, pass;

    private StorageReference mStorage;
    private static final int GALLERY_INTENT = 1;

    FirebaseAuth firebaseAuth;
    AwesomeValidation awesomeValidation;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    String perfilHom = "https://firebasestorage.googleapis.com/v0/b/bookapp-3c15f.appspot.com/o/fotosPerfil%2Ficons8_user_male.png?alt=media&token=e7aa38b8-195c-4d39-8384-1bef39162bcd";
    String perfilMuj = "https://firebasestorage.googleapis.com/v0/b/bookapp-3c15f.appspot.com/o/fotosPerfil%2Ficons8_user_female.png?alt=media&token=6fdfb7c3-05ad-4cb3-b39f-9f8ac3c8f134";
    String defecto = "https://firebasestorage.googleapis.com/v0/b/bookapp-3c15f.appspot.com/o/fotosPerfil%2Fno-image.png?alt=media&token=2f77ddd8-1cc8-456f-9aca-e0dbc1bde9fb";
    String urlPerfil, urlImagen;
    boolean generoFem = false;

    Button seleccionarBTN;
    RadioButton hombreRB, mujerRB, galeriaRB;
    ImageView hombreiv, mujeriv, galeriaiv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        registro();

    }

    private void registro(){

        firebaseAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();

        //Filtros comprobacion email y contraseña
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.emailEditText, Patterns.EMAIL_ADDRESS, R.string.invalid_mail);
        awesomeValidation.addValidation(this, R.id.passwordEditText, ".{6,}" ,R.string.invalid_password);

        registrar = findViewById(R.id.registrar);

        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);
        nombre = findViewById(R.id.nombreEditText);
        apellido = findViewById(R.id.apellidoEditText);

        hombreiv = findViewById(R.id.hombreIV);
        mujeriv = findViewById(R.id.mujerIV);
        galeriaiv = findViewById(R.id.galeriaIV);

        hombreRB = findViewById(R.id.defectoHomRB);
        mujerRB = findViewById(R.id.defectoMujRB);
        galeriaRB = findViewById(R.id.galeriaPerfilRB);

        seleccionarBTN = findViewById(R.id.selecFotoPerfil);

        registrar.setOnClickListener( (View v) -> {
            mail = email.getText().toString();
            pass = password.getText().toString();


            if(awesomeValidation.validate()){
                firebaseAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener( (@NonNull Task<AuthResult> task) -> {
                    if(task.isSuccessful()){
                        FirebaseUser usuario = firebaseAuth.getCurrentUser();
                        if (usuario != null) {
                            usuario.sendEmailVerification();
                        }
                        Toast.makeText(RegistrarActivity.this, "Usuario creado con exito. Verifique su correo", Toast.LENGTH_SHORT).show();

                        Long num = 0L;

                        /*if (generoFem) {
                            urlPerfil = perfilMuj;
                        } else {
                            urlPerfil = perfilHom;
                        }*/

                        System.out.println("HHH\t"+urlImagen);

                        //guardar en la firestore
                        Map<String, Object> user = new HashMap<>();
                        user.put("nombre", nombre.getText().toString());
                        user.put("apellido", apellido.getText().toString());
                        user.put("email",mail);
                        user.put("esAdmin","false");
                        user.put("numDonaciones", num);
                        user.put("numPrestamos",num);
                        user.put("fotoPerfil",urlImagen);

                        db.collection("users").document(mail).set(user);


                        finish();
                    }else {
                        String errorCode = ((FirebaseAuthException) Objects.requireNonNull(task.getException())).getErrorCode();
                        MensajeError.menError(errorCode,RegistrarActivity.this, email, password);
                    }
                });
            }else{
                Toast.makeText(RegistrarActivity.this, "Completa todos los datos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void comprobarRBPerfil(View view){

        if(hombreRB.isChecked()){
            System.out.println("hhh");
            hombreiv.setVisibility(View.VISIBLE);
            mujeriv.setVisibility(View.INVISIBLE);
            galeriaiv.setVisibility(View.INVISIBLE);

            urlImagen = perfilHom;
            Glide.with(RegistrarActivity.this)
                    .load(urlImagen)
                    .into(hombreiv);
            String frase ="Imagen por defecto perfil hombre";
            //imagenTv.setText(frase);

            //anadirBTN.setVisibility(View.VISIBLE);

        }else if(mujerRB.isChecked()){
            hombreiv.setVisibility(View.INVISIBLE);
            mujeriv.setVisibility(View.VISIBLE);
            galeriaiv.setVisibility(View.INVISIBLE);

            urlImagen = perfilMuj;
            Glide.with(RegistrarActivity.this)
                    .load(urlImagen)
                    .into(mujeriv);
            String frase ="Imagen por defecto perfil mujer";

        }else if(galeriaRB.isChecked()){

            hombreiv.setVisibility(View.INVISIBLE);
            mujeriv.setVisibility(View.INVISIBLE);
            galeriaiv.setVisibility(View.VISIBLE);


            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_INTENT);

            /*urlImagen = defecto;
            Glide.with(RegistrarActivity.this)
                    .load(urlImagen)
                    .into(galeriaiv);
            String frase ="Imagen no disponible";*/

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK && data != null){

            Uri fileUri = data.getData();

            StorageReference carpeta = mStorage.child("fotosPerfil");

            StorageReference filePath = carpeta.child("file"+fileUri.getLastPathSegment());

            filePath.putFile(fileUri).addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener( uri -> {
                urlImagen = String.valueOf(uri);
                System.out.println(urlImagen);
            }));

            Glide.with(RegistrarActivity.this)
                    .load(fileUri)
                    .into(galeriaiv);

            //String frase = "Imagen de la portada del libro que quiere donar";
            //imagenTv.setText(frase);
        }
    }

}