package ohos.aafwk.ability;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import ohos.aafwk.content.Intent;
import ohos.aafwk.utils.log.Log;
import ohos.aafwk.utils.log.LogLabel;
import ohos.rpc.IRemoteBroker;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageOption;
import ohos.rpc.MessageParcel;
import ohos.rpc.RemoteException;
import ohos.rpc.RemoteObject;

interface IAbilityFormProvider extends IRemoteBroker {
    public static final LogLabel PROVIDER_LABEL = LogLabel.create();
    public static final int TRANSACTION_ACQUIRE_ABILITY_FORM = 0;
    public static final int TRANSACTION_ACQUIRE_PROVIDER_FORM_INFO = 1;
    public static final int TRANSACTION_EVENT_NOTIFY = 6;
    public static final int TRANSACTION_FIRE_FORM_EVENT = 4;
    public static final int TRANSACTION_NOTIFY_FORMS_DELETE = 7;
    public static final int TRANSACTION_NOTIFY_FORM_DELETE = 2;
    public static final int TRANSACTION_NOTIFY_FORM_UPDATE = 3;
    public static final int TRANSACTION_NOTIFY_TEMP_FORM_CAST = 5;

    AbilityForm acquireAbilityForm() throws RemoteException;

    void acquireProviderFormInfo(Intent intent, IRemoteObject iRemoteObject) throws RemoteException;

    void eventNotify(Map<Long, Integer> map, Intent intent, IRemoteObject iRemoteObject) throws RemoteException;

    void fireFormEvent(long j, String str, Intent intent, IRemoteObject iRemoteObject) throws RemoteException;

    void notifyFormCastTempForm(long j, Intent intent, IRemoteObject iRemoteObject) throws RemoteException;

    void notifyFormDelete(long j, Intent intent, IRemoteObject iRemoteObject) throws RemoteException;

    void notifyFormUpdate(long j, Intent intent, IRemoteObject iRemoteObject) throws RemoteException;

    void notifyFormsDelete(Set<Long> set, Intent intent, IRemoteObject iRemoteObject) throws RemoteException;

    public static abstract class FormProviderStub extends RemoteObject implements IAbilityFormProvider {
        private static final int MAX_FORM_SIZE = 512;

        @Override // ohos.rpc.IRemoteBroker
        public IRemoteObject asObject() {
            return this;
        }

        FormProviderStub() {
            super("");
        }

        static IAbilityFormProvider asProxy(IRemoteObject iRemoteObject) {
            if (iRemoteObject == null) {
                return null;
            }
            return new FormProviderProxy(iRemoteObject);
        }

        /* JADX DEBUG: Multi-variable search result rejected for r6v0, resolved type: ohos.aafwk.ability.IAbilityFormProvider$FormProviderStub */
        /* JADX WARN: Multi-variable type inference failed */
        @Override // ohos.rpc.RemoteObject
        public boolean onRemoteRequest(int i, MessageParcel messageParcel, MessageParcel messageParcel2, MessageOption messageOption) throws RemoteException {
            if (messageParcel == null || messageParcel2 == null) {
                return false;
            }
            switch (i) {
                case 0:
                    messageParcel2.writeSequenceable(acquireAbilityForm());
                    return true;
                case 1:
                    Intent intent = new Intent();
                    if (!messageParcel.readSequenceable(intent)) {
                        return false;
                    }
                    acquireProviderFormInfo(intent, messageParcel.readRemoteObject());
                    return true;
                case 2:
                    long readLong = messageParcel.readLong();
                    Intent intent2 = new Intent();
                    if (!messageParcel.readSequenceable(intent2)) {
                        return false;
                    }
                    notifyFormDelete(readLong, intent2, messageParcel.readRemoteObject());
                    return true;
                case 3:
                    long readLong2 = messageParcel.readLong();
                    Intent intent3 = new Intent();
                    if (!messageParcel.readSequenceable(intent3)) {
                        return false;
                    }
                    notifyFormUpdate(readLong2, intent3, messageParcel.readRemoteObject());
                    return true;
                case 4:
                    long readLong3 = messageParcel.readLong();
                    String readString = messageParcel.readString();
                    Intent intent4 = new Intent();
                    if (!messageParcel.readSequenceable(intent4)) {
                        return false;
                    }
                    fireFormEvent(readLong3, readString, intent4, messageParcel.readRemoteObject());
                    return true;
                case 5:
                    long readLong4 = messageParcel.readLong();
                    Intent intent5 = new Intent();
                    if (!messageParcel.readSequenceable(intent5)) {
                        return false;
                    }
                    notifyFormCastTempForm(readLong4, intent5, messageParcel.readRemoteObject());
                    return true;
                case 6:
                    Map<?, ?> readMap = messageParcel.readMap();
                    Intent intent6 = new Intent();
                    if (!messageParcel.readSequenceable(intent6)) {
                        return false;
                    }
                    eventNotify(readMap, intent6, messageParcel.readRemoteObject());
                    return true;
                case 7:
                    callFormsDelete(messageParcel);
                    return true;
                default:
                    return super.onRemoteRequest(i, messageParcel, messageParcel2, messageOption);
            }
        }

