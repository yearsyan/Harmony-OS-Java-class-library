package ohos.agp.render.opengl;

public class EGL {
    public static final int EGL_ALPHA_SIZE = 12321;
    public static final int EGL_BAD_NATIVE_WINDOW = 12299;
    public static final int EGL_BLUE_SIZE = 12322;
    public static final int EGL_BUFFER_SIZE = 12320;
    public static final int EGL_COLOR_BUFFER_TYPE = 12351;
    public static final int EGL_DEFAULT_DISPLAY = 0;
    public static final int EGL_DEPTH_SIZE = 12325;
    public static final int EGL_GREEN_SIZE = 12323;
    public static final int EGL_HEIGHT = 12374;
    public static final int EGL_LEVEL = 12329;
    public static final int EGL_NONE = 12344;
    public static final EGLContext EGL_NO_CONTEXT = null;
    public static final EGLDisplay EGL_NO_DISPLAY = null;
    public static final EGLSurface EGL_NO_SURFACE = null;
    public static final int EGL_PBUFFER_BIT = 1;
    public static final int EGL_RED_SIZE = 12324;
    public static final int EGL_RENDERABLE_TYPE = 12352;
    public static final int EGL_RGB_BUFFER = 12430;
    public static final int EGL_STENCIL_SIZE = 12326;
    public static final int EGL_SUCCESS = 12288;
    public static final int EGL_SURFACE_TYPE = 12339;
    public static final int EGL_VERSION = 12372;
    public static final int EGL_WIDTH = 12375;
    private static final int INITIAL_OFFSET = 0;
    private static final String TAG = "EGL";

    private static native void nativeClassInit();

    private static native boolean nativeEglBindAPI(int i);

    private static native boolean nativeEglChooseConfig(EGLDisplay eGLDisplay, int[] iArr, int i, EGLConfig[] eGLConfigArr, int i2, int i3, int[] iArr2, int i4);

    private static native EGLContext nativeEglCreateContext(EGLDisplay eGLDisplay, EGLConfig eGLConfig, EGLContext eGLContext, int[] iArr, int i);

    private static native EGLSurface nativeEglCreatePbufferSurface(EGLDisplay eGLDisplay, EGLConfig eGLConfig, int[] iArr, int i);

    private static native EGLSurface nativeEglCreateWindowSurface(EGLDisplay eGLDisplay, EGLConfig eGLConfig, Object obj, int[] iArr, int i);

    private static native boolean nativeEglDestroyContext(EGLDisplay eGLDisplay, EGLContext eGLContext);

    private static native boolean nativeEglDestroySurface(EGLDisplay eGLDisplay, EGLSurface eGLSurface);

    private static native boolean nativeEglGetConfigAttrib(EGLDisplay eGLDisplay, EGLConfig eGLConfig, int i, int[] iArr, int i2);

    private static native boolean nativeEglGetConfigs(EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr, int i, int i2, int[] iArr, int i3);

    private static native EGLContext nativeEglGetCurrentContext();

    private static native EGLDisplay nativeEglGetCurrentDisplay();

    private static native EGLSurface nativeEglGetCurrentSurface(int i);

    private static native EGLDisplay nativeEglGetDisplay(long j);

    private static native int nativeEglGetError();

    private static native boolean nativeEglInitialize(EGLDisplay eGLDisplay, int[] iArr, int i, int[] iArr2, int i2);

    private static native boolean nativeEglMakeCurrent(EGLDisplay eGLDisplay, EGLSurface eGLSurface, EGLSurface eGLSurface2, EGLContext eGLContext);

    private static native boolean nativeEglQueryContext(EGLDisplay eGLDisplay, EGLContext eGLContext, int i, int[] iArr, int i2);

    private static native String nativeEglQueryString(EGLDisplay eGLDisplay, int i);

    private static native boolean nativeEglQuerySurface(EGLDisplay eGLDisplay, EGLSurface eGLSurface, int i, int[] iArr, int i2);

    private static native boolean nativeEglReleaseThread();

    private static native boolean nativeEglSwapBuffers(EGLDisplay eGLDisplay, EGLSurface eGLSurface);

