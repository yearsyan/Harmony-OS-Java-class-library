package ohos.agp.animation;

import java.io.IOException;
import ohos.aafwk.utils.log.LogDomain;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.ResourceManager;
import ohos.global.resource.WrongTypeException;
import ohos.global.resource.solidxml.Node;
import ohos.global.resource.solidxml.SolidXml;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class AnimatorScatter {
    private static final HiLogLabel TAG = new HiLogLabel(3, LogDomain.END, "AGP_AnimatorScatter");
    private final Context mContext;
    private final ResourceManager mResourceManager;

    private AnimatorScatter(Context context) {
        if (context != null) {
            this.mContext = context;
            this.mResourceManager = this.mContext.getResourceManager();
            return;
        }
        throw new AnimatorScatterException("context is null");
    }

    public static AnimatorScatter getInstance(Context context) {
        if (context != null) {
            return new AnimatorScatter(context);
        }
        throw new AnimatorScatterException("get context is null");
    }

    public Animator parse(int i) {
        HiLog.debug(TAG, "enter parse Animator", new Object[0]);
        ResourceManager resourceManager = this.mResourceManager;
        if (resourceManager != null) {
            try {
                SolidXml solidXml = resourceManager.getSolidXml(i);
                HiLog.debug(TAG, "load drawable xml", new Object[0]);
                return parseSolidXml(solidXml);
            } catch (NotExistException unused) {
                throw new AnimatorScatterException("Can't open solid xml: file not exist: " + i);
            } catch (IOException unused2) {
                throw new AnimatorScatterException("Can't open solid xml: io exception: " + i);
            } catch (WrongTypeException unused3) {
                throw new AnimatorScatterException("Can't open solid xml: wrong type: " + i);
            }
        } else {
            throw new AnimatorScatterException("AnimatorScatter should init Context first.");
        }
    }

    private Animator parseSolidXml(SolidXml solidXml) {
        if (solidXml != null) {
            Node root = solidXml.getRoot();
            if (root != null) {
                String name = root.getName();
                HiLog.debug(TAG, "parseSolidXml: %{public}s", name);
                if (name == null || name.length() == 0) {
                    throw new AnimatorScatterException("Solid XML root node has no name!");
                }
                Animator parseFromTag = parseFromTag(name);
                if (parseFromTag != null) {
                    parseFromTag.parse(root, this.mResourceManager);
                }
                return parseFromTag;
            }
            throw new AnimatorScatterException("Solid XML has no root node!");
        }
        throw new AnimatorScatterException("Can't open solid XML!");
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0039  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0050  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private ohos.agp.animation.Animator parseFromTag(java.lang.String r5) {
        /*
            r4 = this;
            int r4 = r5.hashCode()
            r0 = -795202841(0xffffffffd09a2ae7, float:-2.06920233E10)
            r1 = 0
            r2 = 2
            r3 = 1
            if (r4 == r0) goto L_0x002c
            r0 = -423011167(0xffffffffe6c95ca1, float:-4.754522E23)
            if (r4 == r0) goto L_0x0021
            r0 = 1664652764(0x633895dc, float:3.4049994E21)
            if (r4 == r0) goto L_0x0017
            goto L_0x0036
        L_0x0017:
            java.lang.String r4 = "animatorProperty"
            boolean r4 = r5.equals(r4)
            if (r4 == 0) goto L_0x0036
            r4 = r2
            goto L_0x0037
        L_0x0021:
            java.lang.String r4 = "viewPropertyAnimator"
            boolean r4 = r5.equals(r4)
            if (r4 == 0) goto L_0x0036
            r4 = r3
            goto L_0x0037
        L_0x002c:
            java.lang.String r4 = "animator"
            boolean r4 = r5.equals(r4)
            if (r4 == 0) goto L_0x0036
            r4 = r1
            goto L_0x0037
        L_0x0036:
            r4 = -1
        L_0x0037:
            if (r4 == 0) goto L_0x0050
            if (r4 == r3) goto L_0x004a
            if (r4 == r2) goto L_0x004a
            ohos.hiviewdfx.HiLogLabel r4 = ohos.agp.animation.AnimatorScatter.TAG
            java.lang.Object[] r0 = new java.lang.Object[r3]
            r0[r1] = r5
            java.lang.String r5 = "do not support this tag: %{public}s"
            ohos.hiviewdfx.HiLog.debug(r4, r5, r0)
            r4 = 0
            return r4
        L_0x004a:
            ohos.agp.animation.AnimatorProperty r4 = new ohos.agp.animation.AnimatorProperty
            r4.<init>()
            return r4
        L_0x0050:
            ohos.agp.animation.AnimatorValue r4 = new ohos.agp.animation.AnimatorValue
            r4.<init>()
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: ohos.agp.animation.AnimatorScatter.parseFromTag(java.lang.String):ohos.agp.animation.Animator");
    }
}
