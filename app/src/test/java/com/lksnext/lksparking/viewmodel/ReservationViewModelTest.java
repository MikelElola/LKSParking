package com.lksnext.lksparking.viewmodel;

import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lksnext.lksparking.data.DataRepository;
import com.lksnext.lksparking.data.TipoVehiculo;
import com.lksnext.lksparking.domain.Hora;
import com.lksnext.lksparking.domain.Plaza;
import com.lksnext.lksparking.domain.Reserva;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import android.content.Context;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FirebaseAuth.class, DataRepository.class})
public class ReservationViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private ReservationViewModel viewModel;

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

        viewModel = new ReservationViewModel();
    }

    @Test
    public void getReservasDia() throws InterruptedException {
        // Crear una lista de reservas simulada
        List<Reserva> fakeReservas = new ArrayList<>();
        fakeReservas.add(new Reserva("01-07-2024", "user@example.com", new Plaza(TipoVehiculo.NORMAL,1), new Hora(480, 600)));

        // Configurar el comportamiento del mock
        doAnswer(invocation -> {
            DataRepository.ReservasCallback<List<Reserva>> callback = invocation.getArgument(2);
            callback.onSuccess(fakeReservas);
            return null;
        }).when(dataRepository).getReservasPorFecha(anyString(), anyLong(), any());

        // Ejecutar el método a probar
        viewModel.getReservasDia();

        // Obtener el valor de LiveData
        List<Reserva> reservas = LiveDataTestUtil.getValue(viewModel.getReservas());

        // Comprobar los resultados
        assertNotNull(reservas);
        assertEquals(1, reservas.size());
        assertEquals("01-07-2024", reservas.get(0).getFecha());
    }

    @Test
    public void addReserva() {
        // Configurar el comportamiento del mock para añadir reserva
        doAnswer(invocation -> {
            DataRepository.Callback callback = invocation.getArgument(1);
            callback.onSuccess();
            return null;
        }).when(dataRepository).addReserva(any(), any());

        // Crear una plaza y hora de reserva
        Plaza plaza = new Plaza(TipoVehiculo.NORMAL,1);
        Hora hora = new Hora(480, 600);

        // Ejecutar el método a probar
        Reserva nuevaReserva = viewModel.addReserva("01-07-2024", plaza, hora, mock(Context.class));

        // Comprobar los resultados
        assertNotNull(nuevaReserva);
        assertEquals("01-07-2024", nuevaReserva.getFecha());
        assertEquals("user@example.com", nuevaReserva.getUsuario());

        // Verificar que se llamaron los métodos esperados en el mock
        verify(dataRepository).addReserva(any(), any());
    }
}
