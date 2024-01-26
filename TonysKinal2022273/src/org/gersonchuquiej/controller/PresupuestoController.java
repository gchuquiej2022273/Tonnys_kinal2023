package org.gersonchuquiej.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.gersonchuquiej.bean.Empresa;
import org.gersonchuquiej.bean.Presupuesto;
import org.gersonchuquiej.db.Conexion;
import org.gersonchuquiej.main.Principal;
import org.gersonchuquiej.report.GenerarReporte;

public class PresupuestoController implements Initializable{
    
    private Principal escenarioPrincipal;
    private enum operaciones {GUARDAR,ELIMINAR,ACTUALIZAR,NINGUNO};
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Presupuesto> listaPresupuesto;
    private ObservableList<Empresa> listaEmpresa;
    private DatePicker fecha;
    
    @FXML private TextField txtCodigoPresupuesto;  
    @FXML private TextField txtCantidadPresupuesto; 
    
    @FXML private GridPane grpFecha;
    
    @FXML private ComboBox cmbCodigoEmpresa;
    
    @FXML private TableView tblPresupuestos;
    
    @FXML private TableColumn colCodigoPresupuesto;
    @FXML private TableColumn colFechaSolicitud;
    @FXML private TableColumn colCantidadPresupuesto;
    @FXML private TableColumn colCodigoEmpresa;
    
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
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-MM-dd")); //se le da el formato ala Fecha
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/gersonchuquiej/resource/TonnysKinal.css");
        grpFecha.add(fecha, 3, 0);
        cmbCodigoEmpresa.setItems(getEmpresa());
    }
    
    public void cargarDatos(){
        tblPresupuestos.setItems(getPresupuesto());
        colCodigoPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoPresupuesto"));
        colFechaSolicitud.setCellValueFactory(new PropertyValueFactory<Presupuesto, Date>("fechaSolucitud"));
        colCantidadPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto, Double>("cantidadPresupuesto"));
        colCodigoEmpresa.setCellValueFactory(new PropertyValueFactory<Presupuesto, Integer>("codigoEmpresa"));
    }

    
    
    public Empresa buscarEmpresa(int codigoEmpresa){
        Empresa resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_BuscarEmpresa(?)");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empresa(registro.getInt("codigoEmpresa"),
                                    registro.getString("nombreEmpresa"),
                                    registro.getString("direccion"),
                                    registro.getString("telefono"));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<Presupuesto> getPresupuesto(){
        ArrayList<Presupuesto> lista = new ArrayList<Presupuesto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarPresupuesto()");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Presupuesto(resultado.getInt("codigoPresupuesto"),
                                            resultado.getDate("fechaSolucitud"),
                                            resultado.getDouble("cantidadPresupuesto"),
                                            resultado.getInt("codigoEmpresa")));
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return listaPresupuesto = FXCollections.observableArrayList(lista);
    }
    
     public ObservableList<Empresa> getEmpresa() {
        ArrayList<Empresa> lista = new ArrayList<Empresa>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_ListarEmpresas()");
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
        return listaEmpresa =   FXCollections.observableList(lista);
    }
     
    
    public void nuevo(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                    limpiarControler();
                    activarControles();
                    btnNuevo.setText("Guardar");
                    btnEliminar.setText("Cancelar");
                    btnEditar.setDisable(true);
                    btnReporte.setDisable(true);
                    imgNuevo.setImage(new Image("/org/gersonchuquiej/image/Guardar.png"));
                    imgEliminar.setImage(new Image("/org/gersonchuquiej/image/Cancelar.png"));
                    tipoDeOperaciones = operaciones.GUARDAR;
                break;
            case GUARDAR:
                    guardar();
                    limpiarControler();
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
        switch (tipoDeOperaciones) {
            case GUARDAR:
                    limpiarControler();
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
                    if(tblPresupuestos.getSelectionModel().getSelectedItem() !=null){
                        int respuesta = JOptionPane.showConfirmDialog(null, "¿Estas seguro de eliminar?");
                        if(respuesta == JOptionPane.YES_OPTION){
                            try{
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EliminarPresupuesto(?)");
                                procedimiento.setInt(1,((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto());
                                procedimiento.execute();
                                listaPresupuesto.remove(tblPresupuestos.getSelectionModel().getSelectedItem());
                                limpiarControler();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                    }
                break;
                
        }
    }
    
    
   public void seleccionarElemento(){
        txtCodigoPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoPresupuesto()));
        fecha.selectedDateProperty().set(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getFechaSolucitud());
        txtCantidadPresupuesto.setText(String.valueOf(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
        cmbCodigoEmpresa.getSelectionModel().select(buscarEmpresa(((Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
    }
   
    public void Editar(){
        switch (tipoDeOperaciones) {
            case NINGUNO:
                if(tblPresupuestos.getSelectionModel().getSelectedItem() !=null){
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
                    limpiarControler();
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
    
    public void guardar(){
        Presupuesto registro = new Presupuesto();
        registro.setFechaSolucitud(fecha.getSelectedDate());
        registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
        registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_AgregarPresupuesto(?,?,?)");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaSolucitud().getTime()));
            procedimiento.setDouble(2, registro.getCantidadPresupuesto());
            procedimiento.setInt(3, registro.getCodigoEmpresa() );
            procedimiento.execute();
            listaPresupuesto.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    public void reporte(){
        switch (tipoDeOperaciones) {
            case ACTUALIZAR:
                    actualizar();
                    limpiarControler();
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
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("call sp_EditarPresupuesto(?,?,?,?)");
            Presupuesto registro = (Presupuesto)tblPresupuestos.getSelectionModel().getSelectedItem();
            registro.setFechaSolucitud(fecha.getSelectedDate());
            registro.setCantidadPresupuesto(Double.parseDouble(txtCantidadPresupuesto.getText()));
            registro.setCodigoEmpresa(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
            procedimiento.setInt(1, registro.getCodigoPresupuesto());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaSolucitud().getTime()));
            procedimiento.setDouble(3, registro.getCantidadPresupuesto());
            procedimiento.setInt(4, registro.getCodigoEmpresa());
            procedimiento.execute();
            tipoDeOperaciones = operaciones.ACTUALIZAR;
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void limpiarControler(){
        txtCodigoPresupuesto.clear();
        txtCantidadPresupuesto.clear();
        cmbCodigoEmpresa.setValue(null);
        fecha.selectedDateProperty().set(null);
    }
    
    public void activarControles(){
        txtCodigoPresupuesto.setEditable(false);
        txtCantidadPresupuesto.setEditable(true);
        cmbCodigoEmpresa.setDisable(false);
        fecha.setDisable(false);
    }
    
    public void desactivarControles(){
        txtCantidadPresupuesto.setEditable(false);
        txtCodigoPresupuesto.setEditable(false);
        cmbCodigoEmpresa.setDisable(false);
        fecha.setDisable(false);
    }
    
    public void generarReporte(){
        switch (tipoDeOperaciones) {
            case NINGUNO:
                    imprimirReporte();
                break;
            case ACTUALIZAR:
                
                break;
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        int codEmpresa = Integer.valueOf(((Empresa)cmbCodigoEmpresa.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        parametros.put("codEmpresa", codEmpresa);
        GenerarReporte.mostrarReporte("ReportePresupuesto.jasper", "Reporte de Presupuesto", parametros);
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
    
    public void ventanaEmpresa(){
        escenarioPrincipal.ventanaEmpresa();
    }
}
