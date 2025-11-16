package ClarityTimer.ClarityTimer_backend.repository;

import ClarityTimer.ClarityTimer_backend.model.CategoriaPersonaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaPersonajeRepository extends JpaRepository<CategoriaPersonaje, Long> {
}

