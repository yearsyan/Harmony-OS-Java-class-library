package ohos.miscservices.inputmethod;

import ohos.rpc.IRemoteObject;

public class RemoteInputMethodAgentProxy implements IRemoteInputMethodAgent {
    private static final int COMMAND_SET_REMOTE_OBJECT = 1;
    private static final int ERR_OK = 0;
    private final IRemoteObject remote;

    public RemoteInputMethodAgentProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this.remote;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:9|10|11) */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0037, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0038, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x003e, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0030, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0032 */
    @Override // ohos.miscservices.inputmethod.IRemoteInputMethodAgent
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setRemoteObject(ohos.rpc.IRemoteObject r5, ohos.rpc.IRemoteObject r6, java.lang.String r7) throws ohos.rpc.RemoteException {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            r0.writeRemoteObject(r5)
            r0.writeRemoteObject(r6)
            r0.writeString(r7)
            ohos.rpc.IRemoteObject r4 = r4.remote     // Catch:{ RemoteException -> 0x0032 }
            r5 = 1
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x0032 }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0032 }
            if (r4 != 0) goto L_0x002a
            r0.reclaim()
            r1.reclaim()
            return
        L_0x002a:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException
            r4.<init>()
            throw r4
        L_0x0030:
            r4 = move-exception
            goto L_0x0038
        L_0x0032:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0030 }
            r4.<init>()     // Catch:{ all -> 0x0030 }
            throw r4     // Catch:{ all -> 0x0030 }
        L_0x0038:
            r0.reclaim()
            r1.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.RemoteInputMethodAgentProxy.setRemoteObject(ohos.rpc.IRemoteObject, ohos.rpc.IRemoteObject, java.lang.String):void");
    }
}
