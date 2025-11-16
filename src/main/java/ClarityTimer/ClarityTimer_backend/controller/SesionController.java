package ClarityTimer.ClarityTimer_backend.controller;

import ClarityTimer.ClarityTimer_backend.dto.SesionPomodoroRequest;
import ClarityTimer.ClarityTimer_backend.model.SesionPomodoro;
import ClarityTimer.ClarityTimer_backend.security.UserPrincipal;
import ClarityTimer.ClarityTimer_backend.service.SesionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sesiones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SesionController {

    private final SesionService sesionService;

    @PostMapping
    public ResponseEntity<SesionPomodoro> crearSesion(
            @Valid @RequestBody SesionPomodoroRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(sesionService.crearSesion(request, userPrincipal.getId()));
    }

    @GetMapping
    public ResponseEntity<List<SesionPomodoro>> getMisSesiones(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(sesionService.getMisSesiones(userPrincipal.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SesionPomodoro> getSesionById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(sesionService.getSesionById(id, userPrincipal.getId()));
    }

    @PostMapping("/{id}/completar")
    public ResponseEntity<Void> completarSesion(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        sesionService.completarSesion(id, userPrincipal.getId());
        return ResponseEntity.ok().build();
    }
}

