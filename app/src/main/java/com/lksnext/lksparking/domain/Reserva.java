package com.lksnext.lksparking.domain;

public class Reserva {

    String fecha, usuario, id;

    Plaza plaza;

    Hora hora;

    public Reserva() {

    }

    public Reserva(String fecha, String usuario, String id, Plaza plaza, Hora hora) {
        this.fecha = fecha;
        this.usuario = usuario;
        this.plaza = plaza;
        this.hora = hora;
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Plaza getPlazaId() {
        return plaza;
    }

    public void setPlazaId(Plaza plaza) {
        this.plaza = plaza;
    }

    public Hora getHora() {
        return hora;
    }

    public void setHora(Hora hora) {
        this.hora = hora;
    }

    public long getHoraInicio() {
        return hora.getHoraInicio();
    }

    public void setHoraInicio(long hora) {
        this.hora.setHoraInicio(hora);
    }

    public long getHoraFin() {
        return hora.getHoraFin();
    }

    public void setHoraFin(long hora) {
        this.hora.setHoraFin(hora);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
