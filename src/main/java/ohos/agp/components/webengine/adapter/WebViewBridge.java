package ohos.agp.components.webengine.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.webengine.AsyncCallback;
import ohos.agp.components.webengine.FontConfig;
import ohos.agp.components.webengine.Navigator;
import ohos.agp.components.webengine.ScaleChangeListener;
import ohos.agp.components.webengine.ScaleController;
import ohos.agp.components.webengine.WebConfig;
import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.PixelMap;
import ohos.media.image.inner.ImageDoubleFwConverter;

public class WebViewBridge {
    private static final HiLogLabel TAG = new HiLogLabel(3, LogDomain.END, "WebViewBridge");
    private final Object mAbutmentZ;
    private BrowserAgentBridge mBrowserAgentBridge;
    private FrameLayout mContentView;
    private final DownloadListener mDownloadListener = new DownloadListener() {
        /* class ohos.agp.components.webengine.adapter.WebViewBridge.AnonymousClass1 */

        public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
            BrowserAgentBridge browserAgentBridge = WebViewBridge.this.mBrowserAgentBridge;
            if (browserAgentBridge != null) {
                browserAgentBridge.getBrowserAgentInterface().onDownload(str, str2, str3, str4, j);
            }
        }
    };
    private Bitmap mFavicon;
    private final Navigator mNavigator;
    private ScaleChangeListener mScaleChangeListener;
    private final ScaleController mScaleController;
    private final WebAgentBridge mWebAgentBridgeObserveScaleChangeOnly = new WebAgentBridge();
    private WebAgentBridge mWebAgentBridgeUsing;
    private final WebConfigBridge mWebConfigBridge = new WebConfigBridge();
    private final WebView mWebView;
    private WebViewInterface mWebViewInterface;

    public WebViewBridge(Object obj, Context context) {
        android.content.Context context2;
        this.mAbutmentZ = obj;
        if (context != null) {
            Object hostContext = context.getHostContext();
            if (hostContext instanceof android.content.Context) {
                context2 = (android.content.Context) hostContext;
                initContentView(context2);
                this.mWebView = new WebView(context2);
                this.mNavigator = new NavigatorInner(this.mWebView);
                this.mScaleController = new ScaleControllerInner(this.mWebView, this);
                this.mWebConfigBridge.setWebSettings(this.mWebView.getSettings());
                this.mWebView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    /* class ohos.agp.components.webengine.adapter.$$Lambda$WebViewBridge$F72Fxpma6VBZR_3KbvJ4JZ8wX24 */

                    public final void onFocusChange(View view, boolean z) {
                        WebViewBridge.this.lambda$new$0$WebViewBridge(view, z);
                    }
                });
            }
        }
        context2 = null;
        initContentView(context2);
        this.mWebView = new WebView(context2);
        this.mNavigator = new NavigatorInner(this.mWebView);
        this.mScaleController = new ScaleControllerInner(this.mWebView, this);
        this.mWebConfigBridge.setWebSettings(this.mWebView.getSettings());
        this.mWebView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            /* class ohos.agp.components.webengine.adapter.$$Lambda$WebViewBridge$F72Fxpma6VBZR_3KbvJ4JZ8wX24 */

            public final void onFocusChange(View view, boolean z) {
                WebViewBridge.this.lambda$new$0$WebViewBridge(view, z);
            }
        });
    }

    public /* synthetic */ void lambda$new$0$WebViewBridge(View view, boolean z) {
        HiLog.debug(TAG, z ? "WebView focused" : "WebView not focused", new Object[0]);
        WebViewInterface webViewInterface = this.mWebViewInterface;
        if (webViewInterface != null) {
            webViewInterface.onFocusChange(z);
        }
    }

    private void initContentView(android.content.Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.getWindow() != null) {
                View findViewById = activity.getWindow().getDecorView().findViewById(16908290);
                if (findViewById instanceof FrameLayout) {
                    this.mContentView = (FrameLayout) findViewById;
                    this.mContentView.setBackgroundColor(Color.WHITE.getValue());
                }
            }
        }
    }

    public void setWebViewInterface(WebViewInterface webViewInterface) {
        this.mWebViewInterface = webViewInterface;
    }

    public void setWebViewBackground(RgbColor rgbColor) {
        int argb = Color.argb(rgbColor.getAlpha(), rgbColor.getRed(), rgbColor.getGreen(), rgbColor.getBlue());
        FrameLayout frameLayout = this.mContentView;
        if (frameLayout != null) {
            frameLayout.setBackgroundColor(argb);
        }
    }

    public void setWidth(int i) {
        this.mWebView.post(new Runnable(i) {
            /* class ohos.agp.components.webengine.adapter.$$Lambda$WebViewBridge$I_YEfUXVC0oHTwDVZhmAw9eoiQ */
            private final /* synthetic */ int f$1;

            {
                this.f$1 = r2;
            }

            public final void run() {
                WebViewBridge.this.lambda$setWidth$1$WebViewBridge(this.f$1);
            }
        });
    }

    public /* synthetic */ void lambda$setWidth$1$WebViewBridge(int i) {
        ViewGroup.LayoutParams layoutParams = this.mWebView.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = i;
        } else {
            layoutParams = new ViewGroup.LayoutParams(i, -1);
        }
        this.mWebView.setLayoutParams(layoutParams);
    }

    public void setHeight(int i) {
        this.mWebView.post(new Runnable(i) {
            /* class ohos.agp.components.webengine.adapter.$$Lambda$WebViewBridge$uYuigwxsPjud7mE_1RjkVNs3A0c */
            private final /* synthetic */ int f$1;

            {
                this.f$1 = r2;
            }

            public final void run() {
                WebViewBridge.this.lambda$setHeight$2$WebViewBridge(this.f$1);
            }
        });
    }

    public /* synthetic */ void lambda$setHeight$2$WebViewBridge(int i) {
        ViewGroup.LayoutParams layoutParams = this.mWebView.getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height = i;
        } else {
            layoutParams = new ViewGroup.LayoutParams(-1, i);
        }
        this.mWebView.setLayoutParams(layoutParams);
    }

    public void setPosition(float f, float f2) {
        this.mWebView.setX(f);
        this.mWebView.setY(f2);
    }

    public void addToWindow() {
        FrameLayout frameLayout = this.mContentView;
        if (frameLayout != null) {
            int childCount = frameLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (this.mContentView.getChildAt(i) == this.mWebView) {
                    HiLog.warn(TAG, "WebView has already in, just return.", new Object[0]);
                    return;
                }
            }
            if (this.mWebView.getParent() == null) {
                HiLog.debug(TAG, "WebView add to window.", new Object[0]);
                this.mContentView.addView(this.mWebView);
            }
        }
    }

    public void removeFromWindow() {
        FrameLayout frameLayout = this.mContentView;
        if (frameLayout != null) {
            frameLayout.removeView(this.mWebView);
        }
    }

    public void load(String str) {
        this.mWebView.loadUrl(str);
    }

    public void load(String str, String str2, boolean z) {
        this.mWebView.loadData(str, str2, z ? "base64" : null);
    }

    public void load(String str, String str2, String str3, String str4, String str5) {
        this.mWebView.loadDataWithBaseURL(str4, str, str2, str3, str5);
    }

    public WebConfig getWebConfig() {
        return this.mWebConfigBridge;
    }

    public Navigator getNavigator() {
        return this.mNavigator;
    }

    public ScaleController getScaleController() {
        return this.mScaleController;
    }

    public FontConfig getFontConfig() {
        return this.mWebConfigBridge.getFontConfig();
    }

    public void reload() {
        this.mWebView.reload();
    }

    public String getFirstRequestUrl() {
        return this.mWebView.getOriginalUrl();
    }

    public String getTitle() {
        return this.mWebView.getTitle();
    }

    public void onInactive() {
        this.mWebView.onPause();
    }

    public void onActive() {
        this.mWebView.onResume();
    }

    public void onStop() {
        this.mWebView.destroy();
    }

    public void executeJs(String str, AsyncCallback<String> asyncCallback) {
        this.mWebView.evaluateJavascript(str, asyncCallback == null ? null : new ValueCallback() {
            /* class ohos.agp.components.webengine.adapter.$$Lambda$5RUxybMiFznEcS1YkmXcw7tsQ */

            @Override // android.webkit.ValueCallback
            public final void onReceiveValue(Object obj) {
                AsyncCallback.this.onReceive((String) obj);
            }
        });
    }

    public void addJsCallback(String str, JsCallbackBridge jsCallbackBridge) {
        this.mWebView.addJavascriptInterface(jsCallbackBridge, str);
    }

    public void setWebAgent(WebAgentBridge webAgentBridge) {
        WebAgentBridge webAgentBridge2 = this.mWebAgentBridgeUsing;
        if (webAgentBridge2 == webAgentBridge) {
            HiLog.debug(TAG, "skip since web agent not really changed", new Object[0]);
            return;
        }
        if (webAgentBridge2 != null) {
            webAgentBridge2.removeAbutmentMap(this.mWebView);
            this.mWebAgentBridgeUsing.removeScaleChangeListener(this.mWebView);
        }
        if (webAgentBridge == null) {
            webAgentBridge = this.mScaleChangeListener == null ? null : this.mWebAgentBridgeObserveScaleChangeOnly;
        }
        this.mWebAgentBridgeUsing = webAgentBridge;
        WebAgentBridge webAgentBridge3 = this.mWebAgentBridgeUsing;
        if (webAgentBridge3 == null) {
            this.mWebView.setWebViewClient(null);
            return;
        }
        if (webAgentBridge3 != this.mWebAgentBridgeObserveScaleChangeOnly) {
            webAgentBridge3.putAbutmentMap(this.mWebView, this.mAbutmentZ);
        }
        this.mWebAgentBridgeUsing.putScaleChangeListener(this.mWebView, this.mScaleChangeListener);
        this.mWebView.setWebViewClient(this.mWebAgentBridgeUsing);
    }

    public void setBrowserAgent(BrowserAgentBridge browserAgentBridge) {
        BrowserAgentBridge browserAgentBridge2 = this.mBrowserAgentBridge;
        if (browserAgentBridge2 == browserAgentBridge) {
            HiLog.debug(TAG, "skip since browser agent not really changed", new Object[0]);
            return;
        }
        if (browserAgentBridge2 != null) {
            browserAgentBridge2.removeAbutmentMap(this.mWebView);
        }
        this.mBrowserAgentBridge = browserAgentBridge;
        BrowserAgentBridge browserAgentBridge3 = this.mBrowserAgentBridge;
        if (browserAgentBridge3 != null) {
            browserAgentBridge3.putAbutmentMap(this.mWebView, this.mAbutmentZ);
            this.mWebView.setWebChromeClient(this.mBrowserAgentBridge);
            this.mWebView.setDownloadListener(this.mDownloadListener);
            return;
        }
        this.mWebView.setWebChromeClient(null);
        this.mWebView.setDownloadListener(null);
    }

    public void setVisibility(int i) {
        if (i == 0) {
            this.mWebView.setVisibility(0);
        } else if (i == 1) {
            this.mWebView.setVisibility(4);
        } else if (i != 2) {
            HiLog.warn(TAG, "unsupported type.", new Object[0]);
        } else {
            this.mWebView.setVisibility(8);
        }
    }

    public boolean requestFocus() {
        return this.mWebView.requestFocus();
    }

    public void clearFocus() {
        this.mWebView.clearFocus();
    }

    public boolean executeLongClick() {
        return this.mWebView.performLongClick();
    }

    public WebView getWebViewInstance() {
        return this.mWebView;
    }

    public void stopLoading() {
        this.mWebView.stopLoading();
    }

    public int getProgress() {
        return this.mWebView.getProgress();
    }

    public void post(String str, byte[] bArr) {
        this.mWebView.postUrl(str, bArr);
    }

    public void doFling(int i, int i2) {
        this.mWebView.flingScroll(i, i2);
    }

    public int getWebPageHeight() {
        return this.mWebView.getContentHeight();
    }

    public PixelMap getFavicon() {
        Bitmap favicon = this.mWebView.getFavicon();
        if (favicon == null) {
            return null;
        }
        this.mFavicon = favicon.copy(favicon.getConfig(), true);
        Bitmap bitmap = this.mFavicon;
        if (bitmap == null) {
            return null;
        }
        return ImageDoubleFwConverter.createShellPixelMap(bitmap);
    }

    public String getCurrentUrl() {
        return this.mWebView.getUrl();
    }

    public boolean pageDown(boolean z) {
        return this.mWebView.pageDown(z);
    }

    public boolean pageUp(boolean z) {
        return this.mWebView.pageUp(z);
    }

    public void removeJsCallback(String str) {
        this.mWebView.removeJavascriptInterface(str);
    }

    public void clearMemoryCache() {
        this.mWebView.clearCache(false);
    }

    public void clearAllCache() {
        this.mWebView.clearCache(true);
    }

    public void notifyJsOnline(boolean z) {
        this.mWebView.setNetworkAvailable(z);
    }

    public boolean canScroll(int i, int i2) {
        if (i == 0) {
            return this.mWebView.canScrollHorizontally(i2);
        }
        if (i == 1) {
            return this.mWebView.canScrollVertically(i2);
        }
        return false;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setScaleChangeListener(ScaleChangeListener scaleChangeListener) {
        this.mScaleChangeListener = scaleChangeListener;
        if (this.mWebAgentBridgeUsing != null || this.mScaleChangeListener != null) {
            WebAgentBridge webAgentBridge = this.mWebAgentBridgeUsing;
            if (webAgentBridge == null) {
                this.mWebAgentBridgeUsing = this.mWebAgentBridgeObserveScaleChangeOnly;
                this.mWebAgentBridgeUsing.putScaleChangeListener(this.mWebView, this.mScaleChangeListener);
                this.mWebView.setWebViewClient(this.mWebAgentBridgeUsing);
            } else if (this.mScaleChangeListener == null && webAgentBridge == this.mWebAgentBridgeObserveScaleChangeOnly) {
                webAgentBridge.removeScaleChangeListener(this.mWebView);
                this.mWebAgentBridgeUsing = null;
                this.mWebView.setWebViewClient(null);
            } else {
                this.mWebAgentBridgeUsing.putScaleChangeListener(this.mWebView, this.mScaleChangeListener);
            }
        }
    }

    private static class NavigatorInner implements Navigator {
        private final WebView mWebViewNavigator;

        private NavigatorInner(WebView webView) {
            this.mWebViewNavigator = webView;
        }

        @Override // ohos.agp.components.webengine.Navigator
        public boolean canGoBack() {
            return this.mWebViewNavigator.canGoBack();
        }

        @Override // ohos.agp.components.webengine.Navigator
        public void goBack() {
            this.mWebViewNavigator.goBack();
        }

        @Override // ohos.agp.components.webengine.Navigator
        public boolean canGoForward() {
            return this.mWebViewNavigator.canGoForward();
        }

        @Override // ohos.agp.components.webengine.Navigator
        public void goForward() {
            this.mWebViewNavigator.goForward();
        }

        @Override // ohos.agp.components.webengine.Navigator
        public void clear() {
            this.mWebViewNavigator.clearHistory();
        }

        @Override // ohos.agp.components.webengine.Navigator
        public BrowsingListBridge copyBrowsingList() {
            return BrowsingListBridge.create(this.mWebViewNavigator.copyBackForwardList());
        }

        @Override // ohos.agp.components.webengine.Navigator
        public boolean canGo(int i) {
            return this.mWebViewNavigator.canGoBackOrForward(i);
        }

        @Override // ohos.agp.components.webengine.Navigator
        public void go(int i) {
            this.mWebViewNavigator.goBackOrForward(i);
        }
    }

    private static class ScaleControllerInner implements ScaleController {
        private static final int DEFAULT_TEXT_SCALE = 100;
        private final WebSettings mWebSettings;
        private final WebView mWebView;
        private final WebViewBridge mWebViewBridge;

        private ScaleControllerInner(WebView webView, WebViewBridge webViewBridge) {
            this.mWebView = webView;
            this.mWebSettings = this.mWebView.getSettings();
            this.mWebViewBridge = webViewBridge;
        }

        @Override // ohos.agp.components.webengine.ScaleController
        public void setScale(int i) {
            this.mWebView.setInitialScale(i);
        }

        @Override // ohos.agp.components.webengine.ScaleController
        public boolean scaleUp() {
            return this.mWebView.zoomIn();
        }

        @Override // ohos.agp.components.webengine.ScaleController
        public boolean scaleDown() {
            return this.mWebView.zoomOut();
        }

        @Override // ohos.agp.components.webengine.ScaleController
        public void setScalable(boolean z) {
            WebSettings webSettings = this.mWebSettings;
            if (webSettings != null) {
                webSettings.setSupportZoom(z);
            }
        }

        @Override // ohos.agp.components.webengine.ScaleController
        public boolean isScalable() {
            WebSettings webSettings = this.mWebSettings;
            if (webSettings == null) {
                return true;
            }
            return webSettings.supportZoom();
        }

        @Override // ohos.agp.components.webengine.ScaleController
        public void setGestureScalable(boolean z) {
            WebSettings webSettings = this.mWebSettings;
            if (webSettings != null) {
                webSettings.setBuiltInZoomControls(z);
            }
        }

        @Override // ohos.agp.components.webengine.ScaleController
        public boolean isGestureScalable() {
            WebSettings webSettings = this.mWebSettings;
            if (webSettings == null) {
                return false;
            }
            return webSettings.getBuiltInZoomControls();
        }

        @Override // ohos.agp.components.webengine.ScaleController
        public void setTextScale(int i) {
            WebSettings webSettings = this.mWebSettings;
            if (webSettings != null) {
                webSettings.setTextZoom(i);
            }
        }

        @Override // ohos.agp.components.webengine.ScaleController
        public int getTextScale() {
            WebSettings webSettings = this.mWebSettings;
            if (webSettings == null) {
                return 100;
            }
            return webSettings.getTextZoom();
        }

        @Override // ohos.agp.components.webengine.ScaleController
        public void setScaleChangeListener(ScaleChangeListener scaleChangeListener) {
            this.mWebViewBridge.setScaleChangeListener(scaleChangeListener);
        }
    }
}
