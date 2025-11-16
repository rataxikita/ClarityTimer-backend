package ClarityTimer.ClarityTimer_backend.repository;

import ClarityTimer.ClarityTimer_backend.model.DetalleSesion;
import ClarityTimer.ClarityTimer_backend.model.SesionPomodoro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleSesionRepository extends JpaRepository<DetalleSesion, Long> {
    List<DetalleSesion> findBySesion(SesionPomodoro sesion);
}

