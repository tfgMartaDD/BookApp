package com.marta.bookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassActivity extends AppCompatActivity {

    EditText email;
    Button reset, volver;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        String mail = email.getText().toString();
        reset = findViewById(R.id.resetContra);

        if(!mail.isEmpty()){
            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    auth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ResetPassActivity.this,"Correo enviado correctamente." , Toast.LENGTH_SHORT).show();
                                redirigirAmain();
                            }
                        }
                    });
                }
            });
        }

        volver = findViewById(R.id.volverButton);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirigirAmain();
            }
        });
    }

    private void redirigirAmain() {
        Intent i = new Intent (this, MainActivity.class);
        startActivity(i);
    }
}