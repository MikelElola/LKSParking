package com.lksnext.lksparking.viewmodel;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.os.Build;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lksnext.lksparking.data.DataRepository;
import com.lksnext.lksparking.data.TipoVehiculo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReservationViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private ReservationViewModel viewModel = new ReservationViewModel();

    @Test
    public void testGetSelectedDate() throws InterruptedException {
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        LiveData<String> selectedDateLiveData = viewModel.getSelectedDate();

        String date = LiveDataTestUtil.getValue(selectedDateLiveData);
        assertEquals(currentDate, date);
    }

    /*@Test
    public void getReservasDia() {
        // Implementación del test
    }

    @Test
    public void addReserva() {
        // Implementación del test
    }*/
}