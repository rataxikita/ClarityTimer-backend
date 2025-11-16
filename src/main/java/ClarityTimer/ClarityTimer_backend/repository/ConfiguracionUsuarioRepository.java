package ClarityTimer.ClarityTimer_backend.repository;

import ClarityTimer.ClarityTimer_backend.model.ConfiguracionUsuario;
import ClarityTimer.ClarityTimer_backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracionUsuarioRepository extends JpaRepository<ConfiguracionUsuario, Long> {
    Optional<ConfiguracionUsuario> findByUsuario(Usuario usuario);
}

