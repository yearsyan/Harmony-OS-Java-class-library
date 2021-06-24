package ohos.agp.components.element;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.components.AttrHelper;
import ohos.agp.render.LinearShader;
import ohos.agp.render.Paint;
import ohos.agp.render.Path;
import ohos.agp.render.RadialShader;
import ohos.agp.render.Shader;
import ohos.agp.render.SweepShader;
import ohos.agp.utils.Color;
import ohos.agp.utils.Point;
import ohos.app.Context;
import ohos.com.sun.org.apache.xpath.internal.compiler.Keywords;
import ohos.global.resource.NotExistException;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import ohos.global.resource.solidxml.Node;
import ohos.global.resource.solidxml.TypedAttribute;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class VectorElement extends Element {
    private static final String ALPHA = "alpha";
    private static final String CLIP_PATH = "clip-path";
    private static final String GROUP = "group";
    private static final String HEIGHT = "height";
    private static final int MAX_DEPTH = 200;
    private static final int MAX_ELEMENT_COUNT = 5000;
    private static final float MAX_FLOAT_ALPHA = 1.0f;
    private static final float MIN_FLOAT_ALPHA = 0.0f;
    private static final String MIRROR = "autoMirrored";
    private static final String PATH = "path";
    private static final HiLogLabel TAG = new HiLogLabel(3, LogDomain.END, "VectorDrawable");
    private static final String VECTOR = "vector";
    private static final String VIEWPORT_HEIGHT = "viewportHeight";
    private static final String VIEWPORT_WIDTH = "viewportWidth";
    private static final String WIDTH = "width";
    private Context mContext;
    private boolean mIsAntiAlias = false;
    private int mParseDepth = 0;
    private int mParseElementCount = 0;
    private ResourceManager mResourceManager;
    private VGroup mRootGroup;

    /* access modifiers changed from: private */
    public enum GradientType {
        LINEAR,
        RADIAL,
        SWEEP
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native void nativeAddChild(long j, long j2);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native long nativeCreateClipPath();

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native long nativeCreateFullPath();

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native long nativeCreateGroup();

    private native long nativeCreateVectorElement(long j);

    private native void nativeSetAntiAlias(long j, boolean z);

    private native void nativeSetAutoMirror(long j, boolean z);

    private native void nativeSetElementSize(long j, int i, int i2, int i3, int i4);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native void nativeSetPathString(long j, String str, int i);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native void nativeUpdateFullPathFillGradient(long j, long j2);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native void nativeUpdateFullPathProperties(long j, float f, int i, float f2, int i2, float f3, float f4, float f5, float f6, float f7, int i3, int i4, int i5);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native void nativeUpdateFullPathStrokeGradient(long j, long j2);

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private native void nativeUpdateGroupProperties(long j, float f, float f2, float f3, float f4, float f5, float f6, float f7);

    public VectorElement() {
    }

    public VectorElement(Context context, int i) {
        if (context != null) {
            this.mResourceManager = context.getResourceManager();
            this.mContext = context;
            parseXMLNode(context, ElementScatter.getInstance(context).getRootNodeFromXmlId(i));
        }
    }

    public void setAntiAlias(boolean z) {
        this.mIsAntiAlias = z;
        nativeSetAntiAlias(this.mNativeElementPtr, z);
    }

    public boolean isAntiAlias() {
        return this.mIsAntiAlias;
    }

    @Override // ohos.agp.components.element.Element
    public void createNativePtr(Object obj) {
        if (this.mNativeElementPtr == 0) {
            this.mRootGroup = new VGroup();
            this.mNativeElementPtr = nativeCreateVectorElement(this.mRootGroup.getNativePtr());
        }
    }

    /* access modifiers changed from: private */
    public class VGroup extends VObject {
        private static final String PIVOT_X = "pivotX";
        private static final String PIVOT_Y = "pivotY";
        private static final String ROTATE = "rotate";
        private static final String SCALE_X = "scaleX";
        private static final String SCALE_Y = "scaleY";
        private static final String TRANSLATE_X = "translateX";
        private static final String TRANSLATE_Y = "translateY";

        VGroup() {
            super(null);
            this.mNativePtr = VectorElement.this.nativeCreateGroup();
        }

        /* access modifiers changed from: package-private */
        @Override // ohos.agp.components.element.VectorElement.VObject
        public long getNativePtr() {
            return this.mNativePtr;
        }

        /* access modifiers changed from: package-private */
        public void addChild(VObject vObject) {
            VectorElement.this.nativeAddChild(this.mNativePtr, vObject.getNativePtr());
        }

        /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
        /* access modifiers changed from: package-private */
        public void parseProperties(List<TypedAttribute> list) {
            char c;
            HiLog.debug(VectorElement.TAG, "parseProperty:attr size: %{public}d", Integer.valueOf(list.size()));
            float f = 1.0f;
            float f2 = 1.0f;
            float f3 = 0.0f;
            float f4 = 0.0f;
            float f5 = 0.0f;
            float f6 = 0.0f;
            float f7 = 0.0f;
            for (TypedAttribute typedAttribute : list) {
                String name = typedAttribute.getName();
                if (name == null) {
                    HiLog.error(VectorElement.TAG, " attribute name is null", new Object[0]);
                } else {
                    try {
                        float convertValueToFloat = AttrHelper.convertValueToFloat(typedAttribute.getStringValue(), 0.0f);
                        switch (name.hashCode()) {
                            case -1721943862:
                                if (name.equals(TRANSLATE_X)) {
                                    c = 5;
                                    break;
                                }
                                c = 65535;
                                break;
                            case -1721943861:
                                if (name.equals(TRANSLATE_Y)) {
                                    c = 6;
                                    break;
                                }
                                c = 65535;
                                break;
                            case -987906986:
                                if (name.equals(PIVOT_X)) {
                                    c = 1;
                                    break;
                                }
                                c = 65535;
                                break;
                            case -987906985:
                                if (name.equals(PIVOT_Y)) {
                                    c = 2;
                                    break;
                                }
                                c = 65535;
                                break;
                            case -925180581:
                                if (name.equals("rotate")) {
                                    c = 0;
                                    break;
                                }
                                c = 65535;
                                break;
                            case -908189618:
                                if (name.equals(SCALE_X)) {
                                    c = 3;
                                    break;
                                }
                                c = 65535;
                                break;
                            case -908189617:
                                if (name.equals(SCALE_Y)) {
                                    c = 4;
                                    break;
                                }
                                c = 65535;
                                break;
                            default:
                                c = 65535;
                                break;
                        }
                        switch (c) {
                            case 0:
                                f3 = convertValueToFloat;
                                continue;
                            case 1:
                                f4 = convertValueToFloat;
                                continue;
                            case 2:
                                f5 = convertValueToFloat;
                                continue;
                            case 3:
                                f = convertValueToFloat;
                                continue;
                            case 4:
                                f2 = convertValueToFloat;
                                continue;
                            case 5:
                                f6 = convertValueToFloat;
                                continue;
                            case 6:
                                f7 = convertValueToFloat;
                                continue;
                        }
                    } catch (IOException | NotExistException | WrongTypeException unused) {
                        HiLog.error(VectorElement.TAG, "parse property %{public}s typedAttribute failed", name);
                    }
                }
            }
            VectorElement.this.nativeUpdateGroupProperties(this.mNativePtr, f3, f4, f5, f, f2, f6, f7);
        }
    }

    private static abstract class VPath extends VObject {
        static final String PATH_DATA = "pathData";

        private VPath() {
            super(null);
        }

        /* synthetic */ VPath(AnonymousClass1 r1) {
            this();
        }
    }

    /* access modifiers changed from: private */
    public class VClipPath extends VPath {
        VClipPath() {
            super(null);
            this.mNativePtr = VectorElement.this.nativeCreateClipPath();
        }

        /* access modifiers changed from: package-private */
        @Override // ohos.agp.components.element.VectorElement.VObject
        public long getNativePtr() {
            return this.mNativePtr;
        }

        /* access modifiers changed from: package-private */
        public void parseProperties(List<TypedAttribute> list) {
            String str;
            HiLog.debug(VectorElement.TAG, "parseProperty:attr size: %{public}d", Integer.valueOf(list.size()));
            Iterator<TypedAttribute> it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    str = null;
                    break;
                }
                TypedAttribute next = it.next();
                if ("pathData".equals(next.getName())) {
                    try {
                        str = next.getStringValue();
                        HiLog.debug(VectorElement.TAG, "parse clip-pathData: %{public}s", str);
                        break;
                    } catch (IOException | NotExistException | WrongTypeException unused) {
                        HiLog.error(VectorElement.TAG, "parse property pathData typedAttribute failed", new Object[0]);
                    }
                }
            }
            if (str != null) {
                VectorElement.this.nativeSetPathString(this.mNativePtr, str, str.length());
            }
        }
    }

    /* access modifiers changed from: private */
    public class VFullPath extends VPath {
        private static final String ATTR = "attr";
        private static final String BUTT_CAP = "butt";
        private static final String CENTER_X = "centerX";
        private static final String CENTER_Y = "centerY";
        private static final String CLAMP = "clamp";
        private static final String COLOR = "color";
        private static final String END_X = "endX";
        private static final String END_Y = "endY";
        private static final String EVEN_ODD = "evenOdd";
        private static final String FILL_ALPHA = "fillAlpha";
        private static final String FILL_COLOR = "fillColor";
        private static final String FILL_TYPE = "filltype";
        private static final String GRADIENT = "gradient";
        private static final String GRADIENT_RADIUS = "gradientRadius";
        private static final String ITEM = "item";
        private static final String LINEAR_GRADIENT = "linear";
        private static final String MIRROR = "mirror";
        private static final String MITER_JOIN = "miter";
        private static final String NAME = "name";
        private static final String OFFSET = "offset";
        private static final String RADIAL_GRADIENT = "radial";
        private static final String REPEAT = "repeat";
        private static final String ROUND_CAP = "round";
        private static final String ROUND_JOIN = "round";
        private static final String START_X = "startX";
        private static final String START_Y = "startY";
        private static final String STROKE_ALPHA = "strokeAlpha";
        private static final String STROKE_COLOR = "strokeColor";
        private static final String STROKE_LINE_CAP = "strokeLineCap";
        private static final String STROKE_LINE_JOIN = "strokeLineJoin";
        private static final String STROKE_MITER_LIMIT = "strokeMiterLimit";
        private static final String STROKE_WIDTH = "strokeWidth";
        private static final String SWEEP_GRADIENT = "sweep";
        private static final String TILE_MODE = "tileMode";
        private static final String TRIM_PATH_END = "trimPathEnd";
        private static final String TRIM_PATH_OFFSET = "trimPathOffset";
        private static final String TRIM_PATH_START = "trimPathStart";
        private static final String TYPE = "type";
        private Shader mFillGradient = null;
        private final GradientProperty mGradientProperty = new GradientProperty();
        private final FullPathProperty mProperty = new FullPathProperty();
        private Shader mStrokeGradient = null;

        VFullPath() {
            super(null);
            this.mNativePtr = VectorElement.this.nativeCreateFullPath();
        }

        /* access modifiers changed from: package-private */
        @Override // ohos.agp.components.element.VectorElement.VObject
        public long getNativePtr() {
            return this.mNativePtr;
        }

        /* access modifiers changed from: package-private */
        public void parseProperties(Node node) {
            List<TypedAttribute> typedAttributes = node.getTypedAttributes(VectorElement.this.mResourceManager);
            HiLog.debug(VectorElement.TAG, "parseProperty:attr size: %{public}d", Integer.valueOf(typedAttributes.size()));
            for (TypedAttribute typedAttribute : typedAttributes) {
                VectorElement.this.mParseElementCount++;
                parseProperty(typedAttribute);
            }
            VectorElement.this.nativeUpdateFullPathProperties(this.mNativePtr, this.mProperty.mStrokeWidth, this.mProperty.mStrokeColor, this.mProperty.mStrokeAlpha, this.mProperty.mFillColor, this.mProperty.mFillAlpha, this.mProperty.mTrimPathStart, this.mProperty.mTrimPathEnd, this.mProperty.mTrimPathOffset, this.mProperty.mStrokeMiterLimit, this.mProperty.mStrokeLineCap, this.mProperty.mStrokeLineJoin, this.mProperty.mFillType);
            String str = this.mProperty.mPathDataString;
            if (str != null) {
                VectorElement.this.nativeSetPathString(this.mNativePtr, str, str.length());
            }
            Node child = node.getChild();
            if (child != null) {
                HiLog.debug(VectorElement.TAG, "parse gradient property: %{public}s", child.getName());
                VectorElement.this.mParseDepth++;
                parseFullPathAttribute(child);
            }
        }

        /* access modifiers changed from: private */
        public class FullPathProperty {
            private float mFillAlpha;
            private int mFillColor;
            private int mFillType;
            private String mPathDataString;
            private float mStrokeAlpha;
            private int mStrokeColor;
            private int mStrokeLineCap;
            private int mStrokeLineJoin;
            private float mStrokeMiterLimit;
            private float mStrokeWidth;
            private float mTrimPathEnd;
            private float mTrimPathOffset;
            private float mTrimPathStart;

            FullPathProperty() {
                reset();
            }

            private void reset() {
                this.mPathDataString = null;
                this.mStrokeWidth = 0.0f;
                this.mStrokeColor = 0;
                this.mStrokeAlpha = 1.0f;
                this.mFillColor = 0;
                this.mFillAlpha = 1.0f;
                this.mTrimPathStart = 0.0f;
                this.mTrimPathEnd = 1.0f;
                this.mTrimPathOffset = 0.0f;
                this.mStrokeLineCap = 0;
                this.mStrokeLineJoin = 0;
                this.mStrokeMiterLimit = 0.0f;
                this.mFillType = 0;
            }
        }

        /* access modifiers changed from: private */
        public class GradientProperty {
            private float mCenterX;
            private float mCenterY;
            private float mEndX;
            private float mEndY;
            private float mGradientRadius;
            private GradientType mGradientType;
            private Vector<Integer> mShaderColors = new Vector<>();
            private Vector<Float> mShaderStops = new Vector<>();
            private float mStartX;
            private float mStartY;
            private Shader.TileMode mTileMode;

            GradientProperty() {
                reset();
            }

            /* access modifiers changed from: package-private */
            public void reset() {
                this.mGradientType = GradientType.LINEAR;
                this.mTileMode = Shader.TileMode.CLAMP_TILEMODE;
                this.mStartX = 0.0f;
                this.mStartY = 0.0f;
                this.mEndX = 0.0f;
                this.mEndY = 0.0f;
                this.mCenterX = 0.0f;
                this.mCenterY = 0.0f;
                this.mGradientRadius = 0.0f;
                this.mShaderColors.clear();
                this.mShaderStops.clear();
            }
        }

        /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
        private void parseProperty(TypedAttribute typedAttribute) {
            char c;
            String name = typedAttribute.getName();
            try {
                String stringValue = typedAttribute.getStringValue();
                if (name != null && stringValue != null) {
                    switch (name.hashCode()) {
                        case -1143814757:
                            if (name.equals(FILL_ALPHA)) {
                                c = 4;
                                break;
                            }
                            c = 65535;
                            break;
                        case -1141881952:
                            if (name.equals(FILL_COLOR)) {
                                c = 3;
                                break;
                            }
                            c = 65535;
                            break;
                        case -1121758502:
                            if (name.equals(TRIM_PATH_OFFSET)) {
                                c = 7;
                                break;
                            }
                            c = 65535;
                            break;
                        case -728102083:
                            if (name.equals(FILL_TYPE)) {
                                c = 11;
                                break;
                            }
                            c = 65535;
                            break;
                        case -170626757:
                            if (name.equals(TRIM_PATH_START)) {
                                c = 5;
                                break;
                            }
                            c = 65535;
                            break;
                        case 49292814:
                            if (name.equals(STROKE_MITER_LIMIT)) {
                                c = '\n';
                                break;
                            }
                            c = 65535;
                            break;
                        case 1027544550:
                            if (name.equals(STROKE_LINE_CAP)) {
                                c = '\b';
                                break;
                            }
                            c = 65535;
                            break;
                        case 1233923439:
                            if (name.equals("pathData")) {
                                c = '\f';
                                break;
                            }
                            c = 65535;
                            break;
                        case 1789331862:
                            if (name.equals(STROKE_LINE_JOIN)) {
                                c = '\t';
                                break;
                            }
                            c = 65535;
                            break;
                        case 1903848966:
                            if (name.equals(STROKE_ALPHA)) {
                                c = 2;
                                break;
                            }
                            c = 65535;
                            break;
                        case 1905781771:
                            if (name.equals(STROKE_COLOR)) {
                                c = 1;
                                break;
                            }
                            c = 65535;
                            break;
                        case 1924065902:
                            if (name.equals(STROKE_WIDTH)) {
                                c = 0;
                                break;
                            }
                            c = 65535;
                            break;
                        case 2136119284:
                            if (name.equals(TRIM_PATH_END)) {
                                c = 6;
                                break;
                            }
                            c = 65535;
                            break;
                        default:
                            c = 65535;
                            break;
                    }
                    switch (c) {
                        case 0:
                            this.mProperty.mStrokeWidth = AttrHelper.convertValueToFloat(stringValue, 0.0f);
                            return;
                        case 1:
                            this.mProperty.mStrokeColor = AttrHelper.convertValueToColor(stringValue).getValue();
                            return;
                        case 2:
                            this.mProperty.mStrokeAlpha = AttrHelper.convertValueToFloat(stringValue, 1.0f);
                            return;
                        case 3:
                            this.mProperty.mFillColor = AttrHelper.convertValueToColor(stringValue).getValue();
                            return;
                        case 4:
                            this.mProperty.mFillAlpha = AttrHelper.convertValueToFloat(stringValue, 1.0f);
                            return;
                        case 5:
                            this.mProperty.mTrimPathStart = AttrHelper.convertValueToFloat(stringValue, 0.0f);
                            return;
                        case 6:
                            this.mProperty.mTrimPathEnd = AttrHelper.convertValueToFloat(stringValue, 1.0f);
                            return;
                        case 7:
                            this.mProperty.mTrimPathOffset = AttrHelper.convertValueToFloat(stringValue, 0.0f);
                            return;
                        case '\b':
                            this.mProperty.mStrokeLineCap = getStrokeCapType(stringValue).value();
                            return;
                        case '\t':
                            this.mProperty.mStrokeLineJoin = getStrokeJoinType(stringValue).value();
                            return;
                        case '\n':
                            this.mProperty.mStrokeMiterLimit = (float) AttrHelper.convertValueToInt(stringValue, 0);
                            return;
                        case 11:
                            this.mProperty.mFillType = getFillType(stringValue).value();
                            return;
                        case '\f':
                            this.mProperty.mPathDataString = stringValue;
                            HiLog.debug(VectorElement.TAG, "parse pathData: %{public}s", this.mProperty.mPathDataString);
                            return;
                        default:
                            return;
                    }
                }
            } catch (IOException | NotExistException | WrongTypeException unused) {
                HiLog.error(VectorElement.TAG, "parse property failed", new Object[0]);
            }
        }

        private Path.FillType getFillType(String str) {
            if (str.equals(EVEN_ODD)) {
                return Path.FillType.EVEN_ODD;
            }
            return Path.FillType.WINDING_ORDER;
        }

        private Paint.StrokeCap getStrokeCapType(String str) {
            if (BUTT_CAP.equals(str)) {
                return Paint.StrokeCap.BUTT_CAP;
            }
            if (Keywords.FUNC_ROUND_STRING.equals(str)) {
                return Paint.StrokeCap.ROUND_CAP;
            }
            return Paint.StrokeCap.SQUARE_CAP;
        }

        private Paint.Join getStrokeJoinType(String str) {
            if (MITER_JOIN.equals(str)) {
                return Paint.Join.MITER_JOIN;
            }
            if (Keywords.FUNC_ROUND_STRING.equals(str)) {
                return Paint.Join.ROUND_JOIN;
            }
            return Paint.Join.BEVEL_JOIN;
        }

        private void parseFullPathAttribute(Node node) {
            VectorElement.this.checkXMLDepthAndElementCount();
            VectorElement.this.mParseElementCount++;
            String name = node.getName();
            if (!(name == null || name.length() == 0 || !name.equals(ATTR))) {
                for (TypedAttribute typedAttribute : node.getTypedAttributes(VectorElement.this.mResourceManager)) {
                    String name2 = typedAttribute.getName();
                    try {
                        String stringValue = typedAttribute.getStringValue();
                        if (!(name2 == null || stringValue == null || !"name".equals(name2))) {
                            this.mGradientProperty.reset();
                            VectorElement.this.mParseDepth++;
                            parseGradient(node.getChild());
                            if (this.mGradientProperty.mShaderColors.size() > 1) {
                                makeShader(stringValue);
                            }
                        }
                    } catch (IOException | NotExistException | WrongTypeException unused) {
                        HiLog.error(VectorElement.TAG, "get path typedAttribute failed", new Object[0]);
                    }
                }
            }
        }

        private void makeShader(String str) {
            Vector vector = this.mGradientProperty.mShaderColors;
            Color[] colorArr = new Color[vector.size()];
            for (int i = 0; i < vector.size(); i++) {
                colorArr[i] = new Color(((Integer) vector.get(i)).intValue());
            }
            Vector vector2 = this.mGradientProperty.mShaderStops;
            float[] fArr = new float[vector2.size()];
            for (int i2 = 0; i2 < vector2.size(); i2++) {
                fArr[i2] = ((Float) vector2.get(i2)).floatValue();
            }
            Shader shader = null;
            int i3 = AnonymousClass1.$SwitchMap$ohos$agp$components$element$VectorElement$GradientType[this.mGradientProperty.mGradientType.ordinal()];
            if (i3 == 1) {
                shader = new LinearShader(new Point[]{new Point(this.mGradientProperty.mStartX, this.mGradientProperty.mStartY), new Point(this.mGradientProperty.mEndX, this.mGradientProperty.mEndY)}, fArr, colorArr, this.mGradientProperty.mTileMode);
            } else if (i3 == 2) {
                shader = new SweepShader(this.mGradientProperty.mCenterX, this.mGradientProperty.mCenterY, colorArr, fArr);
            } else if (i3 == 3) {
                shader = new RadialShader(new Point(this.mGradientProperty.mCenterX, this.mGradientProperty.mCenterY), this.mGradientProperty.mGradientRadius, fArr, colorArr, this.mGradientProperty.mTileMode);
            }
            if (str.equals(FILL_COLOR)) {
                this.mFillGradient = shader;
                VectorElement.this.nativeUpdateFullPathFillGradient(this.mNativePtr, this.mFillGradient.getNativeHandle());
            } else if (str.equals(STROKE_COLOR)) {
                this.mStrokeGradient = shader;
                VectorElement.this.nativeUpdateFullPathStrokeGradient(this.mNativePtr, this.mStrokeGradient.getNativeHandle());
            } else {
                HiLog.debug(VectorElement.TAG, "unknown attr, ignore it", new Object[0]);
            }
        }

        private void parseGradient(Node node) {
            VectorElement.this.checkXMLDepthAndElementCount();
            if (node != null) {
                String name = node.getName();
                if (name == null || name.length() == 0 || !name.equals(GRADIENT)) {
                    parseGradient(node.getSibling());
                    return;
                }
                VectorElement.this.mParseElementCount++;
                for (TypedAttribute typedAttribute : node.getTypedAttributes(VectorElement.this.mResourceManager)) {
                    parseGradientProperty(typedAttribute);
                }
                Node child = node.getChild();
                if (child != null) {
                    VectorElement.this.mParseDepth++;
                    parseGradientColors(child);
                }
            }
        }

        /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x004e, code lost:
            if (r0.equals("type") != false) goto L_0x0087;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void parseGradientProperty(ohos.global.resource.solidxml.TypedAttribute r6) {
            /*
            // Method dump skipped, instructions count: 358
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.agp.components.element.VectorElement.VFullPath.parseGradientProperty(ohos.global.resource.solidxml.TypedAttribute):void");
        }

        private void parseGradientColors(Node node) {
            VectorElement.this.checkXMLDepthAndElementCount();
            VectorElement.this.mParseElementCount++;
            String name = node.getName();
            if (name != null && name.length() != 0 && name.equals(ITEM)) {
                for (TypedAttribute typedAttribute : node.getTypedAttributes(VectorElement.this.mResourceManager)) {
                    String name2 = typedAttribute.getName();
                    String str = null;
                    try {
                        str = typedAttribute.getStringValue();
                    } catch (IOException | NotExistException | WrongTypeException unused) {
                        HiLog.error(VectorElement.TAG, "get gradient colors %{public}s typedAttribute failed", name2);
                    }
                    if (!(name2 == null || str == null)) {
                        if (name2.equals(COLOR)) {
                            Vector vector = this.mGradientProperty.mShaderColors;
                            vector.add(Integer.valueOf(AttrHelper.convertValueToColor(str).getValue()));
                            this.mGradientProperty.mShaderColors = vector;
                        } else if (name2.equals(OFFSET)) {
                            Vector vector2 = this.mGradientProperty.mShaderStops;
                            vector2.add(Float.valueOf(AttrHelper.convertValueToFloat(str, 0.0f)));
                            this.mGradientProperty.mShaderStops = vector2;
                        } else {
                            HiLog.debug(VectorElement.TAG, "unknown attr in shader, ignore it", new Object[0]);
                        }
                    }
                }
                if (node.getSibling() != null) {
                    parseGradientColors(node.getSibling());
                }
            } else if (node.getSibling() != null) {
                parseGradientColors(node.getSibling());
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: ohos.agp.components.element.VectorElement$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$ohos$agp$components$element$VectorElement$GradientType = new int[GradientType.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            /*
                ohos.agp.components.element.VectorElement$GradientType[] r0 = ohos.agp.components.element.VectorElement.GradientType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                ohos.agp.components.element.VectorElement.AnonymousClass1.$SwitchMap$ohos$agp$components$element$VectorElement$GradientType = r0
                int[] r0 = ohos.agp.components.element.VectorElement.AnonymousClass1.$SwitchMap$ohos$agp$components$element$VectorElement$GradientType     // Catch:{ NoSuchFieldError -> 0x0014 }
                ohos.agp.components.element.VectorElement$GradientType r1 = ohos.agp.components.element.VectorElement.GradientType.LINEAR     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = ohos.agp.components.element.VectorElement.AnonymousClass1.$SwitchMap$ohos$agp$components$element$VectorElement$GradientType     // Catch:{ NoSuchFieldError -> 0x001f }
                ohos.agp.components.element.VectorElement$GradientType r1 = ohos.agp.components.element.VectorElement.GradientType.SWEEP     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = ohos.agp.components.element.VectorElement.AnonymousClass1.$SwitchMap$ohos$agp$components$element$VectorElement$GradientType     // Catch:{ NoSuchFieldError -> 0x002a }
                ohos.agp.components.element.VectorElement$GradientType r1 = ohos.agp.components.element.VectorElement.GradientType.RADIAL     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.agp.components.element.VectorElement.AnonymousClass1.<clinit>():void");
        }
    }

    /* access modifiers changed from: private */
    public static abstract class VObject {
        long mNativePtr;

        /* access modifiers changed from: package-private */
        public abstract long getNativePtr();

        private VObject() {
            this.mNativePtr = 0;
        }

        /* synthetic */ VObject(AnonymousClass1 r1) {
            this();
        }
    }

    @Override // ohos.agp.components.element.Element
    public void parseXMLNode(Context context, Node node) {
        super.parseXMLNode(context, node);
        if (context != null) {
            this.mContext = context;
            this.mResourceManager = this.mContext.getResourceManager();
            if (this.mResourceManager == null) {
                throw new ElementScatterException("mResourceManager is null");
            } else if (node != null) {
                String name = node.getName();
                if (name == null || name.length() == 0) {
                    throw new ElementScatterException("Solid XML root node has no name!");
                } else if (name.equals(VECTOR)) {
                    HiLog.debug(TAG, "rootNode: %{public}s", name);
                    parseProperties(node.getTypedAttributes(this.mResourceManager));
                    this.mParseDepth = 1;
                    this.mParseElementCount = 1;
                    Node child = node.getChild();
                    if (child != null) {
                        parseSolidXmlNode(this.mRootGroup, child);
                    }
                } else {
                    throw new ElementScatterException("vector XML is not valid!");
                }
            } else {
                throw new ElementScatterException("Solid XML has no root node!");
            }
        } else {
            throw new ElementScatterException("context is null");
        }
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    private void parseProperties(List<TypedAttribute> list) {
        String str;
        char c;
        char c2 = 0;
        HiLog.debug(TAG, "parseProperty:attr size: %{public}d", Integer.valueOf(list.size()));
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        boolean z = false;
        float f = 1.0f;
        for (TypedAttribute typedAttribute : list) {
            String name = typedAttribute.getName();
            try {
                str = typedAttribute.getStringValue();
            } catch (IOException | NotExistException | WrongTypeException unused) {
                HiLogLabel hiLogLabel = TAG;
                Object[] objArr = new Object[1];
                objArr[c2] = name;
                HiLog.error(hiLogLabel, "parse property %{public}s typedAttribute failed", objArr);
                str = null;
            }
            if (!(name == null || str == null)) {
                switch (name.hashCode()) {
                    case -1499022144:
                        if (name.equals(VIEWPORT_WIDTH)) {
                            c = 2;
                            break;
                        }
                        c = 65535;
                        break;
                    case -1410029715:
                        if (name.equals(MIRROR)) {
                            c = 5;
                            break;
                        }
                        c = 65535;
                        break;
                    case -1221029593:
                        if (name.equals("height")) {
                            c = 1;
                            break;
                        }
                        c = 65535;
                        break;
                    case 92909918:
                        if (name.equals("alpha")) {
                            c = 4;
                            break;
                        }
                        c = 65535;
                        break;
                    case 113126854:
                        if (name.equals("width")) {
                            c = 0;
                            break;
                        }
                        c = 65535;
                        break;
                    case 341959021:
                        if (name.equals(VIEWPORT_HEIGHT)) {
                            c = 3;
                            break;
                        }
                        c = 65535;
                        break;
                    default:
                        c = 65535;
                        break;
                }
                if (c == 0) {
                    i3 = AttrHelper.convertDimensionToPix(this.mContext, str, 0);
                } else if (c == 1) {
                    i4 = AttrHelper.convertDimensionToPix(this.mContext, str, 0);
                } else if (c == 2) {
                    i = AttrHelper.convertDimensionToPix(this.mContext, str, 0);
                } else if (c == 3) {
                    i2 = AttrHelper.convertDimensionToPix(this.mContext, str, 0);
                } else if (c == 4) {
                    f = AttrHelper.convertValueToFloat(str, 1.0f);
                } else if (c == 5) {
                    z = AttrHelper.convertValueToBoolean(str, false);
                }
            }
            c2 = 0;
        }
        nativeSetElementSize(this.mNativeElementPtr, i, i2, i3, i4);
        nativeSetAutoMirror(this.mNativeElementPtr, z);
        setAlpha((int) (Math.max(0.0f, Math.min(1.0f, f)) * 255.0f));
        HiLog.debug(TAG, "parseProperty:width: %{public}d height: %{public}d", Integer.valueOf(i3), Integer.valueOf(i4));
    }

    private void parseSolidXmlNode(VGroup vGroup, Node node) {
        checkXMLDepthAndElementCount();
        String name = node.getName();
        if (name == null || name.length() == 0) {
            HiLog.error(TAG, "null node", new Object[0]);
            return;
        }
        HiLog.debug(TAG, "parseSolidXmlNode: %{public}s: %{public}s", vGroup, name);
        List<TypedAttribute> typedAttributes = node.getTypedAttributes(this.mResourceManager);
        this.mParseElementCount++;
        char c = 65535;
        int hashCode = name.hashCode();
        if (hashCode != -1649314686) {
            if (hashCode != 3433509) {
                if (hashCode == 98629247 && name.equals(GROUP)) {
                    c = 2;
                }
            } else if (name.equals("path")) {
                c = 0;
            }
        } else if (name.equals(CLIP_PATH)) {
            c = 1;
        }
        if (c == 0) {
            VFullPath vFullPath = new VFullPath();
            vFullPath.parseProperties(node);
            vGroup.addChild(vFullPath);
        } else if (c == 1) {
            VClipPath vClipPath = new VClipPath();
            vClipPath.parseProperties(typedAttributes);
            vGroup.addChild(vClipPath);
        } else if (c != 2) {
            HiLog.debug(TAG, "unsupported attribute:%{public}s, ignore it", name);
        } else {
            VGroup vGroup2 = new VGroup();
            vGroup2.parseProperties(typedAttributes);
            vGroup.addChild(vGroup2);
            Node child = node.getChild();
            if (child != null) {
                HiLog.debug(TAG, "parseSolidXmlNode: child: %{public}s: %{public}s", vGroup2, child.getName());
                this.mParseDepth++;
                parseSolidXmlNode(vGroup2, child);
            }
        }
        HiLog.debug(TAG, "get sibling", new Object[0]);
        Node sibling = node.getSibling();
        if (sibling != null) {
            HiLog.debug(TAG, "parseSolidXmlNode: sibling: %{public}s: %{public}s", vGroup, sibling.getName());
            parseSolidXmlNode(vGroup, sibling);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void checkXMLDepthAndElementCount() {
        int i = this.mParseDepth;
        if (i > 200 || this.mParseElementCount > MAX_ELEMENT_COUNT) {
            throw new ElementScatterException(String.format(Locale.ROOT, "Exceeded the depth limit: %d Or count limit: %d", 200, Integer.valueOf((int) MAX_ELEMENT_COUNT)));
        }
        HiLog.debug(TAG, "parseSolidXmlNode current depth: %{public}d", Integer.valueOf(i));
    }
}
