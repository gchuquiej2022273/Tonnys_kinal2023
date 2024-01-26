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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.gersonchuquiej.bean.TipoEmpleado;
import org.gersonchuquiej.db.Conexion;
import org.gersonchuquiej.main.Principal;

public class TipoEmpleadoController implements Initializable {
    
    private enum operaciones {NUEVO,GUARDAR,ELIMINAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<TipoEmpleado> ListaTipoEmpleado;
    
    public TipoEmpleadoController() {
        
    }

    @FXML private TextField txtCodigoEmpleado;
    @FXML private TextField txtDescripcionEmpleado;
    
    @FXML private TableView tblTipoEmpleado;
    
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private TableColumn colDescripcionEmpleado;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblTipoEmpleado.setItems(getTipoPlatos());
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado,Integer>("codigoTipoEmpleado"));
        colDescripcionEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado, String>("descripcion"));
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
        return ListaTipoEmpleado = FXCollections.observableList(listaEmpleado);
    }
    
    public void nuevo(){
        switch (tipoDeOperacion) {
            case NINGUNO:
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
                
                    if(tblTipoEmpleado.getSelectionModel().getSelectedItem() != null){
                        int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro de Eliminar el registro?","Eliminar TipoEmpleado21ws",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarTipoEmpleado(?)");
                                procedimiento.setInt(1,((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
                                procedimiento.execute();
                                ListaTipoEmpleado.remove(tblTipoEmpleado.getSelectionModel().getSelectedItem());
                                limpiarControles();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Debe de seleccionar un elemento");
                    }
                break;
        }
    }
    
    public void editar(){
        switch (tipoDeOperacion) {
            case NINGUNO:
                    if(tblTipoEmpleado.getSelectionModel().getSelectedItem() !=null){
                        btnNuevo.setDisable(true);
                        btnEliminar.setDisable(true);
                        btnEditar.setText("Actualizar");
                        btnReporte.setText("Cancelar");
                        imgEditar.setImage(new Image("/org/gersonchuquiej/image/Actualizar.png"));
                        imgReporte.setImage(new Image("/org/gersonchuquiej/image/Cancelar.png"));
                        activarControles();
                        tipoDeOperacion = operaciones.ACTUALIZAR;
                    }else{
                        JOptionPane.showMessageDialog(null, "Debe de seleccionar un elemento");
                    }
                break;
            case ACTUALIZAR:
                    
                break;
        }
    }
    
    public void reporte(){
        switch (tipoDeOperacion) {
            case ACTUALIZAR:
                    actualizar();
                    limpiarControles();
                    desactivarControles();
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    imgEditar.setImage(new Image("/org/gersonchuquiej/image/Editar.png"));
                    imgReporte.setImage(new Image("/org/gersonchuquiej/image/Reporte.png"));
                    cargarDatos();
                    tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                throw new AssertionError();
        }
    
    }
    
    public void actualizar(){
        try {
            PreparedStatement procedimeinto = Conexion.getInstance().getConexion().prepareCall("call sp_EditarTipoEmpleado(?,?)");
            TipoEmpleado registro = (TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcionEmpleado.getText());
            procedimeinto.setInt(1, registro.getCodigoTipoEmpleado());
            procedimeinto.setString(2, registro.getDescripcion());
            procedimeinto.execute();
            tipoDeOperacion = operaciones.ACTUALIZAR;
        } catch (Exception e) {
            
            e.printStackTrace();
        }
    }
        
    public void guardar(){
        TipoEmpleado registro = new TipoEmpleado();
            registro.setDescripcion(txtDescripcionEmpleado.getText());
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarTipoEmpleado(?)");
                procedimiento.setString(1, registro.getDescripcion());
                procedimiento.execute();
                ListaTipoEmpleado.add(registro);
            } catch (Exception e) {
                
                e.printStackTrace();
                
            }
    }
    
    public void seleccionarElemento(){
        txtCodigoEmpleado.setText(String.valueOf(((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
        txtDescripcionEmpleado.setText(((TipoEmpleado)tblTipoEmpleado.getSelectionModel().getSelectedItem()).getDescripcion());
    }
    
    public void desactivarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtDescripcionEmpleado.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoEmpleado.setEditable(false);
        txtDescripcionEmpleado.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoEmpleado.clear();
        txtDescripcionEmpleado.clear();
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
