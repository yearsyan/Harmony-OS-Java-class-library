package ohos.agp.components.webengine.adapter;

import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import java.util.Set;
import ohos.agp.components.webengine.AsyncCallback;

public class LocationAccessControllerBridge {
    private final GeolocationPermissions mGeolocationPermissions = GeolocationPermissions.getInstance();

    public void setOriginAllow(String str, boolean z) {
        if (z) {
            this.mGeolocationPermissions.allow(str);
        } else {
            this.mGeolocationPermissions.clear(str);
        }
    }

    public void disallowAll() {
        this.mGeolocationPermissions.clearAll();
    }

    public void checkOriginAllowed(String str, AsyncCallback<Boolean> asyncCallback) {
        if (asyncCallback != null) {
            this.mGeolocationPermissions.getAllowed(str, new ValueCallback() {
                /* class ohos.agp.components.webengine.adapter.$$Lambda$mmZASEuj2aE5aHrxb57u9e4X8Wc */

                @Override // android.webkit.ValueCallback
                public final void onReceiveValue(Object obj) {
                    AsyncCallback.this.onReceive((Boolean) obj);
                }
            });
        }
    }

    public void getAllOrigins(AsyncCallback<Set<String>> asyncCallback) {
        if (asyncCallback != null) {
            this.mGeolocationPermissions.getOrigins(new ValueCallback() {
                /* class ohos.agp.components.webengine.adapter.$$Lambda$D0hyGw7ajMFamjmELBILST8VuXs */

                @Override // android.webkit.ValueCallback
                public final void onReceiveValue(Object obj) {
                    AsyncCallback.this.onReceive((Set) obj);
                }
            });
        }
    }
}
