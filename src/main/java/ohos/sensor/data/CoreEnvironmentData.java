package ohos.sensor.data;

import ohos.sensor.bean.CoreEnvironment;

public class CoreEnvironmentData extends CoreSensorData<CoreEnvironment> {
    private static final int[] SENSOR_ENVIRONMENT_DIMS = {1, 3, 6, 1, 1, 1};

    public CoreEnvironmentData(CoreEnvironment coreEnvironment, long j, float[] fArr, int i, boolean z) {
        if (coreEnvironment != null) {
            this.sensor = coreEnvironment;
            this.timestamp = j;
            int parserType = parserType(coreEnvironment.getSensorId());
            int[] iArr = SENSOR_ENVIRONMENT_DIMS;
            if (parserType < iArr.length) {
                this.values = new float[iArr[parserType]];
                this.sensorDataDim = iArr[parserType];
            } else {
                this.values = new float[16];
                this.sensorDataDim = 16;
            }
            System.arraycopy(fArr, 0, this.values, 0, this.values.length);
            if (z) {
                this.accuracy = i;
            }
        }
    }

    @Override // ohos.sensor.data.CoreSensorData
    public CoreEnvironment getSensor() {
        if (this.sensor instanceof CoreEnvironment) {
            return (CoreEnvironment) this.sensor;
        }
        return new CoreEnvironment();
    }
}
