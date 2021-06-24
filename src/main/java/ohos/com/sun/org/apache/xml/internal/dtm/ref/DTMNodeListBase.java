package ohos.com.sun.org.apache.xml.internal.dtm.ref;

import ohos.org.w3c.dom.Node;
import ohos.org.w3c.dom.NodeList;

public class DTMNodeListBase implements NodeList {
    @Override // ohos.org.w3c.dom.NodeList
    public int getLength() {
        return 0;
    }

    @Override // ohos.org.w3c.dom.NodeList
    public Node item(int i) {
        return null;
    }
}
