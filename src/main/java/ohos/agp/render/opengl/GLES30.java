package ohos.agp.render.opengl;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class GLES30 extends GLES20 {
    public static final int GL_DEPTH24_STENCIL8 = 35056;
    public static final int GL_DEPTH_STENCIL_ATTACHMENT = 33306;
    public static final int GL_DRAW_FRAMEBUFFER = 36009;
    public static final int GL_READ_FRAMEBUFFER = 36008;
    public static final int GL_RGBA8 = 32856;

    private static native void nativeGlBeginQuery(int i, int i2);

    private static native void nativeGlBeginTransformFeedback(int i);

    private static native void nativeGlBindBufferBase(int i, int i2, int i3);

    private static native void nativeGlBindBufferRange(int i, int i2, int i3, int i4, int i5);

    private static native void nativeGlBindFramebuffer(int i, int i2);

    private static native void nativeGlBindImageTexture(int i, int i2, int i3, boolean z, int i4, int i5, int i6);

    private static native void nativeGlBindRenderbuffer(int i, int i2);

    private static native void nativeGlBindSampler(int i, int i2);

    private static native void nativeGlBindTransformFeedback(int i, int i2);

    private static native void nativeGlBindVertexArray(int i);

    private static native void nativeGlBlitFramebuffer(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10);

    private static native void nativeGlClearBufferfi(int i, int i2, float f, int i3);

    private static native void nativeGlClearBufferfv(int i, int i2, float[] fArr);

    private static native void nativeGlClearBufferiv(int i, int i2, int[] iArr);

    private static native void nativeGlClearBufferuiv(int i, int i2, int[] iArr);

    private static native int nativeGlClientWaitSync(long j, int i, long j2);

    private static native void nativeGlCompressedTexImage3D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, Buffer buffer);

    private static native void nativeGlCompressedTexSubImage3D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, Buffer buffer);

    private static native void nativeGlCopyBufferSubData(int i, int i2, int i3, int i4, int i5);

    private static native void nativeGlCopyTexSubImage3D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9);

    private static native void nativeGlDeleteQueries(int i, IntBuffer intBuffer);

    private static native void nativeGlDeleteSamplers(int i, int[] iArr);

    private static native void nativeGlDeleteSync(long j);

    private static native void nativeGlDeleteTransformFeedbacks(int i, int[] iArr);

    private static native void nativeGlDeleteVertexArrays(int i, int[] iArr);

    private static native void nativeGlDispatchCompute(int i, int i2, int i3);

    private static native void nativeGlDrawArraysInstanced(int i, int i2, int i3, int i4);

    private static native void nativeGlDrawBuffers(int i, int[] iArr);

    private static native void nativeGlDrawElementsInstanced(int i, int i2, int i3, Buffer buffer, int i4);

    private static native void nativeGlDrawRangeElements(int i, int i2, int i3, int i4, int i5, Buffer buffer);

    private static native void nativeGlEndQuery(int i);

    private static native void nativeGlEndTransformFeedback();

    private static native long nativeGlFenceSync(int i, int i2);

    private static native void nativeGlFlushMappedBufferRange(int i, int i2, int i3);

    private static native void nativeGlFramebufferTextureLayer(int i, int i2, int i3, int i4, int i5);

    private static native void nativeGlGenQueries(int i, IntBuffer intBuffer);

    private static native void nativeGlGenSamplers(int i, int[] iArr);

    private static native void nativeGlGenTransformFeedbacks(int i, int[] iArr);

    private static native void nativeGlGenVertexArrays(int i, int[] iArr);

    private static native void nativeGlGetActiveUniformBlockName(int i, int i2, int i3, int[] iArr, byte[] bArr);

    private static native String nativeGlGetActiveUniformBlockNameWithProgramAndUniformBlockIndex(int i, int i2);

    private static native void nativeGlGetActiveUniformBlockiv(int i, int i2, int i3, int[] iArr);

    private static native void nativeGlGetActiveUniformsiv(int i, int i2, int[] iArr, int i3, int[] iArr2);

    private static native void nativeGlGetBufferParameteri64v(int i, int i2, long[] jArr);

    private static native void nativeGlGetBufferPointerv(int i, int i2, Buffer buffer);

    private static native int nativeGlGetFragDataLocation(int i, String str);

    private static native void nativeGlGetInteger64iV(int i, int i2, long[] jArr);

    private static native void nativeGlGetInteger64v(int i, long[] jArr);

    private static native void nativeGlGetIntegerIndexv(int i, int i2, IntBuffer intBuffer);

    private static native void nativeGlGetInternalformativ(int i, int i2, int i3, int i4, int[] iArr);

    private static native void nativeGlGetProgramBinary(int i, int i2, int[] iArr, int[] iArr2, Buffer buffer);

    private static native void nativeGlGetQueryObjectuiv(int i, int i2, IntBuffer intBuffer);

    private static native void nativeGlGetQueryiv(int i, int i2, IntBuffer intBuffer);

    private static native void nativeGlGetSamplerParameterfv(int i, int i2, float[] fArr);

    private static native void nativeGlGetSamplerParameteriv(int i, int i2, int[] iArr);

    private static native String nativeGlGetStringi(int i, int i2);

    private static native void nativeGlGetSynciv(long j, int i, int i2, int[] iArr, int[] iArr2);

    private static native void nativeGlGetTransformFeedbackVarying(int i, int i2, int i3, int[] iArr, int[] iArr2, int[] iArr3, byte[] bArr);

    private static native int nativeGlGetUniformBlockIndex(int i, String str);

    private static native void nativeGlGetUniformIndices(int i, String[] strArr, int[] iArr);

    private static native void nativeGlGetUniformuiv(int i, int i2, int[] iArr);

    private static native void nativeGlGetVertexAttribIiv(int i, int i2, int[] iArr);

    private static native void nativeGlGetVertexAttribIuiv(int i, int i2, int[] iArr);

    private static native void nativeGlInvalidateFramebuffer(int i, int i2, int[] iArr);

    private static native void nativeGlInvalidateSubFramebuffer(int i, int i2, int[] iArr, int i3, int i4, int i5, int i6);

    private static native boolean nativeGlIsQuery(int i);

    private static native boolean nativeGlIsSampler(int i);

    private static native boolean nativeGlIsSync(long j);

    private static native boolean nativeGlIsTransformFeedback(int i);

    private static native boolean nativeGlIsVertexArray(int i);

    private static native Buffer nativeGlMapBufferRange(int i, int i2, int i3, int i4);

    private static native void nativeGlMemoryBarrier(int i);

    private static native void nativeGlPauseTransformFeedback();

    private static native void nativeGlProgramBinary(int i, int i2, Buffer buffer, int i3);

    private static native void nativeGlProgramParameteri(int i, int i2, int i3);

    private static native void nativeGlReadBuffer(int i);

    private static native void nativeGlReadPixels(int i, int i2, int i3, int i4, int i5, int i6, int i7);

    private static native void nativeGlRenderbufferStorageMultisample(int i, int i2, int i3, int i4, int i5);

    private static native void nativeGlResumeTransformFeedback();

    private static native void nativeGlSamplerParameterf(int i, int i2, float f);

    private static native void nativeGlSamplerParameterfv(int i, int i2, float[] fArr);

    private static native void nativeGlSamplerParameteri(int i, int i2, int i3);

    private static native void nativeGlSamplerParameteriv(int i, int i2, int[] iArr);

    private static native void nativeGlTexImage3D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, Buffer buffer);

    private static native void nativeGlTexStorage2D(int i, int i2, int i3, int i4, int i5);

    private static native void nativeGlTexStorage3D(int i, int i2, int i3, int i4, int i5, int i6);

    private static native void nativeGlTexSubImage3D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, Buffer buffer);

    private static native void nativeGlTransformFeedbackVaryings(int i, int i2, String[] strArr, int i3);

    private static native void nativeGlUniform1ui(int i, int i2);

    private static native void nativeGlUniform1uiv(int i, int i2, int[] iArr);

    private static native void nativeGlUniform2ui(int i, int i2, int i3);

    private static native void nativeGlUniform2uiv(int i, int i2, int[] iArr);

    private static native void nativeGlUniform3ui(int i, int i2, int i3, int i4);

    private static native void nativeGlUniform3uiv(int i, int i2, int[] iArr);

    private static native void nativeGlUniform4ui(int i, int i2, int i3, int i4, int i5);

    private static native void nativeGlUniform4uiv(int i, int i2, int[] iArr);

    private static native void nativeGlUniformBlockBinding(int i, int i2, int i3);

    private static native void nativeGlUniformMatrix2x3fv(int i, int i2, boolean z, FloatBuffer floatBuffer);

    private static native void nativeGlUniformMatrix2x3fvWithArray(int i, int i2, boolean z, float[] fArr);

    private static native void nativeGlUniformMatrix2x4fv(int i, int i2, boolean z, FloatBuffer floatBuffer);

    private static native void nativeGlUniformMatrix2x4fvWithArray(int i, int i2, boolean z, float[] fArr);

    private static native void nativeGlUniformMatrix3x2fv(int i, int i2, boolean z, FloatBuffer floatBuffer);

    private static native void nativeGlUniformMatrix3x2fvWithArray(int i, int i2, boolean z, float[] fArr);

    private static native void nativeGlUniformMatrix3x4fv(int i, int i2, boolean z, FloatBuffer floatBuffer);

    private static native void nativeGlUniformMatrix3x4fvWithArray(int i, int i2, boolean z, float[] fArr);

    private static native void nativeGlUniformMatrix4x2fv(int i, int i2, boolean z, FloatBuffer floatBuffer);

    private static native void nativeGlUniformMatrix4x2fvWithArray(int i, int i2, boolean z, float[] fArr);

    private static native void nativeGlUniformMatrix4x3fv(int i, int i2, boolean z, FloatBuffer floatBuffer);

    private static native void nativeGlUniformMatrix4x3fvWithArray(int i, int i2, boolean z, float[] fArr);

    private static native boolean nativeGlUnmapBuffer(int i);

    private static native void nativeGlVertexAttribDivisor(int i, int i2);

    private static native void nativeGlVertexAttribI4i(int i, int i2, int i3, int i4, int i5);

    private static native void nativeGlVertexAttribI4iv(int i, int[] iArr);

    private static native void nativeGlVertexAttribI4ui(int i, int i2, int i3, int i4, int i5);

    private static native void nativeGlVertexAttribI4uiv(int i, int[] iArr);

    private static native void nativeGlVertexAttribIPointer(int i, int i2, int i3, int i4, int i5);

    private static native void nativeGlVertexAttribIPointerWithBuffer(int i, int i2, int i3, int i4, Buffer buffer);

    private static native void nativeGlWaitSync(long j, int i, long j2);

    public static void glBindFramebuffer(int i, int i2) {
        nativeGlBindFramebuffer(i, i2);
    }

    public static void glBindRenderbuffer(int i, int i2) {
        nativeGlBindRenderbuffer(i, i2);
    }

    public static void glRenderbufferStorageMultisample(int i, int i2, int i3, int i4, int i5) {
        nativeGlRenderbufferStorageMultisample(i, i2, i3, i4, i5);
    }

    public static void glBlitFramebuffer(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        nativeGlBlitFramebuffer(i, i2, i3, i4, i5, i6, i7, i8, i9, i10);
    }

    public static void glDrawBuffers(int i, int[] iArr) {
        nativeGlDrawBuffers(i, iArr);
    }

    public static void glGenVertexArrays(int i, int[] iArr) {
        nativeGlGenVertexArrays(i, iArr);
    }

    public static void glBindVertexArray(int i) {
        nativeGlBindVertexArray(i);
    }

    public static void glWaitSync(long j, int i, long j2) {
        nativeGlWaitSync(j, i, j2);
    }

    public static void glTransformFeedbackVaryings(int i, int i2, String[] strArr, int i3) {
        nativeGlTransformFeedbackVaryings(i, i2, strArr, i3);
    }

    public static void glReadPixels(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        nativeGlReadPixels(i, i2, i3, i4, i5, i6, i7);
    }

    public static void glReadBuffer(int i) {
        nativeGlReadBuffer(i);
    }

    public static Buffer glMapBufferRange(int i, int i2, int i3, int i4) {
        return nativeGlMapBufferRange(i, i2, i3, i4);
    }

    public static long glFenceSync(int i, int i2) {
        return nativeGlFenceSync(i, i2);
    }

    public static void glBeginTransformFeedback(int i) {
        nativeGlBeginTransformFeedback(i);
    }

    public static void glEndTransformFeedback() {
        nativeGlEndTransformFeedback();
    }

    public static void glDeleteVertexArrays(int i, int[] iArr) {
        nativeGlDeleteVertexArrays(i, iArr);
    }

    public static void glBindBufferBase(int i, int i2, int i3) {
        nativeGlBindBufferBase(i, i2, i3);
    }

    public static boolean glUnmapBuffer(int i) {
        return nativeGlUnmapBuffer(i);
    }

    public static void glDrawRangeElements(int i, int i2, int i3, int i4, int i5, Buffer buffer) {
        nativeGlDrawRangeElements(i, i2, i3, i4, i5, buffer);
    }

    public static void glTexImage3D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, Buffer buffer) {
        nativeGlTexImage3D(i, i2, i3, i4, i5, i6, i7, i8, i9, buffer);
    }

    public static void glTexSubImage3D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, Buffer buffer) {
        nativeGlTexSubImage3D(i, i2, i3, i4, i5, i6, i7, i8, i9, i10, buffer);
    }

    public static void glCopyTexSubImage3D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        nativeGlCopyTexSubImage3D(i, i2, i3, i4, i5, i6, i7, i8, i9);
    }

    public static void glCompressedTexImage3D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, Buffer buffer) {
        nativeGlCompressedTexImage3D(i, i2, i3, i4, i5, i6, i7, i8, buffer);
    }

    public static void glCompressedTexSubImage3D(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, Buffer buffer) {
        nativeGlCompressedTexSubImage3D(i, i2, i3, i4, i5, i6, i7, i8, i9, i10, buffer);
    }

    public static void glGenQueries(int i, IntBuffer intBuffer) {
        nativeGlGenQueries(i, intBuffer);
    }

    public static void glDeleteQueries(int i, IntBuffer intBuffer) {
        nativeGlDeleteQueries(i, intBuffer);
    }

    public static boolean glIsQuery(int i) {
        return nativeGlIsQuery(i);
    }

    public static void glBeginQuery(int i, int i2) {
        nativeGlBeginQuery(i, i2);
    }

    public static void glEndQuery(int i) {
        nativeGlEndQuery(i);
    }

    public static void glGetQueryiv(int i, int i2, IntBuffer intBuffer) {
        nativeGlGetQueryiv(i, i2, intBuffer);
    }

    public static void glGetQueryObjectuiv(int i, int i2, IntBuffer intBuffer) {
        nativeGlGetQueryObjectuiv(i, i2, intBuffer);
    }

    public static void glGetBufferPointerv(int i, int i2, Buffer buffer) {
        nativeGlGetBufferPointerv(i, i2, buffer);
    }

    public static void glBindBufferRange(int i, int i2, int i3, int i4, int i5) {
        nativeGlBindBufferRange(i, i2, i3, i4, i5);
    }

    public static void glGetTransformFeedbackVarying(int i, int i2, int i3, int[] iArr, int[] iArr2, int[] iArr3, byte[] bArr) {
        nativeGlGetTransformFeedbackVarying(i, i2, i3, iArr, iArr2, iArr3, bArr);
    }

    public static void glVertexAttribIPointer(int i, int i2, int i3, int i4, int i5) {
        nativeGlVertexAttribIPointer(i, i2, i3, i4, i5);
    }

    public static void glGetVertexAttribIiv(int i, int i2, int[] iArr) {
        nativeGlGetVertexAttribIiv(i, i2, iArr);
    }

    public static void glGetVertexAttribIuiv(int i, int i2, int[] iArr) {
        nativeGlGetVertexAttribIuiv(i, i2, iArr);
    }

    public static void glVertexAttribI4i(int i, int i2, int i3, int i4, int i5) {
        nativeGlVertexAttribI4i(i, i2, i3, i4, i5);
    }

    public static void glVertexAttribI4ui(int i, int i2, int i3, int i4, int i5) {
        nativeGlVertexAttribI4ui(i, i2, i3, i4, i5);
    }

    public static void glVertexAttribI4iv(int i, int[] iArr) {
        nativeGlVertexAttribI4iv(i, iArr);
    }

    public static void glVertexAttribI4uiv(int i, int[] iArr) {
        nativeGlVertexAttribI4uiv(i, iArr);
    }

    public static void glGetUniformuiv(int i, int i2, int[] iArr) {
        nativeGlGetUniformuiv(i, i2, iArr);
    }

    public static int glGetFragDataLocation(int i, String str) {
        return nativeGlGetFragDataLocation(i, str);
    }

    public static void glUniform1ui(int i, int i2) {
        nativeGlUniform1ui(i, i2);
    }

    public static void glUniform2ui(int i, int i2, int i3) {
        nativeGlUniform2ui(i, i2, i3);
    }

    public static void glUniformMatrix2x3fv(int i, int i2, boolean z, FloatBuffer floatBuffer) {
        nativeGlUniformMatrix2x3fv(i, i2, z, floatBuffer);
    }

    public static void glUniform3ui(int i, int i2, int i3, int i4) {
        nativeGlUniform3ui(i, i2, i3, i4);
    }

    public static void glUniformMatrix3x2fv(int i, int i2, boolean z, FloatBuffer floatBuffer) {
        nativeGlUniformMatrix3x2fv(i, i2, z, floatBuffer);
    }

    public static void glUniformMatrix2x4fv(int i, int i2, boolean z, FloatBuffer floatBuffer) {
        nativeGlUniformMatrix2x4fv(i, i2, z, floatBuffer);
    }

    public static void glUniform4ui(int i, int i2, int i3, int i4, int i5) {
        nativeGlUniform4ui(i, i2, i3, i4, i5);
    }

    public static void glUniform1uiv(int i, int i2, int[] iArr) {
        nativeGlUniform1uiv(i, i2, iArr);
    }

    public static void glUniformMatrix4x2fv(int i, int i2, boolean z, FloatBuffer floatBuffer) {
        nativeGlUniformMatrix4x2fv(i, i2, z, floatBuffer);
    }

    public static void glUniform2uiv(int i, int i2, int[] iArr) {
        nativeGlUniform2uiv(i, i2, iArr);
    }

    public static void glUniformMatrix3x4fv(int i, int i2, boolean z, FloatBuffer floatBuffer) {
        nativeGlUniformMatrix3x4fv(i, i2, z, floatBuffer);
    }

    public static void glUniform3uiv(int i, int i2, int[] iArr) {
        nativeGlUniform3uiv(i, i2, iArr);
    }

    public static void glUniformMatrix4x3fv(int i, int i2, boolean z, FloatBuffer floatBuffer) {
        nativeGlUniformMatrix4x3fv(i, i2, z, floatBuffer);
    }

    public static void glFramebufferTextureLayer(int i, int i2, int i3, int i4, int i5) {
        nativeGlFramebufferTextureLayer(i, i2, i3, i4, i5);
    }

    public static void glFlushMappedBufferRange(int i, int i2, int i3) {
        nativeGlFlushMappedBufferRange(i, i2, i3);
    }

    public static boolean glIsVertexArray(int i) {
        return nativeGlIsVertexArray(i);
    }

    public static void glGetIntegerIndexv(int i, int i2, IntBuffer intBuffer) {
        nativeGlGetIntegerIndexv(i, i2, intBuffer);
    }

    public static void glSamplerParameteri(int i, int i2, int i3) {
        nativeGlSamplerParameteri(i, i2, i3);
    }

    public static void glSamplerParameteriv(int i, int i2, int[] iArr) {
        nativeGlSamplerParameteriv(i, i2, iArr);
    }

    public static void glSamplerParameterf(int i, int i2, float f) {
        nativeGlSamplerParameterf(i, i2, f);
    }

    public static void glSamplerParameterfv(int i, int i2, float[] fArr) {
        nativeGlSamplerParameterfv(i, i2, fArr);
    }

    public static void glGetSamplerParameteriv(int i, int i2, int[] iArr) {
        nativeGlGetSamplerParameteriv(i, i2, iArr);
    }

    public static void glGetSamplerParameterfv(int i, int i2, float[] fArr) {
        nativeGlGetSamplerParameterfv(i, i2, fArr);
    }

    public static void glVertexAttribDivisor(int i, int i2) {
        nativeGlVertexAttribDivisor(i, i2);
    }

    public static void glBindTransformFeedback(int i, int i2) {
        nativeGlBindTransformFeedback(i, i2);
    }

    public static void glDeleteTransformFeedbacks(int i, int[] iArr) {
        nativeGlDeleteTransformFeedbacks(i, iArr);
    }

    public static void glGenTransformFeedbacks(int i, int[] iArr) {
        nativeGlGenTransformFeedbacks(i, iArr);
    }

    public static boolean glIsTransformFeedback(int i) {
        return nativeGlIsTransformFeedback(i);
    }

    public static void glPauseTransformFeedback() {
        nativeGlPauseTransformFeedback();
    }

    public static void glResumeTransformFeedback() {
        nativeGlResumeTransformFeedback();
    }

    public static void glGetProgramBinary(int i, int i2, int[] iArr, int[] iArr2, Buffer buffer) {
        nativeGlGetProgramBinary(i, i2, iArr, iArr2, buffer);
    }

    public static void glProgramBinary(int i, int i2, Buffer buffer, int i3) {
        nativeGlProgramBinary(i, i2, buffer, i3);
    }

    public static void glProgramParameteri(int i, int i2, int i3) {
        nativeGlProgramParameteri(i, i2, i3);
    }

    public static void glInvalidateFramebuffer(int i, int i2, int[] iArr) {
        nativeGlInvalidateFramebuffer(i, i2, iArr);
    }

    public static void glInvalidateSubFramebuffer(int i, int i2, int[] iArr, int i3, int i4, int i5, int i6) {
        nativeGlInvalidateSubFramebuffer(i, i2, iArr, i3, i4, i5, i6);
    }

    public static void glTexStorage2D(int i, int i2, int i3, int i4, int i5) {
        nativeGlTexStorage2D(i, i2, i3, i4, i5);
    }

    public static void glTexStorage3D(int i, int i2, int i3, int i4, int i5, int i6) {
        nativeGlTexStorage3D(i, i2, i3, i4, i5, i6);
    }

    public static void glGetInternalformativ(int i, int i2, int i3, int i4, int[] iArr) {
        nativeGlGetInternalformativ(i, i2, i3, i4, iArr);
    }

    public static void glUniform4uiv(int i, int i2, int[] iArr) {
        nativeGlUniform4uiv(i, i2, iArr);
    }

    public static void glClearBufferiv(int i, int i2, int[] iArr) {
        nativeGlClearBufferiv(i, i2, iArr);
    }

    public static void glClearBufferuiv(int i, int i2, int[] iArr) {
        nativeGlClearBufferuiv(i, i2, iArr);
    }

    public static void glClearBufferfv(int i, int i2, float[] fArr) {
        nativeGlClearBufferfv(i, i2, fArr);
    }

    public static void glClearBufferfi(int i, int i2, float f, int i3) {
        nativeGlClearBufferfi(i, i2, f, i3);
    }

    public static String glGetStringi(int i, int i2) {
        return nativeGlGetStringi(i, i2);
    }

    public static void glCopyBufferSubData(int i, int i2, int i3, int i4, int i5) {
        nativeGlCopyBufferSubData(i, i2, i3, i4, i5);
    }

    public static void glGetUniformIndices(int i, String[] strArr, int[] iArr) {
        nativeGlGetUniformIndices(i, strArr, iArr);
    }

    public static void glGetActiveUniformsiv(int i, int i2, int[] iArr, int i3, int[] iArr2) {
        nativeGlGetActiveUniformsiv(i, i2, iArr, i3, iArr2);
    }

    public static int glGetUniformBlockIndex(int i, String str) {
        return nativeGlGetUniformBlockIndex(i, str);
    }

    public static void glGetActiveUniformBlockiv(int i, int i2, int i3, int[] iArr) {
        nativeGlGetActiveUniformBlockiv(i, i2, i3, iArr);
    }

    public static void glGetActiveUniformBlockName(int i, int i2, int i3, int[] iArr, byte[] bArr) {
        nativeGlGetActiveUniformBlockName(i, i2, i3, iArr, bArr);
    }

    public static void glUniformBlockBinding(int i, int i2, int i3) {
        nativeGlUniformBlockBinding(i, i2, i3);
    }

    public static void glDrawArraysInstanced(int i, int i2, int i3, int i4) {
        nativeGlDrawArraysInstanced(i, i2, i3, i4);
    }

    public static void glDrawElementsInstanced(int i, int i2, int i3, Buffer buffer, int i4) {
        nativeGlDrawElementsInstanced(i, i2, i3, buffer, i4);
    }

    public static boolean glIsSync(long j) {
        return nativeGlIsSync(j);
    }

    public static void glDeleteSync(long j) {
        nativeGlDeleteSync(j);
    }

    public static int glClientWaitSync(long j, int i, long j2) {
        return nativeGlClientWaitSync(j, i, j2);
    }

    public static void glGetInteger64v(int i, long[] jArr) {
        nativeGlGetInteger64v(i, jArr);
    }

    public static void glGetSynciv(long j, int i, int i2, int[] iArr, int[] iArr2) {
        nativeGlGetSynciv(j, i, i2, iArr, iArr2);
    }

    public static void glGetInteger64iV(int i, int i2, long[] jArr) {
        nativeGlGetInteger64iV(i, i2, jArr);
    }

    public static void glGetBufferParameteri64v(int i, int i2, long[] jArr) {
        nativeGlGetBufferParameteri64v(i, i2, jArr);
    }

    public static void glGenSamplers(int i, int[] iArr) {
        nativeGlGenSamplers(i, iArr);
    }

    public static void glDeleteSamplers(int i, int[] iArr) {
        nativeGlDeleteSamplers(i, iArr);
    }

    public static boolean glIsSampler(int i) {
        return nativeGlIsSampler(i);
    }

    public static void glBindSampler(int i, int i2) {
        nativeGlBindSampler(i, i2);
    }

    public static void glBindImageTexture(int i, int i2, int i3, boolean z, int i4, int i5, int i6) {
        nativeGlBindImageTexture(i, i2, i3, z, i4, i5, i6);
    }

    public static void glDispatchCompute(int i, int i2, int i3) {
        nativeGlDispatchCompute(i, i2, i3);
    }

    public static void glMemoryBarrier(int i) {
        nativeGlMemoryBarrier(i);
    }

    public static String glGetActiveUniformBlockName(int i, int i2) {
        return nativeGlGetActiveUniformBlockNameWithProgramAndUniformBlockIndex(i, i2);
    }

    public static void glUniformMatrix2x3fv(int i, int i2, boolean z, float[] fArr) {
        nativeGlUniformMatrix2x3fvWithArray(i, i2, z, fArr);
    }

    public static void glUniformMatrix2x4fv(int i, int i2, boolean z, float[] fArr) {
        nativeGlUniformMatrix2x4fvWithArray(i, i2, z, fArr);
    }

    public static void glUniformMatrix3x2fv(int i, int i2, boolean z, float[] fArr) {
        nativeGlUniformMatrix3x2fvWithArray(i, i2, z, fArr);
    }

    public static void glUniformMatrix3x4fv(int i, int i2, boolean z, float[] fArr) {
        nativeGlUniformMatrix3x4fvWithArray(i, i2, z, fArr);
    }

    public static void glUniformMatrix4x2fv(int i, int i2, boolean z, float[] fArr) {
        nativeGlUniformMatrix4x2fvWithArray(i, i2, z, fArr);
    }

    public static void glUniformMatrix4x3fv(int i, int i2, boolean z, float[] fArr) {
        nativeGlUniformMatrix4x3fvWithArray(i, i2, z, fArr);
    }

    public static void glVertexAttribIPointer(int i, int i2, int i3, int i4, Buffer buffer) {
        nativeGlVertexAttribIPointerWithBuffer(i, i2, i3, i4, buffer);
    }
}
