package ClarityTimer.ClarityTimer_backend.model;

public enum Rareza {
    COMUN(50),       // 50 puntos
    RARO(150),       // 150 puntos
    EPICO(300),      // 300 puntos
    LEGENDARIO(500); // 500 puntos
    
    private final int puntosBase;
    
    Rareza(int puntosBase) {
        this.puntosBase = puntosBase;
    }
    
    public int getPuntosBase() {
        return puntosBase;
    }
}

