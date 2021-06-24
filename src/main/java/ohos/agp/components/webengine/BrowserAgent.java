package ohos.agp.components.webengine;

import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.components.webengine.LocationAccessController;
import ohos.agp.components.webengine.adapter.BrowserAgentBridge;
import ohos.agp.components.webengine.adapter.BrowserAgentInterface;
import ohos.agp.components.webengine.adapter.LocationAccessResponseInterface;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.net.Uri;

public class BrowserAgent {
    private static final HiLogLabel TAG = new HiLogLabel(3, LogDomain.END, "BrowserAgent");
    private BrowserAgentBridge mBrowserAgentBridge;

    public void onDownload(String str, String str2, String str3, String str4, long j) {
    }

    public boolean onJsMessageShow(WebView webView, String str, String str2, boolean z, JsMessageResult jsMessageResult) {
        return false;
    }

    public boolean onJsTextInput(WebView webView, String str, String str2, String str3, JsTextInputResult jsTextInputResult) {
        return false;
    }

    public void onLocationApiAccessCancel() {
    }

    public void onLocationApiAccessRequest(String str, LocationAccessController.Response response) {
    }

    public boolean onPickFiles(WebView webView, AsyncCallback<Uri[]> asyncCallback, PickFilesParams pickFilesParams) {
        return false;
    }

    public void onProgressUpdated(WebView webView, int i) {
    }

    public void onTitleUpdated(WebView webView, String str) {
    }

    public BrowserAgent(Context context) {
        if (context == null) {
            HiLog.error(TAG, "context is null", new Object[0]);
        } else {
            this.mBrowserAgentBridge = BrowserAgentBridge.create(context, new BrowserAgentInterface() {
                /* class ohos.agp.components.webengine.BrowserAgent.AnonymousClass1 */

                static /* synthetic */ void lambda$onLocationApiAccessRequest$0(String str, boolean z, boolean z2) {
                }

                @Override // ohos.agp.components.webengine.adapter.BrowserAgentInterface
                public void onTitleUpdated(Object obj, String str) {
                    BrowserAgent.this.onTitleUpdated(obj instanceof WebView ? (WebView) obj : null, str);
                }

                @Override // ohos.agp.components.webengine.adapter.BrowserAgentInterface
                public void onProgressUpdated(Object obj, int i) {
                    BrowserAgent.this.onProgressUpdated(obj instanceof WebView ? (WebView) obj : null, i);
                }

                @Override // ohos.agp.components.webengine.adapter.BrowserAgentInterface
                public boolean onJsMessageShow(Object obj, String str, String str2, boolean z, JsMessageResult jsMessageResult) {
                    return BrowserAgent.this.onJsMessageShow(obj instanceof WebView ? (WebView) obj : null, str, str2, z, jsMessageResult);
                }

                @Override // ohos.agp.components.webengine.adapter.BrowserAgentInterface
                public boolean onJsTextInput(Object obj, String str, String str2, String str3, JsTextInputResult jsTextInputResult) {
                    return BrowserAgent.this.onJsTextInput(obj instanceof WebView ? (WebView) obj : null, str, str2, str3, jsTextInputResult);
                }

                @Override // ohos.agp.components.webengine.adapter.BrowserAgentInterface
                public void onDownload(String str, String str2, String str3, String str4, long j) {
                    BrowserAgent.this.onDownload(str, str2, str3, str4, j);
                }

                @Override // ohos.agp.components.webengine.adapter.BrowserAgentInterface
                public void onLocationApiAccessRequest(String str, LocationAccessResponseInterface locationAccessResponseInterface) {
                    BrowserAgent.this.onLocationApiAccessRequest(str, locationAccessResponseInterface != null ? new LocationAccessController.Response() {
                        /* class ohos.agp.components.webengine.$$Lambda$jtr36wub1xQiL5TsRzHeyl7js */

                        @Override // ohos.agp.components.webengine.LocationAccessController.Response
                        public final void apply(String str, boolean z, boolean z2) {
                            LocationAccessResponseInterface.this.apply(str, z, z2);
                        }
                    } : $$Lambda$BrowserAgent$1$36Mvv1YOVyt2cnWWeDnnr2Y6xk.INSTANCE);
                }

                @Override // ohos.agp.components.webengine.adapter.BrowserAgentInterface
                public void onLocationApiAccessCancel() {
                    BrowserAgent.this.onLocationApiAccessCancel();
                }

                @Override // ohos.agp.components.webengine.adapter.BrowserAgentInterface
                public boolean onPickFiles(Object obj, AsyncCallback<Uri[]> asyncCallback, PickFilesParams pickFilesParams) {
                    return BrowserAgent.this.onPickFiles(obj instanceof WebView ? (WebView) obj : null, asyncCallback, pickFilesParams);
                }
            });
        }
    }

    /* access modifiers changed from: package-private */
    public BrowserAgentBridge getBrowserAgentBridge() {
        return this.mBrowserAgentBridge;
    }
}
