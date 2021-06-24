package ohos.miscservices.inputmethod.internal;

import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;

public class UriPermissionProxy implements IUriPermission {
    private static final int COMMAND_RELEASE = 2;
    private static final int COMMAND_TAKE = 1;
    private static final String DESCRIPTOR = "ohos.miscservices.inputmethod.internal.IUriPermission";
    private static final int ERR_OK = 0;
    private static final HiLogLabel TAG = new HiLogLabel(3, 218110976, "UriPermissionProxy");
    private static final int WRITE_MSG_ERROR = -2;
    private final IRemoteObject remote;

    public UriPermissionProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this.remote;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:12|13|14) */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x003d, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0044, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0045, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004b, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x003f */
    @Override // ohos.miscservices.inputmethod.internal.IUriPermission
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void take() throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IUriPermission"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.UriPermissionProxy.TAG
            java.lang.Object[] r0 = new java.lang.Object[r3]
            java.lang.String r2 = "take writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r2, r0)
            r5 = -2
            r1.writeInt(r5)
            return
        L_0x0024:
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x003f }
            r3 = 1
            r5.sendRequest(r3, r0, r1, r2)     // Catch:{ RemoteException -> 0x003f }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x003f }
            if (r5 != 0) goto L_0x0037
            r0.reclaim()
            r1.reclaim()
            return
        L_0x0037:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x003d:
            r5 = move-exception
            goto L_0x0045
        L_0x003f:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x003d }
            r5.<init>()     // Catch:{ all -> 0x003d }
            throw r5     // Catch:{ all -> 0x003d }
        L_0x0045:
            r0.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.UriPermissionProxy.take():void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:12|13|14) */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x003d, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0044, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0045, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004b, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x003f */
    @Override // ohos.miscservices.inputmethod.internal.IUriPermission
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void release() throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IUriPermission"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.UriPermissionProxy.TAG
            java.lang.Object[] r0 = new java.lang.Object[r3]
            java.lang.String r2 = "release writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r2, r0)
            r5 = -2
            r1.writeInt(r5)
            return
        L_0x0024:
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x003f }
            r3 = 2
            r5.sendRequest(r3, r0, r1, r2)     // Catch:{ RemoteException -> 0x003f }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x003f }
            if (r5 != 0) goto L_0x0037
            r0.reclaim()
            r1.reclaim()
            return
        L_0x0037:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x003d:
            r5 = move-exception
            goto L_0x0045
        L_0x003f:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x003d }
            r5.<init>()     // Catch:{ all -> 0x003d }
            throw r5     // Catch:{ all -> 0x003d }
        L_0x0045:
            r0.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.UriPermissionProxy.release():void");
    }
}
