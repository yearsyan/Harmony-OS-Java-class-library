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

public class BlePeripheralProxy {
    private static final int COMMAND_CLIENT_CONNECT = 23;
    private static final int COMMAND_CLIENT_DISCONNECT = 24;
    private static final int COMMAND_CONFIG_MTU = 39;
    private static final int COMMAND_CONNECTION_PARAM_UPDATE = 40;
    private static final int COMMAND_DISCOVER_SERVICES = 28;
    private static final int COMMAND_READ_CHARACTERISTIC = 30;
    private static final int COMMAND_READ_DESCRIPTOR = 33;
    private static final int COMMAND_READ_RSSI = 38;
    private static final int COMMAND_REGISTER_CLIENT = 21;
    private static final int COMMAND_REGISTER_FOR_NOTIFICATION = 35;
    private static final int COMMAND_UNREGISTER_CLIENT = 22;
    private static final int COMMAND_WRITE_CHARACTERISTIC = 32;
    private static final int COMMAND_WRITE_DESCRIPTOR = 34;
    private static final String DESCRIPTOR = "ohos.bluetooth.IBluetoothGatt";
    private static final int NO_EXCEPTION = 0;
    private static final int PROFILE_BLE = 11;
    private static final HiLogLabel TAG = new HiLogLabel(3, LogHelper.BT_DOMAIN_ID, "BlePeripheralProxy");

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BlePeripheralProxy.TAG, "registerClient call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0058, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0059, code lost:
        r4.reclaim();
        r5.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x005f, code lost:
        throw r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0047, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0049 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void registerClient(ohos.bluetooth.ble.BlePeripheralCallbackWrapper r5) throws ohos.rpc.RemoteException {
        /*
            r4 = this;
            ohos.rpc.IRemoteObject r0 = r4.getProxy()
            ohos.rpc.MessageParcel r4 = r4.createDataWithToken()
            ohos.utils.SequenceUuid r1 = new ohos.utils.SequenceUuid
            java.util.UUID r2 = java.util.UUID.randomUUID()
            r1.<init>(r2)
            r4.writeSequenceable(r1)
            ohos.rpc.IRemoteObject r5 = r5.asObject()
            r4.writeRemoteObject(r5)
            ohos.rpc.MessageParcel r5 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r1 = new ohos.rpc.MessageOption
            r2 = 0
            r1.<init>(r2)
            r3 = 21
            r0.sendRequest(r3, r4, r5, r1)     // Catch:{ RemoteException -> 0x0049 }
            int r0 = r5.readInt()     // Catch:{ RemoteException -> 0x0049 }
            if (r0 != 0) goto L_0x0037
            r4.reclaim()
            r5.reclaim()
            return
        L_0x0037:
            ohos.hiviewdfx.HiLogLabel r0 = ohos.bluetooth.ble.BlePeripheralProxy.TAG
            java.lang.String r1 = "registerClient got error"
            java.lang.Object[] r3 = new java.lang.Object[r2]
            ohos.hiviewdfx.HiLog.error(r0, r1, r3)
            ohos.rpc.RemoteException r0 = new ohos.rpc.RemoteException
            r0.<init>()
            throw r0
        L_0x0047:
            r0 = move-exception
            goto L_0x0059
        L_0x0049:
            ohos.hiviewdfx.HiLogLabel r0 = ohos.bluetooth.ble.BlePeripheralProxy.TAG     // Catch:{ all -> 0x0047 }
            java.lang.String r1 = "registerClient call fail"
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ all -> 0x0047 }
            ohos.hiviewdfx.HiLog.error(r0, r1, r2)     // Catch:{ all -> 0x0047 }
            ohos.rpc.RemoteException r0 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0047 }
            r0.<init>()     // Catch:{ all -> 0x0047 }
            throw r0     // Catch:{ all -> 0x0047 }
        L_0x0059:
            r4.reclaim()
            r5.reclaim()
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BlePeripheralProxy.registerClient(ohos.bluetooth.ble.BlePeripheralCallbackWrapper):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(2:7|8) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0041, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0047, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0034, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BlePeripheralProxy.TAG, "clientConnect call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0036 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void clientConnect(int r3, java.lang.String r4, boolean r5) {
        /*
            r2 = this;
            ohos.rpc.IRemoteObject r0 = r2.getProxy()     // Catch:{ RemoteException -> 0x0048 }
            ohos.rpc.MessageParcel r2 = r2.createDataWithToken()
            r2.writeInt(r3)
            r2.writeString(r4)
            r3 = 1
            r4 = r5 ^ 1
            r2.writeInt(r4)
            r4 = 2
            r2.writeInt(r4)
            r4 = 0
            r2.writeInt(r4)
            r2.writeInt(r3)
            ohos.rpc.MessageParcel r3 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r5 = new ohos.rpc.MessageOption
            r5.<init>(r4)
            r1 = 23
            r0.sendRequest(r1, r2, r3, r5)     // Catch:{ RemoteException -> 0x0036 }
        L_0x002d:
            r2.reclaim()
            r3.reclaim()
            goto L_0x0040
        L_0x0034:
            r4 = move-exception
            goto L_0x0041
        L_0x0036:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.bluetooth.ble.BlePeripheralProxy.TAG     // Catch:{ all -> 0x0034 }
            java.lang.String r0 = "clientConnect call fail"
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x0034 }
            ohos.hiviewdfx.HiLog.error(r5, r0, r4)     // Catch:{ all -> 0x0034 }
            goto L_0x002d
        L_0x0040:
            return
        L_0x0041:
            r2.reclaim()
            r3.reclaim()
            throw r4
        L_0x0048:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BlePeripheralProxy.clientConnect(int, java.lang.String, boolean):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BlePeripheralProxy.TAG, "discoverServices call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0049, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x004a, code lost:
        r3.reclaim();
        r4.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0050, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0039, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x003b */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void discoverServices(int r4, java.lang.String r5) throws ohos.rpc.RemoteException {
        /*
            r3 = this;
            ohos.rpc.IRemoteObject r0 = r3.getProxy()
            ohos.rpc.MessageParcel r3 = r3.createDataWithToken()
            r3.writeInt(r4)
            r3.writeString(r5)
            ohos.rpc.MessageParcel r4 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r5 = new ohos.rpc.MessageOption
            r1 = 0
            r5.<init>(r1)
            r2 = 28
            r0.sendRequest(r2, r3, r4, r5)     // Catch:{ RemoteException -> 0x003b }
            int r5 = r4.readInt()     // Catch:{ RemoteException -> 0x003b }
            if (r5 != 0) goto L_0x002a
            r3.reclaim()
            r4.reclaim()
            return
        L_0x002a:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.bluetooth.ble.BlePeripheralProxy.TAG
            java.lang.String r0 = "discoverServices got error"
            java.lang.Object[] r2 = new java.lang.Object[r1]
            ohos.hiviewdfx.HiLog.error(r5, r0, r2)
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x0039:
            r5 = move-exception
            goto L_0x004a
        L_0x003b:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.bluetooth.ble.BlePeripheralProxy.TAG     // Catch:{ all -> 0x0039 }
            java.lang.String r0 = "discoverServices call fail"
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x0039 }
            ohos.hiviewdfx.HiLog.error(r5, r0, r1)     // Catch:{ all -> 0x0039 }
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0039 }
            r5.<init>()     // Catch:{ all -> 0x0039 }
            throw r5     // Catch:{ all -> 0x0039 }
        L_0x004a:
            r3.reclaim()
            r4.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BlePeripheralProxy.discoverServices(int, java.lang.String):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BlePeripheralProxy.TAG, "clientDisconnect call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0049, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x004a, code lost:
        r3.reclaim();
        r4.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0050, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0039, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x003b */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void clientDisconnect(int r4, java.lang.String r5) throws ohos.rpc.RemoteException {
        /*
            r3 = this;
            ohos.rpc.IRemoteObject r0 = r3.getProxy()
            ohos.rpc.MessageParcel r3 = r3.createDataWithToken()
            r3.writeInt(r4)
            r3.writeString(r5)
            ohos.rpc.MessageParcel r4 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r5 = new ohos.rpc.MessageOption
            r1 = 0
            r5.<init>(r1)
            r2 = 24
            r0.sendRequest(r2, r3, r4, r5)     // Catch:{ RemoteException -> 0x003b }
            int r5 = r4.readInt()     // Catch:{ RemoteException -> 0x003b }
            if (r5 != 0) goto L_0x002a
            r3.reclaim()
            r4.reclaim()
            return
        L_0x002a:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.bluetooth.ble.BlePeripheralProxy.TAG
            java.lang.String r0 = "clientDisconnect got error"
            java.lang.Object[] r2 = new java.lang.Object[r1]
            ohos.hiviewdfx.HiLog.error(r5, r0, r2)
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x0039:
            r5 = move-exception
            goto L_0x004a
        L_0x003b:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.bluetooth.ble.BlePeripheralProxy.TAG     // Catch:{ all -> 0x0039 }
            java.lang.String r0 = "clientDisconnect call fail"
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x0039 }
            ohos.hiviewdfx.HiLog.error(r5, r0, r1)     // Catch:{ all -> 0x0039 }
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0039 }
            r5.<init>()     // Catch:{ all -> 0x0039 }
            throw r5     // Catch:{ all -> 0x0039 }
        L_0x004a:
            r3.reclaim()
            r4.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BlePeripheralProxy.clientDisconnect(int, java.lang.String):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BlePeripheralProxy.TAG, "unregisterClient call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0048, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0049, code lost:
        r4.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x004f, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0037, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0039 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void unregisterClient(int r5) throws ohos.rpc.RemoteException {
        /*
            r4 = this;
            ohos.rpc.IRemoteObject r0 = r4.getProxy()
            ohos.rpc.MessageParcel r4 = r4.createDataWithToken()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            r4.writeInt(r5)
            r5 = 22
            r0.sendRequest(r5, r4, r1, r2)     // Catch:{ RemoteException -> 0x0039 }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x0039 }
            if (r5 != 0) goto L_0x0027
            r4.reclaim()
            r1.reclaim()
            return
        L_0x0027:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.bluetooth.ble.BlePeripheralProxy.TAG
            java.lang.String r0 = "unregisterClient got error"
            java.lang.Object[] r2 = new java.lang.Object[r3]
            ohos.hiviewdfx.HiLog.error(r5, r0, r2)
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x0037:
            r5 = move-exception
            goto L_0x0049
        L_0x0039:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.bluetooth.ble.BlePeripheralProxy.TAG     // Catch:{ all -> 0x0037 }
            java.lang.String r0 = "unregisterClient call fail"
            java.lang.Object[] r2 = new java.lang.Object[r3]     // Catch:{ all -> 0x0037 }
            ohos.hiviewdfx.HiLog.error(r5, r0, r2)     // Catch:{ all -> 0x0037 }
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0037 }
            r5.<init>()     // Catch:{ all -> 0x0037 }
            throw r5     // Catch:{ all -> 0x0037 }
        L_0x0049:
            r4.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BlePeripheralProxy.unregisterClient(int):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(3:9|10|11) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BlePeripheralProxy.TAG, "readCharacteristic call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0051, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0052, code lost:
        r1.reclaim();
        r2.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0058, code lost:
        throw r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0040, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0042 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void readCharacteristic(int r2, java.lang.String r3, int r4, int r5) throws ohos.rpc.RemoteException {
        /*
            r1 = this;
            ohos.rpc.IRemoteObject r0 = r1.getProxy()
            ohos.rpc.MessageParcel r1 = r1.createDataWithToken()
            r1.writeInt(r2)
            r1.writeString(r3)
            r1.writeInt(r4)
            r1.writeInt(r5)
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r4 = 0
            r3.<init>(r4)
            r5 = 30
            r0.sendRequest(r5, r1, r2, r3)     // Catch:{ RemoteException -> 0x0042 }
            int r3 = r2.readInt()     // Catch:{ RemoteException -> 0x0042 }
            if (r3 != 0) goto L_0x0030
            r1.reclaim()
            r2.reclaim()
            return
        L_0x0030:
            ohos.hiviewdfx.HiLogLabel r3 = ohos.bluetooth.ble.BlePeripheralProxy.TAG
            java.lang.String r5 = "readCharacteristic got error"
            java.lang.Object[] r0 = new java.lang.Object[r4]
            ohos.hiviewdfx.HiLog.error(r3, r5, r0)
            ohos.rpc.RemoteException r3 = new ohos.rpc.RemoteException
            r3.<init>()
            throw r3
        L_0x0040:
            r3 = move-exception
            goto L_0x0052
        L_0x0042:
            ohos.hiviewdfx.HiLogLabel r3 = ohos.bluetooth.ble.BlePeripheralProxy.TAG     // Catch:{ all -> 0x0040 }
            java.lang.String r5 = "readCharacteristic call fail"
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x0040 }
            ohos.hiviewdfx.HiLog.error(r3, r5, r4)     // Catch:{ all -> 0x0040 }
            ohos.rpc.RemoteException r3 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0040 }
            r3.<init>()     // Catch:{ all -> 0x0040 }
            throw r3     // Catch:{ all -> 0x0040 }
        L_0x0052:
            r1.reclaim()
            r2.reclaim()
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BlePeripheralProxy.readCharacteristic(int, java.lang.String, int, int):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(3:9|10|11) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BlePeripheralProxy.TAG, "writeCharacteristic call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0063, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0064, code lost:
        r3.reclaim();
        r4.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x006a, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0052, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0054 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void writeCharacteristic(int r4, java.lang.String r5, int r6, ohos.bluetooth.ble.GattCharacteristic r7) throws ohos.rpc.RemoteException {
        /*
        // Method dump skipped, instructions count: 107
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BlePeripheralProxy.writeCharacteristic(int, java.lang.String, int, ohos.bluetooth.ble.GattCharacteristic):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(3:9|10|11) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BlePeripheralProxy.TAG, "registerForNotification call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0051, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0052, code lost:
        r1.reclaim();
        r2.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0058, code lost:
        throw r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0040, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0042 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void registerForNotification(int r2, java.lang.String r3, int r4, boolean r5) throws ohos.rpc.RemoteException {
        /*
            r1 = this;
            ohos.rpc.IRemoteObject r0 = r1.getProxy()
            ohos.rpc.MessageParcel r1 = r1.createDataWithToken()
            r1.writeInt(r2)
            r1.writeString(r3)
            r1.writeInt(r4)
            r1.writeInt(r5)
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r4 = 0
            r3.<init>(r4)
            r5 = 35
            r0.sendRequest(r5, r1, r2, r3)     // Catch:{ RemoteException -> 0x0042 }
            int r3 = r2.readInt()     // Catch:{ RemoteException -> 0x0042 }
            if (r3 != 0) goto L_0x0030
            r1.reclaim()
            r2.reclaim()
            return
        L_0x0030:
            ohos.hiviewdfx.HiLogLabel r3 = ohos.bluetooth.ble.BlePeripheralProxy.TAG
            java.lang.String r5 = "registerForNotification got error"
            java.lang.Object[] r0 = new java.lang.Object[r4]
            ohos.hiviewdfx.HiLog.error(r3, r5, r0)
            ohos.rpc.RemoteException r3 = new ohos.rpc.RemoteException
            r3.<init>()
            throw r3
        L_0x0040:
            r3 = move-exception
            goto L_0x0052
        L_0x0042:
            ohos.hiviewdfx.HiLogLabel r3 = ohos.bluetooth.ble.BlePeripheralProxy.TAG     // Catch:{ all -> 0x0040 }
            java.lang.String r5 = "registerForNotification call fail"
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x0040 }
            ohos.hiviewdfx.HiLog.error(r3, r5, r4)     // Catch:{ all -> 0x0040 }
            ohos.rpc.RemoteException r3 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0040 }
            r3.<init>()     // Catch:{ all -> 0x0040 }
            throw r3     // Catch:{ all -> 0x0040 }
        L_0x0052:
            r1.reclaim()
            r2.reclaim()
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BlePeripheralProxy.registerForNotification(int, java.lang.String, int, boolean):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(3:9|10|11) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BlePeripheralProxy.TAG, "readDescriptor call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0051, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0052, code lost:
        r1.reclaim();
        r2.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0058, code lost:
        throw r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0040, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0042 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void readDescriptor(int r2, java.lang.String r3, int r4, int r5) throws ohos.rpc.RemoteException {
        /*
            r1 = this;
            ohos.rpc.IRemoteObject r0 = r1.getProxy()
            ohos.rpc.MessageParcel r1 = r1.createDataWithToken()
            r1.writeInt(r2)
            r1.writeString(r3)
            r1.writeInt(r4)
            r1.writeInt(r5)
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r4 = 0
            r3.<init>(r4)
            r5 = 33
            r0.sendRequest(r5, r1, r2, r3)     // Catch:{ RemoteException -> 0x0042 }
            int r3 = r2.readInt()     // Catch:{ RemoteException -> 0x0042 }
            if (r3 != 0) goto L_0x0030
            r1.reclaim()
            r2.reclaim()
            return
        L_0x0030:
            ohos.hiviewdfx.HiLogLabel r3 = ohos.bluetooth.ble.BlePeripheralProxy.TAG
            java.lang.String r5 = "readDescriptor got error"
            java.lang.Object[] r0 = new java.lang.Object[r4]
            ohos.hiviewdfx.HiLog.error(r3, r5, r0)
            ohos.rpc.RemoteException r3 = new ohos.rpc.RemoteException
            r3.<init>()
            throw r3
        L_0x0040:
            r3 = move-exception
            goto L_0x0052
        L_0x0042:
            ohos.hiviewdfx.HiLogLabel r3 = ohos.bluetooth.ble.BlePeripheralProxy.TAG     // Catch:{ all -> 0x0040 }
            java.lang.String r5 = "readDescriptor call fail"
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x0040 }
            ohos.hiviewdfx.HiLog.error(r3, r5, r4)     // Catch:{ all -> 0x0040 }
            ohos.rpc.RemoteException r3 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0040 }
            r3.<init>()     // Catch:{ all -> 0x0040 }
            throw r3     // Catch:{ all -> 0x0040 }
        L_0x0052:
            r1.reclaim()
            r2.reclaim()
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BlePeripheralProxy.readDescriptor(int, java.lang.String, int, int):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(3:9|10|11) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BlePeripheralProxy.TAG, "writeDescriptor call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0054, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0055, code lost:
        r1.reclaim();
        r2.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x005b, code lost:
        throw r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0043, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0045 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void writeDescriptor(int r2, java.lang.String r3, int r4, int r5, byte[] r6) throws ohos.rpc.RemoteException {
        /*
            r1 = this;
            ohos.rpc.IRemoteObject r0 = r1.getProxy()
            ohos.rpc.MessageParcel r1 = r1.createDataWithToken()
            r1.writeInt(r2)
            r1.writeString(r3)
            r1.writeInt(r4)
            r1.writeInt(r5)
            r1.writeByteArray(r6)
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r4 = 0
            r3.<init>(r4)
            r5 = 34
            r0.sendRequest(r5, r1, r2, r3)     // Catch:{ RemoteException -> 0x0045 }
            int r3 = r2.readInt()     // Catch:{ RemoteException -> 0x0045 }
            if (r3 != 0) goto L_0x0033
            r1.reclaim()
            r2.reclaim()
            return
        L_0x0033:
            ohos.hiviewdfx.HiLogLabel r3 = ohos.bluetooth.ble.BlePeripheralProxy.TAG
            java.lang.String r5 = "writeDescriptor got error"
            java.lang.Object[] r6 = new java.lang.Object[r4]
            ohos.hiviewdfx.HiLog.error(r3, r5, r6)
            ohos.rpc.RemoteException r3 = new ohos.rpc.RemoteException
            r3.<init>()
            throw r3
        L_0x0043:
            r3 = move-exception
            goto L_0x0055
        L_0x0045:
            ohos.hiviewdfx.HiLogLabel r3 = ohos.bluetooth.ble.BlePeripheralProxy.TAG     // Catch:{ all -> 0x0043 }
            java.lang.String r5 = "writeDescriptor call fail"
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x0043 }
            ohos.hiviewdfx.HiLog.error(r3, r5, r4)     // Catch:{ all -> 0x0043 }
            ohos.rpc.RemoteException r3 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0043 }
            r3.<init>()     // Catch:{ all -> 0x0043 }
            throw r3     // Catch:{ all -> 0x0043 }
        L_0x0055:
            r1.reclaim()
            r2.reclaim()
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BlePeripheralProxy.writeDescriptor(int, java.lang.String, int, int, byte[]):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BlePeripheralProxy.TAG, "readRemoteRssiValue call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x004b, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x004c, code lost:
        r3.reclaim();
        r4.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0052, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003a, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x003c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void readRemoteRssiValue(int r4, java.lang.String r5) throws ohos.rpc.RemoteException {
        /*
            r3 = this;
            ohos.rpc.IRemoteObject r0 = r3.getProxy()
            ohos.rpc.MessageParcel r3 = r3.createDataWithToken()
            r3.writeInt(r4)
            r3.writeString(r5)
            ohos.rpc.MessageParcel r4 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r5 = new ohos.rpc.MessageOption
            r1 = 0
            r5.<init>(r1)
            r2 = 38
            r0.sendRequest(r2, r3, r4, r5)     // Catch:{ RemoteException -> 0x003c }
            int r5 = r4.readInt()     // Catch:{ RemoteException -> 0x003c }
            if (r5 != 0) goto L_0x002a
            r3.reclaim()
            r4.reclaim()
            return
        L_0x002a:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.bluetooth.ble.BlePeripheralProxy.TAG
            java.lang.String r0 = "readRemoteRssiValue got error"
            java.lang.Object[] r2 = new java.lang.Object[r1]
            ohos.hiviewdfx.HiLog.error(r5, r0, r2)
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x003a:
            r5 = move-exception
            goto L_0x004c
        L_0x003c:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.bluetooth.ble.BlePeripheralProxy.TAG     // Catch:{ all -> 0x003a }
            java.lang.String r0 = "readRemoteRssiValue call fail"
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x003a }
            ohos.hiviewdfx.HiLog.error(r5, r0, r1)     // Catch:{ all -> 0x003a }
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x003a }
            r5.<init>()     // Catch:{ all -> 0x003a }
            throw r5     // Catch:{ all -> 0x003a }
        L_0x004c:
            r3.reclaim()
            r4.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BlePeripheralProxy.readRemoteRssiValue(int, java.lang.String):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(3:9|10|11) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BlePeripheralProxy.TAG, "requestBleConnectionPriority call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x004e, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x004f, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0055, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003d, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x003f */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void requestBleConnectionPriority(int r3, java.lang.String r4, int r5) throws ohos.rpc.RemoteException {
        /*
            r2 = this;
            ohos.rpc.IRemoteObject r0 = r2.getProxy()
            ohos.rpc.MessageParcel r2 = r2.createDataWithToken()
            r2.writeInt(r3)
            r2.writeString(r4)
            r2.writeInt(r5)
            ohos.rpc.MessageParcel r3 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r4 = new ohos.rpc.MessageOption
            r5 = 0
            r4.<init>(r5)
            r1 = 40
            r0.sendRequest(r1, r2, r3, r4)     // Catch:{ RemoteException -> 0x003f }
            int r4 = r3.readInt()     // Catch:{ RemoteException -> 0x003f }
            if (r4 != 0) goto L_0x002d
            r2.reclaim()
            r3.reclaim()
            return
        L_0x002d:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.bluetooth.ble.BlePeripheralProxy.TAG
            java.lang.String r0 = "requestBleConnectionPriority got error"
            java.lang.Object[] r1 = new java.lang.Object[r5]
            ohos.hiviewdfx.HiLog.error(r4, r0, r1)
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException
            r4.<init>()
            throw r4
        L_0x003d:
            r4 = move-exception
            goto L_0x004f
        L_0x003f:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.bluetooth.ble.BlePeripheralProxy.TAG     // Catch:{ all -> 0x003d }
            java.lang.String r0 = "requestBleConnectionPriority call fail"
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ all -> 0x003d }
            ohos.hiviewdfx.HiLog.error(r4, r0, r5)     // Catch:{ all -> 0x003d }
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x003d }
            r4.<init>()     // Catch:{ all -> 0x003d }
            throw r4     // Catch:{ all -> 0x003d }
        L_0x004f:
            r2.reclaim()
            r3.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BlePeripheralProxy.requestBleConnectionPriority(int, java.lang.String, int):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(3:9|10|11) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.ble.BlePeripheralProxy.TAG, "requestBleMtuSize call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x004e, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x004f, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0055, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003d, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x003f */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void requestBleMtuSize(int r3, java.lang.String r4, int r5) throws ohos.rpc.RemoteException {
        /*
            r2 = this;
            ohos.rpc.IRemoteObject r0 = r2.getProxy()
            ohos.rpc.MessageParcel r2 = r2.createDataWithToken()
            r2.writeInt(r3)
            r2.writeString(r4)
            r2.writeInt(r5)
            ohos.rpc.MessageParcel r3 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r4 = new ohos.rpc.MessageOption
            r5 = 0
            r4.<init>(r5)
            r1 = 39
            r0.sendRequest(r1, r2, r3, r4)     // Catch:{ RemoteException -> 0x003f }
            int r4 = r3.readInt()     // Catch:{ RemoteException -> 0x003f }
            if (r4 != 0) goto L_0x002d
            r2.reclaim()
            r3.reclaim()
            return
        L_0x002d:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.bluetooth.ble.BlePeripheralProxy.TAG
            java.lang.String r0 = "requestBleMtuSize got error"
            java.lang.Object[] r1 = new java.lang.Object[r5]
            ohos.hiviewdfx.HiLog.error(r4, r0, r1)
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException
            r4.<init>()
            throw r4
        L_0x003d:
            r4 = move-exception
            goto L_0x004f
        L_0x003f:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.bluetooth.ble.BlePeripheralProxy.TAG     // Catch:{ all -> 0x003d }
            java.lang.String r0 = "requestBleMtuSize call fail"
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ all -> 0x003d }
            ohos.hiviewdfx.HiLog.error(r4, r0, r5)     // Catch:{ all -> 0x003d }
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x003d }
            r4.<init>()     // Catch:{ all -> 0x003d }
            throw r4     // Catch:{ all -> 0x003d }
        L_0x004f:
            r2.reclaim()
            r3.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.ble.BlePeripheralProxy.requestBleMtuSize(int, java.lang.String, int):void");
    }

    private IRemoteObject getProxy() throws RemoteException {
        Optional<IRemoteObject> saProfileProxy = BluetoothHostProxy.getInstace().getSaProfileProxy(11);
        if (saProfileProxy.isPresent()) {
            return saProfileProxy.get();
        }
        HiLog.error(TAG, "ble peripheral proxy is null", new Object[0]);
        throw new RemoteException();
    }

    private MessageParcel createDataWithToken() {
        MessageParcel obtain = MessageParcel.obtain();
        obtain.writeInt(1);
        obtain.writeInt(1);
        obtain.writeString(BluetoothAdapterUtils.stringReplace(DESCRIPTOR));
        return obtain;
    }
}
