package ohos.com.sun.org.apache.xerces.internal.dom.events;

import ohos.org.w3c.dom.Node;
import ohos.org.w3c.dom.events.MutationEvent;

public class MutationEventImpl extends EventImpl implements MutationEvent {
    public static final String DOM_ATTR_MODIFIED = "DOMAttrModified";
    public static final String DOM_CHARACTER_DATA_MODIFIED = "DOMCharacterDataModified";
    public static final String DOM_NODE_INSERTED = "DOMNodeInserted";
    public static final String DOM_NODE_INSERTED_INTO_DOCUMENT = "DOMNodeInsertedIntoDocument";
    public static final String DOM_NODE_REMOVED = "DOMNodeRemoved";
    public static final String DOM_NODE_REMOVED_FROM_DOCUMENT = "DOMNodeRemovedFromDocument";
    public static final String DOM_SUBTREE_MODIFIED = "DOMSubtreeModified";
    public short attrChange;
    String attrName = null;
    String newValue = null;
    String prevValue = null;
    Node relatedNode = null;

    @Override // ohos.org.w3c.dom.events.MutationEvent
    public String getAttrName() {
        return this.attrName;
    }

    @Override // ohos.org.w3c.dom.events.MutationEvent
    public short getAttrChange() {
        return this.attrChange;
    }

    @Override // ohos.org.w3c.dom.events.MutationEvent
    public String getNewValue() {
        return this.newValue;
    }

    @Override // ohos.org.w3c.dom.events.MutationEvent
    public String getPrevValue() {
        return this.prevValue;
    }

    @Override // ohos.org.w3c.dom.events.MutationEvent
    public Node getRelatedNode() {
        return this.relatedNode;
    }

    @Override // ohos.org.w3c.dom.events.MutationEvent
    public void initMutationEvent(String str, boolean z, boolean z2, Node node, String str2, String str3, String str4, short s) {
        this.relatedNode = node;
        this.prevValue = str2;
        this.newValue = str3;
        this.attrName = str4;
        this.attrChange = s;
        super.initEvent(str, z, z2);
    }
}
