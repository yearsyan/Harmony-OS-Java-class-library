package ohos.com.sun.org.apache.xml.internal.dtm.ref;

import java.util.BitSet;
import ohos.com.sun.org.apache.xml.internal.res.XMLMessages;

public class CoroutineManager {
    static final int ANYBODY = -1;
    static final int NOBODY = -1;
    static final int m_unreasonableId = 1024;
    BitSet m_activeIDs = new BitSet();
    int m_nextCoroutine = -1;
    Object m_yield = null;

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x000e, code lost:
        if (r3.m_activeIDs.get(r4) != false) goto L_0x0013;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int co_joinCoroutineSet(int r4) {
        /*
            r3 = this;
            monitor-enter(r3)
            r0 = -1
            r1 = 1024(0x400, float:1.435E-42)
            if (r4 < 0) goto L_0x0015
            if (r4 >= r1) goto L_0x0013
            java.util.BitSet r1 = r3.m_activeIDs     // Catch:{ all -> 0x0011 }
            boolean r1 = r1.get(r4)     // Catch:{ all -> 0x0011 }
            if (r1 == 0) goto L_0x0027
            goto L_0x0013
        L_0x0011:
            r4 = move-exception
            goto L_0x002e
        L_0x0013:
            monitor-exit(r3)
            return r0
        L_0x0015:
            r4 = 0
        L_0x0016:
            if (r4 >= r1) goto L_0x0023
            java.util.BitSet r2 = r3.m_activeIDs
            boolean r2 = r2.get(r4)
            if (r2 == 0) goto L_0x0023
            int r4 = r4 + 1
            goto L_0x0016
        L_0x0023:
            if (r4 < r1) goto L_0x0027
            monitor-exit(r3)
            return r0
        L_0x0027:
            java.util.BitSet r0 = r3.m_activeIDs
            r0.set(r4)
            monitor-exit(r3)
            return r4
        L_0x002e:
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.com.sun.org.apache.xml.internal.dtm.ref.CoroutineManager.co_joinCoroutineSet(int):int");
    }

    public synchronized Object co_entry_pause(int i) throws NoSuchMethodException {
        if (this.m_activeIDs.get(i)) {
            while (this.m_nextCoroutine != i) {
                try {
                    wait();
                } catch (InterruptedException unused) {
                }
            }
        } else {
            throw new NoSuchMethodException();
        }
        return this.m_yield;
    }

    public synchronized Object co_resume(Object obj, int i, int i2) throws NoSuchMethodException {
        if (this.m_activeIDs.get(i2)) {
            this.m_yield = obj;
            this.m_nextCoroutine = i2;
            notify();
            while (true) {
                if (this.m_nextCoroutine == i && this.m_nextCoroutine != -1) {
                    if (this.m_nextCoroutine != -1) {
                        break;
                    }
                }
                try {
                    wait();
                } catch (InterruptedException unused) {
                }
            }
            if (this.m_nextCoroutine != -1) {
            } else {
                co_exit(i);
                throw new NoSuchMethodException(XMLMessages.createXMLMessage("ER_COROUTINE_CO_EXIT", null));
            }
        } else {
            throw new NoSuchMethodException(XMLMessages.createXMLMessage("ER_COROUTINE_NOT_AVAIL", new Object[]{Integer.toString(i2)}));
        }
        return this.m_yield;
    }

    public synchronized void co_exit(int i) {
        this.m_activeIDs.clear(i);
        this.m_nextCoroutine = -1;
        notify();
    }

    public synchronized void co_exit_to(Object obj, int i, int i2) throws NoSuchMethodException {
        if (this.m_activeIDs.get(i2)) {
            this.m_yield = obj;
            this.m_nextCoroutine = i2;
            this.m_activeIDs.clear(i);
            notify();
        } else {
            throw new NoSuchMethodException(XMLMessages.createXMLMessage("ER_COROUTINE_NOT_AVAIL", new Object[]{Integer.toString(i2)}));
        }
    }
}
