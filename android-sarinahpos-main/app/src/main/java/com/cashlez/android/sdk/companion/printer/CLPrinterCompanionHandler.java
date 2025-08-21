// 
// Decompiled by Procyon v0.5.36
// 

package com.cashlez.android.sdk.companion.printer;

import com.cashlez.android.sdk.model.CLPrintObject;
import java.util.ArrayList;
import android.graphics.Bitmap;
import com.cashlez.android.sdk.util.CLDeviceUtil;

import com.cashlez.android.sdk.companion.reader.sunmi.CLSunmiP1PrinterHandler;
import java.util.HashMap;
import android.content.Context;
import com.cashlez.android.sdk.ICLApplicationState;
import com.sarinah.pos.R;


public class CLPrinterCompanionHandler implements ICLPrinterHandler
{
    private ICLApplicationState applicationState;
    private ICLPrinterConnection printerConnection;
    private CLPrinterCompanion printerCompanion;
    private Context context;
    private HashMap<CLPrinterTypeEnum, ICLPrinterController> printerTypes;
    
    public CLPrinterCompanionHandler(final Context context, final ICLApplicationState applicationState) {
        this.printerTypes = new HashMap<CLPrinterTypeEnum, ICLPrinterController>();
        this.applicationState = applicationState;
        this.printerTypes.put(CLPrinterTypeEnum.SUNMI_P1_PRINTER, new CLSunmiP1PrinterHandler(context));
        this.printerTypes.put(CLPrinterTypeEnum.SUNMI_P2_PRINTER, new CLSunmiP1PrinterHandler(context));
        this.printerTypes.put(CLPrinterTypeEnum.SUNMI_V1_PRINTER, new CLSunmiP1PrinterHandler(context));
    }
    
    @Override
    public void doInitPrinterConnection(final ICLPrinterConnection printerConnection, final CLPrinterCompanion printerCompanion) {
        this.printerConnection = printerConnection;
        if (printerCompanion.getPrinterTypeEnum() != null) {
            this.printerTypes.get(printerCompanion.getPrinterTypeEnum()).doRegisterPrinterReceiver(printerConnection);
            this.printerTypes.get(printerCompanion.getPrinterTypeEnum()).doInitPrinterConnection(printerCompanion);
        }
        else {
            final CLPrinterCompanion printer = new CLPrinterCompanion();
            printer.setConnected(false);
            printer.setMessage(this.applicationState.getCurrentContext().getString(R.string.no_printer_paired));
            printerConnection.onNoPrinterConnected(printer);
        }
    }
    

    @Override
    public void doPrintQRCodeContent(final CLPrinterCompanion printerCompanion, final Bitmap bitmap) {
        if (printerCompanion != null && CLDeviceUtil.isSunmiP1()) {
            this.printerTypes.get(printerCompanion.getPrinterTypeEnum()).doPrintQRContent(bitmap);
            return;
        }
        final CLPrinterCompanion printer = new CLPrinterCompanion();
        printer.setConnected(false);
        printer.setMessage(this.applicationState.getCurrentContext().getString(R.string.no_printer_paired));
        this.printerConnection.onNoPrinterConnected(printer);
    }


    @Override
    public void doPrintBarcode(final CLPrinterCompanion printerCompanion, final Bitmap barcodeValue) {
        if (printerCompanion == null) {
            final CLPrinterCompanion printer = new CLPrinterCompanion();
            printer.setConnected(false);
            printer.setMessage(this.applicationState.getCurrentContext().getString(R.string.no_printer_paired));
            this.printerConnection.onNoPrinterConnected(printer);
            return;
        }
        this.printerTypes.get(printerCompanion.getPrinterTypeEnum()).doPrintBarcode(barcodeValue);
    }
    
    @Override
    public void doUnRegisterPrinterReceiver(final CLPrinterCompanion printerCompanion) {
        if (printerCompanion.getPrinterTypeEnum() != null) {
            this.printerTypes.get(printerCompanion.getPrinterTypeEnum()).doUnregisterPrinterReceiver();
        }
    }
    
    @Override
    public void doCloseConnection(final CLPrinterCompanion printerCompanion) {
        if (printerCompanion.getPrinterTypeEnum() != null) {
            this.printerTypes.get(printerCompanion.getPrinterTypeEnum()).doClosePrinterConnection();
        }
    }
}
