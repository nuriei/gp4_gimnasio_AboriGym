
package persistencia;

import entidades.Clases;
import entidades.Entrenadores;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class ClaseData {

    private Connection con = null;
    private EntrenadorData entrenData;

    public ClaseData(MiConexion conexion) {
        this.con = conexion.buscarConexion();
        this.entrenData = new EntrenadorData(conexion);
    }

    public void guardarClase(Clases clase) {
        String sql = "INSERT INTO clases (nombre, id_Entrenador,horario,capacidad,estado) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, clase.getNombre());
            ps.setInt(2, clase.getEntrenador().getIdEntrenador());
            ps.setTime(3, Time.valueOf(clase.getHorario())); // Convierte LocalTime a java.sql.Time
            ps.setInt(4, clase.getCapacidad());
            ps.setBoolean(5, clase.isEstado());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    clase.setIdClase(rs.getInt(1));
                    System.out.println("[SQL] Clase guardada con exito. Id asignado:  " + clase.getIdClase());
                }
            }
        } catch (SQLException ex) {
            javax.swing.JOptionPane.showMessageDialog(null, "Error SQL al guardar clase" +ex.getMessage());
            System.out.println("Error al guardar la clase: "+ex.getMessage());
        }
    }

    public void modificarClase(Clases clase) {
        String sql = "UPDATE clases SET nombre = ?, id_Entrenador = ?,horario = ?,capacidad = ?,estado= ? WHERE id_clase = ?";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, clase.getNombre());
            ps.setInt(2, clase.getEntrenador().getIdEntrenador());
            ps.setTime(3, Time.valueOf(clase.getHorario())); // Convierte LocalTime a java.sql.Time
            ps.setInt(4, clase.getCapacidad());
            ps.setBoolean(5, clase.isEstado());
            ps.setInt(6, clase.getIdClase());

            if (ps.executeUpdate() == 1) {
                System.out.println("[SQL] Los datos de la clase fueron actualizados ");
            }
        } catch (SQLException ex) {
            System.out.println("Error al modificar la clase: " + ex.getMessage());
        }
    }

    public void bajaLogicaClase(int idClase) {
        String sql = "UPDATE clases SET estado = 0 where id_Clase = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idClase);
            if (ps.executeUpdate() == 1) {
                System.out.println("[SQL] Clase dada de baja lógica correctamente.");
            }
        } catch (SQLException ex) {
            System.out.println("Error al dar de baja la clase: " + ex.getMessage());
        }
    }
    
    public boolean borrarClase(int idClase){
    String sql = "DELETE FROM clases where id_clase = ?";
    try(PreparedStatement ps = con.prepareStatement(sql)){
    ps.setInt(1, idClase);
    return ps.executeUpdate() == 1;
    }catch(SQLException ex){
        javax.swing.JOptionPane.showMessageDialog(null, "No se puede eliminar la clase porque tiene asistencias registradas.Use 'baja' en su lugar.","Accion denegada",javax.swing.JOptionPane.WARNING_MESSAGE);
        return false;
    }
    }

    public Clases buscarClasePorId(int idClase) {
        String sql = "SELECT * FROM clases WHERE id_clase = ?";
        Clases clase = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idClase);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    clase = mapearClase(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar la clase: " + ex.getMessage());
        }
        return clase;
    }

    public List<Clases> listarClasesActivas() {
        String sql = "SELECT * FROM clases Where estado = 1";
        List<Clases> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearClase(rs));
            }
        } catch (SQLException ex) {
            javax.swing.JOptionPane.showMessageDialog(null, "Error SQL al listar Clases"+ex.getMessage());
            System.out.println("Error al listar clases activas: " + ex.getMessage());    
        }
        return lista;
    }
    
    public List<Clases> listartodaslasclases(){
        String sql = "SELECT * FROM clases";
        List<Clases> lista = new ArrayList<>();
        try(PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()){
            while (rs.next()){
            lista.add(mapearClase(rs));
            }
        }catch(SQLException ex){
            javax.swing.JOptionPane.showMessageDialog(null, "Error SQL al listar todas las clases"+ex.getMessage());
        }
        return lista;
    }

    private Clases mapearClase(ResultSet rs) throws SQLException {
        Clases c = new Clases();
        c.setIdClase(rs.getInt("id_Clase"));
        c.setNombre(rs.getString("nombre"));
        c.setHorario(rs.getTime("horario").toLocalTime());
        c.setCapacidad(rs.getInt("capacidad"));
        c.setEstado(rs.getBoolean("estado"));

        int idEntrenador = rs.getInt("id_Entrenador");
        Entrenadores Entrenador = entrenData.buscarEntrenadorPorId(idEntrenador);
        c.setEntrenador(Entrenador);

        return c;
    }
}
