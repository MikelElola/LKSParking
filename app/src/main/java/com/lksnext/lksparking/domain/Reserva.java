package com.lksnext.lksparking.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Reserva implements Parcelable {

    String fecha;
    String usuario;
    String id;
    Plaza plaza;
    Hora hora;

    public Reserva() { }

    public Reserva(String fecha, String usuario, Plaza plaza, Hora hora) {
        this.fecha = fecha;
        this.usuario = usuario;
        this.plaza = plaza;
        this.hora = hora;
    }

    protected Reserva(Parcel in) {
        fecha = in.readString();
        usuario = in.readString();
        id = in.readString();
        plaza = in.readParcelable(Plaza.class.getClassLoader());
        hora = in.readParcelable(Hora.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fecha);
        dest.writeString(usuario);
        dest.writeString(id);
        dest.writeParcelable(plaza, flags);
        dest.writeParcelable((Parcelable) hora, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Reserva> CREATOR = new Creator<Reserva>() {
        @Override
        public Reserva createFromParcel(Parcel in) {
            return new Reserva(in);
        }

        @Override
        public Reserva[] newArray(int size) {
            return new Reserva[size];
        }
    };

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

    public Plaza getPlaza() {
        return plaza;
    }

    public void setPlaza(Plaza plaza) {
        this.plaza = plaza;
    }

    public Hora getHora() {
        return hora;
    }

    public void setHora(Hora hora) {
        this.hora = hora;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
