package ohos.media.camera.device.adapter;

import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.impl.CameraMetadataNative;
import android.hardware.camera2.params.MeteringRectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import ohos.agp.graphics.Surface;
import ohos.agp.utils.Rect;
import ohos.location.Location;
import ohos.media.camera.device.adapter.utils.CameraCoordinateUtil;
import ohos.media.camera.device.adapter.utils.Converter;
import ohos.media.camera.device.adapter.utils.SurfaceUtils;
import ohos.media.camera.params.adapter.InnerParameterKey;
import ohos.media.camera.params.adapter.ParameterKeyMapper;
import ohos.media.camera.params.adapter.StaticCameraCharacteristics;
import ohos.media.camera.params.adapter.camera2ex.CaptureRequestEx;
import ohos.media.camera.zidl.FrameConfigNative;
import ohos.media.image.common.Size;
import ohos.media.utils.log.Logger;
import ohos.media.utils.log.LoggerFactory;

public class FrameConfigMapper {
    private static final float DEFAULT_ZOOM_RATIO = 1.0f;
    private static final Logger LOGGER = LoggerFactory.getCameraLogger(FrameConfigMapper.class);
    private static final int REPROCESSABLE_SESSION_ID = -1;
    private final Map<Integer, CameraMetadataNative> defaultSettings = new HashMap();
    private final String logicalCameraId;
    private final Float maxZoomValue;
    private final Rect sensorArray;

