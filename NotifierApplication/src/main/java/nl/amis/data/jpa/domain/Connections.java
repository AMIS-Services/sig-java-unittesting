package nl.amis.data.jpa.domain;

import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

import static javax.persistence.GenerationType.IDENTITY;


public class Connections {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "client_name")
    private String clientId;

    @Setter
    @Column(name = "session_id")
    private String sessionId;

    @Setter
    @Column(name = "handshake_successfull")
    private Boolean handshakeSuccessfull;

    @Setter
    @Column(name = "connected_at")
    private Instant connectedAt;

    @Setter
    @Column(name = "disconnected_at")
    private Instant disconnectedAt;


}
