package nl.amis.api.lib;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "status"
})
@XmlRootElement(name = "CheckTokenResponse")
public class CheckTokenResponse
        implements Serializable
{

    @XmlElement(name = "Status", required = true)
    protected CheckTokenResponse.Status status;

    /**
     * Gets the value of the status property.
     *
     * @return
     *     possible object is
     *     {@link CheckTokenResponse.Status }
     *
     */
    public CheckTokenResponse.Status getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value
     *     allowed object is
     *     {@link CheckTokenResponse.Status }
     *
     */
    public void setStatus(CheckTokenResponse.Status value) {
        this.status = value;
    }

    public boolean isSetStatus() {
        return (this.status!= null);
    }


    /**
     * <p>Java class for null.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p>
     * <pre>
     * &lt;simpleType&gt;
     *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *     &lt;enumeration value="valid"/&gt;
     *     &lt;enumeration value="invalid"/&gt;
     *     &lt;enumeration value="expired"/&gt;
     *   &lt;/restriction&gt;
     * &lt;/simpleType&gt;
     * </pre>
     *
     */
    @XmlType(name = "")
    @XmlEnum
    public enum Status {

        @XmlEnumValue("valid")
        VALID("valid"),
        @XmlEnumValue("invalid")
        INVALID("invalid"),
        @XmlEnumValue("expired")
        EXPIRED("expired");
        private final String value;

        Status(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static CheckTokenResponse.Status fromValue(String v) {
            for (CheckTokenResponse.Status c: CheckTokenResponse.Status.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

    }

}
