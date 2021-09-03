package com.marta.bookapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class UsuariosAdapter extends BaseAdapter {

    Context context;
    List<UserLista> lst;

    public UsuariosAdapter(Context context, List<UserLista> lst) {
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

        /*TextView tipoTV;
        TextView asigTV;
        TextView claseCursoTV;
        TextView estadoTV;

        Pendientes p = lst.get(position);

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_pendientes, null);
        }

        tipoTV = convertView.findViewById(R.id.tipoTV);
        asigTV = convertView.findViewById(R.id.asigPendTV);
        claseCursoTV = convertView.findViewById(R.id.clasePendTV);
        estadoTV = convertView.findViewById(R.id.estadoTV);

        String tipo;
        if (p.getEsPeticion()){
            tipo="PETICION";
            tipoTV.setTextColor(Color.argb(200,255,88,41));
        }else{
            tipo="DONACION";

            tipoTV.setTextColor(Color.argb(200, 16,72,2));
        }

        tipoTV.setText(tipo);

        asigTV.setText(p.getAsig());
        String clasecurso = p.getClase() +" " +p.getCurso();
        claseCursoTV.setText(clasecurso);

        String estado = p.getEstado();

        if(estado.equalsIgnoreCase("pendiente")){
            estadoTV.setTextColor(Color.BLUE);
        } else if(estado.equalsIgnoreCase("rechazada")){
            estadoTV.setTextColor(Color.RED);
        }else if(estado.equalsIgnoreCase("aceptada")){
            estadoTV.setTextColor(Color.GREEN);
        }

        estadoTV.setText(estado);*/

        return convertView;
    }
}