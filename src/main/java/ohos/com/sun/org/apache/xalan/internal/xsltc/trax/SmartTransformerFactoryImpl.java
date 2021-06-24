package ohos.com.sun.org.apache.xalan.internal.xsltc.trax;

import ohos.com.sun.org.apache.xalan.internal.utils.ObjectFactory;
import ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import ohos.javax.xml.transform.ErrorListener;
import ohos.javax.xml.transform.Source;
import ohos.javax.xml.transform.Templates;
import ohos.javax.xml.transform.Transformer;
import ohos.javax.xml.transform.TransformerConfigurationException;
import ohos.javax.xml.transform.TransformerException;
import ohos.javax.xml.transform.URIResolver;
import ohos.javax.xml.transform.dom.DOMResult;
import ohos.javax.xml.transform.dom.DOMSource;
import ohos.javax.xml.transform.sax.SAXResult;
import ohos.javax.xml.transform.sax.SAXSource;
import ohos.javax.xml.transform.sax.SAXTransformerFactory;
import ohos.javax.xml.transform.sax.TemplatesHandler;
import ohos.javax.xml.transform.sax.TransformerHandler;
import ohos.javax.xml.transform.stream.StreamResult;
import ohos.javax.xml.transform.stream.StreamSource;
import ohos.org.xml.sax.XMLFilter;

public class SmartTransformerFactoryImpl extends SAXTransformerFactory {
    private static final String CLASS_NAME = "SmartTransformerFactoryImpl";
    private SAXTransformerFactory _currFactory = null;
    private ErrorListener _errorlistener = null;
    private URIResolver _uriresolver = null;
    private SAXTransformerFactory _xalanFactory = null;
    private SAXTransformerFactory _xsltcFactory = null;
    private boolean featureSecureProcessing = false;

    private void createXSLTCTransformerFactory() {
        this._xsltcFactory = new TransformerFactoryImpl();
        this._currFactory = this._xsltcFactory;
    }

    private void createXalanTransformerFactory() {
        try {
            this._xalanFactory = (SAXTransformerFactory) ObjectFactory.findProviderClass("com.sun.org.apache.xalan.internal.processor.TransformerFactoryImpl", true).newInstance();
        } catch (ClassNotFoundException unused) {
            System.err.println("ohos.com.sun.org.apache.xalan.internal.xsltc.trax.SmartTransformerFactoryImpl could not create an com.sun.org.apache.xalan.internal.processor.TransformerFactoryImpl.");
        } catch (InstantiationException unused2) {
            System.err.println("ohos.com.sun.org.apache.xalan.internal.xsltc.trax.SmartTransformerFactoryImpl could not create an com.sun.org.apache.xalan.internal.processor.TransformerFactoryImpl.");
        } catch (IllegalAccessException unused3) {
            System.err.println("ohos.com.sun.org.apache.xalan.internal.xsltc.trax.SmartTransformerFactoryImpl could not create an com.sun.org.apache.xalan.internal.processor.TransformerFactoryImpl.");
        }
        this._currFactory = this._xalanFactory;
    }

    @Override // ohos.javax.xml.transform.TransformerFactory
    public void setErrorListener(ErrorListener errorListener) throws IllegalArgumentException {
        this._errorlistener = errorListener;
    }

    @Override // ohos.javax.xml.transform.TransformerFactory
    public ErrorListener getErrorListener() {
        return this._errorlistener;
    }

    @Override // ohos.javax.xml.transform.TransformerFactory
    public Object getAttribute(String str) throws IllegalArgumentException {
        if (str.equals(TransformerFactoryImpl.TRANSLET_NAME) || str.equals("debug")) {
            if (this._xsltcFactory == null) {
                createXSLTCTransformerFactory();
            }
            return this._xsltcFactory.getAttribute(str);
        }
        if (this._xalanFactory == null) {
            createXalanTransformerFactory();
        }
        return this._xalanFactory.getAttribute(str);
    }

