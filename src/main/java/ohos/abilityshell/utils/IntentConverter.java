package ohos.abilityshell.utils;

import android.app.RemoteInput;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.ArrayMap;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import ohos.aafwk.content.IntentParams;
import ohos.appexecfwk.utils.AppLog;
import ohos.bundle.AbilityInfo;
import ohos.bundle.ElementName;
import ohos.bundle.ShellInfo;
import ohos.hiviewdfx.HiLogLabel;
import ohos.net.UriConverter;
import ohos.utils.PacMap;
import ohos.utils.Sequenceable;
import ohos.utils.adapter.IntentConstantMapper;
import ohos.utils.adapter.PacMapUtils;
import ohos.utils.geo.Rect;

public class IntentConverter {
    private static final String MAGIC_KEY_RAW_PARAMS = "harmony_aafwk_raw_params";
    private static final String NOTIFICATION_USER_INPUT = "harmony_notification_user_input";
    private static final String SCHEME_CONTENT = "content";
    private static final String SCHEME_HARMONY = "dataability";
    private static final HiLogLabel SHELL_LABEL = new HiLogLabel(3, 218108160, "AbilityShell");

    private IntentConverter() {
    }

    public static Optional<Intent> createAndroidIntent(ohos.aafwk.content.Intent intent, ShellInfo shellInfo) {
        Uri uri;
        if (intent == null) {
            return Optional.empty();
        }
        Intent intent2 = new Intent();
        String action = intent.getAction();
        if (action != null) {
            intent2.setAction(IntentConstantMapper.convertToAndroidAction(action).orElse(action));
        }
        intent2.setFlags(intent.getFlags());
        Set<String> entities = intent.getEntities();
        if (entities != null) {
            for (String str : entities) {
                intent2.addCategory(IntentConstantMapper.convertToAndroidEntity(str).orElse(str));
            }
        }
        createComponentName(intent.getElement(), shellInfo).ifPresent(new Consumer(intent2) {
            /* class ohos.abilityshell.utils.$$Lambda$IntentConverter$X8sIYmThMT5HsYXdajrc7kuVZqM */
            private final /* synthetic */ Intent f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Intent unused = this.f$0.setComponent((ComponentName) obj);
            }
        });
        IntentParams params = intent.getParams();
        if (params != null) {
            convertIntentParamsToBundle(params).ifPresent(new Consumer(intent2) {
                /* class ohos.abilityshell.utils.$$Lambda$IntentConverter$ScLLNJBrvmciSb9gCBUWuhz1m58 */
                private final /* synthetic */ Intent f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    Intent unused = this.f$0.putExtras((Bundle) obj);
                }
            });
        }
        String androidPackageName = getAndroidPackageName(intent, shellInfo);
        if (androidPackageName != null) {
            intent2.setPackage(androidPackageName);
        }
        ohos.utils.net.Uri uri2 = intent.getUri();
        if (uri2 != null) {
            uri = !SCHEME_HARMONY.equals(uri2.getScheme()) ? UriConverter.convertToAndroidUri(uri2) : UriConverter.convertToAndroidContentUri(uri2);
        } else {
            uri = null;
        }
        String stringParam = (params == null || !params.isUnpacked()) ? null : intent.getStringParam("mime-type");
        if (uri != null && stringParam != null) {
            intent2.setDataAndType(uri, stringParam);
        } else if (uri != null) {
            intent2.setData(uri);
        } else if (stringParam != null) {
            intent2.setType(stringParam);
        } else {
            AppLog.d(SHELL_LABEL, "IntentConverter::createAndroidIntent uri and mime-type are null", new Object[0]);
        }
        ohos.aafwk.content.Intent picker = intent.getPicker();
        if (picker != null) {
            intent2.setSelector(createAndroidIntent(picker, null).orElse(null));
        }
        Rect bordersRect = intent.getBordersRect();
        if (bordersRect != null) {
            intent2.setSourceBounds(convertBordersRectToRect(bordersRect).orElse(null));
        }
        return Optional.of(intent2);
    }

    private static String getAndroidPackageName(ohos.aafwk.content.Intent intent, ShellInfo shellInfo) {
        if (shellInfo == null || shellInfo.getPackageName() == null || shellInfo.getPackageName().isEmpty()) {
            return intent.getBundle();
        }
        return shellInfo.getPackageName();
    }

    public static Optional<ohos.aafwk.content.Intent> createZidaneIntent(Intent intent, AbilityInfo abilityInfo) {
        ohos.utils.net.Uri uri;
        if (intent == null) {
            return Optional.empty();
        }
        ohos.aafwk.content.Intent intent2 = new ohos.aafwk.content.Intent();
        String action = intent.getAction();
        if (action != null) {
            intent2.setAction(IntentConstantMapper.convertToZidaneAction(action).orElse(action));
        }
        intent2.setFlags(intent.getFlags());
        Set<String> categories = intent.getCategories();
        if (categories != null) {
            for (String str : categories) {
                intent2.addEntity(IntentConstantMapper.convertToZidaneEntity(str).orElse(str));
            }
        }
        createElementName(intent.getComponent(), abilityInfo).ifPresent(new Consumer() {
            /* class ohos.abilityshell.utils.$$Lambda$FG1GMK_2J97cptU0pyS04JgYyow */

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ohos.aafwk.content.Intent.this.setElement((ElementName) obj);
            }
        });
        Bundle extras = intent.getExtras();
        Optional<IntentParams> convertBundleToIntentParams = convertBundleToIntentParams(extras);
        if (convertBundleToIntentParams.isPresent()) {
            if (!(extras == null || extras.getClassLoader() == null)) {
                convertBundleToIntentParams.get().setClassLoader(extras.getClassLoader());
            }
            intent2.setParams(convertBundleToIntentParams.get());
        }
        String str2 = intent.getPackage();
        if (str2 != null) {
            intent2.setBundle(str2);
        }
        Uri data = intent.getData();
        if (data != null) {
            uri = !"content".equals(data.getScheme()) ? UriConverter.convertToZidaneUri(data) : UriConverter.convertToZidaneContentUri(data, "");
        } else {
            uri = null;
        }
        intent2.setUri(uri);
        String type = intent.getType();
        if (type != null) {
            intent2.setParam("mime-type", type);
        }
        Intent selector = intent.getSelector();
        if (selector != null) {
            intent2.setPicker(createZidaneIntent(selector, null).orElse(null));
        }
        Bundle resultsFromIntent = RemoteInput.getResultsFromIntent(intent);
        if (resultsFromIntent != null) {
            intent2.setParam(NOTIFICATION_USER_INPUT, PacMapUtils.convertFromBundle(resultsFromIntent));
        }
        android.graphics.Rect sourceBounds = intent.getSourceBounds();
        if (sourceBounds != null) {
            intent2.setBordersRect(convertRectToBordersRect(sourceBounds).orElse(null));
        }
        return Optional.of(intent2);
    }

    public static Optional<ElementName> createElementName(ComponentName componentName, AbilityInfo abilityInfo) {
        String str;
        String str2;
        if (componentName == null && abilityInfo == null) {
            AppLog.e(SHELL_LABEL, "IntentConverter::createElementName param invalid", new Object[0]);
            return Optional.empty();
        }
        if (abilityInfo != null) {
            str2 = abilityInfo.getBundleName();
            str = abilityInfo.getClassName();
        } else {
            String packageName = componentName.getPackageName();
            str = componentName.getClassName();
            str2 = packageName;
        }
        if (str2 == null || str == null) {
            return Optional.empty();
        }
        return Optional.of(new ElementName("", str2, str));
    }

    public static Optional<ComponentName> createComponentName(ElementName elementName, ShellInfo shellInfo) {
        String str;
        String str2;
        if (elementName == null && shellInfo == null) {
            AppLog.e(SHELL_LABEL, "IntentConverter::createComponentName param invalid", new Object[0]);
            return Optional.empty();
        }
        if (shellInfo != null) {
            str2 = shellInfo.getPackageName();
            str = shellInfo.getName();
        } else {
            String bundleName = elementName.getBundleName();
            str = elementName.getAbilityName();
            str2 = bundleName;
        }
        if (str2 == null || str == null) {
            return Optional.empty();
        }
        return Optional.of(new ComponentName(str2, str));
    }

    private static Optional<Bundle> convertIntentParamsToBundle(IntentParams intentParams) {
        if (intentParams.isUnpacked()) {
            return convertIntentParamsToBundle(intentParams.getParams());
        }
        return convertIntentParamsToBundle(intentParams.getRawParams());
    }

    private static Optional<Bundle> convertIntentParamsToBundle(byte[] bArr) {
        AppLog.d(SHELL_LABEL, "IntentConverter::convertIntentParamsToBundle raw params", new Object[0]);
        if (bArr == null) {
            return Optional.empty();
        }
        Bundle bundle = new Bundle();
        bundle.putByteArray(MAGIC_KEY_RAW_PARAMS, bArr);
        return Optional.of(bundle);
    }

    private static Optional<Bundle> convertIntentParamsToBundle(Map<String, Object> map) {
        Uri uri;
        if (map == null) {
            return Optional.empty();
        }
        Bundle bundle = new Bundle();
        ArrayMap<String, Object> reflectGetInnerMap = reflectGetInnerMap(bundle);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value != null) {
                if (value instanceof Boolean) {
                    bundle.putBoolean(key, ((Boolean) value).booleanValue());
                } else if (value instanceof Byte) {
                    bundle.putByte(key, ((Byte) value).byteValue());
                } else if (value instanceof Character) {
                    bundle.putChar(key, ((Character) value).charValue());
                } else if (value instanceof Short) {
                    bundle.putShort(key, ((Short) value).shortValue());
                } else if (value instanceof Integer) {
                    bundle.putInt(key, ((Integer) value).intValue());
                } else if (value instanceof Long) {
                    bundle.putLong(key, ((Long) value).longValue());
                } else if (value instanceof Float) {
                    bundle.putFloat(key, ((Float) value).floatValue());
                } else if (value instanceof Double) {
                    bundle.putDouble(key, ((Double) value).doubleValue());
                } else if (value instanceof String) {
                    bundle.putString(key, (String) value);
                } else if (value instanceof CharSequence) {
                    bundle.putCharSequence(key, (CharSequence) value);
                } else if (value instanceof boolean[]) {
                    bundle.putBooleanArray(key, (boolean[]) value);
                } else if (value instanceof byte[]) {
                    bundle.putByteArray(key, (byte[]) value);
                } else if (value instanceof char[]) {
                    bundle.putCharArray(key, (char[]) value);
                } else if (value instanceof short[]) {
                    bundle.putShortArray(key, (short[]) value);
                } else if (value instanceof int[]) {
                    bundle.putIntArray(key, (int[]) value);
                } else if (value instanceof long[]) {
                    bundle.putLongArray(key, (long[]) value);
                } else if (value instanceof float[]) {
                    bundle.putFloatArray(key, (float[]) value);
                } else if (value instanceof double[]) {
                    bundle.putDoubleArray(key, (double[]) value);
                } else if (value instanceof String[]) {
                    bundle.putStringArray(key, (String[]) value);
                } else if (value instanceof IntentParams) {
                    bundle.putBundle(key, convertIntentParamsToBundle(((IntentParams) value).getParams()).orElse(null));
                } else if (value instanceof Sequenceable) {
                    if (value instanceof ohos.utils.net.Uri) {
                        ohos.utils.net.Uri uri2 = (ohos.utils.net.Uri) value;
                        if (!SCHEME_HARMONY.equals(uri2.getScheme())) {
                            uri = UriConverter.convertToAndroidUri(uri2);
                        } else {
                            uri = UriConverter.convertToAndroidContentUri(uri2);
                        }
                        bundle.putParcelable(key, uri);
                    } else {
                        bundle.putParcelable(key, new SequenceableWrapper((Sequenceable) value));
                    }
                } else if (value instanceof Sequenceable[]) {
                    bundle.putParcelableArray(key, SequenceableWrapper.wrapArray((Sequenceable[]) value));
                } else if ((value instanceof List) && reflectGetInnerMap != null) {
                    wrapSequenceableForList((List) value);
                    reflectGetInnerMap.put(key, value);
                } else if (value instanceof Serializable) {
                    bundle.putSerializable(key, (Serializable) value);
                } else {
                    AppLog.w(SHELL_LABEL, "IntentConverter::convertIntentParamsToBundle unknown type %{public}s", value.getClass().getName());
                }
            }
        }
        return Optional.of(bundle);
    }

    private static Optional<IntentParams> convertBundleToIntentParams(byte[] bArr) {
        AppLog.d(SHELL_LABEL, "IntentConverter::convertBundleToIntentParams raw params", new Object[0]);
        IntentParams intentParams = new IntentParams();
        intentParams.setRawParams(bArr);
        return Optional.of(intentParams);
    }

    private static Optional<IntentParams> convertBundleToIntentParams(Bundle bundle) {
        ohos.utils.net.Uri uri;
        if (bundle == null) {
            return Optional.empty();
        }
        if (bundle.containsKey(MAGIC_KEY_RAW_PARAMS)) {
            return convertBundleToIntentParams(bundle.getByteArray(MAGIC_KEY_RAW_PARAMS));
        }
        IntentParams intentParams = new IntentParams();
        Set<String> keySet = bundle.keySet();
        if (keySet != null) {
            for (String str : keySet) {
                Object obj = bundle.get(str);
                if (obj == null || (obj instanceof CharSequence)) {
                    intentParams.setParam(str, obj);
                } else if (obj instanceof Bundle) {
                    intentParams.setParam(str, convertBundleToIntentParams((Bundle) obj).orElse(null));
                } else if (obj instanceof SequenceableWrapper) {
                    intentParams.setParam(str, ((SequenceableWrapper) obj).getWrappedSequenceable(bundle.getClassLoader()));
                } else if (obj instanceof Parcelable[]) {
                    intentParams.setParam(str, SequenceableWrapper.unwrapArray((Parcelable[]) obj, bundle.getClassLoader()));
                } else if (obj instanceof List) {
                    unwrapSequenceableForList((List) obj, bundle.getClassLoader());
                    intentParams.setParam(str, obj);
                } else if (obj instanceof Serializable) {
                    intentParams.setParam(str, obj);
                } else if (obj instanceof Uri) {
                    Uri uri2 = (Uri) obj;
                    if (!"content".equals(uri2.getScheme())) {
                        uri = UriConverter.convertToZidaneUri(uri2);
                    } else {
                        uri = UriConverter.convertToZidaneContentUri(uri2, "");
                    }
                    intentParams.setParam(str, uri);
                } else {
                    AppLog.w(SHELL_LABEL, "IntentConverter::createZidaneIntent unknown type %{public}s", obj.getClass().getName());
                }
            }
        }
        return Optional.of(intentParams);
    }

    private static void wrapSequenceableForList(List list) {
        for (int i = 0; i < list.size(); i++) {
            Object obj = list.get(i);
            if (obj != null && (obj instanceof Sequenceable)) {
                list.set(i, new SequenceableWrapper((Sequenceable) obj));
            }
        }
    }

    private static void unwrapSequenceableForList(List list, ClassLoader classLoader) {
        for (int i = 0; i < list.size(); i++) {
            Object obj = list.get(i);
            if (obj != null && (obj instanceof SequenceableWrapper)) {
                list.set(i, ((SequenceableWrapper) obj).getWrappedSequenceable(classLoader));
            }
        }
    }

    private static ArrayMap<String, Object> reflectGetInnerMap(Bundle bundle) {
        try {
            Method declaredMethod = Bundle.class.getSuperclass().getDeclaredMethod("getMap", new Class[0]);
            declaredMethod.setAccessible(true);
            return (ArrayMap) declaredMethod.invoke(bundle, new Object[0]);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            AppLog.e(SHELL_LABEL, "IntentConverter::reflectGetInnerMap fail: %{public}s", e.getMessage());
            return null;
        }
    }

    private static Optional<IntentParams> convertPacMapToIntentParams(PacMap pacMap) {
        AppLog.d(SHELL_LABEL, "IntentConverter::convertPacMapToIntentParams", new Object[0]);
        if (pacMap == null) {
            return Optional.empty();
        }
        Map<String, Object> all = pacMap.getAll();
        if (all == null) {
            return Optional.empty();
        }
        IntentParams intentParams = new IntentParams();
        for (Map.Entry<String, Object> entry : all.entrySet()) {
            intentParams.setParam(entry.getKey(), entry.getValue());
        }
        return Optional.of(intentParams);
    }

    public static Optional<Bundle> convertPacMapToBundle(PacMap pacMap) {
        AppLog.d(SHELL_LABEL, "IntentConverter::convertPacMapToBundle", new Object[0]);
        if (pacMap == null) {
            return Optional.empty();
        }
        Optional<IntentParams> convertPacMapToIntentParams = convertPacMapToIntentParams(pacMap);
        if (convertPacMapToIntentParams.isPresent()) {
            return convertIntentParamsToBundle(convertPacMapToIntentParams.get());
        }
        return Optional.empty();
    }

    private static Optional<PacMap> convertIntentParamsToPacMap(IntentParams intentParams) {
        AppLog.d(SHELL_LABEL, "IntentConverter::convertIntentParamsToPacMap", new Object[0]);
        if (intentParams == null) {
            return Optional.empty();
        }
        Map<String, Object> params = intentParams.getParams();
        if (params == null) {
            return Optional.empty();
        }
        PacMap pacMap = new PacMap(params.size());
        pacMap.putAll(params);
        return Optional.of(pacMap);
    }

    public static Optional<PacMap> convertBundleToPacMap(Bundle bundle) {
        AppLog.d(SHELL_LABEL, "IntentConverter::convertBundleToPacMap", new Object[0]);
        if (bundle == null) {
            return Optional.empty();
        }
        Optional<IntentParams> convertBundleToIntentParams = convertBundleToIntentParams(bundle);
        if (convertBundleToIntentParams.isPresent()) {
            return convertIntentParamsToPacMap(convertBundleToIntentParams.get());
        }
        return Optional.empty();
    }

    public static Optional<android.graphics.Rect> convertBordersRectToRect(Rect rect) {
        if (rect != null) {
            return Optional.of(new android.graphics.Rect(rect.left, rect.top, rect.right, rect.bottom));
        }
        AppLog.e(SHELL_LABEL, "IntentConverter::convertBordersRectToRect param invalid", new Object[0]);
        return Optional.empty();
    }

    public static Optional<Rect> convertRectToBordersRect(android.graphics.Rect rect) {
        if (rect != null) {
            return Optional.of(new Rect(rect.left, rect.top, rect.right, rect.bottom));
        }
        AppLog.e(SHELL_LABEL, "IntentConverter::convertRectToBordersRect param invalid", new Object[0]);
        return Optional.empty();
    }
}
