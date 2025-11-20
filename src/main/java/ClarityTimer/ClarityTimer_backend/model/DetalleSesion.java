package ClarityTimer.ClarityTimer_backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "detalles_sesion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleSesion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sesion_id", nullable = false)
    @JsonBackReference
    private SesionPomodoro sesion; // La "boleta" a la que pertenece
    
    @Column(name = "numero_pomodoro", nullable = false)
    private Integer numeroPomodoro; // Número de item en la boleta
    
    @Column(name = "duracion_minutos", nullable = false)
    private Integer duracionMinutos; // Cantidad
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPomodoro tipo; // Tipo de producto
    
    @Column(name = "puntos_otorgados", nullable = false)
    private Integer puntosOtorgados = 0; // Precio unitario (en puntos)
    
    @Column(nullable = false)
    private Boolean completado = false;
    
    @Column(name = "hora_inicio")
    private LocalTime horaInicio;
    
    @Column(name = "hora_fin")
    private LocalTime horaFin;
    
    // Campo opcional para tracking
    @Column(name = "tarea_descripcion")
    private String tareaDescripcion;
}

