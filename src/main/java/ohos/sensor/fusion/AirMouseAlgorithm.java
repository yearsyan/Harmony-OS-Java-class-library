package ohos.sensor.fusion;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.utils.Pair;

/* access modifiers changed from: package-private */
public class AirMouseAlgorithm {
    private static final int DEFAULT_GAP = 10;
    private static final float DEFAULT_STDEV = 0.2f;
    private static final float DEFAULT_VARIANCE = 0.1f;
    private static final float EPS = 1.0E-9f;
    private static final int GRAVITY_DATA_LEN = 3;
    private static final int GYRO_DATA_LEN = 3;
    private static final float HORIZONTAL_RATIO = 0.6f;
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218113824, "AirMouseAlgorithm");
    private static final float MAX_ANGLE_DIFF = 0.2617994f;
    private static final int MAX_VALID_VALUE = 10000;
    private static final float MS2NS = 1000000.0f;
    private static final float PERPENDICULAR_RATIO = 0.2f;
    private static final int QUEUE_BUFFER_TIME = 100;
    private static final float STILL_THRESHOLD = 0.5f;
    private static final int X_AXIS = 0;
    private static final int Y_AXIS = 1;
    private static final int Z_AXIS = 2;
    private AirMouseStatus currentStatus = AirMouseStatus.HORIZONTAL;
    private int gap = 10;
    private float[] gravityValues = new float[3];
    private float[] gyroValues = new float[3];
    private float lastAngle = 0.0f;
    private BlockingQueue<Pair<Float, Float>> mCoordinates = new ArrayBlockingQueue(this.queueLength);
    private float meanX = 0.0f;
    private float meanY = 0.0f;
    private int queueLength = (100 / this.gap);
    private float variance = DEFAULT_VARIANCE;

    /* access modifiers changed from: private */
    public enum AirMouseStatus {
        HORIZONTAL,
        PERPENDICULAR
    }

    AirMouseAlgorithm() {
    }

    private void updateStatus(float[] fArr) {
        float absValue = getAbsValue(fArr);
        if (Math.abs(absValue) < EPS) {
            HiLog.error(LABEL, "updateStatus gravityLength error", new Object[0]);
            return;
        }
        float f = fArr[1] / absValue;
        float sqrt = (float) Math.sqrt((double) (1.0f - (f * f)));
        if (this.currentStatus == AirMouseStatus.HORIZONTAL && sqrt < 0.2f) {
            this.currentStatus = AirMouseStatus.PERPENDICULAR;
        }
        if (this.currentStatus == AirMouseStatus.PERPENDICULAR && sqrt > 0.6f) {
            this.currentStatus = AirMouseStatus.HORIZONTAL;
        }
    }

    private float getAbsValue(float[] fArr) {
        float f = 0.0f;
        for (int i = 0; i < fArr.length; i++) {
            f += fArr[i] * fArr[i];
        }
        return (float) Math.sqrt((double) f);
    }

    private Pair<Float, Float> transformCoordinate(float[] fArr, float[] fArr2) {
        float f;
        float sqrt = (float) Math.sqrt((double) ((fArr[0] * fArr[0]) + (fArr[2] * fArr[2])));
        float f2 = 0.0f;
        if (Math.abs(sqrt) > EPS) {
            f2 = ((fArr[2] * fArr2[0]) - (fArr[0] * fArr2[2])) / sqrt;
            f = ((fArr[0] * fArr2[0]) + (fArr[2] * fArr2[2])) / sqrt;
        } else {
            f = 0.0f;
        }
        return new Pair<>(Float.valueOf(f2), Float.valueOf(f));
    }

    /* JADX WARNING: Removed duplicated region for block: B:9:0x003b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isResetMode(float r4, float r5) {
        /*
        // Method dump skipped, instructions count: 104
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.sensor.fusion.AirMouseAlgorithm.isResetMode(float, float):boolean");
    }

    private void predict(float f, float f2) {
        float f3 = this.variance;
        float f4 = f3 * f3;
        float f5 = f4 / (0.040000003f + f4);
        this.variance = (float) (((double) f3) * Math.sqrt((double) (1.0f - f5)));
        float f6 = this.meanX;
        this.meanX = f6 + ((f - f6) * f5);
        float f7 = this.meanY;
        this.meanY = f7 + (f5 * (f2 - f7));
    }

    private void reset() {
        this.variance = DEFAULT_VARIANCE;
        this.meanX = 0.0f;
        this.meanY = 0.0f;
    }

    private void updateLastAngle(float f) {
        double d = (double) f;
        if (Math.abs((((double) this.lastAngle) + 6.283185307179586d) - d) < ((double) Math.abs(this.lastAngle - f))) {
            this.lastAngle = (float) (((double) this.lastAngle) + 6.283185307179586d);
        }
        if (Math.abs((((double) this.lastAngle) - 6.283185307179586d) - d) < ((double) Math.abs(this.lastAngle - f))) {
            this.lastAngle = (float) (((double) this.lastAngle) - 6.283185307179586d);
        }
    }

    private float getSmoothAngle(float f, float f2) {
        float atan2 = (float) Math.atan2((double) f, (double) f2);
        updateLastAngle(atan2);
        float min = Math.min(1.0f, Math.abs(this.lastAngle - atan2) / MAX_ANGLE_DIFF);
        return (this.lastAngle * (1.0f - min)) + (atan2 * min);
    }

    private boolean isValidData(float[] fArr) {
        for (float f : fArr) {
            if (Math.abs(f) > 10000.0f) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void handleGravity(float[] fArr) {
        if (isValidData(fArr)) {
            this.gravityValues = (float[]) fArr.clone();
        }
    }

    /* access modifiers changed from: package-private */
    public void handleGyroscope(float[] fArr) {
        if (isValidData(fArr)) {
            this.gyroValues = (float[]) fArr.clone();
        }
    }

    /* access modifiers changed from: package-private */
    public Pair<Float, Float> getCoordinate(long j) {
        updateStatus(this.gravityValues);
        float[] fArr = this.gyroValues;
        float f = fArr[2];
        float f2 = fArr[0];
        if (this.currentStatus == AirMouseStatus.HORIZONTAL) {
            Pair<Float, Float> transformCoordinate = transformCoordinate(this.gravityValues, this.gyroValues);
            f = transformCoordinate.s.floatValue();
            f2 = transformCoordinate.f.floatValue();
        }
        if (!isResetMode(f, f2)) {
            reset();
        } else {
            predict(f, f2);
            f = this.meanX;
            f2 = this.meanY;
        }
        float smoothAngle = getSmoothAngle(f, f2);
        double sqrt = (double) ((float) Math.sqrt((double) ((f * f) + (f2 * f2))));
        double d = (double) smoothAngle;
        float sin = (float) (Math.sin(d) * sqrt);
        float cos = (float) (sqrt * Math.cos(d));
        this.lastAngle = smoothAngle;
        float f3 = (float) j;
        int i = this.gap;
        if (f3 <= ((float) i) * MS2NS) {
            f3 = ((float) i) * MS2NS;
        }
        int i2 = this.gap;
        return new Pair<>(Float.valueOf(sin * (f3 / (((float) i2) * MS2NS))), Float.valueOf(cos * (f3 / (((float) i2) * MS2NS))));
    }
}
