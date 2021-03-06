package com.marta.bookapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.marta.bookapp.Modelo.Libro;
import com.marta.bookapp.R;

import java.util.List;
import java.util.Locale;

/**
 * Clase que sirve de adaptador para mostrar
 * libros en un listView.
 *
 * @author Marta Diego u158691@usal.es
 */
public class ListaLibrosAdapter extends BaseAdapter {

    Context context;
    List<Libro> lst;

    public ListaLibrosAdapter(Context context, List<Libro> lst) {
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

        TextView asignaturaTV;
        TextView claseTV;
        TextView editorialTV;
        TextView estadoTV;

        ImageView portada;

        Libro l = lst.get(position);

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_listalibros, null);
        }
        asignaturaTV = convertView.findViewById(R.id.listaAsig);
        claseTV = convertView.findViewById(R.id.listaClase);
        editorialTV = convertView.findViewById(R.id.listaEditorial);
        estadoTV = convertView.findViewById(R.id.listaEstado);

        portada = convertView.findViewById(R.id.listaPortada);

        asignaturaTV.setText(l.getAsignatura());

        String clasecurso = l.getClase() +" " +l.getCurso();
        claseTV.setText(clasecurso);

        editorialTV.setText(l.getEditorial());

        String estado = l.getEstado();
        if(estado.equalsIgnoreCase("reservado")){
            estadoTV.setTextColor(Color.BLUE);
        } else if(estado.equalsIgnoreCase("prestado")){
            estadoTV.setTextColor(Color.RED);
        }else if(estado.equalsIgnoreCase("disponible")){
            estadoTV.setTextColor(Color.GREEN);
        }

        estadoTV.setText(estado.toUpperCase(Locale.ROOT));

        Glide.with(context)
                .load(l.getImagen())
                .into(portada);

        return convertView;
    }
}
