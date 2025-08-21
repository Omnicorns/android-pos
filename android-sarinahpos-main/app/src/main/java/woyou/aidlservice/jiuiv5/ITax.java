// 
// Decompiled by Procyon v0.5.36
// 

package woyou.aidlservice.jiuiv5;

import android.os.Parcel;
import android.os.IBinder;
import android.os.Binder;
import android.os.RemoteException;
import android.os.IInterface;

public interface ITax extends IInterface
{
    void onDataResult(final byte[] p0) throws RemoteException;
    
    public abstract static class Stub extends Binder implements ITax
    {
        public static ITax asInterface(final IBinder obj) {
            if (obj == null) {
                return null;
            }
            final IInterface iin = obj.queryLocalInterface("woyou.aidlservice.jiuiv5.ITax");
            if (iin != null && iin instanceof ITax) {
                return (ITax)iin;
            }
            return new Proxy(obj);
        }
        
        public boolean onTransact(final int code, final Parcel data, final Parcel reply, final int flags) throws RemoteException {
            final String descriptor = "woyou.aidlservice.jiuiv5.ITax";
            switch (code) {
                case 1598968902: {
                    reply.writeString(descriptor);
                    return true;
                }
                case 1: {
                    data.enforceInterface(descriptor);
                    final byte[] _arg0 = data.createByteArray();
                    this.onDataResult(_arg0);
                    return true;
                }
                default: {
                    return super.onTransact(code, data, reply, flags);
                }
            }
        }
        
        private static class Proxy implements ITax
        {
            private IBinder mRemote;
            
            Proxy(final IBinder remote) {
                this.mRemote = remote;
            }
            
            public IBinder asBinder() {
                return this.mRemote;
            }
            
            @Override
            public void onDataResult(final byte[] data) throws RemoteException {
                final Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken("woyou.aidlservice.jiuiv5.ITax");
                    _data.writeByteArray(data);
                    this.mRemote.transact(1, _data, (Parcel)null, 1);
                }
                finally {
                    _data.recycle();
                }
            }
        }
    }
}