    @Override // ohos.javax.xml.transform.TransformerFactory
    public void setAttribute(String str, Object obj) throws IllegalArgumentException {
        if (str.equals(TransformerFactoryImpl.TRANSLET_NAME) || str.equals("debug")) {
            if (this._xsltcFactory == null) {
                createXSLTCTransformerFactory();
            }
            this._xsltcFactory.setAttribute(str, obj);
            return;
        }
        if (this._xalanFactory == null) {
            createXalanTransformerFactory();
        }
        this._xalanFactory.setAttribute(str, obj);
    }

    @Override // ohos.javax.xml.transform.TransformerFactory
    public void setFeature(String str, boolean z) throws TransformerConfigurationException {
        if (str == null) {
            throw new NullPointerException(new ErrorMsg(ErrorMsg.JAXP_SET_FEATURE_NULL_NAME).toString());
        } else if (str.equals("http://ohos.javax.xml.XMLConstants/feature/secure-processing")) {
            this.featureSecureProcessing = z;
        } else {
            throw new TransformerConfigurationException(new ErrorMsg(ErrorMsg.JAXP_UNSUPPORTED_FEATURE, str).toString());
        }
    }

    @Override // ohos.javax.xml.transform.TransformerFactory
    public boolean getFeature(String str) {
        String[] strArr = {DOMSource.FEATURE, DOMResult.FEATURE, SAXSource.FEATURE, SAXResult.FEATURE, StreamSource.FEATURE, StreamResult.FEATURE};
        if (str != null) {
            for (String str2 : strArr) {
                if (str.equals(str2)) {
                    return true;
                }
            }
            if (str.equals("http://ohos.javax.xml.XMLConstants/feature/secure-processing")) {
                return this.featureSecureProcessing;
            }
            return false;
        }
        throw new NullPointerException(new ErrorMsg(ErrorMsg.JAXP_GET_FEATURE_NULL_NAME).toString());
    }

    @Override // ohos.javax.xml.transform.TransformerFactory
    public URIResolver getURIResolver() {
        return this._uriresolver;
    }

    @Override // ohos.javax.xml.transform.TransformerFactory
    public void setURIResolver(URIResolver uRIResolver) {
        this._uriresolver = uRIResolver;
    }

    @Override // ohos.javax.xml.transform.TransformerFactory
    public Source getAssociatedStylesheet(Source source, String str, String str2, String str3) throws TransformerConfigurationException {
        if (this._currFactory == null) {
            createXSLTCTransformerFactory();
        }
        return this._currFactory.getAssociatedStylesheet(source, str, str2, str3);
    }

    @Override // ohos.javax.xml.transform.TransformerFactory
    public Transformer newTransformer() throws TransformerConfigurationException {
        if (this._xalanFactory == null) {
            createXalanTransformerFactory();
        }
        ErrorListener errorListener = this._errorlistener;
        if (errorListener != null) {
            this._xalanFactory.setErrorListener(errorListener);
        }
        URIResolver uRIResolver = this._uriresolver;
        if (uRIResolver != null) {
            this._xalanFactory.setURIResolver(uRIResolver);
        }
        this._currFactory = this._xalanFactory;
        return this._currFactory.newTransformer();
    }

    @Override // ohos.javax.xml.transform.TransformerFactory
    public Transformer newTransformer(Source source) throws TransformerConfigurationException {
        if (this._xalanFactory == null) {
            createXalanTransformerFactory();
        }
        ErrorListener errorListener = this._errorlistener;
        if (errorListener != null) {
            this._xalanFactory.setErrorListener(errorListener);
        }
        URIResolver uRIResolver = this._uriresolver;
        if (uRIResolver != null) {
            this._xalanFactory.setURIResolver(uRIResolver);
        }
        this._currFactory = this._xalanFactory;
        return this._currFactory.newTransformer(source);
    }

    @Override // ohos.javax.xml.transform.TransformerFactory
    public Templates newTemplates(Source source) throws TransformerConfigurationException {
        if (this._xsltcFactory == null) {
            createXSLTCTransformerFactory();
        }
        ErrorListener errorListener = this._errorlistener;
        if (errorListener != null) {
            this._xsltcFactory.setErrorListener(errorListener);
        }
        URIResolver uRIResolver = this._uriresolver;
        if (uRIResolver != null) {
            this._xsltcFactory.setURIResolver(uRIResolver);
        }
        this._currFactory = this._xsltcFactory;
        return this._currFactory.newTemplates(source);
    }

