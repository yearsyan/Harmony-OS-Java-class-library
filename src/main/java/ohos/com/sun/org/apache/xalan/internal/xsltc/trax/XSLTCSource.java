package ohos.com.sun.org.apache.xalan.internal.xsltc.trax;

import ohos.com.sun.org.apache.xalan.internal.xsltc.DOM;
import ohos.com.sun.org.apache.xalan.internal.xsltc.StripFilter;
import ohos.com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import ohos.com.sun.org.apache.xalan.internal.xsltc.dom.DOMWSFilter;
import ohos.com.sun.org.apache.xalan.internal.xsltc.dom.SAXImpl;
import ohos.com.sun.org.apache.xalan.internal.xsltc.dom.XSLTCDTMManager;
import ohos.com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import ohos.javax.xml.transform.Source;
import ohos.javax.xml.transform.stream.StreamSource;
import ohos.org.xml.sax.SAXException;

public final class XSLTCSource implements Source {
    private ThreadLocal _dom = new ThreadLocal();
    private Source _source = null;
    private String _systemId = null;

    public XSLTCSource(String str) {
        this._systemId = str;
    }

    public XSLTCSource(Source source) {
        this._source = source;
    }

    @Override // ohos.javax.xml.transform.Source
    public void setSystemId(String str) {
        this._systemId = str;
        Source source = this._source;
        if (source != null) {
            source.setSystemId(str);
        }
    }

    @Override // ohos.javax.xml.transform.Source
    public String getSystemId() {
        Source source = this._source;
        if (source != null) {
            return source.getSystemId();
        }
        return this._systemId;
    }

    /* access modifiers changed from: protected */
    public DOM getDOM(XSLTCDTMManager xSLTCDTMManager, AbstractTranslet abstractTranslet) throws SAXException {
        SAXImpl sAXImpl = (SAXImpl) this._dom.get();
        if (sAXImpl == null) {
            Source source = this._source;
            if (source == null) {
                String str = this._systemId;
                if (str == null || str.length() <= 0) {
                    throw new SAXException(new ErrorMsg(ErrorMsg.XSLTC_SOURCE_ERR).toString());
                }
                source = new StreamSource(this._systemId);
            }
            DOMWSFilter dOMWSFilter = null;
            if (abstractTranslet != null && (abstractTranslet instanceof StripFilter)) {
                dOMWSFilter = new DOMWSFilter(abstractTranslet);
            }
            boolean hasIdCall = abstractTranslet != null ? abstractTranslet.hasIdCall() : false;
            if (xSLTCDTMManager == null) {
                xSLTCDTMManager = XSLTCDTMManager.newInstance();
            }
            sAXImpl = (SAXImpl) xSLTCDTMManager.getDTM(source, true, dOMWSFilter, false, false, hasIdCall);
            String systemId = getSystemId();
            if (systemId != null) {
                sAXImpl.setDocumentURI(systemId);
            }
            this._dom.set(sAXImpl);
        } else if (xSLTCDTMManager != null) {
            sAXImpl.migrateTo(xSLTCDTMManager);
        }
        return sAXImpl;
    }
}
