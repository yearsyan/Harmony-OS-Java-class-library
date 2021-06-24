package ohos.global.resource;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;

/* compiled from: ResourceManagerImpl */
class ElementCache<K, V> {
    private static final float DEFAULT_LOAD_FACTORY = 1.0f;
    private static final int DEFAULT_MAX_CACHE_SIZE = 50;
    private LinkedHashMap<K, SoftReference<V>> cache;

    public ElementCache() {
        this(50);
    }

    public ElementCache(int i) {
        this.cache = new LinkedHashMap<K, SoftReference<V>>(50, 1.0f, true) {
            /* class ohos.global.resource.ElementCache.AnonymousClass1 */
            private static final long serialVersionUID = 1;

            /* access modifiers changed from: protected */
            @Override // java.util.LinkedHashMap
            public boolean removeEldestEntry(Map.Entry<K, SoftReference<V>> entry) {
                return size() > 50;
            }
        };
    }

    /* access modifiers changed from: package-private */
    public synchronized void put(K k, V v) {
        this.cache.put(k, new SoftReference<>(v));
    }

    /* access modifiers changed from: package-private */
    public synchronized V get(K k) {
        SoftReference<V> softReference = this.cache.get(k);
        if (softReference != null) {
            return softReference.get();
        }
        this.cache.remove(k);
        return null;
    }

    /* access modifiers changed from: package-private */
    public synchronized void remove(K k) {
        this.cache.remove(k);
    }

    /* access modifiers changed from: package-private */
    public synchronized boolean containsKey(K k) {
        if (this.cache.get(k) != null) {
            return true;
        }
        this.cache.remove(k);
        return false;
    }

    /* access modifiers changed from: package-private */
    public synchronized void clear() {
        this.cache.clear();
    }

    public String toString() {
        return this.cache.toString();
    }
}
