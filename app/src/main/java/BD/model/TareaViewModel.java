package BD.model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.agenda.CreateTaskActivity;

import java.util.List;

import BD.database.AgendaRepository;
import BD.tareas.Tarea;

public class TareaViewModel extends AndroidViewModel {

    AgendaRepository repository;

    public TareaViewModel(Application app){
        super(app);
        repository = new AgendaRepository(app);
    }

    public void insertar(Tarea tarea , final CreateTaskActivity.SetUpAlarm setUpAlarm){
        repository.insertarTarea(tarea , setUpAlarm);
    }

    public void actualizar(Tarea tarea , final CreateTaskActivity.SetUpAlarm setUpAlarm){
        repository.actualizarTarea(tarea ,  setUpAlarm);
    }

    public void eliminar(Tarea tarea){
        repository.eliminarTarea(tarea);
    }

    public LiveData<List<Tarea>> getTareas(){
        return repository.getTareas();
    }
}
