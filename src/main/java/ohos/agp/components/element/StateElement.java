package ohos.agp.components.element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ohos.aafwk.utils.log.LogDomain;
import ohos.app.Context;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.solidxml.Node;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class StateElement extends ElementContainer {
    private static final HiLogLabel TAG = new HiLogLabel(3, LogDomain.END, "StateElement");
    private Context mContext;
    private ResourceManager mResourceManager;
    private final ArrayList<int[]> mStateList = new ArrayList<>();
    private final List<Integer> mStates = new ArrayList(Arrays.asList(64, 32, 0, 2, 268435456, 16384, 4));

    private native void nativeAddState(long j, int[] iArr, long j2);

    private native int nativeGetCurrentIndex(long j);

    private native long nativeGetDrawableContainerStateHandle(long j);

    private native long nativeGetStateListDrawableHandle();

    @Override // ohos.agp.components.element.Element, ohos.agp.components.element.ElementContainer
    public void createNativePtr(Object obj) {
        if (this.mNativeElementPtr == 0) {
            this.mNativeElementPtr = nativeGetStateListDrawableHandle();
        }
    }

    @Override // ohos.agp.components.element.Element, ohos.agp.components.element.ElementContainer
    public Element getCurrentElement() {
        if (getElementState().mElementList.size() == 0) {
            return null;
        }
        int nativeGetCurrentIndex = nativeGetCurrentIndex(this.mNativeElementPtr);
        if (nativeGetCurrentIndex >= 0 && nativeGetCurrentIndex < getElementState().mElementList.size()) {
            return getElementState().mElementList.get(nativeGetCurrentIndex);
        }
        HiLog.error(TAG, "get current index fail.", new Object[0]);
        return null;
    }

    public void addState(int[] iArr, Element element) {
        if (element == null) {
            getElementState().mElementList.add(null);
            this.mStateList.add(iArr);
            nativeAddState(this.mNativeElementPtr, iArr, 0);
        } else if (!element.isStateful()) {
            getElementState().mElementList.add(element);
            this.mStateList.add(iArr);
            nativeAddState(this.mNativeElementPtr, iArr, element.getNativeElementPtr());
        }
    }

    public int findStateElementIndex(int[] iArr) {
        int stateCount = getStateCount();
        for (int i = 0; i < stateCount; i++) {
            if (Arrays.equals(iArr, this.mStateList.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public int[] getStateSet(int i) {
        if (i >= 0 && i < this.mStateList.size()) {
            return this.mStateList.get(i);
        }
        HiLog.error(TAG, "get state set fail.", new Object[0]);
        return new int[0];
    }

    public Element getStateElement(int i) {
        return getElementState().mElementList.get(i);
    }

    public int getStateCount() {
        return this.mStateList.size();
    }

    @Override // ohos.agp.components.element.Element
    public void parseXMLNode(Context context, Node node) {
        super.parseXMLNode(context, node);
        HiLog.debug(TAG, "enter state-container, new stateElement", new Object[0]);
        if (context != null) {
            this.mContext = context;
            this.mResourceManager = this.mContext.getResourceManager();
            if (this.mResourceManager == null) {
                throw new ElementScatterException("mResourceManager is null");
            } else if (node.getName() != null) {
                Node child = node.getChild();
                if (child != null) {
                    parseNodeInternal(child);
                    return;
                }
                throw new ElementScatterException("no items in stateElement!");
            }
        } else {
            throw new ElementScatterException("context is null");
        }
    }

    private void parseNodeInternal(Node node) {
        if (node != null) {
            if ("item".equals(node.getName())) {
                parseItem(node.getTypedAttributes(this.mResourceManager));
            }
            Node sibling = node.getSibling();
            if (sibling != null) {
                parseNodeInternal(sibling);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x004b A[Catch:{ IOException | NotExistException | WrongTypeException -> 0x0077 }] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0062 A[Catch:{ IOException | NotExistException | WrongTypeException -> 0x0077 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void parseItem(java.util.List<ohos.global.resource.solidxml.TypedAttribute> r10) {
        /*
        // Method dump skipped, instructions count: 180
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.agp.components.element.StateElement.parseItem(java.util.List):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0076, code lost:
        ohos.hiviewdfx.HiLog.error(ohos.agp.components.element.StateElement.TAG, "parse element failed.", new java.lang.Object[0]);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0080, code lost:
        return null;
     */
    /* JADX WARNING: Removed duplicated region for block: B:32:? A[ExcHandler: IOException | NotExistException | WrongTypeException (unused java.lang.Throwable), SYNTHETIC, Splitter:B:8:0x0024] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private ohos.agp.components.element.Element getElement(ohos.global.resource.solidxml.TypedAttribute r5) {
        /*
        // Method dump skipped, instructions count: 129
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.agp.components.element.StateElement.getElement(ohos.global.resource.solidxml.TypedAttribute):ohos.agp.components.element.Element");
    }

    @Override // ohos.agp.components.element.ElementContainer
    public long getElementContainerStateHandle() {
        return nativeGetDrawableContainerStateHandle(this.mNativeElementPtr);
    }
}
