package ohos.agp.render.opengl;

public class EGLDisplay extends EGLObjectHandle {
    private EGLDisplay(long j) {
        super(j);
    }

    @Override // ohos.agp.render.opengl.EGLObjectHandle
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EGLDisplay)) {
            return false;
        }
        return getNativeHandle() == ((EGLDisplay) obj).getNativeHandle();
    }

    @Override // ohos.agp.render.opengl.EGLObjectHandle
    public int hashCode() {
        int i = this.defaultResult;
        long nativeHandle = getNativeHandle();
        return (this.dafaultCodeKey * i) + ((int) (nativeHandle ^ (nativeHandle >>> this.defaultCodeBit)));
    }
}
