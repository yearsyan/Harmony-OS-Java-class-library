package ohos.agp.utils;

import java.util.function.Predicate;

public class ErrorHandler {
    private ErrorHandler() {
    }

    public static void handleInvalidParams(String str) {
        throw new IllegalArgumentException(str);
    }

    public static <T> boolean validateParam(T t, Predicate<T> predicate, String str) {
        boolean test = predicate.test(t);
        if (!test) {
            handleInvalidParams(str);
        }
        return test;
    }

    public static <T> boolean validateParamNotNull(T t) {
        return validateParamNotNull(t, "");
    }

    public static <T> boolean validateParamNotNull(T t, String str) {
        String str2;
        $$Lambda$ErrorHandler$wemGins1JBTOa6vBYK6EDLxj9Ys r0 = $$Lambda$ErrorHandler$wemGins1JBTOa6vBYK6EDLxj9Ys.INSTANCE;
        StringBuilder sb = new StringBuilder();
        sb.append("argument can't be null");
        if (str == null || str.isEmpty()) {
            str2 = "";
        } else {
            str2 = ". " + str;
        }
        sb.append(str2);
        return validateParam(t, r0, sb.toString());
    }

    static /* synthetic */ boolean lambda$validateParamNonNegative$0(Long l) {
        return l.longValue() >= 0;
    }

    public static boolean validateParamNonNegative(long j, String str) {
        String str2;
        Long valueOf = Long.valueOf(j);
        $$Lambda$ErrorHandler$V8BPMz5ldd95VAcBu0K0D7Vook r4 = $$Lambda$ErrorHandler$V8BPMz5ldd95VAcBu0K0D7Vook.INSTANCE;
        StringBuilder sb = new StringBuilder();
        sb.append("the value can't be negative");
        if (str == null || str.isEmpty()) {
            str2 = "";
        } else {
            str2 = ". " + str;
        }
        sb.append(str2);
        return validateParam(valueOf, r4, sb.toString());
    }

    public static boolean validateParamNonNegative(long j) {
        return validateParamNonNegative(j, "");
    }

    static /* synthetic */ boolean lambda$validateParamNonNegative$1(float f, Float f2) {
        return f2.floatValue() > (-f);
    }

    public static boolean validateParamNonNegative(float f) {
        return validateParam(Float.valueOf(f), new Predicate(1.0E-8f) {
            /* class ohos.agp.utils.$$Lambda$ErrorHandler$8AnnqPvgtwU5zlcPQfxiJSBkAY */
            private final /* synthetic */ float f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ErrorHandler.lambda$validateParamNonNegative$1(this.f$0, (Float) obj);
            }
        }, "the value can't be negative");
    }

    static /* synthetic */ boolean lambda$validateParamIsNaturalNumber$2(Integer num) {
        return num.intValue() > 0;
    }

    public static boolean validateParamIsNaturalNumber(int i) {
        return validateParam(Integer.valueOf(i), $$Lambda$ErrorHandler$ZtlHoLDKy5Aa444Orp6T6IotPig.INSTANCE, "the value should be > 0");
    }

    static /* synthetic */ boolean lambda$validateParamIsNaturalNumber$3(Long l) {
        return l.longValue() > 0;
    }

    public static boolean validateParamIsNaturalNumber(long j) {
        return validateParam(Long.valueOf(j), $$Lambda$ErrorHandler$093DlAqXxWAfwtBJ9fsKQBFVdQ.INSTANCE, "the value should be > 0");
    }

    static /* synthetic */ boolean lambda$validateParamPercent$4(float f, Float f2) {
        return f2.floatValue() > 0.0f - f && f2.floatValue() < f + 1.0f;
    }

    public static boolean validateParamPercent(float f) {
        return validateParam(Float.valueOf(f), new Predicate(1.0E-5f) {
            /* class ohos.agp.utils.$$Lambda$ErrorHandler$29Xm7bbIgjw77LYe3ctI5rGCeoU */
            private final /* synthetic */ float f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ErrorHandler.lambda$validateParamPercent$4(this.f$0, (Float) obj);
            }
        }, "the value should be more or equal 0.0f and less or equal 1.0f");
    }
}
