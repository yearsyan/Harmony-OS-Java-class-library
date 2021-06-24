package com.huawei.ohos.foundation;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.AndroidRuntimeException;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.huawei.android.os.BuildEx;
import com.huawei.pgmng.log.LogPower;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import ohos.aafwk.content.Intent;
import ohos.abilityshell.utils.IntentConverter;
import ohos.bundle.AbilityInfo;
import ohos.bundle.BundleManager;
import ohos.bundle.ElementName;
import ohos.bundle.ShellInfo;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.net.UriConverter;
import ohos.rpc.IPCAdapter;
import ohos.rpc.IPCSkeleton;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;
import ohos.sysability.samgr.SysAbilityManager;

public class DmsProxyManagerWrap extends RemoteObject implements IRemoteBroker {
    private static final String BACKGROUND_SERVICE = "backgroundService";
    private static final int CALLER_TYPE_EMUI = 2;
    private static final int CALLER_TYPE_HARMONY = 1;
    private static final int CALLER_TYPE_NONE = 0;
    private static final int CODE_REPORT_DATA_RES_SCHED_SERVICE = 1001;
    private static final int CONNECT_ABILITY_EVENT = 504;
    private static final String DATA_ABILITY_FLAG = "RemoteDataAbility";
    private static final String DATA_ABILITY_INTERFACE_TOKEN = "ohos.abilityshell.DataAbilityCallback";
    private static final String DELIMITER = "|";
    private static final int DISCONNECT_ABILITY_EVENT = 505;
    private static final int DMSPROXY_SERVICE_ERR_OFFSET = 29425664;
    private static final String DMS_CALL_METHOD = "DMS_GetRemoteDataAbility";
    private static final HiLogLabel DMS_LABEL = new HiLogLabel(3, 218109952, "DmsProxy_Service");
    private static final int DMS_MODULE_TYPE_PROXY_SERVICE = 1;
    private static final String DMS_PROXY_INTERFACE_TOKEN = "com.huawei.ohos.foundation.DmsProxyManagerWrap";
    private static final int ERR_BINDER_CONVERTER = 29425668;
    private static final int ERR_BIND_ABILITY_CRASH = 29425665;
    private static final int ERR_BIND_ABILITY_DIED = 29425666;
    private static final int ERR_BIND_ABILITY_NULL = 29425667;
    private static final int ERR_OK = 0;
    private static final String FASTAPP_URI_PREFIX = "hwfastapp://";
    private static final int HID_HAP = 10000;
    private static final int INVALID_CALLER_PID = -1;
    private static final int INVALID_CALLER_UID = -1;
    private static final int LOG_EVENT_CONNECT_ABILITY = 0;
    private static final int LOG_EVENT_DISCONNECT_ABILITY = 1;
    private static final int LOG_EVENT_OFFLINE = 2;
    private static final int LOG_EVENT_PROCESS_DIED = 3;
    private static final int META_TYPE_DTB_BIND = 7;
    private static final int MULTIUSER_HAP_PER_USER_RANGE = 100000;
    private static final String NAME_FASTAPP_CLASS = "com.huawei.fastapp.app.processManager.DeepLinkActivity";
    private static final String NAME_FASTAPP_PKG = "com.huawei.fastapp";
    private static final String NONE_PACKAGE_NAME = "NA\n";
    private static final int ON_ABILITY_CONNECT_DONE = 1;
    private static final int ON_ABILITY_DISCONNECT_DONE = 2;
    private static final int ON_DATA_ABILITY_DONE = 0;
    private static final String PACKAGE_NAME_INTERVAL = "\n";
    private static final String REPORT_TYPE = "2";
    private static final long RES_DATA_DEFAULE_VALUE = 0;
    private static final String RES_SCHED_INTERFACE_TOKEN = "OHOS.ResourceSchedule.ResSchedService";
    private static final String SERVICE_TYPE = "serviceType";
    private static final String START_ABILITY_SCREEN_ON = "startAbility_ScreenOn";
    private static final int SUBSYS_DISTRIBUTEDSCHEDULE = 14;
    private static final int TRANSACTION_CONNECTABILITY = 1;
    private static final int TRANSACTION_DISCONNECTABILITY = 2;
    private static final int TRANSACTION_DUMP_INFO = 7;
    private static final int TRANSACTION_GET_DATAABILITY = 8;
    private static final int TRANSACTION_GET_UDID = 9;
    private static final int TRANSACTION_NOTIFY_DEVICE_OFFLINE = 5;
    private static final int TRANSACTION_NOTIFY_PROCESS_DIED = 6;
    private static final int TRANSACTION_START_ABILITY = 3;
    private static final int TRANSACTION_STOP_ABILITY = 4;
    private Context mContext;
    private volatile IRemoteObject mResSchedProxy;
    private ScreenStateHelper mScreenStateHelper;
    private final HashMap<IRemoteObject, ProxyServiceConnection> mServiceConnections = new HashMap<>();

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this;
    }

    DmsProxyManagerWrap(Context context) {
        super(DMS_PROXY_INTERFACE_TOKEN);
        this.mContext = context;
        this.mScreenStateHelper = new ScreenStateHelper(context);
    }

    private boolean connectAbility(Intent intent, ShellInfo shellInfo, IRemoteObject iRemoteObject, CallerInfo callerInfo) {
        boolean z;
        boolean bindServiceAsUser;
        if (isAbnormalParameters(intent, shellInfo, iRemoteObject, callerInfo)) {
            HiLog.error(DMS_LABEL, "connectAbility parameter is null", new Object[0]);
            return false;
        }
        HiLog.debug(DMS_LABEL, "connectAbility callerType is %{private}s, sourceDeviceId is %{private}s", Integer.valueOf(callerInfo.getCallerType()), callerInfo.getSourceDeviceId());
        ElementName element = intent.getElement();
        if (element != null) {
            HiLog.debug(DMS_LABEL, "connectAbility BundleName %{public}s, AbilityName %{public}s", element.getBundleName(), element.getAbilityName());
        }
        Optional<android.content.Intent> checkIntentIsValid = checkIntentIsValid(intent, shellInfo);
        if (!checkIntentIsValid.isPresent()) {
            HiLog.error(DMS_LABEL, "convert android intent fail", new Object[0]);
            return false;
        } else if (this.mContext == null) {
            return false;
        } else {
            synchronized (this.mServiceConnections) {
                ProxyServiceConnection proxyServiceConnection = this.mServiceConnections.get(iRemoteObject);
                if (proxyServiceConnection == null) {
                    proxyServiceConnection = new ProxyServiceConnection(iRemoteObject, callerInfo);
                    if (element != null) {
                        proxyServiceConnection.setTargetBundleName(element.getBundleName());
                    }
                    z = false;
                } else {
                    z = true;
                }
                try {
                    bindServiceAsUser = this.mContext.bindServiceAsUser(checkIntentIsValid.get(), proxyServiceConnection, 1, UserHandle.CURRENT);
                    HiLog.debug(DMS_LABEL, "bind Service by android result is %{public}b", Boolean.valueOf(bindServiceAsUser));
                    if (bindServiceAsUser && !z) {
                        this.mServiceConnections.put(iRemoteObject, proxyServiceConnection);
                        proxyServiceConnection.reportConnectAbilityInfoToPg(504, 0);
                    }
                } catch (SecurityException unused) {
                    HiLog.error(DMS_LABEL, "bind Service SecurityException happens!", new Object[0]);
                    return false;
                }
            }
            return bindServiceAsUser;
        }
    }

    private Optional<android.content.Intent> checkIntentIsValid(Intent intent, ShellInfo shellInfo) {
        Optional<android.content.Intent> createAndroidIntent = IntentConverter.createAndroidIntent(intent, shellInfo);
        if (!createAndroidIntent.isPresent() || createAndroidIntent.get().getComponent() == null) {
            return Optional.empty();
        }
        HiLog.info(DMS_LABEL, "intent convert ok", new Object[0]);
        return createAndroidIntent;
    }

    private boolean connectAbilityInner(MessageParcel messageParcel, MessageParcel messageParcel2) {
        Intent intent = new Intent();
        if (!messageParcel.readSequenceable(intent)) {
            HiLog.error(DMS_LABEL, "on connectAbility done. Intent is null", new Object[0]);
            return false;
        }
        ShellInfo shellInfo = new ShellInfo();
        if (!messageParcel.readSequenceable(shellInfo)) {
            HiLog.error(DMS_LABEL, "on connectAbility done. ShellInfo is null", new Object[0]);
            return false;
        } else if (messageParcel2.writeBoolean(connectAbility(intent, shellInfo, messageParcel.readRemoteObject(), new CallerInfo(messageParcel.readInt(), messageParcel.readInt(), messageParcel.readString(), messageParcel.readInt())))) {
            return true;
        } else {
            HiLog.error(DMS_LABEL, "connectAbilityInner write return failed!", new Object[0]);
            return false;
        }
    }

    private boolean disconnectAbility(IRemoteObject iRemoteObject) {
        if (iRemoteObject == null) {
            HiLog.error(DMS_LABEL, "disconnectAbility parameter is null", new Object[0]);
            return false;
        }
        if (this.mContext != null) {
            synchronized (this.mServiceConnections) {
                ProxyServiceConnection proxyServiceConnection = this.mServiceConnections.get(iRemoteObject);
                if (proxyServiceConnection == null) {
                    HiLog.error(DMS_LABEL, "can't find the remote callback's serviceconnecion", new Object[0]);
                    return false;
                }
                try {
                    this.mContext.unbindService(proxyServiceConnection);
                    this.mServiceConnections.remove(iRemoteObject);
                    reportDtbBindPackageNames();
                    proxyServiceConnection.reportConnectAbilityInfoToPg(505, 1);
                } catch (SecurityException unused) {
                    HiLog.error(DMS_LABEL, "disconnectAbility unbindService SecurityException!", new Object[0]);
                    return false;
                }
            }
        }
        return true;
    }

    private boolean disconnectAbilityInner(MessageParcel messageParcel, MessageParcel messageParcel2) {
        if (messageParcel2.writeBoolean(disconnectAbility(messageParcel.readRemoteObject()))) {
            return true;
        }
        HiLog.error(DMS_LABEL, "connectAbilityInner write return failed!", new Object[0]);
        return false;
    }

    private boolean startActivity(android.content.Intent intent) {
        if (this.mContext == null) {
            HiLog.error(DMS_LABEL, "startActivity context null", new Object[0]);
            return false;
        }
        this.mScreenStateHelper.screenOnNote();
        try {
            this.mContext.startActivityAsUser(intent, UserHandle.CURRENT);
            return true;
        } catch (ActivityNotFoundException unused) {
            HiLog.error(DMS_LABEL, "startActivity ActivityNotFoundException!", new Object[0]);
            return false;
        }
    }

    private boolean startService(android.content.Intent intent) {
        if (BACKGROUND_SERVICE.equals(intent.getStringExtra(SERVICE_TYPE)) && Build.VERSION.SDK_INT >= 26) {
            return startBackgroundService(intent);
        }
        Context context = this.mContext;
        if (context == null) {
            HiLog.error(DMS_LABEL, "startService context null", new Object[0]);
            return false;
        }
        try {
            if (context.startServiceAsUser(intent, UserHandle.CURRENT) != null) {
                return true;
            }
            HiLog.error(DMS_LABEL, "startService failed!", new Object[0]);
            return false;
        } catch (SecurityException unused) {
            HiLog.error(DMS_LABEL, "startService SecurityException!", new Object[0]);
            return false;
        }
    }

    private boolean startBackgroundService(android.content.Intent intent) {
        Context context = this.mContext;
        if (context == null) {
            HiLog.error(DMS_LABEL, "startBackgroundService context null", new Object[0]);
            return false;
        }
        try {
            if (context.startForegroundServiceAsUser(intent, UserHandle.CURRENT) != null) {
                return true;
            }
            HiLog.warn(DMS_LABEL, "startBackgroundService failed!", new Object[0]);
            return false;
        } catch (SecurityException unused) {
            HiLog.error(DMS_LABEL, "startBackgroundService SecurityException!", new Object[0]);
            return false;
        }
    }

    private boolean stopService(android.content.Intent intent) {
        Context context = this.mContext;
        if (context == null) {
            HiLog.error(DMS_LABEL, "stopService context null", new Object[0]);
            return false;
        }
        try {
            return context.stopServiceAsUser(intent, UserHandle.CURRENT);
        } catch (SecurityException unused) {
            HiLog.error(DMS_LABEL, "stopService SecurityException!", new Object[0]);
            return false;
        } catch (IllegalStateException unused2) {
            HiLog.error(DMS_LABEL, "stopService IllegalStateException!", new Object[0]);
            return false;
        }
    }

    private boolean startAbility(Intent intent, ShellInfo shellInfo, int i) {
        if (intent == null || shellInfo == null) {
            HiLog.error(DMS_LABEL, "startAbility parameter is null", new Object[0]);
            return false;
        }
        if (intent.getElement() != null) {
            HiLog.debug(DMS_LABEL, "startAbility BundleName %{public}s, AbilityName %{public}s", intent.getElement().getBundleName(), intent.getElement().getAbilityName());
        }
        Optional<android.content.Intent> checkIntentIsValid = checkIntentIsValid(intent, shellInfo);
        if (!checkIntentIsValid.isPresent()) {
            HiLog.error(DMS_LABEL, "startAbility convert android intent fail", new Object[0]);
            return false;
        }
        android.content.Intent intent2 = checkIntentIsValid.get();
        ShellInfo.ShellType type = shellInfo.getType();
        if (type == ShellInfo.ShellType.ACTIVITY) {
            intent2.addFlags(335544320);
            return startActivity(intent2);
        } else if (type == ShellInfo.ShellType.SERVICE) {
            return startService(intent2);
        } else {
            if (type == ShellInfo.ShellType.WEB) {
                intent2.setClassName(NAME_FASTAPP_PKG, NAME_FASTAPP_CLASS);
                intent2.setData(Uri.parse(FASTAPP_URI_PREFIX + shellInfo.getPackageName() + "/"));
                intent2.setAction("android.intent.action.VIEW");
                intent2.addFlags(335544320);
                return startActivity(intent2);
            }
            HiLog.error(DMS_LABEL, "startAbility shellType not supported", new Object[0]);
            return false;
        }
    }

    private boolean startAbilityInner(MessageParcel messageParcel, MessageParcel messageParcel2) {
        Intent intent = new Intent();
        if (!messageParcel.readSequenceable(intent)) {
            HiLog.error(DMS_LABEL, "startAbilityInner intent is null", new Object[0]);
            return false;
        }
        ShellInfo shellInfo = new ShellInfo();
        if (!messageParcel.readSequenceable(shellInfo)) {
            HiLog.error(DMS_LABEL, "startAbilityInner ShellInfo is null", new Object[0]);
            return false;
        } else if (messageParcel2.writeBoolean(startAbility(intent, shellInfo, messageParcel.readInt()))) {
            return true;
        } else {
            HiLog.error(DMS_LABEL, "startAbilityInner write return failed!", new Object[0]);
            return false;
        }
    }

    private boolean stopAbility(Intent intent, ShellInfo shellInfo) {
        if (intent == null || shellInfo == null) {
            HiLog.error(DMS_LABEL, "stopAbility parameter is null", new Object[0]);
            return false;
        }
        if (intent.getElement() != null) {
            HiLog.debug(DMS_LABEL, "stopAbility BundleName %{public}s, AbilityName %{public}s", intent.getElement().getBundleName(), intent.getElement().getAbilityName());
        }
        Optional<android.content.Intent> checkIntentIsValid = checkIntentIsValid(intent, shellInfo);
        if (!checkIntentIsValid.isPresent()) {
            HiLog.error(DMS_LABEL, "stopAbility convert android intent fail", new Object[0]);
            return false;
        } else if (shellInfo.getType() == ShellInfo.ShellType.SERVICE) {
            return stopService(checkIntentIsValid.get());
        } else {
            HiLog.error(DMS_LABEL, "stopAbility shellType not supported", new Object[0]);
            return false;
        }
    }

    private boolean stopAbilityInner(MessageParcel messageParcel, MessageParcel messageParcel2) {
        Intent intent = new Intent();
        if (!messageParcel.readSequenceable(intent)) {
            HiLog.error(DMS_LABEL, "stopAbilityInner intent is null", new Object[0]);
            return false;
        }
        ShellInfo shellInfo = new ShellInfo();
        if (!messageParcel.readSequenceable(shellInfo)) {
            HiLog.error(DMS_LABEL, "stopAbilityInner ShellInfo is null", new Object[0]);
            return false;
        } else if (messageParcel2.writeBoolean(stopAbility(intent, shellInfo))) {
            return true;
        } else {
            HiLog.error(DMS_LABEL, "stopAbilityInner write return failed!", new Object[0]);
            return false;
        }
    }

    private void deviceOffline(String str) {
        if (str == null || str.isEmpty()) {
            HiLog.error(DMS_LABEL, "deviceOffline deviceId is null!", new Object[0]);
            return;
        }
        synchronized (this.mServiceConnections) {
            Iterator<Map.Entry<IRemoteObject, ProxyServiceConnection>> it = this.mServiceConnections.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<IRemoteObject, ProxyServiceConnection> next = it.next();
                IRemoteObject key = next.getKey();
                ProxyServiceConnection value = next.getValue();
                if (!(key == null || value == null || !value.getSourceDeviceId().equals(str) || this.mContext == null)) {
                    HiLog.debug(DMS_LABEL, "unbind and remove device offline session : %s", str);
                    try {
                        this.mContext.unbindService(value);
                    } catch (SecurityException unused) {
                        HiLog.warn(DMS_LABEL, "deviceOffline unbind service SecurityException", new Object[0]);
                    }
                    it.remove();
                    value.reportConnectAbilityInfoToPg(505, 2);
                }
            }
            reportDtbBindPackageNames();
        }
    }

    private boolean deviceOfflineInner(MessageParcel messageParcel, MessageParcel messageParcel2) {
        deviceOffline(messageParcel.readString());
        if (messageParcel2.writeBoolean(true)) {
            return true;
        }
        HiLog.error(DMS_LABEL, "deviceOfflineInner write return failed!", new Object[0]);
        return false;
    }

    private void processDied(CallerInfo callerInfo) {
        if (callerInfo == null) {
            HiLog.error(DMS_LABEL, "processDied callerInfo is null!", new Object[0]);
            return;
        }
        synchronized (this.mServiceConnections) {
            Iterator<Map.Entry<IRemoteObject, ProxyServiceConnection>> it = this.mServiceConnections.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<IRemoteObject, ProxyServiceConnection> next = it.next();
                IRemoteObject key = next.getKey();
                ProxyServiceConnection value = next.getValue();
                if (!(key == null || value == null || !value.isSameCaller(callerInfo) || this.mContext == null)) {
                    HiLog.debug(DMS_LABEL, "unbind and remove died caller's session: %s, uid %d, pid %d", value.getSourceDeviceId(), Integer.valueOf(value.getUid()), Integer.valueOf(value.getPid()));
                    try {
                        this.mContext.unbindService(value);
                    } catch (SecurityException unused) {
                        HiLog.warn(DMS_LABEL, "processDied unbind service SecurityException", new Object[0]);
                    }
                    it.remove();
                    value.reportConnectAbilityInfoToPg(505, 3);
                }
            }
            reportDtbBindPackageNames();
        }
    }

    private boolean processDiedInner(MessageParcel messageParcel, MessageParcel messageParcel2) {
        processDied(new CallerInfo(messageParcel.readInt(), messageParcel.readInt(), messageParcel.readString(), messageParcel.readInt()));
        if (messageParcel2.writeBoolean(true)) {
            return true;
        }
        HiLog.error(DMS_LABEL, "processDiedInner write return failed!", new Object[0]);
        return false;
    }

    private String dumpInfo() {
        synchronized (this.mServiceConnections) {
            if (this.mServiceConnections.isEmpty()) {
                return "<none info>";
            }
            StringBuilder sb = new StringBuilder(":");
            for (Map.Entry<IRemoteObject, ProxyServiceConnection> entry : this.mServiceConnections.entrySet()) {
                if (entry.getValue() != null) {
                    sb.append(entry.getValue().getUid());
                    sb.append(",");
                    sb.append(entry.getValue().getPid());
                    sb.append(",");
                    sb.append(entry.getValue().getSourceDeviceId());
                    sb.append(",");
                    sb.append(entry.getValue().getCallerType());
                    sb.append(System.lineSeparator());
                }
            }
            return sb.toString();
        }
    }

    private boolean dumpInfoInner(MessageParcel messageParcel, MessageParcel messageParcel2) {
        if (messageParcel2.writeString(dumpInfo())) {
            return true;
        }
        HiLog.warn(DMS_LABEL, "dumpInfoInner write return failed!", new Object[0]);
        return false;
    }

    private Optional<IRemoteObject> getRemoteDataAbility(ohos.utils.net.Uri uri) {
        if (this.mContext == null) {
            HiLog.error(DMS_LABEL, "DmsProxy getRemoteDataAbility mContext null!", new Object[0]);
            return Optional.empty();
        }
        try {
            Bundle bundle = null;
            try {
                bundle = this.mContext.getContentResolver().call(UriConverter.convertToAndroidContentUri(uri), DMS_CALL_METHOD, (String) null, (Bundle) null);
            } catch (IllegalArgumentException unused) {
                HiLog.error(DMS_LABEL, "DmsProxyWrap getRemoteDataAbility getContentResolver().call() fail !", new Object[0]);
            }
            if (bundle == null) {
                HiLog.error(DMS_LABEL, "DmsProxyWrap getRemoteDataAbility resolver.call bundle null!", new Object[0]);
                return Optional.empty();
            }
            IBinder binder = bundle.getBinder(DATA_ABILITY_FLAG);
            if (binder != null) {
                return IPCAdapter.translateToIRemoteObject(binder);
            }
            HiLog.error(DMS_LABEL, "DmsProxyWrap getRemoteDataAbility getBinder null!", new Object[0]);
            return Optional.empty();
        } catch (IllegalArgumentException unused2) {
            HiLog.debug(DMS_LABEL, "DmsProxyWrap getRemoteDataAbility convertToAndroidContentUri exception!", new Object[0]);
            return Optional.empty();
        }
    }

    private boolean writeResult(MessageParcel messageParcel, boolean z) {
        if (messageParcel == null) {
            return false;
        }
        return messageParcel.writeBoolean(z);
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:43:0x00ad */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean getDataAbilityInner(ohos.rpc.MessageParcel r8, ohos.rpc.MessageParcel r9) {
        /*
        // Method dump skipped, instructions count: 228
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.ohos.foundation.DmsProxyManagerWrap.getDataAbilityInner(ohos.rpc.MessageParcel, ohos.rpc.MessageParcel):boolean");
    }

    private boolean getUdidInner(MessageParcel messageParcel, MessageParcel messageParcel2) {
        try {
            String udid = BuildEx.getUDID();
            if (udid == null) {
                HiLog.error(DMS_LABEL, "DmsProxy getUdidInner object null!", new Object[0]);
                return false;
            }
            boolean writeString = messageParcel2.writeString(udid);
            if (!writeString) {
                HiLog.error(DMS_LABEL, "DmsProxy getUdidInner object null!", new Object[0]);
            }
            return writeString;
        } catch (SecurityException unused) {
            HiLog.error(DMS_LABEL, "DmsProxy getUdidInner catch SecurityException!", new Object[0]);
            return false;
        } catch (AndroidRuntimeException unused2) {
            HiLog.error(DMS_LABEL, "DmsProxy getUdidInner catch AndroidRuntimeException!", new Object[0]);
            return false;
        }
    }

    @Override // ohos.rpc.RemoteObject
    public boolean onRemoteRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException {
        if (IPCSkeleton.getCallingUid() % 100000 >= 10000) {
            HiLog.warn(DMS_LABEL, "request caller is illegal", new Object[0]);
            return false;
        } else if (messageParcel == null || messageParcel2 == null) {
            return false;
        } else {
            if (!DMS_PROXY_INTERFACE_TOKEN.equals(messageParcel.readInterfaceToken())) {
                HiLog.warn(DMS_LABEL, "interface token check failed", new Object[0]);
                return false;
            }
            HiLog.debug(DMS_LABEL, "[PerformanceTest] onRemoteRequest code=%{public}d begin", Integer.valueOf(i));
            HiLog.info(DMS_LABEL, "onRemoteRequest code=%{public}d begin", Integer.valueOf(i));
            switch (i) {
                case 1:
                    return connectAbilityInner(messageParcel, messageParcel2);
                case 2:
                    return disconnectAbilityInner(messageParcel, messageParcel2);
                case 3:
                    return startAbilityInner(messageParcel, messageParcel2);
                case 4:
                    return stopAbilityInner(messageParcel, messageParcel2);
                case 5:
                    return deviceOfflineInner(messageParcel, messageParcel2);
                case 6:
                    return processDiedInner(messageParcel, messageParcel2);
                case 7:
                    return dumpInfoInner(messageParcel, messageParcel2);
                case 8:
                    return getDataAbilityInner(messageParcel, messageParcel2);
                case 9:
                    return getUdidInner(messageParcel, messageParcel2);
                default:
                    return super.onRemoteRequest(i, messageParcel, messageParcel2, messageOption);
            }
        }
    }

    /* access modifiers changed from: private */
    public class ProxyServiceConnection implements ServiceConnection {
        private static final String INTERFACE_TOKEN = "harmonyos.abilityshell.DistributedConnection";
        private final ArrayMap<String, ArraySet<String>> mCacheNames = new ArrayMap<>();
        private CallerInfo mCallerInfo;
        private IRemoteObject mZCallback;
        private String targetBundleName;

        public ProxyServiceConnection(IRemoteObject iRemoteObject, CallerInfo callerInfo) {
            this.mZCallback = iRemoteObject;
            this.mCallerInfo = callerInfo;
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (componentName == null || iBinder == null) {
                HiLog.error(DmsProxyManagerWrap.DMS_LABEL, "onServiceConnected ComponentName or IBinder service is null", new Object[0]);
                return;
            }
            HiLog.debug(DmsProxyManagerWrap.DMS_LABEL, "onServiceConnected ComponentName package:%{public}s, class name:%{public}s", componentName.getPackageName(), componentName.getClassName());
            Optional<ElementName> elementNameByName = getElementNameByName(componentName);
            if (!elementNameByName.isPresent()) {
                HiLog.error(DmsProxyManagerWrap.DMS_LABEL, "elementName is null", new Object[0]);
                return;
            }
            if (this.mCallerInfo.getCallerType() == 1) {
                Optional<IRemoteObject> translateToIRemoteObject = IPCAdapter.translateToIRemoteObject(iBinder);
                if (!translateToIRemoteObject.isPresent()) {
                    HiLog.error(DmsProxyManagerWrap.DMS_LABEL, "convert IRemoteObject fail", new Object[0]);
                    transactDisconnectToRemoteAbility(elementNameByName.get(), DmsProxyManagerWrap.ERR_BINDER_CONVERTER);
                    return;
                }
                transactConnectToRemoteAbility(elementNameByName.get(), translateToIRemoteObject.get(), 0);
            } else if (this.mCallerInfo.getCallerType() == 2) {
                EmuiTransporter.transactConnectToRemoteService(this.mZCallback, elementNameByName.get(), iBinder, 0);
            } else {
                HiLog.warn(DmsProxyManagerWrap.DMS_LABEL, "onServiceConnected: check callerType failed", new Object[0]);
            }
            addCacheName(componentName);
        }

        public void onServiceDisconnected(ComponentName componentName) {
            if (componentName == null) {
                HiLog.error(DmsProxyManagerWrap.DMS_LABEL, "onServiceDisconnected ComponentName is null", new Object[0]);
                return;
            }
            HiLog.debug(DmsProxyManagerWrap.DMS_LABEL, "onServiceDisconnected ComponentName package:%{public}s, class name:%{public}s", componentName.getPackageName(), componentName.getClassName());
            Optional<ElementName> elementNameByName = getElementNameByName(componentName);
            if (!elementNameByName.isPresent()) {
                HiLog.error(DmsProxyManagerWrap.DMS_LABEL, "elementName is null", new Object[0]);
                return;
            }
            if (this.mCallerInfo.getCallerType() == 1) {
                transactDisconnectToRemoteAbility(elementNameByName.get(), DmsProxyManagerWrap.ERR_BIND_ABILITY_CRASH);
            } else if (this.mCallerInfo.getCallerType() == 2) {
                EmuiTransporter.transactDisconnectToRemoteService(this.mZCallback, elementNameByName.get(), DmsProxyManagerWrap.ERR_BIND_ABILITY_CRASH);
            } else {
                HiLog.warn(DmsProxyManagerWrap.DMS_LABEL, "onServiceDisconnected: check callerType failed", new Object[0]);
            }
            removeCacheName(componentName);
        }

        public void onBindingDied(ComponentName componentName) {
            if (componentName == null) {
                HiLog.error(DmsProxyManagerWrap.DMS_LABEL, "onBindingDied ComponentName is null", new Object[0]);
                return;
            }
            HiLog.debug(DmsProxyManagerWrap.DMS_LABEL, "onBindingDied ComponentName package:%{public}s, class name:%{public}s", componentName.getPackageName(), componentName.getClassName());
            Optional<ElementName> elementNameByName = getElementNameByName(componentName);
            if (!elementNameByName.isPresent()) {
                HiLog.error(DmsProxyManagerWrap.DMS_LABEL, "elementName is null", new Object[0]);
                return;
            }
            if (this.mCallerInfo.getCallerType() == 1) {
                transactDisconnectToRemoteAbility(elementNameByName.get(), DmsProxyManagerWrap.ERR_BIND_ABILITY_DIED);
            } else if (this.mCallerInfo.getCallerType() == 2) {
                EmuiTransporter.transactDisconnectToRemoteService(this.mZCallback, elementNameByName.get(), DmsProxyManagerWrap.ERR_BIND_ABILITY_DIED);
            } else {
                HiLog.warn(DmsProxyManagerWrap.DMS_LABEL, "onBindingDied: check callerType failed", new Object[0]);
            }
            removeCacheName(componentName);
        }

        public void onNullBinding(ComponentName componentName) {
            if (componentName == null) {
                HiLog.error(DmsProxyManagerWrap.DMS_LABEL, "onNullBinding ComponentName is null", new Object[0]);
                return;
            }
            HiLog.debug(DmsProxyManagerWrap.DMS_LABEL, "onNullBinding ComponentName package:%{public}s, class name:%{public}s", componentName.getPackageName(), componentName.getClassName());
            Optional<ElementName> elementNameByName = getElementNameByName(componentName);
            if (!elementNameByName.isPresent()) {
                HiLog.error(DmsProxyManagerWrap.DMS_LABEL, "elementName is null", new Object[0]);
                return;
            }
            if (this.mCallerInfo.getCallerType() == 1) {
                transactDisconnectToRemoteAbility(elementNameByName.get(), DmsProxyManagerWrap.ERR_BIND_ABILITY_NULL);
            } else if (this.mCallerInfo.getCallerType() == 2) {
                EmuiTransporter.transactDisconnectToRemoteService(this.mZCallback, elementNameByName.get(), DmsProxyManagerWrap.ERR_BIND_ABILITY_NULL);
            } else {
                HiLog.warn(DmsProxyManagerWrap.DMS_LABEL, "onNullBinding: check callerType failed", new Object[0]);
            }
            removeCacheName(componentName);
        }

        public void getPackageNames(ArraySet<String> arraySet) {
            if (arraySet != null) {
                synchronized (this.mCacheNames) {
                    for (String str : this.mCacheNames.keySet()) {
                        if (str != null) {
                            arraySet.add(str);
                        }
                    }
                }
            }
        }

        /* JADX WARNING: Can't wrap try/catch for region: R(2:15|16) */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0071, code lost:
            r6 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
            ohos.hiviewdfx.HiLog.error(com.huawei.ohos.foundation.DmsProxyManagerWrap.DMS_LABEL, "RemoteException", new java.lang.Object[0]);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0085, code lost:
            r0.reclaim();
            r2.reclaim();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x008b, code lost:
            throw r6;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0073 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void transactDisconnectToRemoteAbility(ohos.bundle.ElementName r7, int r8) {
            /*
            // Method dump skipped, instructions count: 140
            */
            throw new UnsupportedOperationException("Method not decompiled: com.huawei.ohos.foundation.DmsProxyManagerWrap.ProxyServiceConnection.transactDisconnectToRemoteAbility(ohos.bundle.ElementName, int):void");
        }

        /* JADX WARNING: Can't wrap try/catch for region: R(2:18|19) */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0071, code lost:
            r4 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:?, code lost:
            ohos.hiviewdfx.HiLog.error(com.huawei.ohos.foundation.DmsProxyManagerWrap.DMS_LABEL, "RemoteException", new java.lang.Object[0]);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0085, code lost:
            r0.reclaim();
            r1.reclaim();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x008b, code lost:
            throw r4;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:18:0x0073 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void transactConnectToRemoteAbility(ohos.bundle.ElementName r5, ohos.rpc.IRemoteObject r6, int r7) {
            /*
            // Method dump skipped, instructions count: 140
            */
            throw new UnsupportedOperationException("Method not decompiled: com.huawei.ohos.foundation.DmsProxyManagerWrap.ProxyServiceConnection.transactConnectToRemoteAbility(ohos.bundle.ElementName, ohos.rpc.IRemoteObject, int):void");
        }

        private Optional<ElementName> getElementNameByName(ComponentName componentName) {
            if (componentName == null) {
                return Optional.empty();
            }
            ShellInfo shellInfo = new ShellInfo();
            shellInfo.setPackageName(componentName.getPackageName());
            shellInfo.setName(componentName.getClassName());
            shellInfo.setType(ShellInfo.ShellType.SERVICE);
            IRemoteObject checkSysAbility = SysAbilityManager.checkSysAbility(401);
            if (checkSysAbility == null) {
                return Optional.empty();
            }
            AbilityInfo abilityByShell = new BundleManager(checkSysAbility).getAbilityByShell(shellInfo);
            if (abilityByShell == null) {
                return Optional.empty();
            }
            String bundleName = abilityByShell.getBundleName();
            String className = abilityByShell.getClassName();
            if (bundleName == null || className == null) {
                return Optional.empty();
            }
            return Optional.of(new ElementName(abilityByShell.getDeviceId(), bundleName, className));
        }

        /* access modifiers changed from: package-private */
        public String getSourceDeviceId() {
            CallerInfo callerInfo = this.mCallerInfo;
            return callerInfo != null ? callerInfo.getSourceDeviceId() : "";
        }

        /* access modifiers changed from: package-private */
        public int getUid() {
            CallerInfo callerInfo = this.mCallerInfo;
            if (callerInfo != null) {
                return callerInfo.getUid();
            }
            return -1;
        }

        /* access modifiers changed from: package-private */
        public int getPid() {
            CallerInfo callerInfo = this.mCallerInfo;
            if (callerInfo != null) {
                return callerInfo.getPid();
            }
            return -1;
        }

        /* access modifiers changed from: package-private */
        public int getCallerType() {
            CallerInfo callerInfo = this.mCallerInfo;
            if (callerInfo != null) {
                return callerInfo.getCallerType();
            }
            return 0;
        }

        /* access modifiers changed from: package-private */
        public boolean isSameCaller(CallerInfo callerInfo) {
            CallerInfo callerInfo2;
            if (callerInfo == null || (callerInfo2 = this.mCallerInfo) == null) {
                return false;
            }
            return callerInfo2.isSame(callerInfo);
        }

        private void addCacheName(ComponentName componentName) {
            if (componentName != null) {
                String packageName = componentName.getPackageName();
                String className = componentName.getClassName();
                if (packageName != null && className != null && !packageName.isEmpty() && !className.isEmpty()) {
                    synchronized (this.mCacheNames) {
                        ArraySet<String> arraySet = this.mCacheNames.get(packageName);
                        if (arraySet == null) {
                            arraySet = new ArraySet<>();
                            this.mCacheNames.put(packageName, arraySet);
                        }
                        arraySet.add(className);
                    }
                    DmsProxyManagerWrap.this.reportDtbBindPackageNames();
                }
            }
        }

        private void removeCacheName(ComponentName componentName) {
            if (componentName != null) {
                String packageName = componentName.getPackageName();
                String className = componentName.getClassName();
                if (packageName != null && className != null && !packageName.isEmpty() && !className.isEmpty()) {
                    synchronized (this.mCacheNames) {
                        ArraySet<String> arraySet = this.mCacheNames.get(packageName);
                        if (arraySet != null) {
                            arraySet.remove(className);
                            if (arraySet.isEmpty()) {
                                this.mCacheNames.remove(packageName);
                            }
                            DmsProxyManagerWrap.this.reportDtbBindPackageNames();
                        }
                    }
                }
            }
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void setTargetBundleName(String str) {
            this.targetBundleName = str;
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void reportConnectAbilityInfoToPg(int i, int i2) {
            CallerInfo callerInfo;
            String str = this.targetBundleName;
            if (str == null || str.isEmpty() || (callerInfo = this.mCallerInfo) == null) {
                HiLog.warn(DmsProxyManagerWrap.DMS_LABEL, "reportConnectAbilityInfoToPg invalid parameter", new Object[0]);
                return;
            }
            String sourceDeviceId = callerInfo.getSourceDeviceId();
            if (sourceDeviceId == null || sourceDeviceId.isEmpty()) {
                HiLog.warn(DmsProxyManagerWrap.DMS_LABEL, "reportConnectAbilityInfoToPg callerDeviceId invalid parameter", new Object[0]);
                return;
            }
            HiLog.info(DmsProxyManagerWrap.DMS_LABEL, "reportConnectInfo eventId:%{public}d bundleName:%{public}s callerFrom:%{public}d", Integer.valueOf(i), this.targetBundleName, Integer.valueOf(i2));
            LogPower.push(i, this.targetBundleName, appendReportInfo(sourceDeviceId), "2");
        }

        private String appendReportInfo(String str) {
            String str2 = -1 + DmsProxyManagerWrap.DELIMITER + -1 + DmsProxyManagerWrap.DELIMITER + str;
            HiLog.debug(DmsProxyManagerWrap.DMS_LABEL, "appendReportInfo appendInfo:%s ", str2);
            return str2;
        }
    }

    /* access modifiers changed from: private */
    public static class CallerInfo {
        private int mCallerType;
        private int mPid;
        private String mSourceDeviceId = "";
        private int mUid;

        public CallerInfo(int i, int i2, String str, int i3) {
            this.mUid = i;
            this.mPid = i2;
            if (str != null) {
                this.mSourceDeviceId = str;
            }
            this.mCallerType = i3;
        }

        /* access modifiers changed from: package-private */
        public int getUid() {
            return this.mUid;
        }

        /* access modifiers changed from: package-private */
        public int getPid() {
            return this.mPid;
        }

        /* access modifiers changed from: package-private */
        public String getSourceDeviceId() {
            return this.mSourceDeviceId;
        }

        /* access modifiers changed from: package-private */
        public int getCallerType() {
            return this.mCallerType;
        }

        /* access modifiers changed from: package-private */
        public boolean isSame(CallerInfo callerInfo) {
            if (callerInfo != null && this.mUid == callerInfo.getUid() && this.mPid == callerInfo.getPid() && this.mSourceDeviceId.equals(callerInfo.getSourceDeviceId()) && this.mCallerType == callerInfo.getCallerType()) {
                return true;
            }
            return false;
        }
    }

    private boolean isAbnormalParameters(Intent intent, ShellInfo shellInfo, IRemoteObject iRemoteObject, CallerInfo callerInfo) {
        return intent == null || shellInfo == null || iRemoteObject == null || callerInfo == null || callerInfo.getSourceDeviceId().isEmpty();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* JADX WARNING: Can't wrap try/catch for region: R(2:29|30) */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00a2, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
        ohos.hiviewdfx.HiLog.error(com.huawei.ohos.foundation.DmsProxyManagerWrap.DMS_LABEL, "RemoteException when reportDtbBindPackageNames.", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00b4, code lost:
        r2.reclaim();
        r3.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00ba, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x00a4 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void reportDtbBindPackageNames() {
        /*
        // Method dump skipped, instructions count: 187
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.ohos.foundation.DmsProxyManagerWrap.reportDtbBindPackageNames():void");
    }

    private String getPackageNamesStr() {
        ArraySet<String> arraySet = new ArraySet<>();
        synchronized (this.mServiceConnections) {
            for (ProxyServiceConnection proxyServiceConnection : this.mServiceConnections.values()) {
                if (proxyServiceConnection != null) {
                    proxyServiceConnection.getPackageNames(arraySet);
                }
            }
        }
        if (arraySet.isEmpty()) {
            return NONE_PACKAGE_NAME;
        }
        StringBuffer stringBuffer = new StringBuffer();
        Iterator<String> it = arraySet.iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (next != null && !next.isEmpty()) {
                stringBuffer.append(next);
                stringBuffer.append("\n");
            }
        }
        return stringBuffer.toString();
    }

    /* access modifiers changed from: private */
    public static class ScreenStateHelper {
        private Context mContext;

        public ScreenStateHelper(Context context) {
            this.mContext = context;
        }

        public void screenOnNote() {
            Context context = this.mContext;
            if (context == null) {
                HiLog.error(DmsProxyManagerWrap.DMS_LABEL, "screenOnNote context null!", new Object[0]);
                return;
            }
            Object systemService = context.getSystemService("power");
            if (!(systemService instanceof PowerManager)) {
                HiLog.warn(DmsProxyManagerWrap.DMS_LABEL, "screenOnNote getSystemService invalid instance!", new Object[0]);
                return;
            }
            PowerManager powerManager = (PowerManager) systemService;
            powerManager.wakeUp(SystemClock.uptimeMillis(), 2, DmsProxyManagerWrap.START_ABILITY_SCREEN_ON);
            powerManager.userActivity(SystemClock.uptimeMillis(), 0, 0);
        }
    }
}
