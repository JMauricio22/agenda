package com.example.agenda.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DialogoFecha extends DialogFragment {

    private DialogoFecha(){}

    private DatePickerDialog.OnDateSetListener listener;

    public static DialogoFecha getInstance(DatePickerDialog.OnDateSetListener listener){

        DialogoFecha dialogo = new DialogoFecha();

        dialogo.setListener(listener);

        return dialogo;
    }

    private void setListener(DatePickerDialog.OnDateSetListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int month = calendar.get(Calendar.MONTH);

        int year = calendar.get(Calendar.YEAR);

        return new DatePickerDialog(getActivity() , listener , year , month , day );
    }
}
