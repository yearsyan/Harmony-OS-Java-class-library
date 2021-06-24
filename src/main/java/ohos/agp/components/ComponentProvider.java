package ohos.agp.components;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.ComponentProvider;
import ohos.agp.components.Text;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ElementScatter;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.bundle.ApplicationInfo;
import ohos.event.intentagent.IntentAgent;
import ohos.event.intentagent.IntentAgentHelper;
import ohos.global.resource.NotExistException;
import ohos.global.resource.ResourceManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.PixelMap;
import ohos.rpc.MessageParcel;
import ohos.utils.Parcel;
import ohos.utils.Sequenceable;

public class ComponentProvider implements Sequenceable {
    private static final int ACTION_COUNT_MAX = 1000;
    public static final int APPLY_TYPE_ACTIONS_ONLY = 2;
    public static final int APPLY_TYPE_LAYOUT_AND_ACTIONS = 1;
    private static final int DYNAMIC_ACTION_TAG = 1;
    private static final int HAVE_DATA_FLAG = 1;
    private static final HiLogLabel LABEL = new HiLogLabel(3, LogDomain.END, "ComponentProvider");
    private static final int MAX_PIXEL_MAP_CACHE_SIZE = 104857600;
    private static final int NO_DATA_FLAG = 0;
    private static final int ON_CLICK_ACTION_TAG = 8;
    private static final int SET_IMAGE_RESOURCE_ACTION_TAG = 10;
    private static final int SET_PADDING_ACTION_TAG = 5;
    private static final int SET_PIXEL_MAP_ACTION_TAG = 7;
    private static final int SET_PROGRESSBAR_ACTION_TAG = 4;
    private static final int SET_TEXT_COLOR_ACTION_TAG = 3;
    private static final int SET_TEXT_SIZE_ACTION_MARSHALLING_INT_COUNT = 3;
    private static final int SET_TEXT_SIZE_ACTION_TAG = 9;
    private static final int SET_VIEW_GROUP_MARGIN_LAYOUT_PARAM_ACTION_TAG = 6;
    private final LinkedHashMap<String, Action> mActionsMap = new LinkedHashMap<>();
    private ApplicationInfo mApplicationInfo;
    private int mApplyType = 1;
    private String mBundleName;
    private Context mContext;
    private String mDefaultBundleName;
    private int mLayoutXMLId = 0;
    private PixelMapCache mPixelMapCache;
    private ComponentContainer mRemoteLayout;

    public ComponentProvider() {
        HiLog.debug(LABEL, "enter ComponentProvider", new Object[0]);
        this.mPixelMapCache = new PixelMapCache();
    }

    public ComponentProvider(String str) {
        HiLog.debug(LABEL, "enter ComponentProvider", new Object[0]);
        this.mApplicationInfo = null;
        this.mPixelMapCache = new PixelMapCache();
    }

    public ComponentProvider(int i, Context context) {
        HiLog.debug(LABEL, "enter ComponentProvider", new Object[0]);
        if (i != 0) {
            this.mLayoutXMLId = i;
            this.mBundleName = context.getBundleName();
            this.mApplicationInfo = context.getApplicationInfo();
            this.mContext = context;
            this.mPixelMapCache = new PixelMapCache();
            return;
        }
        throw new ComponentProviderException("layoutId is zero!");
    }

    public boolean setApplyType(int i) {
        HiLog.debug(LABEL, "enter setApplyType, applyType:%{public}d", Integer.valueOf(i));
        if (i == 1 || i == 2) {
            this.mApplyType = i;
            return true;
        }
        HiLog.error(LABEL, "invalid apply type:%{public}d ", Integer.valueOf(i));
        return false;
    }

    public int getApplyType() {
        HiLog.debug(LABEL, "enter getApplyType", new Object[0]);
        return this.mApplyType;
    }

    public void setDefaultBundleName(String str) {
        this.mDefaultBundleName = str;
    }

    public String getDefaultBundleName() {
        return this.mDefaultBundleName;
    }

    public boolean isValidComponentId(int i) {
        HiLog.debug(LABEL, "enter isValidComponentId, componentId:%{public}d", Integer.valueOf(i));
        ComponentContainer allComponents = getAllComponents();
        if (allComponents == null) {
            HiLog.error(LABEL, "component group is null", new Object[0]);
            return false;
        } else if (allComponents.findComponentById(i) != null) {
            return true;
        } else {
            HiLog.error(LABEL, "invalid component id:%{public}d ", Integer.valueOf(i));
            return false;
        }
    }

    @Override // ohos.utils.Sequenceable
    public boolean marshalling(Parcel parcel) {
        if (parcel == null) {
            HiLog.error(LABEL, "marshalling out is null.", new Object[0]);
            return false;
        }
        HiLog.debug(LABEL, "start marshalling out.getCapacity()=%d", Integer.valueOf(parcel.getCapacity()));
        if (!(parcel instanceof MessageParcel)) {
            HiLog.error(LABEL, "ComponentProvider marshalling error, the output parcel is not instance of MessageParcel", new Object[0]);
            return false;
        }
        parcel.writeInt(this.mApplyType);
        if (this.mApplyType == 1 && !marshallingLayoutType(parcel)) {
            HiLog.error(LABEL, "marshallingLayoutType error.", new Object[0]);
            return false;
        } else if (!this.mPixelMapCache.marshalling(parcel)) {
            HiLog.error(LABEL, "marshalling PixelMapCache error.", new Object[0]);
            return false;
        } else if (!writeActionsToParcel(parcel)) {
            HiLog.error(LABEL, "writeActionsToParcel error.", new Object[0]);
            return false;
        } else {
            HiLog.debug(LABEL, "marshalling succeed", new Object[0]);
            return true;
        }
    }

