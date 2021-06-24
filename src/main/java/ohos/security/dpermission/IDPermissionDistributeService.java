package ohos.security.dpermission;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IPCSkeleton;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;
import ohos.security.permission.adapter.ActivityManagerAdapter;
import ohos.security.permission.infrastructure.utils.GzipUtil;
import ohos.security.permission.infrastructure.utils.HiLogLabelUtil;

interface IDPermissionDistributeService extends IRemoteBroker {
    public static final int TRANSACT_CODE_CAN_REQUEST_PERMISSION = 65283;
    public static final int TRANSACT_CODE_EXECUTE_REMOTE_COMMAND = 65281;
    public static final int TRANSACT_CODE_EXECUTE_REMOTE_COMMAND_BACKTRACK = 65282;
    public static final int TRANSACT_CODE_GET_PERMISSION_USAGE_INFO = 65285;
    public static final int TRANSACT_CODE_NOTIFY_UID_PERMISSION_CHANGED = 65284;

    boolean canRequestPermission(String str, String str2, int i) throws RemoteException;

    String executeRemoteCommand(String str, String str2) throws RemoteException;

    String executeRemoteCommandBacktrack(String str, String str2, String str3) throws RemoteException;

    String getPermissionUsagesInfo(String str, String[] strArr) throws RemoteException;

    int notifyUidPermissionChanged(int i) throws RemoteException;

    public static abstract class Stub extends RemoteObject implements IDPermissionDistributeService {
        private static final int AID_ROOT = 0;
        private static final int AID_SYSTEM = 1000;
        private static final String DESCRIPTOR = "ohos.security.dpermission.IDPermissionDistributeService";
        private static final HiLogLabel LABEL = HiLogLabelUtil.SERVICE.newHiLogLabel(TAG);
        private static final String MANAGE_DISTRIBUTED_PERMISSION = "com.huawei.permission.MANAGE_DISTRIBUTED_PERMISSION";
        private static final int PER_USER_RANGE = 100000;
        private static final String TAG = "IDPermissionDistributeService.Stub";

        @Override // ohos.rpc.IRemoteBroker
        public IRemoteObject asObject() {
            return this;
        }

        Stub() {
            super(DESCRIPTOR);
        }

        static IDPermissionDistributeService asInterface(IRemoteObject iRemoteObject) {
            Objects.requireNonNull(iRemoteObject, "remote object is null when as interface");
            return new Proxy(iRemoteObject);
        }

        @Override // ohos.rpc.RemoteObject
        public boolean onRemoteRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException {
            if (messageParcel == null || messageParcel2 == null) {
                HiLog.error(LABEL, "onRemoteRequest: data or reply is null", new Object[0]);
                return false;
            } else if (!DESCRIPTOR.equals(messageParcel.readInterfaceToken())) {
                HiLog.error(LABEL, "onRemoteRequest: interface descriptor check fail", new Object[0]);
                return false;
            } else {
                switch (i) {
                    case IDPermissionDistributeService.TRANSACT_CODE_EXECUTE_REMOTE_COMMAND /*{ENCODED_INT: 65281}*/:
                        return executeRemoteCommandInner(messageParcel, messageParcel2);
                    case IDPermissionDistributeService.TRANSACT_CODE_EXECUTE_REMOTE_COMMAND_BACKTRACK /*{ENCODED_INT: 65282}*/:
                        return executeRemoteCommandBacktrackInner(messageParcel, messageParcel2);
                    case IDPermissionDistributeService.TRANSACT_CODE_CAN_REQUEST_PERMISSION /*{ENCODED_INT: 65283}*/:
                        return canRequestPermissionInner(messageParcel, messageParcel2);
                    case IDPermissionDistributeService.TRANSACT_CODE_NOTIFY_UID_PERMISSION_CHANGED /*{ENCODED_INT: 65284}*/:
                        return notifyUidPermissionChangedInner(messageParcel, messageParcel2);
                    case IDPermissionDistributeService.TRANSACT_CODE_GET_PERMISSION_USAGE_INFO /*{ENCODED_INT: 65285}*/:
                        return getPermissionUsagesInfoInner(messageParcel, messageParcel2);
                    default:
                        return super.onRemoteRequest(i, messageParcel, messageParcel2, messageOption);
                }
            }
        }

