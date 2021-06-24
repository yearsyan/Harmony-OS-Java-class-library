package ohos.media.tubecore.adapter;

import android.media.MediaRouter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;
import ohos.app.Context;
import ohos.media.tubecore.AVTube;
import ohos.media.tubecore.AVTubeManager;
import ohos.media.tubecore.adapter.AVTubeAdapter;
import ohos.media.tubecore.adapter.AVTubeManagerAdapter;
import ohos.media.utils.log.Logger;
import ohos.media.utils.log.LoggerFactory;

public class AVTubeManagerAdapter {
    private static final Logger LOGGER = LoggerFactory.getMediaLogger(AVTubeManagerAdapter.class);
    private static final HashMap<AVTubeManager.TubeObserver, MediaRouter.Callback> Z_2_A_OBSERVER_MAP = new HashMap<>();
    private static AVTubeManagerAdapter instance;
    private final Context ctx;
    private final AVTubeAdapter defaultTube;
    private final MediaRouter mediaRouter;
    private final CopyOnWriteArrayList<AVTubeManager.TubeObserver> tubeObserver = new CopyOnWriteArrayList<>();
    private final Map<String, AVTube> tubes = new HashMap();

    public static AVTubeManagerAdapter getInstance(Context context) {
        Objects.requireNonNull(context, "context must not be null");
        if (instance == null) {
            instance = new AVTubeManagerAdapter(context);
        }
        return instance;
    }

    private AVTubeManagerAdapter(Context context) {
        Object hostContext = context.getHostContext();
        if (hostContext instanceof android.content.Context) {
            this.mediaRouter = new MediaRouter((android.content.Context) hostContext);
            this.ctx = context;
            this.defaultTube = new AVTubeAdapter(this.mediaRouter.getDefaultRoute());
            return;
        }
        throw new IllegalArgumentException("context invalid");
    }

    public AVTubeAdapter defaultTube() {
        return this.defaultTube;
    }

    public Optional<AVTubeAdapter> getTubeByIndex(int i) {
        Objects.requireNonNull(this.mediaRouter, "not init yet");
        MediaRouter.RouteInfo routeAt = this.mediaRouter.getRouteAt(i);
        if (Objects.isNull(routeAt)) {
            return Optional.empty();
        }
        return Optional.of(TubeMappingTable.findAVTubeAdapter(routeAt).orElseGet(new Supplier(routeAt) {
            /* class ohos.media.tubecore.adapter.$$Lambda$AVTubeManagerAdapter$wru4xF_YrYDBRGPS1oOYJz1RBA */
            private final /* synthetic */ MediaRouter.RouteInfo f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Supplier
            public final Object get() {
                return AVTubeManagerAdapter.lambda$getTubeByIndex$0(this.f$0);
            }
        }));
    }

    static /* synthetic */ AVTubeAdapter lambda$getTubeByIndex$0(MediaRouter.RouteInfo routeInfo) {
        return new AVTubeAdapter(routeInfo);
    }

    public int tubeNumber() {
        Objects.requireNonNull(this.mediaRouter, "not init yet");
        return this.mediaRouter.getRouteCount();
    }

    public Optional<AVTubeAdapter> tubeInUse(int i) {
        Objects.requireNonNull(this.mediaRouter, "not init yet");
        MediaRouter.RouteInfo selectedRoute = this.mediaRouter.getSelectedRoute(i);
        if (Objects.isNull(selectedRoute)) {
            return Optional.empty();
        }
        return Optional.of(TubeMappingTable.findAVTubeAdapter(selectedRoute).orElseGet(new Supplier(selectedRoute) {
            /* class ohos.media.tubecore.adapter.$$Lambda$AVTubeManagerAdapter$X3Jmf2a4Obvmakiyi91FNhcao */
            private final /* synthetic */ MediaRouter.RouteInfo f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Supplier
            public final Object get() {
                return AVTubeManagerAdapter.lambda$tubeInUse$1(this.f$0);
            }
        }));
    }

    static /* synthetic */ AVTubeAdapter lambda$tubeInUse$1(MediaRouter.RouteInfo routeInfo) {
        return new AVTubeAdapter(routeInfo);
    }

