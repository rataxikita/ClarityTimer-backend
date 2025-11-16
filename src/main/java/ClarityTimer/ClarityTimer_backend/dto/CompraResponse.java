package ClarityTimer.ClarityTimer_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompraResponse {
    private Boolean exito;
    private String mensaje;
    private Integer puntosRestantes;
}

