// 
// Decompiled by Procyon v0.5.36
// 

package com.cashlez.android.sdk.model;

public enum CLDeviceTypeEnum
{
    CASH(0, "CASH"), 
    INGENICO_ICMP_122(1, "INGENICO_ICMP_122"), 
    BBPOS_WISEPAD2(2, "BBPOS_WISEPAD2"), 
    BBPOS_WISEPAD2_PLUS(3, "BBPOS_WISEPAD2_PLUS"), 
    SUNMI_P1(7, "SUNMI_P1"), 
    BBPOS_WISEPOS_PLUS(8, "BBPOS_WISEPOS_PLUS"), 
    SUNMI_P2_LITE(9, "SUNMI_P2_LITE"), 
    SUNMI_P2_PRO(10, "SUNMI_P2_PRO");
    
    private final int deviceType;
    private final String deviceName;
    
    private CLDeviceTypeEnum(final int deviceType, final String deviceName) {
        this.deviceType = deviceType;
        this.deviceName = deviceName;
    }
    
    public int getDeviceType() {
        return this.deviceType;
    }
    
    public String getDeviceName() {
        return this.deviceName;
    }
    
    @Override
    public String toString() {
        return "DeviceTypeEnum{deviceType=" + this.deviceType + ", deviceName='" + this.deviceName + '\'' + '}';
    }
}
