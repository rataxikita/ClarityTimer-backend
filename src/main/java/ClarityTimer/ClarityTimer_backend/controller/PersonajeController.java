package ClarityTimer.ClarityTimer_backend.controller;

import ClarityTimer.ClarityTimer_backend.dto.CompraResponse;
import ClarityTimer.ClarityTimer_backend.model.InventarioUsuario;
import ClarityTimer.ClarityTimer_backend.model.PersonajeSanrio;
import ClarityTimer.ClarityTimer_backend.security.UserPrincipal;
import ClarityTimer.ClarityTimer_backend.service.PersonajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/personajes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PersonajeController {

    private final PersonajeService personajeService;

    @GetMapping
    public ResponseEntity<List<PersonajeSanrio>> getAllPersonajes() {
        List<PersonajeSanrio> personajes = personajeService.getAllPersonajes();
        System.out.println(
                "🔍 DEBUG: PersonajeController.getAllPersonajes() - Devolviendo " + personajes.size() + " personajes");
        return ResponseEntity.ok(personajes);
    }

    // Endpoint para Admin/Vendedor: devuelve TODOS los personajes (incluyendo no
    // disponibles)
    @GetMapping("/admin/all")
    public ResponseEntity<List<PersonajeSanrio>> getAllPersonajesAdmin() {
        return ResponseEntity.ok(personajeService.getAllPersonajesAdmin());
    }

    // Endpoint para Admin/Vendedor: devuelve TODAS las adopciones
    @GetMapping("/admin/adopciones")
    public ResponseEntity<List<InventarioUsuario>> getAllAdopciones() {
        return ResponseEntity.ok(personajeService.getAllAdopciones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonajeSanrio> getPersonajeById(@PathVariable Long id) {
        return ResponseEntity.ok(personajeService.getPersonajeById(id));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<PersonajeSanrio>> getPersonajesByCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(personajeService.getPersonajesByCategoria(categoriaId));
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<PersonajeSanrio>> getPersonajesDisponibles(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(personajeService.getPersonajesDisponibles(userPrincipal.getId()));
    }

    @GetMapping("/desbloqueados")
    public ResponseEntity<List<InventarioUsuario>> getPersonajesDesbloqueados(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(personajeService.getPersonajesDesbloqueados(userPrincipal.getId()));
    }

    @PostMapping("/{id}/comprar")
    public ResponseEntity<CompraResponse> comprarPersonaje(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(personajeService.comprarPersonaje(id, userPrincipal.getId()));
    }

    @PutMapping("/{id}/activar")
    public ResponseEntity<Void> activarPersonaje(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        personajeService.activarPersonaje(id, userPrincipal.getId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonajeSanrio> updatePersonaje(
            @PathVariable Long id,
            @RequestBody PersonajeSanrio personaje) {
        return ResponseEntity.ok(personajeService.updatePersonaje(id, personaje));
    }

    // Endpoint de diagnóstico (público para debugging)
    @GetMapping("/diagnostico")
    public ResponseEntity<Object> diagnostico() {
        return ResponseEntity.ok(personajeService.getDiagnostico());
    }
}
