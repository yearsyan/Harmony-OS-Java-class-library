package ohos.miscservices.inputmethod.internal;

import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;

public class InputDataChannelCallbackProxy implements IInputDataChannelCallback {
    private static final int COMMAND_NOTIFY_CARET_CONTEXT_SUBSCRIPTION = 6;
    private static final int COMMAND_NOTIFY_EDITING_TEXT = 3;
    private static final int COMMAND_NOTIFY_INSERT_RICH_CONTENT = 7;
    private static final int COMMAND_SET_AUTO_CAPITALIZE_MODE = 4;
    private static final int COMMAND_SET_BACKWARD = 2;
    private static final int COMMAND_SET_FORWARD = 1;
    private static final int COMMAND_SET_SELECTED_TEXT = 5;
    private static final String DESCRIPTOR = "ohos.miscservices.inputmethod.internal.IInputDataChannelCallback";
    private static final int ERR_OK = 0;
    private static final HiLogLabel TAG = new HiLogLabel(3, 218110976, "InputDataChannelCallbackProxy");
    private static final int WRITE_MSG_ERROR = -2;
    private final IRemoteObject remote;

    public InputDataChannelCallbackProxy(IRemoteObject iRemoteObject) {
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
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannelCallback
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setForward(java.lang.String r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannelCallback"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelCallbackProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r0 = "setForward writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r0, r6)
            r5 = -2
            r1.writeInt(r5)
            return
        L_0x0024:
            r0.writeString(r6)
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
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelCallbackProxy.setForward(java.lang.String):void");
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
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannelCallback
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setBackward(java.lang.String r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannelCallback"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelCallbackProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r0 = "setBackward writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r0, r6)
            r5 = -2
            r1.writeInt(r5)
            return
        L_0x0024:
            r0.writeString(r6)
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
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelCallbackProxy.setBackward(java.lang.String):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x004a, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0051, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0052, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0058, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x004c */
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannelCallback
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void notifyEditingText(ohos.miscservices.inputmethod.EditingText r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannelCallback"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelCallbackProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r0 = "notifyEditingText writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r0, r6)
            r5 = -2
            r1.writeInt(r5)
            return
        L_0x0024:
            if (r6 == 0) goto L_0x002e
            r3 = 1
            r0.writeInt(r3)
            r6.marshalling(r0)
            goto L_0x0031
        L_0x002e:
            r0.writeInt(r3)
        L_0x0031:
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x004c }
            r6 = 3
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x004c }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x004c }
            if (r5 != 0) goto L_0x0044
            r0.reclaim()
            r1.reclaim()
            return
        L_0x0044:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x004a:
            r5 = move-exception
            goto L_0x0052
        L_0x004c:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x004a }
            r5.<init>()     // Catch:{ all -> 0x004a }
            throw r5     // Catch:{ all -> 0x004a }
        L_0x0052:
            r0.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelCallbackProxy.notifyEditingText(ohos.miscservices.inputmethod.EditingText):void");
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
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannelCallback
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setAutoCapitalizeMode(int r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannelCallback"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelCallbackProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r0 = "setAutoCapitalizeMode writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r0, r6)
            r5 = -2
            r1.writeInt(r5)
            return
        L_0x0024:
            r0.writeInt(r6)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x0042 }
            r6 = 4
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
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelCallbackProxy.setAutoCapitalizeMode(int):void");
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
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannelCallback
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setSelectedText(java.lang.String r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannelCallback"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelCallbackProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r0 = "setSelectedText writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r0, r6)
            r5 = -2
            r1.writeInt(r5)
            return
        L_0x0024:
            r0.writeString(r6)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x0042 }
            r6 = 5
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
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelCallbackProxy.setSelectedText(java.lang.String):void");
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
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannelCallback
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void notifySubscribeCaretContextResult(boolean r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannelCallback"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelCallbackProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r0 = "notifySubscribeCaretContextResult writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r0, r6)
            r5 = -2
            r1.writeInt(r5)
            return
        L_0x0024:
            r0.writeBoolean(r6)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x0042 }
            r6 = 6
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
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelCallbackProxy.notifySubscribeCaretContextResult(boolean):void");
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
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannelCallback
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void notifyInsertRichContentResult(boolean r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannelCallback"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelCallbackProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r0 = "notifyInsertRichContentResult writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r0, r6)
            r5 = -2
            r1.writeInt(r5)
            return
        L_0x0024:
            r0.writeBoolean(r6)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x0042 }
            r6 = 7
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
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelCallbackProxy.notifyInsertRichContentResult(boolean):void");
    }
}
