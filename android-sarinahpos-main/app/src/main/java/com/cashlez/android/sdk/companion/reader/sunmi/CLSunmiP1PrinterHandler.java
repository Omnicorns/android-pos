// 
// Decompiled by Procyon v0.5.36
// 

package com.cashlez.android.sdk.companion.reader.sunmi;

import com.cashlez.android.sdk.util.ESCUtil;
import android.graphics.Bitmap;
import java.util.List;
import com.cashlez.android.sdk.model.CLPrintObject;
import java.util.ArrayList;

import android.content.Intent;
import android.os.RemoteException;
import woyou.aidlservice.jiuiv5.ICallback;
import com.cashlez.android.sdk.util.CLLoggingHelper;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.ServiceConnection;
import com.cashlez.android.sdk.util.CLDeviceUtil;
import com.cashlez.android.sdk.printing.ICLPrintingHandler;
import com.cashlez.android.sdk.companion.printer.CLPrinterCompanion;
import android.content.Context;
import woyou.aidlservice.jiuiv5.IWoyouService;
import com.cashlez.android.sdk.companion.CLConnectionStatus;
import com.cashlez.android.sdk.companion.printer.ICLPrinterConnection;
import android.annotation.SuppressLint;
import com.cashlez.android.sdk.companion.printer.ICLPrinterController;
import com.sarinah.pos.R;


public class CLSunmiP1PrinterHandler implements ICLPrinterController
{
    @SuppressLint({ "StaticFieldLeak" })
    private static ICLPrinterConnection printerConnection;
    private static CLConnectionStatus connectionStatus;
    private static IWoyouService woyouService;
    private final String TAG;
    protected Context context;
    private CLPrinterCompanion printerCompanion;
    private ICLPrintingHandler printingCallback;
    private CLDeviceUtil deviceUtil;
    private String bankCode;
    private ServiceConnection connService;
    
    public CLSunmiP1PrinterHandler(final Context context, final CLPrinterCompanion printerCompanion) {
        this.TAG = CLSunmiP1PrinterHandler.class.getSimpleName();
        this.bankCode = "";
        this.connService = (ServiceConnection)new ServiceConnection() {
            public void onServiceDisconnected(final ComponentName name) {
                CLSunmiP1PrinterHandler.woyouService = null;
            }
            
            public void onServiceConnected(final ComponentName name, final IBinder service) {
                CLSunmiP1PrinterHandler.woyouService = IWoyouService.Stub.asInterface(service);
                CLSunmiP1PrinterHandler.this.initPrinter();
                CLSunmiP1PrinterHandler.connectionStatus = CLConnectionStatus.CONNECTED;
                CLSunmiP1PrinterHandler.this.printerCompanion.setConnectionStatus(CLSunmiP1PrinterHandler.connectionStatus);
                CLSunmiP1PrinterHandler.printerConnection.onPrinterConnected(CLSunmiP1PrinterHandler.this.printerCompanion);
                CLLoggingHelper.verbose("SUNMIPRINTER", "SUNMIPRINTER :" + CLSunmiP1PrinterHandler.connectionStatus);
                CLLoggingHelper.verbose("SUNMIPRINTER", "SUNMIPRINTER :" + CLSunmiP1PrinterHandler.printerConnection);
                CLLoggingHelper.verbose("SUNMIPRINTER", "SUNMIPRINTER :" + service);
            }
        };
        this.context = context;
        this.printerCompanion = printerCompanion;
    }
    
