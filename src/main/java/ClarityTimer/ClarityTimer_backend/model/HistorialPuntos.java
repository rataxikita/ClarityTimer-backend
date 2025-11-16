package ClarityTimer.ClarityTimer_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_puntos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialPuntos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTransaccion tipo; // GANADO, GASTADO, BONUS
    
    @Column(nullable = false)
    private Integer cantidad; // Positivo para ganado, negativo para gastado
    
    @Column(length = 255)
    private String descripcion; // "Completaste 4 pomodoros", "Compraste Kuromi"
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sesion_id")
    private SesionPomodoro sesion; // Si fue por sesión
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personaje_id")
    private PersonajeSanrio personaje; // Si fue por compra
    
    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();
}

