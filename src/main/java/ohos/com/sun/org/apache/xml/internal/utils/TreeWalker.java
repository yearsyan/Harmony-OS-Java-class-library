package ohos.com.sun.org.apache.xml.internal.utils;

import ohos.com.sun.org.apache.xml.internal.dtm.ref.dom2dtm.DOM2DTM;
import ohos.javax.xml.transform.Result;
import ohos.org.w3c.dom.Comment;
import ohos.org.w3c.dom.Element;
import ohos.org.w3c.dom.EntityReference;
import ohos.org.w3c.dom.NamedNodeMap;
import ohos.org.w3c.dom.Node;
import ohos.org.w3c.dom.ProcessingInstruction;
import ohos.org.w3c.dom.Text;
import ohos.org.xml.sax.ContentHandler;
import ohos.org.xml.sax.Locator;
import ohos.org.xml.sax.SAXException;
import ohos.org.xml.sax.ext.LexicalHandler;
import ohos.org.xml.sax.helpers.LocatorImpl;

public class TreeWalker {
    private ContentHandler m_contentHandler;
    private LocatorImpl m_locator;
    boolean nextIsRaw;

    public ContentHandler getContentHandler() {
        return this.m_contentHandler;
    }

    public void setContentHandler(ContentHandler contentHandler) {
        this.m_contentHandler = contentHandler;
    }

    public TreeWalker(ContentHandler contentHandler, String str) {
        this.m_contentHandler = null;
        this.m_locator = new LocatorImpl();
        this.nextIsRaw = false;
        this.m_contentHandler = contentHandler;
        ContentHandler contentHandler2 = this.m_contentHandler;
        if (contentHandler2 != null) {
            contentHandler2.setDocumentLocator(this.m_locator);
        }
        if (str != null) {
            this.m_locator.setSystemId(str);
        }
    }

    public TreeWalker(ContentHandler contentHandler) {
        this(contentHandler, null);
    }

