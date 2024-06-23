package com.lksnext.lksparking.domain;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.PropertyName;
import com.lksnext.lksparking.data.TipoVehiculo;

public class Plaza implements Parcelable {

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
    protected Plaza(Parcel in) {
        id = in.readLong();
        tipo = TipoVehiculo.valueOf(in.readString());
        pos = in.readInt();
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

    public static final Creator<Plaza> CREATOR = new Creator<Plaza>() {
        @Override
        public Plaza createFromParcel(Parcel in) {
            return new Plaza(in);
        }

        @Override
        public Plaza[] newArray(int size) {
            return new Plaza[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(tipo.name());
        dest.writeInt(pos);
    }

}
