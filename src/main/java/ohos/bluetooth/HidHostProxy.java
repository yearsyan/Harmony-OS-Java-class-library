package ohos.bluetooth;

import java.util.function.Consumer;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;

class HidHostProxy implements IHidHost {
    private static final int COMMAND_CONNECT_HOST = 3;
    private static final int COMMAND_DISCONNECT_HOST = 4;
    private static final int COMMAND_GET_DEVICES_BY_STATES_HOST = 1;
    private static final int COMMAND_GET_DEVICE_STATE_HOST = 2;
    private static final int DEFAULT_HID_HOST_NUM = 5;
    private static final int ERR_OK = 0;
    private static final int MIN_TRANSACTION_ID = 1;
    private static final HiLogLabel TAG = new HiLogLabel(3, LogHelper.BT_DOMAIN_ID, "HidHostProxy");
    private IRemoteObject remote;

    HidHostProxy() {
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        this.remote = null;
        BluetoothHostProxy.getInstace().getSaProfileProxy(14).ifPresent(new Consumer() {
            /* class ohos.bluetooth.$$Lambda$HidHostProxy$Zo7G9YQ1p8HWFGbH7yxNzpljXA */

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                HidHostProxy.this.lambda$asObject$0$HidHostProxy((IRemoteObject) obj);
            }
        });
        return this.remote;
    }

    public /* synthetic */ void lambda$asObject$0$HidHostProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x006b, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HidHostProxy.TAG, "HidHostProxy getDevicesByStates : call error", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0076, code lost:
        r0.reclaim();
        r2.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0081, code lost:
        return new java.util.ArrayList();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0082, code lost:
        r0.reclaim();
        r2.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0088, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x006d */
    @Override // ohos.bluetooth.IHidHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<ohos.bluetooth.BluetoothRemoteDevice> getDevicesByStates(int[] r6) {
        /*
        // Method dump skipped, instructions count: 137
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HidHostProxy.getDevicesByStates(int[]):java.util.List");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:17|18|19|20) */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0061, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HidHostProxy.TAG, "getDeviceState : call error", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0072, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0073, code lost:
        r0.reclaim();
        r2.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0079, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0063 */
    @Override // ohos.bluetooth.IHidHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getDeviceState(ohos.bluetooth.BluetoothRemoteDevice r6) {
        /*
        // Method dump skipped, instructions count: 122
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HidHostProxy.getDeviceState(ohos.bluetooth.BluetoothRemoteDevice):int");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:17|18|19|20) */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0061, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HidHostProxy.TAG, "disconnect hid device : a remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0072, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0073, code lost:
        r0.reclaim();
        r2.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0079, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0063 */
    @Override // ohos.bluetooth.IHidHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean disconnect(ohos.bluetooth.BluetoothRemoteDevice r6) {
        /*
        // Method dump skipped, instructions count: 122
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HidHostProxy.disconnect(ohos.bluetooth.BluetoothRemoteDevice):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:17|18|19|20) */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0061, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HidHostProxy.TAG, "connect hid device : a remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0072, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0073, code lost:
        r0.reclaim();
        r2.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0079, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0063 */
    @Override // ohos.bluetooth.IHidHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean connect(ohos.bluetooth.BluetoothRemoteDevice r6) {
        /*
        // Method dump skipped, instructions count: 122
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HidHostProxy.connect(ohos.bluetooth.BluetoothRemoteDevice):boolean");
    }
}
