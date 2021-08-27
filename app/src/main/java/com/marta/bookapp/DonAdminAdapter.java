package com.marta.bookapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DonAdminAdapter extends BaseAdapter {

    Context context;
    List<DonacionPeticion> lst;

    public DonAdminAdapter(Context context, List<DonacionPeticion> lst) {
        this.context = context;
        this.lst = lst;
    }

    @Override
    public int getCount() {
        return lst.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        TextView libroTV;
        TextView userTV;
        TextView fechaTV;

        DonacionPeticion d = lst.get(position);
        Libro l = d.getLibro();

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_admin_donaciones, null);
        }

        libroTV = convertView.findViewById(R.id.libroDon);
        userTV = convertView.findViewById(R.id.usuarioDon);
        fechaTV = convertView.findViewById(R.id.fechaDon);


        String libro = l.getAsignatura()+" " +l.getClase() +" " +l.getCurso()+" " +l.getEditorial();
        libroTV.setText(libro);


        userTV.setText(d.getEmailUsuario());
        String fecha = String.valueOf(d.getFecha());
        fechaTV.setText(fecha);

        return convertView;
    }
}
