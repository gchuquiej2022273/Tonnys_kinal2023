package org.gersonchuquiej.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import org.gersonchuquiej.bean.Login;
import org.gersonchuquiej.bean.Usuario;
import org.gersonchuquiej.db.Conexion;
import org.gersonchuquiej.main.Principal;

public class LoginController implements Initializable{

    private Principal escenarioPrincipal;
    private ObservableList<Usuario> listarUsuarui;
    
    @FXML private TextField txtUsuario;
    @FXML private TextField txtPassword; 

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
    public ObservableList<Usuario> getUsuarios(){
        ArrayList<Usuario> lista = new ArrayList<Usuario>();
        
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarUsuario()}");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()){
                lista.add(new Usuario(resultado.getInt("codigoUsuario"),
                                        resultado.getString("nombreUsuario"),
                                        resultado.getString("apellidoUsuario"),
                                        resultado.getString("usuarioLogin"),
                                        resultado.getString("contrasena")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listarUsuarui = FXCollections.observableArrayList(lista);
}
    
    @FXML
        private void login(){
        Login login = new Login();
        int x = 0;
        
        boolean bandera = false;// bandera se usa para encontrar tuplas en un arrayList
        
        login.setUsuarioMaster(txtUsuario.getText());
        login.setPasswordLogin(txtPassword.getText());
        
        while(x < getUsuarios().size()){
            String user =   getUsuarios().get(x).getUsuarioLogin();
            String pass = getUsuarios().get(x).getContrasena();
            if (user.equals(login.getUsuarioMaster()) && pass.equals(login.getPasswordLogin())){
                JOptionPane.showMessageDialog(null, "Sesión Iniciada\n"
                        +getUsuarios().get(x).getNombreUsuario() + " " +
                         getUsuarios().get(x).getApellidoUsuario() +"\n" + "Bienvenido!!");
                escenarioPrincipal.menuPrincipal();
                x = getUsuarios().size();
                bandera = true;
            }
            x++; // solo se puede usar con variables de tipo ordinal osea 
        }
        
        if(bandera == false){
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
        }
    }
    

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaUsuario(){
        escenarioPrincipal.ventanaUsuario();
    }
    
}
