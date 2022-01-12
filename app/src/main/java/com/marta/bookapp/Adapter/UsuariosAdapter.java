package com.marta.bookapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.marta.bookapp.R;
import com.marta.bookapp.Modelo.Usuario;

import java.util.List;

/**
 * Clase que sirve de adaptador para mostrar
 * usuarios en un listView.
 *
 * @author Marta Diego u158691@usal.es
 */
public class UsuariosAdapter extends BaseAdapter {

    Context context;
    List<Usuario> lst;

    public UsuariosAdapter(Context context, List<Usuario> lst) {
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

        Usuario u = lst.get(position);

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

        numDonTV.setText(Long.toString(u.getDonaciones()));

        numPresTV.setText(Long.toString(u.getPrestamos()));

        return convertView;
    }
}