    private static native boolean nativeEglTerminate(EGLDisplay eGLDisplay);

    public static boolean eglChooseConfig(EGLDisplay eGLDisplay, int[] iArr, EGLConfig[] eGLConfigArr, int i, int[] iArr2) {
        return nativeEglChooseConfig(eGLDisplay, iArr, 0, eGLConfigArr, 0, i, iArr2, 0);
    }

    public static EGLContext eglCreateContext(EGLDisplay eGLDisplay, EGLConfig eGLConfig, EGLContext eGLContext, int[] iArr) {
        return nativeEglCreateContext(eGLDisplay, eGLConfig, eGLContext, iArr, 0);
    }

    public static EGLSurface eglCreateWindowSurface(EGLDisplay eGLDisplay, EGLConfig eGLConfig, Object obj, int[] iArr) {
        return nativeEglCreateWindowSurface(eGLDisplay, eGLConfig, obj, iArr, 0);
    }

    public static boolean eglDestroyContext(EGLDisplay eGLDisplay, EGLContext eGLContext) {
        return nativeEglDestroyContext(eGLDisplay, eGLContext);
    }

    public static boolean eglDestroySurface(EGLDisplay eGLDisplay, EGLSurface eGLSurface) {
        return nativeEglDestroySurface(eGLDisplay, eGLSurface);
    }

    public static EGLDisplay eglGetDisplay(long j) {
        return nativeEglGetDisplay(j);
    }

    public static boolean eglInitialize(EGLDisplay eGLDisplay, int[] iArr, int[] iArr2) {
        return nativeEglInitialize(eGLDisplay, iArr, 0, iArr2, 0);
    }

    public static boolean eglMakeCurrent(EGLDisplay eGLDisplay, EGLSurface eGLSurface, EGLSurface eGLSurface2, EGLContext eGLContext) {
        return nativeEglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface2, eGLContext);
    }

    public static boolean eglSwapBuffers(EGLDisplay eGLDisplay, EGLSurface eGLSurface) {
        return nativeEglSwapBuffers(eGLDisplay, eGLSurface);
    }

    public static boolean eglTerminate(EGLDisplay eGLDisplay) {
        return nativeEglTerminate(eGLDisplay);
    }

    public static int eglGetError() {
        return nativeEglGetError();
    }

    public static EGLContext eglGetCurrentContext() {
        return nativeEglGetCurrentContext();
    }

    public static EGLDisplay eglGetCurrentDisplay() {
        return nativeEglGetCurrentDisplay();
    }

    public static EGLSurface eglGetCurrentSurface(int i) {
        return nativeEglGetCurrentSurface(i);
    }

    public static EGLSurface eglCreatePbufferSurface(EGLDisplay eGLDisplay, EGLConfig eGLConfig, int[] iArr) {
        return nativeEglCreatePbufferSurface(eGLDisplay, eGLConfig, iArr, 0);
    }

    public static boolean eglQuerySurface(EGLDisplay eGLDisplay, EGLSurface eGLSurface, int i, int[] iArr) {
        return nativeEglQuerySurface(eGLDisplay, eGLSurface, i, iArr, 0);
    }

    public static boolean eglGetConfigs(EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr, int i, int[] iArr) {
        return nativeEglGetConfigs(eGLDisplay, eGLConfigArr, 0, i, iArr, 0);
    }

    public static boolean eglReleaseThread() {
        return nativeEglReleaseThread();
    }

    public static String eglQueryString(EGLDisplay eGLDisplay, int i) {
        return nativeEglQueryString(eGLDisplay, i);
    }

    public static boolean eglQueryContext(EGLDisplay eGLDisplay, EGLContext eGLContext, int i, int[] iArr) {
        return nativeEglQueryContext(eGLDisplay, eGLContext, i, iArr, 0);
    }

    public static boolean eglGetConfigAttrib(EGLDisplay eGLDisplay, EGLConfig eGLConfig, int i, int[] iArr) {
        return nativeEglGetConfigAttrib(eGLDisplay, eGLConfig, i, iArr, 0);
    }

    public static boolean eglBindAPI(int i) {
        return nativeEglBindAPI(i);
    }

    static {
        nativeClassInit();
    }
}
