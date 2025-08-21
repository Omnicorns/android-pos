// 
// Decompiled by Procyon v0.5.36
// 

package com.cashlez.android.sdk.companion.printer;

import com.cashlez.android.sdk.model.CLPrintObject;
import java.util.ArrayList;
import android.graphics.Bitmap;

public interface ICLPrinterHandler
{
    void doInitPrinterConnection(final ICLPrinterConnection p0, final CLPrinterCompanion p1);
    

    void doPrintQRCodeContent(final CLPrinterCompanion p0, final Bitmap p1);
    

    void doPrintBarcode(final CLPrinterCompanion p0, final Bitmap p1);
    
    void doUnRegisterPrinterReceiver(final CLPrinterCompanion p0);
    
    void doCloseConnection(final CLPrinterCompanion p0);
}
