package ohos.com.sun.org.apache.xerces.internal.dom;

import ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLParseException;
import ohos.org.w3c.dom.DOMError;
import ohos.org.w3c.dom.DOMLocator;

public class DOMErrorImpl implements DOMError {
    public Exception fException = null;
    public DOMLocatorImpl fLocator = new DOMLocatorImpl();
    public String fMessage = null;
    public Object fRelatedData;
    public short fSeverity = 1;
    public String fType;

    public DOMErrorImpl() {
    }

    public DOMErrorImpl(short s, XMLParseException xMLParseException) {
        this.fSeverity = s;
        this.fException = xMLParseException;
        this.fLocator = createDOMLocator(xMLParseException);
    }

    @Override // ohos.org.w3c.dom.DOMError
    public short getSeverity() {
        return this.fSeverity;
    }

    @Override // ohos.org.w3c.dom.DOMError
    public String getMessage() {
        return this.fMessage;
    }

    @Override // ohos.org.w3c.dom.DOMError
    public DOMLocator getLocation() {
        return this.fLocator;
    }

    private DOMLocatorImpl createDOMLocator(XMLParseException xMLParseException) {
        return new DOMLocatorImpl(xMLParseException.getLineNumber(), xMLParseException.getColumnNumber(), xMLParseException.getCharacterOffset(), xMLParseException.getExpandedSystemId());
    }

    @Override // ohos.org.w3c.dom.DOMError
    public Object getRelatedException() {
        return this.fException;
    }

    public void reset() {
        this.fSeverity = 1;
        this.fException = null;
    }

    @Override // ohos.org.w3c.dom.DOMError
    public String getType() {
        return this.fType;
    }

    @Override // ohos.org.w3c.dom.DOMError
    public Object getRelatedData() {
        return this.fRelatedData;
    }
}
