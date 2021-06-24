package ohos.agp.components.webengine;

import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.components.webengine.adapter.ResourceResponseBridge;
import ohos.agp.components.webengine.adapter.WebAgentBridge;
import ohos.agp.components.webengine.adapter.WebAgentInterface;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.PixelMap;
import ohos.net.http.SslError;

public class WebAgent {
    private static final HiLogLabel TAG = new HiLogLabel(3, LogDomain.END, "WebAgent");
    private final WebAgentBridge mWebAgentBridge = new WebAgentBridge();

    public boolean isNeedLoadUrl(WebView webView, ResourceRequest resourceRequest) {
        return true;
    }

    public void onError(WebView webView, ResourceRequest resourceRequest, ResourceError resourceError) {
    }

    public void onHttpError(WebView webView, ResourceRequest resourceRequest, ResourceResponse resourceResponse) {
    }

    public void onLoadingContent(WebView webView, String str) {
    }

    public void onLoadingPage(WebView webView, String str, PixelMap pixelMap) {
    }

    public void onLogin(WebView webView, String str, String str2, String str3) {
    }

    public void onPageLoaded(WebView webView, String str) {
    }

    public void onSslError(WebView webView, SslError sslError) {
    }

    public ResourceResponse processResourceRequest(WebView webView, ResourceRequest resourceRequest) {
        return null;
    }

    public WebAgent() {
        this.mWebAgentBridge.setWebAgentInterface(new WebAgentInterface() {
            /* class ohos.agp.components.webengine.WebAgent.AnonymousClass1 */

            @Override // ohos.agp.components.webengine.adapter.WebAgentInterface
            public boolean isNeedLoadUrl(Object obj, ResourceRequest resourceRequest) {
                return WebAgent.this.isNeedLoadUrl(obj instanceof WebView ? (WebView) obj : null, resourceRequest);
            }

            @Override // ohos.agp.components.webengine.adapter.WebAgentInterface
            public void onError(Object obj, ResourceRequest resourceRequest, ResourceError resourceError) {
                WebAgent.this.onError(obj instanceof WebView ? (WebView) obj : null, resourceRequest, resourceError);
            }

            @Override // ohos.agp.components.webengine.adapter.WebAgentInterface
            public void onLoadingPage(Object obj, String str, PixelMap pixelMap) {
                WebAgent.this.onLoadingPage(obj instanceof WebView ? (WebView) obj : null, str, pixelMap);
            }

            @Override // ohos.agp.components.webengine.adapter.WebAgentInterface
            public void onPageLoaded(Object obj, String str) {
                WebAgent.this.onPageLoaded(obj instanceof WebView ? (WebView) obj : null, str);
            }

            @Override // ohos.agp.components.webengine.adapter.WebAgentInterface
            public void onLoadingContent(Object obj, String str) {
                WebAgent.this.onLoadingContent(obj instanceof WebView ? (WebView) obj : null, str);
            }

            @Override // ohos.agp.components.webengine.adapter.WebAgentInterface
            public void onCertificateRequested(Object obj, CertificateRequest certificateRequest) {
                WebAgent.this.onCertificateRequested(obj instanceof WebView ? (WebView) obj : null, certificateRequest);
            }

            @Override // ohos.agp.components.webengine.adapter.WebAgentInterface
            public void onAuthRequested(Object obj, AuthRequest authRequest, String str, String str2) {
                WebAgent.this.onAuthRequested(obj instanceof WebView ? (WebView) obj : null, authRequest, str, str2);
            }

            @Override // ohos.agp.components.webengine.adapter.WebAgentInterface
            public void onLogin(Object obj, String str, String str2, String str3) {
                WebAgent.this.onLogin(obj instanceof WebView ? (WebView) obj : null, str, str2, str3);
            }

            @Override // ohos.agp.components.webengine.adapter.WebAgentInterface
            public void onHttpError(Object obj, ResourceRequest resourceRequest, ResourceResponseBridge resourceResponseBridge) {
                WebAgent.this.onHttpError(obj instanceof WebView ? (WebView) obj : null, resourceRequest, ResourceResponse.create(resourceResponseBridge));
            }

            @Override // ohos.agp.components.webengine.adapter.WebAgentInterface
            public void onSslError(Object obj, SslError sslError) {
                WebAgent.this.onSslError(obj instanceof WebView ? (WebView) obj : null, sslError);
            }

            @Override // ohos.agp.components.webengine.adapter.WebAgentInterface
            public ResourceResponseBridge processResourceRequest(Object obj, ResourceRequest resourceRequest) {
                ResourceResponse processResourceRequest = WebAgent.this.processResourceRequest(obj instanceof WebView ? (WebView) obj : null, resourceRequest);
                if (processResourceRequest == null) {
                    return null;
                }
                return processResourceRequest.getResourceResponseBridge();
            }
        });
    }

    public void onCertificateRequested(WebView webView, CertificateRequest certificateRequest) {
        if (certificateRequest != null) {
            certificateRequest.cancelAlways();
        }
    }

    public void onAuthRequested(WebView webView, AuthRequest authRequest, String str, String str2) {
        if (authRequest != null) {
            authRequest.cancel();
        }
    }

    /* access modifiers changed from: package-private */
    public WebAgentBridge getWebAgentBridge() {
        return this.mWebAgentBridge;
    }
}
