package ClarityTimer.ClarityTimer_backend.dto;

import ClarityTimer.ClarityTimer_backend.model.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponse {
    private Long id;
    private String username;
    private String email;
    private String nombre;
    private String apellido;
    private Rol rol;
    private Integer puntosTotales;
    private Integer puntosDisponibles;
    private Integer streakDias;
    private Long personajeActivoId;
    private String personajeActivoNombre;
}

