/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import entidades.Entrenadores;   
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author analf
 */
public class EntrenadorData {
      private Connection con = null;
 
    public EntrenadorData(MiConexion conexion) {
        this.con = conexion.buscarConexion();
    }
 
    // ALTA: INSERT
    public void guardarEntrenador(Entrenadores entrenador) {
        String sql = "INSERT INTO entrenadores (dni, nombre, apellido, especialidad, estado) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entrenador.getDni());
            ps.setString(2, entrenador.getNombre());
            ps.setString(3, entrenador.getApellido());
            ps.setString(4, entrenador.getEspecialidad());
            ps.setBoolean(5, entrenador.isEstado());
 
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entrenador.setIdEntrenadores(rs.getInt(1));
                    System.out.println("[SQL] Entrenador guardado. ID asignado: " + entrenador.getIdEntrenadores());
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al guardar el entrenador: " + ex.getMessage());
        }
    }
 
    // MODIFICACIÓN: UPDATE
    public void modificarEntrenador(Entrenadores entrenador) {
        String sql = "UPDATE entrenadores SET dni = ?, nombre = ?, apellido = ?, especialidad = ?, estado = ? WHERE id_Entrenador = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, entrenador.getDni());
            ps.setString(2, entrenador.getNombre());
            ps.setString(3, entrenador.getApellido());
            ps.setString(4, entrenador.getEspecialidad());
            ps.setBoolean(5, entrenador.isEstado());
            ps.setInt(6, entrenador.getIdEntrenadores());
 
            if (ps.executeUpdate() == 1) {
                System.out.println("[SQL] Datos del entrenador actualizados correctamente.");
            }
        } catch (SQLException ex) {
            System.out.println("Error al modificar el entrenador: " + ex.getMessage());
        }
    }
 
    // BAJA FÍSICA: DELETE (elimina la fila definitivamente)
    // Falla si el entrenador tiene clases asignadas (FK); en ese caso usar baja lógica.
    public void borrarEntrenador(int idEntrenador) {
        String sql = "DELETE FROM entrenadores WHERE id_Entrenador = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEntrenador);
            if (ps.executeUpdate() == 1) {
                System.out.println("[SQL] Entrenador eliminado correctamente.");
            } else {
                System.out.println("No existe un entrenador con ese ID.");
            }
        } catch (SQLException ex) {
            System.out.println("Error al borrar el entrenador (¿tiene clases asignadas?): " + ex.getMessage());
        }
    }
 
    // BAJA LÓGICA: estado = 0
    public void bajaLogicaEntrenador(int idEntrenador) {
        cambiarEstado(idEntrenador, false);
    }
 
    // ALTA LÓGICA: estado = 1 (reactivar)
    public void altaLogicaEntrenador(int idEntrenador) {
        cambiarEstado(idEntrenador, true);
    }
 
    private void cambiarEstado(int idEntrenador, boolean estado) {
        String sql = "UPDATE entrenadores SET estado = ? WHERE id_Entrenador = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, estado);
            ps.setInt(2, idEntrenador);
            if (ps.executeUpdate() == 1) {
                System.out.println("[SQL] Entrenador " + (estado ? "activado" : "dado de baja lógica") + " correctamente.");
            }
        } catch (SQLException ex) {
            System.out.println("Error al cambiar el estado del entrenador: " + ex.getMessage());
        }
    }
 
    // BÚSQUEDAS
    public Entrenadores buscarEntrenadorPorId(int idEntrenador) {
        String sql = "SELECT * FROM entrenadores WHERE id_Entrenador = ?";
        Entrenadores entrenador = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEntrenador);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    entrenador = mapearEntrenador(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar el entrenador: " + ex.getMessage());
        }
        return entrenador;
    }
 
    public Entrenadores buscarEntrenadorPorDni(String dni) {
        String sql = "SELECT * FROM entrenadores WHERE dni = ?";
        Entrenadores entrenador = null;
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    entrenador = mapearEntrenador(rs);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al buscar el entrenador: " + ex.getMessage());
        }
        return entrenador;
    }
 
    // LISTADOS
    public List<Entrenadores> listarEntrenadores() {
        String sql = "SELECT * FROM entrenadores";
        List<Entrenadores> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearEntrenador(rs));
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar entrenadores: " + ex.getMessage());
        }
        return lista;
    }
 
    // Para el combo de ClaseView: solo activos cuya especialidad coincide con la disciplina
    public List<Entrenadores> listarPorEspecialidad(String especialidad) {
        String sql = "SELECT * FROM entrenadores WHERE especialidad = ? AND estado = 1";
        List<Entrenadores> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, especialidad);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearEntrenador(rs));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar entrenadores por especialidad: " + ex.getMessage());
        }
        return lista;
    }
 
    // Convierte una fila del ResultSet en un objeto Entrenador (evita repetir código)
    private Entrenadores mapearEntrenador(ResultSet rs) throws SQLException {
        Entrenadores e = new Entrenadores();
        e.setIdEntrenadores(rs.getInt("id_Entrenador"));
        e.setDni(rs.getString("dni"));
        e.setNombre(rs.getString("nombre"));
        e.setApellido(rs.getString("apellido"));
        e.setEspecialidad(rs.getString("especialidad"));
        e.setEstado(rs.getBoolean("estado"));
        return e;
    }
    // listar entrenadores que esten activos 
    public List<Entrenadores> listarActivos() {
        String sql = "SELECT * FROM entrenadores WHERE estado = 1";
        List<Entrenadores> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearEntrenador(rs));
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar entrenadores activos: " + ex.getMessage());
        }
        return lista;
    }
    //listar entrenadores que estan inactivos
    public List<Entrenadores> listarInactivos() {
        String sql = "SELECT * FROM entrenadores WHERE estado = 0";
        List<Entrenadores> lista = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearEntrenador(rs));
            }
        } catch (SQLException ex) {
            System.out.println("Error al listar entrenadores inactivos: " + ex.getMessage());
        }
        return lista;
    }
}

