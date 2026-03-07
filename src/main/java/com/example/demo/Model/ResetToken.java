package com.example.demo.Model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "reset_token")
public class ResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public ResetToken() {}

    public ResetToken(String token, Usuario usuario, Instant expiration) {
        this.token = token;
        this.usuario = usuario;
        this.expiration = expiration;
    }

    // getters y setters
    public Long getId() { return id; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Instant getExpiration() { return expiration; }
    public void setExpiration(Instant expiration) { this.expiration = expiration; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
