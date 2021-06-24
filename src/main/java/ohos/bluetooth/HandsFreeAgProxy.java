package ohos.bluetooth;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;

class HandsFreeAgProxy implements IHandsFreeAg {
    private static final int COMMAND_ALL_CONNECTED = 10;
    private static final int COMMAND_CONNECT_AUDIO = 8;
    private static final int COMMAND_CONNECT_HF_DEVICE = 4;
    private static final int COMMAND_DISCONNECT_AUDIO = 9;
    private static final int COMMAND_DISCONNECT_HF_DEVICE = 5;
    private static final int COMMAND_GET_DEVICES_BY_STATES_AG = 1;
    private static final int COMMAND_GET_DEVICE_STATE_AG = 2;
    private static final int COMMAND_GET_SCO_STATE_AG = 3;
    private static final int COMMAND_START_VOICE_RECOGNITION = 6;
    private static final int COMMAND_STOP_VOICE_RECOGNITION = 7;
    private static final int DEFAULT_HANDS_FREE_AG_NUM = 5;
    private static final int ERR_OK = 0;
    private static final int MIN_TRANSACTION_ID = 1;
    private static final HiLogLabel TAG = new HiLogLabel(3, LogHelper.BT_DOMAIN_ID, "HandsFreeAgProxy");
    private final Object mRemoteLock = new Object();
    private IRemoteObject mRemoteService;

    HandsFreeAgProxy() {
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        synchronized (this.mRemoteLock) {
            if (this.mRemoteService != null) {
                return this.mRemoteService;
            }
            this.mRemoteService = BluetoothHostProxy.getInstace().getSaProfileProxy(1).orElse(null);
            if (this.mRemoteService == null) {
                HiLog.error(TAG, "get HFP failed.", new Object[0]);
            } else {
                this.mRemoteService.addDeathRecipient(new HandsFreeAgDeathRecipient(), 0);
            }
            return this.mRemoteService;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setRemoteObject(IRemoteObject iRemoteObject) {
        synchronized (this.mRemoteLock) {
            HiLog.info(TAG, "HandsFreeAgProxy::setRemoteObject.", new Object[0]);
            this.mRemoteService = iRemoteObject;
        }
    }

    /* access modifiers changed from: private */
    public class HandsFreeAgDeathRecipient implements IRemoteObject.DeathRecipient {
        private HandsFreeAgDeathRecipient() {
        }

        @Override // ohos.rpc.IRemoteObject.DeathRecipient
        public void onRemoteDied() {
            HiLog.warn(HandsFreeAgProxy.TAG, "HandsFreeAgDeathRecipient::onRemoteDied.", new Object[0]);
            HandsFreeAgProxy.this.setRemoteObject(null);
        }
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0069, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HandsFreeAgProxy.TAG, "getDevicesByStates for HFP Audio Gateway: call error", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0074, code lost:
        r2.reclaim();
        r3.reclaim();
        r6 = new java.util.ArrayList();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0080, code lost:
        return r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0081, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0087, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x006b */
    @Override // ohos.bluetooth.IHandsFreeAg
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<ohos.bluetooth.BluetoothRemoteDevice> getDevicesByStates(int[] r7) {
        /*
        // Method dump skipped, instructions count: 139
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HandsFreeAgProxy.getDevicesByStates(int[]):java.util.List");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x005e, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HandsFreeAgProxy.TAG, "getScoState : call error", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0070, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0071, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0077, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0060 */
    @Override // ohos.bluetooth.IHandsFreeAg
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getScoState(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 123
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HandsFreeAgProxy.getScoState(ohos.bluetooth.BluetoothRemoteDevice):int");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x005e, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HandsFreeAgProxy.TAG, "getDeviceState : call error", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0070, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0071, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0077, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0060 */
    @Override // ohos.bluetooth.IHandsFreeAg
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getDeviceState(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 123
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HandsFreeAgProxy.getDeviceState(ohos.bluetooth.BluetoothRemoteDevice):int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0079, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HandsFreeAgProxy.TAG, "connect : a remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0084, code lost:
        r2.reclaim();
        r3.reclaim();
        ohos.hiviewdfx.HiLog.debug(ohos.bluetooth.HandsFreeAgProxy.TAG, "parcel reclaimed after HFP ag connecting", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0095, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0096, code lost:
        r2.reclaim();
        r3.reclaim();
        ohos.hiviewdfx.HiLog.debug(ohos.bluetooth.HandsFreeAgProxy.TAG, "parcel reclaimed after HFP ag connecting", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00a6, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x007b */
    @Override // ohos.bluetooth.IHandsFreeAg
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean connect(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 170
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HandsFreeAgProxy.connect(ohos.bluetooth.BluetoothRemoteDevice):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0065, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HandsFreeAgProxy.TAG, "disconnect : a remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0077, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0078, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x007e, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0067 */
    @Override // ohos.bluetooth.IHandsFreeAg
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean disconnect(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 130
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HandsFreeAgProxy.disconnect(ohos.bluetooth.BluetoothRemoteDevice):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0067, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HandsFreeAgProxy.TAG, "openVoiceRecognition : a remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x007a, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x007b, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0081, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0069 */
    @Override // ohos.bluetooth.IHandsFreeAg
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean openVoiceRecognition(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 133
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HandsFreeAgProxy.openVoiceRecognition(ohos.bluetooth.BluetoothRemoteDevice):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0065, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HandsFreeAgProxy.TAG, "closeVoiceRecognition : a remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0077, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0078, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x007e, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0067 */
    @Override // ohos.bluetooth.IHandsFreeAg
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean closeVoiceRecognition(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 130
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HandsFreeAgProxy.closeVoiceRecognition(ohos.bluetooth.BluetoothRemoteDevice):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0063, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HandsFreeAgProxy.TAG, "connectSco : a remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0075, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0076, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x007c, code lost:
        throw r7;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0065 */
    @Override // ohos.bluetooth.IHandsFreeAg
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean connectSco() {
        /*
        // Method dump skipped, instructions count: 128
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HandsFreeAgProxy.connectSco():boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0063, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HandsFreeAgProxy.TAG, "disconnectSco : a remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0075, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0076, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x007c, code lost:
        throw r7;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0065 */
    @Override // ohos.bluetooth.IHandsFreeAg
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean disconnectSco() {
        /*
        // Method dump skipped, instructions count: 128
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HandsFreeAgProxy.disconnectSco():boolean");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:36|37|38|39|40) */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00a3, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HandsFreeAgProxy.TAG, "getConnectedDevices : a remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00ae, code lost:
        r2.reclaim();
        r3.reclaim();
        r7 = new java.util.ArrayList();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00ba, code lost:
        return r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00bb, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00c1, code lost:
        throw r7;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:36:0x00a5 */
    @Override // ohos.bluetooth.IHandsFreeAg
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<ohos.bluetooth.BluetoothRemoteDevice> getConnectedDevices() {
        /*
        // Method dump skipped, instructions count: 197
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HandsFreeAgProxy.getConnectedDevices():java.util.List");
    }
}
