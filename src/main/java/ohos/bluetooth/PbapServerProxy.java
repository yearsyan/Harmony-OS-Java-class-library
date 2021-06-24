package ohos.bluetooth;

import java.util.function.Consumer;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;

class PbapServerProxy implements IPbapServer {
    private static final int COMMAND_DISCONNECT_SERVER = 4;
    private static final int COMMAND_GET_DEVICES_BY_STATES_SERVER = 1;
    private static final int COMMAND_GET_DEVICE_STATE_SERVER = 2;
    private static final int DEFAULT_PBAP_SERVER_NUM = 5;
    private static final int ERR_OK = 0;
    private static final int MIN_TRANSACTION_ID = 1;
    private static final int STRATEGY_ALLOW = 100;
    private static final int STRATEGY_AUTO = 1000;
    private static final int STRATEGY_DISALLOW = 0;
    private static final int STRATEGY_UNKNOWN = -1;
    private static final HiLogLabel TAG = new HiLogLabel(3, LogHelper.BT_DOMAIN_ID, "PbapServerProxy");
    private IRemoteObject remote;

    PbapServerProxy() {
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        this.remote = null;
        BluetoothHostProxy.getInstace().getSaProfileProxy(7).ifPresent(new Consumer() {
            /* class ohos.bluetooth.$$Lambda$PbapServerProxy$IQKgRwlWvRCC_8C1QcerGnEFdg */

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                PbapServerProxy.this.lambda$asObject$0$PbapServerProxy((IRemoteObject) obj);
            }
        });
        return this.remote;
    }

    public /* synthetic */ void lambda$asObject$0$PbapServerProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x005f, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.PbapServerProxy.TAG, "getDevicesByStates : call error", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x006a, code lost:
        r0.reclaim();
        r2.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0075, code lost:
        return new java.util.ArrayList();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0076, code lost:
        r0.reclaim();
        r2.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x007c, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x0061 */
    @Override // ohos.bluetooth.IPbapServer
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<ohos.bluetooth.BluetoothRemoteDevice> getDevicesByStates(int[] r6) {
        /*
        // Method dump skipped, instructions count: 125
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.PbapServerProxy.getDevicesByStates(int[]):java.util.List");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:17|18|19|20) */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0061, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.PbapServerProxy.TAG, "disconnect server : a remote exception occurred", new java.lang.Object[0]);
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
    @Override // ohos.bluetooth.IPbapServer
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean disconnect(ohos.bluetooth.BluetoothRemoteDevice r6) {
        /*
        // Method dump skipped, instructions count: 122
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.PbapServerProxy.disconnect(ohos.bluetooth.BluetoothRemoteDevice):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:14|15|16|17) */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0055, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.PbapServerProxy.TAG, "getDeviceState : call error", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0066, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0067, code lost:
        r0.reclaim();
        r2.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x006d, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x0057 */
    @Override // ohos.bluetooth.IPbapServer
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getDeviceState(ohos.bluetooth.BluetoothRemoteDevice r6) {
        /*
        // Method dump skipped, instructions count: 110
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.PbapServerProxy.getDeviceState(ohos.bluetooth.BluetoothRemoteDevice):int");
    }
}
