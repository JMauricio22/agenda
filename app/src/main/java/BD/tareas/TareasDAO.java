package BD.tareas;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TareasDAO {

    @Insert
    long insertar(Tarea tarea);

    @Update
    int actualizar(Tarea tarea);

    @Delete
    void eliminar(Tarea tarea);

    @Query("SELECT * FROM tareas")
    LiveData<List<Tarea>> getTareas();
}
