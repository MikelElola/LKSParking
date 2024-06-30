package com.lksnext.lksparking.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Rule;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lksnext.lksparking.data.DataRepository;
import com.lksnext.lksparking.data.TipoVehiculo;
import com.lksnext.lksparking.domain.Hora;
import com.lksnext.lksparking.domain.Plaza;
import com.lksnext.lksparking.domain.Reserva;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FirebaseAuth.class, DataRepository.class})
public class VerReservasViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private VerReservasViewModel viewModel;

    @Mock
    private DataRepository dataRepository;

    @Mock
    private FirebaseAuth firebaseAuth;

    @Mock
    private FirebaseUser firebaseUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        // Mocking FirebaseAuth and its static methods
        mockStatic(FirebaseAuth.class);
        when(FirebaseAuth.getInstance()).thenReturn(firebaseAuth);
        when(firebaseAuth.getCurrentUser()).thenReturn(firebaseUser);
        when(firebaseUser.getEmail()).thenReturn("user@example.com");

        // Mocking DataRepository and its static methods
        mockStatic(DataRepository.class);
        when(DataRepository.getInstance()).thenReturn(dataRepository);

        viewModel = new VerReservasViewModel();
    }

    @Test
    public void getReservasMes_success() throws InterruptedException {
        // Mockear una lista de reservas simulada
        List<Reserva> fakeReservas = new ArrayList<>();
        fakeReservas.add(new Reserva("01-07-2024", "user@example.com", new Plaza(TipoVehiculo.NORMAL, 1), new Hora(480, 600)));

        // Configurar el comportamiento del mock para el callback de éxito
        doAnswer(invocation -> {
            DataRepository.ReservasCallback<List<Reserva>> callback = invocation.getArgument(3);
            callback.onSuccess(fakeReservas);
            return null;
        }).when(dataRepository).getReservasPorMes(anyInt(), anyInt(), anyString(), any());

        // Llamar al método a probar
        viewModel.getReservasMes();

        // Obtener el valor de LiveData de reservas
        LiveData<List<Reserva>> reservasLiveData = viewModel.getReservas();
        List<Reserva> reservas = LiveDataTestUtil.getValue(reservasLiveData);

        // Verificar que las reservas no sean nulas y que contengan los datos esperados
        assertNotNull(reservas);
        assertEquals(1, reservas.size());
        assertEquals("01-07-2024", reservas.get(0).getFecha());
        assertEquals("user@example.com", reservas.get(0).getUsuario());
        assertEquals("NORMAL", reservas.get(0).getPlaza().getTipo());
        assertEquals(1, reservas.get(0).getPlaza().getPos());
        assertEquals(480, reservas.get(0).getHora().getHoraInicio());
        assertEquals(600, reservas.get(0).getHora().getHoraFin());
    }

    @Test
    public void getReservasMes_failure() throws InterruptedException {
        // Configurar el comportamiento del mock para el callback de fracaso
        doAnswer(invocation -> {
            DataRepository.ReservasCallback<List<Reserva>> callback = invocation.getArgument(3);
            callback.onFailure();
            return null;
        }).when(dataRepository).getReservasPorMes(anyInt(), anyInt(), anyString(), any());

        // Llamar al método a probar
        viewModel.getReservasMes();

        // Obtener el valor de LiveData de reservas
        LiveData<List<Reserva>> reservasLiveData = viewModel.getReservas();
        List<Reserva> reservas = LiveDataTestUtil.getValue(reservasLiveData);

        // Verificar que las reservas sean nulas, lo que indica un fallo en la obtención de las reservas
        assertNull(reservas);
    }
}
