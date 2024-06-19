package com.lksnext.lksparking.viewmodel;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ReservationViewModel extends ViewModel {

    private MutableLiveData<String> selectedDate = new MutableLiveData<>();

    public LiveData<String> getSelectedDate() {
        return selectedDate;
    }

    public ReservationViewModel() {
        // Inicializa con la fecha actual
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        selectedDate.setValue(currentDate);
    }

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

    public void setSelectedDate(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String formattedDate = String.format("%02d/%02d/%d", day, month + 1, year);
        selectedDate.setValue(formattedDate);
    }

}
