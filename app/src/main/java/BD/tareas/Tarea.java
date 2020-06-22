package BD.tareas;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tareas")
public class Tarea {

    @PrimaryKey(autoGenerate = true)
    public Integer id;

    @NonNull
    public String titulo;

    @NonNull
    public String fecha;

    @NonNull
    public String hora;

    @NonNull
    public String observaciones;
}
