package BD.notas;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notas")
public class Nota {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    @NonNull
    public String titulo;

    @NonNull
    public String descripcion;
}
