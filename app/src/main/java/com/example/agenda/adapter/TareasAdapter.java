package com.example.agenda.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import BD.tareas.Tarea;

public class TareasAdapter extends RecyclerView.Adapter<TareasAdapter.ViewHolder> implements Filterable{

    //Interface
    public interface OnItemClickListener {
        void onItemClick (TareasAdapter.ViewHolder holder, int posicion);
    }

    public interface OnRemoveClickListener {
        void onItemClick (Tarea tarea);
    }

    //Inner Class
    public class ViewHolder extends RecyclerView.ViewHolder{

        public View view;
        public TextView tituloTarea;
        public TextView fecha;
        public ImageView btnEliminar;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.view = view;
            tituloTarea = (TextView) view.findViewById(R.id.titulo_tarea);
            fecha = (TextView) view.findViewById(R.id.fecha_tarea);
            btnEliminar = (ImageView) view.findViewById(R.id.btn_eliminar_tarea);
        }

        public void bind(final Tarea tarea , final OnRemoveClickListener removeClickListener){

            tituloTarea.setText(tarea.titulo);

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");

            Calendar calendar = Calendar.getInstance();

            calendar.setTimeInMillis(tarea.fecha);

            fecha.setText(format.format(calendar.getTime()));

            btnEliminar.setOnClickListener( new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    removeClickListener.onItemClick(tarea);
                }
            });
        }
    }

    public class CustomFilter extends Filter{

        public CustomFilter(){
            super();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.i("QueryAdapter" , constraint.toString());

            filterList.clear();

            FilterResults filterResults = new FilterResults();


            if(constraint.toString().length() == 0 || constraint.toString().equals("")) {

                filterList.addAll(taskList);

                return filterResults;
            }

            for(Tarea tarea: taskList){
                if(tarea.titulo.contains(constraint.toString())) {
                    filterList.add(tarea);
                }
            }

            filterResults.values = filterList;
            filterResults.count = filterList.size();

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            self.notifyDataSetChanged();
        }
    }

    //Fields
    private List<Tarea> taskList;
    private List<Tarea> filterList;
    private Context context;
    private OnRemoveClickListener removeClickListener;
    public Filter filter;
    private TareasAdapter self = this;

    public TareasAdapter(Context context , OnRemoveClickListener removeClickListener){
        this.context = context;
        this.removeClickListener = removeClickListener;
        taskList = new ArrayList<>();
        filterList = new ArrayList<>();
        filter = new CustomFilter();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tareas , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(filterList.get(position) , removeClickListener);
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public void setItems(List<Tarea> newList){
        taskList.addAll(newList);
        filterList.addAll(taskList);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter(){
        return filter;
    }
}
