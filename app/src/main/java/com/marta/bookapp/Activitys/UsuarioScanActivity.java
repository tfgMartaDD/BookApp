package com.marta.bookapp.Activitys;

import static com.marta.bookapp.BotonesComunes.volverAMenuAdmin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.marta.bookapp.R;

/**
 * Clase para mostrar al administrador todos los datos de perfil
 * que hay de un usuario en concreto.
 * Desde aqui se puede hacer administrador a otro usuario.
 *
 * @author Marta Diego u158691@usal.es
 */
public class UsuarioScanActivity extends AppCompatActivity {

    TextView nombreET, apellidoET, emailTV;
    Button  volverBTN;
    Button adminBTN;

    ImageView perfil;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    String urlDefecto = "https://firebasestorage.googleapis.com/v0/b/bookapp-3c15f.appspot.com/o/fotosPerfil%2Fno-image.png?alt=media&token=2f77ddd8-1cc8-456f-9aca-e0dbc1bde9fb";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_scan);

        findViewById(R.id.fondoUsuario).getBackground().mutate().setAlpha(80);

        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");

        perfil = findViewById(R.id.imagenPerfil);

        volverBTN = findViewById(R.id.buttonVolver);
        volverBTN.setOnClickListener( (View v) -> volverAMenuAdmin(UsuarioScanActivity.this) );

        nombreET = findViewById(R.id.nombretv);
        apellidoET = findViewById(R.id.apellidotv);
        emailTV = findViewById(R.id.correoTV);

        db.collection("users").document(email).get().addOnSuccessListener((DocumentSnapshot documentSnapshot) -> {
            nombreET.setText(documentSnapshot.getString("nombre"));
            apellidoET.setText(documentSnapshot.getString("apellido"));
            emailTV.setText(documentSnapshot.getString("email"));

            String foto = documentSnapshot.getString("fotoPerfil");
            if(foto == null){
                Glide.with(UsuarioScanActivity.this)
                        .load(urlDefecto)
                        .into(perfil);

            }else{
                Glide.with(UsuarioScanActivity.this)
                        .load(foto)
                        .into(perfil);
            }
        });


        adminBTN = findViewById(R.id.adminButton);
        adminBTN.setOnClickListener( (View v)  -> {

            String frase = "¿Estás seguro de que desea hacer que "+email+" sea administrador?";
            AlertDialog.Builder alerta = new AlertDialog.Builder(UsuarioScanActivity.this);
            alerta.setMessage(frase).setPositiveButton("ACEPTAR",  (DialogInterface dialog, int id) -> {
                db.collection("users").document(email).update("esAdmin", "true");
                Toast.makeText(this, "Ahora "+email+" también es administrador en la app.", Toast.LENGTH_LONG).show();

            }).setNegativeButton("RECHAZAR",  (DialogInterface dialog, int id) ->   {
                dialog.dismiss();
                Toast.makeText(this, "ACCIÓN CANCELADA", Toast.LENGTH_SHORT).show();
            } );

            AlertDialog alertDialog = alerta.create();
            alertDialog.setTitle("NUEVO ADMINISTRADOR DE LA APP");
            alertDialog.show();

        });


    }
}