package ohos.account;

import ohos.account.AccountProxy;
import ohos.rpc.MessageParcel;

/* renamed from: ohos.account.-$$Lambda$AccountProxy$SjafWP0dQP_XBKtdLV0myUjJzhE  reason: invalid class name */
/* compiled from: lambda */
public final /* synthetic */ class $$Lambda$AccountProxy$SjafWP0dQP_XBKtdLV0myUjJzhE implements AccountProxy.UnmarshallingInterface {
    public static final /* synthetic */ $$Lambda$AccountProxy$SjafWP0dQP_XBKtdLV0myUjJzhE INSTANCE = new $$Lambda$AccountProxy$SjafWP0dQP_XBKtdLV0myUjJzhE();

    private /* synthetic */ $$Lambda$AccountProxy$SjafWP0dQP_XBKtdLV0myUjJzhE() {
    }

    @Override // ohos.account.AccountProxy.UnmarshallingInterface
    public final boolean unmarshalling(MessageParcel messageParcel) {
        return AccountProxy.lambda$setOsAccountName$18(messageParcel);
    }
}
