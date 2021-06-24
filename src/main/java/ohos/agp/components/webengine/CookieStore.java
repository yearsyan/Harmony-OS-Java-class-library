package ohos.agp.components.webengine;

import ohos.agp.components.webengine.adapter.CookieStoreBridge;

public abstract class CookieStore {
    public abstract String getCookie(String str);

    public abstract boolean hasCookies();

    public abstract boolean isCookieEnable();

    public abstract boolean isCrossDomainCookieEnable(WebView webView);

    public abstract void persist();

    public abstract void removeCookies(boolean z, AsyncCallback<Boolean> asyncCallback);

    public abstract void setCookie(String str, String str2);

    public abstract void setCookieEnable(boolean z);

    public abstract void setCrossDomainCookieEnable(WebView webView, boolean z);

    public static CookieStore getInstance() {
        return CookieStoreHolder.INSTANCE;
    }

    private static class CookieStoreHolder {
        private static final CookieStore INSTANCE = new CookieStore() {
            /* class ohos.agp.components.webengine.CookieStore.CookieStoreHolder.AnonymousClass1 */
            private final CookieStoreBridge mCookieStoreBridge = new CookieStoreBridge();

            @Override // ohos.agp.components.webengine.CookieStore
            public void setCookieEnable(boolean z) {
                this.mCookieStoreBridge.setCookieEnable(z);
            }

            @Override // ohos.agp.components.webengine.CookieStore
            public void removeCookies(boolean z, AsyncCallback<Boolean> asyncCallback) {
                this.mCookieStoreBridge.removeCookies(z, asyncCallback);
            }

            @Override // ohos.agp.components.webengine.CookieStore
            public String getCookie(String str) {
                return this.mCookieStoreBridge.getCookie(str);
            }

            @Override // ohos.agp.components.webengine.CookieStore
            public void persist() {
                this.mCookieStoreBridge.persist();
            }

            @Override // ohos.agp.components.webengine.CookieStore
            public void setCookie(String str, String str2) {
                this.mCookieStoreBridge.setCookie(str, str2);
            }

            @Override // ohos.agp.components.webengine.CookieStore
            public void setCrossDomainCookieEnable(WebView webView, boolean z) {
                this.mCookieStoreBridge.setCrossDomainCookieEnable(webView.getWebViewBridge(), z);
            }

            @Override // ohos.agp.components.webengine.CookieStore
            public boolean isCookieEnable() {
                return this.mCookieStoreBridge.isCookieEnable();
            }

            @Override // ohos.agp.components.webengine.CookieStore
            public boolean isCrossDomainCookieEnable(WebView webView) {
                return this.mCookieStoreBridge.isCrossDomainCookieEnable(webView.getWebViewBridge());
            }

            @Override // ohos.agp.components.webengine.CookieStore
            public boolean hasCookies() {
                return this.mCookieStoreBridge.hasCookies();
            }
        };

        private CookieStoreHolder() {
        }
    }
}
