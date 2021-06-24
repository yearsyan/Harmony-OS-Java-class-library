package ohos.agp.render.opengl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class GLES20 {
    public static final int GL_ACTIVE_TEXTURE = 34016;
    public static final int GL_ALWAYS = 519;
    public static final int GL_ARRAY_BUFFER = 34962;
    public static final int GL_BACK = 1029;
    public static final int GL_BLEND = 3042;
    public static final int GL_CCW = 2305;
    public static final int GL_CLAMP_TO_EDGE = 33071;
    public static final int GL_COLOR_ATTACHMENT0 = 36064;
    public static final int GL_COLOR_BUFFER_BIT = 16384;
    public static final int GL_COMPILE_STATUS = 35713;
    public static final int GL_CULL_FACE = 2884;
    public static final int GL_CW = 2304;
    public static final int GL_DELETE_STATUS = 35712;
    public static final int GL_DEPTH_ATTACHMENT = 36096;
    public static final int GL_DEPTH_BUFFER_BIT = 256;
    public static final int GL_DEPTH_COMPONENT16 = 33189;
    public static final int GL_DEPTH_TEST = 2929;
    public static final int GL_FLOAT = 5126;
    public static final int GL_FRAGMENT_SHADER = 35632;
    public static final int GL_FRAMEBUFFER = 36160;
    public static final int GL_FRAMEBUFFER_COMPLETE = 36053;
    public static final int GL_FRONT = 1028;
    public static final int GL_INVALID_VALUE = 1281;
    public static final int GL_KEEP = 7680;
    public static final int GL_LEQUAL = 515;
    public static final int GL_LESS = 513;
    public static final int GL_LINEAR = 9729;
    public static final int GL_LINEAR_MIPMAP_LINEAR = 9987;
    public static final int GL_LINES = 1;
    public static final int GL_LINE_LOOP = 2;
    public static final int GL_LINE_STRIP = 3;
    public static final int GL_LINK_STATUS = 35714;
    public static final int GL_MAX_TEXTURE_SIZE = 3379;
    public static final int GL_NEAREST = 9728;
    public static final int GL_NICEST = 4354;
    public static final int GL_NONE = 0;
    public static final int GL_NO_ERROR = 0;
    public static final int GL_ONE = 1;
    public static final int GL_ONE_MINUS_SRC_ALPHA = 771;
    public static final int GL_POINTS = 0;
    public static final int GL_RENDERBUFFER = 36161;
    public static final int GL_RENDERER = 7937;
    public static final int GL_REPEAT = 10497;
    public static final int GL_REPLACE = 7681;
    public static final int GL_RGB = 6407;
    public static final int GL_RGBA = 6408;
    public static final int GL_SAMPLER_2D = 35678;
    public static final int GL_SHADER_COMPILER = 36346;
    public static final int GL_SRC_ALPHA = 770;
    public static final int GL_STATIC_DRAW = 35044;
    public static final int GL_STENCIL_ATTACHMENT = 36128;
    public static final int GL_STENCIL_TEST = 2960;
    public static final int GL_TEXTURE0 = 33984;
    public static final int GL_TEXTURE1 = 33985;
    public static final int GL_TEXTURE3 = 33987;
    public static final int GL_TEXTURE_2D = 3553;
    public static final int GL_TEXTURE_MAG_FILTER = 10240;
    public static final int GL_TEXTURE_MIN_FILTER = 10241;
    public static final int GL_TEXTURE_WRAP_S = 10242;
    public static final int GL_TEXTURE_WRAP_T = 10243;
    public static final int GL_TRIANGLES = 4;
    public static final int GL_TRIANGLE_FAN = 6;
    public static final int GL_TRIANGLE_STRIP = 5;
    public static final int GL_TRUE = 1;
    public static final int GL_UNPACK_ALIGNMENT = 3317;
    public static final int GL_UNSIGNED_BYTE = 5121;
    public static final int GL_UNSIGNED_SHORT = 5123;
    public static final int GL_VALIDATE_STATUS = 35715;
    public static final int GL_VENDOR = 7936;
    public static final int GL_VERSION = 7938;
    public static final int GL_VERTEX_SHADER = 35633;
    public static final int GL_ZERO = 0;

    private static native void nativeGlActiveTexture(int i);

    private static native void nativeGlAttachShader(int i, int i2);

    private static native void nativeGlBindAttribLocation(int i, int i2, String str);

    private static native void nativeGlBindBuffer(int i, int i2);

    private static native void nativeGlBindFramebuffer(int i, int i2);

    private static native void nativeGlBindRenderbuffer(int i, int i2);

    private static native void nativeGlBindTexture(int i, int i2);

    private static native void nativeGlBlendColor(float f, float f2, float f3, float f4);

    private static native void nativeGlBlendEquation(int i);

    private static native void nativeGlBlendEquationSeparate(int i, int i2);

    private static native void nativeGlBlendFunc(int i, int i2);

    private static native void nativeGlBlendFuncSeparate(int i, int i2, int i3, int i4);

    private static native void nativeGlBufferData(int i, int i2, Buffer buffer, int i3);

    private static native void nativeGlBufferSubData(int i, int i2, int i3, Buffer buffer);

    private static native int nativeGlCheckFramebufferStatus(int i);

    private static native void nativeGlClear(int i);

    private static native void nativeGlClearColor(float f, float f2, float f3, float f4);

    private static native void nativeGlClearDepthf(float f);

    private static native void nativeGlClearStencil(int i);

    private static native void nativeGlColorMask(boolean z, boolean z2, boolean z3, boolean z4);

    private static native void nativeGlCompileShader(int i);

    private static native void nativeGlCompressedTexImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, Buffer buffer);

    private static native void nativeGlCompressedTexSubImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, Buffer buffer);

    private static native void nativeGlCopyTexImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    private static native void nativeGlCopyTexSubImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    private static native int nativeGlCreateProgram();

    private static native int nativeGlCreateShader(int i);

    private static native void nativeGlCullFace(int i);

    private static native void nativeGlDeleteBuffers(int i, IntBuffer intBuffer);

    private static native void nativeGlDeleteFramebuffersByBuffer(int i, IntBuffer intBuffer);

    private static native void nativeGlDeleteFramebuffersByInt(int i, int[] iArr);

    private static native void nativeGlDeleteProgram(int i);

    private static native void nativeGlDeleteRenderbuffersByBuffer(int i, IntBuffer intBuffer);

    private static native void nativeGlDeleteRenderbuffersByInt(int i, int[] iArr);

    private static native void nativeGlDeleteShader(int i);

    private static native void nativeGlDeleteTexturesByBuffer(int i, IntBuffer intBuffer);

    private static native void nativeGlDeleteTexturesByInt(int i, int[] iArr);

    private static native void nativeGlDepthFunc(int i);

    private static native void nativeGlDepthMask(boolean z);

    private static native void nativeGlDepthRangef(float f, float f2);

    private static native void nativeGlDetachShader(int i, int i2);

    private static native void nativeGlDisable(int i);

    private static native void nativeGlDisableVertexAttribArray(int i);

    private static native void nativeGlDrawArrays(int i, int i2, int i3);

    private static native void nativeGlDrawElements(int i, int i2, int i3, Buffer buffer);

    private static native void nativeGlEnable(int i);

    private static native void nativeGlEnableVertexAttribArray(int i);

    private static native void nativeGlFinish();

    private static native void nativeGlFlush();

    private static native void nativeGlFramebufferRenderbuffer(int i, int i2, int i3, int i4);

    private static native void nativeGlFramebufferTexture2D(int i, int i2, int i3, int i4, int i5);

    private static native void nativeGlFrontFace(int i);

    private static native void nativeGlGenBuffersByBuffer(int i, IntBuffer intBuffer);

    private static native void nativeGlGenBuffersByInt(int i, int[] iArr);

    private static native void nativeGlGenFramebuffersByBuffer(int i, IntBuffer intBuffer);

    private static native void nativeGlGenFramebuffersByInt(int i, int[] iArr);

    private static native void nativeGlGenRenderbuffersByBuffer(int i, IntBuffer intBuffer);

    private static native void nativeGlGenRenderbuffersByInt(int i, int[] iArr);

    private static native void nativeGlGenTexturesByBuffer(int i, IntBuffer intBuffer);

    private static native void nativeGlGenTexturesByInt(int i, int[] iArr);

    private static native void nativeGlGenerateMipmap(int i);

    private static native void nativeGlGetActiveAttrib(int i, int i2, int i3, int[] iArr, int[] iArr2, int[] iArr3, byte[] bArr);

    private static native void nativeGlGetActiveUniform(int i, int i2, int i3, int[] iArr, int[] iArr2, int[] iArr3, byte[] bArr);

    private static native void nativeGlGetAttachedShaders(int i, int i2, IntBuffer intBuffer, IntBuffer intBuffer2);

    private static native int nativeGlGetAttribLocation(int i, String str);

    private static native void nativeGlGetBooleanv(int i, boolean[] zArr);

    private static native void nativeGlGetBufferParameteriv(int i, int i2, IntBuffer intBuffer);

    private static native int nativeGlGetError();

    private static native void nativeGlGetFloatv(int i, float[] fArr);

    private static native void nativeGlGetFramebufferAttachmentParameteriv(int i, int i2, int i3, IntBuffer intBuffer);

    private static native void nativeGlGetIntegerv(int i, int[] iArr);

    private static native String nativeGlGetProgramInfoLog(int i);

    private static native void nativeGlGetProgramiv(int i, int i2, int[] iArr);

    private static native void nativeGlGetRenderbufferParameteriv(int i, int i2, IntBuffer intBuffer);

    private static native String nativeGlGetShaderInfoLog(int i);

    private static native void nativeGlGetShaderPrecisionFormat(int i, int i2, IntBuffer intBuffer, IntBuffer intBuffer2);

    private static native void nativeGlGetShaderSource(int i, int i2, int[] iArr, int i3, byte[] bArr, int i4);

    private static native void nativeGlGetShaderivByBuffer(int i, int i2, IntBuffer intBuffer);

    private static native void nativeGlGetShaderivByInt(int i, int i2, int[] iArr);

    private static native String nativeGlGetString(int i);

    private static native void nativeGlGetTexParameterfv(int i, int i2, FloatBuffer floatBuffer);

    private static native void nativeGlGetTexParameteriv(int i, int i2, IntBuffer intBuffer);

    private static native int nativeGlGetUniformLocation(int i, String str);

    private static native void nativeGlGetUniformfv(int i, int i2, FloatBuffer floatBuffer);

    private static native void nativeGlGetUniformiv(int i, int i2, IntBuffer intBuffer);

    private static native void nativeGlGetVertexAttribfv(int i, int i2, FloatBuffer floatBuffer);

    private static native void nativeGlGetVertexAttribiv(int i, int i2, IntBuffer intBuffer);

    private static native void nativeGlHint(int i, int i2);

    private static native boolean nativeGlIsBuffer(int i);

    private static native boolean nativeGlIsEnabled(int i);

    private static native boolean nativeGlIsFramebuffer(int i);

    private static native boolean nativeGlIsProgram(int i);

    private static native boolean nativeGlIsRenderbuffer(int i);

    private static native boolean nativeGlIsShader(int i);

    private static native boolean nativeGlIsTexture(int i);

    private static native void nativeGlLineWidth(float f);

    private static native void nativeGlLinkProgram(int i);

    private static native void nativeGlPixelStorei(int i, int i2);

    private static native void nativeGlPolygonOffset(float f, float f2);

    private static native void nativeGlReadPixels(int i, int i2, int i3, int i4, int i5, int i6, Buffer buffer);

    private static native void nativeGlReleaseShaderCompiler();

    private static native void nativeGlRenderbufferStorage(int i, int i2, int i3, int i4);

    private static native void nativeGlSampleCoverage(float f, boolean z);

    private static native void nativeGlScissor(int i, int i2, int i3, int i4);

    private static native void nativeGlShaderBinary(int i, IntBuffer intBuffer, int i2, Buffer buffer, int i3);

    private static native void nativeGlShaderSource(int i, String str);

    private static native void nativeGlStencilFunc(int i, int i2, int i3);

    private static native void nativeGlStencilFuncSeparate(int i, int i2, int i3, int i4);

    private static native void nativeGlStencilMask(int i);

    private static native void nativeGlStencilMaskSeparate(int i, int i2);

    private static native void nativeGlStencilOp(int i, int i2, int i3);

    private static native void nativeGlStencilOpSeparate(int i, int i2, int i3, int i4);

    private static native void nativeGlTexImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, Buffer buffer);

    private static native void nativeGlTexParameterf(int i, int i2, float f);

    private static native void nativeGlTexParameterfv(int i, int i2, FloatBuffer floatBuffer);

    private static native void nativeGlTexParameteri(int i, int i2, int i3);

    private static native void nativeGlTexParameteriv(int i, int i2, IntBuffer intBuffer);

    private static native void nativeGlTexSubImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, Buffer buffer);

    private static native void nativeGlUniform1f(int i, float f);

    private static native void nativeGlUniform1fv(int i, int i2, FloatBuffer floatBuffer);

    private static native void nativeGlUniform1i(int i, int i2);

    private static native void nativeGlUniform1iv(int i, int i2, int[] iArr);

    private static native void nativeGlUniform2f(int i, float f, float f2);

    private static native void nativeGlUniform2fv(int i, int i2, FloatBuffer floatBuffer);

    private static native void nativeGlUniform2i(int i, int i2, int i3);

    private static native void nativeGlUniform2iv(int i, int i2, IntBuffer intBuffer);

    private static native void nativeGlUniform3f(int i, float f, float f2, float f3);

    private static native void nativeGlUniform3fv(int i, int i2, float[] fArr);

    private static native void nativeGlUniform3i(int i, int i2, int i3, int i4);

    private static native void nativeGlUniform3iv(int i, int i2, IntBuffer intBuffer);

    private static native void nativeGlUniform4f(int i, float f, float f2, float f3, float f4);

    private static native void nativeGlUniform4fv(int i, int i2, float[] fArr);

    private static native void nativeGlUniform4i(int i, int i2, int i3, int i4, int i5);

    private static native void nativeGlUniform4iv(int i, int i2, IntBuffer intBuffer);

    private static native void nativeGlUniformMatrix2fv(int i, int i2, boolean z, float[] fArr);

    private static native void nativeGlUniformMatrix3fv(int i, int i2, boolean z, float[] fArr);

    private static native void nativeGlUniformMatrix4fv(int i, int i2, boolean z, float[] fArr);

    private static native void nativeGlUseProgram(int i);

    private static native void nativeGlValidateProgram(int i);

    private static native void nativeGlVertexAttrib1f(int i, float f);

    private static native void nativeGlVertexAttrib1fv(int i, FloatBuffer floatBuffer);

    private static native void nativeGlVertexAttrib2f(int i, float f, float f2);

    private static native void nativeGlVertexAttrib2fv(int i, FloatBuffer floatBuffer);

    private static native void nativeGlVertexAttrib3f(int i, float f, float f2, float f3);

    private static native void nativeGlVertexAttrib3fv(int i, FloatBuffer floatBuffer);

    private static native void nativeGlVertexAttrib4f(int i, float f, float f2, float f3, float f4);

    private static native void nativeGlVertexAttrib4fv(int i, FloatBuffer floatBuffer);

    private static native void nativeGlVertexAttribPointerByBuffer(int i, int i2, int i3, boolean z, int i4, Buffer buffer);

    private static native void nativeGlVertexAttribPointerByInt(int i, int i2, int i3, boolean z, int i4, int i5);

    private static native void nativeGlViewport(int i, int i2, int i3, int i4);

    public static void glClearColor(float f, float f2, float f3, float f4) {
        nativeGlClearColor(f, f2, f3, f4);
    }

    public static void glAttachShader(int i, int i2) {
        nativeGlAttachShader(i, i2);
    }

    public static int glCreateProgram() {
        return nativeGlCreateProgram();
    }

    public static void glLinkProgram(int i) {
        nativeGlLinkProgram(i);
    }

    public static void glViewport(int i, int i2, int i3, int i4) {
        nativeGlViewport(i, i2, i3, i4);
    }

    public static void glUseProgram(int i) {
        nativeGlUseProgram(i);
    }

    public static int glGetAttribLocation(int i, String str) {
        return nativeGlGetAttribLocation(i, str);
    }

    public static void glEnableVertexAttribArray(int i) {
        nativeGlEnableVertexAttribArray(i);
    }

    public static void glVertexAttribPointer(int i, int i2, int i3, boolean z, int i4, int i5) {
        nativeGlVertexAttribPointerByInt(i, i2, i3, z, i4, i5);
    }

    public static void glVertexAttribPointer(int i, int i2, int i3, boolean z, int i4, Buffer buffer) {
        nativeGlVertexAttribPointerByBuffer(i, i2, i3, z, i4, buffer);
    }

    public static int glGetUniformLocation(int i, String str) {
        return nativeGlGetUniformLocation(i, str);
    }

    public static void glUniform4fv(int i, int i2, float[] fArr) {
        nativeGlUniform4fv(i, i2, fArr);
    }

    public static void glDrawArrays(int i, int i2, int i3) {
        nativeGlDrawArrays(i, i2, i3);
    }

    public static void glDisableVertexAttribArray(int i) {
        nativeGlDisableVertexAttribArray(i);
    }

    public static int glCreateShader(int i) {
        return nativeGlCreateShader(i);
    }

    public static void glShaderSource(int i, int i2, String[] strArr, IntBuffer intBuffer) {
        StringBuilder sb = new StringBuilder();
        for (String str : strArr) {
            sb.append(str);
        }
        nativeGlShaderSource(i, sb.toString());
    }

    public static void glCompileShader(int i) {
        nativeGlCompileShader(i);
    }

    public static void glEnable(int i) {
        nativeGlEnable(i);
    }

    public static void glActiveTexture(int i) {
        nativeGlActiveTexture(i);
    }

    public static void glClear(int i) {
        nativeGlClear(i);
    }

    public static void glTexParameteri(int i, int i2, int i3) {
        nativeGlTexParameteri(i, i2, i3);
    }

    public static void glGenTextures(int i, int[] iArr) {
        nativeGlGenTexturesByInt(i, iArr);
    }

    public static void glBindTexture(int i, int i2) {
        nativeGlBindTexture(i, i2);
    }

    public static void glDrawElements(int i, int i2, int i3, Buffer buffer) {
        nativeGlDrawElements(i, i2, i3, buffer);
    }

    public static void glUniform1i(int i, int i2) {
        nativeGlUniform1i(i, i2);
    }

    public static void glBindFramebuffer(int i, int i2) {
        nativeGlBindFramebuffer(i, i2);
    }

    public static void glBindRenderbuffer(int i, int i2) {
        nativeGlBindRenderbuffer(i, i2);
    }

    public static int glCheckFramebufferStatus(int i) {
        return nativeGlCheckFramebufferStatus(i);
    }

    public static void glCullFace(int i) {
        nativeGlCullFace(i);
    }

    public static void glDeleteProgram(int i) {
        nativeGlDeleteProgram(i);
    }

    public static void glDepthFunc(int i) {
        nativeGlDepthFunc(i);
    }

    public static void glDisable(int i) {
        nativeGlDisable(i);
    }

    public static void glFramebufferRenderbuffer(int i, int i2, int i3, int i4) {
        nativeGlFramebufferRenderbuffer(i, i2, i3, i4);
    }

    public static void glFramebufferTexture2D(int i, int i2, int i3, int i4, int i5) {
        nativeGlFramebufferTexture2D(i, i2, i3, i4, i5);
    }

    public static void glFrontFace(int i) {
        nativeGlFrontFace(i);
    }

    public static void glRenderbufferStorage(int i, int i2, int i3, int i4) {
        nativeGlRenderbufferStorage(i, i2, i3, i4);
    }

    public static void glTexParameterf(int i, int i2, float f) {
        nativeGlTexParameterf(i, i2, f);
    }

    public static void glUniform1f(int i, float f) {
        nativeGlUniform1f(i, f);
    }

    public static void glUniform2f(int i, float f, float f2) {
        nativeGlUniform2f(i, f, f2);
    }

    public static void glUniform3f(int i, float f, float f2, float f3) {
        nativeGlUniform3f(i, f, f2, f3);
    }

    public static void glUniform4f(int i, float f, float f2, float f3, float f4) {
        nativeGlUniform4f(i, f, f2, f3, f4);
    }

    public static void glBlendFunc(int i, int i2) {
        nativeGlBlendFunc(i, i2);
    }

    public static void glBlendFuncSeparate(int i, int i2, int i3, int i4) {
        nativeGlBlendFuncSeparate(i, i2, i3, i4);
    }

    public static int glGetError() {
        return nativeGlGetError();
    }

    public static void glDeleteFramebuffers(int i, IntBuffer intBuffer) {
        nativeGlDeleteFramebuffersByBuffer(i, intBuffer);
    }

    public static void glDeleteRenderbuffers(int i, IntBuffer intBuffer) {
        nativeGlDeleteRenderbuffersByBuffer(i, intBuffer);
    }

    public static void glDeleteTextures(int i, IntBuffer intBuffer) {
        nativeGlDeleteTexturesByBuffer(i, intBuffer);
    }

    public static void glGenFramebuffers(int i, IntBuffer intBuffer) {
        nativeGlGenFramebuffersByBuffer(i, intBuffer);
    }

    public static void glGenRenderbuffers(int i, IntBuffer intBuffer) {
        nativeGlGenRenderbuffersByBuffer(i, intBuffer);
    }

    public static void glGenTextures(int i, IntBuffer intBuffer) {
        nativeGlGenTexturesByBuffer(i, intBuffer);
    }

    public static void glGetShaderiv(int i, int i2, IntBuffer intBuffer) {
        nativeGlGetShaderivByBuffer(i, i2, intBuffer);
    }

    public static void glReadPixels(int i, int i2, int i3, int i4, int i5, int i6, Buffer buffer) {
        nativeGlReadPixels(i, i2, i3, i4, i5, i6, buffer);
    }

    public static void glTexImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, Buffer buffer) {
        nativeGlTexImage2D(i, i2, i3, i4, i5, i6, i7, i8, buffer);
    }

    public static void glDeleteShader(int i) {
        nativeGlDeleteShader(i);
    }

    public static void glGetProgramiv(int i, int i2, int[] iArr) {
        nativeGlGetProgramiv(i, i2, iArr);
    }

    public static void glUniform3fv(int i, int i2, float[] fArr) {
        nativeGlUniform3fv(i, i2, fArr);
    }

    public static void glUniformMatrix4fv(int i, int i2, boolean z, float[] fArr) {
        nativeGlUniformMatrix4fv(i, i2, z, fArr);
    }

    public static void glDeleteFramebuffers(int i, int[] iArr) {
        nativeGlDeleteFramebuffersByInt(i, iArr);
    }

    public static void glDeleteRenderbuffers(int i, int[] iArr) {
        nativeGlDeleteRenderbuffersByInt(i, iArr);
    }

    public static void glDeleteTextures(int i, int[] iArr) {
        nativeGlDeleteTexturesByInt(i, iArr);
    }

    public static void glGenFramebuffers(int i, int[] iArr) {
        nativeGlGenFramebuffersByInt(i, iArr);
    }

    public static void glGenRenderbuffers(int i, int[] iArr) {
        nativeGlGenRenderbuffersByInt(i, iArr);
    }

    public static void glGetProgramInfoLog(int i, int i2, IntBuffer intBuffer, StringBuffer stringBuffer) {
        String nativeGlGetProgramInfoLog = nativeGlGetProgramInfoLog(i);
        if (nativeGlGetProgramInfoLog.length() > i2) {
            stringBuffer.append(nativeGlGetProgramInfoLog.substring(0, i2));
        } else {
            stringBuffer.append(nativeGlGetProgramInfoLog);
        }
        if (intBuffer != null) {
            intBuffer.put(stringBuffer.length());
        }
    }

    public static void glGetShaderInfoLog(int i, int i2, IntBuffer intBuffer, StringBuffer stringBuffer) {
        String nativeGlGetShaderInfoLog = nativeGlGetShaderInfoLog(i);
        if (nativeGlGetShaderInfoLog.length() > i2) {
            stringBuffer.append(nativeGlGetShaderInfoLog.substring(0, i2));
        } else {
            stringBuffer.append(nativeGlGetShaderInfoLog);
        }
        if (intBuffer != null) {
            intBuffer.put(stringBuffer.length());
        }
    }

    public static void glGenBuffers(int i, int[] iArr) {
        nativeGlGenBuffersByInt(i, iArr);
    }

    public static void glBindBuffer(int i, int i2) {
        nativeGlBindBuffer(i, i2);
    }

    public static void glBufferData(int i, int i2, Buffer buffer, int i3) {
        nativeGlBufferData(i, i2, buffer, i3);
    }

    public static void glGetShaderiv(int i, int i2, int[] iArr) {
        nativeGlGetShaderivByInt(i, i2, iArr);
    }

    public static void glGenerateMipmap(int i) {
        nativeGlGenerateMipmap(i);
    }

    public static void glGetIntegerv(int i, int[] iArr) {
        nativeGlGetIntegerv(i, iArr);
    }

    public static void glCompressedTexImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, Buffer buffer) {
        nativeGlCompressedTexImage2D(i, i2, i3, i4, i5, i6, i7, buffer);
    }

    public static void glCompressedTexSubImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, Buffer buffer) {
        nativeGlCompressedTexSubImage2D(i, i2, i3, i4, i5, i6, i7, i8, buffer);
    }

    public static void glDepthRangef(float f, float f2) {
        nativeGlDepthRangef(f, f2);
    }

    public static void glGetAttachedShaders(int i, int i2, IntBuffer intBuffer, IntBuffer intBuffer2) {
        nativeGlGetAttachedShaders(i, i2, intBuffer, intBuffer2);
    }

    public static void glGetBufferParameteriv(int i, int i2, IntBuffer intBuffer) {
        nativeGlGetBufferParameteriv(i, i2, intBuffer);
    }

    public static void glGetFramebufferAttachmentParameteriv(int i, int i2, int i3, IntBuffer intBuffer) {
        nativeGlGetFramebufferAttachmentParameteriv(i, i2, i3, intBuffer);
    }

    public static void glGetRenderbufferParameteriv(int i, int i2, IntBuffer intBuffer) {
        nativeGlGetBufferParameteriv(i, i2, intBuffer);
    }

    public static void glGetShaderPrecisionFormat(int i, int i2, IntBuffer intBuffer, IntBuffer intBuffer2) {
        nativeGlGetShaderPrecisionFormat(i, i2, intBuffer, intBuffer2);
    }

    public static void glGetShaderSource(int i, int i2, IntBuffer intBuffer, CharBuffer charBuffer) {
        int[] array = intBuffer.array();
        ByteBuffer allocate = ByteBuffer.allocate(charBuffer.capacity() * 2);
        while (charBuffer.hasRemaining()) {
            allocate.putChar(charBuffer.get());
        }
        nativeGlGetShaderSource(i, i2, array, 0, allocate.array(), 0);
    }

    public static void glGetTexParameterfv(int i, int i2, FloatBuffer floatBuffer) {
        nativeGlGetTexParameterfv(i, i2, floatBuffer);
    }

    public static void glGetTexParameteriv(int i, int i2, IntBuffer intBuffer) {
        nativeGlGetTexParameteriv(i, i2, intBuffer);
    }

    public static void glGetUniformfv(int i, int i2, FloatBuffer floatBuffer) {
        nativeGlGetUniformfv(i, i2, floatBuffer);
    }

    public static void glGetUniformiv(int i, int i2, IntBuffer intBuffer) {
        nativeGlGetUniformiv(i, i2, intBuffer);
    }

    public static void glGetVertexAttribfv(int i, int i2, FloatBuffer floatBuffer) {
        nativeGlGetVertexAttribfv(i, i2, floatBuffer);
    }

    public static void glGetVertexAttribiv(int i, int i2, IntBuffer intBuffer) {
        nativeGlGetVertexAttribiv(i, i2, intBuffer);
    }

    public static void glUniformMatrix2fv(int i, int i2, boolean z, float[] fArr) {
        nativeGlUniformMatrix2fv(i, i2, z, fArr);
    }

    public static void glUniform1iv(int i, int i2, int[] iArr) {
        nativeGlUniform1iv(i, i2, iArr);
    }

    public static boolean glIsTexture(int i) {
        return nativeGlIsTexture(i);
    }

    public static boolean glIsProgram(int i) {
        return nativeGlIsProgram(i);
    }

    public static boolean glIsEnabled(int i) {
        return nativeGlIsEnabled(i);
    }

    public static void glGetFloatv(int i, float[] fArr) {
        nativeGlGetFloatv(i, fArr);
    }

    public static void glGetBooleanv(int i, boolean[] zArr) {
        nativeGlGetBooleanv(i, zArr);
    }

    public static void glGetActiveUniform(int i, int i2, int i3, int[] iArr, int[] iArr2, int[] iArr3, byte[] bArr) {
        nativeGlGetActiveUniform(i, i2, i3, iArr, iArr2, iArr3, bArr);
    }

    public static void glGetActiveAttrib(int i, int i2, int i3, int[] iArr, int[] iArr2, int[] iArr3, byte[] bArr) {
        nativeGlGetActiveAttrib(i, i2, i3, iArr, iArr2, iArr3, bArr);
    }

    public static void glDetachShader(int i, int i2) {
        nativeGlDetachShader(i, i2);
    }

    public static void glCopyTexSubImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        nativeGlCopyTexSubImage2D(i, i2, i3, i4, i5, i6, i7, i8);
    }

    public static void glCopyTexImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        nativeGlCopyTexImage2D(i, i2, i3, i4, i5, i6, i7, i8);
    }

    public static void glBlendEquationSeparate(int i, int i2) {
        nativeGlBlendEquationSeparate(i, i2);
    }

    public static void glBlendEquation(int i) {
        nativeGlBlendEquation(i);
    }

    public static void glHint(int i, int i2) {
        nativeGlHint(i, i2);
    }

    public static boolean glIsBuffer(int i) {
        return nativeGlIsBuffer(i);
    }

    public static boolean glIsFramebuffer(int i) {
        return nativeGlIsFramebuffer(i);
    }

    public static boolean glIsRenderbuffer(int i) {
        return nativeGlIsRenderbuffer(i);
    }

    public static void glPolygonOffset(float f, float f2) {
        nativeGlPolygonOffset(f, f2);
    }

    public static void glReleaseShaderCompiler() {
        nativeGlReleaseShaderCompiler();
    }

    public static void glSampleCoverage(float f, boolean z) {
        nativeGlSampleCoverage(f, z);
    }

    public static void glShaderBinary(int i, IntBuffer intBuffer, int i2, Buffer buffer, int i3) {
        nativeGlShaderBinary(i, intBuffer, i2, buffer, i3);
    }

    public static void glStencilFuncSeparate(int i, int i2, int i3, int i4) {
        nativeGlStencilFuncSeparate(i, i2, i3, i4);
    }

    public static void glStencilMaskSeparate(int i, int i2) {
        nativeGlStencilMaskSeparate(i, i2);
    }

    public static void glStencilOpSeparate(int i, int i2, int i3, int i4) {
        nativeGlStencilOpSeparate(i, i2, i3, i4);
    }

    public static void glTexParameterfv(int i, int i2, FloatBuffer floatBuffer) {
        nativeGlTexParameterfv(i, i2, floatBuffer);
    }

    public static void glTexParameteriv(int i, int i2, IntBuffer intBuffer) {
        nativeGlTexParameteriv(i, i2, intBuffer);
    }

    public static void glValidateProgram(int i) {
        nativeGlValidateProgram(i);
    }

    public static void glUniformMatrix3fv(int i, int i2, boolean z, float[] fArr) {
        nativeGlUniformMatrix3fv(i, i2, z, fArr);
    }

    public static void glTexSubImage2D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, Buffer buffer) {
        nativeGlTexSubImage2D(i, i2, i3, i4, i5, i6, i7, i8, buffer);
    }

    public static void glStencilOp(int i, int i2, int i3) {
        nativeGlStencilOp(i, i2, i3);
    }

    public static void glStencilMask(int i) {
        nativeGlStencilMask(i);
    }

    public static void glStencilFunc(int i, int i2, int i3) {
        nativeGlStencilFunc(i, i2, i3);
    }

    public static void glPixelStorei(int i, int i2) {
        nativeGlPixelStorei(i, i2);
    }

    public static void glLineWidth(float f) {
        nativeGlLineWidth(f);
    }

    public static String glGetString(int i) {
        return nativeGlGetString(i);
    }

    public static void glGenBuffers(int i, IntBuffer intBuffer) {
        nativeGlGenBuffersByBuffer(i, intBuffer);
    }

    public static void glFlush() {
        nativeGlFlush();
    }

    public static void glFinish() {
        nativeGlFinish();
    }

    public static void glDepthMask(boolean z) {
        nativeGlDepthMask(z);
    }

    public static void glDeleteBuffers(int i, IntBuffer intBuffer) {
        nativeGlDeleteBuffers(i, intBuffer);
    }

    public static void glColorMask(boolean z, boolean z2, boolean z3, boolean z4) {
        nativeGlColorMask(z, z2, z3, z4);
    }

    public static void glClearStencil(int i) {
        nativeGlClearStencil(i);
    }

    public static void glClearDepthf(float f) {
        nativeGlClearDepthf(f);
    }

    public static void glBufferSubData(int i, int i2, int i3, Buffer buffer) {
        nativeGlBufferSubData(i, i2, i3, buffer);
    }

    public static void glBlendColor(float f, float f2, float f3, float f4) {
        nativeGlBlendColor(f, f2, f3, f4);
    }

    public static void glScissor(int i, int i2, int i3, int i4) {
        nativeGlScissor(i, i2, i3, i4);
    }

    public static void glBindAttribLocation(int i, int i2, String str) {
        nativeGlBindAttribLocation(i, i2, str);
    }

    public static void glUniform1fv(int i, int i2, FloatBuffer floatBuffer) {
        nativeGlUniform1fv(i, i2, floatBuffer);
    }

    public static void glUniform2fv(int i, int i2, FloatBuffer floatBuffer) {
        nativeGlUniform2fv(i, i2, floatBuffer);
    }

    public static void glUniform2i(int i, int i2, int i3) {
        nativeGlUniform2i(i, i2, i3);
    }

    public static void glUniform2iv(int i, int i2, IntBuffer intBuffer) {
        nativeGlUniform2iv(i, i2, intBuffer);
    }

    public static void glUniform3i(int i, int i2, int i3, int i4) {
        nativeGlUniform3i(i, i2, i3, i4);
    }

    public static void glUniform3iv(int i, int i2, IntBuffer intBuffer) {
        nativeGlUniform3iv(i, i2, intBuffer);
    }

    public static void glUniform4i(int i, int i2, int i3, int i4, int i5) {
        nativeGlUniform4i(i, i2, i3, i4, i5);
    }

    public static void glUniform4iv(int i, int i2, IntBuffer intBuffer) {
        nativeGlUniform4iv(i, i2, intBuffer);
    }

    public static void glVertexAttrib1f(int i, float f) {
        nativeGlVertexAttrib1f(i, f);
    }

    public static void glVertexAttrib1fv(int i, FloatBuffer floatBuffer) {
        nativeGlVertexAttrib1fv(i, floatBuffer);
    }

    public static void glVertexAttrib2f(int i, float f, float f2) {
        nativeGlVertexAttrib2f(i, f, f2);
    }

    public static void glVertexAttrib2fv(int i, FloatBuffer floatBuffer) {
        nativeGlVertexAttrib2fv(i, floatBuffer);
    }

    public static void glVertexAttrib3f(int i, float f, float f2, float f3) {
        nativeGlVertexAttrib3f(i, f, f2, f3);
    }

    public static void glVertexAttrib3fv(int i, FloatBuffer floatBuffer) {
        nativeGlVertexAttrib3fv(i, floatBuffer);
    }

    public static void glVertexAttrib4f(int i, float f, float f2, float f3, float f4) {
        nativeGlVertexAttrib4f(i, f, f2, f3, f4);
    }

    public static void glVertexAttrib4fv(int i, FloatBuffer floatBuffer) {
        nativeGlVertexAttrib4fv(i, floatBuffer);
    }

    public static boolean glIsShader(int i) {
        return nativeGlIsShader(i);
    }
}
