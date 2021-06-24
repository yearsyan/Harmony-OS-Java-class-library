package ohos.account;

import ohos.account.AccountProxy;
import ohos.rpc.MessageParcel;

/* renamed from: ohos.account.-$$Lambda$AccountProxy$0OlSVnXF6ktAUiBA5GhpwUPtRrU  reason: invalid class name */
/* compiled from: lambda */
public final /* synthetic */ class $$Lambda$AccountProxy$0OlSVnXF6ktAUiBA5GhpwUPtRrU implements AccountProxy.UnmarshallingInterface {
    public static final /* synthetic */ $$Lambda$AccountProxy$0OlSVnXF6ktAUiBA5GhpwUPtRrU INSTANCE = new $$Lambda$AccountProxy$0OlSVnXF6ktAUiBA5GhpwUPtRrU();

    private /* synthetic */ $$Lambda$AccountProxy$0OlSVnXF6ktAUiBA5GhpwUPtRrU() {
    }

    @Override // ohos.account.AccountProxy.UnmarshallingInterface
    public final boolean unmarshalling(MessageParcel messageParcel) {
        return AccountProxy.lambda$activateOsAccount$15(messageParcel);
    }
}
