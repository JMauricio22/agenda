package BD.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import BD.notas.Nota;
import BD.notas.NotasDAO;
import BD.tareas.Tarea;
import BD.tareas.TareasDAO;

@Database( entities = { Tarea.class , Nota.class } , version = 2 , exportSchema = false)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    public abstract TareasDAO tareasDAO();

    public abstract NotasDAO notasDAO();

    private static volatile RoomDatabase INSTANCIA;

    private static final int NUMERO_SUBPROCESOS = 4;


    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMERO_SUBPROCESOS);


    public static RoomDatabase getDatabase(final Context contexto) {

        if (INSTANCIA == null) {

            synchronized (RoomDatabase.class) {

                if (INSTANCIA == null) {

                    Context contexto_aplicacion = contexto.getApplicationContext();

                    Class<RoomDatabase> clase_room = RoomDatabase.class;

                    String nombre_base_de_datos = "agenda";

                    INSTANCIA = Room.databaseBuilder(contexto_aplicacion, clase_room, nombre_base_de_datos)
                            .build();
                }
            }
        }


        return INSTANCIA;
    }
}
