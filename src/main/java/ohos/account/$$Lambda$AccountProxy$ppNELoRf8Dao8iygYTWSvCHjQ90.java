package ohos.account;

import ohos.account.AccountProxy;
import ohos.rpc.MessageParcel;

/* renamed from: ohos.account.-$$Lambda$AccountProxy$ppNELoRf8Dao8iygYTWSvCHjQ90  reason: invalid class name */
/* compiled from: lambda */
public final /* synthetic */ class $$Lambda$AccountProxy$ppNELoRf8Dao8iygYTWSvCHjQ90 implements AccountProxy.UnmarshallingInterface {
    public static final /* synthetic */ $$Lambda$AccountProxy$ppNELoRf8Dao8iygYTWSvCHjQ90 INSTANCE = new $$Lambda$AccountProxy$ppNELoRf8Dao8iygYTWSvCHjQ90();

    private /* synthetic */ $$Lambda$AccountProxy$ppNELoRf8Dao8iygYTWSvCHjQ90() {
    }

    @Override // ohos.account.AccountProxy.UnmarshallingInterface
    public final boolean unmarshalling(MessageParcel messageParcel) {
        return AccountProxy.lambda$setOsAccountConstraints$20(messageParcel);
    }
}
