package ClarityTimer.ClarityTimer_backend.config;

import ClarityTimer.ClarityTimer_backend.model.Rol;
import ClarityTimer.ClarityTimer_backend.model.Usuario;
import ClarityTimer.ClarityTimer_backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("🌱 Iniciando carga de datos semilla...");

        createUserIfNotExists(
                "admin",
                "admin@claritytimer.com",
                "admin123",
                "Admin",
                "Sistema",
                Rol.ADMIN);

        createUserIfNotExists(
                "vendedor",
                "vendedor@claritytimer.com",
                "vendedor123",
                "Vendedor",
                "Tienda",
                Rol.VENDEDOR);

        createUserIfNotExists(
                "cliente",
                "cliente@claritytimer.com",
                "cliente123",
                "Cliente",
                "Demo",
                Rol.CLIENTE);

        log.info("✅ Carga de datos semilla completada");
    }

    private void createUserIfNotExists(
            String username,
            String email,
            String password,
            String nombre,
            String apellido,
            Rol rol) {

        if (usuarioRepository.findByUsername(username).isEmpty()) {
            Usuario usuario = Usuario.builder()
                    .username(username)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .nombre(nombre)
                    .apellido(apellido)
                    .rol(rol)
                    .puntosTotales(1000)
                    .puntosDisponibles(1000)
                    .streakDias(0)
                    .fechaRegistro(LocalDateTime.now())
                    .activo(true)
                    .build();

            usuarioRepository.save(usuario);
            log.info("✨ Usuario creado: {} ({}) - Rol: {}", username, email, rol);
        } else {
            log.info("ℹ️  Usuario ya existe: {}", username);
        }
    }
}
