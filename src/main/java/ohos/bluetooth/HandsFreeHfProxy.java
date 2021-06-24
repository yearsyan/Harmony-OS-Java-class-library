package ohos.bluetooth;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;

class HandsFreeHfProxy implements IHandsFreeHf {
    private static final int COMMAND_ACCEPT_CALL_HF = 5;
    private static final int COMMAND_CONNECT_AG_DEVICE_HF = 12;
    private static final int COMMAND_CONNECT_AUDIO_HF = 10;
    private static final int COMMAND_DIAL_HF = 4;
    private static final int COMMAND_DISCONNECT_AG_DEVICE_HF = 13;
    private static final int COMMAND_DISCONNECT_AUDIO_HF = 11;
    private static final int COMMAND_GET_DEVICES_BY_STATES_HF = 1;
    private static final int COMMAND_GET_DEVICE_STATE_HF = 2;
    private static final int COMMAND_GET_SCO_STATE_HF = 3;
    private static final int COMMAND_HOLD_CALL_HF = 6;
    private static final int COMMAND_REJECT_CALL_HF = 7;
    private static final int COMMAND_SEND_DTMF_HF = 9;
    private static final int COMMAND_TERMINATE_CALL_HF = 8;
    private static final int DEFAULT_HANDS_FREE_HF_NUM = 5;
    private static final int ERR_OK = 0;
    private static final int MIN_TRANSACTION_ID = 1;
    private static final HiLogLabel TAG = new HiLogLabel(3, LogHelper.BT_DOMAIN_ID, "HandsFreeHfProxy");
    private final Object mRemoteLock = new Object();
    private IRemoteObject mRemoteService;

    HandsFreeHfProxy() {
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        synchronized (this.mRemoteLock) {
            if (this.mRemoteService != null) {
                return this.mRemoteService;
            }
            this.mRemoteService = BluetoothHostProxy.getInstace().getSaProfileProxy(2).orElse(null);
            if (this.mRemoteService == null) {
                HiLog.error(TAG, "get HFP_UNIT failed.", new Object[0]);
            } else {
                this.mRemoteService.addDeathRecipient(new HandsFreeHfProxyDeathRecipient(), 0);
            }
            return this.mRemoteService;
        }
    }

    /* access modifiers changed from: private */
    public class HandsFreeHfProxyDeathRecipient implements IRemoteObject.DeathRecipient {
        private HandsFreeHfProxyDeathRecipient() {
        }

