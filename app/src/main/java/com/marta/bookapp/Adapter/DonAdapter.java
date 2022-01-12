package com.marta.bookapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.marta.bookapp.Modelo.DonacionPeticion;
import com.marta.bookapp.Modelo.Libro;
import com.marta.bookapp.R;

import java.util.List;

/**
 * Clase que sirve de adaptador para mostrar
 * donaciones en un listView.
 *
 * @author Marta Diego u158691@usal.es
 */
public class DonAdapter extends BaseAdapter {

    Context context;
    List<DonacionPeticion> lst;

    public DonAdapter(Context context, List<DonacionPeticion> lst) {
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
        TextView libroTV;
        //TextView userTV;
        TextView fechaTV;

        DonacionPeticion d = lst.get(position);
        Libro l = d.getLibro();

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_donaciones, null);
        }
        //portada = convertView.findViewById(R.id.portadaImageView);
        libroTV = convertView.findViewById(R.id.libroTV);
        //userTV = convertView.findViewById(R.id.userTV);
        fechaTV = convertView.findViewById(R.id.fechaTV);

        //portada.setImageResource(l.imagen);
        String libro = l.getAsignatura()+" " +l.getClase() +" " +l.getCurso()+" " +l.getEditorial();
        libroTV.setText(libro);


        //userTV.setText(d.getEmailUsuario());
        fechaTV.setText(d.getFecha().toString());

        return convertView;
    }
}
