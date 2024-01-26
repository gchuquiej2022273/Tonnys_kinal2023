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
import org.gersonchuquiej.bean.Plato;
import org.gersonchuquiej.bean.TipoPlato;
import org.gersonchuquiej.db.Conexion;
import org.gersonchuquiej.main.Principal;


    public class PlatoController implements Initializable{
    
    private Principal escenarioPrincipal;
    private enum operaciones {GUARDAR,ELIMINAR,ACTUALIZAR,NINGUNO};
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
        private ObservableList<Plato> listaPlato;
         private ObservableList<TipoPlato> ListaTipoPlato;

    
        @FXML private TextField txtCodigoPlato;
        @FXML private TextField txtCantidad;
        @FXML private TextField txtNombrePlato;
        @FXML private TextField txtDescripcionPlato;
        @FXML private TextField txtPrecioPlato;
        
        @FXML private ComboBox cmbCodigoTipoPlato;
        
        @FXML private TableView tblPlatos;
        
        @FXML private TableColumn colCodigoPlato;
        @FXML private TableColumn colCantidad;
        @FXML private TableColumn colNombrePlato;
        @FXML private TableColumn colDescripcionPlato;
        @FXML private TableColumn colPrecioPlato;
        @FXML private TableColumn colCodigoTipoPlato;
        
        @FXML private Button btnNuevo;
        @FXML private Button btnEditar;
        @FXML private Button btnReporte;
        @FXML private Button btnEliminar;
        
        @FXML private ImageView imgNuevo;
        @FXML private ImageView imgEliminar;
        @FXML private ImageView imgReporte;
        @FXML private ImageView imgEditar;
        
        
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoTipoPlato.setItems(getTipoPlatos());
    }
    
    public void cargarDatos(){
        tblPlatos.setItems(getPlatos());
        colCodigoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, Integer>("codigoPlato"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<TipoPlato, Integer>("cantidad"));
        colNombrePlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, String>("nombrePlato"));
        colDescripcionPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato, String>("descripcionPlato"));
        colPrecioPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato , Double>("precioPlato"));
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato , Integer>("codigoTipoPlato"));
    }
    public ObservableList<Plato> getPlatos(){
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
        switch(tipoDeOperaciones){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/gersonchuquiej/image/Guardar.png"));
                imgEliminar.setImage(new Image("/org/gersonchuquiej/image/Cancelar.png"));
                tipoDeOperaciones = operaciones.GUARDAR;
                cargarDatos();
                break;
            case GUARDAR :
                    guardar();
                    limpiarControles();
                    desactivarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    imgNuevo.setImage(new Image("/org/gersonchuquiej/image/Agregar.png"));
                    imgEliminar.setImage(new Image("/org/gersonchuquiej/image/Eliminar.png"));
                    tipoDeOperaciones = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
           public void eliminar(){
        switch(tipoDeOperaciones){
            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/gersonchuquiej/image/Agregar.png"));
                imgEliminar.setImage(new Image("/org/gersonchuquiej/image/Eliminar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
            default:
                    if(tblPlatos.getSelectionModel().getSelectedItem() != null){
                        int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro de Eliminar el registro?","Eliminar Plato"+
                                                                            "¿vas a eliminar una llave foranea?",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarPlato(?)");
                                procedimiento.setInt(1,((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());
                                procedimiento.execute();
                                listaPlato.remove(tblPlatos.getSelectionModel().getSelectedItem());
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
    
    public void Editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                    if(tblPlatos.getSelectionModel().getSelectedItem() !=null){
                        btnNuevo.setDisable(true);
                        btnEliminar.setDisable(true);
                        btnEditar.setText("Actualizar");
                        btnReporte.setText("Cancelar");
                        imgEditar.setImage(new Image("/org/gersonchuquiej/image/Actualizar.png"));
                        imgReporte.setImage(new Image("/org/gersonchuquiej/image/Cancelar.png"));
                        activarControles();
                        tipoDeOperaciones = operaciones.ACTUALIZAR;
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
                    tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
        
    }
    
    public void reporte(){
        switch (tipoDeOperaciones) {
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
                    tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }
    
    public void actualizar(){
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarPlato(?,?,?,?,?,?)");
            Plato registro = (Plato)tblPlatos.getSelectionModel().getSelectedItem();
            registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
            registro.setNombrePlato(txtNombrePlato.getText());
            registro.setDescripcionPlato(txtDescripcionPlato.getText());
            registro.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
            registro.setCodigoTipoPlato(((TipoPlato)cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
            procedimiento.setInt(1, registro.getCodigoPlato());
            procedimiento.setInt(2, registro.getCantidad());
            procedimiento.setString(3, registro.getNombrePlato());
            procedimiento.setString(4, registro.getDescripcionPlato()); 
            procedimiento.setDouble(5, registro.getPrecioPlato());
            procedimiento.setInt(6, registro.getCodigoTipoPlato());
            procedimiento.execute();
            tipoDeOperaciones = operaciones.ACTUALIZAR;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void guardar(){
        Plato registro = new Plato();
        registro.setCantidad(Integer.parseInt(txtCantidad.getText()));
        registro.setNombrePlato(txtNombrePlato.getText());
        registro.setDescripcionPlato(txtDescripcionPlato.getText());
        registro.setPrecioPlato(Double.parseDouble(txtPrecioPlato.getText()));
        registro.setCodigoTipoPlato(((TipoPlato)cmbCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarPlatos(?,?,?,?,?)");
            
            procedimiento.setInt(1, registro.getCantidad());
            procedimiento.setString(2, registro.getNombrePlato());
            procedimiento.setString(3, registro.getDescripcionPlato()); 
            procedimiento.setDouble(4, registro.getPrecioPlato());
            procedimiento.setInt(5, registro.getCodigoTipoPlato());
            
            procedimiento.execute();
            listaPlato.add(registro);
               
        }catch(Exception e){
            e.printStackTrace();
        }
    }
   
       public TipoPlato buscarPlatos(int codigoTipoPlato){
        TipoPlato resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarTipoPlato(?)");
            procedimiento.setInt(1, codigoTipoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TipoPlato(registro.getInt("codigoTipoPlato"),
                                    registro.getString("descripcionTipoPlato"));
                
            }
        }catch(Exception e){
            e.printStackTrace();
        
        }

        return resultado;
    }
       
       public void seleccionarElemento(){
          txtNombrePlato.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getNombrePlato()));
          txtDescripcionPlato.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getDescripcionPlato()));
          txtPrecioPlato.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getPrecioPlato()));
          txtCodigoPlato.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
          txtCantidad.setText(String.valueOf(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCantidad()));
          cmbCodigoTipoPlato.getSelectionModel().select(buscarPlatos(((Plato)tblPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
      }
    public void desactivarControles(){
        txtCodigoPlato.setEditable(false);
        txtNombrePlato.setEditable(false);
        txtCantidad.setEditable(false);
        txtDescripcionPlato.setEditable(false);
        txtPrecioPlato.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoPlato.setEditable(false);
        txtNombrePlato.setEditable(true);
        txtCantidad.setEditable(true);
        txtDescripcionPlato.setEditable(true);
        txtPrecioPlato.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoPlato.clear();
        txtNombrePlato.clear();
        txtCantidad.clear();
        txtDescripcionPlato.clear();
        txtPrecioPlato.clear();
        cmbCodigoTipoPlato.setValue(null);
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