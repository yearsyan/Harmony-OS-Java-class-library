package ohos.agp.components.webengine.adapter;

import android.webkit.JavascriptInterface;
import ohos.agp.components.webengine.JsCallback;

public class JsCallbackBridge {
    private final JsCallback mJsCallback;

    private JsCallbackBridge(JsCallback jsCallback) {
        this.mJsCallback = jsCallback;
    }

    public static JsCallbackBridge create(JsCallback jsCallback) {
        if (jsCallback == null) {
            return null;
        }
        return new JsCallbackBridge(jsCallback);
    }

    @JavascriptInterface
    public String call(String str) {
        return this.mJsCallback.onCallback(str);
    }
}
