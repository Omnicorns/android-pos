// 
// Decompiled by Procyon v0.5.36
// 

package com.cashlez.android.sdk.printing;

import com.cashlez.android.sdk.CLErrorResponse;
import com.cashlez.android.sdk.companion.printer.CLPrinterCompanion;

public interface ICLPrinterService
{
    void onPrintingSuccess(final CLPrinterCompanion p0);
    
    void onPrintingError(final CLErrorResponse p0);
}
