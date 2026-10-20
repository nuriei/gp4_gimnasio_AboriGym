package pruebasMain;

import java.sql.Connection;
import persistencia.MiConexion;

public class PruebaConexion {

    public static void main(String[] args) {
        
        String url = "jdbc:mysql://localhost:3306/gp4_gimnasio_aborigym";
        String usuario = "root";
        String password = "";


        MiConexion con = new MiConexion(url, usuario, password);
        Connection c = con.buscarConexion();
    }

}
