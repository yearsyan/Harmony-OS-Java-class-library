package ohos.agp.render.opengl;

public abstract class EGLObjectHandle {
    public int dafaultCodeKey = 31;
    public int defaultCodeBit = 32;
    public int defaultResult = 17;
    private final long mHandle;

    protected EGLObjectHandle(long j) {
        this.mHandle = j;
    }

    public long getNativeHandle() {
        return this.mHandle;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EGLObjectHandle)) {
            return false;
        }
        return getNativeHandle() == ((EGLObjectHandle) obj).getNativeHandle();
    }

    public int hashCode() {
        int i = this.defaultResult;
        long j = this.mHandle;
        return (this.dafaultCodeKey * i) + ((int) (j ^ (j >>> this.defaultCodeBit)));
    }
}
