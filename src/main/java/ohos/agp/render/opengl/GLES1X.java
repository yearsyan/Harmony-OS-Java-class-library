package ohos.agp.render.opengl;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class GLES1X {
    public static final int GL_ACTIVE_TEXTURE = 34016;
    public static final int GL_ALWAYS = 519;
    public static final int GL_AMBIENT = 4608;
    public static final int GL_AMBIENT_AND_DIFFUSE = 5634;
    public static final int GL_ARRAY_BUFFER = 34962;
    public static final int GL_BACK = 1029;
    public static final int GL_BLEND = 3042;
    public static final int GL_CCW = 2305;
    public static final int GL_CLAMP_TO_EDGE = 33071;
    public static final int GL_COLOR_ATTACHMENT0 = 36064;
    public static final int GL_COLOR_BUFFER_BIT = 16384;
    public static final int GL_CONSTANT_ATTENUATION = 4615;
    public static final int GL_CULL_FACE = 2884;
    public static final int GL_CW = 2304;
    public static final int GL_DEPTH_ATTACHMENT = 36096;
    public static final int GL_DEPTH_BUFFER_BIT = 256;
    public static final int GL_DEPTH_TEST = 2929;
    public static final int GL_DIFFUSE = 4609;
    public static final int GL_EMISSION = 5632;
    public static final int GL_FLOAT = 5126;
    public static final int GL_FOG_COLOR = 2918;
    public static final int GL_FOG_DENSITY = 2914;
    public static final int GL_FOG_END = 2916;
    public static final int GL_FOG_MODE = 2917;
    public static final int GL_FOG_START = 2915;
    public static final int GL_FRAMEBUFFER = 36160;
    public static final int GL_FRONT = 1028;
    public static final int GL_INVALID_VALUE = 1281;
    public static final int GL_LEQUAL = 515;
    public static final int GL_LESS = 513;
    public static final int GL_LIGHT_MODEL_AMBIENT = 2899;
    public static final int GL_LIGHT_MODEL_TWO_SIDE = 2898;
    public static final int GL_LINEAR = 9729;
    public static final int GL_LINEAR_ATTENUATION = 4616;
    public static final int GL_LINEAR_MIPMAP_LINEAR = 9987;
    public static final int GL_LINES = 1;
    public static final int GL_LINE_LOOP = 2;
    public static final int GL_LINE_STRIP = 3;
    public static final int GL_MAX_TEXTURE_SIZE = 3379;
    public static final int GL_NEAREST = 9728;
    public static final int GL_NICEST = 4354;
    public static final int GL_NO_ERROR = 0;
    public static final int GL_ONE = 1;
    public static final int GL_ONE_MINUS_SRC_ALPHA = 771;
    public static final int GL_POINTS = 0;
    public static final int GL_POSITION = 4611;
    public static final int GL_QUADRATIC_ATTENUATION = 4617;
    public static final int GL_RENDERER = 7937;
    public static final int GL_REPEAT = 10497;
    public static final int GL_REPLACE = 7681;
    public static final int GL_RGB = 6407;
    public static final int GL_RGBA = 6408;
    public static final int GL_SHININESS = 5633;
    public static final int GL_SPECULAR = 4610;
    public static final int GL_SPOT_CUTOFF = 4614;
    public static final int GL_SPOT_DIRECTION = 4612;
    public static final int GL_SPOT_EXPONENT = 4613;
    public static final int GL_SRC_ALPHA = 770;
    public static final int GL_STATIC_DRAW = 35044;
    public static final int GL_STENCIL_ATTACHMENT = 36128;
    public static final int GL_TEXTURE0 = 33984;
    public static final int GL_TEXTURE1 = 33985;
    public static final int GL_TEXTURE3 = 33987;
    public static final int GL_TEXTURE_2D = 3553;
    public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_X = 34070;
    public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Y = 34072;
    public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Z = 34074;
    public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_X = 34069;
    public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Y = 34071;
    public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Z = 34073;
    public static final int GL_TEXTURE_MAG_FILTER = 10240;
    public static final int GL_TEXTURE_MIN_FILTER = 10241;
    public static final int GL_TEXTURE_WRAP_S = 10242;
    public static final int GL_TEXTURE_WRAP_T = 10243;
    public static final int GL_TRIANGLES = 4;
    public static final int GL_TRIANGLE_FAN = 6;
    public static final int GL_TRIANGLE_STRIP = 5;
    public static final int GL_TRUE = 1;
    public static final int GL_UNSIGNED_BYTE = 5121;
    public static final int GL_UNSIGNED_SHORT = 5123;
    public static final int GL_VENDOR = 7936;
    public static final int GL_VERSION = 7938;
    public static final int GL_ZERO = 0;

    private static native void nativeGlActiveTexture(int i);

    private static native void nativeGlAlphaFunc(int i, float f);

    private static native void nativeGlAlphaFuncx(int i, int i2);

    private static native void nativeGlBindBuffer(int i, int i2);

    private static native void nativeGlBindFramebufferOES(int i, int i2);

    private static native void nativeGlBindTexture(int i, int i2);

    private static native void nativeGlBlendFunc(int i, int i2);

    private static native void nativeGlBufferData(int i, int i2, Buffer buffer, int i3);

    private static native void nativeGlBufferSubData(int i, int i2, int i3, Buffer buffer);

    private static native int nativeGlCheckFramebufferStatusOES(int i);

    private static native void nativeGlClear(int i);

    private static native void nativeGlClearColor(float f, float f2, float f3, float f4);

    private static native void nativeGlClearColorx(int i, int i2, int i3, int i4);

    private static native void nativeGlClearDepthf(float f);

    private static native void nativeGlClearDepthx(int i);

    private static native void nativeGlClearStencil(int i);

    private static native void nativeGlClientActiveTexture(int i);

    private static native void nativeGlClipPlanef(int i, float[] fArr);

    private static native void nativeGlColor4f(float f, float f2, float f3, float f4);

    private static native void nativeGlColor4x(int i, int i2, int i3, int i4);

    private static native void nativeGlColorMask(boolean z, boolean z2, boolean z3, boolean z4);

    private static native void nativeGlColorPointer(int i, int i2, int i3, Buffer buffer);

    private static native void nativeGlCompressedTexImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, Buffer buffer);

    private static native void nativeGlCompressedTexSubImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, Buffer buffer);

    private static native void nativeGlCopyTexImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    private static native void nativeGlCopyTexSubImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    private static native void nativeGlCullFace(int i);

    private static native void nativeGlDeleteBuffers(int i, IntBuffer intBuffer);

    private static native void nativeGlDeleteBuffersByInt(int i, int[] iArr, int i2);

    private static native void nativeGlDeleteFramebuffersOES(int i, IntBuffer intBuffer);

    private static native void nativeGlDeleteTextures(int i, int[] iArr);

    private static native void nativeGlDepthFunc(int i);

    private static native void nativeGlDepthMask(boolean z);

    private static native void nativeGlDepthRangef(float f, float f2);

    private static native void nativeGlDepthRangex(int i, int i2);

    private static native void nativeGlDisable(int i);

    private static native void nativeGlDisableClientState(int i);

    private static native void nativeGlDrawArrays(int i, int i2, int i3);

    private static native void nativeGlDrawElements(int i, int i2, int i3, Buffer buffer);

    private static native void nativeGlEGLImageTargetTexture2DOES(int i, Buffer buffer);

    private static native void nativeGlEnable(int i);

    private static native void nativeGlEnableClientState(int i);

    private static native void nativeGlFinish();

    private static native void nativeGlFlush();

    private static native void nativeGlFogf(int i, float f);

    private static native void nativeGlFogfv(int i, float[] fArr);

    private static native void nativeGlFogx(int i, int i2);

    private static native void nativeGlFogxv(int i, IntBuffer intBuffer);

    private static native void nativeGlFogxvArray(int i, int[] iArr);

    private static native void nativeGlFramebufferTexture2DOES(int i, int i2, int i3, int i4, int i5);

    private static native void nativeGlFrontFace(int i);

    private static native void nativeGlFrustumf(float f, float f2, float f3, float f4, float f5, float f6);

    private static native void nativeGlFrustumx(int i, int i2, int i3, int i4, int i5, int i6);

    private static native void nativeGlGenBuffers(int i, IntBuffer intBuffer);

    private static native void nativeGlGenBuffersByInt(int i, int[] iArr, int i2);

    private static native void nativeGlGenFramebuffersOES(int i, IntBuffer intBuffer);

    private static native void nativeGlGenTextures(int i, int[] iArr);

    private static native int nativeGlGetError();

    private static native void nativeGlGetIntegerv(int i, int[] iArr);

    private static native String nativeGlGetString(int i);

    private static native void nativeGlGetTexEnvxv(int i, int i2, IntBuffer intBuffer);

    private static native void nativeGlGetTexParameteriv(int i, int i2, IntBuffer intBuffer);

    private static native void nativeGlGetTexParameterxv(int i, int i2, IntBuffer intBuffer);

    private static native void nativeGlHint(int i, int i2);

    private static native boolean nativeGlIsBuffer(int i);

    private static native boolean nativeGlIsEnabled(int i);

    private static native boolean nativeGlIsTexture(int i);

    private static native void nativeGlLightModelf(int i, float f);

    private static native void nativeGlLightModelfv(int i, float[] fArr);

    private static native void nativeGlLightModelx(int i, int i2);

    private static native void nativeGlLightModelxv(int i, IntBuffer intBuffer);

    private static native void nativeGlLightModelxvArray(int i, int[] iArr);

    private static native void nativeGlLightf(int i, int i2, float f);

    private static native void nativeGlLightfv(int i, int i2, float[] fArr);

    private static native void nativeGlLightx(int i, int i2, int i3);

    private static native void nativeGlLightxv(int i, int i2, IntBuffer intBuffer);

    private static native void nativeGlLightxvArray(int i, int i2, int[] iArr);

    private static native void nativeGlLineWidth(float f);

    private static native void nativeGlLineWidthx(int i);

    private static native void nativeGlLoadIdentity();

    private static native void nativeGlLoadMatrixf(float[] fArr);

    private static native void nativeGlLoadMatrixfByBuffer(FloatBuffer floatBuffer);

    private static native void nativeGlLoadMatrixx(IntBuffer intBuffer);

    private static native void nativeGlLoadMatrixxArray(int[] iArr);

    private static native void nativeGlLogicOp(int i);

    private static native void nativeGlMaterialf(int i, int i2, float f);

    private static native void nativeGlMaterialfv(int i, int i2, float[] fArr);

    private static native void nativeGlMaterialx(int i, int i2, int i3);

    private static native void nativeGlMaterialxv(int i, int i2, IntBuffer intBuffer);

    private static native void nativeGlMaterialxvArray(int i, int i2, int[] iArr);

    private static native void nativeGlMatrixMode(int i);

    private static native void nativeGlMultMatrixf(float[] fArr);

    private static native void nativeGlMultMatrixfByBuffer(FloatBuffer floatBuffer);

    private static native void nativeGlMultMatrixx(IntBuffer intBuffer);

    private static native void nativeGlMultMatrixxArray(int[] iArr);

    private static native void nativeGlMultiTexCoord4f(int i, float f, float f2, float f3, float f4);

    private static native void nativeGlMultiTexCoord4x(int i, int i2, int i3, int i4, int i5);

    private static native void nativeGlNormal3f(float f, float f2, float f3);

    private static native void nativeGlNormal3x(int i, int i2, int i3);

    private static native void nativeGlNormalPointer(int i, int i2, Buffer buffer);

    private static native void nativeGlOrthof(float f, float f2, float f3, float f4, float f5, float f6);

    private static native void nativeGlOrthox(int i, int i2, int i3, int i4, int i5, int i6);

    private static native void nativeGlPixelStorei(int i, int i2);

    private static native void nativeGlPointSize(float f);

    private static native void nativeGlPointSizex(int i);

    private static native void nativeGlPolygonOffset(float f, float f2);

    private static native void nativeGlPolygonOffsetx(int i, int i2);

    private static native void nativeGlPopMatrix();

    private static native void nativeGlPushMatrix();

    private static native void nativeGlReadPixels(int i, int i2, int i3, int i4, int i5, int i6, Buffer buffer);

    private static native void nativeGlRotatef(float f, float f2, float f3, float f4);

    private static native void nativeGlRotatex(int i, int i2, int i3, int i4);

    private static native void nativeGlSampleCoverage(float f, boolean z);

    private static native void nativeGlSampleCoveragex(int i, boolean z);

    private static native void nativeGlScalef(float f, float f2, float f3);

    private static native void nativeGlScalex(int i, int i2, int i3);

    private static native void nativeGlScissor(int i, int i2, int i3, int i4);

    private static native void nativeGlShadeModel(int i);

    private static native void nativeGlStencilFunc(int i, int i2, int i3);

    private static native void nativeGlStencilMask(int i);

    private static native void nativeGlStencilOp(int i, int i2, int i3);

    private static native void nativeGlTexCoordPointer(int i, int i2, int i3, Buffer buffer);

    private static native void nativeGlTexCoordPointerByInt(int i, int i2, int i3, int i4);

    private static native void nativeGlTexEnvf(int i, int i2, float f);

    private static native void nativeGlTexEnvfv(int i, int i2, float[] fArr);

    private static native void nativeGlTexEnvx(int i, int i2, int i3);

    private static native void nativeGlTexEnvxv(int i, int i2, IntBuffer intBuffer);

    private static native void nativeGlTexEnvxvArray(int i, int i2, int[] iArr);

    private static native void nativeGlTexImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, Buffer buffer);

    private static native void nativeGlTexParameterf(int i, int i2, float f);

    private static native void nativeGlTexParameteri(int i, int i2, int i3);

    private static native void nativeGlTexParameterx(int i, int i2, int i3);

    private static native void nativeGlTexSubImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, Buffer buffer);

    private static native void nativeGlTranslatef(float f, float f2, float f3);

    private static native void nativeGlTranslatex(int i, int i2, int i3);

    private static native void nativeGlVertexPointer(int i, int i2, int i3, Buffer buffer);

    private static native void nativeGlVertexPointerByInt(int i, int i2, int i3, int i4);

    private static native void nativeGlViewport(int i, int i2, int i3, int i4);

    public static void glClear(int i) {
        nativeGlClear(i);
    }

    public static void glClearColorx(int i, int i2, int i3, int i4) {
        nativeGlClearColorx(i, i2, i3, i4);
    }

    public static void glClearDepthx(int i) {
        nativeGlClearDepthx(i);
    }

    public static void glClearStencil(int i) {
        nativeGlClearStencil(i);
    }

    public static void glClientActiveTexture(int i) {
        nativeGlClientActiveTexture(i);
    }

    public static void glColor4x(int i, int i2, int i3, int i4) {
        nativeGlColor4x(i, i2, i3, i4);
    }

    public static void glColorMask(boolean z, boolean z2, boolean z3, boolean z4) {
        nativeGlColorMask(z, z2, z3, z4);
    }

    public static void glColorPointer(int i, int i2, int i3, Buffer buffer) {
        nativeGlColorPointer(i, i2, i3, buffer);
    }

    public static void glCompressedTexImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, Buffer buffer) {
        nativeGlCompressedTexImage2D(i, i2, i3, i4, i5, i6, i7, buffer);
    }

    public static void glCompressedTexSubImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, Buffer buffer) {
        nativeGlCompressedTexSubImage2D(i, i2, i3, i4, i5, i6, i7, i8, buffer);
    }

    public static void glCopyTexImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        nativeGlCopyTexImage2D(i, i2, i3, i4, i5, i6, i7, i8);
    }

    public static void glCopyTexSubImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        nativeGlCopyTexSubImage2D(i, i2, i3, i4, i5, i6, i7, i8);
    }

    public static void glCullFace(int i) {
        nativeGlCullFace(i);
    }

    public static void glDepthFunc(int i) {
        nativeGlDepthFunc(i);
    }

    public static void glDepthMask(boolean z) {
        nativeGlDepthMask(z);
    }

    public static void glDepthRangex(int i, int i2) {
        nativeGlDepthRangex(i, i2);
    }

    public static void glDisable(int i) {
        nativeGlDisable(i);
    }

    public static void glDisableClientState(int i) {
        nativeGlDisableClientState(i);
    }

    public static void glDrawElements(int i, int i2, int i3, Buffer buffer) {
        nativeGlDrawElements(i, i2, i3, buffer);
    }

    public static void glEnable(int i) {
        nativeGlEnable(i);
    }

    public static void glFinish() {
        nativeGlFinish();
    }

    public static void glFlush() {
        nativeGlFlush();
    }

    public static void glFogx(int i, int i2) {
        nativeGlFogx(i, i2);
    }

    public static void glFogxv(int i, IntBuffer intBuffer) {
        nativeGlFogxv(i, intBuffer);
    }

    public static void glFrontFace(int i) {
        nativeGlFrontFace(i);
    }

    public static void glFrustumx(int i, int i2, int i3, int i4, int i5, int i6) {
        nativeGlFrustumx(i, i2, i3, i4, i5, i6);
    }

    public static int glGetError() {
        return nativeGlGetError();
    }

    public static void glGetIntegerv(int i, int[] iArr) {
        nativeGlGetIntegerv(i, iArr);
    }

    public static void glGenTextures(int i, int[] iArr) {
        nativeGlGenTextures(i, iArr);
    }

    public static void glEnableClientState(int i) {
        nativeGlEnableClientState(i);
    }

    public static void glDrawArrays(int i, int i2, int i3) {
        nativeGlDrawArrays(i, i2, i3);
    }

    public static void glDeleteTextures(int i, int[] iArr) {
        nativeGlDeleteTextures(i, iArr);
    }

    public static void glOrthof(float f, float f2, float f3, float f4, float f5, float f6) {
        nativeGlOrthof(f, f2, f3, f4, f5, f6);
    }

    public static void glLightxv(int i, int i2, IntBuffer intBuffer) {
        nativeGlLightxv(i, i2, intBuffer);
    }

    public static void glLineWidthx(int i) {
        nativeGlLineWidthx(i);
    }

    public static void glLoadIdentity() {
        nativeGlLoadIdentity();
    }

    public static void glLoadMatrixx(IntBuffer intBuffer) {
        nativeGlLoadMatrixx(intBuffer);
    }

    public static void glLogicOp(int i) {
        nativeGlLogicOp(i);
    }

    public static void glMaterialx(int i, int i2, int i3) {
        nativeGlMaterialx(i, i2, i3);
    }

    public static void glMaterialxv(int i, int i2, IntBuffer intBuffer) {
        nativeGlMaterialxv(i, i2, intBuffer);
    }

    public static void glMatrixMode(int i) {
        nativeGlMatrixMode(i);
    }

    public static void glMultMatrixx(IntBuffer intBuffer) {
        nativeGlMultMatrixx(intBuffer);
    }

    public static void glMultiTexCoord4x(int i, int i2, int i3, int i4, int i5) {
        nativeGlMultiTexCoord4x(i, i2, i3, i4, i5);
    }

    public static void glNormal3x(int i, int i2, int i3) {
        nativeGlNormal3x(i, i2, i3);
    }

    public static void glNormalPointer(int i, int i2, Buffer buffer) {
        nativeGlNormalPointer(i, i2, buffer);
    }

    public static void glOrthox(int i, int i2, int i3, int i4, int i5, int i6) {
        nativeGlOrthox(i, i2, i3, i4, i5, i6);
    }

    public static void glPixelStorei(int i, int i2) {
        nativeGlPixelStorei(i, i2);
    }

    public static void glPointSizex(int i) {
        nativeGlPointSizex(i);
    }

    public static void glPolygonOffsetx(int i, int i2) {
        nativeGlPolygonOffsetx(i, i2);
    }

    public static void glPopMatrix() {
        nativeGlPopMatrix();
    }

    public static void glPushMatrix() {
        nativeGlPushMatrix();
    }

    public static void glReadPixels(int i, int i2, int i3, int i4, int i5, int i6, Buffer buffer) {
        nativeGlReadPixels(i, i2, i3, i4, i5, i6, buffer);
    }

    public static void glRotatex(int i, int i2, int i3, int i4) {
        nativeGlRotatex(i, i2, i3, i4);
    }

    public static void glSampleCoverage(float f, boolean z) {
        nativeGlSampleCoverage(f, z);
    }

    public static void glSampleCoveragex(int i, boolean z) {
        nativeGlSampleCoveragex(i, z);
    }

    public static void glScalex(int i, int i2, int i3) {
        nativeGlScalex(i, i2, i3);
    }

    public static void glScissor(int i, int i2, int i3, int i4) {
        nativeGlScissor(i, i2, i3, i4);
    }

    public static void glShadeModel(int i) {
        nativeGlShadeModel(i);
    }

    public static void glStencilFunc(int i, int i2, int i3) {
        nativeGlStencilFunc(i, i2, i3);
    }

    public static void glStencilMask(int i) {
        nativeGlStencilMask(i);
    }

    public static void glStencilOp(int i, int i2, int i3) {
        nativeGlStencilOp(i, i2, i3);
    }

    public static void glTexCoordPointer(int i, int i2, int i3, Buffer buffer) {
        nativeGlTexCoordPointer(i, i2, i3, buffer);
    }

    public static void glTexEnvx(int i, int i2, int i3) {
        nativeGlTexEnvx(i, i2, i3);
    }

    public static void glTexEnvxv(int i, int i2, IntBuffer intBuffer) {
        nativeGlTexEnvxv(i, i2, intBuffer);
    }

    public static void glTexImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, Buffer buffer) {
        nativeGlTexImage2D(i, i2, i3, i4, i5, i6, i7, i8, buffer);
    }

    public static void glTexParameterx(int i, int i2, int i3) {
        nativeGlTexParameterx(i, i2, i3);
    }

    public static void glTexSubImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, Buffer buffer) {
        nativeGlTexSubImage2D(i, i2, i3, i4, i5, i6, i7, i8, buffer);
    }

    public static void glTranslatex(int i, int i2, int i3) {
        nativeGlTranslatex(i, i2, i3);
    }

    public static void glVertexPointer(int i, int i2, int i3, Buffer buffer) {
        nativeGlVertexPointer(i, i2, i3, buffer);
    }

    public static void glViewport(int i, int i2, int i3, int i4) {
        nativeGlViewport(i, i2, i3, i4);
    }

    public static String glGetString(int i) {
        return nativeGlGetString(i);
    }

    public static void glGetTexEnvxv(int i, int i2, IntBuffer intBuffer) {
        nativeGlGetTexEnvxv(i, i2, intBuffer);
    }

    public static void glGetTexParameteriv(int i, int i2, IntBuffer intBuffer) {
        nativeGlGetTexParameteriv(i, i2, intBuffer);
    }

    public static void glGetTexParameterxv(int i, int i2, IntBuffer intBuffer) {
        nativeGlGetTexParameterxv(i, i2, intBuffer);
    }

    public static void glHint(int i, int i2) {
        nativeGlHint(i, i2);
    }

    public static boolean glIsBuffer(int i) {
        return nativeGlIsBuffer(i);
    }

    public static boolean glIsEnabled(int i) {
        return nativeGlIsEnabled(i);
    }

    public static boolean glIsTexture(int i) {
        return nativeGlIsTexture(i);
    }

    public static void glLightModelx(int i, int i2) {
        nativeGlLightModelx(i, i2);
    }

    public static void glLightModelxv(int i, IntBuffer intBuffer) {
        nativeGlLightModelxv(i, intBuffer);
    }

    public static void glLightx(int i, int i2, int i3) {
        nativeGlLightx(i, i2, i3);
    }

    public static void glDepthRangef(float f, float f2) {
        nativeGlDepthRangef(f, f2);
    }

    public static void glFogf(int i, float f) {
        nativeGlFogf(i, f);
    }

    public static void glFogfv(int i, float[] fArr) {
        nativeGlFogfv(i, fArr);
    }

    public static void glFrustumf(float f, float f2, float f3, float f4, float f5, float f6) {
        nativeGlFrustumf(f, f2, f3, f4, f5, f6);
    }

    public static void glLightModelf(int i, float f) {
        nativeGlLightModelf(i, f);
    }

    public static void glLightModelfv(int i, float[] fArr) {
        nativeGlLightModelfv(i, fArr);
    }

    public static void glLightf(int i, int i2, float f) {
        nativeGlLightf(i, i2, f);
    }

    public static void glLightfv(int i, int i2, float[] fArr) {
        nativeGlLightfv(i, i2, fArr);
    }

    public static void glLineWidth(float f) {
        nativeGlLineWidth(f);
    }

    public static void glLoadMatrixf(float[] fArr) {
        nativeGlLoadMatrixf(fArr);
    }

    public static void glMaterialf(int i, int i2, float f) {
        nativeGlMaterialf(i, i2, f);
    }

    public static void glMaterialfv(int i, int i2, float[] fArr) {
        nativeGlMaterialfv(i, i2, fArr);
    }

    public static void glMultMatrixf(float[] fArr) {
        nativeGlMultMatrixf(fArr);
    }

    public static void glMultiTexCoord4f(int i, float f, float f2, float f3, float f4) {
        nativeGlMultiTexCoord4f(i, f, f2, f3, f4);
    }

    public static void glNormal3f(float f, float f2, float f3) {
        nativeGlNormal3f(f, f2, f3);
    }

    public static void glPointSize(float f) {
        nativeGlPointSize(f);
    }

    public static void glPolygonOffset(float f, float f2) {
        nativeGlPolygonOffset(f, f2);
    }

    public static void glRotatef(float f, float f2, float f3, float f4) {
        nativeGlRotatef(f, f2, f3, f4);
    }

    public static void glScalef(float f, float f2, float f3) {
        nativeGlScalef(f, f2, f3);
    }

    public static void glTexEnvf(int i, int i2, float f) {
        nativeGlTexEnvf(i, i2, f);
    }

    public static void glTexEnvfv(int i, int i2, float[] fArr) {
        nativeGlTexEnvfv(i, i2, fArr);
    }

    public static void glTexParameterf(int i, int i2, float f) {
        nativeGlTexParameterf(i, i2, f);
    }

    public static void glTranslatef(float f, float f2, float f3) {
        nativeGlTranslatef(f, f2, f3);
    }

    public static void glActiveTexture(int i) {
        nativeGlActiveTexture(i);
    }

    public static void glAlphaFuncx(int i, int i2) {
        nativeGlAlphaFuncx(i, i2);
    }

    public static void glBindTexture(int i, int i2) {
        nativeGlBindTexture(i, i2);
    }

    public static void glBlendFunc(int i, int i2) {
        nativeGlBlendFunc(i, i2);
    }

    public static void glAlphaFunc(int i, float f) {
        nativeGlAlphaFunc(i, f);
    }

    public static void glClearColor(float f, float f2, float f3, float f4) {
        nativeGlClearColor(f, f2, f3, f4);
    }

    public static void glClearDepthf(float f) {
        nativeGlClearDepthf(f);
    }

    public static void glClipPlanef(int i, float[] fArr) {
        nativeGlClipPlanef(i, fArr);
    }

    public static void glColor4f(float f, float f2, float f3, float f4) {
        nativeGlColor4f(f, f2, f3, f4);
    }

    public static void glBindBuffer(int i, int i2) {
        nativeGlBindBuffer(i, i2);
    }

    public static void glBufferData(int i, int i2, Buffer buffer, int i3) {
        nativeGlBufferData(i, i2, buffer, i3);
    }

    public static void glBufferSubData(int i, int i2, int i3, Buffer buffer) {
        nativeGlBufferSubData(i, i2, i3, buffer);
    }

    public static void glDeleteBuffers(int i, IntBuffer intBuffer) {
        nativeGlDeleteBuffers(i, intBuffer);
    }

    public static void glGenBuffers(int i, IntBuffer intBuffer) {
        nativeGlGenBuffers(i, intBuffer);
    }

    public static void glTexParameteri(int i, int i2, int i3) {
        nativeGlTexParameteri(i, i2, i3);
    }

    public static void glLoadMatrixf(FloatBuffer floatBuffer) {
        nativeGlLoadMatrixfByBuffer(floatBuffer);
    }

    public static void glMultMatrixf(FloatBuffer floatBuffer) {
        nativeGlMultMatrixfByBuffer(floatBuffer);
    }

    public static void glDeleteBuffers(int i, int[] iArr, int i2) {
        nativeGlDeleteBuffersByInt(i, iArr, i2);
    }

    public static void glGenBuffers(int i, int[] iArr, int i2) {
        nativeGlGenBuffersByInt(i, iArr, i2);
    }

    public static void glTexCoordPointer(int i, int i2, int i3, int i4) {
        nativeGlTexCoordPointerByInt(i, i2, i3, i4);
    }

    public static void glVertexPointer(int i, int i2, int i3, int i4) {
        nativeGlVertexPointerByInt(i, i2, i3, i4);
    }

    public static void glBindFramebufferOES(int i, int i2) {
        nativeGlBindFramebufferOES(i, i2);
    }

    public static int glCheckFramebufferStatusOES(int i) {
        return nativeGlCheckFramebufferStatusOES(i);
    }

    public static void glDeleteFramebuffersOES(int i, IntBuffer intBuffer) {
        nativeGlDeleteFramebuffersOES(i, intBuffer);
    }

    public static void glFramebufferTexture2DOES(int i, int i2, int i3, int i4, int i5) {
        nativeGlFramebufferTexture2DOES(i, i2, i3, i4, i5);
    }

    public static void glGenFramebuffersOES(int i, IntBuffer intBuffer) {
        nativeGlGenFramebuffersOES(i, intBuffer);
    }

    public static void glEGLImageTargetTexture2DOES(int i, Buffer buffer) {
        nativeGlEGLImageTargetTexture2DOES(i, buffer);
    }

    public static void glFogxv(int i, int[] iArr) {
        nativeGlFogxvArray(i, iArr);
    }

    public static void glLightModelxv(int i, int[] iArr) {
        nativeGlLightModelxvArray(i, iArr);
    }

    public static void glLightxv(int i, int i2, int[] iArr) {
        nativeGlLightxvArray(i, i2, iArr);
    }

    public static void glLoadMatrixx(int[] iArr) {
        nativeGlLoadMatrixxArray(iArr);
    }

    public static void glMaterialxv(int i, int i2, int[] iArr) {
        nativeGlMaterialxvArray(i, i2, iArr);
    }

    public static void glMultMatrixx(int[] iArr) {
        nativeGlMultMatrixxArray(iArr);
    }

    public static void glTexEnvxv(int i, int i2, int[] iArr) {
        nativeGlTexEnvxvArray(i, i2, iArr);
    }
}
