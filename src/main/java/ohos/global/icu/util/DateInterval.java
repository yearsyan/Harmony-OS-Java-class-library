package ohos.global.icu.util;

import java.io.Serializable;

public final class DateInterval implements Serializable {
    private static final long serialVersionUID = 1;
    private final long fromDate;
    private final long toDate;

    public DateInterval(long j, long j2) {
        this.fromDate = j;
        this.toDate = j2;
    }

    public long getFromDate() {
        return this.fromDate;
    }

    public long getToDate() {
        return this.toDate;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DateInterval)) {
            return false;
        }
        DateInterval dateInterval = (DateInterval) obj;
        if (this.fromDate == dateInterval.fromDate && this.toDate == dateInterval.toDate) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return (int) (this.fromDate + this.toDate);
    }

    public String toString() {
        return String.valueOf(this.fromDate) + " " + String.valueOf(this.toDate);
    }
}
