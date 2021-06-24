package ohos.miscservices.adapter.utils;

import android.app.KeyguardManager;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import java.util.Optional;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class AdaptUtil {
    private static final HiLogLabel TAG = new HiLogLabel(3, 218110976, "AdaptUtil");
    private static Context sReservedAndroidContext;
    private static volatile ohos.app.Context sReservedContext;
    private static InputMethodManager sReservedImm;

    private AdaptUtil() {
    }

    public static Optional<InputMethodManager> getInputMethodManager() {
        if (isContextChanged() || sReservedImm == null) {
            HiLog.debug(TAG, "Reversed imm is null or the ability context is changed, create a new Imm instance", new Object[0]);
            if (sReservedContext == null) {
                HiLog.error(TAG, "getInputMethodManager failed, current ability context is null.", new Object[0]);
                return Optional.empty();
            }
            Object hostContext = sReservedContext.getHostContext();
            if (!(hostContext instanceof Context)) {
                HiLog.error(TAG, "getInputMethodManager failed, getHostContext is not the instance of Context.", new Object[0]);
                return Optional.empty();
            }
            sReservedImm = InputMethodManager.forContext((Context) hostContext);
            return Optional.of(sReservedImm);
        }
        HiLog.debug(TAG, "The ability context is not changed, return a reversed InputMethodManger instance", new Object[0]);
        return Optional.of(sReservedImm);
    }

    public static Optional<KeyguardManager> getKeyguardManager() {
        HiLog.debug(TAG, "get key guard manager.", new Object[0]);
        Optional<ohos.app.Context> harmonyContextOtherMethod = ReflectUtil.getHarmonyContextOtherMethod();
        if (!harmonyContextOtherMethod.isPresent()) {
            return Optional.empty();
        }
        ohos.app.Context applicationContext = harmonyContextOtherMethod.get().getApplicationContext();
        if (applicationContext == null) {
            return Optional.empty();
        }
        Object hostContext = applicationContext.getHostContext();
        if (!(hostContext instanceof Context)) {
            HiLog.error(TAG, "get key guard manager failed, getHostContext is not the instance of Context.", new Object[0]);
            return Optional.empty();
        }
        Object systemService = ((Context) hostContext).getSystemService("keyguard");
        if (systemService instanceof KeyguardManager) {
            return Optional.of((KeyguardManager) systemService);
        }
        return Optional.empty();
    }

    public static Optional<Context> getAndroidContext() {
        if (isContextChanged() || sReservedAndroidContext == null) {
            HiLog.debug(TAG, "Reversed activity context is null or the ability context is changed, get a new ability context", new Object[0]);
            if (sReservedContext == null) {
                HiLog.error(TAG, "getContext failed, current ability context is null.", new Object[0]);
                return Optional.empty();
            }
            Object hostContext = sReservedContext.getHostContext();
            if (!(hostContext instanceof Context)) {
                HiLog.error(TAG, "getContext failed, getHostContext is not the instance of Context.", new Object[0]);
                return Optional.empty();
            }
            sReservedAndroidContext = (Context) hostContext;
            return Optional.of(sReservedAndroidContext);
        }
        HiLog.debug(TAG, "The ability context is not changed, return a reversed ability context object.", new Object[0]);
        return Optional.of(sReservedAndroidContext);
    }

    private static boolean isContextChanged() {
        Optional<ohos.app.Context> harmonyContext = ReflectUtil.getHarmonyContext();
        if (!harmonyContext.isPresent()) {
            return false;
        }
        ohos.app.Context applicationContext = harmonyContext.get().getApplicationContext();
        if (sReservedContext == null || sReservedContext != applicationContext) {
            HiLog.debug(TAG, "Current ability context is changed or not reserved. Get a new harmony context by reflection.", new Object[0]);
            sReservedContext = applicationContext;
            return true;
        }
        HiLog.debug(TAG, "Current ability context is not changed.", new Object[0]);
        return false;
    }
}
