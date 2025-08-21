// 
// Decompiled by Procyon v0.5.36
// 

package com.cashlez.android.sdk.companion;

import android.os.Parcel;
import android.os.Parcelable;

public class CLCompanion implements Parcelable
{
    protected String companionName;
    protected String btAddress;
    protected boolean isConnected;
    protected boolean isHybrid;
    protected boolean isConnectedViaBT;
    protected String message;
    private int batteryPercentage;
    private boolean charging;
    private CLConnectionStatus connectionStatus;
    public static final Parcelable.Creator<CLCompanion> CREATOR;
    
    public CLCompanion() {
        this.companionName = "";
        this.btAddress = "";
        this.connectionStatus = CLConnectionStatus.DISCONNECTED;
    }
    
    protected CLCompanion(final Parcel in) {
        this.companionName = "";
        this.btAddress = "";
        this.connectionStatus = CLConnectionStatus.DISCONNECTED;
        this.companionName = in.readString();
        this.btAddress = in.readString();
        this.isConnected = (in.readByte() != 0);
        this.isHybrid = (in.readByte() != 0);
        this.isConnectedViaBT = (in.readByte() != 0);
        this.message = in.readString();
        this.batteryPercentage = in.readInt();
        this.charging = (in.readByte() != 0);
    }
    
    public int describeContents() {
        return 0;
    }
    
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(this.companionName);
        dest.writeString(this.btAddress);
        dest.writeByte((byte)(byte)(this.isConnected ? 1 : 0));
        dest.writeByte((byte)(byte)(this.isHybrid ? 1 : 0));
        dest.writeByte((byte)(byte)(this.isConnectedViaBT ? 1 : 0));
        dest.writeString(this.message);
        dest.writeInt(this.batteryPercentage);
        dest.writeByte((byte)(byte)(this.charging ? 1 : 0));
    }
    
    public String getCompanionName() {
        return this.companionName;
    }
    
    public void setCompanionName(final String companionName) {
        this.companionName = companionName;
    }
    
    public String getBtAddress() {
        return this.btAddress;
    }
    
    public void setBtAddress(final String btAddress) {
        this.btAddress = btAddress;
    }
    
    public boolean isConnected() {
        return this.isConnected;
    }
    
    public void setConnected(final boolean connected) {
        this.isConnected = connected;
    }
    
    public boolean isHybrid() {
        return this.isHybrid;
    }
    
    public void setHybrid(final boolean hybrid) {
        this.isHybrid = hybrid;
    }
    
    public boolean isConnectedViaBT() {
        return this.isConnectedViaBT;
    }
    
    public void setConnectedViaBT(final boolean connectedViaBT) {
        this.isConnectedViaBT = connectedViaBT;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
    
    public int getBatteryPercentage() {
        return this.batteryPercentage;
    }
    
    public void setBatteryPercentage(final int batteryPercentage) {
        this.batteryPercentage = batteryPercentage;
    }
    
    public boolean isCharging() {
        return this.charging;
    }
    
    public void setCharging(final boolean charging) {
        this.charging = charging;
    }
    
    public CLConnectionStatus getConnectionStatus() {
        return this.connectionStatus;
    }
    
    public void setConnectionStatus(final CLConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }
    
    static {
        CREATOR = (Parcelable.Creator)new Parcelable.Creator<CLCompanion>() {
            public CLCompanion createFromParcel(final Parcel in) {
                return new CLCompanion(in);
            }
            
            public CLCompanion[] newArray(final int size) {
                return new CLCompanion[size];
            }
        };
    }
}
