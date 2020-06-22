package BD.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import BD.database.AgendaRepository;
import BD.tareas.Tarea;

public class TareaViewModel extends AndroidViewModel {

    AgendaRepository repository;

    public TareaViewModel(Application app){
        super(app);
        repository = new AgendaRepository(app);
    }

    public void insertar(Tarea tarea){
        repository.insertar(tarea);
    }

    public void actualizar(Tarea tarea){
        repository.actualizar(tarea);
    }

    public void eliminar(Tarea tarea){
        repository.eliminar(tarea);
    }

    public LiveData<List<Tarea>> getTareas(){
        return repository.getTareas();
    }
}
