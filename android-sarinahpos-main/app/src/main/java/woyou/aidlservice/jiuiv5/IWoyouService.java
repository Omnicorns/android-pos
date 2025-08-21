// 
// Decompiled by Procyon v0.5.36
// 

package woyou.aidlservice.jiuiv5;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.graphics.Bitmap;
import android.os.RemoteException;
import android.os.IInterface;

public interface IWoyouService extends IInterface
{
    void updateFirmware() throws RemoteException;
    
    int getFirmwareStatus() throws RemoteException;
    
    String getServiceVersion() throws RemoteException;
    
    void printerInit(final ICallback p0) throws RemoteException;
    
    void printerSelfChecking(final ICallback p0) throws RemoteException;
    
    String getPrinterSerialNo() throws RemoteException;
    
    String getPrinterVersion() throws RemoteException;
    
    String getPrinterModal() throws RemoteException;
    
    int getPrintedLength() throws RemoteException;
    
    void lineWrap(final int p0, final ICallback p1) throws RemoteException;
    
    void sendRAWData(final byte[] p0, final ICallback p1) throws RemoteException;
    
    void setAlignment(final int p0, final ICallback p1) throws RemoteException;
    
    void setFontName(final String p0, final ICallback p1) throws RemoteException;
    
    void setFontSize(final float p0, final ICallback p1) throws RemoteException;
    
    void printText(final String p0, final ICallback p1) throws RemoteException;
    
    void printTextWithFont(final String p0, final String p1, final float p2, final ICallback p3) throws RemoteException;
    
    void printColumnsText(final String[] p0, final int[] p1, final int[] p2, final ICallback p3) throws RemoteException;
    
    void printBitmap(final Bitmap p0, final ICallback p1) throws RemoteException;
    
    void printBarCode(final String p0, final int p1, final int p2, final int p3, final int p4, final ICallback p5) throws RemoteException;
    
    void printQRCode(final String p0, final int p1, final int p2, final ICallback p3) throws RemoteException;
    
    void printOriginalText(final String p0, final ICallback p1) throws RemoteException;
    
    void commitPrinterBuffer() throws RemoteException;
    
    void cutPaper(final ICallback p0) throws RemoteException;
    
    int getCutPaperTimes() throws RemoteException;
    
    void openDrawer(final ICallback p0) throws RemoteException;
    
    int getOpenDrawerTimes() throws RemoteException;
    
    void enterPrinterBuffer(final boolean p0) throws RemoteException;
    
    void exitPrinterBuffer(final boolean p0) throws RemoteException;
    
    void tax(final byte[] p0, final ITax p1) throws RemoteException;
    
    int getPrinterMode() throws RemoteException;
    
    int getPrinterBBMDistance() throws RemoteException;
    
    void printColumnsString(final String[] p0, final int[] p1, final int[] p2, final ICallback p3) throws RemoteException;
    
    int updatePrinterState() throws RemoteException;
    
    void sendLCDCommand(final int p0) throws RemoteException;
    
    void sendLCDString(final String p0, final ILcdCallback p1) throws RemoteException;
    
    void sendLCDBitmap(final Bitmap p0, final ILcdCallback p1) throws RemoteException;
    
    void commitPrinterBufferWithCallback(final ICallback p0) throws RemoteException;
    
    void exitPrinterBufferWithCallback(final boolean p0, final ICallback p1) throws RemoteException;
    
    public abstract static class Stub extends Binder implements IWoyouService
    {
        public static IWoyouService asInterface(final IBinder obj) {
            if (obj == null) {
                return null;
            }
            final IInterface iin = obj.queryLocalInterface("woyou.aidlservice.jiuiv5.IWoyouService");
            if (iin != null && iin instanceof IWoyouService) {
                return (IWoyouService)iin;
            }
            return new Proxy(obj);
        }
        
