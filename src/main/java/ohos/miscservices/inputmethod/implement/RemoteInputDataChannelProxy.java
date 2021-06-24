package ohos.miscservices.inputmethod.implement;

import ohos.miscservices.inputmethod.interfaces.IRemoteInputDataChannel;
import ohos.rpc.IRemoteObject;

public class RemoteInputDataChannelProxy implements IRemoteInputDataChannel {
    private static final int COMMAND_CLOSE = 12;
    private static final int COMMAND_DELETE_BACKWARD = 3;
    private static final int COMMAND_DELETE_FORWARD = 4;
    private static final int COMMAND_GET_BACKWARD = 6;
    private static final int COMMAND_GET_FORWARD = 5;
    private static final int COMMAND_INSERT_RICH_CONTENT = 2;
    private static final int COMMAND_INSERT_TEXT = 1;
    private static final int COMMAND_MARK_TEXT = 7;
    private static final int COMMAND_REPLACE_MARKED_TEXT = 9;
    private static final int COMMAND_SELECT_TEXT = 15;
    private static final int COMMAND_SEND_CUSTOMIZED_DATA = 13;
    private static final int COMMAND_SEND_KEY_EVENT = 11;
    private static final int COMMAND_SEND_KEY_FUNCTION = 14;
    private static final int COMMAND_SUBSCRIBE_EDITING_TEXT = 10;
    private static final int COMMAND_UNMARK_TEXT = 8;
    private static final int ERR_OK = 0;
    private final IRemoteObject remote;

