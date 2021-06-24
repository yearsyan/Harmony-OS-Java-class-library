package ohos.agp.components.webengine.adapter;

import android.webkit.HttpAuthHandler;
import ohos.agp.components.webengine.AuthRequest;

public class AuthRequestBridge implements AuthRequest {
    private final HttpAuthHandler mHttpAuthHandler;

    private AuthRequestBridge(HttpAuthHandler httpAuthHandler) {
        this.mHttpAuthHandler = httpAuthHandler;
    }

    static AuthRequestBridge create(HttpAuthHandler httpAuthHandler) {
        if (httpAuthHandler == null) {
            return null;
        }
        return new AuthRequestBridge(httpAuthHandler);
    }

    @Override // ohos.agp.components.webengine.AuthRequest
    public void respond(String str, String str2) {
        this.mHttpAuthHandler.proceed(str, str2);
    }

    @Override // ohos.agp.components.webengine.AuthRequest
    public void cancel() {
        this.mHttpAuthHandler.cancel();
    }

    @Override // ohos.agp.components.webengine.AuthRequest
    public boolean isCredentialsStored() {
        return this.mHttpAuthHandler.useHttpAuthUsernamePassword();
    }
}
