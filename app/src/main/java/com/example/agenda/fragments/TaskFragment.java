package com.example.agenda.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda.CreateTaskActivity;
import com.example.agenda.R;
import com.example.agenda.adapter.TaskAdapter;
import com.example.agenda.receiver.AlarmReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import BD.model.TareaViewModel;
import BD.tareas.Tarea;

import static android.content.Context.ALARM_SERVICE;

public class TaskFragment extends Fragment {

    private View[] list = new View[6];

    private Filter[] filters = new Filter[list.length];

    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_list , container , false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);

        context = getActivity().getApplicationContext();

        Toolbar toolbar = getView().findViewById(R.id.toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.open_drawer, R.string.close_drawer);

        toggle.syncState();

        SearchView searchView = (SearchView) getView().findViewById(R.id.search);

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

        FloatingActionButton btn = (FloatingActionButton) getView().findViewById(R.id.btn_agregar_tarea);

        btn.setOnClickListener( onCreateTask() );

        LinearLayoutManager layoutManager = new LinearLayoutManager( context );

        createLiskTask();
    }

    public void createLiskTask(){

        new ViewModelProvider(this).get(TareaViewModel.class).getTareas().observe(getViewLifecycleOwner() , new Observer<List<Tarea>>(){

            @Override
            public void onChanged(@NonNull final List<Tarea> listaTareas) {

                final String[] filterName = {"Atrasado" , "Hoy" , "Esta semana" , "Semana siguiente" , "Proximo mes" , "Mas tarde"};

                Calendar currentCalendar = Calendar.getInstance();

                ViewGroup container = (ViewGroup) getView().findViewById(R.id.list_container);

                for(View v: list)
                    container.removeView(v);

                for(int i = 0 ; i < filterName.length ; i++){

                    View view = getLayoutInflater().inflate(R.layout.lista_tareas , null);

                    list[i] = view;

                    RecyclerView list = (RecyclerView) view.findViewById(R.id.lista_tareas);

                    TextView titleView = ( (TextView) view.findViewById(R.id.titulo_lista_tareas));

                    titleView.setText(filterName[i]);

                    List<Tarea> filterList = new ArrayList<>();

                    for(Tarea tarea: listaTareas){

                        Calendar taskCalendar = Calendar.getInstance();

                        taskCalendar.setTimeInMillis(tarea.fecha);

                        Date currentDate;
                        Date taskDate;

                        switch (i){
                            case 0:

                                currentDate = currentCalendar.getTime();
                                taskDate = taskCalendar.getTime();

                                if( taskDate.before( currentDate ))
                                    filterList.add(tarea);

                                break;
                            case 1:

                                currentDate = currentCalendar.getTime();
                                taskDate = taskCalendar.getTime();


                                if( taskDate.before( currentDate ) == false && currentCalendar.get(Calendar.YEAR) == taskCalendar.get(Calendar.YEAR) && currentCalendar.get(Calendar.MONTH) == taskCalendar.get(Calendar.MONTH) && currentCalendar.get(Calendar.DAY_OF_MONTH) == taskCalendar.get(Calendar.DAY_OF_MONTH))
                                    filterList.add(tarea);

                                break;
                            case 2:
                            case 3:

                                int limit = 6 - (currentCalendar.get(Calendar.DAY_OF_WEEK) - 1);

                                if(currentCalendar.get(Calendar.YEAR) == taskCalendar.get(Calendar.YEAR))

                                    if(taskCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)){

                                        if( i == 2 && taskCalendar.get(Calendar.DAY_OF_MONTH) > currentCalendar.get(Calendar.DAY_OF_MONTH) && taskCalendar.get(Calendar.DAY_OF_MONTH) <= (currentCalendar.get(Calendar.DAY_OF_MONTH) + limit) ){
                                            filterList.add(tarea);
                                        }else if( i == 3 && (taskCalendar.get(Calendar.DAY_OF_MONTH) <= (currentCalendar.get(Calendar.DAY_OF_MONTH) + limit + 7) && taskCalendar.get(Calendar.DAY_OF_MONTH) > (currentCalendar.get(Calendar.DAY_OF_MONTH) + limit))){
                                            filterList.add(tarea);
                                        }

                                    }

                                break;

                            case 4:

                                if(currentCalendar.get(Calendar.YEAR) == taskCalendar.get(Calendar.YEAR) && (taskCalendar.get(Calendar.MONTH) - currentCalendar.get(Calendar.MONTH)) == 1)
                                    filterList.add(tarea);

                                break;

                            case 5:

                                if(taskCalendar.get(Calendar.YEAR) >= currentCalendar.get(Calendar.YEAR) && (taskCalendar.get(Calendar.MONTH) - currentCalendar.get(Calendar.MONTH)) > 1)
                                    filterList.add(tarea);

                                break;
                        }
                    }

                    if(filterList.isEmpty())
                        continue;

                    list.setLayoutManager(new LinearLayoutManager( context ));

                    TaskAdapter adapter = new TaskAdapter( titleView , context , onDeleteListItem() , onEditListItem());

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


    public View.OnClickListener onCreateTask(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent( context , CreateTaskActivity.class );

                intent.putExtra(CreateTaskActivity.ACCION , CreateTaskActivity.CODE_REQUEST_ADD_TASK);

                startActivity( intent );
            }
        };
    }

    public TaskAdapter.OnRemoveClickListener onDeleteListItem(){
        return new TaskAdapter.OnRemoveClickListener() {
            @Override
            public void onItemClick(Tarea tarea) {

                TareaViewModel model = new ViewModelProvider(getActivity()).get(TareaViewModel.class);

                AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

                Intent intent = new Intent( context , AlarmReceiver.class);

                PendingIntent pendingIntent =  PendingIntent.getBroadcast( context , (int) tarea.id , intent ,  PendingIntent.FLAG_ONE_SHOT);

                alarmManager.cancel( pendingIntent );

                model.eliminar(tarea);
            }
        };
    }

    public TaskAdapter.OnItemClickListener onEditListItem(){
        return new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Tarea tarea) {
                Intent intent = new Intent( context , CreateTaskActivity.class);

                intent.putExtra( CreateTaskActivity.ACCION , CreateTaskActivity.CODE_REQUEST_EDIT_TASK);

                intent.putExtra( CreateTaskActivity.TASK_ID , tarea.id);

                intent.putExtra( CreateTaskActivity.TASK_TITLE , tarea.titulo);

                intent.putExtra( CreateTaskActivity.TASK_DATE, tarea.fecha);

                intent.putExtra( CreateTaskActivity.TASK_OBSERVATION , tarea.observaciones);

                startActivity( intent );
            }
        };
    }

}
