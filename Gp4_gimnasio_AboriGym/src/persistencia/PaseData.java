/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import entidades.Pases;
import entidades.Socio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PaseData {

    private Connection con = null;
    private SocioData socioData;   // para reconstruir el Socio de cada pase

    public PaseData(MiConexion conexion) {
        this.con = (Connection) conexion.buscarConexion();
        this.socioData = new SocioData(conexion);
    }

    // ==========================================
    // 1. MÉTODOS DE ABM (Alta, Modificación, Baja)
    // ==========================================

    // ALTA: INSERT
    public void guardarPase(Pases pase) {
        String sql = "INSERT INTO pases (id_Socio, cantidadEntradas, fecha_Inicio, fecha_Fin, costo, estado) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, pase.getSocio().getIdSocio());
            ps.setInt(2, pase.getCantidadPases());
            ps.setDate(3, java.sql.Date.valueOf(pase.getFechaInicio()));
            ps.setDate(4, java.sql.Date.valueOf(pase.getFechaFin()));
            ps.setDouble(5, pase.getCosto());
            ps.setBoolean(6, pase.isEstado());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    pase.setIdMembresia(rs.getInt(1));
                    System.out.println("[SQL] Pase guardado. ID asignado: " + pase.getIdMembresia());
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al guardar el pase: " + ex.getMessage());
        }
    }

    // MODIFICACIÓN: UPDATE
    public void modificarPase(Pases pase) {
        String sql = "UPDATE pases SET id_Socio = ?, cantidadEntradas = ?, fecha_Inicio = ?, fecha_Fin = ?, "
                   + "costo = ?, estado = ? WHERE id_Pase = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, pase.getSocio().getIdSocio());
            ps.setInt(2, pase.getCantidadPases());
            ps.setDate(3, java.sql.Date.valueOf(pase.getFechaInicio()));
            ps.setDate(4, java.sql.Date.valueOf(pase.getFechaFin()));
            ps.setDouble(5, pase.getCosto());
            ps.setBoolean(6, pase.isEstado());
            ps.setInt(7, pase.getIdMembresia());

            if (ps.executeUpdate() == 1) {
                System.out.println("[SQL] Pase actualizado correctamente.");
            }
        } catch (SQLException ex) {
            System.out.println("Error al modificar el pase: " + ex.getMessage());
        }
    }

    // BAJA FÍSICA: DELETE
    public void borrarPase(int idPase) {
        String sql = "DELETE FROM pases WHERE id_Pase = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPase);
            if (ps.executeUpdate() == 1) {
                System.out.println("[SQL] Pase eliminado correctamente.");
            } else {
                System.out.println("No existe un pase con ese ID.");
            }
        } catch (SQLException ex) {
            System.out.println("Error al borrar el pase: " + ex.getMessage());
        }
    }

    // BAJA / ALTA LÓGICA
    public void bajaLogicaPase(int idPase) {
        cambiarEstado(idPase, false);
    }

    public void altaLogicaPase(int idPase) {
        cambiarEstado(idPase, true);
    }

    private void cambiarEstado(int idPase, boolean estado) {
        String sql = "UPDATE pases SET estado = ? WHERE id_Pase = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, estado);
            ps.setInt(2, idPase);
            if (ps.executeUpdate() == 1) {
                System.out.println("[SQL] Pase " + (estado ? "activado" : "dado de baja lógica") + " correctamente.");
            }
        } catch (SQLException ex) {
            System.out.println("Error al cambiar el estado del pase: " + ex.getMessage());
        }
    }


    // ==========================================
    // 2. MÉTODOS DE BÚSQUEDA Y LISTADOS
    // ==========================================

    // BÚSQUEDA POR ID
    public Pases buscarPasePorId(int idPase) {
        String sql = "SELECT * FROM pases WHERE id_Pase = ?";
        Pases pase = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPase);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pase = mapearPase(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar el pase: " + ex.getMessage());
        }
        return pase;
    }

    // LISTADO COMPLETO (TODOS LOS PASES)
    public List<Pases> listarPases() {
        String sql = "SELECT * FROM pases";
        List<Pases> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearPase(rs));
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar pases: " + ex.getMessage());
        }
        return lista;
    }

    // LISTADO GENERAL POR DNI (Trae activos e inactivos de ese socio)
    public List<Pases> listarPasesPorDni(String dni) {
        String sql = "SELECT p.* FROM pases p JOIN socios s ON p.id_Socio = s.id_Socio WHERE s.dni = ?";
        List<Pases> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearPase(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar pases por DNI: " + ex.getMessage());
        }
        return lista;
    }

    
    // LISTADO DE SOCIOS ACTIVOS QUE NO TIENEN NINGÚN PASE ASIGNADO
public List<Socio> listarSociosSinPase() {
    String sql = "SELECT s.* FROM socios s LEFT JOIN pases p ON s.id_Socio = p.id_Socio WHERE p.id_Pase IS NULL AND s.estado = 1";
    List<Socio> lista = new ArrayList<>();
    try (PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            lista.add(socioData.buscarSocioPorId(rs.getInt("id_Socio")));
        }
    } catch (SQLException ex) {
        System.out.println("Error al listar socios sin pase: " + ex.getMessage());
    }
    return lista;
}


    // LISTADO DE PASES ACTIVOS (GENERAL)
    public List<Pases> listarPasesActivos() {
        String sql = "SELECT * FROM pases WHERE estado = 1";
        List<Pases> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearPase(rs));
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar pases activos: " + ex.getMessage());
        }
        return lista;
    }

    // LISTADO DE PASES INACTIVOS (GENERAL)
    public List<Pases> listarPasesInactivos() {
        String sql = "SELECT * FROM pases WHERE estado = 0";
        List<Pases> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearPase(rs));
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar pases inactivos: " + ex.getMessage());
        }
        return lista;
    }

    // LISTADO DE PASES ACTIVOS POR DNI DEL SOCIO
    public List<Pases> listarPasesActivosPorDni(String dni) {
        String sql = "SELECT p.* FROM pases p JOIN socios s ON p.id_Socio = s.id_Socio WHERE s.dni = ? AND p.estado = 1";
        List<Pases> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearPase(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar pases activos por DNI: " + ex.getMessage());
        }
        return lista;
    }

    // LISTADO DE PASES INACTIVOS POR DNI DEL SOCIO
    public List<Pases> listarPasesInactivosPorDni(String dni) {
        String sql = "SELECT p.* FROM pases p JOIN socios s ON p.id_Socio = s.id_Socio WHERE s.dni = ? AND p.estado = 0";
        List<Pases> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearPase(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar pases inactivos por DNI: " + ex.getMessage());
        }
        return lista;
    }


    

    private Pases mapearPase(ResultSet rs) throws SQLException {
        Pases p = new Pases();
        p.setIdMembresia(rs.getInt("id_Pase"));
        p.setSocio(socioData.buscarSocioPorId(rs.getInt("id_Socio")));
        p.setCantidadPases(rs.getInt("cantidadEntradas"));
        p.setFechaInicio(rs.getDate("fecha_Inicio").toLocalDate());
        p.setFechaFin(rs.getDate("fecha_Fin").toLocalDate());
        p.setCosto(rs.getDouble("costo"));
        p.setEstado(rs.getBoolean("estado"));
        return p;
    }
}