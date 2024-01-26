package org.gersonchuquiej.controller;


import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.commons.codec.digest.DigestUtils;
import org.gersonchuquiej.bean.Usuario;
import org.gersonchuquiej.db.Conexion;
import org.gersonchuquiej.main.Principal;

public class UsuariosController implements Initializable{

    private Principal escenarioPrincipal;
    private enum operaciones{GUARDAR,NUEVO,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    @FXML private TextField txtCodgoUsuario;
    @FXML private TextField txtNombreUsuario;
    @FXML private TextField txtApellidoUsuario;
    @FXML private TextField txtUsuario;
    @FXML private TextField txtPasword;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;

    @FXML private  ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    
    public UsuariosController() {
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    
    }
    
    public void nuevo(){
        switch (tipoDeOperacion) {
            case NINGUNO:
                    limpiarControler();
                    activarControler();
                    btnNuevo.setText("Guardar");
                    btnEliminar.setText("Cancelar");
                    imgNuevo.setImage(new Image("/org/gersonchuquiej/image/Guardar.png"));
                    imgEliminar.setImage(new Image("/org/gersonchuquiej/image/Actualizar.png"));
                    tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR :
                    guardar();
                    limpiarControler();
                    desactivarControler();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    imgNuevo.setImage(new Image("/org/gersonchuquiej/image/Agregar.png"));
                    imgEliminar.setImage(new Image("/org/gersonchuquiej/image/Eliminar.png"));
                    tipoDeOperacion = operaciones.NINGUNO;
                    ventanaLogin();
                break;
                
        }
    }
    
    public void guardar(){
        Usuario registro = new Usuario();
        // registro.setCodigoUsuario(Integer.parseInt(txtCodigoUsuario.getText()));
        registro.setNombreUsuario(txtNombreUsuario.getText());
        registro.setApellidoUsuario(txtApellidoUsuario.getText());
        registro.setUsuarioLogin(txtUsuario.getText());
        registro.setContrasena(txtPasword.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarUsuario(?,?,?,?)}");
            procedimiento.setString(1, registro.getNombreUsuario());
            procedimiento.setString(2, registro.getApellidoUsuario());
            procedimiento.setString(3, registro.getUsuarioLogin());
            procedimiento.setString(4, registro.getContrasena());
            procedimiento.execute();
//            String contra ="";
//            String encript = DigestUtils.md5Hex(contra);
//            System.out.println(encript);  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void desactivarControler(){
        txtCodgoUsuario.setEditable(false);
        txtNombreUsuario.setEditable(false);
        txtApellidoUsuario.setEditable(false);
        txtUsuario.setEditable(false);
        txtPasword.setEditable(false);
    }
    
    public void activarControler(){
        txtCodgoUsuario.setEditable(false);
        txtNombreUsuario.setEditable(true);
        txtApellidoUsuario.setEditable(true);
        txtUsuario.setEditable(true);
        txtPasword.setEditable(true);
    }
    
    public void limpiarControler(){
        txtCodgoUsuario.clear();
        txtNombreUsuario.clear();
        txtApellidoUsuario.clear();
        txtUsuario.clear();
        txtPasword.clear();
    }
    
   

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.login();
    }
}
