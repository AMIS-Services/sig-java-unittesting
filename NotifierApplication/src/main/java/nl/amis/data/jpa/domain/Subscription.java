package nl.amis.data.jpa.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
public class Subscription {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String api;

    private String subscriber;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "channel_id")
    private String channelId;

    @Column(name = "subscribed_at")
    private Instant subscribedAt;

    @Setter
    @Column(name = "unsubscribed_at")
    private Instant unsubscribedAt;

    @Setter
    @Column(name = "session_removed_at")
    private Instant sessionRemovedAt;

    public Instant getStart() {
        return this.subscribedAt;
    }

    public Instant getEnd() {
        final Instant result;
        if (unsubscribedAt == null && sessionRemovedAt == null) {
            result=null;
        } else {
            if (unsubscribedAt == null && sessionRemovedAt != null) {
                result = sessionRemovedAt;
            } else if (unsubscribedAt != null && sessionRemovedAt == null) {
                result = unsubscribedAt;
            } else {
                if (unsubscribedAt.isBefore(sessionRemovedAt)) {
                    result=sessionRemovedAt;
                } else {
                    result=unsubscribedAt;
                }
            }
        }
        return result;
    }
}
