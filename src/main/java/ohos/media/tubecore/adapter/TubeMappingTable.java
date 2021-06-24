package ohos.media.tubecore.adapter;

import android.media.MediaRouter;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import ohos.media.tubecore.AVTube;
import ohos.media.utils.log.Logger;
import ohos.media.utils.log.LoggerFactory;

public class TubeMappingTable {
    private static final HashMap<MediaRouter.RouteInfo, AVTubeAdapter> A_2_Z_TUBE_MAP = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getMediaLogger(TubeMappingTable.class);
    private static final HashMap<AVTubeAdapter, AVTube> Z_2_Z_TUBE_MAP = new HashMap<>();

    private TubeMappingTable() {
    }

    public static void addKeyValuePair(MediaRouter.RouteInfo routeInfo, AVTubeAdapter aVTubeAdapter) {
        if (Objects.isNull(routeInfo) || Objects.isNull(aVTubeAdapter)) {
            LOGGER.warn("[addKeyValuePair]routeInfo or tubeAdapter is null", new Object[0]);
        } else {
            A_2_Z_TUBE_MAP.put(routeInfo, aVTubeAdapter);
        }
    }

    public static void addKeyValuePair(AVTubeAdapter aVTubeAdapter, AVTube aVTube) {
        if (Objects.isNull(aVTubeAdapter) || Objects.isNull(aVTube)) {
            LOGGER.warn("[addKeyValuePair]tubeAdapter or tube is null", new Object[0]);
        } else {
            Z_2_Z_TUBE_MAP.put(aVTubeAdapter, aVTube);
        }
    }

    public static Optional<AVTubeAdapter> findAVTubeAdapter(MediaRouter.RouteInfo routeInfo) {
        if (!Objects.isNull(routeInfo)) {
            return Optional.ofNullable(A_2_Z_TUBE_MAP.get(routeInfo));
        }
        LOGGER.warn("[findAVTubeAdapter]routeInfo is null", new Object[0]);
        return Optional.empty();
    }

    public static Optional<AVTube> findAVTube(AVTubeAdapter aVTubeAdapter) {
        if (!Objects.isNull(aVTubeAdapter)) {
            return Optional.ofNullable(Z_2_Z_TUBE_MAP.get(aVTubeAdapter));
        }
        LOGGER.warn("[findAVTube]adapter is null", new Object[0]);
        return Optional.empty();
    }

    public static Optional<AVTube> findAVTube(MediaRouter.RouteInfo routeInfo) {
        if (Objects.isNull(routeInfo)) {
            LOGGER.warn("[findAVTube]routeInfo is null", new Object[0]);
            return Optional.empty();
        } else if (!A_2_Z_TUBE_MAP.containsKey(routeInfo)) {
            LOGGER.warn("[findAVTube]key routeInfo not exist", new Object[0]);
            return Optional.empty();
        } else if (Z_2_Z_TUBE_MAP.containsKey(A_2_Z_TUBE_MAP.get(routeInfo))) {
            return Optional.ofNullable(Z_2_Z_TUBE_MAP.get(A_2_Z_TUBE_MAP.get(routeInfo)));
        } else {
            LOGGER.warn("[findAVTube]key tubeAdapter not exist", new Object[0]);
            return Optional.empty();
        }
    }
}