    public FrameConfigMapper(String str, StaticCameraCharacteristics staticCameraCharacteristics) {
        this.logicalCameraId = str;
        this.sensorArray = Converter.convert2ZRect((android.graphics.Rect) staticCameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE));
        this.maxZoomValue = (Float) staticCameraCharacteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM);
    }

    public void addDefaultMetadataSetting(int i, CameraMetadataNative cameraMetadataNative) {
        this.defaultSettings.put(Integer.valueOf(i), cameraMetadataNative);
    }

    public CaptureRequest[] convert2CaptureRequests(List<FrameConfigNative> list) {
        ArrayList arrayList = new ArrayList();
        for (FrameConfigNative frameConfigNative : list) {
            int frameConfigType = frameConfigNative.getFrameConfigType();
            CameraMetadataNative cameraMetadataNative = this.defaultSettings.get(Integer.valueOf(frameConfigType));
            if (cameraMetadataNative == null) {
                LOGGER.warn("Failed to get default settings for template %{public}d", Integer.valueOf(frameConfigType));
            } else {
                CameraMetadataNative cameraRequestMetadatas = ParameterKeyMapper.getCameraRequestMetadatas(frameConfigNative.getConfigParameters(), cameraMetadataNative);
                setExtraParameters(cameraRequestMetadatas, frameConfigNative);
                CaptureRequest.Builder builder = new CaptureRequest.Builder(cameraRequestMetadatas, false, -1, this.logicalCameraId, null);
                for (Surface surface : frameConfigNative.getSurfaces()) {
                    builder.addTarget(Converter.convert2ASurface(surface));
                }
                arrayList.add(builder.build());
            }
        }
        return (CaptureRequest[]) arrayList.toArray(new CaptureRequest[0]);
    }

    private void setExtraParameters(CameraMetadataNative cameraMetadataNative, FrameConfigNative frameConfigNative) {
        setFlashMode(cameraMetadataNative, frameConfigNative);
        setAfParameters(cameraMetadataNative, frameConfigNative);
        setAeParameters(cameraMetadataNative, frameConfigNative);
        setAwbParameters(cameraMetadataNative, frameConfigNative);
        setZoomParameters(cameraMetadataNative, frameConfigNative);
        setFaceDetectionParameters(cameraMetadataNative, frameConfigNative);
        setImageRotation(cameraMetadataNative, frameConfigNative);
        setLocation(cameraMetadataNative, frameConfigNative);
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0038  */
    /* JADX WARNING: Removed duplicated region for block: B:18:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void setFlashMode(android.hardware.camera2.impl.CameraMetadataNative r5, ohos.media.camera.zidl.FrameConfigNative r6) {
        /*
            r4 = this;
            ohos.media.camera.params.ParameterKey$Key<java.lang.Integer> r4 = ohos.media.camera.params.adapter.InnerParameterKey.FLASH_MODE
            java.lang.Object r4 = r6.get(r4)
            java.lang.Integer r4 = (java.lang.Integer) r4
            if (r4 == 0) goto L_0x0035
            int r6 = r4.intValue()
            r0 = 1
            if (r6 == 0) goto L_0x0030
            r1 = 0
            if (r6 == r0) goto L_0x002b
            r2 = 2
            if (r6 == r2) goto L_0x0030
            r3 = 3
            if (r6 == r3) goto L_0x0026
            ohos.media.utils.log.Logger r6 = ohos.media.camera.device.adapter.FrameConfigMapper.LOGGER
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r0[r1] = r4
            java.lang.String r4 = "Unsupported FLASH_MODE: %{public}d"
            r6.warn(r4, r0)
            goto L_0x0035
        L_0x0026:
            java.lang.Integer r4 = java.lang.Integer.valueOf(r2)
            goto L_0x0036
        L_0x002b:
            java.lang.Integer r4 = java.lang.Integer.valueOf(r1)
            goto L_0x0036
        L_0x0030:
            java.lang.Integer r4 = java.lang.Integer.valueOf(r0)
            goto L_0x0036
        L_0x0035:
            r4 = 0
        L_0x0036:
            if (r4 == 0) goto L_0x003d
            android.hardware.camera2.CaptureRequest$Key r6 = android.hardware.camera2.CaptureRequest.FLASH_MODE
            r5.set(r6, r4)
        L_0x003d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.media.camera.device.adapter.FrameConfigMapper.setFlashMode(android.hardware.camera2.impl.CameraMetadataNative, ohos.media.camera.zidl.FrameConfigNative):void");
    }

    private void setAfParameters(CameraMetadataNative cameraMetadataNative, FrameConfigNative frameConfigNative) {
        setAfMode(cameraMetadataNative, frameConfigNative);
        setAfRegion(cameraMetadataNative, frameConfigNative);
        setAfTrigger(cameraMetadataNative, frameConfigNative);
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x003b  */
    /* JADX WARNING: Removed duplicated region for block: B:16:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void setAfMode(android.hardware.camera2.impl.CameraMetadataNative r3, ohos.media.camera.zidl.FrameConfigNative r4) {
        /*
            r2 = this;
            ohos.media.camera.params.ParameterKey$Key<java.lang.Integer> r2 = ohos.media.camera.params.adapter.InnerParameterKey.AF_MODE
            java.lang.Object r2 = r4.get(r2)
            java.lang.Integer r2 = (java.lang.Integer) r2
            if (r2 == 0) goto L_0x0038
            int r0 = r2.intValue()
            r1 = 1
            if (r0 == r1) goto L_0x0026
            r4 = 2
            if (r0 == r4) goto L_0x0021
            ohos.media.utils.log.Logger r4 = ohos.media.camera.device.adapter.FrameConfigMapper.LOGGER
            java.lang.Object[] r0 = new java.lang.Object[r1]
            r1 = 0
            r0[r1] = r2
            java.lang.String r2 = "Unsupported AF_MODE: %{public}d"
            r4.warn(r2, r0)
            goto L_0x0038
        L_0x0021:
            java.lang.Integer r2 = java.lang.Integer.valueOf(r1)
            goto L_0x0039
        L_0x0026:
            int r2 = r4.getFrameConfigType()
            r4 = 3
            if (r2 != r4) goto L_0x0032
            java.lang.Integer r2 = java.lang.Integer.valueOf(r4)
            goto L_0x0039
        L_0x0032:
            r2 = 4
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            goto L_0x0039
        L_0x0038:
            r2 = 0
        L_0x0039:
            if (r2 == 0) goto L_0x0040
            android.hardware.camera2.CaptureRequest$Key r4 = android.hardware.camera2.CaptureRequest.CONTROL_AF_MODE
            r3.set(r4, r2)
        L_0x0040:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.media.camera.device.adapter.FrameConfigMapper.setAfMode(android.hardware.camera2.impl.CameraMetadataNative, ohos.media.camera.zidl.FrameConfigNative):void");
    }

    private void setAfRegion(CameraMetadataNative cameraMetadataNative, FrameConfigNative frameConfigNative) {
        Size surfaceSize = SurfaceUtils.getSurfaceSize(frameConfigNative.getCoordinateSurface());
        LOGGER.debug("setAfRegion coordinate surface size: %{public}s", surfaceSize);
        MeteringRectangle transformRect = getTransformRect((Rect) frameConfigNative.get(InnerParameterKey.AF_REGION), surfaceSize, get3ACropRegion(frameConfigNative).orElse(null));
        cameraMetadataNative.set(CaptureRequest.CONTROL_AF_REGIONS, new MeteringRectangle[]{transformRect});
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0035  */
    /* JADX WARNING: Removed duplicated region for block: B:16:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void setAfTrigger(android.hardware.camera2.impl.CameraMetadataNative r4, ohos.media.camera.zidl.FrameConfigNative r5) {
        /*
            r3 = this;
            ohos.media.camera.params.ParameterKey$Key<java.lang.Integer> r3 = ohos.media.camera.params.adapter.InnerParameterKey.AF_TRIGGER
            java.lang.Object r3 = r5.get(r3)
            java.lang.Integer r3 = (java.lang.Integer) r3
            if (r3 == 0) goto L_0x0032
            int r5 = r3.intValue()
            r0 = 0
            if (r5 == 0) goto L_0x002d
            r1 = 1
            if (r5 == r1) goto L_0x0028
            r2 = 2
            if (r5 == r2) goto L_0x0023
            ohos.media.utils.log.Logger r5 = ohos.media.camera.device.adapter.FrameConfigMapper.LOGGER
            java.lang.Object[] r1 = new java.lang.Object[r1]
            r1[r0] = r3
            java.lang.String r3 = "Unsupported AF_TRIGGER: %{public}d"
            r5.warn(r3, r1)
            goto L_0x0032
        L_0x0023:
            java.lang.Integer r3 = java.lang.Integer.valueOf(r2)
            goto L_0x0033
        L_0x0028:
            java.lang.Integer r3 = java.lang.Integer.valueOf(r1)
            goto L_0x0033
        L_0x002d:
            java.lang.Integer r3 = java.lang.Integer.valueOf(r0)
            goto L_0x0033
        L_0x0032:
            r3 = 0
        L_0x0033:
            if (r3 == 0) goto L_0x003a
            android.hardware.camera2.CaptureRequest$Key r5 = android.hardware.camera2.CaptureRequest.CONTROL_AF_TRIGGER
            r4.set(r5, r3)
        L_0x003a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.media.camera.device.adapter.FrameConfigMapper.setAfTrigger(android.hardware.camera2.impl.CameraMetadataNative, ohos.media.camera.zidl.FrameConfigNative):void");
    }

    private void setAeParameters(CameraMetadataNative cameraMetadataNative, FrameConfigNative frameConfigNative) {
        setAeMode(cameraMetadataNative, frameConfigNative);
        setAeRegion(cameraMetadataNative, frameConfigNative);
        setAeTrigger(cameraMetadataNative, frameConfigNative);
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0050  */
    /* JADX WARNING: Removed duplicated region for block: B:20:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void setAeMode(android.hardware.camera2.impl.CameraMetadataNative r6, ohos.media.camera.zidl.FrameConfigNative r7) {
        /*
            r5 = this;
            ohos.media.camera.params.ParameterKey$Key<java.lang.Integer> r5 = ohos.media.camera.params.adapter.InnerParameterKey.FLASH_MODE
            java.lang.Object r5 = r7.get(r5)
            java.lang.Integer r5 = (java.lang.Integer) r5
            r0 = 1
            java.lang.Integer r1 = java.lang.Integer.valueOf(r0)
            if (r5 != 0) goto L_0x0010
            r5 = r1
        L_0x0010:
            r2 = 0
            ohos.media.camera.params.ParameterKey$Key<java.lang.Integer> r3 = ohos.media.camera.params.adapter.InnerParameterKey.AE_MODE
            java.lang.Object r7 = r7.get(r3)
            java.lang.Integer r7 = (java.lang.Integer) r7
            if (r7 == 0) goto L_0x004d
            int r3 = r7.intValue()
            r4 = 0
            if (r3 == 0) goto L_0x0048
            if (r3 == r0) goto L_0x0030
            ohos.media.utils.log.Logger r5 = ohos.media.camera.device.adapter.FrameConfigMapper.LOGGER
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r0[r4] = r7
            java.lang.String r7 = "Unsupported AE_MODE: %{public}d"
            r5.warn(r7, r0)
            goto L_0x004d
        L_0x0030:
            int r7 = r5.intValue()
            r0 = 2
            if (r7 != 0) goto L_0x003c
            java.lang.Integer r1 = java.lang.Integer.valueOf(r0)
            goto L_0x004e
        L_0x003c:
            int r5 = r5.intValue()
            if (r5 != r0) goto L_0x004e
            r5 = 3
            java.lang.Integer r1 = java.lang.Integer.valueOf(r5)
            goto L_0x004e
        L_0x0048:
            java.lang.Integer r1 = java.lang.Integer.valueOf(r4)
            goto L_0x004e
        L_0x004d:
            r1 = r2
        L_0x004e:
            if (r1 == 0) goto L_0x0055
            android.hardware.camera2.CaptureRequest$Key r5 = android.hardware.camera2.CaptureRequest.CONTROL_AE_MODE
            r6.set(r5, r1)
        L_0x0055:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.media.camera.device.adapter.FrameConfigMapper.setAeMode(android.hardware.camera2.impl.CameraMetadataNative, ohos.media.camera.zidl.FrameConfigNative):void");
    }

    private void setAeRegion(CameraMetadataNative cameraMetadataNative, FrameConfigNative frameConfigNative) {
        Size surfaceSize = SurfaceUtils.getSurfaceSize(frameConfigNative.getCoordinateSurface());
        LOGGER.debug("setAeRegion coordinate surface size: %{public}s", surfaceSize);
        MeteringRectangle transformRect = getTransformRect((Rect) frameConfigNative.get(InnerParameterKey.AE_REGION), surfaceSize, get3ACropRegion(frameConfigNative).orElse(null));
        cameraMetadataNative.set(CaptureRequest.CONTROL_AE_REGIONS, new MeteringRectangle[]{transformRect});
    }

    private void setAeTrigger(CameraMetadataNative cameraMetadataNative, FrameConfigNative frameConfigNative) {
        Integer convertAeTrigger;
        Integer num = (Integer) frameConfigNative.get(InnerParameterKey.AE_TRIGGER);
        if (num != null && (convertAeTrigger = getConvertAeTrigger(num.intValue())) != null) {
            cameraMetadataNative.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, convertAeTrigger);
        }
    }

    private Integer getConvertAeTrigger(int i) {
        if (i == 0) {
            return 0;
        }
        if (i == 1) {
            return 1;
        }
        if (i == 2) {
            return 2;
        }
        LOGGER.warn("Unsupported AE_TRIGGER: %{public}d", Integer.valueOf(i));
        return null;
    }

    private void setAwbParameters(CameraMetadataNative cameraMetadataNative, FrameConfigNative frameConfigNative) {
        setAwbMode(cameraMetadataNative, frameConfigNative);
        setAwbRegion(cameraMetadataNative, frameConfigNative);
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x002d  */
    /* JADX WARNING: Removed duplicated region for block: B:13:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void setAwbMode(android.hardware.camera2.impl.CameraMetadataNative r3, ohos.media.camera.zidl.FrameConfigNative r4) {
        /*
            r2 = this;
            ohos.media.camera.params.ParameterKey$Key<java.lang.Integer> r2 = ohos.media.camera.params.adapter.InnerParameterKey.AWB_MODE
            java.lang.Object r2 = r4.get(r2)
            java.lang.Integer r2 = (java.lang.Integer) r2
            if (r2 == 0) goto L_0x002a
            int r4 = r2.intValue()
            r0 = 0
            if (r4 == 0) goto L_0x0025
            r1 = 1
            if (r4 == r1) goto L_0x0020
            ohos.media.utils.log.Logger r4 = ohos.media.camera.device.adapter.FrameConfigMapper.LOGGER
            java.lang.Object[] r1 = new java.lang.Object[r1]
            r1[r0] = r2
            java.lang.String r2 = "Unsupported AWB_MODE: %{public}d"
            r4.warn(r2, r1)
            goto L_0x002a
        L_0x0020:
            java.lang.Integer r2 = java.lang.Integer.valueOf(r1)
            goto L_0x002b
        L_0x0025:
            java.lang.Integer r2 = java.lang.Integer.valueOf(r0)
            goto L_0x002b
        L_0x002a:
            r2 = 0
        L_0x002b:
            if (r2 == 0) goto L_0x0032
            android.hardware.camera2.CaptureRequest$Key r4 = android.hardware.camera2.CaptureRequest.CONTROL_AWB_MODE
            r3.set(r4, r2)
        L_0x0032:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.media.camera.device.adapter.FrameConfigMapper.setAwbMode(android.hardware.camera2.impl.CameraMetadataNative, ohos.media.camera.zidl.FrameConfigNative):void");
    }

    private void setAwbRegion(CameraMetadataNative cameraMetadataNative, FrameConfigNative frameConfigNative) {
        Size surfaceSize = SurfaceUtils.getSurfaceSize(frameConfigNative.getCoordinateSurface());
        LOGGER.debug("setAeRegion coordinate surface size: %{public}s", surfaceSize);
        MeteringRectangle transformRect = getTransformRect((Rect) frameConfigNative.get(InnerParameterKey.AWB_REGION), surfaceSize, get3ACropRegion(frameConfigNative).orElse(null));
        cameraMetadataNative.set(CaptureRequest.CONTROL_AWB_REGIONS, new MeteringRectangle[]{transformRect});
    }

    private Optional<Rect> get3ACropRegion(FrameConfigNative frameConfigNative) {
        Optional<Rect> empty = Optional.empty();
        if (this.maxZoomValue == null) {
            return empty;
        }
        Float f = (Float) frameConfigNative.get(InnerParameterKey.ZOOM_RATIO);
        if (f == null) {
            f = Float.valueOf(1.0f);
        }
        return CameraCoordinateUtil.getCropRegion(this.sensorArray, this.maxZoomValue.floatValue(), f.floatValue());
    }

    private void setFaceDetectionParameters(CameraMetadataNative cameraMetadataNative, FrameConfigNative frameConfigNative) {
        byte b;
        Integer num = (Integer) frameConfigNative.get(InnerParameterKey.FACE_DETECTION_TYPE);
        if (num == null) {
            LOGGER.debug("setFaceDetectionParameters failed for null FACE_DETECTION_TYPE key value", new Object[0]);
            return;
        }
        int i = 2;
        if ((num.intValue() & 2) != 0) {
            b = 1;
        } else if ((num.intValue() & 1) != 0) {
            b = 0;
        } else {
            b = 0;
            i = 0;
        }
        cameraMetadataNative.set(CaptureRequest.STATISTICS_FACE_DETECT_MODE, Integer.valueOf(i));
        try {
            cameraMetadataNative.set(CaptureRequestEx.HUAWEI_SMILE_DETECTION, Byte.valueOf(b));
        } catch (IllegalArgumentException e) {
            LOGGER.debug("Failed to set HUAWEI_SMILE_DETECTION to metadata, exception: %{public}s", e.getMessage());
        }
    }

    private void setZoomParameters(CameraMetadataNative cameraMetadataNative, FrameConfigNative frameConfigNative) {
        Optional<Rect> empty = Optional.empty();
        if (this.maxZoomValue != null) {
            Float f = (Float) frameConfigNative.get(InnerParameterKey.ZOOM_RATIO);
            if (f == null) {
                LOGGER.debug("setZoomParameters failed for null ZOOM_RATIO key value", new Object[0]);
                return;
            }
            empty = CameraCoordinateUtil.getCropRegion(this.sensorArray, this.maxZoomValue.floatValue(), f.floatValue());
        }
        if (!empty.isPresent()) {
            LOGGER.warn("setZoomParameters failed, get cropRegion is null", new Object[0]);
            return;
        }
        Rect rect = empty.get();
        LOGGER.debug("setZoomParameters cropRegion is %{public}s.", rect.toString());
        cameraMetadataNative.set(CaptureRequest.SCALER_CROP_REGION, Converter.convert2ARect(rect));
    }

    private void setImageRotation(CameraMetadataNative cameraMetadataNative, FrameConfigNative frameConfigNative) {
        Integer num = (Integer) frameConfigNative.get(InnerParameterKey.IMAGE_ROTATION);
        if (num != null) {
            cameraMetadataNative.set(CaptureRequest.JPEG_ORIENTATION, num);
        }
    }

    private void setLocation(CameraMetadataNative cameraMetadataNative, FrameConfigNative frameConfigNative) {
        Location location = (Location) frameConfigNative.get(InnerParameterKey.LOCATION);
        if (location != null) {
            cameraMetadataNative.set(CaptureRequest.JPEG_GPS_LOCATION, Converter.convert2ALocation(location));
        }
    }

    private MeteringRectangle getTransformRect(Rect rect, Size size, Rect rect2) {
        if (rect != null) {
            return Converter.convert2MeteringRectangles(CameraCoordinateUtil.screenToDriver(rect, size, rect2, this.sensorArray));
        }
        LOGGER.debug("getTransformRects rect is null, return default region", new Object[0]);
        return Converter.convert2MeteringRectangles(null);
    }
}