    private boolean marshallingLayoutType(Parcel parcel) {
        if (this.mApplicationInfo == null) {
            HiLog.error(LABEL, "marshalling failed for layout is invalid!", new Object[0]);
            return false;
        } else if (marshallingAppInfo(parcel)) {
            return true;
        } else {
            HiLog.error(LABEL, "marshalling application info failed!", new Object[0]);
            return false;
        }
    }

    private boolean marshallingAppInfo(Parcel parcel) {
        if (!parcel.writeInt(this.mLayoutXMLId)) {
            HiLog.error(LABEL, "marshalling application info failed: xml id", new Object[0]);
            return false;
        } else if (!this.mApplicationInfo.marshalling(parcel)) {
            HiLog.error(LABEL, "marshalling application info failed: info", new Object[0]);
            return false;
        } else if (parcel.writeString(this.mBundleName)) {
            return true;
        } else {
            HiLog.error(LABEL, "marshalling application info failed: bundle name", new Object[0]);
            return false;
        }
    }

    @Override // ohos.utils.Sequenceable
    public boolean unmarshalling(Parcel parcel) {
        if (parcel == null) {
            HiLog.error(LABEL, "unmarshalling in is null.", new Object[0]);
            return false;
        }
        HiLog.debug(LABEL, "start unmarshalling %{public}d", Integer.valueOf(parcel.getSize()));
        if (!(parcel instanceof MessageParcel)) {
            HiLog.error(LABEL, "ComponentProvider marshalling error, the input parcel is not instance of MessageParcel", new Object[0]);
            return false;
        }
        this.mApplyType = parcel.readInt();
        if (this.mApplyType == 1) {
            this.mLayoutXMLId = parcel.readInt();
            if (!unmarshallingAppInfo(parcel)) {
                HiLog.error(LABEL, "unmarshalling application info failed", new Object[0]);
                return false;
            }
        }
        this.mPixelMapCache = new PixelMapCache(parcel);
        return readActionsFromParcel(parcel);
    }

    private boolean unmarshallingAppInfo(Parcel parcel) {
        this.mApplicationInfo = new ApplicationInfo();
        if (!this.mApplicationInfo.unmarshalling(parcel)) {
            return false;
        }
        this.mBundleName = parcel.readString();
        return true;
    }

    private boolean writeActionsToParcel(Parcel parcel) {
        HiLog.debug(LABEL, "try write %{public}d actions", Integer.valueOf(this.mActionsMap.size()));
        parcel.writeInt(this.mActionsMap.size());
        for (Map.Entry<String, Action> entry : this.mActionsMap.entrySet()) {
            Action value = entry.getValue();
            if (!parcel.writeInt(value.getActionTag()) || !value.marshalling(parcel)) {
                HiLog.error(LABEL, "marshalling action %{public}d failed!", Integer.valueOf(value.getActionTag()));
                return false;
            }
            HiLog.debug(LABEL, "marshalling action %{public}s ok", value.getActionKey());
        }
        return true;
    }

    private boolean readActionsFromParcel(Parcel parcel) {
        int readInt = parcel.readInt();
        if (readInt > 1000) {
            HiLog.error(LABEL, "read action count from parcel overflows", new Object[0]);
            return false;
        }
        HiLog.debug(LABEL, "try read %{public}d actions", Integer.valueOf(readInt));
        for (int i = 0; i < readInt; i++) {
            try {
                mergeAction(getActionFromParcel(parcel));
            } catch (ComponentProviderException unused) {
                HiLog.error(LABEL, "read action from parcel failed", new Object[0]);
                return false;
            }
        }
        return true;
    }

    public void inflateLayout(Context context) {
        HiLog.info(LABEL, "enter inflateLayout by context", new Object[0]);
        if (this.mApplicationInfo == null) {
            HiLog.error(LABEL, "application info is null", new Object[0]);
        } else if (this.mLayoutXMLId == 0) {
            HiLog.error(LABEL, "layoutId is zero", new Object[0]);
        } else if (context == null) {
            HiLog.error(LABEL, "context is null", new Object[0]);
        } else {
            Context createBundleContext = context.createBundleContext(this.mBundleName, 3);
            if (createBundleContext == null) {
                HiLog.error(LABEL, "remote context is null", new Object[0]);
                return;
            }
            ResourceManager resourceManager = createBundleContext.getResourceManager();
            if (resourceManager == null) {
                HiLog.error(LABEL, "remoteResourceManager is null", new Object[0]);
                return;
            }
            Component parse = LayoutScatter.getInstance(context).clone(createBundleContext, resourceManager).parse(this.mLayoutXMLId, null, false);
            if (parse instanceof ComponentContainer) {
                this.mRemoteLayout = (ComponentContainer) parse;
            } else {
                HiLog.error(LABEL, "inflate layout fail", new Object[0]);
            }
            HiLog.debug(LABEL, "inflateLayout ok", new Object[0]);
        }
    }

    public ComponentContainer getAllComponents() {
        HiLog.debug(LABEL, "enter getAllViews", new Object[0]);
        return this.mRemoteLayout;
    }

    public void applyAction(ComponentContainer componentContainer) {
        HiLog.debug(LABEL, "enter applyAction", new Object[0]);
        if (componentContainer == null) {
            HiLog.error(LABEL, "applyAction input is null", new Object[0]);
            return;
        }
        for (Map.Entry<String, Action> entry : this.mActionsMap.entrySet()) {
            entry.getValue().apply(componentContainer);
        }
    }

    private Action getActionFromParcel(Parcel parcel) {
        int readInt = parcel.readInt();
        switch (readInt) {
            case 1:
                return new DynamicAction(parcel);
            case 2:
            default:
                throw new ComponentProviderException("Tag " + readInt + " not found");
            case 3:
                return new SetTextColorAction(parcel);
            case 4:
                return new SetProgressBarAction(parcel);
            case 5:
                return new SetPaddingAction(parcel);
            case 6:
                return new ComponentContainerLayoutAction(parcel);
            case 7:
                return new SetPixelMapAction(parcel);
            case 8:
                return new OnClickAction(parcel);
            case 9:
                return new SetTextSizeAction(parcel);
            case 10:
                return new SetImageContentAction(parcel);
        }
    }

