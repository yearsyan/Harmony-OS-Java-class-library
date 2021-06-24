package ohos.agp.components.webengine.adapter;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.concurrent.ConcurrentHashMap;
import ohos.agp.components.webengine.ScaleChangeListener;
import ohos.media.image.PixelMap;
import ohos.media.image.inner.ImageDoubleFwConverter;

public class WebAgentBridge extends WebViewClient {
    private final ConcurrentHashMap<Object, Object> mAbutmentMap = new ConcurrentHashMap<>(1);
    private Bitmap mFavicon;
    private final ConcurrentHashMap<Object, ScaleChangeListener> mListenerMap = new ConcurrentHashMap<>(1);
    private WebAgentInterface mWebAgentInterface;

    public void setWebAgentInterface(WebAgentInterface webAgentInterface) {
        this.mWebAgentInterface = webAgentInterface;
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

    /* access modifiers changed from: package-private */
    public void putScaleChangeListener(Object obj, ScaleChangeListener scaleChangeListener) {
        if (obj != null && scaleChangeListener != null) {
            this.mListenerMap.put(obj, scaleChangeListener);
        }
    }

    /* access modifiers changed from: package-private */
    public void removeScaleChangeListener(Object obj) {
        if (obj != null) {
            this.mListenerMap.remove(obj);
        }
    }

    @Override // android.webkit.WebViewClient
    public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
        WebAgentInterface webAgentInterface;
        if (webView == null || (webAgentInterface = this.mWebAgentInterface) == null) {
            return false;
        }
        return !webAgentInterface.isNeedLoadUrl(this.mAbutmentMap.get(webView), ResourceRequestBridge.create(webResourceRequest));
    }

    public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        if (webView != null && this.mWebAgentInterface != null) {
            PixelMap pixelMap = null;
            if (bitmap != null) {
                this.mFavicon = bitmap.copy(bitmap.getConfig(), true);
                Bitmap bitmap2 = this.mFavicon;
                if (bitmap2 != null) {
                    pixelMap = ImageDoubleFwConverter.createShellPixelMap(bitmap2);
                }
            }
            this.mWebAgentInterface.onLoadingPage(this.mAbutmentMap.get(webView), str, pixelMap);
        }
    }

    public void onPageFinished(WebView webView, String str) {
        WebAgentInterface webAgentInterface;
        if (webView != null && (webAgentInterface = this.mWebAgentInterface) != null) {
            webAgentInterface.onPageLoaded(this.mAbutmentMap.get(webView), str);
        }
    }

    public void onLoadResource(WebView webView, String str) {
        WebAgentInterface webAgentInterface;
        if (webView != null && (webAgentInterface = this.mWebAgentInterface) != null) {
            webAgentInterface.onLoadingContent(this.mAbutmentMap.get(webView), str);
        }
    }

    public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
        WebAgentInterface webAgentInterface;
        if (webView != null && (webAgentInterface = this.mWebAgentInterface) != null) {
            webAgentInterface.onError(this.mAbutmentMap.get(webView), ResourceRequestBridge.create(webResourceRequest), ResourceErrorBridge.create(webResourceError));
        }
    }

    public void onReceivedClientCertRequest(WebView webView, ClientCertRequest clientCertRequest) {
        WebAgentInterface webAgentInterface;
        if (webView != null && (webAgentInterface = this.mWebAgentInterface) != null) {
            webAgentInterface.onCertificateRequested(this.mAbutmentMap.get(webView), CertificateRequestBridge.create(clientCertRequest));
        }
    }

    public void onReceivedHttpAuthRequest(WebView webView, HttpAuthHandler httpAuthHandler, String str, String str2) {
        WebAgentInterface webAgentInterface;
        if (webView != null && (webAgentInterface = this.mWebAgentInterface) != null) {
            webAgentInterface.onAuthRequested(this.mAbutmentMap.get(webView), AuthRequestBridge.create(httpAuthHandler), str, str2);
        }
    }

    public void onReceivedLoginRequest(WebView webView, String str, String str2, String str3) {
        WebAgentInterface webAgentInterface;
        if (webView != null && (webAgentInterface = this.mWebAgentInterface) != null) {
            webAgentInterface.onLogin(this.mAbutmentMap.get(webView), str, str2, str3);
        }
    }

    public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
        WebAgentInterface webAgentInterface;
        if (webView != null && (webAgentInterface = this.mWebAgentInterface) != null) {
            webAgentInterface.onHttpError(this.mAbutmentMap.get(webView), ResourceRequestBridge.create(webResourceRequest), ResourceResponseBridge.create(webResourceResponse));
        }
    }

    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        WebAgentInterface webAgentInterface;
        if (sslErrorHandler != null) {
            sslErrorHandler.cancel();
        }
        if (!(webView == null || (webAgentInterface = this.mWebAgentInterface) == null)) {
            if (sslError == null) {
                webAgentInterface.onSslError(this.mAbutmentMap.get(webView), null);
                return;
            }
            int primaryError = sslError.getPrimaryError();
            ohos.net.http.SslError sslError2 = new ohos.net.http.SslError(primaryError, sslError.getCertificate().getX509Certificate(), sslError.getUrl());
            for (int i = 1; i < primaryError; i++) {
                if (sslError.hasError(i)) {
                    sslError2.addError(i);
                }
            }
            this.mWebAgentInterface.onSslError(this.mAbutmentMap.get(webView), sslError2);
        }
    }

    @Override // android.webkit.WebViewClient
    public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
        WebAgentInterface webAgentInterface;
        ResourceResponseBridge processResourceRequest;
        if (webView == null || (webAgentInterface = this.mWebAgentInterface) == null || (processResourceRequest = webAgentInterface.processResourceRequest(this.mAbutmentMap.get(webView), ResourceRequestBridge.create(webResourceRequest))) == null) {
            return null;
        }
        return processResourceRequest.getWebResourceResponse();
    }

    public void onScaleChanged(WebView webView, float f, float f2) {
        ScaleChangeListener scaleChangeListener = this.mListenerMap.get(webView);
        if (scaleChangeListener != null) {
            scaleChangeListener.onChange(f, f2);
        }
    }
}
