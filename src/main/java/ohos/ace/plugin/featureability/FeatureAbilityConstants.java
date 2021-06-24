package ohos.ace.plugin.featureability;

public interface FeatureAbilityConstants {
    public static final int INTERFACE_TYPE_EXTERNAL = 0;
    public static final int INTERFACE_TYPE_EXTERNAL_ZIDL = 2;
    public static final int INTERFACE_TYPE_INTERNAL = 1;
    public static final int INTERFACE_TYPE_INTERNAL_INVOKE = 3;
    public static final String PLUGIN_GROUP_NAME = "AcePluginGroup/FeatureAbility";
    public static final int REMOTE_INTERFACE_CONNECT_STATUS_DIED = -1;
    public static final int REMOTE_INTERFACE_CONNECT_STATUS_INIT = 0;
    public static final int REMOTE_INTERFACE_CONNECT_STATUS_LIVE = 1;
    public static final int REQUEST_TYPE_ASYNC = 1;
    public static final int REQUEST_TYPE_SYNC = 0;

    static default boolean isExtendInterface(int i) {
        return (i & 2) == 2;
    }

    static default boolean isLocalInterface(int i) {
        return (i & 1) == 1;
    }

    static default boolean isSyncRequest(int i) {
        return (i & 1) != 1;
    }
}
