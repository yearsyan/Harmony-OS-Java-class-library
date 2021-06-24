package ohos.miscservices.inputmethod.internal;

import ohos.hiviewdfx.HiLogLabel;
import ohos.miscservices.inputmethod.RecommendationInfo;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageParcel;

public class InputMethodAgentProxy implements IInputMethodAgent {
    private static final String DESCRIPTOR = "ohos.miscservices.inputmethod.internal.IInputMethodAgent";
    private static final int DISPATCH_EVENT = 31;
    private static final int ERR_OK = 0;
    private static final int NOTIFY_CURSOR_CHANGED = 4;
    private static final int NOTIFY_EDITING_TEXT_CHANGED = 2;
    private static final int NOTIFY_SELECTION_CHANGED = 1;
    private static final int SEND_RECOMMENDATION_INFO = 3;
    private static final HiLogLabel TAG = new HiLogLabel(3, 218110976, "InputMethodAgentProxy");
    private static final int WRITE_MSG_ERROR = -2;
    private final IRemoteObject remote;

    public InputMethodAgentProxy(IRemoteObject iRemoteObject) {
        this.remote = iRemoteObject;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this.remote;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:13|14|15) */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0052, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0059, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x005a, code lost:
        r0.reclaim();
        r2.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0060, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0054 */
    @Override // ohos.miscservices.inputmethod.internal.IInputMethodAgent
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void notifySelectionChanged(int r6, int r7, int r8, int r9) throws ohos.rpc.RemoteException {
        /*
            r5 = this;
            ohos.hiviewdfx.HiLogLabel r0 = ohos.miscservices.inputmethod.internal.InputMethodAgentProxy.TAG
            r1 = 0
            java.lang.Object[] r2 = new java.lang.Object[r1]
            java.lang.String r3 = "notifySelectionChanged"
            ohos.hiviewdfx.HiLog.debug(r0, r3, r2)
            ohos.rpc.MessageParcel r0 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageParcel r2 = ohos.rpc.MessageParcel.obtain()
            ohos.rpc.MessageOption r3 = new ohos.rpc.MessageOption
            r3.<init>(r1)
            java.lang.String r4 = "ohos.miscservices.inputmethod.internal.IInputMethodAgent"
            boolean r4 = r0.writeInterfaceToken(r4)
            if (r4 != 0) goto L_0x002d
            ohos.hiviewdfx.HiLogLabel r5 = ohos.miscservices.inputmethod.internal.InputMethodAgentProxy.TAG
            java.lang.Object[] r6 = new java.lang.Object[r1]
            java.lang.String r7 = "notifySelectionChanged writeInterfaceToken failed."
            ohos.hiviewdfx.HiLog.error(r5, r7, r6)
            r5 = -2
            r2.writeInt(r5)
            return
        L_0x002d:
            r0.writeInt(r6)
            r0.writeInt(r7)
            r0.writeInt(r8)
            r0.writeInt(r9)
            ohos.rpc.IRemoteObject r5 = r5.remote     // Catch:{ RemoteException -> 0x0054 }
            r6 = 1
            r5.sendRequest(r6, r0, r2, r3)     // Catch:{ RemoteException -> 0x0054 }
            int r5 = r2.readInt()     // Catch:{ RemoteException -> 0x0054 }
            if (r5 != 0) goto L_0x004c
            r0.reclaim()
            r2.reclaim()
            return
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
            r2.reclaim()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputMethodAgentProxy.notifySelectionChanged(int, int, int, int):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0057, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x005e, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x005f, code lost:
        r0.reclaim();
        r2.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0065, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0059 */
    @Override // ohos.miscservices.inputmethod.internal.IInputMethodAgent
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void sendRecommendationInfo(ohos.miscservices.inputmethod.RecommendationInfo[] r6) throws ohos.rpc.RemoteException {
        /*
        // Method dump skipped, instructions count: 102
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputMethodAgentProxy.sendRecommendationInfo(ohos.miscservices.inputmethod.RecommendationInfo[]):void");
    }

    private void marshallingRecommendationInfos(RecommendationInfo[] recommendationInfoArr, MessageParcel messageParcel) {
        for (RecommendationInfo recommendationInfo : recommendationInfoArr) {
            if (recommendationInfo == null) {
                messageParcel.writeBoolean(true);
            } else {
                messageParcel.writeBoolean(false);
                recommendationInfo.marshalling(messageParcel);
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:16|17|18) */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0056, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x005d, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x005e, code lost:
        r0.reclaim();
        r2.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0064, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:16:0x0058 */
    @Override // ohos.miscservices.inputmethod.internal.IInputMethodAgent
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void notifyEditingTextChanged(int r6, ohos.miscservices.inputmethod.EditingText r7) throws ohos.rpc.RemoteException {
        /*
        // Method dump skipped, instructions count: 101
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputMethodAgentProxy.notifyEditingTextChanged(int, ohos.miscservices.inputmethod.EditingText):void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:10|11|12) */
    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.miscservices.inputmethod.internal.InputMethodAgentProxy.TAG, "notifyCursorCoordinateChanged send msg failed", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0063, code lost:
        throw new ohos.rpc.RemoteException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0064, code lost:
        r0.reclaim();
        r2.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x006a, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0053, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0055 */
    @Override // ohos.miscservices.inputmethod.internal.IInputMethodAgent
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void notifyCursorCoordinateChanged(float r5, float r6, float r7, float[] r8) throws ohos.rpc.RemoteException {
        /*
        // Method dump skipped, instructions count: 107
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputMethodAgentProxy.notifyCursorCoordinateChanged(float, float, float, float[]):void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:12|13|14|15) */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0055, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        ohos.hiviewdfx.HiLog.debug(ohos.miscservices.inputmethod.internal.InputMethodAgentProxy.TAG, "send msg failed", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0066, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0067, code lost:
        r0.reclaim();
        r2.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x006d, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:12:0x0057 */
    @Override // ohos.miscservices.inputmethod.internal.IInputMethodAgent
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean dispatchMultimodalEvent(ohos.multimodalinput.event.MultimodalEvent r7) throws ohos.rpc.RemoteException {
        /*
        // Method dump skipped, instructions count: 110
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.miscservices.inputmethod.internal.InputMethodAgentProxy.dispatchMultimodalEvent(ohos.multimodalinput.event.MultimodalEvent):boolean");
    }
}
