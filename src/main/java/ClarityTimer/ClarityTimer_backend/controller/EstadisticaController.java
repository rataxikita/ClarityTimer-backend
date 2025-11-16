package ClarityTimer.ClarityTimer_backend.controller;

import ClarityTimer.ClarityTimer_backend.model.HistorialPuntos;
import ClarityTimer.ClarityTimer_backend.model.Usuario;
import ClarityTimer.ClarityTimer_backend.repository.HistorialPuntosRepository;
import ClarityTimer.ClarityTimer_backend.repository.UsuarioRepository;
import ClarityTimer.ClarityTimer_backend.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/estadisticas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EstadisticaController {

    private final HistorialPuntosRepository historialPuntosRepository;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/puntos/historial")
    public ResponseEntity<List<HistorialPuntos>> getHistorialPuntos(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Usuario usuario = usuarioRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(historialPuntosRepository.findByUsuarioOrderByFechaDesc(usuario));
    }

    @GetMapping("/mi-progreso")
    public ResponseEntity<Map<String, Object>> getMiProgreso(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Usuario usuario = usuarioRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Map<String, Object> progreso = new HashMap<>();
        progreso.put("puntosTotales", usuario.getPuntosTotales());
        progreso.put("puntosDisponibles", usuario.getPuntosDisponibles());
        progreso.put("streakDias", usuario.getStreakDias());
        progreso.put("fechaUltimaSesion", usuario.getFechaUltimaSesion());

        return ResponseEntity.ok(progreso);
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<Usuario>> getRanking() {
        return ResponseEntity.ok(usuarioRepository.findAll().stream()
                .sorted((u1, u2) -> u2.getPuntosTotales().compareTo(u1.getPuntosTotales()))
                .limit(10)
                .toList());
    }
}

