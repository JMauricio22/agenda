package com.example.agenda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import BD.model.NotaViewModel;
import BD.notas.Nota;

public class CrearNotaActivity extends AppCompatActivity {

    public static final String CODE_REQUEST_ADD_NOTE = "ADD_NOTE";
    public static final String CODE_REQUEST_EDIT_NOTE = "EDIT_NOTE";
    public static final String ACCION = "ACCION";

    EditText titulo , descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crear_nota);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_crear_tarea);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titulo = (EditText) findViewById(R.id.txt_nota_titulo);

        descripcion = (EditText) findViewById(R.id.txt_nota_descripcion);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate( R.menu.menu_crear_tarea , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.btn_guardar_tarea:

                saveNote();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNote(){

        Nota note = getData();

        if(!validateData(note))
            return;

        NotaViewModel model = new ViewModelProvider(this).get( NotaViewModel.class );

        switch (getIntent().getStringExtra(ACCION)){
            case CODE_REQUEST_ADD_NOTE:
                model.insertar(note);
                break;
            case CODE_REQUEST_EDIT_NOTE:
                model.actualizar(note);
                break;
        }

        finish();
    }

    private Nota getData(){
        Nota note = new Nota();

        note.titulo = titulo.getText().toString();

        note.descripcion = descripcion.getText().toString();

        return note;
    }

    private boolean validateData(Nota note){

        if (note.titulo.equals("")){
            showMessage("titulo");
            return false;
        }

        if(note.descripcion.equals("")){
            showMessage("descripcion");
            return false;
        }

        return true;
    }

    private void showMessage(String message){
        Toast.makeText(this, "Complete el campo " + message, Toast.LENGTH_SHORT).show();
    }
}
