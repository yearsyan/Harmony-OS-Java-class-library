package ohos.agp.components;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.components.ComponentObserverHandler;
import ohos.agp.components.Image;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.FlexElement;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.components.element.StateElement;
import ohos.agp.components.element.VectorElement;
import ohos.agp.image.PixelMapFactory;
import ohos.agp.render.PixelMapHolder;
import ohos.agp.styles.Style;
import ohos.agp.styles.attributes.ImageAttrsConstants;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.PixelMap;

public class Image extends Component {
    private static final int CLIP_DIRECTION_MASK = 3;
    public static final int CLIP_DIRECTION_NOT_SET = 0;
    public static final int CLIP_GRAVITY_NOT_SET = 0;
    public static final int CLIP_HORIZONTAL = 1;
    public static final int CLIP_VERTICAL = 2;
    private static final int CORNER_ARRAY_LENGTH = 8;
    public static final int GRAVITY_BOTTOM = 2048;
    public static final int GRAVITY_CENTER = 4096;
    private static final int GRAVITY_CENTER_MASK = 4096;
    public static final int GRAVITY_LEFT = 256;
    private static final int GRAVITY_LEFT_RIGHT_MASK = 768;
    private static final int GRAVITY_MASK = 3840;
    public static final int GRAVITY_RIGHT = 512;
    public static final int GRAVITY_TOP = 1024;
    private static final int GRAVITY_TOP_BOTTOM_MASK = 3072;
    private static final int IMAGE_CALLBACK_PARAMS_NUM = 0;
    private static final List<Class<?>> SUPPORTED_ELEMENT_TYPES = Arrays.asList(ShapeElement.class, VectorElement.class, PixelMapElement.class, StateElement.class, FlexElement.class);
    private static final HiLogLabel TAG = new HiLogLabel(3, LogDomain.END, "Image");
    private int mClipAlignment;
    private int mClipDirection;
    private Element mElement;
    private final ImageObserverHandler mImageObserverHandler;
    private PixelMapHolder mPixelMapHolder;

    public interface ImageObserver extends ComponentObserverHandler.Observer {
        void onImageChanged();
    }

    private native int nativeGetClipAlignment(long j);

    private native int nativeGetClipDirection(long j);

    private native float[] nativeGetCornerRadii(long j);

    private native float nativeGetCornerRadius(long j);

    private native long nativeGetImageHandle();

    private native int nativeGetScaleMode(long j);

    private native void nativeSetClipAlignment(long j, int i);

    private native void nativeSetClipDirection(long j, int i);

    private native void nativeSetCornerRadii(long j, float[] fArr);

    private native void nativeSetCornerRadius(long j, float f);

    private native void nativeSetImageElement(long j, long j2);

    private native void nativeSetImageHolder(long j, long j2);

    private native void nativeSetResourceCache(long j, long j2);

    private native void nativeSetScaleMode(long j, int i);

    public enum ScaleMode {
        ZOOM_CENTER(0),
        ZOOM_START(1),
        ZOOM_END(2),
        STRETCH(3),
        CENTER(4),
        INSIDE(5),
        CLIP_CENTER(6);
        
        private final int nativeValue;

        private ScaleMode(int i) {
            this.nativeValue = i;
        }

        /* access modifiers changed from: protected */
        public int getValue() {
            return this.nativeValue;
        }

        public static ScaleMode getByInt(int i) {
            return (ScaleMode) Arrays.stream(values()).filter(new Predicate(i) {
                /* class ohos.agp.components.$$Lambda$Image$ScaleMode$j9EGlu66tnY96_GZq1RQag35cw */
                private final /* synthetic */ int f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return Image.ScaleMode.lambda$getByInt$0(this.f$0, (Image.ScaleMode) obj);
                }
            }).findAny().orElse(ZOOM_CENTER);
        }

        static /* synthetic */ boolean lambda$getByInt$0(int i, ScaleMode scaleMode) {
            return scaleMode.getValue() == i;
        }
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.Component
    public void createNativePtr() {
        if (this.mNativeViewPtr == 0) {
            this.mNativeViewPtr = nativeGetImageHandle();
        }
    }

    public Image(Context context) {
        this(context, null);
    }

    public Image(Context context, AttrSet attrSet) {
        this(context, attrSet, "ImageDefaultStyle");
    }

