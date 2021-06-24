package ohos.security.fileprotect;

import com.huawei.fileprotect.HwSfpPolicyManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import ohos.app.Context;
import ohos.rpc.IRemoteObject;

/* access modifiers changed from: package-private */
public class SfpPolicyProxy implements ISfpPolicyProxy {
    private static final String ERROR_MESSAGE = "Invalid input value!";
    private final IRemoteObject mRemote;

    SfpPolicyProxy(IRemoteObject iRemoteObject) {
        this.mRemote = iRemoteObject;
    }

    @Override // ohos.rpc.IRemoteBroker
    public IRemoteObject asObject() {
        return this.mRemote;
    }

    @Override // ohos.security.fileprotect.ISfpPolicyProxy
    public void setEcePolicy(Context context, String str) throws IllegalArgumentException, IllegalStateException, IllegalAccessException, IOException {
        android.content.Context transferZContextToAContext = transferZContextToAContext(context);
        if (transferZContextToAContext == null || str == null) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        try {
            HwSfpPolicyManager.getDefault().setEcePolicy(transferZContextToAContext, str);
        } catch (FileNotFoundException unused) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
    }

    @Override // ohos.security.fileprotect.ISfpPolicyProxy
    public void setSecePolicy(Context context, String str) throws IllegalArgumentException, IllegalStateException, IllegalAccessException, IOException {
        android.content.Context transferZContextToAContext = transferZContextToAContext(context);
        if (context == null || str == null) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        try {
            HwSfpPolicyManager.getDefault().setSecePolicy(transferZContextToAContext, str);
        } catch (FileNotFoundException unused) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
    }

    @Override // ohos.security.fileprotect.ISfpPolicyProxy
    public int getPolicyProtectType(Context context, String str) throws IllegalArgumentException, IllegalStateException, IllegalAccessException {
        android.content.Context transferZContextToAContext = transferZContextToAContext(context);
        if (context == null || str == null) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
        try {
            return HwSfpPolicyManager.getPolicyProtectType(transferZContextToAContext, str);
        } catch (FileNotFoundException unused) {
            throw new IllegalArgumentException(ERROR_MESSAGE);
        }
    }

    @Override // ohos.security.fileprotect.ISfpPolicyProxy
    public int setLabel(Context context, String str, String str2, String str3, int i) {
        return HwSfpPolicyManager.getDefault().setLabel(transferZContextToAContext(context), str, str2, str3, i);
    }

    @Override // ohos.security.fileprotect.ISfpPolicyProxy
    public String getLabel(Context context, String str, String str2) {
        return HwSfpPolicyManager.getDefault().getLabel(transferZContextToAContext(context), str, str2);
    }

    @Override // ohos.security.fileprotect.ISfpPolicyProxy
    public int getFlag(Context context, String str, String str2) {
        return HwSfpPolicyManager.getDefault().getFlag(transferZContextToAContext(context), str, str2);
    }

    private android.content.Context transferZContextToAContext(Context context) throws IllegalArgumentException {
        if (context != null) {
            Object hostProtectedStorageContext = context.getHostProtectedStorageContext();
            if (hostProtectedStorageContext instanceof android.content.Context) {
                return (android.content.Context) hostProtectedStorageContext;
            }
        }
        return null;
    }
}
