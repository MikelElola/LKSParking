package com.lksnext.lksparking.viewmodel;

import static org.junit.jupiter.api.Assertions.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Rule;
import org.junit.jupiter.api.Test;

class VerReservasViewModelTest {

    //Swap the background executor used by some components with a executor that executes tasks synchronously
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    VerReservasViewModel viewModel = new VerReservasViewModel();

    @Test
    void getMesActual() {
    }

    @Test
    void getYearActual() {
    }

    @Test
    void getReservas() {
    }

    @Test
    void getReservasMes() {
    }
}