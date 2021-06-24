package ohos.global.icu.impl.duration;

import ohos.global.icu.impl.duration.BasicPeriodFormatterFactory;
import ohos.global.icu.impl.duration.impl.PeriodFormatterData;

class BasicPeriodFormatter implements PeriodFormatter {
    private BasicPeriodFormatterFactory.Customizations customs;
    private PeriodFormatterData data;
    private BasicPeriodFormatterFactory factory;
    private String localeName;

    BasicPeriodFormatter(BasicPeriodFormatterFactory basicPeriodFormatterFactory, String str, PeriodFormatterData periodFormatterData, BasicPeriodFormatterFactory.Customizations customizations) {
        this.factory = basicPeriodFormatterFactory;
        this.localeName = str;
        this.data = periodFormatterData;
        this.customs = customizations;
    }

    @Override // ohos.global.icu.impl.duration.PeriodFormatter
    public String format(Period period) {
        if (period.isSet()) {
            return format(period.timeLimit, period.inFuture, period.counts);
        }
        throw new IllegalArgumentException("period is not set");
    }

    @Override // ohos.global.icu.impl.duration.PeriodFormatter
    public PeriodFormatter withLocale(String str) {
        if (this.localeName.equals(str)) {
            return this;
        }
        return new BasicPeriodFormatter(this.factory, str, this.factory.getData(str), this.customs);
    }

    /* JADX DEBUG: Multi-variable search result rejected for r19v0, resolved type: boolean */
    /* JADX DEBUG: Multi-variable search result rejected for r20v0, resolved type: boolean */
    /* JADX DEBUG: Multi-variable search result rejected for r20v2, resolved type: boolean */
    /* JADX DEBUG: Multi-variable search result rejected for r20v3, resolved type: boolean */
    /* JADX DEBUG: Multi-variable search result rejected for r19v2, resolved type: boolean */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:125:0x00aa A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x009c  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x00d5  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x00d8  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x00e0  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x00e3  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x00ec  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String format(int r28, boolean r29, int[] r30) {
        /*
        // Method dump skipped, instructions count: 402
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.global.icu.impl.duration.BasicPeriodFormatter.format(int, boolean, int[]):java.lang.String");
    }
}
