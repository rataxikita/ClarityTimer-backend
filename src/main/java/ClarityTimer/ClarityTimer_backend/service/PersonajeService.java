package ClarityTimer.ClarityTimer_backend.service;

import ClarityTimer.ClarityTimer_backend.dto.CompraResponse;
import ClarityTimer.ClarityTimer_backend.exception.BadRequestException;
import ClarityTimer.ClarityTimer_backend.exception.ResourceNotFoundException;
import ClarityTimer.ClarityTimer_backend.model.*;
import ClarityTimer.ClarityTimer_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonajeService {

    private final PersonajeSanrioRepository personajeRepository;
    private final UsuarioRepository usuarioRepository;
    private final InventarioUsuarioRepository inventarioRepository;
    private final HistorialPuntosRepository historialPuntosRepository;

    public List<PersonajeSanrio> getAllPersonajes() {
        List<PersonajeSanrio> personajes = personajeRepository.findByDisponibleTrueOrderByOrdenTiendaAsc();
        System.out.println("🔍 DEBUG: getAllPersonajes() - Total personajes encontrados: " + personajes.size());
        personajes.forEach(p -> System.out.println("  - " + p.getNombre() + " (ID: " + p.getId() + ", Disponible: " + p.getDisponible() + ")"));
        return personajes;
    }

    public PersonajeSanrio getPersonajeById(Long id) {
        return personajeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Personaje no encontrado"));
    }

    public List<PersonajeSanrio> getPersonajesByCategoria(Long categoriaId) {
        return personajeRepository.findByCategoriaId(categoriaId);
    }

    public List<PersonajeSanrio> getPersonajesDisponibles(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        List<PersonajeSanrio> todos = personajeRepository.findByDisponibleTrueOrderByOrdenTiendaAsc();
        List<InventarioUsuario> inventario = inventarioRepository.findByUsuario(usuario);

        // Filtrar solo los que ya tiene (no filtrar por puntos - la tienda debe mostrar todos)
        // El frontend se encargará de deshabilitar el botón si no tiene puntos suficientes
        return todos.stream()
                .filter(p -> inventario.stream()
                        .noneMatch(i -> i.getPersonaje().getId().equals(p.getId())))
                .toList();
    }

    public List<InventarioUsuario> getPersonajesDesbloqueados(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        List<InventarioUsuario> inventario = inventarioRepository.findByUsuario(usuario);
        System.out.println("🔍 DEBUG: getPersonajesDesbloqueados() - Usuario ID: " + usuarioId + ", Inventario encontrado: " + inventario.size());
        inventario.forEach(inv -> System.out.println("  - Personaje: " + (inv.getPersonaje() != null ? inv.getPersonaje().getNombre() : "NULL") + " (ID: " + inv.getId() + ")"));
        return inventario;
    }

    @Transactional
    public CompraResponse comprarPersonaje(Long personajeId, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        PersonajeSanrio personaje = personajeRepository.findById(personajeId)
                .orElseThrow(() -> new ResourceNotFoundException("Personaje no encontrado"));

        // 1. Validar que no lo tenga ya
        boolean yaLoTiene = inventarioRepository.existsByUsuarioIdAndPersonajeId(usuarioId, personajeId);

        if (yaLoTiene) {
            throw new BadRequestException("Ya tienes este personaje");
        }

        // 2. Validar puntos suficientes
        if (usuario.getPuntosDisponibles() < personaje.getPrecioPuntos()) {
            throw new BadRequestException(
                    "Puntos insuficientes. Necesitas " + personaje.getPrecioPuntos() +
                            " pero tienes " + usuario.getPuntosDisponibles()
            );
        }

        // 3. Validar que esté disponible
        if (!personaje.getDisponible()) {
            throw new BadRequestException("Este personaje no está disponible");
        }

        // 4. 🎮 REALIZAR COMPRA
        usuario.setPuntosDisponibles(
                usuario.getPuntosDisponibles() - personaje.getPrecioPuntos()
        );

        // 5. Agregar al inventario
        InventarioUsuario inventario = InventarioUsuario.builder()
                .usuario(usuario)
                .personaje(personaje)
                .fechaObtencion(LocalDateTime.now())
                .puntosGastados(personaje.getPrecioPuntos())
                .esActivo(false)
                .vecesUsado(0)
                .build();

        InventarioUsuario inventarioGuardado = inventarioRepository.save(inventario);
        System.out.println("🔍 DEBUG: comprarPersonaje() - Inventario guardado con ID: " + inventarioGuardado.getId() + 
                ", Personaje: " + inventarioGuardado.getPersonaje().getNombre() + 
                ", Usuario ID: " + usuarioId);

        // 6. Registrar en historial
        HistorialPuntos historial = HistorialPuntos.builder()
                .usuario(usuario)
                .tipo(TipoTransaccion.GASTADO)
                .cantidad(-personaje.getPrecioPuntos())
                .descripcion("Compraste " + personaje.getNombre())
                .personaje(personaje)
                .fecha(LocalDateTime.now())
                .build();

        historialPuntosRepository.save(historial);

        usuarioRepository.save(usuario);

        return CompraResponse.builder()
                .exito(true)
                .mensaje("¡Felicidades! Desbloqueaste " + personaje.getNombre())
                .puntosRestantes(usuario.getPuntosDisponibles())
                .build();
    }

    @Transactional
    public void activarPersonaje(Long personajeId, Long usuarioId) {
        // 1. Desactivar personaje actual
        inventarioRepository.desactivarTodosDelUsuario(usuarioId);

        // 2. Activar el nuevo
        InventarioUsuario inventario = inventarioRepository
                .findByUsuarioIdAndPersonajeId(usuarioId, personajeId)
                .orElseThrow(() -> new ResourceNotFoundException("No tienes este personaje"));

        inventario.setEsActivo(true);
        inventario.setVecesUsado(inventario.getVecesUsado() + 1);

        inventarioRepository.save(inventario);
    }

    // Método de diagnóstico
    public Object getDiagnostico() {
        long totalPersonajes = personajeRepository.count();
        long disponibles = personajeRepository.findByDisponibleTrue().size();
        long noDisponibles = totalPersonajes - disponibles;
        
        List<PersonajeSanrio> todos = personajeRepository.findAll();
        
        java.util.Map<String, Object> diagnostico = new java.util.HashMap<>();
        diagnostico.put("totalPersonajes", totalPersonajes);
        diagnostico.put("disponibles", disponibles);
        diagnostico.put("noDisponibles", noDisponibles);
        diagnostico.put("esperados", 10);
        diagnostico.put("personajes", todos.stream().map(p -> java.util.Map.of(
            "id", p.getId(),
            "nombre", p.getNombre(),
            "disponible", p.getDisponible(),
            "precioPuntos", p.getPrecioPuntos(),
            "ordenTienda", p.getOrdenTienda()
        )).collect(java.util.stream.Collectors.toList()));
        
        return diagnostico;
    }
}

