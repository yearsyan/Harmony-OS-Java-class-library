package com.huawei.ace.plugin.texture;

import android.graphics.Paint;
import android.view.TextureLayer;
import com.huawei.ace.runtime.ALog;
import com.huawei.ace.runtime.IAceOnCallResourceMethod;
import com.huawei.ace.runtime.IAceOnResourceEvent;
import java.util.HashMap;
import java.util.Map;

public class AceLayerTexture extends AceTexture {
    private static final String LOG_TAG = "AceLayerTexture";
    private IAceTextureLayer aceTextureLayerCreator;
    private Map<String, IAceOnCallResourceMethod> callMethodMap;
    private Paint paint;
    private TextureLayer textureLayer;
    private int textureLayerHeight = 0;
    private int textureLayerWidth = 0;

    @Override // com.huawei.ace.plugin.texture.AceTexture
    public void setSurfaceTexture() {
    }

    public AceLayerTexture(long j, IAceTexture iAceTexture, IAceOnResourceEvent iAceOnResourceEvent, IAceTextureLayer iAceTextureLayer) {
        super(j, iAceTexture, iAceOnResourceEvent);
        this.aceTextureLayerCreator = iAceTextureLayer;
        this.callMethodMap = new HashMap();
        $$Lambda$AceLayerTexture$z8rYiOt1WFssr36xaM2n2VuNsM r3 = new IAceOnCallResourceMethod() {
            /* class com.huawei.ace.plugin.texture.$$Lambda$AceLayerTexture$z8rYiOt1WFssr36xaM2n2VuNsM */

            @Override // com.huawei.ace.runtime.IAceOnCallResourceMethod
            public final String onCall(Map map) {
                return AceLayerTexture.this.lambda$new$0$AceLayerTexture(map);
            }
        };
        Map<String, IAceOnCallResourceMethod> map = this.callMethodMap;
        map.put("texture@" + j + "method=setTextureSize?", r3);
    }

    public Map<String, IAceOnCallResourceMethod> getCallMethod() {
        return this.callMethodMap;
    }

    public long getLayerHandle() {
        TextureLayer textureLayer2 = this.textureLayer;
        if (textureLayer2 == null) {
            return 0;
        }
        return textureLayer2.getLayerHandle();
    }

    public void setLayerAlpha(int i) {
        if (this.textureLayer != null) {
            if (this.paint == null) {
                this.paint = new Paint();
            }
            this.paint.setAlpha(i);
            this.textureLayer.setLayerPaint(this.paint);
        }
    }

    /* renamed from: setTextureSize */
    public String lambda$new$0$AceLayerTexture(Map<String, String> map) {
        if (map.containsKey("textureWidth") && map.containsKey("textureHeight")) {
            try {
                this.textureLayerWidth = Integer.parseInt(map.get("textureWidth"));
                this.textureLayerHeight = Integer.parseInt(map.get("textureHeight"));
                return updateTexture(this.textureLayerWidth, this.textureLayerHeight);
            } catch (NumberFormatException unused) {
                ALog.w(LOG_TAG, "NumberFormatException, setTextureSize failed. value = " + map.get("textureWidth") + map.get("textureHeight"));
            }
        }
        return "false";
    }

    private String updateTexture(int i, int i2) {
        TextureLayer textureLayer2 = this.textureLayer;
        if (textureLayer2 == null) {
            return "false";
        }
        textureLayer2.prepare(i, i2, false);
        this.textureLayer.updateSurfaceTexture();
        return "success";
    }

    @Override // com.huawei.ace.plugin.texture.AceTexture
    public void markTextureFrame() {
        super.markTextureFrame();
        if (this.textureLayer == null) {
            this.textureLayer = this.aceTextureLayerCreator.createTextureLayer();
            if (this.textureLayer != null) {
                this.textureImpl.registerTexture(this.id, this);
                this.textureLayer.setSurfaceTexture(this.surfaceTexture);
                this.textureLayer.prepare(this.textureLayerWidth, this.textureLayerHeight, false);
            } else {
                return;
            }
        }
        this.textureLayer.updateSurfaceTexture();
    }

    @Override // com.huawei.ace.plugin.texture.AceTexture
    public void release() {
        this.surfaceTexture.setOnFrameAvailableListener(null);
        if (this.textureLayer != null) {
            this.textureImpl.unregisterTexture(this.id);
        }
        this.surfaceTexture.release();
    }
}
