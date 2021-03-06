package com.marta.bookapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.marta.bookapp.Activitys.AdminMenuActivity;
import com.marta.bookapp.Activitys.MainActivity;
import com.marta.bookapp.Activitys.MenuActivity;

/**
 * CLase con algunos métodos utilizados por varias clases
 * para facilitar la reutilización.
 *
 * @author Marta Diego u158691@usal.es
 */
public class BotonesComunes {

    static String prefs_file = "com.marta.bookapp.PREFERENCE_FILE_KEY";

    /**
     * Método por el que se cierra sesión en el sistema y
     * se borran los datos de sesión que se estaban guardando.
     * @param context vista en la que está
     */
    public static void cerrarSesion(Context context){

        //Borrado de datos del usuario actual
        SharedPreferences prefs  = context.getSharedPreferences(prefs_file, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        FirebaseAuth.getInstance().signOut();
        Toast.makeText(context, "Sesión cerrada correctamente.", Toast.LENGTH_SHORT).show();

        Intent i = new Intent (context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    /**
     * Método para redirigir al menu principal
     * @param context vista en la que está
     */
    public static void volverAMenu(Context context){

        SharedPreferences prefs = context.getSharedPreferences(prefs_file, Context.MODE_PRIVATE);
        String email = prefs.getString("email","");

        Intent in = new Intent (context, MenuActivity.class);
        in.putExtra("email",email);
        context.startActivity(in);
    }

    /**
     * Método para redirigir al menu principal de administradores
     * @param context vista en la que está
     */
    public static void volverAMenuAdmin(Context context){
        SharedPreferences prefs = context.getSharedPreferences(prefs_file, Context.MODE_PRIVATE);
        String email = prefs.getString("email","");

        Intent in = new Intent (context, AdminMenuActivity.class);
        in.putExtra("email",email);
        context.startActivity(in);
    }


}
