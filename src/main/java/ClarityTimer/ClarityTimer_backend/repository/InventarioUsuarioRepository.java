package ClarityTimer.ClarityTimer_backend.repository;

import ClarityTimer.ClarityTimer_backend.model.InventarioUsuario;
import ClarityTimer.ClarityTimer_backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioUsuarioRepository extends JpaRepository<InventarioUsuario, Long> {
    List<InventarioUsuario> findByUsuario(Usuario usuario);
    Optional<InventarioUsuario> findByUsuarioIdAndPersonajeId(Long usuarioId, Long personajeId);
    Boolean existsByUsuarioIdAndPersonajeId(Long usuarioId, Long personajeId);
    Optional<InventarioUsuario> findByUsuarioAndEsActivoTrue(Usuario usuario);
    
    @Modifying
    @Query("UPDATE InventarioUsuario i SET i.esActivo = false WHERE i.usuario.id = :usuarioId")
    void desactivarTodosDelUsuario(@Param("usuarioId") Long usuarioId);
}

