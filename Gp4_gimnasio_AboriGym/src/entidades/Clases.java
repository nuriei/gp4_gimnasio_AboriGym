
package entidades;
import java.time.LocalTime;

    
public class Clases{

    private int idClase;
    private String nombre;
    private Entrenadores entrenador;   // FK ID_Entrenador
    private LocalTime horario;       // hora de inicio (dura 1 hora)
    private int capacidad;
    private boolean estado;

    public Clases() {
    }

    public Clases(String nombre, Entrenadores entrenador, LocalTime horario,
                 int capacidad, boolean estado) {
        this.nombre = nombre;
        this.entrenador = entrenador;
        this.horario = horario;
        this.capacidad = capacidad;
        this.estado = estado;
    }

    public Clases(int idClase, String nombre, Entrenadores entrenador, LocalTime horario,
                 int capacidad, boolean estado) {
        this(nombre, entrenador, horario, capacidad, estado);
        this.idClase = idClase;
    }

    public int getIdClase() { return idClase; }
    public void setIdClase(int idClase) { this.idClase = idClase; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Entrenadores getEntrenador() { return entrenador; }
    public void setEntrenador(Entrenadores entrenador) { this.entrenador = entrenador; }

    public LocalTime getHorario() { return horario; }
    public void setHorario(LocalTime horario) { this.horario = horario; }

    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    // Hora de fin: toda clase dura 1 hora
    public LocalTime getHorarioFin() {
        return horario.plusHours(1);
    }

    @Override
    public String toString() {
        return nombre + " - " + horario;
    }
}
    

