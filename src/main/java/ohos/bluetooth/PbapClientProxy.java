package ohos.bluetooth;

import java.util.function.Consumer;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;

class PbapClientProxy implements IPbapClient {
    private static final int COMMAND_CONNECT_CLIENT = 3;
    private static final int COMMAND_DISCONNECT_CLIENT = 4;
    private static final int COMMAND_GET_CONNECT_STRATEGY_CLIENT = 6;
    private static final int COMMAND_GET_DEVICES_BY_STATES_CLIENT = 1;
    private static final int COMMAND_GET_DEVICE_STATE_CLIENT = 2;
    private static final int COMMAND_SET_CONNECT_STRATEGY_CLIENT = 5;
    private static final int DEFAULT_PBAP_CLIENT_NUM = 5;
    private static final int ERR_OK = 0;
    private static final int MIN_TRANSACTION_ID = 1;
    private static final int STRATEGY_ALLOW = 100;
    private static final int STRATEGY_AUTO = 1000;
    private static final int STRATEGY_DISALLOW = 0;
    private static final int STRATEGY_UNKNOWN = -1;
    private static final HiLogLabel TAG = new HiLogLabel(3, LogHelper.BT_DOMAIN_ID, "PbapClientProxy");
    private IRemoteObject remote;

    PbapClientProxy() {
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        this.remote = null;
        BluetoothHostProxy.getInstace().getSaProfileProxy(8).ifPresent(new Consumer() {
            /* class ohos.bluetooth.$$Lambda$PbapClientProxy$AdjXoIvlISrtRxk7Vjq6GDqUKFg */

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                PbapClientProxy.this.lambda$asObject$0$PbapClientProxy((IRemoteObject) obj);
            }
        });
        return this.remote;
    }

    public /* synthetic */ void lambda$asObject$0$PbapClientProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x006b, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.PbapClientProxy.TAG, "PbapClientProxy getDevicesByStates : call error", new java.lang.Object[0]);
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
    @Override // ohos.bluetooth.IPbapClient
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<ohos.bluetooth.BluetoothRemoteDevice> getDevicesByStates(int[] r6) {
        /*
        // Method dump skipped, instructions count: 137
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.PbapClientProxy.getDevicesByStates(int[]):java.util.List");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:17|18|19|20) */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0061, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.PbapClientProxy.TAG, "getDeviceState : call error", new java.lang.Object[0]);
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
    @Override // ohos.bluetooth.IPbapClient
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getDeviceState(ohos.bluetooth.BluetoothRemoteDevice r6) {
        /*
        // Method dump skipped, instructions count: 122
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.PbapClientProxy.getDeviceState(ohos.bluetooth.BluetoothRemoteDevice):int");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:17|18|19|20) */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0061, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.PbapClientProxy.TAG, "disconnect client : a remote exception occurred", new java.lang.Object[0]);
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
    @Override // ohos.bluetooth.IPbapClient
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean disconnect(ohos.bluetooth.BluetoothRemoteDevice r6) {
        /*
        // Method dump skipped, instructions count: 122
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.PbapClientProxy.disconnect(ohos.bluetooth.BluetoothRemoteDevice):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:17|18|19|20) */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0061, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.PbapClientProxy.TAG, "connect client : a remote exception occurred", new java.lang.Object[0]);
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
    @Override // ohos.bluetooth.IPbapClient
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean connect(ohos.bluetooth.BluetoothRemoteDevice r6) {
        /*
        // Method dump skipped, instructions count: 122
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.PbapClientProxy.connect(ohos.bluetooth.BluetoothRemoteDevice):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:22|23|24|25) */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x006e, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.PbapClientProxy.TAG, "setConnectStrategy : a remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0080, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0081, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0087, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:22:0x0070 */
    @Override // ohos.bluetooth.IPbapClient
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setConnectStrategy(ohos.bluetooth.BluetoothRemoteDevice r7, int r8) {
        /*
        // Method dump skipped, instructions count: 147
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.PbapClientProxy.setConnectStrategy(ohos.bluetooth.BluetoothRemoteDevice, int):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:28|29|30|31) */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0073, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.PbapClientProxy.TAG, "getConnectStrategy : call error", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0084, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0085, code lost:
        r0.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x008b, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:28:0x0075 */
    @Override // ohos.bluetooth.IPbapClient
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getConnectStrategy(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 140
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.PbapClientProxy.getConnectStrategy(ohos.bluetooth.BluetoothRemoteDevice):int");
    }
}
