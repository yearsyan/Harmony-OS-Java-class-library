package ohos.distributedhardware.devicemanager;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IDeviceStateCallback extends IInterface {
    void onDeviceOffline(DeviceBasicInfo deviceBasicInfo) throws RemoteException;

    void onDeviceOnline(DeviceBasicInfo deviceBasicInfo) throws RemoteException;

    public static abstract class Stub extends Binder implements IDeviceStateCallback {
        private static final String DESCRIPTOR = "com.huawei.systemserver.dmaccessservice.IDeviceStateCallback";
        static final int TRANSACTION_onDeviceOffline = 1;
        static final int TRANSACTION_onDeviceOnline = 2;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDeviceStateCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IDeviceStateCallback)) {
                return new Proxy(iBinder);
            }
            return (IDeviceStateCallback) queryLocalInterface;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            DeviceBasicInfo deviceBasicInfo = null;
            if (i == 1) {
                parcel.enforceInterface(DESCRIPTOR);
                if (parcel.readInt() != 0) {
                    deviceBasicInfo = DeviceBasicInfo.CREATOR.createFromParcel(parcel);
                }
                onDeviceOffline(deviceBasicInfo);
                parcel2.writeNoException();
                return true;
            } else if (i == 2) {
                parcel.enforceInterface(DESCRIPTOR);
                if (parcel.readInt() != 0) {
                    deviceBasicInfo = DeviceBasicInfo.CREATOR.createFromParcel(parcel);
                }
                onDeviceOnline(deviceBasicInfo);
                parcel2.writeNoException();
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IDeviceStateCallback {
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

            @Override // ohos.distributedhardware.devicemanager.IDeviceStateCallback
            public void onDeviceOffline(DeviceBasicInfo deviceBasicInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (deviceBasicInfo != null) {
                        obtain.writeInt(1);
                        deviceBasicInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // ohos.distributedhardware.devicemanager.IDeviceStateCallback
            public void onDeviceOnline(DeviceBasicInfo deviceBasicInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (deviceBasicInfo != null) {
                        obtain.writeInt(1);
                        deviceBasicInfo.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
