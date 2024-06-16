package com.lksnext.lksparking.viewmodel;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.lksnext.lksparking.view.fragment.ReservationTimeFragment;

public class ReservationTimeViewModel extends ViewModel {

    public void showTimePicker(FragmentManager fragmentManager, TextInputEditText timeInputEditText) {
        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Appointment time")
                .build();

        timePicker.addOnPositiveButtonClickListener(dialog -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
            String formattedTime = String.format("%02d:%02d", hour, minute);
            timeInputEditText.setText(formattedTime);
        });

        timePicker.show(fragmentManager, "timePicker");
    }


}
