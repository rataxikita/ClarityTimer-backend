package ClarityTimer.ClarityTimer_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categorias_personaje")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaPersonaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 100)
    private String nombre; // Kawaii, Premium, Legendarios, Estacionales
    
    @Column(length = 255)
    private String descripcion;
    
    @Column(length = 7)
    private String color = "#FF69B4"; // Color para la UI
    
    @Column(length = 10)
    private String icono = "✨"; // Emoji representativo
    
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    @JsonIgnore // Evitar referencia circular en JSON
    private List<PersonajeSanrio> personajes = new ArrayList<>();
}

