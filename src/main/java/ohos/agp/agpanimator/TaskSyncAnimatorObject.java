package ohos.agp.agpanimator;

import android.view.RemoteAnimationTarget;
import ohos.annotation.SystemApi;
import ohos.utils.geo.Point;
import ohos.utils.geo.Rect;

@SystemApi
public class TaskSyncAnimatorObject {
    private int activityType;
    private Rect clipRect;
    private Rect contentInsets;
    private boolean isInRecents;
    private boolean isTranslucent;
    private int mode;
    private Point position;
    private int prefixOrderIndex;
    private Rect sourceContainerBounds;
    private int taskId;

    static TaskSyncAnimatorObject[] create(RemoteAnimationTarget[] remoteAnimationTargetArr) {
        if (remoteAnimationTargetArr.length > 0) {
            TaskSyncAnimatorObject[] taskSyncAnimatorObjectArr = new TaskSyncAnimatorObject[remoteAnimationTargetArr.length];
            for (int i = 0; i < remoteAnimationTargetArr.length; i++) {
                taskSyncAnimatorObjectArr[i] = new TaskSyncAnimatorObject();
                taskSyncAnimatorObjectArr[i].setInternalMembers(remoteAnimationTargetArr[i]);
            }
            return taskSyncAnimatorObjectArr;
        }
        TaskSyncAnimatorObject[] taskSyncAnimatorObjectArr2 = new TaskSyncAnimatorObject[1];
        taskSyncAnimatorObjectArr2[1] = new TaskSyncAnimatorObject();
        return taskSyncAnimatorObjectArr2;
    }

    private TaskSyncAnimatorObject() {
    }

    private void setInternalMembers(RemoteAnimationTarget remoteAnimationTarget) {
        this.taskId = remoteAnimationTarget.taskId;
        this.mode = remoteAnimationTarget.mode;
        this.isTranslucent = remoteAnimationTarget.isTranslucent;
        this.clipRect = convertRect(remoteAnimationTarget.clipRect);
        this.position = convertPoint(remoteAnimationTarget.position);
        this.sourceContainerBounds = convertRect(remoteAnimationTarget.sourceContainerBounds);
        this.prefixOrderIndex = remoteAnimationTarget.prefixOrderIndex;
        this.isInRecents = remoteAnimationTarget.isNotInRecents;
        this.contentInsets = convertRect(remoteAnimationTarget.contentInsets);
        this.activityType = remoteAnimationTarget.windowConfiguration.getActivityType();
    }

    private static Point convertPoint(android.graphics.Point point) {
        return new Point((float) point.x, (float) point.y);
    }

    private static Rect convertRect(android.graphics.Rect rect) {
        return new Rect(rect.left, rect.top, rect.right, rect.bottom);
    }
}
