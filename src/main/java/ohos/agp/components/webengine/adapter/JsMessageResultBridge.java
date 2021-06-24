package ohos.agp.components.webengine.adapter;

import android.webkit.JsResult;
import ohos.agp.components.webengine.JsMessageResult;

public class JsMessageResultBridge implements JsMessageResult {
    private final JsResult mJsResult;

    private JsMessageResultBridge(JsResult jsResult) {
        this.mJsResult = jsResult;
    }

    static JsMessageResultBridge create(JsResult jsResult) {
        if (jsResult == null) {
            return null;
        }
        return new JsMessageResultBridge(jsResult);
    }

    @Override // ohos.agp.components.webengine.JsMessageResult
    public void confirm() {
        this.mJsResult.confirm();
    }

    @Override // ohos.agp.components.webengine.JsMessageResult
    public void cancel() {
        this.mJsResult.cancel();
    }
}
