package com.marta.bookapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class PendientesAdapter extends BaseAdapter {

    Context context;
    List<Pendientes> lst;

    public PendientesAdapter(Context context, List<Pendientes> lst) {
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
        TextView libroTV;
        TextView estadoTV;

        Pendientes p = lst.get(position);

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_pendientes, null);
        }

        tipoTV = convertView.findViewById(R.id.tipoTV);
        libroTV = convertView.findViewById(R.id.libroPendTV);
        estadoTV = convertView.findViewById(R.id.estadoTV);

        String tipo;
        if (p.getEsPeticion()){
            tipo="Peticion";
        }else{
            tipo="Donacion";
        }

        tipoTV.setText(tipo);

        String libro = p.getAsig()+" "+ p.getClase() +" " +p.getCurso();
        libroTV.setText(libro);

        estadoTV.setText(p.getEstado());

        return convertView;
    }
}
