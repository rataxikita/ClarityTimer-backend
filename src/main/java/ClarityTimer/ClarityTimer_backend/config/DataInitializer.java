package ClarityTimer.ClarityTimer_backend.config;

import ClarityTimer.ClarityTimer_backend.model.CategoriaPersonaje;
import ClarityTimer.ClarityTimer_backend.model.PersonajeSanrio;
import ClarityTimer.ClarityTimer_backend.model.Rareza;
import ClarityTimer.ClarityTimer_backend.repository.CategoriaPersonajeRepository;
import ClarityTimer.ClarityTimer_backend.repository.PersonajeSanrioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final CategoriaPersonajeRepository categoriaRepository;
    private final PersonajeSanrioRepository personajeRepository;

    @Override
    public void run(String... args) {
        long cantidadCategorias = categoriaRepository.count();
        long cantidadPersonajes = personajeRepository.count();
        
        log.info("Verificando datos iniciales... Categorías: {}, Personajes: {}", cantidadCategorias, cantidadPersonajes);
        
        // Verificar si faltan personajes (deberían ser 10)
        long personajesEsperados = 10;
        boolean faltanPersonajes = cantidadPersonajes < personajesEsperados;
        
        if (cantidadCategorias == 0 || cantidadPersonajes == 0 || faltanPersonajes) {
            if (faltanPersonajes && cantidadPersonajes > 0) {
                log.warn("⚠️ Datos incompletos detectados. Se esperan {} personajes pero solo hay {}. Reinicializando...", 
                        personajesEsperados, cantidadPersonajes);
            } else {
                log.warn("⚠️ Datos iniciales incompletos detectados. Inicializando...");
            }
            inicializarDatos();
            
            long categoriasCreadas = categoriaRepository.count();
            long personajesCreados = personajeRepository.count();
            log.info("✅ Datos inicializados correctamente. Categorías: {}, Personajes: {}", 
                    categoriasCreadas, personajesCreados);
            
            // Verificar que todos los personajes esperados existen
            if (personajesCreados < personajesEsperados) {
                log.error("❌ ERROR: Se esperaban {} personajes pero solo se crearon {}. " +
                        "Revisa los logs para ver qué falló.", personajesEsperados, personajesCreados);
            }
            
            // Verificar que Cinnamoroll existe (crítico para el registro)
            personajeRepository.findByNombre("Cinnamoroll")
                    .ifPresentOrElse(
                            p -> log.info("✅ Personaje inicial 'Cinnamoroll' verificado correctamente"),
                            () -> log.error("❌ ERROR: Personaje 'Cinnamoroll' no se pudo crear. El registro de usuarios fallará.")
                    );
            
            // Listar todos los personajes disponibles
            long disponibles = personajeRepository.findByDisponibleTrue().size();
            log.info("📊 Personajes disponibles en la tienda: {}", disponibles);
        } else {
            log.info("✅ Los datos ya están inicializados correctamente ({} personajes)", cantidadPersonajes);
            
            // Verificar que Cinnamoroll existe
            personajeRepository.findByNombre("Cinnamoroll")
                    .ifPresentOrElse(
                            p -> log.debug("Personaje inicial 'Cinnamoroll' encontrado"),
                            () -> log.warn("⚠️ ADVERTENCIA: Personaje 'Cinnamoroll' no encontrado. " +
                                    "El registro de usuarios puede fallar. Considera reiniciar el backend.")
                    );
            
            // Verificar cuántos personajes están disponibles
            long disponibles = personajeRepository.findByDisponibleTrue().size();
            if (disponibles < personajesEsperados) {
                log.warn("⚠️ ADVERTENCIA: Solo {} de {} personajes están disponibles. " +
                        "Algunos personajes pueden no aparecer en la tienda.", disponibles, personajesEsperados);
            } else {
                log.info("📊 Personajes disponibles en la tienda: {}", disponibles);
            }
        }
    }

    private void inicializarDatos() {
        // 1. Crear categorías
        CategoriaPersonaje kawaii = CategoriaPersonaje.builder()
                .nombre("Kawaii Clásicos")
                .descripcion("Los personajes más adorables y populares")
                .color("#FFB3D9")
                .icono("💖")
                .build();
        kawaii = categoriaRepository.save(kawaii);

        CategoriaPersonaje premium = CategoriaPersonaje.builder()
                .nombre("Premium")
                .descripcion("Personajes exclusivos de alta calidad")
                .color("#FFD700")
                .icono("⭐")
                .build();
        premium = categoriaRepository.save(premium);

        CategoriaPersonaje legendarios = CategoriaPersonaje.builder()
                .nombre("Legendarios")
                .descripcion("Los más raros y difíciles de conseguir")
                .color("#9370DB")
                .icono("👑")
                .build();
        legendarios = categoriaRepository.save(legendarios);

        CategoriaPersonaje estacionales = CategoriaPersonaje.builder()
                .nombre("Estacionales")
                .descripcion("Personajes de temporada limitada")
                .color("#87CEEB")
                .icono("🎄")
                .build();
        estacionales = categoriaRepository.save(estacionales);

        // 2. Crear personajes - Kawaii Clásicos
        personajeRepository.save(PersonajeSanrio.builder()
                .nombre("Cinnamoroll")
                .descripcion("Un cachorro blanco con orejas largas y cola rizada como un rollo de canela")
                .categoria(kawaii)
                .precioPuntos(0)
                .rareza(Rareza.COMUN)
                .imagenEstudio("/characters/cinnamoroll-study.png")
                .imagenDescanso("/characters/cinnamoroll-break.png")
                .disponible(true)
                .esDefault(true)
                .ordenTienda(1)
                .fechaCreacion(LocalDateTime.now())
                .build());

        personajeRepository.save(PersonajeSanrio.builder()
                .nombre("Hello Kitty")
                .descripcion("La gatita más famosa del mundo con su lazo rojo")
                .categoria(kawaii)
                .precioPuntos(100)
                .rareza(Rareza.COMUN)
                .imagenEstudio("/characters/hello-kitty-study.png")
                .imagenDescanso("/characters/hello-kitty-break.png")
                .disponible(true)
                .esDefault(false)
                .ordenTienda(2)
                .fechaCreacion(LocalDateTime.now())
                .build());

        personajeRepository.save(PersonajeSanrio.builder()
                .nombre("My Melody")
                .descripcion("Una conejita dulce con capucha rosa")
                .categoria(kawaii)
                .precioPuntos(120)
                .rareza(Rareza.COMUN)
                .imagenEstudio("/characters/my-melody-study.png")
                .imagenDescanso("/characters/my-melody-break.png")
                .disponible(true)
                .esDefault(false)
                .ordenTienda(3)
                .fechaCreacion(LocalDateTime.now())
                .build());

        personajeRepository.save(PersonajeSanrio.builder()
                .nombre("Kuromi")
                .descripcion("La conejita rebelde con actitud punk")
                .categoria(kawaii)
                .precioPuntos(150)
                .rareza(Rareza.RARO)
                .imagenEstudio("/characters/kuromi-study.png")
                .imagenDescanso("/characters/kuromi-break.png")
                .disponible(true)
                .esDefault(false)
                .ordenTienda(4)
                .fechaCreacion(LocalDateTime.now())
                .build());

        // 3. Crear personajes - Premium
        personajeRepository.save(PersonajeSanrio.builder()
                .nombre("Pochacco")
                .descripcion("Un perrito deportista muy activo")
                .categoria(premium)
                .precioPuntos(200)
                .rareza(Rareza.RARO)
                .imagenEstudio("/characters/pochacco-study.png")
                .imagenDescanso("/characters/pochacco-break.png")
                .disponible(true)
                .esDefault(false)
                .ordenTienda(5)
                .fechaCreacion(LocalDateTime.now())
                .build());

        personajeRepository.save(PersonajeSanrio.builder()
                .nombre("Keroppi")
                .descripcion("Una rana alegre que vive en un estanque")
                .categoria(premium)
                .precioPuntos(250)
                .rareza(Rareza.EPICO)
                .imagenEstudio("/characters/keroppi-study.png")
                .imagenDescanso("/characters/keroppi-break.png")
                .disponible(true)
                .esDefault(false)
                .ordenTienda(6)
                .fechaCreacion(LocalDateTime.now())
                .build());

        personajeRepository.save(PersonajeSanrio.builder()
                .nombre("Badtz-Maru")
                .descripcion("Un pingüino con actitud malhumorada")
                .categoria(premium)
                .precioPuntos(280)
                .rareza(Rareza.EPICO)
                .imagenEstudio("/characters/badtz-maru-study.png")
                .imagenDescanso("/characters/badtz-maru-break.png")
                .disponible(true)
                .esDefault(false)
                .ordenTienda(7)
                .fechaCreacion(LocalDateTime.now())
                .build());

        // 4. Crear personajes - Legendarios
        personajeRepository.save(PersonajeSanrio.builder()
                .nombre("Chococat")
                .descripcion("Un gato negro súper curioso y tecnológico")
                .categoria(legendarios)
                .precioPuntos(400)
                .rareza(Rareza.LEGENDARIO)
                .imagenEstudio("/characters/chococat-study.png")
                .imagenDescanso("/characters/chococat-break.png")
                .disponible(true)
                .esDefault(false)
                .ordenTienda(8)
                .fechaCreacion(LocalDateTime.now())
                .build());

        personajeRepository.save(PersonajeSanrio.builder()
                .nombre("Gudetama")
                .descripcion("El huevo perezoso que no quiere hacer nada")
                .categoria(legendarios)
                .precioPuntos(500)
                .rareza(Rareza.LEGENDARIO)
                .imagenEstudio("/characters/gudetama-study.png")
                .imagenDescanso("/characters/gudetama-break.png")
                .disponible(true)
                .esDefault(false)
                .ordenTienda(9)
                .fechaCreacion(LocalDateTime.now())
                .build());

        personajeRepository.save(PersonajeSanrio.builder()
                .nombre("Aggretsuko")
                .descripcion("La panda roja de oficina que ama el karaoke metal")
                .categoria(legendarios)
                .precioPuntos(600)
                .rareza(Rareza.LEGENDARIO)
                .imagenEstudio("/characters/aggretsuko-study.png")
                .imagenDescanso("/characters/aggretsuko-break.png")
                .disponible(true)
                .esDefault(false)
                .ordenTienda(10)
                .fechaCreacion(LocalDateTime.now())
                .build());
    }
}

