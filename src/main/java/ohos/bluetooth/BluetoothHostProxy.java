package ohos.bluetooth;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageParcel;
import ohos.sysability.samgr.SysAbilityManager;
import ohos.utils.SequenceUuid;
import ohos.utils.system.safwk.java.SystemAbilityDefinition;

public class BluetoothHostProxy implements IBluetoothHost {
    private static final int DEFAULT_REMOTE_NUM = 5;
    private static final int MAX_NUM_OF_UUID = 50;
    private static final int REMOTE_OPERATION_FAILED = 0;
    private static final HiLogLabel TAG = new HiLogLabel(3, LogHelper.BT_DOMAIN_ID, "BluetoothHostProxy");
    private static final int TRANSACT_VALUE_BLUETOOTH_REMOVE_PAIR = 108;
    private static final int TRANSACT_VALUE_CANCEL_DISCOVERY = 6;
    private static final int TRANSACT_VALUE_DISABLE = 1;
    private static final int TRANSACT_VALUE_ENABLE = 0;
    private static final int TRANSACT_VALUE_GET_BLE_CAPABILITIES = 11;
    private static final int TRANSACT_VALUE_GET_BLE_MAX_ADV_DATA_LEN = 12;
    private static final int TRANSACT_VALUE_GET_BT_CONNECTION_STATE = 20;
    private static final int TRANSACT_VALUE_GET_DISCOVERY_END_MILLIS = 16;
    private static final int TRANSACT_VALUE_GET_FACTORY_RESET = 14;
    private static final int TRANSACT_VALUE_GET_LOCAL_ADDRESS = 13;
    private static final int TRANSACT_VALUE_GET_LOCAL_UUIDS = 15;
    private static final int TRANSACT_VALUE_GET_MAX_CONN_AUDIO_DEVICES = 17;
    private static final int TRANSACT_VALUE_GET_NAME = 3;
    private static final int TRANSACT_VALUE_GET_PAIRED_DEVICES = 10;
    private static final int TRANSACT_VALUE_GET_PROFILE_CONN_STATE = 9;
    private static final int TRANSACT_VALUE_GET_SA_PROFILE = 1000;
    private static final int TRANSACT_VALUE_GET_SCAN_MODE = 8;
    private static final int TRANSACT_VALUE_GET_STATE = 2;
    private static final int TRANSACT_VALUE_GET_SUPPORTED_PROFILES = 18;
    private static final int TRANSACT_VALUE_IS_DISCOVERING = 7;
    private static final int TRANSACT_VALUE_IS_HEARING_AID_SUPPORTED = 19;
    private static final int TRANSACT_VALUE_REMOTE_BONDED_FROM_LOCAL = 112;
    private static final int TRANSACT_VALUE_REMOTE_CANCEL_PAIRING = 113;
    private static final int TRANSACT_VALUE_REMOTE_GET_ALIAS = 109;
    private static final int TRANSACT_VALUE_REMOTE_GET_BATTERY = 111;
    private static final int TRANSACT_VALUE_REMOTE_GET_CLASS = 102;
    private static final int TRANSACT_VALUE_REMOTE_GET_CONNECTION_STATE = 114;
    private static final int TRANSACT_VALUE_REMOTE_GET_MESSAGE_PERMISSION = 118;
    private static final int TRANSACT_VALUE_REMOTE_GET_NAME = 100;
    private static final int TRANSACT_VALUE_REMOTE_GET_PAIR_STATE = 103;
    private static final int TRANSACT_VALUE_REMOTE_GET_PHONEBOOK_PERMISSION = 116;
    private static final int TRANSACT_VALUE_REMOTE_GET_TYPE = 101;
    private static final int TRANSACT_VALUE_REMOTE_GET_UUIDS = 115;
    private static final int TRANSACT_VALUE_REMOTE_PAIR_CONFIRM = 106;
    private static final int TRANSACT_VALUE_REMOTE_SET_ALIAS = 110;
    private static final int TRANSACT_VALUE_REMOTE_SET_MESSAGE_PERMISSION = 119;
    private static final int TRANSACT_VALUE_REMOTE_SET_PHONEBOOK_PERMISSION = 117;
    private static final int TRANSACT_VALUE_REMOTE_SET_PIN = 105;
    private static final int TRANSACT_VALUE_REMOTE_START_PAIR = 104;
    private static final int TRANSACT_VALUE_SET_NAME = 4;
    private static final int TRANSACT_VALUE_SET_SCAN_MODE = 107;
    private static final int TRANSACT_VALUE_START_DISCOVERY = 5;
    private static BluetoothHostProxy sInstance = null;
    private IRemoteObject mBluetoothService = null;
    private final Object mRemoteLock = new Object();

