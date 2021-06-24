package ohos.ai.engine.aimodel;

import java.util.Optional;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;

public abstract class ModelCoreSkeleton extends RemoteObject implements IModelCore {
    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this;
    }

    public ModelCoreSkeleton() {
        super(IModelCore.DESCRIPTOR);
    }

    public static Optional<IModelCore> asInterface(IRemoteObject iRemoteObject) {
        if (iRemoteObject == null) {
            return Optional.empty();
        }
        IRemoteBroker queryLocalInterface = iRemoteObject.queryLocalInterface(IModelCore.DESCRIPTOR);
        if (queryLocalInterface == null || !(queryLocalInterface instanceof IModelCore)) {
            return Optional.of(new ModelCoreProxy(iRemoteObject));
        }
        return Optional.of((IModelCore) queryLocalInterface);
    }

    @Override // ohos.rpc.RemoteObject
    public boolean onRemoteRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException {
        messageParcel.readInterfaceToken();
        switch (i) {
            case 1:
                return messageParcel2.writeBoolean(isConnect());
            case 2:
                connect();
                return true;
            case 3:
                return messageParcel2.writeBoolean(insertResourceInformation(messageParcel.readString()));
            case 4:
                return messageParcel2.writeLong(getResourceVersionCode(messageParcel.readString()));
            case 5:
                return messageParcel2.writeString(requestModelPath(messageParcel.readLong()));
            case 6:
                return messageParcel2.writeByteArray(requestModelBytes(messageParcel.readLong()));
            case 7:
                return messageParcel2.writeSequenceableList(requestModelsByBusiDomain(messageParcel.readString()));
            case 8:
                return messageParcel2.writeBoolean(syncModel(messageParcel.readString(), messageParcel.readLong()));
            case 9:
                return messageParcel2.writeBoolean(isSupportModelManagement());
            case 10:
                ModelUpInfo modelUpInfo = new ModelUpInfo();
                messageParcel.readSequenceable(modelUpInfo);
                subscribeModel(modelUpInfo, RecordObserverCallbackSkeleton.asInterface(messageParcel.readRemoteObject()).orElse(null));
                return true;
            case 11:
                ModelUpInfo modelUpInfo2 = new ModelUpInfo();
                messageParcel.readSequenceable(modelUpInfo2);
                unsubscribeModel(modelUpInfo2);
                return true;
            default:
                return super.onRemoteRequest(i, messageParcel, messageParcel2, messageOption);
        }
    }
}
