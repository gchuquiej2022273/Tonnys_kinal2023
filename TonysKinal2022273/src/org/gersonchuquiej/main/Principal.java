/*
    Programador : Gerson Emmanuel Chuquiej Pirir
    Código  carné : 2022-273
    Código Técnico : IN5BV
    Fecha de Creacion 14-04-2023 13:22
    Fecha de Modificacion : 15-04-2023 :20:00
    fecha de Modificaion : 20-04-2023 15:00:
    fecha de Modificacion : 21-04-2023 13:23;
 */
package org.gersonchuquiej.main;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.gersonchuquiej.controller.EmpleadoController;
import org.gersonchuquiej.controller.EmpresaController;
import org.gersonchuquiej.controller.LoginController;
import org.gersonchuquiej.controller.MenuPrincipalController;
import org.gersonchuquiej.controller.PlatoController;
import org.gersonchuquiej.controller.PresupuestoController;
import org.gersonchuquiej.controller.ProductosController;
import org.gersonchuquiej.controller.ProductosHasPlatosController;
import org.gersonchuquiej.controller.ProgramadorController;
import org.gersonchuquiej.controller.ServicioController;
import org.gersonchuquiej.controller.ServicioHasEmpleadoController;
import org.gersonchuquiej.controller.ServicioHasPlatoController;
import org.gersonchuquiej.controller.TipoEmpleadoController;
import org.gersonchuquiej.controller.UsuariosController;
import org.gersonchuquiej.controller.tipoPlatoController;

public class Principal extends Application {
    
    private final String PAQUETE_VISTA = "/org/gersonchuquiej/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Tonny´s Kinal 2023");
        escenarioPrincipal.getIcons().add(new Image("/org/gersonchuquiej/image/LogoSecundario.png"));
//        Parent root = FXMLLoader.load(getClass().getResource("/org/gersonchuquiej/view/EmpresaView.fxml"));
//        Scene escena = new Scene(root);
//        escenarioPrincipal.setScene(escena);
       login();
        escenarioPrincipal.show();
    }
        
    public void menuPrincipal(){
        try{
            MenuPrincipalController menu = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml", 1000, 590);
            menu.setEscenarioPrincipal(this);
        }catch(Exception e){
        e.printStackTrace();
     }
    }
    
    public void ventanaProgramador(){
        try{
            ProgramadorController programadorview = (ProgramadorController)cambiarEscena("ProgramadorView.fxml", 1000, 590);
            programadorview.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaEmpresa(){
        try{
            EmpresaController empresaview = (EmpresaController)cambiarEscena("EmpresaView.fxml", 1000, 590);
            empresaview.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
   public void ventanaTipoPlato(){
       try {
           tipoPlatoController tipoPlatoview = (tipoPlatoController)cambiarEscena("TipoPlatoView.fxml", 1000, 590);
           tipoPlatoview.setEscenarioPrincipal(this);
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
   
   public void ventanaTipoEmpleado(){
       try {
           TipoEmpleadoController tipoEmpleadoview = (TipoEmpleadoController)cambiarEscena("TipoEmpleadoView.fxml", 1000, 590);
           tipoEmpleadoview.setEscenarioPrincipal(this);
       } catch (Exception e) {
           e.printStackTrace();
       }
   
   }
   
   public void ventanaEmpleado(){
       try {
           EmpleadoController empleadoView = (EmpleadoController)cambiarEscena("EmpleadoView.fxml",1460,590 );
           empleadoView.setEscenarioPrincipal(this);
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
   public void ventanaProducto(){
       try {
           ProductosController productoview = (ProductosController)cambiarEscena("ProductosView.fxml",1000,590);
           productoview.setEscenarioPrincipal(this);
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
   
   public void ventanaPresupuesto(){
       try{
           PresupuestoController presupuestoView = (PresupuestoController)cambiarEscena("PresupuestoView.fxml", 1000, 590);
           presupuestoView.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();
       }
   }
   
   public void ventanaServicio(){
       try{
           ServicioController servicioView = (ServicioController)cambiarEscena("ServiciosView.fxml", 1450, 590);
           servicioView.setEscenarioPrincipal(this);
       }catch(Exception e){
           e.printStackTrace();
       }
   }
   
   public void ventanaPlato(){
       try {
           PlatoController platoView = (PlatoController)cambiarEscena("PlatoView.fxml", 1227, 590);
           platoView.setEscenarioPrincipal(this);
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
   
   public void ventanaUsuario(){
       try {
           UsuariosController usuarioVIew = (UsuariosController)cambiarEscena("UsuarioView.fxml", 1227, 590);
           usuarioVIew.setEscenarioPrincipal(this);
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
   public void login(){
       try {
           LoginController LoginView = (LoginController)cambiarEscena("LoginView.fxml", 643, 458);
           LoginView.setEscenarioPrincipal(this);
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
   public void ventanaProductoHasPlato(){
       try {
           ProductosHasPlatosController productoHasPlato = (ProductosHasPlatosController)cambiarEscena("ProductoHasEmpleadosView.fxml", 1000, 590);
           productoHasPlato.setEscenarioPrincipal(this);
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
   
   public void ventanaServicioHasPlato(){
       try {
           ServicioHasPlatoController SHasPlato = (ServicioHasPlatoController)cambiarEscena("ServicioHasPlatoView.fxml", 1000, 590);
           SHasPlato.setEscenarioPrincipal(this);
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
   public void ventanaServicioHasEmpleado(){
       try {
           ServicioHasEmpleadoController SsHasEmpleado = (ServicioHasEmpleadoController)cambiarEscena("ServiciosHasEmpleados.fxml", 1100, 590);
           SsHasEmpleado.setEscenarioPrincipal(this);
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena(String fxml, int ancho,int alto) throws Exception{
            Initializable resultado = null;
            FXMLLoader cargadorFXML = new FXMLLoader();
            InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
            cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
            cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
            escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
            escenarioPrincipal.setScene(escena);
            escenarioPrincipal.sizeToScene();
            resultado = (Initializable)cargadorFXML.getController();
            return resultado;
    }
}