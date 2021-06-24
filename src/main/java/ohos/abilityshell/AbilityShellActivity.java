package ohos.abilityshell;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import ohos.abilityshell.utils.AbilityResolver;
import ohos.appexecfwk.utils.AppConstants;
import ohos.appexecfwk.utils.AppLog;
import ohos.appexecfwk.utils.HiViewUtil;
import ohos.appexecfwk.utils.JLogUtil;
import ohos.bundle.AbilityInfo;
import ohos.devtools.JLog;
import ohos.devtools.JLogConstants;
import ohos.hiviewdfx.HiLogLabel;

public class AbilityShellActivity extends Activity implements IAbilityShell {
    private static final HiLogLabel SHELL_LABEL = new HiLogLabel(3, 218108160, "AbilityShell");
    private AbilityShellActivityDelegate delegate = new AbilityShellActivityDelegate(this);

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return false;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.delegate.onCreate(bundle);
        HiViewUtil.sendAbilityStartResult(this.delegate.abilityInfo.getBundleName(), this.delegate.abilityInfo.getClassName(), 1, 0, 0);
    }

    /* access modifiers changed from: protected */
    public void onPostCreate(Bundle bundle) {
        AppLog.d(SHELL_LABEL, "AbilityShellActivity::onPostCreate called", new Object[0]);
        super.onPostCreate(bundle);
        this.delegate.onPostCreate(bundle);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        long currentTimeMillis = System.currentTimeMillis();
        this.delegate.onStart();
        long currentTimeMillis2 = System.currentTimeMillis();
        JLog.debug(JLogConstants.JLID_ABILITY_SHELL_START, this.delegate.abilityInfo.getBundleName() + "/" + this.delegate.abilityInfo.getClassName(), (int) (currentTimeMillis2 - currentTimeMillis), "");
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        long currentTimeMillis = System.currentTimeMillis();
        this.delegate.onResume();
        JLogUtil.debugLog(JLogConstants.JLID_ABILITY_SHELL_ACTIVE, this.delegate.abilityInfo.getBundleName(), this.delegate.abilityInfo.getClassName(), currentTimeMillis);
    }

    /* access modifiers changed from: protected */
    public void onPostResume() {
        AppLog.d(SHELL_LABEL, "AbilityShellActivity::onPostResume called", new Object[0]);
        super.onPostResume();
        this.delegate.onPostResume();
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        AppLog.d(SHELL_LABEL, "AbilityShellActivity::onRestart called", new Object[0]);
        super.onRestart();
        this.delegate.onRestart();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        AppLog.d(SHELL_LABEL, "AbilityShellActivity::onPause called", new Object[0]);
        super.onPause();
        long currentTimeMillis = System.currentTimeMillis();
        this.delegate.onPause();
        JLogUtil.debugLog(JLogConstants.JLID_ABILITY_SHELL_INACTIVE, this.delegate.abilityInfo.getBundleName(), this.delegate.abilityInfo.getClassName(), currentTimeMillis);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        AppLog.d(SHELL_LABEL, "AbilityShellActivity::onStop called", new Object[0]);
        long currentTimeMillis = System.currentTimeMillis();
        this.delegate.onStop();
        JLogUtil.debugLog(JLogConstants.JLID_ABILITY_SHELL_STOP, this.delegate.abilityInfo.getBundleName(), this.delegate.abilityInfo.getClassName(), currentTimeMillis);
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        AppLog.d(SHELL_LABEL, "AbilityShellActivity::onDestroy called", new Object[0]);
        long currentTimeMillis = System.currentTimeMillis();
        this.delegate.onDestroy();
        JLogUtil.debugLog(JLogConstants.JLID_ABILITY_SHELL_DESTORY, this.delegate.abilityInfo.getBundleName(), this.delegate.abilityInfo.getClassName(), currentTimeMillis);
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        AppLog.d(SHELL_LABEL, "AbilityShellActivity::onNewIntent called", new Object[0]);
        super.onNewIntent(intent);
        setIntent(intent);
        this.delegate.onNewIntent(intent);
    }

    public void onTrimMemory(int i) {
        AppLog.d(SHELL_LABEL, "AbilityShellActivity::onTrimMemory called", new Object[0]);
        super.onTrimMemory(i);
        this.delegate.onTrimMemory(i);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        AppLog.d(SHELL_LABEL, "AbilityShellActivity::onActivityResult called", new Object[0]);
        this.delegate.onActivityResult(i, i2, intent);
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (isInMultiWindowMode()) {
            if (!super.dispatchTouchEvent(motionEvent)) {
                return this.delegate.convertTouchEventThenDispatch(motionEvent);
            }
            AppLog.d(SHELL_LABEL, "AbilityShellActivity::dispatchTouchEvent on System side", new Object[0]);
            return true;
        } else if (!this.delegate.convertTouchEventThenDispatch(motionEvent)) {
            return super.dispatchTouchEvent(motionEvent);
        } else {
            AppLog.d(SHELL_LABEL, "AbilityShellActivity::dispatchTouchEvent on Z-side consumed", new Object[0]);
            return true;
        }
    }

    public boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
        if (!this.delegate.convertTouchEventThenDispatch(motionEvent)) {
            return super.dispatchGenericMotionEvent(motionEvent);
        }
        AppLog.d(SHELL_LABEL, "AbilityShellActivity::dispatchGenericMotionEvent touchEvent Z-side consumed", new Object[0]);
        return true;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        this.delegate.dispatchPopulateAccessibilityEvent(accessibilityEvent);
        return super.dispatchPopulateAccessibilityEvent(accessibilityEvent);
    }

    public boolean dispatchKeyShortcutEvent(KeyEvent keyEvent) {
        if (!this.delegate.dispatchKeyShortcutEvent(keyEvent.getKeyCode(), keyEvent)) {
            return super.dispatchKeyShortcutEvent(keyEvent);
        }
        AppLog.d(SHELL_LABEL, "AbilityShellActivity::dispatchKeyShortcutEvent keyEvent Z-side consumed", new Object[0]);
        return true;
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        boolean z;
        if (this.delegate.convertKeyEventThenDispatch(keyEvent)) {
            AppLog.d(SHELL_LABEL, "AbilityShellActivity::dispatchKeyEvent keyEvent Z-side consumed", new Object[0]);
            return true;
        }
        int action = keyEvent.getAction();
        if (action != 0) {
            z = action != 1 ? false : this.delegate.onKeyUp(keyEvent.getKeyCode(), keyEvent);
        } else {
            z = this.delegate.onKeyDown(keyEvent.getKeyCode(), keyEvent);
            if (keyEvent.isLongPress()) {
                z = this.delegate.dispatchKeyLongPress(keyEvent.getKeyCode(), keyEvent);
            }
        }
        return z || super.dispatchKeyEvent(keyEvent);
    }

    public void onBackPressed() {
        AppLog.d(SHELL_LABEL, "dispatch back key", new Object[0]);
        this.delegate.onBackPressed();
    }

    public void onSaveInstanceState(Bundle bundle) {
        this.delegate.onSaveInstanceState(bundle);
    }

    public Object onRetainNonConfigurationInstance() {
        return this.delegate.onRetainNonConfigurationInstance();
    }

    public Uri onProvideReferrer() {
        return this.delegate.onProvideReferrer();
    }

    public void onRestoreInstanceState(Bundle bundle) {
        this.delegate.onRestoreInstanceState(bundle);
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.delegate.updateConfiguration(configuration);
        int i = configuration.orientation;
        if (i == 1) {
            AppLog.d(SHELL_LABEL, "AbilityShellActivity:onConfigurationChanged ORIENTATION_PORTRAIT", new Object[0]);
            this.delegate.onConfigurationChanged(AbilityInfo.DisplayOrientation.PORTRAIT);
        } else if (i != 2) {
            AppLog.w(SHELL_LABEL, "AbilityShellActivity:onConfigurationChanged unknown", new Object[0]);
        } else {
            AppLog.d(SHELL_LABEL, "AbilityShellActivity:onConfigurationChanged ORIENTATION_LANDSCAPE", new Object[0]);
            this.delegate.onConfigurationChanged(AbilityInfo.DisplayOrientation.LANDSCAPE);
        }
    }

    public void onWindowFocusChanged(boolean z) {
        this.delegate.onWindowFocusChanged(z);
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        this.delegate.dump(str, fileDescriptor, printWriter, strArr);
    }

    @Override // ohos.abilityshell.IAbilityShell
    public void setSystemView(View view) {
        if (view != null) {
            AppLog.d(SHELL_LABEL, "AbilityShellActivity::setContentView called", new Object[0]);
            setContentView(view);
        }
    }

    @Override // ohos.abilityshell.IAbilityShell
    public Context getSystemContext() {
        AppLog.d(SHELL_LABEL, "AbilityShellActivity::getSystemContext called", new Object[0]);
        return this;
    }

    @Override // ohos.abilityshell.IAbilityShell
    public ClassLoader getSystemClassLoader() {
        AppLog.d(SHELL_LABEL, "AbilityShellActivity::getClassLoader called", new Object[0]);
        return getClassLoader();
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        AppLog.d(SHELL_LABEL, "AbilityShellActivity::onRequestPermissionsResult called", new Object[0]);
        this.delegate.onRequestPermissionsFromUserResult(i, strArr, iArr);
    }

    public CharSequence onCreateDescription() {
        return this.delegate.onCreateDescription();
    }

    public void onUserLeaveHint() {
        this.delegate.onUserLeaveHint();
    }

    public void onUserInteraction() {
        this.delegate.onUserInteraction();
    }

    public boolean continueAbility(boolean z, String str) {
        AppLog.i(SHELL_LABEL, "AbilityShellActivity::continueAbility start", new Object[0]);
        return this.delegate.handleContinueAbility(z, str);
    }

    public boolean reverseContinueAbility() {
        AppLog.i(SHELL_LABEL, "AbilityShellActivity::reverseContinueAbility start", new Object[0]);
        return this.delegate.handleReverseContinueAbility();
    }

    private void showDrivingSaftyTips() {
        AppLog.d(SHELL_LABEL, "AbilityShellActivity::showDrivingSaftyDialog called", new Object[0]);
        final AbilityResolver abilityResolver = new AbilityResolver(this);
        abilityResolver.setTitle(AppConstants.DEFAULT_PROMPT_LABEL);
        abilityResolver.setMessage(AppConstants.NOT_SUPPORT_DRIVING_MODE_TIPS);
        abilityResolver.show();
        new Handler().postDelayed(new Runnable() {
            /* class ohos.abilityshell.AbilityShellActivity.AnonymousClass1 */

            public void run() {
                abilityResolver.dismiss();
                AbilityShellActivity.this.exitApp();
            }
        }, 3000);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void exitApp() {
        finish();
        System.exit(0);
    }

    public void onTopResumedActivityChanged(boolean z) {
        this.delegate.onTopResumedActivityChanged(z);
    }

    public void onMultiWindowModeChanged(boolean z, Configuration configuration) {
        this.delegate.onWindowDisplayModeChanged(z, configuration);
    }
}
