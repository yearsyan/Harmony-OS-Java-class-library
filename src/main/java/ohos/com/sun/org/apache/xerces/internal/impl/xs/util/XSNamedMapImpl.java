package ohos.com.sun.org.apache.xerces.internal.impl.xs.util;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import ohos.com.sun.org.apache.xerces.internal.util.SymbolHash;
import ohos.com.sun.org.apache.xerces.internal.xs.XSNamedMap;
import ohos.com.sun.org.apache.xerces.internal.xs.XSObject;
import ohos.javax.xml.namespace.QName;

public class XSNamedMapImpl extends AbstractMap implements XSNamedMap {
    public static final XSNamedMapImpl EMPTY_MAP = new XSNamedMapImpl(new XSObject[0], 0);
    XSObject[] fArray = null;
    private Set fEntrySet = null;
    int fLength = -1;
    final SymbolHash[] fMaps;
    final int fNSNum;
    final String[] fNamespaces;

    public XSNamedMapImpl(String str, SymbolHash symbolHash) {
        this.fNamespaces = new String[]{str};
        this.fMaps = new SymbolHash[]{symbolHash};
        this.fNSNum = 1;
    }

    public XSNamedMapImpl(String[] strArr, SymbolHash[] symbolHashArr, int i) {
        this.fNamespaces = strArr;
        this.fMaps = symbolHashArr;
        this.fNSNum = i;
    }

    public XSNamedMapImpl(XSObject[] xSObjectArr, int i) {
        if (i == 0) {
            this.fNamespaces = null;
            this.fMaps = null;
            this.fNSNum = 0;
            this.fArray = xSObjectArr;
            this.fLength = 0;
            return;
        }
        this.fNamespaces = new String[]{xSObjectArr[0].getNamespace()};
        this.fMaps = null;
        this.fNSNum = 1;
        this.fArray = xSObjectArr;
        this.fLength = i;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xs.XSNamedMap
    public synchronized int getLength() {
        if (this.fLength == -1) {
            this.fLength = 0;
            for (int i = 0; i < this.fNSNum; i++) {
                this.fLength += this.fMaps[i].getLength();
            }
        }
        return this.fLength;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xs.XSNamedMap
    public XSObject itemByName(String str, String str2) {
        for (int i = 0; i < this.fNSNum; i++) {
            if (isEqual(str, this.fNamespaces[i])) {
                SymbolHash[] symbolHashArr = this.fMaps;
                if (symbolHashArr != null) {
                    return (XSObject) symbolHashArr[i].get(str2);
                }
                for (int i2 = 0; i2 < this.fLength; i2++) {
                    XSObject xSObject = this.fArray[i2];
                    if (xSObject.getName().equals(str2)) {
                        return xSObject;
                    }
                }
                return null;
            }
        }
        return null;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.xs.XSNamedMap
    public synchronized XSObject item(int i) {
        if (this.fArray == null) {
            getLength();
            this.fArray = new XSObject[this.fLength];
            int i2 = 0;
            for (int i3 = 0; i3 < this.fNSNum; i3++) {
                i2 += this.fMaps[i3].getValues(this.fArray, i2);
            }
        }
        if (i >= 0) {
            if (i < this.fLength) {
                return this.fArray[i];
            }
        }
        return null;
    }

    static boolean isEqual(String str, String str2) {
        if (str != null) {
            return str.equals(str2);
        }
        return str2 == null;
    }

    public boolean containsKey(Object obj) {
        return get(obj) != null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object get(Object obj) {
        if (!(obj instanceof QName)) {
            return null;
        }
        QName qName = (QName) obj;
        String namespaceURI = qName.getNamespaceURI();
        if ("".equals(namespaceURI)) {
            namespaceURI = null;
        }
        return itemByName(namespaceURI, qName.getLocalPart());
    }

    public int size() {
        return getLength();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public synchronized Set entrySet() {
        if (this.fEntrySet == null) {
            final int length = getLength();
            final XSNamedMapEntry[] xSNamedMapEntryArr = new XSNamedMapEntry[length];
            for (int i = 0; i < length; i++) {
                XSObject item = item(i);
                xSNamedMapEntryArr[i] = new XSNamedMapEntry(new QName(item.getNamespace(), item.getName()), item);
            }
            this.fEntrySet = new AbstractSet() {
                /* class ohos.com.sun.org.apache.xerces.internal.impl.xs.util.XSNamedMapImpl.AnonymousClass1 */

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set, java.lang.Iterable
                public Iterator iterator() {
                    return new Iterator() {
                        /* class ohos.com.sun.org.apache.xerces.internal.impl.xs.util.XSNamedMapImpl.AnonymousClass1.AnonymousClass1 */
                        private int index = 0;

                        public boolean hasNext() {
                            return this.index < length;
                        }

                        @Override // java.util.Iterator
                        public Object next() {
                            if (this.index < length) {
                                XSNamedMapEntry[] xSNamedMapEntryArr = xSNamedMapEntryArr;
                                int i = this.index;
                                this.index = i + 1;
                                return xSNamedMapEntryArr[i];
                            }
                            throw new NoSuchElementException();
                        }

                        public void remove() {
                            throw new UnsupportedOperationException();
                        }
                    };
                }

                public int size() {
                    return length;
                }
            };
        }
        return this.fEntrySet;
    }

    private static final class XSNamedMapEntry implements Map.Entry {
        private final QName key;
        private final XSObject value;

        public XSNamedMapEntry(QName qName, XSObject xSObject) {
            this.key = qName;
            this.value = xSObject;
        }

        @Override // java.util.Map.Entry
        public Object getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public Object getValue() {
            return this.value;
        }

        @Override // java.util.Map.Entry
        public Object setValue(Object obj) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARNING: Removed duplicated region for block: B:12:0x0029 A[ORIG_RETURN, RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean equals(java.lang.Object r4) {
            /*
                r3 = this;
                boolean r0 = r4 instanceof java.util.Map.Entry
                r1 = 0
                if (r0 == 0) goto L_0x002a
                java.util.Map$Entry r4 = (java.util.Map.Entry) r4
                java.lang.Object r0 = r4.getKey()
                java.lang.Object r4 = r4.getValue()
                ohos.javax.xml.namespace.QName r2 = r3.key
                if (r2 != 0) goto L_0x0016
                if (r0 != 0) goto L_0x002a
                goto L_0x001c
            L_0x0016:
                boolean r0 = r2.equals(r0)
                if (r0 == 0) goto L_0x002a
            L_0x001c:
                ohos.com.sun.org.apache.xerces.internal.xs.XSObject r3 = r3.value
                if (r3 != 0) goto L_0x0023
                if (r4 != 0) goto L_0x002a
                goto L_0x0029
            L_0x0023:
                boolean r3 = r3.equals(r4)
                if (r3 == 0) goto L_0x002a
            L_0x0029:
                r1 = 1
            L_0x002a:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.xs.util.XSNamedMapImpl.XSNamedMapEntry.equals(java.lang.Object):boolean");
        }

        public int hashCode() {
            QName qName = this.key;
            int i = 0;
            int hashCode = qName == null ? 0 : qName.hashCode();
            XSObject xSObject = this.value;
            if (xSObject != null) {
                i = xSObject.hashCode();
            }
            return hashCode ^ i;
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(String.valueOf(this.key));
            stringBuffer.append('=');
            stringBuffer.append(String.valueOf(this.value));
            return stringBuffer.toString();
        }
    }
}
