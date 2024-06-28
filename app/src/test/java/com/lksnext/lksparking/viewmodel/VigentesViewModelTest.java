package com.lksnext.lksparking.viewmodel;

import static org.junit.jupiter.api.Assertions.*;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Rule;
import org.junit.jupiter.api.Test;

class VigentesViewModelTest {

    //Swap the background executor used by some components with a executor that executes tasks synchronously
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    VigentesViewModel viewModel = new VigentesViewModel();
    @Test
    void getMesActual() {
    }

    @Test
    void getReservas() {
    }

    @Test
    void getReservasVigentes() {
    }

    @Test
    void cancelReserva() {
    }

    @Test
    void addItem() {
    }
}