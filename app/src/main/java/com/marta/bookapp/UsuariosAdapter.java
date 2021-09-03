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

        TextView emailTV;
        TextView nombreTV;
        TextView numDonTV;
        TextView numPresTV;

        UserLista u = lst.get(position);

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_usuarios, null);
        }

        emailTV = convertView.findViewById(R.id.emailUserTV);
        nombreTV = convertView.findViewById(R.id.nombreUserTV);
        numDonTV = convertView.findViewById(R.id.donacionesUserTV);
        numPresTV = convertView.findViewById(R.id.prestamosUserTV);


        emailTV.setText(u.getId());


        String nombre = u.getNombre() +" " +u.getApellido();
        nombreTV.setText(nombre);

        numDonTV.setText(Integer.toString(u.getDonaciones()));

        numPresTV.setText(Integer.toString(u.getPrestamos()));

        return convertView;
    }
}