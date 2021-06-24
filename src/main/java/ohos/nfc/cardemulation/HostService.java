package ohos.nfc.cardemulation;

import java.util.Optional;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.eventhandler.Courier;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.interwork.eventhandler.CourierEx;
import ohos.interwork.eventhandler.EventHandlerEx;
import ohos.interwork.eventhandler.InnerEventUtils;
import ohos.interwork.utils.PacMapEx;
import ohos.nfc.NfcKitsUtils;
import ohos.rpc.IRemoteObject;
import ohos.rpc.RemoteException;
import ohos.utils.PacMap;

public abstract class HostService extends Ability {
    public static final int ERR_DESELECTED = 1;
    public static final int ERR_LINK_LOSS = 0;
    public static final String KEY_DATA = "data";
    private static final HiLogLabel LABEL = new HiLogLabel(3, NfcKitsUtils.NFC_DOMAIN_ID, "HostService");
    public static final String META_DATA_NAME = "ohos.nfc.cardemulation.data.host_service";
    public static final int MSG_APP_RESPONSE = 1;
    public static final int MSG_DISABLED_CALLBACK = 2;
    public static final int MSG_REMOTE_COMMAND = 0;
    public static final int MSG_UNHANDLED = 3;
    public static final String SERVICE_NAME = "ohos.nfc.cardemulation.action.HOST_SERVICE";
    final CourierEx mCourier = new CourierEx(new MsgHandler(EventRunner.create(true)));
    Courier mNfcService = null;

    public abstract void disabledCallback(int i);

    public abstract byte[] handleRemoteCommand(byte[] bArr, IntentParams intentParams);

    final class MsgHandler extends EventHandlerEx {
        public MsgHandler(EventRunner eventRunner) {
            super(eventRunner);
        }

        @Override // ohos.eventhandler.EventHandler
        public void processEvent(InnerEvent innerEvent) {
            Object obj = innerEvent.object;
            int i = innerEvent.eventId;
            if (i == 0) {
                HiLog.error(HostService.LABEL, "processEvent: %{public}d", 0);
                if (innerEvent.getPacMap() != null) {
                    if (HostService.this.mNfcService == null) {
                        HostService.this.mNfcService = innerEvent.replyTo;
                    }
                    PacMapEx pacMapEx = InnerEventUtils.getPacMapEx(innerEvent);
                    if (pacMapEx != null) {
                        byte[] bArr = (byte[]) HostService.this.getObjectValue(pacMapEx.getObjectValue("data").get(), byte[].class).get();
                        if (bArr != null) {
                            byte[] handleRemoteCommand = HostService.this.handleRemoteCommand(bArr, null);
                            if (handleRemoteCommand == null) {
                                return;
                            }
                            if (HostService.this.mNfcService == null) {
                                HiLog.error(HostService.LABEL, "Response not sent; service was deactivated.", new Object[0]);
                                return;
                            }
                            InnerEvent innerEvent2 = InnerEvent.get(1);
                            PacMapEx pacMapEx2 = new PacMapEx();
                            pacMapEx2.putObjectValue("data", handleRemoteCommand);
                            InnerEventUtils.setExInfo(innerEvent2, 0, 0, pacMapEx2);
                            innerEvent2.replyTo = HostService.this.mCourier;
                            try {
                                HostService.this.mNfcService.send(innerEvent2);
                            } catch (RemoteException unused) {
                                HiLog.error(HostService.LABEL, "Response not sent; RemoteException calling into NfcService.", new Object[0]);
                            }
                        } else {
                            HiLog.error(HostService.LABEL, "Received MSG_COMMAND_APDU without data.", new Object[0]);
                        }
                    }
                }
            } else if (i == 1) {
                HiLog.error(HostService.LABEL, "processEvent: %{public}d", 1);
                if (HostService.this.mNfcService == null) {
                    HiLog.error(HostService.LABEL, "Response not sent; service was deactivated.", new Object[0]);
                    return;
                }
                try {
                    innerEvent.replyTo = HostService.this.mCourier;
                    HostService.this.mNfcService.send(innerEvent);
                } catch (RemoteException unused2) {
                    HiLog.error(HostService.LABEL, "RemoteException calling into NfcService.", new Object[0]);
                }
            } else if (i == 2) {
                HiLog.error(HostService.LABEL, "processEvent: %{public}d", 2);
                HiLog.info(HostService.LABEL, "Received MSG_DISABLED_CALLBACK", new Object[0]);
                HostService hostService = HostService.this;
                hostService.mNfcService = null;
                hostService.disabledCallback((int) innerEvent.param);
            } else if (i != 3) {
                HiLog.warn(HostService.LABEL, "ignored event: %{public}d", Integer.valueOf(innerEvent.eventId));
            } else {
                HiLog.error(HostService.LABEL, "processEvent: %{public}d", 3);
                if (HostService.this.mNfcService == null) {
                    HiLog.error(HostService.LABEL, "notifyUnhandled not sent; service was deactivated.", new Object[0]);
                    return;
                }
                try {
                    innerEvent.replyTo = HostService.this.mCourier;
                    HostService.this.mNfcService.send(innerEvent);
                } catch (RemoteException unused3) {
                    HiLog.error(HostService.LABEL, "RemoteException calling into NfcService.", new Object[0]);
                }
            }
        }
    }

    public final void sendResponse(byte[] bArr) {
        InnerEvent innerEvent = InnerEvent.get(1);
        PacMap pacMap = new PacMap();
        pacMap.putByteValueArray("data", bArr);
        innerEvent.setPacMap(pacMap);
        try {
            this.mCourier.send(innerEvent);
        } catch (RemoteException unused) {
            HiLog.error(LABEL, "Local messenger has died.", new Object[0]);
        }
    }

    @Override // ohos.aafwk.ability.Ability
    public IRemoteObject onConnect(Intent intent) {
        HiLog.error(LABEL, "onConnect!", new Object[0]);
        super.onConnect(intent);
        return this.mCourier.getRemoteObject();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private final <T> Optional<T> getObjectValue(Object obj, Class<T> cls) {
        if (obj == null || cls == null) {
            return Optional.empty();
        }
        if (cls == obj.getClass()) {
            return Optional.of(obj);
        }
        HiLog.error(LABEL, "PacMap has type error, the key expect the value type is %{public}s not %{public}s, just return default value.", cls.getName(), obj.getClass().getName());
        return Optional.empty();
    }
}
