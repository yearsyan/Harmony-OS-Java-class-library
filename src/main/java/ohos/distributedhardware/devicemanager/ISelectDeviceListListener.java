package ohos.distributedhardware.devicemanager;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface ISelectDeviceListListener extends IInterface {

    public static class Default implements ISelectDeviceListListener {
        public IBinder asBinder() {
            return null;
        }

        @Override // ohos.distributedhardware.devicemanager.ISelectDeviceListListener
        public int onGetDeviceList(List<DeviceBasicInfo> list, int i) throws RemoteException {
            return 0;
        }
    }

    int onGetDeviceList(List<DeviceBasicInfo> list, int i) throws RemoteException;

    public static abstract class Stub extends Binder implements ISelectDeviceListListener {
        private static final String DESCRIPTOR = "com.huawei.systemserver.dmaccessservice.ISelectDeviceListListener";
        static final int TRANSACTION_onGetDeviceList = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISelectDeviceListListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ISelectDeviceListListener)) {
                return new Proxy(iBinder);
            }
            return (ISelectDeviceListListener) queryLocalInterface;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface(DESCRIPTOR);
                int onGetDeviceList = onGetDeviceList(parcel.createTypedArrayList(DeviceBasicInfo.CREATOR), parcel.readInt());
                parcel2.writeNoException();
                parcel2.writeInt(onGetDeviceList);
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
        }

        /* access modifiers changed from: private */
        public static class Proxy implements ISelectDeviceListListener {
            public static ISelectDeviceListListener sDefaultImpl;
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // ohos.distributedhardware.devicemanager.ISelectDeviceListListener
            public int onGetDeviceList(List<DeviceBasicInfo> list, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedList(list);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().onGetDeviceList(list, i);
                    }
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    obtain2.recycle();
                    obtain.recycle();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ISelectDeviceListListener iSelectDeviceListListener) {
            if (Proxy.sDefaultImpl != null || iSelectDeviceListListener == null) {
                return false;
            }
            Proxy.sDefaultImpl = iSelectDeviceListListener;
            return true;
        }

        public static ISelectDeviceListListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
