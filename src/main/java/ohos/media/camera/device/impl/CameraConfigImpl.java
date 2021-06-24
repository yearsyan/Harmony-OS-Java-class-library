package ohos.media.camera.device.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import ohos.agp.graphics.Surface;
import ohos.eventhandler.EventHandler;
import ohos.media.camera.device.CameraAbility;
import ohos.media.camera.device.CameraConfig;
import ohos.media.camera.device.FrameStateCallback;
import ohos.media.image.common.Size;
import ohos.media.utils.log.Logger;
import ohos.media.utils.log.LoggerFactory;

public class CameraConfigImpl implements CameraConfig {
    private static final Logger LOGGER = LoggerFactory.getCameraLogger(CameraConfigImpl.class);
    private final List<StreamDescription> deferredStreamList = new ArrayList();
    private FrameStateCallback frameStateCallback;
    private EventHandler handler;
    private boolean isDeferredConfig = false;
    private boolean isDeferredSurfaceAttached = false;
    private final List<StreamDescription> normalStreamList = new ArrayList();
    @CameraAbility.CameraRunningMode
    private int runningMode = 0;

    public CameraConfigImpl() {
    }

    public CameraConfigImpl(CameraConfigImpl cameraConfigImpl) {
        this.normalStreamList.addAll(cameraConfigImpl.normalStreamList);
        this.deferredStreamList.addAll(cameraConfigImpl.deferredStreamList);
        this.frameStateCallback = cameraConfigImpl.frameStateCallback;
        this.handler = cameraConfigImpl.handler;
        this.isDeferredSurfaceAttached = cameraConfigImpl.isDeferredSurfaceAttached;
        this.isDeferredConfig = cameraConfigImpl.isDeferredConfig;
        this.runningMode = cameraConfigImpl.runningMode;
    }

    public void addSurface(Surface surface) {
        Objects.requireNonNull(surface, "surface should not be null!");
        this.normalStreamList.add(new StreamDescription(surface));
    }

