package ohos.agp.components.webengine;

import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public interface CertificateRequest {
    void cancelAlways();

    void cancelOnce();

    String getHost();

    int getPort();

    Principal[] getPrincipals();

    String[] getTypes();

    void respond(PrivateKey privateKey, X509Certificate[] x509CertificateArr);
}
