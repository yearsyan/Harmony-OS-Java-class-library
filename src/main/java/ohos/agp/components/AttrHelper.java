package ohos.agp.components;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.function.BiConsumer;
import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.colors.RgbColor;
import ohos.agp.colors.RgbPalette;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.image.PixelMapFactory;
import ohos.agp.styles.attributes.AbsButtonAttrsConstants;
import ohos.agp.styles.attributes.AbsSeekBarAttrsConstants;
import ohos.agp.styles.attributes.CheckboxAttrsConstants;
import ohos.agp.styles.attributes.ChronometerAttrsConstants;
import ohos.agp.styles.attributes.ClockAttrsConstants;
import ohos.agp.styles.attributes.DatePickerAttrsConstants;
import ohos.agp.styles.attributes.DependentLayoutAttrsConstants;
import ohos.agp.styles.attributes.DirectionalLayoutAttrsConstants;
import ohos.agp.styles.attributes.ImageAttrsConstants;
import ohos.agp.styles.attributes.ListContainerAttrsConstants;
import ohos.agp.styles.attributes.MagicLayoutAttrsConstants;
import ohos.agp.styles.attributes.PageSliderAttrsConstants;
import ohos.agp.styles.attributes.PageSliderIndicatorAttrsConstants;
import ohos.agp.styles.attributes.PickerAttrsConstants;
import ohos.agp.styles.attributes.ProgressBarAttrsConstants;
import ohos.agp.styles.attributes.RadioButtonAttrsConstants;
import ohos.agp.styles.attributes.RatingAttrsConstants;
import ohos.agp.styles.attributes.RoundProgressBarAttrsConstants;
import ohos.agp.styles.attributes.ScrollViewAttrsConstants;
import ohos.agp.styles.attributes.SearchViewAttrsConstants;
import ohos.agp.styles.attributes.SlideDrawerAttrsConstants;
import ohos.agp.styles.attributes.StackLayoutAttrsConstants;
import ohos.agp.styles.attributes.SwitchAttrsConstants;
import ohos.agp.styles.attributes.TabAttrsConstants;
import ohos.agp.styles.attributes.TabListAttrsConstants;
import ohos.agp.styles.attributes.TableLayoutAttrsConstants;
import ohos.agp.styles.attributes.TextAttrsConstants;
import ohos.agp.styles.attributes.TextFieldAttrsConstants;
import ohos.agp.styles.attributes.TimePickerAttrsConstants;
import ohos.agp.styles.attributes.ToggleButtonAttrsConstants;
import ohos.agp.styles.attributes.ViewAttrsConstants;
import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import ohos.global.resource.solidxml.Pattern;
import ohos.global.resource.solidxml.TypedAttribute;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class AttrHelper {
    private static final float DEFAULT_DENSITY = 0.0f;
    private static final int DEFAULT_DENSITY_DPI = 160;
    private static final float DEFAULT_FONT_RATIO = 1.0f;
    private static final float DIMEN_CONVERT_FACTOR = 0.5f;
    private static final String FP = "fp";
    private static final String PX = "px";
    private static final HiLogLabel TAG = new HiLogLabel(3, LogDomain.END, "AGP_AttrHelper");
    private static final String VP = "vp";
    private static volatile AbsButtonAttrsConstants sAbsButtonAttrsConstants;
    private static volatile AbsSeekBarAttrsConstants sAbsSeekBarAttrsConstants;
    private static volatile CheckboxAttrsConstants sCheckboxAttrsConstants;
    private static volatile ChronometerAttrsConstants sChronometerAttrsConstants;
    private static volatile ClockAttrsConstants sClockAttrsConstants;
    private static volatile DatePickerAttrsConstants sDatePickerAttrsConstants;
    private static float sDensity = 0.0f;
    private static volatile DependentLayoutAttrsConstants sDependentLayoutAttrsConstants;
    private static volatile DirectionalLayoutAttrsConstants sDirectionalLayoutAttrsConstants;
    private static float sFontRatio = 1.0f;
    private static volatile ImageAttrsConstants sImageAttrsConstants;
    private static volatile ListContainerAttrsConstants sListContainerAttrsConstants;
    private static volatile MagicLayoutAttrsConstants sMagicLayoutAttrsConstants;
    private static volatile PageSliderAttrsConstants sPageSliderAttrsConstants;
    private static volatile PageSliderIndicatorAttrsConstants sPageSliderIndicatorAttrsConstants;
    private static volatile PickerAttrsConstants sPickerAttrsConstants;
    private static volatile ProgressBarAttrsConstants sProgressBarAttrsConstants;
    private static volatile RadioButtonAttrsConstants sRadioButtonAttrsConstants;
    private static volatile RatingAttrsConstants sRatingAttrsConstants;
    private static volatile RoundProgressBarAttrsConstants sRoundProgressBarAttrsConstants;
    private static volatile ScrollViewAttrsConstants sScrollViewAttrsConstants;
    private static volatile SearchViewAttrsConstants sSearchViewAttrsConstants;
    private static volatile SlideDrawerAttrsConstants sSlideDrawerAttrsConstants;
    private static volatile StackLayoutAttrsConstants sStackLayoutAttrsConstants;
    private static volatile SwitchAttrsConstants sSwitchAttrsConstants;
    private static volatile TabAttrsConstants sTabAttrsConstants;
    private static volatile TabListAttrsConstants sTabListAttrsConstants;
    private static volatile TableLayoutAttrsConstants sTableLayoutAttrsConstants;
    private static volatile TextAttrsConstants sTextAttrsConstants;
    private static volatile TextFieldAttrsConstants sTextFieldAttrsConstants;
    private static volatile TimePickerAttrsConstants sTimePickerAttrsConstants;
    private static volatile ToggleButtonAttrsConstants sToggleButtonAttrsConstants;
    private static volatile ViewAttrsConstants sViewAttrsConstants;

    private static int dimensionToPixelSizeInternal(float f) {
        int i = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
        int i2 = (int) (i >= 0 ? f + 0.5f : f - 0.5f);
        if (i2 != 0) {
            return i2;
        }
        if (i == 0) {
            return 0;
        }
        return i > 0 ? 1 : -1;
    }

    public static float getDensity(Context context) {
        ResourceManager resourceManager;
        float f = sDensity;
        if (f != 0.0f) {
            return f;
        }
        if (!(context == null || (resourceManager = context.getResourceManager()) == null || resourceManager.getDeviceCapability() == null)) {
            sDensity = ((float) resourceManager.getDeviceCapability().screenDensity) / 160.0f;
        }
        return sDensity;
    }

    public static float getFontRatio(Context context) {
        ResourceManager resourceManager;
        if (!(context == null || (resourceManager = context.getResourceManager()) == null || resourceManager.getDeviceCapability() == null)) {
            sFontRatio = resourceManager.getConfiguration().fontRatio;
        }
        return sFontRatio;
    }

    public static int convertValueToInt(String str, int i) {
        if (str == null) {
            return i;
        }
        try {
            return Long.decode(str).intValue();
        } catch (NumberFormatException unused) {
            return i;
        }
    }

    public static boolean convertValueToBoolean(String str, boolean z) {
        return str == null ? z : !str.equalsIgnoreCase("false");
    }

    public static float convertValueToFloat(String str, float f) {
        if (str == null) {
            return f;
        }
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException unused) {
            return f;
        }
    }

    public static long convertValueToLong(String str, long j) {
        if (str == null) {
            return j;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException unused) {
            return j;
        }
    }

    public static Element convertValueToElement(String str) {
        try {
            ShapeElement shapeElement = new ShapeElement();
            shapeElement.setRgbColor(RgbColor.fromArgbInt(RgbPalette.parse(str)));
            return shapeElement;
        } catch (IllegalArgumentException unused) {
            if (isPathFormat(str)) {
                return new PixelMapElement(PixelMapFactory.createFromPath(str));
            }
            return null;
        }
    }

    private static boolean isPathFormat(String str) {
        try {
            Paths.get(str, new String[0]);
            return true;
        } catch (NullPointerException | InvalidPathException unused) {
            return false;
        }
    }

    private static boolean isDataBindingExpression(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        if ((str.startsWith("${") || str.startsWith("$={")) && str.endsWith("}")) {
            return true;
        }
        return false;
    }

    public static Color convertValueToColor(String str) {
        if (isDataBindingExpression(str)) {
            return Color.BLACK;
        }
        return new Color(RgbPalette.parse(str));
    }

    public static int convertDimensionToPix(String str, float f, int i) {
        String str2;
        int i2;
        if (str == null) {
            return i;
        }
        int length = str.length();
        if (!Character.isLowerCase(str.charAt(length - 1)) || length - 2 <= 0) {
            str2 = "";
        } else {
            str2 = str.substring(i2);
            str = str.substring(0, i2);
        }
        try {
            float parseFloat = Float.parseFloat(str);
            if ("".equals(str2) || PX.equals(str2)) {
                return (int) parseFloat;
            }
            if (!VP.equals(str2) && !FP.equals(str2)) {
                return i;
            }
            if (f == 0.0f) {
                return (int) parseFloat;
            }
            return vp2px(parseFloat, f);
        } catch (NumberFormatException unused) {
            return i;
        }
    }

    public static int convertDimensionToPix(Context context, String str, int i) {
        String str2;
        int i2;
        if (str == null) {
            return i;
        }
        int length = str.length();
        if (!Character.isLowerCase(str.charAt(length - 1)) || (i2 = length - 2) <= 0) {
            str2 = "";
        } else {
            str2 = str.substring(i2);
            str = str.substring(0, i2);
        }
        try {
            float parseFloat = Float.parseFloat(str);
            char c = 65535;
            int hashCode = str2.hashCode();
            if (hashCode != 0) {
                if (hashCode != 3274) {
                    if (hashCode != 3592) {
                        if (hashCode == 3770 && str2.equals(VP)) {
                            c = 2;
                        }
                    } else if (str2.equals(PX)) {
                        c = 1;
                    }
                } else if (str2.equals(FP)) {
                    c = 3;
                }
            } else if (str2.equals("")) {
                c = 0;
            }
            if (c == 0 || c == 1) {
                return (int) parseFloat;
            }
            if (c == 2) {
                return vp2px(parseFloat, context);
            }
            if (c != 3) {
                return i;
            }
            return fp2px(parseFloat, context);
        } catch (NumberFormatException unused) {
            return i;
        }
    }

    public static int vp2px(float f, float f2) {
        return dimensionToPixelSizeInternal(f * f2);
    }

    public static int vp2px(float f, Context context) {
        return vp2px(f, getDensity(context));
    }

    public static int fp2px(float f, float f2) {
        return fp2px(f, f2, 1.0f);
    }

    public static int fp2px(float f, float f2, float f3) {
        return dimensionToPixelSizeInternal(f * f2 * f3);
    }

    public static int fp2px(float f, Context context) {
        return fp2px(f, getDensity(context), getFontRatio(context));
    }

    public static AttrSet mergeStyle(Context context, AttrSet attrSet, int i) {
        Pattern pattern;
        if (attrSet == null) {
            attrSet = new AttrSetImpl();
        }
        if (context == null) {
            return attrSet;
        }
        Pattern pattern2 = null;
        ResourceManager resourceManager = context.getResourceManager();
        if (!(resourceManager == null || i == 0)) {
            try {
                pattern2 = resourceManager.getElement(i).getPattern();
            } catch (IOException | NotExistException | WrongTypeException unused) {
                HiLog.debug(TAG, "no default theme!", new Object[0]);
            }
        }
        if (pattern2 == null) {
            try {
                pattern = context.getPattern();
            } catch (IllegalArgumentException unused2) {
                HiLog.error(TAG, "Context getTheme failed!", new Object[0]);
                return attrSet;
            }
        } else {
            pattern = context.getCombinedPattern(pattern2);
        }
        if (pattern == null) {
            return attrSet;
        }
        AttrSetImpl attrSetImpl = new AttrSetImpl(attrSet);
        pattern.getPatternHash().forEach(new BiConsumer(context) {
            /* class ohos.agp.components.$$Lambda$AttrHelper$FIlldv3eNdNE6yOahGVYsD0YDP4 */
            private final /* synthetic */ Context f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                AttrHelper.lambda$mergeStyle$0(AttrSetImpl.this, this.f$1, (String) obj, (TypedAttribute) obj2);
            }
        });
        return attrSetImpl;
    }

    static /* synthetic */ void lambda$mergeStyle$0(AttrSetImpl attrSetImpl, Context context, String str, TypedAttribute typedAttribute) {
        if (!attrSetImpl.getAttr(str).isPresent()) {
            attrSetImpl.addAttr(new AttrImpl(str, typedAttribute, context));
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0069, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x006a, code lost:
        if (r4 != null) goto L_0x006c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        r4.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0070, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0071, code lost:
        r0.addSuppressed(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0074, code lost:
        throw r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static ohos.agp.components.element.Element getElementFromPath(java.lang.String r4, ohos.global.resource.ResourceManager r5) {
        /*
        // Method dump skipped, instructions count: 127
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.agp.components.AttrHelper.getElementFromPath(java.lang.String, ohos.global.resource.ResourceManager):ohos.agp.components.element.Element");
    }

    public static ViewAttrsConstants getComponentAttrsConstants() {
        if (sViewAttrsConstants == null) {
            sViewAttrsConstants = new ViewAttrsConstants();
        }
        return sViewAttrsConstants;
    }

    public static ImageAttrsConstants getImageAttrsConstants() {
        if (sImageAttrsConstants == null) {
            sImageAttrsConstants = new ImageAttrsConstants();
        }
        return sImageAttrsConstants;
    }

    public static TextAttrsConstants getTextAttrsConstants() {
        if (sTextAttrsConstants == null) {
            sTextAttrsConstants = new TextAttrsConstants();
        }
        return sTextAttrsConstants;
    }

    public static AbsButtonAttrsConstants getAbsButtonAttrsConstants() {
        if (sAbsButtonAttrsConstants == null) {
            sAbsButtonAttrsConstants = new AbsButtonAttrsConstants();
        }
        return sAbsButtonAttrsConstants;
    }

    public static AbsSeekBarAttrsConstants getAbsSeekBarAttrsConstants() {
        if (sAbsSeekBarAttrsConstants == null) {
            sAbsSeekBarAttrsConstants = new AbsSeekBarAttrsConstants();
        }
        return sAbsSeekBarAttrsConstants;
    }

    public static MagicLayoutAttrsConstants getMagicLayoutAttrsConstants() {
        if (sMagicLayoutAttrsConstants == null) {
            sMagicLayoutAttrsConstants = new MagicLayoutAttrsConstants();
        }
        return sMagicLayoutAttrsConstants;
    }

    public static CheckboxAttrsConstants getCheckboxAttrsConstants() {
        if (sCheckboxAttrsConstants == null) {
            sCheckboxAttrsConstants = new CheckboxAttrsConstants();
        }
        return sCheckboxAttrsConstants;
    }

    public static ClockAttrsConstants getClockAttrsConstants() {
        if (sClockAttrsConstants == null) {
            sClockAttrsConstants = new ClockAttrsConstants();
        }
        return sClockAttrsConstants;
    }

    public static DatePickerAttrsConstants getDatePickerAttrsConstants() {
        if (sDatePickerAttrsConstants == null) {
            sDatePickerAttrsConstants = new DatePickerAttrsConstants();
        }
        return sDatePickerAttrsConstants;
    }

    public static DependentLayoutAttrsConstants getDependentLayoutAttrsConstants() {
        if (sDependentLayoutAttrsConstants == null) {
            sDependentLayoutAttrsConstants = new DependentLayoutAttrsConstants();
        }
        return sDependentLayoutAttrsConstants;
    }

    public static DirectionalLayoutAttrsConstants getDirectionalLayoutAttrsConstants() {
        if (sDirectionalLayoutAttrsConstants == null) {
            sDirectionalLayoutAttrsConstants = new DirectionalLayoutAttrsConstants();
        }
        return sDirectionalLayoutAttrsConstants;
    }

    public static TableLayoutAttrsConstants getTableLayoutAttrsConstants() {
        if (sTableLayoutAttrsConstants == null) {
            sTableLayoutAttrsConstants = new TableLayoutAttrsConstants();
        }
        return sTableLayoutAttrsConstants;
    }

    public static ListContainerAttrsConstants getListContainerAttrsConstants() {
        if (sListContainerAttrsConstants == null) {
            sListContainerAttrsConstants = new ListContainerAttrsConstants();
        }
        return sListContainerAttrsConstants;
    }

    public static PageSliderAttrsConstants getPageSliderAttrsConstants() {
        if (sPageSliderAttrsConstants == null) {
            sPageSliderAttrsConstants = new PageSliderAttrsConstants();
        }
        return sPageSliderAttrsConstants;
    }

    public static PageSliderIndicatorAttrsConstants getPageSliderIndicatorAttrsConstants() {
        if (sPageSliderIndicatorAttrsConstants == null) {
            sPageSliderIndicatorAttrsConstants = new PageSliderIndicatorAttrsConstants();
        }
        return sPageSliderIndicatorAttrsConstants;
    }

    public static PickerAttrsConstants getPickerAttrsConstants() {
        if (sPickerAttrsConstants == null) {
            sPickerAttrsConstants = new PickerAttrsConstants();
        }
        return sPickerAttrsConstants;
    }

    public static ProgressBarAttrsConstants getProgressBarAttrsConstants() {
        if (sProgressBarAttrsConstants == null) {
            sProgressBarAttrsConstants = new ProgressBarAttrsConstants();
        }
        return sProgressBarAttrsConstants;
    }

    public static RadioButtonAttrsConstants getRadioButtonAttrsConstants() {
        if (sRadioButtonAttrsConstants == null) {
            sRadioButtonAttrsConstants = new RadioButtonAttrsConstants();
        }
        return sRadioButtonAttrsConstants;
    }

    public static RatingAttrsConstants getRatingAttrsConstants() {
        if (sRatingAttrsConstants == null) {
            sRatingAttrsConstants = new RatingAttrsConstants();
        }
        return sRatingAttrsConstants;
    }

    public static RoundProgressBarAttrsConstants getRoundProgressBarAttrsConstants() {
        if (sRoundProgressBarAttrsConstants == null) {
            sRoundProgressBarAttrsConstants = new RoundProgressBarAttrsConstants();
        }
        return sRoundProgressBarAttrsConstants;
    }

    public static ScrollViewAttrsConstants getScrollViewAttrsConstants() {
        if (sScrollViewAttrsConstants == null) {
            sScrollViewAttrsConstants = new ScrollViewAttrsConstants();
        }
        return sScrollViewAttrsConstants;
    }

    public static SearchViewAttrsConstants getSearchViewAttrsConstants() {
        if (sSearchViewAttrsConstants == null) {
            sSearchViewAttrsConstants = new SearchViewAttrsConstants();
        }
        return sSearchViewAttrsConstants;
    }

    public static SlideDrawerAttrsConstants getSlideDrawerAttrsConstants() {
        if (sSlideDrawerAttrsConstants == null) {
            sSlideDrawerAttrsConstants = new SlideDrawerAttrsConstants();
        }
        return sSlideDrawerAttrsConstants;
    }

    public static StackLayoutAttrsConstants getStackLayoutAttrsConstants() {
        if (sStackLayoutAttrsConstants == null) {
            sStackLayoutAttrsConstants = new StackLayoutAttrsConstants();
        }
        return sStackLayoutAttrsConstants;
    }

    public static SwitchAttrsConstants getSwitchAttrsConstants() {
        if (sSwitchAttrsConstants == null) {
            sSwitchAttrsConstants = new SwitchAttrsConstants();
        }
        return sSwitchAttrsConstants;
    }

    public static TabAttrsConstants getTabAttrsConstants() {
        if (sTabAttrsConstants == null) {
            sTabAttrsConstants = new TabAttrsConstants();
        }
        return sTabAttrsConstants;
    }

    public static TabListAttrsConstants getTabListAttrsConstants() {
        if (sTabListAttrsConstants == null) {
            sTabListAttrsConstants = new TabListAttrsConstants();
        }
        return sTabListAttrsConstants;
    }

    public static TextFieldAttrsConstants getTextFieldAttrsConstants() {
        if (sTextFieldAttrsConstants == null) {
            sTextFieldAttrsConstants = new TextFieldAttrsConstants();
        }
        return sTextFieldAttrsConstants;
    }

    public static ChronometerAttrsConstants getChronometerAttrsConstants() {
        if (sChronometerAttrsConstants == null) {
            sChronometerAttrsConstants = new ChronometerAttrsConstants();
        }
        return sChronometerAttrsConstants;
    }

    public static TimePickerAttrsConstants getTimePickerAttrsConstants() {
        if (sTimePickerAttrsConstants == null) {
            sTimePickerAttrsConstants = new TimePickerAttrsConstants();
        }
        return sTimePickerAttrsConstants;
    }

    public static ToggleButtonAttrsConstants getToggleButtonAttrsConstants() {
        if (sToggleButtonAttrsConstants == null) {
            sToggleButtonAttrsConstants = new ToggleButtonAttrsConstants();
        }
        return sToggleButtonAttrsConstants;
    }
}
