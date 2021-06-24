package ohos.com.sun.org.apache.xerces.internal.jaxp;

import java.util.HashMap;
import java.util.Map;
import ohos.com.sun.org.apache.xerces.internal.util.SAXMessageFormatter;
import ohos.javax.xml.parsers.ParserConfigurationException;
import ohos.javax.xml.parsers.SAXParser;
import ohos.javax.xml.parsers.SAXParserFactory;
import ohos.javax.xml.validation.Schema;
import ohos.org.xml.sax.SAXException;
import ohos.org.xml.sax.SAXNotRecognizedException;
import ohos.org.xml.sax.SAXNotSupportedException;

public class SAXParserFactoryImpl extends SAXParserFactory {
    private static final String NAMESPACES_FEATURE = "http://xml.org/sax/features/namespaces";
    private static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
    private static final String XINCLUDE_FEATURE = "http://apache.org/xml/features/xinclude";
    private boolean fSecureProcess = true;
    private Map<String, Boolean> features;
    private Schema grammar;
    private boolean isXIncludeAware;

    @Override // ohos.javax.xml.parsers.SAXParserFactory
    public SAXParser newSAXParser() throws ParserConfigurationException {
        try {
            return new SAXParserImpl(this, this.features, this.fSecureProcess);
        } catch (SAXException e) {
            throw new ParserConfigurationException(e.getMessage());
        }
    }

    private SAXParserImpl newSAXParserImpl() throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
        try {
            return new SAXParserImpl(this, this.features);
        } catch (SAXNotSupportedException e) {
            throw e;
        } catch (SAXNotRecognizedException e2) {
            throw e2;
        } catch (SAXException e3) {
            throw new ParserConfigurationException(e3.getMessage());
        }
    }

    @Override // ohos.javax.xml.parsers.SAXParserFactory
    public void setFeature(String str, boolean z) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
        if (str == null) {
            throw new NullPointerException();
        } else if (!str.equals("http://ohos.javax.xml.XMLConstants/feature/secure-processing")) {
            putInFeatures(str, z);
            try {
                newSAXParserImpl();
            } catch (SAXNotSupportedException e) {
                this.features.remove(str);
                throw e;
            } catch (SAXNotRecognizedException e2) {
                this.features.remove(str);
                throw e2;
            }
        } else if (System.getSecurityManager() == null || z) {
            this.fSecureProcess = z;
            putInFeatures(str, z);
        } else {
            throw new ParserConfigurationException(SAXMessageFormatter.formatMessage(null, "jaxp-secureprocessing-feature", null));
        }
    }

    @Override // ohos.javax.xml.parsers.SAXParserFactory
    public boolean getFeature(String str) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
        if (str == null) {
            throw new NullPointerException();
        } else if (str.equals("http://ohos.javax.xml.XMLConstants/feature/secure-processing")) {
            return this.fSecureProcess;
        } else {
            return newSAXParserImpl().getXMLReader().getFeature(str);
        }
    }

    @Override // ohos.javax.xml.parsers.SAXParserFactory
    public Schema getSchema() {
        return this.grammar;
    }

    @Override // ohos.javax.xml.parsers.SAXParserFactory
    public void setSchema(Schema schema) {
        this.grammar = schema;
    }

    @Override // ohos.javax.xml.parsers.SAXParserFactory
    public boolean isXIncludeAware() {
        return getFromFeatures(XINCLUDE_FEATURE);
    }

    @Override // ohos.javax.xml.parsers.SAXParserFactory
    public void setXIncludeAware(boolean z) {
        putInFeatures(XINCLUDE_FEATURE, z);
    }

    @Override // ohos.javax.xml.parsers.SAXParserFactory
    public void setValidating(boolean z) {
        putInFeatures(VALIDATION_FEATURE, z);
    }

    @Override // ohos.javax.xml.parsers.SAXParserFactory
    public boolean isValidating() {
        return getFromFeatures(VALIDATION_FEATURE);
    }

    private void putInFeatures(String str, boolean z) {
        if (this.features == null) {
            this.features = new HashMap();
        }
        this.features.put(str, z ? Boolean.TRUE : Boolean.FALSE);
    }

    private boolean getFromFeatures(String str) {
        Boolean bool;
        Map<String, Boolean> map = this.features;
        if (map == null || (bool = map.get(str)) == null) {
            return false;
        }
        return bool.booleanValue();
    }

    @Override // ohos.javax.xml.parsers.SAXParserFactory
    public boolean isNamespaceAware() {
        return getFromFeatures("http://xml.org/sax/features/namespaces");
    }

    @Override // ohos.javax.xml.parsers.SAXParserFactory
    public void setNamespaceAware(boolean z) {
        putInFeatures("http://xml.org/sax/features/namespaces", z);
    }
}
