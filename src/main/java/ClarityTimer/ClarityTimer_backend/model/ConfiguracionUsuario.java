package ClarityTimer.ClarityTimer_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "configuraciones_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfiguracionUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", unique = true, nullable = false)
    private Usuario usuario;
    
    // Configuraciones de tiempo
    @Column(name = "tiempo_trabajo")
    private Integer tiempoTrabajo = 25;
    
    @Column(name = "tiempo_descanso_corto")
    private Integer tiempoDescansoCorto = 5;
    
    @Column(name = "tiempo_descanso_largo")
    private Integer tiempoDescansoLargo = 15;
    
    @Column(name = "sesiones_antes_descanso_largo")
    private Integer sesionesAntesDescansoLargo = 4;
    
    // Configuraciones de audio
    @Column(name = "sonido_notificacion", length = 50)
    private String sonidoNotificacion = "Campana.mp3";
    
    @Column(name = "volumen_notificacion")
    private Integer volumenNotificacion = 50;
    
    @Column(name = "sonido_ambiente", length = 50)
    private String sonidoAmbiente = "Olas.mp3";
    
    // Configuraciones de comportamiento
    @Column(name = "modo_automatico")
    private Boolean modoAutomatico = false;
    
    @Column(name = "mostrar_notificaciones")
    private Boolean mostrarNotificaciones = true;
    
    // 🎮 GAMIFICACIÓN
    @Column(name = "mostrar_animaciones")
    private Boolean mostrarAnimaciones = true;
    
    @Column(name = "notificar_logros")
    private Boolean notificarLogros = true;
}

