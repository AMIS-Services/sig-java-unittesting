package nl.amis.api.lib;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class CertificateKeyVault {
    private final String certificateName;
    private final byte[] certificate;
    private final String thumbprint;
    private final OffsetDateTime createdOn;
    private final OffsetDateTime expiresOn;
    private final Tags tags;
    private final String policySubject;
}