    @Override // ohos.javax.xml.transform.sax.SAXTransformerFactory
    public TemplatesHandler newTemplatesHandler() throws TransformerConfigurationException {
        if (this._xsltcFactory == null) {
            createXSLTCTransformerFactory();
        }
        ErrorListener errorListener = this._errorlistener;
        if (errorListener != null) {
            this._xsltcFactory.setErrorListener(errorListener);
        }
        URIResolver uRIResolver = this._uriresolver;
        if (uRIResolver != null) {
            this._xsltcFactory.setURIResolver(uRIResolver);
        }
        return this._xsltcFactory.newTemplatesHandler();
    }

    @Override // ohos.javax.xml.transform.sax.SAXTransformerFactory
    public TransformerHandler newTransformerHandler() throws TransformerConfigurationException {
        if (this._xalanFactory == null) {
            createXalanTransformerFactory();
        }
        ErrorListener errorListener = this._errorlistener;
        if (errorListener != null) {
            this._xalanFactory.setErrorListener(errorListener);
        }
        URIResolver uRIResolver = this._uriresolver;
        if (uRIResolver != null) {
            this._xalanFactory.setURIResolver(uRIResolver);
        }
        return this._xalanFactory.newTransformerHandler();
    }

    @Override // ohos.javax.xml.transform.sax.SAXTransformerFactory
    public TransformerHandler newTransformerHandler(Source source) throws TransformerConfigurationException {
        if (this._xalanFactory == null) {
            createXalanTransformerFactory();
        }
        ErrorListener errorListener = this._errorlistener;
        if (errorListener != null) {
            this._xalanFactory.setErrorListener(errorListener);
        }
        URIResolver uRIResolver = this._uriresolver;
        if (uRIResolver != null) {
            this._xalanFactory.setURIResolver(uRIResolver);
        }
        return this._xalanFactory.newTransformerHandler(source);
    }

    @Override // ohos.javax.xml.transform.sax.SAXTransformerFactory
    public TransformerHandler newTransformerHandler(Templates templates) throws TransformerConfigurationException {
        if (this._xsltcFactory == null) {
            createXSLTCTransformerFactory();
        }
        ErrorListener errorListener = this._errorlistener;
        if (errorListener != null) {
            this._xsltcFactory.setErrorListener(errorListener);
        }
        URIResolver uRIResolver = this._uriresolver;
        if (uRIResolver != null) {
            this._xsltcFactory.setURIResolver(uRIResolver);
        }
        return this._xsltcFactory.newTransformerHandler(templates);
    }

    @Override // ohos.javax.xml.transform.sax.SAXTransformerFactory
    public XMLFilter newXMLFilter(Source source) throws TransformerConfigurationException {
        if (this._xsltcFactory == null) {
            createXSLTCTransformerFactory();
        }
        ErrorListener errorListener = this._errorlistener;
        if (errorListener != null) {
            this._xsltcFactory.setErrorListener(errorListener);
        }
        URIResolver uRIResolver = this._uriresolver;
        if (uRIResolver != null) {
            this._xsltcFactory.setURIResolver(uRIResolver);
        }
        Templates newTemplates = this._xsltcFactory.newTemplates(source);
        if (newTemplates == null) {
            return null;
        }
        return newXMLFilter(newTemplates);
    }

    @Override // ohos.javax.xml.transform.sax.SAXTransformerFactory
    public XMLFilter newXMLFilter(Templates templates) throws TransformerConfigurationException {
        try {
            return new TrAXFilter(templates);
        } catch (TransformerConfigurationException e) {
            if (this._xsltcFactory == null) {
                createXSLTCTransformerFactory();
            }
            ErrorListener errorListener = this._xsltcFactory.getErrorListener();
            if (errorListener != null) {
                try {
                    errorListener.fatalError(e);
                    return null;
                } catch (TransformerException e2) {
                    new TransformerConfigurationException(e2);
                    throw e;
                }
            }
            throw e;
        }
    }
}
