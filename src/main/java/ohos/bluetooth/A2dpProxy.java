package ohos.bluetooth;

import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.sysability.samgr.SysAbilityManager;
import ohos.utils.system.safwk.java.SystemAbilityDefinition;

class A2dpProxy implements IA2dp {
    private static final int DEFAULT_A2DP_DEVICE_NUM = 5;
    private static final int STRATEGY_ALLOW = 100;
    private static final int STRATEGY_AUTO = 1000;
    private static final int STRATEGY_DISALLOW = 0;
    private static final int STRATEGY_UNKNOWN = -1;
    private static final HiLogLabel TAG = new HiLogLabel(3, LogHelper.BT_DOMAIN_ID, "A2dpProxy");
    private static final int TRANSACT_VALUE_A2DP_SINK_CONNECT_SOURCE_REMOTE = 253;
    private static final int TRANSACT_VALUE_A2DP_SINK_DISCONNECT_SOURCE_REMOTE = 254;
    private static final int TRANSACT_VALUE_A2DP_SINK_GET_CONNECT_STRATEGY = 256;
    private static final int TRANSACT_VALUE_A2DP_SINK_GET_DEVICES_BY_STATE = 250;
    private static final int TRANSACT_VALUE_A2DP_SINK_GET_DEVICE_STATE = 251;
    private static final int TRANSACT_VALUE_A2DP_SINK_GET_PLAYING_STATE = 252;
    private static final int TRANSACT_VALUE_A2DP_SINK_SET_CONNECT_STRATEGY = 255;
    private static final int TRANSACT_VALUE_A2DP_SOURCE_CONNECT_SINK_REMOTE = 203;
    private static final int TRANSACT_VALUE_A2DP_SOURCE_DISCONNECT_SINK_REMOTE = 204;
    private static final int TRANSACT_VALUE_A2DP_SOURCE_ENABLE_DISABLE_OPTIONAL_CODECS = 211;
    private static final int TRANSACT_VALUE_A2DP_SOURCE_GET_ACTIVE_DEVICE = 206;
    private static final int TRANSACT_VALUE_A2DP_SOURCE_GET_CODEC_STATUS = 209;
    private static final int TRANSACT_VALUE_A2DP_SOURCE_GET_CONNECT_STRATEGY = 208;
    private static final int TRANSACT_VALUE_A2DP_SOURCE_GET_DEVICES_BY_STATE = 200;
    private static final int TRANSACT_VALUE_A2DP_SOURCE_GET_DEVICE_STATE = 201;
    private static final int TRANSACT_VALUE_A2DP_SOURCE_GET_OPTIONAL_CODECS_OPTION = 213;
    private static final int TRANSACT_VALUE_A2DP_SOURCE_GET_PLAYING_STATE = 202;
    private static final int TRANSACT_VALUE_A2DP_SOURCE_IS_OPTIONAL_CODECS_SUPPORTED = 212;
    private static final int TRANSACT_VALUE_A2DP_SOURCE_SET_ACTIVE_DEVICE = 205;
    private static final int TRANSACT_VALUE_A2DP_SOURCE_SET_CODEC_PRE = 210;
    private static final int TRANSACT_VALUE_A2DP_SOURCE_SET_CONNECT_STRATEGY = 207;
    private static final int TRANSACT_VALUE_A2DP_SOURCE_SET_OPTIONAL_CODECS_OPTION = 214;
    private static A2dpProxy sInstance = null;
    private IRemoteObject mBluetoothService = null;
    private final Context mContext;
    private final Object mRemoteLock = new Object();

    private int getFixStrategy(int i) {
        if (i == -1) {
            return -1;
        }
        if (i == 0) {
            return 0;
        }
        if (i != 100) {
            return i != 1000 ? -1 : 2;
        }
        return 1;
    }

    private A2dpProxy(Context context) {
        this.mContext = context;
    }

