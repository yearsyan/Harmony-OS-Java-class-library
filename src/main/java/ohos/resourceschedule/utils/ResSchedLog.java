package ohos.resourceschedule.utils;

import ohos.hiviewdfx.HiLogLabel;

public class ResSchedLog {

    public enum Domain {
        RESSCHED(218109696),
        BGTASK(218109697),
        WORKSCHED(218109698);
        
        private final int domain;

        private Domain(int i) {
            this.domain = i;
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private int get() {
            return this.domain;
        }
    }

    private ResSchedLog() {
    }

    public static HiLogLabel getLabel(Domain domain, String str) {
        return new HiLogLabel(3, domain.get(), str);
    }
}
