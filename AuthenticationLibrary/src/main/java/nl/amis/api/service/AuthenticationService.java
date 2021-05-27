package nl.amis.api.service;

import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.telemetry.Duration;
import lombok.RequiredArgsConstructor;
import nl.amis.api.lib.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Qualifier("tokenWebServiceTemplate")
    private final WebServiceTemplate tokenWebServiceTemplate;

    private final TelemetryClient telemetryClient;
    private final KeyVaultService keyVaultService;


    public Tags getTags(RequestIdentifier requestIdentifier) {
        final CertificateKeyVault certInfo = getCertificate(requestIdentifier);
        return certInfo.getTags();
    }

    public CertificateKeyVault getCertificate(RequestIdentifier requestIdentifier) {
        String thumbprint = requestIdentifier.thumbPrint;
        String token = requestIdentifier.token;
        String apk = requestIdentifier.appKey;

        if (StringUtils.isNotEmpty(token)) {
            String userName = checkTokenAndDecrypt(token);
            try {
                return keyVaultService.getCertificateByUsername(userName);
            } catch (Exception e) {
                Map<String, String> properties = new HashMap<>();
                properties.put("Operation", "getCertificate");
                properties.put("Thumbprint", thumbprint);
                properties.put("Token", token);
                properties.put("AppKey", apk);
                telemetryClient.trackException(e, properties, null);
                throw e;
            }
        } else {
            throw new RuntimeException("Geen gebruiker gevonden op basis van thumbprint / token");
        }
    }

    public String checkTokenAndDecrypt(String token) {
        CheckToken checkToken = new CheckToken();
        checkToken.setToken(token);

        CheckTokenResponse checkResponse;
        long start = System.currentTimeMillis();
        boolean success = true;

        try {
            checkResponse = (CheckTokenResponse) tokenWebServiceTemplate
                    .marshalSendAndReceive(checkToken, new SoapActionCallback("http://www.aplaza.nl/token/CheckToken"));
        } catch (Exception e) {
            telemetryClient.trackException(e, Collections.singletonMap("Token", token), null);
            success = false;
            throw e;
        } finally {
            long finish = System.currentTimeMillis();
            Duration duration = new Duration(finish - start);
            telemetryClient.trackDependency("CheckToken", token, duration, success);
        }
        if (!checkResponse.getStatus().equals(CheckTokenResponse.Status.VALID)) {
            throw new RuntimeException("Expired token");
        }

        DecryptToken decryptToken = new DecryptToken();
        decryptToken.setToken(token);

        DecryptTokenResponse decryptResponse;
        long start2 = System.currentTimeMillis();
        boolean success2 = true;

        try {
            decryptResponse = (DecryptTokenResponse) tokenWebServiceTemplate
                    .marshalSendAndReceive(decryptToken, new SoapActionCallback("http://www.aplaza.nl/token/DecryptToken"));
        } catch (Exception e) {
            telemetryClient.trackException(e, Collections.singletonMap("Token", token), null);
            success2 = false;
            throw e;
        } finally {
            long finish = System.currentTimeMillis();
            Duration duration = new Duration(finish - start2);
            telemetryClient.trackDependency("DecryptToken", decryptToken.getToken(), duration, success2);
        }

        return decryptResponse.getDecrypted();
    }
}
