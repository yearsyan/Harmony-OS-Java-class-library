package ohos.com.sun.org.apache.xerces.internal.impl.xs.identity;

import ohos.com.sun.org.apache.xerces.internal.impl.xs.util.ShortListImpl;
import ohos.com.sun.org.apache.xerces.internal.xs.ShortList;
import ohos.com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition;
import ohos.com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;

public class Field {
    protected IdentityConstraint fIdentityConstraint;
    protected XPath fXPath;

    public Field(XPath xPath, IdentityConstraint identityConstraint) {
        this.fXPath = xPath;
        this.fIdentityConstraint = identityConstraint;
    }

    public ohos.com.sun.org.apache.xerces.internal.impl.xpath.XPath getXPath() {
        return this.fXPath;
    }

    public IdentityConstraint getIdentityConstraint() {
        return this.fIdentityConstraint;
    }

    public XPathMatcher createMatcher(FieldActivator fieldActivator, ValueStore valueStore) {
        return new Matcher(this.fXPath, fieldActivator, valueStore);
    }

    public String toString() {
        return this.fXPath.toString();
    }

    public static class XPath extends ohos.com.sun.org.apache.xerces.internal.impl.xpath.XPath {
        /* JADX WARNING: Illegal instructions before constructor call */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public XPath(java.lang.String r3, ohos.com.sun.org.apache.xerces.internal.util.SymbolTable r4, ohos.com.sun.org.apache.xerces.internal.xni.NamespaceContext r5) throws ohos.com.sun.org.apache.xerces.internal.impl.xpath.XPathException {
            /*
            // Method dump skipped, instructions count: 104
            */
            throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.impl.xs.identity.Field.XPath.<init>(java.lang.String, ohos.com.sun.org.apache.xerces.internal.util.SymbolTable, ohos.com.sun.org.apache.xerces.internal.xni.NamespaceContext):void");
        }
    }

    protected class Matcher extends XPathMatcher {
        protected FieldActivator fFieldActivator;
        protected ValueStore fStore;

        private short convertToPrimitiveKind(short s) {
            if (s <= 20) {
                return s;
            }
            if (s <= 29) {
                return 2;
            }
            if (s <= 42) {
                return 4;
            }
            return s;
        }

        public Matcher(XPath xPath, FieldActivator fieldActivator, ValueStore valueStore) {
            super(xPath);
            this.fFieldActivator = fieldActivator;
            this.fStore = valueStore;
        }

        /* access modifiers changed from: protected */
        @Override // ohos.com.sun.org.apache.xerces.internal.impl.xs.identity.XPathMatcher
        public void matched(Object obj, short s, ShortList shortList, boolean z) {
            super.matched(obj, s, shortList, z);
            if (z && Field.this.fIdentityConstraint.getCategory() == 1) {
                this.fStore.reportError("KeyMatchesNillable", new Object[]{Field.this.fIdentityConstraint.getElementName(), Field.this.fIdentityConstraint.getIdentityConstraintName()});
            }
            this.fStore.addValue(Field.this, obj, convertToPrimitiveKind(s), convertToPrimitiveKind(shortList));
            this.fFieldActivator.setMayMatch(Field.this, Boolean.FALSE);
        }

        private ShortList convertToPrimitiveKind(ShortList shortList) {
            if (shortList != null) {
                int length = shortList.getLength();
                int i = 0;
                while (i < length) {
                    short item = shortList.item(i);
                    if (item != convertToPrimitiveKind(item)) {
                        break;
                    }
                    i++;
                }
                if (i != length) {
                    short[] sArr = new short[length];
                    for (int i2 = 0; i2 < i; i2++) {
                        sArr[i2] = shortList.item(i2);
                    }
                    while (i < length) {
                        sArr[i] = convertToPrimitiveKind(shortList.item(i));
                        i++;
                    }
                    return new ShortListImpl(sArr, sArr.length);
                }
            }
            return shortList;
        }

        /* access modifiers changed from: protected */
        @Override // ohos.com.sun.org.apache.xerces.internal.impl.xs.identity.XPathMatcher
        public void handleContent(XSTypeDefinition xSTypeDefinition, boolean z, Object obj, short s, ShortList shortList) {
            if (xSTypeDefinition == null || (xSTypeDefinition.getTypeCategory() == 15 && ((XSComplexTypeDefinition) xSTypeDefinition).getContentType() != 1)) {
                this.fStore.reportError("cvc-id.3", new Object[]{Field.this.fIdentityConstraint.getName(), Field.this.fIdentityConstraint.getElementName()});
            }
            this.fMatchedString = obj;
            matched(this.fMatchedString, s, shortList, z);
        }
    }
}
