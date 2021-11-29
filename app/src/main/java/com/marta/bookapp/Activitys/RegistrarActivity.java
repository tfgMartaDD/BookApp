package com.marta.bookapp.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.marta.bookapp.MensajeError;
import com.marta.bookapp.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegistrarActivity extends AppCompatActivity {

    EditText  email, password, nombre, apellido;
    Button registrar;

    String mail, pass;

    FirebaseAuth firebaseAuth;
    AwesomeValidation awesomeValidation;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    String perfilHom = "gs://bookapp-3c15f.appspot.com/fotosPerfil/icons8_user_male.png";
    String perfilMuj = "gs://bookapp-3c15f.appspot.com/fotosPerfil/icons8_user_female.png";
    String defecto = "gs://bookapp-3c15f.appspot.com/fotosPerfil/icono_hombre.png";
    String urlPerfil, urlImagen;
    boolean generoFem = false;

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

                        if (generoFem) {
                            urlPerfil = perfilMuj;
                        } else {
                            urlPerfil = perfilHom;
                        }

                        //guardar en la firestore
                        Map<String, Object> user = new HashMap<>();
                        user.put("nombre", nombre.getText().toString());
                        user.put("apellido", apellido.getText().toString());
                        user.put("email",mail);
                        user.put("esAdmin","false");
                        user.put("numDonaciones", num);
                        user.put("numPrestamos",num);
                        user.put("fotoPerfil",urlPerfil);

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

            urlImagen = defecto;
            Glide.with(RegistrarActivity.this)
                    .load(urlImagen)
                    .into(galeriaiv);
            String frase ="Imagen no disponible";

        }
    }

}