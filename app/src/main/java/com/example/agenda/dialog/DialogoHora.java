package com.example.agenda.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.agenda.R;

import java.util.Calendar;

public class DialogoHora extends DialogFragment {

    private TimePickerDialog.OnTimeSetListener listener;

    private DialogoHora(){}

    public static DialogoHora getInstance(TimePickerDialog.OnTimeSetListener listener){
        DialogoHora dialogo = new DialogoHora();
        dialogo.setListener(listener);
        return dialogo;
    }

    private void setListener(TimePickerDialog.OnTimeSetListener listener){
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        int minute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity() , R.style.Theme_MaterialComponents_Light_Dialog_Alert , listener , hour , minute , false);
    }
}
