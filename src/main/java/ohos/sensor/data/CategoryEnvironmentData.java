package ohos.sensor.data;

import java.math.BigDecimal;
import ohos.com.sun.org.apache.xpath.internal.XPath;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.sensor.bean.CategoryEnvironment;

public class CategoryEnvironmentData extends SensorData<CategoryEnvironment> {
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218113824, "CategoryEnvironmentData");
    private static final int SCALE_BIGDECIMAL = 16;
    public static final float SEA_PRESSURE = 1013.25f;
    private static final int[] SENSOR_DATA_DIMS = {1, 3, 6, 1, 1, 1};
    private static final float ZERO_PRESSURE_ALTITUDE = 44330.0f;

    public CategoryEnvironmentData(CategoryEnvironment categoryEnvironment, int i, long j, int i2, float[] fArr) {
        super(categoryEnvironment, i, j, i2, fArr);
    }

    public static float getDeviceAltitude(float f, float f2) {
        if (!Float.isNaN(f) && !Float.isInfinite(f2) && !Float.isNaN(f2) && !Float.isInfinite(f2)) {
            BigDecimal valueOf = BigDecimal.valueOf((double) XPath.MATCH_SCORE_QNAME);
            BigDecimal valueOf2 = BigDecimal.valueOf((double) f);
            BigDecimal valueOf3 = BigDecimal.valueOf((double) f2);
            if (valueOf.compareTo(valueOf2) >= 0 || valueOf.compareTo(valueOf3) > 0) {
                HiLog.error(LABEL, "getDeviceAltitude input parameter is invalid.", new Object[0]);
            } else {
                return (1.0f - BigDecimal.valueOf(Math.pow((double) valueOf3.divide(valueOf2, 16, 4).floatValue(), (double) BigDecimal.valueOf(1.0d).divide(BigDecimal.valueOf(5.255d), 16, 4).floatValue())).floatValue()) * ZERO_PRESSURE_ALTITUDE;
            }
        }
        return Float.NaN;
    }

    public static float getDeviceAltitude(float f) {
        return getDeviceAltitude(1013.25f, f);
    }
}
