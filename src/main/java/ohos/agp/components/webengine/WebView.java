package ohos.agp.components.webengine;

import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.webengine.adapter.JsCallbackBridge;
import ohos.agp.components.webengine.adapter.WebViewBridge;
import ohos.agp.components.webengine.adapter.WebViewInterface;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.PixelMap;

public class WebView extends Component {
    private static final int FLAG_FOCUSED = 1;
    private static final int FLAG_REQUEST_FOCUS = 2;
    private static final int FLAG_UNFOCUSED = 0;
    private static final OnLayoutChangeListener ON_LAYOUT_CHANGE_LISTENER = new OnLayoutChangeListener() {
        /* class ohos.agp.components.webengine.WebView.AnonymousClass1 */

        @Override // ohos.agp.components.webengine.WebView.OnLayoutChangeListener
        public void onSetWidth(WebView webView, int i) {
            if (webView != null && webView.mWebViewBridge != null) {
                webView.mWebViewBridge.setWidth(i);
            }
        }

        @Override // ohos.agp.components.webengine.WebView.OnLayoutChangeListener
        public void onSetHeight(WebView webView, int i) {
            if (webView != null && webView.mWebViewBridge != null) {
                webView.mWebViewBridge.setHeight(i);
            }
        }

        @Override // ohos.agp.components.webengine.WebView.OnLayoutChangeListener
        public void onSetPosition(WebView webView, float f, float f2) {
            if (webView != null && webView.mWebViewBridge != null) {
                webView.mWebViewBridge.setPosition(f, f2);
            }
        }

        @Override // ohos.agp.components.webengine.WebView.OnLayoutChangeListener
        public void onFirstLayout(WebView webView) {
            if (webView != null && webView.mWebViewBridge != null) {
                webView.mWebViewBridge.setWidth(webView.getWidth());
                webView.mWebViewBridge.setHeight(webView.getHeight());
                webView.mWebViewBridge.addToWindow();
            }
        }
    };
    private static final HiLogLabel TAG = new HiLogLabel(3, LogDomain.END, "WebView");
    private Component.FocusChangedListener mUserFocusChangedListener;
    private final WebViewBridge mWebViewBridge;

    private interface OnLayoutChangeListener {
        void onFirstLayout(WebView webView);

        void onSetHeight(WebView webView, int i);

        void onSetPosition(WebView webView, float f, float f2);

        void onSetWidth(WebView webView, int i);
    }

    private native long nativeGetAgpWebViewHandle();

    private native void nativeNotifyFocusChange(long j, int i);

    private native void nativeRemoveFromWindow(long j);

    private native void nativeSetOnLayoutChangeCallback(long j, OnLayoutChangeListener onLayoutChangeListener);

    public WebView(Context context) {
        this(context, null);
    }

    public WebView(Context context, AttrSet attrSet) {
        this(context, attrSet, "webviewDefaultStyle");
    }

    public WebView(Context context, AttrSet attrSet, String str) {
        super(context, attrSet, str);
        setAlpha(0.0f);
        this.mWebViewBridge = new WebViewBridge(this, context);
        nativeSetOnLayoutChangeCallback(this.mNativeViewPtr, ON_LAYOUT_CHANGE_LISTENER);
        setBindStateChangedListener(new Component.BindStateChangedListener() {
            /* class ohos.agp.components.webengine.WebView.AnonymousClass2 */

            @Override // ohos.agp.components.Component.BindStateChangedListener
            public void onComponentBoundToWindow(Component component) {
                HiLog.debug(WebView.TAG, "add to window.", new Object[0]);
                WebView.this.mWebViewBridge.addToWindow();
            }

            @Override // ohos.agp.components.Component.BindStateChangedListener
            public void onComponentUnboundFromWindow(Component component) {
                HiLog.debug(WebView.TAG, "remove from window.", new Object[0]);
                WebView.this.removeFromWindow();
            }
        });
        setTouchFocusable(true);
        this.mWebViewBridge.setWebViewInterface(new WebViewInterface() {
            /* class ohos.agp.components.webengine.$$Lambda$WebView$567UisoXWfwC0M3YHFjtgUmYNwo */

            @Override // ohos.agp.components.webengine.adapter.WebViewInterface
            public final void onFocusChange(boolean z) {
                WebView.this.lambda$new$0$WebView(z);
            }
        });
        super.setFocusChangedListener(new Component.FocusChangedListener() {
            /* class ohos.agp.components.webengine.$$Lambda$WebView$gM_ruGzZ9gF2pTgvqFlactWERWo */

            @Override // ohos.agp.components.Component.FocusChangedListener
            public final void onFocusChange(Component component, boolean z) {
                WebView.this.lambda$new$1$WebView(component, z);
            }
        });
    }

    public /* synthetic */ void lambda$new$0$WebView(boolean z) {
        nativeNotifyFocusChange(this.mNativeViewPtr, z ? 1 : 0);
        if (z && !isFocused()) {
            HiLog.debug(TAG, "sync A focused", new Object[0]);
            super.requestFocus();
        }
        if (!z && isFocused()) {
            HiLog.debug(TAG, "sync A unfocused", new Object[0]);
            super.clearFocus();
        }
    }

