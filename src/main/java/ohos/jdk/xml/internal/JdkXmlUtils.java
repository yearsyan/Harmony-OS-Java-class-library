package ohos.jdk.xml.internal;

import ohos.com.sun.org.apache.xalan.internal.utils.XMLSecurityManager;
import ohos.com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import ohos.com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;
import ohos.com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl;
import ohos.javax.xml.parsers.DocumentBuilderFactory;
import ohos.javax.xml.parsers.ParserConfigurationException;
import ohos.javax.xml.parsers.SAXParserFactory;
import ohos.javax.xml.transform.TransformerConfigurationException;
import ohos.javax.xml.transform.sax.SAXTransformerFactory;
import ohos.org.w3c.dom.Document;
import ohos.org.xml.sax.SAXException;
import ohos.org.xml.sax.SAXNotRecognizedException;
import ohos.org.xml.sax.SAXNotSupportedException;
import ohos.org.xml.sax.XMLReader;
import ohos.org.xml.sax.helpers.XMLReaderFactory;

public class JdkXmlUtils {
    private static final String DOM_FACTORY_ID = "ohos.javax.xml.parsers.DocumentBuilderFactory";
    public static final String FEATURE_FALSE = "false";
    public static final String FEATURE_TRUE = "true";
    public static final String NAMESPACES_FEATURE = "http://xml.org/sax/features/namespaces";
    public static final String NAMESPACE_PREFIXES_FEATURE = "http://xml.org/sax/features/namespace-prefixes";
    public static final String OVERRIDE_PARSER = "jdk.xml.overrideDefaultParser";
    public static final boolean OVERRIDE_PARSER_DEFAULT = ((Boolean) SecuritySupport.getJAXPSystemProperty(Boolean.class, OVERRIDE_PARSER, "false")).booleanValue();
    private static final String SAX_DRIVER = "org.xml.sax.driver";
    private static final String SAX_FACTORY_ID = "ohos.javax.xml.parsers.SAXParserFactory";
    private static final SAXParserFactory defaultSAXFactory = getSAXFactory(false);

    public static int getValue(Object obj, int i) {
        if (obj == null) {
            return i;
        }
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        if (obj instanceof String) {
            return Integer.parseInt(String.valueOf(obj));
        }
        throw new IllegalArgumentException("Unexpected class: " + obj.getClass());
    }

    public static void setXMLReaderPropertyIfSupport(XMLReader xMLReader, String str, Object obj, boolean z) {
        try {
            xMLReader.setProperty(str, obj);
        } catch (SAXNotRecognizedException | SAXNotSupportedException e) {
            if (z) {
                XMLSecurityManager.printWarning(xMLReader.getClass().getName(), str, e);
            }
        }
    }

    public static XMLReader getXMLReader(boolean z, boolean z2) {
        XMLReader xMLReader;
        if (SecuritySupport.getSystemProperty(SAX_DRIVER) != null) {
            xMLReader = getXMLReaderWXMLReaderFactory();
        } else {
            xMLReader = z ? getXMLReaderWSAXFactory(z) : null;
        }
        if (xMLReader != null) {
            if (z2) {
                try {
                    xMLReader.setFeature("http://ohos.javax.xml.XMLConstants/feature/secure-processing", z2);
                } catch (SAXException e) {
                    XMLSecurityManager.printWarning(xMLReader.getClass().getName(), "http://ohos.javax.xml.XMLConstants/feature/secure-processing", e);
                }
            }
            try {
                xMLReader.setFeature("http://xml.org/sax/features/namespaces", true);
                xMLReader.setFeature(NAMESPACE_PREFIXES_FEATURE, false);
            } catch (SAXException unused) {
            }
            return xMLReader;
        }
        try {
            return defaultSAXFactory.newSAXParser().getXMLReader();
        } catch (ParserConfigurationException | SAXException unused2) {
            return xMLReader;
        }
    }

    public static Document getDOMDocument() {
        try {
            return getDOMFactory(false).newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException unused) {
            return null;
        }
    }

    public static DocumentBuilderFactory getDOMFactory(boolean z) {
        DocumentBuilderFactory documentBuilderFactory;
        if (SecuritySupport.getJAXPSystemProperty(DOM_FACTORY_ID) != null && System.getSecurityManager() == null) {
            z = true;
        }
        if (!z) {
            documentBuilderFactory = new DocumentBuilderFactoryImpl();
        } else {
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
        }
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setValidating(false);
        return documentBuilderFactory;
    }

    public static SAXParserFactory getSAXFactory(boolean z) {
        SAXParserFactory sAXParserFactory;
        if (SecuritySupport.getJAXPSystemProperty(SAX_FACTORY_ID) != null && System.getSecurityManager() == null) {
            z = true;
        }
        if (!z) {
            sAXParserFactory = new SAXParserFactoryImpl();
        } else {
            sAXParserFactory = SAXParserFactory.newInstance();
        }
        sAXParserFactory.setNamespaceAware(true);
        return sAXParserFactory;
    }

    public static SAXTransformerFactory getSAXTransformFactory(boolean z) {
        SAXTransformerFactory sAXTransformerFactory;
        if (z) {
            sAXTransformerFactory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
        } else {
            sAXTransformerFactory = new TransformerFactoryImpl();
        }
        try {
            sAXTransformerFactory.setFeature(OVERRIDE_PARSER, z);
        } catch (TransformerConfigurationException unused) {
        }
        return sAXTransformerFactory;
    }

    private static XMLReader getXMLReaderWSAXFactory(boolean z) {
        try {
            return getSAXFactory(z).newSAXParser().getXMLReader();
        } catch (ParserConfigurationException | SAXException unused) {
            return getXMLReaderWXMLReaderFactory();
        }
    }

    private static XMLReader getXMLReaderWXMLReaderFactory() {
        try {
            return XMLReaderFactory.createXMLReader();
        } catch (SAXException unused) {
            return null;
        }
    }
}
