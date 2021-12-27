package com.marta.bookapp.Activitys;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.marta.bookapp.Capture;
import com.marta.bookapp.R;

import java.util.Objects;


public class EscaneoActivity extends AppCompatActivity {

    Button scanBTN;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escaneo);

        findViewById(R.id.fondoEscaneo).getBackground().mutate().setAlpha(80);

        scanBTN = findViewById(R.id.scanButton);
        scanBTN.setOnClickListener( (View v) -> {
            IntentIntegrator intentIntegrator = new IntentIntegrator( EscaneoActivity.this);

            intentIntegrator.setPrompt("Utiliza las teclas de volumen para activar el flash");

            intentIntegrator.setBeepEnabled(false);
            intentIntegrator.setOrientationLocked(true);
            //set capture activity
            intentIntegrator.setCaptureActivity(Capture.class);

            intentIntegrator.initiateScan();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Initialize intent result

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        //check condition
        if(intentResult.getContents() != null){

            String user = intentResult.getContents();
            String email = user.replaceAll("http://","");

            db.collection("users").whereEqualTo("email",email).get().addOnCompleteListener((@NonNull Task<QuerySnapshot> task) -> {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot query : Objects.requireNonNull(task.getResult())){
                        Intent in = new Intent (this, UsuarioScanActivity.class);
                        in.putExtra("email",email);
                        startActivity(in);
                    }
                }
            });

        }else{
            Toast.makeText(getApplicationContext(), "No has escaneado nada.", Toast.LENGTH_SHORT).show();
        }
    }
}