package com.marta.bookapp;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class EscaneoActivity extends AppCompatActivity {

    Button scanBTN;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escaneo);

        scanBTN = findViewById(R.id.scanButton);
        scanBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator( EscaneoActivity.this);

                intentIntegrator.setPrompt("Utiliza las teclas de volumen para activar el flash");

                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.setOrientationLocked(true);
                //set capture activity
                intentIntegrator.setCaptureActivity(Capture.class);

                intentIntegrator.initiateScan();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Initialize intent result

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        //check condition
        if(intentResult.getContents() != null){
            /*AlertDialog.Builder builder = new AlertDialog.Builder(EscaneoActivity.this);
            builder.setTitle("Resultado del escaneo");
            builder.setMessage(intentResult.getContents());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();*/
            String user = intentResult.getContents();
            String email = user.replaceAll("http://","");
            System.out.println("user: "+user);
            System.out.println("email: "+email);

            db.collection("users").document(email).get().addOnSuccessListener( (DocumentSnapshot documentSnapshot) -> {
                Intent in = new Intent (this,UsuarioScanActivity.class);
                in.putExtra("email",email);
                startActivity(in);
            });

        }else{
            Toast.makeText(getApplicationContext(), "No has escaneado nada.", Toast.LENGTH_SHORT).show();
        }
    }
}