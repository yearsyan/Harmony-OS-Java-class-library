package ohos.ai.profile.sa;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import ohos.ai.profile.sa.util.AuthorityUtil;
import ohos.ai.profile.sa.util.LogUtil;
import ohos.ai.profile.sa.util.SensitiveUtil;
import ohos.rpc.IPCAdapter;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;

public class DeviceProfileSystemAbility extends RemoteObject implements IRemoteBroker {
    private static final int COMMAND_CONNECT = 1;
    private static final int COMMAND_DISCONNECT = 2;
    private static final int DEATH_RECIPIENT_FLAG = 0;
    private static final String DESCRIPTOR = "DeviceProfileSystemAbility";
    private static final int FAIL = 0;
    private static final String JOINT_MARK = ":";
    private static final long PROFILE_BIND_SERVICE_TIMEOUT = 10000;
    private static final String PROFILE_PACKAGE_NAME = "com.huawei.profile";
    private static final String PROFILE_SERVICE_CLASS_NAME = "com.huawei.profile.harmonyadapter.HarmonyProfileService";
    private static final int SUCCESS = 1;
    private static final String TAG = "DeviceProfileSystemAbility";
    private static DeviceProfileSystemAbility instance;
    private Map<String, Integer> bindServiceManifests = new HashMap();
    private IBinder binder;
    private ServiceConnection connection;
    private Context context;

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this;
    }

    private DeviceProfileSystemAbility(Context context2) {
        super("DeviceProfileSystemAbility");
        this.context = context2;
    }

    static synchronized DeviceProfileSystemAbility getInstance(Context context2) {
        DeviceProfileSystemAbility deviceProfileSystemAbility;
        synchronized (DeviceProfileSystemAbility.class) {
            if (instance == null) {
                instance = new DeviceProfileSystemAbility(context2);
            }
            deviceProfileSystemAbility = instance;
        }
        return deviceProfileSystemAbility;
    }

    @Override // ohos.rpc.RemoteObject
    public boolean onRemoteRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException {
        boolean z;
        int i2 = 0;
        boolean z2 = false;
        LogUtil.info("DeviceProfileSystemAbility", "OnRemoteRequest begin, code = " + i, new Object[0]);
        if (messageParcel == null || messageParcel2 == null) {
            LogUtil.error("DeviceProfileSystemAbility", "Data and reply cannot be null.", new Object[0]);
            return false;
        }
        String readInterfaceToken = messageParcel.readInterfaceToken();
        if (!"DeviceProfileSystemAbility".equals(readInterfaceToken)) {
            LogUtil.info("DeviceProfileSystemAbility", "Interface token does not match, token = " + readInterfaceToken, new Object[0]);
            return false;
        }
        String readString = messageParcel.readString();
        String readString2 = messageParcel.readString();
        if (!AuthorityUtil.checkAuthority(this.context)) {
            LogUtil.error("DeviceProfileSystemAbility", "Calling package has no authority, package name is " + readString, new Object[0]);
            return false;
        } else if (i == 1) {
            try {
                z2 = connectService(readString, readString2, getCallingPid(), messageParcel.readRemoteObject());
            } catch (Throwable th) {
                LogUtil.error("DeviceProfileSystemAbility", "Connect service error, caused by " + SensitiveUtil.getMessage(th), new Object[0]);
            }
            messageParcel2.writeNoException();
            Optional<IRemoteObject> translateToIRemoteObject = translateToIRemoteObject();
            messageParcel2.writeRemoteObject((!z2 || !translateToIRemoteObject.isPresent()) ? null : translateToIRemoteObject.get());
            return true;
        } else if (i != 2) {
            LogUtil.warn("DeviceProfileSystemAbility", "Request code does not support.", new Object[0]);
            return super.onRemoteRequest(i, messageParcel, messageParcel2, messageOption);
        } else {
            try {
                z = disconnectService(readString, readString2, getCallingPid());
            } catch (Throwable th2) {
                LogUtil.error("DeviceProfileSystemAbility", "Disconnect service error, caused by " + SensitiveUtil.getMessage(th2), new Object[0]);
                z = false;
            }
            messageParcel2.writeNoException();
            if (z) {
                i2 = 1;
            }
            messageParcel2.writeInt(i2);
            return true;
        }
    }

    private synchronized boolean connectService(String str, String str2, int i, IRemoteObject iRemoteObject) {
        markCallInterface("connectService", str, str2, i);
        if (hasOtherProcessConnected(str, str2, i, iRemoteObject)) {
            return true;
        }
        final CompletableFuture completableFuture = new CompletableFuture();
        this.connection = new ServiceConnection() {
            /* class ohos.ai.profile.sa.DeviceProfileSystemAbility.AnonymousClass1 */

            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                LogUtil.info("DeviceProfileSystemAbility", "Device profile service has connected.", new Object[0]);
                completableFuture.complete(iBinder);
            }

            public void onServiceDisconnected(ComponentName componentName) {
                LogUtil.info("DeviceProfileSystemAbility", "Device profile service has disconnected.", new Object[0]);
                DeviceProfileSystemAbility.this.binder = null;
            }
        };
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(PROFILE_PACKAGE_NAME, PROFILE_SERVICE_CLASS_NAME));
        try {
            boolean bindService = this.context.bindService(intent, this.connection, 1);
            LogUtil.info("DeviceProfileSystemAbility", "Bind device profile service result is " + bindService, new Object[0]);
            if (bindService) {
                this.binder = (IBinder) completableFuture.get(PROFILE_BIND_SERVICE_TIMEOUT, TimeUnit.MILLISECONDS);
                addServerDeathRecipient();
                addStubDeathRecipient(str, str2, i, iRemoteObject);
                this.bindServiceManifests.put(str + JOINT_MARK + str2, Integer.valueOf(i));
                return true;
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LogUtil.error("DeviceProfileSystemAbility", "Bind device profile service error, caused by " + SensitiveUtil.getMessage(e), new Object[0]);
            disconnectService(str, str2, i);
        }
        this.connection = null;
        return false;
    }

    private synchronized boolean disconnectService(String str, String str2, int i) {
        markCallInterface("disconnectService", str, str2, i);
        String str3 = str + JOINT_MARK + str2;
        if (this.bindServiceManifests.containsKey(str3)) {
            if (this.bindServiceManifests.get(str3).intValue() == i) {
                this.bindServiceManifests.remove(str3);
                if (!this.bindServiceManifests.isEmpty()) {
                    LogUtil.info("DeviceProfileSystemAbility", "Other process still connected device profile service, don't have to disconnect.", new Object[0]);
                    return true;
                }
                try {
                    this.context.unbindService(this.connection);
                    this.connection = null;
                    this.binder = null;
                    return true;
                } catch (RuntimeException e) {
                    LogUtil.error("DeviceProfileSystemAbility", "Unbind device profile service error, caused by " + SensitiveUtil.getMessage(e), new Object[0]);
                    this.connection = null;
                    this.binder = null;
                    return false;
                }
            }
        }
        LogUtil.warn("DeviceProfileSystemAbility", "Has not connected device profile service, don't have to disconnect.", new Object[0]);
        return false;
    }

    private boolean hasOtherProcessConnected(String str, String str2, int i, IRemoteObject iRemoteObject) {
        if (this.bindServiceManifests.isEmpty()) {
            return false;
        }
        LogUtil.info("DeviceProfileSystemAbility", "Has connected device profile service, don't need to connect again.", new Object[0]);
        addStubDeathRecipient(str, str2, i, iRemoteObject);
        this.bindServiceManifests.put(str + JOINT_MARK + str2, Integer.valueOf(i));
        return true;
    }

    private void addServerDeathRecipient() {
        try {
            this.binder.linkToDeath(new IBinder.DeathRecipient() {
                /* class ohos.ai.profile.sa.$$Lambda$DeviceProfileSystemAbility$mr_MAvVx56bfd4zGCzkCIFqxH98 */

                public final void binderDied() {
                    DeviceProfileSystemAbility.this.lambda$addServerDeathRecipient$0$DeviceProfileSystemAbility();
                }
            }, 0);
        } catch (android.os.RemoteException unused) {
            LogUtil.warn("DeviceProfileSystemAbility", "bind service process has died.", new Object[0]);
        }
    }

    public /* synthetic */ void lambda$addServerDeathRecipient$0$DeviceProfileSystemAbility() {
        LogUtil.warn("DeviceProfileSystemAbility", "Device profile service process has died.", new Object[0]);
        synchronized (this) {
            this.connection = null;
            this.binder = null;
            this.bindServiceManifests.clear();
        }
    }

    private void addStubDeathRecipient(String str, String str2, int i, IRemoteObject iRemoteObject) {
        iRemoteObject.addDeathRecipient(new IRemoteObject.DeathRecipient(str, str2, i) {
            /* class ohos.ai.profile.sa.$$Lambda$DeviceProfileSystemAbility$T01p5hKL7XcDb71RanRFGWPVMs0 */
            private final /* synthetic */ String f$1;
            private final /* synthetic */ String f$2;
            private final /* synthetic */ int f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // ohos.rpc.IRemoteObject.DeathRecipient
            public final void onRemoteDied() {
                DeviceProfileSystemAbility.this.lambda$addStubDeathRecipient$1$DeviceProfileSystemAbility(this.f$1, this.f$2, this.f$3);
            }
        }, 0);
    }

    public /* synthetic */ void lambda$addStubDeathRecipient$1$DeviceProfileSystemAbility(String str, String str2, int i) {
        LogUtil.warn("DeviceProfileSystemAbility", "Bind service process has died.", new Object[0]);
        disconnectService(str, str2, i);
    }

    private Optional<IRemoteObject> translateToIRemoteObject() {
        IBinder iBinder = this.binder;
        if (iBinder == null) {
            return Optional.empty();
        }
        return IPCAdapter.translateToIRemoteObject(iBinder);
    }

    private void markCallInterface(String str, String str2, String str3, int i) {
        LogUtil.info("DeviceProfileSystemAbility", String.format(Locale.ENGLISH, "Start to call %s function, packageName is %s, processName is %s, callingPid %d", str, str2, str3, Integer.valueOf(i)), new Object[0]);
    }
}
