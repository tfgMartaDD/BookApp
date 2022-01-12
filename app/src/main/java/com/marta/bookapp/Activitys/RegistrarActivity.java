package com.marta.bookapp.Activitys;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

/**
 * Clase  en la que un usuario encontrará el formulario para
 * poder registrarse en el sistema.
 *
 * @author Marta Diego u158691@usal.es
 */
public class RegistrarActivity extends AppCompatActivity {

    EditText  email, password, password2, nombre, apellido;

    Button registrar;

    String mail, pass, pass2;

    private StorageReference mStorage;

    FirebaseAuth firebaseAuth;
    AwesomeValidation awesomeValidation;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    String perfilHom = "https://firebasestorage.googleapis.com/v0/b/bookapp-3c15f.appspot.com/o/fotosPerfil%2Ficons8_user_male.png?alt=media&token=e7aa38b8-195c-4d39-8384-1bef39162bcd";
    String perfilMuj = "https://firebasestorage.googleapis.com/v0/b/bookapp-3c15f.appspot.com/o/fotosPerfil%2Ficons8_user_female.png?alt=media&token=6fdfb7c3-05ad-4cb3-b39f-9f8ac3c8f134";

    Boolean flag = true;

    String urlImagen;
    Uri uriImagen;

    Button seleccionarBTN;
    RadioButton hombreRB, mujerRB, galeriaRB;
    ImageView hombreiv, mujeriv, galeriaiv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        findViewById(R.id.fondoRegistrar).getBackground().mutate().setAlpha(80);


        registro();

    }

    /**
     * Método donde se encuentra toda la lógica necesaria para el registro
     * de un usuario en el sistema.
     */
    private void registro(){

        firebaseAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();

        //Filtros comprobacion email y contraseña
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this, R.id.emailEditText, Patterns.EMAIL_ADDRESS, R.string.error_email);

        // to validate the confirmation of another field
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(this, R.id.passwordEditText, regexPassword, R.string.error_pass);
        // to validate a confirmation field (don't validate any rule other than confirmation on confirmation field)
        awesomeValidation.addValidation(this, R.id.passwordEditText2, R.id.passwordEditText, R.string.error_pass2);

        /*awesomeValidation.addValidation(this,R.id.emailEditText, Patterns.EMAIL_ADDRESS, R.string.invalid_mail);
        awesomeValidation.addValidation(this, R.id.passwordEditText, ".{6,}" ,R.string.invalid_password);
*/
        registrar = findViewById(R.id.registrar);

        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);
        password2 = findViewById(R.id.passwordEditText2);
        nombre = findViewById(R.id.nombreEditText);
        apellido = findViewById(R.id.apellidoEditText);


        hombreiv = findViewById(R.id.hombreIV);
        mujeriv = findViewById(R.id.mujerIV);
        galeriaiv = findViewById(R.id.galeriaIV);

        hombreRB = findViewById(R.id.defectoHomRB);
        mujerRB = findViewById(R.id.defectoMujRB);
        galeriaRB = findViewById(R.id.galeriaPerfilRB);

        seleccionarBTN = findViewById(R.id.selecFotoPerfil);


        password.setOnClickListener( (View v) ->
                Toast.makeText(this, getString(R.string.error_pass), Toast.LENGTH_LONG).show());

        registrar.setOnClickListener( (View v) -> {
            mail = email.getText().toString();
            pass = password.getText().toString();
            pass2 = password2.getText().toString();

            //if (pass.equalsIgnoreCase(pass2)){
                if(awesomeValidation.validate()){
                    firebaseAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener( (@NonNull Task<AuthResult> task) -> {
                        if(task.isSuccessful()){
                            FirebaseUser usuario = firebaseAuth.getCurrentUser();
                            if (usuario != null) {
                                usuario.sendEmailVerification();
                            }
                            Toast.makeText(RegistrarActivity.this, "Usuario creado con exito. Verifique su correo", Toast.LENGTH_SHORT).show();

                            long num = 0L;

                            //guardar en la firestore
                            Map<String, Object> user = new HashMap<>();
                            user.put("nombre", nombre.getText().toString());
                            user.put("apellido", apellido.getText().toString());
                            user.put("email",mail);
                            user.put("esAdmin","false");
                            user.put("numDonaciones", num);
                            user.put("numPrestamos", num);


                            if(flag){
                                urlImagen = "";
                                db.collection("users").document(mail).set(user);
                            }else{
                                if(urlImagen == null){
                                    StorageReference carpeta = mStorage.child("fotosPerfil").child("archivos");
                                    StorageReference filePath = carpeta.child(uriImagen.getLastPathSegment());
                                    filePath.putFile(uriImagen).addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener( uri -> {
                                        urlImagen = String.valueOf(uri);

                                        user.put("fotoPerfil",urlImagen);
                                        db.collection("users").document(mail).set(user);
                                    }));
                                }else{

                                    user.put("fotoPerfil",urlImagen);
                                    db.collection("users").document(mail).set(user);
                                }
                            }

                            finish();
                        }else {
                            String errorCode = ((FirebaseAuthException) Objects.requireNonNull(task.getException())).getErrorCode();
                            MensajeError.menError(errorCode,RegistrarActivity.this, email, password);
                        }
                    });
                }else{
                    Toast.makeText(RegistrarActivity.this, "Completa todos los datos.", Toast.LENGTH_SHORT).show();
                }
            //} else {
                //Toast.makeText(RegistrarActivity.this, "La contraseña no es correcta. \n Tiene que ser igual en los dos campos, intentelo de nuevo.", Toast.LENGTH_LONG).show();
            //}

        });

        seleccionarBTN.setOnClickListener( (View v) -> {
            flag = false;
            comprobarRBPerfil(v);
        });
    }

    /**
     * Método en el que se comprueba que Radio Button se ha seleccionado
     * para elegir la foto de perfil desde la galeria o una por defecto.
     *
     * @param view Vista en la que está.
     */
    public void comprobarRBPerfil(View view){

        if(hombreRB.isChecked()){
            hombreiv.setVisibility(View.VISIBLE);
            mujeriv.setVisibility(View.INVISIBLE);
            galeriaiv.setVisibility(View.INVISIBLE);

            urlImagen = perfilHom;
            Glide.with(RegistrarActivity.this)
                    .load(urlImagen)
                    .into(hombreiv);

        }else if(mujerRB.isChecked()){
            hombreiv.setVisibility(View.INVISIBLE);
            mujeriv.setVisibility(View.VISIBLE);
            galeriaiv.setVisibility(View.INVISIBLE);

            urlImagen = perfilMuj;
            Glide.with(RegistrarActivity.this)
                    .load(urlImagen)
                    .into(mujeriv);

        }else if(galeriaRB.isChecked()){

            hombreiv.setVisibility(View.INVISIBLE);
            mujeriv.setVisibility(View.INVISIBLE);
            galeriaiv.setVisibility(View.VISIBLE);

            mGetContent.launch("image/*");

        }
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null){
                        galeriaiv.setImageURI(result);
                        uriImagen = result;
                    }
                }
            });

}