package ohos.agp.render.render3d.impl;

import ohos.agp.render.render3d.components.CameraComponent;
import ohos.agp.render.render3d.components.LightComponent;
import ohos.agp.render.render3d.components.SceneComponent;
import ohos.agp.render.render3d.math.Quaternion;
import ohos.agp.render.render3d.math.Vector2;
import ohos.agp.render.render3d.math.Vector3;
import ohos.agp.render.render3d.math.Vector4;

/* access modifiers changed from: package-private */
public class Swig {
    private Swig() {
    }

    static Vector2 get(CoreVec2 coreVec2) {
        if (coreVec2 != null) {
            return new Vector2(coreVec2.getX(), coreVec2.getY());
        }
        return Vector2.ZERO;
    }

    static Vector3 get(CoreVec3 coreVec3) {
        if (coreVec3 != null) {
            return new Vector3(coreVec3.getX(), coreVec3.getY(), coreVec3.getZ());
        }
        return Vector3.ZERO;
    }

    static Vector4 get(CoreVec4 coreVec4) {
        if (coreVec4 != null) {
            return new Vector4(coreVec4.getX(), coreVec4.getY(), coreVec4.getZ(), coreVec4.getW());
        }
        return Vector4.ZERO;
    }

    static Quaternion get(CoreQuat coreQuat) {
        if (coreQuat != null) {
            return new Quaternion(coreQuat.getX(), coreQuat.getY(), coreQuat.getZ(), coreQuat.getW());
        }
        return Quaternion.ZERO;
    }

    static CoreVec2 set(Vector2 vector2) {
        return new CoreVec2(vector2.getX(), vector2.getY());
    }

    static CoreVec3 set(Vector3 vector3) {
        return new CoreVec3(vector3.getX(), vector3.getY(), vector3.getZ());
    }

    static CoreVec4 set(Vector4 vector4) {
        return new CoreVec4(vector4.getX(), vector4.getY(), vector4.getZ(), vector4.getW());
    }

    static CoreQuat set(Quaternion quaternion) {
        return new CoreQuat(quaternion.getX(), quaternion.getY(), quaternion.getZ(), quaternion.getW());
    }

    static short getNativeCameraType(CameraComponent.CameraType cameraType) {
        int swigValue;
        int i = AnonymousClass1.$SwitchMap$ohos$agp$render$render3d$components$CameraComponent$CameraType[cameraType.ordinal()];
        if (i == 1) {
            swigValue = CoreCameraType.CORE_CAMERA_TYPE_ORTHOGRAPHIC.swigValue();
        } else if (i == 2) {
            swigValue = CoreCameraType.CORE_CAMERA_TYPE_PERSPECTIVE.swigValue();
        } else if (i == 3) {
            swigValue = CoreCameraType.CORE_CAMERA_TYPE_CUSTOM.swigValue();
        } else {
            throw new IllegalArgumentException();
        }
        return (short) swigValue;
    }

    static CameraComponent.CameraType getCameraType(short s) {
        int i = AnonymousClass1.$SwitchMap$ohos$agp$render$render3d$impl$CoreCameraType[CoreCameraType.swigToEnum(s).ordinal()];
        if (i == 1) {
            return CameraComponent.CameraType.ORTHOGRAPHIC;
        }
        if (i == 2) {
            return CameraComponent.CameraType.PERSPECTIVE;
        }
        if (i == 3) {
            return CameraComponent.CameraType.CUSTOM;
        }
        throw new IllegalArgumentException();
    }

    static short getNativeLightType(LightComponent.LightType lightType) {
        int swigValue;
        int i = AnonymousClass1.$SwitchMap$ohos$agp$render$render3d$components$LightComponent$LightType[lightType.ordinal()];
        if (i == 1) {
            swigValue = CoreLightType.CORE_LIGHT_TYPE_INVALID.swigValue();
        } else if (i == 2) {
            swigValue = CoreLightType.CORE_LIGHT_TYPE_DIRECTIONAL.swigValue();
        } else if (i == 3) {
            swigValue = CoreLightType.CORE_LIGHT_TYPE_POINT.swigValue();
        } else if (i == 4) {
            swigValue = CoreLightType.CORE_LIGHT_TYPE_SPOT.swigValue();
        } else {
            throw new IllegalArgumentException();
        }
        return (short) swigValue;
    }

