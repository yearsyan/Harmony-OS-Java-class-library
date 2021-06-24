package com.huawei.ace.plugin.vibrator;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import com.huawei.ace.runtime.ALog;

public class VibratorPlugin extends VibratorPluginBase {
    private static final String LOG_TAG = "Ace_Vibrator";
    private final Vibrator vibrator;
    private final View view;

    public VibratorPlugin(View view2) {
        this.view = view2;
        if (view2 == null || view2.getContext() == null) {
            ALog.e(LOG_TAG, "View or context is null");
            this.vibrator = null;
        } else {
            Object systemService = view2.getContext().getSystemService("vibrator");
            if (systemService instanceof Vibrator) {
                this.vibrator = (Vibrator) systemService;
            } else {
                ALog.e(LOG_TAG, "Unable to get VIBRATOR_SERVICE");
                this.vibrator = null;
            }
        }
        nativeInit();
    }

    @Override // com.huawei.ace.plugin.vibrator.VibratorPluginBase
    public void vibrate(int i) {
        try {
            if (this.vibrator != null) {
                this.vibrator.vibrate(VibrationEffect.createOneShot((long) i, -1));
            }
        } catch (SecurityException unused) {
            ALog.w(LOG_TAG, "Has no vibrate permission");
        }
    }
}
