package ohos.hiviewdfx;

import java.util.EnumSet;
import ohos.hiviewdfx.HiChecker;

public interface IHiCheckerPlatformAdapter {
    void notifySlowProcess(String str);

    void setResourceTagEnabled(boolean z);

    void updateThreadCheckRule(EnumSet<HiChecker.Rule> enumSet);
}
