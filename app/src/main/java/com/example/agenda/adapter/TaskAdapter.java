package com.example.agenda.adapter;

import android.app.AlertDialog;
import android.content.Context;
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

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> implements Filterable{

    //Interface
    public interface OnItemClickListener{
        void onItemClick( Tarea tarea);
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

        public void bind(final Tarea tarea , final OnRemoveClickListener removeClickListener , final OnItemClickListener clickListener){

            tituloTarea.setText(tarea.titulo);

            SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d, yyyy hh:mm a");

            Calendar calendar = Calendar.getInstance();

            calendar.setTimeInMillis(tarea.fecha);

            fecha.setText(format.format(calendar.getTime()));

            btnEliminar.setOnClickListener( new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    removeClickListener.onItemClick(tarea);
                }
            });

            view.setOnClickListener( new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick( tarea );
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
            filterList.clear();

            FilterResults filterResults = new FilterResults();

            if(constraint.toString().length() == 0 || constraint.toString().equals("")) {

                filterList.addAll(taskList);

                filterResults.count = filterList.size();

                return filterResults;
            }

            for(Tarea tarea: taskList){
                if(tarea.titulo.toLowerCase().contains(constraint.toString().toLowerCase())) {
                    filterList.add(tarea);
                }
            }

            filterResults.count = filterList.size();

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if(results.count == 0)
                listTitleView.setVisibility(View.GONE);
            else
                listTitleView.setVisibility(View.VISIBLE);

            notifyDataSetChanged();
        }
    }

    //Fields
    private List<Tarea> taskList;

    private List<Tarea> filterList;

    private Context context;

    private OnRemoveClickListener removeClickListener;

    private OnItemClickListener clickListener;

    public Filter filter;

    public View listTitleView;

    public TaskAdapter(View listTitleView , Context context , OnRemoveClickListener removeClickListener , OnItemClickListener clickListener){
        this.context = context;

        this.removeClickListener = removeClickListener;

        this.clickListener = clickListener;

        this.listTitleView = listTitleView;

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
        holder.bind(filterList.get(position) , removeClickListener , clickListener);
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
