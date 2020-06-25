package com.example.agenda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.agenda.receiver.AlarmReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import BD.model.TareaViewModel;
import BD.tareas.Tarea;

public class CreateTaskActivity extends AppCompatActivity {

    public static final String CODE_REQUEST_ADD_TASK = "ADD_TASK";
    public static final String CODE_REQUEST_EDIT_TASK = "EDIT_TASK";
    public static final String ACCION = "ACCION";

    public static final String TASK_ID = "TASK_ID";
    public static final String TASK_TITLE = "TASK_TITLE";
    public static final String TASK_DATE= "TASK_DATE";
    public static final String TASK_OBSERVATION = "TASK_OBSERVATION";

    private EditText taskTitle, taskDate, taskTime, observation;

    private ImageView imgCalendar , imgTime;

    private Date date = Calendar.getInstance().getTime();

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("" , Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crear_tarea);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_crear_tarea);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        taskTitle = (EditText) findViewById(R.id.txt_tarea_titulo);

        taskDate = (EditText) findViewById(R.id.txt_tarea_fecha);

        taskDate.setOnClickListener( onShowDialogoFecha());

        imgCalendar = (ImageView) findViewById(R.id.img_calendar);

        imgCalendar.setOnClickListener( onShowDialogoFecha());

        taskTime = (EditText) findViewById(R.id.txt_tarea_hora);

        taskTime.setOnClickListener( onShowDialogoHora());

        imgTime = (ImageView) findViewById(R.id.img_time);

        imgTime.setOnClickListener( onShowDialogoHora());

        observation = (EditText) findViewById(R.id.txt_tarea_observaciones);

        initializateData();
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

                saveTask();

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

                calendar.setTime(date);

                calendar.set(year , month , dayOfMonth);

                date = calendar.getTime();

                simpleDateFormat.applyPattern("EEE,  MMM d, yyyy");

                taskDate.setText(simpleDateFormat.format(date));
            }
        };
    }

    public TimePickerDialog.OnTimeSetListener onTimeSetListener(){
        return new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Calendar calendar = Calendar.getInstance();

                calendar.setTime(date);

                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) , calendar.get(Calendar.DAY_OF_MONTH), hourOfDay , minute);

                date = calendar.getTime();

                simpleDateFormat.applyPattern("hh:mm a");

                taskTime.setText(simpleDateFormat.format(date));
            }
        };
    }

    private void initializateData(){
        Intent intent = getIntent();

        if(intent.getStringExtra(ACCION).equals(CODE_REQUEST_EDIT_TASK)){

            Calendar calendar = Calendar.getInstance();

            long timeInMilis = intent.getLongExtra( TASK_DATE , -1);

            if(timeInMilis != -1)
                calendar.setTimeInMillis( intent.getLongExtra(TASK_DATE , 0));

            date = calendar.getTime();

            taskTitle.setText( intent.getStringExtra(TASK_TITLE) );

            simpleDateFormat.applyPattern("EEE,  MMM d, yyyy");

            taskDate.setText(simpleDateFormat.format(calendar.getTime()));

            simpleDateFormat.applyPattern("hh:mm a");

            taskTime.setText(simpleDateFormat.format(calendar.getTime()));

            observation.setText( intent.getStringExtra(TASK_OBSERVATION));
        }else{

            simpleDateFormat.applyPattern("EEE, MMM d, yyy");

            taskDate.setText( simpleDateFormat.format(Calendar.getInstance().getTime()));

            simpleDateFormat.applyPattern("hh:mm a");

            taskTime.setText(simpleDateFormat.format(Calendar.getInstance().getTime()));

        }
    }

    private void saveTask(){
        Tarea tarea = getData();

        if(!validateData(tarea))
            return;

        TareaViewModel model = new ViewModelProvider(this).get(TareaViewModel.class);

        switch (getIntent().getStringExtra(ACCION)){
            case CODE_REQUEST_ADD_TASK:

                    model.insertar(tarea);

                    setAlarm(tarea);

                break;
            case CODE_REQUEST_EDIT_TASK:

                    model.actualizar(tarea);

                    setAlarm(tarea);

                break;
        }

        finish();
    }

    public void setAlarm(Tarea tarea){
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent intent = new Intent( this , AlarmReceiver.class);

        intent.putExtra("titulo" , tarea.titulo);

        intent.putExtra("observaciones"  , tarea.observaciones);

        PendingIntent pendingIntent =  PendingIntent.getBroadcast( getApplicationContext() , 0 , intent ,  PendingIntent.FLAG_ONE_SHOT);

        alarmManager.set(AlarmManager.RTC_WAKEUP ,  tarea.fecha , pendingIntent);
    }

    private Tarea getData(){

        Tarea tarea = new Tarea();

        if(getIntent().getStringExtra(ACCION).equals(CODE_REQUEST_EDIT_TASK))
            tarea.id = getIntent().getIntExtra( TASK_ID , -1);

        tarea.titulo = taskTitle.getText().toString();

        tarea.fecha = date.getTime();

        tarea.observaciones = observation.getText().toString();

        return tarea;
    }

    private boolean validateData(Tarea tarea){

        if(tarea.titulo.equals("")) {
            showMessage("Titulo");
            return false;
        }

        return true;
    }

    private void showMessage(String message){
        Toast.makeText(getApplicationContext() , "Complete el campo " + message , Toast.LENGTH_SHORT)
                .show();
    }
}
