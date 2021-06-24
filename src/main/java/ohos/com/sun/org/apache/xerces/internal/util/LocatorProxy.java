package ohos.com.sun.org.apache.xerces.internal.util;

import ohos.com.sun.org.apache.xerces.internal.xni.XMLLocator;
import ohos.org.xml.sax.ext.Locator2;

public class LocatorProxy implements Locator2 {
    private final XMLLocator fLocator;

    public LocatorProxy(XMLLocator xMLLocator) {
        this.fLocator = xMLLocator;
    }

    @Override // ohos.org.xml.sax.Locator
    public String getPublicId() {
        return this.fLocator.getPublicId();
    }

    @Override // ohos.org.xml.sax.Locator
    public String getSystemId() {
        return this.fLocator.getExpandedSystemId();
    }

    @Override // ohos.org.xml.sax.Locator
    public int getLineNumber() {
        return this.fLocator.getLineNumber();
    }

    @Override // ohos.org.xml.sax.Locator
    public int getColumnNumber() {
        return this.fLocator.getColumnNumber();
    }

    @Override // ohos.org.xml.sax.ext.Locator2
    public String getXMLVersion() {
        return this.fLocator.getXMLVersion();
    }

    @Override // ohos.org.xml.sax.ext.Locator2
    public String getEncoding() {
        return this.fLocator.getEncoding();
    }
}
