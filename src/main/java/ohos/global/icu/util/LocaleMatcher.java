package ohos.global.icu.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import ohos.global.icu.impl.locale.LSR;
import ohos.global.icu.impl.locale.LocaleDistance;
import ohos.global.icu.impl.locale.XLikelySubtags;
import ohos.global.icu.util.ULocale;

public final class LocaleMatcher {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final Locale EMPTY_LOCALE = new Locale("");
    private static final boolean TRACE_MATCHER = false;
    private static final Locale UND_LOCALE = new Locale("und");
    private static final LSR UND_LSR = new LSR("und", "", "", 7);
    private static final ULocale UND_ULOCALE = new ULocale("und");
    private final Locale defaultLocale;
    private final ULocale defaultULocale;
    private final int demotionPerDesiredLocale;
    private final Direction direction;
    private final FavorSubtag favorSubtag;
    private final int[] supportedIndexes;
    private final LSR[] supportedLSRs;
    private final int supportedLSRsLength;
    private final Locale[] supportedLocales;
    private final Map<LSR, Integer> supportedLsrToIndex;
    private final ULocale[] supportedULocales;
    private final int thresholdDistance;

    public enum Demotion {
        NONE,
        REGION
    }

    public enum Direction {
        WITH_ONE_WAY,
        ONLY_TWO_WAY
    }

    public enum FavorSubtag {
        LANGUAGE,
        SCRIPT
    }

    /* access modifiers changed from: private */
    public static abstract class LsrIterator implements Iterator<LSR> {
        int bestDesiredIndex;

        public abstract void rememberCurrent(int i);