    public CLSunmiP1PrinterHandler(final Context context) {
        this.TAG = CLSunmiP1PrinterHandler.class.getSimpleName();
        this.bankCode = "";
        this.connService = (ServiceConnection)new ServiceConnection() {
            public void onServiceDisconnected(final ComponentName name) {
                CLSunmiP1PrinterHandler.woyouService = null;
            }
            
            public void onServiceConnected(final ComponentName name, final IBinder service) {
                CLSunmiP1PrinterHandler.woyouService = IWoyouService.Stub.asInterface(service);
                CLSunmiP1PrinterHandler.this.initPrinter();
                CLSunmiP1PrinterHandler.connectionStatus = CLConnectionStatus.CONNECTED;
                CLSunmiP1PrinterHandler.this.printerCompanion.setConnectionStatus(CLSunmiP1PrinterHandler.connectionStatus);
                CLSunmiP1PrinterHandler.printerConnection.onPrinterConnected(CLSunmiP1PrinterHandler.this.printerCompanion);
                CLLoggingHelper.verbose("SUNMIPRINTER", "SUNMIPRINTER :" + CLSunmiP1PrinterHandler.connectionStatus);
                CLLoggingHelper.verbose("SUNMIPRINTER", "SUNMIPRINTER :" + CLSunmiP1PrinterHandler.printerConnection);
                CLLoggingHelper.verbose("SUNMIPRINTER", "SUNMIPRINTER :" + service);
            }
        };
        this.context = context;
    }
    
    public CLSunmiP1PrinterHandler() {
        this.TAG = CLSunmiP1PrinterHandler.class.getSimpleName();
        this.bankCode = "";
        this.connService = (ServiceConnection)new ServiceConnection() {
            public void onServiceDisconnected(final ComponentName name) {
                CLSunmiP1PrinterHandler.woyouService = null;
            }
            
            public void onServiceConnected(final ComponentName name, final IBinder service) {
                CLSunmiP1PrinterHandler.woyouService = IWoyouService.Stub.asInterface(service);
                CLSunmiP1PrinterHandler.this.initPrinter();
                CLSunmiP1PrinterHandler.connectionStatus = CLConnectionStatus.CONNECTED;
                CLSunmiP1PrinterHandler.this.printerCompanion.setConnectionStatus(CLSunmiP1PrinterHandler.connectionStatus);
                CLSunmiP1PrinterHandler.printerConnection.onPrinterConnected(CLSunmiP1PrinterHandler.this.printerCompanion);
                CLLoggingHelper.verbose("SUNMIPRINTER", "SUNMIPRINTER :" + CLSunmiP1PrinterHandler.connectionStatus);
                CLLoggingHelper.verbose("SUNMIPRINTER", "SUNMIPRINTER :" + CLSunmiP1PrinterHandler.printerConnection);
                CLLoggingHelper.verbose("SUNMIPRINTER", "SUNMIPRINTER :" + service);
            }
        };
    }
    
