package ohos.ace.plugin.video;

import com.huawei.ace.adapter.AceSurfaceAdapter;
import com.huawei.ace.plugin.texture.AceTexture;
import com.huawei.ace.plugin.video.AceVideoPluginBase;
import com.huawei.ace.runtime.ALog;
import java.util.Map;
import ohos.agp.graphics.Surface;
import ohos.app.Context;

public class AceVideoPlugin extends AceVideoPluginBase {
    private static final String LOG_TAG = "AceVideoPlugin";
    private static String instanceName;
    private final Context context;

    private AceVideoPlugin(Context context2) {
        this.context = context2;
    }

    public static AceVideoPlugin createRegister(Context context2, String str) {
        instanceName = str;
        return new AceVideoPlugin(context2);
    }

    @Override // com.huawei.ace.runtime.AceResourcePlugin, com.huawei.ace.plugin.video.AceVideoPluginBase
    public long create(Map<String, String> map) {
        if (!map.containsKey("texture")) {
            return -1;
        }
        try {
            long parseLong = Long.parseLong(map.get("texture"));
            Object object = this.resRegister.getObject("texture", parseLong);
            if (object != null) {
                if (object instanceof AceTexture) {
                    Surface surface = new AceSurfaceAdapter(((AceTexture) object).getSurface()).getSurface();
                    if (surface == null) {
                        ALog.e(LOG_TAG, "surface is null");
                        return -1;
                    }
                    long atomicId = getAtomicId();
                    addResource(atomicId, new AceVideo(atomicId, instanceName, surface, this.context, getEventCallback()));
                    return atomicId;
                }
            }
            ALog.e(LOG_TAG, "create fail , failed to find texture, texture id = " + parseLong);
            return -1;
        } catch (NumberFormatException unused) {
            ALog.e(LOG_TAG, "NumberFormatException texture:" + map.get("texture"));
            return -1;
        }
    }
}
