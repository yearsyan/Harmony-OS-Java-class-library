package ohos.miscservices.inputmethod.adapter;

import ohos.rpc.IRemoteObject;

public class InputMethodManagerAdapterControlProxy implements IInputMethodManagerAdapterControl {
    private static final int COMMAND_NOTIFY_CLIENT_DISCONNECT = 1;
    private static final int ERR_OK = 0;
    private final IRemoteObject remote;

    public InputMethodManagerAdapterControlProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this.remote;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:12|13|14) */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x002e, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0035, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0036, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003c, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0030 */
    @Override // ohos.miscservices.inputmethod.adapter.IInputMethodManagerAdapterControl
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean notifyClientDisconnect() throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x0030 }
            r4 = 1
            r5.sendRequest(r4, r0, r1, r2)     // Catch:{ RemoteException -> 0x0030 }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x0030 }
            if (r5 != 0) goto L_0x0028
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x0030 }
            if (r5 != r4) goto L_0x0021
            r3 = r4
        L_0x0021:
            r0.reclaim()
            r1.reclaim()
            return r3
        L_0x0028:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x002e:
            r5 = move-exception
            goto L_0x0036
        L_0x0030:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x002e }
            r5.<init>()     // Catch:{ all -> 0x002e }
            throw r5     // Catch:{ all -> 0x002e }
        L_0x0036:
            r0.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.adapter.InputMethodManagerAdapterControlProxy.notifyClientDisconnect():boolean");
    }
}
