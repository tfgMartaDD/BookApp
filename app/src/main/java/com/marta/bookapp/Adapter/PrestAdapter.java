package com.marta.bookapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.marta.bookapp.Modelo.Libro;
import com.marta.bookapp.Modelo.Prestamo;
import com.marta.bookapp.R;

import java.util.List;

/**
 * Clase que sirve de adaptador para mostrar
 * prestamos en un listView.
 *
 * @author Marta Diego u158691@usal.es
 */
public class PrestAdapter extends BaseAdapter {

    Context context;
    List<Prestamo> lst;

    public PrestAdapter(Context context, List<Prestamo> lst) {
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

        TextView asigTV;
        TextView clasecursoTV;
        TextView fechaTV;
        TextView fechaDevTV;

        Prestamo p = lst.get(position);
        Libro l = p.getLibro();

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_prestamos, null);
        }
        asigTV = convertView.findViewById(R.id.asigtv);
        clasecursoTV = convertView.findViewById(R.id.clasecurso);
        fechaTV = convertView.findViewById(R.id.fechaTextv);
        fechaDevTV = convertView.findViewById(R.id.fechaDevTextv);

        asigTV.setText(l.getAsignatura());

        String clasecurso = l.getClase() +" " +l.getCurso();
        clasecursoTV.setText(clasecurso);

        fechaTV.setText(p.getFecha().toString());
        fechaDevTV.setText(p.getFechaDev().toString());

        return convertView;
    }
}
