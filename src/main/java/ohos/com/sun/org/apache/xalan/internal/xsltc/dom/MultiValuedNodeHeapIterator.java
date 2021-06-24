package ohos.com.sun.org.apache.xalan.internal.xsltc.dom;

import ohos.com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
import ohos.com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import ohos.com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase;

public abstract class MultiValuedNodeHeapIterator extends DTMAxisIteratorBase {
    private static final int InitSize = 8;
    private int _cachedHeapSize;
    private int _cachedReturnedLast = -1;
    private int _free = 0;
    private HeapNode[] _heap = new HeapNode[8];
    private int _heapSize = 0;
    private int _returnedLast;
    private int _size = 8;

    public abstract class HeapNode implements Cloneable {
        protected boolean _isStartSet = false;
        protected int _markedNode;
        protected int _node;

        public abstract boolean isLessThan(HeapNode heapNode);

        public abstract HeapNode reset();

        public abstract HeapNode setStartNode(int i);

        public abstract int step();

        public HeapNode() {
        }

        public HeapNode cloneHeapNode() {
            try {
                HeapNode heapNode = (HeapNode) super.clone();
                heapNode._node = this._node;
                heapNode._markedNode = this._node;
                return heapNode;
            } catch (CloneNotSupportedException e) {
                BasisLibrary.runTimeError(BasisLibrary.ITERATOR_CLONE_ERR, e.toString());
                return null;
            }
        }

        public void setMark() {
            this._markedNode = this._node;
        }

        public void gotoMark() {
            this._node = this._markedNode;
        }
    }

    @Override // ohos.com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, ohos.com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public DTMAxisIterator cloneIterator() {
        this._isRestartable = false;
        HeapNode[] heapNodeArr = new HeapNode[this._heap.length];
        try {
            MultiValuedNodeHeapIterator multiValuedNodeHeapIterator = (MultiValuedNodeHeapIterator) super.clone();
            for (int i = 0; i < this._free; i++) {
                heapNodeArr[i] = this._heap[i].cloneHeapNode();
            }
            multiValuedNodeHeapIterator.setRestartable(false);
            multiValuedNodeHeapIterator._heap = heapNodeArr;
            return multiValuedNodeHeapIterator.reset();
        } catch (CloneNotSupportedException e) {
            BasisLibrary.runTimeError(BasisLibrary.ITERATOR_CLONE_ERR, e.toString());
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void addHeapNode(HeapNode heapNode) {
        int i = this._free;
        int i2 = this._size;
        if (i == i2) {
            int i3 = i2 * 2;
            this._size = i3;
            HeapNode[] heapNodeArr = new HeapNode[i3];
            System.arraycopy(this._heap, 0, heapNodeArr, 0, i);
            this._heap = heapNodeArr;
        }
        this._heapSize++;
        HeapNode[] heapNodeArr2 = this._heap;
        int i4 = this._free;
        this._free = i4 + 1;
        heapNodeArr2[i4] = heapNode;
    }

    @Override // ohos.com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public int next() {
        while (this._heapSize > 0) {
            int i = this._heap[0]._node;
            if (i == -1) {
                int i2 = this._heapSize;
                if (i2 <= 1) {
                    return -1;
                }
                HeapNode[] heapNodeArr = this._heap;
                HeapNode heapNode = heapNodeArr[0];
                int i3 = i2 - 1;
                this._heapSize = i3;
                heapNodeArr[0] = heapNodeArr[i3];
                heapNodeArr[this._heapSize] = heapNode;
            } else if (i == this._returnedLast) {
                this._heap[0].step();
            } else {
                this._heap[0].step();
                heapify(0);
                this._returnedLast = i;
                return returnNode(i);
            }
            heapify(0);
        }
        return -1;
    }

    @Override // ohos.com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public DTMAxisIterator setStartNode(int i) {
        int i2;
        if (!this._isRestartable) {
            return this;
        }
        this._startNode = i;
        int i3 = 0;
        while (true) {
            i2 = this._free;
            if (i3 >= i2) {
                break;
            }
            if (!this._heap[i3]._isStartSet) {
                this._heap[i3].setStartNode(i);
                this._heap[i3].step();
                this._heap[i3]._isStartSet = true;
            }
            i3++;
        }
        this._heapSize = i2;
        for (int i4 = i2 / 2; i4 >= 0; i4--) {
            heapify(i4);
        }
        this._returnedLast = -1;
        return resetPosition();
    }

    /* access modifiers changed from: protected */
    public void init() {
        for (int i = 0; i < this._free; i++) {
            this._heap[i] = null;
        }
        this._heapSize = 0;
        this._free = 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0014, code lost:
        if (r2[r1].isLessThan(r2[r5]) != false) goto L_0x0018;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0026, code lost:
        if (r2[r0].isLessThan(r2[r1]) != false) goto L_0x002a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void heapify(int r5) {
        /*
            r4 = this;
        L_0x0000:
            int r0 = r5 + 1
            int r0 = r0 << 1
            int r1 = r0 + -1
            int r2 = r4._heapSize
            if (r1 >= r2) goto L_0x0017
            ohos.com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator$HeapNode[] r2 = r4._heap
            r3 = r2[r1]
            r2 = r2[r5]
            boolean r2 = r3.isLessThan(r2)
            if (r2 == 0) goto L_0x0017
            goto L_0x0018
        L_0x0017:
            r1 = r5
        L_0x0018:
            int r2 = r4._heapSize
            if (r0 >= r2) goto L_0x0029
            ohos.com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator$HeapNode[] r2 = r4._heap
            r3 = r2[r0]
            r2 = r2[r1]
            boolean r2 = r3.isLessThan(r2)
            if (r2 == 0) goto L_0x0029
            goto L_0x002a
        L_0x0029:
            r0 = r1
        L_0x002a:
            if (r0 == r5) goto L_0x0038
            ohos.com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator$HeapNode[] r1 = r4._heap
            r2 = r1[r0]
            r3 = r1[r5]
            r1[r0] = r3
            r1[r5] = r2
            r5 = r0
            goto L_0x0000
        L_0x0038:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xalan.internal.xsltc.dom.MultiValuedNodeHeapIterator.heapify(int):void");
    }

    @Override // ohos.com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public void setMark() {
        for (int i = 0; i < this._free; i++) {
            this._heap[i].setMark();
        }
        this._cachedReturnedLast = this._returnedLast;
        this._cachedHeapSize = this._heapSize;
    }

    @Override // ohos.com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public void gotoMark() {
        for (int i = 0; i < this._free; i++) {
            this._heap[i].gotoMark();
        }
        int i2 = this._cachedHeapSize;
        this._heapSize = i2;
        for (int i3 = i2 / 2; i3 >= 0; i3--) {
            heapify(i3);
        }
        this._returnedLast = this._cachedReturnedLast;
    }

    @Override // ohos.com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase, ohos.com.sun.org.apache.xml.internal.dtm.DTMAxisIterator
    public DTMAxisIterator reset() {
        int i;
        int i2 = 0;
        while (true) {
            i = this._free;
            if (i2 >= i) {
                break;
            }
            this._heap[i2].reset();
            this._heap[i2].step();
            i2++;
        }
        this._heapSize = i;
        for (int i3 = i / 2; i3 >= 0; i3--) {
            heapify(i3);
        }
        this._returnedLast = -1;
        return resetPosition();
    }
}
