package ClarityTimer.ClarityTimer_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 50)
    private String username;
    
    @Column(unique = true, nullable = false, length = 100)
    private String email;
    
    @Column(nullable = false)
    private String password; // BCrypt
    
    private String nombre;
    private String apellido;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol = Rol.CLIENTE;
    
    // 🎮 SISTEMA DE PUNTOS
    @Column(name = "puntos_totales", nullable = false)
    private Integer puntosTotales = 0; // Histórico (nunca baja)
    
    @Column(name = "puntos_disponibles", nullable = false)
    private Integer puntosDisponibles = 0; // Para gastar (baja al comprar)
    
    @Column(name = "streak_dias", nullable = false)
    private Integer streakDias = 0; // Días consecutivos
    
    @Column(name = "fecha_ultima_sesion")
    private LocalDate fechaUltimaSesion;
    
    private LocalDateTime fechaRegistro = LocalDateTime.now();
    private Boolean activo = true;
    
    // Relaciones
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<SesionPomodoro> sesiones = new ArrayList<>();
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<InventarioUsuario> inventario = new ArrayList<>();
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<HistorialPuntos> historialPuntos = new ArrayList<>();
    
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private ConfiguracionUsuario configuracion;
}
