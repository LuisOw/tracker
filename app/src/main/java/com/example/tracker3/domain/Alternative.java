package com.example.tracker3.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Alternative implements Parcelable {

    private int id;
    private String type;
    private String text;
    private int value;

    public Alternative(int id, String type, String text, int value) {
        this.id = id;
        this.type = type;
        this.text = text;
        this.value = value;
    }

    public Alternative(Parcel parcel) {
        this.id = parcel.readInt();
        this.type = parcel.readString();
        this.text = parcel.readString();
        this.value = parcel.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Alternative{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.text);
        parcel.writeString(this.type);
        parcel.writeInt(this.value);
    }

    public static final Creator<Alternative> CREATOR = new Parcelable.Creator<>() {
        @Override
        public Alternative createFromParcel(Parcel parcel) {
            return new Alternative(parcel);
        }

        @Override
        public Alternative[] newArray(int i) {
            return new Alternative[i];
        }
    };
}
