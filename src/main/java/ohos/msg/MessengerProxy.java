package ohos.msg;

import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;

class MessengerProxy implements IMessenger {
    private static final HiLogLabel TAG = new HiLogLabel(3, 0, "MessengerProxy");
    private final IRemoteObject mRemoteObject;

    MessengerProxy(IRemoteObject iRemoteObject) {
        this.mRemoteObject = iRemoteObject;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this.mRemoteObject;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:10|11|12) */
    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.msg.MessengerProxy.TAG, "fail to sendMessage in transcation", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0067, code lost:
        throw new ohos.msg.MessengerException("SendMessage failed in transcat", -1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0068, code lost:
        r3.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x006e, code lost:
        throw r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0055, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x0057 */
    @Override // ohos.msg.IMessenger
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int sendMessage(ohos.msg.Message r9) throws ohos.msg.MessengerException {
        /*
        // Method dump skipped, instructions count: 111
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.msg.MessengerProxy.sendMessage(ohos.msg.Message):int");
    }
}
