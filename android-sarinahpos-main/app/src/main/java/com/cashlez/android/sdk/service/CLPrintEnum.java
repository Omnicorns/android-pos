// 
// Decompiled by Procyon v0.5.36
// 

package com.cashlez.android.sdk.service;

public enum CLPrintEnum implements CLBaseEnum
{
    NORMAL(0), 
    NORMAL_BIG(1), 
    BOLD(2), 
    TITLE(3), 
    BARCODE(4), 
    QR_CODE(5), 
    SMALL_LOGO(6);
    
    private final int applicationType;
    
    private CLPrintEnum(final int applicationType) {
        this.applicationType = applicationType;
    }
    
    @Override
    public int getValue() {
        return this.applicationType;
    }
    
    @Override
    public String getDisplayValue() {
        return String.valueOf(this.applicationType);
    }
}
