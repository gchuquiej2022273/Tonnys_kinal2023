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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.gersonchuquiej.bean.Empleado;
import org.gersonchuquiej.bean.TipoEmpleado;
import org.gersonchuquiej.db.Conexion;
import org.gersonchuquiej.main.Principal;

public class EmpleadoController implements Initializable {
  private Principal escenarioPrincipal;

    
    private enum operaciones{NUEVO,GUARDAR,ELIMINAR,REPORTAR,CANCELAR,NINGUNO,ACTUALIZAR};
        private operaciones tipoDeOperacion = operaciones.NINGUNO; 
        private ObservableList<Empleado> listaEmpleados;
        private ObservableList<TipoEmpleado> listaTipoEmpleado;
        
        @FXML private Button btnNuevo;
        @FXML private Button btnEditar;
        @FXML private Button btnEliminar;
        @FXML private Button btnReporte;
        
        @FXML private ImageView imgNuevo;
        @FXML private ImageView imgEditar;
        @FXML private ImageView imgEliminar;
        @FXML private ImageView imgReporte;
        
        @FXML private TableColumn colCodigoEmpleado;
        @FXML private TableColumn colNumEmpleado;
        @FXML private TableColumn colApellidoEmpleado;
        @FXML private TableColumn colNombresEmpleado;
        @FXML private TableColumn colDireccionEmpleado;
        @FXML private TableColumn colTelefonoContacto;
        @FXML private TableColumn colGradoCocinero;
        @FXML private TableColumn colCodTipoEmpleado;
        
        @FXML private TextField txtEmpleado;
        @FXML private TextField txtNumeroEmpleado;
        @FXML private TextField txtApellidoEmpleado;
        @FXML private TextField txtNombreEmpleado;
        @FXML private TextField txtDireccionEmpleado;
        @FXML private TextField txtTelefonoEmpleado;
        @FXML private TextField txtGradoCocinero;
        
        @FXML private TableView tblEmpleados;
        
