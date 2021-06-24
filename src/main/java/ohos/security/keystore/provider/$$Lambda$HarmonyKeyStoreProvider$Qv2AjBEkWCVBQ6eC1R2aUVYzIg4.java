package ohos.security.keystore.provider;

/* renamed from: ohos.security.keystore.provider.-$$Lambda$HarmonyKeyStoreProvider$Qv2AjBEkWCVBQ6eC1R2aUVYzIg4  reason: invalid class name */
/* compiled from: lambda */
public final /* synthetic */ class $$Lambda$HarmonyKeyStoreProvider$Qv2AjBEkWCVBQ6eC1R2aUVYzIg4 implements LoadCallback {
    public static final /* synthetic */ $$Lambda$HarmonyKeyStoreProvider$Qv2AjBEkWCVBQ6eC1R2aUVYzIg4 INSTANCE = new $$Lambda$HarmonyKeyStoreProvider$Qv2AjBEkWCVBQ6eC1R2aUVYzIg4();

    private /* synthetic */ $$Lambda$HarmonyKeyStoreProvider$Qv2AjBEkWCVBQ6eC1R2aUVYzIg4() {
    }

    @Override // ohos.security.keystore.provider.LoadCallback
    public final void loadComplete() {
        HarmonyKeyStoreProvider.executor.shutdown();
    }
}
