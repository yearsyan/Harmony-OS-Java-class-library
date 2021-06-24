package ohos.com.sun.org.apache.xalan.internal.lib;

import ohos.com.sun.org.apache.xalan.internal.extensions.ExpressionContext;
import ohos.com.sun.org.apache.xml.internal.dtm.ref.DTMNodeProxy;
import ohos.javax.xml.transform.SourceLocator;
import ohos.org.w3c.dom.NodeList;

public class NodeInfo {
    public static String systemId(ExpressionContext expressionContext) {
        DTMNodeProxy dTMNodeProxy = (DTMNodeProxy) expressionContext.getContextNode();
        SourceLocator sourceLocatorFor = dTMNodeProxy.getDTM().getSourceLocatorFor(dTMNodeProxy.getDTMNodeNumber());
        if (sourceLocatorFor != null) {
            return sourceLocatorFor.getSystemId();
        }
        return null;
    }

    public static String systemId(NodeList nodeList) {
        DTMNodeProxy dTMNodeProxy;
        SourceLocator sourceLocatorFor;
        if (nodeList == null || nodeList.getLength() == 0 || (sourceLocatorFor = dTMNodeProxy.getDTM().getSourceLocatorFor((dTMNodeProxy = (DTMNodeProxy) nodeList.item(0)).getDTMNodeNumber())) == null) {
            return null;
        }
        return sourceLocatorFor.getSystemId();
    }

    public static String publicId(ExpressionContext expressionContext) {
        DTMNodeProxy dTMNodeProxy = (DTMNodeProxy) expressionContext.getContextNode();
        SourceLocator sourceLocatorFor = dTMNodeProxy.getDTM().getSourceLocatorFor(dTMNodeProxy.getDTMNodeNumber());
        if (sourceLocatorFor != null) {
            return sourceLocatorFor.getPublicId();
        }
        return null;
    }

    public static String publicId(NodeList nodeList) {
        DTMNodeProxy dTMNodeProxy;
        SourceLocator sourceLocatorFor;
        if (nodeList == null || nodeList.getLength() == 0 || (sourceLocatorFor = dTMNodeProxy.getDTM().getSourceLocatorFor((dTMNodeProxy = (DTMNodeProxy) nodeList.item(0)).getDTMNodeNumber())) == null) {
            return null;
        }
        return sourceLocatorFor.getPublicId();
    }

    public static int lineNumber(ExpressionContext expressionContext) {
        DTMNodeProxy dTMNodeProxy = (DTMNodeProxy) expressionContext.getContextNode();
        SourceLocator sourceLocatorFor = dTMNodeProxy.getDTM().getSourceLocatorFor(dTMNodeProxy.getDTMNodeNumber());
        if (sourceLocatorFor != null) {
            return sourceLocatorFor.getLineNumber();
        }
        return -1;
    }

    public static int lineNumber(NodeList nodeList) {
        DTMNodeProxy dTMNodeProxy;
        SourceLocator sourceLocatorFor;
        if (nodeList == null || nodeList.getLength() == 0 || (sourceLocatorFor = dTMNodeProxy.getDTM().getSourceLocatorFor((dTMNodeProxy = (DTMNodeProxy) nodeList.item(0)).getDTMNodeNumber())) == null) {
            return -1;
        }
        return sourceLocatorFor.getLineNumber();
    }

    public static int columnNumber(ExpressionContext expressionContext) {
        DTMNodeProxy dTMNodeProxy = (DTMNodeProxy) expressionContext.getContextNode();
        SourceLocator sourceLocatorFor = dTMNodeProxy.getDTM().getSourceLocatorFor(dTMNodeProxy.getDTMNodeNumber());
        if (sourceLocatorFor != null) {
            return sourceLocatorFor.getColumnNumber();
        }
        return -1;
    }

    public static int columnNumber(NodeList nodeList) {
        DTMNodeProxy dTMNodeProxy;
        SourceLocator sourceLocatorFor;
        if (nodeList == null || nodeList.getLength() == 0 || (sourceLocatorFor = dTMNodeProxy.getDTM().getSourceLocatorFor((dTMNodeProxy = (DTMNodeProxy) nodeList.item(0)).getDTMNodeNumber())) == null) {
            return -1;
        }
        return sourceLocatorFor.getColumnNumber();
    }
}
