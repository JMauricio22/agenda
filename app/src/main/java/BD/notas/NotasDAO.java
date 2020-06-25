package BD.notas;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NotasDAO {

    @Insert
    public void insertar(Nota nota);

    @Delete
    public void eliminar(Nota nota);

    @Update
    public void actualizar(Nota nota);

    @Query("SELECT *  FROM notas")
    public LiveData<List<Nota>> getNotas();

}
