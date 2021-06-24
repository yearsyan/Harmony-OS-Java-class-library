package ohos.bluetooth.ble;

import java.util.Optional;
import ohos.bluetooth.BluetoothAdapterUtils;
import ohos.bluetooth.BluetoothHostProxy;
import ohos.bluetooth.LogHelper;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageParcel;

class BlePeripheralManagerProxy {
    private static final int DEFAULT_GATT_NUM = 3;
    private static final int MIN_TRANSACTION_ID = 1;
    private static final HiLogLabel TAG = new HiLogLabel(3, LogHelper.BT_DOMAIN_ID, "BlePeripheralManagerProxy");
    private static final int TRANSACT_ADD_SCANNER_ID = 2;
    private static final int TRANSACT_ADD_SERVICE = 48;
    private static final int TRANSACT_CLEAR_SERVICES = 50;
    private static final int TRANSACT_DISCONNECT_SERVER = 45;
    private static final int TRANSACT_GET_CONNECTION_STATES_BY_STATES = 1;
    private static final int TRANSACT_GET_DEVICES_BY_STATE = 1;
    private static final int TRANSACT_REGISTER_SERVER = 42;
    private static final int TRANSACT_REMOVE_SCANNER_ID = 3;
    private static final int TRANSACT_REMOVE_SERVICE = 49;
    private static final int TRANSACT_SEND_NOTIFICATION = 52;
    private static final int TRANSACT_SNED_RESPONSE = 51;
    private static final int TRANSACT_STOP_SCAN = 7;
    private static final int TRANSACT_UNREGISTER_SERVER = 43;
    private final Object mLock = new Object();
    private IRemoteObject remote;

    BlePeripheralManagerProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    private void writeInterfaceToken(MessageParcel messageParcel) {
        messageParcel.writeInt(1);
        messageParcel.writeInt(1);
        messageParcel.writeString(BluetoothAdapterUtils.stringReplace("ohos.bluetooth.IBluetoothGatt"));
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:21|22|23|24|25) */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x006e, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BlePeripheralManagerProxy.TAG, "addService : remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0080, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0081, code lost:
        r1.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0087, code lost:
        throw r7;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0070 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addService(int r8, ohos.bluetooth.ble.GattService r9) {
        /*
        // Method dump skipped, instructions count: 139
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BlePeripheralManagerProxy.addService(int, ohos.bluetooth.ble.GattService):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:20|21|22|23|24) */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0071, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BlePeripheralManagerProxy.TAG, "disconnectServer : remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0083, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0084, code lost:
        r1.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x008a, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x0073 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void disconnectServer(int r7, java.lang.String r8) {
        /*
        // Method dump skipped, instructions count: 142
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BlePeripheralManagerProxy.disconnectServer(int, java.lang.String):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x007d, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BlePeripheralManagerProxy.TAG, "registerServer : remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0090, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0091, code lost:
        r1.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0097, code lost:
        throw r7;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x007f */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void registerServer(ohos.utils.SequenceUuid r8, ohos.bluetooth.ble.BlePeripheralManagerCallbackWrapper r9) {
        /*
        // Method dump skipped, instructions count: 155
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BlePeripheralManagerProxy.registerServer(ohos.utils.SequenceUuid, ohos.bluetooth.ble.BlePeripheralManagerCallbackWrapper):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x006a, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BlePeripheralManagerProxy.TAG, "unregisterServer : remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x007d, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x007e, code lost:
        r1.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0084, code lost:
        throw r7;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x006c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void unregisterServer(int r8) {
        /*
        // Method dump skipped, instructions count: 136
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BlePeripheralManagerProxy.unregisterServer(int):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x006b, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BlePeripheralManagerProxy.TAG, "clearServices : remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x007d, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x007e, code lost:
        r1.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0084, code lost:
        throw r7;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x006d */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void clearServices(int r8) {
        /*
        // Method dump skipped, instructions count: 136
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BlePeripheralManagerProxy.clearServices(int):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0079, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BlePeripheralManagerProxy.TAG, "sendNotification : remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x008c, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x008d, code lost:
        r1.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0093, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x007b */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void sendNotification(int r5, java.lang.String r6, int r7, boolean r8, byte[] r9) {
        /*
        // Method dump skipped, instructions count: 151
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BlePeripheralManagerProxy.sendNotification(int, java.lang.String, int, boolean, byte[]):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:20|21|22|23|24) */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0070, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BlePeripheralManagerProxy.TAG, "removeService : remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0083, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0084, code lost:
        r1.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x008a, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x0072 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void removeService(int r7, int r8) {
        /*
        // Method dump skipped, instructions count: 142
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BlePeripheralManagerProxy.removeService(int, int):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x007c, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BlePeripheralManagerProxy.TAG, "sendResponse : remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x008f, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0090, code lost:
        r1.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0096, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x007e */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void sendResponse(int r5, java.lang.String r6, int r7, int r8, int r9, byte[] r10) {
        /*
        // Method dump skipped, instructions count: 154
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BlePeripheralManagerProxy.sendResponse(int, java.lang.String, int, int, int, byte[]):void");
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x009a, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BlePeripheralManagerProxy.TAG, "addScanner : remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00a5, code lost:
        r1.reclaim();
        r3.reclaim();
        r8 = new java.util.ArrayList();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00b1, code lost:
        return r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00b2, code lost:
        r1.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00b8, code lost:
        throw r8;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x009c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<ohos.bluetooth.ble.BlePeripheralDevice> getDevicesByStates(int[] r9) {
        /*
        // Method dump skipped, instructions count: 188
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BlePeripheralManagerProxy.getDevicesByStates(int[]):java.util.List");
    }

    private Optional<IRemoteObject> getRemote() {
        return BluetoothHostProxy.getInstace().getSaProfileProxy(11);
    }
}
