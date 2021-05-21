package nl.amis.data.jpa.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor
public class Notification implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "channel_id", nullable = false)
	private String channelId;

	@Column(name = "message", nullable = false)
	private String message;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	public Notification(final String channelId, final String message) {
		this.channelId = channelId;
		this.message = message;
		this.createdAt = Instant.now();
	}

}