        public boolean onTransact(final int code, final Parcel data, final Parcel reply, final int flags) throws RemoteException {
            final String descriptor = "woyou.aidlservice.jiuiv5.IWoyouService";
            switch (code) {
                case 1598968902: {
                    reply.writeString(descriptor);
                    return true;
                }
                case 1: {
                    data.enforceInterface(descriptor);
                    this.updateFirmware();
                    reply.writeNoException();
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    final int _result = this.getFirmwareStatus();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    final String _result2 = this.getServiceVersion();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                }
                case 4: {
                    data.enforceInterface(descriptor);
                    final ICallback _arg0 = ICallback.Stub.asInterface(data.readStrongBinder());
                    this.printerInit(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 5: {
                    data.enforceInterface(descriptor);
                    final ICallback _arg0 = ICallback.Stub.asInterface(data.readStrongBinder());
                    this.printerSelfChecking(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 6: {
                    data.enforceInterface(descriptor);
                    final String _result2 = this.getPrinterSerialNo();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                }
                case 7: {
                    data.enforceInterface(descriptor);
                    final String _result2 = this.getPrinterVersion();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                }
                case 8: {
                    data.enforceInterface(descriptor);
                    final String _result2 = this.getPrinterModal();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                }
                case 9: {
                    data.enforceInterface(descriptor);
                    final int _result = this.getPrintedLength();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 10: {
                    data.enforceInterface(descriptor);
                    final int _arg2 = data.readInt();
                    final ICallback _arg3 = ICallback.Stub.asInterface(data.readStrongBinder());
                    this.lineWrap(_arg2, _arg3);
                    reply.writeNoException();
                    return true;
                }
                case 11: {
                    data.enforceInterface(descriptor);
                    final byte[] _arg4 = data.createByteArray();
                    final ICallback _arg3 = ICallback.Stub.asInterface(data.readStrongBinder());
                    this.sendRAWData(_arg4, _arg3);
                    reply.writeNoException();
                    return true;
                }
                case 12: {
                    data.enforceInterface(descriptor);
                    final int _arg2 = data.readInt();
                    final ICallback _arg3 = ICallback.Stub.asInterface(data.readStrongBinder());
                    this.setAlignment(_arg2, _arg3);
                    reply.writeNoException();
                    return true;
                }
                case 13: {
                    data.enforceInterface(descriptor);
                    final String _arg5 = data.readString();
                    final ICallback _arg3 = ICallback.Stub.asInterface(data.readStrongBinder());
                    this.setFontName(_arg5, _arg3);
                    reply.writeNoException();
                    return true;
                }
                case 14: {
                    data.enforceInterface(descriptor);
                    final float _arg6 = data.readFloat();
                    final ICallback _arg3 = ICallback.Stub.asInterface(data.readStrongBinder());
                    this.setFontSize(_arg6, _arg3);
                    reply.writeNoException();
                    return true;
                }
                case 15: {
                    data.enforceInterface(descriptor);
                    final String _arg5 = data.readString();
                    final ICallback _arg3 = ICallback.Stub.asInterface(data.readStrongBinder());
                    this.printText(_arg5, _arg3);
                    reply.writeNoException();
                    return true;
                }
                case 16: {
                    data.enforceInterface(descriptor);
                    final String _arg5 = data.readString();
                    final String _arg7 = data.readString();
                    final float _arg8 = data.readFloat();
                    final ICallback _arg9 = ICallback.Stub.asInterface(data.readStrongBinder());
                    this.printTextWithFont(_arg5, _arg7, _arg8, _arg9);
                    reply.writeNoException();
                    return true;
                }
                case 17: {
                    data.enforceInterface(descriptor);
                    final String[] _arg10 = data.createStringArray();
                    final int[] _arg11 = data.createIntArray();
                    final int[] _arg12 = data.createIntArray();
                    final ICallback _arg9 = ICallback.Stub.asInterface(data.readStrongBinder());
                    this.printColumnsText(_arg10, _arg11, _arg12, _arg9);
                    reply.writeNoException();
                    return true;
                }
                case 18: {
                    data.enforceInterface(descriptor);
                    Bitmap _arg13;
                    if (0 != data.readInt()) {
                        _arg13 = (Bitmap)Bitmap.CREATOR.createFromParcel(data);
                    }
                    else {
                        _arg13 = null;
                    }
                    final ICallback _arg3 = ICallback.Stub.asInterface(data.readStrongBinder());
                    this.printBitmap(_arg13, _arg3);
                    reply.writeNoException();
                    return true;
                }
                case 19: {
                    data.enforceInterface(descriptor);
                    final String _arg5 = data.readString();
                    final int _arg14 = data.readInt();
                    final int _arg15 = data.readInt();
                    final int _arg16 = data.readInt();
                    final int _arg17 = data.readInt();
                    final ICallback _arg18 = ICallback.Stub.asInterface(data.readStrongBinder());
                    this.printBarCode(_arg5, _arg14, _arg15, _arg16, _arg17, _arg18);
                    reply.writeNoException();
                    return true;
                }
                case 20: {
                    data.enforceInterface(descriptor);
                    final String _arg5 = data.readString();
                    final int _arg14 = data.readInt();
                    final int _arg15 = data.readInt();
                    final ICallback _arg9 = ICallback.Stub.asInterface(data.readStrongBinder());
                    this.printQRCode(_arg5, _arg14, _arg15, _arg9);
                    reply.writeNoException();
                    return true;
                }
                case 21: {
                    data.enforceInterface(descriptor);
                    final String _arg5 = data.readString();
                    final ICallback _arg3 = ICallback.Stub.asInterface(data.readStrongBinder());
                    this.printOriginalText(_arg5, _arg3);
                    reply.writeNoException();
                    return true;
                }
                case 22: {
                    data.enforceInterface(descriptor);
                    this.commitPrinterBuffer();
                    reply.writeNoException();
                    return true;
                }
                case 23: {
                    data.enforceInterface(descriptor);
                    final ICallback _arg0 = ICallback.Stub.asInterface(data.readStrongBinder());
                    this.cutPaper(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 24: {
                    data.enforceInterface(descriptor);
                    final int _result = this.getCutPaperTimes();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 25: {
                    data.enforceInterface(descriptor);
                    final ICallback _arg0 = ICallback.Stub.asInterface(data.readStrongBinder());
                    this.openDrawer(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 26: {
                    data.enforceInterface(descriptor);
                    final int _result = this.getOpenDrawerTimes();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 27: {
                    data.enforceInterface(descriptor);
                    final boolean _arg19 = 0 != data.readInt();
                    this.enterPrinterBuffer(_arg19);
                    reply.writeNoException();
                    return true;
                }
                case 28: {
                    data.enforceInterface(descriptor);
                    final boolean _arg19 = 0 != data.readInt();
                    this.exitPrinterBuffer(_arg19);
                    reply.writeNoException();
                    return true;
                }
                case 29: {
                    data.enforceInterface(descriptor);
                    final byte[] _arg4 = data.createByteArray();
                    final ITax _arg20 = ITax.Stub.asInterface(data.readStrongBinder());
                    this.tax(_arg4, _arg20);
                    reply.writeNoException();
                    return true;
                }
                case 30: {
                    data.enforceInterface(descriptor);
                    final int _result = this.getPrinterMode();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 31: {
                    data.enforceInterface(descriptor);
                    final int _result = this.getPrinterBBMDistance();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 32: {
                    data.enforceInterface(descriptor);
                    final String[] _arg10 = data.createStringArray();
                    final int[] _arg11 = data.createIntArray();
                    final int[] _arg12 = data.createIntArray();
                    final ICallback _arg9 = ICallback.Stub.asInterface(data.readStrongBinder());
                    this.printColumnsString(_arg10, _arg11, _arg12, _arg9);
                    reply.writeNoException();
                    return true;
                }
                case 33: {
                    data.enforceInterface(descriptor);
                    final int _result = this.updatePrinterState();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                }
                case 34: {
                    data.enforceInterface(descriptor);
                    final int _arg2 = data.readInt();
                    this.sendLCDCommand(_arg2);
                    reply.writeNoException();
                    return true;
                }
                case 35: {
                    data.enforceInterface(descriptor);
                    final String _arg5 = data.readString();
                    final ILcdCallback _arg21 = ILcdCallback.Stub.asInterface(data.readStrongBinder());
                    this.sendLCDString(_arg5, _arg21);
                    reply.writeNoException();
                    return true;
                }
                case 36: {
                    data.enforceInterface(descriptor);
                    Bitmap _arg13;
                    if (0 != data.readInt()) {
                        _arg13 = (Bitmap)Bitmap.CREATOR.createFromParcel(data);
                    }
                    else {
                        _arg13 = null;
                    }
                    final ILcdCallback _arg21 = ILcdCallback.Stub.asInterface(data.readStrongBinder());
                    this.sendLCDBitmap(_arg13, _arg21);
                    reply.writeNoException();
                    return true;
                }
                case 37: {
                    data.enforceInterface(descriptor);
                    final ICallback _arg0 = ICallback.Stub.asInterface(data.readStrongBinder());
                    this.commitPrinterBufferWithCallback(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case 38: {
                    data.enforceInterface(descriptor);
                    final boolean _arg19 = 0 != data.readInt();
                    final ICallback _arg3 = ICallback.Stub.asInterface(data.readStrongBinder());
                    this.exitPrinterBufferWithCallback(_arg19, _arg3);
                    reply.writeNoException();
                    return true;
                }
                default: {
                    return super.onTransact(code, data, reply, flags);
                }
            }
        }
        
        private static class Proxy implements IWoyouService
        {
            private IBinder mRemote;
            
            Proxy(final IBinder remote) {
                this.mRemote = remote;
            }
            
            public IBinder asBinder() {
                return this.mRemote;
            }
            
            @Override
            public void updateFirmware() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override
            public int getFirmwareStatus() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            
            @Override
            public String getServiceVersion() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                String _result;
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readString();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            
            @Override
            public void printerInit(final ICallback callback) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    _data.writeStrongBinder((callback != null) ? callback.asBinder() : null);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override
            public void printerSelfChecking(final ICallback callback) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    _data.writeStrongBinder((callback != null) ? callback.asBinder() : null);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override
            public String getPrinterSerialNo() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                String _result;
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readString();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            
            @Override
            public String getPrinterVersion() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                String _result;
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readString();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            
            @Override
            public String getPrinterModal() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                String _result;
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readString();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            
            @Override
            public int getPrintedLength() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            
            @Override
            public void lineWrap(final int n, final ICallback callback) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    _data.writeInt(n);
                    _data.writeStrongBinder((callback != null) ? callback.asBinder() : null);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override
            public void sendRAWData(final byte[] data, final ICallback callback) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    _data.writeByteArray(data);
                    _data.writeStrongBinder((callback != null) ? callback.asBinder() : null);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override
            public void setAlignment(final int alignment, final ICallback callback) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    _data.writeInt(alignment);
                    _data.writeStrongBinder((callback != null) ? callback.asBinder() : null);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override
            public void setFontName(final String typeface, final ICallback callback) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    _data.writeString(typeface);
                    _data.writeStrongBinder((callback != null) ? callback.asBinder() : null);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override
            public void setFontSize(final float fontsize, final ICallback callback) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    _data.writeFloat(fontsize);
                    _data.writeStrongBinder((callback != null) ? callback.asBinder() : null);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override
            public void printText(final String text, final ICallback callback) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    _data.writeString(text);
                    _data.writeStrongBinder((callback != null) ? callback.asBinder() : null);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override
            public void printTextWithFont(final String text, final String typeface, final float fontsize, final ICallback callback) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    _data.writeString(text);
                    _data.writeString(typeface);
                    _data.writeFloat(fontsize);
                    _data.writeStrongBinder((callback != null) ? callback.asBinder() : null);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override
            public void printColumnsText(final String[] colsTextArr, final int[] colsWidthArr, final int[] colsAlign, final ICallback callback) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    _data.writeStringArray(colsTextArr);
                    _data.writeIntArray(colsWidthArr);
                    _data.writeIntArray(colsAlign);
                    _data.writeStrongBinder((callback != null) ? callback.asBinder() : null);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override
            public void printBitmap(final Bitmap bitmap, final ICallback callback) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    if (bitmap != null) {
                        _data.writeInt(1);
                        bitmap.writeToParcel(_data, 0);
                    }
                    else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder((callback != null) ? callback.asBinder() : null);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override
            public void printBarCode(final String data, final int symbology, final int height, final int width, final int textposition, final ICallback callback) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    _data.writeString(data);
                    _data.writeInt(symbology);
                    _data.writeInt(height);
                    _data.writeInt(width);
                    _data.writeInt(textposition);
                    _data.writeStrongBinder((callback != null) ? callback.asBinder() : null);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override
            public void printQRCode(final String data, final int modulesize, final int errorlevel, final ICallback callback) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    _data.writeString(data);
                    _data.writeInt(modulesize);
                    _data.writeInt(errorlevel);
                    _data.writeStrongBinder((callback != null) ? callback.asBinder() : null);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override
            public void printOriginalText(final String text, final ICallback callback) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    _data.writeString(text);
                    _data.writeStrongBinder((callback != null) ? callback.asBinder() : null);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override
            public void commitPrinterBuffer() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override
            public void cutPaper(final ICallback callback) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    _data.writeStrongBinder((callback != null) ? callback.asBinder() : null);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override
            public int getCutPaperTimes() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            
            @Override
            public void openDrawer(final ICallback callback) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    _data.writeStrongBinder((callback != null) ? callback.asBinder() : null);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override
            public int getOpenDrawerTimes() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            
            @Override
            public void enterPrinterBuffer(final boolean clean) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    _data.writeInt((int)(clean ? 1 : 0));
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override
            public void exitPrinterBuffer(final boolean commit) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    _data.writeInt((int)(commit ? 1 : 0));
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override
            public void tax(final byte[] data, final ITax callback) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    _data.writeByteArray(data);
                    _data.writeStrongBinder((callback != null) ? callback.asBinder() : null);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override
            public int getPrinterMode() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            
            @Override
            public int getPrinterBBMDistance() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            
            @Override
            public void printColumnsString(final String[] colsTextArr, final int[] colsWidthArr, final int[] colsAlign, final ICallback callback) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    _data.writeStringArray(colsTextArr);
                    _data.writeIntArray(colsWidthArr);
                    _data.writeIntArray(colsAlign);
                    _data.writeStrongBinder((callback != null) ? callback.asBinder() : null);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override
            public int updatePrinterState() throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                int _result;
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.readInt();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }
            
            @Override
            public void sendLCDCommand(final int flag) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    _data.writeInt(flag);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override
            public void sendLCDString(final String string, final ILcdCallback callback) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    _data.writeString(string);
                    _data.writeStrongBinder((callback != null) ? callback.asBinder() : null);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override
            public void sendLCDBitmap(final Bitmap bitmap, final ILcdCallback callback) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    if (bitmap != null) {
                        _data.writeInt(1);
                        bitmap.writeToParcel(_data, 0);
                    }
                    else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder((callback != null) ? callback.asBinder() : null);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override
            public void commitPrinterBufferWithCallback(final ICallback callback) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    _data.writeStrongBinder((callback != null) ? callback.asBinder() : null);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
            
            @Override
            public void exitPrinterBufferWithCallback(final boolean commit, final ICallback callback) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                final Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.IWoyouService");
                    _data.writeInt((int)(commit ? 1 : 0));
                    _data.writeStrongBinder((callback != null) ? callback.asBinder() : null);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                }
                finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
