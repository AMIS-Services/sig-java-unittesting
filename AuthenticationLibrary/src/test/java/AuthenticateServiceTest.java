import com.azure.core.exception.ResourceNotFoundException;
import com.azure.core.http.HttpResponse;
import com.microsoft.applicationinsights.TelemetryClient;
import nl.amis.api.lib.*;
import nl.amis.api.service.AuthenticationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticateServiceTest {

    @InjectMocks
    @Spy
    private AuthenticationService testSubject;

    @Mock
    private WebServiceTemplate tokenWebServiceTemplate;

    @Mock
    private KeyVaultService keyVaultService;

    @Mock
    private TelemetryClient telemetryClient;

    @Test
    public void testGetTags() {
        // Given
        setUpTokenQuery();
        RequestIdentifier reqId = RequestIdentifier.create();

        CheckTokenResponse checkResponse = new CheckTokenResponse();
        checkResponse.setStatus(CheckTokenResponse.Status.VALID);

        DecryptTokenResponse decryptResponse = new DecryptTokenResponse();
        decryptResponse.setDecrypted("Username");

        Tags mockTags = new Tags("APK", "Username", "RoleGroup");
        CertificateKeyVault certificate = new CertificateKeyVault("certificate", "value".getBytes(StandardCharsets.UTF_8), "thumb", null, null, mockTags, null);

        doReturn(checkResponse).when(tokenWebServiceTemplate).marshalSendAndReceive(any(CheckToken.class), any(SoapActionCallback.class));
        doReturn(decryptResponse).when(tokenWebServiceTemplate).marshalSendAndReceive(any(DecryptToken.class), any(SoapActionCallback.class));
        doReturn(certificate).when(keyVaultService).getCertificateByUsername(any());

        // When
        Tags tags = testSubject.getTags(reqId);

        // Then
        verify(keyVaultService, times(1)).getCertificateByUsername(anyString());
        assertThat(tags.getUsername()).isEqualTo("Username");
    }

    @Test
    public void testGetTagsNotFound() {
        // Given
        setUpTokenQuery();
        RequestIdentifier reqId = RequestIdentifier.create();

        CheckTokenResponse checkResponse = new CheckTokenResponse();
        checkResponse.setStatus(CheckTokenResponse.Status.VALID);

        DecryptTokenResponse decryptResponse = new DecryptTokenResponse();
        decryptResponse.setDecrypted("Username");

        HttpResponse http = mock(HttpResponse.class);
        ResourceNotFoundException notFound = new ResourceNotFoundException("Not found", http);

        ArgumentCaptor<Map<String,String>> metadataCapture = ArgumentCaptor.forClass(Map.class);

        doReturn(checkResponse).when(tokenWebServiceTemplate).marshalSendAndReceive(any(CheckToken.class), any(SoapActionCallback.class));
        doReturn(decryptResponse).when(tokenWebServiceTemplate).marshalSendAndReceive(any(DecryptToken.class), any(SoapActionCallback.class));
        doThrow(notFound).when(keyVaultService).getCertificateByUsername(any());
        doNothing().when(telemetryClient).trackException(any(), metadataCapture.capture(), any());

        // When
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            Tags tags = testSubject.getTags(reqId);
            assertThat(tags.getUsername()).isEqualTo("Username");
        });

        // Then
        verify(keyVaultService, times(1)).getCertificateByUsername(anyString());
        assertThat(metadataCapture.getValue().get("AppKey")).isNotNull();
    }

    public void setUpTokenQuery() {
        String query = "oid=999&goid=888&tkn=token&apk=APK";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setQueryString(query);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }
}
