package com.lksnext.lksparking.viewmodel;

import static org.junit.jupiter.api.Assertions.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Rule;
import org.junit.jupiter.api.Test;

class ReservationViewModelTest {

    //Swap the background executor used by some components with a executor that executes tasks synchronously
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    ReservationViewModel viewModel = new ReservationViewModel();
    @Test
    void getReservas() {
    }

    @Test
    void getSelectedDate() {
    }

    @Test
    void getSelectedPos() {
    }

    @Test
    void setSelectedPos() {
    }

    @Test
    void getSelectedType() {
    }

    @Test
    void setSelectedType() {
    }

    @Test
    void getSelectedStartTime() {
    }

    @Test
    void getSelectedEndTime() {
    }

    @Test
    void setSelectedDate() {
    }

    @Test
    void getReservasDia() {
    }

    @Test
    void addReserva() {
    }
}