    public void useTube(AVTubeAdapter aVTubeAdapter, int i) {
        Objects.requireNonNull(this.mediaRouter, "not init yet");
        Objects.requireNonNull(aVTubeAdapter, "tube must not be null");
        this.mediaRouter.selectRoute(i, aVTubeAdapter.routeInfo);
    }

    public void addTubeObserver(AVTubeManager.TubeObserver tubeObserver2, int i, int i2) {
        Objects.requireNonNull(this.mediaRouter, "not init yet");
        Objects.requireNonNull(tubeObserver2, "observer must not be null");
        synchronized (Z_2_A_OBSERVER_MAP) {
            CallbackAdapter callbackAdapter = new CallbackAdapter(tubeObserver2);
            this.mediaRouter.addCallback(i, callbackAdapter, i2);
            Z_2_A_OBSERVER_MAP.put(tubeObserver2, callbackAdapter);
        }
    }

    public void removeTubeObserver(AVTubeManager.TubeObserver tubeObserver2) {
        Objects.requireNonNull(this.mediaRouter, "not init yet");
        Objects.requireNonNull(tubeObserver2, "observer must not be null");
        synchronized (Z_2_A_OBSERVER_MAP) {
            if (Z_2_A_OBSERVER_MAP.containsKey(tubeObserver2)) {
                this.mediaRouter.removeCallback(Z_2_A_OBSERVER_MAP.get(tubeObserver2));
                Z_2_A_OBSERVER_MAP.remove(tubeObserver2);
            }
        }
    }

    /* access modifiers changed from: private */
    public class CallbackAdapter extends MediaRouter.SimpleCallback {
        AVTubeManager.TubeObserver tubeObserver;

        CallbackAdapter(AVTubeManager.TubeObserver tubeObserver2) {
            this.tubeObserver = tubeObserver2;
        }

        static /* synthetic */ AVTube lambda$onRouteSelected$0(MediaRouter.RouteInfo routeInfo) {
            return new AVTube(new AVTubeAdapter(routeInfo));
        }

        public void onRouteSelected(MediaRouter mediaRouter, int i, MediaRouter.RouteInfo routeInfo) {
            this.tubeObserver.onTubeEnterWorkingState(AVTubeManager.getInstance(), TubeMappingTable.findAVTube(routeInfo).orElseGet(new Supplier(routeInfo) {
                /* class ohos.media.tubecore.adapter.$$Lambda$AVTubeManagerAdapter$CallbackAdapter$8qcpgbVFySKtqOu4uGCsHQH9JY4 */
                private final /* synthetic */ MediaRouter.RouteInfo f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.util.function.Supplier
                public final Object get() {
                    return AVTubeManagerAdapter.CallbackAdapter.lambda$onRouteSelected$0(this.f$0);
                }
            }), i);
        }

        static /* synthetic */ AVTube lambda$onRouteUnselected$1(MediaRouter.RouteInfo routeInfo) {
            return new AVTube(new AVTubeAdapter(routeInfo));
        }

        public void onRouteUnselected(MediaRouter mediaRouter, int i, MediaRouter.RouteInfo routeInfo) {
            this.tubeObserver.onTubeExitWorkingState(AVTubeManager.getInstance(), TubeMappingTable.findAVTube(routeInfo).orElseGet(new Supplier(routeInfo) {
                /* class ohos.media.tubecore.adapter.$$Lambda$AVTubeManagerAdapter$CallbackAdapter$qHE29BfTu0JqPe0LSIHxeJrIQ */
                private final /* synthetic */ MediaRouter.RouteInfo f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.util.function.Supplier
                public final Object get() {
                    return AVTubeManagerAdapter.CallbackAdapter.lambda$onRouteUnselected$1(this.f$0);
                }
            }), i);
        }

        static /* synthetic */ AVTube lambda$onRouteAdded$2(MediaRouter.RouteInfo routeInfo) {
            return new AVTube(new AVTubeAdapter(routeInfo));
        }

