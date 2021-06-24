package ohos.agp.render.layoutboost;

import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.colors.RgbColor;
import ohos.agp.components.AttrHelper;
import ohos.agp.components.Text;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ElementScatter;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.text.Font;
import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.hiviewdfx.HiLogLabel;

public class LayoutBoostUtils {
    private static final String ELEMENT_STARTWITH_COLOR = "@color";
    private static final String ELEMENT_STARTWITH_GRAPHIC = "@graphic";
    private static final String ELEMENT_STARTWITH_MEDIA = "@media";
    private static final String IMAGE_FORMAT = "image/png";
    private static final HiLogLabel TAG = new HiLogLabel(3, LogDomain.END, "LayoutBosstUtils");

    public static int getDimensionValue(Context context, String str) {
        return AttrHelper.convertDimensionToPix(str, AttrHelper.getDensity(context), 0);
    }

    public static Element getElement(Context context, String str, int i, String str2) {
        if (str.startsWith(ELEMENT_STARTWITH_GRAPHIC)) {
            return ElementScatter.getInstance(context).parse(i);
        }
        if (str.startsWith(ELEMENT_STARTWITH_MEDIA)) {
            return getElementFromPath(str2, context.getResourceManager());
        }
        if (!str.startsWith(ELEMENT_STARTWITH_COLOR)) {
            return AttrHelper.convertValueToElement(str);
        }
        ShapeElement shapeElement = new ShapeElement();
        shapeElement.setRgbColor(RgbColor.fromArgbInt(i));
        return shapeElement;
    }

    public static Color getColorValue(String str) {
        return AttrHelper.convertValueToColor(str);
    }

    public static Font getTextFont(Text text, String str) {
        return new Font.Builder(str).setWeight(text.getFont().getWeight()).makeItalic(text.getFont().isItalic()).build();
    }

    public static Font getTextWeight(Text text, int i) {
        return new Font.Builder("").setWeight(i).makeItalic(text.getFont().isItalic()).build();
    }

    public static Font getItalic(Text text, Boolean bool) {
        return new Font.Builder("").setWeight(text.getFont().getWeight()).makeItalic(bool.booleanValue()).build();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x004e, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        r4.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0053, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0054, code lost:
        r0.addSuppressed(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0057, code lost:
        throw r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static ohos.agp.components.element.Element getElementFromPath(java.lang.String r4, ohos.global.resource.ResourceManager r5) {
        /*
            ohos.media.image.ImageSource$SourceOptions r0 = new ohos.media.image.ImageSource$SourceOptions
            r0.<init>()
            java.lang.String r1 = "image/png"
            r0.formatHint = r1
            ohos.media.image.ImageSource$DecodingOptions r1 = new ohos.media.image.ImageSource$DecodingOptions
            r1.<init>()
            ohos.media.image.common.Size r2 = new ohos.media.image.common.Size
            r3 = 0
            r2.<init>(r3, r3)
            r1.desiredSize = r2
            ohos.media.image.common.Rect r2 = new ohos.media.image.common.Rect
            r2.<init>(r3, r3, r3, r3)
            r1.desiredRegion = r2
            ohos.media.image.common.PixelFormat r2 = ohos.media.image.common.PixelFormat.ARGB_8888
            r1.desiredPixelFormat = r2
            ohos.global.resource.RawFileEntry r4 = r5.getRawFileEntry(r4)
            r5 = 0
            if (r4 != 0) goto L_0x0029
            return r5
        L_0x0029:
            ohos.global.resource.Resource r4 = r4.openRawFile()     // Catch:{ IOException -> 0x0058 }
            if (r4 != 0) goto L_0x0035
            if (r4 == 0) goto L_0x0034
            r4.close()     // Catch:{ IOException -> 0x0058 }
        L_0x0034:
            return r5
        L_0x0035:
            ohos.media.image.ImageSource r0 = ohos.media.image.ImageSource.create(r4, r0)     // Catch:{ all -> 0x004c }
            if (r0 != 0) goto L_0x003f
            r4.close()
            return r5
        L_0x003f:
            ohos.agp.components.element.PixelMapElement r2 = new ohos.agp.components.element.PixelMapElement
            ohos.media.image.PixelMap r0 = r0.createPixelmap(r1)
            r2.<init>(r0)
            r4.close()
            return r2
        L_0x004c:
            r0 = move-exception
            throw r0     // Catch:{ all -> 0x004e }
        L_0x004e:
            r1 = move-exception
            r4.close()     // Catch:{ all -> 0x0053 }
            goto L_0x0057
        L_0x0053:
            r4 = move-exception
            r0.addSuppressed(r4)
        L_0x0057:
            throw r1
        L_0x0058:
            ohos.hiviewdfx.HiLogLabel r4 = ohos.agp.render.layoutboost.LayoutBoostUtils.TAG
            java.lang.Object[] r0 = new java.lang.Object[r3]
            java.lang.String r1 = "Get element from path failed"
            ohos.hiviewdfx.HiLog.error(r4, r1, r0)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.agp.render.layoutboost.LayoutBoostUtils.getElementFromPath(java.lang.String, ohos.global.resource.ResourceManager):ohos.agp.components.element.Element");
    }
}
