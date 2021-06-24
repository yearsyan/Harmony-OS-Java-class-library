package ohos.agp.render;

import ohos.agp.utils.Matrix;
import ohos.agp.utils.MemoryCleanerRegistry;
import ohos.agp.utils.NativeMemoryCleanerHelper;

public class ThreeDimView {
    private long mNative3DViewHandle;

    private native void nativeApplyToCanvas(long j, long j2);

    private native long nativeGet3DViewHandle();

    private native float nativeGetLocationX(long j);

    private native float nativeGetLocationY(long j);

    private native float nativeGetLocationZ(long j);

    private native void nativeGetMatrix(long j, long j2);

    private native void nativeRestore(long j);

    private native void nativeRotateX(long j, float f);

    private native void nativeRotateY(long j, float f);

    private native void nativeRotateZ(long j, float f);

    private native void nativeSave(long j);

    private native void nativeSetLocation(long j, float f, float f2, float f3);

    private native void nativeTranslate(long j, float f, float f2, float f3);

    public ThreeDimView() {
        this.mNative3DViewHandle = 0;
        this.mNative3DViewHandle = nativeGet3DViewHandle();
        MemoryCleanerRegistry.getInstance().register(this, new ThreeDimViewCleaner(this.mNative3DViewHandle));
    }

    public void save() {
        nativeSave(this.mNative3DViewHandle);
    }

    public void restore() {
        nativeRestore(this.mNative3DViewHandle);
    }

    public void translate(float f, float f2, float f3) {
        nativeTranslate(this.mNative3DViewHandle, f, f2, f3);
    }

    public float getLocationX() {
        return nativeGetLocationX(this.mNative3DViewHandle);
    }

    public float getLocationY() {
        return nativeGetLocationY(this.mNative3DViewHandle);
    }

    public float getLocationZ() {
        return nativeGetLocationZ(this.mNative3DViewHandle);
    }

    public void setLocation(float f, float f2, float f3) {
        nativeSetLocation(this.mNative3DViewHandle, f, f2, f3);
    }

    public void getMatrix(Matrix matrix) {
        nativeGetMatrix(this.mNative3DViewHandle, matrix.getNativeHandle());
    }

    public void rotateX(float f) {
        nativeRotateX(this.mNative3DViewHandle, f);
    }

    public void rotateY(float f) {
        nativeRotateY(this.mNative3DViewHandle, f);
    }

    public void rotateZ(float f) {
        nativeRotateZ(this.mNative3DViewHandle, f);
    }

    public void applyToCanvas(Canvas canvas) {
        nativeApplyToCanvas(this.mNative3DViewHandle, canvas.getNativePtr());
    }

    protected static class ThreeDimViewCleaner extends NativeMemoryCleanerHelper {
        private native void nativeThreeDimViewRelease(long j);

        public ThreeDimViewCleaner(long j) {
            super(j);
        }

        /* access modifiers changed from: protected */
        @Override // ohos.agp.utils.NativeMemoryCleanerHelper
        public void releaseNativeMemory(long j) {
            if (j != 0) {
                nativeThreeDimViewRelease(j);
            }
        }
    }

    /* access modifiers changed from: protected */
    public long getNativeHandle() {
        return this.mNative3DViewHandle;
    }
}
