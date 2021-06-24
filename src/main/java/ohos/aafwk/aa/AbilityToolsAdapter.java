package ohos.aafwk.aa;

import android.app.IActivityManager;
import android.app.IApplicationThread;
import android.app.ProfilerInfo;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

public class AbilityToolsAdapter {
    private static final String ACTIVITY_SERVICE_NAME = "activity";
    private static final String CMD_START_ABILITY = "start";
    private static final String CMD_START_SERVICE = "startforegroundservice";
    private static final String CMD_STOP_SERVICE = "stopservice";
    private static final int ERR_NOT_RUNNING = 0;
    private static final int ERR_STOPPED = 1;
    private static final int ERR_STOPPING = -1;
    private static final String JAVA_DEBUG_LABEL_STR = "-D";
    private static final String NOT_ALLOW_START_STR = "?";
    private static final String PERMISSION_DENIED_STR = "!";
    private static final String SHELL_PACKAGE_NAME = "com.android.shell";
    private static final String UNABLE_START_STR = "!!";
    private IActivityManager mAm;
    private int mStartFlags = 0;
    private int mUserId = -2;

    static /* synthetic */ int access$076(AbilityToolsAdapter abilityToolsAdapter, int i) {
        int i2 = i | abilityToolsAdapter.mStartFlags;
        abilityToolsAdapter.mStartFlags = i2;
        return i2;
    }

    public static void main(String[] strArr) {
        new AbilityToolsAdapter().run(strArr);
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0059 A[Catch:{ RemoteException -> 0x0070 }] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x006c A[Catch:{ RemoteException -> 0x0070 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run(java.lang.String[] r8) {
        /*
        // Method dump skipped, instructions count: 124
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.aafwk.aa.AbilityToolsAdapter.run(java.lang.String[]):void");
    }

    private void runStartPageAbility(String[] strArr) throws RemoteException {
        Intent intent = getIntent(strArr, true);
        String type = intent.getType();
        if (type == null && intent.getData() != null && "content".equals(intent.getData().getScheme())) {
            type = this.mAm.getProviderMimeType(intent.getData(), this.mUserId);
        }
        AbilityToolUtils.printMessageToConsole("Starting: " + AbilityToolUtils.getIntentUri(intent, 0));
        intent.addFlags(268435456);
        handleStartResult(this.mAm.startActivityAsUser((IApplicationThread) null, (String) null, intent, type, (IBinder) null, (String) null, 0, this.mStartFlags, (ProfilerInfo) null, (Bundle) null, this.mUserId), intent);
    }

    private void handleStartResult(int i, Intent intent) {
        String str;
        if (i == -98) {
            str = "Error: Not allowed to start background user ability that shouldn't be displayed for all users.";
        } else if (i == -97) {
            str = "Error: Ability not started, voice control not allowed for: " + AbilityToolUtils.getIntentUri(intent, 0);
        } else if (i == 0) {
            str = "intent send success";
        } else if (i == 1) {
            str = "Warning: Ability not started because intent should be handled by the caller";
        } else if (i == 2) {
            str = "Warning: Ability not started, its current task has been brought to the front";
        } else if (i == 3) {
            str = "Warning: Ability not started, intent has been delivered to currently running top-most instance.";
        } else if (i != 100) {
            switch (i) {
                case -94:
                    str = "Error: Ability not started, you do not have permission to access it.";
                    break;
                case -93:
                    str = "Error: Ability not started, you requested to both forward and receive its result";
                    break;
                case -92:
                    str = "Error: Ability class " + AbilityToolUtils.getElementUri(intent, 0) + " does not exist.";
                    break;
                case -91:
                    str = "Error: Ability not started, unable to resolve " + AbilityToolUtils.getIntentUri(intent, 0);
                    break;
                default:
                    str = "Error: Ability not started, unknown error code: " + i;
                    break;
            }
        } else {
            str = "Warning: not started, because the  current ability is being kept for the user. ";
        }
        AbilityToolUtils.printMessageToConsole(str);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0063, code lost:
        if (r9.equals(ohos.aafwk.aa.AbilityToolsAdapter.PERMISSION_DENIED_STR) != false) goto L_0x0067;
     */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0069  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x00a2  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void runStartServiceAbility(java.lang.String[] r9) throws android.os.RemoteException {
        /*
        // Method dump skipped, instructions count: 191
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.aafwk.aa.AbilityToolsAdapter.runStartServiceAbility(java.lang.String[]):void");
    }

    private void runStopServiceAbility(String[] strArr) throws RemoteException {
        Intent intent = getIntent(strArr, false);
        AbilityToolUtils.printMessageToConsole("Stopping service ability: " + intent);
        int stopService = this.mAm.stopService((IApplicationThread) null, intent, intent.getType(), this.mUserId);
        AbilityToolUtils.printMessageToConsole(stopService != -1 ? stopService != 0 ? stopService != 1 ? "ability stop success" : "Error: ability has been stopped" : "ability not stopped: was not running." : "Error stopping ability");
    }

    private Intent getIntent(String[] strArr, boolean z) {
        this.mStartFlags = 0;
        return AbilityToolUtils.getIntentFromArgs(strArr, z ? new AbilityOptionHandler() {
            /* class ohos.aafwk.aa.AbilityToolsAdapter.AnonymousClass1 */

            @Override // ohos.aafwk.aa.AbilityOptionHandler
            public boolean handleAbilityOption(String str) {
                if (!AbilityToolsAdapter.JAVA_DEBUG_LABEL_STR.equals(str)) {
                    return false;
                }
                AbilityToolsAdapter.access$076(AbilityToolsAdapter.this, 2);
                return true;
            }
        } : null);
    }
}
