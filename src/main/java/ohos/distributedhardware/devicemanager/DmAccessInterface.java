package ohos.distributedhardware.devicemanager;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;
import ohos.distributedhardware.devicemanager.IDeviceStateCallback;
import ohos.distributedhardware.devicemanager.ISelectDeviceListListener;

public interface DmAccessInterface extends IInterface {

    public static class Default implements DmAccessInterface {
        public IBinder asBinder() {
            return null;
        }

        @Override // ohos.distributedhardware.devicemanager.DmAccessInterface
        public DeviceBasicInfo getLocalDeviceInfo() throws RemoteException {
            return null;
        }

        @Override // ohos.distributedhardware.devicemanager.DmAccessInterface
        public List<DeviceBasicInfo> getTrustedDeviceList(String str, Bundle bundle) throws RemoteException {
            return null;
        }

        @Override // ohos.distributedhardware.devicemanager.DmAccessInterface
        public int registerDeviceStateCallback(String str, Bundle bundle, IDeviceStateCallback iDeviceStateCallback) throws RemoteException {
            return 0;
        }

        @Override // ohos.distributedhardware.devicemanager.DmAccessInterface
        public int selectDeviceList(String str, Bundle bundle, ISelectDeviceListListener iSelectDeviceListListener) throws RemoteException {
            return 0;
        }

        @Override // ohos.distributedhardware.devicemanager.DmAccessInterface
        public int unregisterDeviceStateCallback(String str, IDeviceStateCallback iDeviceStateCallback) throws RemoteException {
            return 0;
        }
    }

    DeviceBasicInfo getLocalDeviceInfo() throws RemoteException;

    List<DeviceBasicInfo> getTrustedDeviceList(String str, Bundle bundle) throws RemoteException;

    int registerDeviceStateCallback(String str, Bundle bundle, IDeviceStateCallback iDeviceStateCallback) throws RemoteException;

    int selectDeviceList(String str, Bundle bundle, ISelectDeviceListListener iSelectDeviceListListener) throws RemoteException;

    int unregisterDeviceStateCallback(String str, IDeviceStateCallback iDeviceStateCallback) throws RemoteException;

    public static abstract class Stub extends Binder implements DmAccessInterface {
        private static final String DESCRIPTOR = "com.huawei.systemserver.dmaccessservice.DmAccessInterface";
        static final int TRANSACTION_getLocalDeviceInfo = 3;
        static final int TRANSACTION_getTrustedDeviceList = 1;
        static final int TRANSACTION_registerDeviceStateCallback = 4;
        static final int TRANSACTION_selectDeviceList = 2;
        static final int TRANSACTION_unregisterDeviceStateCallback = 5;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static DmAccessInterface asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof DmAccessInterface)) {
                return new Proxy(iBinder);
            }
            return (DmAccessInterface) queryLocalInterface;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            Bundle bundle = null;
            if (i == 1) {
                parcel.enforceInterface(DESCRIPTOR);
                String readString = parcel.readString();
                if (parcel.readInt() != 0) {
                    bundle = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                }
                List<DeviceBasicInfo> trustedDeviceList = getTrustedDeviceList(readString, bundle);
                parcel2.writeNoException();
                parcel2.writeTypedList(trustedDeviceList);
                return true;
            } else if (i == 2) {
                parcel.enforceInterface(DESCRIPTOR);
                String readString2 = parcel.readString();
                if (parcel.readInt() != 0) {
                    bundle = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                }
                int selectDeviceList = selectDeviceList(readString2, bundle, ISelectDeviceListListener.Stub.asInterface(parcel.readStrongBinder()));
                parcel2.writeNoException();
                parcel2.writeInt(selectDeviceList);
                return true;
            } else if (i == 3) {
                parcel.enforceInterface(DESCRIPTOR);
                DeviceBasicInfo localDeviceInfo = getLocalDeviceInfo();
                parcel2.writeNoException();
                if (localDeviceInfo != null) {
                    parcel2.writeInt(1);
                    localDeviceInfo.writeToParcel(parcel2, 1);
                } else {
                    parcel2.writeInt(0);
                }
                return true;
            } else if (i == 4) {
                parcel.enforceInterface(DESCRIPTOR);
                String readString3 = parcel.readString();
                if (parcel.readInt() != 0) {
                    bundle = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                }
                int registerDeviceStateCallback = registerDeviceStateCallback(readString3, bundle, IDeviceStateCallback.Stub.asInterface(parcel.readStrongBinder()));
                parcel2.writeNoException();
                parcel2.writeInt(registerDeviceStateCallback);
                return true;
            } else if (i == 5) {
                parcel.enforceInterface(DESCRIPTOR);
                int unregisterDeviceStateCallback = unregisterDeviceStateCallback(parcel.readString(), IDeviceStateCallback.Stub.asInterface(parcel.readStrongBinder()));
                parcel2.writeNoException();
                parcel2.writeInt(unregisterDeviceStateCallback);
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
        }

        /* access modifiers changed from: private */
        public static class Proxy implements DmAccessInterface {
            public static DmAccessInterface sDefaultImpl;
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

            @Override // ohos.distributedhardware.devicemanager.DmAccessInterface
            public List<DeviceBasicInfo> getTrustedDeviceList(String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (!this.mRemote.transact(1, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getTrustedDeviceList(str, bundle);
                    }
                    obtain2.readException();
                    ArrayList createTypedArrayList = obtain2.createTypedArrayList(DeviceBasicInfo.CREATOR);
                    obtain2.recycle();
                    obtain.recycle();
                    return createTypedArrayList;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // ohos.distributedhardware.devicemanager.DmAccessInterface
            public int selectDeviceList(String str, Bundle bundle, ISelectDeviceListListener iSelectDeviceListListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iSelectDeviceListListener != null ? iSelectDeviceListListener.asBinder() : null);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().selectDeviceList(str, bundle, iSelectDeviceListListener);
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

            @Override // ohos.distributedhardware.devicemanager.DmAccessInterface
            public DeviceBasicInfo getLocalDeviceInfo() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(3, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLocalDeviceInfo();
                    }
                    obtain2.readException();
                    DeviceBasicInfo createFromParcel = obtain2.readInt() != 0 ? DeviceBasicInfo.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return createFromParcel;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // ohos.distributedhardware.devicemanager.DmAccessInterface
            public int registerDeviceStateCallback(String str, Bundle bundle, IDeviceStateCallback iDeviceStateCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeStrongBinder(iDeviceStateCallback != null ? iDeviceStateCallback.asBinder() : null);
                    if (!this.mRemote.transact(4, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().registerDeviceStateCallback(str, bundle, iDeviceStateCallback);
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

            @Override // ohos.distributedhardware.devicemanager.DmAccessInterface
            public int unregisterDeviceStateCallback(String str, IDeviceStateCallback iDeviceStateCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeStrongBinder(iDeviceStateCallback != null ? iDeviceStateCallback.asBinder() : null);
                    if (!this.mRemote.transact(5, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().unregisterDeviceStateCallback(str, iDeviceStateCallback);
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

        public static boolean setDefaultImpl(DmAccessInterface dmAccessInterface) {
            if (Proxy.sDefaultImpl != null || dmAccessInterface == null) {
                return false;
            }
            Proxy.sDefaultImpl = dmAccessInterface;
            return true;
        }

        public static DmAccessInterface getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
