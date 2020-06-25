package BD.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import BD.database.AgendaRepository;
import BD.notas.Nota;
import BD.tareas.Tarea;

public class NotaViewModel extends AndroidViewModel {

    AgendaRepository repository;

    public NotaViewModel(@NonNull Application application) {
        super(application);

        repository = new AgendaRepository(application);
    }

    public void insertar(Nota nota){
        repository.insertarNota(nota);
    }

    public void actualizar(Nota nota){
        repository.actualizarNota(nota);
    }

    public void eliminar(Nota nota){
        repository.eliminarNota(nota);
    }

    public LiveData<List<Nota>> getNotas(){
        return repository.getNotas();
    }
}
