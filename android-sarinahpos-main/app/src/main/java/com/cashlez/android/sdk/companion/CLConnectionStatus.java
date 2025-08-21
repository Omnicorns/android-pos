// 
// Decompiled by Procyon v0.5.36
// 

package com.cashlez.android.sdk.companion;

public enum CLConnectionStatus
{
    CONNECTING(0), 
    CONNECTED(1), 
    DISCONNECTED(2);
    
    private final int value;
    
    private CLConnectionStatus(final int value) {
        this.value = value;
    }
    
    public int getValue() {
        return this.value;
    }
}
