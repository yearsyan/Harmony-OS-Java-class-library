package ohos.com.sun.org.apache.xerces.internal.impl;

import java.util.HashMap;
import ohos.com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
import ohos.com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
import ohos.com.sun.xml.internal.stream.StaxEntityResolverWrapper;
import ohos.javax.xml.stream.XMLInputFactory;
import ohos.javax.xml.stream.XMLOutputFactory;
import ohos.javax.xml.stream.XMLResolver;

public class PropertyManager {
    public static final int CONTEXT_READER = 1;
    public static final int CONTEXT_WRITER = 2;
    private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
    public static final String STAX_ENTITIES = "javax.xml.stream.entities";
    public static final String STAX_NOTATIONS = "javax.xml.stream.notations";
    private static final String STRING_INTERNING = "http://xml.org/sax/features/string-interning";
    private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
    private XMLSecurityManager fSecurityManager;
    private XMLSecurityPropertyManager fSecurityPropertyMgr;
    HashMap supportedProps = new HashMap();

    public PropertyManager(int i) {
        if (i == 1) {
            initConfigurableReaderProperties();
        } else if (i == 2) {
            initWriterProps();
        }
    }

    public PropertyManager(PropertyManager propertyManager) {
        this.supportedProps.putAll(propertyManager.getProperties());
        this.fSecurityManager = (XMLSecurityManager) getProperty("http://apache.org/xml/properties/security-manager");
        this.fSecurityPropertyMgr = (XMLSecurityPropertyManager) getProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager");
    }

    private HashMap getProperties() {
        return this.supportedProps;
    }

    private void initConfigurableReaderProperties() {
        this.supportedProps.put(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
        this.supportedProps.put(XMLInputFactory.IS_VALIDATING, Boolean.FALSE);
        this.supportedProps.put(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.TRUE);
        this.supportedProps.put(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.TRUE);
        this.supportedProps.put(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
        this.supportedProps.put(XMLInputFactory.SUPPORT_DTD, Boolean.TRUE);
        this.supportedProps.put(XMLInputFactory.REPORTER, null);
        this.supportedProps.put(XMLInputFactory.RESOLVER, null);
        this.supportedProps.put(XMLInputFactory.ALLOCATOR, null);
        this.supportedProps.put(STAX_NOTATIONS, null);
        this.supportedProps.put(STRING_INTERNING, new Boolean(true));
        this.supportedProps.put("http://apache.org/xml/features/allow-java-encodings", new Boolean(true));
        this.supportedProps.put(Constants.ADD_NAMESPACE_DECL_AS_ATTRIBUTE, Boolean.FALSE);
        this.supportedProps.put(Constants.READER_IN_DEFINED_STATE, new Boolean(true));
        this.supportedProps.put(Constants.REUSE_INSTANCE, new Boolean(true));
        this.supportedProps.put("http://java.sun.com/xml/stream/properties/report-cdata-event", new Boolean(false));
        this.supportedProps.put("http://java.sun.com/xml/stream/properties/ignore-external-dtd", Boolean.FALSE);
        this.supportedProps.put("http://apache.org/xml/features/validation/warn-on-duplicate-attdef", new Boolean(false));
        this.supportedProps.put("http://apache.org/xml/features/warn-on-duplicate-entitydef", new Boolean(false));
        this.supportedProps.put("http://apache.org/xml/features/validation/warn-on-undeclared-elemdef", new Boolean(false));
        this.fSecurityManager = new XMLSecurityManager(true);
        this.supportedProps.put("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
        this.fSecurityPropertyMgr = new XMLSecurityPropertyManager();
        this.supportedProps.put("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.fSecurityPropertyMgr);
    }

    private void initWriterProps() {
        this.supportedProps.put(XMLOutputFactory.IS_REPAIRING_NAMESPACES, Boolean.FALSE);
        this.supportedProps.put(Constants.ESCAPE_CHARACTERS, Boolean.TRUE);
        this.supportedProps.put(Constants.REUSE_INSTANCE, new Boolean(true));
    }

    public boolean containsProperty(String str) {
        XMLSecurityManager xMLSecurityManager;
        XMLSecurityPropertyManager xMLSecurityPropertyManager;
        return this.supportedProps.containsKey(str) || ((xMLSecurityManager = this.fSecurityManager) != null && xMLSecurityManager.getIndex(str) > -1) || ((xMLSecurityPropertyManager = this.fSecurityPropertyMgr) != null && xMLSecurityPropertyManager.getIndex(str) > -1);
    }

    public Object getProperty(String str) {
        return this.supportedProps.get(str);
    }

    public void setProperty(String str, Object obj) {
        String str2;
        XMLSecurityPropertyManager xMLSecurityPropertyManager;
        if (str == XMLInputFactory.IS_NAMESPACE_AWARE || str.equals(XMLInputFactory.IS_NAMESPACE_AWARE)) {
            str2 = "http://apache.org/xml/features/namespaces";
        } else {
            if (str == XMLInputFactory.IS_VALIDATING || str.equals(XMLInputFactory.IS_VALIDATING)) {
                if ((obj instanceof Boolean) && ((Boolean) obj).booleanValue()) {
                    throw new IllegalArgumentException("true value of isValidating not supported");
                }
            } else if (str == STRING_INTERNING || str.equals(STRING_INTERNING)) {
                if ((obj instanceof Boolean) && !((Boolean) obj).booleanValue()) {
                    throw new IllegalArgumentException("false value of http://xml.org/sax/features/string-interningfeature is not supported");
                }
            } else if (str == XMLInputFactory.RESOLVER || str.equals(XMLInputFactory.RESOLVER)) {
                this.supportedProps.put("http://apache.org/xml/properties/internal/stax-entity-resolver", new StaxEntityResolverWrapper((XMLResolver) obj));
            }
            str2 = null;
        }
        if (str.equals("http://apache.org/xml/properties/security-manager")) {
            this.fSecurityManager = XMLSecurityManager.convert(obj, this.fSecurityManager);
            this.supportedProps.put("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
        } else if (str.equals("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager")) {
            if (obj == null) {
                this.fSecurityPropertyMgr = new XMLSecurityPropertyManager();
            } else {
                this.fSecurityPropertyMgr = (XMLSecurityPropertyManager) obj;
            }
            this.supportedProps.put("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.fSecurityPropertyMgr);
        } else {
            XMLSecurityManager xMLSecurityManager = this.fSecurityManager;
            if ((xMLSecurityManager == null || !xMLSecurityManager.setLimit(str, XMLSecurityManager.State.APIPROPERTY, obj)) && ((xMLSecurityPropertyManager = this.fSecurityPropertyMgr) == null || !xMLSecurityPropertyManager.setValue(str, XMLSecurityPropertyManager.State.APIPROPERTY, obj))) {
                this.supportedProps.put(str, obj);
            }
            if (str2 != null) {
                this.supportedProps.put(str2, obj);
            }
        }
    }

    public String toString() {
        return this.supportedProps.toString();
    }
}
