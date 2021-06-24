package ohos.global.i18n.text;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import ohos.global.i18n.text.MeasureOptions;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public abstract class MeasureFormatter {
    private static final HiLogLabel LABEL = new HiLogLabel(3, 218111488, "MeasureFormatter");

    public abstract String format(MeasureOptions.Unit unit, double d, MeasureOptions.FormatStyle formatStyle);

    public abstract String format(MeasureOptions.Unit unit, double d, MeasureOptions.Unit unit2, MeasureOptions.FormatStyle formatStyle) throws NotSupportConvertException;

    public abstract String format(MeasureOptions.Unit unit, double d, MeasureOptions.Usage usage, MeasureOptions.FormatStyle formatStyle, MeasureOptions.Style style) throws NotSupportConvertException;

    public abstract void setFractionPrecision(int i);

    public static MeasureFormatter getInstance() {
        return getInstance(Locale.getDefault());
    }

    public static MeasureFormatter getInstance(Locale locale) {
        Constructor<?> constructor;
        Object newInstance;
        try {
            Class<?> cls = Class.forName("ohos.global.i18n.text.MeasureFormatterImpl");
            if (cls == null || (constructor = cls.getConstructor(Locale.class)) == null || (newInstance = constructor.newInstance(locale)) == null || !(newInstance instanceof MeasureFormatter)) {
                return null;
            }
            return (MeasureFormatter) newInstance;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException unused) {
            HiLog.debug(LABEL, "get MeasureFormatter instance failed.", new Object[0]);
            return null;
        }
    }
}