        private boolean executeRemoteCommandInner(MessageParcel messageParcel, MessageParcel messageParcel2) throws RemoteException {
            if (!hasApiPermission()) {
                return false;
            }
            String readString = messageParcel.readString();
            int readInt = messageParcel.readInt();
            byte[] readByteArray = messageParcel.readByteArray();
            byte[] inflater = GzipUtil.inflater(readByteArray);
            String str = new String(inflater, StandardCharsets.UTF_8);
            HiLog.debug(LABEL, "executeRemoteCommandInner: begin, command %{public}s, expect %{public}d, compressed %{public}d, actual %{public}d", readString, Integer.valueOf(readInt), Integer.valueOf(readByteArray.length), Integer.valueOf(inflater.length));
            String executeRemoteCommand = executeRemoteCommand(readString, str);
            messageParcel2.writeInt(200);
            byte[] bytes = executeRemoteCommand.getBytes(StandardCharsets.UTF_8);
            byte[] deflater = GzipUtil.deflater(bytes);
            messageParcel2.writeInt(bytes.length);
            messageParcel2.writeByteArray(deflater);
            HiLog.debug(LABEL, "executeRemoteCommandInner: done, command %{public}s, raw %{public}d, compressed %{public}d", readString, Integer.valueOf(bytes.length), Integer.valueOf(deflater.length));
            return true;
        }

        private boolean executeRemoteCommandBacktrackInner(MessageParcel messageParcel, MessageParcel messageParcel2) throws RemoteException {
            if (!hasApiPermission()) {
                return false;
            }
            HiLog.debug(LABEL, "executeRemoteCommandBacktrack: called", new Object[0]);
            messageParcel2.writeString(executeRemoteCommandBacktrack(messageParcel.readString(), messageParcel.readString(), messageParcel.readString()));
            HiLog.debug(LABEL, "executeRemoteCommandBacktrack: done", new Object[0]);
            return true;
        }

        private boolean canRequestPermissionInner(MessageParcel messageParcel, MessageParcel messageParcel2) throws RemoteException {
            if (messageParcel == null || messageParcel2 == null) {
                HiLog.error(LABEL, "canRequestPermissionInner data or reply null!", new Object[0]);
                return false;
            } else if (messageParcel2.writeBoolean(canRequestPermission(messageParcel.readString(), messageParcel.readString(), messageParcel.readInt()))) {
                return true;
            } else {
                HiLog.error(LABEL, "canRequestPermissionInner write return failed!", new Object[0]);
                return true;
            }
        }

        private boolean notifyUidPermissionChangedInner(MessageParcel messageParcel, MessageParcel messageParcel2) throws RemoteException {
            if (!hasApiPermission()) {
                return false;
            }
            HiLog.debug(LABEL, "notifyUidPermissionChangedInner: called", new Object[0]);
            messageParcel2.writeInt(notifyUidPermissionChanged(messageParcel.readInt()));
            return true;
        }

        private boolean getPermissionUsagesInfoInner(MessageParcel messageParcel, MessageParcel messageParcel2) throws RemoteException {
            HiLog.debug(LABEL, "getPermissionUsagesInfoInner: called", new Object[0]);
            messageParcel2.writeString(getPermissionUsagesInfo(messageParcel.readString(), messageParcel.readStringArray()));
            return true;
        }

        private boolean hasApiPermission() {
            int callingPid = IPCSkeleton.getCallingPid();
            int callingUid = IPCSkeleton.getCallingUid();
            boolean z = true;
            HiLog.debug(LABEL, "hasApiPermission pid = %{public}d, uid = %{public}d", Integer.valueOf(callingPid), Integer.valueOf(callingUid));
            int i = callingUid % 100000;
            if (i == 0 || i == 1000) {
                HiLog.debug(LABEL, "hasApiPermission root or system user is granted", new Object[0]);
                return true;
            }
            if (ActivityManagerAdapter.getInstance().checkPermission("com.huawei.permission.MANAGE_DISTRIBUTED_PERMISSION", callingPid, callingUid) != 0) {
                z = false;
            }
            if (!z) {
                HiLog.debug(LABEL, "hasApiPermission no permission!", new Object[0]);
            }
            return z;
        }

        private static class Proxy implements IDPermissionDistributeService {
            private static final int GENERAL_ERROR = -1;
            private final IRemoteObject remote;

            Proxy(IRemoteObject iRemoteObject) {
                this.remote = iRemoteObject;
            }

            @Override // ohos.security.dpermission.IDPermissionDistributeService
            public String executeRemoteCommand(String str, String str2) throws RemoteException {
                MessageParcel create = MessageParcel.create();
                MessageParcel create2 = MessageParcel.create();
                try {
                    create.writeInterfaceToken(Stub.DESCRIPTOR);
                    create.writeString(str);
                    byte[] bytes = str2.getBytes(StandardCharsets.UTF_8);
                    byte[] deflater = GzipUtil.deflater(bytes);
                    create.writeInt(bytes.length);
                    create.writeByteArray(deflater);
                    HiLog.info(Stub.LABEL, "executeRemoteCommand: begin, command %{public}s, raw %{public}d, compressed %{public}d", str, Integer.valueOf(bytes.length), Integer.valueOf(deflater.length));
                    if (!this.remote.sendRequest(IDPermissionDistributeService.TRANSACT_CODE_EXECUTE_REMOTE_COMMAND, create, create2, new MessageOption())) {
                        HiLog.info(Stub.LABEL, "executeRemoteCommand: isSendSucceed fail", new Object[0]);
                        return "";
                    }
                    int readInt = create2.readInt();
                    int readInt2 = create2.readInt();
                    byte[] readByteArray = create2.readByteArray();
                    byte[] inflater = GzipUtil.inflater(readByteArray);
                    String str3 = new String(inflater, StandardCharsets.UTF_8);
                    HiLog.info(Stub.LABEL, "executeRemoteCommand: end, command %{public}s, ipcCode %{public}d, expect %{public}d, compressed %{public}d, actual %{public}d", str, Integer.valueOf(readInt), Integer.valueOf(readInt2), Integer.valueOf(readByteArray.length), Integer.valueOf(inflater.length));
                    create.reclaim();
                    create2.reclaim();
                    return str3;
                } finally {
                    create.reclaim();
                    create2.reclaim();
                }
            }

