package ohos.security;

import ohos.security.permission.PermissionAvailableScope;
import ohos.security.permission.PermissionGrantMode;
import ohos.security.permission.PermissionLevel;

public final class SystemPermission {
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String ACCELEROMETER = "ohos.permission.ACCELEROMETER";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String ACCESS_BIOMETRIC = "ohos.permission.ACCESS_BIOMETRIC";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String ACCESS_BIOMETRIC_INTERNAL = "ohos.permission.ACCESS_BIOMETRIC_INTERNAL";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.USER_GRANT)
    @Deprecated
    public static final String ACCESS_DISTRIBUTED_ABILITY = "ohos.permission.ACCESS_DISTRIBUTED_ABILITY";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String ACCESS_FM_AM = "ohos.permission.radio.ACCESS_FM_AM";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String ACCESS_HW_ASSETSTORE_SERVICE = "ohos.permission.ACCESS_HW_ASSETSTORE_SERVICE";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String ACCESS_HW_KEYSTORE = "ohos.permission.ACCESS_HW_KEYSTORE";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String ACCESS_MISSIONS = "ohos.permission.ACCESS_MISSIONS";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String ACCESS_UDID = "ohos.permission.sec.ACCESS_UDID";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String ACTIVITY_MOTION = "ohos.permission.ACTIVITY_MOTION";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED, PermissionAvailableScope.RESTRICTED}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String ANSWER_CALL = "ohos.permission.ANSWER_CALL";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String BUNDLE_ACTIVE_INFO = "ohos.permission.BUNDLE_ACTIVE_INFO";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String CAMERA = "ohos.permission.CAMERA";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String CAPTURE_SCREEN = "ohos.permission.CAPTURE_SCREEN";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String CHANGE_ABILITY_ENABLED_STATE = "ohos.permission.CHANGE_ABILITY_ENABLED_STATE";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String CLEAN_BACKGROUND_PROCESSES = "ohos.permission.CLEAN_BACKGROUND_PROCESSES";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String COMMONEVENT_STICKY = "ohos.permission.COMMONEVENT_STICKY";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String CONNECT_ACCESSIBILITY_ABILITY = "ohos.permission.CONNECT_ACCESSIBILITY_ABILITY";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String CONNECT_IME_ABILITY = "ohos.permission.CONNECT_IME_ABILITY";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String CONNECT_SCREEN_SAVER_ABILITY = "ohos.permission.CONNECT_SCREEN_SAVER_ABILITY";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    @Deprecated
    public static final String CONNECT_WALLPAPER = "ohos.permission.CONNECT_WALLPAPER";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String CONTROL_VEHICLE_HMI_INFO = "ohos.permission.vehicle.CONTROL_VEHICLE_HMI_INFO";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String DISCOVER_BLUETOOTH = "ohos.permission.DISCOVER_BLUETOOTH";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String DISTRIBUTED_DATA = "ohos.permission.DISTRIBUTED_DATA";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String DISTRIBUTED_DATASYNC = "ohos.permission.DISTRIBUTED_DATASYNC";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String DISTRIBUTED_VIRTUALDEVICE = "com.huawei.permission.DISTRIBUTED_VIRTUALDEVICE";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String DOWNLOAD_SESSION_MANAGER = "ohos.permission.DOWNLOAD_SESSION_MANAGER";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String ENROLL_BIOMETRIC = "ohos.permission.ENROLL_BIOMETRIC";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String FORMAT_USER_STORAGE = "ohos.permission.FORMAT_USER_STORAGE";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String GET_BUNDLE_INFO = "ohos.permission.GET_BUNDLE_INFO";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String GET_BUNDLE_INFO_PRIVILEGED = "ohos.permission.GET_BUNDLE_INFO_PRIVILEGED";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String GET_MPLINK_INFO = "ohos.permission.GET_MPLINK_INFO";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String GET_NETWORK_INFO = "ohos.permission.GET_NETWORK_INFO";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String GET_SENSITIVE_PERMISSIONS = "ohos.permission.GET_SENSITIVE_PERMISSIONS";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String GET_TELEPHONY_STATE = "ohos.permission.GET_TELEPHONY_STATE";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String GET_WALLPAPER = "ohos.permission.GET_WALLPAPER";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String GET_WIFI_CONFIG = "ohos.permission.GET_WIFI_CONFIG";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String GET_WIFI_INFO = "ohos.permission.GET_WIFI_INFO";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String GET_WIFI_LOCAL_MAC = "ohos.permission.GET_WIFI_LOCAL_MAC";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String GET_WIFI_PEERS_MAC = "ohos.permission.GET_WIFI_PEERS_MAC";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String GRANT_SENSITIVE_PERMISSIONS = "ohos.permission.GRANT_SENSITIVE_PERMISSIONS";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String GYROSCOPE = "ohos.permission.GYROSCOPE";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String INSTALL_BUNDLE = "ohos.permission.INSTALL_BUNDLE";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String INTERACT_ACROSS_LOCAL_ACCOUNTS = "ohos.permission.INTERACT_ACROSS_LOCAL_ACCOUNTS";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String INTERACT_ACROSS_LOCAL_ACCOUNTS_EXTENSION = "ohos.permission.INTERACT_ACROSS_LOCAL_ACCOUNTS_EXTENSION";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String INTERNET = "ohos.permission.INTERNET";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String KEEP_BACKGROUND_RUNNING = "ohos.permission.KEEP_BACKGROUND_RUNNING";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String LISTEN_BUNDLE_CHANGE = "ohos.permission.LISTEN_BUNDLE_CHANGE";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    @Deprecated
    public static final String LISTEN_PERMISSIONS_CHANGE = "ohos.permission.LISTEN_PERMISSIONS_CHANGE";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String LISTEN_PERMISSION_CHANGE = "ohos.permission.LISTEN_PERMISSION_CHANGE";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String LOCATION = "ohos.permission.LOCATION";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String LOCATION_IN_BACKGROUND = "ohos.permission.LOCATION_IN_BACKGROUND";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String MANAGE_APP_POLICY = "ohos.permission.MANAGE_APP_POLICY";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String MANAGE_BLUETOOTH = "ohos.permission.MANAGE_BLUETOOTH";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String MANAGE_DISTRIBUTED_PERMISSION = "com.huawei.permission.MANAGE_DISTRIBUTED_PERMISSION";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String MANAGE_ENHANCER_WIFI = "ohos.permission.MANAGE_ENHANCER_WIFI";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String MANAGE_LOCAL_ACCOUNTS = "ohos.permission.MANAGE_LOCAL_ACCOUNTS";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String MANAGE_SECURE_SETTINGS = "ohos.permission.MANAGE_SECURE_SETTINGS";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String MANAGE_SHORTCUTS = "ohos.permission.MANAGE_SHORTCUTS";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String MANAGE_USER_STORAGE = "ohos.permission.MANAGE_USER_STORAGE";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String MANAGE_VOICEMAIL = "ohos.permission.MANAGE_VOICEMAIL";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String MANAGE_WIFI_CONNECTION = "ohos.permission.MANAGE_WIFI_CONNECTION";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String MANAGE_WIFI_HOTSPOT = "ohos.permission.MANAGE_WIFI_HOTSPOT";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String MEDIA_LOCATION = "ohos.permission.MEDIA_LOCATION";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String MICROPHONE = "ohos.permission.MICROPHONE";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String MODIFY_AUDIO_SETTINGS = "ohos.permission.MODIFY_AUDIO_SETTINGS";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String MODIFY_SETTINGS = "ohos.permission.MODIFY_SETTINGS";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String MULTIMODAL_INPUT_ABILITY = "ohos.permission.MULTIMODAL_INPUT_ABILITY";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String MULTIMODAL_INTERACTIVE = "ohos.permission.MULTIMODAL_INTERACTIVE";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String NFC_CARD_EMULATION = "ohos.permission.NFC_CARD_EMULATION";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String NFC_TAG = "ohos.permission.NFC_TAG";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String NOTIFY_PERMISSION_CHANGE = "ohos.permission.NOTIFY_PERMISSION_CHANGE";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String PERMISSION_USED_STATS = "ohos.permission.PERMISSION_USED_STATS";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String PERMISSION_USING_REMIND = "ohos.permission.PERMISSION_USING_REMIND";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String PLACE_CALL = "ohos.permission.PLACE_CALL";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String POWER_MANAGER = "ohos.permission.POWER_MANAGER";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String POWER_OPTIMIZATION = "ohos.permission.POWER_OPTIMIZATION";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String PREVENT_IMMEDIATE_LAUNCH = "ohos.permission.PREVENT_IMMEDIATE_LAUNCH";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String RCV_NFC_TRANSACTION_EVENT = "ohos.permission.RCV_NFC_TRANSACTION_EVENT";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String READ_CALENDAR = "ohos.permission.READ_CALENDAR";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED, PermissionAvailableScope.RESTRICTED}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String READ_CALL_LOG = "ohos.permission.READ_CALL_LOG";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED, PermissionAvailableScope.RESTRICTED}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String READ_CELL_MESSAGES = "ohos.permission.READ_CELL_MESSAGES";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED, PermissionAvailableScope.RESTRICTED}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String READ_CONTACTS = "ohos.permission.READ_CONTACTS";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String READ_HEALTH_DATA = "ohos.permission.READ_HEALTH_DATA";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String READ_MEDIA = "ohos.permission.READ_MEDIA";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED, PermissionAvailableScope.RESTRICTED}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String READ_MESSAGES = "ohos.permission.READ_MESSAGES";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String READ_SCREEN_SAVER = "ohos.permission.READ_SCREEN_SAVER";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String READ_USER_STORAGE = "ohos.permission.READ_USER_STORAGE";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String READ_VEHICLE_CHASSIS_INFO = "ohos.permission.vehicle.READ_VEHICLE_CHASSIS_INFO";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String READ_VEHICLE_EXTERIOR_ENVIRONMENT = "ohos.permission.vehicle.READ_VEHICLE_EXTERIOR_ENVIRONMENT";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String READ_VEHICLE_FUEL_INFO = "ohos.permission.vehicle.READ_VEHICLE_FUEL_INFO";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String READ_VEHICLE_HMI_INFO = "ohos.permission.vehicle.READ_VEHICLE_HMI_INFO";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String READ_VEHICLE_IDENTIFICATION = "ohos.permission.vehicle.READ_VEHICLE_IDENTIFICATION";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String READ_VEHICLE_SPEED_INFO = "ohos.permission.vehicle.READ_VEHICLE_SPEED_INFO";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String READ_VEHICLE_STEERING_WHEEL = "ohos.permission.vehicle.READ_VEHICLE_STEERING_WHEEL";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String READ_VEHICLE_TRANSMISSION_INFO = "ohos.permission.vehicle.READ_VEHICLE_TRANSMISSION_INFO";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String REARRANGE_MISSIONS = "ohos.permission.REARRANGE_MISSIONS";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String REBOOT = "ohos.permission.REBOOT";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String REBOOT_RECOVERY = "ohos.permission.REBOOT_RECOVERY";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String RECEIVER_STARTUP_COMPLETED = "ohos.permission.RECEIVER_STARTUP_COMPLETED";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED, PermissionAvailableScope.RESTRICTED}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String RECEIVE_MMS = "ohos.permission.RECEIVE_MMS";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED, PermissionAvailableScope.RESTRICTED}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String RECEIVE_SMS = "ohos.permission.RECEIVE_SMS";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED, PermissionAvailableScope.RESTRICTED}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String RECEIVE_WAP_MESSAGES = "ohos.permission.RECEIVE_WAP_MESSAGES";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String REFRESH_USER_ACTION = "ohos.permission.REFRESH_USER_ACTION";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String REMOVE_CACHE_FILES = "ohos.permission.REMOVE_CACHE_FILES";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String REQUIRE_FORM = "ohos.permission.REQUIRE_FORM";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String RESET_BIOMETRIC_LOCKOUT = "ohos.permission.RESET_BIOMETRIC_LOCKOUT";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String REVOKE_SENSITIVE_PERMISSIONS = "ohos.permission.REVOKE_SENSITIVE_PERMISSIONS";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String RUNNING_LOCK = "ohos.permission.RUNNING_LOCK";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED, PermissionAvailableScope.RESTRICTED}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String SEND_MESSAGES = "ohos.permission.SEND_MESSAGES";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    @Deprecated
    public static final String SET_LIVEWALLPAPER = "ohos.permission.SET_LIVEWALLPAPER";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String SET_NETWORK_INFO = "ohos.permission.SET_NETWORK_INFO";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String SET_TELEPHONY_STATE = "ohos.permission.SET_TELEPHONY_STATE";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String SET_TIME = "ohos.permission.SET_TIME";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String SET_TIME_ZONE = "ohos.permission.SET_TIME_ZONE";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String SET_WALLPAPER = "ohos.permission.SET_WALLPAPER";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String SET_WALLPAPER_DIMENSION = "ohos.permission.SET_WALLPAPER_DIMENSION";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String SET_WIFI_CONFIG = "ohos.permission.SET_WIFI_CONFIG";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String SET_WIFI_INFO = "ohos.permission.SET_WIFI_INFO";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String SPREAD_STATUS_BAR = "ohos.permission.SPREAD_STATUS_BAR";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String START_ABILIIES_FROM_BACKGROUND = "ohos.permission.START_ABILIIES_FROM_BACKGROUND";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String START_INVISIBLE_ABILITY = "ohos.permission.START_INVISIBLE_ABILITY";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String SYSTEM_FLOAT_WINDOW = "ohos.permission.SYSTEM_FLOAT_WINDOW";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String UPDATE_CONFIGURATION = "ohos.permission.UPDATE_CONFIGURATION";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String USE_BLUETOOTH = "ohos.permission.USE_BLUETOOTH";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String USE_TRUSTCIRCLE_MANAGER = "ohos.permission.USE_TRUSTCIRCLE_MANAGER";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String USE_WHOLE_SCREEN = "ohos.permission.USE_WHOLE_SCREEN";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String VIBRATE = "ohos.permission.VIBRATE";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String WRITE_CALENDAR = "ohos.permission.WRITE_CALENDAR";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED, PermissionAvailableScope.RESTRICTED}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String WRITE_CALL_LOG = "ohos.permission.WRITE_CALL_LOG";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED, PermissionAvailableScope.RESTRICTED}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String WRITE_CONTACTS = "ohos.permission.WRITE_CONTACTS";
    @PermissionLevel(availableScope = {PermissionAvailableScope.ALL}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String WRITE_MEDIA = "ohos.permission.WRITE_MEDIA";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.SYSTEM_GRANT)
    public static final String WRITE_SCREEN_SAVER = "ohos.permission.WRITE_SCREEN_SAVER";
    @PermissionLevel(availableScope = {PermissionAvailableScope.SIGNATURE, PermissionAvailableScope.PRIVILEGED}, grantMode = PermissionGrantMode.USER_GRANT)
    public static final String WRITE_USER_STORAGE = "ohos.permission.WRITE_USER_STORAGE";

    private SystemPermission() {
    }
}
