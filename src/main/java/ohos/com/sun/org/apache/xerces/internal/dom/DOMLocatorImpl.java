package ohos.com.sun.org.apache.xerces.internal.dom;

import ohos.org.w3c.dom.DOMLocator;
import ohos.org.w3c.dom.Node;

public class DOMLocatorImpl implements DOMLocator {
    public int fByteOffset = -1;
    public int fColumnNumber = -1;
    public int fLineNumber = -1;
    public Node fRelatedNode = null;
    public String fUri = null;
    public int fUtf16Offset = -1;

    public DOMLocatorImpl() {
    }

    public DOMLocatorImpl(int i, int i2, String str) {
        this.fLineNumber = i;
        this.fColumnNumber = i2;
        this.fUri = str;
    }

    public DOMLocatorImpl(int i, int i2, int i3, String str) {
        this.fLineNumber = i;
        this.fColumnNumber = i2;
        this.fUri = str;
        this.fUtf16Offset = i3;
    }

    public DOMLocatorImpl(int i, int i2, int i3, Node node, String str) {
        this.fLineNumber = i;
        this.fColumnNumber = i2;
        this.fByteOffset = i3;
        this.fRelatedNode = node;
        this.fUri = str;
    }

    public DOMLocatorImpl(int i, int i2, int i3, Node node, String str, int i4) {
        this.fLineNumber = i;
        this.fColumnNumber = i2;
        this.fByteOffset = i3;
        this.fRelatedNode = node;
        this.fUri = str;
        this.fUtf16Offset = i4;
    }

    @Override // ohos.org.w3c.dom.DOMLocator
    public int getLineNumber() {
        return this.fLineNumber;
    }

    @Override // ohos.org.w3c.dom.DOMLocator
    public int getColumnNumber() {
        return this.fColumnNumber;
    }

    @Override // ohos.org.w3c.dom.DOMLocator
    public String getUri() {
        return this.fUri;
    }

    @Override // ohos.org.w3c.dom.DOMLocator
    public Node getRelatedNode() {
        return this.fRelatedNode;
    }

    @Override // ohos.org.w3c.dom.DOMLocator
    public int getByteOffset() {
        return this.fByteOffset;
    }

    @Override // ohos.org.w3c.dom.DOMLocator
    public int getUtf16Offset() {
        return this.fUtf16Offset;
    }
}