    public Collection<Action> getActions() {
        HiLog.debug(LABEL, "enter getActions", new Object[0]);
        return this.mActionsMap.values();
    }

    public int getLayoutId() {
        HiLog.debug(LABEL, "enter getLayoutId", new Object[0]);
        return this.mLayoutXMLId;
    }

    public void mergeActions(Collection<Action> collection) {
        HiLog.debug(LABEL, "enter mergeActions", new Object[0]);
        for (Action action : collection) {
            mergeAction(action);
        }
    }

    public Action setTextSize(int i, int i2) {
        HiLog.debug(LABEL, "enter setTextSize", new Object[0]);
        return setTextSize(i, i2, Text.TextSizeType.PX);
    }

    public Action setTextSize(int i, int i2, Text.TextSizeType textSizeType) {
        HiLog.debug(LABEL, "enter setTextSize with textSizeType", new Object[0]);
        SetTextSizeAction setTextSizeAction = new SetTextSizeAction(i, i2, textSizeType.textSizeTypeValue());
        mergeAction(setTextSizeAction);
        return setTextSizeAction;
    }

    public Action setTextColor(int i, Color color) {
        HiLog.debug(LABEL, "enter setTextColor", new Object[0]);
        SetTextColorAction setTextColorAction = new SetTextColorAction(i, color);
        mergeAction(setTextColorAction);
        return setTextColorAction;
    }

    public Action setTextAlignment(int i, int i2) {
        HiLog.debug(LABEL, "enter setTextAlignment", new Object[0]);
        return setInt(i, "setTextAlignment", i2);
    }

    public Action setComponentContainerLayoutConfig(int i, ComponentContainer.LayoutConfig layoutConfig) {
        HiLog.debug(LABEL, "enter setViewGroupMarginLayoutParams", new Object[0]);
        ComponentContainerLayoutAction componentContainerLayoutAction = new ComponentContainerLayoutAction(i, layoutConfig);
        mergeAction(componentContainerLayoutAction);
        return componentContainerLayoutAction;
    }

    public Action setVisibility(int i, int i2) {
        HiLog.debug(LABEL, "enter setVisibility", new Object[0]);
        return setInt(i, "setVisibility", i2);
    }

    public Action setProgressBar(int i, int i2, int i3, boolean z) {
        HiLog.debug(LABEL, "enter setProgressBar", new Object[0]);
        SetProgressBarAction setProgressBarAction = new SetProgressBarAction(i, i2, i3, z);
        mergeAction(setProgressBarAction);
        return setProgressBarAction;
    }

    public Action setPadding(int i, int i2, int i3, int i4, int i5) {
        HiLog.debug(LABEL, "enter setPadding", new Object[0]);
        SetPaddingAction setPaddingAction = new SetPaddingAction(i, i2, i3, i4, i5);
        mergeAction(setPaddingAction);
        return setPaddingAction;
    }

    public Action setText(int i, String str) {
        HiLog.debug(LABEL, "enter setText", new Object[0]);
        return setString(i, "setText", str);
    }

    public Action setAccessibilityDescription(int i, String str) {
        HiLog.debug(LABEL, "enter setAccessibilityDescription", new Object[0]);
        return setString(i, "setAccessibilityDescription", str);
    }

    public void mergeAction(Action action) {
        HiLog.debug(LABEL, "enter mergeAction", new Object[0]);
        if (action == null) {
            HiLog.error(LABEL, "mergeAction fail, action is null", new Object[0]);
        } else if (action.mergeOperation() == 0) {
            this.mActionsMap.put(action.getActionKey(), action);
        }
    }

    public void resetActions() {
        HiLog.debug(LABEL, "enter resetActions", new Object[0]);
        this.mActionsMap.clear();
    }

    public Action setInt(int i, String str, int i2) {
        HiLog.debug(LABEL, "enter setInt", new Object[0]);
        DynamicAction dynamicAction = new DynamicAction(i, str, 1, Integer.valueOf(i2));
        mergeAction(dynamicAction);
        return dynamicAction;
    }

    public Action setString(int i, String str, String str2) {
        HiLog.debug(LABEL, "enter setString", new Object[0]);
        DynamicAction dynamicAction = new DynamicAction(i, str, 2, str2);
        mergeAction(dynamicAction);
        return dynamicAction;
    }

    public Action setFloat(int i, String str, float f) {
        HiLog.debug(LABEL, "enter setFloat", new Object[0]);
        DynamicAction dynamicAction = new DynamicAction(i, str, 3, Float.valueOf(f));
        mergeAction(dynamicAction);
        return dynamicAction;
    }

    public Action setDouble(int i, String str, double d) {
        HiLog.debug(LABEL, "enter setDouble", new Object[0]);
        DynamicAction dynamicAction = new DynamicAction(i, str, 5, Double.valueOf(d));
        mergeAction(dynamicAction);
        return dynamicAction;
    }

    public Action setLong(int i, String str, long j) {
        HiLog.debug(LABEL, "enter setLong", new Object[0]);
        DynamicAction dynamicAction = new DynamicAction(i, str, 6, Long.valueOf(j));
        mergeAction(dynamicAction);
        return dynamicAction;
    }

    public Action setBoolean(int i, String str, boolean z) {
        HiLog.debug(LABEL, "enter setBoolean", new Object[0]);
        DynamicAction dynamicAction = new DynamicAction(i, str, 7, Boolean.valueOf(z));
        mergeAction(dynamicAction);
        return dynamicAction;
    }

