package com.arrow.kronos.api.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreatedDate implements Parcelable {

    @SerializedName("epochSecond")
    @Expose
    private Integer epochSecond;
    @SerializedName("nano")
    @Expose
    private Integer nano;

    /**
     *
     * @return
     *     The epochSecond
     */
    public Integer getEpochSecond() {
        return epochSecond;
    }

    /**
     *
     * @param epochSecond
     *     The epochSecond
     */
    public void setEpochSecond(Integer epochSecond) {
        this.epochSecond = epochSecond;
    }

    /**
     *
     * @return
     *     The nano
     */
    public Integer getNano() {
        return nano;
    }

    /**
     *
     * @param nano
     *     The nano
     */
    public void setNano(Integer nano) {
        this.nano = nano;
    }


    protected CreatedDate(Parcel in) {
        epochSecond = in.readByte() == 0x00 ? null : in.readInt();
        nano = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (epochSecond == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(epochSecond);
        }
        if (nano == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(nano);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CreatedDate> CREATOR = new Parcelable.Creator<CreatedDate>() {
        @Override
        public CreatedDate createFromParcel(Parcel in) {
            return new CreatedDate(in);
        }

        @Override
        public CreatedDate[] newArray(int size) {
            return new CreatedDate[size];
        }
    };
}