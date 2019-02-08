package com.avob.openadr.server.oadr20a.ven;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticationConfig {

    @Value("${oadr.security.vtn.rsaPrivateKeyPath:#{null}}")
    private String vtnRsaPrivateKeyPath;

    @Value("${oadr.security.vtn.rsaCertificatePath:#{null}}")
    private String vtnRsaCertificatePath;

    @Value("${oadr.security.vtn.eccPrivateKeyPath:#{null}}")
    private String vtnEccPrivateKeyPath;

    @Value("${oadr.security.vtn.eccCertificatePath:#{null}}")
    private String vtnEccCertificatePath;

    @Value("${oadr.security.vtn.trustcertificate.oadrRsaRootCertificate:#{null}}")
    private String vtnRsaRootCertificate;

    @Value("${oadr.security.vtn.trustcertificate.oadrRsaIntermediateCertificate:#{null}}")
    private String vtnRsaIntermediateCertificate;

    @Value("${oadr.security.vtn.trustcertificate.oadrEccRootCertificate:#{null}}")
    private String vtnEccRootCertificate;

    @Value("${oadr.security.vtn.trustcertificate.oadrEccIntermediateCertificate:#{null}}")
    private String vtnEccIntermediateCertificate;

    @Value("${oadr.security.ven.rsaPrivateKeyPath:#{null}}")
    private String venRsaPrivateKeyPath;

    @Value("${oadr.security.ven.rsaCertificatePath:#{null}}")
    private String venRsaCertificatePath;

    @Value("${oadr.security.ven.eccPrivateKeyPath:#{null}}")
    private String venEccPrivateKeyPath;

    @Value("${oadr.security.ven.eccCertificatePath:#{null}}")
    private String venEccCertificatePath;

    @Value("${oadr.security.ven.trustcertificate.oadrRsaRootCertificate:#{null}}")
    private String venRsaRootCertificate;

    @Value("${oadr.security.ven.trustcertificate.oadrRsaIntermediateCertificate:#{null}}")
    private String venRsaIntermediateCertificate;

    @Value("${oadr.security.ven.trustcertificate.oadrEccRootCertificate:#{null}}")
    private String venEccRootCertificate;

    @Value("${oadr.security.ven.trustcertificate.oadrEccIntermediateCertificate:#{null}}")
    private String venEccIntermediateCertificate;

    @Value("${oadr.security.authentication.basic.username:#{null}}")
    private String basicUsername;

    @Value("${oadr.security.authentication.basic.password:#{null}}")
    private String basicPassword;

    @Value("${oadr.security.authentication.digest.username:#{null}}")
    private String digestUsername;

    @Value("${oadr.security.authentication.digest.password:#{null}}")
    private String digestPassword;

    public boolean isBasicAuthenticationConfigured() {
        return basicUsername != null && basicPassword != null;
    }

    public boolean isDigestAuthenticationConfigured() {
        return digestUsername != null && digestPassword != null;
    }

    public boolean isClientCertificateAuthenticationConfigured() {
        return this.getVenRsaPrivateKeyPath() != null && this.getVenRsaCertificatePath() != null;
    }

    public List<String> getTrustCertificate() {
        List<String> trustCertificate = new ArrayList<String>();
        trustCertificate.add(this.getVtnEccRootCertificate());
        trustCertificate.add(this.getVtnEccIntermediateCertificate());
        trustCertificate.add(this.getVtnRsaRootCertificate());
        trustCertificate.add(this.getVtnRsaIntermediateCertificate());
        return trustCertificate;
    }

    public String getBasicUsername() {
        return basicUsername;
    }

    public void setBasicUsername(String basicUsername) {
        this.basicUsername = basicUsername;
    }

    public String getBasicPassword() {
        return basicPassword;
    }

    public void setBasicPassword(String basicPassword) {
        this.basicPassword = basicPassword;
    }

    public String getDigestUsername() {
        return digestUsername;
    }

    public void setDigestUsername(String digestUsername) {
        this.digestUsername = digestUsername;
    }

    public String getDigestPassword() {
        return digestPassword;
    }

    public void setDigestPassword(String digestPassword) {
        this.digestPassword = digestPassword;
    }

    public String getVtnRsaPrivateKeyPath() {
        return vtnRsaPrivateKeyPath;
    }

    public void setVtnRsaPrivateKeyPath(String vtnRsaPrivateKeyPath) {
        this.vtnRsaPrivateKeyPath = vtnRsaPrivateKeyPath;
    }

    public String getVtnRsaCertificatePath() {
        return vtnRsaCertificatePath;
    }

    public void setVtnRsaCertificatePath(String vtnRsaCertificatePath) {
        this.vtnRsaCertificatePath = vtnRsaCertificatePath;
    }

    public String getVtnEccPrivateKeyPath() {
        return vtnEccPrivateKeyPath;
    }

    public void setVtnEccPrivateKeyPath(String vtnEccPrivateKeyPath) {
        this.vtnEccPrivateKeyPath = vtnEccPrivateKeyPath;
    }

    public String getVtnEccCertificatePath() {
        return vtnEccCertificatePath;
    }

    public void setVtnEccCertificatePath(String vtnEccCertificatePath) {
        this.vtnEccCertificatePath = vtnEccCertificatePath;
    }

    public String getVtnRsaRootCertificate() {
        return vtnRsaRootCertificate;
    }

    public void setVtnRsaRootCertificate(String vtnRsaRootCertificate) {
        this.vtnRsaRootCertificate = vtnRsaRootCertificate;
    }

    public String getVtnRsaIntermediateCertificate() {
        return vtnRsaIntermediateCertificate;
    }

    public void setVtnRsaIntermediateCertificate(String vtnRsaIntermediateCertificate) {
        this.vtnRsaIntermediateCertificate = vtnRsaIntermediateCertificate;
    }

    public String getVtnEccRootCertificate() {
        return vtnEccRootCertificate;
    }

    public void setVtnEccRootCertificate(String vtnEccRootCertificate) {
        this.vtnEccRootCertificate = vtnEccRootCertificate;
    }

    public String getVtnEccIntermediateCertificate() {
        return vtnEccIntermediateCertificate;
    }

    public void setVtnEccIntermediateCertificate(String vtnEccIntermediateCertificate) {
        this.vtnEccIntermediateCertificate = vtnEccIntermediateCertificate;
    }

    public String getVenRsaPrivateKeyPath() {
        return venRsaPrivateKeyPath;
    }

    public void setVenRsaPrivateKeyPath(String venRsaPrivateKeyPath) {
        this.venRsaPrivateKeyPath = venRsaPrivateKeyPath;
    }

    public String getVenRsaCertificatePath() {
        return venRsaCertificatePath;
    }

    public void setVenRsaCertificatePath(String venRsaCertificatePath) {
        this.venRsaCertificatePath = venRsaCertificatePath;
    }

    public String getVenEccPrivateKeyPath() {
        return venEccPrivateKeyPath;
    }

    public void setVenEccPrivateKeyPath(String venEccPrivateKeyPath) {
        this.venEccPrivateKeyPath = venEccPrivateKeyPath;
    }

    public String getVenEccCertificatePath() {
        return venEccCertificatePath;
    }

    public void setVenEccCertificatePath(String venEccCertificatePath) {
        this.venEccCertificatePath = venEccCertificatePath;
    }

    public String getVenRsaRootCertificate() {
        return venRsaRootCertificate;
    }

    public void setVenRsaRootCertificate(String venRsaRootCertificate) {
        this.venRsaRootCertificate = venRsaRootCertificate;
    }

    public String getVenRsaIntermediateCertificate() {
        return venRsaIntermediateCertificate;
    }

    public void setVenRsaIntermediateCertificate(String venRsaIntermediateCertificate) {
        this.venRsaIntermediateCertificate = venRsaIntermediateCertificate;
    }

    public String getVenEccRootCertificate() {
        return venEccRootCertificate;
    }

    public void setVenEccRootCertificate(String venEccRootCertificate) {
        this.venEccRootCertificate = venEccRootCertificate;
    }

    public String getVenEccIntermediateCertificate() {
        return venEccIntermediateCertificate;
    }

    public void setVenEccIntermediateCertificate(String venEccIntermediateCertificate) {
        this.venEccIntermediateCertificate = venEccIntermediateCertificate;
    }

}
