package BD.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import BD.tareas.Tarea;
import BD.tareas.TareasDAO;

public class AgendaRepository {

    private RoomDatabase db;
    private TareasDAO tareasDAO;

    public AgendaRepository(Application app){

        db = RoomDatabase.getDatabase(app);

        tareasDAO = db.tareasDAO();
    }

    public void insertar(final Tarea tarea){
        RoomDatabase.databaseWriteExecutor.execute(new Runnable(){
            @Override
            public void run() {
                tareasDAO.insertar(tarea);
            }
        });
    }

    public void actualizar(final Tarea tarea){
        RoomDatabase.databaseWriteExecutor.execute(new Runnable(){
            @Override
            public void run() {
                tareasDAO.actualizar(tarea);
            }
        });
    }

    public void eliminar(final Tarea tarea){
        RoomDatabase.databaseWriteExecutor.execute(new Runnable(){
            @Override
            public void run() {
                tareasDAO.eliminar(tarea);
            }
        });
    }

    public LiveData<List<Tarea>> getTareas(){
        return tareasDAO.getTareas();
    }
}