            @Override // ohos.security.dpermission.IDPermissionDistributeService
            public String executeRemoteCommandBacktrack(String str, String str2, String str3) throws RemoteException {
                String readString;
                MessageParcel create = MessageParcel.create();
                MessageParcel create2 = MessageParcel.create();
                MessageOption messageOption = new MessageOption();
                try {
                    create.writeInterfaceToken(Stub.DESCRIPTOR);
                    create.writeString(str);
                    create.writeString(str2);
                    create.writeString(str3);
                    if (!this.remote.sendRequest(IDPermissionDistributeService.TRANSACT_CODE_EXECUTE_REMOTE_COMMAND_BACKTRACK, create, create2, messageOption)) {
                        HiLog.info(Stub.LABEL, "executeRemoteCommand: isSendSucceed fail", new Object[0]);
                        readString = "";
                    } else {
                        readString = create2.readString();
                    }
                    return readString;
                } finally {
                    create.reclaim();
                    create2.reclaim();
                }
            }

            @Override // ohos.security.dpermission.IDPermissionDistributeService
            public boolean canRequestPermission(String str, String str2, int i) throws RemoteException {
                MessageParcel create = MessageParcel.create();
                MessageParcel create2 = MessageParcel.create();
                try {
                    create.writeInterfaceToken(Stub.DESCRIPTOR);
                    create.writeString(str);
                    create.writeString(str2);
                    create.writeInt(i);
                    this.remote.sendRequest(IDPermissionDistributeService.TRANSACT_CODE_CAN_REQUEST_PERMISSION, create, create2, new MessageOption());
                    boolean readBoolean = create2.readBoolean();
                    HiLog.debug(Stub.LABEL, "PermissionKitInnerProxy canRequestPermission replyData is %{public}b", Boolean.valueOf(readBoolean));
                    return readBoolean;
                } finally {
                    create.reclaim();
                    create2.reclaim();
                }
            }

            @Override // ohos.security.dpermission.IDPermissionDistributeService
            public int notifyUidPermissionChanged(int i) throws RemoteException {
                int readInt;
                MessageParcel create = MessageParcel.create();
                MessageParcel create2 = MessageParcel.create();
                MessageOption messageOption = new MessageOption();
                try {
                    create.writeInterfaceToken(Stub.DESCRIPTOR);
                    create.writeInt(i);
                    if (!this.remote.sendRequest(IDPermissionDistributeService.TRANSACT_CODE_NOTIFY_UID_PERMISSION_CHANGED, create, create2, messageOption)) {
                        HiLog.error(Stub.LABEL, "notifyUidPermissionChanged: isSendSucceed fail", new Object[0]);
                        readInt = -1;
                    } else {
                        readInt = create2.readInt();
                    }
                    return readInt;
                } finally {
                    create.reclaim();
                    create2.reclaim();
                }
            }

            @Override // ohos.security.dpermission.IDPermissionDistributeService
            public String getPermissionUsagesInfo(String str, String[] strArr) throws RemoteException {
                String readString;
                MessageParcel create = MessageParcel.create();
                MessageParcel create2 = MessageParcel.create();
                MessageOption messageOption = new MessageOption();
                try {
                    create.writeInterfaceToken(Stub.DESCRIPTOR);
                    create.writeString(str);
                    create.writeStringArray(strArr);
                    if (!this.remote.sendRequest(IDPermissionDistributeService.TRANSACT_CODE_GET_PERMISSION_USAGE_INFO, create, create2, messageOption)) {
                        HiLog.error(Stub.LABEL, "getPermissionUsagesInfo: isSendSucceed fail", new Object[0]);
                        readString = null;
                    } else {
                        readString = create2.readString();
                    }
                    return readString;
                } finally {
                    create.reclaim();
                    create2.reclaim();
                }
            }

            @Override // ohos.rpc.IRemoteBroker
            public IRemoteObject asObject() {
                return this.remote;
            }
        }
    }
}