        private void callFormsDelete(MessageParcel messageParcel) throws RemoteException {
            Intent intent = new Intent();
            if (messageParcel.readSequenceable(intent)) {
                IRemoteObject readRemoteObject = messageParcel.readRemoteObject();
                int readInt = messageParcel.readInt();
                if (readInt > 512) {
                    notifyFormsDelete(new HashSet(), intent, readRemoteObject);
                    return;
                }
                HashSet hashSet = new HashSet();
                for (int i = 0; i < readInt; i++) {
                    hashSet.add(Long.valueOf(messageParcel.readLong()));
                }
                notifyFormsDelete(hashSet, intent, readRemoteObject);
            }
        }

        /* access modifiers changed from: private */
        public static class FormProviderProxy implements IAbilityFormProvider {
            private IRemoteObject remote;

            FormProviderProxy(IRemoteObject iRemoteObject) {
                this.remote = iRemoteObject;
            }

            @Override // ohos.rpc.IRemoteBroker
            public IRemoteObject asObject() {
                return this.remote;
            }

            @Override // ohos.aafwk.ability.IAbilityFormProvider
            public AbilityForm acquireAbilityForm() throws RemoteException {
                MessageParcel obtain = MessageParcel.obtain();
                MessageParcel obtain2 = MessageParcel.obtain();
                try {
                    this.remote.sendRequest(0, obtain, obtain2, new MessageOption());
                    return AbilityForm.createFromParcel(obtain2);
                } finally {
                    obtain.reclaim();
                    obtain2.reclaim();
                }
            }

            @Override // ohos.aafwk.ability.IAbilityFormProvider
            public void acquireProviderFormInfo(Intent intent, IRemoteObject iRemoteObject) throws RemoteException {
                MessageParcel obtain = MessageParcel.obtain();
                MessageParcel obtain2 = MessageParcel.obtain();
                MessageOption messageOption = new MessageOption(1);
                try {
                    obtain.writeSequenceable(intent);
                    obtain.writeRemoteObject(iRemoteObject);
                    this.remote.sendRequest(1, obtain, obtain2, messageOption);
                } finally {
                    obtain.reclaim();
                    obtain2.reclaim();
                }
            }

            @Override // ohos.aafwk.ability.IAbilityFormProvider
            public void notifyFormDelete(long j, Intent intent, IRemoteObject iRemoteObject) throws RemoteException {
                communication(j, 2, intent, iRemoteObject);
            }

            @Override // ohos.aafwk.ability.IAbilityFormProvider
            public void notifyFormUpdate(long j, Intent intent, IRemoteObject iRemoteObject) throws RemoteException {
                communication(j, 3, intent, iRemoteObject);
            }

            @Override // ohos.aafwk.ability.IAbilityFormProvider
            public void fireFormEvent(long j, String str, Intent intent, IRemoteObject iRemoteObject) throws RemoteException {
                MessageParcel obtain = MessageParcel.obtain();
                MessageParcel obtain2 = MessageParcel.obtain();
                try {
                    obtain.writeLong(j);
                    obtain.writeString(str);
                    obtain.writeSequenceable(intent);
                    obtain.writeRemoteObject(iRemoteObject);
                    this.remote.sendRequest(4, obtain, obtain2, new MessageOption(1));
                } finally {
                    obtain.reclaim();
                    obtain2.reclaim();
                }
            }

            @Override // ohos.aafwk.ability.IAbilityFormProvider
            public void notifyFormCastTempForm(long j, Intent intent, IRemoteObject iRemoteObject) throws RemoteException {
                communication(j, 5, intent, iRemoteObject);
            }

            @Override // ohos.aafwk.ability.IAbilityFormProvider
            public void eventNotify(Map<Long, Integer> map, Intent intent, IRemoteObject iRemoteObject) throws RemoteException {
                Log.debug(PROVIDER_LABEL, "FormProviderProxy eventNotify come", new Object[0]);
                MessageParcel obtain = MessageParcel.obtain();
                MessageParcel obtain2 = MessageParcel.obtain();
                try {
                    obtain.writeMap(map);
                    obtain.writeSequenceable(intent);
                    obtain.writeRemoteObject(iRemoteObject);
                    this.remote.sendRequest(6, obtain, obtain2, new MessageOption(1));
                } finally {
                    obtain.reclaim();
                    obtain2.reclaim();
                }
            }

            @Override // ohos.aafwk.ability.IAbilityFormProvider
            public void notifyFormsDelete(Set<Long> set, Intent intent, IRemoteObject iRemoteObject) throws RemoteException {
                MessageParcel obtain = MessageParcel.obtain();
                MessageParcel obtain2 = MessageParcel.obtain();
                try {
                    obtain.writeSequenceable(intent);
                    obtain.writeRemoteObject(iRemoteObject);
                    obtain.writeInt(set.size());
                    for (Long l : set) {
                        obtain.writeLong(l.longValue());
                    }
                    this.remote.sendRequest(7, obtain, obtain2, new MessageOption(1));
                } finally {
                    obtain.reclaim();
                    obtain2.reclaim();
                }
            }

            private void communication(long j, int i, Intent intent, IRemoteObject iRemoteObject) throws RemoteException {
                MessageParcel obtain = MessageParcel.obtain();
                MessageParcel obtain2 = MessageParcel.obtain();
                try {
                    obtain.writeLong(j);
                    obtain.writeSequenceable(intent);
                    obtain.writeRemoteObject(iRemoteObject);
                    this.remote.sendRequest(i, obtain, obtain2, new MessageOption(1));
                } finally {
                    obtain.reclaim();
                    obtain2.reclaim();
                }
            }
        }
    }
}
