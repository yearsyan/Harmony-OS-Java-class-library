package ohos.media.tubecore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import ohos.app.Context;
import ohos.media.tubecore.AVTube;
import ohos.media.tubecore.adapter.AVTubeAdapter;
import ohos.media.tubecore.adapter.AVTubeExAdapter;
import ohos.media.tubecore.adapter.AVTubeManagerAdapter;
import ohos.media.utils.log.Logger;
import ohos.media.utils.log.LoggerFactory;

public class AVTubeManager {
    private static final Logger LOGGER = LoggerFactory.getMediaLogger(AVTubeManager.class);
    private static final Object SINGLETON_LOCK = new Object();
    private static AVTubeManager instance = null;
    private final Context ctx;
    private final AVTube defaultTube;
    private final AVTubeManagerAdapter managerAdapter;
    private final CopyOnWriteArrayList<TubeObserver> tubeObserverList = new CopyOnWriteArrayList<>();
    private final Map<String, AVTube> tubes = new HashMap();

    public interface TubeObserver {
        void onTubeDelete(AVTubeManager aVTubeManager, AVTube aVTube);

        void onTubeEnterWorkingState(AVTubeManager aVTubeManager, AVTube aVTube, int i);

        void onTubeExitWorkingState(AVTubeManager aVTubeManager, AVTube aVTube, int i);

        void onTubeInsert(AVTubeManager aVTubeManager, AVTube aVTube);

        void onTubeUpdate(AVTubeManager aVTubeManager, AVTube aVTube);
    }

    public static class ObserverFlag {
        public static final int EXEC_SCANNING = 1;
        public static final int IGNORE_FILTER = 2;
        public static final int NO_FLAG = 0;
        private static int[] flagSet = {1, 2};

        private ObserverFlag() {
        }

        static int sanitize(int i) {
            if (i < 0) {
                AVTubeManager.LOGGER.warn("[sanitize]invalid observerFlags: %{public}d", Integer.valueOf(i));
                return 0;
            }
            int i2 = 0;
            int i3 = 0;
            while (true) {
                int[] iArr = flagSet;
                if (i2 >= iArr.length) {
                    return i3;
                }
                i3 |= (iArr[i2] & i) != 0 ? iArr[i2] : 0;
                i2++;
            }
        }
    }

    public static AVTubeManager getInstance(Context context) {
        AVTubeManager aVTubeManager;
        Objects.requireNonNull(context, "context can not be null");
        synchronized (SINGLETON_LOCK) {
            if (instance == null) {
                instance = new AVTubeManager(context);
            }
            aVTubeManager = instance;
        }
        return aVTubeManager;
    }

    public static AVTubeManager getInstance() {
        Objects.requireNonNull(instance, "the singleton instance is not initialized yet");
        return instance;
    }

    private AVTubeManager(Context context) {
        AVTubeManagerAdapter instance2 = AVTubeManagerAdapter.getInstance(context);
        Objects.requireNonNull(instance2, "initialize failed");
        this.ctx = context;
        this.managerAdapter = instance2;
        this.defaultTube = new AVTube(this.managerAdapter.defaultTube());
    }

    public AVTube defaultTube() {
        Objects.requireNonNull(this.managerAdapter, "not initialized");
        return this.defaultTube;
    }

    public Optional<AVTube> getTubeByIndex(int i) {
        Objects.requireNonNull(this.managerAdapter, "not initialized");
        if (i >= 0) {
            return this.managerAdapter.getTubeByIndex(i).map($$Lambda$AVTubeManager$UlctEsjjRW5r9geipZMZkNga2s.INSTANCE);
        }
        LOGGER.warn("[getTubeByIndex]index is invalid:%{public}d", Integer.valueOf(i));
        return Optional.empty();
    }

    static /* synthetic */ AVTube lambda$getTubeByIndex$0(AVTubeAdapter aVTubeAdapter) {
        return new AVTube(aVTubeAdapter);
    }

    public int tubeNumber() {
        return this.managerAdapter.tubeNumber();
    }

    public Optional<AVTube> tubeInUse() {
        return tubeInUse(AVTube.UsageScene.ANY);
    }

