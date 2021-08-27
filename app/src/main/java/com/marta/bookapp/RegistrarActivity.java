package com.marta.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.FirebaseFirestore;

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

        registrar.setOnClickListener( (View v) -> {
            mail = email.getText().toString();
            pass = password.getText().toString();


            if(awesomeValidation.validate()){
                firebaseAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener( (@NonNull Task<AuthResult> task) -> {
                    if(task.isSuccessful()){
                        Toast.makeText(RegistrarActivity.this, "Usuario creado con exito", Toast.LENGTH_SHORT).show();

                        //guardar en la firestore
                        Map<String, Object> user = new HashMap<>();
                        user.put("nombre", nombre.getText().toString());
                        user.put("apellido", apellido.getText().toString());
                        user.put("email",mail);
                        user.put("esAdmin","false");

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

}