// 
// Decompiled by Procyon v0.5.36
// 

package com.cashlez.android.sdk.printing;

import android.graphics.Bitmap;
import com.cashlez.android.sdk.model.CLPrintObject;
import java.util.ArrayList;

public interface ICLPrintingHandler
{
    void doInitPrinterConnection(final ICLPrinterService p0);
    
    void doCheckPrinterCompanion();

    void doPrintQRCode(final Bitmap p0);
    
    void doUnregisterPrinterReceiver();

    void doClosePrinterConnection();
}
