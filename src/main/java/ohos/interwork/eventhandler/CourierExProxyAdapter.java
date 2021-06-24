package ohos.interwork.eventhandler;

import ohos.eventhandler.Courier;
import ohos.eventhandler.InnerEvent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;

/* access modifiers changed from: package-private */
public final class CourierExProxyAdapter {
    private static final String DESCRIPTOR = "android.os.IMessenger";
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218108208, "CourierExProxyAdapter");
    private static final int LOG_DOMAIN = 218108208;
    private static final int SEND = 1;
    private IRemoteObject mRemote;

    CourierExProxyAdapter(IRemoteObject iRemoteObject) {
        this.mRemote = iRemoteObject;
    }

    /* access modifiers changed from: package-private */
    public void send(InnerEvent innerEvent) throws RemoteException {
        HiLog.debug(LABEL, "Proxy::send called", new Object[0]);
        if (this.mRemote == null) {
            HiLog.error(LABEL, "Proxy::send remote is null", new Object[0]);
            return;
        }
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        MessageOption messageOption = new MessageOption();
        try {
            if (obtain.writeInterfaceToken("android.os.IMessenger")) {
                writeMessageToData(innerEvent, obtain);
                this.mRemote.sendRequest(1, obtain, obtain2, messageOption);
                obtain.reclaim();
                obtain2.reclaim();
            }
        } finally {
            obtain.reclaim();
            obtain2.reclaim();
        }
    }

    private void writeMessageToData(InnerEvent innerEvent, MessageParcel messageParcel) {
        if (innerEvent == null) {
            messageParcel.writeInt(0);
            return;
        }
        messageParcel.writeInt(1);
        messageParcel.writeInt(innerEvent.eventId);
        IRemoteObject iRemoteObject = null;
        InnerEventExInfo innerEventExInfo = innerEvent.object instanceof InnerEventExInfo ? (InnerEventExInfo) innerEvent.object : null;
        if (innerEventExInfo != null) {
            messageParcel.writeInt(innerEventExInfo.arg1);
            messageParcel.writeInt(innerEventExInfo.arg2);
        } else {
            messageParcel.writeInt(0);
            messageParcel.writeInt(0);
        }
        messageParcel.writeInt(0);
        messageParcel.writeLong(innerEvent.getHandleTime());
        if (innerEventExInfo != null) {
            messageParcel.writePacMapEx(innerEventExInfo.pacMapEx);
        } else {
            messageParcel.writePacMapEx(null);
        }
        Courier courier = innerEvent.replyTo;
        if (courier != null) {
            iRemoteObject = courier.getRemoteObject();
        }
        messageParcel.writeRemoteObject(iRemoteObject);
        messageParcel.writeInt(innerEvent.sendingUid);
        messageParcel.writeInt(-1);
    }
}