    public Action setPixelMap(int i, String str, PixelMap pixelMap) {
        HiLog.debug(LABEL, "enter setPixelMap", new Object[0]);
        SetPixelMapAction setPixelMapAction = new SetPixelMapAction(i, str, pixelMap);
        mergeAction(setPixelMapAction);
        return setPixelMapAction;
    }

    public Action setImagePixelMap(int i, PixelMap pixelMap) {
        HiLog.debug(LABEL, "enter setImagePixelMap", new Object[0]);
        return setPixelMap(i, "setPixelMap", pixelMap);
    }

    public Action setImageContent(int i, int i2) {
        HiLog.debug(LABEL, "enter setImageContent", new Object[0]);
        SetImageContentAction setImageContentAction = new SetImageContentAction(i, i2);
        mergeAction(setImageContentAction);
        return setImageContentAction;
    }

    public Action setBackgroundPixelMap(int i, PixelMap pixelMap) {
        HiLog.debug(LABEL, "enter setBackgroundPixelMap", new Object[0]);
        return setPixelMap(i, "setBackground", pixelMap);
    }

    public Action setIntentAgent(int i, IntentAgent intentAgent) {
        HiLog.debug(LABEL, "enter setIntentAgent", new Object[0]);
        OnClickAction onClickAction = new OnClickAction(i, intentAgent);
        mergeAction(onClickAction);
        return onClickAction;
    }

    /* access modifiers changed from: private */
    public static final class SetImageContentAction extends Action {
        int resId;

        @Override // ohos.agp.components.ComponentProvider.Action
        public int getActionTag() {
            return 10;
        }

        SetImageContentAction(int i, int i2) {
            this.viewId = i;
            this.resId = i2;
        }

        SetImageContentAction(Parcel parcel) {
            unmarshalling(parcel);
        }

        @Override // ohos.utils.Sequenceable
        public boolean marshalling(Parcel parcel) {
            return parcel.writeInt(this.resId) & parcel.writeInt(this.viewId);
        }

        @Override // ohos.utils.Sequenceable
        public boolean unmarshalling(Parcel parcel) {
            this.viewId = parcel.readInt();
            this.resId = parcel.readInt();
            return true;
        }

        @Override // ohos.agp.components.ComponentProvider.Action
        public void apply(ComponentContainer componentContainer) throws ComponentProviderException {
            Component findComponentById = componentContainer.findComponentById(this.viewId);
            if (findComponentById == null) {
                HiLog.error(ComponentProvider.LABEL, "SetImageContentAction apply error component is null.", new Object[0]);
            } else if (findComponentById instanceof Image) {
                ResourceManager resourceManager = findComponentById.getResourceManager();
                if (resourceManager == null) {
                    HiLog.error(ComponentProvider.LABEL, "SetImageContentAction apply error: Get res resourceManager failed.", new Object[0]);
                    return;
                }
                try {
                    String identifier = resourceManager.getIdentifier(this.resId);
                    if (identifier == null) {
                        HiLog.error(ComponentProvider.LABEL, "SetImageContentAction apply error: Get res identifier failed.", new Object[0]);
                    } else if (identifier.startsWith("media")) {
                        ((Image) findComponentById).setPixelMap(this.resId);
                    } else if (identifier.startsWith("graphic")) {
                        Element parse = ElementScatter.getInstance(findComponentById.getContext()).parse(this.resId);
                        if (parse != null) {
                            ((Image) findComponentById).setImageElement(parse);
                        }
                    } else {
                        HiLog.error(ComponentProvider.LABEL, "SetImageContentAction apply error: Unknown graphic resource", new Object[0]);
                    }
                } catch (IOException | NotExistException unused) {
                    HiLog.error(ComponentProvider.LABEL, "SetImageContentAction apply error: Get resId=%d identifier exception.", Integer.valueOf(this.resId));
                }
            } else {
                HiLog.error(ComponentProvider.LABEL, "SetImageContentAction apply error: Component is not instance of Image.", new Object[0]);
            }
        }
    }

    /* access modifiers changed from: private */
    public final class SetPixelMapAction extends Action {
        String methodName;
        PixelMap pixelMap;
        int pixelMapId;

        @Override // ohos.agp.components.ComponentProvider.Action
        public int getActionTag() {
            return 7;
        }

        @Override // ohos.agp.components.ComponentProvider.Action
        public int mergeOperation() {
            return 0;
        }

        SetPixelMapAction(int i, String str, PixelMap pixelMap2) {
            this.viewId = i;
            this.methodName = str;
            this.pixelMap = pixelMap2;
            HiLog.debug(ComponentProvider.LABEL, "SetPixelMapAction this.pixelMap=%{public}s", this.pixelMap);
            this.pixelMapId = ComponentProvider.this.mPixelMapCache.getPixelMapId(pixelMap2);
        }

        SetPixelMapAction(Parcel parcel) {
            unmarshalling(parcel);
            this.pixelMap = ComponentProvider.this.mPixelMapCache.getPixelMap(this.pixelMapId);
        }

        @Override // ohos.utils.Sequenceable
        public boolean marshalling(Parcel parcel) {
            return parcel.writeInt(this.pixelMapId) & parcel.writeInt(this.viewId) & parcel.writeString(this.methodName);
        }

        @Override // ohos.utils.Sequenceable
        public boolean unmarshalling(Parcel parcel) {
            this.viewId = parcel.readInt();
            this.methodName = parcel.readString();
            this.pixelMapId = parcel.readInt();
            return true;
        }

