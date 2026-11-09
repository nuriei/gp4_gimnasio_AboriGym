/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import entidades.Asistencias;

/**
 *
 * @author Abel
 */
public class AsistenciaData {
    
    private Connection connection = null;
    
        public AsistenciaData() {

            connection = new MiConexion("jdbc:mysql://localhost/nombre_de_tu_base", "root", "").buscarConexion();

        }
    
        public void registrarAsistencia(Asistencias asistencia) {
            
        String sql = "INSERT INTO asistencia (idSocio, fechaAsistencia) VALUES (?, ?)";
        
        try {
            
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setInt(1, asistencia.getSocio().getIdSocio());
            ps.setDate(2, Date.valueOf(asistencia.getFechaAsistencia()));
            
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                asistencia.setIdAsistencia(rs.getInt(1));
                JOptionPane.showMessageDialog(null, "Asistencia registrada");
            }
            
                ps.close();
                
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error al acceder a la tabla asistencia: " + ex.getMessage());
                    }
        }
        
        public List<Asistencias> listarAsistencias() {
        List<Asistencias> asistencias = new ArrayList<>();
        String sql = "SELECT idAsistencia, idSocio, fechaAsistencia FROM asistencia";
        
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Asistencias asistencia = new Asistencias();
                
                asistencia.setIdAsistencia(rs.getInt("idAsistencia"));

                asistencia.setFechaAsistencia(rs.getDate("fechaAsistencia").toLocalDate());
                
                asistencias.add(asistencia);
            }
            
            ps.close();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al listar las asistencias: " + ex.getMessage());
        }
        return asistencias;
    }
        
        public List<Asistencias> obtenerAsistenciasPorSocio(int idSocio) {
        List<Asistencias> asistencias = new ArrayList<>();
        String sql = "SELECT idAsistencia, idSocio, fechaAsistencia FROM asistencia WHERE idSocio = ?";
        
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, idSocio);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Asistencias asistencia = new Asistencias();
                asistencia.setIdAsistencia(rs.getInt("idAsistencia"));
                asistencia.setFechaAsistencia(rs.getDate("fechaAsistencia").toLocalDate());
                
                asistencias.add(asistencia);
            }
            
            ps.close();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al obtener las asistencias del socio: " + ex.getMessage());
        }
        
        return asistencias;
    }
    
}
