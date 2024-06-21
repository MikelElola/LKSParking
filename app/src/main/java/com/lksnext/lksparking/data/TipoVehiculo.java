package com.lksnext.lksparking.data;

public enum TipoVehiculo {
    NORMAL("normal"),
    ELECTRICO("eléctrico"),
    MOTO("moto");

    private final String tipo;

    TipoVehiculo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return tipo;
    }
}
