// 
// Decompiled by Procyon v0.5.36
// 

package com.cashlez.android.sdk.printing;

import com.cashlez.android.sdk.model.CLPrintObject;
import java.util.ArrayList;
import android.graphics.Bitmap;
import com.cashlez.android.sdk.util.CLLoggingHelper;
import androidx.annotation.NonNull;

import com.cashlez.android.sdk.CLErrorResponse;
import com.cashlez.android.sdk.service.CLErrorStatus;
import com.cashlez.android.sdk.CLPropertiesHolder;
import com.cashlez.android.sdk.companion.printer.CLPrinterCompanionHandler;
import com.cashlez.android.sdk.companion.printer.CLPrinterTypeEnum;
import com.cashlez.android.sdk.util.CLDeviceUtil;
import android.content.Context;
import com.cashlez.android.sdk.companion.printer.CLPrinterCompanion;
import com.cashlez.android.sdk.companion.printer.ICLPrinterHandler;
import com.cashlez.android.sdk.companion.printer.ICLPrinterConnection;
import com.cashlez.android.sdk.CLBaseRequestHandler;
import com.sarinah.pos.R;


public class CLPrinterHandler extends CLBaseRequestHandler implements ICLPrintingHandler, ICLPrinterConnection
{
    private static final String TAG;
    private ICLPrinterHandler printerHandler;
    private ICLPrinterService printerService;
    private boolean isRestart;
    private String[] printerCompanion;
    private CLPrinterCompanion printer;
    
    public CLPrinterHandler(final Context context) {
        super(context);
        if (CLDeviceUtil.isSunmiP1()) {
            (this.printer = new CLPrinterCompanion()).setPrinterTypeEnum(CLPrinterTypeEnum.SUNMI_P1_PRINTER);
            this.printer.setCompanionName("" + CLPrinterTypeEnum.SUNMI_P1_PRINTER);
            this.printer.setConnectedViaBT(false);
            this.printer.setHybrid(false);
            this.printer.setConnected(true);
            this.applicationState.setPrinterCompanion(this.printer);
        }
        else {
            this.applicationState.setPrinterCompanion(this.emptyPrinterCompanion());
        }
        this.printerHandler = new CLPrinterCompanionHandler(context, this.applicationState);
    }
    
    private CLPrinterCompanion getPrinter(final String[] printerCompanion) {
        if (this.printer != null) {
            this.printer.setCompanionName(printerCompanion[0]);
            this.printer.setBtAddress(printerCompanion[1]);
            this.printer.setPrinterTypeEnum(CLDeviceUtil.getPrinterTypeEnum(printerCompanion[0]));
            final CLPropertiesHolder holder = new CLPropertiesHolder();
            final String[] readerPrefixes = holder.getPrinterNamePrefixes();
            if (printerCompanion[0].startsWith(readerPrefixes[1])) {
                this.printer.setHybrid(true);
            }
        }
        return this.printer;
    }
    
    @Override
    public void onNoPrinterConnected(final CLPrinterCompanion printerCompanion) {
        this.applicationState.setPrinterCompanion(printerCompanion);
        final CLErrorResponse errorResponse = this.printerFailedResponse(CLErrorStatus.NO_PRINTER_PAIRED.getCode(), this.applicationState.getCurrentContext().getResources().getString(CLErrorStatus.NO_PRINTER_PAIRED.getDefaultMessage()));
        this.printerService.onPrintingError(errorResponse);
    }
    
    @NonNull
    private CLPrinterCompanion emptyPrinterCompanion() {
        final CLPrinterCompanion printer = new CLPrinterCompanion();
        printer.setConnected(false);
        printer.setMessage(this.context.getResources().getString(R.string.no_printer_paired));
        return printer;
    }
    
