package com.marta.bookapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.marta.bookapp.Modelo.Libro;
import com.marta.bookapp.R;

import java.util.List;

/**
 * Clase que sirve de adaptador para mostrar
 * prestamos y donaciones de un usuario en un listView.
 *
 * @author Marta Diego u158691@usal.es
 */
public class ResumenAdapter extends BaseAdapter {

    Context context;
    List<Libro> lst;

    public ResumenAdapter(Context context, List<Libro> lst) {
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

        TextView tipoTV;
        TextView asigTV;
        TextView claseTV;
        TextView cursoTV;


        Libro l = lst.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_resumen, null);
        }

        tipoTV = convertView.findViewById(R.id.tvtipo);
        asigTV = convertView.findViewById(R.id.tvasignaturaRes);
        claseTV = convertView.findViewById(R.id.tvclaseRes);
        cursoTV = convertView.findViewById(R.id.tvcursoRes);

        String tipo = l.getTipo();

        if(tipo.equalsIgnoreCase("donacion")){
            tipoTV.setTextColor(Color.MAGENTA);
        }else if(tipo.equalsIgnoreCase("prestamo")){
            tipoTV.setTextColor(Color.CYAN);
        }
        tipoTV.setText(tipo);

        asigTV.setText(l.getAsignatura());
        claseTV.setText(l.getClase());
        cursoTV.setText(l.getCurso());

        return convertView;
    }
}