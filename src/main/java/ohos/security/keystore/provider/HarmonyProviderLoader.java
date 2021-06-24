package ohos.security.keystore.provider;

import java.security.Provider;
import java.security.Security;
import java.util.concurrent.Callable;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class HarmonyProviderLoader implements Callable<Boolean> {
    private static final int DEFAULT_INVALID_INDEX = -1;
    private static final HiLogLabel LABEL = KeyStoreLogger.getLabel(TAG);
    private static final String PROVIDER_ANDROID_BOUNCYCASTLE = "AndroidKeyStoreBCWorkaround";
    private static final String PROVIDER_ANDROID_KEYSTORE = "AndroidKeyStore";
    private static final String PROVIDER_ANDROID_NETWORK = "AndroidNSSP";
    private static final String PROVIDER_ANDROID_OPENSSL = "AndroidOpenSSL";
    private static final String PROVIDER_BC = "BC";
    private static final String TAG = "HarmonyProviderLoader";
    private final LoadCallback callback;
    private final int mHybridMode;

    HarmonyProviderLoader(int i, LoadCallback loadCallback) {
        this.mHybridMode = i;
        this.callback = loadCallback;
    }

    @Override // java.util.concurrent.Callable
    public Boolean call() {
        return Boolean.valueOf(selectAndroidMode(this.mHybridMode));
    }

    private boolean selectAndroidMode(int i) {
        int i2 = this.mHybridMode;
        if (i2 == 0) {
            return handlePure();
        }
        if (i2 == 1) {
            return handleHybrid();
        }
        HiLog.error(LABEL, "please import correct hybrid mode", new Object[0]);
        return false;
    }

    private boolean handleHybrid() {
        boolean z;
        HiLog.info(LABEL, "install hybrid harmony keystore start", new Object[0]);
        try {
            HarmonyKeyStoreBCWorkaroundProvider harmonyKeyStoreBCWorkaroundProvider = new HarmonyKeyStoreBCWorkaroundProvider();
            insertHarmonyProvider(Security.getProviders(), PROVIDER_ANDROID_OPENSSL, new HarmonyOpenSSLProvider());
            insertHarmonyProvider(Security.getProviders(), PROVIDER_ANDROID_BOUNCYCASTLE, harmonyKeyStoreBCWorkaroundProvider);
            insertHarmonyProvider(Security.getProviders(), PROVIDER_ANDROID_KEYSTORE, new HarmonyKeyStoreProvider());
            if (checkBcAndWorkaroundIndex(Security.getProviders())) {
                Provider provider = Security.getProvider(PROVIDER_BC);
                Security.removeProvider(provider.getName());
                insertHarmonyProvider(Security.getProviders(), harmonyKeyStoreBCWorkaroundProvider.getName(), provider);
            }
            z = true;
        } catch (SecurityException unused) {
            HiLog.error(LABEL, "security manager exists and denies access to add or remove provider", new Object[0]);
            z = false;
            HiLog.info(LABEL, "install hybrid harmony keystore end", new Object[0]);
            this.callback.loadComplete();
            return z;
        } catch (Exception unused2) {
            HiLog.error(LABEL, "install keystore fail", new Object[0]);
            z = false;
            HiLog.info(LABEL, "install hybrid harmony keystore end", new Object[0]);
            this.callback.loadComplete();
            return z;
        }
        HiLog.info(LABEL, "install hybrid harmony keystore end", new Object[0]);
        this.callback.loadComplete();
        return z;
    }

    private void insertHarmonyProvider(Provider[] providerArr, String str, Provider provider) {
        int providerIndex = getProviderIndex(providerArr, str);
        if (providerIndex != -1) {
            Security.insertProviderAt(provider, providerIndex + 2);
        } else {
            Security.addProvider(provider);
        }
    }

    private boolean checkBcAndWorkaroundIndex(Provider[] providerArr) {
        return getProviderIndex(providerArr, PROVIDER_BC) < getProviderIndex(providerArr, PROVIDER_ANDROID_BOUNCYCASTLE);
    }

    private int getProviderIndex(Provider[] providerArr, String str) {
        for (int i = 0; i < providerArr.length; i++) {
            if (str.equals(providerArr[i].getName())) {
                return i;
            }
        }
        return -1;
    }

    private boolean handlePure() {
        HiLog.info(LABEL, "install pure harmony keystore start", new Object[0]);
        boolean z = true;
        try {
            Security.removeProvider(PROVIDER_ANDROID_OPENSSL);
            Security.removeProvider(PROVIDER_ANDROID_BOUNCYCASTLE);
            Security.removeProvider(PROVIDER_ANDROID_KEYSTORE);
            Security.removeProvider(PROVIDER_ANDROID_NETWORK);
            Security.insertProviderAt(new HarmonyOpenSSLProvider(), 1);
            int providerIndex = getProviderIndex(Security.getProviders(), PROVIDER_BC);
            Security.addProvider(new HarmonyKeyStoreProvider());
            HarmonyKeyStoreBCWorkaroundProvider harmonyKeyStoreBCWorkaroundProvider = new HarmonyKeyStoreBCWorkaroundProvider();
            if (providerIndex != -1) {
                Security.insertProviderAt(harmonyKeyStoreBCWorkaroundProvider, providerIndex + 1);
            } else {
                Security.addProvider(harmonyKeyStoreBCWorkaroundProvider);
            }
        } catch (SecurityException unused) {
            HiLog.error(LABEL, "security manager exists and denies access to add or remove provider", new Object[0]);
            z = false;
            HiLog.info(LABEL, "install pure harmony keystore end", new Object[0]);
            this.callback.loadComplete();
            return z;
        } catch (Exception unused2) {
            HiLog.error(LABEL, "install keystore fail", new Object[0]);
            z = false;
            HiLog.info(LABEL, "install pure harmony keystore end", new Object[0]);
            this.callback.loadComplete();
            return z;
        }
        HiLog.info(LABEL, "install pure harmony keystore end", new Object[0]);
        this.callback.loadComplete();
        return z;
    }
}
