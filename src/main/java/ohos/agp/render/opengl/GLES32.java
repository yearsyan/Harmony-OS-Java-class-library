package ohos.agp.render.opengl;

import java.nio.Buffer;

public class GLES32 extends GLES31 {
    public static final int GL_BUFFER = 33504;
    public static final int GL_CLAMP_TO_BORDER = 33069;
    public static final int GL_COLORBURN = 37530;
    public static final int GL_COLORDODGE = 37529;
    public static final int GL_COMPRESSED_RGBA_ASTC_10x10 = 37819;
    public static final int GL_COMPRESSED_RGBA_ASTC_10x5 = 37816;
    public static final int GL_COMPRESSED_RGBA_ASTC_10x6 = 37817;
    public static final int GL_COMPRESSED_RGBA_ASTC_10x8 = 37818;
    public static final int GL_COMPRESSED_RGBA_ASTC_12x10 = 37820;
    public static final int GL_COMPRESSED_RGBA_ASTC_12x12 = 37821;
    public static final int GL_COMPRESSED_RGBA_ASTC_4x4 = 37808;
    public static final int GL_COMPRESSED_RGBA_ASTC_5x4 = 37809;
    public static final int GL_COMPRESSED_RGBA_ASTC_5x5 = 37810;
    public static final int GL_COMPRESSED_RGBA_ASTC_6x5 = 37811;
    public static final int GL_COMPRESSED_RGBA_ASTC_6x6 = 37812;
    public static final int GL_COMPRESSED_RGBA_ASTC_8x5 = 37813;
    public static final int GL_COMPRESSED_RGBA_ASTC_8x6 = 37814;
    public static final int GL_COMPRESSED_RGBA_ASTC_8x8 = 37815;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x10 = 37851;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x5 = 37848;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x6 = 37849;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x8 = 37850;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_12x10 = 37852;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_12x12 = 37853;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_4x4 = 37840;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_5x4 = 37841;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_5x5 = 37842;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_6x5 = 37843;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_6x6 = 37844;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_8x5 = 37845;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_8x6 = 37846;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_8x8 = 37847;
    public static final int GL_CONTEXT_FLAGS = 33310;
    public static final int GL_CONTEXT_FLAG_DEBUG_BIT = 2;
    public static final int GL_CONTEXT_FLAG_ROBUST_ACCESS_BIT = 4;
    public static final int GL_CONTEXT_LOST = 1287;
    public static final int GL_DARKEN = 37527;
    public static final int GL_DEBUG_CALLBACK_FUNCTION = 33348;
    public static final int GL_DEBUG_CALLBACK_USER_PARAM = 33349;
    public static final int GL_DEBUG_GROUP_STACK_DEPTH = 33389;
    public static final int GL_DEBUG_LOGGED_MESSAGES = 37189;
    public static final int GL_DEBUG_NEXT_LOGGED_MESSAGE_LENGTH = 33347;
    public static final int GL_DEBUG_OUTPUT = 37600;
    public static final int GL_DEBUG_OUTPUT_SYNCHRONOUS = 33346;
    public static final int GL_DEBUG_SEVERITY_HIGH = 37190;
    public static final int GL_DEBUG_SEVERITY_LOW = 37192;
    public static final int GL_DEBUG_SEVERITY_MEDIUM = 37191;
    public static final int GL_DEBUG_SEVERITY_NOTIFICATION = 33387;
    public static final int GL_DEBUG_SOURCE_API = 33350;
    public static final int GL_DEBUG_SOURCE_APPLICATION = 33354;
    public static final int GL_DEBUG_SOURCE_OTHER = 33355;
    public static final int GL_DEBUG_SOURCE_SHADER_COMPILER = 33352;
    public static final int GL_DEBUG_SOURCE_THIRD_PARTY = 33353;
    public static final int GL_DEBUG_SOURCE_WINDOW_SYSTEM = 33351;
    public static final int GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR = 33357;
    public static final int GL_DEBUG_TYPE_ERROR = 33356;
    public static final int GL_DEBUG_TYPE_MARKER = 33384;
    public static final int GL_DEBUG_TYPE_OTHER = 33361;
    public static final int GL_DEBUG_TYPE_PERFORMANCE = 33360;
    public static final int GL_DEBUG_TYPE_POP_GROUP = 33386;
    public static final int GL_DEBUG_TYPE_PORTABILITY = 33359;
    public static final int GL_DEBUG_TYPE_PUSH_GROUP = 33385;
    public static final int GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR = 33358;
    public static final int GL_DIFFERENCE = 37534;
    public static final int GL_EXCLUSION = 37536;
    public static final int GL_FIRST_VERTEX_CONVENTION = 36429;
    public static final int GL_FRACTIONAL_EVEN = 36476;
    public static final int GL_FRACTIONAL_ODD = 36475;
    public static final int GL_FRAGMENT_INTERPOLATION_OFFSET_BITS = 36445;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_LAYERED = 36263;
    public static final int GL_FRAMEBUFFER_DEFAULT_LAYERS = 37650;
    public static final int GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS = 36264;
    public static final int GL_GEOMETRY_INPUT_TYPE = 35095;
    public static final int GL_GEOMETRY_OUTPUT_TYPE = 35096;
    public static final int GL_GEOMETRY_SHADER = 36313;
    public static final int GL_GEOMETRY_SHADER_BIT = 4;
    public static final int GL_GEOMETRY_SHADER_INVOCATIONS = 34943;
    public static final int GL_GEOMETRY_VERTICES_OUT = 35094;
    public static final int GL_GUILTY_CONTEXT_RESET = 33363;
    public static final int GL_HARDLIGHT = 37531;
    public static final int GL_HSL_COLOR = 37551;
    public static final int GL_HSL_HUE = 37549;
    public static final int GL_HSL_LUMINOSITY = 37552;
    public static final int GL_HSL_SATURATION = 37550;
    public static final int GL_IMAGE_BUFFER = 36945;
    public static final int GL_IMAGE_CUBE_MAP_ARRAY = 36948;
    public static final int GL_INNOCENT_CONTEXT_RESET = 33364;
    public static final int GL_INT_IMAGE_BUFFER = 36956;
    public static final int GL_INT_IMAGE_CUBE_MAP_ARRAY = 36959;
    public static final int GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY = 37132;
    public static final int GL_INT_SAMPLER_BUFFER = 36304;
    public static final int GL_INT_SAMPLER_CUBE_MAP_ARRAY = 36878;
    public static final int GL_ISOLINES = 36474;
    public static final int GL_IS_PER_PATCH = 37607;
    public static final int GL_LAST_VERTEX_CONVENTION = 36430;
    public static final int GL_LAYER_PROVOKING_VERTEX = 33374;
    public static final int GL_LIGHTEN = 37528;
    public static final int GL_LINES_ADJACENCY = 10;
    public static final int GL_LINE_STRIP_ADJACENCY = 11;
    public static final int GL_LOSE_CONTEXT_ON_RESET = 33362;
    public static final int GL_MAX_COMBINED_GEOMETRY_UNIFORM_COMPONENTS = 35378;
    public static final int GL_MAX_COMBINED_TESS_CONTROL_UNIFORM_COMPONENTS = 36382;
    public static final int GL_MAX_COMBINED_TESS_EVALUATION_UNIFORM_COMPONENTS = 36383;
    public static final int GL_MAX_DEBUG_GROUP_STACK_DEPTH = 33388;
    public static final int GL_MAX_DEBUG_LOGGED_MESSAGES = 37188;
    public static final int GL_MAX_DEBUG_MESSAGE_LENGTH = 37187;
    public static final int GL_MAX_FRAGMENT_INTERPOLATION_OFFSET = 36444;
    public static final int GL_MAX_FRAMEBUFFER_LAYERS = 37655;
    public static final int GL_MAX_GEOMETRY_ATOMIC_COUNTERS = 37589;
    public static final int GL_MAX_GEOMETRY_ATOMIC_COUNTER_BUFFERS = 37583;
    public static final int GL_MAX_GEOMETRY_IMAGE_UNIFORMS = 37069;
    public static final int GL_MAX_GEOMETRY_INPUT_COMPONENTS = 37155;
    public static final int GL_MAX_GEOMETRY_OUTPUT_COMPONENTS = 37156;
    public static final int GL_MAX_GEOMETRY_OUTPUT_VERTICES = 36320;
    public static final int GL_MAX_GEOMETRY_SHADER_INVOCATIONS = 36442;
    public static final int GL_MAX_GEOMETRY_SHADER_STORAGE_BLOCKS = 37079;
    public static final int GL_MAX_GEOMETRY_TEXTURE_IMAGE_UNITS = 35881;
    public static final int GL_MAX_GEOMETRY_TOTAL_OUTPUT_COMPONENTS = 36321;
    public static final int GL_MAX_GEOMETRY_UNIFORM_BLOCKS = 35372;
    public static final int GL_MAX_GEOMETRY_UNIFORM_COMPONENTS = 36319;
    public static final int GL_MAX_LABEL_LENGTH = 33512;
    public static final int GL_MAX_PATCH_VERTICES = 36477;
    public static final int GL_MAX_TESS_CONTROL_ATOMIC_COUNTERS = 37587;
    public static final int GL_MAX_TESS_CONTROL_ATOMIC_COUNTER_BUFFERS = 37581;
    public static final int GL_MAX_TESS_CONTROL_IMAGE_UNIFORMS = 37067;
    public static final int GL_MAX_TESS_CONTROL_INPUT_COMPONENTS = 34924;
    public static final int GL_MAX_TESS_CONTROL_OUTPUT_COMPONENTS = 36483;
    public static final int GL_MAX_TESS_CONTROL_SHADER_STORAGE_BLOCKS = 37080;
    public static final int GL_MAX_TESS_CONTROL_TEXTURE_IMAGE_UNITS = 36481;
    public static final int GL_MAX_TESS_CONTROL_TOTAL_OUTPUT_COMPONENTS = 36485;
    public static final int GL_MAX_TESS_CONTROL_UNIFORM_BLOCKS = 36489;
    public static final int GL_MAX_TESS_CONTROL_UNIFORM_COMPONENTS = 36479;
    public static final int GL_MAX_TESS_EVALUATION_ATOMIC_COUNTERS = 37588;
    public static final int GL_MAX_TESS_EVALUATION_ATOMIC_COUNTER_BUFFERS = 37582;
    public static final int GL_MAX_TESS_EVALUATION_IMAGE_UNIFORMS = 37068;
    public static final int GL_MAX_TESS_EVALUATION_INPUT_COMPONENTS = 34925;
    public static final int GL_MAX_TESS_EVALUATION_OUTPUT_COMPONENTS = 36486;
    public static final int GL_MAX_TESS_EVALUATION_SHADER_STORAGE_BLOCKS = 37081;
    public static final int GL_MAX_TESS_EVALUATION_TEXTURE_IMAGE_UNITS = 36482;
    public static final int GL_MAX_TESS_EVALUATION_UNIFORM_BLOCKS = 36490;
    public static final int GL_MAX_TESS_EVALUATION_UNIFORM_COMPONENTS = 36480;
    public static final int GL_MAX_TESS_GEN_LEVEL = 36478;
    public static final int GL_MAX_TESS_PATCH_COMPONENTS = 36484;
    public static final int GL_MAX_TEXTURE_BUFFER_SIZE = 35883;
    public static final int GL_MIN_FRAGMENT_INTERPOLATION_OFFSET = 36443;
    public static final int GL_MIN_SAMPLE_SHADING_VALUE = 35895;
    public static final int GL_MULTIPLY = 37524;
    public static final int GL_MULTISAMPLE_LINE_WIDTH_GRANULARITY = 37762;
    public static final int GL_MULTISAMPLE_LINE_WIDTH_RANGE = 37761;
    public static final int GL_NO_RESET_NOTIFICATION = 33377;
    public static final int GL_OVERLAY = 37526;
    public static final int GL_PATCHES = 14;
    public static final int GL_PATCH_VERTICES = 36466;
    public static final int GL_PRIMITIVES_GENERATED = 35975;
    public static final int GL_PRIMITIVE_BOUNDING_BOX = 37566;
    public static final int GL_PRIMITIVE_RESTART_FOR_PATCHES_SUPPORTED = 33313;
    public static final int GL_PROGRAM = 33506;
    public static final int GL_PROGRAM_PIPELINE = 33508;
    public static final int GL_QUADS = 7;
    public static final int GL_QUERY = 33507;
    public static final int GL_REFERENCED_BY_GEOMETRY_SHADER = 37641;
    public static final int GL_REFERENCED_BY_TESS_CONTROL_SHADER = 37639;
    public static final int GL_REFERENCED_BY_TESS_EVALUATION_SHADER = 37640;
    public static final int GL_RESET_NOTIFICATION_STRATEGY = 33366;
    public static final int GL_SAMPLER = 33510;
    public static final int GL_SAMPLER_2D_MULTISAMPLE_ARRAY = 37131;
    public static final int GL_SAMPLER_BUFFER = 36290;
    public static final int GL_SAMPLER_CUBE_MAP_ARRAY = 36876;
    public static final int GL_SAMPLER_CUBE_MAP_ARRAY_SHADOW = 36877;
    public static final int GL_SAMPLE_SHADING = 35894;
    public static final int GL_SCREEN = 37525;
    public static final int GL_SHADER = 33505;
    public static final int GL_SOFTLIGHT = 37532;
    public static final int GL_STACK_OVERFLOW = 1283;
    public static final int GL_STACK_UNDERFLOW = 1284;
    public static final int GL_TESS_CONTROL_OUTPUT_VERTICES = 36469;
    public static final int GL_TESS_CONTROL_SHADER = 36488;
    public static final int GL_TESS_CONTROL_SHADER_BIT = 8;
    public static final int GL_TESS_EVALUATION_SHADER = 36487;
    public static final int GL_TESS_EVALUATION_SHADER_BIT = 16;
    public static final int GL_TESS_GEN_MODE = 36470;
    public static final int GL_TESS_GEN_POINT_MODE = 36473;
    public static final int GL_TESS_GEN_SPACING = 36471;
    public static final int GL_TESS_GEN_VERTEX_ORDER = 36472;
    public static final int GL_TEXTURE_2D_MULTISAMPLE_ARRAY = 37122;
    public static final int GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY = 37125;
    public static final int GL_TEXTURE_BINDING_BUFFER = 35884;
    public static final int GL_TEXTURE_BINDING_CUBE_MAP_ARRAY = 36874;
    public static final int GL_TEXTURE_BORDER_COLOR = 4100;
    public static final int GL_TEXTURE_BUFFER = 35882;
    public static final int GL_TEXTURE_BUFFER_BINDING = 35882;
    public static final int GL_TEXTURE_BUFFER_DATA_STORE_BINDING = 35885;
    public static final int GL_TEXTURE_BUFFER_OFFSET = 37277;
    public static final int GL_TEXTURE_BUFFER_OFFSET_ALIGNMENT = 37279;
    public static final int GL_TEXTURE_BUFFER_SIZE = 37278;
    public static final int GL_TEXTURE_CUBE_MAP_ARRAY = 36873;
    public static final int GL_TRIANGLES_ADJACENCY = 12;
    public static final int GL_TRIANGLE_STRIP_ADJACENCY = 13;
    public static final int GL_UNDEFINED_VERTEX = 33376;
    public static final int GL_UNKNOWN_CONTEXT_RESET = 33365;
    public static final int GL_UNSIGNED_INT_IMAGE_BUFFER = 36967;
    public static final int GL_UNSIGNED_INT_IMAGE_CUBE_MAP_ARRAY = 36970;
    public static final int GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY = 37133;
    public static final int GL_UNSIGNED_INT_SAMPLER_BUFFER = 36312;
    public static final int GL_UNSIGNED_INT_SAMPLER_CUBE_MAP_ARRAY = 36879;
    public static final int GL_VERTEX_ARRAY = 32884;