    public Image(Context context, AttrSet attrSet, String str) {
        super(context, attrSet, str);
        this.mClipDirection = 3;
        this.mImageObserverHandler = new ImageObserverHandler();
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.Component
    public Style convertAttrToStyle(AttrSet attrSet) {
        if (this.mAttrsConstants == null) {
            this.mAttrsConstants = AttrHelper.getImageAttrsConstants();
        }
        return super.convertAttrToStyle(attrSet);
    }

    @Override // ohos.agp.components.Component
    public void applyStyle(Style style) {
        super.applyStyle(style);
        if (style.hasProperty(ImageAttrsConstants.IMAGE_SRC)) {
            Element asElement = style.getPropertyValue(ImageAttrsConstants.IMAGE_SRC).asElement();
            if (asElement instanceof PixelMapElement) {
                setPixelMap(((PixelMapElement) asElement).getPixelMap());
            } else if ((asElement instanceof VectorElement) || (asElement instanceof ShapeElement)) {
                setImageElement(asElement);
            } else {
                HiLog.error(TAG, "Unsupported drawable", new Object[0]);
            }
        }
    }

    public void setPixelMapHolder(PixelMapHolder pixelMapHolder) {
        long j = 0;
        if (this.mElement != null) {
            this.mElement = null;
            nativeSetImageElement(this.mNativeViewPtr, 0);
        }
        this.mPixelMapHolder = pixelMapHolder;
        long j2 = this.mNativeViewPtr;
        if (pixelMapHolder != null) {
            j = pixelMapHolder.getNativeHolder();
        }
        nativeSetImageHolder(j2, j);
    }

    public PixelMapHolder getPixelMapHolder() {
        return this.mPixelMapHolder;
    }

    public void setPixelMap(PixelMap pixelMap) {
        if (pixelMap == null) {
            setPixelMapHolder(null);
            return;
        }
        PixelMapHolder pixelMapHolder = this.mPixelMapHolder;
        if (pixelMapHolder != null) {
            pixelMapHolder.resetPixelMap(pixelMap);
            postLayout();
            invalidate();
            return;
        }
        setPixelMapHolder(new PixelMapHolder(pixelMap));
    }

    public PixelMap getPixelMap() {
        if (this.mPixelMapHolder == null) {
            Element element = this.mElement;
            if (element instanceof PixelMapElement) {
                return ((PixelMapElement) element).getPixelMap();
            }
        }
        PixelMapHolder pixelMapHolder = this.mPixelMapHolder;
        if (pixelMapHolder == null) {
            return null;
        }
        return pixelMapHolder.getPixelMap();
    }

    public void setImageElement(Element element) {
        if (SUPPORTED_ELEMENT_TYPES.stream().anyMatch(new Predicate() {
            /* class ohos.agp.components.$$Lambda$Image$pr8i5yV7NK06cF2DKKUcUheOD4 */

            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((Class) obj).isInstance(Element.this);
            }
        })) {
            this.mElement = element;
            nativeSetImageElement(this.mNativeViewPtr, element.getNativeElementPtr());
            if (this.mPixelMapHolder != null) {
                this.mPixelMapHolder = null;
                nativeSetImageHolder(this.mNativeViewPtr, 0);
            }
        }
    }

    public Element getImageElement() {
        return this.mElement;
    }

    private static class ImageCacheForResource {
        private static final ImageCacheForResource INSTANCE = new ImageCacheForResource();
        public static final long INVALID_CACHE = 0;

        private static native void nativeAddByteData(long j, byte[] bArr, int i);

        private static native long nativeCreateBuffer();

        private static native long nativeFindCacheById(int i);

        private static native long nativeReleaseBufferToCache(int i, long j, long j2);

        private ImageCacheForResource() {
        }

        public static ImageCacheForResource getInstance() {
            return INSTANCE;
        }

