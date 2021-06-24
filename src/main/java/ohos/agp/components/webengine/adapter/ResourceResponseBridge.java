package ohos.agp.components.webengine.adapter;

import android.webkit.WebResourceResponse;
import java.io.InputStream;
import java.util.Map;

public class ResourceResponseBridge {
    private final WebResourceResponse mWebResourceResponse;

    private ResourceResponseBridge(WebResourceResponse webResourceResponse) {
        this.mWebResourceResponse = webResourceResponse;
    }

    public ResourceResponseBridge(String str, InputStream inputStream, String str2) {
        this.mWebResourceResponse = new WebResourceResponse(str, str2, inputStream);
    }

    static ResourceResponseBridge create(WebResourceResponse webResourceResponse) {
        if (webResourceResponse == null) {
            return null;
        }
        return new ResourceResponseBridge(webResourceResponse);
    }

    /* access modifiers changed from: package-private */
    public WebResourceResponse getWebResourceResponse() {
        return this.mWebResourceResponse;
    }

    public Map<String, String> getHeaders() {
        return this.mWebResourceResponse.getResponseHeaders();
    }

    public void setHeaders(Map<String, String> map) {
        this.mWebResourceResponse.setResponseHeaders(map);
    }

    public InputStream getData() {
        return this.mWebResourceResponse.getData();
    }

    public String getCharset() {
        return this.mWebResourceResponse.getEncoding();
    }

    public void setData(InputStream inputStream, String str) {
        this.mWebResourceResponse.setData(inputStream);
        this.mWebResourceResponse.setEncoding(str);
    }

    public String getMimeType() {
        return this.mWebResourceResponse.getMimeType();
    }

    public void setMimeType(String str) {
        this.mWebResourceResponse.setMimeType(str);
    }

    public int getStatus() {
        return this.mWebResourceResponse.getStatusCode();
    }

    public String getReason() {
        return this.mWebResourceResponse.getReasonPhrase();
    }

    public void setStatusAndReason(int i, String str) {
        this.mWebResourceResponse.setStatusCodeAndReasonPhrase(i, str);
    }
}
