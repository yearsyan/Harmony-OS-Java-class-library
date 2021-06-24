package ohos.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class LruBuffer<K, V> {
    private static final int DEFAULT_CAPACITY = 64;
    private int capacity;
    private int createCount;
    private LinkedHashMap<K, V> data;
    private int matchCount;
    private int missCount;
    private int putCount;
    private int removalCount;

    /* access modifiers changed from: protected */
    public void afterRemoval(boolean z, K k, V v, V v2) {
    }

    /* access modifiers changed from: protected */
    public V createDefault(K k) {
        return null;
    }

    public LruBuffer() {
        this(64);
    }

    public LruBuffer(int i) {
        this.capacity = 0;
        if (i > 0) {
            this.data = new LinkedHashMap<>(i, 0.75f, true);
            this.capacity = i;
            return;
        }
        throw new IllegalArgumentException("capacity <= 0.");
    }

    public final int capacity() {
        int i;
        synchronized (this) {
            i = this.capacity;
        }
        return i;
    }

    private void setCapacity(int i) {
        this.capacity = i;
    }

    public final void clear() {
        synchronized (this) {
            Iterator<Map.Entry<K, V>> it = this.data.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<K, V> next = it.next();
                it.remove();
                afterRemoval(false, next.getKey(), next.getValue(), null);
            }
            this.matchCount = 0;
            this.missCount = 0;
            this.putCount = 0;
        }
    }

    public final int getMatchCount() {
        int i;
        synchronized (this) {
            i = this.matchCount;
        }
        return i;
    }

    private void addMatchCount() {
        this.matchCount++;
    }

    public final int getMissCount() {
        int i;
        synchronized (this) {
            i = this.missCount;
        }
        return i;
    }

    private void addMissCount() {
        this.missCount++;
    }

    public final int getPutCount() {
        int i;
        synchronized (this) {
            i = this.putCount;
        }
        return i;
    }

    private void addPutCount() {
        this.putCount++;
    }

    public final boolean contains(K k) {
        synchronized (this) {
            if (k != null) {
                if (this.data.containsKey(k)) {
                    this.data.get(k);
                    addMatchCount();
                    return true;
                }
            }
            addMissCount();
            return false;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0017, code lost:
        r0 = createDefault(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001b, code lost:
        if (r0 != null) goto L_0x001f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001d, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001f, code lost:
        monitor-enter(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        r3.createCount++;
        r1 = r3.data.put(r4, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002c, code lost:
        if (r1 == null) goto L_0x0033;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002e, code lost:
        r3.data.put(r4, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0033, code lost:
        monitor-exit(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0034, code lost:
        if (r1 == null) goto L_0x003b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0036, code lost:
        afterRemoval(false, r4, r0, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x003a, code lost:
        return r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x003b, code lost:
        monitor-enter(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        capacityRebalance(r3.capacity);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0041, code lost:
        monitor-exit(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0042, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final V get(K r4) {
        /*
            r3 = this;
            java.lang.String r0 = "Key is null."
            java.util.Objects.requireNonNull(r4, r0)
            monitor-enter(r3)
            java.util.LinkedHashMap<K, V> r0 = r3.data     // Catch:{ all -> 0x0049 }
            java.lang.Object r0 = r0.get(r4)     // Catch:{ all -> 0x0049 }
            if (r0 == 0) goto L_0x0013
            r3.addMatchCount()     // Catch:{ all -> 0x0049 }
            monitor-exit(r3)     // Catch:{ all -> 0x0049 }
            return r0
        L_0x0013:
            r3.addMissCount()     // Catch:{ all -> 0x0049 }
            monitor-exit(r3)     // Catch:{ all -> 0x0049 }
            java.lang.Object r0 = r3.createDefault(r4)
            if (r0 != 0) goto L_0x001f
            r3 = 0
            return r3
        L_0x001f:
            monitor-enter(r3)
            int r1 = r3.createCount     // Catch:{ all -> 0x0046 }
            int r1 = r1 + 1
            r3.createCount = r1     // Catch:{ all -> 0x0046 }
            java.util.LinkedHashMap<K, V> r1 = r3.data     // Catch:{ all -> 0x0046 }
            java.lang.Object r1 = r1.put(r4, r0)     // Catch:{ all -> 0x0046 }
            if (r1 == 0) goto L_0x0033
            java.util.LinkedHashMap<K, V> r2 = r3.data     // Catch:{ all -> 0x0046 }
            r2.put(r4, r1)     // Catch:{ all -> 0x0046 }
        L_0x0033:
            monitor-exit(r3)     // Catch:{ all -> 0x0046 }
            if (r1 == 0) goto L_0x003b
            r2 = 0
            r3.afterRemoval(r2, r4, r0, r1)
            return r1
        L_0x003b:
            monitor-enter(r3)
            int r4 = r3.capacity     // Catch:{ all -> 0x0043 }
            r3.capacityRebalance(r4)     // Catch:{ all -> 0x0043 }
            monitor-exit(r3)     // Catch:{ all -> 0x0043 }
            return r0
        L_0x0043:
            r4 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0043 }
            throw r4
        L_0x0046:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        L_0x0049:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.utils.LruBuffer.get(java.lang.Object):java.lang.Object");
    }

    public final int getCreateCount() {
        int i;
        synchronized (this) {
            i = this.createCount;
        }
        return i;
    }

    public final boolean isEmpty() {
        boolean isEmpty;
        synchronized (this) {
            isEmpty = this.data.isEmpty();
        }
        return isEmpty;
    }

    public final List<K> keys() {
        ArrayList arrayList;
        synchronized (this) {
            arrayList = new ArrayList(this.data.keySet());
        }
        return arrayList;
    }

    public final V put(K k, V v) {
        V put;
        Objects.requireNonNull(k, "key is null.");
        Objects.requireNonNull(v, "value is null.");
        synchronized (this) {
            boolean containsKey = this.data.containsKey(k);
            put = this.data.put(k, v);
            addPutCount();
            if (containsKey) {
                afterRemoval(false, k, put, v);
            } else if (this.data.size() > this.capacity) {
                capacityRebalance(this.capacity);
            }
        }
        return put;
    }

    private void capacityRebalance(int i) {
        Iterator<Map.Entry<K, V>> it = this.data.entrySet().iterator();
        while (this.data.size() > i) {
            Map.Entry<K, V> next = it.next();
            it.remove();
            this.removalCount++;
            afterRemoval(true, next.getKey(), next.getValue(), null);
        }
    }

    public final int getRemovalCount() {
        int i;
        synchronized (this) {
            i = this.removalCount;
        }
        return i;
    }

    public final Optional<V> remove(K k) {
        Objects.requireNonNull(k, "Key is null.");
        synchronized (this) {
            if (!this.data.containsKey(k)) {
                return Optional.empty();
            }
            V remove = this.data.remove(k);
            afterRemoval(false, k, remove, null);
            return Optional.of(remove);
        }
    }

    public final int size() {
        int size;
        synchronized (this) {
            size = this.data.size();
        }
        return size;
    }

    public String toString() {
        String linkedHashMap;
        synchronized (this) {
            linkedHashMap = this.data.toString();
        }
        return linkedHashMap;
    }

    public final void updateCapacity(int i) {
        if (i > 0) {
            synchronized (this) {
                if (i < this.capacity) {
                    capacityRebalance(i);
                }
                setCapacity(i);
            }
            return;
        }
        throw new IllegalArgumentException("capacity <= 0.");
    }

    public final List<V> values() {
        ArrayList arrayList;
        synchronized (this) {
            arrayList = new ArrayList(this.data.values());
        }
        return arrayList;
    }
}