    public RemoteInputDataChannelProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this.remote;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:12|13|14) */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0032, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0039, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003a, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0040, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0034 */
    @Override // ohos.miscservices.inputmethod.interfaces.IRemoteInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean insertText(java.lang.String r5) throws ohos.rpc.RemoteException {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            r0.writeString(r5)
            ohos.rpc.IRemoteObject r4 = r4.remote     // Catch:{ RemoteException -> 0x0034 }
            r5 = 1
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x0034 }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0034 }
            if (r4 != 0) goto L_0x002c
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0034 }
            if (r4 != r5) goto L_0x0024
            goto L_0x0025
        L_0x0024:
            r5 = r3
        L_0x0025:
            r0.reclaim()
            r1.reclaim()
            return r5
        L_0x002c:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException
            r4.<init>()
            throw r4
        L_0x0032:
            r4 = move-exception
            goto L_0x003a
        L_0x0034:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0032 }
            r4.<init>()     // Catch:{ all -> 0x0032 }
            throw r4     // Catch:{ all -> 0x0032 }
        L_0x003a:
            r0.reclaim()
            r1.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.implement.RemoteInputDataChannelProxy.insertText(java.lang.String):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:13|14|15) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0033, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003a, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003b, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0041, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0035 */
    @Override // ohos.miscservices.inputmethod.interfaces.IRemoteInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean insertRichContent(ohos.miscservices.inputmethod.RichContent r5) throws ohos.rpc.RemoteException {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            r5.marshalling(r0)
            ohos.rpc.IRemoteObject r4 = r4.remote     // Catch:{ RemoteException -> 0x0035 }
            r5 = 2
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x0035 }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0035 }
            if (r4 != 0) goto L_0x002d
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0035 }
            r5 = 1
            if (r4 != r5) goto L_0x0025
            goto L_0x0026
        L_0x0025:
            r5 = r3
        L_0x0026:
            r0.reclaim()
            r1.reclaim()
            return r5
        L_0x002d:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException
            r4.<init>()
            throw r4
        L_0x0033:
            r4 = move-exception
            goto L_0x003b
        L_0x0035:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0033 }
            r4.<init>()     // Catch:{ all -> 0x0033 }
            throw r4     // Catch:{ all -> 0x0033 }
        L_0x003b:
            r0.reclaim()
            r1.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.implement.RemoteInputDataChannelProxy.insertRichContent(ohos.miscservices.inputmethod.RichContent):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:13|14|15) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0033, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003a, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003b, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0041, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0035 */
    @Override // ohos.miscservices.inputmethod.interfaces.IRemoteInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean deleteBackward(int r5) throws ohos.rpc.RemoteException {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            r0.writeInt(r5)
            ohos.rpc.IRemoteObject r4 = r4.remote     // Catch:{ RemoteException -> 0x0035 }
            r5 = 3
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x0035 }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0035 }
            if (r4 != 0) goto L_0x002d
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0035 }
            r5 = 1
            if (r4 != r5) goto L_0x0025
            goto L_0x0026
        L_0x0025:
            r5 = r3
        L_0x0026:
            r0.reclaim()
            r1.reclaim()
            return r5
        L_0x002d:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException
            r4.<init>()
            throw r4
        L_0x0033:
            r4 = move-exception
            goto L_0x003b
        L_0x0035:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0033 }
            r4.<init>()     // Catch:{ all -> 0x0033 }
            throw r4     // Catch:{ all -> 0x0033 }
        L_0x003b:
            r0.reclaim()
            r1.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.implement.RemoteInputDataChannelProxy.deleteBackward(int):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:13|14|15) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0033, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003a, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003b, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0041, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0035 */
    @Override // ohos.miscservices.inputmethod.interfaces.IRemoteInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean deleteForward(int r5) throws ohos.rpc.RemoteException {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            r0.writeInt(r5)
            ohos.rpc.IRemoteObject r4 = r4.remote     // Catch:{ RemoteException -> 0x0035 }
            r5 = 4
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x0035 }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0035 }
            if (r4 != 0) goto L_0x002d
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0035 }
            r5 = 1
            if (r4 != r5) goto L_0x0025
            goto L_0x0026
        L_0x0025:
            r5 = r3
        L_0x0026:
            r0.reclaim()
            r1.reclaim()
            return r5
        L_0x002d:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException
            r4.<init>()
            throw r4
        L_0x0033:
            r4 = move-exception
            goto L_0x003b
        L_0x0035:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0033 }
            r4.<init>()     // Catch:{ all -> 0x0033 }
            throw r4     // Catch:{ all -> 0x0033 }
        L_0x003b:
            r0.reclaim()
            r1.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.implement.RemoteInputDataChannelProxy.deleteForward(int):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:10|11|12) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0035, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0036, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003c, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x002e, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0030 */
    @Override // ohos.miscservices.inputmethod.interfaces.IRemoteInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getForward(int r5) throws ohos.rpc.RemoteException {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            r0.writeInt(r5)
            ohos.rpc.IRemoteObject r4 = r4.remote     // Catch:{ RemoteException -> 0x0030 }
            r5 = 5
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x0030 }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0030 }
            if (r4 != 0) goto L_0x0028
            java.lang.String r4 = r1.readString()     // Catch:{ RemoteException -> 0x0030 }
            r0.reclaim()
            r1.reclaim()
            return r4
        L_0x0028:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException
            r4.<init>()
            throw r4
        L_0x002e:
            r4 = move-exception
            goto L_0x0036
        L_0x0030:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x002e }
            r4.<init>()     // Catch:{ all -> 0x002e }
            throw r4     // Catch:{ all -> 0x002e }
        L_0x0036:
            r0.reclaim()
            r1.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.implement.RemoteInputDataChannelProxy.getForward(int):java.lang.String");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:10|11|12) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0035, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0036, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003c, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x002e, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0030 */
    @Override // ohos.miscservices.inputmethod.interfaces.IRemoteInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getBackward(int r5) throws ohos.rpc.RemoteException {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            r0.writeInt(r5)
            ohos.rpc.IRemoteObject r4 = r4.remote     // Catch:{ RemoteException -> 0x0030 }
            r5 = 6
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x0030 }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0030 }
            if (r4 != 0) goto L_0x0028
            java.lang.String r4 = r1.readString()     // Catch:{ RemoteException -> 0x0030 }
            r0.reclaim()
            r1.reclaim()
            return r4
        L_0x0028:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException
            r4.<init>()
            throw r4
        L_0x002e:
            r4 = move-exception
            goto L_0x0036
        L_0x0030:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x002e }
            r4.<init>()     // Catch:{ all -> 0x002e }
            throw r4     // Catch:{ all -> 0x002e }
        L_0x0036:
            r0.reclaim()
            r1.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.implement.RemoteInputDataChannelProxy.getBackward(int):java.lang.String");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:13|14|15) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0036, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003d, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003e, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0044, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0038 */
    @Override // ohos.miscservices.inputmethod.interfaces.IRemoteInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean markText(int r5, int r6) throws ohos.rpc.RemoteException {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            r0.writeInt(r5)
            r0.writeInt(r6)
            ohos.rpc.IRemoteObject r4 = r4.remote     // Catch:{ RemoteException -> 0x0038 }
            r5 = 7
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x0038 }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0038 }
            if (r4 != 0) goto L_0x0030
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0038 }
            r5 = 1
            if (r4 != r5) goto L_0x0028
            goto L_0x0029
        L_0x0028:
            r5 = r3
        L_0x0029:
            r0.reclaim()
            r1.reclaim()
            return r5
        L_0x0030:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException
            r4.<init>()
            throw r4
        L_0x0036:
            r4 = move-exception
            goto L_0x003e
        L_0x0038:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0036 }
            r4.<init>()     // Catch:{ all -> 0x0036 }
            throw r4     // Catch:{ all -> 0x0036 }
        L_0x003e:
            r0.reclaim()
            r1.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.implement.RemoteInputDataChannelProxy.markText(int, int):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:13|14|15) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0031, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0038, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0039, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x003f, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0033 */
    @Override // ohos.miscservices.inputmethod.interfaces.IRemoteInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean unmarkText() throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x0033 }
            r4 = 8
            r5.sendRequest(r4, r0, r1, r2)     // Catch:{ RemoteException -> 0x0033 }
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x0033 }
            if (r5 != 0) goto L_0x002b
            int r5 = r1.readInt()     // Catch:{ RemoteException -> 0x0033 }
            r2 = 1
            if (r5 != r2) goto L_0x0023
            goto L_0x0024
        L_0x0023:
            r2 = r3
        L_0x0024:
            r0.reclaim()
            r1.reclaim()
            return r2
        L_0x002b:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException
            r5.<init>()
            throw r5
        L_0x0031:
            r5 = move-exception
            goto L_0x0039
        L_0x0033:
            ohos.rpc.RemoteException r5 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0031 }
            r5.<init>()     // Catch:{ all -> 0x0031 }
            throw r5     // Catch:{ all -> 0x0031 }
        L_0x0039:
            r0.reclaim()
            r1.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.implement.RemoteInputDataChannelProxy.unmarkText():boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:13|14|15) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0034, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003b, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003c, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0042, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0036 */
    @Override // ohos.miscservices.inputmethod.interfaces.IRemoteInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean replaceMarkedText(java.lang.String r5) throws ohos.rpc.RemoteException {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            r0.writeString(r5)
            ohos.rpc.IRemoteObject r4 = r4.remote     // Catch:{ RemoteException -> 0x0036 }
            r5 = 9
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x0036 }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0036 }
            if (r4 != 0) goto L_0x002e
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0036 }
            r5 = 1
            if (r4 != r5) goto L_0x0026
            goto L_0x0027
        L_0x0026:
            r5 = r3
        L_0x0027:
            r0.reclaim()
            r1.reclaim()
            return r5
        L_0x002e:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException
            r4.<init>()
            throw r4
        L_0x0034:
            r4 = move-exception
            goto L_0x003c
        L_0x0036:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0034 }
            r4.<init>()     // Catch:{ all -> 0x0034 }
            throw r4     // Catch:{ all -> 0x0034 }
        L_0x003c:
            r0.reclaim()
            r1.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.implement.RemoteInputDataChannelProxy.replaceMarkedText(java.lang.String):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:10|11|12) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x003a, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x003b, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0041, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0033, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0035 */
    @Override // ohos.miscservices.inputmethod.interfaces.IRemoteInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ohos.miscservices.inputmethod.EditingText subscribeEditingText(ohos.miscservices.inputmethod.EditingCapability r5) throws ohos.rpc.RemoteException {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            r5.marshalling(r0)
            ohos.rpc.IRemoteObject r4 = r4.remote     // Catch:{ RemoteException -> 0x0035 }
            r5 = 10
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x0035 }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0035 }
            if (r4 != 0) goto L_0x002d
            ohos.miscservices.inputmethod.EditingText r4 = new ohos.miscservices.inputmethod.EditingText     // Catch:{ RemoteException -> 0x0035 }
            r4.<init>()     // Catch:{ RemoteException -> 0x0035 }
            r4.unmarshalling(r1)     // Catch:{ RemoteException -> 0x0035 }
            r0.reclaim()
            r1.reclaim()
            return r4
        L_0x002d:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException
            r4.<init>()
            throw r4
        L_0x0033:
            r4 = move-exception
            goto L_0x003b
        L_0x0035:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0033 }
            r4.<init>()     // Catch:{ all -> 0x0033 }
            throw r4     // Catch:{ all -> 0x0033 }
        L_0x003b:
            r0.reclaim()
            r1.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.implement.RemoteInputDataChannelProxy.subscribeEditingText(ohos.miscservices.inputmethod.EditingCapability):ohos.miscservices.inputmethod.EditingText");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x002f, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0030, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0036, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0028, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x002a */
    @Override // ohos.miscservices.inputmethod.interfaces.IRemoteInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void close() throws ohos.rpc.RemoteException {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            ohos.rpc.IRemoteObject r4 = r4.remote     // Catch:{ RemoteException -> 0x002a }
            r3 = 12
            r4.sendRequest(r3, r0, r1, r2)     // Catch:{ RemoteException -> 0x002a }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x002a }
            if (r4 != 0) goto L_0x0022
            r0.reclaim()
            r1.reclaim()
            return
        L_0x0022:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException
            r4.<init>()
            throw r4
        L_0x0028:
            r4 = move-exception
            goto L_0x0030
        L_0x002a:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0028 }
            r4.<init>()     // Catch:{ all -> 0x0028 }
            throw r4     // Catch:{ all -> 0x0028 }
        L_0x0030:
            r0.reclaim()
            r1.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.implement.RemoteInputDataChannelProxy.close():void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0037, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003e, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003f, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0045, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0039 */
    @Override // ohos.miscservices.inputmethod.interfaces.IRemoteInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean sendCustomizedData(java.lang.String r5, ohos.utils.PacMap r6) throws ohos.rpc.RemoteException {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            r0.writeString(r5)
            r6.marshalling(r0)
            ohos.rpc.IRemoteObject r4 = r4.remote     // Catch:{ RemoteException -> 0x0039 }
            r5 = 13
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x0039 }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0039 }
            if (r4 != 0) goto L_0x0031
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0039 }
            r5 = 1
            if (r4 != r5) goto L_0x0029
            goto L_0x002a
        L_0x0029:
            r5 = r3
        L_0x002a:
            r0.reclaim()
            r1.reclaim()
            return r5
        L_0x0031:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException
            r4.<init>()
            throw r4
        L_0x0037:
            r4 = move-exception
            goto L_0x003f
        L_0x0039:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0037 }
            r4.<init>()     // Catch:{ all -> 0x0037 }
            throw r4     // Catch:{ all -> 0x0037 }
        L_0x003f:
            r0.reclaim()
            r1.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.implement.RemoteInputDataChannelProxy.sendCustomizedData(java.lang.String, ohos.utils.PacMap):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:13|14|15) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0034, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003b, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003c, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0042, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0036 */
    @Override // ohos.miscservices.inputmethod.interfaces.IRemoteInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean sendKeyEvent(ohos.multimodalinput.event.KeyEvent r5) throws ohos.rpc.RemoteException {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            r5.marshalling(r0)
            ohos.rpc.IRemoteObject r4 = r4.remote     // Catch:{ RemoteException -> 0x0036 }
            r5 = 11
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x0036 }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0036 }
            if (r4 != 0) goto L_0x002e
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0036 }
            r5 = 1
            if (r4 != r5) goto L_0x0026
            goto L_0x0027
        L_0x0026:
            r5 = r3
        L_0x0027:
            r0.reclaim()
            r1.reclaim()
            return r5
        L_0x002e:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException
            r4.<init>()
            throw r4
        L_0x0034:
            r4 = move-exception
            goto L_0x003c
        L_0x0036:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0034 }
            r4.<init>()     // Catch:{ all -> 0x0034 }
            throw r4     // Catch:{ all -> 0x0034 }
        L_0x003c:
            r0.reclaim()
            r1.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.implement.RemoteInputDataChannelProxy.sendKeyEvent(ohos.multimodalinput.event.KeyEvent):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:13|14|15) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0034, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003b, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003c, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0042, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0036 */
    @Override // ohos.miscservices.inputmethod.interfaces.IRemoteInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean sendKeyFunction(int r5) throws ohos.rpc.RemoteException {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            r0.writeInt(r5)
            ohos.rpc.IRemoteObject r4 = r4.remote     // Catch:{ RemoteException -> 0x0036 }
            r5 = 14
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x0036 }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0036 }
            if (r4 != 0) goto L_0x002e
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0036 }
            r5 = 1
            if (r4 != r5) goto L_0x0026
            goto L_0x0027
        L_0x0026:
            r5 = r3
        L_0x0027:
            r0.reclaim()
            r1.reclaim()
            return r5
        L_0x002e:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException
            r4.<init>()
            throw r4
        L_0x0034:
            r4 = move-exception
            goto L_0x003c
        L_0x0036:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0034 }
            r4.<init>()     // Catch:{ all -> 0x0034 }
            throw r4     // Catch:{ all -> 0x0034 }
        L_0x003c:
            r0.reclaim()
            r1.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.implement.RemoteInputDataChannelProxy.sendKeyFunction(int):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0037, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x003e, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x003f, code lost:
        r0.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0045, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0039 */
    @Override // ohos.miscservices.inputmethod.interfaces.IRemoteInputDataChannel
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean selectText(int r5, int r6) throws ohos.rpc.RemoteException {
        /*
            r4 = this;
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r1 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r2 = new ohos.rpc.MessageOption
            r3 = 0
            r2.<init>(r3)
            r0.writeInt(r5)
            r0.writeInt(r6)
            ohos.rpc.IRemoteObject r4 = r4.remote     // Catch:{ RemoteException -> 0x0039 }
            r5 = 15
            r4.sendRequest(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x0039 }
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0039 }
            if (r4 != 0) goto L_0x0031
            int r4 = r1.readInt()     // Catch:{ RemoteException -> 0x0039 }
            r5 = 1
            if (r4 != r5) goto L_0x0029
            goto L_0x002a
        L_0x0029:
            r5 = r3
        L_0x002a:
            r0.reclaim()
            r1.reclaim()
            return r5
        L_0x0031:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException
            r4.<init>()
            throw r4
        L_0x0037:
            r4 = move-exception
            goto L_0x003f
        L_0x0039:
            ohos.rpc.RemoteException r4 = new ohos.rpc.RemoteException     // Catch:{ all -> 0x0037 }
            r4.<init>()     // Catch:{ all -> 0x0037 }
            throw r4     // Catch:{ all -> 0x0037 }
        L_0x003f:
            r0.reclaim()
            r1.reclaim()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.implement.RemoteInputDataChannelProxy.selectText(int, int):boolean");
    }
}
