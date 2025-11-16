package ClarityTimer.ClarityTimer_backend.service;

import ClarityTimer.ClarityTimer_backend.dto.AuthResponse;
import ClarityTimer.ClarityTimer_backend.dto.LoginRequest;
import ClarityTimer.ClarityTimer_backend.dto.RegisterRequest;
import ClarityTimer.ClarityTimer_backend.dto.UsuarioResponse;
import ClarityTimer.ClarityTimer_backend.exception.BadRequestException;
import ClarityTimer.ClarityTimer_backend.model.*;
import ClarityTimer.ClarityTimer_backend.repository.*;
import ClarityTimer.ClarityTimer_backend.security.JwtTokenProvider;
import ClarityTimer.ClarityTimer_backend.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final ConfiguracionUsuarioRepository configuracionRepository;
    private final PersonajeSanrioRepository personajeRepository;
    private final InventarioUsuarioRepository inventarioRepository;
    private final HistorialPuntosRepository historialPuntosRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Validar que no exista
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("El nombre de usuario ya está en uso");
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El email ya está en uso");
        }

        // 1. Crear usuario con 600 puntos de regalo
        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .rol(Rol.CLIENTE)
                .puntosTotales(600) // 🎁 Puntos de regalo iniciales
                .puntosDisponibles(600) // 🎁 Puntos de regalo iniciales
                .streakDias(0)
                .activo(true)
                .build();

        usuarioRepository.save(usuario);

        // 2. Crear configuración por defecto
        ConfiguracionUsuario config = ConfiguracionUsuario.builder()
                .usuario(usuario)
                .build();
        configuracionRepository.save(config);

        // 3. 🎮 REGALAR PERSONAJE INICIAL GRATIS
        PersonajeSanrio personajeInicial = obtenerPersonajeInicial();
        
        InventarioUsuario inventario = InventarioUsuario.builder()
                .usuario(usuario)
                .personaje(personajeInicial)
                .fechaObtencion(LocalDateTime.now())
                .puntosGastados(0) // Gratis
                .esActivo(true) // Es el activo por defecto
                .vecesUsado(0)
                .build();

        inventarioRepository.save(inventario);
        log.info("Personaje inicial '{}' asignado al usuario '{}'", personajeInicial.getNombre(), usuario.getUsername());

        // 4. Registrar puntos de regalo en historial
        HistorialPuntos historialRegalo = HistorialPuntos.builder()
                .usuario(usuario)
                .tipo(TipoTransaccion.REGALO)
                .cantidad(600)
                .descripcion("🎁 Puntos de bienvenida")
                .fecha(LocalDateTime.now())
                .build();
        historialPuntosRepository.save(historialRegalo);

        // 5. Generar JWT
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        return AuthResponse.builder()
                .token(token)
                .usuario(mapToResponse(usuario))
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Usuario usuario = usuarioRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado"));

        return AuthResponse.builder()
                .token(token)
                .usuario(mapToResponse(usuario))
                .build();
    }

    public UsuarioResponse getCurrentUser(Long userId) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado"));
        return mapToResponse(usuario);
    }

    /**
     * Obtiene el personaje inicial para regalar a nuevos usuarios.
     * Intenta encontrar en este orden:
     * 1. Cinnamoroll (personaje por defecto preferido)
     * 2. Cualquier personaje marcado como esDefault=true
     * 3. El primer personaje disponible
     * 
     * @return PersonajeSanrio para regalar
     * @throws BadRequestException si no hay personajes disponibles
     */
    private PersonajeSanrio obtenerPersonajeInicial() {
        // 1. Intentar encontrar Cinnamoroll (personaje preferido)
        Optional<PersonajeSanrio> cinnamoroll = personajeRepository.findByNombre("Cinnamoroll");
        if (cinnamoroll.isPresent() && cinnamoroll.get().getDisponible()) {
            log.debug("Personaje inicial encontrado: Cinnamoroll");
            return cinnamoroll.get();
        }

        // 2. Buscar cualquier personaje marcado como default
        Optional<PersonajeSanrio> personajeDefault = personajeRepository.findByEsDefaultTrueAndDisponibleTrue()
                .stream()
                .findFirst();
        if (personajeDefault.isPresent()) {
            log.warn("Cinnamoroll no encontrado, usando personaje por defecto: {}", personajeDefault.get().getNombre());
            return personajeDefault.get();
        }

        // 3. Buscar el primer personaje disponible
        Optional<PersonajeSanrio> primerPersonaje = personajeRepository.findByDisponibleTrue()
                .stream()
                .findFirst();
        if (primerPersonaje.isPresent()) {
            log.warn("No se encontró personaje por defecto, usando primer personaje disponible: {}", 
                    primerPersonaje.get().getNombre());
            return primerPersonaje.get();
        }

        // 4. No hay personajes disponibles
        log.error("ERROR CRÍTICO: No hay personajes disponibles en la base de datos. " +
                "El DataInitializer debería haber creado los personajes iniciales.");
        throw new BadRequestException(
                "No hay personajes disponibles en el sistema. " +
                "Por favor, contacta al administrador o reinicia el backend para inicializar los datos."
        );
    }

    private UsuarioResponse mapToResponse(Usuario usuario) {
        InventarioUsuario personajeActivo = inventarioRepository.findByUsuarioAndEsActivoTrue(usuario)
                .orElse(null);

        return UsuarioResponse.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .rol(usuario.getRol())
                .puntosTotales(usuario.getPuntosTotales())
                .puntosDisponibles(usuario.getPuntosDisponibles())
                .streakDias(usuario.getStreakDias())
                .personajeActivoId(personajeActivo != null ? personajeActivo.getPersonaje().getId() : null)
                .personajeActivoNombre(personajeActivo != null ? personajeActivo.getPersonaje().getNombre() : null)
                .build();
    }
}

