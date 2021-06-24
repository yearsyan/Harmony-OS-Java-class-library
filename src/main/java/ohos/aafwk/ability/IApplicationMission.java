package ohos.aafwk.ability;

import ohos.annotation.SystemApi;

@SystemApi
public interface IApplicationMission {
    void bringToForeground();

    void finishMission();

    void keepOutOfRecents(boolean z);
}
