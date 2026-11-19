
package persistencia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import entidades.Asistencias;
import java.time.LocalDate;

/**
 *
 * @author Abel
 */
public class AsistenciaData {
    
    private Connection connection = null;
    private SocioData socioData;
    private ClaseData claseData;
    
        public AsistenciaData(MiConexion conexion) {
         
        connection = conexion.buscarConexion();

        socioData = new SocioData(conexion);
        claseData = new ClaseData(conexion);
                }
    
        public boolean registrarAsistencia(Asistencias asistencia) {

        String sql = "INSERT INTO asistencias "
                + "(id_socio, id_clase, fecha_asistencia) "
                + "VALUES (?, ?, ?)";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, asistencia.getSocio().getIdSocio());
            ps.setInt(2, asistencia.getClase().getIdClase());
            ps.setDate(3, java.sql.Date.valueOf(asistencia.getFechaAsistencia()));

            ps.executeUpdate();
            ps.close();

            return true;

        } catch (SQLException ex) {
            System.out.println("Error al registrar asistencia: " + ex.getMessage());
            return false;
        }
}
   
        
        public List<Asistencias> listarAsistencias() {
        List<Asistencias> asistencias = new ArrayList<>();
        String sql = "SELECT * FROM asistencias";
        
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Asistencias asistencia = new Asistencias();
                
                asistencia.setIdAsistencia(rs.getInt("id_Asistencia"));

                asistencia.setFechaAsistencia(rs.getDate("fecha_Asistencia").toLocalDate());
                asistencia.setSocio(socioData.buscarSocioPorId(rs.getInt("id_socio")));

                asistencia.setClase(claseData.buscarClasePorId(rs.getInt("id_clase")));
                
                asistencias.add(asistencia);
            }
            
            ps.close();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al listar las asistencias: " + ex.getMessage());
        }
        return asistencias;
    }
         //Consultamos las asitencias por fecha y las guardamos en un arreglo para desp mostrarlas
    //Tamb usamos eeste metodo para que al registrar asistencias anteriores se pueda conabilizar el cupo 
    public List<Asistencias> listarAsistenciasPorDia(LocalDate fecha) { 

        List<Asistencias> asistencias = new ArrayList<>();

        String sql = "SELECT * FROM asistencias WHERE fecha_asistencia = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setDate(1, java.sql.Date.valueOf(fecha));

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Asistencias asistencia = new Asistencias();

                asistencia.setIdAsistencia(rs.getInt("id_asistencia"));
                asistencia.setFechaAsistencia(
                        rs.getDate("fecha_asistencia").toLocalDate());
                asistencia.setSocio(
                        socioData.buscarSocioPorId(rs.getInt("id_socio")));
                asistencia.setClase(
                        claseData.buscarClasePorId(rs.getInt("id_clase")));

                asistencias.add(asistencia);
            }

            rs.close();
            ps.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error al listar asistencias por día: " + ex.getMessage());
        }

        return asistencias;
    }
        public List<Asistencias> obtenerAsistenciasPorSocio(int idSocio) {
        List<Asistencias> asistencias = new ArrayList<>();
        String sql = "SELECT * FROM asistencias WHERE id_socio = ?";
        
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, idSocio);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Asistencias asistencia = new Asistencias();
                asistencia.setIdAsistencia(rs.getInt("id_asistencia"));
                asistencia.setFechaAsistencia(rs.getDate("fecha_asistencia").toLocalDate());
                
                asistencia.setSocio(socioData.buscarSocioPorId(rs.getInt("id_socio")));

                asistencia.setClase(claseData.buscarClasePorId(rs.getInt("id_clase")));
                
                asistencias.add(asistencia);
            }
            
            ps.close();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al obtener las asistencias del socio: " + ex.getMessage());
        }
        
        return asistencias;
    }
        
    
}
