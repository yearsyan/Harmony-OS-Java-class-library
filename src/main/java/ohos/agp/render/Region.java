package ohos.agp.render;

import ohos.agp.utils.MemoryCleanerRegistry;
import ohos.agp.utils.NativeMemoryCleanerHelper;
import ohos.agp.utils.Rect;

public class Region {
    private static final int UNION_OP_TYPE = 2;
    private long mNativeRegionHandle = nativeGetRegionHandle();

    private native boolean nativeContains(long j, int i, int i2);

    private native boolean nativeGetBoundaryPath(long j, long j2);

    private native boolean nativeGetBounds(long j, Rect rect);

    private native long nativeGetRegionHandle();

    private native boolean nativeIsComplex(long j);

    private native boolean nativeIsEmpty(long j);

    private native boolean nativeIsRect(long j);

    private native boolean nativeOpWith2Region(long j, long j2, long j3, int i);

    private native boolean nativeOpWithRect(long j, int i, int i2, int i3, int i4, int i5);

    private native boolean nativeOpWithRectAndRegion(long j, Rect rect, long j2, int i);

    private native boolean nativeOpWithRegion(long j, long j2, int i);

    private native boolean nativeQuickContains(long j, int i, int i2, int i3, int i4);

    private native boolean nativeQuickRejectWithRect(long j, int i, int i2, int i3, int i4);

    private native boolean nativeQuickRejectWithRegion(long j, long j2);

    private native boolean nativeSetPath(long j, long j2, long j3);

    private native void nativeSetRect(long j, int i, int i2, int i3, int i4);

    private native boolean nativeSetRectReturnBoolean(long j, int i, int i2, int i3, int i4);

    private native void nativeSetRegion(long j, long j2);

    private native String nativeToString(long j);

    private native void nativeTranslate(long j, int i, int i2, long j2);

    private native void nativeTranslateWithoutRegion(long j, int i, int i2);

    public Region() {
        MemoryCleanerRegistry.getInstance().register(this, new RegionCleaner(this.mNativeRegionHandle));
    }

    public Region(Region region) {
        if (region != null) {
            nativeSetRegion(this.mNativeRegionHandle, region.getNativeHandle());
        }
        MemoryCleanerRegistry.getInstance().register(this, new RegionCleaner(this.mNativeRegionHandle));
    }

    public Region(Rect rect) {
        if (rect != null) {
            nativeSetRect(this.mNativeRegionHandle, rect.left, rect.top, rect.right, rect.bottom);
        }
        MemoryCleanerRegistry.getInstance().register(this, new RegionCleaner(this.mNativeRegionHandle));
    }

    public final boolean union(Rect rect) {
        return nativeOpWithRect(this.mNativeRegionHandle, rect.left, rect.top, rect.right, rect.bottom, 2);
    }

    public void clear() {
        nativeSetRect(this.mNativeRegionHandle, 0, 0, 0, 0);
    }

    public Rect getBounds() {
        Rect rect = new Rect();
        nativeGetBounds(this.mNativeRegionHandle, rect);
        return rect;
    }

    public boolean getBounds(Rect rect) {
        if (rect == null) {
            return false;
        }
        return nativeGetBounds(this.mNativeRegionHandle, rect);
    }

    public Path getBoundaryPath() {
        Path path = new Path();
        nativeGetBoundaryPath(this.mNativeRegionHandle, path.getNativeHandle());
        return path;
    }

    public boolean getBoundaryPath(Path path) {
        return nativeGetBoundaryPath(this.mNativeRegionHandle, path.getNativeHandle());
    }

    public boolean op(Region region, Region region2, Op op) {
        if (region == null || region2 == null || op == null) {
            return false;
        }
        return nativeOpWith2Region(this.mNativeRegionHandle, region.getNativeHandle(), region2.getNativeHandle(), op.value());
    }

