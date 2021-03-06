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
 * Clase que sirve de adaptador para mostrar a un administrador
 * prestamos en un listView.
 *
 * @author Marta Diego u158691@usal.es
 */
public class PrestAdminAdapter extends BaseAdapter {

    Context context;
    List<Prestamo> lst;

    public PrestAdminAdapter(Context context, List<Prestamo> lst) {
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

        TextView usuarioTV;
        TextView asigTV;
        TextView clasecursoTV;
        TextView fechaDevTV;

        Prestamo p = lst.get(position);
        Libro l = p.getLibro();

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_admin_prestamos, null);
        }

        usuarioTV = convertView.findViewById(R.id.usuarioAdmPres);
        asigTV = convertView.findViewById(R.id.asigAdmPres);
        clasecursoTV = convertView.findViewById(R.id.claseAdmPres);
        fechaDevTV = convertView.findViewById(R.id.fechaDevAdmPres);

        usuarioTV.setText(p.getUsuario());
        asigTV.setText(l.getAsignatura());

        String clasecurso = l.getClase() + " " + l.getCurso();
        clasecursoTV.setText(clasecurso);

        fechaDevTV.setText(p.getFechaDev().toString());

        return convertView;
    }
}
