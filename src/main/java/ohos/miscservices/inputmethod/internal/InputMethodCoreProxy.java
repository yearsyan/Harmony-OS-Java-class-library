package ohos.miscservices.inputmethod.internal;

import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;

public class InputMethodCoreProxy implements IInputMethodCore {
    private static final int COMMAND_CREATE_AGENT = 2;
    private static final int COMMAND_HIDE_KEYBOARD = 5;
    private static final int COMMAND_INITIALIZE_INPUT = 1;
    private static final int COMMAND_SHOW_KEYBOARD = 4;
    private static final int COMMAND_START_INPUT = 3;
    private static final String DESCRIPTOR = "ohos.miscservices.inputmethod.internal.IInputMethodCore";
    private static final int ERR_OK = 0;
    private static final HiLogLabel TAG = new HiLogLabel(3, 218110976, "InputMethodCoreProxy");
    private static final int WRITE_MSG_ERROR = -2;
    private final IRemoteObject remote;

    public InputMethodCoreProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this.remote;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:13|14|15) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0046, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x004d, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004e, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0054, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0048 */
    @Override // ohos.miscservices.inputmethod.internal.IInputMethodCore
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void initializeInput(ohos.rpc.IRemoteObject r6, int r7, ohos.rpc.IRemoteObject r8) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputMethodCore"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputMethodCoreProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r7 = "initializeInput writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r7, r6)
            r5 = -2
            r1.writeInt(r5)
            return
        L_0x0024:
            r0.writeRemoteObject(r6)
            r0.writeInt(r7)
            r0.writeRemoteObject(r8)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x0048 }
            r6 = 1
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x0048 }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x0048 }
            if (r5 != 0) goto L_0x0040
            r0.reclaim()
            r1.reclaim()
            return
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
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputMethodCoreProxy.initializeInput(ohos.rpc.IRemoteObject, int, ohos.rpc.IRemoteObject):void");
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
    @Override // ohos.miscservices.inputmethod.internal.IInputMethodCore
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void createAgent(ohos.rpc.IRemoteObject r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputMethodCore"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputMethodCoreProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r0 = "createAgent writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r0, r6)
            r5 = -2
            r1.writeInt(r5)
            return
        L_0x0024:
            r0.writeRemoteObject(r6)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x0042 }
            r6 = 2
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
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputMethodCoreProxy.createAgent(ohos.rpc.IRemoteObject):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0058, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x005f, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0060, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0066, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x005a */
    @Override // ohos.miscservices.inputmethod.internal.IInputMethodCore
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean startInput(ohos.rpc.IRemoteObject r6, ohos.miscservices.inputmethod.EditorAttribute r7, ohos.rpc.IRemoteObject r8) throws ohos.rpc.RemoteException {
        /*
        // Method dump skipped, instructions count: 103
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputMethodCoreProxy.startInput(ohos.rpc.IRemoteObject, ohos.miscservices.inputmethod.EditorAttribute, ohos.rpc.IRemoteObject):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0049, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0050, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0051, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0057, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x004b */
    @Override // ohos.miscservices.inputmethod.internal.IInputMethodCore
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean showKeyboard(int r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputMethodCore"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputMethodCoreProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r0 = "showKeyboard writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r0, r6)
            r5 = -2
            r1.writeInt(r5)
            return r3
        L_0x0024:
            r0.writeInt(r6)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x004b }
            r6 = 4
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x004b }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x004b }
            if (r5 != 0) goto L_0x0043
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x004b }
            r6 = 1
            if (r5 != r6) goto L_0x003b
            goto L_0x003c
        L_0x003b:
            r6 = r3
        L_0x003c:
            r0.reclaim()
            r1.reclaim()
            return r6
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
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputMethodCoreProxy.showKeyboard(int):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0049, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0050, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0051, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0057, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x004b */
    @Override // ohos.miscservices.inputmethod.internal.IInputMethodCore
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean hideKeyboard(int r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputMethodCore"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputMethodCoreProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r0 = "hideKeyboard writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r0, r6)
            r5 = -2
            r1.writeInt(r5)
            return r3
        L_0x0024:
            r0.writeInt(r6)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x004b }
            r6 = 5
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x004b }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x004b }
            if (r5 != 0) goto L_0x0043
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x004b }
            r6 = 1
            if (r5 != r6) goto L_0x003b
            goto L_0x003c
        L_0x003b:
            r6 = r3
        L_0x003c:
            r0.reclaim()
            r1.reclaim()
            return r6
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
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputMethodCoreProxy.hideKeyboard(int):boolean");
    }
}