    public static synchronized A2dpProxy getInstace(Context context) {
        A2dpProxy a2dpProxy;
        synchronized (A2dpProxy.class) {
            if (sInstance == null) {
                sInstance = new A2dpProxy(context);
            }
            a2dpProxy = sInstance;
        }
        return a2dpProxy;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        synchronized (this.mRemoteLock) {
            if (this.mBluetoothService != null) {
                return this.mBluetoothService;
            }
            this.mBluetoothService = SysAbilityManager.getSysAbility(SystemAbilityDefinition.BLUETOOTH_HOST_SYS_ABILITY_ID);
            if (this.mBluetoothService == null) {
                HiLog.error(TAG, "getSysAbility for A2dp failed.", new Object[0]);
            } else {
                this.mBluetoothService.addDeathRecipient(new A2dpDeathRecipient(), 0);
            }
            return this.mBluetoothService;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setRemoteObject(IRemoteObject iRemoteObject) {
        synchronized (this.mRemoteLock) {
            this.mBluetoothService = iRemoteObject;
        }
    }

    /* access modifiers changed from: private */
    public class A2dpDeathRecipient implements IRemoteObject.DeathRecipient {
        private A2dpDeathRecipient() {
        }

        @Override // ohos.rpc.IRemoteObject.DeathRecipient
        public void onRemoteDied() {
            HiLog.warn(A2dpProxy.TAG, "A2dpDeathRecipient::onRemoteDied.", new Object[0]);
            A2dpProxy.this.setRemoteObject(null);
        }
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x006b, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.A2dpProxy.TAG, "getDevicesByStatesForSource : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0076, code lost:
        r2.reclaim();
        r3.reclaim();
        r5 = new java.util.ArrayList();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0082, code lost:
        return r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0083, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0089, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x006d */
    @Override // ohos.bluetooth.IA2dp
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<ohos.bluetooth.BluetoothRemoteDevice> getDevicesByStatesForSource(int[] r6) {
        /*
        // Method dump skipped, instructions count: 141
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.A2dpProxy.getDevicesByStatesForSource(int[]):java.util.List");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0060, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.A2dpProxy.TAG, "getDeviceStateForSource : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0072, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0073, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0079, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0062 */
    @Override // ohos.bluetooth.IA2dp
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getDeviceStateForSource(ohos.bluetooth.BluetoothRemoteDevice r6) {
        /*
        // Method dump skipped, instructions count: 125
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.A2dpProxy.getDeviceStateForSource(ohos.bluetooth.BluetoothRemoteDevice):int");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0060, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.A2dpProxy.TAG, "getPlayingStateForSource : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0072, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0073, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0079, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0062 */
    @Override // ohos.bluetooth.IA2dp
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getPlayingStateForSource(ohos.bluetooth.BluetoothRemoteDevice r6) {
        /*
        // Method dump skipped, instructions count: 125
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.A2dpProxy.getPlayingStateForSource(ohos.bluetooth.BluetoothRemoteDevice):int");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x006b, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.A2dpProxy.TAG, "getDevicesByStatesForSink : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0076, code lost:
        r2.reclaim();
        r3.reclaim();
        r5 = new java.util.ArrayList();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0082, code lost:
        return r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0083, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0089, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x006d */
    @Override // ohos.bluetooth.IA2dp
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<ohos.bluetooth.BluetoothRemoteDevice> getDevicesByStatesForSink(int[] r6) {
        /*
        // Method dump skipped, instructions count: 141
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.A2dpProxy.getDevicesByStatesForSink(int[]):java.util.List");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0060, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.A2dpProxy.TAG, "getDeviceStateForSink : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0072, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0073, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0079, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0062 */
    @Override // ohos.bluetooth.IA2dp
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getDeviceStateForSink(ohos.bluetooth.BluetoothRemoteDevice r6) {
        /*
        // Method dump skipped, instructions count: 125
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.A2dpProxy.getDeviceStateForSink(ohos.bluetooth.BluetoothRemoteDevice):int");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0060, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.A2dpProxy.TAG, "getPlayingStateForSink : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0072, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0073, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0079, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0062 */
    @Override // ohos.bluetooth.IA2dp
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getPlayingStateForSink(ohos.bluetooth.BluetoothRemoteDevice r6) {
        /*
        // Method dump skipped, instructions count: 125
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.A2dpProxy.getPlayingStateForSink(ohos.bluetooth.BluetoothRemoteDevice):int");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0064, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.A2dpProxy.TAG, "connectSourceDevice : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0076, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0077, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x007d, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0066 */
    @Override // ohos.bluetooth.IA2dp
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean connectSourceDevice(ohos.bluetooth.BluetoothRemoteDevice r6) {
        /*
        // Method dump skipped, instructions count: 129
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.A2dpProxy.connectSourceDevice(ohos.bluetooth.BluetoothRemoteDevice):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0064, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.A2dpProxy.TAG, "disconnectSourceDevice : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0076, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0077, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x007d, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0066 */
    @Override // ohos.bluetooth.IA2dp
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean disconnectSourceDevice(ohos.bluetooth.BluetoothRemoteDevice r6) {
        /*
        // Method dump skipped, instructions count: 129
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.A2dpProxy.disconnectSourceDevice(ohos.bluetooth.BluetoothRemoteDevice):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:27|28|29|30|31) */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0065, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.A2dpProxy.TAG, "connectSinkDevice : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0077, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0078, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x007e, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0067 */
    @Override // ohos.bluetooth.IA2dp
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean connectSinkDevice(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 130
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.A2dpProxy.connectSinkDevice(ohos.bluetooth.BluetoothRemoteDevice):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:27|28|29|30|31) */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0065, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.A2dpProxy.TAG, "disconnectSinkDevice : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0077, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0078, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x007e, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0067 */
    @Override // ohos.bluetooth.IA2dp
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean disconnectSinkDevice(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 130
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.A2dpProxy.disconnectSinkDevice(ohos.bluetooth.BluetoothRemoteDevice):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0067, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.A2dpProxy.TAG, "setActiveDeviceForSource : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x007a, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x007b, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0081, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0069 */
    @Override // ohos.bluetooth.IA2dp
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setActiveDeviceForSource(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 133
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.A2dpProxy.setActiveDeviceForSource(ohos.bluetooth.BluetoothRemoteDevice):boolean");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x007c, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.A2dpProxy.TAG, "getActiveDeviceForSource : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0087, code lost:
        r2.reclaim();
        r3.reclaim();
        r6 = java.util.Optional.empty();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0092, code lost:
        return r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0093, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0099, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:30:0x007e */
    @Override // ohos.bluetooth.IA2dp
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Optional<ohos.bluetooth.BluetoothRemoteDevice> getActiveDeviceForSource() {
        /*
        // Method dump skipped, instructions count: 157
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.A2dpProxy.getActiveDeviceForSource():java.util.Optional");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:31|32|33|34|35) */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0071, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.A2dpProxy.TAG, "setConnectStrategyForSource : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0084, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0085, code lost:
        r3.reclaim();
        r4.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x008b, code lost:
        throw r7;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x0073 */
    @Override // ohos.bluetooth.IA2dp
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setConnectStrategyForSource(ohos.bluetooth.BluetoothRemoteDevice r8, int r9) {
        /*
        // Method dump skipped, instructions count: 155
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.A2dpProxy.setConnectStrategyForSource(ohos.bluetooth.BluetoothRemoteDevice, int):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0065, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.A2dpProxy.TAG, "getConnectStrategyForSource : call error", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0077, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0078, code lost:
        r4.reclaim();
        r5.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x007e, code lost:
        throw r8;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0067 */
    @Override // ohos.bluetooth.IA2dp
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getConnectStrategyForSource(ohos.bluetooth.BluetoothRemoteDevice r9) {
        /*
        // Method dump skipped, instructions count: 130
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.A2dpProxy.getConnectStrategyForSource(ohos.bluetooth.BluetoothRemoteDevice):int");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:29|30|31|32|33) */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0083, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.A2dpProxy.TAG, "getCodecStatusForSource : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x008e, code lost:
        r2.reclaim();
        r3.reclaim();
        r6 = java.util.Optional.empty();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0099, code lost:
        return r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x009a, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00a0, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x0085 */
    @Override // ohos.bluetooth.IA2dp
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Optional<ohos.bluetooth.A2dpCodecStatus> getCodecStatusForSource(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 164
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.A2dpProxy.getCodecStatusForSource(ohos.bluetooth.BluetoothRemoteDevice):java.util.Optional");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x005c, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.A2dpProxy.TAG, "setCodecPreferenceForSource : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0068, code lost:
        r2.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x006e, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0074, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x005e */
    @Override // ohos.bluetooth.IA2dp
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setCodecPreferenceForSource(ohos.bluetooth.BluetoothRemoteDevice r7, ohos.bluetooth.A2dpCodecInfo r8) {
        /*
        // Method dump skipped, instructions count: 120
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.A2dpProxy.setCodecPreferenceForSource(ohos.bluetooth.BluetoothRemoteDevice, ohos.bluetooth.A2dpCodecInfo):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x005c, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.A2dpProxy.TAG, "switchOptionalCodecsForSource : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0068, code lost:
        r2.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x006e, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0074, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x005e */
    @Override // ohos.bluetooth.IA2dp
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void switchOptionalCodecsForSource(ohos.bluetooth.BluetoothRemoteDevice r7, boolean r8) {
        /*
        // Method dump skipped, instructions count: 120
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.A2dpProxy.switchOptionalCodecsForSource(ohos.bluetooth.BluetoothRemoteDevice, boolean):void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0061, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.A2dpProxy.TAG, "getOptionalCodecsSupportStateForSource : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0073, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0074, code lost:
        r3.reclaim();
        r4.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x007a, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0063 */
    @Override // ohos.bluetooth.IA2dp
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getOptionalCodecsSupportStateForSource(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 126
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.A2dpProxy.getOptionalCodecsSupportStateForSource(ohos.bluetooth.BluetoothRemoteDevice):int");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0061, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.A2dpProxy.TAG, "getOptionalCodecsOptionForSource : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0073, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0074, code lost:
        r3.reclaim();
        r4.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x007a, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0063 */
    @Override // ohos.bluetooth.IA2dp
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getOptionalCodecsOptionForSource(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 126
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.A2dpProxy.getOptionalCodecsOptionForSource(ohos.bluetooth.BluetoothRemoteDevice):int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x005c, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.A2dpProxy.TAG, "setOptionalCodecsOptionForSource : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0068, code lost:
        r2.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x006e, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0074, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x005e */
    @Override // ohos.bluetooth.IA2dp
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setOptionalCodecsOptionForSource(ohos.bluetooth.BluetoothRemoteDevice r7, int r8) {
        /*
        // Method dump skipped, instructions count: 120
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.A2dpProxy.setOptionalCodecsOptionForSource(ohos.bluetooth.BluetoothRemoteDevice, int):void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:31|32|33|34|35) */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0071, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.A2dpProxy.TAG, "setConnectStrategyForSink : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0084, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0085, code lost:
        r3.reclaim();
        r4.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x008b, code lost:
        throw r7;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x0073 */
    @Override // ohos.bluetooth.IA2dp
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setConnectStrategyForSink(ohos.bluetooth.BluetoothRemoteDevice r8, int r9) {
        /*
        // Method dump skipped, instructions count: 155
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.A2dpProxy.setConnectStrategyForSink(ohos.bluetooth.BluetoothRemoteDevice, int):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0065, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.A2dpProxy.TAG, "getConnectStrategyForSink : call error", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0077, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0078, code lost:
        r4.reclaim();
        r5.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x007e, code lost:
        throw r8;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0067 */
    @Override // ohos.bluetooth.IA2dp
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getConnectStrategyForSink(ohos.bluetooth.BluetoothRemoteDevice r9) {
        /*
        // Method dump skipped, instructions count: 130
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.A2dpProxy.getConnectStrategyForSink(ohos.bluetooth.BluetoothRemoteDevice):int");
    }
}
