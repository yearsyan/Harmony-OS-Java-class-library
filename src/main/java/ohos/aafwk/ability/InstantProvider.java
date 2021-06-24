package ohos.aafwk.ability;

import java.io.FileDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.app.Context;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.MessageParcel;
import ohos.utils.Ashmem;
import ohos.utils.Pair;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;
import ohos.utils.fastjson.JSONException;
import ohos.utils.fastjson.JSONObject;
import ohos.utils.zson.ZSONObject;

public class InstantProvider implements Sequenceable {
    private static final String ABILITY_NAME_KEY = "abilityName";
    private static final String BUNDLE_NAME_KEY = "bundleName";
    private static final int IMAGE_DATA_STATE_ADDED = 1;
    private static final int IMAGE_DATA_STATE_NO_OPERATION = 0;
    private static final int IMAGE_DATA_STATE_REMOVED = -1;
    private static final String INSTANT_COMPONENT_CLASS_NAME = "ohos.ace.ability.InstantComponent";
    private static final Object LOCK = new Object();
    private static final int LOG_DOMAIN = 218118416;
    private static final String LOG_FORMAT = "%{public}s: %{private}s";
    private static final HiLogLabel LOG_LABEL = new HiLogLabel(3, LOG_DOMAIN, "Ace");
    private static final int MESSAGE_EVENT = 101;
    private static final String PARAMS_KEY = "params";
    private static final String PARAM_MESSAGE_KEY = "ohos.extra.param.key.message";
    public static final Sequenceable.Producer<InstantProvider> PRODUCER = $$Lambda$InstantProvider$zrfQOJPISc9lIg7UTGe_hs3YyyU.INSTANCE;
    private static final int ROUTER_EVENT = 100;
    private static final String TAG = "InstantProvider";
    private static volatile Method destroy;
    private static volatile Class<?> instantComponentClass;
    private static volatile Constructor<?> instantComponentConstructor;
    private static volatile boolean isReflectionsInitialized = false;
    private static volatile Method render;
    private static volatile Method renderWithWH;
    private static volatile Method setEventHandler;
    private static volatile Method update;
    private EventHandler abilityHandler = null;
    private int[] byteLenArray;
    private Component component;
    private FileDescriptor[] fdArray;
    private FormBindingData formBindingData = new FormBindingData();
    private boolean hasUpgrade = false;
    private int height = 0;
    private Map<String, Pair<FileDescriptor, Integer>> imageDataMap;
    private boolean isInitializedData = false;
    private String jsFormCodePath = "";
    private String jsFormModuleName = "";
    private String[] picNameArray;
    private int width = 0;

    @Override // ohos.utils.Sequenceable
    public boolean hasFileDescriptor() {
        return false;
    }

    static /* synthetic */ InstantProvider lambda$static$0(Parcel parcel) {
        InstantProvider instantProvider = new InstantProvider();
        instantProvider.unmarshalling(parcel);
        return instantProvider;
    }

    public InstantProvider() {
    }

    public InstantProvider(String str, String str2) {
        this.jsFormCodePath = str2;
        this.jsFormModuleName = str;
    }

    public FormBindingData getFormBindingData() {
        return this.formBindingData;
    }

    public void setFormBindingData(FormBindingData formBindingData2) {
        if (formBindingData2 != null) {
            this.formBindingData = formBindingData2;
            this.isInitializedData = true;
        }
    }

    public void mergeFormBindingData(FormBindingData formBindingData2) {
        if (formBindingData2 != null) {
            this.formBindingData.mergeData(ZSONObject.stringToZSON(formBindingData2.getDataString()));
            this.formBindingData.setImageDataState(formBindingData2.getImageDataState());
            this.formBindingData.setImageDataMap(formBindingData2.getImageDataMap());
            this.isInitializedData = true;
        }
    }

    public void resetFormBindingData() {
        this.isInitializedData = false;
        this.formBindingData = new FormBindingData();
    }

