package ohos.agp.agpanimator;

import android.os.RemoteException;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.IRemoteAnimationRunner;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationTarget;
import ohos.aafwk.utils.log.LogDomain;
import ohos.annotation.SystemApi;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

@SystemApi
public class TaskSyncAnimatorCore {
    private static final HiLogLabel LABEL = new HiLogLabel(3, LogDomain.END, "TaskSyncAnimatorCore");
    private RemoteAnimationAdapter mRemoteAnimationAdapter;

    public TaskSyncAnimatorCore(ITaskSyncAnimatorCallback iTaskSyncAnimatorCallback, long j, long j2, boolean z) {
        this.mRemoteAnimationAdapter = new RemoteAnimationAdapter(new TaskSyncAnimatorRunnerImpl(iTaskSyncAnimatorCallback), j, j2, z);
    }

    public TaskSyncAnimatorCore(ITaskSyncAnimatorCallback iTaskSyncAnimatorCallback, long j, long j2) {
        this(iTaskSyncAnimatorCallback, j, j2, false);
    }

    /* access modifiers changed from: package-private */
    public RemoteAnimationAdapter getAdapter() {
        return this.mRemoteAnimationAdapter;
    }

    private class TaskSyncAnimatorRunnerImpl extends IRemoteAnimationRunner.Stub {
        private ITaskSyncAnimatorCallback mCallback;

        TaskSyncAnimatorRunnerImpl(ITaskSyncAnimatorCallback iTaskSyncAnimatorCallback) {
            this.mCallback = iTaskSyncAnimatorCallback;
        }

        public void onAnimationStart(RemoteAnimationTarget[] remoteAnimationTargetArr, final IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
            if (this.mCallback == null) {
                HiLog.error(TaskSyncAnimatorCore.LABEL, "Failed to call app controlled animator finished callback", new Object[0]);
                return;
            }
            this.mCallback.beginAnimation(TaskSyncAnimatorObject.create(remoteAnimationTargetArr), new Runnable() {
                /* class ohos.agp.agpanimator.TaskSyncAnimatorCore.TaskSyncAnimatorRunnerImpl.AnonymousClass1 */

                public void run() {
                    try {
                        if (iRemoteAnimationFinishedCallback != null) {
                            iRemoteAnimationFinishedCallback.onAnimationFinished();
                        }
                    } catch (RemoteException unused) {
                        HiLog.error(TaskSyncAnimatorCore.LABEL, "Failed to call app controlled animator finished callback", new Object[0]);
                    }
                }
            });
        }

        public void onAnimationCancelled() {
            ITaskSyncAnimatorCallback iTaskSyncAnimatorCallback = this.mCallback;
            if (iTaskSyncAnimatorCallback != null) {
                iTaskSyncAnimatorCallback.abortAnimation();
            }
        }
    }
}
