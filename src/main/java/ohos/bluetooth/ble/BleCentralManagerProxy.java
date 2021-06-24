package ohos.bluetooth.ble;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ohos.bluetooth.BluetoothAdapterUtils;
import ohos.bluetooth.BluetoothHostProxy;
import ohos.bluetooth.LogHelper;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageParcel;

class BleCentralManagerProxy {
    private static final int MIN_TRANSACTION_ID = 1;
    private static final HiLogLabel TAG = new HiLogLabel(3, LogHelper.BT_DOMAIN_ID, "BleCentralManagerProxy");
    private static final int TRANSACT_ADD_SCANNER_ID = 2;
    private static final int TRANSACT_DO_SCAN = 4;
    private static final int TRANSACT_GET_DEVICES_BY_STATE = 1;
    private static final int TRANSACT_REMOVE_SCANNER_ID = 3;
    private static final int TRANSACT_STOP_SCAN = 7;
    private final Object mLock = new Object();
    private IRemoteObject remote;

    BleCentralManagerProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    private void writeInterfaceToken(MessageParcel messageParcel) {
        messageParcel.writeInt(1);
        messageParcel.writeInt(1);
        messageParcel.writeString(BluetoothAdapterUtils.stringReplace("ohos.bluetooth.IBluetoothGatt"));
    }

    private void writeFilters(List<BleScanFilter> list, MessageParcel messageParcel) {
        messageParcel.writeInt(list.size());
        for (BleScanFilter bleScanFilter : list) {
            if (bleScanFilter != null) {
                messageParcel.writeInt(1);
                bleScanFilter.marshalling(messageParcel);
            } else {
                messageParcel.writeInt(0);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:27|28|29|30|31) */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0073, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BleCentralManagerProxy.TAG, "doScan : remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0085, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0086, code lost:
        r1.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x008c, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0075 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean doScan(int r5, java.util.List<ohos.bluetooth.ble.BleScanFilter> r6, ohos.bluetooth.ble.ScannerCallbackWrapper.ScanParameter r7, java.lang.String r8) {
        /*
        // Method dump skipped, instructions count: 144
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BleCentralManagerProxy.doScan(int, java.util.List, ohos.bluetooth.ble.ScannerCallbackWrapper$ScanParameter, java.lang.String):boolean");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:20|21|22|23|24) */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0062, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BleCentralManagerProxy.TAG, "addScanner : remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0074, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0075, code lost:
        r1.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x007b, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x0064 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean addScanner(ohos.bluetooth.ble.BleCentralManager r6) {
        /*
        // Method dump skipped, instructions count: 127
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BleCentralManagerProxy.addScanner(ohos.bluetooth.ble.BleCentralManager):boolean");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0058, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BleCentralManagerProxy.TAG, "stopScan : remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x006b, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x006c, code lost:
        r1.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0072, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x005a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean stopScan(int r6) {
        /*
        // Method dump skipped, instructions count: 118
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BleCentralManagerProxy.stopScan(int):boolean");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0058, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BleCentralManagerProxy.TAG, "removeScanner : remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x006b, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x006c, code lost:
        r1.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0072, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x005a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean removeScanner(int r6) {
        /*
        // Method dump skipped, instructions count: 118
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BleCentralManagerProxy.removeScanner(int):boolean");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:20|21|22|23|24) */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0063, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BleCentralManagerProxy.TAG, "getPeripheralDevicesByStates: remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0075, code lost:
        return r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0076, code lost:
        r3.reclaim();
        r4.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x007c, code lost:
        throw r7;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x0065 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<ohos.bluetooth.ble.BlePeripheralDevice> getPeripheralDevicesByStates(int[] r8) {
        /*
        // Method dump skipped, instructions count: 128
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BleCentralManagerProxy.getPeripheralDevicesByStates(int[]):java.util.List");
    }

    private List<BlePeripheralDevice> readDeviceList(MessageParcel messageParcel) {
        if (messageParcel.readInt() != 0) {
            HiLog.error(TAG, "readDeviceList got error", new Object[0]);
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        int readInt = messageParcel.readInt();
        for (int i = 0; i < readInt; i++) {
            if (messageParcel.getReadableBytes() <= 0) {
                HiLog.warn(TAG, "readDeviceList: data read failed due to data size mismatch", new Object[0]);
                return arrayList;
            }
            messageParcel.readInt();
            arrayList.add(new BlePeripheralDevice(messageParcel.readString()));
        }
        return arrayList;
    }

    private Optional<IRemoteObject> getRemote() {
        return BluetoothHostProxy.getInstace().getSaProfileProxy(11);
    }
}
