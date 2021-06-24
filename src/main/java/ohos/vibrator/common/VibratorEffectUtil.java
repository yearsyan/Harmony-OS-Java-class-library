package ohos.vibrator.common;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class VibratorEffectUtil {
    private static final HiLogLabel TAG = new HiLogLabel(3, 218113808, "VibratorEffectUtil");
    private int count;
    private int duration;
    private int[] intensities;
    private int intensity;
    private int[] timing;

    public VibratorEffectUtil(int i, int i2) {
        this.intensities = new int[0];
        this.timing = new int[0];
        this.duration = i;
        this.intensity = i2;
    }

    public VibratorEffectUtil(int[] iArr, int[] iArr2, int i) {
        this.intensities = new int[0];
        this.timing = new int[0];
        if (iArr == null || iArr2 == null) {
            HiLog.error(TAG, "VibratorEffectUtil timing or intensities both cannot be null", new Object[0]);
            return;
        }
        this.timing = (int[]) iArr.clone();
        this.intensities = (int[]) iArr2.clone();
        this.count = i;
    }

    public VibratorEffectUtil(int[] iArr, int i) {
        this.intensities = new int[0];
        this.timing = new int[0];
        if (iArr == null) {
            HiLog.error(TAG, "VibratorEffectUtil timing cannot be null", new Object[0]);
            return;
        }
        this.timing = (int[]) iArr.clone();
        this.count = i;
    }

    public static final VibratorEffectUtil createSingleEffect(int i, int i2) {
        return new VibratorEffectUtil(i, i2);
    }

    public static final VibratorEffectUtil createPeriodEffect(int[] iArr, int[] iArr2, int i) {
        if (iArr != null && iArr2 != null) {
            return new VibratorEffectUtil(iArr, iArr2, i);
        }
        HiLog.error(TAG, "createPeriodEffect timing or intensities both cannot be null", new Object[0]);
        return null;
    }

    public static final VibratorEffectUtil createPeriodEffect(int[] iArr, int i) {
        if (iArr != null) {
            return new VibratorEffectUtil(iArr, i);
        }
        HiLog.error(TAG, "createPeriodEffect timing cannot be null", new Object[0]);
        return null;
    }

    public int getIntensity() {
        return this.intensity;
    }

    public void setIntensity(int i) {
        this.intensity = i;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int i) {
        this.count = i;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int i) {
        this.duration = i;
    }

    public int[] getIntensities() {
        return (int[]) this.intensities.clone();
    }

    public void setIntensities(int[] iArr) {
        if (iArr == null) {
            HiLog.error(TAG, "setIntensities intensities cannot be null", new Object[0]);
        } else {
            this.intensities = (int[]) iArr.clone();
        }
    }

    public int[] getTiming() {
        return (int[]) this.timing.clone();
    }

    public void setTiming(int[] iArr) {
        if (iArr == null) {
            HiLog.error(TAG, "setTiming timing cannot be null", new Object[0]);
        } else {
            this.timing = (int[]) iArr.clone();
        }
    }
}
