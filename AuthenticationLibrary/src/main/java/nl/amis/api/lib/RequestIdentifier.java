package nl.amis.api.lib;

import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

public class RequestIdentifier {
    private static final String OID = "oid";
    private static final String GOID = "goid";
    private static final String TKN = "tkn";
    private static final String THUMBPRINT = "thumbprint";
    private static final String APK = "apk";

    public final String ownerId;
    public final String objectId;
    public final String thumbPrint;
    public final String token;
    public final String appKey;
    public final String queryString;

    private RequestIdentifier(String ownerId, String objectId, String thumbPrint, String token, String appKey) {
        this.ownerId = ownerId;
        this.objectId = objectId;
        this.thumbPrint = thumbPrint;
        this.token = token;
        this.appKey = appKey;
        this.queryString = createQueryString(ownerId, objectId);
    }

    public static RequestIdentifier create() {
        MultiValueMap<String, String> queryParameters = getQueryParameters();
        validate(queryParameters);

        return new RequestIdentifier(
                getValue(queryParameters, OID),
                getValue(queryParameters, GOID),
                getValue(queryParameters, THUMBPRINT),
                getValue(queryParameters, TKN),
                getValue(queryParameters, APK));
    }

    public static void validate() {
        validate(getQueryParameters());
    }

    /**
     * Check required URL parameters
     */
    private static void validate(MultiValueMap<String, String> queryParameters) {
        String oid = getValue(queryParameters, OID);
        Assert.notNull(oid, "422 Geen eigenaarid (oid) meegegeven in aanroep");

        String goid = getValue(queryParameters, GOID);
        Assert.notNull(goid, "422 Geen objectId (goid) meegegeven in aanroep");

        String token = getValue(queryParameters, TKN);
        if (token == null) {
            String thumbPrint = getValue(queryParameters, THUMBPRINT);
            if (thumbPrint == null) {
                throw new IllegalArgumentException("422 Geen token (tkn) of certificaat meegegeven in aanroep");
            }
        }

        String apk = getValue(queryParameters, APK);
        Assert.notNull(apk, "422 Geen applicationKey (apk) meegegeven in aanroep");
    }

    private static String createQueryString(String ownerId, String objectId) {
        return  "?" +
                OID + '=' + ownerId + '&' +
                GOID + '=' + objectId;
    }

    private static MultiValueMap<String, String> getQueryParameters() {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .build().encode().getQueryParams();
    }

    private static String getValue(MultiValueMap<String, String> values, String key) {
        List<String> value = values.get(key);
        if (value == null || value.isEmpty()) {
            return null;
        }

        return value.get(0);
    }
}
