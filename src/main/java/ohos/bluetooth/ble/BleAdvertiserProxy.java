package ohos.bluetooth.ble;

import java.util.Optional;
import ohos.bluetooth.BluetoothAdapterUtils;
import ohos.bluetooth.BluetoothHostProxy;
import ohos.bluetooth.LogHelper;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;

class BleAdvertiserProxy {
    private static final int COMMAND_START_ADV = 9;
    private static final int COMMAND_STOP_ADV = 10;
    private static final String DESCRIPTOR = "ohos.bluetooth.IBluetoothGatt";
    private static final int MIN_TRANSACTION_ID = 1;
    private static final int NO_EXCEPTION = 0;
    private static final int PROFILE_BLE = 11;
    private static final HiLogLabel TAG = new HiLogLabel(3, LogHelper.BT_DOMAIN_ID, "BleAdvertiserProxy");
    private BleAdvertiseCallback mBleAdvertiserCallback;
    private BleAdvertiseCallbackWrapper mCallbackWrapper;

    BleAdvertiserProxy(BleAdvertiseCallback bleAdvertiseCallback) {
        this.mBleAdvertiserCallback = bleAdvertiseCallback;
        this.mCallbackWrapper = new BleAdvertiseCallbackWrapper(bleAdvertiseCallback, "ohos.bluetooth.ble.IBleAdvertiseCallback");
    }

    private IRemoteObject getProxy() throws RemoteException {
        Optional<IRemoteObject> saProfileProxy = BluetoothHostProxy.getInstace().getSaProfileProxy(11);
        if (saProfileProxy.isPresent()) {
            return saProfileProxy.get();
        }
        HiLog.error(TAG, "ble advertise proxy is null", new Object[0]);
        throw new RemoteException();
    }

    private MessageParcel createDataWithToken() {
        MessageParcel obtain = MessageParcel.obtain();
        obtain.writeInt(1);
        obtain.writeInt(1);
        obtain.writeString(BluetoothAdapterUtils.stringReplace(DESCRIPTOR));
        return obtain;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:19|20|21) */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x006e, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BleAdvertiserProxy.TAG, "startAdvertising call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x007f, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0080, code lost:
        r1.reclaim();
        r4.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0086, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0070 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void startAdvertising(ohos.bluetooth.ble.BleAdvertiseSettings r5, ohos.bluetooth.ble.BleAdvertiseData r6, ohos.bluetooth.ble.BleAdvertiseData r7) throws ohos.rpc.RemoteException {
        /*
        // Method dump skipped, instructions count: 135
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BleAdvertiserProxy.startAdvertising(ohos.bluetooth.ble.BleAdvertiseSettings, ohos.bluetooth.ble.BleAdvertiseData, ohos.bluetooth.ble.BleAdvertiseData):void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:9|10|11) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BleAdvertiserProxy.TAG, "stopAdvertising call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x004e, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x004f, code lost:
        r1.reclaim();
        r5.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0055, code lost:
        throw r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003d, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x003f */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void stopAdvertising() throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.IRemoteObject r0 = r5.getProxy()
            ohos.rpc.MessageParcel r1 = r5.createDataWithToken()
            ohos.bluetooth.ble.BleAdvertiseCallbackWrapper r5 = r5.mCallbackWrapper
            ohos.rpc.IRemoteObject r5 = r5.asObject()
            r1.writeRemoteObject(r5)
            ohos.rpc.MessageParcel r5 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            r4 = 10
            r0.sendRequest(r4, r1, r5, r2)     // Catch:{ RemoteException -> 0x003f }
            int r0 = r5.readInt()     // Catch:{ RemoteException -> 0x003f }
            if (r0 != 0) goto L_0x002d
            r1.reclaim()
            r5.reclaim()
            return
        L_0x002d:
            ohos.hiviewdfx.HiLogLabel r0 = ohos.bluetooth.ble.BleAdvertiserProxy.TAG
            java.lang.String r2 = "stopAdvertising got error"
            java.lang.Object[] r4 = new java.lang.Object[r3]
            ohos.hiviewdfx.HiLog.error(r0, r2, r4)
            ohos.rpc.RemoteException r0 = new ohos.rpc.RemoteException
            r0.<init>()
            throw r0
        L_0x003d:
            r0 = move-exception
            goto L_0x004f
        L_0x003f:
            ohos.hiviewdfx.HiLogLabel r0 = ohos.bluetooth.ble.BleAdvertiserProxy.TAG     // Catch:{ all -> 0x003d }
            java.lang.String r2 = "stopAdvertising call fail"
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x003d }
            ohos.hiviewdfx.HiLog.error(r0, r2, r3)     // Catch:{ all -> 0x003d }
            ohos.rpc.RemoteException r0 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x003d }
            r0.<init>()     // Catch:{ all -> 0x003d }
            throw r0     // Catch:{ all -> 0x003d }
        L_0x004f:
            r1.reclaim()
            r5.reclaim()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BleAdvertiserProxy.stopAdvertising():void");
    }
}
