package ohos.com.sun.org.apache.xerces.internal.impl.xs.opti;

import ohos.org.w3c.dom.Attr;
import ohos.org.w3c.dom.DOMException;
import ohos.org.w3c.dom.Document;
import ohos.org.w3c.dom.Element;
import ohos.org.w3c.dom.TypeInfo;

public class AttrImpl extends NodeImpl implements Attr {
    Element element;
    String value;

    @Override // ohos.org.w3c.dom.Attr
    public TypeInfo getSchemaTypeInfo() {
        return null;
    }

    @Override // ohos.org.w3c.dom.Attr
    public boolean getSpecified() {
        return true;
    }

    @Override // ohos.org.w3c.dom.Attr
    public boolean isId() {
        return false;
    }

    public AttrImpl() {
        this.nodeType = 2;
    }

    public AttrImpl(Element element2, String str, String str2, String str3, String str4, String str5) {
        super(str, str2, str3, str4, 2);
        this.element = element2;
        this.value = str5;
    }

    @Override // ohos.org.w3c.dom.Attr
    public String getName() {
        return this.rawname;
    }

    @Override // ohos.org.w3c.dom.Attr
    public String getValue() {
        return this.value;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultNode, ohos.org.w3c.dom.Node
    public String getNodeValue() {
        return getValue();
    }

    @Override // ohos.org.w3c.dom.Attr
    public Element getOwnerElement() {
        return this.element;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultNode, ohos.org.w3c.dom.Node
    public Document getOwnerDocument() {
        return this.element.getOwnerDocument();
    }

    @Override // ohos.org.w3c.dom.Attr
    public void setValue(String str) throws DOMException {
        this.value = str;
    }

    public String toString() {
        return getName() + "=\"" + getValue() + "\"";
    }
}
