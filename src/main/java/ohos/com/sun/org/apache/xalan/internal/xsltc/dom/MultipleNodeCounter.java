package ohos.com.sun.org.apache.xalan.internal.xsltc.dom;

import ohos.com.sun.org.apache.xalan.internal.templates.Constants;
import ohos.com.sun.org.apache.xalan.internal.xsltc.DOM;
import ohos.com.sun.org.apache.xalan.internal.xsltc.Translet;
import ohos.com.sun.org.apache.xalan.internal.xsltc.util.IntegerArray;
import ohos.com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import ohos.com.sun.org.apache.xpath.internal.XPath;

public abstract class MultipleNodeCounter extends NodeCounter {
    private DTMAxisIterator _precSiblings = null;

    public MultipleNodeCounter(Translet translet, DOM dom, DTMAxisIterator dTMAxisIterator) {
        super(translet, dom, dTMAxisIterator);
    }

    public MultipleNodeCounter(Translet translet, DOM dom, DTMAxisIterator dTMAxisIterator, boolean z) {
        super(translet, dom, dTMAxisIterator, z);
    }

    @Override // ohos.com.sun.org.apache.xalan.internal.xsltc.dom.NodeCounter
    public NodeCounter setStartNode(int i) {
        this._node = i;
        this._nodeType = this._document.getExpandedTypeID(i);
        this._precSiblings = this._document.getAxisIterator(12);
        return this;
    }

    @Override // ohos.com.sun.org.apache.xalan.internal.xsltc.dom.NodeCounter
    public String getCounter() {
        if (this._value == -2.147483648E9d) {
            IntegerArray integerArray = new IntegerArray();
            int i = this._node;
            integerArray.add(i);
            while (true) {
                i = this._document.getParent(i);
                if (i <= -1 || matchesFrom(i)) {
                    int cardinality = integerArray.cardinality();
                    int[] iArr = new int[cardinality];
                    int i2 = 0;
                } else {
                    integerArray.add(i);
                }
            }
            int cardinality2 = integerArray.cardinality();
            int[] iArr2 = new int[cardinality2];
            int i22 = 0;
            for (int i3 = 0; i3 < cardinality2; i3++) {
                iArr2[i3] = Integer.MIN_VALUE;
            }
            int i4 = cardinality2 - 1;
            while (i4 >= 0) {
                int i5 = iArr2[i22];
                int at = integerArray.at(i4);
                if (matchesCount(at)) {
                    this._precSiblings.setStartNode(at);
                    while (true) {
                        int next = this._precSiblings.next();
                        if (next == -1) {
                            break;
                        } else if (matchesCount(next)) {
                            iArr2[i22] = iArr2[i22] == Integer.MIN_VALUE ? 1 : iArr2[i22] + 1;
                        }
                    }
                    iArr2[i22] = iArr2[i22] == Integer.MIN_VALUE ? 1 : iArr2[i22] + 1;
                }
                i4--;
                i22++;
            }
            return formatNumbers(iArr2);
        } else if (this._value == XPath.MATCH_SCORE_QNAME) {
            return "0";
        } else {
            if (Double.isNaN(this._value)) {
                return "NaN";
            }
            if (this._value < XPath.MATCH_SCORE_QNAME && Double.isInfinite(this._value)) {
                return "-Infinity";
            }
            if (Double.isInfinite(this._value)) {
                return Constants.ATTRVAL_INFINITY;
            }
            return formatNumbers((int) this._value);
        }
    }

    public static NodeCounter getDefaultNodeCounter(Translet translet, DOM dom, DTMAxisIterator dTMAxisIterator) {
        return new DefaultMultipleNodeCounter(translet, dom, dTMAxisIterator);
    }

    static class DefaultMultipleNodeCounter extends MultipleNodeCounter {
        public DefaultMultipleNodeCounter(Translet translet, DOM dom, DTMAxisIterator dTMAxisIterator) {
            super(translet, dom, dTMAxisIterator);
        }
    }
}
