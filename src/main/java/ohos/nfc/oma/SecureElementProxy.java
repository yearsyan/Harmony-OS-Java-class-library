package ohos.nfc.oma;

import java.util.Optional;
import ohos.global.icu.text.Bidi;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.nfc.NfcCommProxy;
import ohos.nfc.NfcKitsUtils;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.utils.system.safwk.java.SystemAbilityDefinition;

public final class SecureElementProxy extends NfcCommProxy implements ISecureElement {
    private static final int FALSE = 0;
    private static final HiLogLabel LABEL = new HiLogLabel(3, NfcKitsUtils.NFC_DOMAIN_ID, "SecureElementProxy");
    private static final String SECURE_ELEMENT_TOKEN = "ohos.nfc.oma.ISecureElement";
    private static final int TRANSACTION_BIND_SECURE_ELEMENT = 50;
    private static final int TRUE = 1;
    private SEService mSeService = null;

    public SecureElementProxy(SEService sEService) {
        super(SystemAbilityDefinition.NFC_MANAGER_SYS_ABILITY_ID);
        this.mSeService = sEService;
    }

    private IRemoteObject getNxpService() throws RemoteException {
        IRemoteObject asObject = asObject();
        if (asObject != null) {
            MessageParcel obtain = MessageParcel.obtain();
            MessageParcel obtain2 = MessageParcel.obtain();
            MessageOption messageOption = new MessageOption();
            NfcKitsUtils.writeInterfaceToken(NfcKitsUtils.ADAPTER_DESCRIPTOR, obtain);
            try {
                if (asObject.sendRequest(12, obtain, obtain2, messageOption)) {
                    IRemoteObject readRemoteObject = obtain2.readRemoteObject();
                    obtain2.reclaim();
                    obtain.reclaim();
                    return readRemoteObject;
                }
                HiLog.error(LABEL, "getNxpService: IPC error: %{public}d", Integer.valueOf(obtain2.readInt()));
                throw new RemoteException();
            } catch (RemoteException e) {
                HiLog.error(LABEL, "getNxpService : call fail", new Object[0]);
                throw e;
            } catch (Throwable th) {
                obtain2.reclaim();
                obtain.reclaim();
                throw th;
            }
        } else {
            HiLog.error(LABEL, "getNxpService service is null", new Object[0]);
            throw new RemoteException();
        }
    }