    public void traverse(Node node) throws SAXException {
        this.m_contentHandler.startDocument();
        traverseFragment(node);
        this.m_contentHandler.endDocument();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0028, code lost:
        if (r0 == null) goto L_0x002d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002a, code lost:
        endNode(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002d, code lost:
        r0 = null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void traverseFragment(ohos.org.w3c.dom.Node r4) throws ohos.org.xml.sax.SAXException {
        /*
            r3 = this;
            r0 = r4
        L_0x0001:
            if (r0 == 0) goto L_0x0031
            r3.startNode(r0)
            ohos.org.w3c.dom.Node r1 = r0.getFirstChild()
        L_0x000a:
            if (r1 != 0) goto L_0x002f
            r3.endNode(r0)
            boolean r2 = r4.equals(r0)
            if (r2 == 0) goto L_0x0016
            goto L_0x002f
        L_0x0016:
            ohos.org.w3c.dom.Node r1 = r0.getNextSibling()
            if (r1 != 0) goto L_0x000a
            ohos.org.w3c.dom.Node r0 = r0.getParentNode()
            if (r0 == 0) goto L_0x0028
            boolean r2 = r4.equals(r0)
            if (r2 == 0) goto L_0x000a
        L_0x0028:
            if (r0 == 0) goto L_0x002d
            r3.endNode(r0)
        L_0x002d:
            r0 = 0
            goto L_0x0001
        L_0x002f:
            r0 = r1
            goto L_0x0001
        L_0x0031:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xml.internal.utils.TreeWalker.traverseFragment(ohos.org.w3c.dom.Node):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0030, code lost:
        r3 = null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void traverse(ohos.org.w3c.dom.Node r3, ohos.org.w3c.dom.Node r4) throws ohos.org.xml.sax.SAXException {
        /*
            r2 = this;
            ohos.org.xml.sax.ContentHandler r0 = r2.m_contentHandler
            r0.startDocument()
        L_0x0005:
            if (r3 == 0) goto L_0x0034
            r2.startNode(r3)
            ohos.org.w3c.dom.Node r0 = r3.getFirstChild()
        L_0x000e:
            if (r0 != 0) goto L_0x0032
            r2.endNode(r3)
            if (r4 == 0) goto L_0x001c
            boolean r1 = r4.equals(r3)
            if (r1 == 0) goto L_0x001c
            goto L_0x0032
        L_0x001c:
            ohos.org.w3c.dom.Node r0 = r3.getNextSibling()
            if (r0 != 0) goto L_0x000e
            ohos.org.w3c.dom.Node r3 = r3.getParentNode()
            if (r3 == 0) goto L_0x0030
            if (r4 == 0) goto L_0x000e
            boolean r1 = r4.equals(r3)
            if (r1 == 0) goto L_0x000e
        L_0x0030:
            r3 = 0
            goto L_0x0005
        L_0x0032:
            r3 = r0
            goto L_0x0005
        L_0x0034:
            ohos.org.xml.sax.ContentHandler r2 = r2.m_contentHandler
            r2.endDocument()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xml.internal.utils.TreeWalker.traverse(ohos.org.w3c.dom.Node, ohos.org.w3c.dom.Node):void");
    }

    private final void dispatachChars(Node node) throws SAXException {
        ContentHandler contentHandler = this.m_contentHandler;
        if (contentHandler instanceof DOM2DTM.CharacterNodeHandler) {
            ((DOM2DTM.CharacterNodeHandler) contentHandler).characters(node);
            return;
        }
        String data = ((Text) node).getData();
        this.m_contentHandler.characters(data.toCharArray(), 0, data.length());
    }

    /* access modifiers changed from: protected */
    public void startNode(Node node) throws SAXException {
        String str;
        ContentHandler contentHandler = this.m_contentHandler;
        if (contentHandler instanceof NodeConsumer) {
            ((NodeConsumer) contentHandler).setOriginatingNode(node);
        }
        if (node instanceof Locator) {
            Locator locator = (Locator) node;
            this.m_locator.setColumnNumber(locator.getColumnNumber());
            this.m_locator.setLineNumber(locator.getLineNumber());
            this.m_locator.setPublicId(locator.getPublicId());
            this.m_locator.setSystemId(locator.getSystemId());
        } else {
            this.m_locator.setColumnNumber(0);
            this.m_locator.setLineNumber(0);
        }
        short nodeType = node.getNodeType();
        if (nodeType == 1) {
            NamedNodeMap attributes = ((Element) node).getAttributes();
            int length = attributes.getLength();
            for (int i = 0; i < length; i++) {
                Node item = attributes.item(i);
                String nodeName = item.getNodeName();
                if (nodeName.equals("xmlns") || nodeName.startsWith("xmlns:")) {
                    int indexOf = nodeName.indexOf(":");
                    if (indexOf < 0) {
                        str = "";
                    } else {
                        str = nodeName.substring(indexOf + 1);
                    }
                    this.m_contentHandler.startPrefixMapping(str, item.getNodeValue());
                }
            }
            String namespaceOfNode = DOM2Helper.getNamespaceOfNode(node);
            if (namespaceOfNode == null) {
                namespaceOfNode = "";
            }
            this.m_contentHandler.startElement(namespaceOfNode, DOM2Helper.getLocalNameOfNode(node), node.getNodeName(), new AttList(attributes));
        } else if (nodeType != 11) {
            if (nodeType != 3) {
                if (nodeType == 4) {
                    ContentHandler contentHandler2 = this.m_contentHandler;
                    boolean z = contentHandler2 instanceof LexicalHandler;
                    LexicalHandler lexicalHandler = z ? (LexicalHandler) contentHandler2 : null;
                    if (z) {
                        lexicalHandler.startCDATA();
                    }
                    dispatachChars(node);
                    if (z) {
                        lexicalHandler.endCDATA();
                    }
                } else if (nodeType == 5) {
                    EntityReference entityReference = (EntityReference) node;
                    ContentHandler contentHandler3 = this.m_contentHandler;
                    if (contentHandler3 instanceof LexicalHandler) {
                        ((LexicalHandler) contentHandler3).startEntity(entityReference.getNodeName());
                    }
                } else if (nodeType == 7) {
                    ProcessingInstruction processingInstruction = (ProcessingInstruction) node;
                    if (processingInstruction.getNodeName().equals("xslt-next-is-raw")) {
                        this.nextIsRaw = true;
                    } else {
                        this.m_contentHandler.processingInstruction(processingInstruction.getNodeName(), processingInstruction.getData());
                    }
                } else if (nodeType == 8) {
                    String data = ((Comment) node).getData();
                    ContentHandler contentHandler4 = this.m_contentHandler;
                    if (contentHandler4 instanceof LexicalHandler) {
                        ((LexicalHandler) contentHandler4).comment(data.toCharArray(), 0, data.length());
                    }
                }
            } else if (this.nextIsRaw) {
                this.nextIsRaw = false;
                this.m_contentHandler.processingInstruction(Result.PI_DISABLE_OUTPUT_ESCAPING, "");
                dispatachChars(node);
                this.m_contentHandler.processingInstruction(Result.PI_ENABLE_OUTPUT_ESCAPING, "");
            } else {
                dispatachChars(node);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void endNode(Node node) throws SAXException {
        String str;
        short nodeType = node.getNodeType();
        if (nodeType == 1) {
            String namespaceOfNode = DOM2Helper.getNamespaceOfNode(node);
            if (namespaceOfNode == null) {
                namespaceOfNode = "";
            }
            this.m_contentHandler.endElement(namespaceOfNode, DOM2Helper.getLocalNameOfNode(node), node.getNodeName());
            NamedNodeMap attributes = ((Element) node).getAttributes();
            int length = attributes.getLength();
            for (int i = 0; i < length; i++) {
                String nodeName = attributes.item(i).getNodeName();
                if (nodeName.equals("xmlns") || nodeName.startsWith("xmlns:")) {
                    int indexOf = nodeName.indexOf(":");
                    if (indexOf < 0) {
                        str = "";
                    } else {
                        str = nodeName.substring(indexOf + 1);
                    }
                    this.m_contentHandler.endPrefixMapping(str);
                }
            }
        } else if (nodeType != 9 && nodeType != 4 && nodeType == 5) {
            EntityReference entityReference = (EntityReference) node;
            ContentHandler contentHandler = this.m_contentHandler;
            if (contentHandler instanceof LexicalHandler) {
                ((LexicalHandler) contentHandler).endEntity(entityReference.getNodeName());
            }
        }
    }
}
