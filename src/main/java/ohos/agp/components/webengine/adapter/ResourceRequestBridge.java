package ohos.agp.components.webengine.adapter;

import android.webkit.WebResourceRequest;
import java.util.Map;
import ohos.agp.components.webengine.ResourceRequest;
import ohos.utils.net.Uri;

public class ResourceRequestBridge implements ResourceRequest {
    private final WebResourceRequest mResourceRequest;

    private ResourceRequestBridge(WebResourceRequest webResourceRequest) {
        this.mResourceRequest = webResourceRequest;
    }

    static ResourceRequestBridge create(WebResourceRequest webResourceRequest) {
        if (webResourceRequest == null) {
            return null;
        }
        return new ResourceRequestBridge(webResourceRequest);
    }

    @Override // ohos.agp.components.webengine.ResourceRequest
    public Uri getRequestUrl() {
        android.net.Uri url = this.mResourceRequest.getUrl();
        return url == null ? Uri.EMPTY_URI : Uri.parse(url.toString());
    }

    @Override // ohos.agp.components.webengine.ResourceRequest
    public Map<String, String> getHeaders() {
        return this.mResourceRequest.getRequestHeaders();
    }

    @Override // ohos.agp.components.webengine.ResourceRequest
    public boolean isServerSideRedirected() {
        return this.mResourceRequest.isRedirect();
    }

    @Override // ohos.agp.components.webengine.ResourceRequest
    public boolean isMainFrame() {
        return this.mResourceRequest.isForMainFrame();
    }

    @Override // ohos.agp.components.webengine.ResourceRequest
    public String getMethod() {
        return this.mResourceRequest.getMethod();
    }
}
