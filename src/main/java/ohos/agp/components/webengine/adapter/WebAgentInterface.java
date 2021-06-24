package ohos.agp.components.webengine.adapter;

import ohos.agp.components.webengine.AuthRequest;
import ohos.agp.components.webengine.CertificateRequest;
import ohos.agp.components.webengine.ResourceError;
import ohos.agp.components.webengine.ResourceRequest;
import ohos.media.image.PixelMap;
import ohos.net.http.SslError;

public interface WebAgentInterface {
    boolean isNeedLoadUrl(Object obj, ResourceRequest resourceRequest);

    void onAuthRequested(Object obj, AuthRequest authRequest, String str, String str2);

    void onCertificateRequested(Object obj, CertificateRequest certificateRequest);

    void onError(Object obj, ResourceRequest resourceRequest, ResourceError resourceError);

    void onHttpError(Object obj, ResourceRequest resourceRequest, ResourceResponseBridge resourceResponseBridge);

    void onLoadingContent(Object obj, String str);

    void onLoadingPage(Object obj, String str, PixelMap pixelMap);

    void onLogin(Object obj, String str, String str2, String str3);

    void onPageLoaded(Object obj, String str);

    void onSslError(Object obj, SslError sslError);

    ResourceResponseBridge processResourceRequest(Object obj, ResourceRequest resourceRequest);
}
