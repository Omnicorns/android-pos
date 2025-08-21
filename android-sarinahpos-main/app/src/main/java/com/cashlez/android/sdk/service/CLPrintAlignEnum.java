// 
// Decompiled by Procyon v0.5.36
// 

package com.cashlez.android.sdk.service;

public enum CLPrintAlignEnum implements CLBaseEnum
{
    LEFT(0), 
    CENTER(1), 
    RIGHT(2);
    
    private final int applicationType;
    
    private CLPrintAlignEnum(final int applicationType) {
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
