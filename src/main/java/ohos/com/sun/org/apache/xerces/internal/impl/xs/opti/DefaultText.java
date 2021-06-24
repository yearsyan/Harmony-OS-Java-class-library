package ohos.com.sun.org.apache.xerces.internal.impl.xs.opti;

import ohos.org.w3c.dom.DOMException;
import ohos.org.w3c.dom.Text;

public class DefaultText extends NodeImpl implements Text {
    @Override // ohos.org.w3c.dom.CharacterData
    public String getData() throws DOMException {
        return null;
    }

    @Override // ohos.org.w3c.dom.CharacterData
    public int getLength() {
        return 0;
    }

    @Override // ohos.org.w3c.dom.CharacterData
    public void setData(String str) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    @Override // ohos.org.w3c.dom.CharacterData
    public String substringData(int i, int i2) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    @Override // ohos.org.w3c.dom.CharacterData
    public void appendData(String str) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    @Override // ohos.org.w3c.dom.CharacterData
    public void insertData(int i, String str) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    @Override // ohos.org.w3c.dom.CharacterData
    public void deleteData(int i, int i2) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    @Override // ohos.org.w3c.dom.CharacterData
    public void replaceData(int i, int i2, String str) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    @Override // ohos.org.w3c.dom.Text
    public Text splitText(int i) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }

    @Override // ohos.org.w3c.dom.Text
    public boolean isElementContentWhitespace() {
        throw new DOMException(9, "Method not supported");
    }

    @Override // ohos.org.w3c.dom.Text
    public String getWholeText() {
        throw new DOMException(9, "Method not supported");
    }

    @Override // ohos.org.w3c.dom.Text
    public Text replaceWholeText(String str) throws DOMException {
        throw new DOMException(9, "Method not supported");
    }
}
