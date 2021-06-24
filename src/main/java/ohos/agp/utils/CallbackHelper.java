package ohos.agp.utils;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public final class CallbackHelper {
    private static final Map<WeakReference<?>, Long> JAVA_TO_NATIVE = new HashMap();
    private static Object LINK_HOLDER = null;
    private static final Map<Long, WeakReference<?>> NATIVE_TO_JAVA = new HashMap();

    private CallbackHelper() {
    }

    public static synchronized Object find(long j) {
        Object obj;
        Object obj2;
        synchronized (CallbackHelper.class) {
            WeakReference<?> weakReference = NATIVE_TO_JAVA.get(Long.valueOf(j));
            if (weakReference == null) {
                obj = null;
            } else {
                obj = weakReference.get();
            }
            LINK_HOLDER = obj;
            obj2 = LINK_HOLDER;
        }
        return obj2;
    }

    public static synchronized void remove(WeakReference<?> weakReference) {
        synchronized (CallbackHelper.class) {
            NATIVE_TO_JAVA.remove(JAVA_TO_NATIVE.remove(weakReference));
        }
    }

    private static synchronized void removeFromNative(long j) {
        synchronized (CallbackHelper.class) {
            JAVA_TO_NATIVE.remove(NATIVE_TO_JAVA.remove(Long.valueOf(j)));
        }
    }

    public static synchronized void add(long j, WeakReference<?> weakReference) {
        synchronized (CallbackHelper.class) {
            JAVA_TO_NATIVE.put(weakReference, Long.valueOf(j));
            NATIVE_TO_JAVA.put(Long.valueOf(j), weakReference);
        }
    }

    public static synchronized int getCount() {
        int size;
        synchronized (CallbackHelper.class) {
            size = NATIVE_TO_JAVA.size();
        }
        return size;
    }

    public static synchronized void clear() {
        synchronized (CallbackHelper.class) {
            JAVA_TO_NATIVE.clear();
            NATIVE_TO_JAVA.clear();
        }
    }

    public static synchronized void releaseLink() {
        synchronized (CallbackHelper.class) {
            LINK_HOLDER = null;
        }
    }
}
