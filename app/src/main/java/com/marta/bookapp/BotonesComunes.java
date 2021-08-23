package com.marta.bookapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class BotonesComunes {

    public static void cerrarSesion(Context context){

        String prefs_file = "com.marta.bookapp.PREFERENCE_FILE_KEY";

        //Borrado de datos del usuario actual
        SharedPreferences prefs  = context.getSharedPreferences(prefs_file, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        FirebaseAuth.getInstance().signOut();
        Toast.makeText(context, "Sesión cerrada correctamente.", Toast.LENGTH_SHORT).show();

        Intent i = new Intent (context,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public static void volverAMenu(Context context){
        Intent in = new Intent (context,MenuActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(in);
    }


}
