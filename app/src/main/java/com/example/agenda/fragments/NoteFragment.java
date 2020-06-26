package com.example.agenda.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda.CreateNoteActivity;
import com.example.agenda.R;
import com.example.agenda.adapter.NoteAdapater;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import BD.model.NotaViewModel;
import BD.notas.Nota;

public class NoteFragment extends Fragment {

    NoteAdapater adapater;

    Context context;

    Filter filter;

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstace){
        return inflater.inflate(R.layout.fragment_list , container , false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);

        context = getActivity().getApplicationContext();

        Toolbar toolbar = getView().findViewById(R.id.toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.open_drawer, R.string.close_drawer);

        toggle.syncState();

        SearchView searchView = (SearchView) getView().findViewById(R.id.search);

        searchView.setOnQueryTextListener( onQueryTextListener());

        FloatingActionButton btn = (FloatingActionButton) getView().findViewById(R.id.btn_agregar_tarea);

        btn.setOnClickListener( onCreateNote() );

        View view = getLayoutInflater().inflate(R.layout.lista_notas , null);

        RecyclerView recyclerView = view.findViewById(R.id.lista_notas);

        ViewGroup container = getView().findViewById(R.id.list_container);

        recyclerView.setLayoutManager( new GridLayoutManager( context , 2));

        adapater = new NoteAdapater( context , onDeleteListItem() , onEditListItem());

        filter = adapater.getFilter();

        recyclerView.setAdapter(adapater);

        NotaViewModel model = new ViewModelProvider(this).get( NotaViewModel.class);

        model.getNotas().observe(getViewLifecycleOwner(), new Observer<List<Nota>>() {
            @Override
            public void onChanged(final List<Nota> notas) {
                adapater.setItems(notas);

            }
        });

        container.addView(view);

    }

    public View.OnClickListener onCreateNote(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity().getApplicationContext() , CreateNoteActivity.class );

                intent.putExtra(CreateNoteActivity.ACCION , CreateNoteActivity.CODE_REQUEST_ADD_NOTE);

                startActivity( intent );
            }
        };
    }

    public NoteAdapater.OnRemoveClickListener onDeleteListItem(){
        return new NoteAdapater.OnRemoveClickListener() {
            @Override
            public void onItemClick(Nota nota) {

                NotaViewModel model = new ViewModelProvider(getActivity()).get(NotaViewModel.class);

                model.eliminar(nota);
            }
        };
    }

    public NoteAdapater.onItemClickListener onEditListItem(){
        return new NoteAdapater.onItemClickListener() {
            @Override
            public void onItemClick(Nota nota) {

                Intent intent = new Intent( getActivity().getApplicationContext()  , CreateNoteActivity.class);

                intent.putExtra(CreateNoteActivity.ACCION , CreateNoteActivity.CODE_REQUEST_EDIT_NOTE);

                intent.putExtra(CreateNoteActivity.NOTE_ID , nota.id);

                intent.putExtra(CreateNoteActivity.NOTE_TITLE , nota.titulo);

                intent.putExtra(CreateNoteActivity.NOTE_DESCRIPTION , nota.descripcion);

                startActivity(intent);
            }
        };
    }

    public SearchView.OnQueryTextListener onQueryTextListener(){
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                filter.filter(query);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                filter.filter(query);

                return true;
            }
        };
    }


}
