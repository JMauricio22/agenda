package com.example.agenda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import BD.tareas.Tarea;

public class TareasAdapter extends RecyclerView.Adapter<TareasAdapter.ViewHolder> {

    //Interface
    public interface OnItemClickListener {
        void onItemClick (TareasAdapter.ViewHolder holder, int posicion);
    }

    public interface OnRemoveClickListener {
        void onItemClick (Tarea tarea);
    }

    //ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder{

        public View view;
        public TextView tituloTarea;
        public TextView fecha;
        public ImageView btnEliminar;
        public Tarea tarea;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.view = view;
            tituloTarea = (TextView) view.findViewById(R.id.titulo_tarea);
            fecha = (TextView) view.findViewById(R.id.fecha_tarea);
            btnEliminar = (ImageView) view.findViewById(R.id.btn_eliminar_tarea);
        }

        public void bind(final Tarea t , final OnRemoveClickListener removeClickListener){
            this.tarea = t;
            tituloTarea.setText(t.titulo);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(t.fecha);
            fecha.setText(format.format(calendar.getTime()));
            btnEliminar.setOnClickListener( new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    removeClickListener.onItemClick(t);
                }
            });
        }
    }

    private List<Tarea> listaTareas;
    private Context context;
    private OnRemoveClickListener removeClickListener;

    public TareasAdapter(Context context , OnRemoveClickListener removeClickListener){
        this.context = context;
        this.removeClickListener = removeClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tareas , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(listaTareas.get(position) , removeClickListener);
    }

    @Override
    public int getItemCount() {
        if(listaTareas == null)
            return 0;
        return listaTareas.size();
    }

    public void setItems(List<Tarea> nuevaLista){
        listaTareas = nuevaLista;
        notifyDataSetChanged();
    }
}
