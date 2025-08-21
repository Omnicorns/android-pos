// 
// Decompiled by Procyon v0.5.36
// 

package com.cashlez.android.sdk.companion.printer;

//import com.cashlez.android.sdk.companion.ICLBluetoothSupportHandler;

public interface ICLPrinterConnection
//        extends ICLBluetoothSupportHandler
{
    void onNoPrinterConnected(final CLPrinterCompanion p0);
    
    void onPrinterConnected(final CLPrinterCompanion p0);
    
    void onPrinterDisconnected(final CLPrinterCompanion p0, final boolean p1);
    
    void onPrinterConnecting(final CLPrinterCompanion p0);
    
    void onPrintingOnProgress();
    
    void onPrintingFailed();
    
    void onPrintingFinished();
    
    void onPaperEmpty();
    
    void onOverHeat();
    
    void onBatteryLow();
}
