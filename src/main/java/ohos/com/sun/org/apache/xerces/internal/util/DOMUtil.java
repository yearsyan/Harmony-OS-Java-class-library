package ohos.com.sun.org.apache.xerces.internal.util;

import java.lang.reflect.Method;
import java.util.Map;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.opti.ElementImpl;
import ohos.com.sun.org.apache.xerces.internal.impl.xs.opti.NodeImpl;
import ohos.org.w3c.dom.Attr;
import ohos.org.w3c.dom.DOMException;
import ohos.org.w3c.dom.Document;
import ohos.org.w3c.dom.Element;
import ohos.org.w3c.dom.NamedNodeMap;
import ohos.org.w3c.dom.Node;
import ohos.org.w3c.dom.ls.LSException;

public class DOMUtil {
    protected DOMUtil() {
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:37:0x0009 */
    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:43:0x00b5 */
    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:41:0x0009 */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v1, types: [ohos.org.w3c.dom.Node] */
    /* JADX WARN: Type inference failed for: r4v3, types: [ohos.org.w3c.dom.Element] */
    /* JADX WARN: Type inference failed for: r4v5, types: [ohos.org.w3c.dom.Text] */
    /* JADX WARN: Type inference failed for: r4v7, types: [ohos.org.w3c.dom.CDATASection] */
    /* JADX WARN: Type inference failed for: r4v9, types: [ohos.org.w3c.dom.EntityReference] */
    /* JADX WARN: Type inference failed for: r4v11, types: [ohos.org.w3c.dom.ProcessingInstruction] */
    /* JADX WARN: Type inference failed for: r4v13, types: [ohos.org.w3c.dom.Comment] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void copyInto(ohos.org.w3c.dom.Node r12, ohos.org.w3c.dom.Node r13) throws ohos.org.w3c.dom.DOMException {
        /*
        // Method dump skipped, instructions count: 221
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xerces.internal.util.DOMUtil.copyInto(ohos.org.w3c.dom.Node, ohos.org.w3c.dom.Node):void");
    }

    public static Element getFirstChildElement(Node node) {
        for (Node firstChild = node.getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
            if (firstChild.getNodeType() == 1) {
                return (Element) firstChild;
            }
        }
        return null;
    }

    public static Element getFirstVisibleChildElement(Node node) {
        for (Node firstChild = node.getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
            if (firstChild.getNodeType() == 1 && !isHidden(firstChild)) {
                return (Element) firstChild;
            }
        }
        return null;
    }

    public static Element getFirstVisibleChildElement(Node node, Map<Node, String> map) {
        for (Node firstChild = node.getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
            if (firstChild.getNodeType() == 1 && !isHidden(firstChild, map)) {
                return (Element) firstChild;
            }
        }
        return null;
    }

    public static Element getLastChildElement(Node node) {
        for (Node lastChild = node.getLastChild(); lastChild != null; lastChild = lastChild.getPreviousSibling()) {
            if (lastChild.getNodeType() == 1) {
                return (Element) lastChild;
            }
        }
        return null;
    }

    public static Element getLastVisibleChildElement(Node node) {
        for (Node lastChild = node.getLastChild(); lastChild != null; lastChild = lastChild.getPreviousSibling()) {
            if (lastChild.getNodeType() == 1 && !isHidden(lastChild)) {
                return (Element) lastChild;
            }
        }
        return null;
    }

    public static Element getLastVisibleChildElement(Node node, Map<Node, String> map) {
        for (Node lastChild = node.getLastChild(); lastChild != null; lastChild = lastChild.getPreviousSibling()) {
            if (lastChild.getNodeType() == 1 && !isHidden(lastChild, map)) {
                return (Element) lastChild;
            }
        }
        return null;
    }

    public static Element getNextSiblingElement(Node node) {
        for (Node nextSibling = node.getNextSibling(); nextSibling != null; nextSibling = nextSibling.getNextSibling()) {
            if (nextSibling.getNodeType() == 1) {
                return (Element) nextSibling;
            }
        }
        return null;
    }

    public static Element getNextVisibleSiblingElement(Node node) {
        for (Node nextSibling = node.getNextSibling(); nextSibling != null; nextSibling = nextSibling.getNextSibling()) {
            if (nextSibling.getNodeType() == 1 && !isHidden(nextSibling)) {
                return (Element) nextSibling;
            }
        }
        return null;
    }

    public static Element getNextVisibleSiblingElement(Node node, Map<Node, String> map) {
        for (Node nextSibling = node.getNextSibling(); nextSibling != null; nextSibling = nextSibling.getNextSibling()) {
            if (nextSibling.getNodeType() == 1 && !isHidden(nextSibling, map)) {
                return (Element) nextSibling;
            }
        }
        return null;
    }

    public static void setHidden(Node node) {
        if (node instanceof NodeImpl) {
            ((NodeImpl) node).setReadOnly(true, false);
        } else if (node instanceof ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl) {
            ((ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl) node).setReadOnly(true, false);
        }
    }

    public static void setHidden(Node node, Map<Node, String> map) {
        if (node instanceof NodeImpl) {
            ((NodeImpl) node).setReadOnly(true, false);
        } else {
            map.put(node, "");
        }
    }

    public static void setVisible(Node node) {
        if (node instanceof NodeImpl) {
            ((NodeImpl) node).setReadOnly(false, false);
        } else if (node instanceof ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl) {
            ((ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl) node).setReadOnly(false, false);
        }
    }

    public static void setVisible(Node node, Map<Node, String> map) {
        if (node instanceof NodeImpl) {
            ((NodeImpl) node).setReadOnly(false, false);
        } else {
            map.remove(node);
        }
    }

    public static boolean isHidden(Node node) {
        if (node instanceof NodeImpl) {
            return ((NodeImpl) node).getReadOnly();
        }
        if (node instanceof ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl) {
            return ((ohos.com.sun.org.apache.xerces.internal.dom.NodeImpl) node).getReadOnly();
        }
        return false;
    }

    public static boolean isHidden(Node node, Map<Node, String> map) {
        if (node instanceof NodeImpl) {
            return ((NodeImpl) node).getReadOnly();
        }
        return map.containsKey(node);
    }

    public static Element getFirstChildElement(Node node, String str) {
        for (Node firstChild = node.getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
            if (firstChild.getNodeType() == 1 && firstChild.getNodeName().equals(str)) {
                return (Element) firstChild;
            }
        }
        return null;
    }

    public static Element getLastChildElement(Node node, String str) {
        for (Node lastChild = node.getLastChild(); lastChild != null; lastChild = lastChild.getPreviousSibling()) {
            if (lastChild.getNodeType() == 1 && lastChild.getNodeName().equals(str)) {
                return (Element) lastChild;
            }
        }
        return null;
    }

    public static Element getNextSiblingElement(Node node, String str) {
        for (Node nextSibling = node.getNextSibling(); nextSibling != null; nextSibling = nextSibling.getNextSibling()) {
            if (nextSibling.getNodeType() == 1 && nextSibling.getNodeName().equals(str)) {
                return (Element) nextSibling;
            }
        }
        return null;
    }

    public static Element getFirstChildElementNS(Node node, String str, String str2) {
        String namespaceURI;
        for (Node firstChild = node.getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
            if (firstChild.getNodeType() == 1 && (namespaceURI = firstChild.getNamespaceURI()) != null && namespaceURI.equals(str) && firstChild.getLocalName().equals(str2)) {
                return (Element) firstChild;
            }
        }
        return null;
    }

    public static Element getLastChildElementNS(Node node, String str, String str2) {
        String namespaceURI;
        for (Node lastChild = node.getLastChild(); lastChild != null; lastChild = lastChild.getPreviousSibling()) {
            if (lastChild.getNodeType() == 1 && (namespaceURI = lastChild.getNamespaceURI()) != null && namespaceURI.equals(str) && lastChild.getLocalName().equals(str2)) {
                return (Element) lastChild;
            }
        }
        return null;
    }

    public static Element getNextSiblingElementNS(Node node, String str, String str2) {
        String namespaceURI;
        for (Node nextSibling = node.getNextSibling(); nextSibling != null; nextSibling = nextSibling.getNextSibling()) {
            if (nextSibling.getNodeType() == 1 && (namespaceURI = nextSibling.getNamespaceURI()) != null && namespaceURI.equals(str) && nextSibling.getLocalName().equals(str2)) {
                return (Element) nextSibling;
            }
        }
        return null;
    }

    public static Element getFirstChildElement(Node node, String[] strArr) {
        for (Node firstChild = node.getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
            if (firstChild.getNodeType() == 1) {
                for (String str : strArr) {
                    if (firstChild.getNodeName().equals(str)) {
                        return (Element) firstChild;
                    }
                }
                continue;
            }
        }
        return null;
    }

    public static Element getLastChildElement(Node node, String[] strArr) {
        for (Node lastChild = node.getLastChild(); lastChild != null; lastChild = lastChild.getPreviousSibling()) {
            if (lastChild.getNodeType() == 1) {
                for (String str : strArr) {
                    if (lastChild.getNodeName().equals(str)) {
                        return (Element) lastChild;
                    }
                }
                continue;
            }
        }
        return null;
    }

    public static Element getNextSiblingElement(Node node, String[] strArr) {
        for (Node nextSibling = node.getNextSibling(); nextSibling != null; nextSibling = nextSibling.getNextSibling()) {
            if (nextSibling.getNodeType() == 1) {
                for (String str : strArr) {
                    if (nextSibling.getNodeName().equals(str)) {
                        return (Element) nextSibling;
                    }
                }
                continue;
            }
        }
        return null;
    }

    public static Element getFirstChildElementNS(Node node, String[][] strArr) {
        for (Node firstChild = node.getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
            if (firstChild.getNodeType() == 1) {
                for (int i = 0; i < strArr.length; i++) {
                    String namespaceURI = firstChild.getNamespaceURI();
                    if (namespaceURI != null && namespaceURI.equals(strArr[i][0]) && firstChild.getLocalName().equals(strArr[i][1])) {
                        return (Element) firstChild;
                    }
                }
                continue;
            }
        }
        return null;
    }

    public static Element getLastChildElementNS(Node node, String[][] strArr) {
        for (Node lastChild = node.getLastChild(); lastChild != null; lastChild = lastChild.getPreviousSibling()) {
            if (lastChild.getNodeType() == 1) {
                for (int i = 0; i < strArr.length; i++) {
                    String namespaceURI = lastChild.getNamespaceURI();
                    if (namespaceURI != null && namespaceURI.equals(strArr[i][0]) && lastChild.getLocalName().equals(strArr[i][1])) {
                        return (Element) lastChild;
                    }
                }
                continue;
            }
        }
        return null;
    }

    public static Element getNextSiblingElementNS(Node node, String[][] strArr) {
        for (Node nextSibling = node.getNextSibling(); nextSibling != null; nextSibling = nextSibling.getNextSibling()) {
            if (nextSibling.getNodeType() == 1) {
                for (int i = 0; i < strArr.length; i++) {
                    String namespaceURI = nextSibling.getNamespaceURI();
                    if (namespaceURI != null && namespaceURI.equals(strArr[i][0]) && nextSibling.getLocalName().equals(strArr[i][1])) {
                        return (Element) nextSibling;
                    }
                }
                continue;
            }
        }
        return null;
    }

    public static Element getFirstChildElement(Node node, String str, String str2, String str3) {
        for (Node firstChild = node.getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
            if (firstChild.getNodeType() == 1) {
                Element element = (Element) firstChild;
                if (element.getNodeName().equals(str) && element.getAttribute(str2).equals(str3)) {
                    return element;
                }
            }
        }
        return null;
    }

    public static Element getLastChildElement(Node node, String str, String str2, String str3) {
        for (Node lastChild = node.getLastChild(); lastChild != null; lastChild = lastChild.getPreviousSibling()) {
            if (lastChild.getNodeType() == 1) {
                Element element = (Element) lastChild;
                if (element.getNodeName().equals(str) && element.getAttribute(str2).equals(str3)) {
                    return element;
                }
            }
        }
        return null;
    }

    public static Element getNextSiblingElement(Node node, String str, String str2, String str3) {
        for (Node nextSibling = node.getNextSibling(); nextSibling != null; nextSibling = nextSibling.getNextSibling()) {
            if (nextSibling.getNodeType() == 1) {
                Element element = (Element) nextSibling;
                if (element.getNodeName().equals(str) && element.getAttribute(str2).equals(str3)) {
                    return element;
                }
            }
        }
        return null;
    }

    public static String getChildText(Node node) {
        if (node == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (Node firstChild = node.getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
            short nodeType = firstChild.getNodeType();
            if (nodeType == 3) {
                stringBuffer.append(firstChild.getNodeValue());
            } else if (nodeType == 4) {
                stringBuffer.append(getChildText(firstChild));
            }
        }
        return stringBuffer.toString();
    }

    public static String getName(Node node) {
        return node.getNodeName();
    }

    public static String getLocalName(Node node) {
        String localName = node.getLocalName();
        return localName != null ? localName : node.getNodeName();
    }

    public static Element getParent(Element element) {
        Node parentNode = element.getParentNode();
        if (parentNode instanceof Element) {
            return (Element) parentNode;
        }
        return null;
    }

    public static Document getDocument(Node node) {
        return node.getOwnerDocument();
    }

    public static Element getRoot(Document document) {
        return document.getDocumentElement();
    }

    public static Attr getAttr(Element element, String str) {
        return element.getAttributeNode(str);
    }

    public static Attr getAttrNS(Element element, String str, String str2) {
        return element.getAttributeNodeNS(str, str2);
    }

    public static Attr[] getAttrs(Element element) {
        NamedNodeMap attributes = element.getAttributes();
        Attr[] attrArr = new Attr[attributes.getLength()];
        for (int i = 0; i < attributes.getLength(); i++) {
            attrArr[i] = (Attr) attributes.item(i);
        }
        return attrArr;
    }

    public static String getValue(Attr attr) {
        return attr.getValue();
    }

    public static String getAttrValue(Element element, String str) {
        return element.getAttribute(str);
    }

    public static String getAttrValueNS(Element element, String str, String str2) {
        return element.getAttributeNS(str, str2);
    }

    public static String getPrefix(Node node) {
        return node.getPrefix();
    }

    public static String getNamespaceURI(Node node) {
        return node.getNamespaceURI();
    }

    public static String getAnnotation(Node node) {
        if (node instanceof ElementImpl) {
            return ((ElementImpl) node).getAnnotation();
        }
        return null;
    }

    public static String getSyntheticAnnotation(Node node) {
        if (node instanceof ElementImpl) {
            return ((ElementImpl) node).getSyntheticAnnotation();
        }
        return null;
    }

    public static DOMException createDOMException(short s, Throwable th) {
        DOMException dOMException = new DOMException(s, th != null ? th.getMessage() : null);
        if (th != null && ThrowableMethods.fgThrowableMethodsAvailable) {
            try {
                ThrowableMethods.fgThrowableInitCauseMethod.invoke(dOMException, th);
            } catch (Exception unused) {
            }
        }
        return dOMException;
    }

    public static LSException createLSException(short s, Throwable th) {
        LSException lSException = new LSException(s, th != null ? th.getMessage() : null);
        if (th != null && ThrowableMethods.fgThrowableMethodsAvailable) {
            try {
                ThrowableMethods.fgThrowableInitCauseMethod.invoke(lSException, th);
            } catch (Exception unused) {
            }
        }
        return lSException;
    }

    /* access modifiers changed from: package-private */
    public static class ThrowableMethods {
        private static Method fgThrowableInitCauseMethod = null;
        private static boolean fgThrowableMethodsAvailable = false;

        private ThrowableMethods() {
        }

        static {
            try {
                fgThrowableInitCauseMethod = Throwable.class.getMethod("initCause", Throwable.class);
                fgThrowableMethodsAvailable = true;
            } catch (Exception unused) {
                fgThrowableInitCauseMethod = null;
                fgThrowableMethodsAvailable = false;
            }
        }
    }
}