        public long find(int i) {
            return nativeFindCacheById(i);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:34:0x0071, code lost:
            r9 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
            r8.close();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x0076, code lost:
            r8 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x0077, code lost:
            r7.addSuppressed(r8);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x007a, code lost:
            throw r9;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public long addCache(int r7, ohos.app.Context r8, ohos.agp.components.Image r9) {
            /*
            // Method dump skipped, instructions count: 147
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.agp.components.Image.ImageCacheForResource.addCache(int, ohos.app.Context, ohos.agp.components.Image):long");
        }
    }

    public void setImageAndDecodeBounds(int i) {
        if (this.mContext == null) {
            HiLog.error(TAG, " mContext is null!", new Object[0]);
            return;
        }
        this.mPixelMapHolder = null;
        if (this.mElement != null) {
            this.mElement = null;
            nativeSetImageElement(this.mNativeViewPtr, 0);
        }
        long find = ImageCacheForResource.getInstance().find(i);
        if (find == 0) {
            find = ImageCacheForResource.getInstance().addCache(i, this.mContext, this);
            if (find == 0) {
                return;
            }
        }
        nativeSetResourceCache(this.mNativeViewPtr, find);
    }

    public void setPixelMap(int i) {
        setPixelMap(PixelMapFactory.createFromResourceId(this.mContext, i).orElse(null));
    }

    public void setClipDirection(int i) {
        int i2 = i & 3;
        if (i2 == 0) {
            i2 = 3;
        }
        this.mClipDirection = i2;
        nativeSetClipDirection(this.mNativeViewPtr, this.mClipDirection);
        setClipAlignment(this.mClipAlignment);
    }

    public int getClipDirection() {
        return nativeGetClipDirection(this.mNativeViewPtr);
    }

    @Deprecated
    public void setClipGravity(int i) {
        this.mClipAlignment = i;
        nativeSetClipAlignment(this.mNativeViewPtr, maskClipAlignment(i));
    }

    @Deprecated
    public int getClipGravity() {
        return nativeGetClipAlignment(this.mNativeViewPtr);
    }

    public void setClipAlignment(int i) {
        this.mClipAlignment = i;
        nativeSetClipAlignment(this.mNativeViewPtr, maskClipAlignment(i));
    }

    public int getClipAlignment() {
        return nativeGetClipAlignment(this.mNativeViewPtr);
    }

    public void setScaleMode(ScaleMode scaleMode) {
        if (scaleMode != null) {
            nativeSetScaleMode(this.mNativeViewPtr, scaleMode.getValue());
        }
    }

    public ScaleMode getScaleMode() {
        return ScaleMode.getByInt(nativeGetScaleMode(this.mNativeViewPtr));
    }

    public void setCornerRadius(float f) {
        if (f < 0.0f) {
            HiLog.error(TAG, "radius is invalid.", new Object[0]);
        } else {
            nativeSetCornerRadius(this.mNativeViewPtr, f);
        }
    }

    public float getCornerRadius() {
        return nativeGetCornerRadius(this.mNativeViewPtr);
    }

    public void setCornerRadii(float[] fArr) {
        if (fArr.length != 8) {
            HiLog.error(TAG, "radii is invalid.", new Object[0]);
        } else {
            nativeSetCornerRadii(this.mNativeViewPtr, fArr);
        }
    }

    public float[] getCornerRadii() {
        return nativeGetCornerRadii(this.mNativeViewPtr);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x002d, code lost:
        if (r0 != false) goto L_0x0045;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001a, code lost:
        if (r0 != false) goto L_0x0045;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int maskClipAlignment(int r7) {
        /*
            r6 = this;
            r0 = r7 & 4096(0x1000, float:5.74E-42)
            r1 = 4096(0x1000, float:5.74E-42)
            if (r0 == 0) goto L_0x0007
            goto L_0x0046
        L_0x0007:
            r7 = r7 & 3840(0xf00, float:5.381E-42)
            int r6 = r6.mClipDirection
            r0 = 0
            r2 = 768(0x300, float:1.076E-42)
            r3 = 1
            if (r6 != r3) goto L_0x001f
            r6 = r7 & 768(0x300, float:1.076E-42)
            if (r6 == 0) goto L_0x0016
            goto L_0x0017
        L_0x0016:
            r6 = r1
        L_0x0017:
            if (r6 != r2) goto L_0x001a
            r0 = r3
        L_0x001a:
            if (r0 == 0) goto L_0x001d
            goto L_0x0045
        L_0x001d:
            r0 = r6
            goto L_0x0046
        L_0x001f:
            r4 = 2
            r5 = 3072(0xc00, float:4.305E-42)
            if (r6 != r4) goto L_0x0030
            r6 = r7 & 3072(0xc00, float:4.305E-42)
            if (r6 == 0) goto L_0x0029
            goto L_0x002a
        L_0x0029:
            r6 = r1
        L_0x002a:
            if (r6 != r5) goto L_0x002d
            r0 = r3
        L_0x002d:
            if (r0 == 0) goto L_0x001d
            goto L_0x0045
        L_0x0030:
            r4 = 3
            if (r6 != r4) goto L_0x0045
            r6 = r7 & 3072(0xc00, float:4.305E-42)
            if (r6 != r5) goto L_0x0039
            r6 = r3
            goto L_0x003a
        L_0x0039:
            r6 = r0
        L_0x003a:
            r4 = r7 & 768(0x300, float:1.076E-42)
            if (r4 != r2) goto L_0x003f
            r0 = r3
        L_0x003f:
            r6 = r6 | r0
            if (r6 == 0) goto L_0x0043
            goto L_0x0045
        L_0x0043:
            r0 = r7
            goto L_0x0046
        L_0x0045:
            r0 = r1
        L_0x0046:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.agp.components.Image.maskClipAlignment(int):int");
    }

    public void addImageObserver(ImageObserver imageObserver) {
        if (this.mImageObserverHandler.getObserversCount() == 0) {
            addObserverHandler(this.mImageObserverHandler);
        }
        this.mImageObserverHandler.addObserver(imageObserver);
    }

    public void removeImageObserver(ImageObserver imageObserver) {
        this.mImageObserverHandler.removeObserver(imageObserver);
        if (this.mImageObserverHandler.getObserversCount() == 0) {
            removeObserverHandler(this.mImageObserverHandler);
        }
    }

    private static class ImageObserverHandler extends ComponentObserverHandler<ImageObserver> {
        private ImageObserverHandler() {
        }

        @Override // ohos.agp.components.ComponentObserverHandler
        public void onChange(int[] iArr) {
            super.onChange(iArr);
            if (iArr.length > 0) {
                HiLog.error(Image.TAG, "Illegal return params, should be size %{public}d", 0);
                return;
            }
            for (ImageObserver imageObserver : this.mObservers) {
                imageObserver.onImageChanged();
            }
        }
    }
}
