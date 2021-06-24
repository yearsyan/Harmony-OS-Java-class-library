package ohos.com.sun.org.apache.xerces.internal.xni.grammars;

import java.io.IOException;
import java.util.Locale;
import ohos.com.sun.org.apache.xerces.internal.xni.XNIException;
import ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
import ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
import ohos.com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;

public interface XMLGrammarLoader {
    XMLEntityResolver getEntityResolver();

    XMLErrorHandler getErrorHandler();

    boolean getFeature(String str) throws XMLConfigurationException;

    Locale getLocale();

    Object getProperty(String str) throws XMLConfigurationException;

    String[] getRecognizedFeatures();

    String[] getRecognizedProperties();

    Grammar loadGrammar(XMLInputSource xMLInputSource) throws IOException, XNIException;

    void setEntityResolver(XMLEntityResolver xMLEntityResolver);

    void setErrorHandler(XMLErrorHandler xMLErrorHandler);

    void setFeature(String str, boolean z) throws XMLConfigurationException;

    void setLocale(Locale locale);

    void setProperty(String str, Object obj) throws XMLConfigurationException;
}
