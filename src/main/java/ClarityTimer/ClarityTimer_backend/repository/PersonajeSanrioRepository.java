package ClarityTimer.ClarityTimer_backend.repository;

import ClarityTimer.ClarityTimer_backend.model.PersonajeSanrio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonajeSanrioRepository extends JpaRepository<PersonajeSanrio, Long> {
    Optional<PersonajeSanrio> findByNombre(String nombre);
    List<PersonajeSanrio> findByDisponibleTrue();
    List<PersonajeSanrio> findByEsDefaultTrueAndDisponibleTrue();
    List<PersonajeSanrio> findByCategoriaId(Long categoriaId);
    List<PersonajeSanrio> findByDisponibleTrueOrderByOrdenTiendaAsc();
}

