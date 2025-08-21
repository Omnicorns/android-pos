// 
// Decompiled by Procyon v0.5.36
// 

package com.cashlez.android.sdk.companion.printer;

import android.graphics.Bitmap;
import com.cashlez.android.sdk.model.CLPrintObject;
import java.util.ArrayList;

public interface ICLPrinterController
{
    void doRegisterPrinterReceiver(final ICLPrinterConnection p0);
    
    void doInitPrinterConnection(final CLPrinterCompanion p0);
    

//    void doPrintFreeText(final ArrayList<CLPrintObject> p0);
    

    void doPrintQRContent(final Bitmap p0);

    void doPrintBarcode(final Bitmap p0);
    
    void doUnregisterPrinterReceiver();
    
    void doClosePrinterConnection();
}
