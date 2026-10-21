
package entidades;

import entidades.Socio;
import java.time.LocalDate;

public class Asistencias {

    private int idAsistencia;
    private Socio socio;             
    private Clases clase;             
    private LocalDate fechaAsistencia;

    public Asistencias() {
    }

    public Asistencias(Socio socio, Clases clase, LocalDate fechaAsistencia) {
        this.socio = socio;
        this.clase = clase;
        this.fechaAsistencia = fechaAsistencia;
    }

    public Asistencias(int idAsistencia, Socio socio, Clases clase, LocalDate fechaAsistencia) {
        this(socio, clase, fechaAsistencia);
        this.idAsistencia = idAsistencia;
    }

    public int getIdAsistencia() { return idAsistencia; }
    public void setIdAsistencia(int idAsistencia) { this.idAsistencia = idAsistencia; }

    public Socio getSocio() { return socio; }
    public void setSocio(Socio socio) { this.socio = socio; }

    public Clases getClase() { return clase; }
    public void setClase(Clases clase) { this.clase = clase; }

    public LocalDate getFechaAsistencia() { return fechaAsistencia; }
    public void setFechaAsistencia(LocalDate fechaAsistencia) { this.fechaAsistencia = fechaAsistencia; }

    @Override
    public String toString() {
        return socio + " -> " + clase + " (" + fechaAsistencia + ")";
    }
}