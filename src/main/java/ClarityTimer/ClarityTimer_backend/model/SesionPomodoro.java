package ClarityTimer.ClarityTimer_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sesiones_pomodoro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SesionPomodoro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnoreProperties({"sesiones", "inventario", "historialPuntos", "password", "hibernateLazyInitializer", "handler"})
    private Usuario usuario;
    
    @Column(nullable = false)
    private LocalDate fecha;
    
    @Column(name = "hora_inicio")
    private LocalTime horaInicio;
    
    @Column(name = "hora_fin")
    private LocalTime horaFin;
    
    // 🎮 GAMIFICACIÓN
    @Column(name = "total_pomodoros")
    @JsonProperty("totalPomodoros")
    private Integer totalPomodoros = 0; // Cantidad de productos en la boleta
    
    @Column(name = "tiempo_total_minutos")
    @JsonProperty("tiempoTotalMinutos")
    private Integer tiempoTotalMinutos = 0;
    
    @Column(name = "puntos_ganados", nullable = false)
    private Integer puntosGanados = 0; // Monto total de la boleta (en puntos)
    
    @Column(name = "puntos_bonus")
    private Integer puntosBonus = 0; // Bonus por streak
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personaje_usado_id")
    @JsonIgnoreProperties({"inventarios", "hibernateLazyInitializer", "handler"})
    private PersonajeSanrio personajeUsado; // Personaje activo durante la sesión
    
    @Column(nullable = false)
    private Boolean completada = false;
    
    // Relaciones
    @OneToMany(mappedBy = "sesion", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<DetalleSesion> detalles = new ArrayList<>();
}

