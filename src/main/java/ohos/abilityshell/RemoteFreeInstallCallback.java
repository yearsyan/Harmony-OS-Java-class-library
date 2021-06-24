package ohos.abilityshell;

import ohos.appexecfwk.utils.AppLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;

public abstract class RemoteFreeInstallCallback extends RemoteObject implements IRemoteFreeInstallCallback {
    private static final String DESCRIPTOR = "ohos.abilityshell.FreeInstallCallback";
    private static final int REMOTE_FREE_INSTALL_CODE = 1;
    private static final HiLogLabel SHELL_LABEL = new HiLogLabel(3, 218108160, "AbilityShell");

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this;
    }

    public RemoteFreeInstallCallback() {
        super(DESCRIPTOR);
    }

    @Override // ohos.rpc.RemoteObject
    public boolean onRemoteRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException {
        if (messageParcel == null || messageParcel2 == null) {
            AppLog.e(SHELL_LABEL, "RemoteFreeInstallCallback::onRemoteRequest param invalid", new Object[0]);
            return false;
        }
        AppLog.d("RemoteFreeInstallCallback::onRemoteRequest code: %{public}d", Integer.valueOf(i));
        if (!DESCRIPTOR.equals(messageParcel.readInterfaceToken())) {
            AppLog.e(SHELL_LABEL, "RemoteFreeInstallCallback::onRemoteRequest token is invalid.", new Object[0]);
            return false;
        } else if (i != 1) {
            AppLog.w(SHELL_LABEL, "RemoteFreeInstallCallback::onRemoteRequest unknown code", new Object[0]);
            return super.onRemoteRequest(i, messageParcel, messageParcel2, messageOption);
        } else {
            AppLog.d(SHELL_LABEL, "RemoteFreeInstallCallback::REMOTE_FREE_INSTALL_CODE receive", new Object[0]);
            onResult(messageParcel.readInt());
            messageParcel2.writeInt(0);
            return true;
        }
    }
}
