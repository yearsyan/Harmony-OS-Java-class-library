package ohos.agp.components.webengine.adapter;

import ohos.agp.components.webengine.AsyncCallback;
import ohos.agp.components.webengine.JsMessageResult;
import ohos.agp.components.webengine.JsTextInputResult;
import ohos.agp.components.webengine.PickFilesParams;
import ohos.utils.net.Uri;

public interface BrowserAgentInterface {
    void onDownload(String str, String str2, String str3, String str4, long j);

    boolean onJsMessageShow(Object obj, String str, String str2, boolean z, JsMessageResult jsMessageResult);

    boolean onJsTextInput(Object obj, String str, String str2, String str3, JsTextInputResult jsTextInputResult);

    void onLocationApiAccessCancel();

    void onLocationApiAccessRequest(String str, LocationAccessResponseInterface locationAccessResponseInterface);

    boolean onPickFiles(Object obj, AsyncCallback<Uri[]> asyncCallback, PickFilesParams pickFilesParams);

    void onProgressUpdated(Object obj, int i);

    void onTitleUpdated(Object obj, String str);
}
