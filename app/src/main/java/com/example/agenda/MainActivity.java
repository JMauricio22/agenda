package com.example.agenda;

import android.content.Intent;
import android.os.Bundle;

import com.example.agenda.R;
import com.example.agenda.TareasAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import BD.model.TareaViewModel;
import BD.tareas.Tarea;

public class MainActivity extends AppCompatActivity {

    private MainActivity self = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.btn_agregar_tarea);

        btn.setOnClickListener( onCreateTask() );

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        final TareasAdapter adapter = new TareasAdapter(getApplicationContext() , onDeleteListItem());

        RecyclerView listaTareas = (RecyclerView) findViewById(R.id.lista_tareas);

        listaTareas.setLayoutManager(layoutManager);

        listaTareas.setAdapter(adapter);

        new ViewModelProvider(this).get(TareaViewModel.class).getTareas().observe(this , new Observer<List<Tarea>>(){
            @Override
            public void onChanged(@NonNull final List<Tarea> listaTareas) {
                adapter.setItems(listaTareas);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            default:
               return super.onOptionsItemSelected(item);
        }
    }

    public View.OnClickListener onCreateTask(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext() , CrearTareaActivity.class );
                intent.putExtra(CrearTareaActivity.ACCION , CrearTareaActivity.CODE_REQUEST_ADD_TASK);
                startActivity( intent );
            }
        };
    }

    public TareasAdapter.OnRemoveClickListener onDeleteListItem(){
        return new TareasAdapter.OnRemoveClickListener() {
            @Override
            public void onItemClick(Tarea tarea) {
                TareaViewModel model = new ViewModelProvider(self).get(TareaViewModel.class);
                model.eliminar(tarea);
            }
        };
    }

}