    public /* synthetic */ void lambda$new$1$WebView(Component component, boolean z) {
        if (z) {
            HiLog.debug(TAG, "sync Z focused", new Object[0]);
            this.mWebViewBridge.requestFocus();
        } else {
            HiLog.debug(TAG, "sync Z unfocused", new Object[0]);
            nativeNotifyFocusChange(this.mNativeViewPtr, 2);
        }
        Component.FocusChangedListener focusChangedListener = this.mUserFocusChangedListener;
        if (focusChangedListener != null) {
            focusChangedListener.onFocusChange(component, z);
        }
    }

    @Override // ohos.agp.components.Component
    public void setFocusChangedListener(Component.FocusChangedListener focusChangedListener) {
        this.mUserFocusChangedListener = focusChangedListener;
    }

    public void setWebViewBackground(RgbColor rgbColor) {
        this.mWebViewBridge.setWebViewBackground(rgbColor);
    }

    public void load(String str) {
        this.mWebViewBridge.load(str);
    }

    public void load(String str, String str2, boolean z) {
        this.mWebViewBridge.load(str, str2, z);
    }

    public void load(String str, String str2, String str3, String str4, String str5) {
        this.mWebViewBridge.load(str, str2, str3, str4, str5);
    }

    public void reload() {
        this.mWebViewBridge.reload();
    }

    public String getFirstRequestUrl() {
        return this.mWebViewBridge.getFirstRequestUrl();
    }

    public String getTitle() {
        return this.mWebViewBridge.getTitle();
    }

    public void onInactive() {
        this.mWebViewBridge.onInactive();
    }

    public void onActive() {
        this.mWebViewBridge.onActive();
    }

    public void onStop() {
        this.mWebViewBridge.onStop();
    }

    public void executeJs(String str, AsyncCallback<String> asyncCallback) {
        this.mWebViewBridge.executeJs(str, asyncCallback);
    }

    public void addJsCallback(String str, JsCallback jsCallback) {
        JsCallbackBridge create = JsCallbackBridge.create(jsCallback);
        if (create != null) {
            this.mWebViewBridge.addJsCallback(str, create);
        }
    }

    public void removeJsCallback(String str) {
        this.mWebViewBridge.removeJsCallback(str);
    }

    public void stopLoading() {
        this.mWebViewBridge.stopLoading();
    }

    public int getProgress() {
        return this.mWebViewBridge.getProgress();
    }

    public void post(String str, byte[] bArr) {
        this.mWebViewBridge.post(str, bArr);
    }

    public void doFling(int i, int i2) {
        this.mWebViewBridge.doFling(i, i2);
    }

    public int getWebPageHeight() {
        return this.mWebViewBridge.getWebPageHeight();
    }

    public PixelMap getFavicon() {
        return this.mWebViewBridge.getFavicon();
    }

    public String getCurrentUrl() {
        return this.mWebViewBridge.getCurrentUrl();
    }

    public boolean pageDown(boolean z) {
        return this.mWebViewBridge.pageDown(z);
    }

    public boolean pageUp(boolean z) {
        return this.mWebViewBridge.pageUp(z);
    }

    public void clearMemoryCache() {
        this.mWebViewBridge.clearMemoryCache();
    }

    public void clearAllCache() {
        this.mWebViewBridge.clearAllCache();
    }

    public void notifyJsOnline(boolean z) {
        this.mWebViewBridge.notifyJsOnline(z);
    }

    public boolean canScroll(int i, int i2) {
        return this.mWebViewBridge.canScroll(i, i2);
    }

    public WebConfig getWebConfig() {
        return this.mWebViewBridge.getWebConfig();
    }

    public Navigator getNavigator() {
        return this.mWebViewBridge.getNavigator();
    }

    public ScaleController getScaleController() {
        return this.mWebViewBridge.getScaleController();
    }

    public FontConfig getFontConfig() {
        return this.mWebViewBridge.getFontConfig();
    }

    public void setWebAgent(WebAgent webAgent) {
        if (webAgent != null) {
            this.mWebViewBridge.setWebAgent(webAgent.getWebAgentBridge());
        } else {
            this.mWebViewBridge.setWebAgent(null);
        }
    }

    public void setBrowserAgent(BrowserAgent browserAgent) {
        if (browserAgent != null) {
            this.mWebViewBridge.setBrowserAgent(browserAgent.getBrowserAgentBridge());
        } else {
            this.mWebViewBridge.setBrowserAgent(null);
        }
    }

    public void removeFromWindow() {
        this.mWebViewBridge.removeFromWindow();
        nativeRemoveFromWindow(this.mNativeViewPtr);
    }

    @Override // ohos.agp.components.Component
    public void setVisibility(int i) {
        this.mWebViewBridge.setVisibility(i);
        super.setVisibility(i);
    }

    @Override // ohos.agp.components.Component
    public boolean requestFocus() {
        return super.requestFocus();
    }

    @Override // ohos.agp.components.Component
    public boolean executeLongClick() {
        return this.mWebViewBridge.executeLongClick();
    }

    /* access modifiers changed from: package-private */
    public WebViewBridge getWebViewBridge() {
        return this.mWebViewBridge;
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.Component
    public void createNativePtr() {
        if (this.mNativeViewPtr == 0) {
            this.mNativeViewPtr = nativeGetAgpWebViewHandle();
        }
    }
}