    public boolean op(Rect rect, Region region, Op op) {
        if (rect == null || region == null || op == null) {
            return false;
        }
        return nativeOpWithRectAndRegion(this.mNativeRegionHandle, rect, region.getNativeHandle(), op.value());
    }

    public boolean op(Region region, Op op) {
        if (region == null || op == null) {
            return false;
        }
        return nativeOpWithRegion(this.mNativeRegionHandle, region.getNativeHandle(), op.value());
    }

    public boolean op(int i, int i2, int i3, int i4, Op op) {
        if (op != null) {
            return nativeOpWithRect(this.mNativeRegionHandle, i, i2, i3, i4, op.value());
        }
        return false;
    }

    public boolean op(Rect rect, Op op) {
        if (rect == null || op == null) {
            return false;
        }
        return nativeOpWithRect(this.mNativeRegionHandle, rect.left, rect.top, rect.right, rect.bottom, op.value());
    }

    public boolean contains(int i, int i2) {
        return nativeContains(this.mNativeRegionHandle, i, i2);
    }

    public String toString() {
        return nativeToString(this.mNativeRegionHandle);
    }

    public boolean quickReject(int i, int i2, int i3, int i4) {
        return nativeQuickRejectWithRect(this.mNativeRegionHandle, i, i2, i3, i4);
    }

    public boolean quickReject(Rect rect) {
        if (rect == null) {
            return false;
        }
        return nativeQuickRejectWithRect(this.mNativeRegionHandle, rect.left, rect.top, rect.right, rect.bottom);
    }

    public boolean quickReject(Region region) {
        if (region == null) {
            return false;
        }
        return nativeQuickRejectWithRegion(this.mNativeRegionHandle, region.getNativeHandle());
    }

    public boolean isEmpty() {
        return nativeIsEmpty(this.mNativeRegionHandle);
    }

    public boolean setRect(Rect rect) {
        if (rect == null) {
            return false;
        }
        return nativeSetRectReturnBoolean(this.mNativeRegionHandle, rect.left, rect.top, rect.right, rect.bottom);
    }

    public boolean setRegion(Region region) {
        if (region == null) {
            return false;
        }
        nativeSetRegion(this.mNativeRegionHandle, region.mNativeRegionHandle);
        return true;
    }

    public boolean isRect() {
        return nativeIsRect(this.mNativeRegionHandle);
    }

    public boolean isComplex() {
        return nativeIsComplex(this.mNativeRegionHandle);
    }

    public boolean setPath(Path path, Region region) {
        if (path == null || region == null) {
            return false;
        }
        return nativeSetPath(this.mNativeRegionHandle, path.mNativePathHandle, region.mNativeRegionHandle);
    }

    public boolean quickContains(Rect rect) {
        if (rect == null) {
            return false;
        }
        return nativeQuickContains(this.mNativeRegionHandle, rect.left, rect.top, rect.right, rect.bottom);
    }

    public void translate(int i, int i2, Region region) {
        if (region == null) {
            nativeTranslateWithoutRegion(this.mNativeRegionHandle, i, i2);
        } else {
            nativeTranslate(this.mNativeRegionHandle, i, i2, region.mNativeRegionHandle);
        }
    }

    public enum Op {
        DIFFERENCE(0),
        INTERSECT(1),
        UNION(2),
        XOR(3),
        REVERSE_DIFFERENCE(4),
        REPLACE(5);
        
        final int enumInt;

        private Op(int i) {
            this.enumInt = i;
        }

        public int value() {
            return this.enumInt;
        }
    }

    protected static class RegionCleaner extends NativeMemoryCleanerHelper {
        private native void nativeRegionRelease(long j);

        public RegionCleaner(long j) {
            super(j);
        }

        /* access modifiers changed from: protected */
        @Override // ohos.agp.utils.NativeMemoryCleanerHelper
        public void releaseNativeMemory(long j) {
            if (j != 0) {
                nativeRegionRelease(j);
            }
        }
    }

    public long getNativeHandle() {
        return this.mNativeRegionHandle;
    }
}
