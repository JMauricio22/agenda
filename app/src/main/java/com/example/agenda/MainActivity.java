package com.example.agenda;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import BD.model.TareaViewModel;
import BD.tareas.Tarea;

public class MainActivity extends AppCompatActivity {

    private MainActivity self = this;

    private View[] list = new View[6];

    private Filter[] filters = new Filter[list.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);

        SearchView searchView = (SearchView) findViewById(R.id.search);

        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                filterTasks(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                filterTasks(query);
                return true;
            }

        });

        setSupportActionBar(toolbar);

        FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.btn_agregar_tarea);

        btn.setOnClickListener( onCreateTask() );

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        createLiskTask();
    }

    public void createLiskTask(){

        new ViewModelProvider(this).get(TareaViewModel.class).getTareas().observe(this , new Observer<List<Tarea>>(){

            @Override
            public void onChanged(@NonNull final List<Tarea> listaTareas) {

                final String[] filterName = {"Atrasado" , "Hoy" , "Esta semana" , "Semana siguiente" , "Proximo mes" , "Mas tarde"};

                Calendar currentCalendar = Calendar.getInstance();

                ViewGroup container = (ViewGroup) findViewById(R.id.task_container);

                for(View v: list)
                    container.removeView(v);

                for(int i = 0 ; i < filterName.length ; i++){

                    View view = getLayoutInflater().inflate(R.layout.lista_tareas , null);

                    list[i] = view;

                    RecyclerView list = (RecyclerView) view.findViewById(R.id.lista_tareas);

                    ( (TextView) view.findViewById(R.id.titulo_lista_tareas)).setText(filterName[i]);

                    List<Tarea> filterList = new ArrayList<>();

                    for(Tarea tarea: listaTareas){

                        Calendar taskCalendar = Calendar.getInstance();

                        taskCalendar.setTimeInMillis(tarea.fecha);

                        switch (i){
                            case 0:

                                if(taskCalendar.get(Calendar.YEAR) < currentCalendar.get(Calendar.YEAR))
                                    filterList.add(tarea);
                                else if(taskCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) && taskCalendar.get(Calendar.MONTH) < currentCalendar.get(Calendar.MONTH) )
                                    filterList.add(tarea);
                                else if(taskCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) && taskCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) && taskCalendar.get(Calendar.DAY_OF_MONTH) < currentCalendar.get(Calendar.DAY_OF_MONTH))
                                    filterList.add(tarea);

                                break;
                            case 1:

                                if(currentCalendar.get(Calendar.DAY_OF_MONTH) == taskCalendar.get(Calendar.DAY_OF_MONTH) && currentCalendar.get(Calendar.YEAR) == taskCalendar.get(Calendar.YEAR))
                                    filterList.add(tarea);

                                break;
                            case 2:
                            case 3:

                                int limite = 6 - (currentCalendar.get(Calendar.DAY_OF_WEEK) - 1);

                                if(currentCalendar.get(Calendar.YEAR) == taskCalendar.get(Calendar.YEAR))

                                    if(taskCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)){

                                        if( i == 2 && taskCalendar.get(Calendar.DAY_OF_MONTH) > currentCalendar.get(Calendar.DAY_OF_MONTH) && taskCalendar.get(Calendar.DAY_OF_MONTH) <= (currentCalendar.get(Calendar.DAY_OF_MONTH) + limite) ){
                                            filterList.add(tarea);
                                        }else if( i == 3 && (taskCalendar.get(Calendar.DAY_OF_MONTH) <= (currentCalendar.get(Calendar.DAY_OF_MONTH) + limite + 7) && taskCalendar.get(Calendar.DAY_OF_MONTH) > (currentCalendar.get(Calendar.DAY_OF_MONTH) + limite))){
                                            filterList.add(tarea);
                                        }

                                    }

                                break;

                            case 4:

                                if((taskCalendar.get(Calendar.MONTH) - currentCalendar.get(Calendar.MONTH)) == 1 && currentCalendar.get(Calendar.YEAR) == taskCalendar.get(Calendar.YEAR))
                                    filterList.add(tarea);

                                break;

                            case 5:
                                if((taskCalendar.get(Calendar.MONTH) - currentCalendar.get(Calendar.MONTH)) > 1 && taskCalendar.get(Calendar.YEAR) >= currentCalendar.get(Calendar.YEAR))
                                    filterList.add(tarea);

                                break;
                        }
                    }

                    if(filterList.isEmpty())
                        continue;

                    list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    TareasAdapter adapter = new TareasAdapter(getApplicationContext() , onDeleteListItem());

                    filters[i] = adapter.getFilter();

                    list.setAdapter(adapter);

                    adapter.setItems(filterList);

                    container.addView(view);
                }
            }
        });
    }

    private void filterTasks(String query){
        for(Filter f: filters){
            if(f != null)
                f.filter(query);
        }
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
