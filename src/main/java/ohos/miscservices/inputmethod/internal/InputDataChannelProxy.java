package ohos.miscservices.inputmethod.internal;

import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;

public class InputDataChannelProxy implements IInputDataChannel {
    private static final int COMMAND_CLEAR_NONCHARACTER_KEY_STATE = 34;
    private static final int COMMAND_DELETE_BACKWARD = 4;
    private static final int COMMAND_DELETE_FORWARD = 3;
    private static final int COMMAND_GET_AUTO_CAPITALIZE_MODE = 23;
    private static final int COMMAND_GET_BACKWARD = 6;
    private static final int COMMAND_GET_EDITING_TEXT = 21;
    private static final int COMMAND_GET_FORWARD = 5;
    private static final int COMMAND_GET_SELECTED_TEXT = 24;
    private static final int COMMAND_INSERT_RICH_CONTENT = 2;
    private static final int COMMAND_INSERT_TEXT = 1;
    private static final int COMMAND_MARK_TEXT = 11;
    private static final int COMMAND_RECOMMEND_TEXT = 14;
    private static final int COMMAND_REPLACE_MARKED_TEXT = 13;
    private static final int COMMAND_REQUEST_CURSOR_CONTEXT_ONCE = 41;
    private static final int COMMAND_REVISE_TEXT = 15;
    private static final int COMMAND_SELECT_TEXT = 16;
    private static final int COMMAND_SEND_CUSTOMIZED_DATA = 32;
    private static final int COMMAND_SEND_KEY_EVENT = 31;
    private static final int COMMAND_SEND_KEY_FUNCTION = 33;
    private static final int COMMAND_SEND_MENU_FUNCTION = 35;
    private static final int COMMAND_SUBSCRIBE_CURSOR_CONTEXT_CHANGED = 42;
    private static final int COMMAND_SUBSCRIBE_EDITING_TEXT = 25;
    private static final int COMMAND_UNMARK_TEXT = 12;
    private static final int COMMAND_UNSUBSCRIBE_CURSOR_CONTEXT_CHANGED = 43;
    private static final int COMMAND_UNSUBSCRIBE_EDITING_TEXT = 26;
    private static final int COMMAND_VARIATION_FIVE = 50;
    private static final int COMMAND_VARIATION_FOUR = 40;
    private static final int COMMAND_VARIATION_ONE = 10;
    private static final int COMMAND_VARIATION_THREE = 30;
    private static final int COMMAND_VARIATION_TWO = 20;
    private static final String DESCRIPTOR = "ohos.miscservices.inputmethod.internal.IInputDataChannel";
    private static final int ERR_OK = 0;
    private static final HiLogLabel TAG = new HiLogLabel(3, 218110976, "InputDataChannelProxy");
    private static final int WRITE_MSG_ERROR = -2;
    private final IRemoteObject remote;

