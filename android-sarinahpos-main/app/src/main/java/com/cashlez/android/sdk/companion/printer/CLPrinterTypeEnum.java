// 
// Decompiled by Procyon v0.5.36
// 

package com.cashlez.android.sdk.companion.printer;

public enum CLPrinterTypeEnum
{
    NONE(0), 
    DATEC_250_PRINTER(1), 
    DATEC_350_PRINTER(2), 
    FUJITSU_PRINTER(3), 
    ZONERICH_PRINTER(4), 
    BBPOS_PRINTER(5), 
    BBPOS_SIMPLY_PRINTER(6), 
    SUNMI_P1_PRINTER(7), 
    SUNMI_P2_PRINTER(8), 
    SUNMI_V1_PRINTER(9);
    
    private final int printerType;
    
    private CLPrinterTypeEnum(final int printerType) {
        this.printerType = printerType;
    }
    
    public int getPrinterType() {
        return this.printerType;
    }
}
