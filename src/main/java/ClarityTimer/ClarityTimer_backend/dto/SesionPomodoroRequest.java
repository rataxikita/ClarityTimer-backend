package ClarityTimer.ClarityTimer_backend.dto;

import ClarityTimer.ClarityTimer_backend.model.DetalleSesion;
import ClarityTimer.ClarityTimer_backend.model.SesionPomodoro;
import ClarityTimer.ClarityTimer_backend.model.TipoPomodoro;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SesionPomodoroRequest {
    @NotNull
    private Long personajeUsadoId;

    private List<DetalleSesionRequest> detalles = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetalleSesionRequest {
        @NotNull
        private Integer numeroPomodoro;

        @NotNull
        private Integer duracionMinutos;

        @NotNull
        private TipoPomodoro tipo;

        private Integer puntosOtorgados = 0;

        private Boolean completado = false;

        private String horaInicio;

        private String horaFin;

        private String tareaDescripcion;
    }

    public SesionPomodoro toEntity(ClarityTimer.ClarityTimer_backend.model.Usuario usuario, 
                                    ClarityTimer.ClarityTimer_backend.model.PersonajeSanrio personaje) {
        SesionPomodoro sesion = SesionPomodoro.builder()
                .usuario(usuario)
                .fecha(LocalDate.now())
                .horaInicio(LocalTime.now())
                .personajeUsado(personaje)
                .completada(false)
                .totalPomodoros(detalles.size())
                .tiempoTotalMinutos(detalles.stream()
                        .mapToInt(DetalleSesionRequest::getDuracionMinutos)
                        .sum())
                .puntosGanados(0)
                .puntosBonus(0)
                .build();

        List<DetalleSesion> detallesEntity = new ArrayList<>();
        for (DetalleSesionRequest detalleReq : detalles) {
            DetalleSesion detalle = DetalleSesion.builder()
                    .sesion(sesion)
                    .numeroPomodoro(detalleReq.getNumeroPomodoro())
                    .duracionMinutos(detalleReq.getDuracionMinutos())
                    .tipo(detalleReq.getTipo())
                    .puntosOtorgados(detalleReq.getPuntosOtorgados())
                    .completado(detalleReq.getCompletado())
                    .tareaDescripcion(detalleReq.getTareaDescripcion())
                    .build();

            if (detalleReq.getHoraInicio() != null) {
                detalle.setHoraInicio(LocalTime.parse(detalleReq.getHoraInicio()));
            }
            if (detalleReq.getHoraFin() != null) {
                detalle.setHoraFin(LocalTime.parse(detalleReq.getHoraFin()));
            }

            detallesEntity.add(detalle);
        }

        sesion.setDetalles(detallesEntity);
        return sesion;
    }
}

