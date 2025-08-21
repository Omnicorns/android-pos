// 
// Decompiled by Procyon v0.5.36
// 

package com.cashlez.android.sdk;

import android.os.Parcel;
import android.os.Parcelable;

public class CLErrorResponse implements Parcelable
{
    private int errorCode;
    private String errorMessage;
    private int hostErrorCode;
    private String hostErrorMessage;
    private int httpStatusCode;
    public static final Parcelable.Creator<CLErrorResponse> CREATOR;
    
    public CLErrorResponse() {
    }
    
    public CLErrorResponse(final int errorCode, final String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
    
    protected CLErrorResponse(final Parcel in) {
        this.errorCode = in.readInt();
        this.errorMessage = in.readString();
        this.hostErrorCode = in.readInt();
        this.hostErrorMessage = in.readString();
        this.httpStatusCode = in.readInt();
    }
    
    public int describeContents() {
        return 0;
    }
    
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeInt(this.errorCode);
        dest.writeString(this.errorMessage);
        dest.writeInt(this.hostErrorCode);
        dest.writeString(this.hostErrorMessage);
        dest.writeInt(this.httpStatusCode);
    }
    
    public int getErrorCode() {
        return this.errorCode;
    }
    
    public void setErrorCode(final int errorCode) {
        this.errorCode = errorCode;
    }
    
    public String getErrorMessage() {
        return this.errorMessage;
    }
    
    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public int getHostErrorCode() {
        return this.hostErrorCode;
    }
    
    public void setHostErrorCode(final int hostErrorCode) {
        this.hostErrorCode = hostErrorCode;
    }
    
    public String getHostErrorMessage() {
        return this.hostErrorMessage;
    }
    
    public void setHostErrorMessage(final String hostErrorMessage) {
        this.hostErrorMessage = hostErrorMessage;
    }
    
    public int getHttpStatusCode() {
        return this.httpStatusCode;
    }
    
    public void setHttpStatusCode(final int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }
    
    @Override
    public String toString() {
        return "CLErrorResponse{errorCode=" + this.errorCode + ", errorMessage='" + this.errorMessage + '\'' + ", hostErrorCode=" + this.hostErrorCode + ", hostErrorMessage='" + this.hostErrorMessage + '\'' + ", httpStatusCode=" + this.httpStatusCode + '}';
    }
    
    static {
        CREATOR = (Parcelable.Creator)new Parcelable.Creator<CLErrorResponse>() {
            public CLErrorResponse createFromParcel(final Parcel in) {
                return new CLErrorResponse(in);
            }
            
            public CLErrorResponse[] newArray(final int size) {
                return new CLErrorResponse[size];
            }
        };
    }
}