        public void onRouteAdded(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            this.tubeObserver.onTubeInsert(AVTubeManager.getInstance(), TubeMappingTable.findAVTube(routeInfo).orElseGet(new Supplier(routeInfo) {
                /* class ohos.media.tubecore.adapter.$$Lambda$AVTubeManagerAdapter$CallbackAdapter$2SYmOwJguwvu1sMrAl67ho1O7c4 */
                private final /* synthetic */ MediaRouter.RouteInfo f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.util.function.Supplier
                public final Object get() {
                    return AVTubeManagerAdapter.CallbackAdapter.lambda$onRouteAdded$2(this.f$0);
                }
            }));
        }

        static /* synthetic */ AVTube lambda$onRouteRemoved$3(MediaRouter.RouteInfo routeInfo) {
            return new AVTube(new AVTubeAdapter(routeInfo));
        }

        public void onRouteRemoved(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            this.tubeObserver.onTubeDelete(AVTubeManager.getInstance(), TubeMappingTable.findAVTube(routeInfo).orElseGet(new Supplier(routeInfo) {
                /* class ohos.media.tubecore.adapter.$$Lambda$AVTubeManagerAdapter$CallbackAdapter$wisJ_UT6FGfFhRvlZ6yLP64dRCs */
                private final /* synthetic */ MediaRouter.RouteInfo f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.util.function.Supplier
                public final Object get() {
                    return AVTubeManagerAdapter.CallbackAdapter.lambda$onRouteRemoved$3(this.f$0);
                }
            }));
        }

        static /* synthetic */ AVTube lambda$onRouteChanged$4(MediaRouter.RouteInfo routeInfo) {
            return new AVTube(new AVTubeAdapter(routeInfo));
        }

        public void onRouteChanged(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            this.tubeObserver.onTubeUpdate(AVTubeManager.getInstance(), TubeMappingTable.findAVTube(routeInfo).orElseGet(new Supplier(routeInfo) {
                /* class ohos.media.tubecore.adapter.$$Lambda$AVTubeManagerAdapter$CallbackAdapter$18lFvNHDLQnrO7QzcjZQP8AQ3DI */
                private final /* synthetic */ MediaRouter.RouteInfo f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.util.function.Supplier
                public final Object get() {
                    return AVTubeManagerAdapter.CallbackAdapter.lambda$onRouteChanged$4(this.f$0);
                }
            }));
        }
    }

    public Optional<AVTubeAdapter.SetInfoAdapter> createTubeSet(String str) {
        Objects.requireNonNull(this.mediaRouter, "not init yet");
        MediaRouter.RouteCategory createRouteCategory = this.mediaRouter.createRouteCategory((CharSequence) str, false);
        if (Objects.isNull(createRouteCategory)) {
            return Optional.empty();
        }
        return Optional.of(new AVTubeAdapter.SetInfoAdapter(createRouteCategory));
    }

    public Optional<AVTubeAdapter.SetInfoAdapter> createTubeSet(int i) {
        Objects.requireNonNull(this.mediaRouter, "not init yet");
        MediaRouter.RouteCategory createRouteCategory = this.mediaRouter.createRouteCategory(i, false);
        if (Objects.isNull(createRouteCategory)) {
            return Optional.empty();
        }
        return Optional.of(new AVTubeAdapter.SetInfoAdapter(createRouteCategory));
    }

    public Optional<AVTubeExAdapter> createAVTubeEx(AVTubeAdapter.SetInfoAdapter setInfoAdapter) {
        Objects.requireNonNull(this.mediaRouter, "not init yet");
        MediaRouter.UserRouteInfo createUserRoute = this.mediaRouter.createUserRoute(setInfoAdapter.routeCategory);
        if (Objects.isNull(createUserRoute)) {
            return Optional.empty();
        }
        return Optional.of(new AVTubeExAdapter(createUserRoute));
    }

    public void insertAVTubeEx(AVTubeExAdapter aVTubeExAdapter) {
        Objects.requireNonNull(this.mediaRouter, "not init yet");
        Objects.requireNonNull(aVTubeExAdapter, "tube must not be null");
        this.mediaRouter.addUserRoute(aVTubeExAdapter.userRouteInfo);
    }

    public void deleteAVTubeEx(AVTubeExAdapter aVTubeExAdapter) {
        Objects.requireNonNull(this.mediaRouter, "not init yet");
        Objects.requireNonNull(aVTubeExAdapter, "tube must not be null");
        this.mediaRouter.removeUserRoute(aVTubeExAdapter.userRouteInfo);
    }
}
