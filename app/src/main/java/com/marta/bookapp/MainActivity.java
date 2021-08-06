package com.marta.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button accederBTN, registrarBTN, olvideBTN;
    EditText emailET, passwordET;

    AwesomeValidation awesomeValidation;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setup();
        sesion();
    }

    private void setup() {
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        /*if(user != null){
            redirigirAhome();
        }*/

        //Filtros comprobacion email y contraseña
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.emailEditText, Patterns.EMAIL_ADDRESS, R.string.invalid_mail);
        awesomeValidation.addValidation(this, R.id.passwordEditText, ".{6,}" ,R.string.invalid_password);



        accederBTN = findViewById(R.id.cerrarBoton);
        registrarBTN = findViewById(R.id.registrarBoton);
        olvideBTN = findViewById(R.id.olvideBoton);

        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);

        registrarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,RegistrarActivity.class);
                startActivity(i);
            }
        });

        String mail = emailET.getText().toString();
        String pass = passwordET.getText().toString();

        accederBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(mail.isEmpty())&&!(pass.isEmpty())){
                    if(awesomeValidation.validate()){
                    /*String mail = emailET.getText().toString();
                    String pass = passwordET.getText().toString();*/

                        firebaseAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    redirigirAhome(task.getResult().getUser().getEmail());
                                }else{
                                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                                    MensajeError.menError(errorCode,MainActivity.this, emailET,passwordET);
                                }
                            }
                        });
                    }
                }else {
                    Toast.makeText(MainActivity.this, "Rellene los campos de Usuario y Contraseña para continuar", Toast.LENGTH_LONG).show();
                }
            }

        });

        olvideBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void sesion() {

        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String mail = prefs.getString("email", null);

        if(mail != null){
            redirigirAhome(mail);
        }

    }

    private void redirigirAhome(String email) {
        Intent i = new Intent (this, HomeActivity.class);
        i.putExtra("email",email);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

}