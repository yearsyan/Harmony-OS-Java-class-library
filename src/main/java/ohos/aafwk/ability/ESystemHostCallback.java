package ohos.aafwk.ability;

import java.util.List;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.interwork.ability.InstantProviderEx;
import ohos.interwork.ui.RemoteViewEx;
import ohos.interwork.utils.PacMapEx;
import ohos.rpc.IRemoteObject;
import ohos.rpc.MessageParcel;

/* access modifiers changed from: package-private */
public class ESystemHostCallback implements IHostCallback {
    private static final int CODE_ON_ACQUIRE = 1;
    private static final int CODE_ON_FORM_UNINSTALLED = 3;
    private static final int CODE_ON_UPDATE = 2;
    private static final int ERR_CODE_OK = 0;
    private static final String ESYSTEM_INTERFACE_TOKEN = "com.huawei.ohos.localability.IFormClient";
    private static final int FLAG_HAS_JAVA_VALUE = 1;
    private static final int FLAG_HAS_JS_VALUE = 2;
    private static final int FLAG_NO_VALUE = 0;
    private static final String KEY_REMOTEVIEW = "REMOTE_VIEW";
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218108160, "ESystemHostCallback");
    private static final int MAX_FORMS_PER_BUNDLE = 64;
    private static final String RELATED_BUNDLE_NAME = "RELATED_BUNDLE_NAME";
    IRemoteObject remoteClient;

    ESystemHostCallback(IRemoteObject iRemoteObject) {
        this.remoteClient = iRemoteObject;
    }

    static void replyFormRecord(long j, MessageParcel messageParcel, FormRecord formRecord) {
        if (messageParcel.writeInt(0)) {
            if (!marshallingBasicData(messageParcel, j, formRecord)) {
                HiLog.error(LABEL, "marshalling basic data failed", new Object[0]);
            } else if (!formRecord.isJsForm) {
                if (formRecord.formView != null) {
                    formRecord.formView.setDefaultBundleName(formRecord.packageName);
                }
                RemoteViewEx remoteViewEx = ComponentUtils.getRemoteViewEx(FormAdapter.getInstance().getContext(), formRecord.packageName, formRecord.originalBundleName, formRecord.hapSourceDirs, formRecord.formView);
                if (remoteViewEx == null) {
                    messageParcel.writeInt(0);
                } else if (messageParcel.writeInt(1)) {
                    PacMapEx pacMapEx = new PacMapEx();
                    pacMapEx.putObjectValue(KEY_REMOTEVIEW, remoteViewEx);
                    messageParcel.writePacMapEx(pacMapEx);
                }
            } else if (messageParcel.writeInt(2)) {
                messageParcel.writeParcelableEx(new InstantProviderEx(formRecord.instantProvider));
            }
        }
    }

    private static boolean marshallingBasicData(MessageParcel messageParcel, long j, FormRecord formRecord) {
        if (!messageParcel.writeInt(1) || !messageParcel.writeLong(j) || !messageParcel.writeString(formRecord.bundleName) || !messageParcel.writeString(formRecord.originalBundleName) || !messageParcel.writeString(formRecord.abilityName) || !messageParcel.writeString(formRecord.formName) || !messageParcel.writeInt(formRecord.eSystemPreviewLayoutId) || !messageParcel.writeBoolean(formRecord.tempFormFlag)) {
            return false;
        }
        PacMapEx pacMapEx = new PacMapEx();
        pacMapEx.putObjectValue(RELATED_BUNDLE_NAME, formRecord.relatedBundleName);
        messageParcel.writePacMapEx(pacMapEx);
        return true;
    }

    @Override // ohos.aafwk.ability.IHostCallback
    public void onAcquire(long j, FormRecord formRecord) {
        handleEvent(1, j, formRecord);
    }

    @Override // ohos.aafwk.ability.IHostCallback
    public void onUpdate(long j, FormRecord formRecord) {
        handleEvent(2, j, formRecord);
    }

    private void handleEvent(int i, long j, FormRecord formRecord) {
        if (this.remoteClient == null || j < 0 || formRecord == null) {
            HiLog.error(LABEL, "invalid param", new Object[0]);
            return;
        }
        boolean z = formRecord.isJsForm;
        RemoteViewEx remoteViewEx = null;
        if (!z) {
            if (formRecord.formView != null) {
                formRecord.formView.setDefaultBundleName(formRecord.packageName);
            }
            remoteViewEx = ComponentUtils.getRemoteViewEx(FormAdapter.getInstance().getContext(), formRecord.packageName, formRecord.originalBundleName, formRecord.hapSourceDirs, formRecord.formView);
            if (remoteViewEx == null) {
                HiLog.error(LABEL, "get RemoteViewEx failed", new Object[0]);
                return;
            }
        }
        MessageParcel obtain = MessageParcel.obtain();
        if (!obtain.writeInterfaceToken(ESYSTEM_INTERFACE_TOKEN)) {
            HiLog.error(LABEL, "write interface failed", new Object[0]);
        } else if (!marshallingBasicData(obtain, j, formRecord)) {
            HiLog.error(LABEL, "marshalling basic data failed", new Object[0]);
        } else {
            if (obtain.writeInt(z ? 2 : 1)) {
                if (z) {
                    obtain.writeParcelableEx(new InstantProviderEx(formRecord.instantProvider));
                } else {
                    PacMapEx pacMapEx = new PacMapEx();
                    pacMapEx.putObjectValue(KEY_REMOTEVIEW, remoteViewEx);
                    obtain.writePacMapEx(pacMapEx);
                }
                sendImpl(i, obtain, MessageParcel.obtain());
            }
        }
    }

    @Override // ohos.aafwk.ability.IHostCallback
    public void onFormUninstalled(List<Long> list) {
        if (this.remoteClient == null || list == null || list.isEmpty()) {
            HiLog.error(LABEL, "onFormUninstalled invalid param", new Object[0]);
            return;
        }
        int size = list.size();
        if (size > 64) {
            HiLog.error(LABEL, "onFormUninstalled invalid param", new Object[0]);
            return;
        }
        MessageParcel obtain = MessageParcel.obtain();
        MessageParcel obtain2 = MessageParcel.obtain();
        if (!obtain.writeInterfaceToken(ESYSTEM_INTERFACE_TOKEN)) {
            HiLog.error(LABEL, "write interface failed", new Object[0]);
        } else if (!obtain.writeInt(size)) {
            HiLog.error(LABEL, "onFormUninstalled write form size failed", new Object[0]);
        } else {
            for (Long l : list) {
                if (!obtain.writeLong(l.longValue())) {
                    HiLog.error(LABEL, "onFormUninstalled write form id item failed", new Object[0]);
                    return;
                }
            }
            sendImpl(3, obtain, obtain2);
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:5|6) */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0012, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
        ohos.hiviewdfx.HiLog.info(ohos.aafwk.ability.ESystemHostCallback.LABEL, "SendImpl get remote exception..", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0020, code lost:
        r4.reclaim();
        r5.reclaim();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0026, code lost:
        throw r2;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x0014 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void sendImpl(int r3, ohos.rpc.MessageParcel r4, ohos.rpc.MessageParcel r5) {
        /*
            r2 = this;
            ohos.rpc.MessageOption r0 = new ohos.rpc.MessageOption
            r1 = 1
            r0.<init>(r1)
            ohos.rpc.IRemoteObject r2 = r2.remoteClient     // Catch:{ RemoteException -> 0x0014 }
            r2.sendRequest(r3, r4, r5, r0)     // Catch:{ RemoteException -> 0x0014 }
        L_0x000b:
            r4.reclaim()
            r5.reclaim()
            goto L_0x001f
        L_0x0012:
            r2 = move-exception
            goto L_0x0020
        L_0x0014:
            ohos.hiviewdfx.HiLogLabel r2 = ohos.aafwk.ability.ESystemHostCallback.LABEL     // Catch:{ all -> 0x0012 }
            java.lang.String r3 = "SendImpl get remote exception.."
            r0 = 0
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ all -> 0x0012 }
            ohos.hiviewdfx.HiLog.info(r2, r3, r0)     // Catch:{ all -> 0x0012 }
            goto L_0x000b
        L_0x001f:
            return
        L_0x0020:
            r4.reclaim()
            r5.reclaim()
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.aafwk.ability.ESystemHostCallback.sendImpl(int, ohos.rpc.MessageParcel, ohos.rpc.MessageParcel):void");
    }
}
