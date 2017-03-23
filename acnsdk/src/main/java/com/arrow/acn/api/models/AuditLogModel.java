package com.arrow.acn.api.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuditLogModel implements Parcelable {

    @SerializedName("createdBy")
    @Expose
    private String createdBy;
    @SerializedName("createdString")
    @Expose
    private String createdString;
    @SerializedName("objectHid")
    @Expose
    private String objectHid;
    @SerializedName("parameters")
    @Expose
    private Parameters parameters;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("type")
    @Expose
    private String type;

    /**
     *
     * @return
     * The createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     *
     * @param createdBy
     * The createdBy
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     *
     * @return
     * The createdString
     */
    public String getCreatedString() {
        return createdString;
    }

    /**
     *
     * @param String
     * The String
     */
    public void setCreatedString(String String) {
        this.createdString = String;
    }

    /**
     *
     * @return
     * The objectHid
     */
    public String getObjectHid() {
        return objectHid;
    }

    /**
     *
     * @param objectHid
     * The objectHid
     */
    public void setObjectHid(String objectHid) {
        this.objectHid = objectHid;
    }

    /**
     *
     * @return
     * The parameters
     */
    public Parameters getParameters() {
        return parameters;
    }

    /**
     *
     * @param parameters
     * The parameters
     */
    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    /**
     *
     * @return
     * The productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     *
     * @param productName
     * The productName
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }


    protected AuditLogModel(@NonNull Parcel in) {
        createdBy = in.readString();
        createdString = (String) in.readValue(String.class.getClassLoader());
        objectHid = in.readString();
        parameters = (Parameters) in.readValue(Parameters.class.getClassLoader());
        productName = in.readString();
        type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(createdBy);
        dest.writeValue(createdString);
        dest.writeString(objectHid);
        dest.writeValue(parameters);
        dest.writeString(productName);
        dest.writeString(type);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<AuditLogModel> CREATOR = new Parcelable.Creator<AuditLogModel>() {
        @NonNull
        @Override
        public AuditLogModel createFromParcel(@NonNull Parcel in) {
            return new AuditLogModel(in);
        }

        @NonNull
        @Override
        public AuditLogModel[] newArray(int size) {
            return new AuditLogModel[size];
        }
    };
}