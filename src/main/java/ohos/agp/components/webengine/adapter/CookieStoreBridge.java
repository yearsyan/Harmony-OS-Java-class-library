package ohos.agp.components.webengine.adapter;

import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import ohos.agp.components.webengine.AsyncCallback;

public class CookieStoreBridge {
    private final CookieManager mCookieManager = CookieManager.getInstance();

    public void setCookieEnable(boolean z) {
        this.mCookieManager.setAcceptCookie(z);
    }

    public void removeCookies(boolean z, AsyncCallback<Boolean> asyncCallback) {
        $$Lambda$mmZASEuj2aE5aHrxb57u9e4X8Wc r0 = null;
        if (z) {
            CookieManager cookieManager = this.mCookieManager;
            if (asyncCallback != null) {
                r0 = new ValueCallback() {
                    /* class ohos.agp.components.webengine.adapter.$$Lambda$mmZASEuj2aE5aHrxb57u9e4X8Wc */

                    @Override // android.webkit.ValueCallback
                    public final void onReceiveValue(Object obj) {
                        AsyncCallback.this.onReceive((Boolean) obj);
                    }
                };
            }
            cookieManager.removeSessionCookies(r0);
            return;
        }
        CookieManager cookieManager2 = this.mCookieManager;
        if (asyncCallback != null) {
            r0 = new ValueCallback() {
                /* class ohos.agp.components.webengine.adapter.$$Lambda$mmZASEuj2aE5aHrxb57u9e4X8Wc */

                @Override // android.webkit.ValueCallback
                public final void onReceiveValue(Object obj) {
                    AsyncCallback.this.onReceive((Boolean) obj);
                }
            };
        }
        cookieManager2.removeAllCookies(r0);
    }

    public String getCookie(String str) {
        return this.mCookieManager.getCookie(str);
    }

    public void persist() {
        this.mCookieManager.flush();
    }

    public void setCookie(String str, String str2) {
        this.mCookieManager.setCookie(str, str2);
    }

    public void setCrossDomainCookieEnable(WebViewBridge webViewBridge, boolean z) {
        this.mCookieManager.setAcceptThirdPartyCookies(webViewBridge.getWebViewInstance(), z);
    }

    public boolean isCookieEnable() {
        return this.mCookieManager.acceptCookie();
    }

    public boolean isCrossDomainCookieEnable(WebViewBridge webViewBridge) {
        return this.mCookieManager.acceptThirdPartyCookies(webViewBridge.getWebViewInstance());
    }

    public boolean hasCookies() {
        return this.mCookieManager.hasCookies();
    }
}
