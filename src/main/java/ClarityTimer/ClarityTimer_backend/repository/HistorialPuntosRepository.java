package ClarityTimer.ClarityTimer_backend.repository;

import ClarityTimer.ClarityTimer_backend.model.HistorialPuntos;
import ClarityTimer.ClarityTimer_backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialPuntosRepository extends JpaRepository<HistorialPuntos, Long> {
    List<HistorialPuntos> findByUsuarioOrderByFechaDesc(Usuario usuario);
}

