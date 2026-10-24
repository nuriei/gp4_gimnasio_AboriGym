/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import entidades.Socio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
        
public class SocioData {
    private Connection con = null;
    
    public SocioData(MiConexion conexion){
    this.con = conexion.buscarConexion();
    }
    
     public void guardarSocio(Socio socio) {
     
      String sql = "INSERT INTO socios (dni, nombre, apellido, edad, correo, telefono, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, socio.getDni());
            ps.setString(2, socio.getNombre());
            ps.setString(3, socio.getApellido());
            ps.setInt(4, socio.getEdad());
            ps.setString(5, socio.getCorreo());
            ps.setString(6, socio.getTelefono());
            ps.setBoolean(7, socio.isEstado());
            
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                socio.setIdSocio(rs.getInt(1));
                System.out.println("[SQL] Socio guardado exitosamente. ID asignado: " + socio.getIdSocio());
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al guardar el socio: " + ex.getMessage());
        }
    }
     
     public void modificarSocio(Socio socio) {
        String sql = "UPDATE socios SET dni = ?, nombre = ?, apellido = ?, edad = ?, correo = ?, telefono = ?, estado = ? WHERE id_Socio = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, socio.getDni());
            ps.setString(2, socio.getNombre());
            ps.setString(3, socio.getApellido());
            ps.setInt(4, socio.getEdad());
            ps.setString(5, socio.getCorreo());
            ps.setString(6, socio.getTelefono());
            ps.setBoolean(7, socio.isEstado());
            ps.setInt(8, socio.getIdSocio());
            
            int exito = ps.executeUpdate();
            if (exito == 1) {
                System.out.println("[SQL] Datos del socio actualizados correctamente.");
            }
            ps.close();
                    } catch (SQLException ex) {
            System.out.println("Error al modificar el socio: " + ex.getMessage());
        }
     }
        public Socio buscarSocioPorDni(String dni) {
        String sql = "SELECT * FROM socios WHERE dni = ?";
        Socio socio = null;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                socio = new Socio();
                socio.setIdSocio(rs.getInt("id_Socio"));
                socio.setDni(rs.getString("dni"));
                socio.setNombre(rs.getString("nombre"));
                socio.setApellido(rs.getString("apellido"));
                socio.setEdad(rs.getInt("edad"));
                socio.setCorreo(rs.getString("correo"));
                socio.setTelefono(rs.getString("telefono"));
                socio.setEstado(rs.getBoolean("estado"));
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al buscar el socio: " + ex.getMessage());
        }
        return socio;
    }

    // BAJA LÓGICA: Desactivar socio (estado = 0)
    public void bajaLogicaSocio(int idSocio) {
        String sql = "UPDATE socios SET estado = 0 WHERE id_Socio = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idSocio);
            int exito = ps.executeUpdate();
            if (exito == 1) {
                System.out.println("[SQL] Socio dado de baja l\u00f3gica correctamente.");
            }
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Error al aplicar baja l\u00f3gica: " + ex.getMessage());
        }
    }
}
