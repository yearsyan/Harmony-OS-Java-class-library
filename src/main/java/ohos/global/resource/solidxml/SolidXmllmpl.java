package ohos.global.resource.solidxml;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import ohos.global.innerkit.asset.Package;
import ohos.global.resource.ResourceManager;
import ohos.hiviewdfx.HiLogLabel;

public class SolidXmllmpl extends SolidXml {
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218111488, "SolidXmllmpl");
    private static final float LOAD_FRACTOR = 1.0f;
    private static final int MAGIC_NAME_LENGTH = 12;
    private static final int MAX_CAPACITY = 5;
    private static String[] dictionaryFileNames = {"nodes.key", "attributes.key", "constants.key", "contents.key"};
    List<NodeImpl> nodeList;
    private String path;
    private ResourceManager resMgr;
    private Package resPackage;
    private String sxmlParentPath = "";

    public SolidXmllmpl(ResourceManager resourceManager, Package r3, String str) {
        this.path = str;
        this.resPackage = r3;
        this.resMgr = resourceManager;
        parseSxml();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0103, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0104, code lost:
        if (r1 != null) goto L_0x0106;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0106, code lost:
        $closeResource(r14, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0109, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void parseSxml() {
        /*
        // Method dump skipped, instructions count: 276
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.resource.solidxml.SolidXmllmpl.parseSxml():void");
    }

    private static /* synthetic */ void $closeResource(Throwable th, AutoCloseable autoCloseable) {
        if (th != null) {
            try {
                autoCloseable.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
        } else {
            autoCloseable.close();
        }
    }

    private List<TypedAttributeImpl> readAttributes(ByteBuffer byteBuffer, int i, String[] strArr, String[] strArr2) {
        ArrayList arrayList = new ArrayList(i);
        for (int i2 = 0; i2 < i; i2++) {
            byteBuffer.getInt();
            int i3 = byteBuffer.getInt();
            String str = "";
            String str2 = (i3 < 0 || i3 >= strArr.length) ? str : strArr[i3];
            int i4 = byteBuffer.getInt();
            if (i4 >= 0 && i4 < strArr2.length) {
                str = strArr2[i4];
            }
            arrayList.add(new TypedAttributeImpl(this.resMgr, str2, str));
        }
        return arrayList;
    }

    private void readNodes(ByteBuffer byteBuffer, int i, String[] strArr, String[] strArr2) {
        for (int i2 = 0; i2 < i; i2++) {
            NodeImpl nodeImpl = new NodeImpl(this);
            byteBuffer.getInt();
            int i3 = byteBuffer.getInt();
            if (i3 >= 0 && i3 < strArr.length) {
                nodeImpl.name = strArr[i3];
            }
            int i4 = byteBuffer.getInt();
            if (i4 >= 0 && i4 < strArr2.length) {
                nodeImpl.value = strArr2[i4];
            }
            nodeImpl.child = byteBuffer.getInt();
            nodeImpl.brother = byteBuffer.getInt();
            nodeImpl.attrIndex = byteBuffer.getInt();
            nodeImpl.attrCount = byteBuffer.getInt();
            this.nodeList.add(nodeImpl);
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x005c, code lost:
        if (r6 != null) goto L_0x005e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x005e, code lost:
        $closeResource(null, r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0090, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0091, code lost:
        if (r6 != null) goto L_0x0093;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0093, code lost:
        $closeResource(r5, r6);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0096, code lost:
        throw r7;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.HashMap<java.lang.String, java.lang.String[]> getKeyInfo() {
        /*
        // Method dump skipped, instructions count: 165
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.resource.solidxml.SolidXmllmpl.getKeyInfo():java.util.HashMap");
    }

    @Override // ohos.global.resource.solidxml.SolidXml
    public NodeImpl getRoot() {
        List<NodeImpl> list = this.nodeList;
        if (list == null || list.size() == 0) {
            return null;
        }
        return this.nodeList.get(0);
    }
}
