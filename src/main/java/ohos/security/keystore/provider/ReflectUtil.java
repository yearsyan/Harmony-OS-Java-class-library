package ohos.security.keystore.provider;

import android.text.TextUtils;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class ReflectUtil {
    private static final HiLogLabel LABEL = KeyStoreLogger.getLabel(TAG);
    private static final String TAG = "ReflectUtil";

    private ReflectUtil() {
    }

    public static <T> T getField(Object obj, String str, Class<T> cls) {
        Class<?> cls2 = obj.getClass();
        boolean z = false;
        while (cls2 != null && cls2 != Object.class) {
            try {
                Field declaredField = cls2.getDeclaredField(str);
                setAccessible(declaredField);
                z = true;
                Object obj2 = declaredField.get(obj);
                if (obj2 != null && obj2.getClass().equals(cls)) {
                    return cls.cast(obj2);
                }
            } catch (IllegalAccessException | NoSuchFieldException unused) {
                if (z) {
                    HiLog.error(LABEL, "getField failed!", new Object[0]);
                }
            }
            cls2 = cls2.getSuperclass();
        }
        return null;
    }

    public static Object getInstance(String str) {
        try {
            Constructor<?> declaredConstructor = Class.forName(str).getDeclaredConstructor(new Class[0]);
            setAccessible(declaredConstructor);
            return declaredConstructor.newInstance(new Object[0]);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException unused) {
            HiLog.error(LABEL, "get instance failed!", new Object[0]);
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x004d A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> ohos.security.keystore.provider.InvokeResult<T> invoke(java.lang.Object r10, java.lang.Class<?>[] r11, java.lang.Object[] r12, java.lang.Class<T> r13) {
        /*
            ohos.security.keystore.provider.InvokeResult r0 = new ohos.security.keystore.provider.InvokeResult
            r0.<init>()
            java.lang.String r1 = getMethodName()
            boolean r2 = checkParameters(r10, r11, r12, r13, r1)
            if (r2 != 0) goto L_0x0010
            return r0
        L_0x0010:
            java.lang.Class r2 = r10.getClass()
            r3 = 0
            r4 = 0
            r5 = r4
        L_0x0017:
            if (r2 == 0) goto L_0x0052
            java.lang.Class<java.lang.Object> r6 = java.lang.Object.class
            if (r2 == r6) goto L_0x0052
            r6 = 1
            java.lang.reflect.Method r7 = r2.getDeclaredMethod(r1, r11)     // Catch:{ IllegalAccessException | NoSuchMethodException -> 0x0040, InvocationTargetException -> 0x0031 }
            setAccessible(r7)     // Catch:{ IllegalAccessException | NoSuchMethodException -> 0x0040, InvocationTargetException -> 0x0031 }
            java.lang.Object r3 = r7.invoke(r10, r12)     // Catch:{ IllegalAccessException | NoSuchMethodException -> 0x002f, InvocationTargetException -> 0x002a }
            goto L_0x0052
        L_0x002a:
            r5 = move-exception
            r9 = r6
            r6 = r5
            r5 = r9
            goto L_0x0032
        L_0x002f:
            r5 = r6
            goto L_0x0040
        L_0x0031:
            r6 = move-exception
        L_0x0032:
            java.lang.Throwable r6 = r6.getCause()
            java.lang.Throwable r6 = ohos.security.keystore.provider.ExceptionAdapter.reThrowException(r6)
            if (r6 == 0) goto L_0x004d
            r0.setThrowable(r6)
            goto L_0x004d
        L_0x0040:
            if (r5 == 0) goto L_0x004d
            ohos.hiviewdfx.HiLogLabel r7 = ohos.security.keystore.provider.ReflectUtil.LABEL
            java.lang.Object[] r6 = new java.lang.Object[r6]
            r6[r4] = r1
            java.lang.String r8 = "invoke %{public}s + failed!"
            ohos.hiviewdfx.HiLog.error(r7, r8, r6)
        L_0x004d:
            java.lang.Class r2 = r2.getSuperclass()
            goto L_0x0017
        L_0x0052:
            if (r3 == 0) goto L_0x0061
            boolean r10 = r13.isInstance(r3)
            if (r10 == 0) goto L_0x0061
            java.lang.Object r10 = r13.cast(r3)
            r0.setResult(r10)
        L_0x0061:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.security.keystore.provider.ReflectUtil.invoke(java.lang.Object, java.lang.Class[], java.lang.Object[], java.lang.Class):ohos.security.keystore.provider.InvokeResult");
    }

    private static <T> boolean checkParameters(Object obj, Class<?>[] clsArr, Object[] objArr, Class<T> cls, String str) {
        if (obj == null || clsArr == null || objArr == null || cls == null) {
            return false;
        }
        if (!TextUtils.isEmpty(str)) {
            return true;
        }
        HiLog.warn(LABEL, "unexpected invocation failure, method not found", new Object[0]);
        return false;
    }

    private static void setAccessible(AccessibleObject accessibleObject) {
        if (!accessibleObject.isAccessible()) {
            AccessController.doPrivileged(new PrivilegedAction(accessibleObject) {
                /* class ohos.security.keystore.provider.$$Lambda$ReflectUtil$EnqSaBiDI2S6rNow6t1KrobRUA */
                private final /* synthetic */ AccessibleObject f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.security.PrivilegedAction
                public final Object run() {
                    return this.f$0.setAccessible(true);
                }
            });
        }
    }

    private static String getMethodName() {
        int i;
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        int i2 = 0;
        while (true) {
            if (i2 >= stackTrace.length) {
                i = -1;
                break;
            } else if (TextUtils.equals(stackTrace[i2].getMethodName(), "invoke")) {
                i = i2 + 1;
                break;
            } else {
                i2++;
            }
        }
        return (i < 0 || i >= stackTrace.length) ? "" : stackTrace[i].getMethodName();
    }
}
