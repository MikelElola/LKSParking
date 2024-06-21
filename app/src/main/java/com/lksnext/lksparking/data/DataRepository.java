package com.lksnext.lksparking.data;

import androidx.recyclerview.widget.AsyncListUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.lksnext.lksparking.domain.Callback;
import com.lksnext.lksparking.domain.Reserva;

import java.util.ArrayList;
import java.util.List;

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

    // Añadir reserva
    public void addReserva(Reserva reserva, Callback callback) {
        db.collection("reservas").add(reserva)
                .addOnSuccessListener(documentReference -> {
                    reserva.setId(documentReference.getId());
                    callback.onSuccess();
                })
                .addOnFailureListener(e -> {
                    callback.onFailure();
                });
    }

    // Obtener todas las reservas
    public List<Reserva> getReservas(Callback callback) {
        List<Reserva> reservas = new ArrayList<>();
        db.collection("reservas").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Reserva reserva = document.toObject(Reserva.class);
                            reserva.setId(document.getId());
                            reservas.add(reserva);
                        }
                        callback.onSuccess();
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
}
