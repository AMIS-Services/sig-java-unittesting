package nl.amis.data.jpa.service;

import java.time.Instant;
import java.util.List;

import nl.amis.data.jpa.domain.Subscription;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {

    @Query(value="SELECT n FROM Subscription n WHERE upper(n.api) = upper(?1) and n.unsubscribedAt is null and n.sessionRemovedAt is null")
    public Iterable<Subscription> findAllActive(final String api);

    @Query(value="SELECT n FROM Subscription n WHERE upper(n.api) = upper(?1) and n.subscribedAt > ?2 and n.unsubscribedAt is null and n.sessionRemovedAt is null")
    public Iterable<Subscription> findNewActive(final String api, final Instant last);

    @Query(value="SELECT n FROM Subscription n WHERE upper(n.api) = upper(?1) and (n.unsubscribedAt > ?2 or n.sessionRemovedAt > ?2)")
    public Iterable<Subscription> findEnded(final String api, final Instant subscribedAt);

}
