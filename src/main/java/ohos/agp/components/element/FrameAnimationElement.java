package ohos.agp.components.element;

import java.io.IOException;
import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.components.element.ElementContainer;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import ohos.global.resource.solidxml.Node;
import ohos.global.resource.solidxml.TypedAttribute;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class FrameAnimationElement extends ElementContainer {
    private static final HiLogLabel TAG = new HiLogLabel(3, LogDomain.END, "AGP_AnimationDrawable");
    private AnimationState mAnimationState;
    private Context mContext;
    private ResourceManager mResourceManager;

    /* access modifiers changed from: private */
    public static native void nativeAddFrame(long j, long j2, int i);

    private native long nativeGetAnimationDrawableHandle();

    private static native long nativeGetAnimationDrawableStateHandle(long j);

    private native boolean nativeGetDisposeMode(long j);

    private native int nativeGetFrameDuration(long j, int i);

    private native int nativeGetPreDecodeFrames(long j);

    private native void nativeSetDisposeMode(long j, boolean z);

    private native void nativeSetOneShot(long j, boolean z);

    private native boolean nativeSetPreDecodeFrames(long j, int i);

    private native void nativeStart(long j);

    private native void nativeStop(long j);

    public FrameAnimationElement() {
        if (this.mElementState == null || !(this.mElementState instanceof AnimationState)) {
            this.mAnimationState = new AnimationState(this);
            setElementState(this.mAnimationState);
            return;
        }
        this.mAnimationState = (AnimationState) this.mElementState;
    }

    public FrameAnimationElement(Context context, int i) {
        if (context != null) {
            if (this.mElementState == null || !(this.mElementState instanceof AnimationState)) {
                this.mAnimationState = new AnimationState(this);
                setElementState(this.mAnimationState);
            } else {
                this.mAnimationState = (AnimationState) this.mElementState;
            }
            parseXMLNode(context, ElementScatter.getInstance(context).getRootNodeFromXmlId(i));
            return;
        }
        throw new ElementScatterException("context is null");
    }

    /* access modifiers changed from: protected */
    @Override // ohos.agp.components.element.ElementContainer
    public AnimationState createState() {
        return new AnimationState(this);
    }

    @Override // ohos.agp.components.element.Element, ohos.agp.components.element.ElementContainer
    public void createNativePtr(Object obj) {
        if (this.mNativeElementPtr == 0) {
            this.mNativeElementPtr = nativeGetAnimationDrawableHandle();
        }
    }

    public void start() {
        nativeStart(this.mNativeElementPtr);
    }

    public void stop() {
        nativeStop(this.mNativeElementPtr);
    }

    public int getNumberOfFrames() {
        return this.mAnimationState.getChildElementCount();
    }

    public Element getFrame(int i) {
        return this.mAnimationState.getChildElement(i);
    }

    public int getDuration(int i) {
        return nativeGetFrameDuration(this.mNativeElementPtr, i);
    }

    public boolean isOneShot() {
        return this.mAnimationState.mOneShot;
    }

    public void setOneShot(boolean z) {
        this.mAnimationState.mOneShot = z;
        nativeSetOneShot(this.mNativeElementPtr, z);
    }

    public void addFrame(Element element, int i) {
        this.mAnimationState.addFrame(element, i);
    }

    /* access modifiers changed from: private */
    public static class AnimationState extends ElementContainer.ElementState {
        private boolean mOneShot = false;

        AnimationState(FrameAnimationElement frameAnimationElement) {
            super(frameAnimationElement);
        }

        /* access modifiers changed from: package-private */
        public void addFrame(Element element, int i) {
            this.mElementList.add(element);
            FrameAnimationElement.nativeAddFrame(this.mNativePtr, element.mNativeElementPtr, i);
        }
    }

    @Override // ohos.agp.components.element.ElementContainer
    public void setElementState(ElementContainer.ElementState elementState) {
        super.setElementState(elementState);
        if (elementState instanceof AnimationState) {
            this.mAnimationState = (AnimationState) elementState;
        }
    }

    @Override // ohos.agp.components.element.Element
    public void parseXMLNode(Context context, Node node) {
        super.parseXMLNode(context, node);
        HiLog.debug(TAG, "enter animation-list, new AnimationDrawable", new Object[0]);
        if (context != null) {
            this.mContext = context;
            this.mResourceManager = this.mContext.getResourceManager();
            ResourceManager resourceManager = this.mResourceManager;
            if (resourceManager != null) {
                for (TypedAttribute typedAttribute : node.getTypedAttributes(resourceManager)) {
                    if (typedAttribute == null) {
                        HiLog.error(TAG, "typedAttribute is null", new Object[0]);
                    } else if ("oneshot".equals(typedAttribute.getName())) {
                        try {
                            boolean z = true;
                            HiLog.debug(TAG, "animationDrawable setOneShot: %{public}s", typedAttribute.getStringValue());
                            if (typedAttribute.getStringValue().equalsIgnoreCase("false")) {
                                z = false;
                            }
                            setOneShot(z);
                        } catch (IOException | NotExistException | WrongTypeException unused) {
                            HiLog.error(TAG, "get oneShot value failed.", new Object[0]);
                        }
                    }
                }
                Node child = node.getChild();
                if (child != null) {
                    parseAddFrame(child);
                    return;
                }
                throw new ElementScatterException("no items in AnimationDrawable!");
            }
            throw new ElementScatterException("mResourceManager is null");
        }
        throw new ElementScatterException("context is null");
    }

    public void setDisposeMode(boolean z) {
        nativeSetDisposeMode(this.mNativeElementPtr, z);
    }

    public boolean getDisposeMode() {
        return nativeGetDisposeMode(this.mNativeElementPtr);
    }

    public boolean setPreDecodeFrames(int i) {
        return nativeSetPreDecodeFrames(this.mNativeElementPtr, i);
    }

    public int getPreDecodeFrames() {
        return nativeGetPreDecodeFrames(this.mNativeElementPtr);
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x006f  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x009d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void parseAddFrame(ohos.global.resource.solidxml.Node r11) {
        /*
        // Method dump skipped, instructions count: 226
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.agp.components.element.FrameAnimationElement.parseAddFrame(ohos.global.resource.solidxml.Node):void");
    }

    private Element parseDrawable(TypedAttribute typedAttribute) {
        try {
            String mediaValue = typedAttribute.getMediaValue();
            HiLog.debug(TAG, "drawable mediaPath is %{public}s", mediaValue);
            Element drawableFromPath = getDrawableFromPath(mediaValue, this.mResourceManager);
            if (drawableFromPath != null) {
                return drawableFromPath;
            }
            HiLog.error(TAG, "AnimationDrawable is null", new Object[0]);
            return null;
        } catch (IOException | NotExistException | WrongTypeException unused) {
            throw new ElementScatterException("get animation media item path failed!");
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0069, code lost:
        r0 = move-exception;
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
        r3.addSuppressed(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0074, code lost:
        throw r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private ohos.agp.components.element.Element getDrawableFromPath(java.lang.String r4, ohos.global.resource.ResourceManager r5) {
        /*
        // Method dump skipped, instructions count: 127
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.agp.components.element.FrameAnimationElement.getDrawableFromPath(java.lang.String, ohos.global.resource.ResourceManager):ohos.agp.components.element.Element");
    }

    @Override // ohos.agp.components.element.ElementContainer
    public long getElementContainerStateHandle() {
        return nativeGetAnimationDrawableStateHandle(this.mNativeElementPtr);
    }
}
