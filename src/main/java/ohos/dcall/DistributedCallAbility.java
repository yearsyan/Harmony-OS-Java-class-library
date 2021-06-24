package ohos.dcall;

import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.annotation.SystemApi;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteObject;
import ohos.utils.PacMap;

@SystemApi
public abstract class DistributedCallAbility extends Ability {
    private static final HiLogLabel LABEL = new HiLogLabel(3, 0, TAG);
    private static final String OHOS_CONNECT_CALL_ABILITY_PERMISSION = "ohos.permission.CONNECT_CALL_ABILITY";
    private static final String TAG = "DistributedCallAbility";
    private DistributedCallRemote mRemote = null;

    public void onCallAudioDeviceChanged(CallAudioDevice callAudioDevice) {
    }

    public void onCallCreated(DistributedCall distributedCall) {
    }

    public void onCallDeleted(DistributedCall distributedCall) {
    }

    public void onIsNewCallAllowedChanged(boolean z) {
    }

    public void onRingtoneMuted() {
    }

    private final class DistributedCallRemote extends RemoteObject implements IRemoteBroker {
        private static final String TAG = "DistributedCallRemote";

        @Override // ohos.rpc.IRemoteBroker
        public IRemoteObject asObject() {
            return this;
        }

        DistributedCallRemote() {
            super(TAG);
        }

        @Override // ohos.rpc.RemoteObject
        public boolean onRemoteRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) {
            HiLog.info(DistributedCallAbility.LABEL, "%{public}s", DistributedCallUtils.msgCodeToString(i));
            if (!DistributedCallAbility.this.isValidRequest(messageParcel, getCallingPid(), getCallingUid())) {
                return false;
            }
            switch (i) {
                case 1:
                    DistributedCallAbility.this.onCallAudioDeviceChanged(CallAudioDevice.CREATOR.createFromParcel(messageParcel));
                    break;
                case 2:
                    processOnCallCreated(messageParcel);
                    break;
                case 3:
                    processOnCallDeleted(messageParcel);
                    break;
                case 4:
                    DistributedCallAbility.this.onIsNewCallAllowedChanged(messageParcel.readBoolean());
                    break;
                case 5:
                    DistributedCallAbility.this.onRingtoneMuted();
                    break;
                case 6:
                    DistributedCall createFromParcel = DistributedCall.CREATOR.createFromParcel(messageParcel);
                    PreciseObserverProxy.getInstance().triggerStatusChanged(createFromParcel, createFromParcel.getPreciseState());
                    break;
                case 7:
                    DistributedCall createFromParcel2 = DistributedCall.CREATOR.createFromParcel(messageParcel);
                    PreciseObserverProxy.getInstance().triggerInfoChanged(createFromParcel2, createFromParcel2.getInfo());
                    break;
                case 8:
                    PreciseObserverProxy.getInstance().triggerPostDialWait(DistributedCall.CREATOR.createFromParcel(messageParcel), messageParcel.readString());
                    break;
                case 9:
                    PreciseObserverProxy.getInstance().triggerCallCompleted(DistributedCall.CREATOR.createFromParcel(messageParcel));
                    break;
                case 10:
                    String readString = messageParcel.readString();
                    PacMap readPacMapFromParcel = DistributedCallUtils.readPacMapFromParcel(messageParcel);
                    PreciseObserverProxy.getInstance().triggerOnCallEventChanged(DistributedCall.CREATOR.createFromParcel(messageParcel), readString, readPacMapFromParcel);
                    break;
                default:
                    return false;
            }
            return true;
        }

        private void processOnCallCreated(MessageParcel messageParcel) {
            DistributedCall createFromParcel = DistributedCall.CREATOR.createFromParcel(messageParcel);
            DistributedCallAbility.this.onCallCreated(createFromParcel);
            PreciseObserverProxy.getInstance().onCallCreated(createFromParcel);
        }

        private void processOnCallDeleted(MessageParcel messageParcel) {
            DistributedCall createFromParcel = DistributedCall.CREATOR.createFromParcel(messageParcel);
            DistributedCallAbility.this.onCallDeleted(createFromParcel);
            PreciseObserverProxy.getInstance().onCallDeleted(createFromParcel);
        }
    }

    @Override // ohos.aafwk.ability.Ability
    public IRemoteObject onConnect(Intent intent) {
        HiLog.info(LABEL, "onConnect", new Object[0]);
        this.mRemote = new DistributedCallRemote();
        return this.mRemote.asObject();
    }

    @Override // ohos.aafwk.ability.Ability
    public void onDisconnect(Intent intent) {
        HiLog.info(LABEL, "onDisconnect", new Object[0]);
        this.mRemote = null;
    }

    /* access modifiers changed from: package-private */
    public boolean isValidRequest(MessageParcel messageParcel, int i, int i2) {
        if (!checkPermission(i, i2)) {
            HiLog.error(LABEL, "isValidRequest: no permission.", new Object[0]);
            return false;
        } else if (!isValidRequestData(messageParcel)) {
            return false;
        } else {
            return true;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean checkPermission(int i, int i2) {
        return verifyPermission(OHOS_CONNECT_CALL_ABILITY_PERMISSION, i, i2) == 0;
    }

    /* access modifiers changed from: package-private */
    public boolean isValidRequestData(MessageParcel messageParcel) {
        if (messageParcel == null) {
            HiLog.error(LABEL, "isValidRequestData: data is null.", new Object[0]);
            return false;
        } else if (DistributedCallUtils.DISTRIBUTED_CALL_ABILITY_DESCRIPTOR.equals(messageParcel.readInterfaceToken())) {
            return true;
        } else {
            HiLog.error(LABEL, "isValidRequestData: invalid interface token.", new Object[0]);
            return false;
        }
    }
}