    public InputDataChannelProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0048, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004f, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0050, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0056, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:16:0x004a */
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean insertText(java.lang.String r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannel"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r0 = "insertText writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r0, r6)
            r5 = -2
            r1.writeInt(r5)
            return r3
        L_0x0024:
            r0.writeString(r6)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x004a }
            r6 = 1
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x004a }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x004a }
            if (r5 != 0) goto L_0x0042
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x004a }
            if (r5 != r6) goto L_0x003a
            goto L_0x003b
        L_0x003a:
            r6 = r3
        L_0x003b:
            r0.reclaim()
            r1.reclaim()
            return r6
        L_0x0042:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x0048:
            r5 = move-exception
            goto L_0x0050
        L_0x004a:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0048 }
            r5.<init>()     // Catch:{ all -> 0x0048 }
            throw r5     // Catch:{ all -> 0x0048 }
        L_0x0050:
            r0.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelProxy.insertText(java.lang.String):boolean");
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this.remote;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x005c, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0063, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0064, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x006a, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x005e */
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean insertRichContent(ohos.miscservices.inputmethod.RichContent r6, ohos.miscservices.inputmethod.internal.IInputDataChannelCallback r7) throws ohos.rpc.RemoteException {
        /*
        // Method dump skipped, instructions count: 107
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelProxy.insertRichContent(ohos.miscservices.inputmethod.RichContent, ohos.miscservices.inputmethod.internal.IInputDataChannelCallback):boolean");
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
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean deleteForward(int r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannel"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r0 = "deleteForward writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r0, r6)
            r5 = -2
            r1.writeInt(r5)
            return r3
        L_0x0024:
            r0.writeInt(r6)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x004b }
            r6 = 3
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
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelProxy.deleteForward(int):boolean");
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
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean deleteBackward(int r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannel"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r0 = "deleteBackward writeInterfaceToken failed."
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
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelProxy.deleteBackward(int):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004b, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0052, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0053, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0059, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x004d */
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void getForward(int r6, ohos.miscservices.inputmethod.internal.IInputDataChannelCallback r7) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannel"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r7 = "getForward writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r7, r6)
            r5 = -2
            r1.writeInt(r5)
            return
        L_0x0024:
            r0.writeInt(r6)
            if (r7 == 0) goto L_0x002e
            ohos.rpc.IRemoteObject r6 = r7.asObject()
            goto L_0x002f
        L_0x002e:
            r6 = 0
        L_0x002f:
            r0.writeRemoteObject(r6)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x004d }
            r6 = 5
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x004d }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x004d }
            if (r5 != 0) goto L_0x0045
            r0.reclaim()
            r1.reclaim()
            return
        L_0x0045:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x004b:
            r5 = move-exception
            goto L_0x0053
        L_0x004d:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x004b }
            r5.<init>()     // Catch:{ all -> 0x004b }
            throw r5     // Catch:{ all -> 0x004b }
        L_0x0053:
            r0.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelProxy.getForward(int, ohos.miscservices.inputmethod.internal.IInputDataChannelCallback):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004b, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0052, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0053, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0059, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x004d */
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void getBackward(int r6, ohos.miscservices.inputmethod.internal.IInputDataChannelCallback r7) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannel"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r7 = "getBackward writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r7, r6)
            r5 = -2
            r1.writeInt(r5)
            return
        L_0x0024:
            r0.writeInt(r6)
            if (r7 == 0) goto L_0x002e
            ohos.rpc.IRemoteObject r6 = r7.asObject()
            goto L_0x002f
        L_0x002e:
            r6 = 0
        L_0x002f:
            r0.writeRemoteObject(r6)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x004d }
            r6 = 6
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x004d }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x004d }
            if (r5 != 0) goto L_0x0045
            r0.reclaim()
            r1.reclaim()
            return
        L_0x0045:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x004b:
            r5 = move-exception
            goto L_0x0053
        L_0x004d:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x004b }
            r5.<init>()     // Catch:{ all -> 0x004b }
            throw r5     // Catch:{ all -> 0x004b }
        L_0x0053:
            r0.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelProxy.getBackward(int, ohos.miscservices.inputmethod.internal.IInputDataChannelCallback):void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:17|18|19) */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004d, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0054, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0055, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x005b, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x004f */
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean markText(int r6, int r7) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannel"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r7 = "markText writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r7, r6)
            r5 = -2
            r1.writeInt(r5)
            return r3
        L_0x0024:
            r0.writeInt(r6)
            r0.writeInt(r7)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x004f }
            r6 = 11
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x004f }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x004f }
            if (r5 != 0) goto L_0x0047
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x004f }
            r6 = 1
            if (r5 != r6) goto L_0x003f
            goto L_0x0040
        L_0x003f:
            r6 = r3
        L_0x0040:
            r0.reclaim()
            r1.reclaim()
            return r6
        L_0x0047:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x004d:
            r5 = move-exception
            goto L_0x0055
        L_0x004f:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x004d }
            r5.<init>()     // Catch:{ all -> 0x004d }
            throw r5     // Catch:{ all -> 0x004d }
        L_0x0055:
            r0.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelProxy.markText(int, int):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0047, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x004e, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x004f, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0055, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:16:0x0049 */
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean unmarkText() throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannel"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelProxy.TAG
            java.lang.Object[] r0 = new java.lang.Object[r3]
            java.lang.String r2 = "unmarkText writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r2, r0)
            r5 = -2
            r1.writeInt(r5)
            return r3
        L_0x0024:
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x0049 }
            r4 = 12
            r5.sendRequest(r4, r0, r1, r2)     // Catch:{ RemoteException -> 0x0049 }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x0049 }
            if (r5 != 0) goto L_0x0041
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x0049 }
            r2 = 1
            if (r5 != r2) goto L_0x0039
            goto L_0x003a
        L_0x0039:
            r2 = r3
        L_0x003a:
            r0.reclaim()
            r1.reclaim()
            return r2
        L_0x0041:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x0047:
            r5 = move-exception
            goto L_0x004f
        L_0x0049:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0047 }
            r5.<init>()     // Catch:{ all -> 0x0047 }
            throw r5     // Catch:{ all -> 0x0047 }
        L_0x004f:
            r0.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelProxy.unmarkText():boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004a, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0051, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0052, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0058, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x004c */
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean replaceMarkedText(java.lang.String r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannel"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r0 = "replaceMarkedText writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r0, r6)
            r5 = -2
            r1.writeInt(r5)
            return r3
        L_0x0024:
            r0.writeString(r6)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x004c }
            r6 = 13
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x004c }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x004c }
            if (r5 != 0) goto L_0x0044
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x004c }
            r6 = 1
            if (r5 != r6) goto L_0x003c
            goto L_0x003d
        L_0x003c:
            r6 = r3
        L_0x003d:
            r0.reclaim()
            r1.reclaim()
            return r6
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
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelProxy.replaceMarkedText(java.lang.String):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0059, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0060, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0061, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0067, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x005b */
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void getEditingText(int r6, ohos.miscservices.inputmethod.EditingCapability r7, ohos.miscservices.inputmethod.internal.IInputDataChannelCallback r8) throws ohos.rpc.RemoteException {
        /*
        // Method dump skipped, instructions count: 104
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelProxy.getEditingText(int, ohos.miscservices.inputmethod.EditingCapability, ohos.miscservices.inputmethod.internal.IInputDataChannelCallback):void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:19|20|21) */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0056, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x005d, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x005e, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0064, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0058 */
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean subscribeEditingText(int r6, ohos.miscservices.inputmethod.EditingCapability r7) throws ohos.rpc.RemoteException {
        /*
        // Method dump skipped, instructions count: 101
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelProxy.subscribeEditingText(int, ohos.miscservices.inputmethod.EditingCapability):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004a, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0051, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0052, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0058, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x004c */
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean unsubscribeEditingText(int r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannel"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r0 = "subscribeEditingText writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r0, r6)
            r5 = -2
            r1.writeInt(r5)
            return r3
        L_0x0024:
            r0.writeInt(r6)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x004c }
            r6 = 26
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x004c }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x004c }
            if (r5 != 0) goto L_0x0044
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x004c }
            r6 = 1
            if (r5 != r6) goto L_0x003c
            goto L_0x003d
        L_0x003c:
            r6 = r3
        L_0x003d:
            r0.reclaim()
            r1.reclaim()
            return r6
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
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelProxy.unsubscribeEditingText(int):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:19|20|21) */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0052, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0059, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x005a, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0060, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0054 */
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean sendKeyEvent(ohos.multimodalinput.event.KeyEvent r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannel"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r0 = "sendKeyEvent writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r0, r6)
            r5 = -2
            r1.writeInt(r5)
            return r3
        L_0x0024:
            r4 = 1
            if (r6 == 0) goto L_0x002e
            r0.writeInt(r4)
            r6.marshalling(r0)
            goto L_0x0031
        L_0x002e:
            r0.writeInt(r3)
        L_0x0031:
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x0054 }
            r6 = 31
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x0054 }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x0054 }
            if (r5 != 0) goto L_0x004c
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x0054 }
            if (r5 != r4) goto L_0x0045
            r3 = r4
        L_0x0045:
            r0.reclaim()
            r1.reclaim()
            return r3
        L_0x004c:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x0052:
            r5 = move-exception
            goto L_0x005a
        L_0x0054:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0052 }
            r5.<init>()     // Catch:{ all -> 0x0052 }
            throw r5     // Catch:{ all -> 0x0052 }
        L_0x005a:
            r0.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelProxy.sendKeyEvent(ohos.multimodalinput.event.KeyEvent):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:19|20|21) */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0056, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x005d, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x005e, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0064, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0058 */
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean sendCustomizedData(java.lang.String r6, ohos.utils.PacMap r7) throws ohos.rpc.RemoteException {
        /*
        // Method dump skipped, instructions count: 101
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelProxy.sendCustomizedData(java.lang.String, ohos.utils.PacMap):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004a, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0051, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0052, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0058, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x004c */
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean sendKeyFunction(int r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannel"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r0 = "sendKeyFunction writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r0, r6)
            r5 = -2
            r1.writeInt(r5)
            return r3
        L_0x0024:
            r0.writeInt(r6)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x004c }
            r6 = 33
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x004c }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x004c }
            if (r5 != 0) goto L_0x0044
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x004c }
            r6 = 1
            if (r5 != r6) goto L_0x003c
            goto L_0x003d
        L_0x003c:
            r6 = r3
        L_0x003d:
            r0.reclaim()
            r1.reclaim()
            return r6
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
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelProxy.sendKeyFunction(int):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:17|18|19) */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004d, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0054, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0055, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x005b, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x004f */
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean selectText(int r6, int r7) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannel"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r7 = "selectText writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r7, r6)
            r5 = -2
            r1.writeInt(r5)
            return r3
        L_0x0024:
            r0.writeInt(r6)
            r0.writeInt(r7)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x004f }
            r6 = 16
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x004f }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x004f }
            if (r5 != 0) goto L_0x0047
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x004f }
            r6 = 1
            if (r5 != r6) goto L_0x003f
            goto L_0x0040
        L_0x003f:
            r6 = r3
        L_0x0040:
            r0.reclaim()
            r1.reclaim()
            return r6
        L_0x0047:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x004d:
            r5 = move-exception
            goto L_0x0055
        L_0x004f:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x004d }
            r5.<init>()     // Catch:{ all -> 0x004d }
            throw r5     // Catch:{ all -> 0x004d }
        L_0x0055:
            r0.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelProxy.selectText(int, int):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004a, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0051, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0052, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0058, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x004c */
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean clearNoncharacterKeyState(int r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannel"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r0 = "clearNoncharacterKeyState writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r0, r6)
            r5 = -2
            r1.writeInt(r5)
            return r3
        L_0x0024:
            r0.writeInt(r6)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x004c }
            r6 = 34
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x004c }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x004c }
            if (r5 != 0) goto L_0x0044
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x004c }
            r6 = 1
            if (r5 != r6) goto L_0x003c
            goto L_0x003d
        L_0x003c:
            r6 = r3
        L_0x003d:
            r0.reclaim()
            r1.reclaim()
            return r6
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
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelProxy.clearNoncharacterKeyState(int):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:19|20|21) */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0052, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0059, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x005a, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0060, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0054 */
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean recommendText(ohos.miscservices.inputmethod.RecommendationInfo r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannel"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r0 = "recommendText writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r0, r6)
            r5 = -2
            r1.writeInt(r5)
            return r3
        L_0x0024:
            r4 = 1
            if (r6 == 0) goto L_0x002e
            r0.writeInt(r4)
            r6.marshalling(r0)
            goto L_0x0031
        L_0x002e:
            r0.writeInt(r3)
        L_0x0031:
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x0054 }
            r6 = 14
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x0054 }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x0054 }
            if (r5 != 0) goto L_0x004c
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x0054 }
            if (r5 != r4) goto L_0x0045
            r3 = r4
        L_0x0045:
            r0.reclaim()
            r1.reclaim()
            return r3
        L_0x004c:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x0052:
            r5 = move-exception
            goto L_0x005a
        L_0x0054:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0052 }
            r5.<init>()     // Catch:{ all -> 0x0052 }
            throw r5     // Catch:{ all -> 0x0052 }
        L_0x005a:
            r0.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelProxy.recommendText(ohos.miscservices.inputmethod.RecommendationInfo):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:17|18|19) */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0050, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0057, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0058, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x005e, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0052 */
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean reviseText(int r6, java.lang.String r7, java.lang.String r8) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannel"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r7 = "reviseText writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r7, r6)
            r5 = -2
            r1.writeInt(r5)
            return r3
        L_0x0024:
            r0.writeInt(r6)
            r0.writeString(r7)
            r0.writeString(r8)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x0052 }
            r6 = 15
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x0052 }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x0052 }
            if (r5 != 0) goto L_0x004a
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x0052 }
            r6 = 1
            if (r5 != r6) goto L_0x0042
            goto L_0x0043
        L_0x0042:
            r6 = r3
        L_0x0043:
            r0.reclaim()
            r1.reclaim()
            return r6
        L_0x004a:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x0050:
            r5 = move-exception
            goto L_0x0058
        L_0x0052:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0050 }
            r5.<init>()     // Catch:{ all -> 0x0050 }
            throw r5     // Catch:{ all -> 0x0050 }
        L_0x0058:
            r0.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelProxy.reviseText(int, java.lang.String, java.lang.String):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004a, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0051, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0052, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0058, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x004c */
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean sendMenuFunction(int r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannel"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r0 = "sendMenuFunction writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r0, r6)
            r5 = -2
            r1.writeInt(r5)
            return r3
        L_0x0024:
            r0.writeInt(r6)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x004c }
            r6 = 35
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x004c }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x004c }
            if (r5 != 0) goto L_0x0044
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x004c }
            r6 = 1
            if (r5 != r6) goto L_0x003c
            goto L_0x003d
        L_0x003c:
            r6 = r3
        L_0x003d:
            r0.reclaim()
            r1.reclaim()
            return r6
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
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelProxy.sendMenuFunction(int):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:20|21|22) */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0052, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0059, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x005a, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0060, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x0054 */
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean requestCurrentCursorContext(ohos.miscservices.inputmethod.internal.IInputDataChannelCallback r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannel"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r1 = "requestCurrentCursorContext writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r1, r6)
            r5 = -2
            r0.writeInt(r5)
            return r3
        L_0x0024:
            if (r6 == 0) goto L_0x002b
            ohos.rpc.IRemoteObject r6 = r6.asObject()
            goto L_0x002c
        L_0x002b:
            r6 = 0
        L_0x002c:
            r0.writeRemoteObject(r6)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x0054 }
            r6 = 41
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x0054 }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x0054 }
            if (r5 != 0) goto L_0x004c
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x0054 }
            r6 = 1
            if (r5 != r6) goto L_0x0044
            goto L_0x0045
        L_0x0044:
            r6 = r3
        L_0x0045:
            r0.reclaim()
            r1.reclaim()
            return r6
        L_0x004c:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x0052:
            r5 = move-exception
            goto L_0x005a
        L_0x0054:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0052 }
            r5.<init>()     // Catch:{ all -> 0x0052 }
            throw r5     // Catch:{ all -> 0x0052 }
        L_0x005a:
            r0.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelProxy.requestCurrentCursorContext(ohos.miscservices.inputmethod.internal.IInputDataChannelCallback):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:20|21|22) */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0052, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0059, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x005a, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0060, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x0054 */
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean subscribeCursorContext(ohos.miscservices.inputmethod.internal.IInputDataChannelCallback r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannel"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r1 = "subscribeCursorContext writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r1, r6)
            r5 = -2
            r0.writeInt(r5)
            return r3
        L_0x0024:
            if (r6 == 0) goto L_0x002b
            ohos.rpc.IRemoteObject r6 = r6.asObject()
            goto L_0x002c
        L_0x002b:
            r6 = 0
        L_0x002c:
            r0.writeRemoteObject(r6)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x0054 }
            r6 = 42
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x0054 }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x0054 }
            if (r5 != 0) goto L_0x004c
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x0054 }
            r6 = 1
            if (r5 != r6) goto L_0x0044
            goto L_0x0045
        L_0x0044:
            r6 = r3
        L_0x0045:
            r0.reclaim()
            r1.reclaim()
            return r6
        L_0x004c:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x0052:
            r5 = move-exception
            goto L_0x005a
        L_0x0054:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0052 }
            r5.<init>()     // Catch:{ all -> 0x0052 }
            throw r5     // Catch:{ all -> 0x0052 }
        L_0x005a:
            r0.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelProxy.subscribeCursorContext(ohos.miscservices.inputmethod.internal.IInputDataChannelCallback):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:20|21|22) */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0052, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0059, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x005a, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0060, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x0054 */
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean unsubscribeCursorContext(ohos.miscservices.inputmethod.internal.IInputDataChannelCallback r6) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannel"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r1 = "unsubscribeCursorContext writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r1, r6)
            r5 = -2
            r0.writeInt(r5)
            return r3
        L_0x0024:
            if (r6 == 0) goto L_0x002b
            ohos.rpc.IRemoteObject r6 = r6.asObject()
            goto L_0x002c
        L_0x002b:
            r6 = 0
        L_0x002c:
            r0.writeRemoteObject(r6)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x0054 }
            r6 = 43
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x0054 }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x0054 }
            if (r5 != 0) goto L_0x004c
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x0054 }
            r6 = 1
            if (r5 != r6) goto L_0x0044
            goto L_0x0045
        L_0x0044:
            r6 = r3
        L_0x0045:
            r0.reclaim()
            r1.reclaim()
            return r6
        L_0x004c:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x0052:
            r5 = move-exception
            goto L_0x005a
        L_0x0054:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0052 }
            r5.<init>()     // Catch:{ all -> 0x0052 }
            throw r5     // Catch:{ all -> 0x0052 }
        L_0x005a:
            r0.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelProxy.unsubscribeCursorContext(ohos.miscservices.inputmethod.internal.IInputDataChannelCallback):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004c, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0053, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0054, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x005a, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x004e */
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void getAutoCapitalizeMode(int r6, ohos.miscservices.inputmethod.internal.IInputDataChannelCallback r7) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannel"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r7 = "getAutoCapitalizeMode writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r7, r6)
            r5 = -2
            r1.writeInt(r5)
            return
        L_0x0024:
            r0.writeInt(r6)
            if (r7 == 0) goto L_0x002e
            ohos.rpc.IRemoteObject r6 = r7.asObject()
            goto L_0x002f
        L_0x002e:
            r6 = 0
        L_0x002f:
            r0.writeRemoteObject(r6)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x004e }
            r6 = 23
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x004e }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x004e }
            if (r5 != 0) goto L_0x0046
            r0.reclaim()
            r1.reclaim()
            return
        L_0x0046:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x004c:
            r5 = move-exception
            goto L_0x0054
        L_0x004e:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x004c }
            r5.<init>()     // Catch:{ all -> 0x004c }
            throw r5     // Catch:{ all -> 0x004c }
        L_0x0054:
            r0.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelProxy.getAutoCapitalizeMode(int, ohos.miscservices.inputmethod.internal.IInputDataChannelCallback):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004c, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0053, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0054, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x005a, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x004e */
    @Override // ohos.miscservices.inputmethod.internal.IInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void getSelectedText(int r6, ohos.miscservices.inputmethod.internal.IInputDataChannelCallback r7) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputDataChannel"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x0024
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputDataChannelProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r3]
            java.lang.String r7 = "getSelectedText writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r7, r6)
            r5 = -2
            r1.writeInt(r5)
            return
        L_0x0024:
            r0.writeInt(r6)
            if (r7 == 0) goto L_0x002e
            ohos.rpc.IRemoteObject r6 = r7.asObject()
            goto L_0x002f
        L_0x002e:
            r6 = 0
        L_0x002f:
            r0.writeRemoteObject(r6)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x004e }
            r6 = 24
            r5.sendRequest(r6, r0, r1, r2)     // Catch:{ RemoteException -> 0x004e }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x004e }
            if (r5 != 0) goto L_0x0046
            r0.reclaim()
            r1.reclaim()
            return
        L_0x0046:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x004c:
            r5 = move-exception
            goto L_0x0054
        L_0x004e:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x004c }
            r5.<init>()     // Catch:{ all -> 0x004c }
            throw r5     // Catch:{ all -> 0x004c }
        L_0x0054:
            r0.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputDataChannelProxy.getSelectedText(int, ohos.miscservices.inputmethod.internal.IInputDataChannelCallback):void");
    }
}
