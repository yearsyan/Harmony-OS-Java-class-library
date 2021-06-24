package ohos.com.sun.org.apache.xerces.internal.dom;

import java.util.Vector;
import ohos.org.w3c.dom.Node;
import ohos.org.w3c.dom.NodeList;

public class DeepNodeListImpl implements NodeList {
    protected int changes;
    protected boolean enableNS;
    protected Vector nodes;
    protected String nsName;
    protected NodeImpl rootNode;
    protected String tagName;

    public DeepNodeListImpl(NodeImpl nodeImpl, String str) {
        this.changes = 0;
        this.enableNS = false;
        this.rootNode = nodeImpl;
        this.tagName = str;
        this.nodes = new Vector();
    }

    public DeepNodeListImpl(NodeImpl nodeImpl, String str, String str2) {
        this(nodeImpl, str2);
        this.nsName = (str == null || str.equals("")) ? null : str;
        this.enableNS = true;
    }

    @Override // ohos.org.w3c.dom.NodeList
    public int getLength() {
        item(Integer.MAX_VALUE);
        return this.nodes.size();
    }

    @Override // ohos.org.w3c.dom.NodeList
    public Node item(int i) {
        Node node;
        if (this.rootNode.changes() != this.changes) {
            this.nodes = new Vector();
            this.changes = this.rootNode.changes();
        }
        if (i < this.nodes.size()) {
            return (Node) this.nodes.elementAt(i);
        }
        if (this.nodes.size() == 0) {
            node = this.rootNode;
        } else {
            node = (NodeImpl) this.nodes.lastElement();
        }
        while (node != null && i >= this.nodes.size()) {
            node = nextMatchingElementAfter(node);
            if (node != null) {
                this.nodes.addElement(node);
            }
        }
        return node;
    }

    /* access modifiers changed from: protected */
    public Node nextMatchingElementAfter(Node node) {
        String str;
        String str2;
        Node nextSibling;
        while (true) {
            Node node2 = null;
            if (node == null) {
                return null;
            }
            if (node.hasChildNodes()) {
                node = node.getFirstChild();
            } else if (node == this.rootNode || (nextSibling = node.getNextSibling()) == null) {
                while (node != this.rootNode && (node2 = node.getNextSibling()) == null) {
                    node = node.getParentNode();
                }
                node = node2;
            } else {
                node = nextSibling;
            }
            if (!(node == this.rootNode || node == null || node.getNodeType() != 1)) {
                if (!this.enableNS) {
                    if (this.tagName.equals("*") || ((ElementImpl) node).getTagName().equals(this.tagName)) {
                        return node;
                    }
                } else if (this.tagName.equals("*")) {
                    String str3 = this.nsName;
                    if (str3 != null && str3.equals("*")) {
                        return node;
                    }
                    ElementImpl elementImpl = (ElementImpl) node;
                    if ((this.nsName == null && elementImpl.getNamespaceURI() == null) || ((str2 = this.nsName) != null && str2.equals(elementImpl.getNamespaceURI()))) {
                        return node;
                    }
                } else {
                    ElementImpl elementImpl2 = (ElementImpl) node;
                    if (elementImpl2.getLocalName() != null && elementImpl2.getLocalName().equals(this.tagName)) {
                        String str4 = this.nsName;
                        if (str4 != null && str4.equals("*")) {
                            return node;
                        }
                        if ((this.nsName == null && elementImpl2.getNamespaceURI() == null) || ((str = this.nsName) != null && str.equals(elementImpl2.getNamespaceURI()))) {
                            return node;
                        }
                    }
                }
            }
        }
        return node;
    }
}