        @Override // ohos.rpc.IRemoteObject.DeathRecipient
        public void onRemoteDied() {
            HiLog.warn(HandsFreeHfProxy.TAG, "HandsFreeHfProxyDeathRecipient::onRemoteDied.", new Object[0]);
            HandsFreeHfProxy.this.setRemoteObject(null);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setRemoteObject(IRemoteObject iRemoteObject) {
        synchronized (this.mRemoteLock) {
            HiLog.info(TAG, "HandsFreeHfProxy::setRemoteObject.", new Object[0]);
            this.mRemoteService = iRemoteObject;
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0083, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HandsFreeHfProxy.TAG, "getDevicesByStates for HFP hands-free unit : call error", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x008e, code lost:
        r2.reclaim();
        r3.reclaim();
        ohos.hiviewdfx.HiLog.debug(ohos.bluetooth.HandsFreeHfProxy.TAG, "parcel reclaimed after HFP hands-free connecting", new java.lang.Object[0]);
        r6 = new java.util.ArrayList();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00a4, code lost:
        return r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00a5, code lost:
        r2.reclaim();
        r3.reclaim();
        ohos.hiviewdfx.HiLog.debug(ohos.bluetooth.HandsFreeHfProxy.TAG, "parcel reclaimed after HFP hands-free connecting", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00b5, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0085 */
    @Override // ohos.bluetooth.IHandsFreeHf
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<ohos.bluetooth.BluetoothRemoteDevice> getDevicesByStates(int[] r7) {
        /*
        // Method dump skipped, instructions count: 185
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HandsFreeHfProxy.getDevicesByStates(int[]):java.util.List");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0065, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HandsFreeHfProxy.TAG, "getDeviceState : call error", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0077, code lost:
        return 0;
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
    @Override // ohos.bluetooth.IHandsFreeHf
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getDeviceState(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 130
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HandsFreeHfProxy.getDeviceState(ohos.bluetooth.BluetoothRemoteDevice):int");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0065, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HandsFreeHfProxy.TAG, "getScoState : call error", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0077, code lost:
        return 0;
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
    @Override // ohos.bluetooth.IHandsFreeHf
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getScoState(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 130
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HandsFreeHfProxy.getScoState(ohos.bluetooth.BluetoothRemoteDevice):int");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0078, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HandsFreeHfProxy.TAG, "dial : a remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0083, code lost:
        r2.reclaim();
        r3.reclaim();
        r6 = java.util.Optional.empty();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x008e, code lost:
        return r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x008f, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0095, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x007a */
    @Override // ohos.bluetooth.IHandsFreeHf
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Optional<ohos.bluetooth.HandsFreeUnitCall> startDial(ohos.bluetooth.BluetoothRemoteDevice r7, java.lang.String r8) {
        /*
        // Method dump skipped, instructions count: 153
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HandsFreeHfProxy.startDial(ohos.bluetooth.BluetoothRemoteDevice, java.lang.String):java.util.Optional");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0068, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HandsFreeHfProxy.TAG, "acceptCall : a remote exception occurred", new java.lang.Object[0]);
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
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x006a */
    @Override // ohos.bluetooth.IHandsFreeHf
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean acceptIncomingCall(ohos.bluetooth.BluetoothRemoteDevice r7, int r8) {
        /*
        // Method dump skipped, instructions count: 133
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HandsFreeHfProxy.acceptIncomingCall(ohos.bluetooth.BluetoothRemoteDevice, int):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0065, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HandsFreeHfProxy.TAG, "holdCall : a remote exception occurred", new java.lang.Object[0]);
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
    @Override // ohos.bluetooth.IHandsFreeHf
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean holdActiveCall(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 130
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HandsFreeHfProxy.holdActiveCall(ohos.bluetooth.BluetoothRemoteDevice):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0067, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HandsFreeHfProxy.TAG, "rejectCall : a remote exception occurred", new java.lang.Object[0]);
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
    @Override // ohos.bluetooth.IHandsFreeHf
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean rejectIncomingCall(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 133
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HandsFreeHfProxy.rejectIncomingCall(ohos.bluetooth.BluetoothRemoteDevice):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x006b, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HandsFreeHfProxy.TAG, "terminateCall : a remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x007e, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x007f, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0085, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x006d */
    @Override // ohos.bluetooth.IHandsFreeHf
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean finishActiveCall(ohos.bluetooth.BluetoothRemoteDevice r7, ohos.bluetooth.HandsFreeUnitCall r8) {
        /*
        // Method dump skipped, instructions count: 137
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HandsFreeHfProxy.finishActiveCall(ohos.bluetooth.BluetoothRemoteDevice, ohos.bluetooth.HandsFreeUnitCall):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x006b, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HandsFreeHfProxy.TAG, "sendDTMF : a remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x007e, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x007f, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0085, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x006d */
    @Override // ohos.bluetooth.IHandsFreeHf
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean sendDTMFTone(ohos.bluetooth.BluetoothRemoteDevice r7, byte r8) {
        /*
        // Method dump skipped, instructions count: 137
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HandsFreeHfProxy.sendDTMFTone(ohos.bluetooth.BluetoothRemoteDevice, byte):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0066, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HandsFreeHfProxy.TAG, "connectAudio : a remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0078, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0079, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x007f, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0068 */
    @Override // ohos.bluetooth.IHandsFreeHf
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean connectSco(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 131
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HandsFreeHfProxy.connectSco(ohos.bluetooth.BluetoothRemoteDevice):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0066, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HandsFreeHfProxy.TAG, "disconnectAudio : a remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0078, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0079, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x007f, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0068 */
    @Override // ohos.bluetooth.IHandsFreeHf
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean disconnectSco(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 131
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HandsFreeHfProxy.disconnectSco(ohos.bluetooth.BluetoothRemoteDevice):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0066, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HandsFreeHfProxy.TAG, "connect hf : a remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0078, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0079, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x007f, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0068 */
    @Override // ohos.bluetooth.IHandsFreeHf
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean connect(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 131
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HandsFreeHfProxy.connect(ohos.bluetooth.BluetoothRemoteDevice):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0066, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.HandsFreeHfProxy.TAG, "disconnect hf : a remote exception occurred", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0078, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0079, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x007f, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0068 */
    @Override // ohos.bluetooth.IHandsFreeHf
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean disconnect(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 131
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.HandsFreeHfProxy.disconnect(ohos.bluetooth.BluetoothRemoteDevice):boolean");
    }
}
