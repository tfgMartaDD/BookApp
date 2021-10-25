package com.marta.bookapp.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.marta.bookapp.R;

public class ResetPassActivity extends AppCompatActivity {

    EditText email;
    Button reset, volver;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.emailEditText2);
        reset = findViewById(R.id.resetContra);

        reset.setOnClickListener( (View v) -> {
            String mail = email.getText().toString();

            //Query query = db.collection("users").whereEqualTo("email",email);

            if(!mail.isEmpty()){
                auth.setLanguageCode("es");
                auth.sendPasswordResetEmail(mail).addOnCompleteListener( (@NonNull Task<Void> task) -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetPassActivity.this,"Correo enviado correctamente." , Toast.LENGTH_SHORT).show();
                        redirigirAmain();
                    }else{
                        Toast.makeText(ResetPassActivity.this,"Debe introducir un correo que haya sido registrado en la app." , Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        volver = findViewById(R.id.volverButton);

        volver.setOnClickListener((View v) ->  redirigirAmain());

    }

    private void redirigirAmain() {
        Intent i = new Intent (this, MainActivity.class);
        startActivity(i);
    }
}