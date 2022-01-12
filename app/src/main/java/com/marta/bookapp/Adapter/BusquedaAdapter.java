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

/**
 * Clase que sirve de adaptador para mostrar los libros
 * buscados en un listView.
 *
 * @author Marta Diego u158691@usal.es
 */
public class BusquedaAdapter extends BaseAdapter {

    Context context;
    List<Libro> lst;

    public BusquedaAdapter(Context context, List<Libro> lst) {
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

        ImageView portada;
        TextView estadoTV;
        TextView userTV;
        TextView fechaTV;
        TextView tipoTV;

        Libro l = lst.get(position);

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_busqueda, null);
        }

        portada = convertView.findViewById(R.id.imageBusqueda);
        estadoTV = convertView.findViewById(R.id.estadoBusqueda);
        userTV = convertView.findViewById(R.id.userBusqueda);
        fechaTV = convertView.findViewById(R.id.fechaBusqueda);
        tipoTV = convertView.findViewById(R.id.tipoBusqueda);

        Glide.with(context)
                .load(l.getImagen())
                .into(portada);

        String estado = l.getEstado();
        System.out.println("estado"+estado);
        if(estado.equalsIgnoreCase("reservado")){
            estadoTV.setTextColor(Color.BLUE);

            String r = "RESERVADO POR";
            tipoTV.setText(r);
        } else if(estado.equalsIgnoreCase("prestado")){
            estadoTV.setTextColor(Color.RED);

            String r = "PRESTADO A";
            tipoTV.setText(r);
        }else if(estado.equalsIgnoreCase("disponible")){
            estadoTV.setTextColor(Color.GREEN);

            String r = "DONADO POR";
            tipoTV.setText(r);
        }
        estadoTV.setText(estado);

        userTV.setText(l.getDonante());
        String fecha = String.valueOf(l.getFecha());
        fechaTV.setText(fecha);

        return convertView;
    }
}
