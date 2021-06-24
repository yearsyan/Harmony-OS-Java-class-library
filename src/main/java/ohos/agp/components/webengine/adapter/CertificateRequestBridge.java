package ohos.agp.components.webengine.adapter;

import android.webkit.ClientCertRequest;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import ohos.agp.components.webengine.CertificateRequest;

public class CertificateRequestBridge implements CertificateRequest {
    private final ClientCertRequest mClientCertRequest;

    private CertificateRequestBridge(ClientCertRequest clientCertRequest) {
        this.mClientCertRequest = clientCertRequest;
    }

    static CertificateRequestBridge create(ClientCertRequest clientCertRequest) {
        if (clientCertRequest == null) {
            return null;
        }
        return new CertificateRequestBridge(clientCertRequest);
    }

    @Override // ohos.agp.components.webengine.CertificateRequest
    public void respond(PrivateKey privateKey, X509Certificate[] x509CertificateArr) {
        this.mClientCertRequest.proceed(privateKey, x509CertificateArr);
    }

    @Override // ohos.agp.components.webengine.CertificateRequest
    public void cancelOnce() {
        this.mClientCertRequest.ignore();
    }

    @Override // ohos.agp.components.webengine.CertificateRequest
    public void cancelAlways() {
        this.mClientCertRequest.cancel();
    }

    @Override // ohos.agp.components.webengine.CertificateRequest
    public String getHost() {
        return this.mClientCertRequest.getHost();
    }

    @Override // ohos.agp.components.webengine.CertificateRequest
    public int getPort() {
        return this.mClientCertRequest.getPort();
    }

    @Override // ohos.agp.components.webengine.CertificateRequest
    public String[] getTypes() {
        return this.mClientCertRequest.getKeyTypes();
    }

    @Override // ohos.agp.components.webengine.CertificateRequest
    public Principal[] getPrincipals() {
        return this.mClientCertRequest.getPrincipals();
    }
}