    private int convertProfile(int i) {
        switch (i) {
            case 1:
                return 1;
            case 2:
                return 3;
            case 3:
                return 13;
            case 4:
                return 14;
            case 5:
                return 15;
            case 6:
                return 7;
            case 7:
                return 11;
            case 8:
                return 12;
            case 9:
                return 9;
            case 10:
                return 16;
            case 11:
                return 4;
            case 12:
                return 5;
            case 13:
                return 6;
            case 14:
            case 15:
            default:
                return 0;
            case 16:
                return 2;
            case 17:
                return 8;
            case 18:
                return 10;
            case 19:
                return 17;
            case 20:
                return 18;
            case 21:
                return 19;
        }
    }

    public static synchronized BluetoothHostProxy getInstace() {
        BluetoothHostProxy bluetoothHostProxy;
        synchronized (BluetoothHostProxy.class) {
            if (sInstance == null) {
                sInstance = new BluetoothHostProxy();
            }
            bluetoothHostProxy = sInstance;
        }
        return bluetoothHostProxy;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        synchronized (this.mRemoteLock) {
            if (this.mBluetoothService != null) {
                return this.mBluetoothService;
            }
            this.mBluetoothService = SysAbilityManager.getSysAbility(SystemAbilityDefinition.BLUETOOTH_HOST_SYS_ABILITY_ID);
            if (this.mBluetoothService == null) {
                HiLog.error(TAG, "getSysAbility for Bluetooth host failed.", new Object[0]);
            } else {
                this.mBluetoothService.addDeathRecipient(new BluetoothHostDeathRecipient(), 0);
            }
            return this.mBluetoothService;
        }
    }

    /* access modifiers changed from: private */
    public class BluetoothHostDeathRecipient implements IRemoteObject.DeathRecipient {
        private BluetoothHostDeathRecipient() {
        }

