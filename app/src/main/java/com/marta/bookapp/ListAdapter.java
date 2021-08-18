package com.marta.bookapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends BaseAdapter {

    Context context;
    List<Libro> lst;
   //ArrayList<Libro> arrayList;


    public ListAdapter(Context context, List<Libro> lst) {
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
        TextView asignaturaTextV;
        TextView claseTextV;
        TextView editorialTextV;

        Libro l = lst.get(position);

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_libros, null);
        }
        //portada = convertView.findViewById(R.id.portadaImageView);
        asignaturaTextV = convertView.findViewById(R.id.asignaturaTV);
        claseTextV = convertView.findViewById(R.id.claseTV);
        editorialTextV = convertView.findViewById(R.id.editorialTV);

        //portada.setImageResource(l.imagen);
        asignaturaTextV.setText(l.getAsignatura());

        String clasecurso = l.getClase() +" " +l.getCurso();
        claseTextV.setText(clasecurso);
        editorialTextV.setText(l.getEditorial());

        return convertView;
    }
}
