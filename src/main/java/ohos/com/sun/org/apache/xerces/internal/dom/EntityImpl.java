package ohos.com.sun.org.apache.xerces.internal.dom;

import ohos.org.w3c.dom.DOMException;
import ohos.org.w3c.dom.Entity;
import ohos.org.w3c.dom.Node;

public class EntityImpl extends ParentNode implements Entity {
    static final long serialVersionUID = -3575760943444303423L;
    protected String baseURI;
    protected String encoding;
    protected String inputEncoding;
    protected String name;
    protected String notationName;
    protected String publicId;
    protected String systemId;
    protected String version;

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node
    public short getNodeType() {
        return 6;
    }

    public EntityImpl(CoreDocumentImpl coreDocumentImpl, String str) {
        super(coreDocumentImpl);
        this.name = str;
        isReadOnly(true);
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node
    public String getNodeName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.name;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node
    public void setNodeValue(String str) throws DOMException {
        if (this.ownerDocument.errorChecking && isReadOnly()) {
            throw new DOMException(7, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null));
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node
    public void setPrefix(String str) throws DOMException {
        if (this.ownerDocument.errorChecking && isReadOnly()) {
            throw new DOMException(7, DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null));
        }
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node, ohos.com.sun.org.apache.xerces.internal.dom.ParentNode, ohos.com.sun.org.apache.xerces.internal.dom.ChildNode
    public Node cloneNode(boolean z) {
        EntityImpl entityImpl = (EntityImpl) super.cloneNode(z);
        entityImpl.setReadOnly(true, z);
        return entityImpl;
    }

    @Override // ohos.org.w3c.dom.Entity
    public String getPublicId() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.publicId;
    }

    @Override // ohos.org.w3c.dom.Entity
    public String getSystemId() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.systemId;
    }

    @Override // ohos.org.w3c.dom.Entity
    public String getXmlVersion() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.version;
    }

    @Override // ohos.org.w3c.dom.Entity
    public String getXmlEncoding() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.encoding;
    }

    @Override // ohos.org.w3c.dom.Entity
    public String getNotationName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.notationName;
    }

    public void setPublicId(String str) {
        if (needsSyncData()) {
            synchronizeData();
        }
        this.publicId = str;
    }

    public void setXmlEncoding(String str) {
        if (needsSyncData()) {
            synchronizeData();
        }
        this.encoding = str;
    }

    @Override // ohos.org.w3c.dom.Entity
    public String getInputEncoding() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.inputEncoding;
    }

    public void setInputEncoding(String str) {
        if (needsSyncData()) {
            synchronizeData();
        }
        this.inputEncoding = str;
    }

    public void setXmlVersion(String str) {
        if (needsSyncData()) {
            synchronizeData();
        }
        this.version = str;
    }

    public void setSystemId(String str) {
        if (needsSyncData()) {
            synchronizeData();
        }
        this.systemId = str;
    }

    public void setNotationName(String str) {
        if (needsSyncData()) {
            synchronizeData();
        }
        this.notationName = str;
    }

    @Override // ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl, ohos.org.w3c.dom.Node
    public String getBaseURI() {
        if (needsSyncData()) {
            synchronizeData();
        }
        String str = this.baseURI;
        return str != null ? str : ((CoreDocumentImpl) getOwnerDocument()).getBaseURI();
    }

    public void setBaseURI(String str) {
        if (needsSyncData()) {
            synchronizeData();
        }
        this.baseURI = str;
    }
}
