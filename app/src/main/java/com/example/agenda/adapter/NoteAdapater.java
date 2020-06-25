package com.example.agenda.adapter;

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

import java.util.ArrayList;
import java.util.List;

import BD.notas.Nota;


public class NoteAdapater extends RecyclerView.Adapter<NoteAdapater.ViewHolder> implements Filterable {

    //Interface

    public interface  onItemClickListener{
        void onItemClick ( Nota nota);
    }

    public interface OnRemoveClickListener {
        void onItemClick (Nota nota);
    }

    // Inner class
    public class ViewHolder extends RecyclerView.ViewHolder{

        public View view;

        public TextView title;

        public ImageView btn_delete;

        public ImageView btn_edit;

        public ViewHolder(@NonNull View view) {
            super(view);

            this.view = view;

            title = (TextView) view.findViewById(R.id.titulo_nota);

            btn_delete = (ImageView) view.findViewById(R.id.btn_eliminar_nota);

            btn_edit = (ImageView) view.findViewById(R.id.btn_editar_nota);
        }

        public void bind(final Nota nota , final NoteAdapater.OnRemoveClickListener removeClickListener , final NoteAdapater.onItemClickListener itemClickListener){

            title.setText(nota.titulo);

            btn_delete.setOnClickListener( new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    removeClickListener.onItemClick( nota );
                }
            });

            btn_edit.setOnClickListener( new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(nota);
                }
            });

        }
    }

    public class CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filterList.clear();

            FilterResults filterResults = new FilterResults();


            if(constraint.toString().length() == 0 || constraint.toString().equals("")) {

                filterList.addAll(noteList);

                return filterResults;
            }

            for(Nota note: noteList){
                if(note.titulo.contains(constraint.toString())) {
                    filterList.add(note);
                }
            }

            filterResults.values = filterList;
            filterResults.count = filterList.size();

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notifyDataSetChanged();
        }
    }

    //Fields
    private List<Nota> noteList;

    private List<Nota> filterList;

    private LayoutInflater inflater;

    private NoteAdapater.OnRemoveClickListener removeClickListener;

    private NoteAdapater.onItemClickListener itemClickListener;

    private CustomFilter filter;

    public NoteAdapater(Context context , NoteAdapater.OnRemoveClickListener removeClickListener , NoteAdapater.onItemClickListener itemClickListener){

        noteList = new ArrayList<>();

        filterList = new ArrayList<>();

        inflater = LayoutInflater.from(context);

        filter = new CustomFilter();

        this.removeClickListener = removeClickListener;

        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_notas , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(filterList.get(position) , removeClickListener , itemClickListener);
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

    @Override
    public Filter getFilter(){
        return filter;
    }

}