    @Override
    public void onPrinterConnected(final CLPrinterCompanion updatedPrinterCompanion) {
        if (this.applicationState.getPrinterCompanion() != null) {
            this.applicationState.getPrinterCompanion().setConnected(updatedPrinterCompanion.isConnected());
            this.applicationState.getPrinterCompanion().setMessage(updatedPrinterCompanion.getMessage());
            this.printerService.onPrintingSuccess(this.applicationState.getPrinterCompanion());
        }
        else if (updatedPrinterCompanion != null) {
            if (this.applicationState.getPrinterCompanion() == null) {
                this.applicationState.setPrinterCompanion(this.getPrinter(this.printerCompanion));
            }
            else if (CLDeviceUtil.isSunmiP1()) {
                (this.printer = new CLPrinterCompanion()).setPrinterTypeEnum(CLPrinterTypeEnum.SUNMI_P1_PRINTER);
                this.printer.setCompanionName("" + CLPrinterTypeEnum.SUNMI_P1_PRINTER);
                this.printer.setConnectedViaBT(false);
                this.printer.setHybrid(false);
                this.applicationState.setPrinterCompanion(this.printer);
            }
        }
        else if (CLDeviceUtil.isSunmiP1()) {
            (this.printer = new CLPrinterCompanion()).setPrinterTypeEnum(CLPrinterTypeEnum.SUNMI_P1_PRINTER);
            this.printer.setCompanionName("" + CLPrinterTypeEnum.SUNMI_P1_PRINTER);
            this.printer.setConnectedViaBT(false);
            this.printer.setHybrid(false);
            this.applicationState.setPrinterCompanion(this.printer);
        }
        else {
            this.applicationState.setPrinterCompanion(this.emptyPrinterCompanion());
            this.applicationState.getPrinterCompanion().setConnected(updatedPrinterCompanion.isConnected());
            this.applicationState.getPrinterCompanion().setMessage(updatedPrinterCompanion.getMessage());
            this.printerService.onPrintingSuccess(this.applicationState.getPrinterCompanion());
        }
    }
    
    @Override
    public void onPrintingFinished() {
        this.isRestart = false;
        this.applicationState.getPrinterCompanion().setConnected(true);
        this.applicationState.getPrinterCompanion().setMessage(this.context.getResources().getString(R.string.printing_finished));
        this.printerService.onPrintingSuccess(this.applicationState.getPrinterCompanion());
    }
    
    @Override
    public void onPrinterDisconnected(final CLPrinterCompanion printerCompanion, final boolean isManualClose) {
        if (this.applicationState.getPrinterCompanion() != null) {
            this.applicationState.getPrinterCompanion().setConnected(printerCompanion.isConnected());
            this.applicationState.getPrinterCompanion().setMessage(printerCompanion.getMessage());
        }
        final CLErrorResponse errorResponse = this.printerFailedResponse(CLErrorStatus.PRINTER_DISCONNECTED.getCode(), this.applicationState.getCurrentContext().getResources().getString(CLErrorStatus.PRINTER_DISCONNECTED.getDefaultMessage()));
        this.printerService.onPrintingError(errorResponse);
        if (isManualClose) {
            this.applicationState.setPrinterCompanion(null);
        }
    }
    
    @Override
    public void onPrinterConnecting(final CLPrinterCompanion printerCompanion) {
        this.applicationState.getPrinterCompanion().setConnected(printerCompanion.isConnected());
        this.applicationState.getPrinterCompanion().setMessage(printerCompanion.getMessage());
        this.printerService.onPrintingSuccess(this.applicationState.getPrinterCompanion());
    }
    
    @Override
    public void onPaperEmpty() {
        this.applicationState.getPrinterCompanion().setMessage(this.context.getResources().getString(R.string.paper_empty));
        this.applicationState.getPrinterCompanion().setConnected(true);
        this.printerService.onPrintingSuccess(this.applicationState.getPrinterCompanion());
    }
    
    @Override
    public void onOverHeat() {
        this.applicationState.getPrinterCompanion().setMessage(this.context.getResources().getString(R.string.printer_overheat));
        this.applicationState.getPrinterCompanion().setConnected(true);
        this.printerService.onPrintingSuccess(this.applicationState.getPrinterCompanion());
    }
    
    @Override
    public void onPrintingOnProgress() {
        this.applicationState.getPrinterCompanion().setMessage(this.context.getResources().getString(R.string.printing_on_progress));
        this.applicationState.getPrinterCompanion().setConnected(true);
        this.printerService.onPrintingSuccess(this.applicationState.getPrinterCompanion());
    }
    
