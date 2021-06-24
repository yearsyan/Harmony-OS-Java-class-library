package ohos.agp.agpanimator;

import android.view.RemoteAnimationDefinition;
import ohos.annotation.SystemApi;

@SystemApi
public class TaskSyncAnimator {
    private RemoteAnimationDefinition mRemoteAnimationDefinition = new RemoteAnimationDefinition();

    public void addTaskSyncAnimatorCore(int i, TaskSyncAnimatorCore taskSyncAnimatorCore) {
        if (taskSyncAnimatorCore != null) {
            this.mRemoteAnimationDefinition.addRemoteAnimation(i, taskSyncAnimatorCore.getAdapter());
        }
    }

    public RemoteAnimationDefinition getTaskSyncDefinition() {
        return this.mRemoteAnimationDefinition;
    }
}
