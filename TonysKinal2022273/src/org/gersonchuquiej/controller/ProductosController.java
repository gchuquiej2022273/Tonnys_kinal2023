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
import org.gersonchuquiej.bean.Producto;
import org.gersonchuquiej.db.Conexion;
import org.gersonchuquiej.main.Principal;

public class ProductosController implements Initializable{
    private enum operaciones {NUEVO,GUARDAR,ELIMINAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Producto> ListaDeProducto;

    public ProductosController() {
    }

    @FXML private TextField txtCodigoProducto;
    @FXML private TextField txtNombreProducto;
    @FXML private TextField txtCantidadProducto;
    
    @FXML private TableView tblProducto;
    
    @FXML private TableColumn colCodigoProducto;
    @FXML private TableColumn colCantidadProducto;
    @FXML private TableColumn colNombreProducto;
    
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
        tblProducto.setItems(getTipoPlatos());
        colCodigoProducto.setCellValueFactory(new PropertyValueFactory<Producto,Integer>("codigoProducto"));
        colCantidadProducto.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("cantidad"));
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<Producto, String>("nombreProducto"));
    }
    
    public ObservableList<Producto> getTipoPlatos(){
        ArrayList<Producto> listaProducto = new ArrayList<Producto>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductos");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                listaProducto.add(new Producto(resultado.getInt("codigoProducto"),
                                                resultado.getString("nombreProducto"),
                                                resultado.getInt("cantidad")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ListaDeProducto = FXCollections.observableList(listaProducto);
    }
    public void nuevo(){
        
        switch (tipoDeOperacion){
            case NINGUNO:
                    activarControles();
                    limpiarControles();
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
                
                    if(tblProducto.getSelectionModel().getSelectedItem() != null){
                        int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro de Eliminar el registro?","Eliminar Producto",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarProducto(?)");
                                procedimiento.setInt(1,((Producto)tblProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());
                                procedimiento.execute();
                                ListaDeProducto.remove(tblProducto.getSelectionModel().getSelectedItem());
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
                    if(tblProducto.getSelectionModel().getSelectedItem() !=null){
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
            PreparedStatement procedimeinto = Conexion.getInstance().getConexion().prepareCall("call sp_EditarProducto(?,?,?)");
            Producto registro = (Producto)tblProducto.getSelectionModel().getSelectedItem();
            registro.setCodigoProducto(Integer.parseInt(txtCodigoProducto.getText()));
            registro.setCantidad(Integer.parseInt(txtCantidadProducto.getText()));
            registro.setNombreProducto(txtNombreProducto.getText());
            procedimeinto.setInt(1, registro.getCodigoProducto());
            procedimeinto.setString(2, registro.getNombreProducto());
            procedimeinto.setInt(3, registro.getCantidad());
            procedimeinto.execute();
            tipoDeOperacion = operaciones.ACTUALIZAR;
        } catch (Exception e) {
            
            e.printStackTrace();
        }
    }
        
    public void guardar(){
        
        Producto registro = new Producto();
            registro.setCodigoProducto(Integer.parseInt(txtCodigoProducto.getText()));
            registro.setCantidad(Integer.parseInt(txtCantidadProducto.getText()));
            registro.setNombreProducto(txtNombreProducto.getText());
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarProductos(?,?,?)");
                procedimiento.setInt(1, registro.getCodigoProducto());
                procedimiento.setString(2, registro.getNombreProducto());
                procedimiento.setInt(3, registro.getCantidad());
                procedimiento.execute();
                ListaDeProducto.add(registro);
            } catch (Exception e) {
                
                e.printStackTrace();
                
            }
    }
    
    public void seleccionarElemento(){
        txtCodigoProducto.setText(String.valueOf(((Producto)tblProducto.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        txtCantidadProducto.setText(String.valueOf(((Producto)tblProducto.getSelectionModel().getSelectedItem()).getCantidad()));
        txtNombreProducto.setText(((Producto)tblProducto.getSelectionModel().getSelectedItem()).getNombreProducto());
    }
    
    public void desactivarControles(){
        txtCodigoProducto.setEditable(false);
        txtCantidadProducto.setEditable(false);
        txtNombreProducto.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoProducto.setEditable(true);
        txtCantidadProducto.setEditable(true);
        txtNombreProducto.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoProducto.clear();
        txtCantidadProducto.clear();
        txtNombreProducto.clear();
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