    public void removeSurface(Surface surface) {
        Objects.requireNonNull(surface, "surface should not be null!");
        if (!this.normalStreamList.removeIf(new Predicate() {
            /* class ohos.media.camera.device.impl.$$Lambda$CameraConfigImpl$DRvQAkTZsCkqV7BSj7MwLRzqsuo */

            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return CameraConfigImpl.lambda$removeSurface$0(Surface.this, (StreamDescription) obj);
            }
        }) && this.deferredStreamList.removeIf(new Predicate() {
            /* class ohos.media.camera.device.impl.$$Lambda$CameraConfigImpl$4b0Fe4pl7PFTorrUDNVhIYhFr98 */

            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return CameraConfigImpl.lambda$removeSurface$1(Surface.this, (StreamDescription) obj);
            }
        })) {
            clearDeferredStatus();
        }
    }

    @Override // ohos.media.camera.device.CameraConfig
    public List<Surface> getSurfaces() {
        ArrayList arrayList = new ArrayList();
        for (StreamDescription streamDescription : this.normalStreamList) {
            arrayList.add(streamDescription.getSurface());
        }
        return arrayList;
    }

    private List<Surface> getDeferredSurfaces() {
        ArrayList arrayList = new ArrayList();
        for (StreamDescription streamDescription : this.deferredStreamList) {
            arrayList.add(streamDescription.getSurface());
        }
        return arrayList;
    }

    public <T> void addDeferredSurfaceSize(Size size, Class<T> cls) {
        Objects.requireNonNull(size, "surfaceSize should not be null!");
        Objects.requireNonNull(cls, "clazz should not be null!");
        if (size.width <= 0 || size.height <= 0) {
            throw new IllegalArgumentException("surfaceSize is invalid!");
        } else if (!this.isDeferredConfig) {
            this.deferredStreamList.add(new StreamDescription(size, cls));
            this.isDeferredConfig = true;
            this.isDeferredSurfaceAttached = false;
        } else if (!this.isDeferredSurfaceAttached) {
            this.deferredStreamList.clear();
            this.deferredStreamList.add(new StreamDescription(size, cls));
        } else {
            throw new IllegalStateException("Cannot addDeferredSurfaceSize after addDeferredSurface!");
        }
    }

    public void addDeferredSurface(Surface surface) {
        Objects.requireNonNull(surface, "surface should not be null!");
        if (!this.isDeferredConfig || this.deferredStreamList.isEmpty()) {
            throw new IllegalStateException("Deferred surface size is not added!");
        }
        for (StreamDescription streamDescription : this.normalStreamList) {
            if (streamDescription.getSurface() == surface) {
                throw new IllegalArgumentException("Surface has been already added as normal surface!");
            }
        }
        this.deferredStreamList.get(0).attachDeferredSurface(surface);
        this.isDeferredSurfaceAttached = true;
    }

    public List<StreamDescription> getNormalStreamList() {
        return this.normalStreamList;
    }

    public List<StreamDescription> getDeferredStreamList() {
        return this.deferredStreamList;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updateState() {
        if (this.isDeferredConfig && this.isDeferredSurfaceAttached) {
            if (this.deferredStreamList.isEmpty()) {
                LOGGER.info("deferredStreamList is empty", new Object[0]);
            } else {
                Surface surface = this.deferredStreamList.get(0).getSurface();
                if (surface == null) {
                    LOGGER.info("surface in deferredStreamList is null!", new Object[0]);
                } else {
                    this.normalStreamList.add(new StreamDescription(surface));
                }
                this.deferredStreamList.clear();
            }
            clearDeferredStatus();
        }
    }

    private void clearDeferredStatus() {
        this.isDeferredConfig = false;
        this.isDeferredSurfaceAttached = false;
    }

    public void setFrameStateCallback(FrameStateCallback frameStateCallback2, EventHandler eventHandler) {
        if (frameStateCallback2 == null) {
            this.frameStateCallback = null;
            this.handler = null;
            return;
        }
        Objects.requireNonNull(eventHandler, "The handler for frame state callback should not be null!");
        this.frameStateCallback = frameStateCallback2;
        this.handler = eventHandler;
    }

    public void setRunningMode(@CameraAbility.CameraRunningMode int i) {
        this.runningMode = i;
    }

    @CameraAbility.CameraRunningMode
    public int getRunningMode() {
        return this.runningMode;
    }

    /* access modifiers changed from: package-private */
    public FrameStateCallback getFrameStateCallback() {
        return this.frameStateCallback;
    }

    /* access modifiers changed from: package-private */
    public EventHandler getFrameStateHandler() {
        return this.handler;
    }

    @Override // ohos.media.camera.device.CameraConfig
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CameraConfigImpl cameraConfigImpl = (CameraConfigImpl) obj;
        if (this.normalStreamList.equals(cameraConfigImpl.normalStreamList)) {
            LOGGER.info("normalStreamList is equals", new Object[0]);
        }
        if (this.deferredStreamList.equals(cameraConfigImpl.deferredStreamList)) {
            LOGGER.info("deferredStreamList is equals", new Object[0]);
        }
        if (Objects.equals(this.frameStateCallback, cameraConfigImpl.frameStateCallback)) {
            LOGGER.info("frameStateCallback is equals", new Object[0]);
        }
        if (Objects.equals(this.handler, cameraConfigImpl.handler)) {
            LOGGER.info("handler is equals", new Object[0]);
        }
        if (this.isDeferredSurfaceAttached == cameraConfigImpl.isDeferredSurfaceAttached) {
            LOGGER.info("isDeferredSurfaceAttached is equals", new Object[0]);
        }
        if (this.isDeferredConfig == cameraConfigImpl.isDeferredConfig) {
            LOGGER.info("isDeferredConfig is equals", new Object[0]);
        }
        if (this.runningMode == cameraConfigImpl.runningMode) {
            LOGGER.info("runningMode is equals", new Object[0]);
        }
        return this.normalStreamList.equals(cameraConfigImpl.normalStreamList) && this.deferredStreamList.equals(cameraConfigImpl.deferredStreamList) && Objects.equals(this.frameStateCallback, cameraConfigImpl.frameStateCallback) && Objects.equals(this.handler, cameraConfigImpl.handler) && this.isDeferredSurfaceAttached == cameraConfigImpl.isDeferredSurfaceAttached && this.isDeferredConfig == cameraConfigImpl.isDeferredConfig && this.runningMode == cameraConfigImpl.runningMode;
    }

    public int hashCode() {
        return Objects.hash(this.normalStreamList, this.deferredStreamList, this.frameStateCallback, Boolean.valueOf(this.isDeferredSurfaceAttached), Boolean.valueOf(this.isDeferredConfig), Integer.valueOf(this.runningMode));
    }

    static final class Builder implements CameraConfig.Builder {
        private final CameraConfigImpl config;

        public Builder(CameraConfigImpl cameraConfigImpl) {
            this.config = cameraConfigImpl;
        }

        @Override // ohos.media.camera.device.CameraConfig.Builder
        public CameraConfig.Builder addSurface(Surface surface) {
            this.config.addSurface(surface);
            return this;
        }

        @Override // ohos.media.camera.device.CameraConfig.Builder
        public CameraConfig.Builder removeSurface(Surface surface) {
            this.config.removeSurface(surface);
            return this;
        }

        @Override // ohos.media.camera.device.CameraConfig.Builder
        public List<Surface> getSurfaces() {
            return this.config.getSurfaces();
        }

        @Override // ohos.media.camera.device.CameraConfig.Builder
        public <T> CameraConfig.Builder addDeferredSurfaceSize(Size size, Class<T> cls) {
            this.config.addDeferredSurfaceSize(size, cls);
            return this;
        }

        @Override // ohos.media.camera.device.CameraConfig.Builder
        public CameraConfig.Builder addDeferredSurface(Surface surface) {
            this.config.addDeferredSurface(surface);
            return this;
        }

        @Override // ohos.media.camera.device.CameraConfig.Builder
        public CameraConfig.Builder setFrameStateCallback(FrameStateCallback frameStateCallback, EventHandler eventHandler) {
            this.config.setFrameStateCallback(frameStateCallback, eventHandler);
            return this;
        }

        @Override // ohos.media.camera.device.CameraConfig.Builder
        public CameraConfig.Builder setRunningMode(@CameraAbility.CameraRunningMode int i) {
            this.config.setRunningMode(i);
            return this;
        }

        @Override // ohos.media.camera.device.CameraConfig.Builder
        public CameraConfig build() {
            CameraConfigImpl cameraConfigImpl = new CameraConfigImpl(this.config);
            this.config.updateState();
            return cameraConfigImpl;
        }
    }
}