    private static native void nativeGlBlendBarrier();

    private static native void nativeGlBlendEquationSeparatei(int i, int i2, int i3);

    private static native void nativeGlBlendEquationi(int i, int i2);

    private static native void nativeGlBlendFuncSeparatei(int i, int i2, int i3, int i4, int i5);

    private static native void nativeGlBlendFunci(int i, int i2, int i3);

    private static native void nativeGlColorMaski(int i, boolean z, boolean z2, boolean z3, boolean z4);

    private static native void nativeGlCopyImageSubData(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15);

    private static native void nativeGlDebugMessageControlArray(int i, int i2, int i3, int i4, int[] iArr, boolean z);

    private static native void nativeGlDebugMessageInsert(int i, int i2, int i3, int i4, int i5, String str);

    private static native void nativeGlDisablei(int i, int i2);

    private static native void nativeGlDrawElementsBaseVertex(int i, int i2, int i3, Buffer buffer, int i4);

    private static native void nativeGlDrawElementsInstancedBaseVertex(int i, int i2, int i3, int i4, int i5, int i6);

    private static native void nativeGlDrawRangeElementsBaseVertex(int i, int i2, int i3, int i4, int i5, Buffer buffer, int i6);

    private static native void nativeGlEnablei(int i, int i2);

    private static native void nativeGlFramebufferTexture(int i, int i2, int i3, int i4);

