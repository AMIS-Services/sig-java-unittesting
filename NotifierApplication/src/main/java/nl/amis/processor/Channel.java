package nl.amis.processor;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class Channel {

    public static final String DOUBLE_HASHTAG = "##";

    private final String subscriber;
    private final String value;
    private final String channelId;
    private final String credential;

    /**
     * Constructor method
     * @param secret a name that uniquely identifies a channel
     * @param value a combined value of channel name, double hash tag and password
     */
    public Channel(final String secret, final String value) {
        this.subscriber = secret;
        this.value = value;
        final int pos = StringUtils.isEmpty(value) ? 0 : value.indexOf(DOUBLE_HASHTAG);
        if (pos > 0) {
            this.channelId = value.substring(0, pos);
            this.credential = value.substring(pos+2);
        } else {
            this.channelId = secret;
            this.credential = StringUtils.EMPTY;
        }
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof Channel) {
            if (this.subscriber.equals(((Channel) other).getSubscriber())) {
                return true;
            }
        }
        return false;
    }
}