package ohos.agp.components.webengine;

import java.util.Map;
import ohos.utils.net.Uri;

public interface ResourceRequest {
    Map<String, String> getHeaders();

    String getMethod();

    Uri getRequestUrl();

    boolean isMainFrame();

    boolean isServerSideRedirected();
}
