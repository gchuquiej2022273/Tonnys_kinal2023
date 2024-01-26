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
import org.gersonchuquiej.bean.Plato;
import org.gersonchuquiej.bean.Producto;
import org.gersonchuquiej.bean.ProductoHasPlatos;
import org.gersonchuquiej.db.Conexion;
import org.gersonchuquiej.main.Principal;

public class ProductosHasPlatosController implements Initializable {

    
    private enum operaciones{NINGUNO,GUARDAR};
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Producto> ListaDeProducto;
    private ObservableList<Plato> listaPlato;
    private ObservableList<ProductoHasPlatos> listaProductoHasPlato;

    @FXML
    private Button btnNuevo;

    @FXML
    private ImageView imgNuevo;

    @FXML
    private Button btnEliminar;

    @FXML
    private ImageView imgEliminar;

    @FXML
    private TextField txtPCodigoProducto;

    @FXML
    private ComboBox cmbCodigoPlato;

    @FXML
    private ComboBox cmbCodigoProducto;

    @FXML
    private TableView tblProductosHasPlatos;

    @FXML
    private TableColumn colCodigoProductoPlato;

    @FXML
    private TableColumn colCodigoPlato;

    @FXML
    private TableColumn colCodgoProducto;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoPlato.setItems(getPlatos());
        cmbCodigoProducto.setItems(getProductos());
    }

    public void cargarDatos() {
        tblProductosHasPlatos.setItems(getProductoHasPlatoses());
        colCodigoProductoPlato.setCellValueFactory(new PropertyValueFactory<ProductoHasPlatos, Integer>("producos_codigoProductos"));
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<ProductoHasPlatos, Integer>("codigoPlato"));
        colCodgoProducto.setCellValueFactory(new PropertyValueFactory<ProductoHasPlatos, Integer>("codigoProducto"));
    }

    
        public void nuevo(){
        
        switch (tipoDeOperaciones){
            case NINGUNO:
                    activarControles();
                    limpiarControles();
                    btnNuevo.setText("Guardar");
                    btnEliminar.setText("Cancelar");
                    imgNuevo.setImage(new Image("/org/gersonchuquiej/image/Guardar.png"));
                    imgEliminar.setImage(new Image("/org/gersonchuquiej/image/Cancelar.png"));
                    tipoDeOperaciones = operaciones.GUARDAR;
                break;
            case GUARDAR:
                    guardar();
                    limpiarControles();
                    desactivarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setDisable(true);
                    imgNuevo.setImage(new Image("/org/gersonchuquiej/image/Agregar.png"));
                    imgEliminar.setImage(new Image("/org/gersonchuquiej/image/Eliminar.png"));
                    tipoDeOperaciones = operaciones.NINGUNO;
                    cargarDatos();
                    
                break;
        }
    }
    public ObservableList<ProductoHasPlatos> getProductoHasPlatoses() {
        ArrayList<ProductoHasPlatos> lista = new ArrayList();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductos_has_Platos()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new ProductoHasPlatos(resultado.getInt("producos_codigoProductos"),
                        resultado.getInt("codigoPlato"),
                        resultado.getInt("codigoProducto")));
            }
        } catch (Exception e) {
        }

        return listaProductoHasPlato = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Plato> getPlatos() {
        ArrayList<Plato> lista = new ArrayList<Plato>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarPlatos()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                lista.add(new Plato(resultado.getInt("codigoPlato"),
                        resultado.getInt("cantidad"),
                        resultado.getString("nombrePlato"),
                        resultado.getString("descripcionPlato"),
                        resultado.getDouble("precioPlato"),
                        resultado.getInt("codigoTipoPlato")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaPlato = FXCollections.observableList(lista);
    }

    public ObservableList<Producto> getProductos() {
        ArrayList<Producto> listaProducto = new ArrayList<Producto>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarProductos()");
            ResultSet resultado = procedimiento.executeQuery();
            while (resultado.next()) {
                listaProducto.add(new Producto(resultado.getInt("codigoProducto"),
                        resultado.getString("nombreProducto"),
                        resultado.getInt("cantidad")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ListaDeProducto = FXCollections.observableList(listaProducto);
    }

        public void guardar(){
        ProductoHasPlatos registro = new ProductoHasPlatos();
        registro.setProducos_codigoProductos(Integer.parseInt(txtPCodigoProducto.getText()));
        registro.setCodigoPlato(((Plato)cmbCodigoPlato.getSelectionModel().getSelectedItem()).getCodigoPlato());
        registro.setCodigoProducto(((Producto)cmbCodigoProducto.getSelectionModel().getSelectedItem()).getCodigoProducto());

        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarProductos_has_Platos(?,?,?)");
            procedimiento.setInt(1, registro.getProducos_codigoProductos());
            procedimiento.setInt(2, registro.getCodigoPlato());
            procedimiento.setInt(3, registro.getCodigoProducto());
            procedimiento.execute();
            listaProductoHasPlato.add(registro);
        }catch(Exception e){
            e.printStackTrace();
            
        }
    }
        
  public Producto buscarCodigoProducto(int codigoProducto){
        Producto resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarProducto(?)");
            procedimiento.setInt(1, codigoProducto);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Producto(registro.getInt("codigoProducto"),
                                                   registro.getString("nombreProducto"),
                                                    registro.getInt("cantidad"));
                                    
                
            }
        }catch(Exception e){
            e.printStackTrace();
        
        }
        
        
        return resultado;
    }
    public Plato buscarCodigoPlato(int codigoPlato){
        Plato resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarPlato(?)");
            procedimiento.setInt(1, codigoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Plato(registro.getInt("codigoPlato"),
                                    registro.getInt("cantidad"),
                                    registro.getString("nombrePlato"),
                                    registro.getString("descripcionPlato"),
                                    registro.getDouble("precioPlato"),
                                    registro.getInt("codigoTipoPlato"));
                                    
                
            }
        }catch(Exception e){
            e.printStackTrace();
        
        }
        
        
        return resultado;
    }
    
    public void seleccionarElemento(){
        txtPCodigoProducto.setText(String.valueOf(((ProductoHasPlatos)tblProductosHasPlatos.getSelectionModel().getSelectedItem()).getProducos_codigoProductos()));
        cmbCodigoPlato.getSelectionModel().select(buscarCodigoPlato(((ProductoHasPlatos)tblProductosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        cmbCodigoProducto.getSelectionModel().select(buscarCodigoProducto(((ProductoHasPlatos)tblProductosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
    }
    public void activarControles(){
       cmbCodigoPlato.setDisable(false);
       cmbCodigoProducto.setDisable(false);
       txtPCodigoProducto.setEditable(true);
    }
    
   public void desactivarControles(){
       cmbCodigoPlato.setDisable(false);
       cmbCodigoProducto.setDisable(false);
       txtPCodigoProducto.setEditable(false);
   }
   
   public void limpiarControles(){
       txtPCodigoProducto.clear();
       cmbCodigoPlato.setValue(null);
       cmbCodigoProducto.setValue(null);
   }
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void menuPrincipal() {
        escenarioPrincipal.menuPrincipal();
    }
}
