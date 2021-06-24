package ohos.aafwk.ability;

import java.util.List;

public interface IHostCallback {
    void onAcquire(long j, FormRecord formRecord);

    void onFormUninstalled(List<Long> list);

    void onUpdate(long j, FormRecord formRecord);
}
