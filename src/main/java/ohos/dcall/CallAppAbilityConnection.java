package ohos.dcall;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.telecom.Call;
import android.telecom.CallAudioState;
import android.telephony.PhoneNumberUtils;
import android.util.ArrayMap;
import com.huawei.ohos.interwork.AbilityUtils;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import ohos.dcall.CallAppAbilityProxy;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.sysability.samgr.SysAbilityManager;
import ohos.sysability.samgr.SystemReadyCallbackSkeleton;

/* access modifiers changed from: package-private */
public class CallAppAbilityConnection {
    private static final int CONNECTION_FAILED = 2;
    private static final int CONNECTION_NOT_SUPPORTED = 3;
    private static final int CONNECTION_SUCCEEDED = 1;
    private static final long DELAYED_TIME_DISCONNECT_PRE_ADDED_CALL = 35000;
    private static final long DELAYED_TIME_RETRY_CONNECT_INCALL_ABILITY = 500;
    private static final int EVENT_DISCONNECT_PRE_ADDED_CALL = 1001;
    private static final int EVENT_RETRY_CONNECT_INCALL_ABILITY = 1000;
    private static final Integer INVALID_KEY_SET_IN_MAP = -1;
    private static final int LOG_DOMAIN_DCALL = 218111744;
    private static final HiLogLabel LOG_LABEL = new HiLogLabel(3, 218111744, TAG);
    private static final int MAXIMUM_CALLS = 32;
    private static final int MAX_COUNTS_RETRY_CONNECT_INCALL_ABILITY = 6;
    private static final String SAMGR_CORESA_INITREADY = "sys.samgr.coresa.initready";
    private static final String TAG = "CallAppAbilityConnection";
    private static int sCallId = 1;
    private final Map<Integer, Call> mCallMapById = new ArrayMap();
    private Context mContext;
    private CallAudioState mCurrentCallAudioState;
    private Boolean mCurrentCanAddCall;
    private CallAppAbilityConnnectionHandler mHandler;
    private boolean mHasActiveBluetoothCall;
    private final CallAppAbilityInfo mInfo;
    private boolean mIsBound;
    private boolean mIsConnected;
    private boolean mIsInCallServiceBinded;
    private boolean mIsPreConnected;
    private boolean mIsReconnectWhenSysReady;
    private Listener mListener;
    private final CallAppAbilityProxy.SyncLock mLock;
    private CallAudioState mPendingCallAudioState;
    private Boolean mPendingCanAddCall;
    private PreAddedCall mPendingDisconnectPreAddedCall;
    private PreAddedCall mPreAddedCall;
    private IBinder mRemote;
    private int mRetryConnectCallAppAbilityCount;
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        /* class ohos.dcall.CallAppAbilityConnection.AnonymousClass1 */

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            synchronized (CallAppAbilityConnection.this.mLock) {
                HiLog.info(CallAppAbilityConnection.LOG_LABEL, "onServiceConnected: %{public}s, mIsBound is %{public}b, mIsConnected is %{public}b.", componentName, Boolean.valueOf(CallAppAbilityConnection.this.mIsBound), Boolean.valueOf(CallAppAbilityConnection.this.mIsConnected));
                CallAppAbilityConnection.this.mIsBound = true;
                if (CallAppAbilityConnection.this.mIsConnected) {
                    CallAppAbilityConnection.this.onConnected(componentName, iBinder);
                }
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            synchronized (CallAppAbilityConnection.this.mLock) {
                HiLog.info(CallAppAbilityConnection.LOG_LABEL, "onServiceConnected: %{public}s.", componentName);
                CallAppAbilityConnection.this.mIsBound = false;
                CallAppAbilityConnection.this.onDisconnected(componentName);
            }
        }
    };
    private SystemReadyCallback mSystemReadyCallback = new SystemReadyCallback(this);

    /* access modifiers changed from: package-private */
    public interface Listener {
        void onConnected(CallAppAbilityConnection callAppAbilityConnection);

        void onDisconnected(CallAppAbilityConnection callAppAbilityConnection);

        void onReleased(CallAppAbilityConnection callAppAbilityConnection);
    }

    public CallAppAbilityConnection(Context context, CallAppAbilityProxy.SyncLock syncLock, CallAppAbilityInfo callAppAbilityInfo) {
        this.mContext = context;
        this.mLock = syncLock;
        this.mInfo = callAppAbilityInfo;
    }

    /* access modifiers changed from: private */
    public class SystemReadyCallback extends SystemReadyCallbackSkeleton {
        private WeakReference<CallAppAbilityConnection> connectionWeakRef;

        public SystemReadyCallback(CallAppAbilityConnection callAppAbilityConnection) {
            this.connectionWeakRef = new WeakReference<>(callAppAbilityConnection);
        }

        @Override // ohos.sysability.samgr.ISystemReadyCallback
        public void onSystemReadyNotify() {
            CallAppAbilityConnection callAppAbilityConnection = this.connectionWeakRef.get();
            if (callAppAbilityConnection == null) {
                HiLog.error(CallAppAbilityConnection.LOG_LABEL, "SystemReadyCallback$onSystemReadyNotify: connection is null.", new Object[0]);
                return;
            }
            HiLog.info(CallAppAbilityConnection.LOG_LABEL, "SystemReadyCallback$onSystemReadyNotify: the core system ability is ready, mIsReconnectWhenSysReady : %{public}b.", Boolean.valueOf(callAppAbilityConnection.mIsReconnectWhenSysReady));
            if (callAppAbilityConnection.mIsReconnectWhenSysReady) {
                callAppAbilityConnection.mIsReconnectWhenSysReady = false;
                CallAppAbilityConnection.this.connect();
            }
        }
    }

    public String toString() {
        CallAppAbilityInfo callAppAbilityInfo = this.mInfo;
        return callAppAbilityInfo != null ? callAppAbilityInfo.toString() : "";
    }

    /* access modifiers changed from: package-private */
    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    /* access modifiers changed from: package-private */
    public int connect() {
        if (this.mIsConnected) {
            HiLog.info(LOG_LABEL, "Already connected, ignoring request.", new Object[0]);
            return 1;
        }
        CallAppAbilityInfo callAppAbilityInfo = this.mInfo;
        if (callAppAbilityInfo == null || callAppAbilityInfo.getComponentName() == null) {
            HiLog.error(LOG_LABEL, "no component to connect.", new Object[0]);
            return 2;
        }
        HiLog.info(LOG_LABEL, "Attempting to connect %{public}s.", this.mInfo);
        this.mIsConnected = true;
        if (connectCallAppAbility(this.mInfo.getComponentName())) {
            return 1;
        }
        HiLog.error(LOG_LABEL, "Failed to connect.", new Object[0]);
        this.mIsConnected = false;
        return 2;
    }

    /* access modifiers changed from: package-private */
    public CallAppAbilityInfo getInfo() {
        return this.mInfo;
    }

    /* access modifiers changed from: package-private */
    public void disconnect() {
        if (this.mIsConnected) {
            disconnectCallAppAbility();
            this.mRemote = null;
            this.mIsConnected = false;
            return;
        }
        HiLog.error(LOG_LABEL, "Already disconnected, ignoring request.", new Object[0]);
    }

    /* access modifiers changed from: package-private */
    public void releaseResource() {
        if (!isPreConnected() && !isInCallServiceBinded() && this.mCallMapById.isEmpty() && this.mPreAddedCall == null && this.mPendingDisconnectPreAddedCall == null) {
            release();
        }
    }

    /* access modifiers changed from: package-private */
    public void release() {
        HiLog.info(LOG_LABEL, "release.", new Object[0]);
        this.mCallMapById.clear();
        this.mRemote = null;
        disconnect();
        destroyHandler();
        this.mContext = null;
        this.mPendingCallAudioState = null;
        this.mCurrentCallAudioState = null;
        this.mPendingCanAddCall = null;
        this.mPreAddedCall = null;
        this.mPendingDisconnectPreAddedCall = null;
        this.mIsConnected = false;
        this.mHasActiveBluetoothCall = false;
        this.mIsReconnectWhenSysReady = false;
        synchronized (this.mLock) {
            this.mIsBound = false;
        }
        this.mIsInCallServiceBinded = false;
        this.mIsPreConnected = false;
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onReleased(this);
            this.mListener = null;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isConnected() {
        HiLog.info(LOG_LABEL, "isConnected: %{public}b", Boolean.valueOf(this.mIsConnected));
        return this.mIsConnected;
    }

    /* access modifiers changed from: package-private */
    public boolean isInCallServiceBinded() {
        HiLog.info(LOG_LABEL, "isInCallServiceBinded: %{public}b", Boolean.valueOf(this.mIsInCallServiceBinded));
        return this.mIsInCallServiceBinded;
    }

    /* access modifiers changed from: package-private */
    public boolean isPreConnected() {
        HiLog.info(LOG_LABEL, "isPreConnected: %{public}b", Boolean.valueOf(this.mIsPreConnected));
        return this.mIsPreConnected;
    }

    /* access modifiers changed from: package-private */
    public void onConnected(ComponentName componentName, IBinder iBinder) {
        HiLog.info(LOG_LABEL, "onConnected.", new Object[0]);
        addPendingCallToProxy(iBinder);
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onConnected(this);
        }
    }

    /* access modifiers changed from: package-private */
    public void onDisconnected(ComponentName componentName) {
        disconnect();
        HiLog.info(LOG_LABEL, "onDisconnected.", new Object[0]);
        this.mRemote = null;
        processLiveCallWhenServiceDisconnected(componentName);
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onDisconnected(this);
        }
    }

    /* access modifiers changed from: package-private */
    public void onInCallServiceBind() {
        HiLog.info(LOG_LABEL, "onInCallServiceBind.", new Object[0]);
        this.mIsInCallServiceBinded = true;
    }

    /* access modifiers changed from: package-private */
    public void onInCallServiceUnbind() {
        HiLog.info(LOG_LABEL, "onInCallServiceUnbind.", new Object[0]);
        this.mIsInCallServiceBinded = false;
        releaseResource();
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x003b */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onCallAudioStateChanged(android.telecom.CallAudioState r6) {
        /*
            r5 = this;
            r5.mCurrentCallAudioState = r6
            android.os.IBinder r0 = r5.mRemote
            r1 = 0
            if (r0 != 0) goto L_0x0014
            ohos.hiviewdfx.HiLogLabel r0 = ohos.dcall.CallAppAbilityConnection.LOG_LABEL
            java.lang.Object[] r2 = new java.lang.Object[r1]
            java.lang.String r3 = "onCallAudioStateChanged fail, no remote."
            ohos.hiviewdfx.HiLog.error(r0, r3, r2)
            r5.mPendingCallAudioState = r6
            return r1
        L_0x0014:
            if (r6 == 0) goto L_0x0054
            android.os.Parcel r0 = android.os.Parcel.obtain()
            android.os.Parcel r2 = android.os.Parcel.obtain()
            r3 = 1
            java.lang.String r4 = "OHOS.Dcall.DistributedCallAbility"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x003a }
            android.os.IBinder r5 = r5.mRemote     // Catch:{ RemoteException -> 0x003a }
            android.os.Parcel r6 = ohos.dcall.CallSerializationUtils.writeCallAudioStateToParcel(r0, r6)     // Catch:{ RemoteException -> 0x003a }
            r5.transact(r3, r6, r2, r1)     // Catch:{ RemoteException -> 0x003a }
            ohos.hiviewdfx.HiLogLabel r5 = ohos.dcall.CallAppAbilityConnection.LOG_LABEL     // Catch:{ RemoteException -> 0x003b }
            java.lang.String r6 = "onCallAudioStateChanged."
            java.lang.Object[] r4 = new java.lang.Object[r1]     // Catch:{ RemoteException -> 0x003b }
            ohos.hiviewdfx.HiLog.info(r5, r6, r4)     // Catch:{ RemoteException -> 0x003b }
            goto L_0x0045
        L_0x0038:
            r5 = move-exception
            goto L_0x004d
        L_0x003a:
            r3 = r1
        L_0x003b:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.dcall.CallAppAbilityConnection.LOG_LABEL     // Catch:{ all -> 0x0038 }
            java.lang.String r6 = "onCallAudioStateChanged got RemoteException."
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x0038 }
            ohos.hiviewdfx.HiLog.error(r5, r6, r1)     // Catch:{ all -> 0x0038 }
        L_0x0045:
            r2.recycle()
            r0.recycle()
            r1 = r3
            goto L_0x005e
        L_0x004d:
            r2.recycle()
            r0.recycle()
            throw r5
        L_0x0054:
            ohos.hiviewdfx.HiLogLabel r5 = ohos.dcall.CallAppAbilityConnection.LOG_LABEL
            java.lang.Object[] r6 = new java.lang.Object[r1]
            java.lang.String r0 = "onCallAudioStateChanged fail, no audioState."
            ohos.hiviewdfx.HiLog.error(r5, r0, r6)
        L_0x005e:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.CallAppAbilityConnection.onCallAudioStateChanged(android.telecom.CallAudioState):boolean");
    }

    /* access modifiers changed from: package-private */
    public boolean onCallAdded(Call call) {
        if (call == null) {
            HiLog.error(LOG_LABEL, "onCallAdded fail, no call.", new Object[0]);
            return false;
        } else if (call.getState() == 9 && this.mPendingDisconnectPreAddedCall != null) {
            HiLog.info(LOG_LABEL, "onCallAdded, has pending disconnect call.", new Object[0]);
            CallAppAbilityConnnectionHandler callAppAbilityConnnectionHandler = this.mHandler;
            if (callAppAbilityConnnectionHandler != null) {
                callAppAbilityConnnectionHandler.removeEvent(1001);
            }
            this.mPendingDisconnectPreAddedCall = null;
            call.disconnect();
            return true;
        } else if (!canAddCallToCallMap()) {
            return false;
        } else {
            if (isConnected()) {
                HiLog.info(LOG_LABEL, "onCallAdded, already connected call ability.", new Object[0]);
                if (this.mPreAddedCall == null && this.mPendingDisconnectPreAddedCall == null) {
                    HiLog.info(LOG_LABEL, "onCallAdded, no pre added call.", new Object[0]);
                    addCallToCallMap(call);
                } else {
                    if (this.mPreAddedCall == null) {
                        PreAddedCall preAddedCall = this.mPendingDisconnectPreAddedCall;
                    }
                    processPreAddedCall(this.mPreAddedCall, call);
                    this.mPreAddedCall = null;
                    this.mPendingDisconnectPreAddedCall = null;
                }
                return addCall(call);
            }
            addCallToCallMap(call);
            this.mPreAddedCall = null;
            this.mPendingDisconnectPreAddedCall = null;
            connect();
            return true;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean processPreAddedCall(PreAddedCall preAddedCall, Call call) {
        if (preAddedCall == null) {
            HiLog.error(LOG_LABEL, "processPreAddedCall fail, no pre added call.", new Object[0]);
            return false;
        } else if (call == null) {
            HiLog.error(LOG_LABEL, "processPreAddedCall fail, no call.", new Object[0]);
            return false;
        } else {
            String normalizeNumber = PhoneNumberUtils.normalizeNumber(AospInCallService.getNumber(call));
            String normalizeNumber2 = PhoneNumberUtils.normalizeNumber(preAddedCall.getNumber());
            if (call.getState() != 9 || normalizeNumber == null || !normalizeNumber.equals(normalizeNumber2)) {
                HiLog.info(LOG_LABEL, "processPreAddedCall, disconnect pre added call.", new Object[0]);
                disconnectPreAddedCall(preAddedCall);
                sCallId++;
                addCallToCallMap(call);
            } else {
                HiLog.info(LOG_LABEL, "processPreAddedCall, update pre added call.", new Object[0]);
                addCallToCallMap(call);
                onStateChanged(call, call.getState());
                onDetailsChanged(call, call.getDetails());
            }
            return true;
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:16:0x004e */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean addCall(android.telecom.Call r7) {
        /*
        // Method dump skipped, instructions count: 101
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.CallAppAbilityConnection.addCall(android.telecom.Call):boolean");
    }

    /* access modifiers changed from: package-private */
    public boolean preOnCallAdded(String str, int i, String str2, String str3) {
        if (str == null) {
            HiLog.error(LOG_LABEL, "preOnCallAdded: no number.", new Object[0]);
            return false;
        } else if (str2 == null || str3 == null) {
            HiLog.error(LOG_LABEL, "preOnCallAdded: no call component or ability name.", new Object[0]);
            return false;
        } else {
            ComponentName componentName = new ComponentName(str2, str3);
            CallAppAbilityInfo callAppAbilityInfo = this.mInfo;
            if (callAppAbilityInfo == null || !componentName.equals(callAppAbilityInfo.getComponentName())) {
                HiLog.info(LOG_LABEL, "preOnCallAdded: call component name is different.", new Object[0]);
                return false;
            } else if (this.mPreAddedCall != null || this.mPendingDisconnectPreAddedCall != null) {
                HiLog.error(LOG_LABEL, "preOnCallAdded: already pre add call before.", new Object[0]);
                return false;
            } else if (hasLiveCalls()) {
                HiLog.error(LOG_LABEL, "preOnCallAdded: already has call before.", new Object[0]);
                return false;
            } else if (!canAddCallToCallMap()) {
                return false;
            } else {
                this.mPreAddedCall = new PreAddedCall(sCallId, str, 9, i);
                return preProcessCall(2, this.mPreAddedCall);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0062 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean preProcessCall(int r8, ohos.dcall.CallAppAbilityConnection.PreAddedCall r9) {
        /*
        // Method dump skipped, instructions count: 122
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.CallAppAbilityConnection.preProcessCall(int, ohos.dcall.CallAppAbilityConnection$PreAddedCall):boolean");
    }

    /* access modifiers changed from: package-private */
    public boolean disconnectPreAddedCall(int i) {
        PreAddedCall preAddedCall = this.mPreAddedCall;
        if (preAddedCall == null || preAddedCall.getCallId() != i) {
            HiLog.error(LOG_LABEL, "disconnectPreAddedCall: no matched pre added call.", new Object[0]);
            return false;
        }
        Call callById = getCallById(Integer.valueOf(this.mPreAddedCall.getCallId()));
        if (callById != null) {
            HiLog.info(LOG_LABEL, "disconnectPreAddedCall: %{public}s", callById.toString());
            callById.disconnect();
        } else {
            HiLog.info(LOG_LABEL, "disconnectPreAddedCall: pending disconnect call, id is %{public}d.", Integer.valueOf(i));
            this.mPendingDisconnectPreAddedCall = this.mPreAddedCall;
            if (this.mHandler == null) {
                this.mHandler = createHandler();
            }
            CallAppAbilityConnnectionHandler callAppAbilityConnnectionHandler = this.mHandler;
            if (callAppAbilityConnnectionHandler == null) {
                HiLog.error(LOG_LABEL, "disconnectPreAddedCall: no handler.", new Object[0]);
                return false;
            } else if (callAppAbilityConnnectionHandler.hasInnerEvent(1001)) {
                HiLog.info(LOG_LABEL, "disconnectPreAddedCall: already call disconnect before, ignore this call.", new Object[0]);
                return true;
            } else {
                try {
                    this.mHandler.sendEvent(InnerEvent.get(1001, 0, null), DELAYED_TIME_DISCONNECT_PRE_ADDED_CALL, EventHandler.Priority.IMMEDIATE);
                } catch (IllegalArgumentException unused) {
                    HiLog.error(LOG_LABEL, "disconnectPreAddedCall: got IllegalArgumentException.", new Object[0]);
                }
            }
        }
        this.mPreAddedCall = null;
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean disconnectPendingDisconnectPreAddedCall() {
        PreAddedCall preAddedCall = this.mPendingDisconnectPreAddedCall;
        if (preAddedCall == null) {
            return true;
        }
        disconnectPreAddedCall(preAddedCall);
        this.mPendingDisconnectPreAddedCall = null;
        releaseResource();
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean disconnectPreAddedCall(PreAddedCall preAddedCall) {
        if (preAddedCall == null) {
            HiLog.info(LOG_LABEL, "disconnectPreAddedCall fail, no call.", new Object[0]);
            return false;
        }
        HiLog.info(LOG_LABEL, "disconnectPreAddedCall.", new Object[0]);
        preAddedCall.setState(7);
        preProcessCall(6, preAddedCall);
        preProcessCall(3, preAddedCall);
        preProcessCall(9, preAddedCall);
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean preConnectCallAppAbility(String str, String str2) {
        HiLog.info(LOG_LABEL, "preConnectCallAppAbility", new Object[0]);
        if (str == null) {
            HiLog.error(LOG_LABEL, "preConnectCallAppAbility: no call component name.", new Object[0]);
            return false;
        } else if (str2 == null) {
            HiLog.error(LOG_LABEL, "preConnectCallAppAbility: no call ability name.", new Object[0]);
            return false;
        } else {
            ComponentName componentName = new ComponentName(str, str2);
            CallAppAbilityInfo callAppAbilityInfo = this.mInfo;
            if (callAppAbilityInfo == null || !componentName.equals(callAppAbilityInfo.getComponentName())) {
                if (!this.mCallMapById.isEmpty()) {
                    HiLog.info(LOG_LABEL, "preConnectCallAppAbility: still has call.", new Object[0]);
                    return false;
                }
                disconnect();
                CallAppAbilityInfo callAppAbilityInfo2 = this.mInfo;
                if (callAppAbilityInfo2 != null) {
                    callAppAbilityInfo2.setComponentName(componentName);
                }
                connect();
            } else if (isConnected()) {
                HiLog.info(LOG_LABEL, "preConnectCallAppAbility: already connected.", new Object[0]);
            } else {
                connect();
            }
            this.mIsPreConnected = true;
            return true;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean preDisconnectCallAppAbility(String str, String str2) {
        HiLog.info(LOG_LABEL, "preDisconnectCallAppAbility", new Object[0]);
        if (str == null) {
            HiLog.error(LOG_LABEL, "preDisconnectCallAppAbility: no call component name.", new Object[0]);
            return false;
        } else if (str2 == null) {
            HiLog.error(LOG_LABEL, "preDisconnectCallAppAbility: no call ability name.", new Object[0]);
            return false;
        } else {
            ComponentName componentName = new ComponentName(str, str2);
            CallAppAbilityInfo callAppAbilityInfo = this.mInfo;
            if (callAppAbilityInfo == null || !componentName.equals(callAppAbilityInfo.getComponentName())) {
                HiLog.error(LOG_LABEL, "preDisconnectCallAppAbility, call component name is different.", new Object[0]);
                return false;
            }
            this.mIsPreConnected = false;
            if (this.mPreAddedCall == null && this.mPendingDisconnectPreAddedCall == null && this.mCallMapById.isEmpty()) {
                disconnect();
                releaseResource();
                return true;
            }
            HiLog.info(LOG_LABEL, "preDisconnectCallAppAbility, still has call.", new Object[0]);
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0075  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x007f  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0083  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean connectCallAppAbility(android.content.ComponentName r5) {
        /*
        // Method dump skipped, instructions count: 141
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.CallAppAbilityConnection.connectCallAppAbility(android.content.ComponentName):boolean");
    }

    /* access modifiers changed from: package-private */
    public void connectCallAppAbilityFail() {
        if (needTryAgain()) {
            HiLog.error(LOG_LABEL, "connectCallAppAbilityFail: retry.", new Object[0]);
            if (this.mHandler == null) {
                this.mHandler = createHandler();
            }
            if (this.mHandler != null) {
                try {
                    this.mHandler.sendEvent(InnerEvent.get(1000, 0, null), DELAYED_TIME_RETRY_CONNECT_INCALL_ABILITY, EventHandler.Priority.IMMEDIATE);
                } catch (IllegalArgumentException unused) {
                    HiLog.error(LOG_LABEL, "connectCallAppAbilityFail: got IllegalArgumentException.", new Object[0]);
                }
            }
        } else {
            HiLog.error(LOG_LABEL, "connectCallAppAbilityFail: reached max retry counts.", new Object[0]);
            disconnectLiveCalls();
            this.mRetryConnectCallAppAbilityCount = 0;
            HiLog.info(LOG_LABEL, "connectCallAppAbilityFail: mHasActiveBluetoothCall : %{public}b.", Boolean.valueOf(this.mHasActiveBluetoothCall));
            if (this.mHasActiveBluetoothCall) {
                this.mHasActiveBluetoothCall = false;
            } else {
                releaseResource();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void connectCallAppAbilitySuccess() {
        HiLog.info(LOG_LABEL, "connectCallAppAbilitySuccess", new Object[0]);
        this.mRetryConnectCallAppAbilityCount = 0;
    }

    /* access modifiers changed from: package-private */
    public void disconnectLiveCalls() {
        if (!this.mCallMapById.isEmpty()) {
            Iterator<Map.Entry<Integer, Call>> it = this.mCallMapById.entrySet().iterator();
            while (it != null) {
                try {
                    if (it.hasNext()) {
                        Call callById = getCallById(it.next().getKey());
                        if (!(callById == null || callById.getState() == 10 || callById.getState() == 7)) {
                            if (AospInCallService.isConnectedBluetoothCall(callById)) {
                                this.mHasActiveBluetoothCall = true;
                            } else {
                                HiLog.info(LOG_LABEL, "disconnectLiveCalls: %{public}s.", callById.toString());
                                callById.disconnect();
                                it.remove();
                            }
                        }
                    } else {
                        return;
                    }
                } catch (NoSuchElementException unused) {
                    HiLog.error(LOG_LABEL, "disconnectLiveCalls: An exception occurred when getting next element.", new Object[0]);
                    return;
                } catch (UnsupportedOperationException unused2) {
                    HiLog.error(LOG_LABEL, "disconnectLiveCalls: An unsupported operation exception occurred when removing element.", new Object[0]);
                    return;
                } catch (IllegalStateException unused3) {
                    HiLog.error(LOG_LABEL, "disconnectLiveCalls: An exception occurred when removing element.", new Object[0]);
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void disconnectCallAppAbility() {
        HiLog.info(LOG_LABEL, "disconnectCallAppAbility", new Object[0]);
        Context context = this.mContext;
        if (context == null) {
            HiLog.error(LOG_LABEL, "disconnectCallAppAbility: no context.", new Object[0]);
            return;
        }
        try {
            AbilityUtils.disconnectAbility(context, this.mServiceConnection);
        } catch (IllegalArgumentException unused) {
            HiLog.error(LOG_LABEL, "disconnectCallAppAbility got IllegalArgumentException.", new Object[0]);
        }
    }

    /* access modifiers changed from: package-private */
    public void addPendingCallToProxy(IBinder iBinder) {
        HiLog.info(LOG_LABEL, "addPendingCallToProxy", new Object[0]);
        this.mRemote = iBinder;
        processLiveCallWhenServiceConnected();
        CallAudioState callAudioState = this.mPendingCallAudioState;
        if (callAudioState != null) {
            HiLog.info(LOG_LABEL, "addPendingCallToProxy:onCallAudioStateChanged %{public}s", callAudioState.toString());
            onCallAudioStateChanged(this.mPendingCallAudioState);
            this.mPendingCallAudioState = null;
        }
        Boolean bool = this.mPendingCanAddCall;
        if (bool != null) {
            HiLog.info(LOG_LABEL, "addPendingCallToProxy:onCanAddCallChanged %{public}s", bool.toString());
            onCanAddCallChanged(this.mPendingCanAddCall.booleanValue());
            this.mPendingCanAddCall = null;
        }
    }

    /* access modifiers changed from: package-private */
    public void processLiveCallWhenServiceConnected() {
        if (!this.mCallMapById.isEmpty()) {
            for (Integer num : this.mCallMapById.keySet()) {
                Call callById = getCallById(num);
                if (!(callById == null || callById.getState() == 10 || callById.getState() == 7)) {
                    HiLog.info(LOG_LABEL, "processLiveCallWhenServiceConnected: still has call", new Object[0]);
                    addCall(callById);
                }
            }
            processCallAudioStateWhenServiceConnected();
            processCanAddCallWhenServiceConnected();
        }
    }

    /* access modifiers changed from: package-private */
    public void processCallAudioStateWhenServiceConnected() {
        CallAudioState callAudioState = this.mCurrentCallAudioState;
        if (callAudioState != null) {
            HiLog.info(LOG_LABEL, "processCallAudioStateWhenServiceConnected:onCallAudioStateChanged %{public}s", callAudioState.toString());
            onCallAudioStateChanged(this.mCurrentCallAudioState);
        }
    }

    /* access modifiers changed from: package-private */
    public void processCanAddCallWhenServiceConnected() {
        Boolean bool = this.mCurrentCanAddCall;
        if (bool != null) {
            HiLog.info(LOG_LABEL, "processCanAddCallWhenServiceConnected:onCanAddCallChanged %{public}s", bool.toString());
            onCanAddCallChanged(this.mCurrentCanAddCall.booleanValue());
        }
    }

    /* access modifiers changed from: package-private */
    public void processLiveCallWhenServiceDisconnected(ComponentName componentName) {
        if (hasLiveCalls()) {
            HiLog.info(LOG_LABEL, "processLiveCallWhenServiceDisconnected, reconnect.", new Object[0]);
            connect();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean hasLiveCalls() {
        if (this.mCallMapById.isEmpty()) {
            HiLog.error(LOG_LABEL, "hasLiveCalls: the map is empty.", new Object[0]);
            return false;
        }
        for (Integer num : this.mCallMapById.keySet()) {
            Call callById = getCallById(num);
            if (!(callById == null || callById.getState() == 10 || callById.getState() == 7)) {
                HiLog.info(LOG_LABEL, "hasLiveCalls.", new Object[0]);
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:6|(4:7|8|9|10)|14|15|16) */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0043, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0057, code lost:
        r3.recycle();
        r2.recycle();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x005d, code lost:
        throw r8;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x0046 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onCallRemoved(android.telecom.Call r9) {
        /*
        // Method dump skipped, instructions count: 102
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.CallAppAbilityConnection.onCallRemoved(android.telecom.Call):boolean");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(9:4|5|6|7|8|12|13|14|15) */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x005a, code lost:
        r2.recycle();
        r0.recycle();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0060, code lost:
        throw r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0046, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0049 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onCanAddCallChanged(boolean r7) {
        /*
            r6 = this;
            java.lang.Boolean r0 = new java.lang.Boolean
            r0.<init>(r7)
            r6.mCurrentCanAddCall = r0
            android.os.IBinder r0 = r6.mRemote
            r1 = 0
            if (r0 != 0) goto L_0x001e
            ohos.hiviewdfx.HiLogLabel r0 = ohos.dcall.CallAppAbilityConnection.LOG_LABEL
            java.lang.Object[] r2 = new java.lang.Object[r1]
            java.lang.String r3 = "onCanAddCallChanged fail, no remote."
            ohos.hiviewdfx.HiLog.error(r0, r3, r2)
            java.lang.Boolean r0 = new java.lang.Boolean
            r0.<init>(r7)
            r6.mPendingCanAddCall = r0
            return r1
        L_0x001e:
            android.os.Parcel r0 = android.os.Parcel.obtain()
            android.os.Parcel r2 = android.os.Parcel.obtain()
            r3 = 1
            java.lang.String r4 = "OHOS.Dcall.DistributedCallAbility"
            r0.writeInterfaceToken(r4)     // Catch:{ RemoteException -> 0x0048 }
            r0.writeBoolean(r7)     // Catch:{ RemoteException -> 0x0048 }
            android.os.IBinder r6 = r6.mRemote     // Catch:{ RemoteException -> 0x0048 }
            r4 = 4
            r6.transact(r4, r0, r2, r1)     // Catch:{ RemoteException -> 0x0048 }
            ohos.hiviewdfx.HiLogLabel r6 = ohos.dcall.CallAppAbilityConnection.LOG_LABEL     // Catch:{ RemoteException -> 0x0049 }
            java.lang.String r4 = "onCanAddCallChanged: %{public}b."
            java.lang.Object[] r5 = new java.lang.Object[r3]     // Catch:{ RemoteException -> 0x0049 }
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r7)     // Catch:{ RemoteException -> 0x0049 }
            r5[r1] = r7     // Catch:{ RemoteException -> 0x0049 }
            ohos.hiviewdfx.HiLog.info(r6, r4, r5)     // Catch:{ RemoteException -> 0x0049 }
            goto L_0x0053
        L_0x0046:
            r6 = move-exception
            goto L_0x005a
        L_0x0048:
            r3 = r1
        L_0x0049:
            ohos.hiviewdfx.HiLogLabel r6 = ohos.dcall.CallAppAbilityConnection.LOG_LABEL     // Catch:{ all -> 0x0046 }
            java.lang.String r7 = "onCanAddCallChanged got RemoteException."
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x0046 }
            ohos.hiviewdfx.HiLog.error(r6, r7, r1)     // Catch:{ all -> 0x0046 }
        L_0x0053:
            r2.recycle()
            r0.recycle()
            return r3
        L_0x005a:
            r2.recycle()
            r0.recycle()
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.CallAppAbilityConnection.onCanAddCallChanged(boolean):boolean");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0032 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onSilenceRinger() {
        /*
            r6 = this;
            android.os.IBinder r0 = r6.mRemote
            r1 = 0
            if (r0 != 0) goto L_0x0010
            ohos.hiviewdfx.HiLogLabel r6 = ohos.dcall.CallAppAbilityConnection.LOG_LABEL
            java.lang.Object[] r0 = new java.lang.Object[r1]
            java.lang.String r2 = "onSilenceRinger fail, no remote."
            ohos.hiviewdfx.HiLog.error(r6, r2, r0)
            return r1
        L_0x0010:
            android.os.Parcel r0 = android.os.Parcel.obtain()
            android.os.Parcel r2 = android.os.Parcel.obtain()
            java.lang.String r3 = "OHOS.Dcall.DistributedCallAbility"
            r0.writeInterfaceToken(r3)     // Catch:{ RemoteException -> 0x0031 }
            android.os.IBinder r6 = r6.mRemote     // Catch:{ RemoteException -> 0x0031 }
            r3 = 5
            r6.transact(r3, r0, r2, r1)     // Catch:{ RemoteException -> 0x0031 }
            r6 = 1
            ohos.hiviewdfx.HiLogLabel r3 = ohos.dcall.CallAppAbilityConnection.LOG_LABEL     // Catch:{ RemoteException -> 0x0032 }
            java.lang.String r4 = "onSilenceRinger."
            java.lang.Object[] r5 = new java.lang.Object[r1]     // Catch:{ RemoteException -> 0x0032 }
            ohos.hiviewdfx.HiLog.info(r3, r4, r5)     // Catch:{ RemoteException -> 0x0032 }
            goto L_0x003c
        L_0x002f:
            r6 = move-exception
            goto L_0x0043
        L_0x0031:
            r6 = r1
        L_0x0032:
            ohos.hiviewdfx.HiLogLabel r3 = ohos.dcall.CallAppAbilityConnection.LOG_LABEL     // Catch:{ all -> 0x002f }
            java.lang.String r4 = "onSilenceRinger got RemoteException."
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ all -> 0x002f }
            ohos.hiviewdfx.HiLog.error(r3, r4, r1)     // Catch:{ all -> 0x002f }
        L_0x003c:
            r2.recycle()
            r0.recycle()
            return r6
        L_0x0043:
            r2.recycle()
            r0.recycle()
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.CallAppAbilityConnection.onSilenceRinger():boolean");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(6:5|(4:6|7|8|9)|13|14|15|20) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0043, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0058, code lost:
        r1.recycle();
        r7.recycle();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x005e, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0046 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onStateChanged(android.telecom.Call r6, int r7) {
        /*
        // Method dump skipped, instructions count: 106
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.CallAppAbilityConnection.onStateChanged(android.telecom.Call, int):boolean");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(6:5|(4:6|7|8|9)|13|14|15|20) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0043, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0058, code lost:
        r1.recycle();
        r7.recycle();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x005e, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0046 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onDetailsChanged(android.telecom.Call r6, android.telecom.Call.Details r7) {
        /*
        // Method dump skipped, instructions count: 106
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.CallAppAbilityConnection.onDetailsChanged(android.telecom.Call, android.telecom.Call$Details):boolean");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(6:5|(4:6|7|8|9)|13|14|15|20) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0047, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x005c, code lost:
        r2.recycle();
        r0.recycle();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0062, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x004a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onPostDialWait(android.telecom.Call r6, java.lang.String r7) {
        /*
        // Method dump skipped, instructions count: 110
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.CallAppAbilityConnection.onPostDialWait(android.telecom.Call, java.lang.String):boolean");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't wrap try/catch for region: R(6:5|(4:6|7|8|9)|13|14|15|20) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0044, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0059, code lost:
        r2.recycle();
        r0.recycle();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x005f, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0047 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onCallDestroyed(android.telecom.Call r7) {
        /*
        // Method dump skipped, instructions count: 107
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.CallAppAbilityConnection.onCallDestroyed(android.telecom.Call):boolean");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x004d */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onConnectionEvent(android.telecom.Call r6, java.lang.String r7, android.os.Bundle r8) {
        /*
        // Method dump skipped, instructions count: 113
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.dcall.CallAppAbilityConnection.onConnectionEvent(android.telecom.Call, java.lang.String, android.os.Bundle):boolean");
    }

    /* access modifiers changed from: package-private */
    public Integer getIdByCall(Call call) {
        if (call == null) {
            HiLog.info(LOG_LABEL, "getIdByCall fail, call is null.", new Object[0]);
            return INVALID_KEY_SET_IN_MAP;
        }
        Integer num = INVALID_KEY_SET_IN_MAP;
        Iterator<Integer> it = this.mCallMapById.keySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Integer next = it.next();
            if (call.equals(getCallById(next))) {
                num = next;
                break;
            }
        }
        if (num == INVALID_KEY_SET_IN_MAP) {
            HiLog.error(LOG_LABEL, "getIdByCall fail, call is not in map.", new Object[0]);
        }
        return num;
    }

    /* access modifiers changed from: package-private */
    public Call getCallById(Integer num) {
        return this.mCallMapById.get(num);
    }

    /* access modifiers changed from: package-private */
    public void addCallToCallMap(Call call) {
        if (call != null && getIdByCall(call) == INVALID_KEY_SET_IN_MAP) {
            HiLog.info(LOG_LABEL, "add call to map.", new Object[0]);
            this.mCallMapById.put(Integer.valueOf(sCallId), call);
            sCallId++;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean canAddCallToCallMap() {
        if (this.mCallMapById.size() < 32) {
            return true;
        }
        HiLog.error(LOG_LABEL, "canAddCallToCallMap: reached the maximum number of calls.", new Object[0]);
        return false;
    }

    /* access modifiers changed from: package-private */
    public void removeCallFromCallMap(Call call) {
        Integer idByCall = getIdByCall(call);
        if (idByCall != INVALID_KEY_SET_IN_MAP) {
            HiLog.info(LOG_LABEL, "removeCallFromCallMap success.", new Object[0]);
            this.mCallMapById.remove(idByCall);
            return;
        }
        HiLog.error(LOG_LABEL, "removeCallFromCallMap fail, call is not in map.", new Object[0]);
    }

    /* access modifiers changed from: package-private */
    public class PreAddedCall {
        private int mCallId;
        private String mNumber;
        private int mState;
        private int mVideoState;

        PreAddedCall(int i, String str, int i2, int i3) {
            this.mCallId = i;
            this.mNumber = str;
            this.mState = i2;
            this.mVideoState = i3;
        }

        /* access modifiers changed from: package-private */
        public int getCallId() {
            return this.mCallId;
        }

        /* access modifiers changed from: package-private */
        public int getState() {
            return this.mState;
        }

        /* access modifiers changed from: package-private */
        public void setState(int i) {
            this.mState = i;
        }

        /* access modifiers changed from: package-private */
        public String getNumber() {
            return this.mNumber;
        }

        /* access modifiers changed from: package-private */
        public int getVideoState() {
            return this.mVideoState;
        }
    }

    /* access modifiers changed from: private */
    public class CallAppAbilityConnnectionHandler extends EventHandler {
        private CallAppAbilityConnnectionHandler(EventRunner eventRunner) {
            super(eventRunner);
        }

        @Override // ohos.eventhandler.EventHandler
        public void processEvent(InnerEvent innerEvent) {
            super.processEvent(innerEvent);
            if (innerEvent != null) {
                HiLog.info(CallAppAbilityConnection.LOG_LABEL, "processEvent, eventId %{public}d.", Integer.valueOf(innerEvent.eventId));
                int i = innerEvent.eventId;
                if (i == 1000) {
                    HiLog.info(CallAppAbilityConnection.LOG_LABEL, "retry connect to call app ability.", new Object[0]);
                    CallAppAbilityConnection.this.connect();
                } else if (i == 1001 && CallAppAbilityConnection.this.mPendingDisconnectPreAddedCall != null) {
                    HiLog.info(CallAppAbilityConnection.LOG_LABEL, "disconnect pending disconnect pre added call.", new Object[0]);
                    CallAppAbilityConnection callAppAbilityConnection = CallAppAbilityConnection.this;
                    callAppAbilityConnection.disconnectPreAddedCall(callAppAbilityConnection.mPendingDisconnectPreAddedCall);
                    CallAppAbilityConnection.this.mPendingDisconnectPreAddedCall = null;
                    CallAppAbilityConnection.this.releaseResource();
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public CallAppAbilityConnnectionHandler createHandler() {
        EventRunner create = EventRunner.create();
        if (create != null) {
            return new CallAppAbilityConnnectionHandler(create);
        }
        HiLog.error(LOG_LABEL, "createHandler: no runner.", new Object[0]);
        return null;
    }

    /* access modifiers changed from: package-private */
    public void destroyHandler() {
        CallAppAbilityConnnectionHandler callAppAbilityConnnectionHandler = this.mHandler;
        if (callAppAbilityConnnectionHandler != null) {
            callAppAbilityConnnectionHandler.removeAllEvent();
            this.mHandler = null;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean needTryAgain() {
        boolean z = this.mRetryConnectCallAppAbilityCount < 6;
        this.mRetryConnectCallAppAbilityCount++;
        return z;
    }

    private void registerSystemReadyCallback(SystemReadyCallbackSkeleton systemReadyCallbackSkeleton) {
        int registerSystemReadyCallback = SysAbilityManager.registerSystemReadyCallback(systemReadyCallbackSkeleton);
        HiLog.info(LOG_LABEL, "registerSystemReadyCallback: registerSystemReadyCallback result: %{public}d", Integer.valueOf(registerSystemReadyCallback));
    }
}
