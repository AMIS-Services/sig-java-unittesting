package nl.amis.api.lib;

import com.azure.security.keyvault.certificates.CertificateClient;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.azure.security.keyvault.certificates.models.CertificateProperties;
import com.azure.security.keyvault.certificates.models.KeyVaultCertificateWithPolicy;


@Service
@RequiredArgsConstructor
public class KeyVaultService {

    @Value("${azure.client.id:?}")
    private String clientId;

    @Value("${azure.client.secret:?}")
    private String clientSecret;

    @Value("${azure.tenant.id:?}")
    private String tenantId;

    @Value("${azure.keyvault.url:?}")
    private String keyVaultUrl;

    private CertificateClient certClient;

    private static final String APP_KEY_TAG_NAME = "appkey";
    private static final String USERNAME_TAG_NAME = "username";
    private static final String ROLE_GROUP_TAG_NAME = "roleGroup";

    /**
     * Get data of given certificate from Key Vault
     *
     * @param certName name of the certificate to retrieve
     * @return Certificate with KeyVault-info
     */
    public CertificateKeyVault getCertificate(final String certName) {
        final KeyVaultCertificateWithPolicy retrievedCert = certClient.getCertificate(certName);

        return toModel(retrievedCert);
    }

    /**
     * Obtain certificate-name (keyvault-entry-name) from username
     * @param username name of the user
     * @return certificate-name (keyvault-entry-name)
     */
    public String getCertificateNameFromUsername(final String username) {
        return "ALL-CERT-" + username.replace("_", "-");
    }

    /**
     * Get data of given user certificate from Key Vault
     *
     * @param username name of the user
     * @return binary value of retrieved certificate
     */
    public CertificateKeyVault getCertificateByUsername(final String username) {
        String certName = getCertificateNameFromUsername(username);
        return getCertificate(certName);
    }

    /* Private Methods */

    /**
     * Transform AzureKeyVault-Certificate-Object to Model-Object
     * @param keyVaultCertificate AzureKeyVault-Certificate-Object
     * @return Certificate Model-Object
     */
    private CertificateKeyVault toModel(KeyVaultCertificateWithPolicy keyVaultCertificate) {
        String certName = keyVaultCertificate.getName();

        byte[] encodedCert;
        encodedCert = keyVaultCertificate.getCer();

        byte[] x509Thumbprint = keyVaultCertificate.getProperties().getX509Thumbprint();
        String thumbprint = DatatypeConverter.printHexBinary(x509Thumbprint);

        final CertificateProperties properties = keyVaultCertificate.getProperties();
        final OffsetDateTime expiresOn = properties.getExpiresOn();
        final OffsetDateTime createdOn = properties.getCreatedOn();

        Map<String, String> tags = properties.getTags();
        String appKey = tags.get(APP_KEY_TAG_NAME);
        String username = tags.get(USERNAME_TAG_NAME);
        String roleGroup = tags.get(ROLE_GROUP_TAG_NAME);

        String policySubject = keyVaultCertificate.getPolicy().getSubject();

        return new CertificateKeyVault(certName, encodedCert, thumbprint, createdOn, expiresOn, new Tags(appKey, username, roleGroup), policySubject);
    }
}
