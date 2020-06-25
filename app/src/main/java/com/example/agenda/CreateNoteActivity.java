package com.example.agenda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import BD.model.NotaViewModel;
import BD.notas.Nota;

public class CreateNoteActivity extends AppCompatActivity {

    public static final String CODE_REQUEST_ADD_NOTE = "ADD_NOTE";
    public static final String CODE_REQUEST_EDIT_NOTE = "EDIT_NOTE";
    public static final String ACCION = "ACCION";

    public static final String NOTE_ID = "NOTE_ID";
    public static final String NOTE_TITLE = "NOTE_TITLE";
    public static final String NOTE_DESCRIPTION = "NOTE_DESCRIPTION";

    EditText title, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crear_nota);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_crear_tarea);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = (EditText) findViewById(R.id.txt_nota_titulo);

        description = (EditText) findViewById(R.id.txt_nota_descripcion);

        initializeNote();
    }

    public void initializeNote(){
        Intent intent = getIntent();

        if(intent.getStringExtra(CreateNoteActivity.ACCION).equals(CreateNoteActivity.CODE_REQUEST_EDIT_NOTE)){

            title.setText( intent.getStringExtra(CreateNoteActivity.NOTE_TITLE));

            description.setText( intent.getStringExtra(CreateNoteActivity.NOTE_DESCRIPTION));

        }
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

        if(getIntent().getStringExtra(CreateNoteActivity.ACCION).equals(CreateNoteActivity.CODE_REQUEST_EDIT_NOTE))
            note.id = getIntent().getIntExtra( NOTE_ID , -1);

        note.titulo = title.getText().toString();

        note.descripcion = description.getText().toString();

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