        private LsrIterator() {
            this.bestDesiredIndex = -1;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static final class Result {
        private final int desiredIndex;
        private final Locale desiredLocale;
        private final ULocale desiredULocale;
        private final int supportedIndex;
        private final Locale supportedLocale;
        private final ULocale supportedULocale;

        private Result(ULocale uLocale, ULocale uLocale2, Locale locale, Locale locale2, int i, int i2) {
            this.desiredULocale = uLocale;
            this.supportedULocale = uLocale2;
            this.desiredLocale = locale;
            this.supportedLocale = locale2;
            this.desiredIndex = i;
            this.supportedIndex = i2;
        }

        public ULocale getDesiredULocale() {
            Locale locale;
            return (this.desiredULocale != null || (locale = this.desiredLocale) == null) ? this.desiredULocale : ULocale.forLocale(locale);
        }

        public Locale getDesiredLocale() {
            ULocale uLocale;
            return (this.desiredLocale != null || (uLocale = this.desiredULocale) == null) ? this.desiredLocale : uLocale.toLocale();
        }

        public ULocale getSupportedULocale() {
            return this.supportedULocale;
        }

        public Locale getSupportedLocale() {
            return this.supportedLocale;
        }

        public int getDesiredIndex() {
            return this.desiredIndex;
        }

        public int getSupportedIndex() {
            return this.supportedIndex;
        }

        public ULocale makeResolvedULocale() {
            ULocale desiredULocale2 = getDesiredULocale();
            ULocale uLocale = this.supportedULocale;
            if (uLocale == null || desiredULocale2 == null || uLocale.equals(desiredULocale2)) {
                return this.supportedULocale;
            }
            ULocale.Builder locale = new ULocale.Builder().setLocale(this.supportedULocale);
            String country = desiredULocale2.getCountry();
            if (!country.isEmpty()) {
                locale.setRegion(country);
            }
            String variant = desiredULocale2.getVariant();
            if (!variant.isEmpty()) {
                locale.setVariant(variant);
            }
            for (Character ch : desiredULocale2.getExtensionKeys()) {
                char charValue = ch.charValue();
                locale.setExtension(charValue, desiredULocale2.getExtension(charValue));
            }
            return locale.build();
        }

        public Locale makeResolvedLocale() {
            ULocale makeResolvedULocale = makeResolvedULocale();
            if (makeResolvedULocale != null) {
                return makeResolvedULocale.toLocale();
            }
            return null;
        }
    }

    public static final class Builder {
        private ULocale defaultLocale;
        private Demotion demotion;
        private Direction direction;
        private FavorSubtag favor;
        private List<ULocale> supportedLocales;
        private int thresholdDistance;

        private Builder() {
            this.thresholdDistance = -1;
        }

        public Builder setSupportedLocales(String str) {
            return setSupportedULocales(LocalePriorityList.add(str).build().getULocales());
        }

        public Builder setSupportedULocales(Collection<ULocale> collection) {
            this.supportedLocales = new ArrayList(collection);
            return this;
        }

        public Builder setSupportedLocales(Collection<Locale> collection) {
            this.supportedLocales = new ArrayList(collection.size());
            for (Locale locale : collection) {
                this.supportedLocales.add(ULocale.forLocale(locale));
            }
            return this;
        }

        public Builder addSupportedULocale(ULocale uLocale) {
            if (this.supportedLocales == null) {
                this.supportedLocales = new ArrayList();
            }
            this.supportedLocales.add(uLocale);
            return this;
        }

        public Builder addSupportedLocale(Locale locale) {
            return addSupportedULocale(ULocale.forLocale(locale));
        }

        public Builder setDefaultULocale(ULocale uLocale) {
            this.defaultLocale = uLocale;
            return this;
        }

        public Builder setDefaultLocale(Locale locale) {
            this.defaultLocale = ULocale.forLocale(locale);
            return this;
        }

        public Builder setFavorSubtag(FavorSubtag favorSubtag) {
            this.favor = favorSubtag;
            return this;
        }

        public Builder setDemotionPerDesiredLocale(Demotion demotion2) {
            this.demotion = demotion2;
            return this;
        }

        public Builder setDirection(Direction direction2) {
            this.direction = direction2;
            return this;
        }

        @Deprecated
        public Builder internalSetThresholdDistance(int i) {
            if (i > 100) {
                i = 100;
            }
            this.thresholdDistance = i;
            return this;
        }

        public LocaleMatcher build() {
            return new LocaleMatcher(this);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("{LocaleMatcher.Builder");
            List<ULocale> list = this.supportedLocales;
            if (list != null && !list.isEmpty()) {
                sb.append(" supported={");
                sb.append(this.supportedLocales);
                sb.append('}');
            }
            if (this.defaultLocale != null) {
                sb.append(" default=");
                sb.append(this.defaultLocale);
            }
            if (this.favor != null) {
                sb.append(" distance=");
                sb.append(this.favor);
            }
            int i = this.thresholdDistance;
            if (i >= 0) {
                sb.append(String.format(" threshold=%d", Integer.valueOf(i)));
            }
            if (this.demotion != null) {
                sb.append(" demotion=");
                sb.append(this.demotion);
            }
            sb.append('}');
            return sb.toString();
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public LocaleMatcher(LocalePriorityList localePriorityList) {
        this(builder().setSupportedULocales(localePriorityList.getULocales()));
    }

    public LocaleMatcher(String str) {
        this(builder().setSupportedLocales(str));
    }

    private LocaleMatcher(Builder builder) {
        Locale locale;
        int i;
        this.thresholdDistance = builder.thresholdDistance < 0 ? LocaleDistance.INSTANCE.getDefaultScriptDistance() : builder.thresholdDistance;
        ULocale uLocale = builder.defaultLocale;
        LSR lsr = null;
        if (uLocale != null) {
            locale = uLocale.toLocale();
            lsr = getMaximalLsrOrUnd(uLocale);
        } else {
            locale = null;
        }
        int i2 = 0;
        int size = builder.supportedLocales != null ? builder.supportedLocales.size() : 0;
        this.supportedULocales = new ULocale[size];
        this.supportedLocales = new Locale[size];
        LSR[] lsrArr = new LSR[size];
        if (size > 0) {
            int i3 = 0;
            for (ULocale uLocale2 : builder.supportedLocales) {
                this.supportedULocales[i3] = uLocale2;
                this.supportedLocales[i3] = uLocale2.toLocale();
                lsrArr[i3] = getMaximalLsrOrUnd(uLocale2);
                i3++;
            }
        }
        this.supportedLsrToIndex = new HashMap(size);
        this.supportedLSRs = new LSR[size];
        this.supportedIndexes = new int[size];
        byte[] bArr = new byte[size];
        ULocale[] uLocaleArr = this.supportedULocales;
        int length = uLocaleArr.length;
        ULocale uLocale3 = uLocale;
        LSR lsr2 = lsr;
        Locale locale2 = locale;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        while (i4 < length) {
            ULocale uLocale4 = uLocaleArr[i4];
            LSR lsr3 = lsrArr[i7];
            if (lsr2 == null) {
                Locale locale3 = this.supportedLocales[i2];
                i5 = putIfAbsent(lsr3, i2, i5);
                locale2 = locale3;
                lsr2 = lsr3;
                uLocale3 = uLocale4;
            } else if (lsr3.isEquivalentTo(lsr2)) {
                i5 = putIfAbsent(lsr3, i7, i5);
            } else if (LocaleDistance.INSTANCE.isParadigmLSR(lsr3)) {
                bArr[i7] = 2;
                i6++;
            } else {
                bArr[i7] = 3;
            }
            i7++;
            i4++;
            i2 = 0;
        }
        int i8 = i6 + i5;
        for (int i9 = 0; i9 < size && i5 < i8; i9++) {
            if (bArr[i9] == 2) {
                i5 = putIfAbsent(lsrArr[i9], i9, i5);
            }
        }
        for (int i10 = 0; i10 < size; i10++) {
            if (bArr[i10] == 3) {
                i5 = putIfAbsent(lsrArr[i10], i10, i5);
            }
        }
        this.supportedLSRsLength = i5;
        this.defaultULocale = uLocale3;
        this.defaultLocale = locale2;
        if (builder.demotion == Demotion.NONE) {
            i = 0;
        } else {
            i = LocaleDistance.INSTANCE.getDefaultDemotionPerDesiredLocale();
        }
        this.demotionPerDesiredLocale = i;
        this.favorSubtag = builder.favor;
        this.direction = builder.direction;
    }

    private final int putIfAbsent(LSR lsr, int i, int i2) {
        if (this.supportedLsrToIndex.containsKey(lsr)) {
            return i2;
        }
        this.supportedLsrToIndex.put(lsr, Integer.valueOf(i));
        this.supportedLSRs[i2] = lsr;
        int i3 = i2 + 1;
        this.supportedIndexes[i2] = i;
        return i3;
    }

    /* access modifiers changed from: private */
    public static final LSR getMaximalLsrOrUnd(ULocale uLocale) {
        if (uLocale.equals(UND_ULOCALE)) {
            return UND_LSR;
        }
        return XLikelySubtags.INSTANCE.makeMaximizedLsrFrom(uLocale);
    }

    /* access modifiers changed from: private */
    public static final LSR getMaximalLsrOrUnd(Locale locale) {
        if (locale.equals(UND_LOCALE) || locale.equals(EMPTY_LOCALE)) {
            return UND_LSR;
        }
        return XLikelySubtags.INSTANCE.makeMaximizedLsrFrom(locale);
    }

    /* access modifiers changed from: private */
    public static final class ULocaleLsrIterator extends LsrIterator {
        private ULocale current;
        private Iterator<ULocale> locales;
        private ULocale remembered;

        ULocaleLsrIterator(Iterator<ULocale> it) {
            super();
            this.locales = it;
        }

        public boolean hasNext() {
            return this.locales.hasNext();
        }

        @Override // java.util.Iterator
        public LSR next() {
            this.current = this.locales.next();
            return LocaleMatcher.getMaximalLsrOrUnd(this.current);
        }

        @Override // ohos.global.icu.util.LocaleMatcher.LsrIterator
        public void rememberCurrent(int i) {
            this.bestDesiredIndex = i;
            this.remembered = this.current;
        }
    }

    /* access modifiers changed from: private */
    public static final class LocaleLsrIterator extends LsrIterator {
        private Locale current;
        private Iterator<Locale> locales;
        private Locale remembered;

        LocaleLsrIterator(Iterator<Locale> it) {
            super();
            this.locales = it;
        }

        public boolean hasNext() {
            return this.locales.hasNext();
        }

        @Override // java.util.Iterator
        public LSR next() {
            this.current = this.locales.next();
            return LocaleMatcher.getMaximalLsrOrUnd(this.current);
        }

        @Override // ohos.global.icu.util.LocaleMatcher.LsrIterator
        public void rememberCurrent(int i) {
            this.bestDesiredIndex = i;
            this.remembered = this.current;
        }
    }

    public ULocale getBestMatch(ULocale uLocale) {
        int bestSuppIndex = getBestSuppIndex(getMaximalLsrOrUnd(uLocale), null);
        return bestSuppIndex >= 0 ? this.supportedULocales[bestSuppIndex] : this.defaultULocale;
    }

    public ULocale getBestMatch(Iterable<ULocale> iterable) {
        Iterator<ULocale> it = iterable.iterator();
        if (!it.hasNext()) {
            return this.defaultULocale;
        }
        ULocaleLsrIterator uLocaleLsrIterator = new ULocaleLsrIterator(it);
        int bestSuppIndex = getBestSuppIndex(uLocaleLsrIterator.next(), uLocaleLsrIterator);
        return bestSuppIndex >= 0 ? this.supportedULocales[bestSuppIndex] : this.defaultULocale;
    }

    public ULocale getBestMatch(String str) {
        return getBestMatch(LocalePriorityList.add(str).build());
    }

    public Locale getBestLocale(Locale locale) {
        int bestSuppIndex = getBestSuppIndex(getMaximalLsrOrUnd(locale), null);
        return bestSuppIndex >= 0 ? this.supportedLocales[bestSuppIndex] : this.defaultLocale;
    }

    public Locale getBestLocale(Iterable<Locale> iterable) {
        Iterator<Locale> it = iterable.iterator();
        if (!it.hasNext()) {
            return this.defaultLocale;
        }
        LocaleLsrIterator localeLsrIterator = new LocaleLsrIterator(it);
        int bestSuppIndex = getBestSuppIndex(localeLsrIterator.next(), localeLsrIterator);
        return bestSuppIndex >= 0 ? this.supportedLocales[bestSuppIndex] : this.defaultLocale;
    }

    private Result defaultResult() {
        return new Result(null, this.defaultULocale, null, this.defaultLocale, -1, -1);
    }

    private Result makeResult(ULocale uLocale, ULocaleLsrIterator uLocaleLsrIterator, int i) {
        if (i < 0) {
            return defaultResult();
        }
        if (uLocale != null) {
            return new Result(uLocale, this.supportedULocales[i], null, this.supportedLocales[i], 0, i);
        }
        return new Result(uLocaleLsrIterator.remembered, this.supportedULocales[i], null, this.supportedLocales[i], uLocaleLsrIterator.bestDesiredIndex, i);
    }

    private Result makeResult(Locale locale, LocaleLsrIterator localeLsrIterator, int i) {
        if (i < 0) {
            return defaultResult();
        }
        if (locale != null) {
            return new Result(null, this.supportedULocales[i], locale, this.supportedLocales[i], 0, i);
        }
        return new Result(null, this.supportedULocales[i], localeLsrIterator.remembered, this.supportedLocales[i], localeLsrIterator.bestDesiredIndex, i);
    }

    public Result getBestMatchResult(ULocale uLocale) {
        return makeResult(uLocale, (ULocaleLsrIterator) null, getBestSuppIndex(getMaximalLsrOrUnd(uLocale), null));
    }

    public Result getBestMatchResult(Iterable<ULocale> iterable) {
        Iterator<ULocale> it = iterable.iterator();
        if (!it.hasNext()) {
            return defaultResult();
        }
        ULocaleLsrIterator uLocaleLsrIterator = new ULocaleLsrIterator(it);
        return makeResult((ULocale) null, uLocaleLsrIterator, getBestSuppIndex(uLocaleLsrIterator.next(), uLocaleLsrIterator));
    }

    public Result getBestLocaleResult(Locale locale) {
        return makeResult(locale, (LocaleLsrIterator) null, getBestSuppIndex(getMaximalLsrOrUnd(locale), null));
    }

    public Result getBestLocaleResult(Iterable<Locale> iterable) {
        Iterator<Locale> it = iterable.iterator();
        if (!it.hasNext()) {
            return defaultResult();
        }
        LocaleLsrIterator localeLsrIterator = new LocaleLsrIterator(it);
        return makeResult((Locale) null, localeLsrIterator, getBestSuppIndex(localeLsrIterator.next(), localeLsrIterator));
    }

    private int getBestSuppIndex(LSR lsr, LsrIterator lsrIterator) {
        int shiftDistance = LocaleDistance.shiftDistance(this.thresholdDistance);
        int i = 0;
        LSR lsr2 = lsr;
        int i2 = -1;
        while (true) {
            Integer num = this.supportedLsrToIndex.get(lsr2);
            if (num != null) {
                int intValue = num.intValue();
                if (lsrIterator != null) {
                    lsrIterator.rememberCurrent(i);
                }
                return intValue;
            }
            int bestIndexAndDistance = LocaleDistance.INSTANCE.getBestIndexAndDistance(lsr2, this.supportedLSRs, this.supportedLSRsLength, shiftDistance, this.favorSubtag, this.direction);
            if (bestIndexAndDistance >= 0) {
                shiftDistance = LocaleDistance.getShiftedDistance(bestIndexAndDistance);
                if (lsrIterator != null) {
                    lsrIterator.rememberCurrent(i);
                }
                i2 = LocaleDistance.getIndex(bestIndexAndDistance);
            }
            shiftDistance -= LocaleDistance.shiftDistance(this.demotionPerDesiredLocale);
            if (shiftDistance > 0 && lsrIterator != null && lsrIterator.hasNext()) {
                lsr2 = (LSR) lsrIterator.next();
                i++;
            }
        }
        if (i2 < 0) {
            return -1;
        }
        return this.supportedIndexes[i2];
    }

    @Deprecated
    public double match(ULocale uLocale, ULocale uLocale2, ULocale uLocale3, ULocale uLocale4) {
        return (100.0d - LocaleDistance.getDistanceDouble(LocaleDistance.INSTANCE.getBestIndexAndDistance(getMaximalLsrOrUnd(uLocale), new LSR[]{getMaximalLsrOrUnd(uLocale3)}, 1, LocaleDistance.shiftDistance(this.thresholdDistance), this.favorSubtag, this.direction))) / 100.0d;
    }

    public ULocale canonicalize(ULocale uLocale) {
        return XLikelySubtags.INSTANCE.canonicalize(uLocale);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{LocaleMatcher");
        if (this.supportedLSRsLength > 0) {
            sb.append(" supportedLSRs={");
            sb.append(this.supportedLSRs[0]);
            for (int i = 1; i < this.supportedLSRsLength; i++) {
                sb.append(", ");
                sb.append(this.supportedLSRs[i]);
            }
            sb.append('}');
        }
        sb.append(" default=");
        sb.append(this.defaultULocale);
        if (this.favorSubtag != null) {
            sb.append(" favor=");
            sb.append(this.favorSubtag);
        }
        if (this.direction != null) {
            sb.append(" direction=");
            sb.append(this.direction);
        }
        int i2 = this.thresholdDistance;
        if (i2 >= 0) {
            sb.append(String.format(" threshold=%d", Integer.valueOf(i2)));
        }
        sb.append(String.format(" demotion=%d", Integer.valueOf(this.demotionPerDesiredLocale)));
        sb.append('}');
        return sb.toString();
    }
}
