package ohos.agp.components.webengine;

public interface AuthRequest {
    void cancel();

    boolean isCredentialsStored();

    void respond(String str, String str2);
}
