// 
// Decompiled by Procyon v0.5.36
// 

package woyou.aidlservice.jiuiv5;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.os.RemoteException;
import android.os.IInterface;

public interface ICallback extends IInterface
{
    void onRunResult(final boolean p0) throws RemoteException;
    
    void onReturnString(final String p0) throws RemoteException;
    
    void onRaiseException(final int p0, final String p1) throws RemoteException;
    
    void onPrintResult(final int p0, final String p1) throws RemoteException;
    
    public abstract static class Stub extends Binder implements ICallback
    {
        public Stub() {
            this.attachInterface((IInterface)this, "woyou.aidlservice.jiuiv5.ICallback");
        }
        
        public static ICallback asInterface(final IBinder obj) {
            if (obj == null) {
                return null;
            }
            final IInterface iin = obj.queryLocalInterface("woyou.aidlservice.jiuiv5.ICallback");
            if (iin != null && iin instanceof ICallback) {
                return (ICallback)iin;
            }
            return new Proxy(obj);
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public boolean onTransact(final int code, final Parcel data, final Parcel reply, final int flags) throws RemoteException {
            final String descriptor = "woyou.aidlservice.jiuiv5.ICallback";
            switch (code) {
                case 1598968902: {
                    reply.writeString(descriptor);
                    return true;
                }
                case 1: {
                    data.enforceInterface(descriptor);
                    final boolean _arg0 = 0 != data.readInt();
                    this.onRunResult(_arg0);
                    return true;
                }
                case 2: {
                    data.enforceInterface(descriptor);
                    final String _arg2 = data.readString();
                    this.onReturnString(_arg2);
                    return true;
                }
                case 3: {
                    data.enforceInterface(descriptor);
                    final int _arg3 = data.readInt();
                    final String _arg4 = data.readString();
                    this.onRaiseException(_arg3, _arg4);
                    return true;
                }
                case 4: {
                    data.enforceInterface(descriptor);
                    final int _arg3 = data.readInt();
                    final String _arg4 = data.readString();
                    this.onPrintResult(_arg3, _arg4);
                    return true;
                }
                default: {
                    return super.onTransact(code, data, reply, flags);
                }
            }
        }
        
        private static class Proxy implements ICallback
        {
            private IBinder mRemote;
            
            Proxy(final IBinder remote) {
                this.mRemote = remote;
            }
            
            public IBinder asBinder() {
                return this.mRemote;
            }
            
            @Override
            public void onRunResult(final boolean isSuccess) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.ICallback");
                    _data.writeInt((int)(isSuccess ? 1 : 0));
                    this.mRemote.transact(1, _data, (Parcel)null, 1);
                }
                finally {
                    _data.recycle();
                }
            }
            
            @Override
            public void onReturnString(final String result) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.ICallback");
                    _data.writeString(result);
                    this.mRemote.transact(2, _data, (Parcel)null, 1);
                }
                finally {
                    _data.recycle();
                }
            }
            
            @Override
            public void onRaiseException(final int code, final String msg) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.ICallback");
                    _data.writeInt(code);
                    _data.writeString(msg);
                    this.mRemote.transact(3, _data, (Parcel)null, 1);
                }
                finally {
                    _data.recycle();
                }
            }
            
            @Override
            public void onPrintResult(final int code, final String msg) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.ICallback");
                    _data.writeInt(code);
                    _data.writeString(msg);
                    this.mRemote.transact(4, _data, (Parcel)null, 1);
                }
                finally {
                    _data.recycle();
                }
            }
        }
    }
}
