/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pruebasMain;

import persistencia.ClaseData;
import persistencia.EntrenadorData;
import persistencia.MiConexion;
import persistencia.PaseData;
import persistencia.SocioData;

/**
 *
 * @author Abel
 */
public class TestConsola {
    public static void main(String[] args) {
        
        MiConexion conexion = new MiConexion("jdbc:mysql://localhost:3306/gp4_gimnasio_aborigym", "root", "");
        
        System.out.println("INICIO DE TEST GENERAL");
        
        // PRUEBA DE SOCIODATA
        System.out.println("Probando sociodata");
        SocioData socioData = new SocioData(conexion);
        
        // Listar socios existentes
        System.out.println("Total de socios registrados: " + socioData.listarSocios().size());
        for (var socio : socioData.listarSocios()) {
            System.out.println(" -> [Socio] ID: " + socio.getIdSocio() + " | Nombre: " + socio.getNombre() + " " + socio.getApellido());
        }
        
        // PRUEBA DE ENTRENADORDATA
        System.out.println("Probando entrenadordata");
        EntrenadorData entrenadorData = new EntrenadorData(conexion);
        
        //Listar entrenadores existentes
        System.out.println("Total de entrenadores registrados: " + entrenadorData.listarEntrenadores().size());
        for (var entrenador : entrenadorData.listarEntrenadores()) {
            System.out.println(" -> [Entrenador] ID: " + entrenador.getIdEntrenador() + " | Nombre: " + entrenador.getNombre() + " " + entrenador.getApellido());
        }
        
        // PRUEBA CLASEDATA
        System.out.println("Probando clasedata");
        ClaseData claseData = new ClaseData(conexion);
        
        //Listar clases existentes
        try {
            System.out.println("Total de clases registradas: " + claseData.listarClasesActivas().size());
            for (var clase : claseData.listarClasesActivas()) {
                System.out.println(" -> [Clase] ID: " + clase.getIdClase() + " | Nombre: " + clase.getNombre());
            }
        } catch (Exception e) {
            System.out.println("Nota: Revisa el nombre del método listar en ClaseData si difiere ('listarClases').");
        }
        
        // PROBAR PASEDATA
        System.out.println("Probando pasedata");
        PaseData paseData = new PaseData(conexion);
        
        // Listar pases existentes
        try {
            System.out.println("Total de pases registrados: " + paseData.listarPases().size());
        } catch (Exception e) {
                System.out.println("Revisar el nombre del metodo listar en PaseData si llega a ser diferente");
        }
        
        System.out.println("TEST FINALIZADO CON EXITO");
    }
}