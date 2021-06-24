package ohos.com.sun.org.apache.xerces.internal.jaxp;

import java.util.HashMap;
import java.util.Map;
import ohos.com.sun.org.apache.xerces.internal.parsers.DOMParser;
import ohos.com.sun.org.apache.xerces.internal.util.SAXMessageFormatter;
import ohos.javax.xml.parsers.DocumentBuilder;
import ohos.javax.xml.parsers.DocumentBuilderFactory;
import ohos.javax.xml.parsers.ParserConfigurationException;
import ohos.javax.xml.validation.Schema;
import ohos.org.xml.sax.SAXException;
import ohos.org.xml.sax.SAXNotRecognizedException;
import ohos.org.xml.sax.SAXNotSupportedException;

public class DocumentBuilderFactoryImpl extends DocumentBuilderFactory {
    private Map<String, Object> attributes;
    private boolean fSecureProcess = true;
    private Map<String, Boolean> features;
    private Schema grammar;
    private boolean isXIncludeAware;

    @Override // ohos.javax.xml.parsers.DocumentBuilderFactory
    public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
        Map<String, Object> map;
        if (!(this.grammar == null || (map = this.attributes) == null)) {
            if (map.containsKey(JAXPConstants.JAXP_SCHEMA_LANGUAGE)) {
                throw new ParserConfigurationException(SAXMessageFormatter.formatMessage(null, "schema-already-specified", new Object[]{JAXPConstants.JAXP_SCHEMA_LANGUAGE}));
            } else if (this.attributes.containsKey(JAXPConstants.JAXP_SCHEMA_SOURCE)) {
                throw new ParserConfigurationException(SAXMessageFormatter.formatMessage(null, "schema-already-specified", new Object[]{JAXPConstants.JAXP_SCHEMA_SOURCE}));
            }
        }
        try {
            return new DocumentBuilderImpl(this, this.attributes, this.features, this.fSecureProcess);
        } catch (SAXException e) {
            throw new ParserConfigurationException(e.getMessage());
        }
    }

    @Override // ohos.javax.xml.parsers.DocumentBuilderFactory
    public void setAttribute(String str, Object obj) throws IllegalArgumentException {
        if (obj == null) {
            Map<String, Object> map = this.attributes;
            if (map != null) {
                map.remove(str);
                return;
            }
            return;
        }
        if (this.attributes == null) {
            this.attributes = new HashMap();
        }
        this.attributes.put(str, obj);
        try {
            new DocumentBuilderImpl(this, this.attributes, this.features);
        } catch (Exception e) {
            this.attributes.remove(str);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override // ohos.javax.xml.parsers.DocumentBuilderFactory
    public Object getAttribute(String str) throws IllegalArgumentException {
        Object obj;
        Map<String, Object> map = this.attributes;
        if (map != null && (obj = map.get(str)) != null) {
            return obj;
        }
        DOMParser dOMParser = null;
        try {
            dOMParser = new DocumentBuilderImpl(this, this.attributes, this.features).getDOMParser();
            return dOMParser.getProperty(str);
        } catch (SAXException e) {
            try {
                return dOMParser.getFeature(str) ? Boolean.TRUE : Boolean.FALSE;
            } catch (SAXException unused) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }
    }

    @Override // ohos.javax.xml.parsers.DocumentBuilderFactory
    public Schema getSchema() {
        return this.grammar;
    }

    @Override // ohos.javax.xml.parsers.DocumentBuilderFactory
    public void setSchema(Schema schema) {
        this.grammar = schema;
    }

    @Override // ohos.javax.xml.parsers.DocumentBuilderFactory
    public boolean isXIncludeAware() {
        return this.isXIncludeAware;
    }

    @Override // ohos.javax.xml.parsers.DocumentBuilderFactory
    public void setXIncludeAware(boolean z) {
        this.isXIncludeAware = z;
    }

    @Override // ohos.javax.xml.parsers.DocumentBuilderFactory
    public boolean getFeature(String str) throws ParserConfigurationException {
        Boolean bool;
        if (str.equals("http://ohos.javax.xml.XMLConstants/feature/secure-processing")) {
            return this.fSecureProcess;
        }
        Map<String, Boolean> map = this.features;
        if (map != null && (bool = map.get(str)) != null) {
            return bool.booleanValue();
        }
        try {
            return new DocumentBuilderImpl(this, this.attributes, this.features).getDOMParser().getFeature(str);
        } catch (SAXException e) {
            throw new ParserConfigurationException(e.getMessage());
        }
    }

    @Override // ohos.javax.xml.parsers.DocumentBuilderFactory
    public void setFeature(String str, boolean z) throws ParserConfigurationException {
        if (this.features == null) {
            this.features = new HashMap();
        }
        if (!str.equals("http://ohos.javax.xml.XMLConstants/feature/secure-processing")) {
            this.features.put(str, z ? Boolean.TRUE : Boolean.FALSE);
            try {
                new DocumentBuilderImpl(this, this.attributes, this.features);
            } catch (SAXNotSupportedException e) {
                this.features.remove(str);
                throw new ParserConfigurationException(e.getMessage());
            } catch (SAXNotRecognizedException e2) {
                this.features.remove(str);
                throw new ParserConfigurationException(e2.getMessage());
            }
        } else if (System.getSecurityManager() == null || z) {
            this.fSecureProcess = z;
            this.features.put(str, z ? Boolean.TRUE : Boolean.FALSE);
        } else {
            throw new ParserConfigurationException(SAXMessageFormatter.formatMessage(null, "jaxp-secureprocessing-feature", null));
        }
    }
}
