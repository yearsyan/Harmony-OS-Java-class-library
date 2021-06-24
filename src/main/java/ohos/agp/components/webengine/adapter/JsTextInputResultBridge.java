package ohos.agp.components.webengine.adapter;

import android.webkit.JsPromptResult;
import ohos.agp.components.webengine.JsTextInputResult;

public class JsTextInputResultBridge implements JsTextInputResult {
    private final JsPromptResult mJsPromptResult;

    private JsTextInputResultBridge(JsPromptResult jsPromptResult) {
        this.mJsPromptResult = jsPromptResult;
    }

    static JsTextInputResultBridge create(JsPromptResult jsPromptResult) {
        if (jsPromptResult == null) {
            return null;
        }
        return new JsTextInputResultBridge(jsPromptResult);
    }

    @Override // ohos.agp.components.webengine.JsTextInputResult
    public void respond(String str) {
        this.mJsPromptResult.confirm(str);
    }
}
