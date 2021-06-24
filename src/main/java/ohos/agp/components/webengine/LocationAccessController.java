package ohos.agp.components.webengine;

import java.util.Set;
import ohos.agp.components.webengine.adapter.LocationAccessControllerBridge;

public abstract class LocationAccessController {

    public interface Response {
        void apply(String str, boolean z, boolean z2);
    }

    public abstract void checkOriginAllowed(String str, AsyncCallback<Boolean> asyncCallback);

    public abstract void disallowAll();

    public abstract void getAllOrigins(AsyncCallback<Set<String>> asyncCallback);

    public abstract void setOriginAllow(String str, boolean z);

    public static LocationAccessController getInstance() {
        return LocationAccessControllerHolder.INSTANCE;
    }

    private static class LocationAccessControllerHolder {
        private static final LocationAccessController INSTANCE = new LocationAccessController() {
            /* class ohos.agp.components.webengine.LocationAccessController.LocationAccessControllerHolder.AnonymousClass1 */
            private final LocationAccessControllerBridge mBridge = new LocationAccessControllerBridge();

            @Override // ohos.agp.components.webengine.LocationAccessController
            public void setOriginAllow(String str, boolean z) {
                this.mBridge.setOriginAllow(str, z);
            }

            @Override // ohos.agp.components.webengine.LocationAccessController
            public void disallowAll() {
                this.mBridge.disallowAll();
            }

            @Override // ohos.agp.components.webengine.LocationAccessController
            public void checkOriginAllowed(String str, AsyncCallback<Boolean> asyncCallback) {
                if (asyncCallback != null) {
                    this.mBridge.checkOriginAllowed(str, asyncCallback);
                }
            }

            @Override // ohos.agp.components.webengine.LocationAccessController
            public void getAllOrigins(AsyncCallback<Set<String>> asyncCallback) {
                if (asyncCallback != null) {
                    this.mBridge.getAllOrigins(asyncCallback);
                }
            }
        };

        private LocationAccessControllerHolder() {
        }
    }
}
