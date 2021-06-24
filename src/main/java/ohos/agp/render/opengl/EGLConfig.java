package ohos.agp.render.opengl;

public class EGLConfig extends EGLObjectHandle {
    private EGLConfig(long j) {
        super(j);
    }

    @Override // ohos.agp.render.opengl.EGLObjectHandle
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EGLConfig)) {
            return false;
        }
        return getNativeHandle() == ((EGLConfig) obj).getNativeHandle();
    }

    @Override // ohos.agp.render.opengl.EGLObjectHandle
    public int hashCode() {
        int i = this.defaultResult;
        long nativeHandle = getNativeHandle();
        return (this.dafaultCodeKey * i) + ((int) (nativeHandle ^ (nativeHandle >>> this.defaultCodeBit)));
    }
}
