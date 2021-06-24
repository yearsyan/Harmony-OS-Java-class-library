package ohos.com.sun.org.apache.xml.internal.utils;

import java.io.Serializable;
import ohos.javax.xml.transform.SourceLocator;
import ohos.org.xml.sax.Locator;
import ohos.org.xml.sax.SAXParseException;
import ohos.org.xml.sax.helpers.LocatorImpl;

public class SAXSourceLocator extends LocatorImpl implements SourceLocator, Serializable {
    static final long serialVersionUID = 3181680946321164112L;
    Locator m_locator;

    public SAXSourceLocator() {
    }

    public SAXSourceLocator(Locator locator) {
        this.m_locator = locator;
        setColumnNumber(locator.getColumnNumber());
        setLineNumber(locator.getLineNumber());
        setPublicId(locator.getPublicId());
        setSystemId(locator.getSystemId());
    }

    public SAXSourceLocator(SourceLocator sourceLocator) {
        this.m_locator = null;
        setColumnNumber(sourceLocator.getColumnNumber());
        setLineNumber(sourceLocator.getLineNumber());
        setPublicId(sourceLocator.getPublicId());
        setSystemId(sourceLocator.getSystemId());
    }

    public SAXSourceLocator(SAXParseException sAXParseException) {
        setLineNumber(sAXParseException.getLineNumber());
        setColumnNumber(sAXParseException.getColumnNumber());
        setPublicId(sAXParseException.getPublicId());
        setSystemId(sAXParseException.getSystemId());
    }

    @Override // ohos.org.xml.sax.helpers.LocatorImpl, ohos.org.xml.sax.Locator, ohos.javax.xml.transform.SourceLocator
    public String getPublicId() {
        Locator locator = this.m_locator;
        return locator == null ? super.getPublicId() : locator.getPublicId();
    }

    @Override // ohos.org.xml.sax.helpers.LocatorImpl, ohos.org.xml.sax.Locator, ohos.javax.xml.transform.SourceLocator
    public String getSystemId() {
        Locator locator = this.m_locator;
        return locator == null ? super.getSystemId() : locator.getSystemId();
    }

    @Override // ohos.org.xml.sax.helpers.LocatorImpl, ohos.org.xml.sax.Locator, ohos.javax.xml.transform.SourceLocator
    public int getLineNumber() {
        Locator locator = this.m_locator;
        return locator == null ? super.getLineNumber() : locator.getLineNumber();
    }

    @Override // ohos.org.xml.sax.helpers.LocatorImpl, ohos.org.xml.sax.Locator, ohos.javax.xml.transform.SourceLocator
    public int getColumnNumber() {
        Locator locator = this.m_locator;
        return locator == null ? super.getColumnNumber() : locator.getColumnNumber();
    }
}