    @Override
    public void onPrintingFailed() {
        this.applicationState.getPrinterCompanion().setMessage(this.context.getResources().getString(R.string.printing_failed));
        this.applicationState.getPrinterCompanion().setConnected(false);
        final CLErrorResponse errorResponse = this.printerFailedResponse(CLErrorStatus.PRINTER_FAILED.getCode(), this.applicationState.getCurrentContext().getResources().getString(CLErrorStatus.PRINTER_FAILED.getDefaultMessage()));
        this.printerService.onPrintingError(errorResponse);
    }
    
    @NonNull
    private CLErrorResponse printerFailedResponse(final int code, final String string) {
        final CLErrorResponse errorResponse = new CLErrorResponse();
        errorResponse.setErrorCode(code);
        errorResponse.setErrorMessage(string);
        return errorResponse;
    }
    

    
    @Override
    public void onBatteryLow() {
        if (this.applicationState.getPrinterCompanion() != null) {
            this.applicationState.getPrinterCompanion().setMessage(this.context.getResources().getString(R.string.battery_low));
            this.applicationState.getPrinterCompanion().setConnected(true);
            this.printerService.onPrintingSuccess(this.applicationState.getPrinterCompanion());
        }
    }
    
    @Override
    public void doInitPrinterConnection(final ICLPrinterService printingService) {
        this.printerService = printingService;
        if (this.applicationState.getPrinterCompanion() != null) {
            this.printerHandler.doInitPrinterConnection(this, this.applicationState.getPrinterCompanion());
        }
        else {
            final CLErrorResponse errorResponse = this.printerFailedResponse(CLErrorStatus.NO_PRINTER_PAIRED.getCode(), this.applicationState.getCurrentContext().getResources().getString(CLErrorStatus.NO_PRINTER_PAIRED.getDefaultMessage()));
            this.printerService.onPrintingError(errorResponse);
        }
    }
    
    @Override
    public void doCheckPrinterCompanion() {
        try {
            this.printerService.onPrintingSuccess(this.applicationState.getPrinterCompanion());
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            CLLoggingHelper.verbose(CLPrinterHandler.TAG, e.getMessage());
            CLLoggingHelper.verbose(CLPrinterHandler.TAG, "ERROR ");
        }
    }
    

    @Override
    public void doPrintQRCode(final Bitmap paymentResponse) {
        if (!this.applicationState.getPrinterCompanion().isConnected()) {
            this.applicationState.getPrinterCompanion().setMessage(this.context.getString(R.string.printer_disconnected));
            this.applicationState.getPrinterCompanion().setConnected(false);
            final CLErrorResponse errorResponse = this.printerFailedResponse(CLErrorStatus.PRINTER_DISCONNECTED.getCode(), this.applicationState.getCurrentContext().getResources().getString(CLErrorStatus.PRINTER_DISCONNECTED.getDefaultMessage()));
            this.printerService.onPrintingError(errorResponse);
            return;
        }
        if (paymentResponse != null && CLDeviceUtil.isSunmiP1()) {
            if (this.isRestart) {
                this.printerHandler.doInitPrinterConnection(this, this.applicationState.getPrinterCompanion());
            }
            this.printer.setBtAddress(null);
            this.printerHandler.doPrintQRCodeContent(this.applicationState.getPrinterCompanion(), paymentResponse);
        }
        else if (paymentResponse != null && CLDeviceUtil.isSunmiP2Lite()) {
            if (this.isRestart) {
                this.printerHandler.doInitPrinterConnection(this, this.applicationState.getPrinterCompanion());
            }
            this.printer.setBtAddress(null);
            this.printerHandler.doPrintQRCodeContent(this.applicationState.getPrinterCompanion(), paymentResponse);
        }
    }

    

    @Override
    public void doUnregisterPrinterReceiver() {
        if (this.applicationState.getPrinterCompanion() != null) {
            this.printerHandler.doUnRegisterPrinterReceiver(this.applicationState.getPrinterCompanion());
        }
    }
    
    @Override
    public void doClosePrinterConnection() {
        if (this.applicationState.getPrinterCompanion() != null) {
            this.printerHandler.doCloseConnection(this.applicationState.getPrinterCompanion());
        }
    }
    
    static {
        TAG = CLPrinterHandler.class.getName();
    }
}
