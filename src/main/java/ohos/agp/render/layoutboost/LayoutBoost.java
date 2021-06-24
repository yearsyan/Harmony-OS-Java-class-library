package ohos.agp.render.layoutboost;

import java.io.IOException;
import java.util.HashMap;
import ohos.aafwk.utils.log.LogDomain;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class LayoutBoost {
    private static final String DIAGONAL_SEPARATOR = "/";
    private static final HashMap<Integer, LayoutMaker> MAP = new HashMap<>();
    private static final String POINT_SEPARATOR = ".";
    private static final String RESOURCE_PATH = "com.huawei.agp.layouts.LayoutBoost";
    private static final HiLogLabel TAG = new HiLogLabel(3, LogDomain.END, "LayoutBosst");
    private static final String UNDER_SEPARATOR = "_";

    private static int generateGroupId(int i) {
        return i >> 24;
    }

    public static Component inflate(Context context, int i, ComponentContainer componentContainer, boolean z) {
        return inflate(LayoutScatter.getInstance(context), context, i, componentContainer, z);
    }

    public static Component inflate(LayoutScatter layoutScatter, Context context, int i, ComponentContainer componentContainer, boolean z) throws IllegalArgumentException {
        Component component = getComponent(context, i);
        if (component != null) {
            if (componentContainer != null && z) {
                componentContainer.addComponent(component);
            }
            return component;
        } else if (layoutScatter != null) {
            return layoutScatter.parse(i, componentContainer, z);
        } else {
            throw new IllegalArgumentException("layoutscatter is null");
        }
    }

    private static Component getComponent(Context context, int i) {
        if (context == null) {
            return null;
        }
        LayoutMaker layoutMaker = MAP.get(Integer.valueOf(i));
        if (layoutMaker == null) {
            try {
                int generateGroupId = generateGroupId(i);
                String string = context.getResourceManager().getElement(i).getString();
                if (string.length() > 0) {
                    String substring = string.substring(string.lastIndexOf("/") + 1, string.lastIndexOf("."));
                    Object newInstance = context.getClassloader().loadClass(RESOURCE_PATH + generateGroupId + "_" + substring).newInstance();
                    if (newInstance instanceof LayoutMaker) {
                        layoutMaker = (LayoutMaker) newInstance;
                    }
                }
            } catch (IOException unused) {
                HiLog.error(TAG, "Get element failed", new Object[0]);
            } catch (WrongTypeException unused2) {
                HiLog.error(TAG, "Get element failed, wrong type", new Object[0]);
            } catch (IllegalAccessException unused3) {
                HiLog.error(TAG, "Get element failed, illegal access", new Object[0]);
            } catch (NotExistException unused4) {
                HiLog.error(TAG, "Get element failed, not exist", new Object[0]);
            } catch (ClassNotFoundException unused5) {
                HiLog.error(TAG, "Have no this class", new Object[0]);
            } catch (InstantiationException unused6) {
                HiLog.error(TAG, "Create new instance failed", new Object[0]);
            }
            if (layoutMaker == null) {
                layoutMaker = new DefaultCreator();
            }
            MAP.put(Integer.valueOf(i), layoutMaker);
        }
        return layoutMaker.createComponent(context);
    }

    /* access modifiers changed from: private */
    public static class DefaultCreator implements LayoutMaker {
        @Override // ohos.agp.render.layoutboost.LayoutMaker
        public Component createComponent(Context context) {
            return null;
        }

        private DefaultCreator() {
        }
    }
}
