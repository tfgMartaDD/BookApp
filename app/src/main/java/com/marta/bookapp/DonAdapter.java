package com.marta.bookapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DonAdapter extends BaseAdapter {

    Context context;
    List<Donacion> lst;

    public DonAdapter(Context context, List<Donacion> lst) {
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

        //ImageView portada;
        TextView asignaturaTV;
        TextView donanteTV;
        TextView claseTV;

        Donacion d = lst.get(position);

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_libros, null);
        }
        //portada = convertView.findViewById(R.id.portadaImageView);
        /*asignaturaTV = convertView.findViewById(R.id.asignaturaTV);
        claseTextV = convertView.findViewById(R.id.claseTV);
        editorialTextV = convertView.findViewById(R.id.editorialTV);

        //portada.setImageResource(l.imagen);
        asignaturaTextV.setText(l.getAsignatura());

        String clasecurso = l.getClase() +" " +l.getCurso();
        claseTextV.setText(clasecurso);
        editorialTextV.setText(l.getEditorial());*/

        return convertView;
    }
}
