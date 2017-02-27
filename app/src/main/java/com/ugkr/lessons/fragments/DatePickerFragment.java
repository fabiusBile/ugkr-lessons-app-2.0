package com.ugkr.lessons.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import java.util.Calendar;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.ugkr.lessons.R;

/**
 * A simple {@link Fragment} subclass.
 */
public  class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public String code="";
    public boolean isGroup;

    public interface OnDateSelectedListener {
        public void OnDateSelected(int year, int month, int day,String code, boolean isGroup);
    }
    OnDateSelectedListener dateListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        dateListener.OnDateSelected(year,month,day,code,isGroup);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnDateSelectedListener) {
            dateListener = (OnDateSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ScheduleFragmentInterface");
        }

    }
}
