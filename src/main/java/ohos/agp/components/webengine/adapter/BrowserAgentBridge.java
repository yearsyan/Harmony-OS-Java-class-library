package ohos.agp.components.webengine.adapter;

import android.net.Uri;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import java.util.concurrent.ConcurrentHashMap;
import ohos.agp.components.webengine.AsyncCallback;
import ohos.app.Context;

public class BrowserAgentBridge extends WebChromeClient {
    private final ConcurrentHashMap<Object, Object> mAbutmentMap = new ConcurrentHashMap<>(1);
    private final BrowserAgentInterface mBrowserAgentInterface;
    private final Context mContext;
    private FullScreenViewControllerBridge mFullScreenViewController = null;

    static /* synthetic */ void lambda$onGeolocationPermissionsShowPrompt$0(String str, boolean z, boolean z2) {
    }

    private BrowserAgentBridge(Context context, BrowserAgentInterface browserAgentInterface) {
        this.mContext = context;
        this.mBrowserAgentInterface = browserAgentInterface;
    }

    public static BrowserAgentBridge create(Context context, BrowserAgentInterface browserAgentInterface) {
        if (context == null || browserAgentInterface == null) {
            return null;
        }
        return new BrowserAgentBridge(context, browserAgentInterface);
    }

    /* access modifiers changed from: package-private */
    public BrowserAgentInterface getBrowserAgentInterface() {
        return this.mBrowserAgentInterface;
    }

    /* access modifiers changed from: package-private */
    public void putAbutmentMap(Object obj, Object obj2) {
        if (obj != null && obj2 != null) {
            this.mAbutmentMap.put(obj, obj2);
        }
    }

    /* access modifiers changed from: package-private */
    public void removeAbutmentMap(Object obj) {
        if (obj != null) {
            this.mAbutmentMap.remove(obj);
        }
    }

    public void onProgressChanged(WebView webView, int i) {
        this.mBrowserAgentInterface.onProgressUpdated(this.mAbutmentMap.get(webView), i);
    }

    public void onReceivedTitle(WebView webView, String str) {
        this.mBrowserAgentInterface.onTitleUpdated(this.mAbutmentMap.get(webView), str);
    }

    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback customViewCallback) {
        this.mFullScreenViewController = new FullScreenViewControllerBridge(this.mContext, view);
        this.mFullScreenViewController.enterFullScreen();
    }

    public void onHideCustomView() {
        FullScreenViewControllerBridge fullScreenViewControllerBridge = this.mFullScreenViewController;
        if (fullScreenViewControllerBridge != null) {
            fullScreenViewControllerBridge.exitFullScreen();
        }
    }

    public boolean onJsAlert(WebView webView, String str, String str2, JsResult jsResult) {
        return this.mBrowserAgentInterface.onJsMessageShow(this.mAbutmentMap.get(webView), str, str2, true, JsMessageResultBridge.create(jsResult));
    }

    public boolean onJsConfirm(WebView webView, String str, String str2, JsResult jsResult) {
        return this.mBrowserAgentInterface.onJsMessageShow(this.mAbutmentMap.get(webView), str, str2, false, JsMessageResultBridge.create(jsResult));
    }

    public boolean onJsPrompt(WebView webView, String str, String str2, String str3, JsPromptResult jsPromptResult) {
        return this.mBrowserAgentInterface.onJsTextInput(this.mAbutmentMap.get(webView), str, str2, str3, JsTextInputResultBridge.create(jsPromptResult));
    }

    public void onGeolocationPermissionsShowPrompt(String str, GeolocationPermissions.Callback callback) {
        LocationAccessResponseInterface locationAccessResponseInterface;
        BrowserAgentInterface browserAgentInterface = this.mBrowserAgentInterface;
        if (callback == null) {
            locationAccessResponseInterface = $$Lambda$BrowserAgentBridge$h3ZigrHSwuCmFuLKDDzMaxEZtU.INSTANCE;
        } else {
            locationAccessResponseInterface = new LocationAccessResponseInterface(callback) {
                /* class ohos.agp.components.webengine.adapter.$$Lambda$BrowserAgentBridge$lUFBj6X3fbbzo9qkNQdWNxuYaEo */
                private final /* synthetic */ GeolocationPermissions.Callback f$0;

                {
                    this.f$0 = r1;
                }

                @Override // ohos.agp.components.webengine.adapter.LocationAccessResponseInterface
                public final void apply(String str, boolean z, boolean z2) {
                    this.f$0.invoke(str, z, !z2);
                }
            };
        }
        browserAgentInterface.onLocationApiAccessRequest(str, locationAccessResponseInterface);
    }

    public void onGeolocationPermissionsHidePrompt() {
        this.mBrowserAgentInterface.onLocationApiAccessCancel();
    }

    @Override // android.webkit.WebChromeClient
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        $$Lambda$BrowserAgentBridge$klVqIRS6AJzl8LS8BjjoXuPIME r3;
        if (valueCallback == null) {
            r3 = null;
        } else {
            r3 = new AsyncCallback(valueCallback) {
                /* class ohos.agp.components.webengine.adapter.$$Lambda$BrowserAgentBridge$klVqIRS6AJzl8LS8BjjoXuPIME */
                private final /* synthetic */ ValueCallback f$0;

                {
                    this.f$0 = r1;
                }

                @Override // ohos.agp.components.webengine.AsyncCallback
                public final void onReceive(Object obj) {
                    BrowserAgentBridge.lambda$onShowFileChooser$2(this.f$0, (ohos.utils.net.Uri[]) obj);
                }
            };
        }
        return this.mBrowserAgentInterface.onPickFiles(this.mAbutmentMap.get(webView), r3, PickFilesParamsBridge.create(fileChooserParams));
    }

    static /* synthetic */ void lambda$onShowFileChooser$2(ValueCallback valueCallback, ohos.utils.net.Uri[] uriArr) {
        if (uriArr == null) {
            valueCallback.onReceiveValue(null);
            return;
        }
        int length = uriArr.length;
        Uri[] uriArr2 = new Uri[length];
        for (int i = 0; i < length; i++) {
            uriArr2[i] = Uri.parse(String.valueOf(uriArr[i]));
        }
        valueCallback.onReceiveValue(uriArr2);
    }
}
