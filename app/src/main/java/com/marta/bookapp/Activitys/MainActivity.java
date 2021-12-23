package com.marta.bookapp.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.marta.bookapp.MensajeError;
import com.marta.bookapp.R;


import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button accederBTN, registrarBTN, olvideBTN;
    EditText emailET, passwordET;

    AwesomeValidation awesomeValidation;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();



        if(user != null){
            redirigirAmenu(user.getEmail());
        }

        //Filtros comprobacion email y contraseña
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.emailEditText, Patterns.EMAIL_ADDRESS, R.string.invalid_mail);
        awesomeValidation.addValidation(this, R.id.passwordEditText, ".{6,}" ,R.string.invalid_password);


        registrarBTN = findViewById(R.id.registrarBoton);

        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);

        registrarBTN.setOnClickListener((View view) -> {
            Intent i = new Intent(MainActivity.this, RegistrarActivity.class);
            startActivity(i);
        });



        accederBTN = findViewById(R.id.accederBoton);
        accederBTN.setOnClickListener((View view) -> {

            String mail = emailET.getText().toString();
            String pass = passwordET.getText().toString();

            if(mail.isEmpty() || pass.isEmpty()){
                Toast.makeText(MainActivity.this, "Rellene los campos de Usuario y Contraseña para continuar", Toast.LENGTH_LONG).show();

            }else {

                if(awesomeValidation.validate()){
                    firebaseAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener((@NonNull Task<AuthResult> task) -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user1 =  firebaseAuth.getCurrentUser();
                            if(user1.isEmailVerified()) {
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                CollectionReference users = db.collection("users");
                                users.document(mail).get().addOnSuccessListener((DocumentSnapshot documentSnapshot) -> {
                                    Boolean esAdmin = Boolean.valueOf(documentSnapshot.getString("esAdmin"));
                                    if (esAdmin) {
                                        Intent intent = new Intent(MainActivity.this, AdminMenuActivity.class);
                                        intent.putExtra("email",mail);
                                        startActivity(intent);
                                    } else {
                                        redirigirAmenu(mail);
                                    }
                                });
                            }else{
                                Toast.makeText(this,"No has verificado el correo electronico.", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            String errorCode = ((FirebaseAuthException) Objects.requireNonNull(task.getException())).getErrorCode();
                            MensajeError.menError(errorCode, MainActivity.this, emailET, passwordET);
                        }
                    });

                }
            }
        });


        olvideBTN = findViewById(R.id.olvideBoton);

        olvideBTN.setOnClickListener( (View view) -> {
            Intent intent = new Intent( MainActivity.this,  ResetPassActivity.class);
            startActivity(intent);
        });

        sesion();
    }


    private void sesion() {

        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        String mail = prefs.getString("email", null);

        if(mail != null){
            redirigirAmenu(mail);
        }

    }

    private void redirigirAmenu(String email) {
        Intent in = new Intent (this, MenuActivity.class);
        in.putExtra("email",email);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(in);
    }

}