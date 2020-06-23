package com.example.agenda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.agenda.dialog.DialogoFecha;
import com.example.agenda.dialog.DialogoHora;

import java.util.Calendar;
import java.util.Date;

import BD.model.TareaViewModel;
import BD.tareas.Tarea;

public class CrearTareaActivity extends AppCompatActivity {

    public static final String CODE_REQUEST_ADD_TASK = "ADD_TASK";
    public static final String CODE_REQUEST_EDIT_TASK = "EDIT_TASK";
    public static final String ACCION = "ACCION";

    private EditText tituloTarea , fechaTarea ,  horaTarea , observaciones;

    private ImageView imgCalendar , imgTime;

    private Date fecha = Calendar.getInstance().getTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crear_tarea);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_crear_tarea);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tituloTarea = (EditText) findViewById(R.id.txt_tarea_titulo);

        fechaTarea = (EditText) findViewById(R.id.txt_tarea_fecha);

        fechaTarea.setOnClickListener( onShowDialogoFecha());

        imgCalendar = (ImageView) findViewById(R.id.img_calendar);

        imgCalendar.setOnClickListener( onShowDialogoFecha());

        horaTarea = (EditText) findViewById(R.id.txt_tarea_hora);

        horaTarea.setOnClickListener( onShowDialogoHora());

        imgTime = (ImageView) findViewById(R.id.img_time);

        imgTime.setOnClickListener( onShowDialogoHora());

        observaciones = (EditText) findViewById(R.id.txt_tarea_observaciones);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_crear_tarea , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.btn_guardar_tarea:
                guardarTarea();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public View.OnClickListener onShowDialogoFecha(){
       return new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               DialogoFecha dialogoFecha = DialogoFecha.getInstance(onDateSetListener());
               dialogoFecha.show(getSupportFragmentManager() , "dialogo_fecha");
           }
       };
    }

    public View.OnClickListener onShowDialogoHora(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DialogoHora dialogoHora = DialogoHora.getInstance(onTimeSetListener());
                dialogoHora.show(getSupportFragmentManager() , "dialogo_hora");
            }
        };
    }

    public DatePickerDialog.OnDateSetListener onDateSetListener(){
        return new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fecha);
                calendar.set(year , month , dayOfMonth);
                fecha = calendar.getTime();
                fechaTarea.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        };
    }

    public TimePickerDialog.OnTimeSetListener onTimeSetListener(){
        return new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fecha);
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) , calendar.get(Calendar.DAY_OF_MONTH), hourOfDay , minute);
                fecha = calendar.getTime();
                horaTarea.setText(hourOfDay + ":" + minute);
            }
        };
    }

    private void guardarTarea(){
        Tarea tarea = obtenerDatos();

        if(!validarDatos(tarea))
            return;

        TareaViewModel model = new ViewModelProvider(this).get(TareaViewModel.class);

        switch (getIntent().getStringExtra(ACCION)){
            case CODE_REQUEST_ADD_TASK:
                    model.insertar(tarea);
                break;
            case CODE_REQUEST_EDIT_TASK:
                    model.actualizar(tarea);
                break;
        }

        finish();
    }

    private Tarea obtenerDatos(){
        Tarea tarea = new Tarea();
        tarea.titulo = tituloTarea.getText().toString();
        tarea.fecha = fecha.getTime();
        tarea.observaciones = observaciones.getText().toString();
        return tarea;
    }

    private boolean validarDatos(Tarea tarea){

        if(tarea.titulo.equals("")) {
            showMessage("Titulo");
            return false;
        }

        return true;
    }

    private void showMessage(String message){
        message = "Complete el campo de " + message;
        Toast.makeText(getApplicationContext() , message , Toast.LENGTH_SHORT).show();
    }
}
