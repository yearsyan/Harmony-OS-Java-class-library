package ohos.sensor.fusion;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import ohos.annotation.SystemApi;
import ohos.bluetooth.ble.BleAdvertiseSettings;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.sensor.agent.CategoryMotionAgent;
import ohos.sensor.bean.CategoryMotion;
import ohos.sensor.bean.SensorBase;
import ohos.sensor.data.CategoryMotionData;
import ohos.sensor.listener.IAirMouseCallback;
import ohos.sensor.listener.ICategoryMotionDataCallback;
import ohos.utils.Pair;

@SystemApi
public class AirMouse {
    private static final long AIR_MOUSE_INTERVAL = 10000000;
    private static final int DEFAULT_SENSITIVITY = 20;
    private static final int GAP = 10;
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218113824, "AirMouse");
    private final AirMouseAlgorithm airMouseAlgorithm = new AirMouseAlgorithm();
    private long lastReportTime = 0;
    private IAirMouseCallback mCallback;
    private final CategoryMotionAgent mCategoryMotionAgent = new CategoryMotionAgent();
    private ICategoryMotionDataCallback mCategoryMotionDataCallback = new ICategoryMotionDataCallback() {
        /* class ohos.sensor.fusion.AirMouse.AnonymousClass2 */

        public void onSensorDataModified(CategoryMotionData categoryMotionData) {
            if (categoryMotionData == null) {
                HiLog.error(AirMouse.LABEL, "onSensorDataModified categoryMotionData is null", new Object[0]);
                return;
            }
            SensorBase sensor = categoryMotionData.getSensor();
            if (sensor == null) {
                HiLog.error(AirMouse.LABEL, "onSensorDataModified sensor is null", new Object[0]);
                return;
            }
            float[] fArr = categoryMotionData.values;
            if (fArr == null) {
                HiLog.error(AirMouse.LABEL, "onSensorDataModified values is null", new Object[0]);
                return;
            }
            if (sensor.getSensorId() == AirMouse.this.mGravitySensor.getSensorId()) {
                AirMouse.this.airMouseAlgorithm.handleGravity(fArr);
            }
            if (sensor.getSensorId() == AirMouse.this.mGyroSensor.getSensorId()) {
                AirMouse.this.airMouseAlgorithm.handleGyroscope(fArr);
            }
        }

        public void onAccuracyDataModified(CategoryMotion categoryMotion, int i) {
            HiLog.debug(AirMouse.LABEL, "onAccuracyDataModified", new Object[0]);
        }

        public void onCommandCompleted(CategoryMotion categoryMotion) {
            HiLog.debug(AirMouse.LABEL, "onCommandCompleted", new Object[0]);
        }
    };
    private final CategoryMotion mGravitySensor = this.mCategoryMotionAgent.getSingleSensor(3);
    private final CategoryMotion mGyroSensor = this.mCategoryMotionAgent.getSingleSensor(4);
    private int mSensitivity = 20;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
    private Runnable sendTask = new Runnable() {
        /* class ohos.sensor.fusion.AirMouse.AnonymousClass1 */

        public void run() {
            long nanoTime = System.nanoTime();
            Pair<Float, Float> coordinate = AirMouse.this.airMouseAlgorithm.getCoordinate(nanoTime - AirMouse.this.lastReportTime);
            float floatValue = coordinate.f.floatValue();
            float floatValue2 = coordinate.s.floatValue();
            float f = ((float) (-AirMouse.this.mSensitivity)) * floatValue;
            float f2 = ((float) (-AirMouse.this.mSensitivity)) * floatValue2;
            int min = Math.min((int) f, 127);
            int min2 = Math.min((int) f2, 127);
            int max = Math.max(min, (int) BleAdvertiseSettings.TX_POWER_MIN);
            int max2 = Math.max(min2, (int) BleAdvertiseSettings.TX_POWER_MIN);
            if (AirMouse.this.mCallback != null) {
                AirMouse.this.mCallback.onAirMouseData(max, max2);
            }
            AirMouse.this.lastReportTime = nanoTime;
        }
    };

    public boolean setSensorDataCallback(IAirMouseCallback iAirMouseCallback, int i) {
        if (iAirMouseCallback == null) {
            HiLog.error(LABEL, "setSensorDataCallback callback is null", new Object[0]);
            return false;
        } else if (i <= 0) {
            HiLog.error(LABEL, "setSensorDataCallback sensitivity is illegal", new Object[0]);
            return false;
        } else {
            CategoryMotion categoryMotion = this.mGravitySensor;
            if (categoryMotion == null || this.mGyroSensor == null) {
                HiLog.error(LABEL, "setSensorDataCallback no gravity or gyro", new Object[0]);
                return false;
            }
            this.mCallback = iAirMouseCallback;
            this.mSensitivity = i;
            boolean sensorDataCallback = this.mCategoryMotionAgent.setSensorDataCallback(this.mCategoryMotionDataCallback, categoryMotion, AIR_MOUSE_INTERVAL);
            boolean sensorDataCallback2 = this.mCategoryMotionAgent.setSensorDataCallback(this.mCategoryMotionDataCallback, this.mGyroSensor, AIR_MOUSE_INTERVAL);
            if (sensorDataCallback && sensorDataCallback2) {
                BlockingQueue<Runnable> queue = this.scheduledThreadPoolExecutor.getQueue();
                if (queue != null) {
                    queue.clear();
                }
                this.scheduledThreadPoolExecutor.scheduleAtFixedRate(this.sendTask, 0, 10, TimeUnit.MILLISECONDS);
            }
            if (!sensorDataCallback || !sensorDataCallback2) {
                return false;
            }
            return true;
        }
    }

    public boolean releaseSensorDataCallback() {
        if (this.mCallback == null) {
            HiLog.warn(LABEL, "releaseSensorDataCallback callback is null", new Object[0]);
        }
        this.mCallback = null;
        BlockingQueue<Runnable> queue = this.scheduledThreadPoolExecutor.getQueue();
        if (queue != null) {
            queue.clear();
        }
        return this.mCategoryMotionAgent.releaseSensorDataCallback(this.mCategoryMotionDataCallback);
    }
}
