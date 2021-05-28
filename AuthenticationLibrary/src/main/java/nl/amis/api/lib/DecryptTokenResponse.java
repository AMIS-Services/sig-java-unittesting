package nl.amis.api.lib;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "decryptionStatus",
        "decrypted"
})
@XmlRootElement(name = "DecryptTokenResponse")
public class DecryptTokenResponse
        implements Serializable
{

    @XmlElement(name = "DecryptionStatus", required = true)
    protected DecryptTokenResponse.DecryptionStatus decryptionStatus;
    @XmlElement(name = "Decrypted")
    protected String decrypted;

    /**
     * Gets the value of the decryptionStatus property.
     *
     * @return
     *     possible object is
     *     {@link DecryptTokenResponse.DecryptionStatus }
     *
     */
    public DecryptTokenResponse.DecryptionStatus getDecryptionStatus() {
        return decryptionStatus;
    }

    /**
     * Sets the value of the decryptionStatus property.
     *
     * @param value
     *     allowed object is
     *     {@link DecryptTokenResponse.DecryptionStatus }
     *
     */
    public void setDecryptionStatus(DecryptTokenResponse.DecryptionStatus value) {
        this.decryptionStatus = value;
    }

    public boolean isSetDecryptionStatus() {
        return (this.decryptionStatus!= null);
    }

    /**
     * Gets the value of the decrypted property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDecrypted() {
        return decrypted;
    }

    /**
     * Sets the value of the decrypted property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDecrypted(String value) {
        this.decrypted = value;
    }

    public boolean isSetDecrypted() {
        return (this.decrypted!= null);
    }


    /**
     * <p>Java class for null.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p>
     * <pre>
     * &lt;simpleType&gt;
     *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *     &lt;enumeration value="succeeded"/&gt;
     *     &lt;enumeration value="failed"/&gt;
     *   &lt;/restriction&gt;
     * &lt;/simpleType&gt;
     * </pre>
     *
     */
    @XmlType(name = "")
    @XmlEnum
    public enum DecryptionStatus {

        @XmlEnumValue("succeeded")
        SUCCEEDED("succeeded"),
        @XmlEnumValue("failed")
        FAILED("failed");
        private final String value;

        DecryptionStatus(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static DecryptTokenResponse.DecryptionStatus fromValue(String v) {
            for (DecryptTokenResponse.DecryptionStatus c: DecryptTokenResponse.DecryptionStatus.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

    }

}
