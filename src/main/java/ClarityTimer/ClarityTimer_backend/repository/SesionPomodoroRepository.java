package ClarityTimer.ClarityTimer_backend.repository;

import ClarityTimer.ClarityTimer_backend.model.SesionPomodoro;
import ClarityTimer.ClarityTimer_backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SesionPomodoroRepository extends JpaRepository<SesionPomodoro, Long> {
    List<SesionPomodoro> findByUsuario(Usuario usuario);
    List<SesionPomodoro> findByUsuarioAndFecha(Usuario usuario, LocalDate fecha);
    Optional<SesionPomodoro> findByIdAndUsuario(Long id, Usuario usuario);
    List<SesionPomodoro> findByUsuarioAndCompletadaTrue(Usuario usuario);
}