    public boolean isInitializedData() {
        return this.isInitializedData;
    }

    public void setComponent(Component component2) {
        this.component = component2;
    }

    public void setFormSize(int i, int i2) {
        this.width = i;
        this.height = i2;
    }

    public Component getInstantComponent(Context context) throws InstantProviderException {
        Component component2 = this.component;
        if (component2 != null) {
            return component2;
        }
        if (context != null) {
            try {
                initializeReflections();
                Object newInstance = instantComponentConstructor.newInstance(context, this.jsFormCodePath, this.jsFormModuleName);
                if (newInstance instanceof Component) {
                    if (renderWithWH != null) {
                        generateArraysForImageData();
                        renderWithWH.invoke(newInstance, this.formBindingData.getDataString(), Integer.valueOf(this.width), Integer.valueOf(this.height), this.picNameArray, this.byteLenArray, this.fdArray);
                    } else {
                        render.invoke(newInstance, this.formBindingData.getDataString());
                    }
                    this.component = (Component) newInstance;
                    return this.component;
                }
                throw new InstantProviderException("the new instant is not component");
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                throw new InstantProviderException("getInstantComponent() exception, error message: " + e.getMessage());
            }
        } else {
            throw new InstantProviderException("context is null");
        }
    }

    public Component upgrade(Context context) throws InstantProviderException {
        try {
            initializeReflections();
            Object newInstance = instantComponentConstructor.newInstance(context, this.jsFormCodePath, this.jsFormModuleName);
            if (newInstance instanceof Component) {
                renderWithWH.invoke(newInstance, this.formBindingData.getDataString(), Integer.valueOf(this.width), Integer.valueOf(this.height), this.picNameArray, this.byteLenArray, this.fdArray);
                this.component = (Component) newInstance;
                return this.component;
            }
            throw new InstantProviderException("the new instant is not component when upgrade");
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new InstantProviderException("upgrade exception, error message: " + e.getMessage());
        }
    }

