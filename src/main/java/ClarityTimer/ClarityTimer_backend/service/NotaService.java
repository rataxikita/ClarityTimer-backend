package ClarityTimer.ClarityTimer_backend.service;

import ClarityTimer.ClarityTimer_backend.model.Nota;
import ClarityTimer.ClarityTimer_backend.model.Usuario;
import ClarityTimer.ClarityTimer_backend.repository.NotaRepository;
import ClarityTimer.ClarityTimer_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotaService {

    private final NotaRepository notaRepository;
    private final UsuarioRepository usuarioRepository;

    public List<Nota> getNotasByUsuario(Long usuarioId) {
        return notaRepository.findByUsuarioIdOrderByPinnedDescCreatedAtDesc(usuarioId);
    }

    @Transactional
    public Nota createNota(Long usuarioId, Nota nota) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        nota.setUsuario(usuario);
        return notaRepository.save(nota);
    }

    @Transactional
    public Nota updateNota(Long notaId, Long usuarioId, Nota notaDetails) {
        Nota nota = notaRepository.findById(notaId)
                .orElseThrow(() -> new RuntimeException("Nota no encontrada"));

        if (!nota.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No tienes permiso para editar esta nota");
        }

        nota.setContent(notaDetails.getContent());
        nota.setCategory(notaDetails.getCategory());
        nota.setCompleted(notaDetails.getCompleted());
        nota.setPinned(notaDetails.getPinned());

        return notaRepository.save(nota);
    }

    @Transactional
    public void deleteNota(Long notaId, Long usuarioId) {
        Nota nota = notaRepository.findById(notaId)
                .orElseThrow(() -> new RuntimeException("Nota no encontrada"));

        if (!nota.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("No tienes permiso para eliminar esta nota");
        }

        notaRepository.delete(nota);
    }
}
