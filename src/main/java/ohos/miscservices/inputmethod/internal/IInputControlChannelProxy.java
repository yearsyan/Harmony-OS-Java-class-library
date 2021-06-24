package ohos.miscservices.inputmethod.internal;

import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;

public class IInputControlChannelProxy implements IInputControlChannel {
    private static final int COMMAND_CREATE_URI_PERMISSION = 4;
    private static final int COMMAND_HIDE_KEY_BOARD_SELF = 1;
    private static final int COMMAND_REPORT_SCREEN_MODE = 3;
    private static final int COMMAND_SWITCH_TO_NEXT_INPUTMETHOD = 2;
    private static final String DESCRIPTOR = "ohos.miscservices.inputmethod.internal.IInputControlChannel";
    private static final int ERR_OK = 0;
    private static final HiLogLabel TAG = new HiLogLabel(3, 218110976, "IInputControlChannelProxy");
    private static final int WRITE_MSG_ERROR = -2;
    private final IRemoteObject remote;

    public IInputControlChannelProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this.remote;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:13|14|15) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0040, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0047, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0048, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x004e, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0042 */
    @Override // ohos.miscservices.inputmethod.internal.IInputControlChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void hideKeyboardSelf(int r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputControlChannel"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.IInputControlChannelProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r0 = "hideKeyboardSelf writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r0, r6)
            r5 = -2
            r1.writeInt(r5)
            return
        L_0x0024:
            r0.writeInt(r6)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x0042 }
            r6 = 1
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x0042 }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x0042 }
            if (r5 != 0) goto L_0x003a
            r0.reclaim()
            r1.reclaim()
            return
        L_0x003a:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x0040:
            r5 = move-exception
            goto L_0x0048
        L_0x0042:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0040 }
            r5.<init>()     // Catch:{ all -> 0x0040 }
            throw r5     // Catch:{ all -> 0x0040 }
        L_0x0048:
            r0.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.IInputControlChannelProxy.hideKeyboardSelf(int):void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:16|17|18) */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0046, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004d, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x004e, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0054, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:16:0x0048 */
    @Override // ohos.miscservices.inputmethod.internal.IInputControlChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean toNextInputMethod() throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputControlChannel"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.IInputControlChannelProxy.TAG
            java.lang.Object[] r0 = new java.lang.Object[r3]
            java.lang.String r2 = "toNextInputMethod writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r2, r0)
            r5 = -2
            r1.writeInt(r5)
            return r3
        L_0x0024:
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x0048 }
            r4 = 2
            r5.sendRequest(r4, r0, r1, r2)     // Catch:{ RemoteException -> 0x0048 }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x0048 }
            if (r5 != 0) goto L_0x0040
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x0048 }
            r2 = 1
            if (r5 != r2) goto L_0x0038
            goto L_0x0039
        L_0x0038:
            r2 = r3
        L_0x0039:
            r0.reclaim()
            r1.reclaim()
            return r2
        L_0x0040:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x0046:
            r5 = move-exception
            goto L_0x004e
        L_0x0048:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0046 }
            r5.<init>()     // Catch:{ all -> 0x0046 }
            throw r5     // Catch:{ all -> 0x0046 }
        L_0x004e:
            r0.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.IInputControlChannelProxy.toNextInputMethod():boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:13|14|15) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0040, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0047, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0048, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x004e, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0042 */
    @Override // ohos.miscservices.inputmethod.internal.IInputControlChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void reportScreenMode(int r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputControlChannel"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.IInputControlChannelProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r0 = "reportScreenMode writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r0, r6)
            r5 = -2
            r1.writeInt(r5)
            return
        L_0x0024:
            r0.writeInt(r6)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x0042 }
            r6 = 3
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x0042 }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x0042 }
            if (r5 != 0) goto L_0x003a
            r0.reclaim()
            r1.reclaim()
            return
        L_0x003a:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x0040:
            r5 = move-exception
            goto L_0x0048
        L_0x0042:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0040 }
            r5.<init>()     // Catch:{ all -> 0x0040 }
            throw r5     // Catch:{ all -> 0x0040 }
        L_0x0048:
            r0.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.IInputControlChannelProxy.reportScreenMode(int):void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:17|18|19) */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0056, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x005d, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x005e, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0064, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0058 */
    @Override // ohos.miscservices.inputmethod.internal.IInputControlChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.miscservices.inputmethod.internal.IUriPermission createUriPermission(ohos.utils.net.Uri r6, java.lang.String r7) throws ohos.rpc.RemoteException {
        /*
        // Method dump skipped, instructions count: 101
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.IInputControlChannelProxy.createUriPermission(ohos.utils.net.Uri, java.lang.String):ohos.miscservices.inputmethod.internal.IUriPermission");
    }
}