        @Override // ohos.agp.components.ComponentProvider.Action
        public void apply(ComponentContainer componentContainer) {
            Component findComponentById = componentContainer.findComponentById(this.viewId);
            if (findComponentById == null) {
                HiLog.error(ComponentProvider.LABEL, "Apply SetPixelMapAction error, found target id=%{public}d component is null.", Integer.valueOf(this.viewId));
            } else if ("setPixelMap".equals(this.methodName)) {
                try {
                    ComponentProvider.this.getReflectMethod(findComponentById, this.methodName, PixelMap.class);
                    PixelMap pixelMap2 = this.pixelMap;
                } catch (Throwable th) {
                    throw new ComponentProviderException(th);
                }
            } else if ("setBackground".equals(this.methodName)) {
                findComponentById.setBackground(new PixelMapElement(this.pixelMap));
            } else {
                HiLog.error(ComponentProvider.LABEL, "Apply SetPixelMapAction error, unknown method name.", new Object[0]);
            }
        }
    }

    /* access modifiers changed from: private */
    public static final class PixelMapCache implements Sequenceable {
        List<PixelMap> mPixelMaps;

        PixelMapCache() {
            this(null);
        }

        /* access modifiers changed from: package-private */
        public int getSize() {
            return this.mPixelMaps.size();
        }

        PixelMapCache(Parcel parcel) {
            this.mPixelMaps = new ArrayList();
            if (parcel != null) {
                HiLog.info(ComponentProvider.LABEL, "Construct PixelMapCache from parcel data.", new Object[0]);
                unmarshalling(parcel);
            }
        }

        /* access modifiers changed from: package-private */
        public int getPixelMapId(PixelMap pixelMap) {
            if (pixelMap == null) {
                return -1;
            }
            if (this.mPixelMaps.contains(pixelMap)) {
                return this.mPixelMaps.indexOf(pixelMap);
            }
            this.mPixelMaps.add(pixelMap);
            return this.mPixelMaps.size() - 1;
        }

        /* access modifiers changed from: package-private */
        public PixelMap getPixelMap(int i) {
            if (i < 0 || i >= this.mPixelMaps.size()) {
                HiLog.error(ComponentProvider.LABEL, "getPixelMap error invalid PixelMap id=%{public}d", Integer.valueOf(i));
                return null;
            }
            HiLog.info(ComponentProvider.LABEL, "getPixelMap id=%{public}d, pixelMap=%{public}s", Integer.valueOf(i), this.mPixelMaps.get(i));
            return this.mPixelMaps.get(i);
        }

        @Override // ohos.utils.Sequenceable
        public boolean marshalling(Parcel parcel) {
            if (!(parcel instanceof MessageParcel)) {
                HiLog.error(ComponentProvider.LABEL, "PixelMapCache marshalling error, output parcel is not instance of MessageParcel", new Object[0]);
                return false;
            }
            Parcel create = Parcel.create();
            create.setCapacity(ComponentProvider.MAX_PIXEL_MAP_CACHE_SIZE);
            boolean writeInt = create.writeInt(this.mPixelMaps.size());
            for (PixelMap pixelMap : this.mPixelMaps) {
                HiLog.debug(ComponentProvider.LABEL, "PixelMapCache marshalling pixelMap=%{public}s", pixelMap);
                create.writeInt(1);
                writeInt &= pixelMap.marshalling(create);
            }
            byte[] bytes = create.getBytes();
            boolean writeRawData = ((MessageParcel) parcel).writeRawData(bytes, bytes.length) & parcel.writeInt(bytes.length) & writeInt;
            create.reclaim();
            return writeRawData;
        }

        @Override // ohos.utils.Sequenceable
        public boolean unmarshalling(Parcel parcel) {
            if (!(parcel instanceof MessageParcel)) {
                HiLog.error(ComponentProvider.LABEL, "PixelMapCache unmarshalling error, input parcel is not instance of MessageParcel", new Object[0]);
                return false;
            }
            byte[] readRawData = ((MessageParcel) parcel).readRawData(parcel.readInt());
            Parcel create = Parcel.create();
            create.setCapacity(ComponentProvider.MAX_PIXEL_MAP_CACHE_SIZE);
            create.writeBytes(readRawData);
            int readInt = create.readInt();
            for (int i = 0; i < readInt; i++) {
                PixelMap createFromParcel = PixelMap.PRODUCER.createFromParcel(create);
                HiLog.debug(ComponentProvider.LABEL, "PixelMapCache unmarshalling pixelMap=%{public}s", createFromParcel);
                this.mPixelMaps.add(createFromParcel);
            }
            create.reclaim();
            return true;
        }
    }

    /* access modifiers changed from: private */
    public static final class SetTextColorAction extends Action {
        int color;

        @Override // ohos.agp.components.ComponentProvider.Action
        public int getActionTag() {
            return 3;
        }

        @Override // ohos.agp.components.ComponentProvider.Action
        public int mergeOperation() {
            return 0;
        }

        public SetTextColorAction(int i, Color color2) {
            this.viewId = i;
            this.color = color2.getValue();
        }

        public SetTextColorAction(Parcel parcel) {
            unmarshalling(parcel);
        }

        @Override // ohos.utils.Sequenceable
        public boolean marshalling(Parcel parcel) {
            return parcel.writeInt(this.viewId) && parcel.writeInt(this.color);
        }

        @Override // ohos.utils.Sequenceable
        public boolean unmarshalling(Parcel parcel) {
            this.viewId = parcel.readInt();
            this.color = parcel.readInt();
            return true;
        }

        @Override // ohos.agp.components.ComponentProvider.Action
        public void apply(ComponentContainer componentContainer) {
            Component findComponentById = componentContainer.findComponentById(this.viewId);
            if (findComponentById != null) {
                if (findComponentById instanceof Text) {
                    ((Text) findComponentById).setTextColor(new Color(this.color));
                    return;
                }
                HiLog.debug(ComponentProvider.LABEL, "component %{public}s is not TextView", Integer.valueOf(this.viewId));
            }
        }
    }

    /* access modifiers changed from: private */
    public static final class SetTextSizeAction extends Action {
        int size;
        int textSizeType;