    public Optional<AVTube> tubeInUse(int i) {
        Objects.requireNonNull(this.managerAdapter, "not initialized");
        return this.managerAdapter.tubeInUse(AVTube.UsageScene.sanitize(i)).map($$Lambda$AVTubeManager$rKFny0_iQijoukH2gT6n55utO2c.INSTANCE);
    }

    static /* synthetic */ AVTube lambda$tubeInUse$2(AVTubeAdapter aVTubeAdapter) {
        return new AVTube(aVTubeAdapter);
    }

    public void useTube(AVTube aVTube, int i) {
        Objects.requireNonNull(this.managerAdapter, "not initialized");
        if (Objects.isNull(aVTube)) {
            LOGGER.warn("[useTube]null tube", new Object[0]);
        } else {
            this.managerAdapter.useTube(aVTube.tubeAdapter, AVTube.UsageScene.sanitize(i));
        }
    }

    public void addTubeObserver(TubeObserver tubeObserver, int i, int i2) {
        Objects.requireNonNull(this.managerAdapter, "not initialized");
        if (Objects.isNull(tubeObserver)) {
            LOGGER.warn("[addTubeObserver]null observer", new Object[0]);
            return;
        }
        this.managerAdapter.addTubeObserver(tubeObserver, AVTube.UsageScene.sanitize(i), ObserverFlag.sanitize(i2));
        this.tubeObserverList.add(tubeObserver);
    }

    public void removeTubeObserver(TubeObserver tubeObserver) {
        Objects.requireNonNull(this.managerAdapter, "not initialized");
        if (Objects.isNull(tubeObserver)) {
            LOGGER.warn("[removeTubeObserver]null observer", new Object[0]);
            return;
        }
        this.managerAdapter.removeTubeObserver(tubeObserver);
        this.tubeObserverList.remove(tubeObserver);
    }

    public Optional<AVTube.SetInfo> createTubeSet(String str) {
        Objects.requireNonNull(this.managerAdapter, "not initialized");
        return this.managerAdapter.createTubeSet(str).map($$Lambda$AVTubeManager$I3A1gQuydjAEGROVV7Iw4RNRmc.INSTANCE);
    }

    static /* synthetic */ AVTube.SetInfo lambda$createTubeSet$4(AVTubeAdapter.SetInfoAdapter setInfoAdapter) {
        return new AVTube.SetInfo(setInfoAdapter);
    }

    public Optional<AVTube.SetInfo> createTubeSet(int i) {
        Objects.requireNonNull(this.managerAdapter, "not initialized");
        return this.managerAdapter.createTubeSet(i).map($$Lambda$AVTubeManager$MbcuVy4fL1EWf0PV5yvQnlZjg.INSTANCE);
    }

    static /* synthetic */ AVTube.SetInfo lambda$createTubeSet$5(AVTubeAdapter.SetInfoAdapter setInfoAdapter) {
        return new AVTube.SetInfo(setInfoAdapter);
    }

    public Optional<AVTubeEx> createAVTubeEx(AVTube.SetInfo setInfo) {
        Objects.requireNonNull(this.managerAdapter, "not initialized");
        return this.managerAdapter.createAVTubeEx(setInfo.setInfoAdapter).map($$Lambda$AVTubeManager$Yr5YzZpCihjEDkDrS_iANnwyb8.INSTANCE);
    }

    static /* synthetic */ AVTubeEx lambda$createAVTubeEx$6(AVTubeExAdapter aVTubeExAdapter) {
        return new AVTubeEx(aVTubeExAdapter);
    }

    public void insertAVTubeEx(AVTubeEx aVTubeEx) {
        Objects.requireNonNull(this.managerAdapter, "not initialized");
        if (Objects.isNull(aVTubeEx)) {
            LOGGER.warn("[insertAVTubeEx]null avTubeEx", new Object[0]);
        } else {
            this.managerAdapter.insertAVTubeEx(aVTubeEx.tubeExAdapter);
        }
    }

    public void deleteAVTubeEx(AVTubeEx aVTubeEx) {
        Objects.requireNonNull(this.managerAdapter, "not initialized");
        if (Objects.isNull(aVTubeEx)) {
            LOGGER.warn("[deleteAVTubeEx]null avTubeEx", new Object[0]);
        } else {
            this.managerAdapter.deleteAVTubeEx(aVTubeEx.tubeExAdapter);
        }
    }
}
