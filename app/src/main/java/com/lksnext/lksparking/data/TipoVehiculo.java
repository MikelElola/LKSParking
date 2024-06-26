package com.lksnext.lksparking.data;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.PropertyName;

@IgnoreExtraProperties
public enum TipoVehiculo {
    NORMAL("Normal"),
    ELECTRICO("Eléctrico"),
    MOTO("Moto"),
    MINUSVALIDO("Minusválido");

    private final String tipo;

    TipoVehiculo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return tipo;
    }

    @PropertyName("tipo") // Nombre del campo en Firestore
    public String getTipo() {
        return tipo;
    }

    public static TipoVehiculo fromString(String tipo) {
        for (TipoVehiculo tv : TipoVehiculo.values()) {
            if (tv.tipo.equalsIgnoreCase(tipo)) {
                return tv;
            }
        }
        return null; // Manejo del caso donde no se encuentre
    }
}
