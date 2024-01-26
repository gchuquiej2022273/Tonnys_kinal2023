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
import org.gersonchuquiej.bean.TipoPlato;
import org.gersonchuquiej.db.Conexion;
import org.gersonchuquiej.main.Principal;

public class tipoPlatoController implements Initializable{
    
    private enum operaciones {NUEVO,GUARDAR,ELIMINAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<TipoPlato> ListaTipoPlato;

    public tipoPlatoController() {
    }
    
    @FXML private TextField txtCodigoTipoPlato;
    @FXML private TextField txtDescripcionTipoPlato;
    
    @FXML private TableView tblTipoPlato;
    
    @FXML private TableColumn colCodigoTipoPlato;
    @FXML private TableColumn colDescripcionTipoPlato;
    
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
        tblTipoPlato.setItems(getTipoPlatos());
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato,Integer>("codigoTipoPlato"));
        colDescripcionTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, String>("descripcionTipoPlato"));
    }
    
    public ObservableList<TipoPlato> getTipoPlatos(){
        ArrayList<TipoPlato> listaPlato = new ArrayList<TipoPlato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarTipoPlato");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                listaPlato.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),
                                                    resultado.getString("descripcionTipoPlato")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ListaTipoPlato = FXCollections.observableList(listaPlato);
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
                    if(tblTipoPlato.getSelectionModel().getSelectedItem() != null){
                        int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro de Eliminar el registro?","Eliminar TipoPlato",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarTipoPlato(?)");
                                procedimiento.setInt(1,((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
                                procedimiento.execute();
                                ListaTipoPlato.remove(tblTipoPlato.getSelectionModel().getSelectedItem());
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
                    if(tblTipoPlato.getSelectionModel().getSelectedItem() !=null){
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
            PreparedStatement procedimeinto = Conexion.getInstance().getConexion().prepareCall("call sp_EditarTipoPlato(?,?)");
            TipoPlato registro = (TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem();
            registro.setDescripcionTipoPlato(txtDescripcionTipoPlato.getText());
            procedimeinto.setInt(1, registro.getCodigoTipoPlato());
            procedimeinto.setString(2, registro.getDescripcionTipoPlato());
            procedimeinto.execute();
            tipoDeOperacion = operaciones.ACTUALIZAR;
        } catch (Exception e) {
            
            e.printStackTrace();
        }
    }
        
    public void guardar(){
        TipoPlato registro = new TipoPlato();
//        registro.setCodigoTipoPlato(Integer.parseInt(txtCodigoTipoPlato.getText()));
            registro.setDescripcionTipoPlato(txtDescripcionTipoPlato.getText());
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarTipoPlato (?)");
                procedimiento.setString(1, registro.getDescripcionTipoPlato());
                procedimiento.execute();
                ListaTipoPlato.add(registro);
            } catch (Exception e) {
                
                e.printStackTrace();
                
            }
    }
    
    public void seleccionarElemento(){
        txtCodigoTipoPlato.setText(String.valueOf(((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
        txtDescripcionTipoPlato.setText(((TipoPlato)tblTipoPlato.getSelectionModel().getSelectedItem()).getDescripcionTipoPlato());
    }
    
    public void desactivarControles(){
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcionTipoPlato.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcionTipoPlato.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoTipoPlato.clear();
        txtDescripcionTipoPlato.clear();
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
