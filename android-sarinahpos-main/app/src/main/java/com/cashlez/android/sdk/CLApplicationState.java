// 
// Decompiled by Procyon v0.5.36
// 

package com.cashlez.android.sdk;

import com.cashlez.android.sdk.util.CLDeviceUtil;
import com.google.android.gms.common.GoogleApiAvailability;
import java.net.CookieHandler;
import java.net.CookieManager;
import android.content.Context;
import android.graphics.Bitmap;
import java.security.PrivateKey;
import com.cashlez.android.sdk.companion.printer.CLPrinterCompanion;
import android.app.Activity;
import android.annotation.SuppressLint;

public class CLApplicationState implements ICLApplicationState
{
    @SuppressLint({ "StaticFieldLeak" })
    private static CLApplicationState ourInstance;
    private Activity context;
    private String sessionKey;
    private String oAuthToken;
    private String aggregatorId;
    private boolean isPlayServiceAvailable;
    private CLPrinterCompanion printerCompanion;
    private boolean isGpn;
    private PrivateKey privateKey;
    private Bitmap merchantLogo;
    
    private CLApplicationState(final Context context) {
        this.context = (Activity)context;
        final CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        if (this.isGooglePlayServicesAvailable(context)) {
            this.SetPlayServiceAvailable(true);
        }
        else {
            this.SetPlayServiceAvailable(false);
        }
    }
    
    static CLApplicationState getInstance(final Context context) {
        if (CLApplicationState.ourInstance == null) {
            CLApplicationState.ourInstance = new CLApplicationState(context);
        }
        return CLApplicationState.ourInstance;
    }
    
    private boolean isGooglePlayServicesAvailable(final Context context) {
        final GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        final int resultCode = api.isGooglePlayServicesAvailable(context);
        return resultCode == 0;
    }
    
    @Override
    public Context getCurrentContext() {
        return (Context)this.context;
    }
    
    @Override
    public String getSessionKey() {
        return this.sessionKey;
    }
    
    @Override
    public void setSessionKey(final String key) {
        this.sessionKey = key;
    }
    

    @Override
    public CLPropertiesHolder getPropertyHolder() {
        return new CLPropertiesHolder();
    }
    
    @Override
    public String getAggregatorId() {
        return this.aggregatorId;
    }
    
    @Override
    public void setAggregatorId(final String aggregatorId) {
        this.aggregatorId = aggregatorId;
    }
    
    @Override
    public boolean isPlayServiceAvailable() {
        return this.isPlayServiceAvailable;
    }
    
    @Override
    public void SetPlayServiceAvailable(final boolean isPlayServiceAvailable) {
        this.isPlayServiceAvailable = isPlayServiceAvailable;
    }
    

    @Override
    public CLPrinterCompanion getPrinterCompanion() {
        return this.printerCompanion;
    }
    
    @Override
    public void setPrinterCompanion(final CLPrinterCompanion printerCompanion) {
        this.printerCompanion = printerCompanion;
    }
    

    @Override
    public boolean isGpn() {
        return this.isGpn;
    }
    
    @Override
    public void setGpn(final boolean gpn) {
        this.isGpn = gpn;
    }
    
}
