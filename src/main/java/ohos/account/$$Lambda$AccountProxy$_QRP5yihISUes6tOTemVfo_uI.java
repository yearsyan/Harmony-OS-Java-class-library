package ohos.account;

import ohos.account.AccountProxy;
import ohos.rpc.MessageParcel;

/* renamed from: ohos.account.-$$Lambda$AccountProxy$_QRP5yihIS-Ues6tOTe-mVfo_uI  reason: invalid class name */
/* compiled from: lambda */
public final /* synthetic */ class $$Lambda$AccountProxy$_QRP5yihISUes6tOTemVfo_uI implements AccountProxy.UnmarshallingInterface {
    public static final /* synthetic */ $$Lambda$AccountProxy$_QRP5yihISUes6tOTemVfo_uI INSTANCE = new $$Lambda$AccountProxy$_QRP5yihISUes6tOTemVfo_uI();

    private /* synthetic */ $$Lambda$AccountProxy$_QRP5yihISUes6tOTemVfo_uI() {
    }

    @Override // ohos.account.AccountProxy.UnmarshallingInterface
    public final boolean unmarshalling(MessageParcel messageParcel) {
        return AccountProxy.lambda$removeOsAccount$8(messageParcel);
    }
}
