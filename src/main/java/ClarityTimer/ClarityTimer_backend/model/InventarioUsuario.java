package ClarityTimer.ClarityTimer_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventario_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "password", "inventario", "configuracion", "sesiones", "historialPuntos"})
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "personaje_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PersonajeSanrio personaje;
    
    @Column(name = "fecha_obtencion", nullable = false)
    private LocalDateTime fechaObtencion = LocalDateTime.now();
    
    @Column(name = "puntos_gastados", nullable = false)
    private Integer puntosGastados; // Cuánto costó cuando lo compró
    
    @Column(name = "es_activo", nullable = false)
    private Boolean esActivo = false; // Si es el personaje actualmente en uso
    
    @Column(name = "veces_usado", nullable = false)
    private Integer vecesUsado = 0; // Estadística

    @Column(name = "codigo_certificado", unique = true)
    private String codigoCertificado; // UUID del certificado
}

