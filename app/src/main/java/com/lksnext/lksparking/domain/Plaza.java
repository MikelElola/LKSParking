package com.lksnext.lksparking.domain;

import com.google.firebase.firestore.PropertyName;
import com.lksnext.lksparking.data.TipoVehiculo;

public class Plaza {

    long id;
    TipoVehiculo tipo;

    int pos;

    public Plaza() {

    }

    public Plaza(long id, TipoVehiculo tipo, int pos) {
        this.id = id;
        this.tipo = tipo;
        this.pos = pos;
    }
    @PropertyName("tipo")
    public String getTipo() {
        return tipo != null ? tipo.getTipo() : null;
    }

    @PropertyName("tipo")
    public void setTipo(String tipo) {
        this.tipo = TipoVehiculo.fromString(tipo);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
