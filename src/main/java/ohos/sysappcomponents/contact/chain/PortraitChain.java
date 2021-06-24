package ohos.sysappcomponents.contact.chain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import ohos.app.Context;

public class PortraitChain extends Insertor {
    private static final String TAG = "PortraitChain";
    private Context context;

    public PortraitChain(Context context2) {
        this.context = context2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0064, code lost:
        if (r4 != null) goto L_0x006e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0067, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        ohos.sysappcomponents.contact.LogUtil.error(ohos.sysappcomponents.contact.chain.PortraitChain.TAG, "InputStream close error");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0076, code lost:
        if (r4 != null) goto L_0x0078;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
        r4.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x007c, code lost:
        ohos.sysappcomponents.contact.LogUtil.error(ohos.sysappcomponents.contact.chain.PortraitChain.TAG, "InputStream close error");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x007f, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x0069 */
    @Override // ohos.sysappcomponents.contact.chain.Insertor
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void fillOperation(ohos.sysappcomponents.contact.entity.Contact r7, java.util.ArrayList<ohos.aafwk.ability.DataAbilityOperation> r8, ohos.sysappcomponents.contact.chain.Insertor.OperationType r9) {
        /*
        // Method dump skipped, instructions count: 132
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.sysappcomponents.contact.chain.PortraitChain.fillOperation(ohos.sysappcomponents.contact.entity.Contact, java.util.ArrayList, ohos.sysappcomponents.contact.chain.Insertor$OperationType):void");
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
    }
}
