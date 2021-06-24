package ohos.agp.agpanimator;

import ohos.annotation.SystemApi;

@SystemApi
public interface ITaskSyncAnimatorCallback {
    void abortAnimation();

    void beginAnimation(TaskSyncAnimatorObject[] taskSyncAnimatorObjectArr, Runnable runnable);
}
