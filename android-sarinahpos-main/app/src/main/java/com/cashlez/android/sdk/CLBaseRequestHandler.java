// 
// Decompiled by Procyon v0.5.36
// 

package com.cashlez.android.sdk;

import com.cashlez.android.sdk.service.CLErrorStatus;
import android.content.Context;

public abstract class CLBaseRequestHandler
{
    protected ICLApplicationState applicationState;
    protected Context context;
    
    public CLBaseRequestHandler(final Context context) {
        this.context = context;
        this.applicationState = CLApplicationState.getInstance(context);
    }
    


    protected CLErrorResponse sessionNotValidResponse() {
        final CLErrorResponse response = new CLErrorResponse();
        response.setErrorCode(CLErrorStatus.SESSION_TIMED_OUT.getCode());
        response.setErrorMessage(this.applicationState.getCurrentContext().getResources().getString(CLErrorStatus.ERROR_CONNECTION_TIMED_OUT.getDefaultMessage()));
        return response;
    }
    

    protected CLErrorResponse noNetworkResponse() {
        final CLErrorResponse errorResponse = new CLErrorResponse();
        errorResponse.setErrorCode(CLErrorStatus.ERROR_CONNECTION_TIMED_OUT.getCode());
        errorResponse.setErrorMessage(this.applicationState.getCurrentContext().getResources().getString(CLErrorStatus.ERROR_CONNECTION_TIMED_OUT.getDefaultMessage()));
        return errorResponse;
    }
}
