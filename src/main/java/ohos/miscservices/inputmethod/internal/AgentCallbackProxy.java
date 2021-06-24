package ohos.miscservices.inputmethod.internal;

import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;

public class AgentCallbackProxy implements IAgentCallback {
    private static final int AGENT_CREATED = 1;
    private static final String DESCRIPTOR = "ohos.miscservices.inputmethod.internal.IAgentCallback";
    private static final int ERR_OK = 0;
    private static final HiLogLabel TAG = new HiLogLabel(3, 218110976, "AgentCallbackProxy");
    private static final int WRITE_MSG_ERROR = -2;
    private final IRemoteObject remote;

    public AgentCallbackProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this.remote;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0049, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0050, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0051, code lost:
        r0.reclaim();
        r2.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0057, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x004b */
    @Override // ohos.miscservices.inputmethod.internal.IAgentCallback
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void agentCreated(ohos.rpc.IRemoteObject r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.hiviewdfx.HiLogLabel r0 = ohos.miscservices.inputmethod.internal.AgentCallbackProxy.TAG
            r1 = 0
            java.lang.Object[] r2 = new java.lang.Object[r1]
            java.lang.String r3 = "agentCreated"
            ohos.hiviewdfx.HiLog.info(r0, r3, r2)
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>(r1)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IAgentCallback"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x002d
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.AgentCallbackProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r1]
            java.lang.String r0 = "agentCreated writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r0, r6)
            r5 = -2
            r2.writeInt(r5)
            return
        L_0x002d:
            r0.writeRemoteObject(r6)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x004b }
            r6 = 1
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x004b }
            int r5 = r2.readInt()     // Catch:{ RemoteException -> 0x004b }
            if (r5 != 0) goto L_0x0043
            r0.reclaim()
            r2.reclaim()
            return
        L_0x0043:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x0049:
            r5 = move-exception
            goto L_0x0051
        L_0x004b:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0049 }
            r5.<init>()     // Catch:{ all -> 0x0049 }
            throw r5     // Catch:{ all -> 0x0049 }
        L_0x0051:
            r0.reclaim()
            r2.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.AgentCallbackProxy.agentCreated(ohos.rpc.IRemoteObject):void");
    }
}