        @Override // ohos.rpc.IRemoteObject.DeathRecipient
        public void onRemoteDied() {
            HiLog.warn(BluetoothHostProxy.TAG, "BluetoothHostDeathRecipient::onRemoteDied.", new Object[0]);
            BluetoothHostProxy.this.setRemoteObject(null);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setRemoteObject(IRemoteObject iRemoteObject) {
        synchronized (this.mRemoteLock) {
            this.mBluetoothService = iRemoteObject;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x005f, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "enable : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0071, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0072, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0078, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0061 */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean enableBt(java.lang.String r6) {
        /*
        // Method dump skipped, instructions count: 124
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.enableBt(java.lang.String):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:26|27|28|29|30) */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x005e, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "disable : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0070, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0071, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0077, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:26:0x0060 */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean disableBt(java.lang.String r6) {
        /*
        // Method dump skipped, instructions count: 123
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.disableBt(java.lang.String):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0058, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "getState : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x006a, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x006b, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0071, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x005a */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getBtState() {
        /*
        // Method dump skipped, instructions count: 117
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.getBtState():int");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x007f, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "getName : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x008a, code lost:
        r2.reclaim();
        r3.reclaim();
        r6 = java.util.Optional.empty();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0095, code lost:
        return r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0096, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x009c, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:28:0x0081 */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Optional<java.lang.String> getLocalName() {
        /*
        // Method dump skipped, instructions count: 160
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.getLocalName():java.util.Optional");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:27|28|29|30|31) */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0062, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "setName : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0075, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0076, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x007c, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0064 */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setLocalName(java.lang.String r6) {
        /*
        // Method dump skipped, instructions count: 128
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.setLocalName(java.lang.String):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:27|28|29|30|31) */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0062, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "startDiscovery : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0075, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0076, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x007c, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0064 */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean startBtDiscovery(java.lang.String r6) {
        /*
        // Method dump skipped, instructions count: 128
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.startBtDiscovery(java.lang.String):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x005c, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "cancelDiscovery : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x006e, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x006f, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0075, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x005e */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean cancelBtDiscovery() {
        /*
        // Method dump skipped, instructions count: 121
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.cancelBtDiscovery():boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x005c, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "isDiscovering : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x006e, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x006f, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0075, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x005e */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isBtDiscovering() {
        /*
        // Method dump skipped, instructions count: 121
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.isBtDiscovering():boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0059, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "getScanMode : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x006b, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x006c, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0072, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x005b */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getBtScanMode() {
        /*
        // Method dump skipped, instructions count: 118
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.getBtScanMode():int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x005c, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "getProfileConnState : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x006e, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x006f, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0075, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x005e */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getProfileConnState(int r6) {
        /*
        // Method dump skipped, instructions count: 121
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.getProfileConnState(int):int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:34:0x009a, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "getPairedDevices : call fail", new java.lang.Object[0]);
        r8 = new java.util.ArrayList();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00b1, code lost:
        return r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00b2, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x00b8, code lost:
        throw r8;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:35:0x009c */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<ohos.bluetooth.BluetoothRemoteDevice> getPairedDevices() {
        /*
        // Method dump skipped, instructions count: 188
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.getPairedDevices():java.util.List");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0059, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "getBleCapabilities : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x006b, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x006c, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0072, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x005b */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getBleCapabilities() {
        /*
        // Method dump skipped, instructions count: 118
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.getBleCapabilities():int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0059, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "getBleMaxAdvertisingDataLength : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x006b, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x006c, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0072, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x005b */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getBleMaxAdvertisingDataLength() {
        /*
        // Method dump skipped, instructions count: 118
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.getBleMaxAdvertisingDataLength():int");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0087, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "getRemoteName : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0092, code lost:
        r2.reclaim();
        r3.reclaim();
        r5 = java.util.Optional.empty();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x009d, code lost:
        return r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x009e, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00a4, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:28:0x0089 */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Optional<java.lang.String> getRemoteName(ohos.bluetooth.BluetoothRemoteDevice r6) {
        /*
        // Method dump skipped, instructions count: 168
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.getRemoteName(ohos.bluetooth.BluetoothRemoteDevice):java.util.Optional");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0060, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "getType : call fail", new java.lang.Object[0]);
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
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getDeviceType(ohos.bluetooth.BluetoothRemoteDevice r6) {
        /*
        // Method dump skipped, instructions count: 125
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.getDeviceType(ohos.bluetooth.BluetoothRemoteDevice):int");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0060, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "getRemoteDeviceClass : call fail", new java.lang.Object[0]);
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
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getRemoteDeviceClass(ohos.bluetooth.BluetoothRemoteDevice r6) {
        /*
        // Method dump skipped, instructions count: 125
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.getRemoteDeviceClass(ohos.bluetooth.BluetoothRemoteDevice):int");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0060, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "getPairState : call fail", new java.lang.Object[0]);
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
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getPairState(ohos.bluetooth.BluetoothRemoteDevice r6) {
        /*
        // Method dump skipped, instructions count: 125
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.getPairState(ohos.bluetooth.BluetoothRemoteDevice):int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0067, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "startPair : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x007a, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x007b, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0081, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0069 */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean startPair(ohos.bluetooth.BluetoothRemoteDevice r6) {
        /*
        // Method dump skipped, instructions count: 133
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.startPair(ohos.bluetooth.BluetoothRemoteDevice):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:22|23|24|25|26) */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0055, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "setPin : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0068, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0069, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x006f, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:22:0x0057 */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setDevicePin(ohos.bluetooth.BluetoothRemoteDevice r6, byte[] r7) {
        /*
        // Method dump skipped, instructions count: 115
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.setDevicePin(ohos.bluetooth.BluetoothRemoteDevice, byte[]):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x005a, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "setPairingConfirmation : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x006d, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x006e, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0074, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x005c */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setDevicePairingConfirmation(ohos.bluetooth.BluetoothRemoteDevice r6, boolean r7) {
        /*
        // Method dump skipped, instructions count: 120
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.setDevicePairingConfirmation(ohos.bluetooth.BluetoothRemoteDevice, boolean):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0067, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "removePair : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x007a, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x007b, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0081, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0069 */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean removePair(ohos.bluetooth.BluetoothRemoteDevice r6) {
        /*
        // Method dump skipped, instructions count: 133
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.removePair(ohos.bluetooth.BluetoothRemoteDevice):boolean");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x005c, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "getSaProfileProxy : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0067, code lost:
        r2.reclaim();
        r3.reclaim();
        r5 = java.util.Optional.empty();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0072, code lost:
        return r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0073, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0079, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x005e */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Optional<ohos.rpc.IRemoteObject> getSaProfileProxy(int r6) {
        /*
        // Method dump skipped, instructions count: 125
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.getSaProfileProxy(int):java.util.Optional");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:27|28|29|30|31) */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0066, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "setScanMode : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0079, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x007a, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0080, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x0068 */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setBtScanMode(int r6, int r7) {
        /*
        // Method dump skipped, instructions count: 132
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.setBtScanMode(int, int):boolean");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:26|27|28|29|30) */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0076, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "getLocalAddress : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0081, code lost:
        r2.reclaim();
        r3.reclaim();
        r6 = java.util.Optional.empty();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x008c, code lost:
        return r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x008d, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0093, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:26:0x0078 */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Optional<java.lang.String> getLocalAddress() {
        /*
        // Method dump skipped, instructions count: 151
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.getLocalAddress():java.util.Optional");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:31|32|33|34|35) */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0065, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "bluetoothFactoryReset : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0077, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0078, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x007e, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x0067 */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean bluetoothFactoryReset() {
        /*
        // Method dump skipped, instructions count: 130
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.bluetoothFactoryReset():boolean");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:25|26|27|28|29) */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0061, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "getLocalSupportedUuids : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x006c, code lost:
        r3.reclaim();
        r4.reclaim();
        r7 = new ohos.utils.SequenceUuid[0];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0075, code lost:
        return r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0076, code lost:
        r3.reclaim();
        r4.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x007c, code lost:
        throw r7;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0063 */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.utils.SequenceUuid[] getLocalSupportedUuids() {
        /*
        // Method dump skipped, instructions count: 128
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.getLocalSupportedUuids():ohos.utils.SequenceUuid[]");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x005f, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "getBtDiscoveryEndMillis : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0071, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0072, code lost:
        r4.reclaim();
        r5.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0078, code lost:
        throw r8;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:25:0x0061 */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long getBtDiscoveryEndMillis() {
        /*
        // Method dump skipped, instructions count: 124
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.getBtDiscoveryEndMillis():long");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:21|22|23|24|25) */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x004e, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "getMaxNumConnectedAudioDevices : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0060, code lost:
        return 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0061, code lost:
        r3.reclaim();
        r4.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0067, code lost:
        throw r7;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0050 */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getMaxNumConnectedAudioDevices() {
        /*
        // Method dump skipped, instructions count: 107
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.getMaxNumConnectedAudioDevices():int");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:38|39|40|41|42) */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00b2, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "getSupportedProfilesList : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00bd, code lost:
        r5.reclaim();
        r6.reclaim();
        r0 = new java.util.ArrayList();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00c9, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00ca, code lost:
        r5.reclaim();
        r6.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00d0, code lost:
        throw r0;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:38:0x00b4 */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<java.lang.Integer> getProfileList() {
        /*
        // Method dump skipped, instructions count: 212
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.getProfileList():java.util.List");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:21|22|23|24|25) */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x004d, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "getBtConnectionState : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x005f, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0060, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0066, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x004f */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getBtConnectionState() {
        /*
        // Method dump skipped, instructions count: 106
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.getBtConnectionState():int");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:20|21|22|23|24) */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0060, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "getRemoteAlias : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x006b, code lost:
        r2.reclaim();
        r3.reclaim();
        r6 = java.util.Optional.empty();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0076, code lost:
        return r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0077, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x007d, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x0062 */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Optional<java.lang.String> getRemoteAlias(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 129
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.getRemoteAlias(ohos.bluetooth.BluetoothRemoteDevice):java.util.Optional");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:23|24|25|26|27) */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x005e, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "setRemoteAlias : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0071, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0072, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0078, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x0060 */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setRemoteAlias(ohos.bluetooth.BluetoothRemoteDevice r7, java.lang.String r8) {
        /*
        // Method dump skipped, instructions count: 124
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.setRemoteAlias(ohos.bluetooth.BluetoothRemoteDevice, java.lang.String):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:20|21|22|23|24) */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0055, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "getDeviceBatteryLevel : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0067, code lost:
        return -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0068, code lost:
        r3.reclaim();
        r4.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x006e, code lost:
        throw r7;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x0057 */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getDeviceBatteryLevel(ohos.bluetooth.BluetoothRemoteDevice r8) {
        /*
        // Method dump skipped, instructions count: 114
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.getDeviceBatteryLevel(ohos.bluetooth.BluetoothRemoteDevice):int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0059, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "isBondedFromLocal : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x006b, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x006c, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0072, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x005b */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isBondedFromLocal(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 118
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.isBondedFromLocal(ohos.bluetooth.BluetoothRemoteDevice):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:27|28|29|30|31) */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0065, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "cancelPairing : call fail", new java.lang.Object[0]);
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
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean cancelPairing(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 130
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.cancelPairing(ohos.bluetooth.BluetoothRemoteDevice):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:26|27|28|29|30) */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0063, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "isAclConnected : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0075, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0076, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x007c, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:26:0x0065 */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isAclConnected(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 128
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.isAclConnected(ohos.bluetooth.BluetoothRemoteDevice):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:27|28|29|30|31) */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0065, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "isAclEncrypted : call fail", new java.lang.Object[0]);
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
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isAclEncrypted(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 130
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.isAclEncrypted(ohos.bluetooth.BluetoothRemoteDevice):boolean");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0068, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "getDeviceUuids : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0073, code lost:
        r3.reclaim();
        r4.reclaim();
        r7 = new ohos.utils.SequenceUuid[0];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x007c, code lost:
        return r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x007d, code lost:
        r3.reclaim();
        r4.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0083, code lost:
        throw r7;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x006a */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.utils.SequenceUuid[] getDeviceUuids(ohos.bluetooth.BluetoothRemoteDevice r8) {
        /*
        // Method dump skipped, instructions count: 135
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.getDeviceUuids(ohos.bluetooth.BluetoothRemoteDevice):ohos.utils.SequenceUuid[]");
    }

    private SequenceUuid[] createUuidArray(MessageParcel messageParcel, int i) {
        if (i < 0 || i > 50) {
            HiLog.error(TAG, "createUuidArray : wrong uuid result, a empty list will be returned", new Object[0]);
            return new SequenceUuid[0];
        }
        SequenceUuid[] sequenceUuidArr = new SequenceUuid[i];
        for (int i2 = 0; i2 < i; i2++) {
            if (messageParcel.getReadableBytes() <= 0) {
                HiLog.error(TAG, "createUuidArray : wrong uuid result, a empty list will be returned", new Object[0]);
            }
            SequenceUuid sequenceUuid = new SequenceUuid();
            messageParcel.readSequenceable(sequenceUuid);
            sequenceUuidArr[i2] = sequenceUuid;
        }
        return sequenceUuidArr;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0060, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "getPhonebookPermission : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0072, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0073, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0079, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0062 */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getPhonebookPermission(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 125
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.getPhonebookPermission(ohos.bluetooth.BluetoothRemoteDevice):int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x006a, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "setPhonebookPermission : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x007d, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x007e, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0084, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x006c */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setPhonebookPermission(ohos.bluetooth.BluetoothRemoteDevice r7, int r8) {
        /*
        // Method dump skipped, instructions count: 136
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.setPhonebookPermission(ohos.bluetooth.BluetoothRemoteDevice, int):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(5:24|25|26|27|28) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0060, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "getMessagePermission : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0072, code lost:
        return 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0073, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0079, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:24:0x0062 */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getMessagePermission(ohos.bluetooth.BluetoothRemoteDevice r7) {
        /*
        // Method dump skipped, instructions count: 125
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.getMessagePermission(ohos.bluetooth.BluetoothRemoteDevice):int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x006a, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "setMessagePermission : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x007d, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x007e, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0084, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:27:0x006c */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setMessagePermission(ohos.bluetooth.BluetoothRemoteDevice r7, int r8) {
        /*
        // Method dump skipped, instructions count: 136
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.setMessagePermission(ohos.bluetooth.BluetoothRemoteDevice, int):boolean");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0087, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.bluetooth.BluetoothHostProxy.TAG, "getRemoteName : call fail", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0092, code lost:
        r2.reclaim();
        r3.reclaim();
        r5 = java.util.Optional.empty();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x009d, code lost:
        return r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x009e, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00a4, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:28:0x0089 */
    @Override // ohos.bluetooth.IBluetoothHost
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Optional<java.lang.String> getRemoteName(ohos.bluetooth.ble.BlePeripheralDevice r6) {
        /*
        // Method dump skipped, instructions count: 168
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.bluetooth.BluetoothHostProxy.getRemoteName(ohos.bluetooth.ble.BlePeripheralDevice):java.util.Optional");
    }
}
