package ClarityTimer.ClarityTimer_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "personajes_sanrio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonajeSanrio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 100)
    private String nombre; // Cinnamoroll, Hello Kitty, My Melody, etc.
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private CategoriaPersonaje categoria;
    
    // 🎮 SISTEMA DE TIENDA
    @Column(name = "precio_puntos", nullable = false)
    private Integer precioPuntos; // Precio en puntos
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rareza rareza = Rareza.COMUN; // Común, Raro, Épico, Legendario
    
    @Column(name = "imagen_estudio", length = 255)
    private String imagenEstudio; // ruta: /characters/cinnamoroll-study.png
    
    @Column(name = "imagen_descanso", length = 255)
    private String imagenDescanso; // ruta: /characters/cinnamoroll-break.png
    
    @Column(nullable = false)
    private Boolean disponible = true; // Se puede comprar
    
    @Column(nullable = false)
    private Boolean esDefault = false; // Si es el personaje inicial gratuito
    
    @Column(name = "orden_tienda")
    private Integer ordenTienda = 0; // Para ordenar en la tienda
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}

