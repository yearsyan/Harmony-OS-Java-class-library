package ohos.com.sun.org.apache.xerces.internal.impl.xs.util;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import ohos.com.sun.org.apache.xerces.internal.xs.XSObject;
import ohos.com.sun.org.apache.xerces.internal.xs.XSObjectList;

public class XSObjectListImpl extends AbstractList implements XSObjectList {
    private static final int DEFAULT_SIZE = 4;
    private static final ListIterator EMPTY_ITERATOR = new ListIterator() {
        /* class ohos.com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl.AnonymousClass1 */

        public boolean hasNext() {
            return false;
        }

        public boolean hasPrevious() {
            return false;
        }

        public int nextIndex() {
            return 0;
        }

        public int previousIndex() {
            return -1;
        }

        @Override // java.util.Iterator, java.util.ListIterator
        public Object next() {
            throw new NoSuchElementException();
        }

        @Override // java.util.ListIterator
        public Object previous() {
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.ListIterator
        public void set(Object obj) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.ListIterator
        public void add(Object obj) {
            throw new UnsupportedOperationException();
        }
    };
    public static final XSObjectListImpl EMPTY_LIST = new XSObjectListImpl(new XSObject[0], 0);
    private XSObject[] fArray;
    private int fLength;

    public XSObjectListImpl() {
        this.fArray = null;
        this.fLength = 0;
        this.fArray = new XSObject[4];
        this.fLength = 0;
    }

    public XSObjectListImpl(XSObject[] xSObjectArr, int i) {
        this.fArray = null;
        this.fLength = 0;
        this.fArray = xSObjectArr;
        this.fLength = i;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xs.XSObjectList
    public int getLength() {
        return this.fLength;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xs.XSObjectList
    public XSObject item(int i) {
        if (i < 0 || i >= this.fLength) {
            return null;
        }
        return this.fArray[i];
    }

    public void clearXSObjectList() {
        for (int i = 0; i < this.fLength; i++) {
            this.fArray[i] = null;
        }
        this.fArray = null;
        this.fLength = 0;
    }

    public void addXSObject(XSObject xSObject) {
        int i = this.fLength;
        XSObject[] xSObjectArr = this.fArray;
        if (i == xSObjectArr.length) {
            XSObject[] xSObjectArr2 = new XSObject[(i + 4)];
            System.arraycopy(xSObjectArr, 0, xSObjectArr2, 0, i);
            this.fArray = xSObjectArr2;
        }
        XSObject[] xSObjectArr3 = this.fArray;
        int i2 = this.fLength;
        this.fLength = i2 + 1;
        xSObjectArr3[i2] = xSObject;
    }

    public void addXSObject(int i, XSObject xSObject) {
        this.fArray[i] = xSObject;
    }

    public boolean contains(Object obj) {
        return obj == null ? containsNull() : containsObject(obj);
    }

    @Override // java.util.List, java.util.AbstractList
    public Object get(int i) {
        if (i >= 0 && i < this.fLength) {
            return this.fArray[i];
        }
        throw new IndexOutOfBoundsException("Index: " + i);
    }

    public int size() {
        return getLength();
    }

    @Override // java.util.AbstractCollection, java.util.List, java.util.Collection, java.util.AbstractList, java.lang.Iterable
    public Iterator iterator() {
        return listIterator0(0);
    }

    @Override // java.util.List, java.util.AbstractList
    public ListIterator listIterator() {
        return listIterator0(0);
    }

    @Override // java.util.List, java.util.AbstractList
    public ListIterator listIterator(int i) {
        if (i >= 0 && i < this.fLength) {
            return listIterator0(i);
        }
        throw new IndexOutOfBoundsException("Index: " + i);
    }

    private ListIterator listIterator0(int i) {
        return this.fLength == 0 ? EMPTY_ITERATOR : new XSObjectListIterator(i);
    }

    private boolean containsObject(Object obj) {
        for (int i = this.fLength - 1; i >= 0; i--) {
            if (obj.equals(this.fArray[i])) {
                return true;
            }
        }
        return false;
    }

    private boolean containsNull() {
        for (int i = this.fLength - 1; i >= 0; i--) {
            if (this.fArray[i] == null) {
                return true;
            }
        }
        return false;
    }

    public Object[] toArray() {
        Object[] objArr = new Object[this.fLength];
        toArray0(objArr);
        return objArr;
    }

    @Override // java.util.AbstractCollection, java.util.List, java.util.Collection
    public Object[] toArray(Object[] objArr) {
        if (objArr.length < this.fLength) {
            objArr = (Object[]) Array.newInstance(objArr.getClass().getComponentType(), this.fLength);
        }
        toArray0(objArr);
        int length = objArr.length;
        int i = this.fLength;
        if (length > i) {
            objArr[i] = null;
        }
        return objArr;
    }

    private void toArray0(Object[] objArr) {
        int i = this.fLength;
        if (i > 0) {
            System.arraycopy(this.fArray, 0, objArr, 0, i);
        }
    }

    /* access modifiers changed from: private */
    public final class XSObjectListIterator implements ListIterator {
        private int index;

        public XSObjectListIterator(int i) {
            this.index = i;
        }

        public boolean hasNext() {
            return this.index < XSObjectListImpl.this.fLength;
        }

        @Override // java.util.Iterator, java.util.ListIterator
        public Object next() {
            if (this.index < XSObjectListImpl.this.fLength) {
                XSObject[] xSObjectArr = XSObjectListImpl.this.fArray;
                int i = this.index;
                this.index = i + 1;
                return xSObjectArr[i];
            }
            throw new NoSuchElementException();
        }

        public boolean hasPrevious() {
            return this.index > 0;
        }

        @Override // java.util.ListIterator
        public Object previous() {
            if (this.index > 0) {
                XSObject[] xSObjectArr = XSObjectListImpl.this.fArray;
                int i = this.index - 1;
                this.index = i;
                return xSObjectArr[i];
            }
            throw new NoSuchElementException();
        }

        public int nextIndex() {
            return this.index;
        }

        public int previousIndex() {
            return this.index - 1;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.ListIterator
        public void set(Object obj) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.ListIterator
        public void add(Object obj) {
            throw new UnsupportedOperationException();
        }
    }
}
