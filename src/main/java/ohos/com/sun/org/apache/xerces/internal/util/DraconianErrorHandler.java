package ohos.com.sun.org.apache.xerces.internal.util;

import ohos.org.xml.sax.ErrorHandler;
import ohos.org.xml.sax.SAXException;
import ohos.org.xml.sax.SAXParseException;

public class DraconianErrorHandler implements ErrorHandler {
    public static final ErrorHandler theInstance = new DraconianErrorHandler();

    @Override // ohos.org.xml.sax.ErrorHandler
    public void warning(SAXParseException sAXParseException) throws SAXException {
    }

    private DraconianErrorHandler() {
    }

    @Override // ohos.org.xml.sax.ErrorHandler
    public void error(SAXParseException sAXParseException) throws SAXException {
        throw sAXParseException;
    }

    @Override // ohos.org.xml.sax.ErrorHandler
    public void fatalError(SAXParseException sAXParseException) throws SAXException {
        throw sAXParseException;
    }
}
