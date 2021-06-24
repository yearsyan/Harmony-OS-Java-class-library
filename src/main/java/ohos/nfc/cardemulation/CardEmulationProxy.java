package ohos.nfc.cardemulation;

import ohos.bundle.ElementName;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.nfc.NfcCommProxy;
import ohos.nfc.NfcKitsUtils;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.utils.Sequenceable;
import ohos.utils.system.safwk.java.SystemAbilityDefinition;

/* access modifiers changed from: package-private */
public class CardEmulationProxy extends NfcCommProxy implements ICardEmulation {
    private static final HiLogLabel LABEL = new HiLogLabel(3, NfcKitsUtils.NFC_DOMAIN_ID, "CardEmulationProxy");
    private static CardEmulationProxy sCardEmulationProxy;

    private CardEmulationProxy() {
        super(SystemAbilityDefinition.NFC_MANAGER_SYS_ABILITY_ID);
    }

    public static synchronized CardEmulationProxy getInstance() {
        CardEmulationProxy cardEmulationProxy;
        synchronized (CardEmulationProxy.class) {
            if (sCardEmulationProxy == null) {
                sCardEmulationProxy = new CardEmulationProxy();
            }
            cardEmulationProxy = sCardEmulationProxy;
        }
        return cardEmulationProxy;
    }

    @Override // ohos.nfc.cardemulation.ICardEmulation
    public void setListenMode(int i) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        NfcKitsUtils.writeInterfaceToken(NfcKitsUtils.NXP_ADAPTER_DESCRIPTOR, obtain);
        obtain.writeInt(i);
        obtain.writeInt(1);
        request(20, obtain).reclaim();
    }

    @Override // ohos.nfc.cardemulation.ICardEmulation
    public boolean isListenModeEnabled() throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        NfcKitsUtils.writeInterfaceToken(NfcKitsUtils.NXP_ADAPTER_DESCRIPTOR, obtain);
        MessageParcel request = request(21, obtain);
        boolean readBoolean = request.readBoolean();
        request.reclaim();
        return readBoolean;
    }

    @Override // ohos.nfc.cardemulation.ICardEmulation
    public String getNfcInfo(String str) throws RemoteException {
        if (str == null || str.length() == 0) {
            HiLog.warn(LABEL, "method getNfcInfo input param is null or empty", new Object[0]);
            return "";
        }
        MessageParcel obtain = MessageParcel.obtain();
        NfcKitsUtils.writeInterfaceToken(NfcKitsUtils.NXP_ADAPTER_DESCRIPTOR, obtain);
        obtain.writeString(str);
        MessageParcel request = request(22, obtain);
        String readString = request.readString();
        request.reclaim();
        return readString;
    }

    @Override // ohos.nfc.cardemulation.ICardEmulation
    public int setRfConfig(String str, String str2) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        NfcKitsUtils.writeInterfaceToken(NfcKitsUtils.NXP_ADAPTER_DESCRIPTOR, obtain);
        obtain.writeString(str);
        obtain.writeString(str2);
        MessageParcel request = request(23, obtain);
        int readInt = request.readInt();
        request.reclaim();
        return readInt;
    }

    @Override // ohos.nfc.cardemulation.ICardEmulation
    public boolean isSupported(int i) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        NfcKitsUtils.writeInterfaceToken(NfcKitsUtils.NXP_ADAPTER_DESCRIPTOR, obtain);
        obtain.writeInt(i);
        MessageParcel request = request(24, obtain);
        boolean z = request.readInt() >= 0;
        request.reclaim();
        return z;
    }

    @Override // ohos.nfc.cardemulation.ICardEmulation
    public boolean registerForegroundPreferred(ElementName elementName) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        NfcKitsUtils.writeInterfaceToken(NfcKitsUtils.CARD_EMULATION_DESCRIPTOR, obtain);
        obtain.writeSequenceable(elementName);
        MessageParcel request = request(36, obtain);
        boolean z = request.readInt() >= 0;
        request.reclaim();
        return z;
    }

    @Override // ohos.nfc.cardemulation.ICardEmulation
    public boolean unregisterForegroundPreferred() throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        NfcKitsUtils.writeInterfaceToken(NfcKitsUtils.CARD_EMULATION_DESCRIPTOR, obtain);
        MessageParcel request = request(37, obtain);
        boolean z = request.readInt() >= 0;
        request.reclaim();
        return z;
    }

    @Override // ohos.nfc.cardemulation.ICardEmulation
    public boolean isDefaultForAid(int i, ElementName elementName, String str) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        NfcKitsUtils.writeInterfaceToken(NfcKitsUtils.CARD_EMULATION_DESCRIPTOR, obtain);
        obtain.writeInt(i);
        obtain.writeSequenceable(elementName);
        obtain.writeString(str);
        MessageParcel request = request(38, obtain);
        boolean z = request.readInt() >= 0;
        request.reclaim();
        return z;
    }

    @Override // ohos.nfc.cardemulation.ICardEmulation
    public boolean registerAids(int i, ElementName elementName, AidGroup aidGroup) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        NfcKitsUtils.writeInterfaceToken(NfcKitsUtils.NXP_ADAPTER_DESCRIPTOR, obtain);
        obtain.writeInt(i);
        obtain.writeString(aidGroup.getType());
        obtain.writeStringList(aidGroup.getAids());
        obtain.writeSequenceable(elementName);
        MessageParcel request = request(39, obtain);
        boolean z = request.readInt() >= 0;
        request.reclaim();
        return z;
    }

    @Override // ohos.nfc.cardemulation.ICardEmulation
    public boolean removeAids(int i, ElementName elementName, String str) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        NfcKitsUtils.writeInterfaceToken(NfcKitsUtils.CARD_EMULATION_DESCRIPTOR, obtain);
        obtain.writeInt(i);
        obtain.writeSequenceable(elementName);
        obtain.writeString(str);
        MessageParcel request = request(40, obtain);
        boolean z = request.readInt() >= 0;
        request.reclaim();
        return z;
    }

    @Override // ohos.nfc.cardemulation.ICardEmulation
    public AidGroup getAids(int i, ElementName elementName, String str) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        NfcKitsUtils.writeInterfaceToken(NfcKitsUtils.CARD_EMULATION_DESCRIPTOR, obtain);
        obtain.writeInt(i);
        obtain.writeSequenceable(elementName);
        obtain.writeString(str);
        MessageParcel request = request(41, obtain);
        AidGroup aidGroup = new AidGroup();
        if (aidGroup instanceof Sequenceable) {
            request.readSequenceable(aidGroup);
        }
        request.reclaim();
        return aidGroup;
    }

    @Override // ohos.nfc.cardemulation.ICardEmulation
    public boolean isDefaultForType(int i, ElementName elementName, String str) throws RemoteException {
        MessageParcel obtain = MessageParcel.obtain();
        NfcKitsUtils.writeInterfaceToken(NfcKitsUtils.NXP_ADAPTER_DESCRIPTOR, obtain);
        obtain.writeInt(i);
        obtain.writeString(str);
        obtain.writeSequenceable(elementName);
        MessageParcel request = request(42, obtain);
        boolean z = request.readInt() >= 0;
        request.reclaim();
        return z;
    }
}
