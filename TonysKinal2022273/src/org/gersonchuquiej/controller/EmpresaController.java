    package org.gersonchuquiej.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import org.gersonchuquiej.bean.Empresa;
import org.gersonchuquiej.db.Conexion;
import org.gersonchuquiej.main.Principal;
import org.gersonchuquiej.report.GenerarReporte;

public class EmpresaController implements Initializable{
    
    private enum operaciones {NUEVO,GUARDAR,ELIMINAR,ACTUALIZAR, CANCELAR , NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Empresa> ListaEmpresa;

    public EmpresaController() {
    }
    
    @FXML private TextField txtCodigoEmpresa;
    @FXML private TextField txtNombreEmpresa;
    @FXML private TextField txtDireccionEmpresa;
    @FXML private TextField txtTelefonoEmpresa;
    
    @FXML private TableView tblEmpresas;
    
    @FXML private TableColumn colCodigoEmpresa;
    @FXML private TableColumn colNombreEmpresa;
    @FXML private TableColumn colDireccionEmpresa;
    @FXML private TableColumn colTelefonoEmpresa;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgReporte;
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblEmpresas.setItems(getEmpresa());
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, Integer>("codigoEmpresa"));
        colNombreEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("nombreEmpresa"));
        colDireccionEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("direccion"));
        colTelefonoEmpresa.setCellValueFactory(new PropertyValueFactory<Empresa, String>("telefono"));
    }
    
    public ObservableList<Empresa> getEmpresa() {
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpresas");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empresa(resultado.getInt("codigoEmpresa"),    
                            resultado.getString("nombreEmpresa"),
                            resultado.getString("direccion"),
                            resultado.getString("telefono")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return ListaEmpresa =   FXCollections.observableList(lista);
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
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
                    if(tblEmpresas.getSelectionModel().getSelectedItem() != null){
                        int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro de Eliminar el registro?","Eliminar Empresa"+
                                                                            "¿vas a eliminar una llave foranea?",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarEmpresas(?)");
                                procedimiento.setInt(1,((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                                procedimiento.execute();
                                ListaEmpresa.remove(tblEmpresas.getSelectionModel().getSelectedItem());
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
        switch(tipoDeOperacion){
            case NINGUNO:
                    if(tblEmpresas.getSelectionModel().getSelectedItem() !=null){
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
        }
    }
    
    public void actualizar(){
        try {
            PreparedStatement procedimeinto = Conexion.getInstance().getConexion().prepareCall("call sp_EditarEmpresa(?,?,?,?)");
            Empresa registro = (Empresa)tblEmpresas.getSelectionModel().getSelectedItem();
            registro.setNombreEmpresa(txtNombreEmpresa.getText());
            registro.setDireccion(txtDireccionEmpresa.getText());
            registro.setTelefono(txtTelefonoEmpresa.getText());
            procedimeinto.setInt(1, registro.getCodigoEmpresa());
            procedimeinto.setString(2, registro.getNombreEmpresa());
            procedimeinto.setString(3, registro.getDireccion());
            procedimeinto.setString(4, registro.getTelefono());
            procedimeinto.execute();
            tipoDeOperacion = operaciones.ACTUALIZAR;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void guardar(){
        Empresa registro = new Empresa();
//        registro.setCodigoEmpresa(Integer.parseInt(txtCodigoEmpresa.getText()));
            registro.setNombreEmpresa(txtNombreEmpresa.getText());
            registro.setDireccion(txtDireccionEmpresa.getText());
            registro.setTelefono(txtTelefonoEmpresa.getText());
            try{
                 PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarEmpresa (?,?,?)");
                 procedimiento.setString(1,registro.getNombreEmpresa());
                 procedimiento.setString(2,registro.getDireccion());
                 procedimiento.setString(3,registro.getTelefono());
                 procedimiento.execute();
                 ListaEmpresa.add(registro);
            }catch(Exception e){
                e.printStackTrace();
            }
    }
    

    public void seleccionarElemento(){
        txtCodigoEmpresa.setText(String.valueOf(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
        txtNombreEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getNombreEmpresa());
        txtDireccionEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getDireccion());
        txtTelefonoEmpresa.setText(((Empresa)tblEmpresas.getSelectionModel().getSelectedItem()).getTelefono());
    }
    
    public void GenerarReporte(){
        switch (tipoDeOperacion) {
            case NINGUNO:
                    imprimirReporte();
                break;
            case ACTUALIZAR:
                    //comandos de cancelar
                break;
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("imgInferios", EmpresaController.class.getResource("/org/gersonchuquiej/image/Empresa.png"));
        parametros.put("codEmpresa", null);
        GenerarReporte.mostrarReporte("ReporteEmpresas.jasper", "Reporte de Empresas", parametros);
    }
    
    public void imprimirReporteGeneral(){
     Map parametros = new HashMap();
     parametros.put("codReporteGeneral", null);
     GenerarReporte.mostrarReporte("ReporteGeneral.jasper", "Reporte General", parametros);
    }
    
    public void desactivarControles(){
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(false);
        txtDireccionEmpresa.setEditable(false);
        txtTelefonoEmpresa.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoEmpresa.setEditable(false);
        txtNombreEmpresa.setEditable(true);
        txtDireccionEmpresa.setEditable(true);
        txtTelefonoEmpresa.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoEmpresa.clear();
        txtNombreEmpresa.clear();
        txtDireccionEmpresa.clear();
        txtTelefonoEmpresa.clear();
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaPresupuesto(){
        escenarioPrincipal.ventanaPresupuesto();
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}