    private static native int nativeGlGetGraphicsResetStatus();

    private static native void nativeGlGetSamplerParameterIiv(int i, int i2, int[] iArr);

    private static native void nativeGlGetSamplerParameterIuiv(int i, int i2, int[] iArr);

    private static native void nativeGlGetTexParameterIiv(int i, int i2, int[] iArr);

    private static native void nativeGlGetTexParameterIuiv(int i, int i2, int[] iArr);

    private static native void nativeGlGetnUniformfv(int i, int i2, int i3, float[] fArr);

    private static native void nativeGlGetnUniformiv(int i, int i2, int i3, int[] iArr);

    private static native void nativeGlGetnUniformuiv(int i, int i2, int i3, int[] iArr);

    private static native boolean nativeGlIsEnabledi(int i, int i2);

    private static native void nativeGlMinSampleShading(float f);

    private static native void nativeGlObjectLabel(int i, int i2, int i3, String str);

    private static native void nativeGlPatchParameteri(int i, int i2);

    private static native void nativeGlPopDebugGroup();

    private static native void nativeGlPrimitiveBoundingBox(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8);

    private static native void nativeGlPushDebugGroup(int i, int i2, int i3, String str);

    private static native void nativeGlReadnPixels(int i, int i2, int i3, int i4, int i5, int i6, int i7, Buffer buffer);

