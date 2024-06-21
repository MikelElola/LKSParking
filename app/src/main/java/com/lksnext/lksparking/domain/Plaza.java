package com.lksnext.lksparking.domain;

public class Plaza {

    long id;
    String tipo;

    int pos;

    public Plaza() {

    }

    public Plaza(long id, String tipo, int pos) {
        this.id = id;
        this.tipo = tipo;
        this.pos = pos;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
