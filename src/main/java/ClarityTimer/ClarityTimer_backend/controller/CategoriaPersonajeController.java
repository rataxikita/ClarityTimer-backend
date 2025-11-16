package ClarityTimer.ClarityTimer_backend.controller;

import ClarityTimer.ClarityTimer_backend.model.CategoriaPersonaje;
import ClarityTimer.ClarityTimer_backend.model.PersonajeSanrio;
import ClarityTimer.ClarityTimer_backend.repository.CategoriaPersonajeRepository;
import ClarityTimer.ClarityTimer_backend.repository.PersonajeSanrioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoriaPersonajeController {

    private final CategoriaPersonajeRepository categoriaRepository;
    private final PersonajeSanrioRepository personajeRepository;

    @GetMapping
    public ResponseEntity<List<CategoriaPersonaje>> getAllCategorias() {
        return ResponseEntity.ok(categoriaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaPersonaje> getCategoriaById(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada")));
    }

    @GetMapping("/{id}/personajes")
    public ResponseEntity<List<PersonajeSanrio>> getPersonajesByCategoria(@PathVariable Long id) {
        return ResponseEntity.ok(personajeRepository.findByCategoriaId(id));
    }
}