    private void initPrinter() {
        try {
            CLSunmiP1PrinterHandler.woyouService.printerInit(new ICallback() {
                @Override
                public void onRunResult(final boolean isSuccess) {
                    CLLoggingHelper.verbose(CLSunmiP1PrinterHandler.this.TAG, isSuccess + "");
                }
                
                @Override
                public void onReturnString(final String result) {
                    CLLoggingHelper.verbose(CLSunmiP1PrinterHandler.this.TAG, result);
                }
                
                @Override
                public void onRaiseException(final int code, final String msg) {
                    CLLoggingHelper.verbose(CLSunmiP1PrinterHandler.this.TAG, code + msg);
                }
                
                @Override
                public void onPrintResult(final int code, final String msg) {
                    CLLoggingHelper.verbose(CLSunmiP1PrinterHandler.this.TAG, code + msg);
                }
                
                public IBinder asBinder() {
                    return null;
                }
            });
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void doInitPrinterConnection(final CLPrinterCompanion printerCompanion) {
        this.printerCompanion = printerCompanion;
        if (CLDeviceUtil.isSunmiP2Lite()) {
            return;
        }
        if (this.printerCompanion.getConnectionStatus() == CLConnectionStatus.DISCONNECTED) {
            this.printerCompanion.setConnectionStatus(CLConnectionStatus.CONNECTING);
            final Intent intent = new Intent();
            intent.setPackage("woyou.aidlservice.jiuiv5");
            intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
            this.context.getApplicationContext().startService(intent);
            this.context.getApplicationContext().bindService(intent, this.connService, Context.BIND_AUTO_CREATE);
            return;
        }
        printerCompanion.setConnected(true);
        printerCompanion.setMessage(this.context.getString(R.string.printer_connecting));
        CLSunmiP1PrinterHandler.printerConnection.onPrinterConnected(printerCompanion);
        CLSunmiP1PrinterHandler.connectionStatus = CLConnectionStatus.CONNECTING;
    }
    
    @Override
    public void doRegisterPrinterReceiver(final ICLPrinterConnection printerConnection) {
        CLSunmiP1PrinterHandler.printerConnection = printerConnection;
    }
    
//    @Override
//    public void doPrintFreeText(final ArrayList<CLPrintObject> freeText) {
//        try {
//            CLSunmiP1PrinterHandler.woyouService.lineWrap(1, null);
//            final List<byte[]> receipts = new ArrayList<byte[]>();
//            final byte[] freeTextReceipt = CLReceiptUtility.genReceiptFreeText(freeText);
//            if (freeTextReceipt != null) {
//                receipts.add(freeTextReceipt);
//                CLSunmiP1PrinterHandler.printerConnection.onPrintingOnProgress();
//                for (int i = 0; i < receipts.size(); ++i) {
//                    CLLoggingHelper.verbose("SUNMIFREETEXT", "SUNMIFREETEXT :" + receipts.get(i));
//                }
//                receipts.clear();
//                CLSunmiP1PrinterHandler.printerConnection.onPrintingFinished();
//            }
//            else {
//                CLSunmiP1PrinterHandler.printerConnection.onPrintingFailed();
//            }
//        }
//        catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }
    

    @Override
    public void doPrintQRContent(final Bitmap bitmap) {
        this.printCodeQRBitmap(bitmap);
    }
    
    @Override
    public void doPrintBarcode(final Bitmap barcodeValue) {
    }
    
    private void print(final byte[] toBePrinted) {
        if (toBePrinted != null) {
            try {
                CLSunmiP1PrinterHandler.printerConnection.onPrintingOnProgress();
                CLSunmiP1PrinterHandler.printerConnection.onPrintingFinished();
            }
            catch (Exception e) {
                CLLoggingHelper.verbose(this.TAG, "Something wrong when printing: " + e);
                CLSunmiP1PrinterHandler.printerConnection.onPrintingFailed();
            }
        }
        else {
            CLSunmiP1PrinterHandler.printerConnection.onPrintingFailed();
        }
    }
    
    @Override
    public void doUnregisterPrinterReceiver() {
        try {
            if (CLSunmiP1PrinterHandler.woyouService != null) {
                this.context.getApplicationContext().unbindService(this.connService);
                CLSunmiP1PrinterHandler.woyouService = null;
            }
            this.printerCompanion.setConnectionStatus(CLConnectionStatus.DISCONNECTED);
            CLSunmiP1PrinterHandler.printerConnection.onPrinterDisconnected(this.printerCompanion, false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void doClosePrinterConnection() {
        this.doUnregisterPrinterReceiver();
    }
    

    private void printCodeQRBitmap(final Bitmap bitmap) {
        try {
            CLSunmiP1PrinterHandler.woyouService.setAlignment(1, null);
            CLSunmiP1PrinterHandler.woyouService.printBitmap(bitmap, null);
            CLSunmiP1PrinterHandler.woyouService.lineWrap(1, null);
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    private ICallback generateCB() {
        return new ICallback.Stub() {
            public void onRunResult(final boolean isSuccess) {
                if (isSuccess) {
                    CLSunmiP1PrinterHandler.printerConnection.onPrintingFinished();
                }
            }
            
            public void onReturnString(final String result) {
                CLLoggingHelper.verbose(CLSunmiP1PrinterHandler.this.TAG, result);
            }
            
            public void onRaiseException(final int code, final String msg) {
                CLLoggingHelper.verbose(CLSunmiP1PrinterHandler.this.TAG, code + " " + msg);
            }
            
            public void onPrintResult(final int code, final String msg) {
                CLLoggingHelper.verbose(CLSunmiP1PrinterHandler.this.TAG, code + " " + msg);
            }
        };
    }
    
    static {
        CLSunmiP1PrinterHandler.connectionStatus = CLConnectionStatus.DISCONNECTED;
    }
}