        @Override // ohos.agp.components.ComponentProvider.Action
        public int getActionTag() {
            return 9;
        }

        @Override // ohos.agp.components.ComponentProvider.Action
        public int mergeOperation() {
            return 0;
        }

        public SetTextSizeAction(int i, int i2, int i3) {
            this.viewId = i;
            this.size = i2;
            this.textSizeType = i3;
        }

        public SetTextSizeAction(Parcel parcel) {
            unmarshalling(parcel);
        }

        @Override // ohos.utils.Sequenceable
        public boolean marshalling(Parcel parcel) {
            return parcel.writeInt(this.viewId) && parcel.writeInt(this.size) && parcel.writeInt(this.textSizeType);
        }

        @Override // ohos.utils.Sequenceable
        public boolean unmarshalling(Parcel parcel) {
            if (parcel.getSize() < 12) {
                HiLog.error(ComponentProvider.LABEL, "SetTextSizeAction unmarshalling with wrong input params", new Object[0]);
                return false;
            }
            this.viewId = parcel.readInt();
            this.size = parcel.readInt();
            this.textSizeType = parcel.readInt();
            return true;
        }

        @Override // ohos.agp.components.ComponentProvider.Action
        public void apply(ComponentContainer componentContainer) {
            Component findComponentById = componentContainer.findComponentById(this.viewId);
            if (findComponentById != null) {
                Text.TextSizeType textSizeType2 = Text.TextSizeType.getTextSizeType(this.textSizeType);
                if (textSizeType2 == null) {
                    HiLog.debug(ComponentProvider.LABEL, "component %{public}s has invalid TextSizeType: %{public}s", Integer.valueOf(this.viewId), Integer.valueOf(this.textSizeType));
                } else if (findComponentById instanceof Text) {
                    ((Text) findComponentById).setTextSize(this.size, textSizeType2);
                } else {
                    HiLog.debug(ComponentProvider.LABEL, "component %{public}s is not TextView", Integer.valueOf(this.viewId));
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public static final class ComponentContainerLayoutAction extends Action {
        int bottomMargin;
        int layoutHeight;
        int layoutWidth;
        int leftMargin;
        int rightMargin;
        int topMargin;

        @Override // ohos.agp.components.ComponentProvider.Action
        public int getActionTag() {
            return 6;
        }

        @Override // ohos.agp.components.ComponentProvider.Action
        public int mergeOperation() {
            return 0;
        }

        ComponentContainerLayoutAction(int i, ComponentContainer.LayoutConfig layoutConfig) {
            this.viewId = i;
            if (layoutConfig != null) {
                this.layoutWidth = layoutConfig.width;
                this.layoutHeight = layoutConfig.height;
                this.bottomMargin = layoutConfig.getMarginBottom();
                this.leftMargin = layoutConfig.getMarginLeft();
                this.rightMargin = layoutConfig.getMarginRight();
                this.topMargin = layoutConfig.getMarginTop();
                return;
            }
            this.layoutWidth = -1;
            this.layoutHeight = -1;
        }

        ComponentContainerLayoutAction(Parcel parcel) {
            unmarshalling(parcel);
        }

        @Override // ohos.utils.Sequenceable
        public boolean marshalling(Parcel parcel) {
            return parcel.writeInt(this.viewId) && parcel.writeInt(this.layoutWidth) && parcel.writeInt(this.layoutHeight) && parcel.writeInt(this.topMargin) && parcel.writeInt(this.bottomMargin) && parcel.writeInt(this.leftMargin) && parcel.writeInt(this.rightMargin);
        }

        @Override // ohos.utils.Sequenceable
        public boolean unmarshalling(Parcel parcel) {
            this.viewId = parcel.readInt();
            this.layoutWidth = parcel.readInt();
            this.layoutHeight = parcel.readInt();
            this.topMargin = parcel.readInt();
            this.bottomMargin = parcel.readInt();
            this.leftMargin = parcel.readInt();
            this.rightMargin = parcel.readInt();
            return true;
        }

        @Override // ohos.agp.components.ComponentProvider.Action
        public void apply(ComponentContainer componentContainer) {
            Component findComponentById = componentContainer.findComponentById(this.viewId);
            if (findComponentById != null) {
                ComponentContainer.LayoutConfig layoutConfig = findComponentById.getLayoutConfig();
                if (layoutConfig != null) {
                    layoutConfig.width = this.layoutWidth;
                    layoutConfig.height = this.layoutHeight;
                } else {
                    layoutConfig = new ComponentContainer.LayoutConfig(this.layoutWidth, this.layoutHeight);
                }
                layoutConfig.setMargins(this.leftMargin, this.topMargin, this.rightMargin, this.bottomMargin);
                findComponentById.setLayoutConfig(layoutConfig);
            }
        }
    }

    /* access modifiers changed from: private */
    public static final class SetProgressBarAction extends Action {
        boolean indeterminate;
        int max;
        int progress;

        @Override // ohos.agp.components.ComponentProvider.Action
        public int getActionTag() {
            return 4;
        }

        @Override // ohos.agp.components.ComponentProvider.Action
        public int mergeOperation() {
            return 0;
        }

        SetProgressBarAction(int i, int i2, int i3, boolean z) {
            this.viewId = i;
            this.max = i2;
            this.progress = i3;
            this.indeterminate = z;
        }

        SetProgressBarAction(Parcel parcel) {
            unmarshalling(parcel);
        }

        @Override // ohos.utils.Sequenceable
        public boolean marshalling(Parcel parcel) {
            return parcel.writeInt(this.viewId) && parcel.writeInt(this.max) && parcel.writeInt(this.progress) && parcel.writeBoolean(this.indeterminate);
        }

        @Override // ohos.utils.Sequenceable
        public boolean unmarshalling(Parcel parcel) {
            this.viewId = parcel.readInt();
            this.max = parcel.readInt();
            this.progress = parcel.readInt();
            this.indeterminate = parcel.readBoolean();
            return true;
        }

        @Override // ohos.agp.components.ComponentProvider.Action
        public void apply(ComponentContainer componentContainer) {
            Component findComponentById = componentContainer.findComponentById(this.viewId);
            if (findComponentById != null) {
                if (findComponentById instanceof ProgressBar) {
                    ProgressBar progressBar = (ProgressBar) findComponentById;
                    progressBar.setMaxValue(this.max);
                    progressBar.setProgressValue(this.progress);
                    progressBar.setIndeterminate(this.indeterminate);
                    return;
                }
                HiLog.debug(ComponentProvider.LABEL, "component %{public}s is not ProgressBar", Integer.valueOf(this.viewId));
            }
        }
    }

    /* access modifiers changed from: private */
    public static final class SetPaddingAction extends Action {
        int bottom;
        int left;
        int right;
        int top;

        @Override // ohos.agp.components.ComponentProvider.Action
        public int getActionTag() {
            return 5;
        }

        @Override // ohos.agp.components.ComponentProvider.Action
        public int mergeOperation() {
            return 0;
        }

        SetPaddingAction(int i, int i2, int i3, int i4, int i5) {
            this.viewId = i;
            this.top = i2;
            this.bottom = i3;
            this.left = i4;
            this.right = i5;
        }

        SetPaddingAction(Parcel parcel) {
            unmarshalling(parcel);
        }

        @Override // ohos.utils.Sequenceable
        public boolean marshalling(Parcel parcel) {
            return parcel.writeInt(this.viewId) && parcel.writeInt(this.top) && parcel.writeInt(this.bottom) && parcel.writeInt(this.left) && parcel.writeInt(this.right);
        }

        @Override // ohos.utils.Sequenceable
        public boolean unmarshalling(Parcel parcel) {
            this.viewId = parcel.readInt();
            this.top = parcel.readInt();
            this.bottom = parcel.readInt();
            this.left = parcel.readInt();
            this.right = parcel.readInt();
            return true;
        }

        @Override // ohos.agp.components.ComponentProvider.Action
        public void apply(ComponentContainer componentContainer) {
            Component findComponentById = componentContainer.findComponentById(this.viewId);
            if (findComponentById != null) {
                findComponentById.setPadding(this.left, this.top, this.right, this.bottom);
            }
        }
    }

    /* access modifiers changed from: private */
    public final class DynamicAction extends Action {
        static final int BOOLEAN = 7;
        static final int DOUBLE = 5;
        static final int FLOAT = 3;
        static final int INT = 1;
        static final int LONG = 6;
        static final int STRING = 2;
        String method;
        int type;
        Object value;

        @Override // ohos.agp.components.ComponentProvider.Action
        public int getActionTag() {
            return 1;
        }

        @Override // ohos.agp.components.ComponentProvider.Action
        public int mergeOperation() {
            return 0;
        }

        DynamicAction(int i, String str, int i2, Object obj) {
            this.viewId = i;
            this.method = str;
            this.type = i2;
            this.value = obj;
        }

        DynamicAction(Parcel parcel) {
            unmarshalling(parcel);
        }

        @Override // ohos.utils.Sequenceable
        public boolean marshalling(Parcel parcel) {
            if (!parcel.writeInt(this.viewId)) {
                HiLog.error(ComponentProvider.LABEL, "write viewId failed!", new Object[0]);
                return false;
            } else if (!parcel.writeString(this.method)) {
                HiLog.error(ComponentProvider.LABEL, "write method failed!", new Object[0]);
                return false;
            } else if (!parcel.writeInt(this.type)) {
                HiLog.error(ComponentProvider.LABEL, "write type failed!", new Object[0]);
                return false;
            } else {
                HiLog.info(ComponentProvider.LABEL, "viewId:%{public}d, methodName:%{public}s, type:%{public}d", Integer.valueOf(this.viewId), this.method, Integer.valueOf(this.type));
                int i = this.type;
                if (i == 1) {
                    return parcel.writeInt(((Integer) this.value).intValue());
                }
                if (i == 2) {
                    return parcel.writeString((String) this.value);
                }
                if (i == 3) {
                    return parcel.writeFloat(((Float) this.value).floatValue());
                }
                if (i == 5) {
                    Object obj = this.value;
                    if (obj instanceof Double) {
                        return parcel.writeDouble(((Double) obj).doubleValue());
                    }
                } else if (i == 6) {
                    Object obj2 = this.value;
                    if (obj2 instanceof Long) {
                        return parcel.writeLong(((Long) obj2).longValue());
                    }
                } else if (i != 7) {
                    HiLog.error(ComponentProvider.LABEL, "unknown type %{public}d", Integer.valueOf(this.type));
                } else {
                    Object obj3 = this.value;
                    if (obj3 instanceof Boolean) {
                        return parcel.writeBoolean(((Boolean) obj3).booleanValue());
                    }
                }
                return false;
            }
        }

        @Override // ohos.utils.Sequenceable
        public boolean unmarshalling(Parcel parcel) {
            this.viewId = parcel.readInt();
            this.method = parcel.readString();
            this.type = parcel.readInt();
            HiLog.info(ComponentProvider.LABEL, "unmarshalling read viewId:%{public}d, methodName:%{public}s, type:%{public}d", Integer.valueOf(this.viewId), this.method, Integer.valueOf(this.type));
            int i = this.type;
            if (i == 1) {
                this.value = Integer.valueOf(parcel.readInt());
            } else if (i == 2) {
                this.value = parcel.readString();
            } else if (i == 3) {
                this.value = Float.valueOf(parcel.readFloat());
            } else if (i == 5) {
                this.value = Double.valueOf(parcel.readDouble());
            } else if (i == 6) {
                this.value = Long.valueOf(parcel.readLong());
            } else if (i == 7) {
                this.value = Boolean.valueOf(parcel.readBoolean());
            }
            return true;
        }

        private Class<?> getParameterType() {
            int i = this.type;
            if (i == 1) {
                return Integer.TYPE;
            }
            if (i == 2) {
                return String.class;
            }
            if (i == 3) {
                return Float.TYPE;
            }
            if (i == 5) {
                return Double.TYPE;
            }
            if (i == 6) {
                return Long.TYPE;
            }
            if (i != 7) {
                return null;
            }
            return Boolean.TYPE;
        }

        @Override // ohos.agp.components.ComponentProvider.Action
        public void apply(ComponentContainer componentContainer) {
            Component findComponentById = componentContainer.findComponentById(this.viewId);
            if (findComponentById != null) {
                Class<?> parameterType = getParameterType();
                if (parameterType != null) {
                    try {
                        ComponentProvider.this.getReflectMethod(findComponentById, this.method, parameterType);
                        Object obj = this.value;
                    } catch (Throwable th) {
                        throw new ComponentProviderException(th);
                    }
                } else {
                    throw new ComponentProviderException("invalid type: " + this.type);
                }
            }
        }

        @Override // ohos.agp.components.ComponentProvider.Action
        public String getActionKey() {
            return super.getActionKey() + this.method + this.type;
        }
    }

    /* access modifiers changed from: private */
    public final class OnClickAction extends Action {
        final IntentAgent intent;

        @Override // ohos.agp.components.ComponentProvider.Action
        public int getActionTag() {
            return 8;
        }

        @Override // ohos.agp.components.ComponentProvider.Action
        public int mergeOperation() {
            return 0;
        }

        public OnClickAction(int i, IntentAgent intentAgent) {
            this.viewId = i;
            this.intent = intentAgent;
        }

        public OnClickAction(Parcel parcel) {
            this.intent = new IntentAgent(null);
            unmarshalling(parcel);
        }

        @Override // ohos.utils.Sequenceable
        public boolean marshalling(Parcel parcel) {
            if (this.intent == null) {
                return parcel.writeInt(this.viewId) && parcel.writeInt(0);
            }
            HiLog.debug(ComponentProvider.LABEL, "OnClickAction marshalling enter", new Object[0]);
            return parcel.writeInt(this.viewId) && parcel.writeInt(1) && this.intent.marshalling(parcel);
        }

        @Override // ohos.utils.Sequenceable
        public boolean unmarshalling(Parcel parcel) {
            HiLog.debug(ComponentProvider.LABEL, "OnClickAction unmarshalling enter", new Object[0]);
            this.viewId = parcel.readInt();
            if (parcel.readInt() == 0) {
                HiLog.error(ComponentProvider.LABEL, "OnClickAction not data in Parcel.", new Object[0]);
                return false;
            }
            IntentAgent intentAgent = this.intent;
            if (intentAgent == null) {
                HiLog.error(ComponentProvider.LABEL, "OnClickAction unmarshalling intent is null", new Object[0]);
                return false;
            }
            intentAgent.unmarshalling(parcel);
            return true;
        }

        @Override // ohos.agp.components.ComponentProvider.Action
        public void apply(ComponentContainer componentContainer) {
            HiLog.debug(ComponentProvider.LABEL, "OnClickAction apply enter", new Object[0]);
            Component findComponentById = componentContainer.findComponentById(this.viewId);
            if (findComponentById == null) {
                HiLog.error(ComponentProvider.LABEL, "OnClickAction apply componentContainer is null", new Object[0]);
            } else if (this.intent != null) {
                findComponentById.setClickedListener(new Component.ClickedListener() {
                    /* class ohos.agp.components.$$Lambda$ComponentProvider$OnClickAction$xkSNLkNFlHHMSGlzitNAeSBMnM8 */

                    @Override // ohos.agp.components.Component.ClickedListener
                    public final void onClick(Component component) {
                        ComponentProvider.OnClickAction.this.lambda$apply$0$ComponentProvider$OnClickAction(component);
                    }
                });
            }
        }

        public /* synthetic */ void lambda$apply$0$ComponentProvider$OnClickAction(Component component) {
            HiLog.debug(ComponentProvider.LABEL, "OnClickAction apply onClick enter", new Object[0]);
            Context context = component.getContext();
            if (context != null) {
                IntentAgentHelper.triggerIntentAgent(context, this.intent, null, null, null);
            } else {
                HiLog.error(ComponentProvider.LABEL, "OnClickAction apply context is null", new Object[0]);
            }
        }
    }

    public static abstract class Action implements Sequenceable {
        public static final int MERGE_REPLACE = 0;
        public int viewId;

        public abstract void apply(ComponentContainer componentContainer) throws ComponentProviderException;

        public abstract int getActionTag();

        public int mergeOperation() {
            return 0;
        }

        public String getActionKey() {
            return getActionTag() + "_" + this.viewId;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private MethodHandle getReflectMethod(Component component, String str, Class<?> cls) {
        Method method;
        Class<?> cls2 = component.getClass();
        if (cls == null) {
            try {
                method = cls2.getMethod(str, new Class[0]);
            } catch (IllegalAccessException | NoSuchMethodException unused) {
                throw new ComponentProviderException("component: " + cls2.getName() + " method not found: " + str + "paramType:" + cls);
            }
        } else {
            method = cls2.getMethod(str, cls);
        }
        return MethodHandles.publicLookup().unreflect(method);
    }

    public static class ComponentProviderException extends RuntimeException {
        private static final long serialVersionUID = 7724893394008725713L;

        public ComponentProviderException(String str) {
            super(str);
        }

        public ComponentProviderException(Throwable th) {
            super(th);
        }
    }
}