    private static native void nativeGlSamplerParameterIiv(int i, int i2, int[] iArr);

    private static native void nativeGlSamplerParameterIuiv(int i, int i2, int[] iArr);

    private static native void nativeGlTexBuffer(int i, int i2, int i3);

    private static native void nativeGlTexBufferRange(int i, int i2, int i3, int i4, int i5);

    private static native void nativeGlTexParameterIiv(int i, int i2, int[] iArr);

    private static native void nativeGlTexParameterIuiv(int i, int i2, int[] iArr);

    private static native void nativeGlTexStorage3DMultisample(int i, int i2, int i3, int i4, int i5, int i6, boolean z);

    public static void glBlendBarrier() {
        nativeGlBlendBarrier();
    }

    public static void glCopyImageSubData(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15) {
        nativeGlCopyImageSubData(i, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12, i13, i14, i15);
    }

    public static void glDebugMessageControl(int i, int i2, int i3, int i4, int[] iArr, boolean z) {
        nativeGlDebugMessageControlArray(i, i2, i3, i4, iArr, z);
    }

    public static void glDebugMessageInsert(int i, int i2, int i3, int i4, int i5, String str) {
        nativeGlDebugMessageInsert(i, i2, i3, i4, i5, str);
    }

    public static void glPushDebugGroup(int i, int i2, int i3, String str) {
        nativeGlPushDebugGroup(i, i2, i3, str);
    }

