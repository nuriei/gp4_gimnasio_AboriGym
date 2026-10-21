
package entidades;

import java.time.LocalDate;


  public class Membresia {
 
    public static final int DIAS_VALIDEZ = 30;
 
    private int idMembresia;
    private Socio socio;             // FK ID_Socio
    private int cantidadPases;       // 8, 12 o 20
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double costo;
    private boolean estado;
 
    public Membresia() {
    }
 
    // Alta: la fechaFin se calcula automáticamente (inicio + 30 días)
    public Membresia(Socio socio, int cantidadPases, LocalDate fechaInicio,
                     double costo, boolean estado) {
        this.socio = socio;
        this.cantidadPases = cantidadPases;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaInicio.plusDays(DIAS_VALIDEZ);
        this.costo = costo;
        this.estado = estado;
    }
 
    // Constructor completo (para cargar desde la BD)
    public Membresia(int idMembresia, Socio socio, int cantidadPases, LocalDate fechaInicio,
                     LocalDate fechaFin, double costo, boolean estado) {
        this.idMembresia = idMembresia;
        this.socio = socio;
        this.cantidadPases = cantidadPases;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.costo = costo;
        this.estado = estado;
    }
 
    public int getIdMembresia() { return idMembresia; }
    public void setIdMembresia(int idMembresia) { this.idMembresia = idMembresia; }
 
    public Socio getSocio() { return socio; }
    public void setSocio(Socio socio) { this.socio = socio; }
 
    public int getCantidadPases() { return cantidadPases; }
    public void setCantidadPases(int cantidadPases) { this.cantidadPases = cantidadPases; }
 
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
 
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
 
    public double getCosto() { return costo; }
    public void setCosto(double costo) { this.costo = costo; }
 
    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }
 
    // ---- Métodos de negocio ----
 
    // Vigente: activa y no vencida por fecha
    public boolean estaVigente() {
        return estado && !LocalDate.now().isAfter(fechaFin);
    }
 
    public boolean tienePases() {
        return cantidadPases > 0;
    }
 
    // Descuenta 1 pase; devuelve false si no hay pases
    public boolean descontarPase() {
        if (cantidadPases > 0) {
            cantidadPases--;
            return true;
        }
        return false;
    }
 
    @Override
    public String toString() {
        return "Membresía #" + idMembresia + " - pases: " + cantidadPases
                + " - vence: " + fechaFin;
    }
}