    public void update() {
        if (this.component != null) {
            try {
                initializeReflections();
                generateArraysForImageData();
                update.invoke(this.component, this.formBindingData.getDataString(), this.picNameArray, this.byteLenArray, this.fdArray);
                closeFileDescriptorMap();
            } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                HiLogLabel hiLogLabel = LOG_LABEL;
                HiLog.error(hiLogLabel, LOG_FORMAT, TAG, "update() exception, error message" + e.getMessage());
            }
        }
    }

    private void closeFileDescriptorMap() {
        Map<String, Pair<FileDescriptor, Integer>> map = this.imageDataMap;
        if (!(map == null || map.isEmpty())) {
            for (Map.Entry<String, Pair<FileDescriptor, Integer>> entry : this.imageDataMap.entrySet()) {
                F f = entry.getValue().f;
                if (f != null) {
                    MessageParcel.closeFileDescriptor(f);
                }
            }
            HiLogLabel hiLogLabel = LOG_LABEL;
            HiLog.info(hiLogLabel, LOG_FORMAT, TAG, "Done close file descriptors in map, number of fd closed is " + this.imageDataMap.size());
            this.imageDataMap.clear();
            this.fdArray = null;
            this.byteLenArray = null;
            this.picNameArray = null;
        }
    }

    private void generateArraysForImageData() {
        Map<String, Pair<FileDescriptor, Integer>> map = this.imageDataMap;
        if (map != null) {
            int size = map.size();
            this.picNameArray = new String[size];
            this.fdArray = new FileDescriptor[size];
            this.byteLenArray = new int[size];
            int i = 0;
            for (Map.Entry<String, Pair<FileDescriptor, Integer>> entry : this.imageDataMap.entrySet()) {
                this.picNameArray[i] = entry.getKey();
                this.fdArray[i] = entry.getValue().f;
                this.byteLenArray[i] = entry.getValue().s.intValue();
                i++;
            }
        }
    }

    public void destroy() {
        if (this.component != null) {
            try {
                initializeReflections();
                destroy.invoke(this.component, new Object[0]);
            } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                HiLogLabel hiLogLabel = LOG_LABEL;
                HiLog.error(hiLogLabel, LOG_FORMAT, TAG, "destroy() exception, error message: " + e.getMessage());
            }
        }
    }

    public void setAbilityHandler(EventHandler eventHandler) {
        this.abilityHandler = eventHandler;
    }

    public EventHandler getAbilityHandler() {
        return this.abilityHandler;
    }

    public void setEventHandler() {
        if (this.component == null) {
            HiLog.error(LOG_LABEL, LOG_FORMAT, TAG, "fail to setEventHandler due to view is null");
            return;
        }
        try {
            AnonymousClass1 r0 = new EventHandler(EventRunner.current()) {
                /* class ohos.aafwk.ability.InstantProvider.AnonymousClass1 */

                @Override // ohos.eventhandler.EventHandler
                public void processEvent(InnerEvent innerEvent) {
                    super.processEvent(innerEvent);
                    if (innerEvent != null && (innerEvent.object instanceof String)) {
                        int i = innerEvent.eventId;
                        String str = (String) innerEvent.object;
                        if (i == 100) {
                            try {
                                HiLog.debug(InstantProvider.LOG_LABEL, InstantProvider.LOG_FORMAT, InstantProvider.TAG, "event type is router event");
                                InnerEvent innerEvent2 = InnerEvent.get(100, 0, JSONObject.parseObject(str));
                                if (InstantProvider.this.abilityHandler != null) {
                                    InstantProvider.this.abilityHandler.sendEvent(innerEvent2);
                                }
                            } catch (JSONException e) {
                                HiLogLabel hiLogLabel = InstantProvider.LOG_LABEL;
                                HiLog.error(hiLogLabel, InstantProvider.LOG_FORMAT, InstantProvider.TAG, "parse param failed, err: " + e.getMessage());
                            }
                        } else if (i == 101) {
                            try {
                                HiLog.debug(InstantProvider.LOG_LABEL, InstantProvider.LOG_FORMAT, InstantProvider.TAG, "event type is message event");
                                String string = JSONObject.parseObject(str).getString(InstantProvider.PARAMS_KEY);
                                Intent intent = new Intent();
                                intent.setParam(InstantProvider.PARAM_MESSAGE_KEY, string);
                                InnerEvent innerEvent3 = InnerEvent.get(101, 0, intent);
                                if (InstantProvider.this.abilityHandler != null) {
                                    InstantProvider.this.abilityHandler.sendEvent(innerEvent3);
                                }
                            } catch (JSONException e2) {
                                HiLogLabel hiLogLabel2 = InstantProvider.LOG_LABEL;
                                HiLog.error(hiLogLabel2, InstantProvider.LOG_FORMAT, InstantProvider.TAG, "parse param failed, err: " + e2.getMessage());
                            }
                        } else {
                            HiLog.error(InstantProvider.LOG_LABEL, InstantProvider.LOG_FORMAT, InstantProvider.TAG, "wrong param of form event");
                        }
                    }
                }
            };
            initializeReflections();
            setEventHandler.invoke(this.component, r0);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException unused) {
            HiLog.error(LOG_LABEL, LOG_FORMAT, TAG, "fail to invoke setEventHandler method");
        }
    }

    @Override // ohos.utils.Sequenceable
    public boolean marshalling(Parcel parcel) {
        parcel.writeString(this.jsFormModuleName);
        parcel.writeString(this.jsFormCodePath);
        parcel.writeBoolean(this.isInitializedData);
        parcel.writeBoolean(this.hasUpgrade);
        parcel.writeString(this.formBindingData.getDataString());
        if (!(parcel instanceof MessageParcel)) {
            return true;
        }
        int imageDataState = this.formBindingData.getImageDataState();
        parcel.writeInt(imageDataState);
        if (!(imageDataState == -1 || imageDataState == 0)) {
            if (imageDataState != 1) {
                HiLogLabel hiLogLabel = LOG_LABEL;
                HiLog.warn(hiLogLabel, LOG_FORMAT, TAG, "unexpected imageDataState: " + imageDataState);
            } else {
                try {
                    Map<String, Pair<Ashmem, Integer>> imageDataMap2 = this.formBindingData.getImageDataMap();
                    parcel.writeInt(imageDataMap2.size());
                    for (Map.Entry<String, Pair<Ashmem, Integer>> entry : imageDataMap2.entrySet()) {
                        if (!((MessageParcel) parcel).writeAshmem(entry.getValue().f)) {
                            HiLog.error(LOG_LABEL, LOG_FORMAT, TAG, "writeAshmem fail");
                            return false;
                        }
                        parcel.writeString(entry.getKey());
                    }
                    closeAshmemMapInFormBindingData();
                } finally {
                    closeAshmemMapInFormBindingData();
                }
            }
        }
        return true;
    }

    @Override // ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        int readInt;
        this.jsFormModuleName = parcel.readString();
        this.jsFormCodePath = parcel.readString();
        this.isInitializedData = parcel.readBoolean();
        this.hasUpgrade = parcel.readBoolean();
        this.formBindingData = new FormBindingData(parcel.readString());
        if (!(!(parcel instanceof MessageParcel) || (readInt = parcel.readInt()) == -1 || readInt == 0)) {
            if (readInt != 1) {
                HiLogLabel hiLogLabel = LOG_LABEL;
                HiLog.warn(hiLogLabel, LOG_FORMAT, TAG, "unexpected imageDataState: " + readInt);
            } else {
                int readInt2 = parcel.readInt();
                if (this.imageDataMap == null) {
                    this.imageDataMap = new HashMap();
                }
                for (int i = 0; i < readInt2; i++) {
                    this.imageDataMap.put(parcel.readString(), Pair.create(((MessageParcel) parcel).readFileDescriptor(), Integer.valueOf(parcel.readInt())));
                }
            }
        }
        return true;
    }

    private static void initializeReflections() throws ClassNotFoundException, NoSuchMethodException {
        if (!isReflectionsInitialized) {
            synchronized (LOCK) {
                if (!isReflectionsInitialized) {
                    instantComponentClass = Class.forName(INSTANT_COMPONENT_CLASS_NAME);
                    try {
                        renderWithWH = instantComponentClass.getDeclaredMethod("render", String.class, Integer.TYPE, Integer.TYPE, String[].class, int[].class, FileDescriptor[].class);
                    } catch (NoSuchMethodException unused) {
                        HiLog.warn(LOG_LABEL, LOG_FORMAT, TAG, "Failed to reflect new interface of render, choose old one");
                        render = instantComponentClass.getDeclaredMethod("render", String.class);
                    }
                    update = instantComponentClass.getDeclaredMethod("updateInstantData", String.class, String[].class, int[].class, FileDescriptor[].class);
                    destroy = instantComponentClass.getDeclaredMethod("destroy", new Class[0]);
                    setEventHandler = instantComponentClass.getDeclaredMethod("setEventHandler", EventHandler.class);
                    instantComponentConstructor = instantComponentClass.getConstructor(Context.class, String.class, String.class);
                    isReflectionsInitialized = true;
                }
            }
        }
    }

    private void closeAshmemMapInFormBindingData() {
        Map<String, Pair<Ashmem, Integer>> imageDataMap2 = this.formBindingData.getImageDataMap();
        if (!(imageDataMap2 == null || imageDataMap2.isEmpty())) {
            for (Map.Entry<String, Pair<Ashmem, Integer>> entry : imageDataMap2.entrySet()) {
                F f = entry.getValue().f;
                if (f != null) {
                    f.closeAshmem();
                }
            }
            imageDataMap2.clear();
        }
    }

    public void setUpgradeFlag(boolean z) {
        this.hasUpgrade = z;
    }

    public boolean hasUpgrade() {
        return this.hasUpgrade;
    }
}
