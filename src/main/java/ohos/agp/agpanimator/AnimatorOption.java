package ohos.agp.agpanimator;

import android.app.ActivityOptions;
import ohos.annotation.SystemApi;

@SystemApi
public class AnimatorOption {
    private static final int NONE_ANIMATOR = 0;
    private static final int REMOTE_ANIMATOR = 13;
    private int mAnimatorType = 0;
    private TaskSyncAnimatorCore mTaskSyncAnimatorCore;

    private AnimatorOption() {
    }

    public static AnimatorOption makeTaskSyncAnimator(TaskSyncAnimatorCore taskSyncAnimatorCore) {
        AnimatorOption animatorOption = new AnimatorOption();
        animatorOption.mTaskSyncAnimatorCore = taskSyncAnimatorCore;
        animatorOption.mAnimatorType = 13;
        return animatorOption;
    }

    public ActivityOptions translation() {
        TaskSyncAnimatorCore taskSyncAnimatorCore = this.mTaskSyncAnimatorCore;
        if (taskSyncAnimatorCore != null) {
            return ActivityOptions.makeRemoteAnimation(taskSyncAnimatorCore.getAdapter());
        }
        return ActivityOptions.makeBasic();
    }
}
