package com.lksnext.lksparking.data;

import android.util.Log;

import androidx.recyclerview.widget.AsyncListUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.lksnext.lksparking.domain.Callback;
import com.lksnext.lksparking.domain.Reserva;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class DataRepository {

    private static DataRepository instance;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private DataRepository(){
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }


    //Creación de la instancia en caso de que no exista.
    public static synchronized DataRepository getInstance(){
        if (instance==null){
            instance = new DataRepository();
        }
        return instance;
    }

    public interface ReservasCallback<T> {
        void onSuccess(T result);
        void onFailure();
    }

    public interface Callback {
        void onSuccess();
        void onFailure();
    }

    //Petición del login.
    public void login(String email, String pass, Callback callback){
        firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure();
                    }
                });
    }

    // Petición del registro.
    public void register(String email, String password, Callback callback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure();
                    }
                });
    }

    public void initiatePasswordReset(String email, Callback callback) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailure();
                    }
                });
    }

    // Añadir reserva
    public void addReserva(Reserva reserva, Callback callback) {
        // Verifica si ya existe una reserva en la misma fecha y plaza
        db.collection("reservas")
                .whereEqualTo("fecha", reserva.getFecha())
                .whereEqualTo("plaza.pos", reserva.getPlaza().getPos())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            // Ya existe una reserva en la misma fecha y plaza
                            Log.e("MiApp", "Ya existe una reserva en la misma fecha y plaza");
                            callback.onFailure();
                        } else {
                            // No existe una reserva conflictiva, añade la nueva reserva
                            db.collection("reservas").add(reserva)
                                    .addOnSuccessListener(documentReference -> {
                                        String generatedId = documentReference.getId();
                                        reserva.setId(generatedId);
                                        documentReference.update("id", generatedId) // Actualiza el campo id en Firestore
                                                .addOnSuccessListener(aVoid -> {
                                                    Log.i("MiApp", "ID de la reserva: " + reserva.getId());
                                                    callback.onSuccess();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Log.e("MiApp", "Error al actualizar el ID de la reserva", e);
                                                    callback.onFailure();
                                                });
                                    });
                        }
                    } else {
                        Log.e("MiApp", "Error al verificar reservas existentes", task.getException());
                        callback.onFailure();
                    }
                });
    }

    // Obtener todas las reservas
    public List<Reserva> getReservas(ReservasCallback<List<Reserva>> callback) {
        List<Reserva> reservas = new ArrayList<>();
        db.collection("reservas").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Reserva reserva = document.toObject(Reserva.class);
                            reserva.setId(document.getId());
                            reservas.add(reserva);
                        }
                        callback.onSuccess(reservas);
                    } else {
                        callback.onFailure();
                    }
                });
        return reservas;
    }

    // Actualizar reserva
    public void updateReserva(Reserva reserva, Callback callback) {
        db.collection("reservas").document(reserva.getId()).set(reserva)
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure());
    }

    // Eliminar reserva
    public void deleteReserva(String id, Callback callback) {
        db.collection("reservas").document(id).delete()
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure());
    }

    public void getReservasPorMes(int mes, int year, String usuario, ReservasCallback<List<Reserva>> callback) {
        List<Reserva> reservas = new ArrayList<>();
        db.collection("reservas").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Reserva reserva = document.toObject(Reserva.class);
                            reserva.setId(document.getId());

                            // Extraer el mes y el año de la fecha
                            String fecha = reserva.getFecha();
                            String[] partes = fecha.split("-");
                            int mesReserva = Integer.parseInt(partes[1]);
                            int yearReserva = Integer.parseInt(partes[2]);
                            String usuarioReserva = reserva.getUsuario();
                            // Filtrar por mes y año
                            if (yearReserva == year && mesReserva == mes && Objects.equals(usuarioReserva, usuario)) {
                                reservas.add(reserva);
                                Log.i("MiApp", "Reserva agregada: " + reserva.toString());
                            }
                        }
                        callback.onSuccess(reservas);
                    } else {
                        callback.onFailure();
                    }
                });
    }

    /*public void getReservasPorFecha(String fecha, ReservasCallback<List<Reserva>> callback) {
        List<Reserva> reservas = new ArrayList<>();
        db.collection("reservas").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Reserva reserva = document.toObject(Reserva.class);
                            reserva.setId(document.getId());

                            String fechaReserva = reserva.getFecha().replace("-", "/");

                            // Filtrar por fecha
                            if (fechaReserva.equals(fecha)) {
                                reservas.add(reserva);
                                Log.i("MiApp", "Reserva en la fecha seleccionada: " + fecha + " - " + reserva.toString());
                            }
                        }
                        callback.onSuccess(reservas);
                    } else {
                        callback.onFailure();
                    }
                });
    }*/
    public void getReservasPorFecha(String fecha, long tiempoActual, ReservasCallback<List<Reserva>> callback) {
        List<Reserva> reservas = new ArrayList<>();
        db.collection("reservas").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Reserva reserva = document.toObject(Reserva.class);
                            reserva.setId(document.getId());

                            String fechaReserva = reserva.getFecha().replace("-", "/");
                            long horaFin = reserva.getHora().getHoraFin();

                            // Convertir la fecha de la reserva a milisegundos
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            try {
                                Date fechaReservaDate = dateFormat.parse(fechaReserva);
                                Calendar calReserva = Calendar.getInstance();
                                calReserva.setTime(fechaReservaDate);
                                calReserva.add(Calendar.MINUTE, (int) horaFin);

                                long tiempoReservaFin = calReserva.getTimeInMillis();

                                // Filtrar por fecha y que la horaFin no haya pasado
                                if (fechaReserva.equals(fecha) && tiempoReservaFin > tiempoActual) {
                                    reservas.add(reserva);
                                    Log.i("MiApp", "Reserva en la fecha seleccionada y vigente: " + fecha + " - " + reserva.toString());
                                }
                            } catch (ParseException e) {
                                Log.e("MiApp", "Error al parsear la fecha de la reserva", e);
                            }
                        }
                        callback.onSuccess(reservas);
                    } else {
                        callback.onFailure();
                    }
                });
    }


    public void getReservasVigentes(long tiempoActual,String usuario, ReservasCallback<List<Reserva>> callback){
        List<Reserva> reservasVigentes = new ArrayList<>();
        db.collection("reservas").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Reserva reserva = document.toObject(Reserva.class);
                            reserva.setId(document.getId());

                            String fechaReserva = reserva.getFecha();
                            long horaFin = reserva.getHora().getHoraFin();
                            String usuarioReserva = reserva.getUsuario();
                            // Convertir la fecha de la reserva a milisegundos
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            try {
                                Date fechaReservaDate = dateFormat.parse(fechaReserva);
                                Calendar calReserva = Calendar.getInstance();
                                calReserva.setTime(fechaReservaDate);
                                calReserva.add(Calendar.MINUTE, (int) horaFin);

                                long tiempoReservaFin = calReserva.getTimeInMillis();

                                // Filtrar reservas vigentes
                                if (tiempoReservaFin > tiempoActual && Objects.equals(usuarioReserva, usuario)) {
                                    reservasVigentes.add(reserva);
                                    Log.i("MiApp", "Reserva vigente agregada: " + reserva.toString());
                                }
                            } catch (ParseException e) {
                                Log.e("MiApp", "Error al parsear la fecha de la reserva", e);
                            }
                        }
                        callback.onSuccess(reservasVigentes);
                    } else {
                        callback.onFailure();
                    }
                });
    }
}