        @FXML private ComboBox cmbCodigoTipoEmpleado;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoTipoEmpleado.setItems(getTipoPlatos());
    }
        
        
        
        public void cargarDatos(){
        tblEmpleados.setItems(getEmpleados());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoEmpleado"));
        colNumEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("numeroEmpleado")); 
        colApellidoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidoEmpleado"));
        colNombresEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombresEmpleado"));
        colDireccionEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, String>("direccionEmpleado"));
        colTelefonoContacto.setCellValueFactory(new PropertyValueFactory<Empleado, String>("telefonoContacto"));
        colGradoCocinero.setCellValueFactory(new PropertyValueFactory<Empleado, String>("gradoCocinero"));
        colCodTipoEmpleado.setCellValueFactory(new PropertyValueFactory<Empleado, Integer>("codigoTipoEmpleado"));
  
        }
        
        
        public ObservableList<Empleado> getEmpleados(){
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpleados()");
            ResultSet resultado = procedimiento.executeQuery();
            
            while (resultado.next()){
                lista.add(new Empleado(resultado.getInt("codigoEmpleado"),
                                          resultado.getInt("numeroEmpleado"),
                                          resultado.getString("apellidoEmpleado"),
                                          resultado.getString("nombresEmpleado"),
                                          resultado.getString("direccionEmpleado"),
                                          resultado.getString("telefonoContacto"),
                                          resultado.getString("gradoCocinero"),
                                          resultado.getInt("codigoTipoEmpleado")));
            }
        }catch(Exception e){
            e.printStackTrace();
            
        }
        return listaEmpleados = FXCollections.observableArrayList(lista);
    }
    
        
    
        
        public TipoEmpleado buscarTipoEmpleado(int codigoTipoEmpleado){
        TipoEmpleado resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarTipoEmpleado(?)");
            procedimiento.setInt(1, codigoTipoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TipoEmpleado(registro.getInt("codigoTipoEmpleado"),
                                    registro.getString("descripcion"));
                                    
                
            }
        }catch(Exception e){
            e.printStackTrace();
        
        }
        
        
        return resultado;
    }
        
        public ObservableList<TipoEmpleado> getTipoPlatos(){
        ArrayList<TipoEmpleado> listaEmpleado = new ArrayList<TipoEmpleado>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTipoEmpleados");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                listaEmpleado.add(new TipoEmpleado(resultado.getInt("codigoTipoEmpleado"),
                                                    resultado.getString("descripcion")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoEmpleado = FXCollections.observableList(listaEmpleado);
    }
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                cargarDatos();
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/gersonchuquiej/image/Guardar.png"));
                imgEliminar.setImage(new Image("/org/gersonchuquiej/image/Cancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;

                break;
            case GUARDAR:
                    guardar();
                    limpiarControles();
                    desactivarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    imgNuevo.setImage(new Image("/org/gersonchuquiej/image/Agregar.png"));
                    imgEliminar.setImage(new Image("/org/gersonchuquiej/image/Eliminar.png"));
                    tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                    limpiarControles();
                    desactivarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    imgNuevo.setImage(new Image("/org/gersonchuquiej/image/Agregar.png"));
                    imgEliminar.setImage(new Image("/org/gersonchuquiej/image/Eliminar.png"));
                    tipoDeOperacion = operaciones.NINGUNO;
                    break;
            default:
                if(tblEmpleados.getSelectionModel().getSelectedItem() != null){
                int respuesta = JOptionPane.showConfirmDialog(null, "Estas seguro de eliminar el registo","Eliminar Empleados",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if(respuesta == JOptionPane.YES_OPTION){
                    try{
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarEmpleado(?)");
                        procedimiento.setInt(1, ((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
                        procedimiento.execute();
                        listaEmpleados.remove(tblEmpleados.getSelectionModel().getSelectedItem());
                        limpiarControles();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                }else{
                JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
        }
    }
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblEmpleados.getSelectionModel().getSelectedItem() !=null){
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/gersonchuquiej/image/Actualizar.png"));
                    imgReporte.setImage(new Image("/org/gersonchuquiej/image/Cancelar.png"));
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "debe seleccionar un elemento");
                }
                
                
                
                break;
            case ACTUALIZAR:
                actualizar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("reporte");
                imgEditar.setImage(new Image("/org/gersonchuquiej/image/Editar.png"));
                imgReporte.setImage(new Image("/org/gersonchuquiej/image/Reporte.png"));
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void actualizar(){
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarEmpleado(?,?,?,?,?,?,?,?)");
            Empleado registro = (Empleado)tblEmpleados.getSelectionModel().getSelectedItem();
            
            registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
            registro.setApellidoEmpleado(txtApellidoEmpleado.getText());
            registro.setNombresEmpleado(txtNombreEmpleado.getText());
            registro.setDireccionEmpleado(txtDireccionEmpleado.getText());
            registro.setTelefonoContacto(txtTelefonoEmpleado.getText());
            registro.setGradoCocinero(txtGradoCocinero.getText());
            registro.setCodigoTipoEmpleado(((TipoEmpleado)cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
            
            procedimiento.setInt(1, registro.getCodigoEmpleado());
            procedimiento.setInt(2, registro.getNumeroEmpleado());
            procedimiento.setString(3, registro.getApellidoEmpleado());
            procedimiento.setString(4, registro.getNombresEmpleado());
            procedimiento.setString(5, registro.getDireccionEmpleado());
            procedimiento.setString(6, registro.getTelefonoContacto());
            procedimiento.setString(7, registro.getGradoCocinero());
            procedimiento.setInt(8, registro.getCodigoTipoEmpleado());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     public void guardar(){
        Empleado registro = new Empleado();
        registro.setNumeroEmpleado(Integer.parseInt(txtNumeroEmpleado.getText()));
        registro.setApellidoEmpleado((txtApellidoEmpleado.getText()));
        registro.setNombresEmpleado((txtNombreEmpleado.getText()));
        registro.setDireccionEmpleado((txtDireccionEmpleado.getText()));
        registro.setTelefonoContacto((txtTelefonoEmpleado.getText()));
        registro.setGradoCocinero((txtGradoCocinero.getText()));
        registro.setCodigoTipoEmpleado(((TipoEmpleado)cmbCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());

        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarEmpleados(?,?,?,?,?,?,?)");
            
            procedimiento.setInt(1, registro.getNumeroEmpleado());
            procedimiento.setString(2, registro.getApellidoEmpleado());            
            procedimiento.setString(3, registro.getNombresEmpleado());
            procedimiento.setString(4, registro.getDireccionEmpleado());
            procedimiento.setString(5, registro.getTelefonoContacto());
            procedimiento.setString(6, registro.getGradoCocinero());
            procedimiento.setInt(7, registro.getCodigoTipoEmpleado());
            procedimiento.execute();
            listaEmpleados.add(registro);
 
        }catch(Exception e){
            e.printStackTrace();
        }
    }
     
     
     public void reporte(){ 
        switch(tipoDeOperacion){ 
            case ACTUALIZAR: 
                        limpiarControles();
                        desactivarControles();
                        btnNuevo.setDisable(false);
                        btnEliminar.setDisable(false);
                        btnReporte.setText("Reporte");
                        btnEditar.setText("Editar");
                        imgEditar.setImage(new Image("/org/gersonchuquiej/image/Editar.png"));
                        imgReporte.setImage(new Image("/org/gersonchuquiej/image/Reporte.png"));
                        tipoDeOperacion = operaciones.NINGUNO; 
                        break; 
                        }

   }
   public void seleccionarElemento(){
        txtEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
        txtNumeroEmpleado.setText(String.valueOf(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNumeroEmpleado()));
        txtApellidoEmpleado.setText((((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getApellidoEmpleado()));
        txtNombreEmpleado.setText((((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getNombresEmpleado()));
        txtDireccionEmpleado.setText((((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleado()));
        txtTelefonoEmpleado.setText((((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getTelefonoContacto()));
        txtGradoCocinero.setText((((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getGradoCocinero()));
        cmbCodigoTipoEmpleado.getSelectionModel().select(buscarTipoEmpleado(((Empleado)tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));

        
    }
    public void desactivarControles(){
            txtEmpleado.setEditable(false);
            txtNumeroEmpleado.setEditable(false);
            txtApellidoEmpleado.setEditable(false);
            txtNombreEmpleado.setEditable(false);
            txtDireccionEmpleado.setEditable(false);
            txtTelefonoEmpleado.setEditable(false);
            txtGradoCocinero.setEditable(false);
    }
    
    public void activarControles(){
        txtEmpleado.setEditable(false);
        txtNumeroEmpleado.setEditable(true);
        txtApellidoEmpleado.setEditable(true);
        txtNombreEmpleado.setEditable(true);
        txtDireccionEmpleado.setEditable(true);
        txtTelefonoEmpleado.setEditable(true);
        txtGradoCocinero.setEditable(true);
    }
    
    public void limpiarControles(){
        txtEmpleado.clear();
        txtNumeroEmpleado.clear();
        txtApellidoEmpleado.clear();
        txtNombreEmpleado.clear();
        txtDireccionEmpleado.clear();
        txtTelefonoEmpleado.clear();
        txtGradoCocinero.clear();
        cmbCodigoTipoEmpleado.setValue(null);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}