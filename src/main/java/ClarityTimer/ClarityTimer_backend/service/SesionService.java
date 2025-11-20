package ClarityTimer.ClarityTimer_backend.service;

import ClarityTimer.ClarityTimer_backend.dto.SesionPomodoroRequest;
import ClarityTimer.ClarityTimer_backend.exception.ResourceNotFoundException;
import ClarityTimer.ClarityTimer_backend.model.*;
import ClarityTimer.ClarityTimer_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SesionService {

    private final SesionPomodoroRepository sesionRepository;
    private final UsuarioRepository usuarioRepository;
    private final HistorialPuntosRepository historialPuntosRepository;
    private final InventarioUsuarioRepository inventarioRepository;
    private final PersonajeSanrioRepository personajeRepository;

    public SesionPomodoro crearSesion(SesionPomodoroRequest request, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        PersonajeSanrio personaje = personajeRepository.findById(request.getPersonajeUsadoId())
                .orElseThrow(() -> new ResourceNotFoundException("Personaje no encontrado"));

        SesionPomodoro sesion = request.toEntity(usuario, personaje);
        
        System.out.println("🎮 Creando sesión:");
        System.out.println("   - Total pomodoros: " + sesion.getTotalPomodoros());
        System.out.println("   - Tiempo total minutos: " + sesion.getTiempoTotalMinutos());
        System.out.println("   - Detalles count: " + sesion.getDetalles().size());
        
        SesionPomodoro sesionGuardada = sesionRepository.save(sesion);
        
        System.out.println("✅ Sesión guardada con ID: " + sesionGuardada.getId());
        System.out.println("   - Tiempo total minutos después de guardar: " + sesionGuardada.getTiempoTotalMinutos());
        
        return sesionGuardada;
    }

    public List<SesionPomodoro> getMisSesiones(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        List<SesionPomodoro> sesiones = sesionRepository.findByUsuario(usuario);
        
        System.out.println("📊 Obteniendo sesiones del usuario " + usuario.getUsername());
        System.out.println("   - Total sesiones encontradas: " + sesiones.size());
        if (!sesiones.isEmpty()) {
            SesionPomodoro primera = sesiones.get(0);
            System.out.println("   - Primera sesión ID: " + primera.getId());
            System.out.println("   - tiempoTotalMinutos: " + primera.getTiempoTotalMinutos());
            System.out.println("   - totalPomodoros: " + primera.getTotalPomodoros());
        }
        
        return sesiones;
    }

    public SesionPomodoro getSesionById(Long sesionId, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return sesionRepository.findByIdAndUsuario(sesionId, usuario)
                .orElseThrow(() -> new ResourceNotFoundException("Sesión no encontrada"));
    }

    @Transactional
    public void completarSesion(Long sesionId, Long usuarioId) {
        SesionPomodoro sesion = sesionRepository.findByIdAndUsuario(sesionId, 
                usuarioRepository.findById(usuarioId)
                        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado")))
                .orElseThrow(() -> new ResourceNotFoundException("Sesión no encontrada"));

        // Verificar si ya está completada
        if (sesion.getCompletada()) {
            System.out.println("⚠️ Intento de completar sesión ya completada: " + sesionId);
            return; // Ya está completada, no hacer nada
        }

        // 1. Marcar como completada
        sesion.setCompletada(true);
        sesion.setHoraFin(LocalTime.now());

        // 2. 🎮 CALCULAR PUNTOS
        int puntosGanados = 0;

        // Contar pomodoros de TRABAJO completados
        long pomodorosTrabajo = sesion.getDetalles().stream()
                .filter(d -> d.getTipo() == TipoPomodoro.TRABAJO && d.getCompletado())
                .count();

        puntosGanados = (int) pomodorosTrabajo * 10; // 10 puntos por pomodoro

        // 3. 🎮 BONUS POR STREAK
        Usuario usuario = sesion.getUsuario();
        int puntosBonus = calcularBonusStreak(usuario);

        sesion.setPuntosGanados(puntosGanados);
        sesion.setPuntosBonus(puntosBonus);

        int totalPuntos = puntosGanados + puntosBonus;

        // 4. Actualizar puntos del usuario
        usuario.setPuntosTotales(usuario.getPuntosTotales() + totalPuntos);
        usuario.setPuntosDisponibles(usuario.getPuntosDisponibles() + totalPuntos);
        usuario.setFechaUltimaSesion(LocalDate.now());

        // 5. Actualizar streak
        actualizarStreak(usuario);

        // 6. Registrar en historial
        HistorialPuntos historial = HistorialPuntos.builder()
                .usuario(usuario)
                .tipo(TipoTransaccion.GANADO)
                .cantidad(totalPuntos)
                .descripcion("Completaste " + pomodorosTrabajo + " pomodoros")
                .sesion(sesion)
                .fecha(LocalDateTime.now())
                .build();

        historialPuntosRepository.save(historial);

        sesionRepository.save(sesion);
        usuarioRepository.save(usuario);
    }

    private int calcularBonusStreak(Usuario usuario) {
        int streak = usuario.getStreakDias();
        if (streak >= 30) return 50;  // 1 mes
        if (streak >= 7) return 20;   // 1 semana
        if (streak >= 3) return 10;   // 3 días
        return 0;
    }

    private void actualizarStreak(Usuario usuario) {
        LocalDate hoy = LocalDate.now();
        LocalDate ultimaSesion = usuario.getFechaUltimaSesion();

        if (ultimaSesion == null) {
            usuario.setStreakDias(1);
        } else if (ultimaSesion.equals(hoy.minusDays(1))) {
            // Día consecutivo
            usuario.setStreakDias(usuario.getStreakDias() + 1);
        } else if (!ultimaSesion.equals(hoy)) {
            // Se rompió el streak
            usuario.setStreakDias(1);
        }
        // Si es el mismo día, no hacer nada
    }

    @Transactional
    public void eliminarSesion(Long sesionId, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        
        SesionPomodoro sesion = sesionRepository.findByIdAndUsuario(sesionId, usuario)
                .orElseThrow(() -> new ResourceNotFoundException("Sesión no encontrada"));

        // Solo se pueden eliminar sesiones NO completadas
        if (sesion.getCompletada()) {
            throw new IllegalStateException("No se puede eliminar una sesión completada");
        }

        sesionRepository.delete(sesion);
    }
}

