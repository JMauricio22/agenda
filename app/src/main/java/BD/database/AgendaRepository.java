package BD.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.agenda.CreateTaskActivity;

import java.util.List;

import BD.notas.Nota;
import BD.notas.NotasDAO;
import BD.tareas.Tarea;
import BD.tareas.TareasDAO;

public class AgendaRepository {

    private RoomDatabase db;

    private TareasDAO tareasDAO;

    private NotasDAO notasDAO;

    public AgendaRepository(Application app){

        db = RoomDatabase.getDatabase(app);

        tareasDAO = db.tareasDAO();

        notasDAO = db.notasDAO();
    }

    //Tareas

    public void insertarTarea(final Tarea tarea , final CreateTaskActivity.SetUpAlarm setUpAlarm){
        RoomDatabase.databaseWriteExecutor.execute(new Runnable(){
            @Override
            public void run() {

                long id = tareasDAO.insertar(tarea);

                if(setUpAlarm != null)
                    setUpAlarm.setAlarm( id , tarea.titulo , tarea.fecha);

            }
        });
    }

    public void actualizarTarea(final Tarea tarea , final CreateTaskActivity.SetUpAlarm setUpAlarm){
        RoomDatabase.databaseWriteExecutor.execute(new Runnable(){
            @Override
            public void run() {

                int id = tareasDAO.actualizar(tarea);

                if(setUpAlarm != null)
                    setUpAlarm.setAlarm( id , tarea.titulo , tarea.fecha);
            }
        });
    }

    public void eliminarTarea(final Tarea tarea){
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


    //Notas

    public void insertarNota(final Nota nota){
        RoomDatabase.databaseWriteExecutor.execute(new Runnable(){
            @Override
            public void run() {
                notasDAO.insertar(nota);
            }
        });
    }

    public void eliminarNota(final Nota nota){
        RoomDatabase.databaseWriteExecutor.execute(new Runnable(){
            @Override
            public void run() {
                notasDAO.eliminar(nota);
            }
        });
    }

    public void actualizarNota(final Nota nota){
        RoomDatabase.databaseWriteExecutor.execute(new Runnable(){
            @Override
            public void run() {
                notasDAO.actualizar(nota);
            }
        });
    }

    public LiveData<List<Nota>> getNotas(){
        return notasDAO.getNotas();
    }

}