    @Override // ohos.nfc.oma.ISecureElement
    public boolean isSeServiceConnected() throws RemoteException {
        request(14, MessageParcel.obtain());
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0069, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        ohos.hiviewdfx.HiLog.warn(ohos.nfc.oma.SecureElementProxy.LABEL, "bindSeService RemoteException failed.", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x007b, code lost:
        r2.reclaim();
        r1.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0081, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x006b */
    @Override // ohos.nfc.oma.ISecureElement
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void bindSeService(ohos.nfc.oma.SecureElementCallbackProxy r6) throws ohos.rpc.RemoteException {
        /*
        // Method dump skipped, instructions count: 130
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.nfc.oma.SecureElementProxy.bindSeService(ohos.nfc.oma.SecureElementCallbackProxy):void");
    }

    @Override // ohos.nfc.oma.ISecureElement
    public Reader[] getReaders(SEService sEService) throws RemoteException {
        if (sEService == null) {
            HiLog.warn(LABEL, "method getReaders input param is null", new Object[0]);
            return new Reader[0];
        }
        MessageParcel obtain = MessageParcel.obtain();
        NfcKitsUtils.writeInterfaceToken(NfcKitsUtils.SE_SERVICE_DESCRIPTOR, obtain);
        MessageParcel request = request(15, obtain);
        int readInt = request.readInt();
        if (readInt <= 0 || readInt > 3) {
            request.reclaim();
            return new Reader[0];
        }
        Reader[] readerArr = new Reader[readInt];
        for (int i = 0; i < readInt; i++) {
            readerArr[i] = new Reader(this, request.readString(), sEService);
        }
        request.reclaim();
        return readerArr;
    }

    @Override // ohos.nfc.oma.ISecureElement
    public boolean isSecureElementPresent(String str) throws RemoteException {
        if (str == null || str.length() == 0) {
            HiLog.warn(LABEL, "method isSecureElementPresent input param is null or empty", new Object[0]);
            return false;
        }
        MessageParcel obtain = MessageParcel.obtain();
        obtain.writeString(SECURE_ELEMENT_TOKEN);
        obtain.writeString(str);
        MessageParcel request = request(16, obtain);
        int readInt = request.readInt();
        request.reclaim();
        HiLog.info(LABEL, "isPresent: present = %{public}d, name = %{public}s", Integer.valueOf(readInt), str);
        if (readInt == 1) {
            return true;
        }
        return false;
    }

    @Override // ohos.nfc.oma.ISecureElement
    public Optional<Channel> openBasicChannel(Session session, byte[] bArr) throws RemoteException {
        if (session == null || bArr == null || bArr.length == 0) {
            HiLog.warn(LABEL, "method openBasicChannel input param is null or empty", new Object[0]);
            return Optional.empty();
        }
        MessageParcel obtain = MessageParcel.obtain();
        obtain.writeString(SECURE_ELEMENT_TOKEN);
        obtain.writeRemoteObject(session.getSessionObject());
        obtain.writeInt(0);
        obtain.writeByteArray(bArr);
        obtain.writeByte((byte) 0);
        obtain.writeRemoteObject(session.getSEService().getCallbackProxy().asObject());
        MessageParcel request = request(17, obtain);
        IRemoteObject readRemoteObject = request.readRemoteObject();
        request.reclaim();
        if (readRemoteObject != null) {
            return Optional.of(new Channel(session.getSEService(), this, readRemoteObject, 0));
        }
        HiLog.error(LABEL, "openBasicChannel: readRemoteObject is null", new Object[0]);
        return Optional.empty();
    }

    @Override // ohos.nfc.oma.ISecureElement
    public Optional<Channel> openLogicalChannel(Session session, byte[] bArr) throws RemoteException {
        if (session == null || bArr == null || bArr.length == 0) {
            HiLog.warn(LABEL, "method openLogicalChannel input param is null or empty", new Object[0]);
            return Optional.empty();
        }
        MessageParcel obtain = MessageParcel.obtain();
        obtain.writeString(SECURE_ELEMENT_TOKEN);
        obtain.writeRemoteObject(session.getSessionObject());
        obtain.writeInt(1);
        obtain.writeByteArray(bArr);
        obtain.writeByte(Bidi.LEVEL_DEFAULT_RTL);
        obtain.writeRemoteObject(session.getSEService().getCallbackProxy().asObject());
        MessageParcel request = request(17, obtain);
        IRemoteObject readRemoteObject = request.readRemoteObject();
        request.reclaim();
        if (readRemoteObject != null) {
            return Optional.of(new Channel(session.getSEService(), this, readRemoteObject, 1));
        }
        HiLog.error(LABEL, "openLogicalChannel: readRemoteObject is null", new Object[0]);
        return Optional.empty();
    }

    @Override // ohos.nfc.oma.ISecureElement
    public byte[] transmit(IRemoteObject iRemoteObject, byte[] bArr) throws RemoteException {
        if (iRemoteObject == null || bArr == null || bArr.length == 0) {
            HiLog.warn(LABEL, "method transmit input param is null or empty", new Object[0]);
            return new byte[0];
        }
        MessageParcel obtain = MessageParcel.obtain();
        obtain.writeString(SECURE_ELEMENT_TOKEN);
        obtain.writeRemoteObject(iRemoteObject);
        obtain.writeByteArray(bArr);
        MessageParcel request = request(18, obtain);
        byte[] readByteArray = request.readByteArray();
        request.reclaim();
        return readByteArray;
    }

    @Override // ohos.nfc.oma.ISecureElement
    public byte[] getSelectResponse(IRemoteObject iRemoteObject) throws RemoteException {
        if (iRemoteObject == null) {
            HiLog.warn(LABEL, "method getSelectResponse input param is null", new Object[0]);
            return new byte[0];
        }
        MessageParcel obtain = MessageParcel.obtain();
        obtain.writeString(SECURE_ELEMENT_TOKEN);
        obtain.writeRemoteObject(iRemoteObject);
        MessageParcel request = request(25, obtain);
        byte[] readByteArray = request.readByteArray();
        request.reclaim();
        return readByteArray;
    }

    @Override // ohos.nfc.oma.ISecureElement
    public Optional<Session> openSession(Reader reader) throws RemoteException {
        if (reader == null) {
            HiLog.warn(LABEL, "method openSession input param is null", new Object[0]);
            return Optional.empty();
        }
        MessageParcel obtain = MessageParcel.obtain();
        obtain.writeString(SECURE_ELEMENT_TOKEN);
        obtain.writeString(reader.getName());
        MessageParcel request = request(26, obtain);
        IRemoteObject readRemoteObject = request.readRemoteObject();
        request.reclaim();
        if (readRemoteObject != null) {
            return Optional.of(new Session(this.mSeService, this, readRemoteObject));
        }
        HiLog.error(LABEL, "openSession: readRemoteObject is null", new Object[0]);
        return Optional.empty();
    }

    @Override // ohos.nfc.oma.ISecureElement
    public byte[] getATR(IRemoteObject iRemoteObject) throws RemoteException {
        if (iRemoteObject == null) {
            HiLog.warn(LABEL, "method getATR input param is null", new Object[0]);
            return new byte[0];
        }
        MessageParcel obtain = MessageParcel.obtain();
        obtain.writeString(SECURE_ELEMENT_TOKEN);
        obtain.writeRemoteObject(iRemoteObject);
        MessageParcel request = request(29, obtain);
        byte[] readByteArray = request.readByteArray();
        request.reclaim();
        return readByteArray;
    }

    @Override // ohos.nfc.oma.ISecureElement
    public void closeSeSessions(Reader reader) throws RemoteException {
        if (reader == null) {
            HiLog.warn(LABEL, "method closeSeSessions input param is null", new Object[0]);
            return;
        }
        MessageParcel obtain = MessageParcel.obtain();
        obtain.writeString(SECURE_ELEMENT_TOKEN);
        obtain.writeString(reader.getName());
        request(27, obtain).reclaim();
    }

    @Override // ohos.nfc.oma.ISecureElement
    public void close(IRemoteObject iRemoteObject) throws RemoteException {
        if (iRemoteObject == null) {
            HiLog.warn(LABEL, "method close input param is null", new Object[0]);
            return;
        }
        MessageParcel obtain = MessageParcel.obtain();
        obtain.writeString(SECURE_ELEMENT_TOKEN);
        obtain.writeRemoteObject(iRemoteObject);
        request(28, obtain).reclaim();
    }

    @Override // ohos.nfc.oma.ISecureElement
    public void closeChannel(IRemoteObject iRemoteObject) throws RemoteException {
        if (iRemoteObject == null) {
            HiLog.warn(LABEL, "method closeChannel input param is null", new Object[0]);
            return;
        }
        MessageParcel obtain = MessageParcel.obtain();
        obtain.writeString(SECURE_ELEMENT_TOKEN);
        obtain.writeRemoteObject(iRemoteObject);
        request(19, obtain).reclaim();
    }

    @Override // ohos.nfc.oma.ISecureElement
    public boolean isChannelClosed(IRemoteObject iRemoteObject) throws RemoteException {
        if (iRemoteObject == null) {
            HiLog.warn(LABEL, "method isChannelClosed input param is null", new Object[0]);
            return false;
        }
        MessageParcel obtain = MessageParcel.obtain();
        obtain.writeString(SECURE_ELEMENT_TOKEN);
        obtain.writeRemoteObject(iRemoteObject);
        MessageParcel request = request(30, obtain);
        boolean readBoolean = request.readBoolean();
        request.reclaim();
        return readBoolean;
    }
}
