package pruebasMain;

import java.sql.Connection;
import persistencia.MiConexion;
import persistencia.SocioData;
import entidades.Socio;


public class PruebaConexion {

    public static void main(String[] args) {
        
        String url = "jdbc:mysql://localhost:3306/gp4_gimnasio_aborigym";
        String usuario = "root";
        String password = "";


        MiConexion con = new MiConexion(url, usuario, password);
        Connection c = con.buscarConexion();
        
                if (c != null) {
            System.out.println("\n--- INICIANDO PRUEBAS DE PERSISTENCIA ---");
            
            SocioData socioData = new SocioData(con);
            
            // prueba uno, creamos un socio nuevo en memoria para la prueba
            Socio nuevoSocio = new Socio("93863238", "Davicito", "Verita", 38, "davidverabarz2017@email.com", "2235396219", true); // la edad duele. ;)
            System.out.println("Intentando guardar socio: " + nuevoSocio.getNombre());
            
            // prueba dos, se inserta en la base de datos (INSERT)
            socioData.guardarSocio(nuevoSocio);
            
            // prueba tres: Buscamos en la BD para comprobar que se grabo (SELECT)
            System.out.println("\nVerificando inserción en la Base de Datos...");
            Socio socioBuscado = socioData.buscarSocioPorDni("93863238");
            
            if (socioBuscado != null) {
                System.out.println("¡Socio recuperado de la BD!: " + socioBuscado.getApellido() + ", " + socioBuscado.getNombre());
                
                System.out.println("\n--- Ejecutando Prueba: Modificar datos ---");
                socioBuscado.setTelefono("2236789056"); // Cambiamos el teléfono por iphone
                socioBuscado.setEdad(28);              // juventud
                socioData.modificarSocio(socioBuscado);
                
                //pruebacuatro: verificamos s se seteo los datos otra vez
                System.out.println("Verificando cambio en BD...");
                Socio socioModificado = socioData.buscarSocioPorDni("93863238");
                System.out.println("Datos actuales en BD: Telefono: " + socioModificado.getTelefono() + " - Edad: " + socioModificado.getEdad());
                
            } else {
                System.out.println("Error: No se pudo encontrar al socio ingresado.");
            }
            
            System.out.println("\n--- FIN DE LAS PRUEBAS ---");
        }
    }

}
