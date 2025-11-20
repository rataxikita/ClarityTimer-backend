package ClarityTimer.ClarityTimer_backend.controller;

import ClarityTimer.ClarityTimer_backend.model.Nota;
import ClarityTimer.ClarityTimer_backend.security.UserPrincipal;
import ClarityTimer.ClarityTimer_backend.service.NotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotaController {

    private final NotaService notaService;

    @GetMapping
    public ResponseEntity<List<Nota>> getNotas(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(notaService.getNotasByUsuario(userPrincipal.getId()));
    }

    @PostMapping
    public ResponseEntity<Nota> createNota(
            @RequestBody Nota nota,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(notaService.createNota(userPrincipal.getId(), nota));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Nota> updateNota(
            @PathVariable Long id,
            @RequestBody Nota nota,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(notaService.updateNota(id, userPrincipal.getId(), nota));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNota(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        notaService.deleteNota(id, userPrincipal.getId());
        return ResponseEntity.ok().build();
    }
}
