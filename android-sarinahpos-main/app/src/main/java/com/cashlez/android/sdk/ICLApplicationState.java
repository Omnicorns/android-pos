// 
// Decompiled by Procyon v0.5.36
// 

package com.cashlez.android.sdk;

import android.graphics.Bitmap;
import com.cashlez.android.sdk.companion.printer.CLPrinterCompanion;
import android.content.Context;

public interface ICLApplicationState
{
    Context getCurrentContext();
    
    String getSessionKey();
    
    void setSessionKey(final String p0);
    

    CLPropertiesHolder getPropertyHolder();
    

    void setAggregatorId(final String p0);
    
    String getAggregatorId();
    
    void SetPlayServiceAvailable(final boolean p0);
    
    boolean isPlayServiceAvailable();
    

    void setPrinterCompanion(final CLPrinterCompanion p0);
    
    CLPrinterCompanion getPrinterCompanion();
    
    boolean isGpn();
    
    void setGpn(final boolean p0);
}
