package org.gersonchuquiej.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.gersonchuquiej.main.Principal;


public class MenuPrincipalController implements  Initializable{
    
    private Principal escenarioPrincipal;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaProgramador(){
        escenarioPrincipal.ventanaProgramador();
    }
    
    public void ventanaEmpresa(){
        escenarioPrincipal.ventanaEmpresa();
    }
    
    public void ventanaTipoPlato(){
        escenarioPrincipal.ventanaTipoPlato();
    }
    
    public void ventanaTipoEmpleado(){
        escenarioPrincipal.ventanaTipoEmpleado();
    }
    
    public void ventanaProductos(){
        escenarioPrincipal.ventanaProducto();
    }
    public void ventanaPresupuesto(){
        escenarioPrincipal.ventanaPresupuesto();
    }
    public void ventanaEmpleado(){
        escenarioPrincipal.ventanaEmpleado();
    }
    
    public void ventanaServicio(){
        escenarioPrincipal.ventanaServicio();
    }
    public void ventanaPlato(){
        escenarioPrincipal.ventanaPlato();
    }
 
    public void ventanaLogin(){
        escenarioPrincipal.ventanaUsuario();
    }
    
    public void ventanaProductoHasPlato(){
        escenarioPrincipal.ventanaProductoHasPlato();
    }
    public void ventanaServicioHasPlato(){
        escenarioPrincipal.ventanaServicioHasPlato();
    }
    
    public void ventanaServicioHasEmpleado(){
        escenarioPrincipal.ventanaServicioHasEmpleado();
    }
}
