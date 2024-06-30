package com.lksnext.lksparking.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import com.lksnext.lksparking.data.DataRepository;
import com.lksnext.lksparking.data.TipoVehiculo;
import com.lksnext.lksparking.domain.Hora;
import com.lksnext.lksparking.domain.Plaza;
import com.lksnext.lksparking.domain.Reserva;

@RunWith(MockitoJUnitRunner.class)
public class VigentesViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    DataRepository dataRepositoryMock;

    @Mock
    Observer<List<Reserva>> reservasObserver;

    VigentesViewModel viewModel;

    @Before
    public void setup() {
        viewModel = new VigentesViewModel();
        viewModel.getReservas().observeForever(reservasObserver);
    }

    @Test
    public void getReservasVigentes_success() {
        // Simular una lista de reservas vigentes
        List<Reserva> reservasVigentes = new ArrayList<>();
        reservasVigentes.add(new Reserva("01-07-2024", "user@example.com", new Plaza(TipoVehiculo.NORMAL, 1), new Hora(480, 600)));

        // Configurar captura para el callback ReservasCallback<List<Reserva>>
        ArgumentCaptor<DataRepository.ReservasCallback> callbackCaptor = ArgumentCaptor.forClass(DataRepository.ReservasCallback.class);

        // Configurar el comportamiento esperado del mock del repositorio de datos
        doAnswer(invocation -> {
            // Capturar el callback pasado como argumento
            DataRepository.ReservasCallback<List<Reserva>> callback = callbackCaptor.getValue();
            // Simular éxito devolviendo las reservas vigentes
            callback.onSuccess(reservasVigentes);
            return null; // En este caso, no hay valor de retorno necesario
        }).when(dataRepositoryMock).getReservasVigentes(anyLong(), anyString(), callbackCaptor.capture());

        // Llamar al método que queremos probar
        viewModel.getReservasVigentes();

        // Verificar que se llamó al método correspondiente del repositorio de datos
        verify(dataRepositoryMock).getReservasVigentes(anyLong(), anyString(), callbackCaptor.capture());

        // Verificar que el Observer recibió las reservas vigentes correctamente
        verify(reservasObserver).onChanged(reservasVigentes);
    }

    @Test
    public void editReserva_success() {
        // Crear una reserva de ejemplo para editar
        Reserva reserva = new Reserva("01-07-2024", "user@example.com", new Plaza(TipoVehiculo.NORMAL, 1), new Hora(480, 600));

        // Configurar captura para el callback DataRepository.Callback
        ArgumentCaptor<DataRepository.Callback> callbackCaptor = ArgumentCaptor.forClass(DataRepository.Callback.class);

        // Configurar el comportamiento esperado del mock del repositorio de datos
        doAnswer(invocation -> {
            // Capturar el callback pasado como argumento
            DataRepository.Callback callback = callbackCaptor.getValue();
            // Simular éxito al actualizar la reserva
            callback.onSuccess();
            return null; // No hay valor de retorno necesario
        }).when(dataRepositoryMock).updateReserva(eq(reserva), callbackCaptor.capture());

        // Llamar al método que queremos probar
        viewModel.editReserva(reserva);

        // Verificar que se llamó al método correspondiente del repositorio con los argumentos correctos
        verify(dataRepositoryMock).updateReserva(eq(reserva), callbackCaptor.getValue());

        // Aquí podrías añadir más verificaciones si necesitas asegurarte de algún comportamiento específico después de la edición exitosa.
    }
    @Test
    public void cancelReserva_success() {
        String reservaId = "id_de_prueba";

        // Configurar captura para el callback DataRepository.Callback
        ArgumentCaptor<DataRepository.Callback> callbackCaptor = ArgumentCaptor.forClass(DataRepository.Callback.class);

        // Configurar el comportamiento esperado del mock del repositorio de datos
        doAnswer(invocation -> {
            // Capturar el callback pasado como argumento
            DataRepository.Callback callback = callbackCaptor.getValue();
            // Simular éxito al cancelar la reserva
            callback.onSuccess();
            return null; // No hay valor de retorno necesario
        }).when(dataRepositoryMock).deleteReserva(eq(reservaId), callbackCaptor.capture());

        // Llamar al método que queremos probar
        viewModel.cancelReserva(reservaId);

        // Verificar que se llamó al método correspondiente del repositorio con los argumentos correctos
        verify(dataRepositoryMock).deleteReserva(eq(reservaId), callbackCaptor.getValue());

        // Aquí podrías añadir más verificaciones si necesitas asegurarte de algún comportamiento específico después de la cancelación exitosa.
    }

}