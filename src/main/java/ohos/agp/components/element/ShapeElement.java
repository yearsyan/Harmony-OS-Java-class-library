package ohos.agp.components.element;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.AttrHelper;
import ohos.agp.render.Arc;
import ohos.agp.render.Path;
import ohos.app.Context;
import ohos.data.search.schema.DocumentItem;
import ohos.global.resource.NotExistException;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import ohos.global.resource.solidxml.Node;
import ohos.global.resource.solidxml.TypedAttribute;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class ShapeElement extends Element {
    public static final int ARC = 3;
    private static final int CORNER_ARRAY_LENGTH = RADII_ARR.length;
    public static final int LINE = 2;
    public static final int LINEAR_GRADIENT_SHADER_TYPE = 0;
    public static final int OVAL = 1;
    public static final int PATH = 4;
    public static final int RADIAL_GRADIENT_SHADER_TYPE = 1;
    private static final String[] RADII_ARR = {"left_top_x", "left_top_y", "right_top_x", "right_top_y", "right_bottom_x", "right_bottom_y", "left_bottom_x", "left_bottom_y"};
    public static final int RECTANGLE = 0;
    public static final int SWEEP_GRADIENT_SHADER_TYPE = 2;
    private static final HiLogLabel TAG = new HiLogLabel(3, LogDomain.END, "ShapeElement");
    private boolean isRectangle = false;
    private Context mContext;
    public Path mPath;
    private ResourceManager mResourceManager;

    public enum Orientation {
        TOP_TO_BOTTOM,
        TOP_RIGHT_TO_BOTTOM_LEFT,
        RIGHT_TO_LEFT,
        BOTTOM_RIGHT_TO_TOP_LEFT,
        BOTTOM_TO_TOP,
        BOTTOM_LEFT_TO_TOP_RIGHT,
        LEFT_TO_RIGHT,
        TOP_LEFT_TO_BOTTOM_RIGHT,
        TOP_START_TO_BOTTOM_END,
        START_TO_END,
        BOTTOM_START_TO_TOP_END,
        BOTTOM_END_TO_TOP_START,
        END_TO_START,
        TOP_END_TO_BOTTOM_START
    }

    private native Arc nativeGetArc(long j);

    private native int nativeGetBorderColor(long j);

    private native int[] nativeGetColors(long j);

    private native float[] nativeGetCornerRadii(long j);

    private native float nativeGetCornerRadius(long j);

    private native long nativeGetGradientDrawableHandle();

    private native int nativeGetOrientation(long j);

    private native int nativeGetShaderType(long j);

    private native int nativeGetShape(long j);

    private native int nativeGetStrokeWidth(long j);

    private native void nativeSetArc(long j, float f, float f2, boolean z);

    private native void nativeSetColor(long j, int i);

    private native void nativeSetColors(long j, int[] iArr);

    private native void nativeSetCornerRadii(long j, float[] fArr);

    private native void nativeSetCornerRadius(long j, float f);

    private native void nativeSetDashPathEffectValues(long j, float[] fArr, int i, float f);

    private native void nativeSetOrientation(long j, int i);

    private native void nativeSetPath(long j, long j2);

    private native void nativeSetShaderType(long j, int i);

    private native void nativeSetShape(long j, int i);

    private native void nativeSetStroke(long j, int i, int i2);

    public ShapeElement() {
    }

    public ShapeElement(Context context, int i) {
        if (context != null) {
            parseXMLNode(context, ElementScatter.getInstance(context).getRootNodeFromXmlId(i));
            return;
        }
        throw new ElementScatterException("context is null");
    }

    @Override // ohos.agp.components.element.Element
    public void parseXMLNode(Context context, Node node) {
        super.parseXMLNode(context, node);
        HiLog.debug(TAG, "enter shape, new ShapeElement", new Object[0]);
        if (context != null) {
            this.mContext = context;
            this.mResourceManager = this.mContext.getResourceManager();
            ResourceManager resourceManager = this.mResourceManager;
            if (resourceManager != null) {
                List<TypedAttribute> typedAttributes = node.getTypedAttributes(resourceManager);
                if (!typedAttributes.isEmpty()) {
                    for (TypedAttribute typedAttribute : typedAttributes) {
                        if (typedAttribute != null) {
                            try {
                                if ("shape".equals(typedAttribute.getName())) {
                                    parseShape(typedAttribute.getStringValue());
                                } else {
                                    HiLog.error(TAG, "do not support this tag: %s", typedAttribute.getName());
                                }
                            } catch (IOException | IllegalArgumentException | NotExistException | WrongTypeException unused) {
                                throw new ElementScatterException("set " + typedAttribute.getName() + " failed.");
                            }
                        }
                    }
                    Node child = node.getChild();
                    if (child != null) {
                        parseNodeInternal(child);
                        return;
                    }
                    throw new ElementScatterException("no items in ShapeElement!");
                }
                throw new ElementScatterException("should define shapeElement's shape!");
            }
            throw new ElementScatterException("mResourceManager is null");
        }
        throw new ElementScatterException("context is null");
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    private void parseShape(String str) {
        char c;
        String lowerCase = str.toLowerCase(Locale.ENGLISH);
        int hashCode = lowerCase.hashCode();
        switch (hashCode) {
            case 48:
                if (lowerCase.equals("0")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 49:
                if (lowerCase.equals("1")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 50:
                if (lowerCase.equals("2")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 51:
                if (lowerCase.equals("3")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 52:
                if (lowerCase.equals("4")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            default:
                switch (hashCode) {
                    case 96850:
                        if (lowerCase.equals("arc")) {
                            c = 6;
                            break;
                        }
                        c = 65535;
                        break;
                    case 3321844:
                        if (lowerCase.equals("line")) {
                            c = 4;
                            break;
                        }
                        c = 65535;
                        break;
                    case 3423314:
                        if (lowerCase.equals("oval")) {
                            c = 2;
                            break;
                        }
                        c = 65535;
                        break;
                    case 3433509:
                        if (lowerCase.equals(DocumentItem.PATH)) {
                            c = '\b';
                            break;
                        }
                        c = 65535;
                        break;
                    case 1121299823:
                        if (lowerCase.equals("rectangle")) {
                            c = 0;
                            break;
                        }
                        c = 65535;
                        break;
                    default:
                        c = 65535;
                        break;
                }
        }
        switch (c) {
            case 0:
            case 1:
                this.isRectangle = true;
                setShape(0);
                break;
            case 2:
            case 3:
                setShape(1);
                break;
            case 4:
            case 5:
                setShape(2);
                break;
            case 6:
            case 7:
                setShape(3);
                break;
            case '\b':
            case '\t':
                setShape(4);
                break;
            default:
                HiLog.error(TAG, "do not support this shape value: %{public}s", str);
                break;
        }
        HiLog.debug(TAG, "set shape : %{public}s", str);
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    private void parseNodeInternal(Node node) {
        char c;
        String name = node.getName();
        HiLog.debug(TAG, "parsing shapeElement attr: %{public}s", name);
        List<TypedAttribute> typedAttributes = node.getTypedAttributes(this.mResourceManager);
        switch (name.hashCode()) {
            case -1383205195:
                if (name.equals("bounds")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -891980232:
                if (name.equals("stroke")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 89650992:
                if (name.equals("gradient")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 109618859:
                if (name.equals("solid")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 955046078:
                if (name.equals("corners")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        if (c == 0) {
            parseCorners(typedAttributes);
        } else if (c == 1) {
            parseColor(typedAttributes);
        } else if (c == 2) {
            parseGradient(typedAttributes);
        } else if (c == 3) {
            parseBounds(typedAttributes);
        } else if (c != 4) {
            HiLog.error(TAG, "do not support this shapeElement tag.", new Object[0]);
        } else {
            parseStroke(typedAttributes);
        }
        Node sibling = node.getSibling();
        if (sibling != null) {
            HiLog.debug(TAG, "continue to parse %{public}s", sibling.getName());
            parseNodeInternal(sibling);
        }
    }

    private void parseBounds(List<TypedAttribute> list) {
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        for (TypedAttribute typedAttribute : list) {
            if (typedAttribute != null) {
                String name = typedAttribute.getName();
                char c = 65535;
                switch (name.hashCode()) {
                    case -1383228885:
                        if (name.equals("bottom")) {
                            c = 3;
                            break;
                        }
                        break;
                    case 115029:
                        if (name.equals("top")) {
                            c = 2;
                            break;
                        }
                        break;
                    case 3317767:
                        if (name.equals("left")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 108511772:
                        if (name.equals("right")) {
                            c = 1;
                            break;
                        }
                        break;
                }
                if (c == 0) {
                    i = getDimenValueInternal(typedAttribute);
                } else if (c == 1) {
                    i3 = getDimenValueInternal(typedAttribute);
                } else if (c == 2) {
                    i2 = getDimenValueInternal(typedAttribute);
                } else if (c != 3) {
                    HiLog.error(TAG, "do not support this attr : %s", typedAttribute.getName());
                } else {
                    i4 = getDimenValueInternal(typedAttribute);
                }
                setBounds(i, i2, i3, i4);
            }
        }
    }

    private void parseGradient(List<TypedAttribute> list) {
        for (TypedAttribute typedAttribute : list) {
            if (typedAttribute != null) {
                try {
                    String name = typedAttribute.getName();
                    char c = 65535;
                    int hashCode = name.hashCode();
                    if (hashCode != -1991968940) {
                        if (hashCode == -1439500848) {
                            if (name.equals("orientation")) {
                                c = 1;
                            }
                        }
                    } else if (name.equals("shader_type")) {
                        c = 0;
                    }
                    if (c == 0) {
                        parseShaderType(typedAttribute.getStringValue());
                    } else if (c != 1) {
                        HiLog.error(TAG, "do not support this gradient tag.", new Object[0]);
                    } else {
                        setOrientation(Orientation.valueOf(typedAttribute.getStringValue().toUpperCase(Locale.ENGLISH)));
                    }
                } catch (IOException | NotExistException | WrongTypeException unused) {
                    throw new ElementScatterException("set " + typedAttribute.getName() + " failed.");
                }
            }
        }
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    private void parseShaderType(String str) {
        char c;
        HiLog.debug(TAG, "set shaderType : %{public}s", str);
        String lowerCase = str.toLowerCase(Locale.ENGLISH);
        switch (lowerCase.hashCode()) {
            case 48:
                if (lowerCase.equals("0")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 49:
                if (lowerCase.equals("1")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 50:
                if (lowerCase.equals("2")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 129565475:
                if (lowerCase.equals("sweep_gradient")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 1342493450:
                if (lowerCase.equals("linear_gradient")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 1881846096:
                if (lowerCase.equals("radial_gradient")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        if (c == 0 || c == 1) {
            setShaderType(0);
        } else if (c == 2 || c == 3) {
            setShaderType(1);
        } else if (c == 4 || c == 5) {
            setShaderType(2);
        } else {
            HiLog.error(TAG, "do not support this shader_type value: %{public}s", str);
        }
    }

    private void parseStroke(List<TypedAttribute> list) {
        int i = 0;
        int i2 = 0;
        for (TypedAttribute typedAttribute : list) {
            if (typedAttribute != null) {
                try {
                    String name = typedAttribute.getName();
                    char c = 65535;
                    int hashCode = name.hashCode();
                    if (hashCode != 94842723) {
                        if (hashCode == 113126854) {
                            if (name.equals("width")) {
                                c = 0;
                            }
                        }
                    } else if (name.equals("color")) {
                        c = 1;
                    }
                    if (c == 0) {
                        i = getDimenValueInternal(typedAttribute);
                        HiLog.debug(TAG, "set stroke width: %{public}d", Integer.valueOf(i));
                    } else if (c != 1) {
                        HiLog.error(TAG, "do not support this stroke tag.", new Object[0]);
                    } else if (typedAttribute.getType() == 1) {
                        i2 = typedAttribute.getColorValue();
                    } else {
                        i2 = AttrHelper.convertValueToColor(typedAttribute.getStringValue()).getValue();
                        HiLog.debug(TAG, "set stroke color: %{public}d", Integer.valueOf(i2));
                    }
                } catch (IOException | NumberFormatException | NotExistException | WrongTypeException unused) {
                    throw new ElementScatterException("set stroke " + typedAttribute.getName() + " failed.");
                } catch (IllegalArgumentException e) {
                    throw new ElementScatterException("stroke color is illegal!", e);
                }
            }
        }
        if (!(i == 0 || i2 == 0)) {
            HiLog.debug(TAG, "setStroke: width: %{public}d, color: %{public}d", Integer.valueOf(i), Integer.valueOf(i2));
            setStroke(i, RgbColor.fromArgbInt(i2));
        }
    }

    private RgbColor[] getColorArray(TypedAttribute typedAttribute) {
        try {
            String[] split = typedAttribute.getStringValue().split(",");
            for (int i = 0; i < split.length; i++) {
                split[i] = split[i].trim();
            }
            RgbColor[] rgbColorArr = new RgbColor[split.length];
            for (int i2 = 0; i2 < split.length; i2++) {
                rgbColorArr[i2] = RgbColor.fromArgbInt(AttrHelper.convertValueToColor(split[i2]).getValue());
            }
            return rgbColorArr;
        } catch (IOException | NotExistException | WrongTypeException unused) {
            HiLog.error(TAG, "get colors failed.", new Object[0]);
            return new RgbColor[0];
        } catch (IllegalArgumentException e) {
            throw new ElementScatterException("solid color is illegal!", e);
        }
    }

    private void parseColor(List<TypedAttribute> list) {
        for (TypedAttribute typedAttribute : list) {
            if (typedAttribute != null) {
                if ("color".equals(typedAttribute.getName())) {
                    try {
                        if (typedAttribute.getType() == 1) {
                            setRgbColor(RgbColor.fromArgbInt(typedAttribute.getColorValue()));
                        } else {
                            setRgbColor(RgbColor.fromArgbInt(AttrHelper.convertValueToColor(typedAttribute.getStringValue()).getValue()));
                        }
                    } catch (IOException | NumberFormatException | NotExistException | WrongTypeException unused) {
                        throw new ElementScatterException("set " + typedAttribute.getName() + " failed.");
                    } catch (IllegalArgumentException e) {
                        throw new ElementScatterException("solid color is illegal!", e);
                    }
                } else if ("colors".equals(typedAttribute.getName())) {
                    RgbColor[] colorArray = getColorArray(typedAttribute);
                    if (colorArray.length != 0) {
                        setRgbColors(colorArray);
                    }
                } else {
                    HiLog.error(TAG, "do not parse this solid tag: %{public}s", typedAttribute.getName());
                }
            }
        }
    }

    private void parseCorners(List<TypedAttribute> list) {
        HashMap hashMap = new HashMap();
        float[] fArr = new float[CORNER_ARRAY_LENGTH];
        for (int i = 0; i < CORNER_ARRAY_LENGTH; i++) {
            hashMap.put(RADII_ARR[i], Integer.valueOf(i));
            fArr[i] = 0.0f;
        }
        boolean z = false;
        boolean z2 = false;
        for (TypedAttribute typedAttribute : list) {
            if (typedAttribute != null) {
                try {
                    String name = typedAttribute.getName();
                    if ("radius".equals(name)) {
                        if (this.isRectangle) {
                            float dimenValueInternal = (float) getDimenValueInternal(typedAttribute);
                            setCornerRadius(dimenValueInternal);
                            HiLog.debug(TAG, "set radius: %{public}f", Float.valueOf(dimenValueInternal));
                            z2 = true;
                        } else {
                            throw new ElementScatterException("radius can only set in rectangle.");
                        }
                    } else if (!hashMap.containsKey(name)) {
                        HiLog.error(TAG, "do not support this corner tag.", new Object[0]);
                    } else if (this.isRectangle) {
                        int intValue = ((Integer) hashMap.get(name)).intValue();
                        fArr[intValue] = (float) getDimenValueInternal(typedAttribute);
                        HiLog.debug(TAG, "set %{public}s: %{public}f", name, Float.valueOf(fArr[intValue]));
                        if (!z) {
                            z = true;
                        }
                    } else {
                        throw new ElementScatterException("radii can only set in rectangle.");
                    }
                } catch (NumberFormatException e) {
                    throw new ElementScatterException("set corners " + typedAttribute.getName() + " failed.", e);
                }
            }
        }
        if (!z) {
            return;
        }
        if (z2) {
            HiLog.error(TAG, "set both radius and radii is conflictual", new Object[0]);
        } else {
            setCornerRadiiArray(fArr);
        }
    }

    private int getDimenValueInternal(TypedAttribute typedAttribute) {
        try {
            if (typedAttribute.getType() == 2) {
                return (int) (((double) typedAttribute.getFloatValue()) + 0.5d);
            }
            return AttrHelper.convertDimensionToPix(this.mContext, typedAttribute.getStringValue(), 0);
        } catch (NotExistException e) {
            throw new ElementScatterException("not exist dimenValue", e);
        } catch (IOException unused) {
            throw new ElementScatterException("get IOException");
        } catch (WrongTypeException e2) {
            throw new ElementScatterException("dimen value's type is wrong", e2);
        }
    }

    @Override // ohos.agp.components.element.Element
    public void createNativePtr(Object obj) {
        if (this.mNativeElementPtr == 0) {
            this.mNativeElementPtr = nativeGetGradientDrawableHandle();
        }
    }

    public void setShape(int i) {
        if (i == 0 || i == 1 || i == 2 || i == 3 || i == 4) {
            nativeSetShape(this.mNativeElementPtr, i);
        }
    }

    public int getShape() {
        return nativeGetShape(this.mNativeElementPtr);
    }

    public void setRgbColor(RgbColor rgbColor) {
        nativeSetColor(this.mNativeElementPtr, rgbColor.asArgbInt());
    }

    public void setRgbColors(RgbColor[] rgbColorArr) {
        int[] iArr = new int[rgbColorArr.length];
        for (int i = 0; i < rgbColorArr.length; i++) {
            iArr[i] = rgbColorArr[i].asArgbInt();
        }
        nativeSetColors(this.mNativeElementPtr, iArr);
    }

    public RgbColor[] getRgbColors() {
        int[] nativeGetColors = nativeGetColors(this.mNativeElementPtr);
        RgbColor[] rgbColorArr = new RgbColor[nativeGetColors.length];
        for (int i = 0; i < nativeGetColors.length; i++) {
            rgbColorArr[i] = RgbColor.fromArgbInt(nativeGetColors[i]);
        }
        return rgbColorArr;
    }

    public void setStroke(int i, RgbColor rgbColor) {
        nativeSetStroke(this.mNativeElementPtr, i, rgbColor.asArgbInt());
    }

    public int getStrokeWidth() {
        return nativeGetStrokeWidth(this.mNativeElementPtr);
    }

    public RgbColor getStrokeColor() {
        return RgbColor.fromArgbInt(nativeGetBorderColor(this.mNativeElementPtr));
    }

    public void setCornerRadius(float f) {
        if (f < 0.0f) {
            HiLog.error(TAG, "radius is invalid.", new Object[0]);
        } else {
            nativeSetCornerRadius(this.mNativeElementPtr, f);
        }
    }

    public float getCornerRadius() {
        return nativeGetCornerRadius(this.mNativeElementPtr);
    }

    public void setCornerRadiiArray(float[] fArr) {
        if (fArr.length != CORNER_ARRAY_LENGTH) {
            HiLog.error(TAG, "radii is invalid.", new Object[0]);
        } else {
            nativeSetCornerRadii(this.mNativeElementPtr, fArr);
        }
    }

    public float[] getCornerRadii() {
        return nativeGetCornerRadii(this.mNativeElementPtr);
    }

    public void setShaderType(int i) {
        nativeSetShaderType(this.mNativeElementPtr, i);
    }

    public int getShaderType() {
        return nativeGetShaderType(this.mNativeElementPtr);
    }

    public void setArc(Arc arc) {
        nativeSetArc(this.mNativeElementPtr, arc.getStartAngle(), arc.getSweepAngle(), arc.getUseCenter());
    }

    public Arc getArc() {
        return nativeGetArc(this.mNativeElementPtr);
    }

    public void setGradientOrientation(Orientation orientation) {
        if (orientation == null) {
            orientation = Orientation.TOP_TO_BOTTOM;
        }
        setOrientation(new Orientation[]{Orientation.TOP_TO_BOTTOM, Orientation.TOP_RIGHT_TO_BOTTOM_LEFT, Orientation.RIGHT_TO_LEFT, Orientation.BOTTOM_RIGHT_TO_TOP_LEFT, Orientation.BOTTOM_TO_TOP, Orientation.BOTTOM_LEFT_TO_TOP_RIGHT, Orientation.LEFT_TO_RIGHT, Orientation.TOP_LEFT_TO_BOTTOM_RIGHT, Orientation.TOP_START_TO_BOTTOM_END, Orientation.START_TO_END, Orientation.BOTTOM_START_TO_TOP_END, Orientation.BOTTOM_END_TO_TOP_START, Orientation.END_TO_START, Orientation.TOP_END_TO_BOTTOM_START}[orientation.ordinal()]);
    }

    public Orientation getGradientOrientation() {
        return new Orientation[]{Orientation.TOP_TO_BOTTOM, Orientation.TOP_RIGHT_TO_BOTTOM_LEFT, Orientation.RIGHT_TO_LEFT, Orientation.BOTTOM_RIGHT_TO_TOP_LEFT, Orientation.BOTTOM_TO_TOP, Orientation.BOTTOM_LEFT_TO_TOP_RIGHT, Orientation.LEFT_TO_RIGHT, Orientation.TOP_LEFT_TO_BOTTOM_RIGHT, Orientation.TOP_START_TO_BOTTOM_END, Orientation.START_TO_END, Orientation.BOTTOM_START_TO_TOP_END, Orientation.BOTTOM_END_TO_TOP_START, Orientation.END_TO_START, Orientation.TOP_END_TO_BOTTOM_START}[getOrientation().ordinal()];
    }

    public void setOrientation(Orientation orientation) {
        if (orientation == null) {
            orientation = Orientation.TOP_TO_BOTTOM;
        }
        nativeSetOrientation(this.mNativeElementPtr, orientation.ordinal());
    }

    public Orientation getOrientation() {
        switch (nativeGetOrientation(this.mNativeElementPtr)) {
            case 1:
                return Orientation.TOP_RIGHT_TO_BOTTOM_LEFT;
            case 2:
                return Orientation.RIGHT_TO_LEFT;
            case 3:
                return Orientation.BOTTOM_RIGHT_TO_TOP_LEFT;
            case 4:
                return Orientation.BOTTOM_TO_TOP;
            case 5:
                return Orientation.BOTTOM_LEFT_TO_TOP_RIGHT;
            case 6:
                return Orientation.LEFT_TO_RIGHT;
            case 7:
                return Orientation.TOP_LEFT_TO_BOTTOM_RIGHT;
            case 8:
                return Orientation.TOP_START_TO_BOTTOM_END;
            case 9:
                return Orientation.START_TO_END;
            case 10:
                return Orientation.BOTTOM_START_TO_TOP_END;
            case 11:
                return Orientation.BOTTOM_END_TO_TOP_START;
            case 12:
                return Orientation.END_TO_START;
            case 13:
                return Orientation.TOP_END_TO_BOTTOM_START;
            default:
                return Orientation.TOP_TO_BOTTOM;
        }
    }

    public void setPath(Path path) {
        if (path == null) {
            HiLog.error(TAG, "path is null.", new Object[0]);
            return;
        }
        this.mPath = path;
        nativeSetPath(this.mNativeElementPtr, this.mPath.getNativeHandle());
    }

    public Path getPath() {
        return this.mPath;
    }

    public void setDashPathEffectValues(float[] fArr, float f) {
        if (fArr.length % 2 == 0 && fArr.length >= 2 && fArr.length <= 20) {
            nativeSetDashPathEffectValues(this.mNativeElementPtr, fArr, fArr.length, f);
        }
    }
}
