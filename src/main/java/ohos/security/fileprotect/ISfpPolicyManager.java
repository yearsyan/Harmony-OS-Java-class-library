package ohos.security.fileprotect;

import java.io.IOException;
import ohos.annotation.SystemApi;
import ohos.app.Context;

public interface ISfpPolicyManager {
    int getFlag(Context context, String str, String str2);

    String getLabel(Context context, String str, String str2);

    @SystemApi
    int getPolicyProtectType(Context context, String str) throws IllegalArgumentException, IllegalStateException, IllegalAccessException;

    @SystemApi
    void setEcePolicy(Context context, String str) throws IllegalArgumentException, IllegalStateException, IllegalAccessException, IOException;

    int setLabel(Context context, String str, String str2, String str3, int i);

    @SystemApi
    void setSecePolicy(Context context, String str) throws IllegalArgumentException, IllegalStateException, IllegalAccessException, IOException;
}