    public static void glPopDebugGroup() {
        nativeGlPopDebugGroup();
    }

    public static void glObjectLabel(int i, int i2, int i3, String str) {
        nativeGlObjectLabel(i, i2, i3, str);
    }

    public static void glEnablei(int i, int i2) {
        nativeGlEnablei(i, i2);
    }

    public static void glDisablei(int i, int i2) {
        nativeGlDisablei(i, i2);
    }

    public static void glBlendEquationi(int i, int i2) {
        nativeGlBlendEquationi(i, i2);
    }

    public static void glBlendEquationSeparatei(int i, int i2, int i3) {
        nativeGlBlendEquationSeparatei(i, i2, i3);
    }

    public static void glBlendFunci(int i, int i2, int i3) {
        nativeGlBlendFunci(i, i2, i3);
    }

    public static void glBlendFuncSeparatei(int i, int i2, int i3, int i4, int i5) {
        nativeGlBlendFuncSeparatei(i, i2, i3, i4, i5);
    }

    public static void glColorMaski(int i, boolean z, boolean z2, boolean z3, boolean z4) {
        nativeGlColorMaski(i, z, z2, z3, z4);
    }

    public static boolean glIsEnabledi(int i, int i2) {
        return nativeGlIsEnabledi(i, i2);
    }

