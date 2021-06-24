package ohos.security.permission.utils;

import java.util.Optional;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.security.permission.infrastructure.utils.HiLogLabelUtil;
import ohos.utils.fastjson.JSON;
import ohos.utils.fastjson.JSONException;
import ohos.utils.fastjson.TypeReference;
import ohos.utils.fastjson.parser.Feature;

@Deprecated
public final class JsonUtil {
    private static final HiLogLabel LABEL = HiLogLabelUtil.INNER_KIT.newHiLogLabel(TAG);
    private static final String TAG = "JsonUtil";

    private JsonUtil() {
    }

    public static <T> Optional<T> fromJson(String str, Class<T> cls) {
        try {
            return Optional.ofNullable(JSON.parseObject(str, cls));
        } catch (JSONException unused) {
            HiLog.error(LABEL, "fromJson: exception: cannot transform json to %{public}s", cls.getName());
            return Optional.empty();
        }
    }

    public static <T> Optional<T> fromJson(String str, TypeReference<T> typeReference) {
        try {
            return Optional.ofNullable(JSON.parseObject(str, typeReference, new Feature[0]));
        } catch (JSONException unused) {
            HiLog.error(LABEL, "fromJson: exception: cannot transform json to %{public}s", typeReference.getType().getTypeName());
            return Optional.empty();
        }
    }

    public static <T> String toJson(T t) {
        try {
            return JSON.toJSONString(t);
        } catch (JSONException unused) {
            HiLog.error(LABEL, "toJson: exception: cannot convert object to json string", new Object[0]);
            return "";
        }
    }
}
