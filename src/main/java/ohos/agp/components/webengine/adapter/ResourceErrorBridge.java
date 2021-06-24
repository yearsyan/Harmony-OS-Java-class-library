package ohos.agp.components.webengine.adapter;

import android.webkit.WebResourceError;
import ohos.agp.components.webengine.ResourceError;

public class ResourceErrorBridge implements ResourceError {
    private final WebResourceError mWebResourceError;

    private ResourceErrorBridge(WebResourceError webResourceError) {
        this.mWebResourceError = webResourceError;
    }

    static ResourceErrorBridge create(WebResourceError webResourceError) {
        if (webResourceError == null) {
            return null;
        }
        return new ResourceErrorBridge(webResourceError);
    }

    @Override // ohos.agp.components.webengine.ResourceError
    public int getErrorCode() {
        return this.mWebResourceError.getErrorCode();
    }

    @Override // ohos.agp.components.webengine.ResourceError
    public CharSequence getInfo() {
        return this.mWebResourceError.getDescription();
    }
}
