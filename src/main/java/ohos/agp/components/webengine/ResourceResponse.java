package ohos.agp.components.webengine;

import java.io.InputStream;
import java.util.Map;
import ohos.agp.components.webengine.adapter.ResourceResponseBridge;

public class ResourceResponse {
    private final ResourceResponseBridge mResourceResponseBridge;

    private ResourceResponse(ResourceResponseBridge resourceResponseBridge) {
        this.mResourceResponseBridge = resourceResponseBridge;
    }

    public ResourceResponse(String str, InputStream inputStream, String str2) {
        this.mResourceResponseBridge = new ResourceResponseBridge(str, inputStream, str2);
    }

    static ResourceResponse create(ResourceResponseBridge resourceResponseBridge) {
        if (resourceResponseBridge == null) {
            return null;
        }
        return new ResourceResponse(resourceResponseBridge);
    }

    /* access modifiers changed from: package-private */
    public ResourceResponseBridge getResourceResponseBridge() {
        return this.mResourceResponseBridge;
    }

    public Map<String, String> getHeaders() {
        return this.mResourceResponseBridge.getHeaders();
    }

    public void setHeaders(Map<String, String> map) {
        this.mResourceResponseBridge.setHeaders(map);
    }

    public InputStream getData() {
        return this.mResourceResponseBridge.getData();
    }

    public String getCharset() {
        return this.mResourceResponseBridge.getCharset();
    }

    public void setData(InputStream inputStream, String str) {
        this.mResourceResponseBridge.setData(inputStream, str);
    }

    public String getMimeType() {
        return this.mResourceResponseBridge.getMimeType();
    }

    public void setMimeType(String str) {
        this.mResourceResponseBridge.setMimeType(str);
    }

    public int getStatus() {
        return this.mResourceResponseBridge.getStatus();
    }

    public String getReason() {
        return this.mResourceResponseBridge.getReason();
    }

    public void setStatusAndReason(int i, String str) {
        this.mResourceResponseBridge.setStatusAndReason(i, str);
    }
}
