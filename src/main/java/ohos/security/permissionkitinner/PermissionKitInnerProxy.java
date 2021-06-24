package ohos.security.permissionkitinner;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.security.permission.infrastructure.utils.HiLogLabelUtil;
import ohos.sysability.samgr.SysAbilityManager;

/* access modifiers changed from: package-private */
public class PermissionKitInnerProxy implements IPermissionKitInner {
    private static final int CAN_REQUEST_PERMISSION = 65283;
    private static final String DESCRIPTOR = "ohos.security.dpermission.IDPermissionDistributeService";
    private static final Object INSTANCE_LOCK = new Object();
    private static final HiLogLabel LABEL = HiLogLabelUtil.INNER_KIT.newHiLogLabel(TAG);
    private static final int PERMISSION_LOCAL_SERVICE_ID = 3502;
    private static final String TAG = "PermissionKitInnerProxy";
    private static volatile PermissionKitInnerProxy sInstance = null;
    private IRemoteObject.DeathRecipient mDeathRecipient;
    private final Object mLock;
    private IRemoteObject mPermissionKitInnerRemoteService;

    private PermissionKitInnerProxy() {
        this.mLock = new Object();
        this.mPermissionKitInnerRemoteService = null;
        this.mDeathRecipient = null;
        this.mDeathRecipient = new IRemoteObject.DeathRecipient() {
            /* class ohos.security.permissionkitinner.PermissionKitInnerProxy.AnonymousClass1 */

            @Override // ohos.rpc.IRemoteObject.DeathRecipient
            public void onRemoteDied() {
                HiLog.warn(PermissionKitInnerProxy.LABEL, "PermissionKitInnerProxy Received onRemoteDied Notify.", new Object[0]);
                synchronized (PermissionKitInnerProxy.this.mLock) {
                    PermissionKitInnerProxy.this.mPermissionKitInnerRemoteService = null;
                }
            }
        };
    }

    public static PermissionKitInnerProxy getInstance() {
        if (sInstance == null) {
            synchronized (INSTANCE_LOCK) {
                if (sInstance == null) {
                    sInstance = new PermissionKitInnerProxy();
                }
            }
        }
        return sInstance;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        synchronized (this.mLock) {
            if (this.mPermissionKitInnerRemoteService != null) {
                return this.mPermissionKitInnerRemoteService;
            }
            this.mPermissionKitInnerRemoteService = SysAbilityManager.getSysAbility(PERMISSION_LOCAL_SERVICE_ID);
            if (this.mPermissionKitInnerRemoteService != null) {
                this.mPermissionKitInnerRemoteService.addDeathRecipient(this.mDeathRecipient, 0);
            } else {
                HiLog.error(LABEL, "PermissionKitInnerProxy getSysAbility %{public}d failed.", Integer.valueOf((int) PERMISSION_LOCAL_SERVICE_ID));
            }
            return this.mPermissionKitInnerRemoteService;
        }
    }

    @Override // ohos.security.permissionkitinner.IPermissionKitInner
    public boolean canRequestPermission(String str, String str2, int i) throws RemoteException {
        if (str == null) {
            HiLog.error(LABEL, "PermissionKitInnerProxy canRequestPermission permissionName is null", new Object[0]);
            return false;
        } else if (str2 == null) {
            HiLog.error(LABEL, "PermissionKitInnerProxy canRequestPermission packageName is null", new Object[0]);
            return false;
        } else {
            IRemoteObject asObject = asObject();
            if (asObject != null) {
                MessageParcel create = MessageParcel.create();
                MessageParcel create2 = MessageParcel.create();
                try {
                    create.writeInterfaceToken(DESCRIPTOR);
                    create.writeString(str);
                    create.writeString(str2);
                    create.writeInt(i);
                    asObject.sendRequest(65283, create, create2, new MessageOption());
                    boolean readBoolean = create2.readBoolean();
                    HiLog.debug(LABEL, "PermissionKitInnerProxy canRequestPermission replyData is %{public}s", Boolean.toString(readBoolean));
                    create.reclaim();
                    create2.reclaim();
                    return readBoolean;
                } catch (RemoteException e) {
                    HiLog.error(LABEL, "PermissionKitInnerProxy Failed to canRequestPermission", new Object[0]);
                    throw e;
                } catch (Throwable th) {
                    create.reclaim();
                    create2.reclaim();
                    throw th;
                }
            } else {
                HiLog.error(LABEL, "PermissionKitInnerProxy Failed to get remote object for canRequestPermission", new Object[0]);
                throw new RemoteException();
            }
        }
    }
}