    static LightComponent.LightType getLightType(short s) {
        int i = AnonymousClass1.$SwitchMap$ohos$agp$render$render3d$impl$CoreLightType[CoreLightType.swigToEnum(s).ordinal()];
        if (i == 1) {
            return LightComponent.LightType.INVALID;
        }
        if (i == 2) {
            return LightComponent.LightType.DIRECTIONAL;
        }
        if (i == 3) {
            return LightComponent.LightType.POINT;
        }
        if (i == 4) {
            return LightComponent.LightType.SPOT;
        }
        throw new IllegalArgumentException();
    }

    static short getNativeEnvBgType(SceneComponent.BackgroundType backgroundType) {
        int swigValue;
        int i = AnonymousClass1.$SwitchMap$ohos$agp$render$render3d$components$SceneComponent$BackgroundType[backgroundType.ordinal()];
        if (i == 1) {
            swigValue = CoreEnvironmentBackgroundType.CORE_ENV_BG_NONE.swigValue();
        } else if (i == 2) {
            swigValue = CoreEnvironmentBackgroundType.CORE_ENV_BG_IMAGE.swigValue();
        } else if (i == 3) {
            swigValue = CoreEnvironmentBackgroundType.CORE_ENV_BG_CUBEMAP.swigValue();
        } else if (i == 4) {
            swigValue = CoreEnvironmentBackgroundType.CORE_ENV_BG_EQUIRECTANGULAR.swigValue();
        } else {
            throw new IllegalArgumentException();
        }
        return (short) swigValue;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.agp.render.render3d.impl.Swig$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$agp$render$render3d$components$CameraComponent$CameraType = new int[CameraComponent.CameraType.values().length];
        static final /* synthetic */ int[] $SwitchMap$ohos$agp$render$render3d$components$LightComponent$LightType = new int[LightComponent.LightType.values().length];
        static final /* synthetic */ int[] $SwitchMap$ohos$agp$render$render3d$components$SceneComponent$BackgroundType = new int[SceneComponent.BackgroundType.values().length];
        static final /* synthetic */ int[] $SwitchMap$ohos$agp$render$render3d$impl$CoreCameraType = new int[CoreCameraType.values().length];
        static final /* synthetic */ int[] $SwitchMap$ohos$agp$render$render3d$impl$CoreEnvironmentBackgroundType = new int[CoreEnvironmentBackgroundType.values().length];
        static final /* synthetic */ int[] $SwitchMap$ohos$agp$render$render3d$impl$CoreLightType = new int[CoreLightType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(47:0|(2:1|2)|3|(2:5|6)|7|(2:9|10)|11|(2:13|14)|15|17|18|19|20|21|22|23|24|25|27|28|29|30|31|32|(2:33|34)|35|37|38|39|40|41|42|43|44|45|47|48|49|50|51|52|53|55|56|57|58|(3:59|60|62)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(48:0|(2:1|2)|3|5|6|7|(2:9|10)|11|(2:13|14)|15|17|18|19|20|21|22|23|24|25|27|28|29|30|31|32|(2:33|34)|35|37|38|39|40|41|42|43|44|45|47|48|49|50|51|52|53|55|56|57|58|(3:59|60|62)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(53:0|1|2|3|5|6|7|(2:9|10)|11|13|14|15|17|18|19|20|21|22|23|24|25|27|28|29|30|31|32|33|34|35|37|38|39|40|41|42|43|44|45|47|48|49|50|51|52|53|55|56|57|58|59|60|62) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0048 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0052 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:23:0x005c */
        /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x0079 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:31:0x0083 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x008d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:39:0x00aa */
        /* JADX WARNING: Missing exception handler attribute for start block: B:41:0x00b4 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:43:0x00be */
        /* JADX WARNING: Missing exception handler attribute for start block: B:49:0x00db */
        /* JADX WARNING: Missing exception handler attribute for start block: B:51:0x00e5 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:57:0x0102 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:59:0x010c */
        static {
            /*
            // Method dump skipped, instructions count: 279
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.agp.render.render3d.impl.Swig.AnonymousClass1.<clinit>():void");
        }
    }

    static SceneComponent.BackgroundType getEnvBgType(short s) {
        int i = AnonymousClass1.$SwitchMap$ohos$agp$render$render3d$impl$CoreEnvironmentBackgroundType[CoreEnvironmentBackgroundType.swigToEnum(s).ordinal()];
        if (i == 1) {
            return SceneComponent.BackgroundType.NONE;
        }
        if (i == 2) {
            return SceneComponent.BackgroundType.IMAGE;
        }
        if (i == 3) {
            return SceneComponent.BackgroundType.CUBEMAP;
        }
        if (i == 4) {
            return SceneComponent.BackgroundType.EQUIRECTANGULAR;
        }
        throw new IllegalArgumentException();
    }
}
