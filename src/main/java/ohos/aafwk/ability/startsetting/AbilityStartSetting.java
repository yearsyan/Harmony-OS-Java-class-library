package ohos.aafwk.ability.startsetting;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import ohos.agp.agpanimator.AnimatorOption;
import ohos.annotation.SystemApi;

public class AbilityStartSetting {
    public static final String BOUNDS_KEY = "abilityBounds";
    public static final String WINDOW_DISPLAY_ID_KEY = "windowId";
    public static final String WINDOW_MODE_KEY = "windowMode";
    private AnimatorOption animatorOption;
    private Map<String, Object> startProperties = new HashMap();

    protected AbilityStartSetting() {
    }

    public static AbilityStartSetting getEmptySetting() {
        return new AbilityStartSetting();
    }

    public boolean isEmpty() {
        return this.startProperties.isEmpty() && this.animatorOption == null;
    }

    @SystemApi
    public void addProperty(String str, Object obj) {
        this.startProperties.put(str, obj);
    }

    @SystemApi
    public void addAnimatorOption(AnimatorOption animatorOption2) {
        this.animatorOption = animatorOption2;
    }

    @SystemApi
    public AnimatorOption getAnimatorOption() {
        return this.animatorOption;
    }

    public Object getProperty(String str) {
        if (this.startProperties.containsKey(str)) {
            return this.startProperties.get(str);
        }
        return null;
    }

    public Set<String> getPropertiesKey() {
        return this.startProperties.keySet();
    }
}
