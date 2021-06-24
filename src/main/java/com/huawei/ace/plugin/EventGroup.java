package com.huawei.ace.plugin;

import com.huawei.ace.plugin.Plugin;
import java.util.List;

public final class EventGroup {
    private final String name;

    public EventGroup(String str) {
        this.name = str;
    }

    public void setEventGroupHandler(EventGroupHandler eventGroupHandler) {
        setEventGroupHandler(eventGroupHandler, null);
    }

    public void setEventGroupHandler(EventGroupHandler eventGroupHandler, Integer num) {
        Plugin.registerPluginHandler(this.name, eventGroupHandler, num);
    }

    public static void registerEventGroup(String str, EventGroupHandler eventGroupHandler, Integer num) {
        Plugin.registerPluginHandler(str, eventGroupHandler, num);
    }

    public interface EventGroupHandler extends Plugin.RequestHandler {
        void onSubscribe(List<Object> list, EventNotifier eventNotifier, Result result);

        default void onUnsubscribe(List<Object> list, Result result) {
        }

        default void onUnsubscribe(List<Object> list, EventNotifier eventNotifier, Result result) {
            onUnsubscribe(list, result);
        }
    }
}
