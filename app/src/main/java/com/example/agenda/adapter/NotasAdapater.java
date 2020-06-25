package com.example.agenda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda.R;

import java.util.ArrayList;
import java.util.List;

import BD.notas.Nota;

public class NotasAdapater extends RecyclerView.Adapter<NotasAdapater.ViewHolder>{

    // Inner class
    public class ViewHolder extends RecyclerView.ViewHolder{

        public View view;
        public TextView titulo;
        public ImageView img;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.view = view;
            titulo = (TextView) view.findViewById(R.id.titulo_nota);
            img = (ImageView) view.findViewById(R.id.btn_eliminar_nota);
        }

        public void bind(Nota nota){
            titulo.setText(nota.titulo);
        }
    }

    //Fields
    private List<Nota> noteList;
    private List<Nota> filterList;
    private LayoutInflater inflater;

    public NotasAdapater(Context context){
        noteList = new ArrayList<>();
        filterList = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_notas , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(filterList.get(position));
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public void setItems(List<Nota> newList){

        noteList.clear();

        filterList.clear();

        noteList.addAll(newList);

        filterList.addAll(newList);

        notifyDataSetChanged();
    }

}