    public static void glDrawElementsBaseVertex(int i, int i2, int i3, Buffer buffer, int i4) {
        nativeGlDrawElementsBaseVertex(i, i2, i3, buffer, i4);
    }

    public static void glDrawRangeElementsBaseVertex(int i, int i2, int i3, int i4, int i5, Buffer buffer, int i6) {
        nativeGlDrawRangeElementsBaseVertex(i, i2, i3, i4, i5, buffer, i6);
    }

    public static void glDrawElementsInstancedBaseVertex(int i, int i2, int i3, int i4, int i5, int i6) {
        nativeGlDrawElementsInstancedBaseVertex(i, i2, i3, i4, i5, i6);
    }

    public static void glFramebufferTexture(int i, int i2, int i3, int i4) {
        nativeGlFramebufferTexture(i, i2, i3, i4);
    }

    public static void glPrimitiveBoundingBox(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        nativeGlPrimitiveBoundingBox(f, f2, f3, f4, f5, f6, f7, f8);
    }

    public static int glGetGraphicsResetStatus() {
        return nativeGlGetGraphicsResetStatus();
    }

    public static void glReadnPixels(int i, int i2, int i3, int i4, int i5, int i6, int i7, Buffer buffer) {
        nativeGlReadnPixels(i, i2, i3, i4, i5, i6, i7, buffer);
    }

    public static void glGetnUniformfv(int i, int i2, int i3, float[] fArr) {
        nativeGlGetnUniformfv(i, i2, i3, fArr);
    }

    public static void glGetnUniformiv(int i, int i2, int i3, int[] iArr) {
        nativeGlGetnUniformiv(i, i2, i3, iArr);
    }

    public static void glGetnUniformuiv(int i, int i2, int i3, int[] iArr) {
        nativeGlGetnUniformuiv(i, i2, i3, iArr);
    }

    public static void glMinSampleShading(float f) {
        nativeGlMinSampleShading(f);
    }

    public static void glPatchParameteri(int i, int i2) {
        nativeGlPatchParameteri(i, i2);
    }

    public static void glTexParameterIiv(int i, int i2, int[] iArr) {
        nativeGlTexParameterIiv(i, i2, iArr);
    }

    public static void glTexParameterIuiv(int i, int i2, int[] iArr) {
        nativeGlTexParameterIuiv(i, i2, iArr);
    }

    public static void glGetTexParameterIiv(int i, int i2, int[] iArr) {
        nativeGlGetTexParameterIiv(i, i2, iArr);
    }

    public static void glGetTexParameterIuiv(int i, int i2, int[] iArr) {
        nativeGlGetTexParameterIuiv(i, i2, iArr);
    }

    public static void glSamplerParameterIiv(int i, int i2, int[] iArr) {
        nativeGlSamplerParameterIiv(i, i2, iArr);
    }

    public static void glSamplerParameterIuiv(int i, int i2, int[] iArr) {
        nativeGlSamplerParameterIuiv(i, i2, iArr);
    }

    public static void glGetSamplerParameterIiv(int i, int i2, int[] iArr) {
        nativeGlGetSamplerParameterIiv(i, i2, iArr);
    }

    public static void glGetSamplerParameterIuiv(int i, int i2, int[] iArr) {
        nativeGlGetSamplerParameterIuiv(i, i2, iArr);
    }

    public static void glTexBuffer(int i, int i2, int i3) {
        nativeGlTexBuffer(i, i2, i3);
    }

    public static void glTexBufferRange(int i, int i2, int i3, int i4, int i5) {
        nativeGlTexBufferRange(i, i2, i3, i4, i5);
    }

    public static void glTexStorage3DMultisample(int i, int i2, int i3, int i4, int i5, int i6, boolean z) {
        nativeGlTexStorage3DMultisample(i, i2, i3, i4, i5, i6, z);
    }
}
