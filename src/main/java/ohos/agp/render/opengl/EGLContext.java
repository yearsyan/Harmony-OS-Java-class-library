package ohos.agp.render.opengl;

public class EGLContext extends EGLObjectHandle {
    private EGLContext(long j) {
        super(j);
    }

    @Override // ohos.agp.render.opengl.EGLObjectHandle
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EGLContext)) {
            return false;
        }
        return getNativeHandle() == ((EGLContext) obj).getNativeHandle();
    }

    @Override // ohos.agp.render.opengl.EGLObjectHandle
    public int hashCode() {
        int i = this.defaultResult;
        long nativeHandle = getNativeHandle();
        return (this.dafaultCodeKey * i) + ((int) (nativeHandle ^ (nativeHandle >>> this.defaultCodeBit)));
    }
}
