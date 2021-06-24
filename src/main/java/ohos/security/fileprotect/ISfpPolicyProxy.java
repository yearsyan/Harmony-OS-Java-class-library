package ohos.security.fileprotect;

import java.io.IOException;
import ohos.app.Context;
import ohos.rpc.IRemoteBroker;

/* access modifiers changed from: package-private */
public interface ISfpPolicyProxy extends IRemoteBroker {
    public static final String DESCRIPTOR = "ISfpPolicyProxy";

    int getFlag(Context context, String str, String str2);

    String getLabel(Context context, String str, String str2);

    int getPolicyProtectType(Context context, String str) throws IllegalArgumentException, IllegalStateException, IllegalAccessException;

    void setEcePolicy(Context context, String str) throws IllegalArgumentException, IllegalStateException, IllegalAccessException, IOException;

    int setLabel(Context context, String str, String str2, String str3, int i);

    void setSecePolicy(Context context, String str) throws IllegalArgumentException, IllegalStateException, IllegalAccessException, IOException;
}
